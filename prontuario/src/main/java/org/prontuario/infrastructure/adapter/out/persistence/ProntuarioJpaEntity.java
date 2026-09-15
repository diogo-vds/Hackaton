package org.prontuario.infrastructure.adapter.out.persistence;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "prontuario")
public class ProntuarioJpaEntity {


    @Id
    private UUID id;

    @Column(name = "paciente_id", nullable = false, unique = true)
    private UUID pacienteId;

    @Column(name = "nome_paciente", nullable = false)
    private String nomePaciente;

    @Column(columnDefinition = "TEXT")
    private String observacoes;

    @ElementCollection
    @CollectionTable(name = "prontuario_responsavel",
            joinColumns = @JoinColumn(name = "prontuario_id"))
    @Column(name = "responsavel_id")
    private List<UUID> responsaveis;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "prontuario_id")
    private List<VacinaJpaEntity> vacinas;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "prontuario_id")
    private List<ConsultaJpaEntity> consultas;

    private LocalDateTime atualizadoEm;

    // getters e setters omitidos
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public UUID getPacienteId() { return pacienteId; }
    public void setPacienteId(UUID pacienteId) { this.pacienteId = pacienteId; }
    public String getNomePaciente() { return nomePaciente; }
    public void setNomePaciente(String nomePaciente) { this.nomePaciente = nomePaciente; }
    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }
    public List<UUID> getResponsaveis() { return responsaveis; }
    public void setResponsaveis(List<UUID> responsaveis) { this.responsaveis = responsaveis; }
    public List<VacinaJpaEntity> getVacinas() { return vacinas; }
    public void setVacinas(List<VacinaJpaEntity> vacinas) { this.vacinas = vacinas; }
    public List<ConsultaJpaEntity> getConsultas() { return consultas; }
    public void setConsultas(List<ConsultaJpaEntity> consultas) { this.consultas = consultas; }
    public LocalDateTime getAtualizadoEm() { return atualizadoEm; }
    public void setAtualizadoEm(LocalDateTime atualizadoEm) { this.atualizadoEm = atualizadoEm; }
}
