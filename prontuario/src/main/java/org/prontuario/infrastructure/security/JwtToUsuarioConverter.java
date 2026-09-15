package org.prontuario.infrastructure.security;

import org.prontuario.domain.model.TipoUsuario;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class JwtToUsuarioConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        UUID userId = UUID.fromString(jwt.getSubject());
        String role = jwt.getClaimAsString("role"); // MEDICO | PACIENTE | ENFERMEIRA

        UsuarioAutenticado user = new UsuarioAutenticado(userId, jwt.getClaimAsString("name"),
                TipoUsuario.valueOf(role));

        var authorities = List.of(new org.springframework.security.core.authority.SimpleGrantedAuthority("ROLE_" + role));
        return new JwtAuthenticationToken(jwt, authorities, user.username()) {
            @Override
            public Object getPrincipal() { return user; }
        };
    }
}
