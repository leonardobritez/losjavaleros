package javaleros.frba.javaleros.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.List;

@Getter
@Setter
@Entity
@AllArgsConstructor
public class PublicacionPerdida extends Publicacion {
  private String especieDeLaMascota;
  private String colorDeLaMascota;
  private String sexoDeLaMascota;


  //todo "La asociación asignada a la publicación debe ser la más cercana a la ubicación donde se encontró la mascota."
  //en un principio, lo mas simple seria encontrar una Asociación en el mismo partido y si nó en la misma provincia.

  //Publicacion: sirve para cuando encontras una mascota sin chapita o publicas en adopcion.

  //ORM requiere este constructor vacío.
  public PublicacionPerdida() {

  }

  @Builder
  public PublicacionPerdida( Usuario usuario, List<Foto> fotos, String descripcion, String calle, String altura, String partido, String provincia, EstadoPublicacion estadoPublicacion, Asociacion asociacion, String especieDeLaMascota, String colorDeLaMascota, String sexoDeLaMascota) {
    super( usuario, fotos, descripcion, calle, altura, partido, provincia, estadoPublicacion, asociacion);
    this.especieDeLaMascota = especieDeLaMascota;
    this.colorDeLaMascota = colorDeLaMascota;
    this.sexoDeLaMascota = sexoDeLaMascota;
  }
}