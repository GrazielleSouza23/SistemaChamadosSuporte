package control;

import model.dao.PessoaDAO;
import model.dao.DAOException;
import util.GerenciadorSenha;

/**
 * Controlador responsável pela lógica de negócio relacionada a Pessoas.
 */
public class ControlePessoa {

    private PessoaDAO pessoaDAO;

    public ControlePessoa() {
        this.pessoaDAO = new PessoaDAO();
    }

    /**
     * Atualiza o perfil de uma pessoa (Usuário ou Agente).
     */
    public boolean atualizarPerfil(int id, String nome, String email, String novaSenha)
            throws DAOException, IllegalArgumentException {

        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("O campo 'Nome' não pode estar vazio.");
        }

        if (email == null || !email.matches("^[^@]+@[^@]+\\.[^@]{2,}$")) {
            throw new IllegalArgumentException("O campo 'Email' parece ser inválido.");
        }

        String senhaParaSalvar = null;
        if (novaSenha != null && !novaSenha.isEmpty()) {
            senhaParaSalvar = GerenciadorSenha.hashSenha(novaSenha);
        }

        return pessoaDAO.atualizarPerfil(id, nome.trim(), email.trim(), senhaParaSalvar);
    }
}