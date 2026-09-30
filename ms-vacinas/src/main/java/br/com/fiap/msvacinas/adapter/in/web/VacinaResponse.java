package br.com.fiap.msvacinas.adapter.in.web;

import br.com.fiap.msvacinas.domain.model.Vacina;
import java.util.UUID;

public record VacinaResponse(UUID id, String nome, String descricao, Integer idadeAlvoEmMeses) {
    static VacinaResponse from(Vacina vacina) {
        return new VacinaResponse(vacina.id(), vacina.nome(), vacina.descricao(), vacina.idadeAlvoEmMeses());
    }
}
