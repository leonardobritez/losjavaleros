package javaleros.frba.javaleros.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import javaleros.frba.javaleros.models.Contacto;

public interface ContactoRepository extends JpaRepository<Contacto, Long> {
}
