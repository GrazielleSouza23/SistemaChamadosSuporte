package model.dao;

import model.entidades.Historico;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HistoricoDAO {

    private static final Logger logger = Logger.getLogger(HistoricoDAO.class.getName());


    /**
     * Salva um novo registro de Histórico no banco de dados.
     */
    public int salvar(Historico historico) {
        String sql = "INSERT INTO Historico (acao, descricao, id_ticket, id_responsavel) VALUES (?, ?, ?, ?)";
        int idGerado = -1;

        try (Connection connection = ConexaoBD.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, historico.getAcao());
            stmt.setString(2, historico.getDescricao());
            stmt.setInt(3, historico.getIdTicket());
            stmt.setInt(4, historico.getIdResponsavel());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        idGerado = rs.getInt(1);
                        historico.setIdHistorico(idGerado);
                    }
                }
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Erro ao salvar Histórico", e);
        }

        return idGerado;
    }

    /**
     * Lista todos os registros de Histórico de um Ticket específico.
     */
    public List<Historico> listarPorTicket(int idTicket) {
        List<Historico> historicos = new ArrayList<>();

        String sql =
            "SELECT id_historico, acao, descricao, data_registro, id_ticket, id_responsavel " +
            "FROM Historico WHERE id_ticket = ? ORDER BY data_registro ASC";

        try (Connection connection = ConexaoBD.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, idTicket);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    historicos.add(mapResultSetToHistorico(rs));
                }
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Erro ao listar Histórico por Ticket", e);
        }

        return historicos;
    }

    /**
     * Mapeia um ResultSet em um objeto Historico.
     */
    private Historico mapResultSetToHistorico(ResultSet rs) throws SQLException {

        int idHistorico = rs.getInt("id_historico");
        String acao = rs.getString("acao");
        String descricao = rs.getString("descricao");

        Timestamp tsRegistro = rs.getTimestamp("data_registro");
        LocalDateTime dataRegistro = tsRegistro != null ? tsRegistro.toLocalDateTime() : null;

        int idTicket = rs.getInt("id_ticket");
        int idResponsavel = rs.getInt("id_responsavel");

        return new Historico(idHistorico, acao, descricao, dataRegistro, idTicket, idResponsavel);
    }
}