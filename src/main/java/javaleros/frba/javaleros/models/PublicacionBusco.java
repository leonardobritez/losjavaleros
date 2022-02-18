package javaleros.frba.javaleros.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import java.util.List;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class PublicacionBusco  extends Publicacion{

  private String especieDeLaMascota;
  private String colorDeLaMascota;
  private String sexoDeLaMascota;
  private Double edad;


  @Builder
  public PublicacionBusco(Usuario usuario, List<Foto> fotos, String descripcion, String calle, String altura, String partido, String provincia, EstadoPublicacion estadoPublicacion, Asociacion asociacion, String especieDeLaMascota, String colorDeLaMascota, String sexoDeLaMascota, Double edad) {
    super( usuario, fotos, descripcion, calle, altura, partido, provincia, estadoPublicacion, asociacion);
    this.especieDeLaMascota = especieDeLaMascota;
    this.colorDeLaMascota = colorDeLaMascota;
    this.sexoDeLaMascota = sexoDeLaMascota;
    this.edad = edad;
  }
}