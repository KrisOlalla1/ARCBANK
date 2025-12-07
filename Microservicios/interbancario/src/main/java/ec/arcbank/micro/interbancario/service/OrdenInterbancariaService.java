package ec.arcbank.micro.interbancario.service;

import ec.arcbank.micro.interbancario.dto.CrearOrdenRequest;
import ec.arcbank.micro.interbancario.model.OrdenInterbancaria;
import ec.arcbank.micro.interbancario.repository.OrdenInterbancariaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrdenInterbancariaService {

    private final OrdenInterbancariaRepository ordenRepo;

    public OrdenInterbancaria crearOrden(CrearOrdenRequest request) {

        OrdenInterbancaria orden = new OrdenInterbancaria();
        orden.setIdCuentaOrigen(request.getIdCuentaOrigen());
        orden.setBancoDestinoId(request.getBancoDestinoId());
        orden.setCuentaDestino(request.getCuentaDestino());
        orden.setNombreBeneficiario(request.getNombreBeneficiario());
        orden.setIdentificacionBeneficiario(request.getIdentificacionBeneficiario());
        orden.setMonto(request.getMonto());

        return ordenRepo.save(orden);
    }

    public OrdenInterbancaria obtenerOrdenPorId(Integer idOrden) {
        return ordenRepo.findById(idOrden).orElse(null);
    }
}
