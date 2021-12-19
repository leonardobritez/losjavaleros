package javaleros.frba.javaleros.security;

import javaleros.frba.javaleros.models.Privilegio;
import javaleros.frba.javaleros.models.Rol;
import javaleros.frba.javaleros.models.Usuario;
import javaleros.frba.javaleros.repository.PrivilegioRepository;
import javaleros.frba.javaleros.repository.RolRepository;
import javaleros.frba.javaleros.repository.UsuarioRepository;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Component
public class SetupDataLoader implements
        ApplicationListener<ContextRefreshedEvent> {

    boolean alreadySetup = false;

    private UsuarioRepository userRepository;

    private RolRepository rolRepository;

    private PrivilegioRepository privilegeRepository;


    @Autowired
    private WebApplicationContext applicationContext;

    @PostConstruct
    public void completeSetup() {
        userRepository = applicationContext.getBean(UsuarioRepository.class);
        rolRepository = applicationContext.getBean(RolRepository.class);
        privilegeRepository = applicationContext.getBean(PrivilegioRepository.class);
    }

    @SneakyThrows
    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {

        if (alreadySetup)
            return;
        Privilegio readPrivilegio
                = createPrivilegioIfNotFound("READ_PRIVILEGE");
        Privilegio writePrivilegio
                = createPrivilegioIfNotFound("WRITE_PRIVILEGE");

        List<Privilegio> adminPrivilegios = Arrays.asList(
                readPrivilegio, writePrivilegio);
        createRolIfNotFound("ROLE_ADMIN", adminPrivilegios);
        createRolIfNotFound("ROLE_USER", Arrays.asList(readPrivilegio));

        Rol adminRol = rolRepository.findByNombre("ROLE_ADMIN");
        Usuario user = new Usuario();
        user.setNombre("Test");
        user.setApellido("Test");
        user.setContrasenia("test1234.");
        user.setEmail("test@test.com");
        user.setRoles(Arrays.asList(adminRol));
        userRepository.save(user);

        alreadySetup = true;
    }

    @Transactional
    public Privilegio createPrivilegioIfNotFound(String name) {

        Privilegio privilege = privilegeRepository.findByName(name);
        if (privilege == null) {
            privilege = new Privilegio(name);
            privilegeRepository.save(privilege);
        }
        return privilege;
    }

    @Transactional
    public Rol createRolIfNotFound(
            String name, Collection<Privilegio> privileges) {

        Rol rol = rolRepository.findByNombre(name);
        if (rol == null) {
            rol = new Rol(name);
            rol.setPrivilegios(privileges);
            rolRepository.save(rol);
        }
        return rol;
    }
}