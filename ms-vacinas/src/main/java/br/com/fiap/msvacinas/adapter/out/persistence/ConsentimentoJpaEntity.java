package br.com.fiap.msvacinas.adapter.out.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "consentimento")
public class ConsentimentoJpaEntity {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "calendario_vacinal_id", nullable = false)
    private UUID calendarioVacinalId;

    @Column(name = "aceita_notificacao", nullable = false)
    private Boolean aceitaNotificacao;

    @Column(name = "data_atualizacao", nullable = false)
    private LocalDateTime dataAtualizacao;

    protected ConsentimentoJpaEntity() {
    }

    public ConsentimentoJpaEntity(
            UUID id,
            UUID calendarioVacinalId,
            Boolean aceitaNotificacao,
            LocalDateTime dataAtualizacao
    ) {
        this.id = id;
        this.calendarioVacinalId = calendarioVacinalId;
        this.aceitaNotificacao = aceitaNotificacao;
        this.dataAtualizacao = dataAtualizacao;
    }

    public UUID getId() {
        return id;
    }

    public UUID getCalendarioVacinalId() {
        return calendarioVacinalId;
    }

    public Boolean getAceitaNotificacao() {
        return aceitaNotificacao;
    }

    public LocalDateTime getDataAtualizacao() {
        return dataAtualizacao;
    }
}
