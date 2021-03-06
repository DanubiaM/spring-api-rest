package br.com.alura.forum.controller;
import br.com.alura.forum.controller.form.TopicoForm;
import br.com.alura.forum.repository.CursoRepostiory;
import br.com.alura.forum.dto.TopicoDTO;
import br.com.alura.forum.modelo.Topico;
import br.com.alura.forum.repository.TopicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

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
    public ResponseEntity<TopicoDTO> cadastar(@RequestBody TopicoForm form, UriComponentsBuilder uriBuilder) {

        Topico topico = form.converter(cursoRepostiory);
        topicoRepository.save(topico);

        URI uri = uriBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();
        return ResponseEntity.created(uri).body(new TopicoDTO(topico));
    }
}
