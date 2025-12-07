package ec.arcbank.micro.interbancario.dto;

import lombok.Data;

@Data
public class HermesResponseDTO {

    private String estado;        
    private String referenciaBCE; 
    private String mensaje;
}
