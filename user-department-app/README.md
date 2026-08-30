# User Department App API

This project exposes a simple REST API for managing users and departments.
-------------------------------------------------------------
## Postman collection
-------------------------------------------------------------
Import the collection from:

- [postman/UserDepartmentApp.postman_collection.json](postman/UserDepartmentApp.postman_collection.json)

You can also use the local Postman import flow:

1. Open Postman.
2. Click Import.
3. Select the file above.
4. Set the environment variable `baseUrl` to `http://localhost:8080`.
-------------------------------------------------------------

## Run the application
-------------------------------------------------------------

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

The app starts on:

```text
http://localhost:8080
```

For H2 database inspection, open:

```text
http://localhost:8080/h2-console
```

## Base URL

```text
http://localhost:8080
```

## API endpoints
-------------------------------------------------------------
### 1) Get all users
-------------------------------------------------------------
Request:

```http
GET /users
```

Response: `200 OK`

```json
[
  {
    "id": 1,
    "name": "Alice Johnson",
    "departmentId": 200,
    "active": true
  },
  {
    "id": 2,
    "name": "Bob Smith",
    "departmentId": 100,
    "active": false
  }
]
```

-------------------------------------------------------------

### 2) Get user by ID
-------------------------------------------------------------
Request:

```http
GET /users/{id}
```

Example:

```http
GET /users/1
```

Response: `200 OK`

```json
{
  "id": 1,
  "name": "Alice Johnson",
  "departmentId": 200,
  "active": true
}
-------------------------------------------------------------

If the user does not exist:

```json
{
  "timestamp": "2026-08-30T12:00:00.000+00:00",
  "status": 404,
  "error": "Not Found",
  "message": "User not found with id: 999",
  "path": "/users/999"
}
```

-------------------------------------------------------------

### 3) Create user
-------------------------------------------------------------
Request:

```http
POST /users
Content-Type: application/json
```

Body:

```json
{
  "name": "Charlie Brown",
  "departmentId": 300
}
```

Response: `201 Created`

```json
{
  "id": 3,
  "name": "Charlie Brown",
  "departmentId": 300,
  "active": true
}
```

If the department ID is invalid:

```json
{
  "timestamp": "2026-08-30T12:00:00.000+00:00",
  "status": 404,
  "error": "Not Found",
  "message": "Department not found with id: 999",
  "path": "/users"
}
```

If request validation fails:

```json
{
  "timestamp": "2026-08-30T12:00:00.000+00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "path": "/users"
}
```

-------------------------------------------------------------

### 4) Toggle user active status
-------------------------------------------------------------
Request:

```http
PATCH /users/{id}/toggle-active
```

Example:

```http
PATCH /users/1/toggle-active
```

Response: `200 OK`

```json
{
  "id": 1,
  "name": "Alice Johnson",
  "departmentId": 200,
  "active": false
}
```

-------------------------------------------------------------

### 5) Get all departments
-------------------------------------------------------------
Request:

```http
GET /departments
```

Response: `200 OK`

```json
[
  {
    "id": 100,
    "name": "Administration"
  },
  {
    "id": 200,
    "name": "Engineering"
  },
  {
    "id": 300,
    "name": "Finance"
  },
  {
    "id": 400,
    "name": "Sales"
  }
]
```

-------------------------------------------------------------

### 6) Get department by ID
-------------------------------------------------------------

Request:

```http
GET /departments/{id}
```

Example:

```http
GET /departments/200
```

Response: `200 OK`

```json
{
  "id": 200,
  "name": "Engineering",
  "activeUserNames": [
    "Alice Johnson",
    "Bob Smith"
  ]
}
```

If the department does not exist:

```json
{
  "timestamp": "2026-08-30T12:00:00.000+00:00",
  "status": 404,
  "error": "Not Found",
  "message": "Department not found with id: 999",
  "path": "/departments/999"
}
```

-------------------------------------------------------------

## Seeded department data

The app seeds these departments on startup:

```text
100 -> Administration
200 -> Engineering
300 -> Finance
400 -> Sales
```
-------------------------------------------------------------
## Notes

- The app uses H2 in-memory database.
- The database resets on application restart because `ddl-auto` is set to `create-drop`.
- The H2 console is enabled in the dev profile.
-------------------------------------------------------------