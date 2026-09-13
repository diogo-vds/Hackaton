package br.com.fiap.msvacinas.adapter.out.persistence;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "vacinas")
public class VacinaJpaEntity {
    @Id private UUID id;
    @Column(nullable = false, length = 150) private String nome;
    @Column(nullable = false, length = 1000) private String descricao;
    @Column(name = "idade_alvo_em_meses", nullable = false) private Integer idadeAlvoEmMeses;

    protected VacinaJpaEntity() {}
    public VacinaJpaEntity(UUID id, String nome, String descricao, Integer idadeAlvoEmMeses) {
        this.id = id; this.nome = nome; this.descricao = descricao; this.idadeAlvoEmMeses = idadeAlvoEmMeses;
    }
    public UUID getId() { return id; }
    public String getNome() { return nome; }
    public String getDescricao() { return descricao; }
    public Integer getIdadeAlvoEmMeses() { return idadeAlvoEmMeses; }
}
