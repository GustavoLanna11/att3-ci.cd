package com.biblioteca.service;

import com.biblioteca.model.Livro;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
public class LivroService {
    
    private final Map<Long, Livro> livros = new HashMap<>();
    private final AtomicLong contador = new AtomicLong();

    public List<Livro> listarTodos() {
        return new ArrayList<>(livros.values());
    }

    public List<Livro> buscarPorTitulo(String titulo) {
        return livros.values().stream()
                .filter(livro -> livro.getTitulo().toLowerCase().contains(titulo.toLowerCase()))
                .collect(Collectors.toList());
    }

    public Optional<Livro> buscarPorId(Long id) {
        return Optional.ofNullable(livros.get(id));
    }

    public Livro criar(Livro livro) {
        livro.setId(contador.incrementAndGet());
        livro.setDisponivel(livro.getDisponivel() != null ? livro.getDisponivel() : true);
        livros.put(livro.getId(), livro);
        return livro;
    }

    public Optional<Livro> atualizar(Long id, Livro livroAtualizado) {
        return buscarPorId(id).map(livro -> {
            livro.setTitulo(livroAtualizado.getTitulo());
            livro.setAutor(livroAtualizado.getAutor());
            livro.setAno(livroAtualizado.getAno());
            if (livroAtualizado.getDisponivel() != null) {
                livro.setDisponivel(livroAtualizado.getDisponivel());
            }
            return livro;
        });
    }

    public boolean deletar(Long id) {
        return livros.remove(id) != null;
    }
}

