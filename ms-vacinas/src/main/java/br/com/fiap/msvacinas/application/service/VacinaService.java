package br.com.fiap.msvacinas.application.service;

import br.com.fiap.msvacinas.application.port.in.VacinaUseCase;
import br.com.fiap.msvacinas.application.port.out.VacinaRepositoryPort;
import br.com.fiap.msvacinas.domain.exception.VacinaNaoEncontradaException;
import br.com.fiap.msvacinas.domain.model.Vacina;
import java.util.List;
import java.util.UUID;

public class VacinaService implements VacinaUseCase {
    private final VacinaRepositoryPort repository;

    public VacinaService(VacinaRepositoryPort repository) {
        this.repository = repository;
    }

    @Override public Vacina criar(Vacina vacina) {
        return repository.salvar(vacina.comId(UUID.randomUUID()));
    }
    @Override public List<Vacina> listar() { return repository.listar(); }
    @Override public Vacina buscarPorId(UUID id) {
        return repository.buscarPorId(id).orElseThrow(() -> new VacinaNaoEncontradaException(id));
    }
    @Override public Vacina atualizar(UUID id, Vacina vacina) {
        buscarPorId(id);
        return repository.salvar(vacina.comId(id));
    }
    @Override public void excluir(UUID id) {
        buscarPorId(id);
        repository.excluir(id);
    }
}
