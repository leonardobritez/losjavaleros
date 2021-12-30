package javaleros.frba.javaleros.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
//@Table(name = "voluntario", uniqueConstraints = {@UniqueConstraint(columnNames = {"usuario_id", "asociacion_id"})})
public class Voluntario {
  @Id
  @Column(name = "id", nullable = false)
  @GeneratedValue
  private Long id;

  @OneToOne
  private Asociacion asociacion;

  @OneToOne
  private Usuario usuario;

  public Voluntario(final Usuario usuario, final Asociacion asociacion) {
    this.usuario = usuario;
    this.asociacion = asociacion;
  }

  public Voluntario() {
  }
}
