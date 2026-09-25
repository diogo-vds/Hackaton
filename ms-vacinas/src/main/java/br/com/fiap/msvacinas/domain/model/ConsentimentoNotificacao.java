package br.com.fiap.msvacinas.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;

public record ConsentimentoNotificacao(
     UUID id,
    UUID calendarioVacinalId,
    boolean aceitaNotificacao,
    LocalDateTime dataAtualizacao){

    }
