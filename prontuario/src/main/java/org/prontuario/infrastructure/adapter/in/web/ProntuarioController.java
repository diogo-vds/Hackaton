package org.prontuario.infrastructure.adapter.in.web;

import jakarta.validation.Valid;
import org.prontuario.application.port.in.AgendarConsultaUseCase;
import org.prontuario.application.port.in.AtualizarProntuarioUseCase;
import org.prontuario.application.port.in.ConsultarProntuarioUseCase;
import org.prontuario.application.port.in.RegistrarVacinacaoUseCase;
import org.prontuario.domain.model.Consulta;
import org.prontuario.domain.model.Prontuario;
import org.prontuario.domain.model.Vacina;
import org.prontuario.infrastructure.adapter.in.dto.AtualizarProntuarioRequest;
import org.prontuario.infrastructure.adapter.in.dto.ConsultaRequest;
import org.prontuario.infrastructure.adapter.in.dto.ProntuarioResponse;
import org.prontuario.infrastructure.adapter.in.dto.VacinaRequest;
import org.prontuario.infrastructure.security.UsuarioAutenticado;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/prontuarios")
public class ProntuarioController {

    private final ConsultarProntuarioUseCase consultarUC;
    private final AtualizarProntuarioUseCase atualizarUC;
    private final RegistrarVacinacaoUseCase vacinacaoUC;
    private final AgendarConsultaUseCase consultaUC;

    public ProntuarioController(ConsultarProntuarioUseCase consultarUC,
                                AtualizarProntuarioUseCase atualizarUC,
                                RegistrarVacinacaoUseCase vacinacaoUC,
                                AgendarConsultaUseCase consultaUC) {
        this.consultarUC = consultarUC;
        this.atualizarUC = atualizarUC;
        this.vacinacaoUC = vacinacaoUC;
        this.consultaUC = consultaUC;
    }

    /** GET — Médico, Paciente (responsável) ou Enfermeira visualizam prontuário */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('MEDICO','PACIENTE','ENFERMEIRA')")
    public ResponseEntity<ProntuarioResponse> buscar(
            @PathVariable UUID id,
            @AuthenticationPrincipal UsuarioAutenticado user) {

        Prontuario p = consultarUC.consultar(id, user.id(), user.tipo());
        return ResponseEntity.ok(toResponse(p));
    }

    /** GET — busca o prontuário do paciente logado (pelo JWT) */
    @GetMapping("/me")
    @PreAuthorize("hasRole('PACIENTE')")
    public ResponseEntity<ProntuarioResponse> meusDados(
            @AuthenticationPrincipal UsuarioAutenticado user) {

        Prontuario p = consultarUC.buscarPorPacienteLogado(user.id());
        return ResponseEntity.ok(toResponse(p));
    }

    /** POST — atualiza observações do prontuário (Médico/Enfermeira) */
    @PostMapping("/{id}")
    @PreAuthorize("hasAnyRole('MEDICO','ENFERMEIRA')")
    public ResponseEntity<ProntuarioResponse> atualizar(
            @PathVariable UUID id,
            @Valid @RequestBody AtualizarProntuarioRequest req,
            @AuthenticationPrincipal UsuarioAutenticado user) {

        Prontuario p = atualizarUC.atualizar(id, req.observacoes(), user.id(), user.tipo());
        return ResponseEntity.ok(toResponse(p));
    }

    /** POST — endpoint para integração/registro de vacinação */
    @PostMapping("/{id}/vacinacao")
    @PreAuthorize("hasAnyRole('MEDICO','ENFERMEIRA')")
    public ResponseEntity<ProntuarioResponse> registrarVacina(
            @PathVariable UUID id,
            @Valid @RequestBody VacinaRequest req) {

        Vacina v = new Vacina(UUID.randomUUID(), req.nome(), req.lote(),
                req.fabricante(), req.dataAplicacao(), req.proximaDose(),
                req.profissionalAplicador());

        Prontuario p = vacinacaoUC.registrar(id, v);
        return ResponseEntity.created(URI.create("/api/v1/prontuarios/" + id)).body(toResponse(p));
    }

    /** POST — agendar consulta (dispara lembrete via SQS) */
    @PostMapping("/{id}/consultas")
    @PreAuthorize("hasAnyRole('MEDICO','ENFERMEIRA')")
    public ResponseEntity<ProntuarioResponse> agendarConsulta(
            @PathVariable UUID id,
            @Valid @RequestBody ConsultaRequest req) {

        Consulta c = new Consulta(UUID.randomUUID(), req.especialidade(),
                req.medicoNome(), req.dataHora(), req.observacoes());

        Prontuario p = consultaUC.agendar(id, c);
        return ResponseEntity.ok(toResponse(p));
    }

    private ProntuarioResponse toResponse(Prontuario p) {
        return new ProntuarioResponse(p.getId(), p.getPacienteId(),
                p.getNomePaciente(), p.getObservacoes(),
                p.getVacinas(), p.getConsultas(), p.getAtualizadoEm());
    }
}
