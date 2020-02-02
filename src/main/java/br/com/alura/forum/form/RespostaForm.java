package br.com.alura.forum.form;


import br.com.alura.forum.model.Resposta;
import br.com.alura.forum.model.Topico;
import br.com.alura.forum.model.Usuario;

public class RespostaForm {

	private String mensagem;
	
	public RespostaForm() {
		
	}

	public Resposta devolveResposta(Topico topico) {
		/*
		 * Simula usuario para teste
		 */
		Usuario usuario = new Usuario();
		usuario.setNome("appadmin");
		usuario.setEmail("admin@tests.alura.com");
		usuario.setId(1L); //Id que ja exista persistido por causa do relacionamento.
		usuario.setSenha("admin");
		
		Resposta resposta = new Resposta();
		resposta.setMensagem(this.mensagem);
		resposta.setSolucao(false);
		resposta.setTopico(topico);
		resposta.setAutor(usuario);
		
		return resposta;
	}

	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}
	
	
}
