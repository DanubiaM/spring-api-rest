package br.com.alura.forum.controller.form;

import br.com.alura.forum.repository.CursoRepostiory;
import br.com.alura.forum.modelo.Curso;
import br.com.alura.forum.modelo.Topico;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class TopicoForm {

    @NotNull
    @NotEmpty
    private String titulo;
    @NotNull
    @NotEmpty
    private String mensagem;
    @NotNull
    @NotEmpty
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
