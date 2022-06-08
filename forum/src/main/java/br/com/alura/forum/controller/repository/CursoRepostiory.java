package br.com.alura.forum.controller.repository;

import br.com.alura.forum.modelo.Curso;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CursoRepostiory extends JpaRepository<Curso, Long> {

    Curso findAllByNome(String nomeCurso);
}
