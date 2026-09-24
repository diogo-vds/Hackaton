-- ============================================================
-- 02_dados_iniciais.sql
-- CALENDÁRIO NACIONAL DE VACINAÇÃO - CRIANÇA - 2026
-- ============================================================

INSERT INTO calendario_vacinal (
    id,
    nome,
    versao,
    data_inicio_vigencia,
    data_fim_vigencia
)
VALUES (
    1,
    'Calendário Nacional de Vacinação - Criança',
    '2026',
    '2026-01-01',
    NULL
);

INSERT INTO vacina (
    id,
    nome,
    sigla,
    fabricante
)
VALUES
    (1, 'BCG', 'BCG', NULL),
    (2, 'Hepatite B', 'HB', NULL),
    (3, 'Pentavalente', 'Penta', NULL),
    (4, 'Poliomielite inativada', 'VIP', NULL),
    (5, 'Rotavírus humano', 'VORH', NULL),
    (6, 'Pneumocócica 10-valente', 'VPC10', NULL),
    (7, 'Meningocócica C', 'MenC', NULL),
    (8, 'Influenza', 'INF3', NULL),
    (9, 'Febre amarela', 'VFA', NULL),
    (10, 'Meningocócica ACWY', 'MenACWY', NULL),
    (11, 'Tríplice viral', 'SCR', NULL),
    (12, 'Hepatite A', 'HA', NULL),
    (13, 'DTP', 'DTP', NULL),
    (14, 'Varicela', 'VZ', NULL),
    (15, 'HPV4', 'HPV4', NULL);