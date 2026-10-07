package com.fiap.hackathon.mvp.service;

import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class RegraIdadeService {

    public boolean estaDentroDaJanela(
            LocalDate dataNascimento,
            LocalDate dataReferencia,
            Integer idadeMinimaValor,
            String idadeMinimaUnidade,
            Integer idadeMaximaValor,
            String idadeMaximaUnidade
    ) {

        LocalDate dataMinima = calcularDataLimite(
                dataNascimento,
                idadeMinimaValor,
                idadeMinimaUnidade
        );

        LocalDate dataMaxima = calcularDataLimite(
                dataNascimento,
                idadeMaximaValor,
                idadeMaximaUnidade
        );

        boolean atingiuMinimo =
                dataMinima == null
                        || !dataReferencia.isBefore(dataMinima);

        boolean naoPassouMaximo =
                dataMaxima == null
                        || !dataReferencia.isAfter(dataMaxima);

        return atingiuMinimo && naoPassouMaximo;
    }

    public boolean respeitaIntervaloMinimo(
            LocalDate dataUltimaAplicacao,
            LocalDate dataReferencia,
            Integer intervaloValor,
            String intervaloUnidade
    ) {

        if (dataUltimaAplicacao == null
                || intervaloValor == null
                || intervaloUnidade == null) {
            return true;
        }

        LocalDate dataPermitida = calcularDataLimite(
                dataUltimaAplicacao,
                intervaloValor,
                intervaloUnidade
        );

        return !dataReferencia.isBefore(dataPermitida);
    }

    private LocalDate calcularDataLimite(
            LocalDate dataBase,
            Integer valor,
            String unidade
    ) {

        if (valor == null || unidade == null) {
            return null;
        }

        return switch (unidade.toUpperCase()) {
            case "DIAS" -> dataBase.plusDays(valor);
            case "SEMANAS" -> dataBase.plusWeeks(valor);
            case "MESES" -> dataBase.plusMonths(valor);
            case "ANOS" -> dataBase.plusYears(valor);
            default -> throw new IllegalArgumentException(
                    "Unidade não suportada: " + unidade
            );
        };
    }
}