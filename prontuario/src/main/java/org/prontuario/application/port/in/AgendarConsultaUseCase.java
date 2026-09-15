package org.prontuario.application.port.in;


import org.prontuario.domain.model.Consulta;
import org.prontuario.domain.model.Prontuario;

import java.util.UUID;

public interface AgendarConsultaUseCase {
    Prontuario agendar(UUID prontuarioId, Consulta consulta);
}
