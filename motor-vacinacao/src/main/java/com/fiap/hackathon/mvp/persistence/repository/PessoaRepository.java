package com.fiap.hackathon.mvp.persistence.repository;

import com.fiap.hackathon.mvp.dto.PessoaElegivelDTO;
import com.fiap.hackathon.mvp.persistence.entity.Pessoa;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PessoaRepository extends JpaRepository<Pessoa, Long> {

    @Query("""
        SELECT new com.fiap.hackathon.mvp.dto.PessoaElegivelDTO(
            p.numeroSus,
            p.dataNascimento
        )
        FROM Pessoa p
        WHERE p.consentimento = true
          AND p.numeroSus > :ultimoNumeroSus
        ORDER BY p.numeroSus
    """)
    List<PessoaElegivelDTO> buscarPessoasElegiveis(
            @Param("ultimoNumeroSus") Long ultimoNumeroSus,
            Pageable pageable
    );
}