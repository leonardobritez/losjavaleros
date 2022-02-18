package javaleros.frba.javaleros.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
@NoArgsConstructor
public class PublicacionAdopcion extends Publicacion {
  @Builder
  public PublicacionAdopcion(Usuario usuario, List<Foto> fotos, String descripcion, String calle, String altura, String partido, String provincia, EstadoPublicacion estadoPublicacion, Asociacion asociacion,
                             Mascota mascota,
                             List<Pregunta> preguntas) {
    super(usuario, fotos, descripcion, calle, altura, partido, provincia, estadoPublicacion, asociacion);
    this.mascota = mascota;
    this.preguntas = preguntas;
  }

  @OneToOne
  private Mascota mascota;


  @OneToMany
  private List<Pregunta> preguntas;



}