package com.biblioteca.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class Livro {
    private Long id;
    
    @NotBlank(message = "Título é obrigatório")
    private String titulo;
    
    @NotBlank(message = "Autor é obrigatório")
    private String autor;
    
    @NotNull(message = "Ano é obrigatório")
    @Positive(message = "Ano deve ser positivo")
    private Integer ano;
    
    private Boolean disponivel;

    public Livro() {
        this.disponivel = true;
    }

    public Livro(Long id, String titulo, String autor, Integer ano, Boolean disponivel) {
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.ano = ano;
        this.disponivel = disponivel != null ? disponivel : true;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public Integer getAno() {
        return ano;
    }

    public void setAno(Integer ano) {
        this.ano = ano;
    }

    public Boolean getDisponivel() {
        return disponivel;
    }

    public void setDisponivel(Boolean disponivel) {
        this.disponivel = disponivel;
    }
}

