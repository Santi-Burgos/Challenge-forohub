package com.forohubBurgos.forohub.infra.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.forohubBurgos.forohub.domain.usuario.Usuario;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    @Value("${forohub.security.token.secret}")
    private String secret;

    @Value("${forohub.security.token.expiration}")
    private Long expiracion;

    @Value("${forohub.security.token.issuer}")
    private String issuer;

    public String generarToken(Usuario usuario) {
        try {
            var algoritmo = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withIssuer(issuer)
                    .withSubject(usuario.getUsername())
                    .withExpiresAt(generarFechaExpiracion())
                    .sign(algoritmo);
        } catch (JWTCreationException ex){
            throw new RuntimeException("Error al generar el token JWT", ex);
        }
    }

    private Instant generarFechaExpiracion() {
        return LocalDateTime.now().plusHours(expiracion).toInstant(ZoneOffset.of("-05:00"));
    }

    public String getSubject(String tokenJWT) throws JWTVerificationException {
        var algoritmo = Algorithm.HMAC256(secret);
        return JWT.require(algoritmo)
                .withIssuer(issuer)
                .build()
                .verify(tokenJWT)
                .getSubject();
    }
}
