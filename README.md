# 📌 Todos API — Spring Boot

A RESTful API for managing tasks with **authentication**, **authorization**, and **JWT** support.  
Designed with best practices, strong security.

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
- MySQL

### **Testing**
- JUnit 5
- Mockito

### **Tools**
- Maven
- Docker (optional)
- Swagger / Springdoc OpenAPI

---

# 📚 Project Overview

The **Todos API** allows users to register, authenticate, receive JWT tokens, and interact with protected endpoints to create, update, list, and delete tasks.

This project demonstrates:
- Modern Spring Boot development
- Secure authentication with JWT
- Clean REST architecture
- Separation of concerns
- Well-structured codebase for scalability

---

# 🔐 Authentication & Security

The API uses **JWT authentication**.

### **Authentication Flow**
1. User sends email and password  
2. API validates credentials  
3. API returns a JWT token  
4. All protected routes require:

```

Authorization: Bearer <your_jwt_token>

````

---

# 🌐 API Endpoints  
**Public vs Protected Routes**

---

## 🟢 Public Routes (No Authentication Required)

### **POST /api/todos/v1/auth/register**
Register a new user.

**Request Body Example**
```json
{
  "email": "matheus@email.com",
  "password": "123456"
}
````

---

### **POST /api/todos/v1/auth/login**

Login with an existing user.

**Request Body Example**

```json
{
  "email": "matheus@email.com",
  "password": "123456"
}
```

---

## 🔴 Protected Routes (Authentication Required)

All protected endpoints require a valid JWT.

### **POST /api/todos/v1**

Create a new task.

**Request Body Example**

```json
{
  "name": "feed the cat",
  "description": "buy the food and serve the cat"
}
```

---

### **PUT /api/todos/v1/{id}**

Update an existing task.

**Request Body Example**

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

---

### **GET /api/todos/v1**

Retrieve all tasks of the authenticated user.

---

### **GET /api/todos/v1/{id}**

Retrieve a task by its ID.

---

# 🏗️ Project Architecture

The project follows a clean and modular architecture.

```
src/main/java/com/project/
├── config
├── controllers
├── data
├── entities
├── enums
├── exceptions
├── jwt
├── repositories
├── security
├── serialization/converter
├── services
└── util
```

---

# 🧩 Module Descriptions

### **📂 config**

Application configuration:

* Security settings
* Public/protected route definitions
* General application setup

---

### **📂 controllers**

REST controllers responsible for:

* Handling HTTP requests
* Returning responses
* Delegating logic to services

---

### **📂 data**

Contains DTOs, response objects, and data formatting utilities:

* Standardized output
* Clean request/response contract
* Abstraction between entity and API layers

---

### **📂 entities**

JPA entities representing database tables:

* `UserEntity`
* `TodoEntity`

Includes relationships and mappings.

---

### **📂 enums**

Contains constants such as:

* User roles (`USER`, `ADMIN`)
* Task statuses

---

### **📂 exceptions**

Custom exceptions and global exception handlers:

* Centralized error messages
* Clear API error responses
* Handles validation, not found, unauthorized, etc.

---

### **📂 jwt**

JWT authentication module:

* Token generation
* Token validation
* Authentication filters
* User extraction

Encapsulates all token-related logic.

---

### **📂 repositories**

Spring Data JPA repositories:

* CRUD operations
* Database queries

---

### **📂 security**

Main security layer:

* Spring Security configuration
* Password encoding
* Access control filters
* Route protection

---

### **📂 serialization/converter**

Serialization utilities:

* JSON/YAML converters
* Custom Jackson serializers

---

### **📂 services**

Business logic layer:

* Validation
* Task ownership checks
* Processing and interaction between controllers and repositories

---

### **📂 util**

Utility classes and helper functions shared across modules.

---

# 🧠 Benefits of This Architecture

* **Highly organized structure**
* **Strong separation of concerns**
* **Easy to maintain and scale**
* **Security isolated in its own modules**
* **Test-friendly architecture**
* **Professional-level code quality**

---

---

# 📖 API Documentation (Swagger)

The full interactive documentation for all routes is available at:

👉 **`/swagger-ui.html`** OR 
👉 **`/`**  


Use it to explore public and protected endpoints, send requests, and test JWT authentication directly from the browser.

---

