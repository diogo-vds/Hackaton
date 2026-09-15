package org.prontuario.application.port.in;

import org.prontuario.domain.model.Prontuario;
import org.prontuario.domain.model.TipoUsuario;

import java.util.UUID;

public interface ConsultarProntuarioUseCase {
    Prontuario consultar(UUID prontuarioId, UUID solicitanteId, TipoUsuario tipo);
    Prontuario buscarPorPacienteLogado(UUID pacienteId);
}
