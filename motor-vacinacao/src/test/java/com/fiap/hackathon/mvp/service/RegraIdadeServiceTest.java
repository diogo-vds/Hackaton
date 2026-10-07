package com.fiap.hackathon.mvp.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class RegraIdadeServiceTest {

    private RegraIdadeService regra;

    @BeforeEach
    void setUp() {
        regra = new RegraIdadeService();
    }

    @Test
    void devePermitirExatamenteNaIdadeMinima() {

        LocalDate nascimento =
                LocalDate.of(2026, 1, 1);

        LocalDate referencia =
                LocalDate.of(2026, 3, 1);

        boolean resultado =
                regra.estaDentroDaJanela(
                        nascimento,
                        referencia,
                        2,
                        "MESES",
                        null,
                        null
                );

        assertTrue(resultado);
    }

    @Test
    void deveNegarUmDiaAntesDaIdadeMinima() {

        LocalDate nascimento =
                LocalDate.of(2026, 1, 1);

        LocalDate referencia =
                LocalDate.of(2026, 2, 28);

        boolean resultado =
                regra.estaDentroDaJanela(
                        nascimento,
                        referencia,
                        2,
                        "MESES",
                        null,
                        null
                );

        assertFalse(resultado);
    }

    @Test
    void devePermitirExatamenteNaIdadeMaxima() {

        LocalDate nascimento =
                LocalDate.of(2025, 1, 1);

        LocalDate referencia =
                LocalDate.of(2026, 1, 1);

        boolean resultado =
                regra.estaDentroDaJanela(
                        nascimento,
                        referencia,
                        null,
                        null,
                        1,
                        "ANOS"
                );

        assertTrue(resultado);
    }

    @Test
    void deveNegarUmDiaDepoisDaIdadeMaxima() {

        LocalDate nascimento =
                LocalDate.of(2025, 1, 1);

        LocalDate referencia =
                LocalDate.of(2026, 1, 2);

        boolean resultado =
                regra.estaDentroDaJanela(
                        nascimento,
                        referencia,
                        null,
                        null,
                        1,
                        "ANOS"
                );

        assertFalse(resultado);
    }

    @Test
    void devePermitirQuandoEstaEntreIdadeMinimaEMaxima() {

        LocalDate nascimento =
                LocalDate.of(2026, 1, 1);

        LocalDate referencia =
                LocalDate.of(2026, 6, 1);

        boolean resultado =
                regra.estaDentroDaJanela(
                        nascimento,
                        referencia,
                        2,
                        "MESES",
                        1,
                        "ANOS"
                );

        assertTrue(resultado);
    }

    @Test
    void deveRetornarTrueQuandoNaoExisteIdadeMinimaNemMaxima() {

        LocalDate nascimento =
                LocalDate.of(2026, 1, 1);

        LocalDate referencia =
                LocalDate.of(2026, 9, 1);

        boolean resultado =
                regra.estaDentroDaJanela(
                        nascimento,
                        referencia,
                        null,
                        null,
                        null,
                        null
                );

        assertTrue(resultado);
    }

    @Test
    void devePermitirExatamenteNoIntervaloMinimo() {

        LocalDate ultimaAplicacao =
                LocalDate.of(2026, 1, 1);

        LocalDate referencia =
                LocalDate.of(2026, 3, 2);

        boolean resultado =
                regra.respeitaIntervaloMinimo(
                        ultimaAplicacao,
                        referencia,
                        60,
                        "DIAS"
                );

        assertTrue(resultado);
    }

    @Test
    void deveNegarUmDiaAntesDoIntervaloMinimo() {

        LocalDate ultimaAplicacao =
                LocalDate.of(2026, 1, 1);

        LocalDate referencia =
                LocalDate.of(2026, 3, 1);

        boolean resultado =
                regra.respeitaIntervaloMinimo(
                        ultimaAplicacao,
                        referencia,
                        60,
                        "DIAS"
                );

        assertFalse(resultado);
    }

    @Test
    void devePermitirQuandoNaoExisteIntervaloMinimo() {

        boolean resultado =
                regra.respeitaIntervaloMinimo(
                        LocalDate.of(2026, 1, 1),
                        LocalDate.of(2026, 1, 2),
                        null,
                        null
                );

        assertTrue(resultado);
    }

    @Test
    void deveLancarExcecaoParaUnidadeDeIdadeNaoSuportada() {

        assertThrows(
                IllegalArgumentException.class,
                () -> regra.estaDentroDaJanela(
                        LocalDate.of(2026, 1, 1),
                        LocalDate.of(2026, 3, 1),
                        2,
                        "decadas",
                        null,
                        null
                )
        );
    }

    @Test
    void deveCalcularJanelaUsandoSemanas() {
        LocalDate nascimento = LocalDate.of(2025, 1, 1);
        LocalDate dataReferencia = LocalDate.of(2025, 1, 15);

        boolean resultado = regra.estaDentroDaJanela(
                nascimento,
                dataReferencia,
                2,
                "SEMANAS",
                4,
                "SEMANAS"
        );

        assertTrue(resultado);
    }

}