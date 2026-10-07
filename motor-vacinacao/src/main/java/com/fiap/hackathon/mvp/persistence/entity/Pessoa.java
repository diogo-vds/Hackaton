package com.fiap.hackathon.mvp.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "pessoa")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pessoa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "numero_sus")
    private Long numeroSus;

    @Column(name = "consentimento", nullable = false)
    private Boolean consentimento;

    @Column(name = "data_nascimento", nullable = false)
    private LocalDate dataNascimento;
}