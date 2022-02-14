package javaleros.frba.javaleros.service;

import javaleros.frba.javaleros.exceptions.NotFound;
import javaleros.frba.javaleros.models.Caracteristica;
import javaleros.frba.javaleros.models.CaracteristicaCompleta;
import javaleros.frba.javaleros.models.Mascota;
import javaleros.frba.javaleros.models.dto.CaracteristicaCompletaDto;
import javaleros.frba.javaleros.models.dto.CaracteristicaDto;
import javaleros.frba.javaleros.repository.CaracteristicaCompletaRepository;
import javaleros.frba.javaleros.repository.CaracteristicaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class CaracteristicaService {

  @Autowired
  private CaracteristicaRepository caracteristicaRepository;

  @Autowired
  private CaracteristicaCompletaRepository completaRepository;

  public void agregarCaracteristica(CaracteristicaDto nuevaCaracteristica) {

    Caracteristica caracteristica = Caracteristica.builder()
        .nombre(nuevaCaracteristica.getNombre())
        .tipo(nuevaCaracteristica.getTipo()).build();

    caracteristicaRepository.save(caracteristica);
  }

  public void borrarCaracteristica(Integer id) {
    caracteristicaRepository.deleteById(id);
  }

  public List<Caracteristica> traerCaracteristicas() {
    return caracteristicaRepository.findAll();
  }

  public List<CaracteristicaCompleta> llenarCaracteristicas(final List<CaracteristicaCompletaDto> dtos) {

    List<CaracteristicaCompleta> caracteristicasCompletas = new ArrayList<>();

    for (CaracteristicaCompletaDto dto : dtos) {
      if (!caracteristicaRepository.existsById(dto.getIdCaracteristicaOriginal())) {
        throw new NotFound("Caracteristica id " + dto.getIdCaracteristicaOriginal() + " not found");
      }
      //traigo de base de datos
      Caracteristica caracteristica = caracteristicaRepository.getById(dto.getIdCaracteristicaOriginal());
      // creo la completa
      CaracteristicaCompleta completa = new CaracteristicaCompleta();
      completa.setId(caracteristica.getId());
      completa.setCaracteristica(caracteristica);
      completa.setRespuesta(dto.getRespuesta());
      caracteristicasCompletas.add(completa);
    }
    return caracteristicasCompletas;
    /*
    //estas características completas pertenecen a esta mascota
    for (CaracteristicaCompleta completa : caracteristicasCompletas) {
      completa.setMascota(mascota);
    }
    completaRepository.saveAll(caracteristicasCompletas);

     */
    //la mascota tiene estas características
    //mascota.setCaracteristicasCompletas(caracteristicasCompletas);
  }
}
