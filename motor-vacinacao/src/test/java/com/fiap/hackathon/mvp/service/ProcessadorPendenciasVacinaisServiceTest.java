package com.fiap.hackathon.mvp.service;

import com.fiap.hackathon.mvp.dto.EsquemaVacinacaoDTO;
import com.fiap.hackathon.mvp.dto.NotificacaoVacinaPendenteDTO;
import com.fiap.hackathon.mvp.dto.PendenciaVacinalDTO;
import com.fiap.hackathon.mvp.dto.PessoaElegivelDTO;
import com.fiap.hackathon.mvp.mapper.VacinacaoMapper;
import com.fiap.hackathon.mvp.persistence.entity.EsquemaVacinacao;
import com.fiap.hackathon.mvp.persistence.repository.AplicacaoVacinaRepository;
import com.fiap.hackathon.mvp.persistence.repository.EsquemaVacinacaoRepository;
import com.fiap.hackathon.mvp.persistence.repository.PessoaRepository;
import com.fiap.hackathon.mvp.producer.NotificacaoVacinaProducer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class ProcessadorPendenciasVacinaisServiceTest {

    private PessoaRepository pessoaRepository;
    private EsquemaVacinacaoRepository esquemaVacinacaoRepository;
    private AplicacaoVacinaRepository aplicacaoVacinaRepository;
    private VacinacaoMapper vacinacaoMapper;
    private MotorVacinacaoService motorVacinacaoService;
    private NotificacaoVacinaProducer notificacaoVacinaProducer;

    private ProcessadorPendenciasVacinaisService service;

    @BeforeEach
    void setUp() {

        pessoaRepository =
                mock(PessoaRepository.class);

        esquemaVacinacaoRepository =
                mock(EsquemaVacinacaoRepository.class);

        aplicacaoVacinaRepository =
                mock(AplicacaoVacinaRepository.class);

        vacinacaoMapper =
                mock(VacinacaoMapper.class);

        motorVacinacaoService =
                mock(MotorVacinacaoService.class);

        notificacaoVacinaProducer =
                mock(NotificacaoVacinaProducer.class);

        service =
                new ProcessadorPendenciasVacinaisService(
                        pessoaRepository,
                        esquemaVacinacaoRepository,
                        aplicacaoVacinaRepository,
                        vacinacaoMapper,
                        motorVacinacaoService,
                        notificacaoVacinaProducer
                );
    }

    @Test
    void deveProcessarMilPessoasEmQuatroBlocosDeDuzentasECinquenta() {

        List<PessoaElegivelDTO> pessoas =
                criarPessoas(1000);

        EsquemaVacinacao esquemaEntity =
                new EsquemaVacinacao();

        EsquemaVacinacaoDTO esquemaDTO =
                esquema();

        when(
                esquemaVacinacaoRepository
                        .findEsquemasVigentes(any(LocalDate.class))
        ).thenReturn(
                List.of(esquemaEntity)
        );

        when(
                vacinacaoMapper.toDTO(esquemaEntity)
        ).thenReturn(esquemaDTO);

        when(
                pessoaRepository.buscarPessoasElegiveis(
                        anyLong(),
                        any(Pageable.class)
                )
        )
                .thenReturn(pessoas)
                .thenReturn(Collections.emptyList());

        when(
                aplicacaoVacinaRepository.findHistoricoPorPessoas(
                        anyList()
                )
        ).thenReturn(Collections.emptyList());

        when(
                motorVacinacaoService.calcularPendencias(
                        any(PessoaElegivelDTO.class),
                        anyList(),
                        anyList(),
                        any(LocalDate.class)
                )
        ).thenReturn(Collections.emptyList());

        service.processar();

        // 1.000 pessoas foram obtidas em uma página.
        // A segunda chamada confirma que não há mais pessoas.
        verify(
                pessoaRepository,
                times(2)
        ).buscarPessoasElegiveis(
                anyLong(),
                any(Pageable.class)
        );

        // 1.000 pessoas / 250 = 4 consultas.
        ArgumentCaptor<List<Long>> captor =
                ArgumentCaptor.forClass(List.class);

        verify(
                aplicacaoVacinaRepository,
                times(4)
        ).findHistoricoPorPessoas(
                captor.capture()
        );

        List<List<Long>> blocos =
                captor.getAllValues();

        assertEquals(4, blocos.size());

        assertEquals(250, blocos.get(0).size());
        assertEquals(250, blocos.get(1).size());
        assertEquals(250, blocos.get(2).size());
        assertEquals(250, blocos.get(3).size());

        List<Long> todosOsSus =
                blocos.stream()
                        .flatMap(List::stream)
                        .toList();

        assertEquals(1000, todosOsSus.size());

        assertEquals(
                1000,
                todosOsSus.stream()
                        .distinct()
                        .count()
        );

        // Cada pessoa passou pelo Motor.
        verify(
                motorVacinacaoService,
                times(1000)
        ).calcularPendencias(
                any(PessoaElegivelDTO.class),
                anyList(),
                anyList(),
                any(LocalDate.class)
        );

        // Nenhuma pendência foi gerada.
        verifyNoInteractions(
                notificacaoVacinaProducer
        );
    }

    private List<PessoaElegivelDTO> criarPessoas(
            int quantidade
    ) {
        List<PessoaElegivelDTO> pessoas =
                new ArrayList<>();

        for (long i = 1; i <= quantidade; i++) {

            pessoas.add(
                    new PessoaElegivelDTO(
                            i,
                            LocalDate.of(2020, 1, 1)
                    )
            );
        }

        return pessoas;
    }

    private EsquemaVacinacaoDTO esquema() {

        return new EsquemaVacinacaoDTO(
                1L,
                10L,
                "Pentavalente",
                1,
                "ROTINA",
                2,
                "MESES",
                null,
                null,
                null,
                null,
                null,
                null
        );
    }

    @Test
    void devePublicarUmaNotificacaoPorPessoaComTodasAsVacinasPendentes() {

        List<PessoaElegivelDTO> pessoas =
                criarPessoas(4);

        EsquemaVacinacao esquemaEntity =
                new EsquemaVacinacao();

        EsquemaVacinacaoDTO esquemaDTO =
                esquema();

        when(
                esquemaVacinacaoRepository
                        .findEsquemasVigentes(any(LocalDate.class))
        ).thenReturn(
                List.of(esquemaEntity)
        );

        when(
                vacinacaoMapper.toDTO(esquemaEntity)
        ).thenReturn(esquemaDTO);

        when(
                pessoaRepository.buscarPessoasElegiveis(
                        anyLong(),
                        any(Pageable.class)
                )
        )
                .thenReturn(pessoas)
                .thenReturn(Collections.emptyList());

        when(
                aplicacaoVacinaRepository.findHistoricoPorPessoas(
                        anyList()
                )
        ).thenReturn(Collections.emptyList());

        PendenciaVacinalDTO pentavalente =
                new PendenciaVacinalDTO(
                        1L,
                        10L,
                        "Pentavalente",
                        1,
                        "ROTINA"
                );

        PendenciaVacinalDTO pneumo =
                new PendenciaVacinalDTO(
                        2L,
                        20L,
                        "Pneumocócica 10-valente",
                        1,
                        "ROTINA"
                );

        PendenciaVacinalDTO vip =
                new PendenciaVacinalDTO(
                        3L,
                        30L,
                        "VIP",
                        1,
                        "ROTINA"
                );

        /*
         * Pessoa 1:
         * duas vacinas pendentes.
         */
        when(
                motorVacinacaoService.calcularPendencias(
                        eq(pessoas.get(0)),
                        anyList(),
                        anyList(),
                        any(LocalDate.class)
                )
        ).thenReturn(
                List.of(
                        pentavalente,
                        pneumo
                )
        );

        /*
         * Pessoa 2:
         * uma vacina pendente.
         */
        when(
                motorVacinacaoService.calcularPendencias(
                        eq(pessoas.get(1)),
                        anyList(),
                        anyList(),
                        any(LocalDate.class)
                )
        ).thenReturn(
                List.of(vip)
        );

        /*
         * Pessoa 3:
         * nenhuma pendência.
         */
        when(
                motorVacinacaoService.calcularPendencias(
                        eq(pessoas.get(2)),
                        anyList(),
                        anyList(),
                        any(LocalDate.class)
                )
        ).thenReturn(
                Collections.emptyList()
        );

        /*
         * Pessoa 4:
         * duas vacinas pendentes.
         */
        when(
                motorVacinacaoService.calcularPendencias(
                        eq(pessoas.get(3)),
                        anyList(),
                        anyList(),
                        any(LocalDate.class)
                )
        ).thenReturn(
                List.of(
                        pentavalente,
                        vip
                )
        );

        service.processar();

        ArgumentCaptor<NotificacaoVacinaPendenteDTO> captor =
                ArgumentCaptor.forClass(
                        NotificacaoVacinaPendenteDTO.class
                );

        verify(
                notificacaoVacinaProducer,
                times(3)
        ).publicar(
                captor.capture()
        );

        List<NotificacaoVacinaPendenteDTO> notificacoes =
                captor.getAllValues();

        assertEquals(3, notificacoes.size());

        /*
         * Pessoa 1
         */
        NotificacaoVacinaPendenteDTO notificacaoPessoa1 =
                notificacoes.stream()
                        .filter(n ->
                                n.numeroSus()
                                        .equals(
                                                pessoas.get(0).numeroSus()
                                        )
                        )
                        .findFirst()
                        .orElseThrow();

        assertEquals(
                2,
                notificacaoPessoa1.vacinasPendentes().size()
        );

        assertTrue(
                notificacaoPessoa1.vacinasPendentes()
                        .stream()
                        .anyMatch(p ->
                                p.vacinaId().equals(10L)
                        )
        );

        assertTrue(
                notificacaoPessoa1.vacinasPendentes()
                        .stream()
                        .anyMatch(p ->
                                p.vacinaId().equals(20L)
                        )
        );

        /*
         * Pessoa 2
         */
        NotificacaoVacinaPendenteDTO notificacaoPessoa2 =
                notificacoes.stream()
                        .filter(n ->
                                n.numeroSus()
                                        .equals(
                                                pessoas.get(1).numeroSus()
                                        )
                        )
                        .findFirst()
                        .orElseThrow();

        assertEquals(
                1,
                notificacaoPessoa2.vacinasPendentes().size()
        );

        assertEquals(
                30L,
                notificacaoPessoa2
                        .vacinasPendentes()
                        .get(0)
                        .vacinaId()
        );

        /*
         * Pessoa 3 não deve receber notificação.
         */
        assertTrue(
                notificacoes.stream()
                        .noneMatch(n ->
                                n.numeroSus()
                                        .equals(
                                                pessoas.get(2).numeroSus()
                                        )
                        )
        );

        /*
         * Pessoa 4
         */
        NotificacaoVacinaPendenteDTO notificacaoPessoa4 =
                notificacoes.stream()
                        .filter(n ->
                                n.numeroSus()
                                        .equals(
                                                pessoas.get(3).numeroSus()
                                        )
                        )
                        .findFirst()
                        .orElseThrow();

        assertEquals(
                2,
                notificacaoPessoa4.vacinasPendentes().size()
        );

        assertTrue(
                notificacaoPessoa4.vacinasPendentes()
                        .stream()
                        .anyMatch(p ->
                                p.vacinaId().equals(10L)
                        )
        );

        assertTrue(
                notificacaoPessoa4.vacinasPendentes()
                        .stream()
                        .anyMatch(p ->
                                p.vacinaId().equals(30L)
                        )
        );
    }
}