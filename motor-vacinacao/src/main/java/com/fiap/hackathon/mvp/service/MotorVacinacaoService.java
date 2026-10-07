package com.fiap.hackathon.mvp.service;

import com.fiap.hackathon.mvp.dto.EsquemaVacinacaoDTO;
import com.fiap.hackathon.mvp.dto.HistoricoVacinalDTO;
import com.fiap.hackathon.mvp.dto.PendenciaVacinalDTO;
import com.fiap.hackathon.mvp.dto.PessoaElegivelDTO;

import java.time.LocalDate;
import java.util.List;

public interface MotorVacinacaoService {

    List<PendenciaVacinalDTO> calcularPendencias(
            PessoaElegivelDTO pessoa,
            List<HistoricoVacinalDTO> historico,
            List<EsquemaVacinacaoDTO> esquemas,
            LocalDate dataReferencia
    );
}
