package javaleros.frba.javaleros.controller;

import javaleros.frba.javaleros.exceptions.NotFound;
import javaleros.frba.javaleros.helpers.QrGenerator;
import javaleros.frba.javaleros.models.CaracteristicaCompleta;
import javaleros.frba.javaleros.models.EstadoPublicacion;
import javaleros.frba.javaleros.models.Foto;
import javaleros.frba.javaleros.models.Mascota;
import javaleros.frba.javaleros.models.MascotaEstadoEnum;
import javaleros.frba.javaleros.models.Publicacion;
import javaleros.frba.javaleros.models.PublicacionAdopcion;
import javaleros.frba.javaleros.models.PublicacionPerdida;
import javaleros.frba.javaleros.models.Usuario;
import javaleros.frba.javaleros.models.dto.MascotaDto;
import javaleros.frba.javaleros.models.dto.PublicacionDTO;
import javaleros.frba.javaleros.models.dto.RescatistaDto;
import javaleros.frba.javaleros.repository.PublicacionRepository;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/mascota")
public class MascotaController {
  public static final String URL_CHAPITA = "localhost:8081/mascota/";
  @Autowired
  private MascotaService mascotaService;

  @Autowired
  private PublicacionRepository publicacionRepository;

  
  @Autowired
  private EnviadorDeEmails enviadorDeEmails;
  @Autowired
  private UsuarioRepository usuarioRepository;
  @Autowired
  private CaracteristicaService caracteristicaService;

/*
  const onFileChange = (e) => {
    const files = e.target.files;
    const pushedFiles = [];
    for (let i = 0; i < files.length; i++) {
      let promise = getBase64(files[i])
      promise.then(function(result) {
        var arch = {
          data: result,
          fileName: files[i].name
        }
        console.log(arch)
        pushedFiles.push(arch)
    });

    }
    setFotos(pushedFiles);
  };

  function getBase64(file) {
    return new Promise((resolve, reject) => {
      const reader = new FileReader();
      reader.readAsDataURL(file);
      reader.onload = () => resolve(reader.result);
      reader.onerror = error => reject(error);
    });
  }

 */

  	@PostMapping()
  	@ResponseBody
	public ResponseEntity<HttpStatus> registrarMascota(@RequestBody MascotaDto mascotaDto) {

		Usuario usuario = getUsuarioLogeado();

	    List<CaracteristicaCompleta> caracteristicasCompletas
	        = caracteristicaService.llenarCaracteristicas(mascotaDto.getCaracteristicas());

        List<Foto> fotos = mascotaDto.getFotos().stream().map(fotoDto -> Foto.builder()
                .data(fotoDto.getData())
                .fileName(fotoDto.getFileName())
                .build()).collect(Collectors.toList());
	    Mascota mascota = Mascota.builder()
	        .apodo(mascotaDto.getApodo())
	        .duenio(usuario)
	        .fotos(fotos)
	        .sexo(mascotaDto.getSexo())
	        .estado(MascotaEstadoEnum.ADOPTADO)
	        .tipo(mascotaDto.getTipo())
	        .descripcion(mascotaDto.getDescripcion())
	        .edad(mascotaDto.getEdad())
	        .nombre(mascotaDto.getNombre())
	        .build();
	    for (CaracteristicaCompleta elem : caracteristicasCompletas) {
	      elem.setMascota(mascota);
	    }    mascota.setCaracteristicas(caracteristicasCompletas);
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
    mascotaService.guardarMascota(mascota);

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
    mascotaService.guardarMascota(mascota);

    String cuerpoEmail = String.format(
        "Hola una persona llamada %s %s encontró a tu mascota %s \n" +
            "Contactate con %s",
        rescatistaDto.getNombre(),
        rescatistaDto.getApellido(),
        mascota.getNombre(), rescatistaDto.getEmail());
    enviadorDeEmails.enviarMail(mascota.getDuenio().getEmail(),
        "Alguien encontro a tu mascota!",
        cuerpoEmail);
    return new ResponseEntity(HttpStatus.OK);
  }
  @PostMapping(value = "/{id}/informarEncontrada")
  public ResponseEntity informarEncontrada(@PathVariable Integer id) {
    Optional<Mascota> mascotaOptional = mascotaService.get(id);
    if (mascotaOptional.isEmpty()) {
      return new ResponseEntity(HttpStatus.NOT_FOUND);
    }
    Mascota mascota = mascotaOptional.get();
    mascota.setEstado(MascotaEstadoEnum.ADOPTADO);
    mascotaService.guardarMascota(mascota);

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

    @PostMapping(value = "/{id}/ponerenadopcion")
    public ResponseEntity ponerEnAdopcion(@PathVariable Integer id, @RequestBody PublicacionDTO publicacionDTO) {

        Optional<Mascota> mascotaOptional = mascotaService.get(id);
        if (mascotaOptional.isEmpty()) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        Mascota mascota = mascotaOptional.get();
        mascota.setEstado(MascotaEstadoEnum.ENADOPCION);
        mascotaService.guardarMascota(mascota);
        Usuario usuario = getUsuarioLogeado();
        Publicacion publicacionAdopcion = PublicacionAdopcion.builder()
                .usuario(usuario)
                .estadoPublicacion(EstadoPublicacion.PENDIENTE)
                //.fotos(publicacionDTO.getFotos())
                .descripcion(publicacionDTO.getDescripcion())
                .partido(publicacionDTO.getPartido())
                .provincia(publicacionDTO.getProvincia())
                .calle(publicacionDTO.getCalle())
                .altura(publicacionDTO.getAltura())
                .mascota(mascota)
                .build();
        publicacionRepository.save(publicacionAdopcion);
        return new ResponseEntity(publicacionAdopcion,HttpStatus.CREATED);
    }

    @PostMapping(value = "/publicarperdida")
    public ResponseEntity informarPerdida(@RequestBody PublicacionDTO publicacionDTO) {

        Usuario usuario = getUsuarioLogeado();
        Publicacion publicacionPerdida = PublicacionPerdida.builder()
                .usuario(usuario)
                .estadoPublicacion(EstadoPublicacion.PENDIENTE)
                //.fotos(publicacionDTO.getFotos())
                .descripcion(publicacionDTO.getDescripcion())
                .partido(publicacionDTO.getPartido())
                .provincia(publicacionDTO.getProvincia())
                .calle(publicacionDTO.getCalle())
                .altura(publicacionDTO.getAltura())
                .especieDeLaMascota(publicacionDTO.getEspecieDeLaMascota())
                .sexoDeLaMascota(publicacionDTO.getSexoDeLaMascota())
                .colorDeLaMascota(publicacionDTO.getColorDeLaMascota())
                .build();

        publicacionRepository.save(publicacionPerdida);
        return new ResponseEntity(publicacionPerdida,HttpStatus.CREATED);
    }


}
