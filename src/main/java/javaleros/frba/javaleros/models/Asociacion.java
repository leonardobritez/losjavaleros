package javaleros.frba.javaleros.models;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Asociacion {
  @Id
  private Long id;

  private String nombre;

  public Asociacion() {
  }

  public Asociacion(final Long id, final String nombre) {
    this.id = id;
    this.nombre = nombre;
  }

}
