package org.prontuario.application.port.in;


import org.prontuario.domain.model.Prontuario;
import org.prontuario.domain.model.TipoUsuario;

import java.util.UUID;

public interface AtualizarProntuarioUseCase {
    Prontuario atualizar(UUID prontuarioId, String observacoes, UUID solicitanteId, TipoUsuario tipo);
}
