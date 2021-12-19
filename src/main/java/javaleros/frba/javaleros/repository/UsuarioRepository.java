package javaleros.frba.javaleros.repository;

import javaleros.frba.javaleros.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Usuario findByEmail(String email);

    @Override
    void delete(Usuario user);
}
