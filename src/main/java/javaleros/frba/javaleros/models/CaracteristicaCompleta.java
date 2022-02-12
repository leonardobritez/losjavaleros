package javaleros.frba.javaleros.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
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
  @ManyToOne
  private Caracteristica caracteristica;
  @ManyToOne
  private Mascota mascota;
  private String respuesta;
  
  
  private String getNombre() {
	  return this.caracteristica.getNombre();
  }



  
  
  
  
  
  
  
}
