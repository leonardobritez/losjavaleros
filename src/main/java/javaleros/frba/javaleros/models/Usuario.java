package javaleros.frba.javaleros.models;

import javaleros.frba.javaleros.exceptions.InvalidPasswordException;
import javaleros.frba.javaleros.security.PasswordValidatorSingleton;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.passay.RuleResult;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Collection;
import java.util.List;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(unique = true)
    private Long dni;
    private String nombre;
    private String apellido;
    private String email;
    private String contrasenia;
    @Column(unique = true)
    private String nombreUsuario;
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Collection<Rol> roles;
    @OneToMany(cascade = CascadeType.ALL)
    private List<Mascota> mascotas;


    public Usuario(final Integer id, final Long dni, final String nombreUsuario, final String nombre, final String apellido, final String email, final String contrasenia,
                   final Collection<Rol> roles,
                   final List<Mascota> mascotas) {
        this.id = id;
        this.dni = dni;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.contrasenia = contrasenia;
        this.roles = roles;
        this.nombreUsuario = nombreUsuario;
        this.mascotas = mascotas;
    }

    //no borrar este constructor!
    public Usuario(Long dni,String nombreUsuario, String firstName, String lastName, String email, String contrasenia, Collection<Rol> roles)
            throws InvalidPasswordException {
        this.dni = dni;
        this.nombre = firstName;
        this.apellido = lastName;
        this.nombreUsuario = nombreUsuario;
        this.email = email;
        this.roles = roles;
        setContrasenia(contrasenia);
    }

    public void setContrasenia(String contrasenia) throws InvalidPasswordException {

        RuleResult validate = PasswordValidatorSingleton.getInstance().validate(this.nombreUsuario, contrasenia);
        if(validate.isValid()) {
            contrasenia = new BCryptPasswordEncoder().encode(contrasenia);
            this.contrasenia = contrasenia;
        }else
            throw new InvalidPasswordException(validate.getDetails().toString());
    }
}