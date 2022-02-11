package javaleros.frba.javaleros.service;

import javaleros.frba.javaleros.models.Mascota;
import javaleros.frba.javaleros.repository.MascotaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MascotaService {

    @Autowired
    private MascotaRepository  mascotaRepository;

    public Mascota get(Integer id) {

       return mascotaRepository.getById(id);
    }

    public Mascota guardarMascota(Mascota mascota) {

        return mascotaRepository.save(mascota);
    }


    public boolean existeMascota(Integer id) {

        return mascotaRepository.existsById(id);
    }

}
