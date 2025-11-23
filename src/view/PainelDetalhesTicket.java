package view;

import control.ControleAnexo;
import control.ControleComentario;
import control.ControleTicket;
import model.dao.DAOException; // <-- IMPORTANTE: Importar a exceção
import model.dao.PessoaDAO;
import model.entidades.Anexo;
import model.entidades.Comentario;
import model.entidades.Pessoa;
import model.entidades.Ticket;
import view.listener.AtualizacaoListener;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Painel para visualização detalhada de um Ticket e interação.
 */
public class PainelDetalhesTicket extends JPanel {

    private Ticket ticket;
    private Pessoa pessoaLogada;
    
    // Controles
    private ControleComentario controleComentario;
    private ControleAnexo controleAnexo;
    private ControleTicket controleTicket; // <-- Transformado em atributo da classe
    private PessoaDAO pessoaDAO;
    
    private AtualizacaoListener atualizacaoListener;

    // Componentes de UI
    private JTextArea txtDetalhes;
    private JTextArea txtComentarios;
    private JTextField txtNovoComentario;
    private JButton btnComentarPublico;
    private JButton btnComentarInterno;
    private JButton btnAnexar;
    
    // Componentes para Agente
    private JComboBox<String> cmbStatus;
    private JButton btnAtualizarStatus;
    private JButton btnFecharTicket;
    private JList<String> listaAnexos;
    private DefaultListModel<String> listModelAnexos;

    public PainelDetalhesTicket(Ticket ticket, Pessoa pessoaLogada, AtualizacaoListener listener) {
        this.ticket = ticket;
        this.pessoaLogada = pessoaLogada;
        this.atualizacaoListener = listener;
        
        // Inicialização dos Controladores e DAOs
        this.controleComentario = new ControleComentario();
        this.controleAnexo = new ControleAnexo();
        this.controleTicket = new ControleTicket();
        this.pessoaDAO = new PessoaDAO();

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // --- Painel Superior (Detalhes do Ticket) ---
        JPanel panelSuperior = new JPanel(new BorderLayout());
        
        JPanel panelDetalhes = new JPanel(new GridLayout(1, 2));
        
        txtDetalhes = new JTextArea();
        txtDetalhes.setEditable(false);
        txtDetalhes.setLineWrap(true);
        txtDetalhes.setWrapStyleWord(true);
        JScrollPane scrollDetalhes = new JScrollPane(txtDetalhes);
        panelDetalhes.add(scrollDetalhes);
        
        // --- Painel de Anexos ---
        JPanel panelAnexos = new JPanel(new BorderLayout());
        panelAnexos.setBorder(BorderFactory.createTitledBorder("Anexos"));
        
        listModelAnexos = new DefaultListModel<>();
        listaAnexos = new JList<>(listModelAnexos);
        panelAnexos.add(new JScrollPane(listaAnexos), BorderLayout.CENTER);
        
        btnAnexar = new JButton("Anexar Arquivo");
        panelAnexos.add(btnAnexar, BorderLayout.SOUTH);
        
        panelDetalhes.add(panelAnexos);
        
        panelSuperior.add(panelDetalhes, BorderLayout.CENTER);
        
        // --- Painel de Ações do Agente (RF004, RF008) ---
        // Uso de "AGENTE".equals(...) evita NullPointerException se getTipoUsuario for null
        if ("AGENTE".equals(pessoaLogada.getTipoUsuario())) {
            JPanel panelAcoesAgente = new JPanel(new FlowLayout(FlowLayout.LEFT));
            panelAcoesAgente.setBorder(BorderFactory.createTitledBorder("Ações do Agente"));
            
            // Atualizar Status
            String[] statusOptions = {"ABERTO", "EM_ANDAMENTO", "AGUARDANDO_RESPOSTA", "RESOLVIDO"};
            cmbStatus = new JComboBox<>(statusOptions);
            cmbStatus.setSelectedItem(ticket.getStatus());
            btnAtualizarStatus = new JButton("Atualizar Status");
            
            panelAcoesAgente.add(new JLabel("Novo Status:"));
            panelAcoesAgente.add(cmbStatus);
            panelAcoesAgente.add(btnAtualizarStatus);
            
            // Fechar Ticket
            btnFecharTicket = new JButton("Fechar Ticket (RF008)");
            panelAcoesAgente.add(btnFecharTicket);
            
            panelSuperior.add(panelAcoesAgente, BorderLayout.SOUTH);
            
            // Ações dos botões do Agente
            btnAtualizarStatus.addActionListener(e -> atualizarStatus());
            btnFecharTicket.addActionListener(e -> fecharTicket());
        }
        
        add(panelSuperior, BorderLayout.NORTH);

        // --- Painel Central (Comentários) ---
        txtComentarios = new JTextArea();
        txtComentarios.setEditable(false);
        txtComentarios.setLineWrap(true);
        txtComentarios.setWrapStyleWord(true);
        JScrollPane scrollComentarios = new JScrollPane(txtComentarios);
        scrollComentarios.setBorder(BorderFactory.createTitledBorder("Comentários"));
        add(scrollComentarios, BorderLayout.CENTER);

        // --- Painel Inferior (Novo Comentário) ---
        JPanel panelNovoComentario = new JPanel(new BorderLayout());
        txtNovoComentario = new JTextField();
        panelNovoComentario.add(txtNovoComentario, BorderLayout.CENTER);

        JPanel panelBotoesComentario = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnComentarPublico = new JButton("Comentar (Público)");
        panelBotoesComentario.add(btnComentarPublico);
        
        // Botão de Comentário Interno visível apenas para Agentes
        if ("AGENTE".equals(pessoaLogada.getTipoUsuario())) {
            btnComentarInterno = new JButton("Comentar (Interno)");
            panelBotoesComentario.add(btnComentarInterno);
            btnComentarInterno.addActionListener(e -> adicionarComentario("INTERNO"));
        }
        
        panelNovoComentario.add(panelBotoesComentario, BorderLayout.EAST);
        add(panelNovoComentario, BorderLayout.SOUTH);

        // Ações Gerais
        btnComentarPublico.addActionListener(e -> adicionarComentario("PUBLICO"));
        btnAnexar.addActionListener(e -> anexarArquivo());

        // Carrega os dados
        carregarDetalhes();
        carregarComentarios();
        carregarAnexos();
    }
    
