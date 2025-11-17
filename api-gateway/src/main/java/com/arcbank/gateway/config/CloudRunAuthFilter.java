package com.arcbank.gateway.config;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Component
public class CloudRunAuthFilter implements GlobalFilter, Ordered {

    private volatile String cachedToken = null;
    private volatile long tokenExpiry = 0;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String requestUrl = exchange.getRequest().getURI().toString();
        
        // Solo agregar auth para servicios privados de Cloud Run
        if (requestUrl.contains(".run.app") && 
            (requestUrl.contains("cbs-service") || 
             requestUrl.contains("bancaweb-service") || 
             requestUrl.contains("cajero-service"))) {
            
            try {
                String token = getIdToken(requestUrl);
                if (token != null && !token.isEmpty()) {
                    exchange = exchange.mutate()
                        .request(r -> r.header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                        .build();
                }
            } catch (Exception e) {
                System.err.println("Error obteniendo token de identidad: " + e.getMessage());
            }
        }
        
        return chain.filter(exchange);
    }

    private String getIdToken(String audience) {
        try {
            // Si el token está en caché y no ha expirado, usarlo
            if (cachedToken != null && System.currentTimeMillis() < tokenExpiry) {
                return cachedToken;
            }

            // Obtener token del metadata server de Google Cloud
            String metadataUrl = "http://metadata.google.internal/computeMetadata/v1/instance/service-accounts/default/identity?audience=" + audience;
            
            URL url = new URL(metadataUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Metadata-Flavor", "Google");
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);

            if (conn.getResponseCode() == 200) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String token = reader.readLine();
                reader.close();
                
                // Cachear el token por 50 minutos (expira en 1 hora)
                cachedToken = token;
                tokenExpiry = System.currentTimeMillis() + (50 * 60 * 1000);
                
                return token;
            }
        } catch (Exception e) {
            System.err.println("Error getting ID token: " + e.getMessage());
        }
        return null;
    }

    @Override
    public int getOrder() {
        return -100; // Ejecutar antes que otros filtros
    }
}
