package com.arcbank.cajero.service;

import com.arcbank.cajero.dto.TransaccionCoreRequest;
import com.arcbank.cajero.dto.TransaccionCoreResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CoreSyncService {

    @Value("${core.api.base}")
    private String coreApiBase;

    private final RestTemplate restTemplate;

    public CoreSyncService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public TransaccionCoreResponse enviar(TransaccionCoreRequest req) {
        String url = coreApiBase + "/api/core/transacciones";
        ResponseEntity<TransaccionCoreResponse> resp = restTemplate.postForEntity(url, req, TransaccionCoreResponse.class);
        return resp.getBody();
    }
}
