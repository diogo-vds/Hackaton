package br.com.fiap.msvacinas.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import br.com.fiap.msvacinas.domain.exception.NumeroSusInvalidoException;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;

class CadernetaServiceTest {
    private final JdbcTemplate jdbcTemplate = mock(JdbcTemplate.class);
    private final CadernetaService service = new CadernetaService(jdbcTemplate);

    @Test
    void deveConsultarCadernetaNoBanco() {
        when(jdbcTemplate.queryForList(
                org.mockito.ArgumentMatchers.anyString(),
                org.mockito.ArgumentMatchers.eq(String.class),
                org.mockito.ArgumentMatchers.eq(123456789012L)))
                .thenReturn(List.of("BCG"));

        var resultado = service.consultar("123456789012");

        assertEquals("123456789012", resultado.numeroSus());
        assertEquals("CONSULTA_REALIZADA", resultado.statusConsulta());
        assertEquals(List.of("BCG"), resultado.vacinas());
    }

    @Test
    void deveRejeitarNumeroSusInvalido() {
        assertThrows(NumeroSusInvalidoException.class, () -> service.consultar("123"));
    }
}
