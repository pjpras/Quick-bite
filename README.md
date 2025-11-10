# 🍔 QuickBite Microservices - Food Delivery Platform

A production-ready microservices architecture following **Single Responsibility Principle (SRP)** for a food delivery platform.

## 🎯 Architecture

```
┌─────────────────────────────────────────────────────┐
│ 1. USERSERVICE (Authentication)                     │
│    - Generates JWT tokens                           │
│    - Validates user credentials                     │
│    - Handles /login, /register                      │
└─────────────────────────────────────────────────────┘
                      ↓ Returns JWT Token
┌─────────────────────────────────────────────────────┐
│ 2. API GATEWAY (Validation + Authorization)         │
│    - Validates JWT token                            │
│    - Checks user roles/permissions                  │
│    - Routes to microservices                        │
└─────────────────────────────────────────────────────┘
                      ↓ Forwards validated request
┌─────────────────────────────────────────────────────┐
│ 3. MICROSERVICES (Business Logic ONLY)              │
│    - UserService: User CRUD operations              │
│    - FoodService: Food management                   │
│    - NO JWT validation (trust Gateway)              │
└─────────────────────────────────────────────────────┘
```

## 🏗️ Services

| Service | Port | Responsibility |
|---------|------|----------------|
| **Eureka Server** | 8761 | Service Discovery |
| **API Gateway** | 9093 | JWT Validation & Routing |
| **UserService** | 8081 | Authentication + User Management |
| **FoodService** | 8082 | Food/Cart/Order Management |
| **Frontend** | 5173 | React UI |

## 🚀 Quick Start

### Prerequisites
- Java 21
- Maven 3.8+
- MySQL 8.0+
- Node.js 18+

### 1. Database Setup
```sql
CREATE DATABASE userdb;
CREATE DATABASE fooddb;
```

Update passwords in `application.properties` files:
```properties
spring.datasource.password=your_mysql_password
```

### 2. Start Services (in this order)

```powershell
# 1. Eureka Server
cd Eureka
mvn spring-boot:run

# 2. UserService (new terminal)
cd userservice
mvn spring-boot:run

# 3. FoodService (new terminal)
cd foodserviceapp
mvn spring-boot:run

# 4. API Gateway (new terminal)
cd Api-Gateway
mvn spring-boot:run

# 5. Frontend (new terminal)
cd Quick-bite-main
npm install
npm run dev
```

### 3. Verify Services
- Eureka Dashboard: http://localhost:8761
- API Gateway: http://localhost:9093
- Frontend: http://localhost:5173

## 🔐 Authentication Flow

### 1. Register User
```powershell
curl -X POST http://localhost:9093/app1/api/v1/users/signup/customer `
  -H "Content-Type: application/json" `
  -d '{
    "email": "user@example.com",
    "password": "password123",
    "name": "John Doe",
    "phno": "1234567890",
    "location": "New York"
  }'
```

### 2. Login
```powershell
curl -X POST http://localhost:9093/app1/api/v1/users/login `
  -H "Content-Type: application/json" `
  -d '{
    "email": "user@example.com",
    "password": "password123"
  }'
```

Response:
```json
{
  "id": 1,
  "email": "user@example.com",
  "name": "John Doe",
  "role": "customer",
  "accessToken": "eyJhbGciOiJIUzI1NiIs..."
}
```

### 3. Access Protected Endpoint
```powershell
curl -X GET http://localhost:9093/app2/api/v1/food `
  -H "Authorization: Bearer <your_token_here>"
```

## 📡 API Endpoints

### UserService (via /app1)
```
POST   /app1/api/v1/users/signup/customer    # Register customer
POST   /app1/api/v1/users/signup/partner     # Register delivery partner
POST   /app1/api/v1/users/login              # Login
GET    /app1/api/v1/users                    # Get all users (protected)
GET    /app1/api/v1/users/{id}               # Get user by ID (protected)
PUT    /app1/api/v1/users/{id}               # Update user (protected)
DELETE /app1/api/v1/users/{id}               # Delete user (protected)
```

### FoodService (via /app2)
```
GET    /app2/api/v1/food                     # Get all food items
GET    /app2/api/v1/food/id?id={id}          # Get food by ID
POST   /app2/api/v1/food/register            # Add food (Admin only)
PUT    /app2/api/v1/food/{id}                # Update food (Admin only)
DELETE /app2/api/v1/food/{id}                # Delete food (Admin only)

GET    /app2/api/v1/cart                     # Get cart (Customer only)
POST   /app2/api/v1/cart/add                 # Add to cart (Customer only)

GET    /app2/api/v1/order                    # Get orders (Customer only)
POST   /app2/api/v1/order                    # Place order (Customer only)

