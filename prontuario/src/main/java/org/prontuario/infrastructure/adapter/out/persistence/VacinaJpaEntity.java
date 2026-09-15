package org.prontuario.infrastructure.adapter.out.persistence;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "vacina")
public class VacinaJpaEntity {
    @Id
    private UUID id;
    private String nome;
    private String lote;
    private String fabricante;
    private LocalDate dataAplicacao;
    private LocalDate proximaDose;
    private String profissionalAplicador;

    // getters/setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getLote() {
        return lote;
    }

    public void setLote(String lote) {
        this.lote = lote;
    }

    public String getFabricante() {
        return fabricante;
    }

    public void setFabricante(String fabricante) {
        this.fabricante = fabricante;
    }

    public LocalDate getDataAplicacao() {
        return dataAplicacao;
    }

    public void setDataAplicacao(LocalDate dataAplicacao) {
        this.dataAplicacao = dataAplicacao;
    }

    public LocalDate getProximaDose() {
        return proximaDose;
    }

    public void setProximaDose(LocalDate proximaDose) {
        this.proximaDose = proximaDose;
    }

    public String getProfissionalAplicador() {
        return profissionalAplicador;
    }

    public void setProfissionalAplicador(String p) {
        this.profissionalAplicador = p;
    }
}
