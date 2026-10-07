package com.fiap.hackathon.mvp.dto;

import java.util.List;

public record NotificacaoVacinaPendenteDTO(
        Long numeroSus,
        List<PendenciaVacinalDTO> vacinasPendentes
) {
}