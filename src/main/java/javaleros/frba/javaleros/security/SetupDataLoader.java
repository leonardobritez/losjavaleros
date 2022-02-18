package javaleros.frba.javaleros.security;

import javaleros.frba.javaleros.models.Asociacion;
import javaleros.frba.javaleros.models.Caracteristica;
import javaleros.frba.javaleros.models.Contacto;
import javaleros.frba.javaleros.models.EstadoPublicacion;
import javaleros.frba.javaleros.models.Privilegio;
import javaleros.frba.javaleros.models.Publicacion;
import javaleros.frba.javaleros.models.PublicacionBusco;
import javaleros.frba.javaleros.models.PublicacionPerdida;
import javaleros.frba.javaleros.models.Rol;
import javaleros.frba.javaleros.models.Usuario;
import javaleros.frba.javaleros.repository.AsociacionRepository;
import javaleros.frba.javaleros.repository.CaracteristicaRepository;
import javaleros.frba.javaleros.repository.ContactoRepository;
import javaleros.frba.javaleros.repository.PrivilegioRepository;
import javaleros.frba.javaleros.repository.PublicacionRepository;
import javaleros.frba.javaleros.repository.RolRepository;
import javaleros.frba.javaleros.repository.UsuarioRepository;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static javaleros.frba.javaleros.models.Constants.*;
import lombok.extern.java.Log;


@Log
@Component
public class SetupDataLoader implements
    ApplicationListener<ContextRefreshedEvent> {

  boolean alreadySetup = false;

  private UsuarioRepository userRepository;

  private RolRepository rolRepository;

  private PrivilegioRepository privilegeRepository;

  private AsociacionRepository asociacionRepository;

  private PublicacionRepository publicacionRepository;

  private ContactoRepository contactoRepository;

  private CaracteristicaRepository caracteristicaRepository;

  @Autowired
  private WebApplicationContext applicationContext;

  @PostConstruct
  public void completeSetup() {
    userRepository = applicationContext.getBean(UsuarioRepository.class);
    rolRepository = applicationContext.getBean(RolRepository.class);
    privilegeRepository = applicationContext.getBean(PrivilegioRepository.class);
    asociacionRepository = applicationContext.getBean(AsociacionRepository.class);
    publicacionRepository = applicationContext.getBean(PublicacionRepository.class);
    contactoRepository = applicationContext.getBean(ContactoRepository.class);
    caracteristicaRepository = applicationContext.getBean(CaracteristicaRepository.class);
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
    createRolIfNotFound(ADMIN, adminPrivilegios);
    createRolIfNotFound(VOLUNTARIO, Arrays.asList(readPrivilegio));
    createRolIfNotFound(DUENIO, Arrays.asList(readPrivilegio));
    createRolIfNotFound(RESCATISTA, Arrays.asList(readPrivilegio));


    Rol adminRol = rolRepository.findByNombre(ADMIN);
    Usuario user = new Usuario();
    user.setNombre("Test");
    user.setApellido("Test");
    user.setNombreUsuario("admin");
    user.setEmail("test@test.com");
    user.setContrasenia("Test1234.");
    user.setRoles(Arrays.asList(adminRol));
    userRepository.save(user);

    Asociacion asociacion1 = new Asociacion(1L, "Salvemos a los gatitos");
    asociacionRepository.save(asociacion1);
    log.info("Asociación 'Salvemos a los gatitos' creada con ID " + asociacion1.getId().toString());

    Publicacion publicacionPendiente = PublicacionPerdida.builder()
            .descripcion("Encontrado")
            .usuario(user)
            .estadoPublicacion(EstadoPublicacion.PENDIENTE)
            .asociacion(asociacion1)
            .colorDeLaMascota("negro")
            .partido("CABA")
            .provincia("Buenos Aires")
            .sexoDeLaMascota("macho")
            .especieDeLaMascota("GATO")
            .calle("Av. Siempreviva")
            .altura("1900")
            .build();
    publicacionRepository.save(publicacionPendiente);
    log.info("Publicacion pendiente creada con ID " + publicacionPendiente.getId().toString());

    Publicacion publicacionRechazada = new Publicacion(user, null, "Descripcion",
        EstadoPublicacion.RECHAZADA);
    publicacionRepository.save(publicacionRechazada);
    log.info("Publicacion rechazada creada con ID " + publicacionRechazada.getId().toString());

    Contacto contacto = new Contacto(null, 44428421L, "esmiemail@gmail.com");
    contactoRepository.save(contacto);
    
    Publicacion publicacionAprobada =  PublicacionBusco.builder()
            .descripcion("Busco Adoptar Gato")
            .usuario(user)
            .estadoPublicacion(EstadoPublicacion.APROBADA)
            .asociacion(asociacion1)
            .colorDeLaMascota("negro")
            .partido("CABA")
            .edad(1.0)
            .provincia("Buenos Aires")
            .sexoDeLaMascota("macho")
            .especieDeLaMascota("GATO")
            .calle("Av. Siempreviva")
            .altura("1900")
            .build();
    publicacionRepository.save(publicacionAprobada);
    log.info("Publicacion aprobada creada con ID " + publicacionAprobada.getId().toString());


    Caracteristica color = new Caracteristica();
    color.setNombre("Color");
    color.setTipo("TEXTO");

    Caracteristica castrado = new Caracteristica();
    castrado.setNombre("¿Está castrado?");
    castrado.setTipo("BOOLEAN");
    caracteristicaRepository.save(color);
    log.info("Caracteristica " + color.getNombre() + " creada con ID " + color.getId());
    caracteristicaRepository.save(castrado);
    log.info("Caracteristica " + castrado.getNombre() + " creada con ID " + castrado.getId());

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