    private void atualizarStatus() {
        String novoStatus = (String) cmbStatus.getSelectedItem();
        
        if (novoStatus.equals(ticket.getStatus())) {
            JOptionPane.showMessageDialog(this, "O status selecionado é o mesmo do atual.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            // Usa o atributo da classe em vez de instanciar novo
            boolean sucesso = controleTicket.atualizarStatus(ticket.getIdTicket(), novoStatus, pessoaLogada.getId());
            
            if (sucesso) {
                JOptionPane.showMessageDialog(this, "Status atualizado para " + novoStatus + " com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                ticket.setStatus(novoStatus);
                carregarDetalhes();
                if (atualizacaoListener != null) {
                    atualizacaoListener.onAtualizacaoRequerida();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Falha ao atualizar status.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro de sistema ao atualizar status: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
    
    private void fecharTicket() {
        int confirm = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja fechar o Ticket #" + ticket.getIdTicket() + "?", "Confirmar Fechamento", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                boolean sucesso = controleTicket.fecharTicket(ticket.getIdTicket(), pessoaLogada.getId());
                
                if (sucesso) {
                    JOptionPane.showMessageDialog(this, "Ticket fechado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                    ticket.setStatus("FECHADO");
                    carregarDetalhes();
                    
                    if (atualizacaoListener != null) {
                        atualizacaoListener.onAtualizacaoRequerida();
                    }
                    
                    // Desabilita botões
                    btnComentarPublico.setEnabled(false);
                    if (btnComentarInterno != null) btnComentarInterno.setEnabled(false);
                    btnAnexar.setEnabled(false);
                    if (btnAtualizarStatus != null) btnAtualizarStatus.setEnabled(false);
                    if (btnFecharTicket != null) btnFecharTicket.setEnabled(false);
                    
                } else {
                    JOptionPane.showMessageDialog(this, "Falha ao fechar o Ticket.", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro de sistema ao fechar ticket: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }
    
    private void carregarDetalhes() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        
        // CORREÇÃO: Tratamento de erro ao buscar nome do responsável no DAO
        String nomeResponsavel = "Nenhum";
        try {
            if (ticket.getIdAgenteResponsavel() != null) {
                String nomeEncontrado = pessoaDAO.buscarNomePorId(ticket.getIdAgenteResponsavel());
                if (nomeEncontrado != null) {
                    nomeResponsavel = nomeEncontrado;
                }
            }
        } catch (DAOException e) {
            nomeResponsavel = "Erro ao carregar nome";
            e.printStackTrace();
        }

        String detalhes = String.format(
            "ID: %d\n" +
            "Título: %s\n" +
            "Descrição: %s\n" +
            "Status: %s\n" +
            "Prioridade: %s\n" +
            "Categoria: %s\n" +
            "Aberto em: %s\n" +
            "Responsável: %s",
            ticket.getIdTicket(),
            ticket.getTitulo(),
            ticket.getDescricao(),
            ticket.getStatus(),
            ticket.getPrioridade(),
            ticket.getCategoria(),
            ticket.getDataAbertura().format(formatter),
            nomeResponsavel // Variável segura
        );
        txtDetalhes.setText(detalhes);
    }

    private void carregarComentarios() {
        txtComentarios.setText("");
        List<Comentario> comentarios;
        
        try {
            if ("AGENTE".equals(pessoaLogada.getTipoUsuario())) {
                comentarios = controleComentario.listarTodosComentariosPorTicket(ticket.getIdTicket());
            } else {
                comentarios = controleComentario.listarComentariosPublicosPorTicket(ticket.getIdTicket());
            }
            
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            StringBuilder sb = new StringBuilder();
            
            for (Comentario c : comentarios) {
                // CORREÇÃO: Tratamento de exceção dentro do loop ao chamar DAO
                String nomeAutor;
                try {
                    String nome = pessoaDAO.buscarNomePorId(c.getIdAutor());
                    nomeAutor = (nome != null) ? nome : "ID " + c.getIdAutor();
                } catch (DAOException e) {
                    nomeAutor = "Desconhecido (Erro)";
                }

                String tipo = "INTERNO".equals(c.getTipo()) ? "[INTERNO]" : "[PÚBLICO]";
                
                sb.append(String.format("[%s] %s em %s:\n%s\n\n",
                    tipo,
                    nomeAutor,
                    c.getDataComentario().format(formatter),
                    c.getConteudo()
                ));
            }
            txtComentarios.setText(sb.toString());

        } catch (Exception ex) {
            txtComentarios.setText("Erro ao carregar comentários: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
    
    private void carregarAnexos() {
        listModelAnexos.clear();
        try {
            List<Anexo> anexos = controleAnexo.listarAnexosPorTicket(ticket.getIdTicket());
            
            for (Anexo a : anexos) {
                listModelAnexos.addElement(String.format("%s (%s, %.2f KB)", a.getNomeArquivo(), a.getTipoArquivo(), a.getTamanho()));
            }
        } catch (Exception e) {
            listModelAnexos.addElement("Erro ao carregar anexos.");
            e.printStackTrace();
        }
    }

    private void adicionarComentario(String tipo) {
        String conteudo = txtNovoComentario.getText();
        if (conteudo.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Digite um comentário.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            int idComentario = controleComentario.adicionarComentario(ticket.getIdTicket(), conteudo, tipo, pessoaLogada.getId());
            
            if (idComentario > 0) {
                JOptionPane.showMessageDialog(this, "Comentário adicionado!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                txtNovoComentario.setText("");
                carregarComentarios();
                if (atualizacaoListener != null) {
                    atualizacaoListener.onAtualizacaoRequerida();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Falha ao adicionar comentário.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Validação", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro de sistema: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void anexarArquivo() {
        JFileChooser fileChooser = new JFileChooser();
        int resultado = fileChooser.showOpenDialog(this);
        
        if (resultado == JFileChooser.APPROVE_OPTION) {
            File arquivoSelecionado = fileChooser.getSelectedFile();
            
            try {
                int idAnexo = controleAnexo.adicionarAnexo(ticket.getIdTicket(), arquivoSelecionado, pessoaLogada.getId());
                
                if (idAnexo > 0) {
                    JOptionPane.showMessageDialog(this, "Arquivo anexado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                    carregarAnexos();
                    if (atualizacaoListener != null) {
                        atualizacaoListener.onAtualizacaoRequerida();
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Falha ao anexar arquivo.", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao anexar: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }
}