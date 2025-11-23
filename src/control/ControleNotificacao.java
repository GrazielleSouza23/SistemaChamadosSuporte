package control;

import model.dao.NotificacaoDAO;
import model.dao.TicketDAO;
import model.entidades.Notificacao;
import model.entidades.Ticket;

import java.util.List;

/**
 * Classe de Controle (Controller) para a funcionalidade de Notificações.
 * Responsável por coordenar a lógica de negócio (criação, listagem, marcação como lida).
 */
public class ControleNotificacao {

    private NotificacaoDAO notificacaoDAO;
    private TicketDAO ticketDAO;

    public ControleNotificacao() {
        this.notificacaoDAO = new NotificacaoDAO();
        this.ticketDAO = new TicketDAO();
    }

    /**
     * Cria e salva uma nova notificação.
     * @param tipo Tipo da notificação (ex: "RESPOSTA_AGENTE", "RESPOSTA_USUARIO", "NOVO_TICKET")
     * @param conteudo Texto descritivo da notificação.
     * @param idDestinatario ID da Pessoa que deve receber a notificação.
     * @param idTicket ID do Ticket relacionado.
     * @return O ID da notificação criada.
     */
    public int criarNotificacao(String tipo, String conteudo, int idDestinatario, int idTicket) {
    if (tipo == null || tipo.isBlank() || conteudo == null || conteudo.isBlank() || idDestinatario <= 0) {
        System.err.println("[Aviso] Dados inválidos para criar notificação.");
        return -1;
    }

    Notificacao notificacao = new Notificacao(tipo, conteudo, idDestinatario, idTicket);
    return notificacaoDAO.salvar(notificacao);
}


    /**
     * Lista todas as notificações não lidas para um destinatário.
     */
    public List<Notificacao> listarNaoLidas(int idDestinatario) {
        return notificacaoDAO.listarNaoLidasPorDestinatario(idDestinatario);
    }

    /**
     * Marca uma notificação como lida.
     */
    public boolean marcarComoLida(int idNotificacao) {
        return notificacaoDAO.marcarComoLida(idNotificacao);
    }

    // --- Integração com a lógica de negócio ---

    /**
     * Notifica o Usuário quando o status do seu Ticket muda ou um Agente comenta.
     */
    public void notificarUsuario(int idTicket, String mensagem) {
        Ticket ticket = ticketDAO.buscarPorId(idTicket);
        if (ticket != null && ticket.getIdUsuarioAbertura() > 0) {
            criarNotificacao(
                "RESPOSTA_AGENTE",
                mensagem,
                ticket.getIdUsuarioAbertura(),
                idTicket
            );
        } else {
            System.err.println("[Aviso] Notificação não enviada: Ticket sem usuário associado (ID " + idTicket + ")");
        }
    }

    /**
     * Notifica o Agente responsável quando o Usuário comenta no Ticket.
     */
    public void notificarAgenteResponsavel(int idTicket, String mensagem) {
        Ticket ticket = ticketDAO.buscarPorId(idTicket);
        if (ticket != null && ticket.getIdAgenteResponsavel() != null) {
            criarNotificacao(
                "RESPOSTA_USUARIO",
                mensagem,
                ticket.getIdAgenteResponsavel(),
                idTicket
            );
        }
    }

    /**
     * Notifica todos os Agentes (ex: quando um novo Ticket é aberto).
     * (Simplificação: em um sistema real, buscaria todos os Agentes no PessoaDAO)
     */
    public void notificarTodosAgentes(int idTicket, String mensagem) {
        criarNotificacao(
            "NOVO_TICKET",
            mensagem,
            3, // exemplo: agente fixo
            idTicket
        );
    }
}