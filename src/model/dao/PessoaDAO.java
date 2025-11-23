package model.dao;

import model.entidades.Pessoa;
import model.entidades.Usuario;
import model.entidades.Agente;
import model.dao.DAOException;
import util.GerenciadorSenha;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PessoaDAO {

    // O formatter não precisa ser recriado a cada chamada, static final é mais eficiente
    private static final DateTimeFormatter FORMATTER = 
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Busca uma pessoa pelo email e valida a senha.
     * @throws DAOException em caso de erro de conexão ou consulta SQL.
     */
    public Pessoa buscarPorEmailESenha(String email, String senhaPlana) throws DAOException {

        String sql = "SELECT p.*, a.nivel_acesso FROM Pessoa p " +
                "LEFT JOIN Agente a ON p.id_pessoa = a.id_agente " +
                "WHERE p.email = ?";

        try (
            Connection conn = ConexaoBD.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setString(1, email);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String hashDB = rs.getString("senha");

                    if (GerenciadorSenha.verificarSenha(senhaPlana, hashDB)) {
                        return mapResultSetToPessoa(rs);
                    }
                }
            }

        } catch (SQLException e) {
            throw new DAOException("Erro ao buscar pessoa por email: " + e.getMessage(), e);
        }

        return null; // Retorna null apenas se não achar, não se der erro.
    }

    private Pessoa mapResultSetToPessoa(ResultSet rs) throws SQLException {
        int id = rs.getInt("id_pessoa");
        String nome = rs.getString("nome");
        String emailDB = rs.getString("email");
        String senhaDB = rs.getString("senha");

        Timestamp tsCadastro = rs.getTimestamp("data_cadastro");
        LocalDateTime dataCadastro = tsCadastro != null ? tsCadastro.toLocalDateTime() : null;

        String nivelAcesso = rs.getString("nivel_acesso");

        if (nivelAcesso != null) {
            return new Agente(id, nome, emailDB, senhaDB, dataCadastro, nivelAcesso);
        } else {
            return new Usuario(id, nome, emailDB, senhaDB, dataCadastro);
        }
    }

    /**
     * Busca apenas o nome pelo ID.
     * @throws DAOException encapsulando erros de SQL.
     */
    public String buscarNomePorId(int id) throws DAOException {
        String sql = "SELECT nome FROM Pessoa WHERE id_pessoa = ?";

        try (
            Connection conn = ConexaoBD.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("nome");
                }
            }

        } catch (SQLException e) {
            throw new DAOException("Erro ao recuperar nome do usuário (ID: " + id + ")", e);
        }
        return null;
    }

    /**
     * Atualiza dados do perfil.
     * @throws DAOException encapsulando erros de SQL.
     */
    public boolean atualizarPerfil(int id, String nome, String email, String senhaParaSalvar)
            throws DAOException {

        String sqlComSenha =
                "UPDATE pessoa SET nome = ?, email = ?, senha = ? WHERE id_pessoa = ?";

        String sqlSemSenha =
                "UPDATE pessoa SET nome = ?, email = ? WHERE id_pessoa = ?";

        try (
            Connection conn = ConexaoBD.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement(
                    (senhaParaSalvar != null && !senhaParaSalvar.isEmpty())
                            ? sqlComSenha : sqlSemSenha
            )
        ) {

            stmt.setString(1, nome);
            stmt.setString(2, email);

            if (senhaParaSalvar != null && !senhaParaSalvar.isEmpty()) {
                stmt.setString(3, senhaParaSalvar);
                stmt.setInt(4, id);
            } else {
                stmt.setInt(3, id);
            }

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            // Substituimos a Exception genérica pela DAOException específica
            throw new DAOException("Falha ao atualizar perfil no banco de dados.", e);
        }
    }
}