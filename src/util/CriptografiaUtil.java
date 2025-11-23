package util;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

public class CriptografiaUtil {

    private static final String ALGORITHM = "AES/GCM/NoPadding";
    private static final int GCM_IV_LENGTH = 12; // 12 bytes (96 bits) é o recomendado para GCM
    private static final int GCM_TAG_LENGTH = 16; // 16 bytes (128 bits)

    private static SecretKey secretKey;

    // Inicializador estático para carregar a chave da variável de ambiente
    static {
        try {
            // 1️⃣ Tenta primeiro buscar da variável de ambiente (ex: Windows/Linux)
            String base64Key = System.getenv("SISTEMA_CHAMADOS_AES_KEY");

            // 2️⃣ Se não encontrar, tenta nas system properties (-D)
            if (base64Key == null || base64Key.isEmpty()) {
                base64Key = System.getProperty("SISTEMA_CHAMADOS_AES_KEY");
            }

            if (base64Key == null || base64Key.isEmpty()) {
                throw new IllegalArgumentException("Variável 'SISTEMA_CHAMADOS_AES_KEY' não encontrada nem no ambiente nem nas propriedades do sistema.");
            }

            // Decodifica e valida o tamanho (32 bytes = 256 bits)
            byte[] keyBytes = Base64.getDecoder().decode(base64Key);
            if (keyBytes.length != 32) {
                throw new IllegalArgumentException("A chave AES deve ter 32 bytes (256 bits).");
            }

            secretKey = new SecretKeySpec(keyBytes, "AES");
            System.out.println("Chave de Criptografia AES carregada com sucesso.");

        } catch (Exception e) {
            System.err.println("FALHA CRÍTICA AO CARREGAR A CHAVE DE CRIPTOGRAFIA!");
            e.printStackTrace();
            throw new RuntimeException("Não foi possível inicializar o CriptografiaUtil", e);
        }
    }

    public static String encrypt(String data) {
        if (data == null) return null;
        try {
            byte[] iv = new byte[GCM_IV_LENGTH];
            new SecureRandom().nextBytes(iv); // Gera um IV (Vetor de Inicialização) aleatório

            Cipher cipher = Cipher.getInstance(ALGORITHM);
            GCMParameterSpec parameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH * 8, iv);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, parameterSpec);

            byte[] encryptedData = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
            
            // Concatena o IV com os dados criptografados (ambos em Base64)
            String ivBase64 = Base64.getEncoder().encodeToString(iv);
            String encryptedDataBase64 = Base64.getEncoder().encodeToString(encryptedData);
            
            return ivBase64 + ":" + encryptedDataBase64;

        } catch (Exception e) {
            System.err.println("Erro ao criptografar: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Erro ao criptografar dados", e);
        }
    }

    /**
     * Descriptografa uma String (formato: IV_Base64 + ":" + Criptografado_Base64)
     */
    public static String decrypt(String encryptedData) {
        if (encryptedData == null) return null;
        try {
            String[] parts = encryptedData.split(":");
            if (parts.length != 2) {
                return encryptedData;
            }

            byte[] iv = Base64.getDecoder().decode(parts[0]);
            byte[] cipherText = Base64.getDecoder().decode(parts[1]);

            Cipher cipher = Cipher.getInstance(ALGORITHM);
            GCMParameterSpec parameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH * 8, iv);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, parameterSpec);

            byte[] decryptedData = cipher.doFinal(cipherText);
            
            return new String(decryptedData, StandardCharsets.UTF_8);

        } catch (Exception e) {
            System.err.println("Erro ao descriptografar: " + e.getMessage() + ". Retornando 'DADO ILEGÍVEL'.");
            // Pode falhar se a chave estiver errada ou os dados corrompidos
            return "[DADO ILEGÍVEL]";
        }
    }
    

}