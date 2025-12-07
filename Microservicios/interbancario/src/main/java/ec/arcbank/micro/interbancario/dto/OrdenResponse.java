package ec.arcbank.micro.interbancario.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrdenResponse {

    private Integer idOrden;
    private String estado;
    private String mensaje;
}
