package org.prontuario.infrastructure.security;


import org.prontuario.domain.model.TipoUsuario;

import java.util.UUID;

public record UsuarioAutenticado(UUID id, String username, TipoUsuario tipo) {}
