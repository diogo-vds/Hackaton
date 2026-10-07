package com.fiap.hackathon.mvp.service;

import com.fiap.hackathon.mvp.dto.*;
import com.fiap.hackathon.mvp.mapper.VacinacaoMapper;
import com.fiap.hackathon.mvp.persistence.entity.AplicacaoVacina;
import com.fiap.hackathon.mvp.persistence.entity.EsquemaVacinacao;
import com.fiap.hackathon.mvp.persistence.repository.AplicacaoVacinaRepository;
import com.fiap.hackathon.mvp.persistence.repository.EsquemaVacinacaoRepository;
import com.fiap.hackathon.mvp.persistence.repository.PessoaRepository;
import com.fiap.hackathon.mvp.producer.NotificacaoVacinaProducer;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class ProcessadorPendenciasVacinaisService {

    private static final int TAMANHO_PAGINA = 1000;
    private static final int TAMANHO_BLOCO = 250;
    private static final int NUMERO_THREADS = 4;

    private final PessoaRepository pessoaRepository;
    private final EsquemaVacinacaoRepository esquemaVacinacaoRepository;
    private final AplicacaoVacinaRepository aplicacaoVacinaRepository;
    private final VacinacaoMapper vacinacaoMapper;
    private final MotorVacinacaoService motorVacinacaoService;
    private final NotificacaoVacinaProducer notificacaoVacinaProducer;

    public ProcessadorPendenciasVacinaisService(
            PessoaRepository pessoaRepository,
            EsquemaVacinacaoRepository esquemaVacinacaoRepository,
            AplicacaoVacinaRepository aplicacaoVacinaRepository,
            VacinacaoMapper vacinacaoMapper,
            MotorVacinacaoService motorVacinacaoService,
            NotificacaoVacinaProducer notificacaoVacinaProducer
    ) {
        this.pessoaRepository = pessoaRepository;
        this.esquemaVacinacaoRepository = esquemaVacinacaoRepository;
        this.aplicacaoVacinaRepository = aplicacaoVacinaRepository;
        this.vacinacaoMapper = vacinacaoMapper;
        this.motorVacinacaoService = motorVacinacaoService;
        this.notificacaoVacinaProducer = notificacaoVacinaProducer;
    }

    public void processar() {

        LocalDate dataReferencia = LocalDate.now();

        List<EsquemaVacinacaoDTO> esquemas =
                carregarEsquemas(dataReferencia);

        if (esquemas.isEmpty()) {
            return;
        }

        ExecutorService executor =
                Executors.newFixedThreadPool(NUMERO_THREADS);

        try {
            processarPessoas(
                    esquemas,
                    dataReferencia,
                    executor
            );
        } finally {
            executor.shutdown();
        }
    }

    private List<EsquemaVacinacaoDTO> carregarEsquemas(
            LocalDate dataReferencia
    ) {

        List<EsquemaVacinacao> entidades =
                esquemaVacinacaoRepository.findEsquemasVigentes(dataReferencia);

        return entidades.stream()
                .map(vacinacaoMapper::toDTO)
                .toList();
    }

    private void processarPessoas(
            List<EsquemaVacinacaoDTO> esquemas,
            LocalDate dataReferencia,
            ExecutorService executor
    ) {
        Long ultimoNumeroSus = 0L;

        while (true) {

            List<PessoaElegivelDTO> pessoas =
                    pessoaRepository.buscarPessoasElegiveis(
                            ultimoNumeroSus,
                            PageRequest.of(0, TAMANHO_PAGINA)
                    );

            if (pessoas.isEmpty()) {
                break;
            }

            processarPagina(
                    pessoas,
                    esquemas,
                    dataReferencia,
                    executor
            );

            ultimoNumeroSus =
                    pessoas.get(pessoas.size() - 1)
                            .numeroSus();

            if (pessoas.size() < TAMANHO_PAGINA) {
                break;
            }
        }
    }

    private void processarPagina(
            List<PessoaElegivelDTO> pessoas,
            List<EsquemaVacinacaoDTO> esquemas,
            LocalDate dataReferencia,
            ExecutorService executor
    ) {
        List<List<PessoaElegivelDTO>> blocos =
                dividirEmBlocos(
                        pessoas,
                        TAMANHO_BLOCO
                );

        List<CompletableFuture<Void>> tarefas =
                new ArrayList<>();

        for (List<PessoaElegivelDTO> bloco : blocos) {

            CompletableFuture<Void> tarefa =
                    CompletableFuture.runAsync(
                            () -> processarBloco(
                                    bloco,
                                    esquemas,
                                    dataReferencia
                            ),
                            executor
                    );

            tarefas.add(tarefa);
        }

        aguardarTarefas(tarefas);
    }

    private void processarBloco(
            List<PessoaElegivelDTO> pessoas,
            List<EsquemaVacinacaoDTO> esquemas,
            LocalDate dataReferencia
    ) {
        List<Long> numerosSus =
                pessoas.stream()
                        .map(PessoaElegivelDTO::numeroSus)
                        .toList();

        List<AplicacaoVacina> aplicacoes =
                aplicacaoVacinaRepository
                        .findHistoricoPorPessoas(numerosSus);

        List<HistoricoVacinalDTO> historicos =
                aplicacoes.stream()
                        .map(vacinacaoMapper::toDTO)
                        .toList();

        Map<Long, List<HistoricoVacinalDTO>>
                historicoPorPessoa =
                agruparHistoricoPorPessoa(historicos);

        for (PessoaElegivelDTO pessoa : pessoas) {

            List<HistoricoVacinalDTO> historico =
                    historicoPorPessoa.getOrDefault(
                            pessoa.numeroSus(),
                            Collections.emptyList()
                    );

            List<PendenciaVacinalDTO> pendencias =
                    motorVacinacaoService.calcularPendencias(
                            pessoa,
                            historico,
                            esquemas,
                            dataReferencia
                    );

            if (pendencias.isEmpty()) {
                continue;
            }

            NotificacaoVacinaPendenteDTO notificacao =
                    new NotificacaoVacinaPendenteDTO(
                            pessoa.numeroSus(),
                            pendencias
                    );

            notificacaoVacinaProducer.publicar(
                    notificacao
            );
        }
    }

    private Map<Long, List<HistoricoVacinalDTO>> agruparHistoricoPorPessoa(
            List<HistoricoVacinalDTO> historicos
    ) {

        Map<Long, List<HistoricoVacinalDTO>> resultado = new HashMap<>();

        for (HistoricoVacinalDTO historico : historicos) {

            resultado
                    .computeIfAbsent(
                            historico.numeroSus(),
                            chave -> new ArrayList<>()
                    )
                    .add(historico);
        }

        return resultado;
    }

    private List<List<PessoaElegivelDTO>> dividirEmBlocos(
            List<PessoaElegivelDTO> pessoas,
            int tamanhoBloco
    ) {

        List<List<PessoaElegivelDTO>> blocos = new ArrayList<>();

        for (int inicio = 0;
             inicio < pessoas.size();
             inicio += tamanhoBloco) {

            int fim = Math.min(
                    inicio + tamanhoBloco,
                    pessoas.size()
            );

            blocos.add(
                    pessoas.subList(inicio, fim)
            );
        }

        return blocos;
    }

    private void aguardarTarefas(
            List<CompletableFuture<Void>> tarefas
    ) {

        CompletableFuture<Void> todas =
                CompletableFuture.allOf(
                        tarefas.toArray(new CompletableFuture[0])
                );

        try {

            todas.get();

        } catch (InterruptedException e) {

            Thread.currentThread().interrupt();

            throw new IllegalStateException(
                    "Processamento das pendências vacinais foi interrompido",
                    e
            );

        } catch (ExecutionException e) {

            throw new IllegalStateException(
                    "Erro durante o processamento das pendências vacinais",
                    e.getCause()
            );
        }
    }
}