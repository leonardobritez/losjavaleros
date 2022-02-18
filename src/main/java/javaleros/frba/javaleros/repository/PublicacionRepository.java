package javaleros.frba.javaleros.repository;

import javaleros.frba.javaleros.models.EstadoPublicacion;
import javaleros.frba.javaleros.models.PublicacionAdopcion;
import javaleros.frba.javaleros.models.PublicacionBusco;
import javaleros.frba.javaleros.models.PublicacionPerdida;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import javaleros.frba.javaleros.models.Publicacion;

import java.util.List;

@Repository
public interface PublicacionRepository extends JpaRepository<Publicacion, Integer> {


    @Query("SELECT u FROM Publicacion u WHERE u.estadoPublicacion = ?1 and TYPE(u) = ?2")
    List<Publicacion> findPublicacionsByEstadoPublicacion(EstadoPublicacion estadoPublicacion,Integer integer);

}

