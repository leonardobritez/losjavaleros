package javaleros.frba.javaleros.models.dto;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FotoDto {
	
	  @Id
	  @GeneratedValue(strategy = GenerationType.IDENTITY)
	  private Long id;

	  //todo implementar tipo de dato para archivo
	  private String foto;

	  //todo aca formatear la foto!!!

}
