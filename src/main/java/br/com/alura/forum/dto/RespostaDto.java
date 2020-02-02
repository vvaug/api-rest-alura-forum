package br.com.alura.forum.dto;

import java.time.LocalDateTime;

import br.com.alura.forum.model.Resposta;

public class RespostaDto {

	private Long id;
	private String autor;
	private LocalDateTime dataCriacao;
	private String mensagem;
	
	public RespostaDto(Resposta resposta) {
		this.id = resposta.getId();
		this.autor = resposta.getAutor().getNome();
		this.dataCriacao = resposta.getDataCriacao();
		this.mensagem = resposta.getMensagem();
	}

	public Long getId() {
		return id;
	}

	public String getAutor() {
		return autor;
	}

	public LocalDateTime getDataCriacao() {
		return dataCriacao;
	}

	public String getMensagem() {
		return mensagem;
	}
	
	
}
