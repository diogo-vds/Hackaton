-- ============================================================
-- 03_esquemas_vacinacao.sql
-- SISTEMA DE CONTROLE DE VACINAÇÃO
--
-- Calendário Nacional de Vacinação - Criança - 2026
-- Ministério da Saúde / PNI
--
-- Calendário:
--     calendario_vacinal_id = 1
--
-- Unidade de idade:
--     MESES
--     ANOS
--     DIAS
--
-- Tipo de dose:
--     DOSE
--     REFORCO
--
-- Escopo MVP:
--     Rotina infantil
--     Sem regras específicas de grupos especiais
--     Sem COVID-19
--     Sem doses de bloqueio/surto
-- ============================================================


-- ============================================================
-- 1. BCG
-- ============================================================
-- Dose única.
-- Recomendada ao nascer.
--
-- Para o MVP, não estamos modelando as regras especiais
-- relacionadas a peso, imunodeficiência, exposição ao HIV etc.
-- ============================================================

INSERT INTO esquema_vacinacao (
    id,
    calendario_vacinal_id,
    vacina_id,
    numero_dose,
    tipo_dose,
    idade_minima_valor,
    idade_minima_unidade,
    idade_recomendada_valor,
    idade_recomendada_unidade,
    idade_maxima_valor,
    idade_maxima_unidade,
    intervalo_minimo_valor,
    intervalo_minimo_unidade
)
VALUES (
    1,
    1,
    1,
    1,
    'DOSE',
    0,
    'DIAS',
    0,
    'DIAS',
    NULL,
    NULL,
    NULL,
    NULL
);


-- ============================================================
-- 2. HEPATITE B
-- ============================================================
-- Dose única ao nascer na rotina infantil.
--
-- A situação de crianças que não receberam a dose ao nascer
-- e passam a utilizar a vacina penta será tratada em uma
-- evolução do motor de regras.
-- ============================================================

INSERT INTO esquema_vacinacao (
    id,
    calendario_vacinal_id,
    vacina_id,
    numero_dose,
    tipo_dose,
    idade_minima_valor,
    idade_minima_unidade,
    idade_recomendada_valor,
    idade_recomendada_unidade,
    idade_maxima_valor,
    idade_maxima_unidade,
    intervalo_minimo_valor,
    intervalo_minimo_unidade
)
VALUES (
    2,
    1,
    2,
    1,
    'DOSE',
    0,
    'DIAS',
    0,
    'DIAS',
    30,
    'DIAS',
    NULL,
    NULL
);


-- ============================================================
-- 3. PENTAVALENTE
-- ============================================================
-- 3 doses:
--   1ª - 2 meses
--   2ª - 4 meses
--   3ª - 6 meses
--
-- Intervalo recomendado: 60 dias
-- Intervalo mínimo: 30 dias
-- ============================================================

INSERT INTO esquema_vacinacao (
    id, calendario_vacinal_id, vacina_id,
    numero_dose, tipo_dose,
    idade_minima_valor, idade_minima_unidade,
    idade_recomendada_valor, idade_recomendada_unidade,
    idade_maxima_valor, idade_maxima_unidade,
    intervalo_minimo_valor, intervalo_minimo_unidade
)
VALUES
(
    3, 1, 3,
    1, 'DOSE',
    6, 'SEMANAS',
    2, 'MESES',
    6, 'ANOS',
    NULL, NULL,
    NULL, NULL
),
(
    4, 1, 3,
    2, 'DOSE',
    NULL, NULL,
    4, 'MESES',
    6, 'ANOS',
    30, 'DIAS'
),
(
    5, 1, 3,
    3, 'DOSE',
    6, 'MESES',
    6, 'MESES',
    6, 'ANOS',
    30, 'DIAS'
);


