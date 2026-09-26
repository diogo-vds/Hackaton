package br.com.fiap.msvacinas.application.service;

import br.com.fiap.msvacinas.application.port.in.ConsultarCadernetaUseCase;
import br.com.fiap.msvacinas.domain.exception.NumeroSusInvalidoException;
import br.com.fiap.msvacinas.domain.model.CadernetaVacinal;
import java.util.List;

public class CadernetaService implements ConsultarCadernetaUseCase {
    private static final String NUMERO_SUS_REGEX = "\\d{15}";

    @Override
    public CadernetaVacinal consultar(String numeroSus) {
        if (numeroSus == null || !numeroSus.matches(NUMERO_SUS_REGEX)) {
            throw new NumeroSusInvalidoException();
        }
        //TODO
        return new CadernetaVacinal(
                numeroSus,
                "CONSULTA_SIMULADA",
                "Integração com a Caderneta do SUS ainda não implementada",
                List.of());
    }
}
