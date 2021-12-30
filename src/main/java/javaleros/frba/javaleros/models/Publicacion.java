package javaleros.frba.javaleros.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
  private Double coordenadasX;
  private Double coordenadasY;

  private EstadoPublicacion estadoPublicacion;

  //ORM requiere este constructor vacío.
  public Publicacion() {
  }

  public Publicacion(final Usuario usuario, final List<Foto> fotos, final String descripcion, final Double coordenadasX,
                     final Double coordenadasY, final EstadoPublicacion estadoPublicacion) {
    this.nombre = usuario.getNombre();
    this.apellido = usuario.getApellido();
    this.dni = usuario.getDni();
    this.fotos = fotos;
    this.descripcion = descripcion;
    this.coordenadasX = coordenadasX;
    this.coordenadasY = coordenadasY;
    this.estadoPublicacion = estadoPublicacion;
  }

  public void aprobar() {
    this.estadoPublicacion = EstadoPublicacion.APROBADA;
  }

  public void rechazar() {
    this.estadoPublicacion = EstadoPublicacion.RECHAZADA;
  }
}