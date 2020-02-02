INSERT INTO USUARIO(id, nome, email, senha) VALUES(1, 'Aluno', 'aluno@email.com', '$2a$10$Examhbym6KxHg87.lqVbte4RKfwvvDydyoM6k3aTuYGaQK72km3dG');
INSERT INTO PERFIL (ID, NOME) VALUES (1, 'ADMIN');

INSERT INTO USUARIO_PERFIS (USUARIO_ID, PERFIS_ID) VALUES (1,1);
       
INSERT INTO CURSO(nome, categoria) VALUES('Spring Boot', 'Programação');
INSERT INTO CURSO(nome, categoria) VALUES('HTML 5', 'Front-end');

INSERT INTO TOPICO(titulo, mensagem, data_criacao, status, autor_id, curso_id) VALUES('Dúvida', 'Erro ao criar projeto', '2019-05-05 18:00:00', 'NAO_RESPONDIDO', 1, 1);
INSERT INTO TOPICO(titulo, mensagem, data_criacao, status, autor_id, curso_id) VALUES('Dúvida 2', 'Projeto não compila', '2019-05-05 19:00:00', 'NAO_RESPONDIDO', 1, 1);
INSERT INTO TOPICO(titulo, mensagem, data_criacao, status, autor_id, curso_id) VALUES('Dúvida 3', 'Tag HTML', '2019-05-05 20:00:00', 'NAO_RESPONDIDO', 1, 2);

