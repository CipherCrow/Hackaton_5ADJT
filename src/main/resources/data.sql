-- Usuários
INSERT INTO usuario (id, login, senha, permissao)
VALUES 
    (1001, 'admin', '$2a$10$A1KFF1KFSHFieWdKwUz7deE14nsjOsYoe.gA3EkFCAyjAfxkP8Una', 'ADMINISTRADOR'),
    (1002, 'visualizador', '$2a$10$A1KFF1KFSHFieWdKwUz7deE14nsjOsYoe.gA3EkFCAyjAfxkP8Una', 'VISUALIZADOR'),
    (1003, 'atendente', '$2a$10$A1KFF1KFSHFieWdKwUz7deE14nsjOsYoe.gA3EkFCAyjAfxkP8Una', 'FUNCIONARIO'),
    (1004, 'doutora', '$2a$10$A1KFF1KFSHFieWdKwUz7deE14nsjOsYoe.gA3EkFCAyjAfxkP8Una', 'FUNCIONARIO'),
	(1005, 'EnfermeiroAlegria', '$2a$10$A1KFF1KFSHFieWdKwUz7deE14nsjOsYoe.gA3EkFCAyjAfxkP8Una', 'FUNCIONARIO');

-- Profissionais de saúde
INSERT INTO profissional_saude (id, nome, crm, especialidade, usuario_id)
VALUES 
    (2001, 'Dra. Ana Clara', 'CRM-SP-123456', 'Clínico Geral', 1004),
    (2002, 'Carlos Souza', NULL, 'Atendimento Geral', 1003),
	(2003, 'Nurse Joy', '154235', 'Enfermeiro', 1005);
