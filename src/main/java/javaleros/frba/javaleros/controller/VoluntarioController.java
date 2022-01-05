package javaleros.frba.javaleros.controller;

import java.util.Collection;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import static javaleros.frba.javaleros.models.Constants.VOLUNTARIO;
import javaleros.frba.javaleros.models.Rol;
import javaleros.frba.javaleros.models.Usuario;
import javaleros.frba.javaleros.models.Voluntario;
import javaleros.frba.javaleros.repository.RolRepository;
import javaleros.frba.javaleros.repository.UsuarioRepository;
import javaleros.frba.javaleros.repository.VoluntarioRepository;
import javaleros.frba.javaleros.service.VoluntarioService;
import javassist.NotFoundException;
import lombok.extern.java.Log;

@Log
@RestController
public class VoluntarioController {

  private final VoluntarioService voluntarioService;
  private final VoluntarioRepository voluntarioRepository;
  private final UsuarioRepository usuarioRepository;
  private final RolRepository rolRepository;


  public VoluntarioController(final VoluntarioService voluntarioService, final VoluntarioRepository voluntarioRepository,
                              final UsuarioRepository usuarioRepository, final RolRepository rolRepository) {
    this.voluntarioService = voluntarioService;
    this.voluntarioRepository = voluntarioRepository;
    this.usuarioRepository = usuarioRepository;
    this.rolRepository = rolRepository;
  }

  //PUNTO 2.3
  @PostMapping("/serVoluntario/asociacion/{asociacionId}")
  public ResponseEntity<Voluntario> serVoluntario(@PathVariable final long asociacionId) throws NotFoundException {

    Usuario usuarioLogeado = getUsuarioLogeado();
    
    if (voluntarioRepository.findByUsuario(usuarioLogeado) != null) {
      log.info("El usuario " + usuarioLogeado.getNombreUsuario() + " ya es voluntario.");
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

    }
    Voluntario nuevoVoluntario = voluntarioService.crearVoluntario(usuarioLogeado.getId(), asociacionId);

    Collection<Rol> roles = usuarioLogeado.getRoles();
    Rol rolVoluntario = rolRepository.findByNombre(VOLUNTARIO);
    roles.add(rolVoluntario);
    usuarioLogeado.setRoles(roles);
    usuarioRepository.save(usuarioLogeado);

    return new ResponseEntity<>(nuevoVoluntario, HttpStatus.OK);

  }

  //PUNTO 2.3
  @PostMapping("/aprobarPublicacion/{idPublicacion}")
  public ResponseEntity<HttpStatus> aprobarPublicacion(@PathVariable final int idPublicacion) {

    //todo probar que el usuario loggeado es un voluntario.

    voluntarioService.aprobarPublicacion(idPublicacion);

    return new ResponseEntity<>(HttpStatus.OK);

  }

  //PUNTO 2.3
  @PostMapping("/rechazarPublicacion/{idPublicacion}")
  public ResponseEntity<HttpStatus> rechazarPublicacion(@PathVariable final int idPublicacion) {

    //todo probar que el usuario loggeado es un voluntario.

    voluntarioService.rechazarPublicacion(idPublicacion);

    return new ResponseEntity<>(HttpStatus.OK);

  }

  private Usuario getUsuarioLogeado() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String currentUserName = authentication.getName();
    return usuarioRepository.findByNombreUsuario(currentUserName);
  }

}
