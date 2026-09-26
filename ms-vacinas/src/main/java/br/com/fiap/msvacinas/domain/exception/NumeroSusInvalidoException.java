package br.com.fiap.msvacinas.domain.exception;

public class NumeroSusInvalidoException extends RuntimeException {
    public NumeroSusInvalidoException() {
        super("O número SUS deve conter exatamente 15 dígitos");
    }
}
