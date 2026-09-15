package org.prontuario.infrastructure.adapter.in.dto;

import jakarta.validation.constraints.NotBlank;

public record AtualizarProntuarioRequest(@NotBlank String observacoes) {}