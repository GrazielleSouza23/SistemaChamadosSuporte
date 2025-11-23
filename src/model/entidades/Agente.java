package model.entidades;

import java.time.LocalDateTime;

/**
 * Subclasse Agente.
 * Herda de Pessoa e adiciona o atributo nivelAcesso.
 */
public class Agente extends Pessoa {

    private String nivelAcesso; // Ex: BASICO, AVANCADO, ADMIN

    // Construtor completo
    public Agente(int id, String nome, String email, String senha, LocalDateTime dataCadastro, String nivelAcesso) {
        super(id, nome, email, senha, dataCadastro);
        this.nivelAcesso = nivelAcesso;
    }
    
    // Construtor para criação de novo agente
    public Agente(String nome, String email, String senha, String nivelAcesso) {
        super(nome, email, senha);
        this.nivelAcesso = nivelAcesso;
    }

    /**
     * Implementação do método abstrato da superclasse (Polimorfismo).
     * @return O tipo de usuário.
     */
    @Override
    public String getTipoUsuario() {
        return "AGENTE";
    }

    // Getters e Setters
    public String getNivelAcesso() {
        return nivelAcesso;
    }

    public void setNivelAcesso(String nivelAcesso) {
        this.nivelAcesso = nivelAcesso;
    }
    
    // Métodos específicos do Agente (atribuirTicket, fecharTicket, etc.)
    // A lógica de negócio será implementada na camada de Controle.
}
