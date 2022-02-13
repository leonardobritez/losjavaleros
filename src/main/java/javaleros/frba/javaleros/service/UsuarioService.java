package javaleros.frba.javaleros.service;

import javaleros.frba.javaleros.models.Usuario;
import javaleros.frba.javaleros.models.dto.UsuarioDto;

import java.util.Optional;

public interface UsuarioService {

    Usuario registerNewUsuarioAccount(UsuarioDto accountDto) throws Exception;

    Usuario getUsuario(String verificationToken);

    void saveRegisteredUsuario(Usuario user);

    void deleteUsuario(Usuario user);

    void createVerificationTokenForUsuario(Usuario user, String token);

    //VerificationToken getVerificationToken(String VerificationToken);

    //VerificationToken generateNewVerificationToken(String token);

    void createPasswordResetTokenForUsuario(Usuario user, String token);

    Usuario findUsuarioByEmail(String email);

    //PasswordResetToken getPasswordResetToken(String token);

    Optional<Usuario> getUsuarioByPasswordResetToken(String token);

    Optional<Usuario> getUsuarioByID(long id);

    void changeUsuarioPassword(Usuario user, String password);
}
