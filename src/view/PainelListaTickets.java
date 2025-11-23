package view;

import control.ControleTicket;
import model.dao.PessoaDAO;
import model.entidades.Agente;
import model.entidades.Ticket;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.format.DateTimeFormatter;
import view.listener.AtualizacaoListener;
import java.util.List;

/**
 * Painel para visualização e gerenciamento de Tickets (View).
 * Utilizado pelo Agente de Suporte.
 */
public class PainelListaTickets extends JPanel implements AtualizacaoListener {

    private Agente agenteLogado;
    private ControleTicket controleTicket;
    private PessoaDAO pessoaDAO;
    private JTable tabelaTickets;
    private DefaultTableModel tableModel;
    private JButton btnAtribuir;
    private JButton btnDetalhes;
    
    // Componentes de Filtro
    private JComboBox<String> cmbStatusFiltro;
    private JComboBox<String> cmbPrioridadeFiltro;
    private JButton btnFiltrar;

    public PainelListaTickets(Agente agente) {
        this.agenteLogado = agente;
        this.controleTicket = new ControleTicket();
        this.pessoaDAO = new PessoaDAO();
        
        setLayout(new BorderLayout());
        
        // --- Painel de Filtros (RF003) ---
        JPanel panelFiltros = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        String[] statusOptions = {"Todos", "ABERTO", "EM_ANDAMENTO", "AGUARDANDO_RESPOSTA", "RESOLVIDO", "FECHADO"};
        cmbStatusFiltro = new JComboBox<>(statusOptions);
        
        String[] prioridadeOptions = {"Todas", "BAIXA", "MEDIA", "ALTA", "URGENTE"};
        cmbPrioridadeFiltro = new JComboBox<>(prioridadeOptions);
        
        btnFiltrar = new JButton("Filtrar Tickets");
        
        panelFiltros.add(new JLabel("Status:"));
        panelFiltros.add(cmbStatusFiltro);
        panelFiltros.add(new JLabel("Prioridade:"));
        panelFiltros.add(cmbPrioridadeFiltro);
        panelFiltros.add(btnFiltrar);
        
        add(panelFiltros, BorderLayout.NORTH);
        
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
        JPanel panelAcoes = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnAtribuir = new JButton("Atribuir Ticket a Mim");
        btnDetalhes = new JButton("Ver Detalhes e Interagir");
        panelAcoes.add(btnAtribuir);
        panelAcoes.add(btnDetalhes);
        
        add(panelAcoes, BorderLayout.SOUTH);
        
        // Ações
        btnAtribuir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                atribuirTicket();
            }
        });
        
        btnDetalhes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                verDetalhes();
            }
        });
        
        btnFiltrar.addActionListener(e -> carregarTickets());
        
        // Carrega os dados iniciais
        carregarTickets();
    }
    
    /**
     * Carrega a lista de tickets do Controller e preenche a tabela, aplicando filtros.
     */
    public void carregarTickets() {
        // Limpa a tabela
        tableModel.setRowCount(0);

        // Obtém os filtros
        String statusFiltro = cmbStatusFiltro.getSelectedItem().toString();
        String prioridadeFiltro = cmbPrioridadeFiltro.getSelectedItem().toString();

        // Converte "Todos" para null ou string vazia para o Controller
        String status = statusFiltro.equals("Todos") ? null : statusFiltro;
        String prioridade = prioridadeFiltro.equals("Todas") ? null : prioridadeFiltro;

        try {
            // CORREÇÃO (Temporária): O seu ControleTicket.java está filtrando em memória (stream)
            // Em vez de no banco. Para que o filtro do ControleTicket funcione, 
            // devemos passar "" (string vazia) em vez de null.
            String statusFiltrar = status == null ? "" : status;
            String prioridadeFiltrar = prioridade == null ? "" : prioridade;
            
            List<Ticket> tickets = controleTicket.listarTicketsComFiltro(statusFiltrar, prioridadeFiltrar);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

            for (Ticket t : tickets) {
                String nomeResponsavel = "Nenhum";
                if (t.getIdAgenteResponsavel() != null) {
                    String nome = pessoaDAO.buscarNomePorId(t.getIdAgenteResponsavel());
                    nomeResponsavel = nome != null ? nome : "Agente ID " + t.getIdAgenteResponsavel();
                }

                tableModel.addRow(new Object[]{
                    t.getIdTicket(),
                    t.getTitulo(),
                    t.getStatus(),
                    t.getPrioridade(),
                    t.getDataAbertura().format(formatter),
                    nomeResponsavel // <-- CORREÇÃO: Usar a variável nomeResponsavel
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar tickets: " + ex.getMessage(), "Erro de BD", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
    
    /*
    formulário de detalhes para o ticket selecionado.
     */
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
                JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
                JDialog dialog = new JDialog(parentFrame, "Detalhes do Ticket #" + idTicket, true);
                dialog.setSize(800, 600);
                dialog.setLocationRelativeTo(parentFrame);
                
                // Passa o próprio painel como ouvinte de atualização
                PainelDetalhesTicket painelDetalhes = new PainelDetalhesTicket(ticketSelecionado, agenteLogado, this);
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
     * Atribui o ticket selecionado ao Agente logado.
     */
    @Override
    public void onAtualizacaoRequerida() {
        carregarTickets();
    }

    private void atribuirTicket() {
        int linhaSelecionada = tabelaTickets.getSelectedRow();
        
        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um ticket para atribuir.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            // Obtém o ID do Ticket da primeira coluna
            int idTicket = (int) tableModel.getValueAt(linhaSelecionada, 0);
            int idAgente = agenteLogado.getId();
            
            boolean sucesso = controleTicket.atribuirTicket(idTicket, idAgente);
            
            if (sucesso) {
                JOptionPane.showMessageDialog(this, "Ticket " + idTicket + " atribuído a você com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                // Chama o método do ouvinte para garantir a atualização da lista
                onAtualizacaoRequerida();
            } else {
                JOptionPane.showMessageDialog(this, "Falha ao atribuir o ticket.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao tentar atribuir ticket: " + ex.getMessage(), "Erro de Sistema", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
}