package br.com.fiap.msvacinas.application.port.in;

import br.com.fiap.msvacinas.domain.model.CadernetaVacinal;

public interface ConsultarCadernetaUseCase {
    CadernetaVacinal consultar(String numeroSus);
}
