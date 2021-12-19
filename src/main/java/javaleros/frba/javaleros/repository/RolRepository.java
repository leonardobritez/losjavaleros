package javaleros.frba.javaleros.repository;

import javaleros.frba.javaleros.models.Rol;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RolRepository extends JpaRepository<Rol, Long> {


    Rol findByNombre(String name);

    @Override
    void delete(Rol role);

}

