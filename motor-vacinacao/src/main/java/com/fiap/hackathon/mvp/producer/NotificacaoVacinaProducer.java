package com.fiap.hackathon.mvp.producer;

import com.fiap.hackathon.mvp.dto.NotificacaoVacinaPendenteDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import tools.jackson.databind.ObjectMapper;

@Component
public class NotificacaoVacinaProducer {

    private final SqsClient sqsClient;
    private final ObjectMapper objectMapper;
    private final String queueUrl;

    public NotificacaoVacinaProducer(
            SqsClient sqsClient,
            ObjectMapper objectMapper,
            @Value("${aws.sqs.notificacao.queue-url}")
            String queueUrl
    ) {
        this.sqsClient = sqsClient;
        this.objectMapper = objectMapper;
        this.queueUrl = queueUrl;
    }

    public void publicar(
            NotificacaoVacinaPendenteDTO notificacao
    ) {


            String mensagem =
                    objectMapper.writeValueAsString(
                            notificacao
                    );

            SendMessageRequest request =
                     SendMessageRequest.builder()
                            .queueUrl(queueUrl)
                            .messageBody(mensagem)
                            .build();

            sqsClient.sendMessage(request);

//        } catch (JsonProcessingException e) {
//
//            throw new IllegalStateException(
//                    "Erro ao serializar notificação de vacina",
//                    e
//            );
//        }
    }
}