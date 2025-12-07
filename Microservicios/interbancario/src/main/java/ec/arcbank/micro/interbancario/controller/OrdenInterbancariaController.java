package ec.arcbank.micro.interbancario.controller;

import ec.arcbank.micro.interbancario.dto.CrearOrdenRequest;
import ec.arcbank.micro.interbancario.model.OrdenInterbancaria;
import ec.arcbank.micro.interbancario.service.OrdenInterbancariaService;
import ec.arcbank.micro.interbancario.service.ProcesadorAsincronoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/interbancario/v1/orden")
@RequiredArgsConstructor
public class OrdenInterbancariaController {

    private final OrdenInterbancariaService ordenService;
    private final ProcesadorAsincronoService procesadorAsincronoService;

    // Crear una nueva orden interbancaria
    @PostMapping
    public OrdenInterbancaria crearOrden(@RequestBody CrearOrdenRequest request) {
        return ordenService.crearOrden(request);
    }

    // Procesar la orden de manera asíncrona
    @PostMapping("/{idOrden}/procesar")
    public String procesarOrden(@PathVariable Integer idOrden) {

        OrdenInterbancaria orden = ordenService.obtenerOrdenPorId(idOrden);

        if (orden == null) {
            return "Orden no encontrada.";
        }

        procesadorAsincronoService.procesarOrden(orden);

        return "Orden enviada para procesamiento asíncrono.";
    }

    // Obtener información de una orden
    @GetMapping("/{idOrden}")
    public OrdenInterbancaria obtenerOrden(@PathVariable Integer idOrden) {
        return ordenService.obtenerOrdenPorId(idOrden);
    }
}
