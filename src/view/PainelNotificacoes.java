package view;

import control.ControleNotificacao;
import model.entidades.Notificacao;
import model.entidades.Pessoa;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Painel para visualização das Notificações do Usuário/Agente.
 */
public class PainelNotificacoes extends JPanel {

    private Pessoa pessoaLogada;
    private ControleNotificacao controleNotificacao;
    private JTable tabelaNotificacoes;
    private DefaultTableModel tableModel;
    private JButton btnMarcarComoLida;

    public PainelNotificacoes(Pessoa pessoa) {
        this.pessoaLogada = pessoa;
        this.controleNotificacao = new ControleNotificacao();

        setLayout(new BorderLayout());

        // Configuração da Tabela
        String[] colunas = {"ID", "Tipo", "Conteúdo", "Ticket", "Data"};
        tableModel = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabelaNotificacoes = new JTable(tableModel);
        tabelaNotificacoes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        add(new JScrollPane(tabelaNotificacoes), BorderLayout.CENTER);

        // Painel de Ações
        JPanel panelAcoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnMarcarComoLida = new JButton("Marcar como Lida");
        panelAcoes.add(btnMarcarComoLida);
        add(panelAcoes, BorderLayout.SOUTH);

        // Ações
        btnMarcarComoLida.addActionListener(e -> marcarComoLida());

        // Carrega os dados iniciais
        carregarNotificacoes();
    }

    /**
     * Carrega a lista de notificações não lidas.
     */
    public void carregarNotificacoes() {
        tableModel.setRowCount(0);

        try {
            List<Notificacao> notificacoes = controleNotificacao.listarNaoLidas(pessoaLogada.getId());

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

            for (Notificacao n : notificacoes) {
                tableModel.addRow(new Object[]{
                    n.getIdNotificacao(),
                    n.getTipo(),
                    n.getConteudo(),
                    n.getIdTicket() != null && n.getIdTicket() > 0 ? "#" + n.getIdTicket() : "-",
                    n.getDataEnvio() != null ? n.getDataEnvio().format(formatter) : "-"
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar notificações: " + ex.getMessage(), "Erro de BD", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    /**
     * Marca a notificação selecionada como lida.
     */
    private void marcarComoLida() {
        int linhaSelecionada = tabelaNotificacoes.getSelectedRow();

        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(this, "Selecione uma notificação para marcar como lida.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int idNotificacao = (int) tableModel.getValueAt(linhaSelecionada, 0);

            boolean sucesso = controleNotificacao.marcarComoLida(idNotificacao);

            if (sucesso) {
                JOptionPane.showMessageDialog(this, "Notificação marcada como lida.", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                carregarNotificacoes(); // Recarrega a lista
            } else {
                JOptionPane.showMessageDialog(this, "Falha ao marcar como lida.", "Erro", JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro de sistema: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
}