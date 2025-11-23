package control;

import model.dao.ComentarioDAO;
import model.dao.HistoricoDAO;
import model.entidades.Historico;
import model.entidades.Ticket;
import model.entidades.Historico;

import java.util.List;
import model.entidades.Comentario;

/**
 * Classe de Controle (Controller) para a funcionalidade de Comentários.
 * Responsável por coordenar a lógica de negócio (adicionar, listar)
 * e interagir com a camada de persistência (ComentarioDAO e HistoricoDAO).
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
     * @param idTicket ID do Ticket.
     * @param conteudo Conteúdo do comentário.
     * @param tipo Tipo do comentário ("PUBLICO" ou "INTERNO").
     * @param idAutor ID da Pessoa (Usuário ou Agente) que está comentando.
     * @return O ID do Comentário recém-criado, ou -1 em caso de falha.
     */
    public int adicionarComentario(int idTicket, String conteudo, String tipo, int idAutor) {
        if (conteudo == null || conteudo.trim().isEmpty()) {
            throw new IllegalArgumentException("O conteúdo do comentário não pode ser vazio.");
        }
        
        Comentario novoComentario = new Comentario(conteudo, tipo, idTicket, idAutor);
        int idGerado = comentarioDAO.salvar(novoComentario);
        
        if (idGerado > 0) {
            // Adiciona registro no Histórico
            String acao = "COMENTARIO_ADICIONADO";
            String descricao = tipo.equals("PUBLICO") ? "Comentário público adicionado." : "Comentário interno adicionado.";
            Historico historico = new Historico(acao, descricao, idTicket, idAutor);
            historicoDAO.salvar(historico);
            
            // Lógica de Notificação
            Ticket ticket = controleTicket.buscarTicketPorId(idTicket);
            if (ticket != null) {
                Integer idUsuarioTicket = ticket.getIdUsuarioAbertura();

                if (idUsuarioTicket != null && idUsuarioTicket.equals(idAutor)) {
                    // Se o autor é o Usuário, notifica o Agente Responsável
                    controleNotificacao.notificarAgenteResponsavel(idTicket, 
                        "O Usuário comentou no Ticket #" + idTicket + ".");
                } else {
                    // Se o autor é o Agente, notifica o Usuário
                    controleNotificacao.notificarUsuario(idTicket, 
                        "O Agente comentou no seu Ticket #" + idTicket + ".");
                }
            }
        }
        
        return idGerado;
    }
    
    /**
     * Lista todos os Comentários (Públicos e Internos) para um Ticket (para Agentes).
     * @param idTicket ID do Ticket.
     * @return Lista de Comentários.
     */
    public List<Comentario> listarTodosComentariosPorTicket(int idTicket) {
        return comentarioDAO.listarPorTicket(idTicket);
    }
    
    /**
     * Lista apenas os Comentários Públicos para um Ticket (para Usuários).
     * @param idTicket ID do Ticket.
     * @return Lista de Comentários Públicos.
     */
    public List<Comentario> listarComentariosPublicosPorTicket(int idTicket) {
        return comentarioDAO.listarPublicosPorTicket(idTicket);
    }
}
