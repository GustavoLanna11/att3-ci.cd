# Biblioteca API - Projeto Spring Boot com CI/CD

API REST para gestão de livros em biblioteca desenvolvida com Spring Boot.

## 📋 Funcionalidades

- **Listar todos os livros** - `GET /api/livros`
- **Buscar livros por título** - `GET /api/livros/buscar?titulo={titulo}`
- **Buscar livro por ID** - `GET /api/livros/{id}`
- **Criar novo livro** - `POST /api/livros`
- **Atualizar livro** - `PUT /api/livros/{id}`
- **Deletar livro** - `DELETE /api/livros/{id}`

## 🚀 Tecnologias

- Java 17
- Spring Boot 3.2.0
- Maven
- JUnit 5
- Mockito
- Docker

## 📦 Estrutura do Projeto

```
src/
├── main/
│   ├── java/com/biblioteca/
│   │   ├── BibliotecaApplication.java
│   │   ├── controller/
│   │   │   └── LivroController.java
│   │   ├── model/
│   │   │   └── Livro.java
│   │   ├── service/
│   │   │   └── LivroService.java
│   │   └── exception/
│   │       └── GlobalExceptionHandler.java
│   └── resources/
│       └── application.properties
└── test/
    └── java/com/biblioteca/
        ├── controller/
        │   └── LivroControllerTest.java
        └── service/
            └── LivroServiceTest.java
```

## 🧪 Testes

O projeto inclui testes unitários completos para:
- Todas as rotas do controller
- Lógica de negócio do service

Para executar os testes:
```bash
mvn test
```

## 🏗️ Build

Para compilar o projeto:
```bash
mvn clean package
```

O arquivo JAR será gerado em `target/biblioteca-api-1.0.0.jar`

Para executar a aplicação:
```bash
java -jar target/biblioteca-api-1.0.0.jar
```

A aplicação estará disponível em: `http://localhost:8080`

## 🐳 Docker

### Construir a imagem Docker:

```bash
docker build -t biblioteca-api:latest .
```

### Executar o container:

```bash
docker run -p 8080:8080 biblioteca-api:latest
```

A aplicação estará disponível em: `http://localhost:8080`

### Executar com Docker Compose (opcional):

```bash
docker-compose up
```

## 🔄 CI/CD

O projeto inclui dois workflows do GitHub Actions:

### 1. CI/CD Pipeline (`.github/workflows/ci-cd.yml`)

Executa em branches: `main`, `master`, `develop`

1. **Test** - Executa todos os testes unitários
2. **Build** - Gera o artefato JAR após os testes passarem

### 2. Docker CD Pipeline (`.github/workflows/docker-cd.yml`)

Executa apenas na branch: `docker-deploy`

1. **Test** - Executa testes usando container Maven
2. **Build and Push** - Constrói e envia a imagem Docker para Docker Hub

### Configuração do Docker Hub

Para que o workflow funcione, configure os seguintes secrets no GitHub:

1. Vá em **Settings** → **Secrets and variables** → **Actions**
2. Adicione:
   - `DOCKER_USERNAME`: Seu nome de usuário do Docker Hub
   - `DOCKER_PASSWORD`: Seu token de acesso do Docker Hub

#### Como obter o token do Docker Hub:

1. Acesse https://hub.docker.com/settings/security
2. Clique em **New Access Token**
3. Crie um token com permissão de **Read & Write**
4. Copie o token e adicione como `DOCKER_PASSWORD` nos secrets

### Artefatos Gerados

- **Test Results**: Relatórios de testes em `target/surefire-reports/`
- **JAR File**: Artefato final em `target/biblioteca-api-*.jar`
- **Docker Image**: Imagem Docker publicada no Docker Hub

## 📝 Modelo de Dados

### Livro

```json
{
  "id": 1,
  "titulo": "Dom Casmurro",
  "autor": "Machado de Assis",
  "ano": 1899,
  "disponivel": true
}
```

### Validações

- `titulo`: Obrigatório, não pode ser vazio
- `autor`: Obrigatório, não pode ser vazio
- `ano`: Obrigatório, deve ser um número positivo

## 📚 Exemplos de Uso

### Criar um livro
```bash
curl -X POST http://localhost:8080/api/livros \
  -H "Content-Type: application/json" \
  -d '{
    "titulo": "Dom Casmurro",
    "autor": "Machado de Assis",
    "ano": 1899
  }'
```

### Listar todos os livros
```bash
curl http://localhost:8080/api/livros
```

### Buscar por título
```bash
curl "http://localhost:8080/api/livros/buscar?titulo=Dom"
```

### Buscar por ID
```bash
curl http://localhost:8080/api/livros/1
```

### Atualizar um livro
```bash
curl -X PUT http://localhost:8080/api/livros/1 \
  -H "Content-Type: application/json" \
  -d '{
    "titulo": "Dom Casmurro - Edição Especial",
    "autor": "Machado de Assis",
    "ano": 1899,
    "disponivel": false
  }'
```

### Deletar um livro
```bash
curl -X DELETE http://localhost:8080/api/livros/1
```

## 🚢 Deploy

### Usando Docker Hub:

Após o workflow executar na branch `docker-deploy`, a imagem estará disponível:

```bash
docker pull <seu-usuario>/biblioteca-api:latest
docker run -p 8080:8080 <seu-usuario>/biblioteca-api:latest
```

## 👨‍💻 Autor

Desenvolvido como parte de uma atividade de CI/CD com Spring Boot e Docker.
