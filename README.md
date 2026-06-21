## 💻 Sobre o Projeto

O Sistema de Chamados de Suporte é uma aplicação desktop robusta, desenvolvida integralmente em Java, com o propósito de otimizar a gestão e o rastreamento de solicitações de suporte técnico, conhecidas como tickets. O projeto foi concebido para criar um ambiente organizado e eficiente, separando de forma clara as interações e responsabilidades entre os Usuários Finais (clientes que abrem os chamados) e os Agentes de Suporte (profissionais responsáveis por resolver as solicitações).

A arquitetura do sistema é baseada em camadas, seguindo o padrão Model-View-Controller (MVC), o que garante alta modularidade, facilidade de manutenção e escalabilidade. Um dos focos principais do desenvolvimento foi a segurança e a performance. Para isso, foram implementadas soluções de nível empresarial, como o uso do HikariCP para gerenciamento de pool de conexões de banco de dados de alto desempenho, o algoritmo BCrypt para o hashing seguro de senhas, e a criptografia AES-256 GCM para proteger dados sensíveis armazenados. Este projeto serve como uma demonstração prática de como integrar segurança, performance e boas práticas de design em uma aplicação de gestão de serviços.

## 🏗️ Arquitetura do Projeto

O sistema foi desenvolvido seguindo o padrão Model-View-Controller (MVC), promovendo a separação de responsabilidades entre interface, regras de negócio e acesso a dados.

```text
src/
├── control/      → Controle das regras e fluxos da aplicação
├── model/
│   ├── entidades → Entidades do domínio do sistema
│   └── dao       → Persistência e acesso ao banco de dados
├── util/         → Utilitários de segurança e suporte
└── view/         → Interface gráfica desenvolvida com Java Swing
```

Essa estrutura facilita a manutenção, evolução e escalabilidade da aplicação.


## ✨ Funcionalidades Principais

*   **Gestão de Tickets:** Abertura, acompanhamento, atribuição e fechamento de chamados.
*   **Comunicação Integrada:** Adição de comentários públicos (visíveis ao cliente) e internos (visíveis apenas aos agentes).
*   **Anexos:** Suporte para anexar arquivos aos tickets.
*   **Notificações:** Sistema de notificação para alertar usuários e agentes sobre atualizações.
*   **Controle de Acesso:** Distinção clara entre perfis de **Usuário** e **Agente** (com diferentes níveis de acesso: Básico, Avançado, Admin).
*   **Segurança Avançada:** Implementação de criptografia forte para senhas e dados sensíveis, além de prevenção contra SQL Injection.

## 🛠️ Tecnologias Utilizadas

| Categoria | Tecnologia | Detalhes |
| :--- | :--- | :--- |
| **Linguagem** | Java | Linguagem principal de desenvolvimento. |
| **Interface** | Java Swing | Utilizado para a construção da interface gráfica desktop. |
| **Banco de Dados** | MySQL | Sistema de gerenciamento de banco de dados relacional. |
| **Persistência** | JDBC, HikariCP | Utilização do **HikariCP** para um pool de conexões de alto desempenho. |
| **Segurança** | BCrypt, AES-256 GCM | **BCrypt** para hashing seguro de senhas e **AES-256 GCM** para criptografia de dados sensíveis. |
| **Arquitetura** | MVC (Model-View-Controller) | Padrão de projeto aplicado para garantir a separação de responsabilidades. |

## 🚀 Desafios Técnicos Implementados

Durante o desenvolvimento foram aplicadas práticas comuns em sistemas corporativos, incluindo:

* Autenticação e controle de acesso por perfis de usuário.
* Hash seguro de senhas utilizando BCrypt.
* Criptografia AES-256 GCM para proteção de dados sensíveis.
* Gerenciamento eficiente de conexões com HikariCP.
* Separação entre camadas de apresentação, controle e persistência.
* Utilização de DAOs específicos para cada entidade do sistema.
* Prevenção contra SQL Injection através de consultas parametrizadas.
* Registro de histórico e notificações de eventos do sistema.


## 📷 Telas do Sistema

### 🔐 Tela de Login - Usuário

<img width="384" height="240" alt="Tela de Login" src="https://github.com/user-attachments/assets/2a9efb51-6b15-4c3c-b1e7-4f3dfffd5aee" />
<img width="383" height="241" alt="Usuário" src="https://github.com/user-attachments/assets/0d840a24-cb97-4bb9-b99c-62c713eb7615" />

