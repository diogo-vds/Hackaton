package br.com.fiap.msvacinas.domain.exception;

import java.util.UUID;

public class VacinaNaoEncontradaException extends RuntimeException {
    public VacinaNaoEncontradaException(UUID id) {
        super("Vacina não encontrada: " + id);
    }
}
