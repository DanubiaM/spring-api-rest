package br.com.alura.forum.controller;
import br.com.alura.forum.controller.form.AtualizarTopicoForm;
import br.com.alura.forum.controller.form.TopicoForm;
import br.com.alura.forum.dto.DetalhesTopicoDTO;
import br.com.alura.forum.repository.CursoRepostiory;
import br.com.alura.forum.dto.TopicoDTO;
import br.com.alura.forum.modelo.Topico;
import br.com.alura.forum.repository.TopicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.net.URI;
import java.util.List;


@RestController
@RequestMapping("/topicos")
public class TopicosController {

    @Autowired
    TopicoRepository  topicoRepository;

    @Autowired
    CursoRepostiory cursoRepostiory;
    @GetMapping
    public List<TopicoDTO> lista(String nomeCurso){
        if (nomeCurso == null){
            List<Topico> topicos = topicoRepository.findAll();
            return TopicoDTO.converter(topicos);

        }
        List<Topico> topicos = topicoRepository.findByCursoNome(nomeCurso);
        return TopicoDTO.converter(topicos);

    }

    @PostMapping
    public ResponseEntity<TopicoDTO> cadastar(@RequestBody @Valid TopicoForm form, UriComponentsBuilder uriBuilder) {

        Topico topico = form.converter(cursoRepostiory);
        topicoRepository.save(topico);

        URI uri = uriBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();
        return ResponseEntity.created(uri).body(new TopicoDTO(topico));
    }

    @GetMapping("/{id}")
    public DetalhesTopicoDTO detalhar(@PathVariable Long id){
        Topico topico = topicoRepository.getOne(id);


        return new DetalhesTopicoDTO(topico);
    }

    /*
    * Para atualização existe dois métodos, Put e Path
    * Put  - é utilizado para sobreescrever toda uma informação
    * Path - é quando se deseja atualizar apenas um campo da informação/ões
    * */
    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<TopicoDTO> atualizar (@PathVariable Long id,@RequestBody @Valid AtualizarTopicoForm form ){
        Topico topico = form.atualizar(id, topicoRepository);

        return ResponseEntity.ok(new TopicoDTO(topico));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> deletar(@PathVariable Long id){
        topicoRepository.deleteById(id);

        return ResponseEntity.ok().build();
    }
}
