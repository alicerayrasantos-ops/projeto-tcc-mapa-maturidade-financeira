package br.edu.ucsal.mapa_maturidade_financeira.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {

    // Chave secreta usada para assinar o token.
    // Em um projeto real de produção isso viria de uma variável de ambiente,
    // mas para o TCC vamos manter fixa aqui, de forma simples.
    private final SecretKey chaveSecreta = Keys.hmacShaKeyFor(
            "chave-secreta-super-longa-para-o-tcc-mapa-maturidade-financeira-2026".getBytes()
    );

    private final long tempoExpiracaoMs = 1000 * 60 * 60 * 10; // 10 horas

    public String gerarToken(String email) {
        Date agora = new Date();
        Date expiracao = new Date(agora.getTime() + tempoExpiracaoMs);

        return Jwts.builder()
                .subject(email)
                .issuedAt(agora)
                .expiration(expiracao)
                .signWith(chaveSecreta)
                .compact();
    }

    public String extrairEmail(String token) {
        return extrairTodasClaims(token).getSubject();
    }

    public boolean tokenValido(String token, String email) {
        String emailDoToken = extrairEmail(token);
        return emailDoToken.equals(email) && !tokenExpirado(token);
    }

    private boolean tokenExpirado(String token) {
        return extrairTodasClaims(token).getExpiration().before(new Date());
    }

    private Claims extrairTodasClaims(String token) {
        return Jwts.parser()
                .verifyWith(chaveSecreta)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
