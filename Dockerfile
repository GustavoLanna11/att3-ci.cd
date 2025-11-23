# Multi-stage build para otimizar o tamanho da imagem
FROM maven:3.9.5-eclipse-temurin-17 AS build
WORKDIR /app

# Copiar arquivos de configuração do Maven
COPY pom.xml .
# Baixar dependências (cache layer)
RUN mvn dependency:go-offline -B

# Copiar código fonte
COPY src ./src

# Compilar e gerar JAR
RUN mvn clean package -DskipTests

# Stage final com imagem menor
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copiar JAR do stage de build
COPY --from=build /app/target/biblioteca-api-*.jar app.jar

# Expor porta da aplicação
EXPOSE 8080

# Comando para executar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]

