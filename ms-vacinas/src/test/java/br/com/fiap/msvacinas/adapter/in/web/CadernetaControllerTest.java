package br.com.fiap.msvacinas.adapter.in.web;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import br.com.fiap.msvacinas.application.port.in.ConsultarCadernetaUseCase;
import br.com.fiap.msvacinas.domain.model.CadernetaVacinal;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(CadernetaController.class)
class CadernetaControllerTest {
    @Autowired MockMvc mvc;
    @MockitoBean ConsultarCadernetaUseCase useCase;

    @Test
    void deveConsultarCaderneta() throws Exception {
        var numeroSus = "123456789012345";
        when(useCase.consultar(numeroSus)).thenReturn(new CadernetaVacinal(
                numeroSus,
                "CONSULTA_SIMULADA",
                "Integração com a Caderneta do SUS ainda não implementada",
                List.of()));

        mvc.perform(get("/api/v1/caderneta").param("numero_sus", numeroSus))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numeroSus").value(numeroSus))
                .andExpect(jsonPath("$.statusConsulta").value("CONSULTA_SIMULADA"))
                .andExpect(jsonPath("$.vacinas").isArray());
    }

    @Test
    void deveExigirNumeroSus() throws Exception {
        mvc.perform(get("/api/v1/caderneta"))
                .andExpect(status().isBadRequest());
    }
}
