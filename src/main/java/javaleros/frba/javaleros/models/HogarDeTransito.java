package javaleros.frba.javaleros.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class HogarDeTransito {
  private String nombre;
  private String direccion;
  private boolean aceptaPerros;
  private boolean aceptaGatos;
  private boolean tienePatio;
  private int lugaresDisponibles;

}
