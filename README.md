# Cadastro de Pessoas - Desafio Sinerji

## Sumário

- [Descrição](#descrição)
- [Arquitetura](#arquitetura)
- [Pré-requisitos](#pré-requisitos)
- [Como Executar](#como-executar)
- [Como Executar os Testes](#como-executar-os-testes)
- [Boas Práticas Implementadas](#boas-práticas-implementadas)

Este projeto é uma aplicação web para cadastro e gerenciamento de pessoas, desenvolvida com a plataforma Jakarta. O sistema inclui funcionalidades de CRUD, controle de acesso baseado em papéis (`ADMIN` e `USER`) e exportação de dados, integração com api de cep para busca de endereço e testes unitários e de integração.

---

## 1. Decisões Técnicas e Arquiteturais

A arquitetura foi pensada para ser simples, robusta e fácil de manter, seguindo boas práticas do ecossistema Jakarta.

- **Camadas bem definidas:** Separação clara entre apresentação, controle, persistência e segurança.
- **MVC com JSF:** Interface, lógica e dados separados de forma organizada.
- **CDI:** Injeção de dependências para facilitar o gerenciamento dos componentes e do ciclo de vida dos beans.
- **JPA com Hibernate:** Persistência de dados feita de forma simples e orientada a objetos, sem SQL direto no código.

### Justificativa de Frameworks e Bibliotecas

- **Jakarta EE 9+:** Base para toda a aplicação, fornece as APIs essenciais para desenvolvimento web, persistência, segurança e injeção de dependências.
- **JSF (Jakarta Server Faces):** Usado para criar as páginas web de forma estruturada, facilitando a ligação entre tela e backend.
- **PrimeFaces 12:** Amplia o JSF com componentes visuais prontos (tabelas, filtros, diálogos, exportação de dados, máscaras de campo), acelerando o desenvolvimento e melhorando a experiência do usuário.
- **Maven:** Gerencia as dependências, plugins e o ciclo de build, garantindo reprodutibilidade e facilidade de atualização do projeto.
- **Hibernate (JPA):** Responsável pelo mapeamento objeto-relacional, simplificando o acesso e manipulação dos dados no banco.
- **H2:** Banco em memória utilizado nos testes de integração, permitindo validação isolada da lógica de persistência.
- **JUnit 5:** Base dos testes automatizados, garantindo qualidade e segurança nas evoluções do sistema.
- **Mockito:** Permite criar cenários de teste simulando comportamentos e dependências, essencial para testes unitários robustos.

Essas escolhas garantem um ambiente moderno, produtivo, com foco em qualidade, facilidade de manutenção e pronta para evolução.

---

## 2. Como Compilar e Executar o Projeto

### Pré-requisitos
- **Java 11** ou superior (JDK)
- **Maven 3.6** ou superior
- **Servidor de Aplicação Jakarta EE 9+** (WildFly)
- **PostgreSQL** (ou outro banco de dados configurado no `persistence.xml`)

### Passos para Execução

1.  **Clone o repositório:**
    ```bash
    git clone git@github.com:Peixoto16/cadastro-jsf.git
    cd cadastro-jsf
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
     - Inicie seu servidor de aplicação (WildFly) normalmente.

5.  **Acesse a Aplicação:**
    - Abra o navegador e acesse a URL da aplicação. Por padrão:
    `http://localhost:8080/cadastro-pessoa/login.xhtml`

---

## 3. Como Executar os Testes

O projeto possui uma suíte completa de testes unitários e de integração implementada com JUnit 5, Mockito e H2 (banco em memória).

### Executando Todos os Testes

```bash
mvn clean test
```

### Executando Apenas Testes Unitários (excluindo testes de integração)

```bash
mvn -Dtest='!*IT' test
```

### Executando Apenas Testes de Integração

```bash
mvn -Dtest='*IT' test
```

### Executando um Teste Específico

```bash
mvn -Dtest=NomeDoTeste test
```

Exemplo: `mvn -Dtest=PessoaServiceTest test`

### Cobertura de Testes

Os testes cobrem os principais componentes da aplicação:

- **Testes Unitários**: Validam o comportamento isolado de classes como `PessoaService`, `LoginBean` e `AuthFilter`.
- **Testes de Integração**: Validam a integração entre componentes, especialmente com o banco de dados H2 em memória.

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
- Servidor de aplicação compatível com Java EE 8 (WildFly)

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
5. Acesse a aplicação em: `http://localhost:8080/cadastro-pessoa/login.xhtml`

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
- **Testes Automatizados:**
    - Testes unitários com JUnit 5 e Mockito para lógica de negócio, beans e filtros.
    - Testes de integração com banco H2 em memória simulando cenários reais.
    - Cobertura de casos de sucesso, exceções e validações.
    - Isolamento de dependências com mocks e injeção manual em filtros.


