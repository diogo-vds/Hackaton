package org.prontuario.application.service;

import org.prontuario.application.port.in.AgendarConsultaUseCase;
import org.prontuario.application.port.in.AtualizarProntuarioUseCase;
import org.prontuario.application.port.in.ConsultarProntuarioUseCase;
import org.prontuario.application.port.in.RegistrarVacinacaoUseCase;
import org.prontuario.application.port.out.EventPublisherPort;
import org.prontuario.application.port.out.ProntuarioRepositoryPort;
import org.prontuario.domain.exception.AcessoNegadoException;
import org.prontuario.domain.exception.ProntuarioNotFoundException;
import org.prontuario.domain.model.Consulta;
import org.prontuario.domain.model.Prontuario;
import org.prontuario.domain.model.TipoUsuario;
import org.prontuario.domain.model.Vacina;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class ProntuarioService implements
        ConsultarProntuarioUseCase,
        AtualizarProntuarioUseCase,
        RegistrarVacinacaoUseCase,
        AgendarConsultaUseCase {

    private final ProntuarioRepositoryPort repository;
    private final EventPublisherPort eventPublisher;

    public ProntuarioService(ProntuarioRepositoryPort repository,
                             EventPublisherPort eventPublisher) {
        this.repository = repository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    @Transactional(readOnly = true)
    public Prontuario consultar(UUID prontuarioId, UUID solicitanteId, TipoUsuario tipo) {
        Prontuario p = repository.buscarPorId(prontuarioId)
                .orElseThrow(() -> new ProntuarioNotFoundException("Prontuário não encontrado"));

        if (!p.podeSerAcessadoPor(solicitanteId, tipo)) {
            throw new AcessoNegadoException("Usuário sem permissão para este prontuário");
        }
        return p;
    }

    @Override
    @Transactional(readOnly = true)
    public Prontuario buscarPorPacienteLogado(UUID pacienteId) {
        return repository.buscarPorPacienteId(pacienteId)
                .orElseThrow(() -> new ProntuarioNotFoundException(
                        "Nenhum prontuário encontrado para o paciente logado"));
    }

    @Override
    public Prontuario atualizar(UUID prontuarioId, String observacoes,
                                UUID solicitanteId, TipoUsuario tipo) {
        if (tipo == TipoUsuario.PACIENTE) {
            throw new AcessoNegadoException("Paciente não pode alterar o prontuário");
        }
        Prontuario p = repository.buscarPorId(prontuarioId)
                .orElseThrow(() -> new ProntuarioNotFoundException("Prontuário não encontrado"));
        p.atualizarObservacoes(observacoes);
        return repository.salvar(p);
    }

    @Override
    public Prontuario registrar(UUID prontuarioId, Vacina vacina) {
        Prontuario p = repository.buscarPorId(prontuarioId)
                .orElseThrow(() -> new ProntuarioNotFoundException("Prontuário não encontrado"));
        p.adicionarVacina(vacina);
        Prontuario salvo = repository.salvar(p);

        // publica para Lambda "Lembrete de vacinas"
        eventPublisher.publicarLembreteVacina(p.getPacienteId(), vacina);
        return salvo;
    }

    @Override
    public Prontuario agendar(UUID prontuarioId, Consulta consulta) {
        Prontuario p = repository.buscarPorId(prontuarioId)
                .orElseThrow(() -> new ProntuarioNotFoundException("Prontuário não encontrado"));
        p.adicionarConsulta(consulta);
        Prontuario salvo = repository.salvar(p);

        // publica para Lambda "Lembrete de Consultas"
        eventPublisher.publicarLembreteConsulta(p.getPacienteId(), consulta);
        return salvo;
    }



}
