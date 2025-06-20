# Sistema de Cadastro de Pessoas

Sistema web Java EE para cadastro de pessoas e seus endereços, desenvolvido como demonstração de conhecimentos em desenvolvimento Java EE com arquitetura em camadas.

## Tecnologias Utilizadas

- **Backend:**
  - Java EE 8
  - EJB 3.2 para injeção de dependência e serviços
  - JPA 2.2 com Hibernate para persistência
  - Bean Validation para validação de dados

- **Frontend:**
  - JSF 2.3 com Facelets
  - PrimeFaces 12.0.0 para componentes UI
  - HTML5, CSS3

- **Banco de Dados:**
  - PostgreSQL

- **Ferramentas:**
  - Maven para gerenciamento de dependências e build
  - Lombok para redução de boilerplate

## Arquitetura

O sistema foi desenvolvido seguindo uma arquitetura em camadas bem definidas:

### 1. Domain Layer (Camada de Domínio)
- **Entidades:** Classes que representam o modelo de domínio (Pessoa, Endereco)
- **Enums:** Valores enumerados (Sexo, Estado)
- **Value Objects:** Objetos imutáveis que representam conceitos do domínio

### 2. Application Layer (Camada de Aplicação)
- **Serviços:** Implementam a lógica de negócio (PessoaService, EnderecoService)
- **DTOs:** Objetos de transferência de dados (PessoaDTO, EnderecoDTO)
- **Mappers:** Convertem entre entidades e DTOs (PessoaMapper, EnderecoMapper)

### 3. Infrastructure Layer (Camada de Infraestrutura)
- **Repositórios:** Acesso a dados via JPA (PessoaRepository, EnderecoRepository)
- **Configurações:** Configurações de persistência e integração

### 4. Presentation Layer (Camada de Apresentação)
- **Controllers:** Managed Beans JSF (PessoaController)
- **Views:** Páginas XHTML com componentes PrimeFaces
- **Conversores:** Conversores JSF para tipos específicos

### 5. Shared Layer (Camada Compartilhada)
- **Exceções:** Exceções customizadas (BusinessException, EntityNotFoundException)
- **Utilitários:** Classes utilitárias compartilhadas

## Funcionalidades

- Cadastro, edição, listagem e exclusão de pessoas
- Cadastro, edição, listagem e exclusão de endereços
- Validação de dados em múltiplas camadas
- Interface responsiva com PrimeFaces
- Tratamento de exceções customizado

## Estrutura do Projeto

```
src/
├── main/
│   ├── java/
│   │   └── com/teste/sinerji/
│   │       ├── application/
│   │       │   ├── dto/
│   │       │   ├── mapper/
│   │       │   └── service/
│   │       ├── domain/
│   │       │   ├── entity/
│   │       │   └── enums/
│   │       ├── infrastructure/
│   │       │   └── repository/
│   │       ├── presentation/
│   │       │   ├── controller/
│   │       │   └── converter/
│   │       └── shared/
│   │           └── exception/
│   ├── resources/
│   │   └── META-INF/
│   │       └── persistence.xml
│   └── webapp/
│       ├── WEB-INF/
│       │   ├── beans.xml
│       │   ├── faces-config.xml
│       │   ├── web.xml
│       │   └── templates/
│       ├── resources/
│       │   ├── css/
│       │   └── images/
│       ├── pages/
│       │   └── pessoa/
│       └── index.xhtml
└── test/
    └── java/
```

## Configuração e Execução

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
