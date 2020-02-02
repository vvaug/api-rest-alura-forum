package br.com.alura.forum.config.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import br.com.alura.forum.model.Usuario;
import br.com.alura.forum.repository.UsuarioRepository;

public class AutenticacaoViaTokenFilter extends OncePerRequestFilter {

	private TokenService tokenService;
	private UsuarioRepository usuarioRepository;
	
	 public AutenticacaoViaTokenFilter(TokenService tokenService, UsuarioRepository usuarioRepository) {
		/*
		 * não é possivel injetar servicos via Autowired em classes Filter, 
		 * por isso, injetamos via constructor.
		 */
		this.tokenService = tokenService;
		this.usuarioRepository = usuarioRepository;
	}
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		// O usuário já se autenticou via "POST /auth" para gerar um token.
		// em suas futuras requisições, o Token será também enviado
		// essas mesmas requisições serão interceptadas por este Filter
		// onde validaremos este Token e se o mesmo estiver válido, forçaremos a autenticação
		// via SecurityContextHolder, se não, a requisição será bloqueada (403).
		
		String token = recuperarToken(request);
		
		boolean valido = tokenService.isTokenValido(token);
		
		if (valido) {
			this.autenticarCliente(token);
		}
		
		filterChain.doFilter(request, response);
	}

	public String recuperarToken(HttpServletRequest request) {
		String token = request.getHeader("Authorization");
		if ( token == null || token.isEmpty() || ! token.startsWith("Bearer")) {
			return null;
		}
		return token.substring(7, token.length());
	}
	
	public void autenticarCliente(String token){
	  Long id = tokenService.getUserId(token);
	  Usuario principal = usuarioRepository.findById(id).get();
	  Authentication authentication = new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());
	  SecurityContextHolder.getContext().setAuthentication(authentication);
	}
}
