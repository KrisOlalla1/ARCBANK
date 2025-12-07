package ec.arcbank.micro.interbancario.dto;

import lombok.Data;

@Data
public class CrearOrdenRequest {

    private Integer idCuentaOrigen;
    private Integer bancoDestinoId;
    private String cuentaDestino;
    private String nombreBeneficiario;
    private String identificacionBeneficiario;
    private Double monto;
}
