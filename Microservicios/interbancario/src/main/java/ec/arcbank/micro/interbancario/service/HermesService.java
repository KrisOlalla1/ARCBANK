package ec.arcbank.micro.interbancario.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ec.arcbank.micro.interbancario.dto.HermesRequestDTO;
import ec.arcbank.micro.interbancario.dto.HermesResponseDTO;
import ec.arcbank.micro.interbancario.model.LogHermes;
import ec.arcbank.micro.interbancario.model.OrdenInterbancaria;
import ec.arcbank.micro.interbancario.repository.LogHermesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class HermesService {

    private final RestTemplate restTemplate;
    private final LogHermesRepository logRepo;
    private final ObjectMapper mapper = new ObjectMapper();

    // NOTA: Cambia este URL cuando tengas el endpoint real de Hermes
    private final String HERMES_URL = "https://api.hermes-financiero.ec/enviarOrden";

    public HermesResponseDTO enviarOrden(OrdenInterbancaria orden) {

        HermesRequestDTO request = new HermesRequestDTO();
        request.setReferenciaInterna(orden.getIdOrden().toString());
        request.setMonto(orden.getMonto());
        request.setCuentaDestino(orden.getCuentaDestino());
        request.setBancoDestino(orden.getBancoDestinoId().toString());

        HermesResponseDTO response = restTemplate.postForObject(
                HERMES_URL,
                request,
                HermesResponseDTO.class
        );

        registrarLog(orden, request, response);

        return response;
    }

    private void registrarLog(OrdenInterbancaria orden, HermesRequestDTO req, HermesResponseDTO resp) {

        try {
            LogHermes log = new LogHermes();
            log.setIdOrden(orden.getIdOrden());
            log.setMensajeEnviado(mapper.writeValueAsString(req));
            log.setRespuestaRecibida(mapper.writeValueAsString(resp));

            logRepo.save(log);

        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error registrando log Hermes", e);
        }
    }
}
