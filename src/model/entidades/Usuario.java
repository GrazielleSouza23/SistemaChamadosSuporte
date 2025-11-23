package model.entidades;

import java.time.LocalDateTime;

/**
 * Subclasse Usuario.
 * Herda de Pessoa.
 */
public class Usuario extends Pessoa {

    // Construtor completo
    public Usuario(int id, String nome, String email, String senha, LocalDateTime dataCadastro) {
        super(id, nome, email, senha, dataCadastro);
    }
    
    // Construtor para criação de novo usuário
    public Usuario(String nome, String email, String senha) {
        super(nome, email, senha);
    }

    /**
     * Implementação do método abstrato da superclasse (Polimorfismo).
     * @return O tipo de usuário.
     */
    @Override
    public String getTipoUsuario() {
        return "USUARIO";
    }
    
    // Métodos específicos do Usuário (abrirTicket, consultarTicket, etc.)
    // A lógica de negócio será implementada na camada de Controle.
}
