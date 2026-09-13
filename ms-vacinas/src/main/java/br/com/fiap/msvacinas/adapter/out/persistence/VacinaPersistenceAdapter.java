package br.com.fiap.msvacinas.adapter.out.persistence;

import br.com.fiap.msvacinas.application.port.out.VacinaRepositoryPort;
import br.com.fiap.msvacinas.domain.model.Vacina;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public class VacinaPersistenceAdapter implements VacinaRepositoryPort {
    private final SpringDataVacinaRepository repository;
    public VacinaPersistenceAdapter(SpringDataVacinaRepository repository) { this.repository = repository; }

    @Override public Vacina salvar(Vacina vacina) { return toDomain(repository.save(toEntity(vacina))); }
    @Override public List<Vacina> listar() { return repository.findAll().stream().map(this::toDomain).toList(); }
    @Override public Optional<Vacina> buscarPorId(UUID id) { return repository.findById(id).map(this::toDomain); }
    @Override public void excluir(UUID id) { repository.deleteById(id); }

    private VacinaJpaEntity toEntity(Vacina v) { return new VacinaJpaEntity(v.id(), v.nome(), v.descricao(), v.idadeAlvoEmMeses()); }
    private Vacina toDomain(VacinaJpaEntity e) { return new Vacina(e.getId(), e.getNome(), e.getDescricao(), e.getIdadeAlvoEmMeses()); }
}
