package com.fiap.hackathon.mvp.service;

import com.fiap.hackathon.mvp.dto.EsquemaVacinacaoDTO;
import com.fiap.hackathon.mvp.dto.HistoricoVacinalDTO;
import com.fiap.hackathon.mvp.dto.PendenciaVacinalDTO;
import com.fiap.hackathon.mvp.dto.PessoaElegivelDTO;
import com.fiap.hackathon.mvp.service.impl.MotorVacinacaoServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MotorVacinacaoServiceImplTest {

    private MotorVacinacaoService motor;

    @BeforeEach
    void setUp() {
        RegraIdadeService regraIdadeService =
                new RegraIdadeService();

        motor = new MotorVacinacaoServiceImpl(
                regraIdadeService
        );
    }

    @Test
    void deveRetornarNenhumaPendenciaQuandoDoseAindaNaoChegouNaIdade() {

        LocalDate dataReferencia =
                LocalDate.of(2026, 2, 15);

        PessoaElegivelDTO pessoa =
                new PessoaElegivelDTO(
                        123456789L,
                        LocalDate.of(2026, 1, 1)
                );

        EsquemaVacinacaoDTO esquema =
                esquema(
                        1L,
                        10L,
                        "Pentavalente",
                        1,
                        "ROTINA",
                        2,
                        "MESES",
                        null,
                        null
                );

        List<PendenciaVacinalDTO> resultado =
                motor.calcularPendencias(
                        pessoa,
                        List.of(),
                        List.of(esquema),
                        dataReferencia
                );

        assertTrue(resultado.isEmpty());
    }

    @Test
    void deveIdentificarDoseComoPendenteQuandoIdadeFoiAtingida() {

        PessoaElegivelDTO pessoa = new PessoaElegivelDTO(
                123456789L,
                LocalDate.of(2026, 1, 1)
        );

        EsquemaVacinacaoDTO esquema = esquema(
                1L,
                10L,
                "Pentavalente",
                1,
                "ROTINA",
                2,
                "MESES",
                null,
                null
        );

        List<PendenciaVacinalDTO> resultado =
                motor.calcularPendencias(
                        pessoa,
                        List.of(),
                        List.of(esquema),
                        LocalDate.of(2026, 3, 1)
                );

        assertEquals(1, resultado.size());

        PendenciaVacinalDTO pendencia =
                resultado.get(0);

        assertEquals(1L, pendencia.esquemaVacinacaoId());
        assertEquals(10L, pendencia.vacinaId());
        assertEquals("Pentavalente", pendencia.vacina());
        assertEquals(1, pendencia.numeroDose());
    }

    @Test
    void naoDeveIdentificarDoseJaAplicadaComoPendente() {

        PessoaElegivelDTO pessoa = new PessoaElegivelDTO(
                123456789L,
                LocalDate.of(2026, 1, 1)
        );

        EsquemaVacinacaoDTO esquema = esquema(
                1L,
                10L,
                "Pentavalente",
                1,
                "ROTINA",
                2,
                "MESES",
                null,
                null
        );

        HistoricoVacinalDTO aplicacao =
                new HistoricoVacinalDTO(
                        123456789L,
                        1L,
                        10L,
                        "Pentavalente",
                        1,
                        "ROTINA",
                        LocalDateTime.of(
                                2026, 3, 5, 10, 0
                        )
                );

        List<PendenciaVacinalDTO> resultado =
                motor.calcularPendencias(
                        pessoa,
                        List.of(aplicacao),
                        List.of(esquema),
                        LocalDate.of(2026, 1, 1)
                );

        assertTrue(resultado.isEmpty());
    }

    @Test
    void deveAguardarIntervaloMinimoAntesDeConsiderarSegundaDosePendente() {

        LocalDate dataReferencia =
                LocalDate.of(2026, 3, 15);

        PessoaElegivelDTO pessoa =
                new PessoaElegivelDTO(
                        123456789L,
                        LocalDate.of(2026, 1, 1)
                );

        EsquemaVacinacaoDTO esquemaDose2 =
                esquemaComIntervalo(
                        2L,
                        10L,
                        "Pentavalente",
                        2,
                        "ROTINA",
                        2,
                        "MESES",
                        60,
                        "DIAS"
                );

        HistoricoVacinalDTO dose1 =
                new HistoricoVacinalDTO(
                        123456789L,
                        1L,
                        10L,
                        "Pentavalente",
                        1,
                        "ROTINA",
                        LocalDateTime.of(
                                2026,
                                2,
                                1,
                                10,
                                0
                        )
                );

        List<PendenciaVacinalDTO> resultado =
                motor.calcularPendencias(
                        pessoa,
                        List.of(dose1),
                        List.of(esquemaDose2),
                        dataReferencia
                );

        assertTrue(resultado.isEmpty());
    }

    @Test
    void deveIdentificarSegundaDoseQuandoIntervaloMinimoFoiRespeitado() {

        LocalDate dataReferencia =
                LocalDate.of(2026, 4, 10);

        PessoaElegivelDTO pessoa =
                new PessoaElegivelDTO(
                        123456789L,
                        LocalDate.of(2026, 1, 1)
                );

        EsquemaVacinacaoDTO esquemaDose2 =
                esquemaComIntervalo(
                        2L,
                        10L,
                        "Pentavalente",
                        2,
                        "ROTINA",
                        2,
                        "MESES",
                        60,
                        "DIAS"
                );

        HistoricoVacinalDTO dose1 =
                new HistoricoVacinalDTO(
                        123456789L,
                        1L,
                        10L,
                        "Pentavalente",
                        1,
                        "ROTINA",
                        LocalDateTime.of(
                                2026,
                                2,
                                1,
                                10,
                                0
                        )
                );

        List<PendenciaVacinalDTO> resultado =
                motor.calcularPendencias(
                        pessoa,
                        List.of(dose1),
                        List.of(esquemaDose2),
                        dataReferencia
                );

        assertEquals(1, resultado.size());
        assertEquals(
                2,
                resultado.get(0).numeroDose()
        );
    }

    @Test
    void naoDeveConsiderarDosePendenteQuandoIdadeMaximaFoiUltrapassada() {

        PessoaElegivelDTO pessoa = new PessoaElegivelDTO(
                123456789L,
                LocalDate.of(2020, 1, 1)
        );

        EsquemaVacinacaoDTO esquema = esquema(
                1L,
                10L,
                "Pentavalente",
                1,
                "ROTINA",
                2,
                "MESES",
                1,
                "ANOS"
        );

        List<PendenciaVacinalDTO> resultado =
                motor.calcularPendencias(
                        pessoa,
                        List.of(),
                        List.of(esquema),
                        LocalDate.of(2020, 1, 1)
                );

        assertTrue(resultado.isEmpty());
    }

    private EsquemaVacinacaoDTO esquema(
            Long id,
            Long vacinaId,
            String vacina,
            Integer numeroDose,
            String tipoDose,
            Integer idadeMinimaValor,
            String idadeMinimaUnidade,
            Integer idadeMaximaValor,
            String idadeMaximaUnidade
    ) {

        return new EsquemaVacinacaoDTO(
                id,
                vacinaId,
                vacina,
                numeroDose,
                tipoDose,

                idadeMinimaValor,
                idadeMinimaUnidade,

                null,
                null,

                idadeMaximaValor,
                idadeMaximaUnidade,

                null,
                null
        );
    }

    private EsquemaVacinacaoDTO esquemaComIntervalo(
            Long id,
            Long vacinaId,
            String vacina,
            Integer numeroDose,
            String tipoDose,
            Integer idadeMinimaValor,
            String idadeMinimaUnidade,
            Integer intervaloValor,
            String intervaloUnidade
    ) {

        return new EsquemaVacinacaoDTO(
                id,
                vacinaId,
                vacina,
                numeroDose,
                tipoDose,

                idadeMinimaValor,
                idadeMinimaUnidade,

                null,
                null,

                null,
                null,

                intervaloValor,
                intervaloUnidade
        );
    }

    @Test
    void deveRetornarTodasAsVacinasPendentesDaPessoa() {

        PessoaElegivelDTO pessoa = new PessoaElegivelDTO(
                123456789L,
                LocalDate.of(2026, 1, 1)
        );

        EsquemaVacinacaoDTO penta = esquema(
                1L,
                10L,
                "Pentavalente",
                1,
                "ROTINA",
                2,
                "MESES",
                null,
                null
        );

        EsquemaVacinacaoDTO pneumo = esquema(
                2L,
                20L,
                "Pneumocócica 10-valente",
                1,
                "ROTINA",
                2,
                "MESES",
                null,
                null
        );

        List<PendenciaVacinalDTO> resultado =
                motor.calcularPendencias(
                        pessoa,
                        List.of(),
                        List.of(penta, pneumo),
                        LocalDate.of(2026, 3, 1)
                );

        assertEquals(2, resultado.size());

        assertTrue(
                resultado.stream()
                        .anyMatch(p -> p.vacinaId().equals(10L))
        );

        assertTrue(
                resultado.stream()
                        .anyMatch(p -> p.vacinaId().equals(20L))
        );
    }

}