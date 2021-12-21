package javaleros.frba.javaleros.controller;

import javaleros.frba.javaleros.exceptions.EmailException;
import javaleros.frba.javaleros.models.dto.LoginRequest;
import javaleros.frba.javaleros.models.dto.UsuarioDto;
import javaleros.frba.javaleros.models.exeptions.InvalidPasswordException;
import javaleros.frba.javaleros.repository.UsuarioRepository;
import javaleros.frba.javaleros.security.storage.TokenRepository;
import javaleros.frba.javaleros.service.UsuarioService;
import javaleros.frba.javaleros.service.impl.UsuarioServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/login")
    @ResponseBody
    public Object login( @RequestBody LoginRequest loginRequest)  {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsuario(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = tokenRepository.save(loginRequest.getUsuario());

        return token;

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

}