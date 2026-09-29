# Arquitetura do Sistema de Controle de Vacinação

## 1. Visão geral

O sistema será desenvolvido com foco em **Java, arquitetura serverless e
processamento assíncrono**, utilizando serviços da AWS.

A solução será composta por:

-   Microserviço Java para exposição da API REST;
-   AWS API Gateway;
-   AWS SQS para processamento assíncrono;
-   AWS Lambda em Java para processamento agendado;
-   AWS Lambda em Python para processamento das filas;
-   EventBridge Scheduler para execução periódica;
-   PostgreSQL para persistência dos dados;
-   Motor de Vacinação responsável pelas regras de negócio do calendário
    vacinal.

O sistema não terá frontend no escopo do MVP.

------------------------------------------------------------------------

# 2. API Java de Vacinação

A aplicação Java disponibilizará dois endpoints principais.

## 2.1 POST --- Registro da aplicação de vacina

O endpoint recebe as informações referentes à aplicação de uma vacina.

Responsabilidades:

1.  Receber os dados;
2.  Validar as informações recebidas;
3.  Publicar uma mensagem na fila `SQS_Aplicacao_Vacina`.

O endpoint não realizará diretamente a persistência da aplicação no
banco de dados.

### Fluxo

``` text
POST /aplicacoes-vacina
        ↓
Microserviço Java
        ↓
Validação dos dados
        ↓
SQS_Aplicacao_Vacina
```

------------------------------------------------------------------------

## 2.2 GET --- Consulta da situação vacinal

O endpoint recebe o identificador da pessoa e retorna as informações
relacionadas à sua situação vacinal.

O processamento utilizará o **Motor de Vacinação**, que avaliará o
histórico de vacinação em relação ao calendário vigente.

### Fluxo

``` text
GET /pessoas/{id}/situacao-vacinal
        ↓
Microserviço Java
        ↓
Motor de Vacinação
        ↓
Banco de Dados
        ↓
JSON com a situação vacinal
```

------------------------------------------------------------------------

# 3. Persistência da aplicação da vacina

A fila `SQS_Aplicacao_Vacina` será consumida por uma AWS Lambda
desenvolvida em Python.

A função será denominada:

`SALVA_APLICACAO_VACINA`

Sua responsabilidade será:

1.  Consumir mensagens da fila;
2.  Interpretar os dados da aplicação da vacina;
3.  Persistir os dados no PostgreSQL;
4.  Registrar a aplicação na tabela `Aplicacao_Vacina`.

### Fluxo

``` text
SQS_Aplicacao_Vacina
        ↓
Lambda Python
SALVA_APLICACAO_VACINA
        ↓
PostgreSQL
        ↓
Tabela Aplicacao_Vacina
```

Dessa forma, a API Java realiza a validação e o envio da mensagem,
enquanto a persistência ocorre de maneira assíncrona.

------------------------------------------------------------------------

# 4. Motor de Vacinação

O **Motor de Vacinação** será o principal componente responsável pelas
regras de negócio do sistema.

Ele será utilizado tanto pelo endpoint GET quanto pelo processamento
diário executado pela Lambda Java.

Entre suas responsabilidades estão:

-   Calcular a idade da criança;
-   Consultar o calendário vacinal vigente;
-   Consultar os esquemas de vacinação;
-   Consultar o histórico de aplicações;
-   Comparar o histórico com as regras do calendário;
-   Identificar vacinas já realizadas;
-   Identificar vacinas previstas;
-   Identificar vacinas pendentes;
-   Identificar vacinas atrasadas.

Exemplo de resultado:

``` json
[
  {
    "vacina": "Penta",
    "dose": 1,
    "situacao": "REALIZADA"
  },
  {
    "vacina": "Penta",
    "dose": 2,
    "situacao": "PENDENTE"
  },
  {
    "vacina": "Penta",
    "dose": 3,
    "situacao": "PREVISTA"
  }
]
```

O motor será um componente de domínio reutilizado pelos diferentes
pontos de entrada do sistema.

------------------------------------------------------------------------

# 5. Verificação diária de vacinas pendentes

O sistema terá um processamento periódico responsável por verificar se
existem crianças com vacinas pendentes.

Para isso será utilizado o **EventBridge Scheduler**, que acionará uma
**AWS Lambda desenvolvida em Java**.

A Lambda Java utilizará diretamente o Motor de Vacinação, sem realizar
uma chamada HTTP para a API.

### Fluxo

``` text
EventBridge Scheduler
        ↓
Lambda Java
        ↓
Motor de Vacinação
        ↓
Calendário + Histórico
        ↓
Identificação de pendências
        ↓
SQS_Notificacao
```

A Lambda Java será responsável por executar o processamento e publicar
as pendências encontradas na fila de notificações.

------------------------------------------------------------------------

# 6. Envio de notificações

A fila `SQS_Notificacao` será consumida por uma segunda Lambda
desenvolvida em Python.

A função será denominada:

`NOTIFICACAO_VACINA_PENDENTE`

Sua responsabilidade será:

1.  Consumir as mensagens da fila;
2.  Interpretar os dados da vacinação pendente;
3.  Realizar o envio da notificação.

### Fluxo

