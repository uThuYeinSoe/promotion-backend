package com.promotion.config;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JWTService {


    private static final String SECRET_KEY="3cf0971ed7ccee938067d3d3c54241fb3cf142a72599db425141506406a7566ccf0fb7865dfbc7f1dbf4f8cabd40e4634cacea56f6c512bcd159585d8587782f2c27c1d5735bb2f08ee792f370901b1c95823744dbdb19f444dcd9a2b6880b7246a6bf24005c8fdb55c257929e93acc5b4248f09dd414f800a3dd64bdd27bdf0dd2f368940cc340fd94759afad41b2485a25bb14ccb212099773b3ceefafe04c98fb0be7c9a2f3b7dc8f93c514425a4c95176bc0f81b04932895d4bf7a7542ab4200ac7cb9f74bf8b3da6b20df1c9842c3f96df4d89433e982986f7cbbd829eadfb0a269aa0c437e4cd42e4cd03fde6260f5f3305ec9f4e2ae9a03464d9c5b24";

    public String extractUsername(String token) {
        return extractClaim(token,Claims::getSubject);
    }

    private Claims extractAllClaims(String token){
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private <T> T extractClaim(String token , Function<Claims , T> claimsResolver){
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(UserDetails userDetails){
        return generateToken(new HashMap<>(),userDetails);
    }


    public String generateToken(
            Map<String , Object> extraClaims,
            UserDetails userDetails
    ){
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24))
                .signWith(getSignInKey(), SignatureAlgorithm. HS256)
                .compact();
    }

    public boolean isTokenValid(String token, UserDetails userDetails){
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token){
        return extractClaim(token , Claims::getExpiration);
    }

    private Key getSignInKey(){
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);

    }

}
