package control;

import model.dao.TicketDAO;
import model.dao.HistoricoDAO;
import model.entidades.Historico;
import model.entidades.Ticket;

import java.util.List;

public class ControleTicket {

    private TicketDAO ticketDAO;
    private HistoricoDAO historicoDAO;
    private ControleNotificacao controleNotificacao;

    public ControleTicket() {
        this.ticketDAO = new TicketDAO();
        this.historicoDAO = new HistoricoDAO();
        this.controleNotificacao = new ControleNotificacao();
    }
  
    public int abrirNovoTicket(String titulo, String descricao, String categoria, String prioridade, int idUsuario) {
        // Validação básica dos dados
        if (titulo == null || titulo.trim().isEmpty() || descricao == null || descricao.trim().isEmpty()) {
            throw new IllegalArgumentException("Título e descrição do ticket não podem ser vazios.");
        }
        
        // Cria o objeto Ticket
        Ticket novoTicket = new Ticket(titulo, descricao, categoria, prioridade, idUsuario);
        
        // Salva no banco de dados via DAO
        int idGerado = ticketDAO.salvar(novoTicket);
        
        if (idGerado > 0) {
            // Adiciona registro no Histórico
            Historico historico = new Historico("TICKET_CRIADO", "Ticket aberto pelo usuário.", idGerado, idUsuario);
            historicoDAO.salvar(historico);
            // Adiciona Notificação para Agentes
            controleNotificacao.notificarTodosAgentes(idGerado, "Novo Ticket #" + idGerado + " aberto: " + titulo);
        }
        
        return idGerado;
    }
    
    public List<Ticket> listarTodosTickets() {
        return ticketDAO.listarTodos();
    }
    
    public Ticket buscarTicketPorId(int idTicket) {
        return ticketDAO.buscarPorId(idTicket);
    }

    public List<Ticket> listarTicketsPorUsuario(int idUsuario) {
        return ticketDAO.listarPorUsuario(idUsuario);
    }
    
    /**
     * Atribui um Ticket a um Agente de Suporte.
     * @param idTicket O ID do Ticket a ser atribuído.
     * @param idAgente O ID do Agente responsável.
     * @return true se a atribuição foi bem-sucedida, false caso contrário.
     */
    public boolean atribuirTicket(int idTicket, int idAgente) {

        boolean sucesso = ticketDAO.atualizarStatusAgente(idTicket, "EM_ANDAMENTO", idAgente);
        
        if (sucesso) {
            // Adiciona registro no Histórico
            Historico historico = new Historico("TICKET_ATRIBUIDO", "Ticket atribuído ao Agente ID " + idAgente + ". Status alterado para EM_ANDAMENTO.", idTicket, idAgente);
            historicoDAO.salvar(historico);
            // Adiciona Notificação para o Usuário
            controleNotificacao.notificarUsuario(idTicket, "Seu Ticket #" + idTicket + " foi atribuído ao Agente ID " + idAgente + ".");
        }
        
        return sucesso;
    }
    
    public boolean atualizarStatus(int idTicket, String novoStatus, int idResponsavel) {
        boolean sucesso = ticketDAO.atualizarStatus(idTicket, novoStatus);
        
        if (sucesso) {
            // Adiciona registro no Histórico
            Historico historico = new Historico("STATUS_ALTERADO", "Status alterado para " + novoStatus + ".", idTicket, idResponsavel);
            historicoDAO.salvar(historico);
            // Adiciona Notificação para o Usuário
            controleNotificacao.notificarUsuario(idTicket, "O status do seu Ticket #" + idTicket + " foi alterado para " + novoStatus + ".");
        }
        
        return sucesso;
    }

    public boolean fecharTicket(int idTicket, int idResponsavel) {
        boolean sucesso = ticketDAO.fecharTicket(idTicket);
        
        if (sucesso) {
            // Adiciona registro no Histórico
            Historico historico = new Historico("TICKET_FECHADO", "Ticket finalizado e fechado.", idTicket, idResponsavel);
            historicoDAO.salvar(historico);
            // Adiciona Notificação para o Usuário
            controleNotificacao.notificarUsuario(idTicket, "Seu Ticket #" + idTicket + " foi fechado.");
        }
        
        return sucesso;
    }
    
    public List<Ticket> listarTicketsComFiltro(String status, String prioridade) {
        List<Ticket> todosTickets = ticketDAO.listarTodos();
        
        if ((status == null || status.isEmpty()) && (prioridade == null || prioridade.isEmpty())) {
            return todosTickets;
        }
        
        return todosTickets.stream()
                .filter(t -> (status == null || status.isEmpty() || t.getStatus().equalsIgnoreCase(status)))
                .filter(t -> (prioridade == null || prioridade.isEmpty() || t.getPrioridade().equalsIgnoreCase(prioridade)))
                .collect(java.util.stream.Collectors.toList());
    }
}