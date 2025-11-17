package com.arcbank.cajero.controller;

import com.arcbank.cajero.accounts.model.TransaccionCajero;
import com.arcbank.cajero.dto.OperacionCajeroRequest;
import com.arcbank.cajero.repository.TransaccionCajeroRepository;
import com.arcbank.cajero.service.TransaccionCajeroService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cajero")
public class TransaccionCajeroController {

    private final TransaccionCajeroService service;
    private final TransaccionCajeroRepository repository;

    public TransaccionCajeroController(TransaccionCajeroService service, TransaccionCajeroRepository repository) {
        this.service = service;
        this.repository = repository;
    }

    @PostMapping("/depositar")
    public TransaccionCajero depositar(@RequestBody OperacionCajeroRequest request) {
        return service.depositar(request);
    }

    @PostMapping("/retirar")
    public TransaccionCajero retirar(@RequestBody OperacionCajeroRequest request) {
        request.setDescripcion(request.getDescripcion());
        return service.retirar(request);
    }

    @GetMapping("/transacciones")
    public java.util.List<TransaccionCajero> listar(@RequestParam String numeroCuenta) {
        return repository.findByNumeroCuentaOrderByFechaHoraDesc(numeroCuenta);
    }
}
