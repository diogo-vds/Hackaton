# MVP -- Controle de Vacinação Infantil SUS

## 1. Visão geral

Este projeto apresenta um MVP de um sistema de controle de vacinação
infantil, desenvolvido como parte de um projeto acadêmico de
pós-graduação.

O objetivo é controlar o calendário vacinal, identificar vacinas
pendentes e utilizar uma arquitetura orientada a eventos para processar
aplicações de vacinas e notificações.

------------------------------------------------------------------------

## 2. Arquitetura

A solução combina serviços Java, AWS Lambda, Amazon SQS, Amazon RDS
PostgreSQL, EventBridge Scheduler, Amazon ECR, CloudWatch e IAM.

### Fluxo principal de pendências

``` text
EventBridge Scheduler
        |
        v
Lambda Scheduler Java
        |
        v
Processador de Pendências Vacinais
        |
        +----------------------+
        |                      |
        v                      v
PostgreSQL RDS          Motor de Vacinação
                               |
                               v
                       SQS_Notificacao
                               |
                               v
                    Lambda de Notificação
```

### Fluxo de aplicações de vacina

``` text
Fonte / Integração SUS
        |
        v
SQS_Aplicacao_Vacina
        |
        v
Lambda salva-aplicacao-vacina
        |
        v
PostgreSQL RDS
```

### Imagem da arquitetura

> **\[INSERIR IMAGEM -- DIAGRAMA DA ARQUITETURA\]**

Exemplo:

``` markdown
![Arquitetura da solução](docs/images/arquitetura.png)
```

------------------------------------------------------------------------

## 3. Componentes

### Lambda Scheduler

A função `mvp-vacinacao-scheduler` é desenvolvida em Java 21 e Spring
Boot. Ela é acionada pelo EventBridge Scheduler e inicia o processamento
das pendências vacinais.

O contexto Spring é inicializado uma vez por ambiente de execução e
reutilizado nas invocações subsequentes.

**\[INSERIR IMAGEM -- LAMBDA SCHEDULER\]**

### Motor de vacinação

O motor avalia cada pessoa em relação aos esquemas do calendário
vigente.

São consideradas:

-   idade mínima;
-   idade recomendada;
-   idade máxima;
-   número da dose;
-   histórico de aplicações;
-   intervalo mínimo entre doses;
-   calendário vigente.

**\[INSERIR IMAGEM -- MOTOR / FLUXO DE PROCESSAMENTO\]**

### SQS_Aplicacao_Vacina

Recebe mensagens contendo os dados necessários para registrar uma
aplicação.

Exemplo:

``` json
{
  "numeroSus": 100000000001,
  "esquemaVacinacaoId": 3,
  "dataAplicacao": "2026-09-29T23:40:00",
  "lote": "LOTE-TESTE-001",
  "agenteSaudeId": 10,
  "observacao": "Aplicacao via integracao SUS",
  "unidadeAtendimentoId": 1
}
```

### Lambda salva-aplicacao-vacina

A função `lambda-salva-aplicacao-vacina` é desenvolvida em Python 3.14.

Ela:

1.  recebe o evento do SQS;
2.  valida a mensagem;
3.  extrai os dados da aplicação;
4.  conecta ao PostgreSQL;
5.  registra a aplicação na tabela `aplicacao_vacina`.

A função utiliza `psycopg2` para acesso ao PostgreSQL.

**\[INSERIR IMAGEM -- LAMBDA DE PERSISTÊNCIA\]**

### SQS_Notificacao

Recebe uma mensagem por pessoa contendo todas as vacinas pendentes.

Exemplo:

``` json
{
  "numeroSus": 100000000001,
  "vacinasPendentes": [
    {
      "esquemaVacinacaoId": 3,
      "vacinaId": 3,
      "vacina": "Pentavalente",
      "numeroDose": 2,
      "tipoDose": "ROTINA"
    }
  ]
}
```

**\[INSERIR IMAGEM -- SQS NOTIFICAÇÃO\]**

------------------------------------------------------------------------

## 4. Processamento em paralelo

Para evitar N+1 queries, as pessoas são processadas em páginas e blocos.

A estratégia utilizada é:

-   página de até 1.000 pessoas;
-   divisão em blocos de 250;
-   até 4 tarefas executadas em paralelo;
-   uma consulta de histórico por bloco;
-   agrupamento do histórico por `numero_sus`;
-   cálculo das pendências pelo motor;
-   publicação de uma mensagem por pessoa com pendências.

``` text
1.000 pessoas
      |
      +--> 250 --> 1 query
      +--> 250 --> 1 query
      +--> 250 --> 1 query
      +--> 250 --> 1 query
```

Essa estratégia reduz a quantidade de acessos ao banco em comparação com
uma consulta individual para cada pessoa.

------------------------------------------------------------------------

## 5. Banco de dados

A persistência utiliza Amazon RDS for PostgreSQL.

Principais tabelas:

``` text
pessoa
   |
   v
aplicacao_vacina
   |
   v
esquema_vacinacao
   |             |
   v             v
vacina     calendario_vacinal
```

### `pessoa`

