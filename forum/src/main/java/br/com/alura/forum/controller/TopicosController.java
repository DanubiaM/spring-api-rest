package br.com.alura.forum.controller;
import br.com.alura.forum.controller.form.AtualizarTopicoForm;
import br.com.alura.forum.controller.form.TopicoForm;
import br.com.alura.forum.dto.DetalhesTopicoDTO;
import br.com.alura.forum.repository.CursoRepostiory;
import br.com.alura.forum.dto.TopicoDTO;
import br.com.alura.forum.modelo.Topico;
import br.com.alura.forum.repository.TopicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/topicos")
public class TopicosController {

    @Autowired
    TopicoRepository  topicoRepository;

    @Autowired
    CursoRepostiory cursoRepostiory;

    @GetMapping
    @Cacheable(value = "listaDeTopicos")
    public Page<TopicoDTO> lista(@RequestParam(required=false) String nomeCurso,
                                @PageableDefault(sort="id", direction= Sort.Direction.ASC,
                                page=0, size=10) Pageable paginacao){


        if (nomeCurso == null){
            Page<Topico> topicos = topicoRepository.findAll(paginacao);
            return TopicoDTO.converter(topicos);

        }
        Page<Topico> topicos = topicoRepository.findByCursoNome(nomeCurso, paginacao);
        return TopicoDTO.converter(topicos);

    }

    @PostMapping
    @Transactional
    @CacheEvict(value = "listaDeTopicos", allEntries = true)
    public ResponseEntity<TopicoDTO> cadastar(@RequestBody @Valid TopicoForm form, UriComponentsBuilder uriBuilder) {

        Topico topico = form.converter(cursoRepostiory);
        topicoRepository.save(topico);

        URI uri = uriBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();
        return ResponseEntity.created(uri).body(new TopicoDTO(topico));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DetalhesTopicoDTO> detalhar(@PathVariable Long id){
        Optional<Topico> topico = topicoRepository.findById(id);

        if(topico.isPresent()){
            return ResponseEntity.ok(new DetalhesTopicoDTO(topico.get()));
        }


        return ResponseEntity.notFound().build();
    }

    /*
    * Para atualização existe dois métodos, Put e Path
    * Put  - é utilizado para sobreescrever toda uma informação
    * Path - é quando se deseja atualizar apenas um campo da informação/ões
    * */
    @PutMapping("/{id}")
    @Transactional
    @CacheEvict(value = "listaDeTopicos", allEntries = true)
    public ResponseEntity<TopicoDTO> atualizar (@PathVariable Long id,@RequestBody @Valid AtualizarTopicoForm form ){
        Optional<Topico> optional = topicoRepository.findById(id);

        if(optional.isPresent()){
            Topico topico = form.atualizar(id, topicoRepository);
            return ResponseEntity.ok(new TopicoDTO(topico));
        }

        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @Transactional
    @CacheEvict(value = "listaDeTopicos", allEntries = true)
    public ResponseEntity<?> deletar(@PathVariable Long id){

        Optional<Topico> optional = topicoRepository.findById(id);

        if(optional.isPresent()){
            topicoRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.notFound().build();

    }
}
