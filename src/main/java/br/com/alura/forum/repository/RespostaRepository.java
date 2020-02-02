package br.com.alura.forum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.alura.forum.model.Resposta;

@Repository
public interface RespostaRepository extends JpaRepository<Resposta, Long>{

	
}