### 🔐 Tela de Login - Agente

<img width="381" height="240" alt="Tela de Login Agente" src="https://github.com/user-attachments/assets/2a0083e1-94d3-4f90-9209-35a8f2a85d6a" />

<img width="381" height="240" alt="Agente" src="https://github.com/user-attachments/assets/7a02c07f-916f-4c14-8b1a-8187011d7b04" />

Sistema de autenticação com validação de credenciais e controle de acesso baseado em perfis (Usuário ou Agente).

---
### 👨‍💼 Painel do Usuário e Meus Tickets (Usuário)

<img width="1074" height="588" alt="Painel do Usuário" src="https://github.com/user-attachments/assets/948a4e91-912a-4825-bea9-daf827836d4e" />

A interface principal para usuários finais, onde podem visualizar seus tickets, abrir novos e gerenciar notificações.
A interface também lista de todos os chamados abertos pelo usuário, com status e prioridade.

---
### 📝 Abrir Ticket (Chamado)
<img width="484" height="442" alt="Abrir Chamado" src="https://github.com/user-attachments/assets/a50b8d56-24b7-494e-95dd-681fa617a5f9" />

Formulário para usuários registrarem novas solicitações de suporte, incluindo título, descrição, categoria e prioridade.

---

### 💬 Detalhes do Ticket (Visão do Usuário)

<img width="1363" height="716" alt="Detalhes Usuário" src="https://github.com/user-attachments/assets/6b1ee5f5-c57b-4f2f-89f3-bf0922ebdcf8" />

Exibição detalhada de um ticket específico, mostrando informações, histórico de comentários públicos e anexos.

---

### 👨‍💼 Painel do Agente e Lista de Tickets (Agente)

<img width="1020" height="691" alt="Painel do Agente" src="https://github.com/user-attachments/assets/0ad2d919-c24d-4d74-a6e8-2b0eeee77755" />

A interface principal para agentes de suporte, com uma visão geral dos tickets e acesso às ferramentas de gerenciamento.
Visão consolidada de todos os tickets no sistema, permitindo filtros por status, prioridade e atribuição.

---

### 🛠️ Detalhes do Ticket (Visão do Agente)

<img width="1061" height="587" alt="Detalhes Agente" src="https://github.com/user-attachments/assets/e013add5-c686-442a-bac6-c70dc265585a" />

Exibição detalhada de um ticket com as ferramentas de gerenciamento para agentes: atualização de status, atribuição, adição 

---

### 🔔 Notificações - Usuário
<img width="1074" height="586" alt="Notificações Usuário" src="https://github.com/user-attachments/assets/4e0fade4-47a8-4d53-aac3-c23acb4e0189" />

### 🔔 Notificações - Agente
<img width="1023" height="693" alt="Notificações Agente" src="https://github.com/user-attachments/assets/992144ac-7cde-41af-9578-64bcd2692c58" />

Tela onde usuários e agentes podem visualizar alertas sobre atualizações de tickets, novos comentários ou atribuições.

---

### ⚙️ Gerenciar Perfil - Usuário

<img width="432" height="289" alt="Gerenciar Usuário" src="https://github.com/user-attachments/assets/1effffb7-4629-463f-a469-c1201ac0a490" />

### ⚙️ Gerenciar Perfil - Agente

<img width="434" height="287" alt="Gerenciar Agente" src="https://github.com/user-attachments/assets/9c5045cd-1efe-45a8-8385-efe553291eaa" />

Formulário para usuários e agentes atualizarem suas informações de perfil, como nome, e-mail e senha.

---

### 📜 Histórico de Chamados
<img width="581" height="390" alt="Histório Usuário" src="https://github.com/user-attachments/assets/21b6473d-fc08-4757-88b4-caeb5a7da314" />

Registro cronológico de todas as ações e mudanças de status em um ticket.

---


## 👩‍💻 Autor

Desenvolvido por **Grazielle Souza**.

---

## 👤 Contato / Sobre mim

[![Email](https://img.shields.io/badge/Email-graziellesouza2305%40gmail.com-red?logo=gmail&logoColor=white)](mailto:graziellesouza2305@gmail.com)  
[![LinkedIn](https://img.shields.io/badge/LinkedIn-Grazielle%20Souza-blue?logo=linkedin&logoColor=white)](https://www.linkedin.com/in/grazielle-souza-5374bb315/)
