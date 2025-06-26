package com.zerogravitysolutions.core.security;

import com.zerogravitysolutions.core.utilities.UserContext;
import com.zerogravitysolutions.core.utilities.UserContextHolder;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtAuthConverter implements Converter<Jwt, JwtAuthenticationToken> {

    @Override
    public JwtAuthenticationToken convert(final Jwt jwt) {
        final String userId = jwt.getSubject();
        final String userEmail = jwt.getClaim("email");

        final UserContext userContext = new UserContext();
        userContext.setUserId(userId);
        userContext.setUserEmail(userEmail);
        UserContextHolder.setContext(userContext);

        final Collection<GrantedAuthority> authorities = extractAuthorities(jwt);
        return new JwtAuthenticationToken(jwt, authorities);
    }

    private Collection<GrantedAuthority> extractAuthorities(final Jwt jwt) {
        final List<String> rolesClaim = jwt.getClaimAsStringList("roles");
        if (rolesClaim != null) {
            return rolesClaim.stream()
                    .map(role -> new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()))
                    .collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }
}
