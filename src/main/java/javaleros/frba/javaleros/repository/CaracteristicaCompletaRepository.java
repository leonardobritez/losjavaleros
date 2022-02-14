package javaleros.frba.javaleros.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import javaleros.frba.javaleros.models.CaracteristicaCompleta;

@Repository
public interface CaracteristicaCompletaRepository extends JpaRepository<CaracteristicaCompleta, Integer> {
}
