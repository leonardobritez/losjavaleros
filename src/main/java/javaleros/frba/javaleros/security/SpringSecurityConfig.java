package javaleros.frba.javaleros.security;

import static javaleros.frba.javaleros.models.Constants.ADMIN;
import static javaleros.frba.javaleros.models.Constants.VOLUNTARIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.http.converter.BufferedImageHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.cors.CorsConfiguration;

import javax.annotation.PostConstruct;
import java.awt.image.BufferedImage;
import java.util.List;

@Order(101)
@EnableWebSecurity
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

  private final WebApplicationContext applicationContext;

  private MyUserDetailsService userDetailsService;

  public SpringSecurityConfig(WebApplicationContext applicationContext) {
    this.applicationContext = applicationContext;
  }

  @PostConstruct
  public void completeSetup() {
    userDetailsService = applicationContext.getBean(MyUserDetailsService.class);
  }

  @Bean
  public HttpMessageConverter<BufferedImage> createImageHttpMessageConverter() {
    return new BufferedImageHttpMessageConverter();
  }

  @Override
  protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(userDetailsService)
        .passwordEncoder(encoder())
        .and()
        .authenticationProvider(authenticationProvider());
  }

  @Override
  public void configure(WebSecurity web) {
    web.ignoring()
        .antMatchers("/resources/**")
        .antMatchers("/h2/**");
  }

  @Override
  protected void configure(final HttpSecurity http) throws Exception {
    http.cors().and().csrf().disable()
        .authorizeRequests()
            .antMatchers(HttpMethod.GET,"/caracterista").authenticated()
            .antMatchers(HttpMethod.GET,"/mascota/*/informarPerdida").authenticated()
            .antMatchers(HttpMethod.POST,"/mascota/*/informarPerdida").permitAll()
            .antMatchers(HttpMethod.POST,"/mascota/*/informarEncontrada").authenticated()
            .antMatchers("/publicaciones/**").authenticated()

            .antMatchers(HttpMethod.GET,"/mascota/{\\\\d+}").permitAll()
            .antMatchers("/mascota/**").authenticated()

            .antMatchers(HttpMethod.GET, "/hogaresDeTransito").permitAll()

            .antMatchers(HttpMethod.POST, "/user/login").permitAll()
            .antMatchers(HttpMethod.POST, "/user/registrarse").permitAll()
            .antMatchers("/caracteristica/**").hasAuthority(ADMIN)
        .and().authorizeRequests().antMatchers("/h2-console/**").permitAll()
        .anyRequest().authenticated();

    // disable frame options
    http.headers().frameOptions().disable();
    http.addFilterBefore(tokenFilter(), UsernamePasswordAuthenticationFilter.class);

    http.cors().configurationSource(request -> {
      var cors = new CorsConfiguration();
      cors.setAllowedOrigins(List.of("http://localhost:3000"));
      cors.setAllowedMethods(List.of("GET","POST", "PUT", "DELETE", "OPTIONS"));
      cors.setAllowedHeaders(List.of("*"));
      return cors;
    });

  }

  @Bean
  public TokenFilter tokenFilter() {
    return new TokenFilter();
  }

  @Bean
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }

  @Bean
  public AuthenticationProvider authenticationProvider() {
    final DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
    authProvider.setUserDetailsService(userDetailsService);
    authProvider.setPasswordEncoder(encoder());
    return authProvider;
  }

  @Bean
  public PasswordEncoder encoder() {
    return new BCryptPasswordEncoder(11);
  }

  @Bean
  public RoleHierarchy roleHierarchy() {
    RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
    String hierarchy = "ADMIN > VOLUNTARIO \n ADMIN > RESCATISTA \n ADMIN > DUENIO";
    roleHierarchy.setHierarchy(hierarchy);
    return roleHierarchy;
  }

  @Bean
  public DefaultWebSecurityExpressionHandler webSecurityExpressionHandler() {
    DefaultWebSecurityExpressionHandler expressionHandler = new DefaultWebSecurityExpressionHandler();
    expressionHandler.setRoleHierarchy(roleHierarchy());
    return expressionHandler;
  }
}