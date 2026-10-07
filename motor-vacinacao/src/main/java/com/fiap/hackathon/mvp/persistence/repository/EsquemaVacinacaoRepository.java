package com.fiap.hackathon.mvp.persistence.repository;

import com.fiap.hackathon.mvp.persistence.entity.EsquemaVacinacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EsquemaVacinacaoRepository
        extends JpaRepository<EsquemaVacinacao, Long> {

    @Query("""
        SELECT e
        FROM EsquemaVacinacao e
        JOIN FETCH e.vacina v
        JOIN FETCH e.calendarioVacinal c
        WHERE c.dataInicioVigencia <= :dataReferencia
          AND (
              c.dataFimVigencia IS NULL
              OR c.dataFimVigencia >= :dataReferencia
          )
        ORDER BY v.id, e.numeroDose
    """)
    List<EsquemaVacinacao> findEsquemasVigentes(
            @Param("dataReferencia") LocalDate dataReferencia
    );
}