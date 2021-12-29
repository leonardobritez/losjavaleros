package javaleros.frba.javaleros.security;

import javaleros.frba.javaleros.models.Privilegio;
import javaleros.frba.javaleros.models.Rol;
import javaleros.frba.javaleros.models.Usuario;
import javaleros.frba.javaleros.repository.UsuarioRepository;
import javaleros.frba.javaleros.security.storage.TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Component
public class TokenFilter extends OncePerRequestFilter {
    private final String HEADER = "Authorization";
    private final String PREFIX = "Bearer ";

    @Autowired
    TokenRepository tokenRepository;
    @Autowired
    UsuarioRepository usuarioRepository;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if(checkToken(request,response)){
            Usuario usuario = validateToken(request);
            if(!Objects.isNull(usuario) && usuario.getRoles() != null){
                setUpSpringAuthentication(usuario);
            }
        }
        filterChain.doFilter(request, response);
    }

    private Usuario validateToken(HttpServletRequest request) {
        String token = request.getHeader(HEADER).replace(PREFIX, "");
        return usuarioRepository.findByNombreUsuario(tokenRepository.findBytoken(token));
    }
    private boolean checkToken(HttpServletRequest request, HttpServletResponse res) {
        String authenticationHeader = request.getHeader(HEADER);
        if (authenticationHeader == null || !authenticationHeader.startsWith(PREFIX))
            return false;
        return true;
    }

    private void setUpSpringAuthentication(Usuario usuario) {
        List<GrantedAuthority> authorities = getGrantedAuthorities(getPrivileges(usuario.getRoles()));

        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(usuario.getNombreUsuario(), null, authorities);
        SecurityContextHolder.getContext().setAuthentication(auth);

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
