package org.prontuario.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;

public record Consulta(
        UUID id,
        String especialidade,
        String medicoNome,
        LocalDateTime dataHora,
        String observacoes
) {}
