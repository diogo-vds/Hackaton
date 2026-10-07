package com.fiap.hackathon.mvp.service;

import com.fiap.hackathon.mvp.dto.NotificacaoVacinaPendenteDTO;
import com.fiap.hackathon.mvp.dto.PendenciaVacinalDTO;
import com.fiap.hackathon.mvp.producer.NotificacaoVacinaProducer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class NotificacaoVacinaProducerTest {

    private SqsClient sqsClient;

    private NotificacaoVacinaProducer producer;

    @BeforeEach
    void setUp() {

        sqsClient = mock(SqsClient.class);

        producer = new NotificacaoVacinaProducer(
                sqsClient,
                new ObjectMapper(),
                "http://localhost:4566/000000000000/SQS_Notificacao"
        );
    }

    @Test
    void devePublicarNotificacaoNaFila() {

        PendenciaVacinalDTO pendencia =
                new PendenciaVacinalDTO(
                        1L,
                        10L,
                        "Pentavalente",
                        1,
                        "ROTINA"
                );

        NotificacaoVacinaPendenteDTO notificacao =
                new NotificacaoVacinaPendenteDTO(
                        123456789L,
                        List.of(pendencia)
                );

        producer.publicar(notificacao);

        ArgumentCaptor<SendMessageRequest> captor =
                ArgumentCaptor.forClass(
                        SendMessageRequest.class
                );

        verify(sqsClient).sendMessage(
                captor.capture()
        );

        SendMessageRequest request =
                captor.getValue();

        assertEquals(
                "http://localhost:4566/000000000000/SQS_Notificacao",
                request.queueUrl()
        );

        assertTrue(
                request.messageBody()
                        .contains("123456789")
        );

        assertTrue(
                request.messageBody()
                        .contains("Pentavalente")
        );
    }

    @Test
    void deveEnviarTodasAsVacinasPendentesNaMesmaMensagem() {

        PendenciaVacinalDTO penta =
                new PendenciaVacinalDTO(
                        1L,
                        10L,
                        "Pentavalente",
                        1,
                        "ROTINA"
                );

        PendenciaVacinalDTO pneumo =
                new PendenciaVacinalDTO(
                        2L,
                        20L,
                        "Pneumocócica 10-valente",
                        1,
                        "ROTINA"
                );

        NotificacaoVacinaPendenteDTO notificacao =
                new NotificacaoVacinaPendenteDTO(
                        123456789L,
                        List.of(
                                penta,
                                pneumo
                        )
                );

        producer.publicar(notificacao);

        ArgumentCaptor<SendMessageRequest> captor =
                ArgumentCaptor.forClass(
                        SendMessageRequest.class
                );

        verify(sqsClient).sendMessage(
                captor.capture()
        );

        String mensagem =
                captor.getValue().messageBody();

        assertTrue(
                mensagem.contains("Pentavalente")
        );

        assertTrue(
                mensagem.contains("Pneumocócica 10-valente")
        );

        assertEquals(
                1,
                captor.getAllValues().size()
        );
    }
}