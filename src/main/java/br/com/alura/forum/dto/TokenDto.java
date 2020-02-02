package br.com.alura.forum.dto;

import br.com.alura.forum.config.security.AuthorizationType;

public class TokenDto {

	private String token;
	private String type;
	
	public TokenDto(String token) {
		this.token = token;
		this.type = String.valueOf(AuthorizationType.Bearer);
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	
}
