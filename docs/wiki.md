# Rede Mais Social – Guia Visual e Fluxo de Uso

> 💡 **Referência principal:** Especificação de Requisitos de Software – UC002 Solicita Afiliação (versão 1.0).  
> Cada seção abaixo menciona explicitamente o passo do fluxo principal ou regras RN1–RN5.

---

## 1. Landing page / Porta de entrada

![Home](docs/images/home.png)

- **URL:** `/`
- **Objetivo:** apresenta a Rede Mais Social e contém os botões “Solicitar Afiliação” e “Cadastro”, alinhados à seção “Objetivo do Documento”.
- **Ligação com a especificação:** inicia o fluxo do UC002 (Passo 1) ao encaminhar o usuário para seleção PF/PJ a partir do botão “Solicitar Afiliação”.

---

## 2. Seleção PF x PJ

![Seleção de Tipo](docs/images/selecionar-tipo.png)

- **URL:** `/afiliacao/inicio`
- **Descrição:** modal pergunta se o solicitante é Pessoa Física ou Pessoa Jurídica antes de preencher e-mail + CPF/CNPJ.
- **Especificação:** corresponde ao **Passo 1 (Ações do Ator)** e prepara o sistema para o **Passo 2 (verificar duplicidade)**.

---

## 3. Identificação – Pessoa Física

![Identificação PF](docs/images/identificacao-pf.png)

- **URL:** `/afiliacao/iniciar` (após seleção PF)
- **Campos:** Nome, CPF (readonly), sexo, data de nascimento, nacionalidade, endereço residencial, profissão.
- **Especificação:** cobre o **Passo 3** (“Sistema exibe formulário de identificação”) e aplica RN3 (checagem de dados pessoais).

---

## 4. Identificação – Pessoa Jurídica

![Identificação PJ](docs/images/identificacao-pj.png)

- **URL:** `/afiliacao/iniciar` (após seleção PJ)
- **Campos:** CNPJ, razão social, endereço comercial e representante legal.
- **Especificação:** ancora o **Passo 3** para CNPJ e prepara o **Fluxo alternativo – Passo 5 (Candidato é CNPJ)**.

---

## 5. Certidões Obrigatórias (PJ)

![Certidões](docs/images/certidoes.png)

- **URL:** `/afiliacao/certidoes`
- **Função:** permite adicionar múltiplas certidões (tipo, número, emissão, validade, arquivo). Há botão “+ Adicionar certidão”.
- **Especificação:** cumpre RN4/RN5 e o fluxo alternativo que exige upload de certidões antes de prosseguir.

---

## 6. Perfil, Habilidades e Interesses

![Perfil](docs/images/perfil.png)

- **URL:** `/afiliacao/identificacao` (etapa “perfil”)
- **Campos:** habilidades, interesses (ambos separados por vírgula) e tipo de perfil (mentor, voluntário, parceiro).
- **Especificação:** Passos 6–8 (“Sistema exibe formulário de Perfil; valida e armazena”).

---

## 7. Termo de Compromisso

![Termo](docs/images/termo.png)

- **URL:** `/afiliacao/perfil` (submissão direciona para termo)
- **Ações:** aceitar ou rejeitar o termo vigente (v.1.0).
- **Especificação:** Passos 9–11 (armazenar aceite e mudar situação para “Aguardando Validação”).  
  Caso rejeite, a aplicação segue o fluxo alternativo que encerra o processo.

---

## 8. Validação de E-mail

![Validação de E-mail](docs/images/validar-email.png)

- **URL:** `/afiliacao/termo`
- **Conteúdo:** token temporário (gerado por `EmailValidacao`) e campo para colar o código recebido.
- **Especificação:** cumpre RN1/RN2 e os **Passos 12–16** (enviar link, validar token, atualizar status para “Aguardando aprovação”).

---

## 9. Status "Aguardando aprovação"

![Aguardando Aprovação](docs/images/aguardando-aprovacao.png)

- **URL:** `/afiliacao/validar-email?token=...` (redireciona após validação bem-sucedida)
- **Descrição:** confirma que o token foi aceito e exibe o status `AGUARDANDO_APROVACAO`, juntamente com os dados do candidato.
- **Especificação:** fecha o Passo 16 do fluxo (informar que o candidato está aguardando aprovação) e satisfaz RN1/RN2.

---

## 10. Login

![Login](docs/images/login.png)

- **URL:** `/login`
- **Fluxo:** autentica o usuário usando credenciais criadas anteriormente. Mensagens de sucesso/erro aparecem conforme `IndexController`.
- **Especificação:** embora não detalhado no UC002 original, esse passo reforça RN1 (só acessa quem validou e criou senha).

---

## 11. Cadastro de Senha

![Cadastro de Senha](docs/images/cadastro-senha.png)

- **URL:** `/cadastro-senha`
- **Função:** cria/atualiza credenciais. Após sucesso, redireciona para `/login`.
- **Especificação:** suporte operacional para RN1 (conta só liberada após cadastro + validação de e-mail).

---

## 12. Modal “Esqueci a senha”

![Recuperar Senha](docs/images/recuperar-senha.png)

- **Contexto:** modal dentro de `/login`.
- **Descrição:** permite simular o envio de link de redefinição (comportamento aderente ao requisito não funcional de usabilidade).

---

## 13. Fluxo Resumido

1. **Landing (`/`)** → Solicitar Afiliação.  
2. **Seleção PF/PJ (`/afiliacao/inicio`)** → Verifica duplicidade.  
3. **Identificação** (PF ou PJ) →  
   - PF segue direto para Perfil.  
   - PJ vai para Certidões antes do Perfil.  
4. **Perfil** → Termo → Envio de Token.  
5. **Aceite**  
   - Se aceitar: status `AGUARDANDO_VALIDACAO`, envia token e solicita validação.  
   - Se rejeitar: processo encerrado.  
6. **Validação de e-mail** → tela de status `AGUARDANDO_APROVACAO`.  
7. **Login / Cadastro de senha** controlam quem pode iniciar o fluxo novamente.

---


