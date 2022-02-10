package javaleros.frba.javaleros.service;

import javaleros.frba.javaleros.models.Caracteristica;
import javaleros.frba.javaleros.models.dto.CaracteristicaDto;
import javaleros.frba.javaleros.repository.CaracteristicaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class CaracteristicaService {

    @Autowired
    private CaracteristicaRepository caracteristicaRepository;

    public void agregarCaracteristica(@RequestBody CaracteristicaDto nuevaCaracteristica) {

        Caracteristica caracteristica = Caracteristica.builder()
                .nombre(nuevaCaracteristica.getNombre())
                .tipo(nuevaCaracteristica.getTipo()).build();

        caracteristicaRepository.save(caracteristica);
    }

}
