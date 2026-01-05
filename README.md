# Inventory Management REST API

A Spring Boot REST API for managing product inventory with H2 in-memory database.

## Features

- CRUD operations for products
- Search products by name
- Filter products by category
- Update product quantity
- H2 in-memory database with console access
- Input validation
- Automatic timestamp management

## Technology Stack

- Spring Boot 3.2.0
- Spring Data JPA
- H2 Database
- Java 17
- Maven

## Project Structure

```
src/
├── main/
│   ├── java/com/inventory/
│   │   ├── InventoryManagementApplication.java
│   │   ├── controller/
│   │   │   └── ProductController.java
│   │   ├── entity/
│   │   │   └── Product.java
│   │   ├── repository/
│   │   │   └── ProductRepository.java
│   │   └── service/
│   │       └── ProductService.java
│   └── resources/
│       └── application.properties
└── pom.xml
```

## Running the Application

1. Make sure you have Java 17 and Maven installed
2. Run the application:
   ```bash
   mvn spring-boot:run
   ```
3. The API will be available at `http://localhost:8080`
4. H2 Console will be available at `http://localhost:8080/h2-console`
   - JDBC URL: `jdbc:h2:mem:inventorydb`
   - Username: `sa`
   - Password: (leave empty)

## API Endpoints

### Get All Products
```
GET /api/products
```

### Get Product by ID
```
GET /api/products/{id}
```

### Create Product
```
POST /api/products
Content-Type: application/json

{
  "name": "Laptop",
  "description": "High-performance laptop",
  "price": 999.99,
  "quantity": 50,
  "sku": "LAP-001",
  "category": "Electronics"
}
```

### Update Product
```
PUT /api/products/{id}
Content-Type: application/json

{
  "name": "Laptop",
  "description": "Updated description",
  "price": 899.99,
  "quantity": 45,
  "sku": "LAP-001",
  "category": "Electronics"
}
```

### Delete Product
```
DELETE /api/products/{id}
```

### Get Products by Category
```
GET /api/products/category/{category}
```

### Search Products by Name
```
GET /api/products/search?name=laptop
```

### Update Product Quantity
```
PATCH /api/products/{id}/quantity?quantity=100
```

## Product Entity Fields

- `id`: Auto-generated unique identifier
- `name`: Product name (required, unique)
- `description`: Product description
- `price`: Product price (required, must be >= 0)
- `quantity`: Stock quantity (required, must be >= 0)
- `sku`: Stock Keeping Unit (unique)
- `category`: Product category
- `createdAt`: Auto-generated creation timestamp
- `updatedAt`: Auto-generated update timestamp

## Example API Calls

### Create a Product
```bash
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Wireless Mouse",
    "description": "Ergonomic wireless mouse",
    "price": 29.99,
    "quantity": 100,
    "sku": "MOUSE-001",
    "category": "Accessories"
  }'
```

### Get All Products
```bash
curl http://localhost:8080/api/products
```

### Update Product Quantity
```bash
curl -X PATCH "http://localhost:8080/api/products/1/quantity?quantity=75"
```

