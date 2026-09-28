import json
import logging
import os
import urllib.request
import urllib.error
from datetime import datetime, timezone

logger = logging.getLogger()
logger.setLevel(logging.INFO)

MS_VACINAS_URL = os.environ.get("MS_VACINAS_URL")
MS_VACINAS_TOKEN = os.environ.get("MS_VACINAS_TOKEN")

REQUIRED_FIELDS = [
    "cpf",
    "vacina",
    "dose",
    "dataAplicacao",
    "calendarioVacinalId",
]


def lambda_handler(event, context):
    """
    Handler acionado por trigger SQS.
    event: {
        "Records": [
            {
                "messageId": "...",
                "receiptHandle": "...",
                "body": "{...}",
                ...
            }
        ]
    }
    """
    records = event.get("Records", [])
    logger.info("Recebidos %d registros da fila", len(records))

    batch_item_failures = []

    for record in records:
        message_id = record.get("messageId")
        try:
            body = record.get("body")
            if not body:
                raise ValueError("Mensagem sem body")

            dados = json.loads(body)
            processar_vacinacao(dados)

        except Exception as exc:
            logger.exception("Erro ao processar mensagem %s: %s", message_id, exc)
            # Retorna o item para ser reprocessado ou ir para DLQ
            batch_item_failures.append({"itemIdentifier": message_id})

    return {"batchItemFailures": batch_item_failures}


def processar_vacinacao(dados: dict) -> None:
    """Valida, normaliza e envia os dados de vacinação."""
    validar_dados(dados)
    payload = normalizar_dados(dados)
    enviar_para_ms_vacinas(payload)


def validar_dados(dados: dict) -> None:
    """Valida campos obrigatórios."""
    faltantes = [campo for campo in REQUIRED_FIELDS if not dados.get(campo)]
    if faltantes:
        raise ValueError(f"Campos obrigatórios ausentes: {', '.join(faltantes)}")

    # Validações extras (exemplo)
    cpf = str(dados["cpf"]).strip()
    if len(cpf) != 11 or not cpf.isdigit():
        raise ValueError(f"CPF inválido: {cpf}")


def normalizar_dados(dados: dict) -> dict:
    """Padroniza o payload para o ms-vacinas."""
    return {
        "cpf": str(dados["cpf"]).strip(),
        "nome": dados.get("nome", "").strip(),
        "vacina": dados["vacina"],
        "dose": dados["dose"],
        "lote": dados.get("lote"),
        "dataAplicacao": dados["dataAplicacao"],
        "unidadeSaude": dados.get("unidadeSaude"),
        "calendarioVacinalId": dados["calendarioVacinalId"],
        "origem": "SUS",
        "recebidoEm": datetime.now(timezone.utc).isoformat(),
    }


def enviar_para_ms_vacinas(payload: dict) -> None:
    """Envia o payload para o ms-vacinas via HTTP POST."""
    if not MS_VACINAS_URL:
        logger.warning(
            "MS_VACINAS_URL não configurada. Payload apenas logado: %s",
            json.dumps(payload, ensure_ascii=False),
        )
        return

    data = json.dumps(payload).encode("utf-8")
    headers = {"Content-Type": "application/json"}

    if MS_VACINAS_TOKEN:
        headers["Authorization"] = f"Bearer {MS_VACINAS_TOKEN}"

    req = urllib.request.Request(
        MS_VACINAS_URL,
        data=data,
        headers=headers,
        method="POST",
    )

    try:
        with urllib.request.urlopen(req, timeout=10) as response:
            if response.status >= 300:
                raise RuntimeError(
                    f"Erro HTTP {response.status}: {response.read().decode()}"
                )
            logger.info("Payload enviado com sucesso. Status: %s", response.status)

    except urllib.error.HTTPError as e:
        logger.error("HTTPError %s: %s", e.code, e.read().decode())
        raise
    except urllib.error.URLError as e:
        logger.error("URLError: %s", e.reason)
        raise