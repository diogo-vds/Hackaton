# Lambda de consentimento

Função AWS Lambda em Python que consome mensagens do Amazon SQS e grava o
consentimento na tabela `pessoa` do PostgreSQL.

## Mensagem da fila

```json
{
  "numero_sus": "123456789012345",
  "consentimento": true
}
```

O processamento é idempotente: mensagens repetidas atualizam o consentimento
da pessoa identificada pelo número SUS.

## Segredo do banco

O segredo indicado por `DatabaseSecretArn` deve possuir:

```json
{
  "username": "usuario",
  "password": "senha"
}
```

Host, porta e nome do banco são parâmetros do template e não ficam no código.

## Build e teste

Requer AWS SAM CLI e Docker para compilar a dependência nativa do PostgreSQL:

```bash
sam build --use-container
sam local invoke ConsentimentoFunction -e events/sqs-consentimento.json
```

Testes unitários:

```bash
python -m pip install -r requirements-dev.txt
python -m pytest
```

## Deploy

```bash
sam deploy --guided
```

Informe sub-redes privadas com acesso ao banco e um security group que permita
saída para o PostgreSQL. O banco deve permitir entrada na porta configurada a
partir do security group da Lambda. Para acessar o Secrets Manager a partir de
sub-redes privadas, disponibilize NAT ou um VPC endpoint.

Mensagens que falharem três vezes serão encaminhadas para a DLQ. O handler usa
resposta parcial de lote para que apenas as mensagens com erro sejam repetidas.
