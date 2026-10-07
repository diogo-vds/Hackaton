package com.fiap.hackathon.mvp.lambda;

import com.fiap.hackathon.mvp.service.ProcessadorPendenciasVacinaisService;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class SchedulerHandlerTest {

    @Test
    void deveAcionarProcessadorDePendencias() {

        ProcessadorPendenciasVacinaisService service =
                mock(
                        ProcessadorPendenciasVacinaisService.class
                );

        SchedulerHandler handler =
                new SchedulerHandler(service);

        handler.handleRequest(
                new Object(),
                null
        );

        verify(service).processar();
    }
}