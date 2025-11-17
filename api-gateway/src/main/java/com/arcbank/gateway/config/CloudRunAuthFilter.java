package com.arcbank.gateway.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.IdTokenCredentials;
import com.google.auth.oauth2.IdTokenProvider;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class CloudRunAuthFilter implements GlobalFilter, Ordered {

    private final Map<String, TokenCache> tokenCache = new ConcurrentHashMap<>();
    private volatile GoogleCredentials credentials;

    private static class TokenCache {
        String token;
        long expiry;

        TokenCache(String token, long expiry) {
            this.token = token;
            this.expiry = expiry;
        }

        boolean isValid() {
            return System.currentTimeMillis() < expiry;
        }
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String targetUrl = exchange.getRequest().getURI().toString();
        
        // Detectar si es un servicio privado de Cloud Run
        String serviceUrl = extractServiceUrl(targetUrl);
        if (serviceUrl != null) {
            try {
                String token = getIdToken(serviceUrl);
                if (token != null && !token.isEmpty()) {
                    exchange = exchange.mutate()
                        .request(r -> r.header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                        .build();
                    System.out.println("Token agregado para: " + serviceUrl);
                }
            } catch (Exception e) {
                System.err.println("Error obteniendo token: " + e.getMessage());
                e.printStackTrace();
            }
        }
        
        return chain.filter(exchange);
    }

    private String extractServiceUrl(String fullUrl) {
        if (fullUrl.contains("cbs-service")) {
            return "https://cbs-service-845403368692.us-central1.run.app";
        } else if (fullUrl.contains("bancaweb-service")) {
            return "https://bancaweb-service-845403368692.us-central1.run.app";
        } else if (fullUrl.contains("cajero-service")) {
            return "https://cajero-service-845403368692.us-central1.run.app";
        }
        return null;
    }

    private String getIdToken(String targetAudience) throws Exception {
        // Verificar cache
        TokenCache cached = tokenCache.get(targetAudience);
        if (cached != null && cached.isValid()) {
            return cached.token;
        }

        // Obtener credenciales si no existen
        if (credentials == null) {
            synchronized (this) {
                if (credentials == null) {
                    credentials = GoogleCredentials.getApplicationDefault();
                }
            }
        }

        // Crear IdTokenCredentials
        IdTokenCredentials tokenCredential = IdTokenCredentials.newBuilder()
                .setIdTokenProvider((IdTokenProvider) credentials)
                .setTargetAudience(targetAudience)
                .build();

        tokenCredential.refresh();
        String token = tokenCredential.getIdToken().getTokenValue();
        
        // Cachear por 50 minutos
        tokenCache.put(targetAudience, new TokenCache(token, System.currentTimeMillis() + (50 * 60 * 1000)));
        
        return token;
    }

    @Override
    public int getOrder() {
        return -100;
    }
}
