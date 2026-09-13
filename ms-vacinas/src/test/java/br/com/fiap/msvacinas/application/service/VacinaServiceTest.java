package br.com.fiap.msvacinas.application.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import br.com.fiap.msvacinas.application.port.out.VacinaRepositoryPort;
import br.com.fiap.msvacinas.domain.exception.VacinaNaoEncontradaException;
import br.com.fiap.msvacinas.domain.model.Vacina;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class VacinaServiceTest {
    private final VacinaRepositoryPort repository = mock(VacinaRepositoryPort.class);
    private final VacinaService service = new VacinaService(repository);

    @Test void deveCriarComNovoId() {
        var entrada = new Vacina(null, "BCG", "Dose única", 0);
        when(repository.salvar(any())).thenAnswer(invocation -> invocation.getArgument(0));
        var criada = service.criar(entrada);
        assertNotNull(criada.id());
        assertEquals("BCG", criada.nome());
    }

    @Test void deveFalharQuandoNaoEncontrar() {
        var id = UUID.randomUUID();
        when(repository.buscarPorId(id)).thenReturn(Optional.empty());
        assertThrows(VacinaNaoEncontradaException.class, () -> service.buscarPorId(id));
    }
}
