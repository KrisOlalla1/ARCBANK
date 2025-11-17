package com.arcbank.gateway.config;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/** Lightweight tracing filter to ensure every request carries a correlation id. */
@Component
public class RequestCorrelationFilter implements GlobalFilter, Ordered {

    public static final String CORRELATION_ID_HEADER = "X-Correlation-Id";
    private static final Logger log = LoggerFactory.getLogger(RequestCorrelationFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        HttpHeaders headers = exchange.getRequest().getHeaders();
        String existingCorrelationId = headers.getFirst(CORRELATION_ID_HEADER);
        boolean hasIncomingCorrelation = existingCorrelationId != null && !existingCorrelationId.isBlank();
        String correlationId = hasIncomingCorrelation ? existingCorrelationId : UUID.randomUUID().toString();

        ServerWebExchange exchangeToUse = hasIncomingCorrelation
            ? exchange
            : exchange.mutate()
                .request(builder -> builder.header(CORRELATION_ID_HEADER, correlationId))
                .build();

        final String finalCorrelationId = correlationId;
        final ServerWebExchange finalExchange = exchangeToUse;
        finalExchange.getResponse().beforeCommit(() -> Mono.fromRunnable(() ->
            finalExchange.getResponse().getHeaders().set(CORRELATION_ID_HEADER, finalCorrelationId)
        ));

        return chain.filter(finalExchange)
            .doOnSubscribe(sub -> log.debug("Routing {} {} with correlation {}", finalExchange.getRequest().getMethod(),
                finalExchange.getRequest().getURI(), finalCorrelationId));
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
