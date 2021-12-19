package javaleros.frba.javaleros.repository;

import javaleros.frba.javaleros.models.Privilegio;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrivilegioRepository extends JpaRepository<Privilegio, Long> {

    Privilegio findByName(String name);

    @Override
    void delete(Privilegio privilege);

}
