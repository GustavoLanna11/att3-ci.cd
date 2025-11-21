package com.biblioteca.controller;

import com.biblioteca.exception.GlobalExceptionHandler;
import com.biblioteca.model.Livro;
import com.biblioteca.service.LivroService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LivroController.class)
@Import(LivroControllerTest.TestConfig.class)
class LivroControllerTest {

    @TestConfiguration
    static class TestConfig {
        @Bean
        public GlobalExceptionHandler globalExceptionHandler() {
            return new GlobalExceptionHandler();
        }
    }

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LivroService livroService;

    @Autowired
    private ObjectMapper objectMapper;

    private Livro livro;

    @BeforeEach
    void setUp() {
        livro = new Livro(1L, "Dom Casmurro", "Machado de Assis", 1899, true);
    }

    @Test
    void testListarTodos() throws Exception {
        List<Livro> livros = Arrays.asList(
                livro,
                new Livro(2L, "O Cortiço", "Aluísio Azevedo", 1890, true)
        );

        when(livroService.listarTodos()).thenReturn(livros);

        mockMvc.perform(get("/api/livros"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].titulo").value("Dom Casmurro"))
                .andExpect(jsonPath("$[0].autor").value("Machado de Assis"))
                .andExpect(jsonPath("$[1].titulo").value("O Cortiço"));

        verify(livroService, times(1)).listarTodos();
    }

    @Test
    void testBuscarPorTitulo() throws Exception {
        List<Livro> livros = Arrays.asList(livro);

        when(livroService.buscarPorTitulo("Dom")).thenReturn(livros);

        mockMvc.perform(get("/api/livros/buscar")
                        .param("titulo", "Dom"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].titulo").value("Dom Casmurro"));

        verify(livroService, times(1)).buscarPorTitulo("Dom");
    }

    @Test
    void testBuscarPorId_Encontrado() throws Exception {
        when(livroService.buscarPorId(1L)).thenReturn(Optional.of(livro));

        mockMvc.perform(get("/api/livros/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.titulo").value("Dom Casmurro"))
                .andExpect(jsonPath("$.autor").value("Machado de Assis"))
                .andExpect(jsonPath("$.ano").value(1899));

        verify(livroService, times(1)).buscarPorId(1L);
    }

    @Test
    void testBuscarPorId_NaoEncontrado() throws Exception {
        when(livroService.buscarPorId(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/livros/999"))
                .andExpect(status().isNotFound());

        verify(livroService, times(1)).buscarPorId(999L);
    }

    @Test
    void testCriar() throws Exception {
        Livro novoLivro = new Livro(null, "Memórias Póstumas de Brás Cubas", "Machado de Assis", 1881, true);
        Livro livroCriado = new Livro(1L, "Memórias Póstumas de Brás Cubas", "Machado de Assis", 1881, true);

        when(livroService.criar(any(Livro.class))).thenReturn(livroCriado);

        mockMvc.perform(post("/api/livros")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(novoLivro)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.titulo").value("Memórias Póstumas de Brás Cubas"))
                .andExpect(jsonPath("$.autor").value("Machado de Assis"))
                .andExpect(jsonPath("$.ano").value(1881));

        verify(livroService, times(1)).criar(any(Livro.class));
    }

    @Test
    void testCriar_ValidacaoFalha() throws Exception {
        Livro livroInvalido = new Livro(null, "", "", null, true);

        mockMvc.perform(post("/api/livros")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(livroInvalido)))
                .andExpect(status().isBadRequest());

        verify(livroService, never()).criar(any(Livro.class));
    }

    @Test
    void testAtualizar_Encontrado() throws Exception {
        Livro livroAtualizado = new Livro(1L, "Dom Casmurro - Edição Especial", "Machado de Assis", 1899, false);

        when(livroService.atualizar(1L, any(Livro.class))).thenReturn(Optional.of(livroAtualizado));

        mockMvc.perform(put("/api/livros/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(livroAtualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo").value("Dom Casmurro - Edição Especial"))
                .andExpect(jsonPath("$.disponivel").value(false));

        verify(livroService, times(1)).atualizar(eq(1L), any(Livro.class));
    }

    @Test
    void testAtualizar_NaoEncontrado() throws Exception {
        when(livroService.atualizar(999L, any(Livro.class))).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/livros/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(livro)))
                .andExpect(status().isNotFound());

        verify(livroService, times(1)).atualizar(eq(999L), any(Livro.class));
    }

    @Test
    void testDeletar_Encontrado() throws Exception {
        when(livroService.deletar(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/livros/1"))
                .andExpect(status().isNoContent());

        verify(livroService, times(1)).deletar(1L);
    }

    @Test
    void testDeletar_NaoEncontrado() throws Exception {
        when(livroService.deletar(999L)).thenReturn(false);

        mockMvc.perform(delete("/api/livros/999"))
                .andExpect(status().isNotFound());

        verify(livroService, times(1)).deletar(999L);
    }
}

