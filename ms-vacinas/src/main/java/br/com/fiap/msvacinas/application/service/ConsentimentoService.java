package br.com.fiap.msvacinas.application.service;



import br.com.fiap.msvacinas.application.port.in.SalvarConsentimentoUseCase;
import br.com.fiap.msvacinas.application.port.out.ConsentimentoPersistencePort;
import br.com.fiap.msvacinas.domain.model.ConsentimentoNotificacao;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

public class ConsentimentoService implements SalvarConsentimentoUseCase {

    private final ConsentimentoPersistencePort consentimentoPersistencePort;

    public ConsentimentoService(ConsentimentoPersistencePort consentimentoPersistencePort) {
        this.consentimentoPersistencePort = consentimentoPersistencePort;
    }

    @Override
    @Transactional
    public ConsentimentoNotificacao registrar(UUID calendarioVacinalId, boolean aceitaNotificacao) {
        LocalDateTime dataAtualizacao = LocalDateTime.now();

        return consentimentoPersistencePort.buscarPorCalendarioVacinalId(calendarioVacinalId)
                .map(consentimentoExistente -> new ConsentimentoNotificacao(
                        consentimentoExistente.id(),
                        calendarioVacinalId,
                        aceitaNotificacao,
                        dataAtualizacao
                ))
                .map(consentimentoPersistencePort::salvar)
                .orElseGet(() -> consentimentoPersistencePort.salvar(new ConsentimentoNotificacao(
                        UUID.randomUUID(),
                        calendarioVacinalId,
                        aceitaNotificacao,
                        dataAtualizacao
                )));
    }
}