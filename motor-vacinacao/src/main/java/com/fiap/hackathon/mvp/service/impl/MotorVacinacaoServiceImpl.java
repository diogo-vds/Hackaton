package com.fiap.hackathon.mvp.service.impl;

import com.fiap.hackathon.mvp.dto.EsquemaVacinacaoDTO;
import com.fiap.hackathon.mvp.dto.HistoricoVacinalDTO;
import com.fiap.hackathon.mvp.dto.PendenciaVacinalDTO;
import com.fiap.hackathon.mvp.dto.PessoaElegivelDTO;
import com.fiap.hackathon.mvp.service.MotorVacinacaoService;
import com.fiap.hackathon.mvp.service.RegraIdadeService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class MotorVacinacaoServiceImpl
        implements MotorVacinacaoService {

    private final RegraIdadeService regraIdadeService;

    public MotorVacinacaoServiceImpl(
            RegraIdadeService regraIdadeService
    ) {
        this.regraIdadeService = regraIdadeService;
    }

    @Override
    public List<PendenciaVacinalDTO> calcularPendencias(
            PessoaElegivelDTO pessoa,
            List<HistoricoVacinalDTO> historico,
            List<EsquemaVacinacaoDTO> esquemas,
            LocalDate dataReferencia
    ) {
        List<PendenciaVacinalDTO> pendencias =
                new ArrayList<>();

        for (EsquemaVacinacaoDTO esquema : esquemas) {

            if (doseJaAplicada(esquema, historico)) {
                continue;
            }

            if (!idadePermiteAplicacao(
                    pessoa,
                    esquema,
                    dataReferencia
            )) {
                continue;
            }

            if (!intervaloMinimoRespeitado(
                    esquema,
                    historico,
                    dataReferencia
            )) {
                continue;
            }

            pendencias.add(
                    criarPendencia(esquema)
            );
        }

        return pendencias;
    }

    private boolean doseJaAplicada(
            EsquemaVacinacaoDTO esquema,
            List<HistoricoVacinalDTO> historico
    ) {

        return historico.stream()
                .anyMatch(aplicacao ->
                        aplicacao.esquemaVacinacaoId()
                                .equals(esquema.id())
                );
    }

    private boolean idadePermiteAplicacao(
            PessoaElegivelDTO pessoa,
            EsquemaVacinacaoDTO esquema,
            LocalDate dataReferencia
    ) {

        return regraIdadeService.estaDentroDaJanela(
                pessoa.dataNascimento(),
                dataReferencia,
                esquema.idadeMinimaValor(),
                esquema.idadeMinimaUnidade(),
                esquema.idadeMaximaValor(),
                esquema.idadeMaximaUnidade()
        );
    }

    private boolean intervaloMinimoRespeitado(
            EsquemaVacinacaoDTO esquema,
            List<HistoricoVacinalDTO> historico,
            LocalDate dataReferencia
    ) {
        if (esquema.intervaloMinimoValor() == null) {
            return true;
        }

        HistoricoVacinalDTO doseAnterior =
                buscarDoseAnterior(esquema, historico);

        if (doseAnterior == null) {
            return true;
        }

        return regraIdadeService.respeitaIntervaloMinimo(
                doseAnterior.dataAplicacao().toLocalDate(),
                dataReferencia,
                esquema.intervaloMinimoValor(),
                esquema.intervaloMinimoUnidade()
        );
    }

    private HistoricoVacinalDTO buscarDoseAnterior(
            EsquemaVacinacaoDTO esquema,
            List<HistoricoVacinalDTO> historico
    ) {
        int doseAnterior = esquema.numeroDose() - 1;

        if (doseAnterior <= 0) {
            return null;
        }

        return historico.stream()
                .filter(aplicacao ->
                        aplicacao.vacinaId()
                                .equals(esquema.vacinaId())
                )
                .filter(aplicacao ->
                        aplicacao.numeroDose() == doseAnterior
                )
                .max(
                        Comparator.comparing(
                                HistoricoVacinalDTO::dataAplicacao
                        )
                )
                .orElse(null);
    }

    private HistoricoVacinalDTO buscarUltimaAplicacaoDaVacina(
            EsquemaVacinacaoDTO esquema,
            List<HistoricoVacinalDTO> historico
    ) {

        return historico.stream()
                .filter(aplicacao ->
                        aplicacao.vacinaId()
                                .equals(esquema.vacinaId())
                )
                .max(
                        Comparator.comparing(
                                HistoricoVacinalDTO::dataAplicacao
                        )
                )
                .orElse(null);
    }

    private PendenciaVacinalDTO criarPendencia(
            EsquemaVacinacaoDTO esquema
    ) {

        return new PendenciaVacinalDTO(
                esquema.id(),
                esquema.vacinaId(),
                esquema.vacina(),
                esquema.numeroDose(),
                esquema.tipoDose()
        );
    }
}