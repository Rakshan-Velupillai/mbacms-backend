package com.mbacms.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtility {


    // private String SECRET_KEY="higiygvdkabshdefqeyhgibdkavdakhiahsbxhcvakqeaq";

    @Value("${secretKey}")
    private String SECRET_KEY;

    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET_KEY));
    }


    public String generateToken(String username){

        Map<String,Object> claims=new HashMap<>();
        return createToken(claims,username);

    }

    //token creation
    private String createToken(Map<String, Object> claims, String username) {

        return Jwts.builder()
                .claims(claims)
                //username
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis()+1 * 60 * 60 * 24 * 1000))
                .signWith(getSecretKey(),Jwts.SIG.HS256)
                .compact();
    }


    //token validation
    public boolean validateToken(String token,String username){
        final String extractedUsername=extractUsername(token);
        return (extractedUsername.equals(username) && !isTokenExpired(token));

    }

    public String extractUsername(String token) {

        return extractClaim(token, Claims::getSubject);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }


}

