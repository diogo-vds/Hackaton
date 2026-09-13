# ms-vacinas

Microserviço Spring Boot para o CRUD de vacinas, organizado em arquitetura hexagonal.

## Executar

Requer Java 25 e Maven 3.6.3+.

```bash
mvn spring-boot:run
```

Por padrão usa H2 em memória. Para PostgreSQL, configure `DB_URL`, `DB_USERNAME` e `DB_PASSWORD`.

## API

| Método | Endpoint | Operação |
|---|---|---|
| POST | `/vacinas` | Cria uma vacina |
| GET | `/vacinas` | Lista todas |
| GET | `/vacinas/{id}` | Busca por ID |
| PUT | `/vacinas/{id}` | Atualiza |
| DELETE | `/vacinas/{id}` | Exclui |

Exemplo de corpo para criação/atualização:

```json
{
  "nome": "BCG",
  "descricao": "Dose única ao nascer",
  "idadeAlvoEmMeses": 0
}
```

### Postman

A collection com exemplos de todas as operações está disponível em
[`docs/postman/Hackaton-Vacinas.postman_collection.json`](docs/postman/Hackaton-Vacinas.postman_collection.json).
Importe esse arquivo no Postman com a aplicação em execução na porta `8080`.

## Arquitetura

- `domain`: modelo e regras independentes de framework;
- `application`: portas de entrada/saída e casos de uso;
- `adapter/in`: API REST;
- `adapter/out`: persistência JPA;
- `config`: composição das dependências.
