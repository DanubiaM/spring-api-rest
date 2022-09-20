package br.com.alura.forum.repository;

import br.com.alura.forum.modelo.Topico;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TopicoRepository extends JpaRepository<Topico, Long> {
    /*Curso refere-se à outra entidade relacionada à classe topico,
    * outra maneira de se referir a entidade curso e seu atributo nome seria: findByCurso_nome
    * */
    Page<Topico> findByCursoNome(String nomeCurso, Pageable pageable);



}
