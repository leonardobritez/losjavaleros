package javaleros.frba.javaleros.repository;

import javaleros.frba.javaleros.models.Mascota;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MascotaRepository extends JpaRepository<Mascota, Long> {
}
