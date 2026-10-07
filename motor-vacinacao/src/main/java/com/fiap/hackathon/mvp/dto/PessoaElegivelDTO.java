package com.fiap.hackathon.mvp.dto;

import java.time.LocalDate;

public record PessoaElegivelDTO(
        Long numeroSus,
        LocalDate dataNascimento
) {
}