package model.entidades;

import java.time.LocalDateTime;

/**
 * Classe abstrata Pessoa.
 * Serve como superclasse para Usuario e Agente, demonstrando o conceito de Herança.
 * Contém atributos e métodos comuns a todos os usuários do sistema.
 */
public abstract class Pessoa {
    
    protected int id;
    protected String nome;
    protected String email;
    protected String senha; // Em um sistema real, seria o hash da senha
    protected LocalDateTime dataCadastro;

    // Construtor completo
    public Pessoa(int id, String nome, String email, String senha, LocalDateTime dataCadastro) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.dataCadastro = dataCadastro;
    }
    
    // Construtor para criação de nova pessoa (sem ID e data de cadastro)
    public Pessoa(String nome, String email, String senha) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        // O ID e a data de cadastro serão definidos pelo banco de dados
    }

    /**
     * Método abstrato para demonstrar Polimorfismo.
     * Cada subclasse (Usuario e Agente) terá uma implementação específica.
     */
    public abstract String getTipoUsuario();
    
    /**
     * Método comum para autenticação (lógica simplificada).
     * @param email O email fornecido.
     * @param senha A senha fornecida.
     * @return true se as credenciais forem válidas (a validação real será no DAO).
     */
    public boolean autenticar(String email, String senha) {
        // Lógica de autenticação (comparação de hash) seria implementada aqui.
        // Por enquanto, apenas verifica se o email e senha correspondem aos dados do objeto.
        return this.email.equals(email) && this.senha.equals(senha);
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public LocalDateTime getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(LocalDateTime dataCadastro) {
        this.dataCadastro = dataCadastro;
    }
}
