package model.dao;

import model.entidades.Notificacao;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NotificacaoDAO {

    private static final Logger logger = Logger.getLogger(NotificacaoDAO.class.getName());
    private final Connection connection;

    public NotificacaoDAO() {
        this.connection = ConexaoBD.getInstance().getConnection();
    }

    /**
     * Salva uma nova notificação no banco.
     */
    public int salvar(Notificacao notificacao) {
        String sql = "INSERT INTO Notificacao (tipo, conteudo, id_destinatario, id_ticket) VALUES (?, ?, ?, ?)";
        int idGerado = -1;

        try (PreparedStatement stmt = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, notificacao.getTipo());
            stmt.setString(2, notificacao.getConteudo());
            stmt.setInt(3, notificacao.getIdDestinatario());

            if (notificacao.getIdTicket() != null && notificacao.getIdTicket() > 0) {
                stmt.setInt(4, notificacao.getIdTicket());
            } else {
                stmt.setNull(4, Types.INTEGER);
            }

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        idGerado = rs.getInt(1);
                        notificacao.setIdNotificacao(idGerado);
                    }
                }
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Erro ao salvar notificação", e);
        }

        return idGerado;
    }

    /**
     * Lista notificações não lidas de um destinatário.
     */
    public List<Notificacao> listarNaoLidasPorDestinatario(int idDestinatario) {
        List<Notificacao> notificacoes = new ArrayList<>();

        String sql =
                "SELECT id_notificacao, tipo, conteudo, lido, data_envio, id_destinatario, id_ticket " +
                "FROM Notificacao WHERE id_destinatario = ? AND lido = FALSE " +
                "ORDER BY data_envio DESC";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idDestinatario);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    notificacoes.add(mapResultSetToNotificacao(rs));
                }
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Erro ao listar notificações não lidas", e);
        }

        return notificacoes;
    }

    /**
     * Marca uma notificação como lida.
     */
    public boolean marcarComoLida(int idNotificacao) {
        String sql = "UPDATE Notificacao SET lido = TRUE WHERE id_notificacao = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idNotificacao);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Erro ao marcar notificação como lida", e);
            return false;
        }
    }

    /**
     * Mapeia o ResultSet para um objeto Notificacao.
     */
    private Notificacao mapResultSetToNotificacao(ResultSet rs) throws SQLException {

        int idNotificacao = rs.getInt("id_notificacao");
        String tipo = rs.getString("tipo");
        String conteudo = rs.getString("conteudo");
        boolean lido = rs.getBoolean("lido");
        int idDestinatario = rs.getInt("id_destinatario");

        Integer idTicket = rs.getObject("id_ticket") != null ?
                rs.getInt("id_ticket") : null;

        Timestamp tsEnvio = rs.getTimestamp("data_envio");
        LocalDateTime dataEnvio = tsEnvio != null ? tsEnvio.toLocalDateTime() : null;

        return new Notificacao(idNotificacao, tipo, conteudo, lido, dataEnvio, idDestinatario, idTicket);
    }
}
