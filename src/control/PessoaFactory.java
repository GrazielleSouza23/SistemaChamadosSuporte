package control;

import model.entidades.Pessoa;
import model.entidades.Usuario;
import model.entidades.Agente;

public class PessoaFactory {

    // CORREÇÃO: o else retornava null silenciosamente, o que causaria
    // NullPointerException no chamador (ControleLogin) sem nenhuma mensagem útil.
    // Agora lança IllegalArgumentException com uma descrição clara do problema.
    public static Pessoa criarPessoa(Pessoa pessoa) {
        if (pessoa instanceof Agente) {
            return (Agente) pessoa;
        } else if (pessoa instanceof Usuario) {
            return (Usuario) pessoa;
        } else {
            throw new IllegalArgumentException(
                "Tipo de Pessoa desconhecido: " + (pessoa != null ? pessoa.getClass().getName() : "null")
            );
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