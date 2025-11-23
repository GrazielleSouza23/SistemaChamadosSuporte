package view;

import control.ControlePessoa;
import model.entidades.Pessoa;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Formulário (JDialog) para o Caso de Uso "Gerenciar Perfil".
 * Permite que a Pessoa logada (Usuário ou Agente) atualize seus dados.
 */
public class FormularioGerenciarPerfil extends JDialog {

    private Pessoa pessoaLogada;
    private ControlePessoa controlePessoa;

    private JTextField txtNome;
    private JTextField txtEmail;
    private JPasswordField txtNovaSenha;
    private JPasswordField txtConfirmarSenha;
    private JButton btnSalvar;
    private JButton btnCancelar;

    public FormularioGerenciarPerfil(JFrame parent, Pessoa pessoa) {
        super(parent, "Gerenciar Meu Perfil", true); // Modal
        this.pessoaLogada = pessoa;
        this.controlePessoa = new ControlePessoa();

        setSize(450, 300);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        // Painel do Formulário
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Nome
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Nome:"), gbc);
        gbc.gridx = 1;
        txtNome = new JTextField(25);
        txtNome.setText(pessoaLogada.getNome());
        formPanel.add(txtNome, gbc);

        // Email
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        txtEmail = new JTextField(25);
        txtEmail.setText(pessoaLogada.getEmail());
        formPanel.add(txtEmail, gbc);

        // --- Separador de Senha ---
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        formPanel.add(new JSeparator(), gbc);
        
        gbc.gridy = 3;
        JLabel lblInfoSenha = new JLabel("Deixe em branco para não alterar a senha");
        lblInfoSenha.setFont(new Font("Arial", Font.ITALIC, 10));
        formPanel.add(lblInfoSenha, gbc);
        gbc.gridwidth = 1; // Reset

        // Nova Senha
        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(new JLabel("Nova Senha:"), gbc);
        gbc.gridx = 1;
        txtNovaSenha = new JPasswordField(25);
        formPanel.add(txtNovaSenha, gbc);

        // Confirmar Senha
        gbc.gridx = 0;
        gbc.gridy = 5;
        formPanel.add(new JLabel("Confirmar Senha:"), gbc);
        gbc.gridx = 1;
        txtConfirmarSenha = new JPasswordField(25);
        formPanel.add(txtConfirmarSenha, gbc);

        add(formPanel, BorderLayout.CENTER);

        // Painel de Botões (Salvar / Cancelar)
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnSalvar = new JButton("Salvar Alterações");
        btnCancelar = new JButton("Cancelar");
        buttonPanel.add(btnCancelar);
        buttonPanel.add(btnSalvar);

        add(buttonPanel, BorderLayout.SOUTH);

        // Ações dos botões
        btnSalvar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                salvarPerfil();
            }
        });

        btnCancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Fecha a janela
            }
        });
    }

    /**
     * Coleta os dados do formulário, valida e chama o Controller.
     */
    private void salvarPerfil() {
        String nome = txtNome.getText();
        String email = txtEmail.getText();
        String novaSenha = new String(txtNovaSenha.getPassword());
        String confirmarSenha = new String(txtConfirmarSenha.getPassword());

        // 1. Validação de Senha (específica da View)
        if (!novaSenha.equals(confirmarSenha)) {
            JOptionPane.showMessageDialog(this, "As senhas não conferem!", "Erro de Validação", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            // 2. Chama o Controller
            boolean sucesso = controlePessoa.atualizarPerfil(pessoaLogada.getId(), nome, email, novaSenha);

            if (sucesso) {
                // 3. Atualiza o objeto 'Pessoa' em memória (na tela principal)
                pessoaLogada.setNome(nome);
                pessoaLogada.setEmail(email);

                JOptionPane.showMessageDialog(this, "Perfil atualizado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                this.dispose(); // Fecha o formulário
            } else {
                JOptionPane.showMessageDialog(this, "Não foi possível atualizar o perfil (verifique os logs).", "Erro", JOptionPane.ERROR_MESSAGE);
            }

        } catch (IllegalArgumentException ex) {
            // Erro de validação (ex: nome/email vazio)
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro de Validação", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            // Erro de sistema (ex: erro de BD)
            JOptionPane.showMessageDialog(this, "Erro de sistema: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
}