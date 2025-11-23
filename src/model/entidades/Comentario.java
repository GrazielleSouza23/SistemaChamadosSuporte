package model.entidades;

import java.time.LocalDateTime;

/**
 * Classe de Entidade Comentario.
 * Representa um comentário (público ou interno) em um Ticket.
 */
public class Comentario {
    
    private int idComentario;
    private String conteudo;
    private String tipo; // Ex: "PUBLICO", "INTERNO"
    private LocalDateTime dataComentario;
    private int idTicket;
    private int idAutor; // ID da Pessoa (Usuário ou Agente) que escreveu o comentário

    // Construtor completo
    public Comentario(int idComentario, String conteudo, String tipo, LocalDateTime dataComentario, int idTicket, int idAutor) {
        this.idComentario = idComentario;
        this.conteudo = conteudo;
        this.tipo = tipo;
        this.dataComentario = dataComentario;
        this.idTicket = idTicket;
        this.idAutor = idAutor;
    }
    
    // Construtor para criação de novo comentário
    public Comentario(String conteudo, String tipo, int idTicket, int idAutor) {
        this.conteudo = conteudo;
        this.tipo = tipo;
        this.idTicket = idTicket;
        this.idAutor = idAutor;
        this.dataComentario = LocalDateTime.now();
    }

    // Getters e Setters
    public int getIdComentario() {
        return idComentario;
    }

    public void setIdComentario(int idComentario) {
        this.idComentario = idComentario;
    }

    public String getConteudo() {
        return conteudo;
    }

    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public LocalDateTime getDataComentario() {
        return dataComentario;
    }

    public void setDataComentario(LocalDateTime dataComentario) {
        this.dataComentario = dataComentario;
    }

    public int getIdTicket() {
        return idTicket;
    }

    public void setIdTicket(int idTicket) {
        this.idTicket = idTicket;
    }

    public int getIdAutor() {
        return idAutor;
    }

    public void setIdAutor(int idAutor) {
        this.idAutor = idAutor;
    }
}
