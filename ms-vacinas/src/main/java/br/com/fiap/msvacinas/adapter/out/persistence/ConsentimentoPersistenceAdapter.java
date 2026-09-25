package br.com.fiap.msvacinas.adapter.out.persistence;

import br.com.fiap.msvacinas.application.port.out.ConsentimentoPersistencePort;
import br.com.fiap.msvacinas.domain.model.ConsentimentoNotificacao;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class ConsentimentoPersistenceAdapter implements ConsentimentoPersistencePort {

    private final SpringDataConsentimentoRepository repository;

    public ConsentimentoPersistenceAdapter(SpringDataConsentimentoRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<ConsentimentoNotificacao> buscarPorCalendarioVacinalId(UUID calendarioVacinalId) {
        return repository.findByCalendarioVacinalId(calendarioVacinalId)
                .map(this::toDomain);
    }

    @Override
    public ConsentimentoNotificacao salvar(ConsentimentoNotificacao consentimento) {
        ConsentimentoJpaEntity entity = new ConsentimentoJpaEntity(
                consentimento.id(),
                consentimento.calendarioVacinalId(),
                consentimento.aceitaNotificacao(),
                consentimento.dataAtualizacao()
        );

        ConsentimentoJpaEntity salvo = repository.save(entity);
        return toDomain(salvo);
    }

    private ConsentimentoNotificacao toDomain(ConsentimentoJpaEntity entity) {
        return new ConsentimentoNotificacao(
                entity.getId(),
                entity.getCalendarioVacinalId(),
                Boolean.TRUE.equals(entity.getAceitaNotificacao()),
                entity.getDataAtualizacao()
        );
    }
}

