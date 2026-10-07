- Prontuario Infantil Online
- Lembrete de vacinas
- Lembrete de Consultas 
	- Enviar notificação para lembrar da consulta (Definir o tempo de antecedencia dos lembretes)
	- Solicitar Confirmação
- Médicos Alteram prontuário
- Enfermeiros podem registrar aplicação de vacinas no prontuário
- Pacientes podem visualizar o prontuário
- Criar uma lista de vacinas com as idades de aplicação para cada vacina

--=====================================================================================================

- Marcação de consultas
	 - MS de agendamento que recebe um payload com os dados do paciente, médico e data/hora(montar o payload)
		- O microserviço verifica se foi enviado agendamento de vacina
	 - Faz as validações e Posta os dados no tópico de agendamento (Tópico SNS)
	 - Lambda que recebe os dados da fila(SQS) e salva as informações na agenda do médico (tabela de Agenda)
	 - Lambda que recebe os dados da fila(SQS) e salva as informações no prontuário do paciente (tabela prontuário)	 
- Prontuario Infantil Online
	- MS Prontuário - Serviço Get para os responsáveis visualizarem o prontuário da criança
- Lembrete de vacinas
	Lambda scheduler que envia mensagem lembrando de vacinas
- Lembrete de Consultas 
	Lambda que Envia notificação para lembrar da consulta (Definir o tempo de antecedencia dos lembretes)
	- Solicitar Confirmação
- Médicos Alteram prontuário
	MS Prontuário - Serviço post que altera o prontuário de um paciente
- Enfermeiros podem registrar aplicação de vacinas no prontuário
	MS Prontuário - Endpoint que recebe as informações de vacinação e salva no prontuário
- Pacientes podem visualizar o prontuário
	MS Prontuário - que busca na tabela de prontuário as informações do paciente logado
- Criar uma lista de vacinas com as idades de aplicação para cada vacina
	MS Vacinação - Com crud de vacinas e calendário de vacinação
	
	
	 MS Prontuário
		- Autenticação para Médico, PAciente, Enfermeiras
		- Serviço Get para os responsáveis visualizarem o prontuário da criança
		- Serviço post que altera o prontuário de um paciente
		- Endpoint que recebe as informações de vacinação e salva no prontuário
		- que busca na tabela de prontuário as informações do paciente logado
	- Lembrete de vacinas
		Lambda scheduler que envia mensagem lembrando de vacinas
	- Lembrete de Consultas 
		Lambda que Envia notificação para lembrar da consulta (Definir o tempo de antecedencia dos lembretes)		
	
	MS Vacinação
		- Com crud de vacinas e calendário de vacinaçãov

--=====================================================================================================

Após conversa com professor, ficou decidido focar no ponto de vacinação. 
A questão de avisos ativos de vacinação e controle de vacinas pendentes. Seguindo essa escolha, Começando abaixo com o MER:

Tabela Pessoa
  │
  ├── Id
  ├── dataNascimento
  ├── sexo
  ├── Nome
  └── CPF

Tabela Parentesco
  │
  ├── Id
  ├── pessoa_id
  ├── responsavel_id
  └── tipo_parentesco
  
Tabela Aplicacao Vacina
  │
  ├── Id
  ├── id Pessoa
  ├── esquema_vacinacao_id
  ├── data Aplicacao
  ├── Lote
  ├── Id Agente Saude
  ├── Observacao  
  └── Id Unidade Atendimento
  
Tabela Vacina
  │
  ├── Id
  ├── Nome
  ├── Sigla
  └── Fabricante
  
Tabela Agente Saúde
  │
  ├── Id
  ├── Nome
  ├── CPF
  ├── Categoria Profissional
  ├── Numero Registro
  └── RG 
  
Tabela Unidade Atendimento
  │
  ├── Id
  ├── Nome
  ├── Cnes
  └── Endereco
   
Tabela Calendario Vacinal
  │
  ├── Id
  ├── nome
  ├── versao
  ├── data_inicio_vigencia
  └── data_fim_vigencia

Tabela Esquema Vacinacao
  │
  ├── Id
  ├── calendario_vacinal_id
  ├── vacina_id
  ├── numero_dose
  ├── tipo_dose
  ├── idade_minima_valor
  ├── idade_minima_unidade
  ├── idade_recomendada_valor
  ├── idade_recomendada_unidade
  ├── idade_maxima_valor
  ├── idade_maxima_unidade  
  ├── intervalo_minimo_valor
  └── intervalo_minimo_unidade    






