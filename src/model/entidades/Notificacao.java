package model.entidades;

import java.time.LocalDateTime;

/**
 * Classe de Entidade Notificacao.
 * Representa uma notificação para um Usuário ou Agente.
 */
public class Notificacao {

    private int idNotificacao;
    private String tipo;
    private String conteudo;
    private boolean lido;
    private LocalDateTime dataEnvio;
    private int idDestinatario;
    private Integer idTicket;

    // Construtor completo (usado ao buscar do banco)
    public Notificacao(int idNotificacao, String tipo, String conteudo, boolean lido, LocalDateTime dataEnvio, int idDestinatario, Integer idTicket) {
        this.idNotificacao = idNotificacao;
        this.tipo = tipo;
        this.conteudo = conteudo;
        this.lido = lido;
        this.dataEnvio = dataEnvio;
        this.idDestinatario = idDestinatario;
        this.idTicket = idTicket;
    }

    // Construtor para criar nova notificação
    public Notificacao(String tipo, String conteudo, int idDestinatario, Integer idTicket) {
        this.tipo = tipo;
        this.conteudo = conteudo;
        this.lido = false;
        this.dataEnvio = LocalDateTime.now();
        this.idDestinatario = idDestinatario;
        this.idTicket = idTicket;
    }

    // Getters e Setters
    public int getIdNotificacao() {
        return idNotificacao;
    }

    public void setIdNotificacao(int idNotificacao) {
        this.idNotificacao = idNotificacao;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getConteudo() {
        return conteudo;
    }

    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }

    public boolean isLido() {
        return lido;
    }

    public void setLido(boolean lido) {
        this.lido = lido;
    }

    public LocalDateTime getDataEnvio() {
        return dataEnvio;
    }

    public void setDataEnvio(LocalDateTime dataEnvio) {
        this.dataEnvio = dataEnvio;
    }

    public int getIdDestinatario() {
        return idDestinatario;
    }

    public void setIdDestinatario(int idDestinatario) {
        this.idDestinatario = idDestinatario;
    }

    public Integer getIdTicket() {
        return idTicket;
    }

    public void setIdTicket(Integer idTicket) {
        this.idTicket = idTicket;
    }
}