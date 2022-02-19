package javaleros.frba.javaleros.controller;

import javaleros.frba.javaleros.exceptions.NoEsVoluntarioException;
import javaleros.frba.javaleros.models.EstadoPublicacion;
import javaleros.frba.javaleros.models.Foto;
import javaleros.frba.javaleros.models.Publicacion;
import javaleros.frba.javaleros.models.PublicacionBusco;
import javaleros.frba.javaleros.models.Usuario;
import javaleros.frba.javaleros.models.dto.PublicacionDTO;
import javaleros.frba.javaleros.repository.PublicacionRepository;
import javaleros.frba.javaleros.repository.UsuarioRepository;
import javaleros.frba.javaleros.repository.VoluntarioRepository;
import javaleros.frba.javaleros.service.EnviadorDeEmails;
import javaleros.frba.javaleros.service.VoluntarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
import java.util.stream.Collectors;

@RestController
@RequestMapping("/publicaciones")
public class PublicacionesController {

    @Autowired
    private PublicacionRepository publicacionRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private  VoluntarioService voluntarioService;
    @Autowired
    private VoluntarioRepository voluntarioRepository;
    @Autowired
    private EnviadorDeEmails enviadorDeEmails;



    @PostMapping("/buscarmascota")
    private ResponseEntity crearPublicacionDeBuscarMascota(@RequestBody PublicacionDTO publicacionDTO){
        Usuario usuario = getUsuarioLogeado();
        List<Foto> fotos = publicacionDTO.getFotos().stream().map(fotoDto -> Foto.builder()
                .data(fotoDto.getData())
                .fileName(fotoDto.getFileName())
                .build()).collect(Collectors.toList());
        Publicacion publicacionPerdida = PublicacionBusco.builder()
                .usuario(usuario)
                .estadoPublicacion(EstadoPublicacion.PENDIENTE)
                .fotos(fotos)
                .edad(publicacionDTO.getEdad())
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
        return new ResponseEntity(publicacionPerdida, HttpStatus.CREATED);

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
    @GetMapping("/pendientes/{tipo}")
    public ResponseEntity<List<Publicacion>> listarPublicacionesPendientes(@PathVariable Integer tipo) {

        List<Publicacion> publicaciones = voluntarioService.listarPublicacionesPendientes(tipo);

        return ResponseEntity.ok().body(publicaciones);

    }

    // Listar publicaciones aprobadas
    //2.5- Se debe permitir que una persona busque a su mascota perdida en la plataforma y que pueda
    //contactarse con el rescatista en caso de encontrarla.
    @GetMapping("/aprobadas/{tipo}")
    public ResponseEntity<List<Publicacion>> listarPublicacionesAprobadas(@PathVariable Integer tipo) {

        List<Publicacion> publicaciones = voluntarioService.listarPublicacionesAprobadas(tipo);

        return ResponseEntity.ok().body(publicaciones);

    }

    @GetMapping("/{id}/contactar")
    public ResponseEntity contactarPublicacion(@PathVariable final Integer  id) {
        Usuario usuario = getUsuarioLogeado();
        Optional<Publicacion> publicacion = publicacionRepository.findById(id);
        if(publicacion.isPresent()){
            Usuario publicador = publicacion.get().getUsuario();
            String cuerpoEmail = String.format(
                    "Hola una persona llamada %s %s quiere contactarte por tu publicacion  %s \n" +
                            "Contactate con %s",
                    usuario.getNombre(),
                    usuario.getApellido(),
                    publicacion.get().getDescripcion(), usuario.getEmail());
            enviadorDeEmails.enviarMail(publicador.getEmail(),
                    "Alguien quiere contactarse con vos!",
                    cuerpoEmail);
            return new ResponseEntity(HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.NOT_FOUND);


    }

    private Usuario getUsuarioLogeado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = authentication.getName();
        return usuarioRepository.findByNombreUsuario(currentUserName);
    }

    @ExceptionHandler({NoEsVoluntarioException.class})
    public ResponseEntity<String> handleNoEsVoluntarioExceptionException(RuntimeException exception) {
        return ResponseEntity.badRequest().body(exception.getMessage());

    }


    private boolean usuarioResgistradoEsVoluntario(Usuario usuario) {
        return voluntarioRepository.findByUsuario(usuario) != null;

    }
    private boolean usuarioResgistradoEsVoluntario() {
        Usuario usuarioLogeado = getUsuarioLogeado();
        return usuarioResgistradoEsVoluntario(usuarioLogeado);

    }
}
