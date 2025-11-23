package model.dao;

import model.entidades.Anexo;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para a entidade Anexo, utilizando Connection Pool (HikariCP).
 */
public class AnexoDAO {

    /**
     * Salva um novo Anexo no banco usando pool de conexões.
     * @param anexo Objeto Anexo a ser persistido
     * @return ID gerado ou -1 em caso de erro
     */
    public int salvar(Anexo anexo) {

        String sql = "INSERT INTO Anexo (nome_arquivo, tipo_arquivo, tamanho, caminho_armazenamento, id_ticket) " +
                     "VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = ConexaoBD.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, anexo.getNomeArquivo());
            stmt.setString(2, anexo.getTipoArquivo());
            stmt.setDouble(3, anexo.getTamanho());
            stmt.setString(4, anexo.getCaminhoArmazenamento());
            stmt.setInt(5, anexo.getIdTicket());

            int affected = stmt.executeUpdate();

            if (affected > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        int idGerado = rs.getInt(1);
                        anexo.setIdAnexo(idGerado);
                        return idGerado;
                    }
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao salvar Anexo: " + e.getMessage());
            e.printStackTrace();
        }
        return -1;
    }


    /**
     * Retorna todos os anexos de um ticket específico.
     */
    public List<Anexo> listarPorTicket(int idTicket) {

        List<Anexo> anexos = new ArrayList<>();
        String sql = "SELECT * FROM Anexo WHERE id_ticket = ? ORDER BY data_upload ASC";

        try (Connection connection = ConexaoBD.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, idTicket);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    anexos.add(mapResultSetToAnexo(rs));
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao listar Anexos: " + e.getMessage());
            e.printStackTrace();
        }

        return anexos;
    }


    /**
     * Mapeia o ResultSet para um objeto Anexo.
     */
    private Anexo mapResultSetToAnexo(ResultSet rs) throws SQLException {

        int idAnexo = rs.getInt("id_anexo");
        String nomeArquivo = rs.getString("nome_arquivo");
        String tipoArquivo = rs.getString("tipo_arquivo");
        double tamanho = rs.getDouble("tamanho");

        Timestamp tsUpload = rs.getTimestamp("data_upload");
        LocalDateTime dataUpload = tsUpload != null ? tsUpload.toLocalDateTime() : null;

        String caminhoArmazenamento = rs.getString("caminho_armazenamento");
        int idTicket = rs.getInt("id_ticket");

        return new Anexo(idAnexo, nomeArquivo, tipoArquivo, tamanho, dataUpload, caminhoArmazenamento, idTicket);
    }
}