package javaleros.frba.javaleros.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Entity
public class Mascota implements Serializable{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
@Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;
	/*
	 * @ManyToOne private Usuario duenio;
	 */
  @OneToMany
  private List<CaracteristicaCompleta> caracteristicas;
  private String chapita;
  private String tipo;
  private String nombre;
  private String apodo;
  private Integer edad;
  private SexoEnum sexo;
  private String descripcion;
  @OneToMany
  private List<Foto> fotos;
  private MascotaEstadoEnum estado;
  
  
  public Mascota() {
  }

public Mascota(final List<CaracteristicaCompleta> caracteristicas, final String chapita, final String tipo, final String nombre, final String apodo, final Integer edad, final SexoEnum sexo, final String descripcion, final List<Foto> fotos, final MascotaEstadoEnum estado) {
	/* this.duenio = duenio; */
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


public List<CaracteristicaCompleta> getCaracteristicas() {
	return caracteristicas;
}

public void setCaracteristicas(List<CaracteristicaCompleta> caracteristicas) {
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

public List<Foto> getFotos() {
	return fotos;
}

public void setFotos(List<Foto> fotos) {
	this.fotos = fotos;
}

public MascotaEstadoEnum getEstado() {
	return estado;
}

public void setEstado(MascotaEstadoEnum estado) {
	this.estado = estado;
}



}
