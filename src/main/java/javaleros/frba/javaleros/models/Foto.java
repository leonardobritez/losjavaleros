package javaleros.frba.javaleros.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;

//Pensamos guardar el binario de la foto
@Entity
@Getter
@Setter
public class Foto {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  //todo implementar tipo de dato para archivo
  private String foto;

  //todo aca formatear la foto!!!

}