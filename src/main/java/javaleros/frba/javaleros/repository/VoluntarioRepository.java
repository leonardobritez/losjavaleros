package javaleros.frba.javaleros.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javaleros.frba.javaleros.models.Voluntario;

@Repository
public interface VoluntarioRepository  extends JpaRepository<Voluntario, Integer>{
}
