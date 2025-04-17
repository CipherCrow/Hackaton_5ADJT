-- Usuários
INSERT INTO usuario (id, login, senha, permissao)
VALUES 
    (1001, 'admin', '$2a$10$J1jCNn8BR3Dy3t6wOH3t/Ocya7R.KkgPbNayRG9Mw5BkBiwgVloUq', 'ADMINISTRADOR'),
    (1002, 'visualizador', '$2a$10$J1jCNn8BR3Dy3t6wOH3t/Ocya7R.KkgPbNayRG9Mw5BkBiwgVloUq', 'VISUALIZADOR'),
    (1003, 'atendente', '$2a$10$J1jCNn8BR3Dy3t6wOH3t/Ocya7R.KkgPbNayRG9Mw5BkBiwgVloUq', 'FUNCIONARIO'),
    (1004, 'doutora', '$2a$10$J1jCNn8BR3Dy3t6wOH3t/Ocya7R.KkgPbNayRG9Mw5BkBiwgVloUq', 'FUNCIONARIO'),
	(1005, 'EnfermeiroAlegria', '$2a$10$J1jCNn8BR3Dy3t6wOH3t/Ocya7R.KkgPbNayRG9Mw5BkBiwgVloUq', 'FUNCIONARIO'),
	(1, 'paciente0', '$2a$10$J1jCNn8BR3Dy3t6wOH3t/Ocya7R.KkgPbNayRG9Mw5BkBiwgVloUq', 'PACIENTE'),
	(2, 'paciente1', '$2a$10$J1jCNn8BR3Dy3t6wOH3t/Ocya7R.KkgPbNayRG9Mw5BkBiwgVloUq', 'PACIENTE'),
	(3, 'paciente2', '$2a$10$J1jCNn8BR3Dy3t6wOH3t/Ocya7R.KkgPbNayRG9Mw5BkBiwgVloUq', 'PACIENTE');

-- Profissionais de saúde
INSERT INTO profissional_saude (id, nome, crm, especialidade, usuario_id)
VALUES 
    (2001, 'Dra. Ana Clara', 'CRM-SP-123456', 'Clínico Geral', 1004),
    (2002, 'Carlos Souza', NULL, 'Atendimento Geral', 1003),
	(2003, 'Nurse Joy', '154235', 'Enfermeiro', 1005);

-- Pacientes

INSERT INTO paciente (id, nome, cpf, data_nascimento, telefone, endereco, usuario_id) 
VALUES 
	(1, 'Ana Silva', '0000000000', '1990-01-01T00:00:00', '11999990000', 'Rua Ana Silva, 123', 1)
	,(2, 'Bruno Souza', '0000000001', '1991-01-01T00:00:00', '11999990001', 'Rua Bruno Souza, 123', 2)
	,(3, 'Carlos Oliveira', '0000000002', '1992-01-01T00:00:00', '11999990002', 'Rua Carlos Oliveira, 123', 3)
	,(4, 'Daniela Costa', '0000000003', '1993-01-01T00:00:00', '11999990003', 'Rua Daniela Costa, 123', NULL)
	,(5, 'Eduardo Santos', '0000000004', '1994-01-01T00:00:00', '11999990004', 'Rua Eduardo Santos, 123', NULL)
	,(6, 'Fernanda Pereira', '0000000005', '1995-01-01T00:00:00', '11999990005', 'Rua Fernanda Pereira, 123', NULL)
	,(7, 'Gustavo Lima', '0000000006', '1996-01-01T00:00:00', '11999990006', 'Rua Gustavo Lima, 123', NULL)
	,(8, 'Helena Gomes', '0000000007', '1997-01-01T00:00:00', '11999990007', 'Rua Helena Gomes, 123', NULL)
	,(9, 'Igor Barros', '0000000008', '1998-01-01T00:00:00', '11999990008', 'Rua Igor Barros, 123', NULL)
	,(10, 'Julia Ramos', '0000000009', '1999-01-01T00:00:00', '11999990009', 'Rua Julia Ramos, 123', NULL);

