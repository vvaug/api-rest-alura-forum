package br.com.alura.forum.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import br.com.alura.forum.repository.UsuarioRepository;

@EnableWebSecurity
@Configuration
public class ConfiguracoesDeSeguranca extends WebSecurityConfigurerAdapter{

	@Autowired
	AutenticacaoService autenticacaoService;
	
	@Autowired
	TokenService tokenService;
	
	@Autowired
	UsuarioRepository usuarioRepository;
	
	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(autenticacaoService).passwordEncoder(new BCryptPasswordEncoder());
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
		    .antMatchers(HttpMethod.GET, "/topicos/**")
		    .permitAll()
		    .antMatchers(HttpMethod.GET, "/actuator/**") 	// não recomendado em produção
		    .permitAll()
		    .antMatchers(HttpMethod.POST, "/auth")
		    .permitAll()
		    .anyRequest()
		    .authenticated()
		    .and()
		    //.formLogin();
		    .csrf()			                       // jwt 		
		    .disable()                
		    .sessionManagement()
		    .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // não é pra criar session
		    .and()
		    .addFilterBefore(new AutenticacaoViaTokenFilter(tokenService, usuarioRepository), UsernamePasswordAuthenticationFilter.class)
		    ;
	}
	

	@Override
	public void configure(WebSecurity web) throws Exception {

	}
	
}
