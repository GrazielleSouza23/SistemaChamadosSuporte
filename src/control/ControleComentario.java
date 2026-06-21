package control;

import model.dao.ComentarioDAO;
import model.dao.HistoricoDAO;
import model.entidades.Comentario;
import model.entidades.Historico;
import model.entidades.Ticket;

import java.util.List;

/**
 * Classe de Controle (Controller) para a funcionalidade de Comentários.
 */
public class ControleComentario {

    private ComentarioDAO comentarioDAO;
    private HistoricoDAO historicoDAO;
    private ControleNotificacao controleNotificacao;
    private ControleTicket controleTicket;

    public ControleComentario() {
        this.comentarioDAO = new ComentarioDAO();
        this.historicoDAO = new HistoricoDAO();
        this.controleNotificacao = new ControleNotificacao();
        this.controleTicket = new ControleTicket();
    }

    /**
     * Adiciona um novo Comentário ao Ticket.
     */
    public int adicionarComentario(int idTicket, String conteudo, String tipo, int idAutor) {
        if (conteudo == null || conteudo.trim().isEmpty()) {
            throw new IllegalArgumentException("O conteúdo do comentário não pode ser vazio.");
        }

        Comentario novoComentario = new Comentario(conteudo, tipo, idTicket, idAutor);
        int idGerado = comentarioDAO.salvar(novoComentario);

        if (idGerado > 0) {
            String acao = "COMENTARIO_ADICIONADO";
            String descricao = tipo.equals("PUBLICO") ? "Comentário público adicionado." : "Comentário interno adicionado.";
            Historico historico = new Historico(acao, descricao, idTicket, idAutor);
            historicoDAO.salvar(historico);

            Ticket ticket = controleTicket.buscarTicketPorId(idTicket);
            if (ticket != null) {
                Integer idUsuarioTicket = ticket.getIdUsuarioAbertura();

                if (idUsuarioTicket != null && idUsuarioTicket.equals(idAutor)) {
                    controleNotificacao.notificarAgenteResponsavel(idTicket,
                        "O Usuário comentou no Ticket #" + idTicket + ".");
                } else {
                    controleNotificacao.notificarUsuario(idTicket,
                        "O Agente comentou no seu Ticket #" + idTicket + ".");
                }
            }
        }

        return idGerado;
    }

    /**
     * Lista todos os Comentários (Públicos e Internos) para um Ticket (para Agentes).
     */
    public List<Comentario> listarTodosComentariosPorTicket(int idTicket) {
        return comentarioDAO.listarPorTicket(idTicket);
    }

    /**
     * Lista apenas os Comentários Públicos para um Ticket (para Usuários).
     */
    public List<Comentario> listarComentariosPublicosPorTicket(int idTicket) {
        return comentarioDAO.listarPublicosPorTicket(idTicket);
    }
}