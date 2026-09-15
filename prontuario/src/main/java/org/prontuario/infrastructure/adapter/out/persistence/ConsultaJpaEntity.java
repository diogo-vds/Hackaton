package org.prontuario.infrastructure.adapter.out.persistence;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import java.util.UUID;


    @Entity
    @Table(name = "consulta")
    public class ConsultaJpaEntity {
        @Id
        private UUID id;
        private String especialidade;
        private String medicoNome;
        private LocalDateTime dataHora;
        private String observacoes;

        public UUID getId() { return id; }
        public void setId(UUID id) { this.id = id; }
        public String getEspecialidade() { return especialidade; }
        public void setEspecialidade(String e) { this.especialidade = e; }
        public String getMedicoNome() { return medicoNome; }
        public void setMedicoNome(String m) { this.medicoNome = m; }
        public LocalDateTime getDataHora() { return dataHora; }
        public void setDataHora(LocalDateTime d) { this.dataHora = d; }
        public String getObservacoes() { return observacoes; }
        public void setObservacoes(String o) { this.observacoes = o; }
}
