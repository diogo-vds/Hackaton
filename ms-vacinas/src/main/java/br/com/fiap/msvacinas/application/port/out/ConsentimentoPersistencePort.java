package br.com.fiap.msvacinas.application.port.out;

import br.com.fiap.msvacinas.domain.model.ConsentimentoNotificacao;

import java.util.Optional;
import java.util.UUID;

public interface ConsentimentoPersistencePort {

    Optional<ConsentimentoNotificacao> buscarPorCalendarioVacinalId(UUID calendarioVacinalId);

    ConsentimentoNotificacao salvar(ConsentimentoNotificacao consentimento);
}
