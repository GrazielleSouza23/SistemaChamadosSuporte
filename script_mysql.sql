-- =================================================================
-- SCRIPT SQL PARA CRIAÇÃO DO BANCO DE DADOS E TABELAS (MySQL)
-- SISTEMA DE CHAMADOS DE SUPORTE
-- =================================================================

-- 1. CRIAÇÃO DO BANCO DE DADOS
DROP DATABASE IF EXISTS `sistema_chamados`;
CREATE DATABASE `sistema_chamados`;
USE `sistema_chamados`;

-- 2. TABELA PESSOA (Superclasse para herança)
-- Contém os dados comuns a Usuários e Agentes.
CREATE TABLE Pessoa (
    id_pessoa INT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL, -- Armazenar hash da senha
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 3. TABELA USUARIO (Subclasse)
-- Herda de Pessoa.
CREATE TABLE Usuario (
    id_usuario INT PRIMARY KEY,
    -- Chave estrangeira para a tabela Pessoa
    FOREIGN KEY (id_usuario) REFERENCES Pessoa(id_pessoa)
        ON DELETE CASCADE -- Se a Pessoa for deletada, o Usuario também é
);

-- 4. TABELA AGENTE (Subclasse)
-- Herda de Pessoa e adiciona atributos específicos.
CREATE TABLE Agente (
    id_agente INT PRIMARY KEY,
    nivel_acesso ENUM('BASICO', 'AVANCADO', 'ADMIN') NOT NULL DEFAULT 'BASICO',
    -- Chave estrangeira para a tabela Pessoa
    FOREIGN KEY (id_agente) REFERENCES Pessoa(id_pessoa)
        ON DELETE CASCADE
);

-- 5. TABELA TICKET
CREATE TABLE Ticket (
    id_ticket INT PRIMARY KEY AUTO_INCREMENT,
    titulo VARCHAR(255) NOT NULL,
    descricao TEXT NOT NULL,
    status ENUM('ABERTO', 'EM_ANDAMENTO', 'AGUARDANDO_RESPOSTA', 'RESOLVIDO', 'FECHADO') NOT NULL DEFAULT 'ABERTO',
    categoria VARCHAR(100) NOT NULL,
    prioridade ENUM('BAIXA', 'MEDIA', 'ALTA', 'URGENTE') NOT NULL DEFAULT 'MEDIA',
    data_abertura TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_fechamento TIMESTAMP NULL,
    
    -- Relacionamento com o Usuário que abriu o ticket
    id_usuario_abertura INT NOT NULL,
    FOREIGN KEY (id_usuario_abertura) REFERENCES Usuario(id_usuario),
    
    -- Relacionamento com o Agente responsável (pode ser NULL)
    id_agente_responsavel INT NULL,
    FOREIGN KEY (id_agente_responsavel) REFERENCES Agente(id_agente)
);

-- 6. TABELA COMENTARIO
CREATE TABLE Comentario (
    id_comentario INT PRIMARY KEY AUTO_INCREMENT,
    conteudo TEXT NOT NULL,
    tipo ENUM('PUBLICO', 'INTERNO') NOT NULL, -- Público (Usuário/Agente) ou Interno (Agente)
    data_comentario TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    -- Relacionamento com o Ticket
    id_ticket INT NOT NULL,
    FOREIGN KEY (id_ticket) REFERENCES Ticket(id_ticket)
        ON DELETE CASCADE,
        
    -- Relacionamento com a Pessoa (Usuário ou Agente) que adicionou o comentário
    id_autor INT NOT NULL,
    FOREIGN KEY (id_autor) REFERENCES Pessoa(id_pessoa)
);

-- 7. TABELA ANEXO
CREATE TABLE Anexo (
    id_anexo INT PRIMARY KEY AUTO_INCREMENT,
    nome_arquivo VARCHAR(255) NOT NULL,
    tipo_arquivo VARCHAR(50),
    tamanho DOUBLE,
    data_upload TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    caminho_armazenamento VARCHAR(512) NOT NULL, -- Caminho físico ou URL do arquivo
    
    -- Relacionamento com o Ticket
    id_ticket INT NOT NULL,
    FOREIGN KEY (id_ticket) REFERENCES Ticket(id_ticket)
        ON DELETE CASCADE
);

-- 8. TABELA HISTORICO
CREATE TABLE Historico (
    id_historico INT PRIMARY KEY AUTO_INCREMENT,
    acao VARCHAR(100) NOT NULL, -- Ex: 'STATUS_ALTERADO', 'COMENTARIO_ADICIONADO', 'TICKET_ATRIBUIDO'
    descricao TEXT,
    data_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    -- Relacionamento com o Ticket
    id_ticket INT NOT NULL,
    FOREIGN KEY (id_ticket) REFERENCES Ticket(id_ticket)
        ON DELETE CASCADE,
        
    -- Relacionamento com a Pessoa (Usuário ou Agente) que realizou a ação
    id_responsavel INT NOT NULL,
    FOREIGN KEY (id_responsavel) REFERENCES Pessoa(id_pessoa)
);

-- 9. TABELA NOTIFICACAO
CREATE TABLE Notificacao (
    id_notificacao INT PRIMARY KEY AUTO_INCREMENT,
    tipo VARCHAR(50) NOT NULL, -- Ex: 'NOVO_TICKET', 'RESPOSTA_AGENTE', 'STATUS_ATUALIZADO'
    conteudo TEXT NOT NULL,
    lido BOOLEAN NOT NULL DEFAULT FALSE,
    data_envio TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    -- Relacionamento com a Pessoa (Usuário ou Agente) que recebe a notificação
    id_destinatario INT NOT NULL,
    FOREIGN KEY (id_destinatario) REFERENCES Pessoa(id_pessoa),
    
    -- Referência opcional ao Ticket relacionado
    id_ticket INT NULL,
    FOREIGN KEY (id_ticket) REFERENCES Ticket(id_ticket)
        ON DELETE SET NULL
);

-- =================================================================
-- DADOS DE TESTE (BCRYPT HASH)
-- =================================================================

-- Senhas planas (apenas para referência):
-- joao.silva@email.com   -> 'senha123'
-- maria.souza@email.com  -> 'senha456'
-- agente.carlos@suporte.com -> 'agente123'
-- agente.marcia@suporte.com -> 'agente456'

-- Inserção de Pessoas
INSERT INTO Pessoa (nome, email, senha) VALUES
-- Usuário 1: João Silva (senha: senha123)
('João Silva', 'joao.silva@email.com', '$2a$12$RSu.YUTx/EAs7Z4Nh56m8u4SKvYK0/BpPy9LuG392dIaM5EeJ9UQe'),
-- Usuário 2: Maria Souza (senha: senha456)
('Maria Souza', 'maria.souza@email.com', '$2a$12$YE8LudP3Ky67Hekby1Erx.5WbSAA5RcQqhJaHCGg3TIRfMMYnFEc.'),
-- Agente 1: Carlos Ribeiro (senha: agente123)
('Carlos Ribeiro', 'agente.carlos@suporte.com', '$2a$12$dfPkprvIk3RS/i1glQopa.kF55Sf7YyuQwxxhkySwgAkUzDOHk46G'),
-- Agente 2: Márcia Fagundes (senha: agente456)
('Márcia Fagundes', 'agente.marcia@suporte.com', '$2a$12$smYK/oDfZFb1mYVD8Ej9duA9ES0HUtai4r6Vsedu/OUvqzrKIZ/be');

-- Inserção de Usuários (Herança: id_usuario = id_pessoa)
INSERT INTO Usuario (id_usuario) VALUES (1), (2);

-- Inserção de Agentes (Relacionamento de Herança)
INSERT INTO Agente (id_agente, nivel_acesso) VALUES
(3, 'AVANCADO'), -- Carlos é o Agente 3
(4, 'BASICO');   -- Márcia é a Agente 4

-- Inserção de Dados nas Tabelas
INSERT INTO `ticket` (`id_ticket`, `titulo`, `descricao`, `status`, `categoria`, `prioridade`, `data_abertura`, `data_fechamento`, `id_usuario_abertura`, `id_agente_responsavel`) VALUES
(1, 'Sem acesso à internet', 'Olá, estou sem conexão com a internet no momento. Já verifiquei os cabos e o roteador, tudo parece normal, mas não consigo acessar nenhum site. Outros dispositivos também não estão conectando.', 'ABERTO', 'ACESSO', 'ALTA', '2025-11-04 15:59:41', NULL, 1, NULL),
(2, 'Impressora não está imprimindo', 'Olá, estou tentando imprimir alguns documentos, mas a impressora não está respondendo. Ela aparece como “offline” mesmo estando ligada e conectada ao computador. Já tentei reiniciar o equipamento e verificar os cabos, mas o problema continua.', 'ABERTO', 'FUNCIONALIDADE', 'MEDIA', '2025-11-04 16:27:47', NULL, 2, NULL),
(3, 'Não consigo acessar a VPN da empresa', 'Ao tentar conectar à VPN da empresa, recebo a mensagem de erro \"Falha na autenticação\". Já tentei reiniciar o computador e verificar minha senha, mas o problema persiste.', 'ABERTO', 'ACESSO', 'ALTA', '2025-11-04 16:52:48', NULL, 2, NULL);