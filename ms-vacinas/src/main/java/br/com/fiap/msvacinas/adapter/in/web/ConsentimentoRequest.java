package br.com.fiap.msvacinas.adapter.in.web;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ConsentimentoRequest(
        @NotNull(message = "O campo calendarioVacinalId é obrigatório")
        UUID calendarioVacinalId,

        @NotNull(message = "O campo aceitaNotificacao é obrigatório")
        Boolean aceitaNotificacao
) {
}