package javaleros.frba.javaleros.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import javaleros.frba.javaleros.models.Asociacion;

public interface AsociacionRepository extends JpaRepository<Asociacion, Long> {
}