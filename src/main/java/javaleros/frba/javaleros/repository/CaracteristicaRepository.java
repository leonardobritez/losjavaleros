package javaleros.frba.javaleros.repository;

import javaleros.frba.javaleros.models.Caracteristica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CaracteristicaRepository extends JpaRepository<Caracteristica, Integer> {
}
