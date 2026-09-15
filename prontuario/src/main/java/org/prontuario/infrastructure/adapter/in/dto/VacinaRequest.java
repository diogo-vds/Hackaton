package org.prontuario.infrastructure.adapter.in.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record VacinaRequest(
        @NotBlank String nome,
        String lote,
        String fabricante,
        @NotNull LocalDate dataAplicacao,
        LocalDate proximaDose,
        String profissionalAplicador
) {}
