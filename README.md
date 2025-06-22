# Cadastro de Pessoas - Desafio Sinerji

Este projeto é uma aplicação web para cadastro e gerenciamento de pessoas, desenvolvida com a plataforma Jakarta EE. O sistema inclui funcionalidades de CRUD, controle de acesso baseado em papéis (`ADMIN` e `USER`) e exportação de dados.

---

## 1. Decisões Técnicas e Arquiteturais

A arquitetura do sistema foi projetada para ser **simples, robusta e escalável**, seguindo os padrões consolidados do ecossistema Jakarta EE.

- **Arquitetura em Camadas:** A aplicação é dividida em camadas claras (Apresentação, Controle, Persistência e Segurança) para garantir a separação de responsabilidades e facilitar a manutenção.
- **Padrão MVC (Model-View-Controller):** Utilizamos o JSF, que implementa o padrão MVC, para separar a lógica de negócio (Controller), os dados (Model) e a interface do usuário (View).
- **Injeção de Dependências (CDI):** O `Contexts and Dependency Injection` (CDI) é usado para gerenciar o ciclo de vida dos beans e desacoplar os componentes. Beans `@RequestScoped` são usados para ações pontuais, enquanto o `@SessionScoped` é usado para manter o estado do usuário logado.
- **Persistência com JPA:** O `Jakarta Persistence API` (JPA) com Hibernate abstrai o acesso ao banco de dados, permitindo um desenvolvimento mais rápido e um código mais limpo, focado em objetos Java em vez de SQL.

### Justificativa de Frameworks e Bibliotecas

- **Jakarta EE 9+:** Plataforma padrão para desenvolvimento corporativo em Java, oferecendo APIs robustas para web, persistência e segurança.
- **JSF (Jakarta Server Faces):** Framework padrão para construir UIs baseadas em componentes, simplificando o gerenciamento de estado e eventos da interface.
- **PrimeFaces 12.0.0:** Biblioteca de componentes UI para JSF. Foi escolhida para acelerar o desenvolvimento do front-end com componentes ricos e prontos para uso (tabelas, diálogos, etc.), garantindo uma melhor experiência do usuário (UX) com menos esforço.
- **Maven:** Ferramenta de automação de build e gerenciamento de dependências, essencial para manter a consistência do projeto.

---

## 2. Como Compilar e Executar o Projeto

### Pré-requisitos
- **Java 11** ou superior (JDK)
- **Maven 3.6** ou superior
- **Servidor de Aplicação Jakarta EE 9+** (Ex: Payara, WildFly, GlassFish)
- **PostgreSQL** (ou outro banco de dados configurado no `persistence.xml`)

### Passos para Execução

1.  **Clone o repositório:**
    ```bash
    git clone <url-do-repositorio>
    cd <pasta-do-projeto>
    ```

2.  **Configure o Banco de Dados:**
    - Crie um banco de dados no PostgreSQL.
    - Abra o arquivo `src/main/resources/META-INF/persistence.xml` e ajuste as propriedades de conexão com o banco (URL, usuário e senha).

3.  **Compile o projeto com Maven:**
    - No terminal, na raiz do projeto, execute o comando:
    ```bash
    mvn clean package
    ```
    - Isso irá gerar o arquivo `cadastro-pessoa.war` (ou `ROOT.war`, dependendo da configuração no `pom.xml`) na pasta `target/`.

4.  **Faça o Deploy no Servidor:**
    - Inicie seu servidor de aplicação (Ex: Payara).
    - Copie o arquivo `.war` gerado para a pasta de autodeploy do servidor (ex: `glassfish/domains/domain1/autodeploy/`).

5.  **Acesse a Aplicação:**
    - Abra o navegador e acesse a URL da aplicação. Por padrão:
    `http://localhost:8080/cadastro-pessoa/`

---

## 3. Como Executar os Testes

Atualmente, o projeto não possui uma suíte de testes automatizados implementada. Para executar testes, o processo é manual, seguindo os fluxos da aplicação.

---

## 4. Notas Adicionais

- **Segurança:** A segurança foi implementada de forma pragmática, utilizando um **Filtro de Servlet** para interceptar requisições e um **Bean de Sessão** para controlar o estado do usuário. Esta abordagem é simples, segura e fácil de auditar, evitando a complexidade do Jakarta Security para este escopo.
- **Usuários Hardcoded:** Para fins de demonstração, os usuários estão hardcoded no `LoginBean.java`:
  - **Admin:** `admin` / `admin`
  - **User:** `user` / `user`
- **Context Path:** O `pom.xml` foi configurado para gerar o artefato com o nome `cadastro-pessoa.war`, removendo o sufixo de versão da URL e tornando-a mais limpa.

### Pré-requisitos

- Java 11 ou superior
- PostgreSQL 12 ou superior
- Maven 3.6 ou superior
- Servidor de aplicação compatível com Java EE 8 (WildFly, GlassFish, etc.)

### Configuração do Banco de Dados

1. Crie um banco de dados PostgreSQL chamado `cadastro`
2. Configure o usuário e senha no arquivo `persistence.xml`

### Execução

1. Clone o repositório
2. Configure o banco de dados conforme instruções acima
3. Execute o build do projeto com Maven:
   ```
   mvn clean package
   ```
4. Implante o arquivo WAR gerado no servidor de aplicação
5. Acesse a aplicação em: `http://localhost:8080/teste-sinerji/`

## Boas Práticas Implementadas

- **Arquitetura em Camadas:** Separação clara de responsabilidades
- **Validação em Múltiplas Camadas:** Validação nos DTOs, entidades e serviços
- **Tratamento de Exceções:** Exceções customizadas para diferentes cenários
- **Padrão DTO:** Separação entre modelo de domínio e objetos de transferência
- **Injeção de Dependência:** Uso de EJB e CDI para gerenciamento de dependências
- **Transações:** Controle transacional nos serviços EJB
- **Encapsulamento:** Proteção do estado interno das entidades
- **Código Limpo:** Nomes significativos, métodos pequenos e coesos
- **Documentação:** Javadoc em classes e métodos importantes

## Melhorias Futuras

- Implementação de testes unitários e de integração
- Implementação de autenticação e autorização
- Containerização com Docker
- Implementação de API REST
- Melhorias de UX/UI
- Implementação de logs e monitoramento
