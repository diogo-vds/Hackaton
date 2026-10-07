package com.fiap.hackathon.mvp.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "esquema_vacinacao")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EsquemaVacinacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "calendario_vacinal_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_esquema_calendario")
    )
    private CalendarioVacinal calendarioVacinal;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "vacina_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_esquema_vacina")
    )
    private Vacina vacina;

    @Column(name = "numero_dose", nullable = false)
    private Integer numeroDose;

    @Column(name = "tipo_dose", nullable = false, length = 50)
    private String tipoDose;

    @Column(name = "idade_minima_valor")
    private Integer idadeMinimaValor;

    @Column(name = "idade_minima_unidade", length = 20)
    private String idadeMinimaUnidade;

    @Column(name = "idade_recomendada_valor")
    private Integer idadeRecomendadaValor;

    @Column(name = "idade_recomendada_unidade", length = 20)
    private String idadeRecomendadaUnidade;

    @Column(name = "idade_maxima_valor")
    private Integer idadeMaximaValor;

    @Column(name = "idade_maxima_unidade", length = 20)
    private String idadeMaximaUnidade;

    @Column(name = "intervalo_minimo_valor")
    private Integer intervaloMinimoValor;

    @Column(name = "intervalo_minimo_unidade", length = 20)
    private String intervaloMinimoUnidade;
}