package com.biblioteca.service;

import com.biblioteca.model.Livro;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class LivroServiceTest {

    private LivroService livroService;

    @BeforeEach
    void setUp() {
        livroService = new LivroService();
    }

    @Test
    void testCriar() {
        Livro livro = new Livro(null, "Dom Casmurro", "Machado de Assis", 1899, true);
        
        Livro livroCriado = livroService.criar(livro);
        
        assertNotNull(livroCriado.getId());
        assertEquals("Dom Casmurro", livroCriado.getTitulo());
        assertEquals("Machado de Assis", livroCriado.getAutor());
        assertEquals(1899, livroCriado.getAno());
        assertTrue(livroCriado.getDisponivel());
    }

    @Test
    void testListarTodos() {
        livroService.criar(new Livro(null, "Livro 1", "Autor 1", 2000, true));
        livroService.criar(new Livro(null, "Livro 2", "Autor 2", 2001, false));
        
        List<Livro> livros = livroService.listarTodos();
        
        assertEquals(2, livros.size());
    }

    @Test
    void testBuscarPorId_Encontrado() {
        Livro livro = livroService.criar(new Livro(null, "Dom Casmurro", "Machado de Assis", 1899, true));
        
        Optional<Livro> livroEncontrado = livroService.buscarPorId(livro.getId());
        
        assertTrue(livroEncontrado.isPresent());
        assertEquals("Dom Casmurro", livroEncontrado.get().getTitulo());
    }

    @Test
    void testBuscarPorId_NaoEncontrado() {
        Optional<Livro> livroEncontrado = livroService.buscarPorId(999L);
        
        assertFalse(livroEncontrado.isPresent());
    }

    @Test
    void testBuscarPorTitulo() {
        livroService.criar(new Livro(null, "Dom Casmurro", "Machado de Assis", 1899, true));
        livroService.criar(new Livro(null, "O Cortiço", "Aluísio Azevedo", 1890, true));
        livroService.criar(new Livro(null, "Dom Quixote", "Miguel de Cervantes", 1605, true));
        
        List<Livro> livros = livroService.buscarPorTitulo("Dom");
        
        assertEquals(2, livros.size());
        assertTrue(livros.stream().anyMatch(l -> l.getTitulo().equals("Dom Casmurro")));
        assertTrue(livros.stream().anyMatch(l -> l.getTitulo().equals("Dom Quixote")));
    }

    @Test
    void testAtualizar_Encontrado() {
        Livro livro = livroService.criar(new Livro(null, "Dom Casmurro", "Machado de Assis", 1899, true));
        
        Livro livroAtualizado = new Livro(null, "Dom Casmurro - Edição Especial", "Machado de Assis", 1899, false);
        Optional<Livro> resultado = livroService.atualizar(livro.getId(), livroAtualizado);
        
        assertTrue(resultado.isPresent());
        assertEquals("Dom Casmurro - Edição Especial", resultado.get().getTitulo());
        assertFalse(resultado.get().getDisponivel());
        assertEquals(livro.getId(), resultado.get().getId());
    }

    @Test
    void testAtualizar_NaoEncontrado() {
        Livro livroAtualizado = new Livro(null, "Novo Título", "Novo Autor", 2000, true);
        Optional<Livro> resultado = livroService.atualizar(999L, livroAtualizado);
        
        assertFalse(resultado.isPresent());
    }

    @Test
    void testDeletar_Encontrado() {
        Livro livro = livroService.criar(new Livro(null, "Dom Casmurro", "Machado de Assis", 1899, true));
        Long id = livro.getId();
        
        boolean deletado = livroService.deletar(id);
        
        assertTrue(deletado);
        assertFalse(livroService.buscarPorId(id).isPresent());
    }

    @Test
    void testDeletar_NaoEncontrado() {
        boolean deletado = livroService.deletar(999L);
        
        assertFalse(deletado);
    }
}

