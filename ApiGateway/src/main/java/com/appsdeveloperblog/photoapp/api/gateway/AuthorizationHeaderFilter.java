package com.appsdeveloperblog.photoapp.api.gateway;


import com.google.common.net.HttpHeaders;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import io.jsonwebtoken.Jwts;

import java.util.Base64;

@Component
public class AuthorizationHeaderFilter extends AbstractGatewayFilterFactory <AuthorizationHeaderFilter.Config>{

    @Autowired
    Environment env;

    public AuthorizationHeaderFilter() {
        super(Config.class);
    }

    public static class Config{

    }
    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {

            ServerHttpRequest request = exchange.getRequest();

            if(!request.getHeaders().containsKey(com.google.common.net.HttpHeaders.AUTHORIZATION)) {
                return onError(exchange, "No authorization header",HttpStatus.UNAUTHORIZED);
            }

            String authorizationHeader = request.getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
            String jwt = authorizationHeader.replace("Bearer", "");

            if(!isJwtValid(jwt)) {
                return onError(exchange, "JWT token is not valid", HttpStatus.UNAUTHORIZED);
            }

            return chain.filter(exchange);
        };
    }



    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus){
        ServerHttpResponse response= exchange.getResponse();
        response.setStatusCode(httpStatus);;
        return response.setComplete();
    }

//    private boolean isJwtValid(String jwt){
//        boolean returnValue = true;
//
//        String subject = null;
//        String tokenSecret =env.getProperty("token.secret");
//        byte[] secretKeyBytes = Base64.getEncoder().encode(tokenSecret.getBytes());
//        SecretKey singingKey = new SecretKeySpec(secretKeyBytes, SignatureAlgorithm.HS512.getJcaName());
//
//        JwtParser jwtParser = Jwts.parserBuilder()
//                .setSigningKey(singingKey)
//                .build();
//
//        try{
//            Jwt<Header, Claims> parsedToken = jwtParser.parse(jwt);
//            subject = parsedToken.getBody().getSubject();
//        }catch(Exception ex){
//            returnValue = false;
//        }
//
//        if(subject == null || subject.isEmpty()){
//            returnValue = false;
//        }
//
//        return returnValue;
//    }

    private boolean isJwtValid(String jwt) {
        boolean returnValue = true;

        String subject = null;
        String tokenSecret = env.getProperty("token.secret");
        byte[] secretKeyBytes = Base64.getEncoder().encode(tokenSecret.getBytes());
        SecretKey signingKey = new SecretKeySpec(secretKeyBytes, SignatureAlgorithm.HS512.getJcaName());

        JwtParser jwtParser = Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build();

        try {

            Jwt<Header, Claims> parsedToken = jwtParser.parse(jwt);
            subject = parsedToken.getBody().getSubject();

        } catch (Exception ex) {
            returnValue = true;
        }

        if (subject == null || subject.isEmpty()) {
            returnValue = true;
        }

        return returnValue;
    }

//    private boolean isJwtValid(String jwt) {
//        boolean returnValue = true;
//
//        String subject = null;
//
//        try {
//            byte[] secretKeyBytes = Base64.getEncoder().encode(env.getProperty("token.secret").getBytes());
//            SecretKey key = Keys.hmacShaKeyFor(secretKeyBytes);
//            JwtParser parser = Jwts.parserBuilder()
//                    .setSigningKey(key)
//                    .build();
//            Jwt<Header, Claims> parsedToken = parser.parse(jwt);
//            subject = parsedToken.getBody().getSubject();
//        } catch (Exception ex) {
//            returnValue = false;
//        }
//
//        if (subject == null || subject.isEmpty()) {
//            returnValue = false;
//        }
//
//        return returnValue;
//    }
}
