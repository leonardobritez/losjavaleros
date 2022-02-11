package javaleros.frba.javaleros.repository;

import javaleros.frba.javaleros.models.Mascota;
import javaleros.frba.javaleros.models.Usuario;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MascotaRepository extends JpaRepository<Mascota,Integer> {
	
	    
}
