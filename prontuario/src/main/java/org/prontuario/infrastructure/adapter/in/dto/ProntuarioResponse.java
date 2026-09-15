package org.prontuario.infrastructure.adapter.in.dto;



import org.prontuario.domain.model.Consulta;
import org.prontuario.domain.model.Vacina;

import java.time.LocalDateTime;

import java.util.List;
import java.util.UUID;

public record ProntuarioResponse(
        UUID id,
        UUID pacienteId,
        String nomePaciente,
        String observacoes,
        List<Vacina> vacinas,
        List<Consulta> consultas,
        LocalDateTime atualizadoEm
) {}
