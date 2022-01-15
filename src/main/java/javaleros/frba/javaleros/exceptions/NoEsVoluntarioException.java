package javaleros.frba.javaleros.exceptions;

public class NoEsVoluntarioException extends RuntimeException {
  public NoEsVoluntarioException() {
    super("El usuario loggeado no es voluntario.");
  }
}
