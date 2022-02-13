package javaleros.frba.javaleros.service;

import javaleros.frba.javaleros.exceptions.NotFound;
import javaleros.frba.javaleros.models.Caracteristica;
import javaleros.frba.javaleros.models.CaracteristicaCompleta;
import javaleros.frba.javaleros.models.dto.CaracteristicaCompletaDto;
import javaleros.frba.javaleros.models.dto.CaracteristicaDto;
import javaleros.frba.javaleros.repository.CaracteristicaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CaracteristicaService {

  @Autowired
  private CaracteristicaRepository caracteristicaRepository;

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

  public List<CaracteristicaCompleta> llenarCaracteristicas(List<CaracteristicaCompletaDto> dtos) {

    ArrayList<CaracteristicaCompleta> caracteristicasCompletas = new ArrayList<>();
    for (CaracteristicaCompletaDto dto : dtos) {
      Caracteristica caracteristica = caracteristicaRepository.getById(dto.getIdCaracteristicaOriginal());
      if (caracteristica == null) {
        throw new NotFound("Caracteristica id " + dto.getIdCaracteristicaOriginal() + " not found");
      }

      CaracteristicaCompleta completa = new CaracteristicaCompleta();
      completa.setCaracteristica(caracteristica);
      completa.setRespuesta(dto.getRespuesta());
      caracteristicasCompletas.add(completa);
    }
    return caracteristicasCompletas;

  }
}
