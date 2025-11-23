package control;

import model.dao.PessoaDAO;
import model.dao.DAOException;
import util.GerenciadorSenha;

/**
 * Controlador responsável pela lógica de negócio relacionada a Pessoas.
 * Usar Hashing (Bcrypt) ao atualizar perfil (Nível 1).
 */
public class ControlePessoa {

    private PessoaDAO pessoaDAO;

    public ControlePessoa() {
        this.pessoaDAO = new PessoaDAO();
    }

    /**
     * Atualiza o perfil de uma pessoa (Usuário ou Agente).
     */
    public boolean atualizarPerfil(int id, String nome, String email, String novaSenha) throws DAOException, IllegalArgumentException {

        // 1. Validação dos Campos
        if (nome == null || nome.trim().isEmpty()) { //
            throw new IllegalArgumentException("O campo 'Nome' não pode estar vazio.");
        }
        if (email == null || email.trim().isEmpty() || !email.contains("@")) { //
            throw new IllegalArgumentException("O campo 'Email' parece ser inválido.");
        }

        // 2. Preparação da Senha
        String senhaParaSalvar = null;
        if (novaSenha != null && !novaSenha.isEmpty()) { //
            senhaParaSalvar = GerenciadorSenha.hashSenha(novaSenha);
        }

        // 3. Chamada ao DAO
        return pessoaDAO.atualizarPerfil(id, nome.trim(), email.trim(), senhaParaSalvar); //
    }
}