-- ============================================================
-- 4. POLIOMIELITE INATIVADA - VIP
-- ============================================================
-- 3 doses:
--   1ª - 2 meses
--   2ª - 4 meses
--   3ª - 6 meses
--
-- Reforço:
--   15 meses
--
-- Intervalo recomendado entre doses básicas: 60 dias
-- Intervalo mínimo: 30 dias
--
-- Reforço:
--   recomendado: 9 meses após a 3ª dose
--   mínimo: 6 meses após a 3ª dose
-- ============================================================

INSERT INTO esquema_vacinacao (
    id, calendario_vacinal_id, vacina_id,
    numero_dose, tipo_dose,
    idade_minima_valor, idade_minima_unidade,
    idade_recomendada_valor, idade_recomendada_unidade,
    idade_maxima_valor, idade_maxima_unidade,
    intervalo_minimo_valor, intervalo_minimo_unidade
)
VALUES
(
    6, 1, 4,
    1, 'DOSE',
    6, 'SEMANAS',
    2, 'MESES',
    4, 'ANOS',
    30, 'DIAS'
),
(
    7, 1, 4,
    2, 'DOSE',
    NULL, NULL,
    4, 'MESES',
    4, 'ANOS',
    30, 'DIAS'
),
(
    8, 1, 4,
    3, 'DOSE',
    6, 'MESES',
    6, 'MESES',
    4, 'ANOS',
    30, 'DIAS'
),
(
    9, 1, 4,
    1, 'REFORCO',
    12, 'MESES',
    15, 'MESES',
    4, 'ANOS',
    6, 'MESES'
);


-- ============================================================
-- 5. ROTAVÍRUS
-- ============================================================
-- 2 doses:
--   1ª - 2 meses
--   2ª - 4 meses
--
-- 1ª dose:
--   mínima: 1 mês e 15 dias
--   máxima: 11 meses e 29 dias
--
-- 2ª dose:
--   mínima: 3 meses e 15 dias
--   máxima: 23 meses e 29 dias
--
-- Intervalo recomendado: 60 dias
-- Intervalo mínimo: 30 dias
-- ============================================================

INSERT INTO esquema_vacinacao (
    id, calendario_vacinal_id, vacina_id,
    numero_dose, tipo_dose,
    idade_minima_valor, idade_minima_unidade,
    idade_recomendada_valor, idade_recomendada_unidade,
    idade_maxima_valor, idade_maxima_unidade,
    intervalo_minimo_valor, intervalo_minimo_unidade
)
VALUES
(
    10, 1, 5,
    1, 'DOSE',
    45, 'DIAS',
    2, 'MESES',
    11, 'MESES',
    60, 'DIAS'
),
(
    11, 1, 5,
    2, 'DOSE',
    105, 'DIAS',
    4, 'MESES',
    23, 'MESES',
    30, 'DIAS'
);


-- ============================================================
-- 6. PNEUMOCÓCICA 10-VALENTE - VPC10
-- ============================================================
-- 2 doses:
--   1ª - 2 meses
--   2ª - 4 meses
--
-- Reforço:
--   12 meses
--
-- Intervalo recomendado: 60 dias
-- Intervalo mínimo: 30 dias
--
-- O calendário também possui regras de atualização para
-- crianças que iniciaram o esquema mais tarde. Essas regras
-- serão tratadas em uma evolução do motor.
-- ============================================================

INSERT INTO esquema_vacinacao (
    id, calendario_vacinal_id, vacina_id,
    numero_dose, tipo_dose,
    idade_minima_valor, idade_minima_unidade,
    idade_recomendada_valor, idade_recomendada_unidade,
    idade_maxima_valor, idade_maxima_unidade,
    intervalo_minimo_valor, intervalo_minimo_unidade
)
VALUES
(
    12, 1, 6,
    1, 'DOSE',
    0,
    'DIAS',
    2, 'MESES',
    4, 'ANOS',
    30, 'DIAS'
),
(
    13, 1, 6,
    2, 'DOSE',
    NULL, NULL,
    4, 'MESES',
    4, 'ANOS',
    30, 'DIAS'
),
(
    14, 1, 6,
    1, 'REFORCO',
    10, 'MESES',
    12, 'MESES',
    4, 'ANOS',
    60, 'DIAS'
);


