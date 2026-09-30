package br.com.fiap.msvacinas.domain.model;

import java.util.UUID;

public record Vacina(UUID id, String nome, String descricao, Integer idadeAlvoEmMeses) {
    public Vacina comId(UUID novoId) {
        return new Vacina(novoId, nome, descricao, idadeAlvoEmMeses);
    }
}
