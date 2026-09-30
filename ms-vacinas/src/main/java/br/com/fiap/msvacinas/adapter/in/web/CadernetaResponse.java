package br.com.fiap.msvacinas.adapter.in.web;

import br.com.fiap.msvacinas.domain.model.CadernetaVacinal;
import java.util.List;

public record CadernetaResponse(
        String numeroSus,
        String statusConsulta,
        String mensagem,
        List<String> vacinas) {

    static CadernetaResponse from(CadernetaVacinal caderneta) {
        return new CadernetaResponse(
                caderneta.numeroSus(), caderneta.statusConsulta(),
                caderneta.mensagem(), caderneta.vacinas());
    }
}
