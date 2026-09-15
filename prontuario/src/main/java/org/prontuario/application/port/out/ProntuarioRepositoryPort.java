package org.prontuario.application.port.out;


import org.prontuario.domain.model.Prontuario;

import java.util.Optional;
import java.util.UUID;

public interface ProntuarioRepositoryPort {
    Optional<Prontuario> buscarPorId(UUID id);
    Optional<Prontuario> buscarPorPacienteId(UUID pacienteId);
    Prontuario salvar(Prontuario prontuario);
}