-- Fila de Triagem

INSERT INTO fila_triagem (id, paciente_id, status_triagem, horario_entrada) 
	VALUES 
		(1, 1, 'AGUARDANDO', '2025-04-17T03:33:28.639562'),
		(2, 2, 'AGUARDANDO', '2025-04-17T03:28:28.639577');


-- Sintomas usados nas triagens
INSERT INTO sintoma (id, descricao, gravidade) VALUES
    (1, 'Febre leve', 2),
    (2, 'Dor moderada', 3),
    (3, 'Cansaço extremo', 3),
    (4, 'Tontura', 2),
    (5, 'Dor intensa', 4);
	
-- Triagens
INSERT INTO triagem (id, paciente_id, nivel_prioridade_enum, data_triagem, profissional_id, prioridade_manual)
VALUES 
    (1, 4, 'AMARELO', '2025-04-17T03:50:00', 2003, false),
    (2, 5, 'AMARELO', '2025-04-17T03:52:00', 2003, false),
    (3, 6, 'VERDE', '2025-04-17T03:54:00', 2003, false),
	(4, 7, 'AMARELO', '2025-04-17T04:00:00', 2001, false),
    (5, 8, 'VERDE'  , '2025-04-17T04:02:00', 2001, false),
    (6, 9, 'LARANJA', '2025-04-17T04:04:00', 2001, false),
    (7, 10,'AZUL'   , '2025-04-17T04:06:00', 2001, false);

	
INSERT INTO triagem_sintomas (triagem_id, sintomas_id) VALUES
	(1, 2),  -- Paciente 4 (AMARELO): Dor moderada + Cansaço extremo
    (1, 3),
	
	(2, 5),  -- Paciente 5 (AMARELO): Dor intensa
	
    (3, 1),  -- Paciente 6 (VERDE): Febre leve + Tontura
    (3, 4),
	
	(4, 2),  -- paciente 7: Dor moderada
    (4, 3),  -- paciente 7: Cansaço extremo

    (5, 1),  -- paciente 8: Febre leve
    (5, 4),  -- paciente 8: Tontura

    (6, 5),  -- paciente 9: Dor intensa
    (6, 2),  -- paciente 9: Dor moderada

    (7, 1),  -- paciente 10: Febre leve
    (7, 4);  -- paciente 10: Tontura

-- Atendimento comum (com triagem)
INSERT INTO fila_atendimento (id, triagem_id, atendimento_administrativo, horario_entrada_fila, status_atendimento_enum, tempo_espera_estimado, peso_fila) 
VALUES
    (1, 1, false, '2025-04-17T03:55:00', 'PENDENTE', NULL, 40), -- AMARELO
    (2, 2, false, '2025-04-17T03:56:00', 'PENDENTE', NULL, 40), -- AMARELO
    (3, 3, false, '2025-04-17T03:57:00', 'PENDENTE', NULL, 20); -- VERDE

-- Atendimento administrativo (sem triagem)
INSERT INTO fila_atendimento (id, triagem_id, atendimento_administrativo, horario_entrada_fila, status_atendimento_enum, tempo_espera_estimado, peso_fila)
VALUES
    (4, NULL, true, '2025-04-17T03:58:00', 'PENDENTE', NULL, 5); -- Base de peso administrativo
	
-- Historico De Atendimentos
INSERT INTO atendimento_historico (
    id, triagem_id, diagnostico, prescricao, data_atendimento, profissional_id
) VALUES
    (1, 4, 'Gripe comum'      , 'Repouso e hidratação', '2025-04-17T04:30:00', 2001),
    (2, 5, 'Resfriado leve'   , 'Vitamina C'         , '2025-04-17T04:32:00', 2001),
    (3, 6, 'Dor de cabeça'    , 'Analgésico'         , '2025-04-17T04:34:00', 2001),
    (4, 7, 'Alergia situacional', 'Anti-histamínico'  , '2025-04-17T04:36:00', 2001);