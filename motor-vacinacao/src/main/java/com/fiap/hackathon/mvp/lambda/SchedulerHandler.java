package com.fiap.hackathon.mvp.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fiap.hackathon.mvp.MvpApplication;
import com.fiap.hackathon.mvp.service.ProcessadorPendenciasVacinaisService;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

public class SchedulerHandler
        implements RequestHandler<Object, Void> {

    private static ConfigurableApplicationContext context;

    private static ProcessadorPendenciasVacinaisService service;

    public SchedulerHandler() {
    }

    SchedulerHandler(
            ProcessadorPendenciasVacinaisService service
    ) {
        SchedulerHandler.service = service;
    }

    @Override
    public Void handleRequest(
            Object event,
            Context awsContext
    ) {

        inicializarContexto();

        service.processar();

        return null;
    }

    private synchronized void inicializarContexto() {

        if (service != null) {
            return;
        }

        if (context == null) {

            context =
                    new SpringApplicationBuilder(
                            MvpApplication.class
                    )
                            .web(WebApplicationType.NONE)
                            .run();

            service =
                    context.getBean(
                            ProcessadorPendenciasVacinaisService.class
                    );
        }
    }
}