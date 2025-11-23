package view;

import control.ControleTicket;
import model.dao.PessoaDAO;
import model.entidades.Ticket;
import model.entidades.Usuario;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.format.DateTimeFormatter;
import view.listener.AtualizacaoListener;
import java.util.List;

/**
 * Painel para visualização dos Tickets abertos pelo Usuário (View).
 * Implementa a funcionalidade "Consultar Status dos Meus Tickets".
 */
public class PainelMeusTickets extends JPanel implements AtualizacaoListener {

    private Usuario usuarioLogado;
    private ControleTicket controleTicket;
    private PessoaDAO pessoaDAO;
    private JTable tabelaTickets;
    private JButton btnDetalhes;
    private DefaultTableModel tableModel;
    private JButton btnVisualizarHistorico;
    private JFrame parentFrame;

    public PainelMeusTickets(JFrame parent, Usuario usuario) {
        this.parentFrame = parent;
        this.usuarioLogado = usuario;
        this.controleTicket = new ControleTicket();
        this.pessoaDAO = new PessoaDAO();
        
        setLayout(new BorderLayout());
        
        // Configuração da Tabela
        String[] colunas = {"ID", "Título", "Status", "Prioridade", "Data Abertura", "Responsável"};
        tableModel = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Torna as células não editáveis
            }
        };
        tabelaTickets = new JTable(tableModel);
        tabelaTickets.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // Permite selecionar apenas uma linha
        
        add(new JScrollPane(tabelaTickets), BorderLayout.CENTER);
        
        // Painel de Ações
        JPanel panelAcoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnDetalhes = new JButton("Ver Detalhes e Comentar");
        btnVisualizarHistorico = new JButton("Visualizar Histórico");
        panelAcoes.add(btnDetalhes);
        panelAcoes.add(btnVisualizarHistorico);
        
        add(panelAcoes, BorderLayout.SOUTH);
        
        // Ações
        btnDetalhes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                verDetalhes();
            }
        });
        
        btnVisualizarHistorico.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                visualizarHistorico();
            }
        });
        
        // Carrega os dados iniciais
        carregarTickets();
    }
    
    /**
     * Carrega a lista de tickets do Controller e preenche a tabela.
     */
    public void carregarTickets() {
    // Limpa a tabela
    tableModel.setRowCount(0);

        try {
            // Chama o novo método do DAO através do Controller
            List<Ticket> tickets = controleTicket.listarTicketsPorUsuario(usuarioLogado.getId());

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

            for (Ticket t : tickets) {
                String nomeResponsavel = "Nenhum";

                // Etapa de Documentação 1: Busca o nome do agente responsável
                // A variável é declarada como nomeResponsavel.
                if (t.getIdAgenteResponsavel() != null) {
                    String nome = pessoaDAO.buscarNomePorId(t.getIdAgenteResponsavel());
                    nomeResponsavel = nome != null ? nome : "Agente ID " + t.getIdAgenteResponsavel();
                }

                // Etapa de Documentação 2: Adiciona a linha à tabela
                // É crucial usar o nome da variável 'nomeResponsavel' aqui.
                tableModel.addRow(new Object[]{
                    t.getIdTicket(),
                    t.getTitulo(),
                    t.getStatus(),
                    t.getPrioridade(),
                    t.getDataAbertura().format(formatter),
                    nomeResponsavel // CORREÇÃO: Usando a variável corretamente nomeada.
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar seus tickets: " + ex.getMessage(), "Erro de BD", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
    
    /**
     * Abre o formulário de detalhes para o ticket selecionado.
     */
    @Override
    public void onAtualizacaoRequerida() {
        carregarTickets();
    }

    private void verDetalhes() {
        int linhaSelecionada = tabelaTickets.getSelectedRow();
        
        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um ticket para ver os detalhes.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            // Obtém o ID do Ticket da primeira coluna
            int idTicket = (int) tableModel.getValueAt(linhaSelecionada, 0);
            
            // Busca o objeto Ticket completo
            Ticket ticketSelecionado = controleTicket.buscarTicketPorId(idTicket);
            
            if (ticketSelecionado != null) {
                // Abre o painel de detalhes em uma nova janela (ou JDialog)
                JDialog dialog = new JDialog(parentFrame, "Detalhes do Ticket #" + idTicket, true);
                dialog.setSize(800, 600);
                dialog.setLocationRelativeTo(parentFrame);
                
                // Passa o próprio painel como ouvinte de atualização
                PainelDetalhesTicket painelDetalhes = new PainelDetalhesTicket(ticketSelecionado, usuarioLogado, this);
                dialog.add(painelDetalhes);
                
                dialog.setVisible(true);
                
                // A recarga da lista é feita pelo PainelDetalhesTicket.java através do ouvinte
                // carregarTickets();
            } else {
                JOptionPane.showMessageDialog(this, "Ticket não encontrado.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao tentar ver detalhes: " + ex.getMessage(), "Erro de Sistema", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
    
    /**
     * Abre o formulário de histórico para o ticket selecionado.
     */
    private void visualizarHistorico() {
        int linhaSelecionada = tabelaTickets.getSelectedRow();
        
        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um ticket para visualizar o histórico.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            // Obtém o ID do Ticket da primeira coluna
            int idTicket = (int) tableModel.getValueAt(linhaSelecionada, 0);
            String tituloTicket = (String) tableModel.getValueAt(linhaSelecionada, 1);
            
            // Abre o formulário modal de histórico
            FormularioHistorico form = new FormularioHistorico(parentFrame, idTicket, tituloTicket);
            form.setVisible(true);
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao tentar visualizar histórico: " + ex.getMessage(), "Erro de Sistema", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
}