-- ============================================================
-- 7. MENINGOCÓCICA C - MenC
-- ============================================================
-- 2 doses:
--   1ª - 3 meses
--   2ª - 5 meses
--
-- Reforço aos 12 meses é realizado com MenACWY.
--
-- Intervalo recomendado: 60 dias
-- Intervalo mínimo: 30 dias
-- ============================================================

INSERT INTO esquema_vacinacao (
    id, calendario_vacinal_id, vacina_id,
    numero_dose, tipo_dose,
    idade_minima_valor, idade_minima_unidade,
    idade_recomendada_valor, idade_recomendada_unidade,
    idade_maxima_valor, idade_maxima_unidade,
    intervalo_minimo_valor, intervalo_minimo_unidade
)
VALUES
(
    15, 1, 7,
    1, 'DOSE',
    3, 'MESES',
    3, 'MESES',
    4, 'ANOS',
    30, 'DIAS'
),
(
    16, 1, 7,
    2, 'DOSE',
    NULL, NULL,
    5, 'MESES',
    4, 'ANOS',
    30, 'DIAS'
);


-- ============================================================
-- 8. FEBRE AMARELA - VFA
-- ============================================================
-- Dose:
--   9 meses
--
-- Reforço:
--   4 anos
--
-- Para o MVP, não estamos modelando a dose excepcional entre
-- 6 e 8 meses.
-- ============================================================

INSERT INTO esquema_vacinacao (
    id, calendario_vacinal_id, vacina_id,
    numero_dose, tipo_dose,
    idade_minima_valor, idade_minima_unidade,
    idade_recomendada_valor, idade_recomendada_unidade,
    idade_maxima_valor, idade_maxima_unidade,
    intervalo_minimo_valor, intervalo_minimo_unidade
)
VALUES
(
    17, 1, 9,
    1, 'DOSE',
    9, 'MESES',
    9, 'MESES',
    4, 'ANOS',
    NULL, NULL
),
(
    18, 1, 9,
    1, 'REFORCO',
    4, 'ANOS',
    4, 'ANOS',
    4, 'ANOS',
    30, 'DIAS'
);


-- ============================================================
-- 9. MENINGOCÓCICA ACWY
-- ============================================================
-- Incluída no MVP porque o calendário 2026 determina que o
-- reforço da série iniciada com MenC seja feito aos 12 meses
-- utilizando a vacina MenACWY.
--
-- 1 dose:
--   12 meses
-- ============================================================

INSERT INTO esquema_vacinacao (
    id, calendario_vacinal_id, vacina_id,
    numero_dose, tipo_dose,
    idade_minima_valor, idade_minima_unidade,
    idade_recomendada_valor, idade_recomendada_unidade,
    idade_maxima_valor, idade_maxima_unidade,
    intervalo_minimo_valor, intervalo_minimo_unidade
)
VALUES
(
    19, 1, 10,
    1, 'REFORCO',
    12, 'MESES',
    12, 'MESES',
    4, 'ANOS',
    60, 'DIAS'
);


-- ============================================================
-- 10. TRÍPLICE VIRAL - SCR
-- ============================================================
-- 2 doses:
--   1ª - 12 meses
--   2ª - 15 meses
--
-- Intervalo recomendado: 30 dias
-- Intervalo mínimo: 15 dias
-- ============================================================

INSERT INTO esquema_vacinacao (
    id, calendario_vacinal_id, vacina_id,
    numero_dose, tipo_dose,
    idade_minima_valor, idade_minima_unidade,
    idade_recomendada_valor, idade_recomendada_unidade,
    idade_maxima_valor, idade_maxima_unidade,
    intervalo_minimo_valor, intervalo_minimo_unidade
)
VALUES
(
    20, 1, 11,
    1, 'DOSE',
    12, 'MESES',
    12, 'MESES',
    9, 'ANOS',
    30, 'DIAS'
),
(
    21, 1, 11,
    2, 'DOSE',
    12, 'MESES',
    15, 'MESES',
    9, 'ANOS',
    15, 'DIAS'
);


