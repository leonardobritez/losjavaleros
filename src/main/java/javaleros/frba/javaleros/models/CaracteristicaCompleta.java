package javaleros.frba.javaleros.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import java.io.Serializable;

//Una caracteristica llena de info sobre una mascota.
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class CaracteristicaCompleta implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @ManyToOne
  private Caracteristica caracteristica;
  private String respuesta;

  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  private Mascota mascota;

}