-   `numero_sus`
-   `consentimento`
-   `data_nascimento`

### `vacina`

-   `id`
-   `nome`
-   `sigla`
-   `fabricante`

### `calendario_vacinal`

-   `id`
-   `nome`
-   `versao`
-   `data_inicio_vigencia`
-   `data_fim_vigencia`

### `esquema_vacinacao`

Define as regras de cada dose, incluindo idade mínima, idade
recomendada, idade máxima e intervalo mínimo.

### `aplicacao_vacina`

Registra:

-   `numero_sus`
-   `esquema_vacinacao_id`
-   `data_aplicacao`
-   `lote`
-   `agente_saude_id`
-   `observacao`
-   `unidade_atendimento_id`

**\[INSERIR IMAGEM -- MER / BANCO DE DADOS\]**

------------------------------------------------------------------------

## 6. Tecnologias

  Tecnologia              Utilização
  ----------------------- ---------------------------------
  Java 21                 Serviços e motor de vacinação
  Spring Boot             Framework principal
  Spring Data JPA         Persistência
  Hibernate               ORM
  Maven                   Build e dependências
  Python 3.14             Lambda de persistência
  psycopg2                Acesso Python ao PostgreSQL
  PostgreSQL              Banco de dados
  Docker                  Empacotamento da aplicação Java
  AWS Lambda              Computação serverless
  Amazon SQS              Mensageria
  EventBridge Scheduler   Agendamento
  Amazon RDS              PostgreSQL gerenciado
  Amazon ECR              Registro de imagem Docker
  CloudWatch              Logs e métricas
  IAM                     Permissões AWS

------------------------------------------------------------------------

## 7. Infraestrutura AWS

Região utilizada:

``` text
us-east-1
Estados Unidos (Norte da Virgínia)
```

Recursos principais:

``` text
AWS
|
+-- Lambda
|   +-- mvp-vacinacao-scheduler
|   +-- lambda-salva-aplicacao-vacina
|
+-- SQS
|   +-- SQS_Aplicacao_Vacina
|   +-- SQS_Notificacao
|
+-- EventBridge Scheduler
|   +-- scheduler-vacinacao-diario
|
+-- RDS
|   +-- mvp-vacinacao-db
|
+-- ECR
|   +-- mvp-vacinacao-lambda
|
+-- IAM
|
+-- CloudWatch
```

**\[INSERIR IMAGEM -- INFRAESTRUTURA AWS\]**

------------------------------------------------------------------------

## 8. EventBridge Scheduler

O Scheduler aciona periodicamente a Lambda responsável pelo
processamento das pendências.

``` text
scheduler-vacinacao-diario
        |
        v
mvp-vacinacao-scheduler
```

A role do Scheduler possui permissão para `lambda:InvokeFunction`.

**\[INSERIR IMAGEM -- EVENTBRIDGE SCHEDULER\]**

------------------------------------------------------------------------

## 9. IAM e segurança

O acesso aos recursos AWS é controlado através de IAM.

Exemplos de permissões utilizadas:

### Lambda Scheduler

``` text
sqs:SendMessage
```

### Lambda de persistência

``` text
sqs:ReceiveMessage
sqs:DeleteMessage
sqs:GetQueueAttributes
```

### EventBridge Scheduler

``` text
lambda:InvokeFunction
```

As credenciais, senhas e tokens não devem ser armazenados neste README.

------------------------------------------------------------------------

## 10. Monitoramento

O Amazon CloudWatch é utilizado para acompanhar:

-   invocações;
-   duração;
-   erros;
-   consumo de memória;
-   logs das funções.

As filas SQS também possuem métricas próprias para acompanhamento do
processamento.

**\[INSERIR IMAGEM -- CLOUDWATCH LAMBDA\]**

**\[INSERIR IMAGEM -- CLOUDWATCH SQS\]**

**\[INSERIR IMAGEM -- LOGS DA LAMBDA\]**

------------------------------------------------------------------------

## 11. Docker e ECR

A Lambda Java é empacotada como imagem Docker.

Fluxo:

``` text
Código Java
    |
    v
Maven
    |
    v
Docker
    |
    v
Amazon ECR
    |
    v
AWS Lambda
```

A imagem é construída para `linux/amd64`.

Exemplo:

``` bash
docker buildx build   --platform linux/amd64   --provenance=false   -t mvp-vacinacao-lambda .
```

**\[INSERIR IMAGEM -- ECR\]**

**\[INSERIR IMAGEM -- LAMBDA COM IMAGEM\]**

------------------------------------------------------------------------

## 12. Tratamento de falhas

O processamento dos eventos SQS utiliza falha parcial de lote.

Quando uma mensagem é processada corretamente:

``` text
SQS -> Lambda -> processamento OK -> mensagem removida
```

Quando ocorre uma falha:

``` text
SQS -> Lambda -> erro -> item retornado para reprocessamento
```

Isso evita que uma falha em uma mensagem impeça o processamento das
demais mensagens do lote.

------------------------------------------------------------------------

## 13. Evidências de funcionamento

### Aplicação de vacina

Foi realizado um teste completo:

