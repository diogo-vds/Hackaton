package br.com.fiap.msvacinas.application.port.in;

import br.com.fiap.msvacinas.domain.model.ConsentimentoNotificacao;

import java.util.UUID;

public interface SalvarConsentimentoUseCase {

    ConsentimentoNotificacao registrar(UUID calendarioVacinalId, boolean aceitaNotificacao);
}