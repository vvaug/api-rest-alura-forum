package br.com.alura.forum.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.alura.forum.model.Topico;

@Repository
public interface TopicoRepository extends JpaRepository<Topico, Long>{

	List<Topico> findByTitulo(String titulo);

	Page<Topico> findByTitulo(String titulo, Pageable pagination);

	//Query personalizada c/ JPQL:
	
	@Query("SELECT t FROM Topico t WHERE t.titulo = :titulo")
	List<Topico> buscarTopicoPorTitulo(@Param("titulo") String titulo);


}