``` text
SQS_Aplicacao_Vacina
        |
        v
lambda-salva-aplicacao-vacina
        |
        v
PostgreSQL
```

A mensagem foi consumida pela Lambda e uma nova aplicação foi registrada
na tabela `aplicacao_vacina`.

**\[INSERIR IMAGEM -- MENSAGEM NA SQS\]**

**\[INSERIR IMAGEM -- EXECUÇÃO DA LAMBDA\]**

**\[INSERIR IMAGEM -- REGISTRO NO BANCO\]**

### Pendências vacinais

O motor também foi validado com cenários de:

-   dose ainda não elegível;
-   dose pendente;
-   dose já aplicada;
-   segunda dose aguardando intervalo;
-   segunda dose após o intervalo;
-   idade máxima ultrapassada;
-   múltiplas vacinas pendentes.

**\[INSERIR IMAGEM -- MENSAGEM SQS_NOTIFICACAO\]**

------------------------------------------------------------------------

## 14. Testes automatizados

Os principais componentes Java possuem testes unitários.

São cobertos:

-   Motor de vacinação;
-   regras de idade;
-   semanas, dias, meses e anos;
-   limites mínimo e máximo;
-   intervalo entre doses;
-   múltiplas vacinas;
-   Processador de Pendências;
-   processamento em blocos;
-   Producer da SQS.

O processamento de 1.000 pessoas foi testado considerando a divisão em
blocos e o acesso ao histórico por bloco.

------------------------------------------------------------------------

## 15. Estrutura do projeto Java

``` text
src/main/java/com/fiap/hackathon/mvp/

+-- dto
+-- enums
+-- mapper
+-- persistence
+-- producer
+-- service
|   +-- impl
|   +-- CalculadoraIdadeService
|   +-- MotorVacinacaoService
|   +-- ProcessadorPendenciasVacinaisService
|   +-- RegraIdadeService
+-- lambda
|   +-- SchedulerHandler
+-- config
|   +-- AwsConfig
+-- MvpApplication
```

------------------------------------------------------------------------

## 16. Fluxo completo da solução

### Fluxo 1 -- aplicação de vacina

``` text
Integração SUS
      |
      v
SQS_Aplicacao_Vacina
      |
      v
Lambda Python
      |
      v
PostgreSQL
```

### Fluxo 2 -- identificação de pendências

``` text
EventBridge
      |
      v
Lambda Java
      |
      v
Processador
      |
      v
Motor de Vacinação
      |
      v
SQS_Notificacao
      |
      v
Lambda de Notificação
```

------------------------------------------------------------------------

## 17. Benefícios da arquitetura

### Desacoplamento

SQS desacopla produtores e consumidores.

### Processamento assíncrono

As aplicações de vacina e notificações podem ser processadas sem
bloquear o sistema de origem.

### Escalabilidade

Lambda e SQS permitem escalar o processamento de acordo com a demanda.

### Serverless

A infraestrutura de execução das funções é gerenciada pela AWS.

### Eficiência

O processamento em páginas e blocos reduz consultas repetitivas ao
banco.

### Separação de responsabilidades

Cada componente possui uma responsabilidade específica:

``` text
Scheduler
  -> inicia processamento

Motor
  -> calcula pendências

SQS
  -> desacopla componentes

Lambda de aplicação
  -> persiste aplicações

RDS
  -> armazena dados

Lambda de notificação
  -> processa notificações
```

------------------------------------------------------------------------

## 18. Recursos AWS

  Recurso     Nome
  ----------- ---------------------------------
  Região      `us-east-1`
  RDS         `mvp-vacinacao-db`
  SQS         `SQS_Aplicacao_Vacina`
  SQS         `SQS_Notificacao`
  Lambda      `mvp-vacinacao-scheduler`
  Lambda      `lambda-salva-aplicacao-vacina`
  Scheduler   `scheduler-vacinacao-diario`
  ECR         `mvp-vacinacao-lambda`

------------------------------------------------------------------------

## 19. Galeria de evidências

### Arquitetura

**\[INSERIR IMAGEM\]**

### Banco de dados

**\[INSERIR IMAGEM\]**

### Lambda Scheduler

**\[INSERIR IMAGEM\]**

### Lambda de persistência

**\[INSERIR IMAGEM\]**

### SQS de aplicação

**\[INSERIR IMAGEM\]**

### SQS de notificação

**\[INSERIR IMAGEM\]**

### EventBridge Scheduler

**\[INSERIR IMAGEM\]**

### ECR

**\[INSERIR IMAGEM\]**

### CloudWatch

**\[INSERIR IMAGEM\]**

### Evidência final no banco

**\[INSERIR IMAGEM\]**

------------------------------------------------------------------------

## 20. Conclusão

O MVP demonstra uma arquitetura distribuída e orientada a eventos para
controle de vacinação infantil, combinando Java, Spring Boot, Python e
serviços gerenciados da AWS.

A solução utiliza processamento assíncrono, filas, funções serverless,
banco PostgreSQL e agendamento automático, permitindo que os componentes
sejam evoluídos e escalados de forma independente.
