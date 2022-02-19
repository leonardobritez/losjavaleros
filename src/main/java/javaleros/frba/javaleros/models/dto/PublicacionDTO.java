package javaleros.frba.javaleros.models.dto;

import javaleros.frba.javaleros.models.Foto;
import javaleros.frba.javaleros.models.Pregunta;
import lombok.Data;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
@Data
public class PublicacionDTO {
    private List<FotoDto> fotos;
    private String descripcion;
    private String calle;
    private String altura;
    private String partido;
    private String provincia;
    private String especieDeLaMascota;
    private String colorDeLaMascota;
    private String sexoDeLaMascota;
    private Double edad;
    private List<Pregunta> preguntas;

}
