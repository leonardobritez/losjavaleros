package javaleros.frba.javaleros.controller;

import javaleros.frba.javaleros.models.dto.LoginRequest;
import javaleros.frba.javaleros.repository.UsuarioRepository;
import javaleros.frba.javaleros.security.storage.TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
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


}