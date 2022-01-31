package javaleros.frba.javaleros.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Getter
@Setter
@Entity
@AllArgsConstructor
public class Publicacion {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;
  private String nombre;
  private String apellido;
  private Long dni;
  @OneToOne
  private Contacto contacto;
  @OneToMany
  private List<Foto> fotos;
  private String descripcion;
  private String especieDeLaMascota;
  private String colorDeLaMascota;
  private String sexoDeLaMascota;
  private String calle;
  private String altura;
  private String partido;
  private String provincia;
  private EstadoPublicacion estadoPublicacion;
  @ManyToOne
  private Asociacion asociacion;

  //todo "La asociación asignada a la publicación debe ser la más cercana a la ubicación donde se encontró la mascota."
  //en un principio, lo mas simple seria encontrar una Asociación en el mismo partido y si nó en la misma provincia.

  //ORM requiere este constructor vacío.
  public Publicacion() {
  }

  public Publicacion(final EstadoPublicacion estadoPublicacion){
    this.estadoPublicacion = estadoPublicacion;
  }

  public Publicacion(final Usuario usuario, final List<Foto> fotos, final String descripcion,
                     final EstadoPublicacion estadoPublicacion) {
    this.nombre = usuario.getNombre();
    this.apellido = usuario.getApellido();
    this.dni = usuario.getDni();
    this.fotos = fotos;
    this.descripcion = descripcion;
    this.estadoPublicacion = estadoPublicacion;
  }

  public void aprobar() {
    this.estadoPublicacion = EstadoPublicacion.APROBADA;
  }

  public void rechazar() {
    this.estadoPublicacion = EstadoPublicacion.RECHAZADA;
  }
}