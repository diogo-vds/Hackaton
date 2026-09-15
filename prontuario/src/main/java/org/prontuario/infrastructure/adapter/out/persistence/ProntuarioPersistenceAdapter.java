package org.prontuario.infrastructure.adapter.out.persistence;


import org.prontuario.application.port.out.ProntuarioRepositoryPort;
import org.prontuario.domain.model.Consulta;
import org.prontuario.domain.model.Prontuario;
import org.prontuario.domain.model.Vacina;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class ProntuarioPersistenceAdapter implements ProntuarioRepositoryPort {

    private final ProntuarioJpaRepository repository;

    public ProntuarioPersistenceAdapter(ProntuarioJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<Prontuario> buscarPorId(UUID id) {
        return repository.findById(id).map(this::toDomain);
    }

    @Override
    public Optional<Prontuario> buscarPorPacienteId(UUID pacienteId) {
        return repository.findByPacienteId(pacienteId).map(this::toDomain);
    }

    @Override
    public Prontuario salvar(Prontuario prontuario) {
        ProntuarioJpaEntity entity = toEntity(prontuario);
        return toDomain(repository.save(entity));
    }

    // ---------- Mappers ----------

    private Prontuario toDomain(ProntuarioJpaEntity e) {
        return new Prontuario(
                e.getId(),
                e.getPacienteId(),
                e.getNomePaciente(),
                e.getObservacoes(),
                e.getVacinas() == null ? null : e.getVacinas().stream()
                        .map(v -> new Vacina(v.getId(), v.getNome(), v.getLote(),
                                v.getFabricante(), v.getDataAplicacao(),
                                v.getProximaDose(), v.getProfissionalAplicador()))
                        .collect(Collectors.toList()),
                e.getConsultas() == null ? null : e.getConsultas().stream()
                        .map(c -> new Consulta(c.getId(), c.getEspecialidade(),
                                c.getMedicoNome(), c.getDataHora(), c.getObservacoes()))
                        .collect(Collectors.toList()),
                e.getResponsaveis()
        );
    }

    private ProntuarioJpaEntity toEntity(Prontuario p) {
        ProntuarioJpaEntity e = new ProntuarioJpaEntity();
        e.setId(p.getId());
        e.setPacienteId(p.getPacienteId());
        e.setNomePaciente(p.getNomePaciente());
        e.setObservacoes(p.getObservacoes());
        e.setResponsaveis(p.getResponsaveis());
        e.setAtualizadoEm(p.getAtualizadoEm());

        e.setVacinas(p.getVacinas().stream().map(v -> {
            VacinaJpaEntity vj = new VacinaJpaEntity();
            vj.setId(v.id());
            vj.setNome(v.nome());
            vj.setLote(v.lote());
            vj.setFabricante(v.fabricante());
            vj.setDataAplicacao(v.dataAplicacao());
            vj.setProximaDose(v.proximaDose());
            vj.setProfissionalAplicador(v.profissionalAplicador());
            return vj;
        }).collect(Collectors.toList()));

        e.setConsultas(p.getConsultas().stream().map(c -> {
            ConsultaJpaEntity cj = new ConsultaJpaEntity();
            cj.setId(c.id());
            cj.setEspecialidade(c.especialidade());
            cj.setMedicoNome(c.medicoNome());
            cj.setDataHora(c.dataHora());
            cj.setObservacoes(c.observacoes());
            return cj;
        }).collect(Collectors.toList()));

        return e;
    }
}
