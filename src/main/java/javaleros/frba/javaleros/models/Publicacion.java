package javaleros.frba.javaleros.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Getter
@Setter
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@AllArgsConstructor
public class Publicacion {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;
  @ManyToOne
  private Usuario usuario;
  @OneToMany
  private List<Foto> fotos;
  private String descripcion;

  private String calle;
  private String altura;
  private String partido;
  private String provincia;
  private EstadoPublicacion estadoPublicacion;

  @ManyToOne
  private Asociacion asociacion;

  //todo "La asociación asignada a la publicación debe ser la más cercana a la ubicación donde se encontró la mascota."
  //en un principio, lo mas simple seria encontrar una Asociación en el mismo partido y si nó en la misma provincia.

  //Publicacion: sirve para cuando encontras una mascota sin chapita o publicas en adopcion.

  //ORM requiere este constructor vacío.
  public Publicacion() {
  }

  public Publicacion(final EstadoPublicacion estadoPublicacion){
    this.estadoPublicacion = estadoPublicacion;
  }

  public Publicacion(Usuario usuario, List<Foto> fotos, String descripcion, String calle, String altura, String partido, String provincia, EstadoPublicacion estadoPublicacion, Asociacion asociacion) {
    this.usuario = usuario;
    this.fotos = fotos;
    this.descripcion = descripcion;
    this.calle = calle;
    this.altura = altura;
    this.partido = partido;
    this.provincia = provincia;
    this.estadoPublicacion = estadoPublicacion;
    this.asociacion = asociacion;
  }

  public Publicacion(final Usuario usuario, final List<Foto> fotos, final String descripcion,
                     final EstadoPublicacion estadoPublicacion) {
    this.usuario = usuario;
    this.descripcion = descripcion;
    this.fotos = fotos;
    this.estadoPublicacion = estadoPublicacion;

  }


  public void aprobar() {
    this.estadoPublicacion = EstadoPublicacion.APROBADA;
  }

  public void rechazar() {
    this.estadoPublicacion = EstadoPublicacion.RECHAZADA;
  }
}