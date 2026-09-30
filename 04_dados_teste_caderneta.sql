-- Dados de teste para GET /api/v1/caderneta?numero_sus=100000000001

INSERT INTO pessoa (numero_sus, consentimento, data_nascimento)
VALUES (100000000001, TRUE, '2020-01-01')
ON CONFLICT (numero_sus) DO UPDATE
SET consentimento = EXCLUDED.consentimento,
    data_nascimento = EXCLUDED.data_nascimento;

INSERT INTO aplicacao_vacina (
    id,
    numero_sus,
    esquema_vacinacao_id,
    data_aplicacao,
    lote,
    agente_saude_id,
    observacao,
    unidade_atendimento_id
)
VALUES
    (900001, 100000000001, 1, '2026-01-10 09:00:00', 'BCG-TESTE-001', 1,
     'Dose BCG para teste da caderneta', 1),
    (900002, 100000000001, 2, '2026-01-10 09:15:00', 'HEPB-TESTE-001', 1,
     'Dose Hepatite B para teste da caderneta', 1)
ON CONFLICT (id) DO UPDATE
SET numero_sus = EXCLUDED.numero_sus,
    esquema_vacinacao_id = EXCLUDED.esquema_vacinacao_id,
    data_aplicacao = EXCLUDED.data_aplicacao,
    lote = EXCLUDED.lote,
    agente_saude_id = EXCLUDED.agente_saude_id,
    observacao = EXCLUDED.observacao,
    unidade_atendimento_id = EXCLUDED.unidade_atendimento_id;
