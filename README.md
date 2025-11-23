## 💻 Sobre o Projeto

O Sistema de Chamados de Suporte é uma aplicação desktop robusta, desenvolvida integralmente em Java, com o propósito de otimizar a gestão e o rastreamento de solicitações de suporte técnico, conhecidas como tickets. O projeto foi concebido para criar um ambiente organizado e eficiente, separando de forma clara as interações e responsabilidades entre os Usuários Finais (clientes que abrem os chamados) e os Agentes de Suporte (profissionais responsáveis por resolver as solicitações).

A arquitetura do sistema é baseada em camadas, seguindo o padrão Model-View-Controller (MVC), o que garante alta modularidade, facilidade de manutenção e escalabilidade. Um dos focos principais do desenvolvimento foi a segurança e a performance. Para isso, foram implementadas soluções de nível empresarial, como o uso do HikariCP para gerenciamento de pool de conexões de banco de dados de alto desempenho, o algoritmo BCrypt para o hashing seguro de senhas, e a criptografia AES-256 GCM para proteger dados sensíveis armazenados. Este projeto serve como uma demonstração prática de como integrar segurança, performance e boas práticas de design em uma aplicação de gestão de serviços.


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

---

## 👩‍💻 Autor

Desenvolvido por **Grazielle Souza**.

---

## 👤 Contato / Sobre mim

[![Email](https://img.shields.io/badge/Email-graziellesouza2305%40gmail.com-red?logo=gmail&logoColor=white)](mailto:graziellesouza2305@gmail.com)  
[![LinkedIn](https://img.shields.io/badge/LinkedIn-Grazielle%20Souza-blue?logo=linkedin&logoColor=white)](https://www.linkedin.com/in/grazielle-souza-5374bb315/)
