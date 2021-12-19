package javaleros.frba.javaleros.models.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UsuarioDto {


    private String firstName;
    private String lastName;
    private String password;
    private String matchingPassword;
    private String email;
    private String rol;
    private Long dni;
}
