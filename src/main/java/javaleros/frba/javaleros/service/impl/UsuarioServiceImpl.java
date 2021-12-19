package javaleros.frba.javaleros.service.impl;

import javaleros.frba.javaleros.models.Usuario;
import javaleros.frba.javaleros.models.dto.UsuarioDto;
import javaleros.frba.javaleros.repository.RolRepository;
import javaleros.frba.javaleros.repository.UsuarioRepository;
import javaleros.frba.javaleros.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.Optional;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private UsuarioRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final WebApplicationContext applicationContext;
    private RolRepository roleRepository;

    public UsuarioServiceImpl(PasswordEncoder passwordEncoder, WebApplicationContext applicationContext) {
        this.passwordEncoder = passwordEncoder;
        this.applicationContext = applicationContext;
    }


    @PostConstruct
    public void completeSetup() {
        userRepository = applicationContext.getBean(UsuarioRepository.class);
        roleRepository = applicationContext.getBean(RolRepository.class);
    }


    @Override
    public Usuario registerNewUsuarioAccount(UsuarioDto accountDto) throws Exception {
        if (emailExists(accountDto.getEmail())) {
            throw new Exception(
                    ("There is an account with that email adress: " + accountDto.getEmail()));
        }
        Usuario user = new Usuario();

        user.setNombre(accountDto.getFirstName());
        user.setApellido(accountDto.getLastName());
        user.setContrasenia(passwordEncoder.encode(accountDto.getPassword()));
        user.setEmail(accountDto.getEmail());

        user.setRoles(Arrays.asList(roleRepository.findByNombre(accountDto.getRol())));
        return userRepository.save(user);
    }

    @Override
    public Usuario getUsuario(String verificationToken) {
        return null;
    }

    @Override
    public void saveRegisteredUsuario(Usuario user) {

    }

    @Override
    public void deleteUsuario(Usuario user) {

    }

    @Override
    public void createVerificationTokenForUsuario(Usuario user, String token) {

    }

    @Override
    public void createPasswordResetTokenForUsuario(Usuario user, String token) {

    }

    @Override
    public Usuario findUsuarioByEmail(String email) {
        return null;
    }

    @Override
    public Optional<Usuario> getUsuarioByPasswordResetToken(String token) {
        return Optional.empty();
    }

    @Override
    public Optional<Usuario> getUsuarioByID(long id) {
        return Optional.empty();
    }

    @Override
    public void changeUsuarioPassword(Usuario user, String password) {

    }

    private boolean emailExists(final String email) {
        return userRepository.findByEmail(email) != null;
    }
}
