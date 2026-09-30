package br.com.fiap.msvacinas.adapter.in.web;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record VacinaRequest(
        @NotBlank @Size(max = 150) String nome,
        @NotBlank @Size(max = 1000) String descricao,
        @NotNull @Min(0) Integer idadeAlvoEmMeses) {
}
