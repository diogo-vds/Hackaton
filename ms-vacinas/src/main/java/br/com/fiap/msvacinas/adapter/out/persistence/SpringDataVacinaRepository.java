package br.com.fiap.msvacinas.adapter.out.persistence;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

interface SpringDataVacinaRepository extends JpaRepository<VacinaJpaEntity, UUID> {
}