GET    /app2/api/v1/category                 # Get categories
POST   /app2/api/v1/category                 # Add category (Admin only)
```

## 🛡️ Security Model

### Single Responsibility Principle

| Component | Responsible For | NOT Responsible For |
|-----------|----------------|---------------------|
| **UserService** | ✅ Generate JWT tokens<br>✅ Validate credentials<br>✅ User CRUD | ❌ Validate JWT<br>❌ Authorization<br>❌ Routing |
| **API Gateway** | ✅ Validate JWT<br>✅ Authorization<br>✅ Route requests | ❌ Generate JWT<br>❌ Business logic<br>❌ Database ops |
| **FoodService** | ✅ Food/Cart/Order logic<br>✅ Trust Gateway headers | ❌ Validate JWT<br>❌ Authentication<br>❌ Generate tokens |

### JWT Token Structure
```json
{
  "sub": "user@example.com",
  "userId": 123,
  "role": "CUSTOMER",
  "iat": 1699123456,
  "exp": 1699209856
}
```

### Roles & Permissions
- **CUSTOMER** - Order food, manage cart
- **ADMIN** - Manage food items, categories
- **DELIVERY_PARTNER** - View orders, update delivery status

## 🔧 Configuration

### Secret Key (must be same across services)
```properties
# All services use this key for JWT
SECRET_KEY=5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437
```

⚠️ **Production**: Use environment variables!

### Service Names (for Eureka)
```properties
# UserService
spring.application.name=USERSERVICEAPP

# FoodService
spring.application.name=FOODSERVICEAPP

# API Gateway
spring.application.name=Api-Gateway
```

## 📚 Documentation

For detailed architecture documentation, see [ARCHITECTURE.md](./ARCHITECTURE.md)

Topics covered:
- Complete authentication & authorization flow
- Service responsibilities breakdown
- Security model details
- Testing examples
- Troubleshooting guide
- Best practices

## 🧪 Testing

### Test Authentication
```powershell
# Register
$body = @{
    email = "test@example.com"
    password = "test123"
    name = "Test User"
    phno = "1234567890"
    location = "Test City"
} | ConvertTo-Json

$response = Invoke-RestMethod -Uri "http://localhost:9093/app1/api/v1/users/signup/customer" `
    -Method Post -Body $body -ContentType "application/json"

# Login
$loginBody = @{
    email = "test@example.com"
    password = "test123"
} | ConvertTo-Json

$loginResponse = Invoke-RestMethod -Uri "http://localhost:9093/app1/api/v1/users/login" `
    -Method Post -Body $loginBody -ContentType "application/json"

$token = $loginResponse.accessToken
Write-Host "Token: $token"

# Test protected endpoint
$headers = @{
    Authorization = "Bearer $token"
}

$foods = Invoke-RestMethod -Uri "http://localhost:9093/app2/api/v1/food" `
    -Method Get -Headers $headers

Write-Host "Foods: $($foods | ConvertTo-Json)"
```

## 📊 Service Health

Check Eureka dashboard to see all registered services:
http://localhost:8761

All services should show as "UP" with green status.

## 🐛 Common Issues

### 401 Unauthorized
- **Cause**: Invalid/missing/expired JWT token
- **Solution**: Login again to get fresh token (24h validity)

### 403 Forbidden
- **Cause**: Valid token but wrong role (e.g., CUSTOMER accessing ADMIN endpoint)
- **Solution**: Use correct user credentials with appropriate role

### Service Not Found
- **Cause**: Service not registered with Eureka
- **Solution**: Check service logs, verify Eureka URL in application.properties

## 🎓 Key Features

✅ **Single Responsibility Principle** - Each service has one clear purpose  
✅ **Centralized Authentication** - API Gateway validates all JWT tokens  
✅ **Stateless Architecture** - No session management, pure JWT  
✅ **Service Discovery** - Eureka for dynamic service registration  
✅ **Load Balancing** - Spring Cloud LoadBalancer  
✅ **Role-Based Access Control** - Fine-grained permissions  
✅ **Trust-Based Security** - Microservices trust Gateway headers  
✅ **Reactive Gateway** - WebFlux for high performance  

## 🛠️ Tech Stack

- **Java**: 21
- **Spring Boot**: 3.5.6
- **Spring Cloud**: 2025.0.0
- **Database**: MySQL 8.0
- **Frontend**: React + Vite
- **Service Discovery**: Netflix Eureka
- **API Gateway**: Spring Cloud Gateway
- **Security**: Spring Security + JWT
- **Build**: Maven

## 📈 Architecture Highlights

### Why This Design?

1. **API Gateway validates JWT** ✅
   - Centralized security, single source of truth
   - Microservices focus on business logic

2. **UserService generates JWT** ✅
   - Authentication domain ownership
   - Only service with password access

3. **X-User-* Headers** ✅
   - Trust model: Gateway already validated
   - Performance: No duplicate JWT parsing

4. **No JWT in Microservices** ✅
   - Single Responsibility Principle
   - Gateway is security boundary

## 🤝 Contributing

This is an educational project demonstrating microservices best practices.

## 📄 License

Educational project for learning microservices architecture.

---

**Built with ❤️ following Clean Architecture & SOLID Principles**
