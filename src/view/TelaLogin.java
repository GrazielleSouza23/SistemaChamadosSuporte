package view;

import control.ControleLogin;
import model.entidades.Pessoa;
import model.entidades.Agente;
import model.entidades.Usuario;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Tela de Login (View).
 * Demonstra a camada de Apresentação (Fronteira) do padrão MVC.
 * Interage com a camada de Controle (ControleLogin).
 */
public class TelaLogin extends JFrame {

    private JTextField txtEmail;
    private JPasswordField txtSenha;
    private JButton btnLogin;
    private ControleLogin controleLogin;

    public TelaLogin() {
        // Inicializa o Controller
        this.controleLogin = new ControleLogin();
        
        setTitle("Sistema de Chamados - Login");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centraliza a janela

        // Configuração do Painel Principal
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Rótulo e Campo de Email
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Email:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        txtEmail = new JTextField(20);
        panel.add(txtEmail, gbc);

        // Rótulo e Campo de Senha
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Senha:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        txtSenha = new JPasswordField(20);
        panel.add(txtSenha, gbc);

        // Botão de Login
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        btnLogin = new JButton("Entrar");
        panel.add(btnLogin, gbc);

        // Adiciona o Listener ao botão
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                realizarLogin();
            }
        });

        add(panel);
    }

    /**
     * Método que chama a lógica de negócio (Controller) para autenticação.
     */
    private void realizarLogin() {
        String email = txtEmail.getText();
        // A senha deve ser tratada como String para ser passada ao Controller
        String senha = new String(txtSenha.getPassword());

        try {
            Pessoa pessoa = controleLogin.fazerLogin(email, senha);

            if (pessoa != null) {
                JOptionPane.showMessageDialog(this, "Login realizado com sucesso! Tipo: " + pessoa.getTipoUsuario(), "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                
                // Fecha a tela de login
                this.dispose();
                
                // Redireciona para o painel apropriado (Polimorfismo)
                if (pessoa instanceof Agente) {
                    new PainelAgente((Agente) pessoa).setVisible(true);
                } else if (pessoa instanceof Usuario) {
                    new PainelUsuario((Usuario) pessoa).setVisible(true);
                }
                
            } else {
                JOptionPane.showMessageDialog(this, "Email ou senha inválidos.", "Erro de Login", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            // Tratamento de exceção (ex: erro de conexão com o BD)
            JOptionPane.showMessageDialog(this, "Erro ao tentar conectar: " + ex.getMessage(), "Erro de Sistema", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // Exemplo de uso do Singleton para garantir a conexão
        // ConexaoBD.getInstance().getConnection(); 
        
        // Inicia a aplicação Swing na Thread de Despacho de Eventos (EDT)
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new TelaLogin().setVisible(true);
            }
        });
    }
}
