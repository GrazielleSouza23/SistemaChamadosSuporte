package control;

import model.dao.PessoaDAO;
import model.dao.DAOException;
import model.entidades.Pessoa;

public class ControleLogin {

    private PessoaDAO pessoaDAO;

    public ControleLogin() {
        this.pessoaDAO = new PessoaDAO();
    }

    public Pessoa fazerLogin(String email, String senha) throws DAOException {
        
        // 1. Busca a pessoa no banco (pode lançar DAOException agora)
        Pessoa pessoaAutenticada = pessoaDAO.buscarPorEmailESenha(email, senha);

        // 2. Verifica se a pessoa foi encontrada
        if (pessoaAutenticada != null) {
            return PessoaFactory.criarPessoa(pessoaAutenticada); 
        }
        
        return null;
    }
}