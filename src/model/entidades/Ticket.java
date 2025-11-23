package model.entidades;

import java.time.LocalDateTime;

/**
 * Classe de Entidade Ticket.
 * Representa um chamado de suporte.
 */
public class Ticket {
    
    private int idTicket;
    private String titulo;
    private String descricao;
    private String status; // Ex: ABERTO, EM_ANDAMENTO, RESOLVIDO, FECHADO
    private String categoria;
    private String prioridade; // Ex: BAIXA, MEDIA, ALTA, URGENTE
    private LocalDateTime dataAbertura;
    private LocalDateTime dataFechamento;
    private int idUsuarioAbertura;
    private Integer idAgenteResponsavel; // Integer para permitir valor null

    // Construtor completo
    public Ticket(int idTicket, String titulo, String descricao, String status, String categoria, String prioridade, LocalDateTime dataAbertura, LocalDateTime dataFechamento, int idUsuarioAbertura, Integer idAgenteResponsavel) {
        this.idTicket = idTicket;
        this.titulo = titulo;
        this.descricao = descricao;
        this.status = status;
        this.categoria = categoria;
        this.prioridade = prioridade;
        this.dataAbertura = dataAbertura;
        this.dataFechamento = dataFechamento;
        this.idUsuarioAbertura = idUsuarioAbertura;
        this.idAgenteResponsavel = idAgenteResponsavel;
    }
    
    // Construtor para abertura de novo ticket (sem ID, datas e agente)
    public Ticket(String titulo, String descricao, String categoria, String prioridade, int idUsuarioAbertura) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.categoria = categoria;
        this.prioridade = prioridade;
        this.idUsuarioAbertura = idUsuarioAbertura;
        this.status = "ABERTO"; // Status inicial
        this.dataAbertura = LocalDateTime.now();
        this.idAgenteResponsavel = null;
        this.dataFechamento = null;
    }

    // Getters e Setters
    public int getIdTicket() {
        return idTicket;
    }

    public void setIdTicket(int idTicket) {
        this.idTicket = idTicket;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getPrioridade() {
        return prioridade;
    }

    public void setPrioridade(String prioridade) {
        this.prioridade = prioridade;
    }

    public LocalDateTime getDataAbertura() {
        return dataAbertura;
    }

    public void setDataAbertura(LocalDateTime dataAbertura) {
        this.dataAbertura = dataAbertura;
    }

    public LocalDateTime getDataFechamento() {
        return dataFechamento;
    }

    public void setDataFechamento(LocalDateTime dataFechamento) {
        this.dataFechamento = dataFechamento;
    }

    public int getIdUsuarioAbertura() {
        return idUsuarioAbertura;
    }

    public void setIdUsuarioAbertura(int idUsuarioAbertura) {
        this.idUsuarioAbertura = idUsuarioAbertura;
    }

    public Integer getIdAgenteResponsavel() {
        return idAgenteResponsavel;
    }

    public void setIdAgenteResponsavel(Integer idAgenteResponsavel) {
        this.idAgenteResponsavel = idAgenteResponsavel;
    }

    public int getIdUsuario() {
        throw new UnsupportedOperationException("Sem suporte ainda."); 
    }
}
