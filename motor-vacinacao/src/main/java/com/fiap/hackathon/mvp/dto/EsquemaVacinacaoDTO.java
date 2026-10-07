package com.fiap.hackathon.mvp.dto;

public record EsquemaVacinacaoDTO(
        Long id,
        Long vacinaId,
        String vacina,
        Integer numeroDose,
        String tipoDose,

        Integer idadeMinimaValor,
        String idadeMinimaUnidade,

        Integer idadeRecomendadaValor,
        String idadeRecomendadaUnidade,

        Integer idadeMaximaValor,
        String idadeMaximaUnidade,

        Integer intervaloMinimoValor,
        String intervaloMinimoUnidade
) {
}
