package javaleros.frba.javaleros.controller;

import javaleros.frba.javaleros.helpers.QrGenerator;
import javaleros.frba.javaleros.models.Mascota;
import javaleros.frba.javaleros.models.MascotaEstadoEnum;
import javaleros.frba.javaleros.models.Usuario;
import javaleros.frba.javaleros.repository.UsuarioRepository;
import javaleros.frba.javaleros.service.EnviadorDeEmails;
import javaleros.frba.javaleros.service.MascotaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mascotas")
public class MascotaController {


    public static final String URL_CHAPITA = "localhost:8081/mascotas/";
    @Autowired
    private MascotaService mascotaService;

    @Autowired
    private EnviadorDeEmails enviadorDeEmails;
    @Autowired
    private UsuarioRepository usuarioRepository;

    @PostMapping("")
    public ResponseEntity<HttpStatus> registrarMascota() {


        return  new ResponseEntity("OK",HttpStatus.OK);
    }
    @PostMapping(value= "/{id}/generarChapita", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity generarChapita(@PathVariable Long id) {

        try {
            if(mascotaService.existeMascota(id)){
                return new ResponseEntity(
                        QrGenerator.generateQRCodeImage(URL_CHAPITA +id),
                        HttpStatus.OK);
            }else {
                return new ResponseEntity(HttpStatus.NOT_FOUND);
            }

        } catch (Exception e) {
            return  new ResponseEntity("No se pudo generar chapita",HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

    @GetMapping(value="{id}/informarPerdida")
    public ResponseEntity informarPerdida(@PathVariable long id){
        Usuario rescatista = getUsuarioLogeado();
        Mascota mascota = mascotaService.get(id);
        mascota.setEstado(MascotaEstadoEnum.PERDIDO);

        String cuerpoEmail = String.format(
                "Hola una persona llamada %s %s encontró a tu mascota %s \n" +
                        "Contactate a %s",
                rescatista.getNombre(),
                rescatista.getApellido(),
                mascota.getNombre(),rescatista.getEmail());
        enviadorDeEmails.enviarMail(mascota.getDuenio().getEmail(),
                "Alguien encontro a tu mascota!",
                cuerpoEmail);
        return new ResponseEntity(HttpStatus.OK);
    }

    private Usuario getUsuarioLogeado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = authentication.getName();
        return usuarioRepository.findByNombreUsuario(currentUserName);
    }


}
