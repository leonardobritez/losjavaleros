package javaleros.frba.javaleros.controller;

import javaleros.frba.javaleros.exceptions.NoEsVoluntarioException;
import javaleros.frba.javaleros.exceptions.NotFound;
import javaleros.frba.javaleros.helpers.QrGenerator;
import javaleros.frba.javaleros.models.CaracteristicaCompleta;
import javaleros.frba.javaleros.models.Mascota;
import javaleros.frba.javaleros.models.MascotaEstadoEnum;
import javaleros.frba.javaleros.models.Usuario;
import javaleros.frba.javaleros.models.dto.MascotaDto;
import javaleros.frba.javaleros.models.dto.RescatistaDto;
import javaleros.frba.javaleros.repository.CaracteristicaRepository;
import javaleros.frba.javaleros.repository.UsuarioRepository;
import javaleros.frba.javaleros.service.CaracteristicaService;
import javaleros.frba.javaleros.service.EnviadorDeEmails;
import javaleros.frba.javaleros.service.MascotaService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/mascota")
public class MascotaController {
  public static final String URL_CHAPITA = "localhost:8081/mascota/";
  @Autowired
  private MascotaService mascotaService;
  @Autowired
  private EnviadorDeEmails enviadorDeEmails;
  @Autowired
  private UsuarioRepository usuarioRepository;
  @Autowired
  private CaracteristicaService caracteristicaService;

  @PostMapping("")
  public ResponseEntity<HttpStatus> registrarMascota(@RequestBody MascotaDto mascotaDto) {
    Usuario usuario = getUsuarioLogeado();

    List<CaracteristicaCompleta> caracteristicasCompletas
        = caracteristicaService.llenarCaracteristicas(mascotaDto.getCaracteristicas());

    Mascota mascota = Mascota.builder()
        .apodo(mascotaDto.getApodo())
        .duenio(usuario)
        //.fotos(mascotaDto.getFotos())
        .sexo(mascotaDto.getSexo())
        .estado(MascotaEstadoEnum.ADOPTADO)
        .tipo(mascotaDto.getTipo())
        .descripcion(mascotaDto.getDescripcion())
        .edad(mascotaDto.getEdad())
        .nombre(mascotaDto.getNombre())
        .build();
    //poner a la mascota en cada caracteristica completa
    for (CaracteristicaCompleta elem : caracteristicasCompletas) {
      elem.setMascota(mascota);
    }
    mascota.setCaracteristicas(caracteristicasCompletas);

    usuario.getMascotas().add(mascota);

    mascotaService.guardarMascota(mascota);

    return new ResponseEntity(mascota, HttpStatus.CREATED);

  }

  @PostMapping(value = "/{id}/generarChapita", produces = MediaType.IMAGE_PNG_VALUE)
  public ResponseEntity generarChapita(@PathVariable Integer id) {
    try {
      if (mascotaService.existeMascota(id)) {
        return new ResponseEntity(
            QrGenerator.generateQRCodeImage(URL_CHAPITA + id),
            HttpStatus.OK);
      } else {
        return new ResponseEntity(HttpStatus.NOT_FOUND);
      }

    } catch (Exception e) {
      return new ResponseEntity("No se pudo generar chapita", HttpStatus.INTERNAL_SERVER_ERROR);

    }
  }

  @GetMapping(value = "/{id}/informarPerdida")
  public ResponseEntity informarPerdidaConChapita(@PathVariable Integer id) {

    Usuario rescatista = getUsuarioLogeado();
    Optional<Mascota> mascotaOptional = mascotaService.get(id);
    if (mascotaOptional.isEmpty()) {
      return new ResponseEntity(HttpStatus.NOT_FOUND);
    }
    Mascota mascota = mascotaOptional.get();
    mascota.setEstado(MascotaEstadoEnum.PERDIDO);

    String cuerpoEmail = String.format(
        "Hola una persona llamada %s %s encontró a tu mascota %s \n" +
            "Contactate con %s",
        rescatista.getNombre(),
        rescatista.getApellido(),
        mascota.getNombre(), rescatista.getEmail());
    enviadorDeEmails.enviarMail(mascota.getDuenio().getEmail(),
        "Alguien encontro a tu mascota!",
        cuerpoEmail);
    return new ResponseEntity(HttpStatus.OK);
  }


  @PostMapping(value = "/{id}/informarPerdida")
  public ResponseEntity informarPerdidaConChapita(@PathVariable Integer id, @RequestBody RescatistaDto rescatistaDto) {
    Optional<Mascota> mascotaOptional = mascotaService.get(id);
    if (mascotaOptional.isEmpty()) {
      return new ResponseEntity(HttpStatus.NOT_FOUND);
    }
    Mascota mascota = mascotaOptional.get();
    mascota.setEstado(MascotaEstadoEnum.PERDIDO);

    String cuerpoEmail = String.format(
        "Hola una persona llamada %s %s encontró a tu mascota %s \n" +
            "Contactate con %s",
        rescatistaDto.getNombre(),
        rescatistaDto.getNombre(),
        mascota.getNombre(), rescatistaDto.getEmail());
    enviadorDeEmails.enviarMail(mascota.getDuenio().getEmail(),
        "Alguien encontro a tu mascota!",
        cuerpoEmail);
    return new ResponseEntity(HttpStatus.OK);
  }

  @GetMapping(value = "/{id}")
  public ResponseEntity obtenerMascota(@PathVariable Integer id) {
    Optional<Mascota> mascotaOptional = mascotaService.get(id);
    if (mascotaOptional.isEmpty()) {
      return new ResponseEntity(HttpStatus.NOT_FOUND);
    }
    Mascota mascota = mascotaOptional.get();
    return new ResponseEntity(mascota, HttpStatus.OK);

  }

  private Usuario getUsuarioLogeado() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String currentUserName = authentication.getName();
    return usuarioRepository.findByNombreUsuario(currentUserName);
  }

  @ExceptionHandler({NotFound.class})
  public ResponseEntity<String> handleNoEsVoluntarioExceptionException(RuntimeException exception) {
    return ResponseEntity.notFound().build();

  }


}
