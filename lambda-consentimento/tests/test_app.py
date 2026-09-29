import json
import os
import sys
from pathlib import Path
from unittest.mock import MagicMock, patch

sys.path.insert(0, str(Path(__file__).parents[1] / "src"))
sys.modules.setdefault("boto3", MagicMock())
sys.modules.setdefault("psycopg", MagicMock())

import app


def setup_function():
    app.obter_credenciais.cache_clear()


def test_validar_mensagem_valida():
    numero_sus, consentimento = app.validar_mensagem(
        json.dumps({"numero_sus": "123456789012345", "consentimento": True})
    )
    assert numero_sus == 123456789012345
    assert consentimento is True


def test_rejeitar_numero_sus_invalido():
    try:
        app.validar_mensagem(json.dumps({"numero_sus": "123", "consentimento": True}))
        assert False, "Era esperada MensagemInvalidaError"
    except app.MensagemInvalidaError:
        pass


def test_retornar_somente_falha_parcial():
    event = {
        "Records": [
            {"messageId": "ok", "body": "{}"},
            {"messageId": "erro", "body": "{}"},
        ]
    }
    connection = MagicMock()
    connection.__enter__.return_value = connection

    with patch.object(app, "abrir_conexao", return_value=connection), patch.object(
        app, "processar_registro", side_effect=[None, RuntimeError("falha")]
    ):
        response = app.lambda_handler(event, None)

    assert response == {"batchItemFailures": [{"itemIdentifier": "erro"}]}
    connection.rollback.assert_called_once()
