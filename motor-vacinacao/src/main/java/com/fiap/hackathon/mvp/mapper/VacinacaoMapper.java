package com.fiap.hackathon.mvp.mapper;

import com.fiap.hackathon.mvp.dto.EsquemaVacinacaoDTO;
import com.fiap.hackathon.mvp.dto.HistoricoVacinalDTO;
import com.fiap.hackathon.mvp.persistence.entity.AplicacaoVacina;
import com.fiap.hackathon.mvp.persistence.entity.EsquemaVacinacao;
import org.springframework.stereotype.Component;

@Component
public class VacinacaoMapper {

    public EsquemaVacinacaoDTO toDTO(EsquemaVacinacao esquema) {

        return new EsquemaVacinacaoDTO(
                esquema.getId(),
                esquema.getVacina().getId(),
                esquema.getVacina().getNome(),
                esquema.getNumeroDose(),
                esquema.getTipoDose(),

                esquema.getIdadeMinimaValor(),
                esquema.getIdadeMinimaUnidade(),

                esquema.getIdadeRecomendadaValor(),
                esquema.getIdadeRecomendadaUnidade(),

                esquema.getIdadeMaximaValor(),
                esquema.getIdadeMaximaUnidade(),

                esquema.getIntervaloMinimoValor(),
                esquema.getIntervaloMinimoUnidade()
        );
    }

    public HistoricoVacinalDTO toDTO(AplicacaoVacina aplicacao) {

        return new HistoricoVacinalDTO(
                aplicacao.getNumeroSus(),
                aplicacao.getEsquemaVacinacao().getId(),
                aplicacao.getEsquemaVacinacao().getVacina().getId(),
                aplicacao.getEsquemaVacinacao().getVacina().getNome(),
                aplicacao.getEsquemaVacinacao().getNumeroDose(),
                aplicacao.getEsquemaVacinacao().getTipoDose(),
                aplicacao.getDataAplicacao()
        );
    }
}
