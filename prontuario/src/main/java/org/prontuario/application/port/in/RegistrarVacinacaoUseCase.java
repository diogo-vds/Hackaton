package org.prontuario.application.port.in;


import org.prontuario.domain.model.Prontuario;
import org.prontuario.domain.model.Vacina;

import java.util.UUID;

public interface RegistrarVacinacaoUseCase {
    Prontuario registrar(UUID prontuarioId, Vacina vacina);
}
