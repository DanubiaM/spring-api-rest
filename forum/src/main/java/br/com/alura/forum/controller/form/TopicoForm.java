package br.com.alura.forum.controller.form;

import br.com.alura.forum.controller.repository.CursoRepostiory;
import br.com.alura.forum.modelo.Curso;
import br.com.alura.forum.modelo.Topico;

import java.util.Objects;

public class TopicoForm {
    private String titulo;
    private String mensagem;
    private String nomeCurso;

    public String getTitulo() {
        return titulo;
    }

    public String getMensagem() {
        return mensagem;
    }

    public String getNomeCurso() {
        return nomeCurso;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public void setNomeCurso(String nomeCurso) {
        this.nomeCurso = nomeCurso;
    }


    public Topico converter(CursoRepostiory cursoRepostiory) {
        Curso curso = cursoRepostiory.findAllByNome(nomeCurso);
        return new Topico(titulo, mensagem, curso);
    }
}
