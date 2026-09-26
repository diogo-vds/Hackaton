package br.com.fiap.msvacinas.domain.model;

import java.util.List;

public record CadernetaVacinal(
        String numeroSus,
        String statusConsulta,
        String mensagem,
        List<String> vacinas) {
}
