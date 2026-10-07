package com.fiap.hackathon.mvp.dto;

public record PendenciaVacinalDTO(
        Long esquemaVacinacaoId,
        Long vacinaId,
        String vacina,
        Integer numeroDose,
        String tipoDose
) {
}