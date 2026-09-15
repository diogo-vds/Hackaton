package org.prontuario.domain.model;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Prontuario {

    private UUID id;
    private UUID pacienteId;
    private String nomePaciente;
    private String observacoes;
    private List<Vacina> vacinas;
    private List<Consulta> consultas;
    private List<UUID> responsaveis; // pais/responsáveis com acesso
    private LocalDateTime atualizadoEm;

    public Prontuario(UUID id, UUID pacienteId, String nomePaciente,
                      String observacoes, List<Vacina> vacinas,
                      List<Consulta> consultas, List<UUID> responsaveis) {
        this.id = id;
        this.pacienteId = pacienteId;
        this.nomePaciente = nomePaciente;
        this.observacoes = observacoes;
        this.vacinas = vacinas != null ? vacinas : new ArrayList<>();
        this.consultas = consultas != null ? consultas : new ArrayList<>();
        this.responsaveis = responsaveis != null ? responsaveis : new ArrayList<>();
        this.atualizadoEm = LocalDateTime.now();
    }

    /** Regra de negócio: só responsáveis, médico ou enfermeira acessam */
    public boolean podeSerAcessadoPor(UUID usuarioId, TipoUsuario tipo) {
        if (tipo == TipoUsuario.MEDICO || tipo == TipoUsuario.ENFERMEIRA) return true;
        return tipo == TipoUsuario.PACIENTE && responsaveis.contains(usuarioId);
    }

    public void atualizarObservacoes(String novasObservacoes) {
        this.observacoes = novasObservacoes;
        this.atualizadoEm = LocalDateTime.now();
    }

    public void adicionarVacina(Vacina vacina) {
        this.vacinas.add(vacina);
        this.atualizadoEm = LocalDateTime.now();
    }

    public void adicionarConsulta(Consulta consulta) {
        this.consultas.add(consulta);
        this.atualizadoEm = LocalDateTime.now();
    }

    // getters
    public UUID getId() { return id; }
    public UUID getPacienteId() { return pacienteId; }
    public String getNomePaciente() { return nomePaciente; }
    public String getObservacoes() { return observacoes; }
    public List<Vacina> getVacinas() { return vacinas; }
    public List<Consulta> getConsultas() { return consultas; }
    public List<UUID> getResponsaveis() { return responsaveis; }
    public LocalDateTime getAtualizadoEm() { return atualizadoEm; }
}
