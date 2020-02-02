package br.com.alura.forum.config.security;

import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import br.com.alura.forum.model.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class TokenService {

	@Value("${forum.jwt.secret}")
	private String secret;
	
	public String gerarToken(Authentication authentication) {
		
		Usuario usuario = (Usuario) authentication.getPrincipal();
		
		Date hoje = new Date();
		Calendar hojeCal = Calendar.getInstance();
		hojeCal.set(Calendar.MINUTE, 30);
		Date expira = hojeCal.getTime();
		
		return Jwts.builder()
				   .setIssuer("API Alura Forum")
				   .setSubject(usuario.getId().toString())
				   .setIssuedAt(hoje)
				   .setExpiration(expira)
				   .signWith(SignatureAlgorithm.HS256, secret)
				   .compact();
	}
	
	/*
	 * Validar se o token informado na requisição é valido.
	 */
	
	public boolean isTokenValido(String token) {
		try {
			Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
			return true;
		}
		catch(Exception e) {
			return false;
		}
	}
	
	/*
	 * retornar o id do usuario pelo Token
	 */
	
	public Long getUserId(String token) {
		 Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
		 return Long.parseLong(claims.getSubject());
	}
}
