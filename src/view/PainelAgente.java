package view;

import model.entidades.Agente;

import javax.swing.*;
import java.awt.*;

/**
 * Painel Principal do Agente de Suporte (View).
 * Exibe as funcionalidades disponíveis para o agente.
 */
public class PainelAgente extends JFrame {

    private Agente agenteLogado;
    private JLabel lblTitulo; // <-- Referência para o título

    public PainelAgente(Agente agente) {
        this.agenteLogado = agente;
        
        setTitle("Painel do Agente - " + agente.getNome() + " (Nível: " + agente.getNivelAcesso() + ")");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Layout principal
        setLayout(new BorderLayout());

        // Título
        // <--  Movido para uma variável de instância
        lblTitulo = new JLabel("Bem-vindo(a), " + agente.getNome() + " (Agente de Suporte)", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        add(lblTitulo, BorderLayout.NORTH);

        // Painel de Botões (Funcionalidades)
        // <-- NOVO: Aumentado de (7, 1) para (8, 1)
        JPanel panelBotoes = new JPanel(new GridLayout(8, 1, 10, 10));
        panelBotoes.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JButton btnVisualizarTodos = new JButton("Visualizar Todos os Tickets (RF003)");
        JButton btnAtribuirTicket = new JButton("Atribuir Ticket (RF003)");
        JButton btnAtualizarStatus = new JButton("Atualizar Status (RF004)");
        JButton btnFiltrarTickets = new JButton("Filtrar Tickets (RF003)");
        JButton btnFecharTicket = new JButton("Fechar Ticket (RF008)");
        JButton btnNotificacoes = new JButton("Notificações");
        JButton btnGerenciarPerfil = new JButton("Gerenciar Meu Perfil"); // <-- NOVO
        JButton btnLogout = new JButton("Sair");

        // Adiciona os botões ao painel
        panelBotoes.add(btnVisualizarTodos);
        panelBotoes.add(btnAtribuirTicket);
        panelBotoes.add(btnAtualizarStatus);
        panelBotoes.add(btnFiltrarTickets);
        panelBotoes.add(btnFecharTicket);
        panelBotoes.add(btnNotificacoes);
        panelBotoes.add(btnGerenciarPerfil); // <-- NOVO
        panelBotoes.add(btnLogout);

        add(panelBotoes, BorderLayout.WEST);
        
        // Painel de Conteúdo (onde a lista de tickets será exibida)
        CardLayout cardLayout = new CardLayout();
        JPanel painelConteudo = new JPanel(cardLayout);
        
        // Painel de Lista de Tickets
        PainelListaTickets painelListaTickets = new PainelListaTickets(agente);
        painelConteudo.add(painelListaTickets, "LISTA_TICKETS");
        
        // Painel de Notificações
        PainelNotificacoes painelNotificacoes = new PainelNotificacoes(agente);
        painelConteudo.add(painelNotificacoes, "NOTIFICACOES");
        
        add(painelConteudo, BorderLayout.CENTER);

        // Ações dos botões
        btnVisualizarTodos.addActionListener(e -> {
            painelListaTickets.carregarTickets();
            cardLayout.show(painelConteudo, "LISTA_TICKETS");
        });
        btnAtribuirTicket.addActionListener(e -> {
            cardLayout.show(painelConteudo, "LISTA_TICKETS");
            JOptionPane.showMessageDialog(this, "Use o botão 'Atribuir Ticket a Mim' na lista de tickets.", "Instrução", JOptionPane.INFORMATION_MESSAGE);
        });
        btnAtualizarStatus.addActionListener(e -> {
            cardLayout.show(painelConteudo, "LISTA_TICKETS");
            JOptionPane.showMessageDialog(this, "A funcionalidade está no botão 'Ver Detalhes e Interagir' da lista de tickets.", "Instrução", JOptionPane.INFORMATION_MESSAGE);
        });
        btnFiltrarTickets.addActionListener(e -> {
            cardLayout.show(painelConteudo, "LISTA_TICKETS");
            JOptionPane.showMessageDialog(this, "Use os filtros acima da lista de tickets.", "Instrução", JOptionPane.INFORMATION_MESSAGE);
        });
        btnFecharTicket.addActionListener(e -> {
            cardLayout.show(painelConteudo, "LISTA_TICKETS");
            JOptionPane.showMessageDialog(this, "A funcionalidade está no botão 'Ver Detalhes e Interagir' da lista de tickets.", "Instrução", JOptionPane.INFORMATION_MESSAGE);
        });
        
        btnNotificacoes.addActionListener(e -> {
            painelNotificacoes.carregarNotificacoes();
            cardLayout.show(painelConteudo, "NOTIFICACOES");
        });

        // <-- NOVO: Ação do botão Gerenciar Perfil -->
        btnGerenciarPerfil.addActionListener(e -> {
            // Abre o formulário modal
            FormularioGerenciarPerfil formPerfil = new FormularioGerenciarPerfil(this, agenteLogado);
            formPerfil.setVisible(true);
            
            // Quando o formulário fechar, o objeto 'agenteLogado' já foi
            // atualizado (se teve sucesso). Vamos atualizar a UI:
            atualizarTituloUI();
        });
        
        btnLogout.addActionListener(e -> {
            this.dispose();
            new TelaLogin().setVisible(true);
        });
    }

    // <-- NOVO: Método para atualizar a UI após a edição do perfil -->
    private void atualizarTituloUI() {
        // Nota: O Nível de Acesso não é editável neste formulário,
        // mas o Agente pode ter seu nível alterado por um Admin.
        // O método 'gerenciarPerfil' do Agente (se fosse diferente)
        // poderia lidar com isso, mas estamos usando o da Pessoa.
        agenteLogado.setNivelAcesso(agenteLogado.getNivelAcesso()); // Recarregar se necessário

        setTitle("Painel do Agente - " + agenteLogado.getNome() + " (Nível: " + agenteLogado.getNivelAcesso() + ")");
        lblTitulo.setText("Bem-vindo(a), " + agenteLogado.getNome() + " (Agente de Suporte)");
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
                if (comp instanceof PainelListaTickets) {
                    ((PainelListaTickets) comp).carregarTickets();
                } else if (comp instanceof PainelNotificacoes) {
                    ((PainelNotificacoes) comp).carregarNotificacoes();
                }
                // Adicione outros painéis que precisam de recarga aqui
                break;
            }
        }
    }
}