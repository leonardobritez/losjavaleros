package javaleros.frba.javaleros.security;

import antlr.StringUtils;
import javaleros.frba.javaleros.models.Privilegio;
import javaleros.frba.javaleros.models.Rol;
import javaleros.frba.javaleros.models.Usuario;
import javaleros.frba.javaleros.repository.RolRepository;
import javaleros.frba.javaleros.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.util.*;

import static javaleros.frba.javaleros.models.Constants.DUENIO;

@Service
public class MyUserDetailsService  implements UserDetailsService {

    private UsuarioRepository userRepository;

    @Autowired
    private WebApplicationContext applicationContext;

    private RolRepository roleRepository;

    @PostConstruct
    public void completeSetup() {
        userRepository = applicationContext.getBean(UsuarioRepository.class);
        roleRepository = applicationContext.getBean(RolRepository.class);
    }




    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {

        Usuario user = userRepository.findByNombreUsuario(email);
        if (user == null) {
            return new org.springframework.security.core.userdetails.User(
                    " ", " ", true, true, true, true,
                    getAuthorities(Arrays.asList(roleRepository.findByNombre(DUENIO))));
        }

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(), user.getContrasenia(), true, true, true,
                true, getAuthorities(user.getRoles()));
    }

    private Collection<? extends GrantedAuthority> getAuthorities(
            Collection<Rol> roles) {

        return getGrantedAuthorities(getPrivileges(roles));
    }

    private List<String> getPrivileges(Collection<Rol> roles) {

        List<String> privileges = new ArrayList<>();
        List<Privilegio> collection = new ArrayList<>();
        for (Rol role : roles) {
            privileges.add(role.getNombre());
            collection.addAll(role.getPrivilegios());
        }
        for (Privilegio item : collection) {
            privileges.add(item.getName());
        }
        return privileges;
    }

    private List<GrantedAuthority> getGrantedAuthorities(List<String> privileges) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (String privilege : privileges) {
            authorities.add(new SimpleGrantedAuthority(privilege));
        }
        return authorities;
    }
}
