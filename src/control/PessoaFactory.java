package control;

import model.entidades.Pessoa;
import model.entidades.Usuario;
import model.entidades.Agente;

public class PessoaFactory {

    public static Pessoa criarPessoa(Pessoa pessoa) {
        if (pessoa instanceof Agente) {
            return (Agente) pessoa;
        } else if (pessoa instanceof Usuario) {
            return (Usuario) pessoa;
        } else {
            return null;
        }
    }
    
    public static Pessoa criarPessoa(String tipo, String nome, String email, String senha, String nivelAcesso) {
        if (tipo.equalsIgnoreCase("AGENTE")) {
            return new Agente(nome, email, senha, nivelAcesso);
        } else if (tipo.equalsIgnoreCase("USUARIO")) {
            return new Usuario(nome, email, senha);
        } else {
            throw new IllegalArgumentException("Tipo de pessoa inválido: " + tipo);
        }
    }
}