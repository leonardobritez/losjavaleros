package javaleros.frba.javaleros.controller;

import javaleros.frba.javaleros.exceptions.EmailException;
import javaleros.frba.javaleros.models.Rol;
import javaleros.frba.javaleros.models.Usuario;
import javaleros.frba.javaleros.models.dto.LoginRequest;
import javaleros.frba.javaleros.models.dto.UsuarioDto;
import javaleros.frba.javaleros.models.exeptions.InvalidPasswordException;
import javaleros.frba.javaleros.repository.UsuarioRepository;
import javaleros.frba.javaleros.security.storage.TokenRepository;
import javaleros.frba.javaleros.service.impl.UsuarioServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Collection;
import java.util.Objects;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequestMapping("/user")
@RestController
public class UserController {


    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    TokenRepository tokenRepository;

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    UsuarioServiceImpl usuarioService;

    private final String PREFIX = "Bearer ";
    @PostMapping("/login")
    @ResponseBody
    public ResponseEntity login( @RequestBody LoginRequest loginRequest)  {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsuario(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = tokenRepository.save(loginRequest.getUsuario());

        return  new ResponseEntity(token,HttpStatus.ACCEPTED);

    }


    @PostMapping("/logout")
    @ResponseBody
    public ResponseEntity logout(@RequestHeader (name="Authorization") String token)  {


        tokenRepository.delete(token.replace(PREFIX, ""));

        return  new ResponseEntity(HttpStatus.NO_CONTENT);

    }
    @GetMapping(value = "/token/{token}",produces = APPLICATION_JSON_VALUE)
    @ResponseBody()
    public ResponseEntity login( @PathVariable String token)  {
       Usuario usuario = usuarioService.getUsuario(token);
       if(Objects.isNull(usuario)){
           return new ResponseEntity(HttpStatus.NOT_FOUND);
       }
       return new ResponseEntity(usuario,HttpStatus.OK);

    }

    @GetMapping("/ping")
    public String ping(){
        return "pong";

    }

    //PUNTO 1.5
    @PostMapping("/registrarse")
    public ResponseEntity registrate(@RequestBody UsuarioDto usuarioDto) {

        try {
            usuarioService.registerNewUsuarioAccount(usuarioDto);
        } catch (EmailException emailException) {
            return ResponseEntity.badRequest().body("Ya existe un usuario con este email.");

        } catch (InvalidPasswordException invalidPasswordException){
            return ResponseEntity.badRequest().body("Contraseña no cumple con los estandares.");

        }
        return ResponseEntity.ok("Usuario registrado con email: " + usuarioDto.getEmail());

    }

    @GetMapping( "/rol")
    @ResponseBody
    public Collection<Rol> getRol(Principal principal) {
        String nombreUsuario = principal.getName();
        Usuario usuario = usuarioRepository.findByEmail(nombreUsuario);
        return usuario.getRoles();

    }

}