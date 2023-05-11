package com.lifat.CircuitsCourtsApi.JWTEndPointsProtection;

import io.jsonwebtoken.*;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

//imports pour la génération de la clé sécurisée
import java.lang.reflect.Array;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.SQLOutput;
import java.util.Arrays;
import java.util.Base64;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import java.util.Date;
import java.time.Instant;
import java.util.List;


import static java.time.temporal.ChronoUnit.MINUTES;

@Component
@Data
public class JwtUtil implements CommandLineRunner {

    @Autowired
    private static String secret = "asdfSFS34wfsdfsdfSDSD32dfsddDDerQSNCK34SOWEK5354fdgdf4";

    @Autowired
    private static Key hmacKey;

    public void generateKey() {
        hmacKey = new SecretKeySpec(Base64.getDecoder().decode(secret),
                SignatureAlgorithm.HS256.getJcaName());
    }

    //génere un token jwt en fonction du nom et pwd du client.
    //début de vie du token à sa creation, date d'expiration dans 60 minutes.
    public String generateNewJwtToken(String name, String pwd) {
        String jwt = Jwts.builder()
                .claim("name", name)
                .claim("pwd", pwd)
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(Instant.now().plus(60l, MINUTES)))
                .signWith(hmacKey)
                .compact();
        return jwt;
    }

    //verifie la validité du token.
    //si non valide, leve SignatureException.
    public boolean isValidToken(String token) throws Exception {
        try {
            JwtParser jwtParser = Jwts.parser().setSigningKey(hmacKey);
            Jws<Claims> claims = jwtParser.parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }

    }

    public Jws<Claims> parseJwt(String token) {
        //décode le token grace à la clé puis renvoi les claims décryptés du token.
        Key hmacKey = new SecretKeySpec(Base64.getDecoder().decode(secret), SignatureAlgorithm.HS256.getJcaName());
        Jws<Claims> claims = Jwts.parser()
                .setSigningKey(hmacKey)
                .parseClaimsJws(token);
        return claims;
    }

    @Override
    public void run(String... args) throws Exception {
        generateKey();
        System.out.println(secret);

        System.out.println("token généré avec \nname : Martin\npwd : 1234");
        String token = generateNewJwtToken("Martin", "1234");
        System.out.println(token);
        try {
            System.out.println("isValidToken : " + isValidToken(token));

            Jws<Claims> claims = parseJwt(token);
            System.out.println(claims);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
