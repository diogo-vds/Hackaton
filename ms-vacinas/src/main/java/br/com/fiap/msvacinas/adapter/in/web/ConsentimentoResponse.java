package br.com.fiap.msvacinas.adapter.in.web;

import br.com.fiap.msvacinas.domain.model.ConsentimentoNotificacao;

import java.time.LocalDateTime;
import java.util.UUID;

public record ConsentimentoResponse(
        UUID id,
        UUID calendarioVacinalId,
        boolean aceitaNotificacao,
        LocalDateTime dataAtualizacao
) {
    public static ConsentimentoResponse from(ConsentimentoNotificacao consentimento) {
        return new ConsentimentoResponse(
                consentimento.id(),
                consentimento.calendarioVacinalId(),
                consentimento.aceitaNotificacao(),
                consentimento.dataAtualizacao()
        );
    }
}