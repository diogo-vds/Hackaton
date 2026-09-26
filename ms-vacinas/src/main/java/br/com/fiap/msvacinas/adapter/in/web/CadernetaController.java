package br.com.fiap.msvacinas.adapter.in.web;

import br.com.fiap.msvacinas.application.port.in.ConsultarCadernetaUseCase;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/caderneta")
public class CadernetaController {
    private final ConsultarCadernetaUseCase consultarCadernetaUseCase;

    public CadernetaController(ConsultarCadernetaUseCase consultarCadernetaUseCase) {
        this.consultarCadernetaUseCase = consultarCadernetaUseCase;
    }

    @GetMapping
    public CadernetaResponse consultar(@RequestParam("numero_sus") String numeroSus) {
        return CadernetaResponse.from(consultarCadernetaUseCase.consultar(numeroSus));
    }
}
