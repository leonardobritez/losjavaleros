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

//Entidad, pensado para que se puedan mantener N caracteristicas en base de datos
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class Caracteristica {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;
  private String nombre;  // Ejemplo: "Ingrese el color principal de su mascota" o "Esta castrado? SI/NO"
  private String tipo; // El tipo de respuesta, puede ser texto o booleano
}
