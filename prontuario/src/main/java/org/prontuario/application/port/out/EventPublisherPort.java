package org.prontuario.application.port.out;



import org.prontuario.domain.model.Consulta;
import org.prontuario.domain.model.Vacina;

import java.util.UUID;

public interface EventPublisherPort {
    void publicarLembreteVacina(UUID pacienteId, Vacina vacina);
    void publicarLembreteConsulta(UUID pacienteId, Consulta consulta);
}
