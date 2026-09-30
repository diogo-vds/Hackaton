package br.com.fiap.msvacinas.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface SpringDataConsentimentoRepository extends JpaRepository<ConsentimentoJpaEntity, UUID> {

    Optional<ConsentimentoJpaEntity> findByCalendarioVacinalId(UUID calendarioVacinalId);
}
