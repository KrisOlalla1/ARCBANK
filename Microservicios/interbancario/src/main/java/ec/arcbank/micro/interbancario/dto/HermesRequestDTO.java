package ec.arcbank.micro.interbancario.dto;

import lombok.Data;

@Data
public class HermesRequestDTO {

    private String referenciaInterna;
    private Double monto;
    private String cuentaDestino;
    private String bancoDestino;
}
