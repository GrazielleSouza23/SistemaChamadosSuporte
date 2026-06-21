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

    // CORREÇÃO: inicialização estática garante thread-safety sem precisar de
    // synchronized. A instância é criada uma única vez quando a classe é carregada.
    private static final ConexaoBD instance = new ConexaoBD();

    private HikariDataSource dataSource;

    private ConexaoBD() {
        try {
            Properties props = new Properties();
            InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties");

            if (input == null) {
                throw new RuntimeException("Arquivo config.properties não encontrado!");
            }

            props.load(input);

            String baseUrl = props.getProperty("db.url");
            String ssl = props.getProperty("db.ssl");

            String envUser = System.getenv("DB_USER");
            String envPassword = System.getenv("DB_PASSWORD");

            String user = (envUser != null && !envUser.isEmpty())
                    ? envUser
                    : props.getProperty("db.user");

            String password = (envPassword != null && !envPassword.isEmpty())
                    ? envPassword
                    : props.getProperty("db.password");

            if (envUser == null) {
                System.out.println("AVISO: DB_USER nao definido. Usando usuario do config.properties.");
            } else {
                System.out.println("Credenciais carregadas a partir das variaveis de ambiente.");
            }

            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(baseUrl + ssl);
            config.setUsername(user);
            config.setPassword(password);

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
        return instance;
    }

    // CORREÇÃO: lança RuntimeException em vez de retornar null silenciosamente.
    // Retornar null causaria NullPointerException nos DAOs sem uma mensagem clara.
    public Connection getConnection() {
        if (dataSource == null) {
            throw new RuntimeException("Pool de conexões não foi inicializado corretamente.");
        }
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            System.err.println("Erro ao obter conexão do pool: " + e.getMessage());
            throw new RuntimeException("Falha ao obter conexão do banco de dados.", e);
        }
    }

    public void closePool() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
            System.out.println("Pool de conexões encerrado.");
        }
    }
}