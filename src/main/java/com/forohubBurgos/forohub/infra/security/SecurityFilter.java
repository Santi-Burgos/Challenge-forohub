package com.forohubBurgos.forohub.infra.security;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.forohubBurgos.forohub.domain.ValidacionException;
import com.forohubBurgos.forohub.domain.usuario.UsuarioRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private TokenService tokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var tokenJWT = recuperarToken(request);

            try {
                if(tokenJWT != null) {
                    var subject = tokenService.getSubject(tokenJWT);
                    var usuario = repository.findByCorreoElectronicoAndActivoTrue(subject);
                    if(usuario == null) {
                        throw new ValidacionException("El usuario no existe, fue eliminado o está inactivo.");
                    }
                    var authentication = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
                filterChain.doFilter(request, response);
            } catch (TokenExpiredException e) {
                prepararCabecera401(response);
                response.getWriter().write("{\"campo\": \"Token\", \"mensaje\": \"El token ha expirado.\"}");
            } catch (JWTVerificationException e) {
                prepararCabecera401(response);
                response.getWriter().write("{\"campo\": \"Token\", \"mensaje\": \"El token es inválido.\"}");
            } catch (ValidacionException e) {
                prepararCabecera401(response);
                response.getWriter().write("{\"error\": \"Error de validación\", \"mensaje\": \"" + e.getMessage() + "\"}");
            }
    }

    private String recuperarToken(HttpServletRequest request) {
        var authorizationHeader = request.getHeader("Authorization");
        if(authorizationHeader != null) {
            return authorizationHeader.replace("Bearer ", "");
        }
        return null;
    }

    private void prepararCabecera401(HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
    }
}
