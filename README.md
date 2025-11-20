# Rede Mais Social – Plataforma de Afiliação

Aplicação web construída com Spring Boot para digitalizar o processo de afiliação de voluntários e organizações descrito na **Especificação de Requisitos de Software – Rede Mais Social** (`Projeto_-_Especificacao_de_Requisitos_de_Software.pdf`). O objetivo é cobrir o fluxo principal do caso de uso **UC002 – Solicita Afiliação** e as regras de negócio RN1–RN5 apresentadas no documento oficial.

## Tecnologias Utilizadas

| Camada | Tecnologia | Propósito |
|--------|------------|-----------|
| Backend | **Java 21 + Spring Boot 3.5.7** | Framework principal; provê MVC, DI, validações e endpoints. |
| Persistência | **Spring Data JPA + MySQL** | ORM e banco relacional para candidatos, perfis, afiliações, termos e certidões. |
| Frontend | **Thymeleaf** | Renderização server-side das telas descritas na especificação (formulários PF/PJ, anexos, termo, etc.). |
| Build/Dev | **Maven**, `mvnw` wrapper | Gestão de dependências e empacotamento. |
| Utilidades | `pdftotext` (documentação), CSS customizado | Conversão/consulta da especificação e visual refinado das telas. |

## Arquitetura e Fluxo Funcional

A estrutura segue a divisão apresentada no diagrama de classes do enunciado (entidades `Candidato`, `Afiliacao`, `Perfil`, `TermoCompromisso`, `EmailValidacao`, `Certidao`) e os serviços correspondentes (`AfiliacaoService`, `ValidacaoService`, `EmailService`, `TermoService`, `CredencialService`). O controlador `AfiliacaoController` orquestra o fluxo descrito no PDF:

1. **Tela inicial / seleção PF x PJ** – `GET /afiliacao/inicio` implementa o Passo 1 do fluxo principal (Especificação, “Fluxo Principal”, passo 1). O modal permite escolher PF ou PJ antes de enviar o formulário.
2. **Validação de duplicidade** – `POST /afiliacao/iniciar` chama `AfiliacaoService.buscarPorEmailOuCpf`, cobrindo o Passo 2 e o fluxo alternativo “E-mail ou CPF encontrado”.
3. **Formulário de identificação** – `GET` das telas `identificacao-pf.html` e `identificacao-pj.html` exibem os campos exigidos (nome, sexo, data, nacionalidade, endereço, profissão ou dados PJ/representante) conforme o Passo 3 e o fluxo alternativo “Candidato é CNPJ” (anexo de certidões).
4. **Validações** – `ValidacaoService` aplica regras RN3–RN5: pessoas jurídicas precisam anexar certidões (`processarCertidoesPJ`); pessoas físicas passam por validações de dados pessoais.
5. **Perfil/Habilidades/Interesses** – `processarFormularioPerfil` cobre os Passos 6–8 do fluxo principal.
6. **Termo de Compromisso** – `TermoService.obterTermoVigente` e `processarAceiteTermo` implementam os Passos 9–11, armazenando o aceite e alterando o status para `AGUARDANDO_VALIDACAO`.
7. **Envio/validação de e-mail** – `EmailService` (token + mensagens) cumpre RN1/RN2 e os Passos 12–16, incluindo a tela `email-validacao.html` para o usuário confirmar o token.

As regras de negócio ficam assim mapeadas:

- **RN1 / RN2** – Token de validação gerado e controlado por `EmailValidacao` (expira e só libera a mudança de status após uso).
- **RN3** – Pessoas físicas não concluem o fluxo sem passar pelo formulário e validação de dados pessoais.
- **RN4 / RN5** – A rota `/afiliacao/certidoes` exige upload/registro das certidões obrigatórias para PJs/ONGs antes de avançar.

## Autenticação e Cadastro de Senha

Embora o escopo principal seja a afiliação, adicionamos um fluxo básico de credenciais para simular RN1 (contas só liberadas após validação):

- `CredencialService` armazena em memória os pares `email/senha` gerados em `/cadastro-senha`.
- Após preencher o formulário (`cadastro-senha.html`), o usuário é redirecionado ao login com mensagem de sucesso.
- `POST /login` valida as credenciais e libera a navegação para `/afiliacao/inicio`, garantindo que somente usuários autenticados iniciem o processo.

## Experiência de Uso (Telas)

As páginas em `src/main/resources/templates` reproduzem os frames descritos na especificação:

- `home.html` – landing page com chamada “Solicitar Afiliação” e links para login/cadastro.
- `afiliacao/inicio.html` – Passo 1 (PF/PJ, e-mail e CPF/CNPJ).
- `identificacao-pf.html` / `identificacao-pj.html` – formulários alinhados aos campos do Passo 3.
- `afiliacao/certidoes.html` – upload/listagem de certidões (RN4/RN5).
- `afiliacao/perfil.html`, `afiliacao/termo.html`, `afiliacao/email-validacao.html`, `afiliacao/processo-encerrado.html` – etapas subsequentes do fluxo.
- `login.html` e `cadastro-senha.html` – autenticação/cadastro, com feedback ao usuário.

Todo o layout foi customizado em CSS puro (gradientes, componentes responsivos) para oferecer a usabilidade esperada na seção “Usabilidade” do PDF.

## Estrutura de Código (resumo)

```
src/main/java/com/example/projeto_so/
├── controllers/
│   ├── IndexController      # Landing, login e cadastro de senha
│   └── AfiliacaoController  # Fluxo completo PF/PJ, certidões, perfil, termo, e-mail
├── entity/                  # Candidato, Afiliacao, Perfil, TermoCompromisso, EmailValidacao, Certidao
├── repository/              # Repositórios Spring Data JPA
├── service/                 # AfiliacaoService, ValidacaoService, EmailService, TermoService, CredencialService
└── ProjetoSoApplication.java
```

## Como Executar

```bash
# 1. Ajuste o banco em application.properties (MySQL local)
# 2. Rode as migrações automáticas com o Hibernate (ddl-auto=update já configurado)
./mvnw spring-boot:run

# ou para gerar o pacote:
./mvnw -DskipTests package
```

A aplicação sobe em `http://localhost:8080`. Use:
- `/cadastro-senha` → criar senha (credencial em memória)
- `/login` → autenticar
- `/afiliacao/inicio` → seguir o fluxo PF/PJ

## Referências

- [Especificação oficial do projeto](Projeto_-_Especificacao_de_Requisitos_de_Software.pdf) – descreve o UC002, fluxos alternativos e regras de negócio mapeadas na implementação.

## Próximos Passos Sugeridos

- Persistir credenciais em tabela dedicada e integrar a RN1/RN2 diretamente ao status do candidato.
- Automatizar anexos de certidões (upload real em storage).
- Cobrir RN3 (ausência de antecedentes) com integração externa.
- Implementar camada de aprovação interna (workflow pós “AGUARDANDO_APROVACAO”).

Com isso, o README passa a contextualizar tecnologias, arquitetura e como cada etapa do código atende ao que foi solicitado na especificação.

## Autores

- Ryan Ledo
- Derick Sant'ana