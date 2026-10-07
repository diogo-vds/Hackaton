package com.fiap.hackathon.mvp.service;

import com.fiap.hackathon.mvp.dto.IdadeDTO;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;

@Service
public class CalculadoraIdadeService {

    public IdadeDTO calcular(
            LocalDate dataNascimento,
            LocalDate dataReferencia
    ) {

        if (dataNascimento.isAfter(dataReferencia)) {
            throw new IllegalArgumentException(
                    "Data de nascimento não pode ser posterior à data de referência"
            );
        }

        Period periodo = Period.between(
                dataNascimento,
                dataReferencia
        );

        return new IdadeDTO(
                periodo.getYears(),
                periodo.getMonths(),
                periodo.getDays()
        );
    }
}