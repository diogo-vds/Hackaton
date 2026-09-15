package org.prontuario.infrastructure.adapter.out.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Value;
import org.prontuario.application.port.out.EventPublisherPort;
import org.prontuario.domain.model.Consulta;
import org.prontuario.domain.model.Vacina;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

/**
 * Publica eventos em filas SQS consumidas pelas Lambdas:
 *  - lembrete-vacina-queue    → Lambda Scheduler "Lembrete de vacinas"
 *  - lembrete-consulta-queue  → Lambda "Lembrete de Consultas"
 */
@Component
public class SqsEventPublisherAdapter implements EventPublisherPort {

    private final SqsClient sqsClient;
    private final ObjectMapper objectMapper;
    private final String filaVacinaUrl;
    private final String filaConsultaUrl;

    public SqsEventPublisherAdapter(
            SqsClient sqsClient,
            ObjectMapper objectMapper,
            @Value("${aws.sqs.lembrete-vacina-url}") String filaVacinaUrl,
            @Value("${aws.sqs.lembrete-consulta-url}") String filaConsultaUrl) {
        this.sqsClient = sqsClient;
        this.objectMapper = objectMapper;
        this.filaVacinaUrl = filaVacinaUrl;
        this.filaConsultaUrl = filaConsultaUrl;
    }

    @Override
    public void publicarLembreteVacina(UUID pacienteId, Vacina vacina) {
        // A Lambda usa `proximaDose` para agendar o lembrete
        Map<String, Object> payload = Map.of(
                "tipo", "LEMBRETE_VACINA",
                "pacienteId", pacienteId.toString(),
                "vacinaNome", vacina.nome(),
                "proximaDose", vacina.proximaDose() != null ? vacina.proximaDose().toString() : "",
                "publicadoEm", Instant.now().toString()
        );
        enviar(filaVacinaUrl, payload);
    }

    @Override
    public void publicarLembreteConsulta(UUID pacienteId, Consulta consulta) {
        // A Lambda lê "dataHora" e "antecedenciaHoras" para agendar o push
        Map<String, Object> payload = Map.of(
                "tipo", "LEMBRETE_CONSULTA",
                "pacienteId", pacienteId.toString(),
                "especialidade", consulta.especialidade(),
                "medicoNome", consulta.medicoNome(),
                "dataHora", consulta.dataHora().toString(),
                "antecedenciaHoras", 24,  // regra padrão: 24h antes
                "publicadoEm", Instant.now().toString()
        );
        enviar(filaConsultaUrl, payload);
    }

    private void enviar(String queueUrl, Map<String, Object> payload) {
        try {
            String body = objectMapper.writeValueAsString(payload);
            sqsClient.sendMessage(SendMessageRequest.builder()
                    .queueUrl(queueUrl)
                    .messageBody(body)
                    .build());
        } catch (Exception ex) {
            throw new RuntimeException("Erro ao publicar evento SQS", ex);
        }
    }
}
