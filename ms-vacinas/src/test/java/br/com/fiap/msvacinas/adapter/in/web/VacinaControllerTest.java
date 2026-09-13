package br.com.fiap.msvacinas.adapter.in.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import br.com.fiap.msvacinas.application.port.in.VacinaUseCase;
import br.com.fiap.msvacinas.domain.model.Vacina;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(VacinaController.class)
class VacinaControllerTest {
    @Autowired MockMvc mvc;
    @MockitoBean VacinaUseCase useCase;

    @Test void deveCriarVacina() throws Exception {
        var id = UUID.randomUUID();
        when(useCase.criar(any())).thenReturn(new Vacina(id, "BCG", "Dose única", 0));
        mvc.perform(post("/vacinas").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\":\"BCG\",\"descricao\":\"Dose única\",\"idadeAlvoEmMeses\":0}"))
                .andExpect(status().isCreated()).andExpect(header().string("Location", "/vacinas/" + id))
                .andExpect(jsonPath("$.nome").value("BCG"));
    }

    @Test void deveRejeitarDadosInvalidos() throws Exception {
        mvc.perform(post("/vacinas").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\":\"\",\"descricao\":\"\",\"idadeAlvoEmMeses\":-1}"))
                .andExpect(status().isBadRequest()).andExpect(jsonPath("$.title").value("Erro de validação"));
    }
}
