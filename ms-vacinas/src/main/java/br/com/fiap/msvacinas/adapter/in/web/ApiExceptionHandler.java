package br.com.fiap.msvacinas.adapter.in.web;

import br.com.fiap.msvacinas.domain.exception.VacinaNaoEncontradaException;
import java.net.URI;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(VacinaNaoEncontradaException.class)
    ProblemDetail naoEncontrada(VacinaNaoEncontradaException exception) {
        var problem = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, exception.getMessage());
        problem.setTitle("Vacina não encontrada");
        problem.setType(URI.create("urn:problema:vacina-nao-encontrada"));
        return problem;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ProblemDetail validacao(MethodArgumentNotValidException exception) {
        var problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Dados da vacina inválidos");
        problem.setTitle("Erro de validação");
        problem.setProperty("erros", exception.getBindingResult().getFieldErrors().stream()
                .collect(java.util.stream.Collectors.toMap(
                        error -> error.getField(),
                        error -> error.getDefaultMessage(),
                        (primeiro, segundo) -> primeiro)));
        return problem;
    }
}