``` text
SQS_Notificacao
        ↓
Lambda Python
NOTIFICACAO_VACINA_PENDENTE
        ↓
Notificação
```

O mecanismo específico de envio da notificação poderá ser definido
posteriormente.

------------------------------------------------------------------------

# 7. Arquitetura completa

``` text
                         ┌──────────────────────┐
                         │      API Gateway     │
                         └──────────┬───────────┘
                                    │
                                    ▼
                         ┌──────────────────────┐
                         │   Microserviço Java  │
                         │                      │
                         │ POST /aplicacoes     │
                         │ GET /situacao        │
                         └───────┬───────┬──────┘
                                 │       │
                     POST        │       │ GET
                                 │       │
                                 ▼       ▼
                    SQS_Aplicacao     Motor de
                       _Vacina        Vacinação
                         │               │
                         ▼               │
                  Lambda Python          │
               SALVA_APLICACAO_VACINA   │
                         │               │
                         ▼               ▼
                    PostgreSQL       PostgreSQL


                 EventBridge Scheduler
                         │
                         ▼
                    Lambda Java
                         │
                         ▼
                 Motor de Vacinação
                         │
                         ▼
               Identifica pendências
                         │
                         ▼
                  SQS_Notificacao
                         │
                         ▼
                  Lambda Python
             NOTIFICACAO_VACINA_PENDENTE
                         │
                         ▼
                    Notificação
```

------------------------------------------------------------------------

# 8. Reutilização do Motor de Vacinação

O Motor de Vacinação será compartilhado entre a API Java e a Lambda
Java.

``` text
                 ┌───────────────────────┐
                 │   Motor de Vacinação  │
                 │                       │
                 │ Regras do calendário  │
                 │ Idade                 │
                 │ Histórico             │
                 │ Doses                 │
                 │ Pendências            │
                 └───────────┬───────────┘
                             │
                 ┌───────────┴───────────┐
                 ▼                       ▼
          Microserviço Java         Lambda Java
                 │                       │
              GET API              Scheduler diário
```

Essa abordagem evita duplicação das regras de negócio.

O endpoint GET utiliza o motor para responder a uma consulta individual,
enquanto a Lambda Java utiliza o mesmo motor para executar a verificação
periódica de todas as crianças.

A Lambda Java não precisa chamar um endpoint HTTP da API para utilizar o
motor. Ambos utilizam diretamente o mesmo componente de negócio.

------------------------------------------------------------------------

# 9. Fluxos principais

## 9.1 Aplicação de vacina

``` text
Cliente/Consumidor
        ↓
API Gateway
        ↓
Microserviço Java
        ↓
Validação
        ↓
SQS_Aplicacao_Vacina
        ↓
Lambda Python
SALVA_APLICACAO_VACINA
        ↓
PostgreSQL
```

## 9.2 Consulta da situação vacinal

``` text
Cliente/Consumidor
        ↓
API Gateway
        ↓
Microserviço Java
        ↓
Motor de Vacinação
        ↓
PostgreSQL
        ↓
JSON
```

## 9.3 Verificação diária

``` text
EventBridge Scheduler
        ↓
Lambda Java
        ↓
Motor de Vacinação
        ↓
PostgreSQL
        ↓
Identificação de pendências
        ↓
SQS_Notificacao
        ↓
Lambda Python
NOTIFICACAO_VACINA_PENDENTE
        ↓
Notificação
```

------------------------------------------------------------------------

# 10. Componentes da solução

  -----------------------------------------------------------------------------
  Componente                    Tecnologia              Responsabilidade
  ----------------------------- ----------------------- -----------------------
  API                           Java                    Exposição dos endpoints
                                                        REST

  API Gateway                   AWS                     Entrada das requisições
                                                        HTTP

  SQS_Aplicacao_Vacina          AWS SQS                 Fila de aplicações de
                                                        vacina

  SALVA_APLICACAO_VACINA        Python / AWS Lambda     Persistência assíncrona
                                                        das aplicações

  Motor de Vacinação            Java                    Regras de negócio do
                                                        calendário

  Lambda de processamento       Java / AWS Lambda       Verificação diária das
                                                        vacinações

  EventBridge Scheduler         AWS                     Agendamento da execução
                                                        diária

  SQS_Notificacao               AWS SQS                 Fila de notificações

  NOTIFICACAO_VACINA_PENDENTE   Python / AWS Lambda     Processamento e envio
                                                        das notificações

  Banco de dados                PostgreSQL              Persistência dos dados
  -----------------------------------------------------------------------------

------------------------------------------------------------------------

# 11. Princípio arquitetural

A arquitetura separa o processamento síncrono do processamento
assíncrono:

-   **API Java:** recebe requisições e disponibiliza consultas;
-   **SQS:** desacopla a API dos processamentos assíncronos;
-   **Lambda Python:** executa tarefas específicas de consumo das filas;
-   **Lambda Java:** executa o processamento periódico;
-   **Motor de Vacinação:** centraliza as regras de negócio;
-   **PostgreSQL:** mantém o histórico e os dados necessários para as
    avaliações.

O principal objetivo do desenho é permitir que o sistema **avalie
automaticamente a situação vacinal das crianças**, identificando
pendências com base no calendário e no histórico de aplicações, e
posteriormente gere as notificações correspondentes.
