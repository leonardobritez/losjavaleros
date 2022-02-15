package javaleros.frba.javaleros.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
import javax.persistence.OneToMany;
import java.io.Serializable;

//Caracteristica guardada dentro de una Mascota.
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class CaracteristicaCompleta implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;
  @ManyToOne(cascade = CascadeType.MERGE)
  private Caracteristica caracteristica;
  @ManyToOne(cascade = CascadeType.MERGE)
  @JsonIgnore
  private Mascota mascota;
  private String respuesta;
  
  
  private String getNombre() {
	  return this.caracteristica.getNombre();
  }



  
  
  
  
  
  
  
}
