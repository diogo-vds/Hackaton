package com.fiap.hackathon.mvp.persistence.repository;

import com.fiap.hackathon.mvp.persistence.entity.AplicacaoVacina;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AplicacaoVacinaRepository
        extends JpaRepository<AplicacaoVacina, Long> {

    @Query("""
        SELECT a
        FROM AplicacaoVacina a
        JOIN FETCH a.esquemaVacinacao e
        JOIN FETCH e.vacina v
        WHERE a.numeroSus IN :numerosSus
        ORDER BY a.numeroSus, a.dataAplicacao
    """)
    List<AplicacaoVacina> findHistoricoPorPessoas(
            @Param("numerosSus") List<Long> numerosSus
    );
}