# 📌 Todos API — Spring Boot

A secure and scalable REST API for task management, featuring **authentication**, **authorization**, and **JWT tokens**, built with modern Spring Boot best practices.

---

## 🚀 Technologies Used

### **Backend**
- Java 21+
- Spring Boot 3.x
- Spring Web
- Spring Security
- Spring Data JPA
- JWT (JSON Web Tokens)

### **Database**
- MySQL 8

### **Testing**
- JUnit 5  
- Mockito

### **Tools**
- Maven  
- Docker  
- Swagger / Springdoc OpenAPI  

---

## 📚 Project Description

**Task Manager API** allows users to register, authenticate using JWT, and manage tasks with protected endpoints.

This project demonstrates:

- Advanced Spring Boot usage  
- Secure authentication and authorization  
- Clean and scalable REST architecture  
- Separation of layers  
- Professional-level project structure  

---

## 🔐 Authentication Flow (JWT)

1. User sends email + password  
2. Server validates credentials  
3. A JWT token is generated and returned  
4. All protected endpoints must include:

```
Authorization: Bearer <your_token>
```

---

# 🌐 API Endpoints — Public & Protected Routes

Below is the complete list of API endpoints.

---

## 🟢 Public Routes (No Authentication Required)

### **POST /api/todos/v1/auth/register**
Create a new user account.

**Body Example:**
```json
{
  "email": "matheus@email.com",
  "password": "123456"
}
```

### **POST /api/todos/v1/auth/login**
Authenticate a registered user.

**Body Example:**
```json
{
  "email": "matheus@email.com",
  "password": "123456"
}
```

---

## 🔴 Protected Routes (JWT Required)

### **POST /api/todos/v1**
Create a new task.

```json
{
  "name": "feed the cat",
  "description": "buy the food and serve the cat"
}
```

---

### **PUT /api/todos/v1/{id}**
Update an existing task.

```json
{
  "name": "name changed",
  "description": "description changed",
  "status": "COMPLETE"
}
```

---

### **DELETE /api/todos/v1/{id}**
Delete a task.

(No body required)

---

### **GET /api/todos/v1**
Retrieve all tasks.

---

### **GET /api/todos/v1/{id}**
Retrieve a task by ID.

---

# 📘 Swagger Documentation

Swagger UI is available at:

👉 **http://localhost:8080/swagger-ui.html**  
or  
👉 **http://localhost:8080/swagger-ui/index.html**

---

# 🧩 Project Architecture

### **📂 config**
Security configurations, CORS, filters, and global settings.

### **📂 controllers**
REST controllers responsible for request handling.

### **📂 data**
DTOs and response models for communication.

### **📂 entities**
JPA entities representing database tables.

### **📂 enums**
Enum types such as task status and user roles.

### **📂 exceptions**
Global exception handling and custom error responses.

### **📂 jwt**
Token provider, JWT filter, validation utilities.

### **📂 repositories**
JPA repositories for database access.

### **📂 security**
Authentication/authorization configuration and password encoding.

### **📂 serialization/converter**
Custom serializers and format handlers.

### **📂 services**
Business logic (task rules, user ownership validation, etc.).

### **📂 util**
Helper functions used across the project.

---

# 📦 Building the Project Locally (IMPORTANT)

If you want to run the application **without Docker**, you must manually build the project first so that the final JAR is generated inside the `target/` directory.

### 🔧 Build the project:
```bash
mvn clean package
```

Then run it with:

```bash
java -jar target/*.jar
```

---

# 🐳 About Docker Builds

If you are running the project using **Docker or Docker Compose**,  
👉 **you DO NOT need to generate the `target/` folder manually.**

The Dockerfile already includes:

```dockerfile
RUN mvn clean package -DskipTests
```

Meaning:

✅ The project is automatically compiled inside the Docker image  
✅ The JAR is created during the image build  
✅ No local `mvn package` is required  

---

# 🐳 Docker Support

This project includes full Docker support for both the application and MySQL.

---

## 📦 Dockerfile (Multi-Stage Build)

```dockerfile
FROM maven:4.0.0-rc-4-eclipse-temurin-21-alpine AS build

WORKDIR /app

COPY pom.xml ./
COPY src ./src

RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

ARG JAR_FILE=target/*.jar
COPY --from=build /app/${JAR_FILE} app.jar

RUN adduser -D todouser
RUN chown todouser:todouser /app

USER todouser

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]
```

