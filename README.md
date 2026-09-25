# Mapa da Maturidade Financeira

## Sobre o projeto

Plataforma web educacional voltada a maiores de 18 anos, com o objetivo de auxiliar no
desenvolvimento da educação financeira por meio de:

- Um **diagnóstico personalizado** baseado em questionário inicial
- Uma **trilha de aprendizagem adaptativa**, organizada em 3 camadas de conteúdo
- **Quizzes** de fixação ao final de cada módulo
- Um **simulador de decisões financeiras** com dinheiro fictício
- Um **diagnóstico gerado por Inteligência Artificial**

O sistema **não** realiza recomendações de investimento, não movimenta dinheiro real e
não integra com corretoras ou instituições financeiras reais.

## Tecnologias utilizadas

| Camada | Tecnologia |
|---|---|
| Frontend | React |
| Backend | Java 17 + Spring Boot 3 (Spring Web, Spring Data JPA, Spring Security) |
| Banco de dados | PostgreSQL |
| Autenticação | Spring Security com JWT |
| Versionamento | Git + GitHub |

## Estrutura do backend

```
src/main/java/br/edu/ucsal/mapa_maturidade_financeira/
 ├── config/    -- configurações de segurança e JWT
 ├── controller/   --- recebe as requisições HTTP
 ├── dto/         --- objetos de entrada/saída da API
 ├── model/       --- entidades (tabelas do banco)
 ├── repository/  --- acesso ao banco de dados
 └── service/     --- regras de negócio
```

## Como rodar o projeto localmente

### Pré-requisitos
- Java 17 (JDK)
- PostgreSQL instalado e rodando
- Git

### Passo a passo

1. Clone o repositório:
   ```bash
   git clone https://github.com/alicerayrasantos-ops/projeto-tcc-mapa-maturidade-financeira.git
   ```

2. Crie o banco de dados no PostgreSQL:
   ```sql
   CREATE DATABASE mapa_financeira;
   ```

3. Confira o arquivo `src/main/resources/application.properties` e ajuste a senha do
   seu PostgreSQL local, se for diferente:
   ```properties
   spring.datasource.password=SUA_SENHA_AQUI
   ```

4. Abra o projeto no VSCode (`File > Open Folder`) — selecione a pasta que contém o
   `pom.xml` na raiz.

5. Rode a aplicação: abra
   `MapaMaturidadeFinanceiraApplication.java` e clique em **Run** acima do `main`.

6. Se tudo der certo, o terminal mostra:
   ```
   Tomcat started on port 8080 (http) with context path '/'
   Started MapaMaturidadeFinanceiraApplication in X.XXX seconds
   ```

## Endpoints já implementados

| Método | Rota | Autenticação | Descrição |
|---|---|---|---|
| POST | `/api/usuarios/cadastro` | Livre | Cadastro de usuário (verifica maioridade) |
| POST | `/api/usuarios/login` | Livre | Login, retorna token JWT |
| POST | `/api/perfil/questionario` | **Exige token** | Responde questionário (alternativas A/B/C/D), calcula perfil |
| GET | `/api/modulos/trilha` | **Exige token** | Lista os módulos da trilha, filtrados pelo perfil do usuário |
| POST | `/api/modulos/concluir` | **Exige token** | Marca um módulo como concluído |
| GET | `/api/modulos/progresso` | **Exige token** | Retorna o percentual de módulos concluídos |
| GET | `/api/quiz/modulo/{moduloId}` | **Exige token** | Lista as perguntas de um módulo |
| POST | `/api/quiz/responder` | **Exige token** | Corrige uma resposta e salva no histórico do usuário |

Para chamar rotas protegidas, envie o header:
```
Authorization: Bearer SEU_TOKEN_AQUI
```

## Perfis de maturidade financeira

O questionário classifica o usuário em um dos três perfis abaixo, com base nas
respostas (regra de negócio em `PerfilOnboardingService`):

| Valor salvo (banco/código) | Nome exibido | Critério |
|---|---|---|
| `INICIANTE` | Iniciante | Sem experiência prática de investimento e com base financeira (orçamento/reserva/dívida) ainda fraca |
| `EM_DESENVOLVIMENTO` | Em desenvolvimento | Sem experiência prática, mas já com alguma base financeira consolidada |
| `INVESTIDOR` | Investidor | Já possui experiência prática com investimentos (respostas C ou D na pergunta de experiência) |

## Progresso do projeto

- [x] Estrutura inicial do backend (Spring Boot + Maven)
- [x] Conexão com PostgreSQL configurada e testada
- [x] RF01 — Cadastro de usuário com verificação de maioridade
- [x] RF02 — Login com autenticação JWT + proteção de rotas
- [x] RF03/RF04 — Questionário de perfil com cálculo automático (regras + perfis renomeados)
- [x] RF05/RF06/RF11 — Módulos de conteúdo, trilha por perfil e progresso do usuário (código pronto, popular dados de teste e rodar pendente)
- [x] RF07 — Quiz com correção, feedback e histórico de respostas por usuário (código pronto, testes pendentes)
- [ ] RF10 — Diagnóstico por IA (entidade e DTO prontos; integração com a API do Gemini ainda por implementar)
- [ ] RF08/RF09 — Simulador de decisões financeiras
- [ ] RF12 — Exclusão de conta
- [ ] Frontend (React)

## Fluxo de trabalho em equipe (Git)

```bash
git pull
# ... mexe no código ...
git add .
git commit -m "mensagem descrevendo o que foi feito"
git push
```

## Escopo — o que o sistema NÃO faz (decisão de projeto)

- Não recomenda produtos, ativos ou fundos de investimento específicos.
- Não movimenta dinheiro real em nenhuma hipótese.
- Não integra com corretoras ou instituições financeiras reais.
- Não aceita usuários menores de 18 anos.
- Não coleta dado financeiro real (CPF, número de conta, saldo bancário).
