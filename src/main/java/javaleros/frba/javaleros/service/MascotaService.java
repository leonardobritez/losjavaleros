package javaleros.frba.javaleros.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javaleros.frba.javaleros.models.Mascota;
import javaleros.frba.javaleros.repository.FotoRepository;
import javaleros.frba.javaleros.repository.MascotaRepository;

@Service
public class MascotaService {

	@Autowired
	private MascotaRepository mascotaRepository;


	
	public Optional<Mascota> get(Integer id) {
		return mascotaRepository.findById(id);
	}
	

	public Mascota guardarMascota(Mascota mascota) {
		return mascotaRepository.save(mascota);
	}
	

	public boolean existeMascota(Integer id) {
		return mascotaRepository.existsById(id);
	}
	
	


}
