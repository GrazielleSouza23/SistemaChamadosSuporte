package model.entidades;

import java.time.LocalDateTime;

/**
 * Classe de Entidade Anexo.
 * Representa um arquivo anexado a um Ticket.
 */
public class Anexo {
    
    private int idAnexo;
    private String nomeArquivo;
    private String tipoArquivo;
    private double tamanho;
    private LocalDateTime dataUpload;
    private String caminhoArmazenamento; // Caminho físico ou URL
    private int idTicket;

    // Construtor completo
    public Anexo(int idAnexo, String nomeArquivo, String tipoArquivo, double tamanho, LocalDateTime dataUpload, String caminhoArmazenamento, int idTicket) {
        this.idAnexo = idAnexo;
        this.nomeArquivo = nomeArquivo;
        this.tipoArquivo = tipoArquivo;
        this.tamanho = tamanho;
        this.dataUpload = dataUpload;
        this.caminhoArmazenamento = caminhoArmazenamento;
        this.idTicket = idTicket;
    }
    
    // Construtor para criação de novo anexo
    public Anexo(String nomeArquivo, String tipoArquivo, double tamanho, String caminhoArmazenamento, int idTicket) {
        this.nomeArquivo = nomeArquivo;
        this.tipoArquivo = tipoArquivo;
        this.tamanho = tamanho;
        this.caminhoArmazenamento = caminhoArmazenamento;
        this.idTicket = idTicket;
        this.dataUpload = LocalDateTime.now();
    }

    // Getters e Setters
    public int getIdAnexo() {
        return idAnexo;
    }

    public void setIdAnexo(int idAnexo) {
        this.idAnexo = idAnexo;
    }

    public String getNomeArquivo() {
        return nomeArquivo;
    }

    public void setNomeArquivo(String nomeArquivo) {
        this.nomeArquivo = nomeArquivo;
    }

    public String getTipoArquivo() {
        return tipoArquivo;
    }

    public void setTipoArquivo(String tipoArquivo) {
        this.tipoArquivo = tipoArquivo;
    }

    public double getTamanho() {
        return tamanho;
    }

    public void setTamanho(double tamanho) {
        this.tamanho = tamanho;
    }

    public LocalDateTime getDataUpload() {
        return dataUpload;
    }

    public void setDataUpload(LocalDateTime dataUpload) {
        this.dataUpload = dataUpload;
    }

    public String getCaminhoArmazenamento() {
        return caminhoArmazenamento;
    }

    public void setCaminhoArmazenamento(String caminhoArmazenamento) {
        this.caminhoArmazenamento = caminhoArmazenamento;
    }

    public int getIdTicket() {
        return idTicket;
    }

    public void setIdTicket(int idTicket) {
        this.idTicket = idTicket;
    }
}
