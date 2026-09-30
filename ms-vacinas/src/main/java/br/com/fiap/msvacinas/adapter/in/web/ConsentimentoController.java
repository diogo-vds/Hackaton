package br.com.fiap.msvacinas.adapter.in.web;



import br.com.fiap.msvacinas.application.port.in.SalvarConsentimentoUseCase;
import br.com.fiap.msvacinas.domain.model.ConsentimentoNotificacao;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/consentimentos")
public class ConsentimentoController {

    private final SalvarConsentimentoUseCase salvarConsentimentoUseCase;

    public ConsentimentoController(SalvarConsentimentoUseCase salvarConsentimentoUseCase) {
        this.salvarConsentimentoUseCase = salvarConsentimentoUseCase;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ConsentimentoResponse registrar(@Valid @RequestBody ConsentimentoRequest request) {
        ConsentimentoNotificacao consentimento = salvarConsentimentoUseCase.registrar(
                request.calendarioVacinalId(),
                request.aceitaNotificacao()
        );

        return ConsentimentoResponse.from(consentimento);
    }
}
