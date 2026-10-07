package com.fiap.hackathon.mvp.dto;

import java.time.LocalDateTime;

public record HistoricoVacinalDTO(
        Long numeroSus,
        Long esquemaVacinacaoId,
        Long vacinaId,
        String vacina,
        Integer numeroDose,
        String tipoDose,
        LocalDateTime dataAplicacao
) {
}