package br.com.fiap.msvacinas.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import br.com.fiap.msvacinas.domain.exception.NumeroSusInvalidoException;
import org.junit.jupiter.api.Test;

class CadernetaServiceTest {
    private final CadernetaService service = new CadernetaService();

    @Test
    void deveRetornarConsultaSimulada() {
        var resultado = service.consultar("123456789012345");

        assertEquals("123456789012345", resultado.numeroSus());
        assertEquals("CONSULTA_SIMULADA", resultado.statusConsulta());
        assertEquals(0, resultado.vacinas().size());
    }

    @Test
    void deveRejeitarNumeroSusInvalido() {
        assertThrows(NumeroSusInvalidoException.class, () -> service.consultar("123"));
    }
}
