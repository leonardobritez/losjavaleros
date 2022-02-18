package javaleros.frba.javaleros.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import javaleros.frba.javaleros.exceptions.NoEsVoluntarioException;
import javaleros.frba.javaleros.models.Asociacion;
import static javaleros.frba.javaleros.models.Constants.VOLUNTARIO;
import javaleros.frba.javaleros.models.HogarDeTransito;
import javaleros.frba.javaleros.models.Publicacion;
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

    if (usuarioResgistradoEsVoluntario()) {
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


  // Listar Asociaciones disponibles
  @GetMapping("/asociaciones")
  public ResponseEntity<List<Asociacion>> listarAsociaciones() {

    List<Asociacion> asociaciones = voluntarioService.listarAsociaciones();

    return ResponseEntity.ok().body(asociaciones);

  }

  //2.4- Se debe permitir que el sistema ofrezca posibles hogares de tránsito a los rescatistas de mascotas.
  //Existe una API REST que ofrece un listado de hogares de tránsito.
  @GetMapping("/hogaresDeTransito")
  public ResponseEntity<List<HogarDeTransito>> listarHogaresDeTransito() {

    //TODO "El rescatista podrá elegir el radio de cercanía de los hogares de tránsito." Esto hay que ver como
    // solucionarlo.

    List<HogarDeTransito> lista = new ArrayList<>();
    HogarDeTransito hogar1 = new HogarDeTransito("Casa de gatitos", "Pasaje Miau-miau 4290",
        false, true, false, 14);
    HogarDeTransito hogar2 = new HogarDeTransito("Refugio animalitos de dios", "Av. Siempreviva 5091",
        true, true, true, 17);
    lista.add(hogar1);
    lista.add(hogar2);

    return ResponseEntity.ok().body(lista);

  }

  private Usuario getUsuarioLogeado() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String currentUserName = authentication.getName();
    return usuarioRepository.findByNombreUsuario(currentUserName);
  }

  private boolean usuarioResgistradoEsVoluntario() {
    Usuario usuarioLogeado = getUsuarioLogeado();
    return usuarioResgistradoEsVoluntario(usuarioLogeado);

  }

  private boolean usuarioResgistradoEsVoluntario(Usuario usuario) {
    return voluntarioRepository.findByUsuario(usuario) != null;

  }

  @ExceptionHandler({NoEsVoluntarioException.class})
  public ResponseEntity<String> handleNoEsVoluntarioExceptionException(RuntimeException exception) {
    return ResponseEntity.badRequest().body(exception.getMessage());

  }

}
