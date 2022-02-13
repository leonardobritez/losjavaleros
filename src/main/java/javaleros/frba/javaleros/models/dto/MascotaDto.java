package javaleros.frba.javaleros.models.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javaleros.frba.javaleros.models.CaracteristicaCompleta;
import javaleros.frba.javaleros.models.MascotaEstadoEnum;
import javaleros.frba.javaleros.models.SexoEnum;
import javaleros.frba.javaleros.models.Usuario;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;


@Builder
@Getter
@EqualsAndHashCode
public class MascotaDto {

	private List<CaracteristicaCompletaDto> caracteristicas = new ArrayList<CaracteristicaCompletaDto>();
	private String tipo;
	private String nombre;
	private String apodo;
	private Integer edad;
	private SexoEnum sexo;
	private String descripcion;
	private List<FotoDto> fotos;
	private MascotaEstadoEnum estado;


}

