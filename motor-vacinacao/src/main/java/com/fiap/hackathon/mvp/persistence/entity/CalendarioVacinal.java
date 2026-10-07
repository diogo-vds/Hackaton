package com.fiap.hackathon.mvp.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(
        name = "calendario_vacinal",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_calendario_versao",
                        columnNames = {"nome", "versao"}
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CalendarioVacinal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nome", nullable = false, length = 150)
    private String nome;

    @Column(name = "versao", nullable = false, length = 20)
    private String versao;

    @Column(name = "data_inicio_vigencia", nullable = false)
    private LocalDate dataInicioVigencia;

    @Column(name = "data_fim_vigencia")
    private LocalDate dataFimVigencia;
}
