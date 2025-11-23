package model.dao;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Classe Singleton para gerenciamento de conexão usando HikariCP.
 * Agora com fallback correto: variáveis de ambiente → config.properties.
 */
public class ConexaoBD {

    private static ConexaoBD instance;
    private HikariDataSource dataSource;

    private ConexaoBD() {
        try {
            // Carregar config.properties (inclui db.user e db.password)
            Properties props = new Properties();
            InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties");

            if (input == null) {
                throw new RuntimeException("Arquivo config.properties não encontrado!");
            }

            props.load(input);

            String baseUrl = props.getProperty("db.url");
            String ssl = props.getProperty("db.ssl");

            // --- tenta ler usuário e senha das variáveis de ambiente ---
            String envUser = System.getenv("DB_USER");
            String envPassword = System.getenv("DB_PASSWORD");

            // Fallback: usar o que está no config.properties (ambiente local)
            String user = (envUser != null && !envUser.isEmpty())
                    ? envUser
                    : props.getProperty("db.user");

            String password = (envPassword != null && !envPassword.isEmpty())
                    ? envPassword
                    : props.getProperty("db.password");

            // Log amigável (sem mostrar senha)
            if (envUser == null) {
                System.out.println("AVISO: DB_USER nao definido. Usando usuario do config.properties.");
            } else {
                System.out.println("Credenciais carregadas a partir das variaveis de ambiente.");
            }

            // Configuração do HikariCP
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(baseUrl + ssl);
            config.setUsername(user);
            config.setPassword(password);

            // Parâmetros do pool
            config.setMaximumPoolSize(Integer.parseInt(props.getProperty("db.pool.max", "10")));
            config.setMinimumIdle(Integer.parseInt(props.getProperty("db.pool.min", "2")));
            config.setConnectionTimeout(Integer.parseInt(props.getProperty("db.pool.timeout", "30000")));
            config.setIdleTimeout(60000);
            config.setMaxLifetime(1800000);

            config.setDriverClassName("com.mysql.cj.jdbc.Driver");

            dataSource = new HikariDataSource(config);

            System.out.println("HikariCP inicializado com sucesso!");

        } catch (Exception e) {
            System.err.println("Erro ao configurar pool de conexões: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static ConexaoBD getInstance() {
        if (instance == null) {
            instance = new ConexaoBD();
        }
        return instance;
    }

    public Connection getConnection() {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            System.err.println("Erro ao obter conexão do pool: " + e.getMessage());
            return null;
        }
    }

    public void closePool() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
            System.out.println("Pool de conexões encerrado.");
        }
    }
}
