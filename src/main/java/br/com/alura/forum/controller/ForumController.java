package br.com.alura.forum.controller;

import java.net.URI;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.alura.forum.dto.DetalhesTopicoDto;
import br.com.alura.forum.dto.RespostaDto;
import br.com.alura.forum.dto.TopicoDto;
import br.com.alura.forum.form.AtualizarTopicoFormDto;
import br.com.alura.forum.form.RespostaForm;
import br.com.alura.forum.form.TopicoForm;
import br.com.alura.forum.model.Resposta;
import br.com.alura.forum.model.StatusTopico;
import br.com.alura.forum.model.Topico;
import br.com.alura.forum.repository.CursoRepository;
import br.com.alura.forum.repository.RespostaRepository;
import br.com.alura.forum.repository.TopicoRepository;

@RequestMapping("/topicos")
@RestController
public class ForumController {

	@Autowired
	TopicoRepository topicoRepository;
	@Autowired
	CursoRepository cursoRepository;
	@Autowired
	RespostaRepository respostaRepository;
    
	@GetMapping
	@Cacheable(value = "listaTopicos")
	public Page<TopicoDto> listarTopicos(@RequestParam(name = "titulo", required = false) String titulo,
			                             @PageableDefault(sort = "id",
			                                              direction = Direction.ASC,
			                                              page = 0,
			                                              size = 10) Pageable pagination) {
		
		//Pageable pagination = PageRequest.of(pagina, quantidade, Direction.DESC, ordenacao);
		
		Page<Topico> topicos = topicoRepository.findAll(pagination);
		
		if (titulo == null) {
			return TopicoDto.converter(topicos);
		}

		Page<Topico> topicosFiltrados = topicoRepository.findByTitulo(titulo, pagination);
		
		return TopicoDto.converter(topicosFiltrados);
	}

	@GetMapping("/{id}")
	public ResponseEntity<DetalhesTopicoDto> getTopicoDetalhe(@PathVariable(name = "id") Long id) {
		Optional<Topico> topico = topicoRepository.findById(id);
		if (topico.isPresent()) {
			return ResponseEntity.ok(new DetalhesTopicoDto(topico.get()));
		}
		return ResponseEntity.notFound().build();
	}

	@PostMapping
	@Transactional
	@CacheEvict(value = "listaTopicos", allEntries = true)
	public ResponseEntity<?> cadastrar(@RequestBody @Valid TopicoForm topico, UriComponentsBuilder UriBuilder) {
		Topico tp = topico.conveter(cursoRepository); // passa instância de cursoRepository para buscar o objeto curso
													  // dado o nome no request.
		topicoRepository.save(tp);
		URI uri = UriBuilder.path("/topicos/{id}").buildAndExpand(tp.getId()).toUri();
		return ResponseEntity.created(uri).body(new TopicoDto(tp));
	}

	@PutMapping("/{id}")
	@Transactional
	@CacheEvict(value = "listaTopicos", allEntries = true)
	public ResponseEntity<TopicoDto> atualizar(@PathVariable(name = "id") Long id,
			@RequestBody @Valid AtualizarTopicoFormDto dto) {

		Optional<Topico> topicoOp = topicoRepository.findById(id);

		if (topicoOp.isPresent()) {
			Topico topico = dto.devolveTopicoAtualizado(id, topicoRepository);
			return ResponseEntity.ok().body(new TopicoDto(topico));
		}

		return ResponseEntity.notFound().build();
	}

	@DeleteMapping("/{id}")
	@Transactional
	@CacheEvict(value = "listaTopicos", allEntries = true)
	public ResponseEntity<?> excluir(@PathVariable(name = "id") Long id) {
		
		Optional<Topico> topicoOp = topicoRepository.findById(id);
 
		if (topicoOp.isPresent()) {
			topicoRepository.deleteById(id);
			return ResponseEntity.ok().build();
		}

		return ResponseEntity.notFound().build();

	}

	@PostMapping("/{id}/respostas")
	@Transactional
	public ResponseEntity<DetalhesTopicoDto> cadastrarResposta(@PathVariable(name = "id") Long id,
			                                                   @RequestBody RespostaForm form, 
			                                                   UriComponentsBuilder URIBuilder) {
		Optional<Topico> topicoOp = topicoRepository.findById(id);

		if (topicoOp.isPresent()) {
			Topico topico = topicoOp.get();
			Resposta resposta = form.devolveResposta(topico);
			
			respostaRepository.save(resposta);
			
			topico.getRespostas().add(resposta);
			topico.setStatus(StatusTopico.NAO_SOLUCIONADO);
			
			URI uri = URIBuilder.path("/topicos/".concat(id.toString()).concat("/respostas/{respId}"))
			            		.buildAndExpand(resposta.getId()).toUri();
			
			return ResponseEntity.created(uri).body(new DetalhesTopicoDto(topico));
		}
		return ResponseEntity.notFound().build();
	}

	@DeleteMapping("/{id}/respostas/{respId}")
	@Transactional
	public ResponseEntity<?> excluirResposta(@PathVariable(name = "id") Long id,
			                                 @PathVariable(name = "respId") Long respId) {
		Optional<Topico> topicoOp = topicoRepository.findById(id);
		Optional<Resposta> respostaOp = respostaRepository.findById(respId);

		if (topicoOp.isPresent()) {
			if (respostaOp.isPresent()) {
				respostaRepository.deleteById(respId);
				return ResponseEntity.ok().build();
			}
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.notFound().build();
	}
	
	@PutMapping("/{id}/respostas/{respId}")
	@Transactional
	public ResponseEntity<RespostaDto> alterarResposta(@PathVariable(name = "id") Long id,
			                                 @PathVariable(name = "respId") Long respId,
			                                 @RequestBody RespostaForm form){
		Optional<Topico> topicoOp = topicoRepository.findById(id);
		Optional<Resposta> respostaOp = respostaRepository.findById(respId);

		if (topicoOp.isPresent()) {
			if (respostaOp.isPresent()) {
				Resposta resposta = respostaOp.get();
				resposta.setMensagem(form.getMensagem());
				return ResponseEntity.ok().body(new RespostaDto(resposta));
			}
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.notFound().build();
	}
	
	@PostMapping("/{id}/respostas/selecionasolucao/{respId}")
	public ResponseEntity<?> selecionarSolucao(@PathVariable(name = "id") Long id,
			                                   @PathVariable(name = "respId") Long respId){
		Optional<Topico> topicoOp = topicoRepository.findById(id);
		Optional<Resposta> respostaOp = respostaRepository.findById(respId);

		if (topicoOp.isPresent()) {
			if (respostaOp.isPresent()) {
				Resposta resposta = respostaOp.get();
				resposta.setSolucao(true);
				return ResponseEntity.ok().build();
		}
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.notFound().build();
	}
	
}