---

## 🏗️ Building the Docker Image Manually

If you prefer to build the image manually:

```bash
docker build -t todos:v1 .
```

---

## 🐳 docker-compose.yml

```yaml
services:
  app:
    image: todos:v1
    build: .
    ports:
      - "8080:8080"
    environment:
      - SPRING_DATASOURCE_URL=jdbc:mysql://mysql:3306/mydatabase
      - SPRING_DATASOURCE_USERNAME=myuser
      - SPRING_DATASOURCE_PASSWORD=mypassword
      - SPRING_JPA_HIBERNATE_DDL_AUTO=update
      - SPRING_JPA_SHOW_SQL=true
      - SPRINGDOC_PATHS_TO_MATCH=/api/**/v1/**
      - SPRINGDOC_SWAGGER_UI_USE_ROOT_PATH=true
      - CORS_ORIGINPATTERNS=http://example1:0000,https://example.com.br
      - JWT_SECRET=d90b2fa69a24fb813194afe9a323541a
    depends_on:
      - mysql

  mysql:
    image: mysql:8.0
    environment:
      - MYSQL_ROOT_PASSWORD=rootpassword
      - MYSQL_DATABASE=mydatabase
      - MYSQL_USER=myuser
      - MYSQL_PASSWORD=mypassword
    ports:
      - "3306:3306"
    volumes:
      - mysql_data:/var/lib/mysql

volumes:
  mysql_data:
```

---

## ▶️ Running the Application with Docker Compose

### 1️⃣ Build all images
```bash
docker compose build
```

### 2️⃣ Start containers
```bash
docker compose up
```

### 3️⃣ Access the API  
👉 http://localhost:8080

### 4️⃣ Access Swagger  
👉 http://localhost:8080/swagger-ui.html

---

## 🛑 Stopping the Containers

```bash
docker compose down
```

---

## 💾 Persistent Database Storage

MySQL uses a Docker volume:

```
mysql_data
```

Your data remains intact even after containers are removed.

---

# ✅ Final Notes

This project follows a clean, scalable, and security-focused architecture suitable for real-world applications and portfolio presentation.# 📌 Todos API — Spring Boot

A secure and scalable REST API for task management, featuring **authentication**, **authorization**, and **JWT tokens**, built with modern Spring Boot best practices.

---

## 🚀 Technologies Used

### **Backend**
- Java 21+
- Spring Boot 3.x
- Spring Web
- Spring Security
- Spring Data JPA
- JWT (JSON Web Tokens)

### **Database**
- MySQL 8

### **Testing**
- JUnit 5  
- Mockito

### **Tools**
- Maven  
- Docker  
- Swagger / Springdoc OpenAPI  

---

## 📚 Project Description

**Task Manager API** allows users to register, authenticate using JWT, and manage tasks with protected endpoints.

This project demonstrates:

- Advanced Spring Boot usage  
- Secure authentication and authorization  
- Clean and scalable REST architecture  
- Separation of layers  
- Professional-level project structure  

---

## 🔐 Authentication Flow (JWT)

1. User sends email + password  
2. Server validates credentials  
3. A JWT token is generated and returned  
4. All protected endpoints must include:

```
Authorization: Bearer <your_token>
```

---

# 🌐 API Endpoints — Public & Protected Routes

Below is the complete list of API endpoints.

---

## 🟢 Public Routes (No Authentication Required)

### **POST /api/todos/v1/auth/register**
Create a new user account.

**Body Example:**
```json
{
  "email": "matheus@email.com",
  "password": "123456"
}
```

### **POST /api/todos/v1/auth/login**
Authenticate a registered user.

**Body Example:**
```json
{
  "email": "matheus@email.com",
  "password": "123456"
}
```

---

## 🔴 Protected Routes (JWT Required)

### **POST /api/todos/v1**
Create a new task.

```json
{
  "name": "feed the cat",
  "description": "buy the food and serve the cat"
}
```

---

### **PUT /api/todos/v1/{id}**
Update an existing task.

```json
{
  "name": "name changed",
  "description": "description changed",
  "status": "COMPLETE"
}
```

---

### **DELETE /api/todos/v1/{id}**
Delete a task.

(No body required)

---

### **GET /api/todos/v1**
Retrieve all tasks.

---

