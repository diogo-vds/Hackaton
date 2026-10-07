package com.fiap.hackathon.mvp.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "aplicacao_vacina")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AplicacaoVacina {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "numero_sus", nullable = false)
    private Long numeroSus;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "esquema_vacinacao_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_aplicacao_esquema")
    )
    private EsquemaVacinacao esquemaVacinacao;

    @Column(name = "data_aplicacao", nullable = false)
    private LocalDateTime dataAplicacao;

    @Column(name = "lote", nullable = false, length = 50)
    private String lote;

    @Column(name = "agente_saude_id", nullable = false)
    private Long agenteSaudeId;

    @Column(name = "observacao", length = 500)
    private String observacao;

    @Column(name = "unidade_atendimento_id", nullable = false)
    private Long unidadeAtendimentoId;
}