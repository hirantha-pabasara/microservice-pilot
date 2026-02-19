# 🚀 Microservice Pilot Project

A Spring Boot Microservices project built with Java 21, featuring service discovery, API gateway, and inter-service communication.

---

## 📐 Architecture

```
Client (Postman / Frontend)
          ↓
  API Gateway (port 8080)        ← Single entry point
       ↓           ↓
Product Service  Order Service
 (port 8081)     (port 8082)
       ↓               ↓
  product_db        order_db
   (MySQL)           (MySQL)
       ↑
  Order Service calls Product Service
  via OpenFeign + Eureka
```

All services register with **Eureka Server (port 8761)** for service discovery.

---

## 🧩 Services

### 1. Eureka Server (`port 8761`)
Service registry — acts as a phone book for all microservices. Every service registers here so others can discover them by name instead of hardcoded URLs.

### 2. API Gateway (`port 8080`)
Single entry point for all client requests. Routes incoming requests to the appropriate microservice using Eureka for load-balanced service discovery.

| Route | Forwarded To |
|-------|-------------|
| `/api/products/**` | Product Service |
| `/api/orders/**` | Order Service |

### 3. Product Service (`port 8081`)
Manages product data with full CRUD operations.

**Endpoints:**

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/products` | Create a product |
| GET | `/api/products` | Get all products |
| GET | `/api/products/{id}` | Get product by ID |
| PUT | `/api/products/{id}` | Update a product |
| DELETE | `/api/products/{id}` | Delete a product |

### 4. Order Service (`port 8082`)
Manages orders and communicates with Product Service via OpenFeign to fetch product details and calculate total price automatically.

**Endpoints:**

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/orders` | Create an order |
| GET | `/api/orders` | Get all orders |
| GET | `/api/orders/{id}` | Get order by ID |
| PUT | `/api/orders/{id}` | Update an order |
| DELETE | `/api/orders/{id}` | Delete an order |

---

## 🛠️ Tech Stack

| Technology | Version | Purpose |
|------------|---------|---------|
| Java | 21 | Programming language |
| Spring Boot | 3.5.0 | Application framework |
| Spring Cloud Gateway | Latest | API Gateway |
| Spring Cloud Netflix Eureka | Latest | Service discovery |
| Spring Data JPA | Latest | Database ORM |
| OpenFeign | Latest | Inter-service communication |
| MySQL | Latest | Database |
| Lombok | Latest | Boilerplate reduction |
| Maven | Latest | Build tool |

---

## 📁 Project Structure

Each service follows a clean layered architecture:

```
com.pilot.<service>/
├── controller/       ← REST API endpoints
├── service/          ← Business logic
├── repository/       ← Database operations
├── entity/           ← Database table mapping
├── dto/              ← Request and Response objects
│   ├── *RequestDTO   ← Incoming data
│   └── *ResponseDTO  ← Outgoing data
└── client/           ← Feign clients (order-service only)
```

---

## ⚙️ Prerequisites

- Java 21
- Maven
- MySQL
- IntelliJ IDEA (recommended)

---

## 🗄️ Database Setup

Create the following databases in MySQL before running the services:

```sql
CREATE DATABASE product_db;
CREATE DATABASE order_db;
```

---

## 🚀 Running the Project

Start the services **in this order** (order matters!):

**1. Start Eureka Server**
```bash
cd eureka-server
mvn spring-boot:run
```
Visit: http://localhost:8761

**2. Start Product Service**
```bash
cd product-service
mvn spring-boot:run
```

**3. Start Order Service**
```bash
cd order-service
mvn spring-boot:run
```

**4. Start API Gateway**
```bash
cd api-gateway
mvn spring-boot:run
```

---

## 🧪 Testing via API Gateway

All requests go through port **8080** (API Gateway).

**Create a Product:**
```http
POST http://localhost:8080/api/products
Content-Type: application/json

{
    "name": "Laptop",
    "description": "Gaming Laptop",
    "price": 1500.00,
    "quantity": 10
}
```

**Create an Order:**
```http
POST http://localhost:8080/api/orders
Content-Type: application/json

{
    "productId": 1,
    "quantity": 2
}
```
> Order Service automatically fetches the product price and calculates `totalPrice = price × quantity`.

---

## 🔄 Inter-Service Communication

Order Service uses **OpenFeign** to call Product Service:

```
Order Service → Feign Client → Eureka (service lookup) → Product Service
```

No hardcoded URLs — Eureka dynamically resolves the service location.

---

## 👤 Author

Built as a pilot project to demonstrate Spring Boot Microservices architecture.
