package control;

import model.dao.AnexoDAO;
import model.dao.HistoricoDAO;
import model.entidades.Anexo;
import model.entidades.Historico;

import java.io.File;
import java.util.List;

/**
 * Classe de Controle (Controller) para a funcionalidade de Anexos.
 * Responsável por coordenar a lógica de negócio (upload, listar)
 * e interagir com a camada de persistência (AnexoDAO e HistoricoDAO).
 */
public class ControleAnexo {

    private AnexoDAO anexoDAO;
    private HistoricoDAO historicoDAO;
    
    // Diretório de armazenamento simulado (em um sistema real, seria um S3 ou servidor de arquivos)
    private static final String DIRETORIO_UPLOAD = "/home/ubuntu/uploads/";

    public ControleAnexo() {
        this.anexoDAO = new AnexoDAO();
        this.historicoDAO = new HistoricoDAO();
        
        // Cria o diretório se não existir
        new File(DIRETORIO_UPLOAD).mkdirs();
    }

    /**
     * Adiciona um novo Anexo ao Ticket.
     * @param idTicket ID do Ticket.
     * @param arquivo Arquivo a ser anexado.
     * @param idAutor ID da Pessoa (Usuário ou Agente) que está anexando.
     * @return O ID do Anexo recém-criado, ou -1 em caso de falha.
     */
    public int adicionarAnexo(int idTicket, File arquivo, int idAutor) {
        if (arquivo == null || !arquivo.exists()) {
            throw new IllegalArgumentException("Arquivo inválido ou não encontrado.");
        }
        
        // 1. Simulação de upload (cria um arquivo de placeholder no diretório de upload)
        String nomeArquivo = arquivo.getName();
        String tipoArquivo = getFileExtension(nomeArquivo);
        double tamanho = arquivo.length() / 1024.0; // Tamanho em KB
        String caminhoArmazenamento = DIRETORIO_UPLOAD + idTicket + "_" + nomeArquivo;
        
        try {
            // Simulação de cópia do arquivo para o diretório de armazenamento
            File destino = new File(caminhoArmazenamento);
            // Em um sistema real, usaríamos Files.copy(arquivo.toPath(), destino.toPath());
            // Aqui, apenas criamos um arquivo vazio para simular o armazenamento.
            destino.createNewFile(); 
            
            Anexo novoAnexo = new Anexo(nomeArquivo, tipoArquivo, tamanho, caminhoArmazenamento, idTicket);
            
            // 2. Salva no banco de dados via DAO
            int idGerado = anexoDAO.salvar(novoAnexo);
            
            if (idGerado > 0) {
                // 3. Adiciona registro no Histórico
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
     * @param idTicket ID do Ticket.
     * @return Lista de Anexos.
     */
    public List<Anexo> listarAnexosPorTicket(int idTicket) {
        return anexoDAO.listarPorTicket(idTicket);
    }
    
    /**
     * Obtém a extensão do arquivo.
     */
    private String getFileExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex == -1 || dotIndex == fileName.length() - 1) {
            return "";
        }
        return fileName.substring(dotIndex + 1);
    }
}
