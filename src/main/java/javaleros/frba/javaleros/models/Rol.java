package javaleros.frba.javaleros.models;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.Collection;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Rol {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String nombre;
    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "roles")
    private Collection<Usuario> users;

    @ManyToMany(cascade = CascadeType.ALL,fetch=FetchType.EAGER)
    @Fetch(FetchMode.SELECT)
    @JoinTable(
            name = "roles_privilegios",
            joinColumns = @JoinColumn(
                    name = "role_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "privilegio_id", referencedColumnName = "id"))
    private Collection<Privilegio> privilegios;


    public Rol(String name) {
        this.nombre = name;
    }
}