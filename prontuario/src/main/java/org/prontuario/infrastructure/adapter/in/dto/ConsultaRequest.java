package org.prontuario.infrastructure.adapter.in.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record ConsultaRequest(
        @NotBlank String especialidade,
        @NotBlank String medicoNome,
        @NotNull LocalDateTime dataHora,
        String observacoes
) {}
