package javaleros.frba.javaleros.controller;

import javaleros.frba.javaleros.exceptions.NoEsVoluntarioException;
import javaleros.frba.javaleros.models.EstadoPublicacion;
import javaleros.frba.javaleros.models.Publicacion;
import javaleros.frba.javaleros.models.PublicacionBusco;
import javaleros.frba.javaleros.models.PublicacionPerdida;
import javaleros.frba.javaleros.models.Usuario;
import javaleros.frba.javaleros.models.dto.PublicacionDTO;
import javaleros.frba.javaleros.repository.PublicacionRepository;
import javaleros.frba.javaleros.repository.UsuarioRepository;
import javaleros.frba.javaleros.repository.VoluntarioRepository;
import javaleros.frba.javaleros.service.MascotaService;
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
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("/publicaciones")
public class PublicacionesController {

    @Autowired
    private PublicacionRepository publicacionRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private  VoluntarioService voluntarioService;
    @Autowired
    private VoluntarioRepository voluntarioRepository;



    @PostMapping("/buscarmascota")
    private ResponseEntity crearPublicacionDeBuscarMascota(@RequestBody PublicacionDTO publicacionDTO){
        Usuario usuario = getUsuarioLogeado();
        Publicacion publicacionPerdida = PublicacionBusco.builder()
                .usuario(usuario)
                .estadoPublicacion(EstadoPublicacion.PENDIENTE)
                //.fotos(publicacionDTO.getFotos())
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
    @GetMapping("/pendientes")
    public ResponseEntity<List<Publicacion>> listarPublicacionesPendientes() {

        if (!usuarioResgistradoEsVoluntario()) {
            throw new NoEsVoluntarioException();
        }

        List<Publicacion> publicaciones = voluntarioService.listarPublicacionesPendientes();

        return ResponseEntity.ok().body(publicaciones);

    }

    // Listar publicaciones aprobadas
    //2.5- Se debe permitir que una persona busque a su mascota perdida en la plataforma y que pueda
    //contactarse con el rescatista en caso de encontrarla.
    @GetMapping("/aprobadas")
    public ResponseEntity<List<Publicacion>> listarPublicacionesAprobadas() {

        List<Publicacion> publicaciones = voluntarioService.listarPublicacionesAprobadas();

        return ResponseEntity.ok().body(publicaciones);

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
