package javaleros.frba.javaleros.models.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javaleros.frba.javaleros.models.MascotaEstadoEnum;
import javaleros.frba.javaleros.models.SexoEnum;
import javaleros.frba.javaleros.models.Usuario;


public class MascotaDto {

	private int id;
	private UsuarioDto duenio;
	private List<CaracteristicaCompletaDto> caracteristicas = new ArrayList<CaracteristicaCompletaDto>();
	private String chapita;
	private String tipo;
	private String nombre;
	private String apodo;
	private Integer edad;
	private SexoEnum sexo;
	private String descripcion;
	private List<FotoDto> fotos;
	private MascotaEstadoEnum estado;
	
	
	
	public MascotaDto(int id, UsuarioDto duenio, List<CaracteristicaCompletaDto> caracteristicas, String chapita,
			String tipo, String nombre, String apodo, Integer edad, SexoEnum sexo, String descripcion,
			List<FotoDto> fotos, MascotaEstadoEnum estado) {
		super();
		this.id = id;
		this.duenio = duenio;
		this.caracteristicas = caracteristicas;
		this.chapita = chapita;
		this.tipo = tipo;
		this.nombre = nombre;
		this.apodo = apodo;
		this.edad = edad;
		this.sexo = sexo;
		this.descripcion = descripcion;
		this.fotos = fotos;
		this.estado = estado;
	}



	public int getId() {
		return id;
	}



	public void setId(int id) {
		this.id = id;
	}



	public UsuarioDto getDuenio() {
		return duenio;
	}



	public void setDuenio(UsuarioDto duenio) {
		this.duenio = duenio;
	}



	public List<CaracteristicaCompletaDto> getCaracteristicas() {
		return caracteristicas;
	}



	public void setCaracteristicas(List<CaracteristicaCompletaDto> caracteristicas) {
		this.caracteristicas = caracteristicas;
	}



	public String getChapita() {
		return chapita;
	}



	public void setChapita(String chapita) {
		this.chapita = chapita;
	}



	public String getTipo() {
		return tipo;
	}



	public void setTipo(String tipo) {
		this.tipo = tipo;
	}



	public String getNombre() {
		return nombre;
	}



	public void setNombre(String nombre) {
		this.nombre = nombre;
	}



	public String getApodo() {
		return apodo;
	}



	public void setApodo(String apodo) {
		this.apodo = apodo;
	}



	public Integer getEdad() {
		return edad;
	}



	public void setEdad(Integer edad) {
		this.edad = edad;
	}



	public SexoEnum getSexo() {
		return sexo;
	}



	public void setSexo(SexoEnum sexo) {
		this.sexo = sexo;
	}



	public String getDescripcion() {
		return descripcion;
	}



	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}



	public List<FotoDto> getFotos() {
		return fotos;
	}



	public void setFotos(List<FotoDto> fotos) {
		this.fotos = fotos;
	}



	public MascotaEstadoEnum getEstado() {
		return estado;
	}



	public void setEstado(MascotaEstadoEnum estado) {
		this.estado = estado;
	}
	
	
	
	
	

	



}

