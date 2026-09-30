package br.com.fiap.msvacinas.application.service;

import br.com.fiap.msvacinas.application.port.in.ConsultarCadernetaUseCase;
import br.com.fiap.msvacinas.domain.exception.NumeroSusInvalidoException;
import br.com.fiap.msvacinas.domain.model.CadernetaVacinal;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;

public class CadernetaService implements ConsultarCadernetaUseCase {
    private static final String NUMERO_SUS_REGEX = "\\d{12}";
    private static final String CONSULTAR_VACINAS_SQL = """
            SELECT v.nome
              FROM aplicacao_vacina av
              JOIN esquema_vacinacao ev ON ev.id = av.esquema_vacinacao_id
              JOIN vacina v ON v.id = ev.vacina_id
             WHERE av.numero_sus = ?
             ORDER BY av.data_aplicacao DESC
            """;

    private final JdbcTemplate jdbcTemplate;

    public CadernetaService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public CadernetaVacinal consultar(String numeroSus) {
        if (numeroSus == null || !numeroSus.matches(NUMERO_SUS_REGEX)) {
            throw new NumeroSusInvalidoException();
        }

        List<String> vacinas = jdbcTemplate.queryForList(
                CONSULTAR_VACINAS_SQL,
                String.class,
                Long.parseLong(numeroSus));

        return new CadernetaVacinal(
                numeroSus,
                "CONSULTA_REALIZADA",
                vacinas.isEmpty()
                        ? "Nenhuma vacina encontrada para o número SUS informado"
                        : "Caderneta do SUS consultada com sucesso",
                vacinas);
    }
}
