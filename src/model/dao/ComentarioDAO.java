package model.dao;

import model.entidades.Comentario;
import util.CriptografiaUtil;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para Comentario utilizando HikariCP + Criptografia AES (Nível 3).
 */
public class ComentarioDAO {

    /**
     * Salva um novo Comentário no banco com criptografia AES.
     */
    public int salvar(Comentario comentario) {

        String sql = "INSERT INTO Comentario (conteudo, tipo, id_ticket, id_autor) " +
                     "VALUES (?, ?, ?, ?)";

        try (Connection connection = ConexaoBD.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            // Criptografa antes de salvar
            String conteudoCriptografado = CriptografiaUtil.encrypt(comentario.getConteudo());
            stmt.setString(1, conteudoCriptografado);

            stmt.setString(2, comentario.getTipo());
            stmt.setInt(3, comentario.getIdTicket());
            stmt.setInt(4, comentario.getIdAutor());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        int id = rs.getInt(1);
                        comentario.setIdComentario(id);
                        return id;
                    }
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao salvar Comentário: " + e.getMessage());
            e.printStackTrace();
        }

        return -1;
    }

    /**
     * Lista todos os comentários de um ticket.
     */
    public List<Comentario> listarPorTicket(int idTicket) {

        List<Comentario> lista = new ArrayList<>();
        String sql = "SELECT * FROM Comentario WHERE id_ticket = ? ORDER BY data_comentario ASC";

        try (Connection connection = ConexaoBD.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, idTicket);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapResultSetToComentario(rs));
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao listar Comentários: " + e.getMessage());
            e.printStackTrace();
        }

        return lista;
    }

    /**
     * Lista apenas comentários públicos.
     */
    public List<Comentario> listarPublicosPorTicket(int idTicket) {

        List<Comentario> lista = new ArrayList<>();
        String sql = "SELECT * FROM Comentario WHERE id_ticket = ? AND tipo = 'PUBLICO' ORDER BY data_comentario ASC";

        try (Connection connection = ConexaoBD.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, idTicket);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapResultSetToComentario(rs));
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao listar Comentários Públicos: " + e.getMessage());
            e.printStackTrace();
        }

        return lista;
    }


    /**
     * Converte um ResultSet em um objeto Comentario com descriptografia AES.
     */
    private Comentario mapResultSetToComentario(ResultSet rs) throws SQLException {

        int idComentario = rs.getInt("id_comentario");

        // Descriptografa o conteúdo ao ler
        String conteudoCriptografado = rs.getString("conteudo");
        String conteudo = CriptografiaUtil.decrypt(conteudoCriptografado);

        String tipo = rs.getString("tipo");

        Timestamp ts = rs.getTimestamp("data_comentario");
        LocalDateTime data = ts != null ? ts.toLocalDateTime() : null;

        int idTicket = rs.getInt("id_ticket");
        int idAutor = rs.getInt("id_autor");

        return new Comentario(idComentario, conteudo, tipo, data, idTicket, idAutor);
    }
}
