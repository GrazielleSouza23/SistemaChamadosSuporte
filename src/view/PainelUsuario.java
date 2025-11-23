package view;

import model.entidades.Usuario;

import javax.swing.*;
import java.awt.*;

/**
 * Painel Principal do Usuário (View).
 * Exibe as funcionalidades disponíveis para o usuário comum.
 */
public class PainelUsuario extends JFrame {

    private Usuario usuarioLogado;
    private JLabel lblTitulo; //

    public PainelUsuario(Usuario usuario) {
        this.usuarioLogado = usuario;
        
        setTitle("Painel do Usuário - " + usuario.getNome());
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Layout principal
        setLayout(new BorderLayout());

        // Título
        // <-- Movido para uma variável de instância
        lblTitulo = new JLabel("Bem-vindo(a), " + usuario.getNome() + " (Usuário Comum)", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        add(lblTitulo, BorderLayout.NORTH);

        // Painel de Botões (Funcionalidades)
        JPanel panelBotoes = new JPanel(new GridLayout(6, 1, 10, 10)); 
        panelBotoes.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JButton btnAbrirTicket = new JButton("Abrir Novo Ticket");
        JButton btnConsultarStatus = new JButton("Consultar Status dos Meus Tickets");
        JButton btnVisualizarHistorico = new JButton("Visualizar Histórico do Ticket");
        JButton btnNotificacoes = new JButton("Notificações");
        JButton btnGerenciarPerfil = new JButton("Gerenciar Meu Perfil");
        JButton btnLogout = new JButton("Sair");

        // Adiciona os botões ao painel
        panelBotoes.add(btnAbrirTicket);
        panelBotoes.add(btnConsultarStatus);
        panelBotoes.add(btnVisualizarHistorico);
        panelBotoes.add(btnNotificacoes);
        panelBotoes.add(btnGerenciarPerfil); 
        panelBotoes.add(btnLogout);

        add(panelBotoes, BorderLayout.WEST);
        
        // Painel de Conteúdo (onde a lista de tickets será exibida)
        CardLayout cardLayout = new CardLayout();
        JPanel painelConteudo = new JPanel(cardLayout);
        
        // Painel de Meus Tickets
        PainelMeusTickets painelMeusTickets = new PainelMeusTickets(this, usuarioLogado);
        painelConteudo.add(painelMeusTickets, "MEUS_TICKETS");
        
        // Painel de Notificações
        PainelNotificacoes painelNotificacoes = new PainelNotificacoes(usuarioLogado);
        painelConteudo.add(painelNotificacoes, "NOTIFICACOES");
        
        // Painel de Abertura de Ticket (temporário, pois o botão já faz isso)
        JPanel painelVazio = new JPanel();
        painelConteudo.add(painelVazio, "VAZIO");
        
        add(painelConteudo, BorderLayout.CENTER);

        // Ações dos botões
        btnAbrirTicket.addActionListener(e -> {
            // Abre o formulário de novo ticket
            FormularioNovoTicket form = new FormularioNovoTicket(this, usuarioLogado);
            form.setVisible(true);
            // Após fechar, recarrega a lista de tickets
            painelMeusTickets.carregarTickets();
            cardLayout.show(painelConteudo, "MEUS_TICKETS");
        });
        
        btnConsultarStatus.addActionListener(e -> {
            painelMeusTickets.carregarTickets();
            cardLayout.show(painelConteudo, "MEUS_TICKETS");
        });
        
        btnVisualizarHistorico.addActionListener(e -> {
            // A funcionalidade de visualizar histórico está integrada ao PainelMeusTickets
            cardLayout.show(painelConteudo, "MEUS_TICKETS");
            JOptionPane.showMessageDialog(this, "Selecione um ticket na lista e clique em 'Visualizar Histórico'.", "Instrução", JOptionPane.INFORMATION_MESSAGE);
        });
        
        btnNotificacoes.addActionListener(e -> {
            painelNotificacoes.carregarNotificacoes();
            cardLayout.show(painelConteudo, "NOTIFICACOES");
        });

        // <-- Ação do botão Gerenciar Perfil -->
        btnGerenciarPerfil.addActionListener(e -> {
            // Abre o formulário modal
            FormularioGerenciarPerfil formPerfil = new FormularioGerenciarPerfil(this, usuarioLogado);
            formPerfil.setVisible(true);
            
            // Quando o formulário fechar, o objeto 'usuarioLogado' já foi
            // atualizado (se teve sucesso). Vamos atualizar a UI:
            atualizarTituloUI();
        });
        
        btnLogout.addActionListener(e -> {
            this.dispose();
            new TelaLogin().setVisible(true);
        });
    }

    // <-- Método para atualizar a UI após a edição do perfil -->
    private void atualizarTituloUI() {
        setTitle("Painel do Usuário - " + usuarioLogado.getNome());
        lblTitulo.setText("Bem-vindo(a), " + usuarioLogado.getNome() + " (Usuário Comum)");
    }
    
    /**
     * Recarrega o painel de conteúdo atualmente visível.
     * Usado para forçar a atualização após uma ação em um JDialog.
     */
    public void recarregarPainelAtual() {
        // Encontra o painel de conteúdo
        JPanel painelConteudo = (JPanel) ((BorderLayout) getContentPane().getLayout()).getLayoutComponent(BorderLayout.CENTER);
        CardLayout cardLayout = (CardLayout) painelConteudo.getLayout();
        
        // Itera sobre os componentes para encontrar o visível
        for (Component comp : painelConteudo.getComponents()) {
            if (comp.isVisible()) {
                if (comp instanceof PainelMeusTickets) {
                    ((PainelMeusTickets) comp).carregarTickets();
                } else if (comp instanceof PainelNotificacoes) {
                    ((PainelNotificacoes) comp).carregarNotificacoes();
                }
                // Adicione outros painéis que precisam de recarga aqui
                break;
            }
        }
    }
}