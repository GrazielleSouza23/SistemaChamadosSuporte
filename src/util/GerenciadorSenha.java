package util;

import org.mindrot.jbcrypt.BCrypt;

/**
 * Utilitário para lidar com Hashing e Verificação de Senhas usando BCrypt.
 */

public class GerenciadorSenha {
    
    // O "work factor" (custo). 12 é um bom padrão.
    private static final int WORK_FACTOR = 12;
    
    public static void main(String[] args) {
    String[] senhasPlanas = { "senha123", "senha456", "agente123", "agente456" };
    for (String senha : senhasPlanas) {
        String hash = hashSenha(senha);
        System.out.println(senha + " -> " + hash);
    }
}

    public static String hashSenha(String senhaPlana) {
        return BCrypt.hashpw(senhaPlana, BCrypt.gensalt(WORK_FACTOR));
    }
    

    public static boolean verificarSenha(String senhaPlana, String hashArmazenado) {
        if (hashArmazenado == null) {
            return false;
        }
        try {
            // O Bcrypt compara a senha plana com o hash.
            return BCrypt.checkpw(senhaPlana, hashArmazenado);
        } catch (IllegalArgumentException e) {
            // Hash mal formatado
            System.err.println("Erro ao verificar hash (formato inválido): " + e.getMessage());
            return false;
        }
    }
}