package com.fiap.hackathon.mvp.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "vacina",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_vacina_nome",
                        columnNames = "nome"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vacina {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nome", nullable = false, length = 150)
    private String nome;

    @Column(name = "sigla", length = 20)
    private String sigla;

    @Column(name = "fabricante", length = 150)
    private String fabricante;
}