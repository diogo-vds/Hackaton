package br.com.fiap.msvacinas.application.port.in;

import br.com.fiap.msvacinas.domain.model.Vacina;
import java.util.List;
import java.util.UUID;

public interface VacinaUseCase {
    Vacina criar(Vacina vacina);
    List<Vacina> listar();
    Vacina buscarPorId(UUID id);
    Vacina atualizar(UUID id, Vacina vacina);
    void excluir(UUID id);
}
