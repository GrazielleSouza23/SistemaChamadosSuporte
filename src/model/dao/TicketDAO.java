package model.dao;

import model.entidades.Ticket;
import util.CriptografiaUtil;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO da entidade Ticket.
 * Inclui criptografia AES em campos sensíveis (Nível 3).
 */
public class TicketDAO {

    public TicketDAO() {}

    /**
     * Salva um novo Ticket no banco.
     */
    public int salvar(Ticket ticket) {
        String sql = "INSERT INTO Ticket (titulo, descricao, status, categoria, prioridade, id_usuario_abertura) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConexaoBD.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, ticket.getTitulo());

            // Criptografia AES
            String descricaoCripto = CriptografiaUtil.encrypt(ticket.getDescricao());
            stmt.setString(2, descricaoCripto);

            stmt.setString(3, ticket.getStatus());
            stmt.setString(4, ticket.getCategoria());
            stmt.setString(5, ticket.getPrioridade());
            stmt.setInt(6, ticket.getIdUsuarioAbertura());

            int linhas = stmt.executeUpdate();

            if (linhas > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        int id = rs.getInt(1);
                        ticket.setIdTicket(id);
                        return id;
                    }
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao salvar Ticket: " + e.getMessage());
        }
        return -1;
    }

    /**
     * Busca um Ticket pelo ID.
     */
    public Ticket buscarPorId(int idTicket) {
        String sql = "SELECT * FROM Ticket WHERE id_ticket = ?";

        try (Connection conn = ConexaoBD.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idTicket);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToTicket(rs);
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar Ticket por ID: " + e.getMessage());
        }
        return null;
    }

    /**
     * Lista todos os Tickets.
     */
    public List<Ticket> listarTodos() {
        String sql = "SELECT * FROM Ticket ORDER BY prioridade DESC, data_abertura ASC";
        List<Ticket> lista = new ArrayList<>();

        try (Connection conn = ConexaoBD.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(mapResultSetToTicket(rs));
            }

        } catch (SQLException e) {
            System.err.println("Erro ao listar Tickets: " + e.getMessage());
        }
        return lista;
    }

    /**
     * Lista Tickets por usuário.
     */
    public List<Ticket> listarPorUsuario(int idUsuario) {
        String sql = "SELECT * FROM Ticket WHERE id_usuario_abertura = ? ORDER BY data_abertura DESC";
        List<Ticket> lista = new ArrayList<>();

        try (Connection conn = ConexaoBD.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idUsuario);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapResultSetToTicket(rs));
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao listar Tickets por usuário: " + e.getMessage());
        }
        return lista;
    }

    /**
     * Mapeamento + DESCRIPTOGRAFIA AES.
     */
    private Ticket mapResultSetToTicket(ResultSet rs) throws SQLException {

        int idTicket = rs.getInt("id_ticket");
        String titulo = rs.getString("titulo");

        // Descriptografia
        String descricaoCripto = rs.getString("descricao");
        String descricao = descricaoCripto != null ? CriptografiaUtil.decrypt(descricaoCripto) : null;

        String status = rs.getString("status");
        String categoria = rs.getString("categoria");
        String prioridade = rs.getString("prioridade");

        Timestamp tsAbertura = rs.getTimestamp("data_abertura");
        Timestamp tsFechamento = rs.getTimestamp("data_fechamento");

        LocalDateTime dataAbertura = tsAbertura != null ? tsAbertura.toLocalDateTime() : null;
        LocalDateTime dataFechamento = tsFechamento != null ? tsFechamento.toLocalDateTime() : null;

        int idUsuarioAbertura = rs.getInt("id_usuario_abertura");

        Integer idAgente = rs.getInt("id_agente_responsavel");
        if (rs.wasNull()) idAgente = null;

        return new Ticket(idTicket, titulo, descricao, status, categoria, prioridade,
                dataAbertura, dataFechamento, idUsuarioAbertura, idAgente);
    }

    public boolean atualizarStatusAgente(int idTicket, String novoStatus, int idAgente) {
        String sql = "UPDATE Ticket SET status = ?, id_agente_responsavel = ? WHERE id_ticket = ?";

        try (Connection conn = ConexaoBD.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, novoStatus);
            stmt.setInt(2, idAgente);
            stmt.setInt(3, idTicket);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar status/agente: " + e.getMessage());
            return false;
        }
    }

    public boolean atualizarStatus(int idTicket, String novoStatus) {
        String sql = "UPDATE Ticket SET status = ? WHERE id_ticket = ?";

        try (Connection conn = ConexaoBD.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, novoStatus);
            stmt.setInt(2, idTicket);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar status: " + e.getMessage());
            return false;
        }
    }

    public boolean fecharTicket(int idTicket) {
        String sql = "UPDATE Ticket SET status = 'FECHADO', data_fechamento = CURRENT_TIMESTAMP WHERE id_ticket = ?";

        try (Connection conn = ConexaoBD.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idTicket);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao fechar ticket: " + e.getMessage());
            return false;
        }
    }
}
