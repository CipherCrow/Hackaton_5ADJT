package br.com.hackaton.priorizasus.infrastructure.security;

import br.com.hackaton.priorizasus.enums.PermissaoEnum;
import br.com.hackaton.priorizasus.exception.TokenInvalidoException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expirationMs;

    public String gerarToken(String username, PermissaoEnum role) {
        Date agora = new Date();
        Date validade = new Date(agora.getTime() + expirationMs);

        return Jwts.builder()
                .setSubject(username)
                .claim("role", role.name())
                .setIssuedAt(agora)
                .setExpiration(validade)
                .signWith(Keys.hmacShaKeyFor(secret.getBytes()), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validarToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(secret.getBytes()))
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            throw new TokenInvalidoException("Token expirado.");
        } catch (UnsupportedJwtException e) {
            throw new TokenInvalidoException("Token JWT não suportado.");
        } catch (MalformedJwtException e) {
            throw new TokenInvalidoException("Token JWT malformado.");
        } catch (SignatureException e) {
            throw new TokenInvalidoException("Assinatura JWT inválida.");
        } catch (IllegalArgumentException e) {
            throw new TokenInvalidoException("Token JWT inválido ou ausente.");
        }
    }
    public String getUsernameFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secret.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}