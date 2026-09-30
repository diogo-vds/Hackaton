package br.com.fiap.msvacinas.application.port.out;

import br.com.fiap.msvacinas.domain.model.Vacina;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface VacinaRepositoryPort {
    Vacina salvar(Vacina vacina);
    List<Vacina> listar();
    Optional<Vacina> buscarPorId(UUID id);
    void excluir(UUID id);
}
