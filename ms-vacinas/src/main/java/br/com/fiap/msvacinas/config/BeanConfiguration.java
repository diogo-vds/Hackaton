package br.com.fiap.msvacinas.config;

import br.com.fiap.msvacinas.application.port.in.SalvarConsentimentoUseCase;
import br.com.fiap.msvacinas.application.port.in.VacinaUseCase;
import br.com.fiap.msvacinas.application.port.out.ConsentimentoPersistencePort;
import br.com.fiap.msvacinas.application.port.out.VacinaRepositoryPort;
import br.com.fiap.msvacinas.application.service.ConsentimentoService;
import br.com.fiap.msvacinas.application.service.VacinaService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {
    @Bean
    VacinaUseCase vacinaUseCase(VacinaRepositoryPort repository) { return new VacinaService(repository); }

    @Bean
    public SalvarConsentimentoUseCase salvarConsentimentoUseCase(
            ConsentimentoPersistencePort consentimentoPersistencePort
    ) {
        return new ConsentimentoService(consentimentoPersistencePort);
    }
}
