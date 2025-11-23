package view.listener;

/**
 * Interface para notificar componentes que precisam de atualização.
 * Implementa o padrão Observer/Listener para desacoplar a lógica de atualização.
 */
public interface AtualizacaoListener {
    /**
     * Método chamado quando uma ação que requer a atualização da lista de tickets é concluída.
     */
    void onAtualizacaoRequerida();
}
