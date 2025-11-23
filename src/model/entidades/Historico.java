package model.entidades;

import java.time.LocalDateTime;

/**
 * Classe de Entidade Historico.
 * Representa um registro de ação no histórico de um Ticket.
 */
public class Historico {
    
    private int idHistorico;
    private String acao; // Ex: 'STATUS_ALTERADO', 'COMENTARIO_ADICIONADO', 'TICKET_ATRIBUIDO'
    private String descricao;
    private LocalDateTime dataRegistro;
    private int idTicket;
    private int idResponsavel;

    // Construtor completo
    public Historico(int idHistorico, String acao, String descricao, LocalDateTime dataRegistro, int idTicket, int idResponsavel) {
        this.idHistorico = idHistorico;
        this.acao = acao;
        this.descricao = descricao;
        this.dataRegistro = dataRegistro;
        this.idTicket = idTicket;
        this.idResponsavel = idResponsavel;
    }
    
    // Construtor para criação de novo registro
    public Historico(String acao, String descricao, int idTicket, int idResponsavel) {
        this.acao = acao;
        this.descricao = descricao;
        this.idTicket = idTicket;
        this.idResponsavel = idResponsavel;
        this.dataRegistro = LocalDateTime.now();
    }

    // Getters e Setters
    public int getIdHistorico() {
        return idHistorico;
    }

    public void setIdHistorico(int idHistorico) {
        this.idHistorico = idHistorico;
    }

    public String getAcao() {
        return acao;
    }

    public void setAcao(String acao) {
        this.acao = acao;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public LocalDateTime getDataRegistro() {
        return dataRegistro;
    }

    public void setDataRegistro(LocalDateTime dataRegistro) {
        this.dataRegistro = dataRegistro;
    }

    public int getIdTicket() {
        return idTicket;
    }

    public void setIdTicket(int idTicket) {
        this.idTicket = idTicket;
    }

    public int getIdResponsavel() {
        return idResponsavel;
    }

    public void setIdResponsavel(int idResponsavel) {
        this.idResponsavel = idResponsavel;
    }
}
