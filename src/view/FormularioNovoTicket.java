package view;

import control.ControleTicket;
import model.entidades.Usuario;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Formulário para abertura de um novo Ticket (View).
 * Interage com a camada de Controle (ControleTicket).
 */
public class FormularioNovoTicket extends JDialog {

    private Usuario usuarioLogado;
    private ControleTicket controleTicket;

    private JTextField txtTitulo;
    private JTextArea txtDescricao;
    private JComboBox<String> cmbCategoria;
    private JComboBox<String> cmbPrioridade;
    private JButton btnAbrir;

    public FormularioNovoTicket(JFrame parent, Usuario usuario) {
        super(parent, "Abrir Novo Ticket", true); // Modal
        this.usuarioLogado = usuario;
        this.controleTicket = new ControleTicket();

        setSize(500, 450);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        // Painel de Formulário
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Título
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Título:"), gbc);
        gbc.gridx = 1;
        txtTitulo = new JTextField(30);
        formPanel.add(txtTitulo, gbc);

        // Descrição
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Descrição:"), gbc);
        gbc.gridx = 1;
        gbc.gridheight = 3; // Ocupa 3 linhas
        txtDescricao = new JTextArea(5, 30);
        txtDescricao.setLineWrap(true);
        txtDescricao.setWrapStyleWord(true);
        JScrollPane scrollDescricao = new JScrollPane(txtDescricao);
        formPanel.add(scrollDescricao, gbc);
        gbc.gridheight = 1; // Reseta para 1 linha

        // Categoria
        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(new JLabel("Categoria:"), gbc);
        gbc.gridx = 1;
        String[] categorias = {"ACESSO", "BUG", "FUNCIONALIDADE", "OUTROS"};
        cmbCategoria = new JComboBox<>(categorias);
        formPanel.add(cmbCategoria, gbc);

        // Prioridade
        gbc.gridx = 0;
        gbc.gridy = 5;
        formPanel.add(new JLabel("Prioridade:"), gbc);
        gbc.gridx = 1;
        String[] prioridades = {"BAIXA", "MEDIA", "ALTA", "URGENTE"};
        cmbPrioridade = new JComboBox<>(prioridades);
        formPanel.add(cmbPrioridade, gbc);

        // Botão Abrir
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        btnAbrir = new JButton("Abrir Ticket");
        formPanel.add(btnAbrir, gbc);

        add(formPanel, BorderLayout.CENTER);

        // Ação do botão Abrir
        btnAbrir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                abrirTicket();
            }
        });
    }

    /**
     * Coleta os dados do formulário e chama o Controller para abrir o Ticket.
     */
    private void abrirTicket() {
        String titulo = txtTitulo.getText();
        String descricao = txtDescricao.getText();
        String categoria = (String) cmbCategoria.getSelectedItem();
        String prioridade = (String) cmbPrioridade.getSelectedItem();
        int idUsuario = usuarioLogado.getId();

        try {
            int idTicket = controleTicket.abrirNovoTicket(titulo, descricao, categoria, prioridade, idUsuario);

            if (idTicket > 0) {
                JOptionPane.showMessageDialog(this, "Ticket aberto com sucesso! ID: " + idTicket, "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                this.dispose(); // Fecha o formulário
            } else {
                JOptionPane.showMessageDialog(this, "Falha ao abrir o Ticket. Tente novamente.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IllegalArgumentException ex) {
            // Tratamento de exceção de validação (ex: campos vazios)
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro de Validação", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            // Tratamento de exceção de sistema (ex: erro de BD)
            JOptionPane.showMessageDialog(this, "Erro de sistema: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
}
