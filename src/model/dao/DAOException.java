package model.dao;

public class DAOException extends Exception {
    
    // Construtor que aceita mensagem e a causa raiz (ex: SQLException)
    public DAOException(String message, Throwable cause) {
        super(message, cause);
    }

    // Construtor apenas com mensagem
    public DAOException(String message) {
        super(message);
    }
}