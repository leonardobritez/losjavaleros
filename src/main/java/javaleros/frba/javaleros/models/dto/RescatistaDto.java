package javaleros.frba.javaleros.models.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class RescatistaDto {

    private String nombre;
    private String apellido;
    private String email;
}