### **GET /api/todos/v1/{id}**
Retrieve a task by ID.

---

# 📘 Swagger Documentation

Swagger UI is available at:

👉 **http://localhost:8080/swagger-ui.html**  
or  
👉 **http://localhost:8080/swagger-ui/index.html**

---

# 🧩 Project Architecture

### **📂 config**
Security configurations, CORS, filters, and global settings.

### **📂 controllers**
REST controllers responsible for request handling.

### **📂 data**
DTOs and response models for communication.

### **📂 entities**
JPA entities representing database tables.

### **📂 enums**
Enum types such as task status and user roles.

### **📂 exceptions**
Global exception handling and custom error responses.

### **📂 jwt**
Token provider, JWT filter, validation utilities.

### **📂 repositories**
JPA repositories for database access.

### **📂 security**
Authentication/authorization configuration and password encoding.

### **📂 serialization/converter**
Custom serializers and format handlers.

### **📂 services**
Business logic (task rules, user ownership validation, etc.).

### **📂 util**
Helper functions used across the project.

---

# 📦 Building the Project Locally (IMPORTANT)

If you want to run the application **without Docker**, you must manually build the project first so that the final JAR is generated inside the `target/` directory.

### 🔧 Build the project:
```bash
mvn clean package
```

Then run it with:

```bash
java -jar target/*.jar
```

---

# 🐳 About Docker Builds

If you are running the project using **Docker or Docker Compose**,  
👉 **you DO NOT need to generate the `target/` folder manually.**

The Dockerfile already includes:

```dockerfile
RUN mvn clean package -DskipTests
```

Meaning:

✅ The project is automatically compiled inside the Docker image  
✅ The JAR is created during the image build  
✅ No local `mvn package` is required  

---

# 🐳 Docker Support

This project includes full Docker support for both the application and MySQL.

---

## 📦 Dockerfile (Multi-Stage Build)

```dockerfile
FROM maven:4.0.0-rc-4-eclipse-temurin-21-alpine AS build

WORKDIR /app

COPY pom.xml ./
COPY src ./src

RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

ARG JAR_FILE=target/*.jar
COPY --from=build /app/${JAR_FILE} app.jar

RUN adduser -D todouser
RUN chown todouser:todouser /app

USER todouser

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]
```

---

## 🏗️ Building the Docker Image Manually

If you prefer to build the image manually:

```bash
docker build -t todos:v1 .
```

---

## 🐳 docker-compose.yml

```yaml
services:
  app:
    image: todos:v1
    build: .
    ports:
      - "8080:8080"
    environment:
      - SPRING_DATASOURCE_URL=jdbc:mysql://mysql:3306/mydatabase
      - SPRING_DATASOURCE_USERNAME=myuser
      - SPRING_DATASOURCE_PASSWORD=mypassword
      - SPRING_JPA_HIBERNATE_DDL_AUTO=update
      - SPRING_JPA_SHOW_SQL=true
      - SPRINGDOC_PATHS_TO_MATCH=/api/**/v1/**
      - SPRINGDOC_SWAGGER_UI_USE_ROOT_PATH=true
      - CORS_ORIGINPATTERNS=http://example1:0000,https://example.com.br
      - JWT_SECRET=d90b2fa69a24fb813194afe9a323541a
    depends_on:
      - mysql

  mysql:
    image: mysql:8.0
    environment:
      - MYSQL_ROOT_PASSWORD=rootpassword
      - MYSQL_DATABASE=mydatabase
      - MYSQL_USER=myuser
      - MYSQL_PASSWORD=mypassword
    ports:
      - "3306:3306"
    volumes:
      - mysql_data:/var/lib/mysql

volumes:
  mysql_data:
```

---

## ▶️ Running the Application with Docker Compose

### 1️⃣ Build all images
```bash
docker compose build
```

### 2️⃣ Start containers
```bash
docker compose up
```

### 3️⃣ Access the API  
👉 http://localhost:8080

### 4️⃣ Access Swagger  
👉 http://localhost:8080/swagger-ui.html

---

## 🛑 Stopping the Containers

```bash
docker compose down
```

---

## 💾 Persistent Database Storage

MySQL uses a Docker volume:

```
mysql_data
```

Your data remains intact even after containers are removed.

---

# ✅ Final Notes

This project follows a clean, scalable, and security-focused architecture suitable for real-world applications and portfolio presentation.
