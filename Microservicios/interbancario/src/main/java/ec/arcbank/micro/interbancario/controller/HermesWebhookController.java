package ec.arcbank.micro.interbancario.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/interbancario/v1/webhook")
public class HermesWebhookController {

    @PostMapping("/respuesta")
    public String recibirRespuesta(@RequestBody String payload) {
        return "Webhook recibido";
    }
}
