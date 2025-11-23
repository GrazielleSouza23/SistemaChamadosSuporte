package view;

import model.dao.HistoricoDAO;
import model.entidades.Historico;

import javax.swing.*;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Formulário para visualização do Histórico de um Ticket (View).
 * Implementa a funcionalidade "Visualizar Histórico do Ticket".
 */
public class FormularioHistorico extends JDialog {

    private HistoricoDAO historicoDAO;
    private JTextArea txtHistorico;
    private int idTicket;
    private String tituloTicket;

    public FormularioHistorico(JFrame parent, int idTicket, String tituloTicket) {
        super(parent, "Histórico do Ticket #" + idTicket + " - " + tituloTicket, true); // Modal
        this.idTicket = idTicket;
        this.tituloTicket = tituloTicket;
        this.historicoDAO = new HistoricoDAO();

        setSize(600, 400);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        // Título
        JLabel lblTitulo = new JLabel("Histórico de Ações para: " + tituloTicket, SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 14));
        add(lblTitulo, BorderLayout.NORTH);

        // Área de Texto para o Histórico
        txtHistorico = new JTextArea();
        txtHistorico.setEditable(false);
        txtHistorico.setLineWrap(true);
        txtHistorico.setWrapStyleWord(true);
        add(new JScrollPane(txtHistorico), BorderLayout.CENTER);

        carregarHistorico();
    }

    /**
     * Carrega o histórico do Ticket e exibe na área de texto.
     */
    private void carregarHistorico() {
        try {
            List<Historico> historicos = historicoDAO.listarPorTicket(idTicket);
            
            if (historicos.isEmpty()) {
                txtHistorico.setText("Nenhum registro de histórico encontrado para este ticket.");
                return;
            }
            
            StringBuilder sb = new StringBuilder();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            
            for (Historico h : historicos) {
                // Formato: [Data e Hora] - [Ação] por [ID Responsável]: [Descrição]
                sb.append("[").append(h.getDataRegistro().format(formatter)).append("] - ");
                sb.append(h.getAcao()).append(" por ID ").append(h.getIdResponsavel());
                
                if (h.getDescricao() != null && !h.getDescricao().isEmpty()) {
                    sb.append(": ").append(h.getDescricao());
                }
                sb.append("\n");
            }
            
            txtHistorico.setText(sb.toString());
            
        } catch (Exception ex) {
            txtHistorico.setText("Erro ao carregar histórico: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}
