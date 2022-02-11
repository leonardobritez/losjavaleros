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
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;
  @ManyToOne
  private Usuario duenio;
  @OneToMany
  private List<CaracteristicaCompleta> caracteristicas;
  private String tipo;
  private String nombre;
  private String apodo;
  private Integer edad;
  private Sexo sexo;
  private String descripcion;
  @OneToMany
  private List<Foto> fotos;
  private MascotaEstadoEnum estado;
  
  
  public Mascota() {
  }

public Mascota(final Usuario duenio, final List<CaracteristicaCompleta> caracteristicas, final String chapita, final String tipo, final String nombre, final String apodo, final Integer edad, final Sexo sexo, final String descripcion, final List<Foto> fotos, final MascotaEstadoEnum estado) {
    this.duenio = duenio;
    this.caracteristicas = caracteristicas;
    this.tipo = tipo;
    this.nombre = nombre;
    this.apodo = apodo;
    this.edad = edad;
    this.sexo = sexo;
    this.descripcion = descripcion;
    this.fotos = fotos;
    this.estado = estado;
  }




}
