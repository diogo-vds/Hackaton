package org.prontuario.infrastructure.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface ProntuarioJpaRepository extends JpaRepository<ProntuarioJpaEntity, UUID> {
    Optional<ProntuarioJpaEntity> findByPacienteId(UUID pacienteId);
}
