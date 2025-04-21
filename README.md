# 🏥 PriorizaSUS

Sistema inteligente de triagem e priorização de pacientes, inspirado no Protocolo de Manchester, focado na otimização do atendimento no SUS.

---

## 💡 Objetivo

O **PriorizaSUS** foi desenvolvido como um MVP para um hackathon de pós-graduação com o tema **“Inovação no Atendimento do SUS”**, oferecendo:

- 📌 Registro de chegada de pacientes
- 📋 Triagem com base em sintomas e urgência
- ⏳ Gerenciamento de filas de atendimento
- ✅ Priorização baseada na gravidade (códigos de cor)
- 🔐 Autenticação com JWT e perfis de acesso

---

## 🛠️ Tecnologias Utilizadas

- Java 17
- Spring Boot
- Spring Web / Spring Data JPA / Spring Security
- H2 (banco de dados em memória)
- Swagger (Documentação da API)
- Docker

---
Observação:
* Configuramos o projeto para não ser possível acessar o h2, exceto caso esteja rodando o projeto em debug. 
Desta forma, acessar o banco h2 por meio do conteiner dockerizado não irá funcionar!

## 🚀 Como Rodar o Projeto (Docker)

1. Gere o `jar` do projeto:

```bash
mvn clean package -DskipTests
```
2. Faça o build da imagem Docker:
```bash
docker build -t priorizasus 
```
3. Rode a aplicação
```bash 
docker run -p 8080:8080 priorizasus
```
4. É possível utilizar do nosso swagger para fazer as requisições e testes.
```bash 
📎 http://localhost:8080/swagger-ui/index.html - Swagger
```

---

## 🚀 Como Rodar o Projeto (Render)
O projeto também se encontra na plataforma Render, entretanto recomendamos a utilização da API via Docker localhost devido aos seguintes pontos:
* Por estarmos utilizando o plano free, terá um cold start e pode demorar para executar.
* Devido a URL pública, teus testes podem ser afetados por outro usuário.

###  Processo
1. Realizar requisições para nossa url pública.
```bash 
   https://hackaton-5adjt.onrender.com
```
Exemplos de requisições:
```bash 
POST https://hackaton-5adjt.onrender.com/auth/login
Content-Type: application/json

{
"usuario": "paciente0",
"senha": "123456"
}
```
É recomendado o uso do swagger para testes enquanto utilizar o render.
```bash 
https://hackaton-5adjt.onrender.com/swagger-ui/index.html
```
---
## Miro
Se estiver em duvida do processo, temos o event storm documentado no Miro:
```bash 
📎 https://miro.com/app/board/uXjVIHkt5zk=/?share_link_id=590168005712
```