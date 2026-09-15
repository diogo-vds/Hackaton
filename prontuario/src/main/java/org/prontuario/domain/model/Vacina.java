package org.prontuario.domain.model;

import java.time.LocalDate;
import java.util.UUID;

public record Vacina(
        UUID id,
        String nome,
        String lote,
        String fabricante,
        LocalDate dataAplicacao,
        LocalDate proximaDose,
        String profissionalAplicador
) {}
