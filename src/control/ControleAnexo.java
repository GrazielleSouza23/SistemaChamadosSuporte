package control;

import model.dao.AnexoDAO;
import model.dao.HistoricoDAO;
import model.entidades.Anexo;
import model.entidades.Historico;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;

/**
 * Classe de Controle (Controller) para a funcionalidade de Anexos.
 */
public class ControleAnexo {

    private AnexoDAO anexoDAO;
    private HistoricoDAO historicoDAO;

    private static final String DIRETORIO_UPLOAD = "/home/ubuntu/uploads/";

    public ControleAnexo() {
        this.anexoDAO = new AnexoDAO();
        this.historicoDAO = new HistoricoDAO();
        new File(DIRETORIO_UPLOAD).mkdirs();
    }

    /**
     * Adiciona um novo Anexo ao Ticket.
     */
    public int adicionarAnexo(int idTicket, File arquivo, int idAutor) {
        if (arquivo == null || !arquivo.exists()) {
            throw new IllegalArgumentException("Arquivo inválido ou não encontrado.");
        }

        String nomeArquivo = arquivo.getName();
        String tipoArquivo = getFileExtension(nomeArquivo);
        double tamanho = arquivo.length() / 1024.0;
        String caminhoArmazenamento = DIRETORIO_UPLOAD + idTicket + "_" + nomeArquivo;

        try {
            File destino = new File(caminhoArmazenamento);

            Files.copy(arquivo.toPath(), destino.toPath(), StandardCopyOption.REPLACE_EXISTING);

            Anexo novoAnexo = new Anexo(nomeArquivo, tipoArquivo, tamanho, caminhoArmazenamento, idTicket);

            int idGerado = anexoDAO.salvar(novoAnexo);

            if (idGerado > 0) {
                String acao = "ANEXO_ADICIONADO";
                String descricao = "Arquivo '" + nomeArquivo + "' anexado.";
                Historico historico = new Historico(acao, descricao, idTicket, idAutor);
                historicoDAO.salvar(historico);
            }

            return idGerado;

        } catch (Exception e) {
            System.err.println("Erro ao adicionar anexo: " + e.getMessage());
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * Lista todos os Anexos para um Ticket.
     */
    public List<Anexo> listarAnexosPorTicket(int idTicket) {
        return anexoDAO.listarPorTicket(idTicket);
    }

    private String getFileExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex == -1 || dotIndex == fileName.length() - 1) {
            return "";
        }
        return fileName.substring(dotIndex + 1);
    }
}