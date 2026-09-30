import json
import logging
import os
import re
from functools import lru_cache

import boto3
import psycopg

LOGGER = logging.getLogger()
LOGGER.setLevel(logging.INFO)

NUMERO_SUS_PATTERN = re.compile(r"^\d{15}$")
UPSERT_CONSENTIMENTO = """
    INSERT INTO pessoa (numero_sus, consentimento)
    VALUES (%s, %s)
    ON CONFLICT (numero_sus)
    DO UPDATE SET consentimento = EXCLUDED.consentimento
"""


class MensagemInvalidaError(ValueError):
    pass


@lru_cache(maxsize=1)
def obter_credenciais():
    secret_arn = os.environ["DB_SECRET_ARN"]
    response = boto3.client("secretsmanager").get_secret_value(SecretId=secret_arn)
    secret = json.loads(response["SecretString"])

    if not secret.get("username") or not secret.get("password"):
        raise RuntimeError("O segredo deve conter username e password")

    return secret["username"], secret["password"]


def abrir_conexao():
    username, password = obter_credenciais()
    return psycopg.connect(
        host=os.environ["DB_HOST"],
        port=int(os.environ.get("DB_PORT", "5432")),
        dbname=os.environ["DB_NAME"],
        user=username,
        password=password,
        connect_timeout=5,
    )


def validar_mensagem(body):
    try:
        payload = json.loads(body)
    except (TypeError, json.JSONDecodeError) as error:
        raise MensagemInvalidaError("A mensagem deve conter um JSON válido") from error

    numero_sus = str(payload.get("numero_sus", ""))
    consentimento = payload.get("consentimento")

    if not NUMERO_SUS_PATTERN.fullmatch(numero_sus):
        raise MensagemInvalidaError("numero_sus deve conter exatamente 15 dígitos")
    if not isinstance(consentimento, bool):
        raise MensagemInvalidaError("consentimento deve ser booleano")

    return int(numero_sus), consentimento


def processar_registro(connection, record):
    numero_sus, consentimento = validar_mensagem(record.get("body"))
    with connection.cursor() as cursor:
        cursor.execute(UPSERT_CONSENTIMENTO, (numero_sus, consentimento))
    connection.commit()


def lambda_handler(event, context):
    failures = []
    records = event.get("Records", [])

    if not records:
        return {"batchItemFailures": failures}

    with abrir_conexao() as connection:
        for record in records:
            message_id = record.get("messageId", "desconhecido")
            try:
                processar_registro(connection, record)
                LOGGER.info("Consentimento processado; messageId=%s", message_id)
            except Exception:
                connection.rollback()
                LOGGER.exception("Falha ao processar consentimento; messageId=%s", message_id)
                failures.append({"itemIdentifier": message_id})

    return {"batchItemFailures": failures}
