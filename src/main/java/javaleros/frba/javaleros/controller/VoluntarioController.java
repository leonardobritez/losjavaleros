package javaleros.frba.javaleros.controller;

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
import static javaleros.frba.javaleros.models.Constants.VOLUNTARIO;
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

  //PUNTO 2.3
  @PostMapping("/aprobarPublicacion/{idPublicacion}")
  public ResponseEntity<HttpStatus> aprobarPublicacion(@PathVariable final int idPublicacion) {

    if (!usuarioResgistradoEsVoluntario()) {
      throw new NoEsVoluntarioException();
    }

    voluntarioService.aprobarPublicacion(idPublicacion);

    return new ResponseEntity<>(HttpStatus.OK);

  }

  //PUNTO 2.3
  @PostMapping("/rechazarPublicacion/{idPublicacion}")
  public ResponseEntity<HttpStatus> rechazarPublicacion(@PathVariable final int idPublicacion) {

    if (!usuarioResgistradoEsVoluntario()) {
      throw new NoEsVoluntarioException();
    }

    voluntarioService.rechazarPublicacion(idPublicacion);

    return new ResponseEntity<>(HttpStatus.OK);

  }

  // Listar publicaciones pendientes, solo pueden acceder voluntarios
  @GetMapping("/publicaciones/pendientes")
  public ResponseEntity<List<Publicacion>> listarPublicacionesPendientes() {

    if (!usuarioResgistradoEsVoluntario()) {
      throw new NoEsVoluntarioException();
    }

    List<Publicacion> publicaciones = voluntarioService.listarPublicacionesPendientes();

    return ResponseEntity.ok().body(publicaciones);

  }

  // Listar publicaciones aprobadas
  @GetMapping("/publicaciones/aprobadas")
  public ResponseEntity<List<Publicacion>> listarPublicacionesAprobadas() {

    List<Publicacion> publicaciones = voluntarioService.listarPublicacionesAprobadas();

    return ResponseEntity.ok().body(publicaciones);

  }



  // todo Listar Asociaciones disponibles




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