-- ============================================================
-- 11. HEPATITE A
-- ============================================================
-- Dose única:
--   15 meses
-- ============================================================

INSERT INTO esquema_vacinacao (
    id, calendario_vacinal_id, vacina_id,
    numero_dose, tipo_dose,
    idade_minima_valor, idade_minima_unidade,
    idade_recomendada_valor, idade_recomendada_unidade,
    idade_maxima_valor, idade_maxima_unidade,
    intervalo_minimo_valor, intervalo_minimo_unidade
)
VALUES
(
    22, 1, 12,
    1, 'DOSE',
    15, 'MESES',
    15, 'MESES',
    4, 'ANOS',
    NULL, NULL
);


-- ============================================================
-- 12. DTP
-- ============================================================
-- A DTP é utilizada como reforço após o esquema básico da
-- pentavalente.
--
-- 1º reforço:
--   15 meses
--
-- 2º reforço:
--   4 anos
--
-- Intervalo mínimo entre o esquema básico e o 1º reforço:
--   6 meses
--
-- Intervalo mínimo entre os reforços:
--   6 meses
--
-- Para o MVP, o motor considerará as doses de reforço como
-- dependentes do esquema básico da Penta.
-- ============================================================

INSERT INTO esquema_vacinacao (
    id, calendario_vacinal_id, vacina_id,
    numero_dose, tipo_dose,
    idade_minima_valor, idade_minima_unidade,
    idade_recomendada_valor, idade_recomendada_unidade,
    idade_maxima_valor, idade_maxima_unidade,
    intervalo_minimo_valor, intervalo_minimo_unidade
)
VALUES
(
    23, 1, 13,
    1, 'REFORCO',
    12, 'MESES',
    15, 'MESES',
    6, 'ANOS',
    6, 'MESES'
),
(
    24, 1, 13,
    2, 'REFORCO',
    3, 'ANOS',
    4, 'ANOS',
    6, 'ANOS',
    6, 'MESES'
);


-- ============================================================
-- 13. VARICELA
-- ============================================================
-- 2 doses:
--   1ª - 15 meses
--   2ª - 4 anos
--
-- Para crianças até 12 anos, intervalo recomendado de 3 meses.
--
-- No MVP, será considerada a rotina infantil.
-- ============================================================

INSERT INTO esquema_vacinacao (
    id, calendario_vacinal_id, vacina_id,
    numero_dose, tipo_dose,
    idade_minima_valor, idade_minima_unidade,
    idade_recomendada_valor, idade_recomendada_unidade,
    idade_maxima_valor, idade_maxima_unidade,
    intervalo_minimo_valor, intervalo_minimo_unidade
)
VALUES
(
    25, 1, 14,
    1, 'DOSE',
    15, 'MESES',
    15, 'MESES',
    6, 'ANOS',
    NULL, NULL
),
(
    26, 1, 14,
    2, 'DOSE',
    18, 'MESES',
    4, 'ANOS',
    6, 'ANOS',
    3, 'MESES'
);


-- ============================================================
-- 14. HPV4
-- ============================================================
-- Dose única:
--   9 anos
--
-- A recomendação de rotina é uma dose entre 9 e 14 anos,
-- 11 meses e 29 dias.
--
-- Para o MVP infantil, consideramos a dose aos 9 anos.
-- ============================================================

INSERT INTO esquema_vacinacao (
    id, calendario_vacinal_id, vacina_id,
    numero_dose, tipo_dose,
    idade_minima_valor, idade_minima_unidade,
    idade_recomendada_valor, idade_recomendada_unidade,
    idade_maxima_valor, idade_maxima_unidade,
    intervalo_minimo_valor, intervalo_minimo_unidade
)
VALUES
(
    27, 1, 15,
    1, 'DOSE',
    9, 'ANOS',
    9, 'ANOS',
    14, 'ANOS',
    NULL, NULL
);


-- ============================================================
-- FIM DO SCRIPT
-- ============================================================