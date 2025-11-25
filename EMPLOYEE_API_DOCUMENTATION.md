# Employee API Documentation

This document provides sample requests and responses for the Employee API endpoints.

## Base URL
```
http://localhost:8080
```

## Authentication

All API endpoints (except login) require session-based authentication. You must first login to obtain a session cookie (JSESSIONID).

---

## 1. Login

Authenticate a user and obtain a session cookie.

### Endpoint
```
POST /api/auth/login
```

### Request Headers
```
Content-Type: application/json
```

### Request Body
```json
{
  "username": "admin",
  "password": "admin123"
}
```

### Sample cURL Request
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "password": "admin123"
  }' \
  -c cookies.txt
```

### Success Response (200 OK)
```json
{
  "firstName": "System",
  "lastName": "Admin",
  "role": "SYSTEM_ADMIN",
  "employeeId": null,
  "id": "6910fe3547a984367dd713d9",
  "position": "System Administrator",
  "department": "IT",
  "message": "Login successful",
  "userId": null,
  "email": "admin@workforce.com",
  "username": "admin"
}
```

### Error Response (401 Unauthorized)
```json
{
  "error": "Invalid username or password"
}
```

### Test Credentials

| Role              | Username     | Password  |
|-------------------|--------------|-----------|
| SYSTEM_ADMIN      | admin        | admin123  |
| PROJECT_MANAGER   | pm_john      | pm123     |
| PROJECT_MANAGER   | pm_sarah     | pm123     |
| DEPARTMENT_HEAD   | dh_jane      | dh123     |
| DEPARTMENT_HEAD   | dh_michael   | dh123     |
| RESOURCE_PLANNER  | rp_bob       | rp123     |
| RESOURCE_PLANNER  | rp_lisa      | rp123     |
| EMPLOYEE          | emp_alice    | emp123    |
| EMPLOYEE          | emp_tom      | emp123    |

---

## 2. Get Employee by Employee ID

Retrieve a specific employee by their employee ID (not MongoDB ObjectId).

### Endpoint
```
GET /api/employees/{employeeId}
```

### Path Parameters
- `employeeId` (Integer): The employee ID of the employee to retrieve

### Request Headers
```
Content-Type: application/json
Cookie: JSESSIONID={session_id}
```

### Sample cURL Request
```bash
curl -X GET http://localhost:8080/api/employees/12345 \
  -H "Content-Type: application/json" \
  -b cookies.txt
```

### Success Response (200 OK)
```json
{
  "id": "6910fe3547a984367dd713da",
  "employeeId": 12345,
  "userId": "user-uuid-123",
  "remoteWorking": true,
  "username": "pm_john",
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@workforce.com",
  "department": "IT",
  "position": "Senior Project Manager",
  "role": "PROJECT_MANAGER",
  "message": null
}
```

### Error Response (404 Not Found)
No response body - HTTP status 404

### Error Response (401 Unauthorized)
```json
{
  "error": "Not authenticated"
}
```

### Authorization
Accessible to users with roles:
- SYSTEM_ADMIN
- PROJECT_MANAGER
- DEPARTMENT_HEAD
- RESOURCE_PLANNER
- EMPLOYEE

---

## 3. Update Employee by Employee ID

Update an existing employee's information using their employee ID.

### Endpoint
```
PUT /api/employees/{employeeId}
```

### Path Parameters
- `employeeId` (Integer): The employee ID of the employee to update

### Request Headers
```
Content-Type: application/json
Cookie: JSESSIONID={session_id}
```

### Request Body
```json
{
  "employeeId": 12345,
  "username": "pm_john",
  "firstName": "John Updated",
  "lastName": "Doe",
  "email": "john.doe@workforce.com",
  "department": "IT",
  "position": "Senior Project Manager",
  "role": "PROJECT_MANAGER",
  "remoteWorking": true,
  "userId": "user-uuid-123"
}
```

### Sample cURL Request
```bash
curl -X PUT http://localhost:8080/api/employees/12345 \
  -H "Content-Type: application/json" \
  -b cookies.txt \
  -d '{
    "employeeId": 12345,
    "username": "pm_john",
    "firstName": "John Updated",
    "lastName": "Doe",
    "email": "john.doe@workforce.com",
    "department": "IT",
    "position": "Senior Project Manager",
    "role": "PROJECT_MANAGER",
    "remoteWorking": true
  }'
```

### Success Response (200 OK)
```json
{
  "id": "6910fe3547a984367dd713da",
  "employeeId": 12345,
  "userId": "user-uuid-123",
  "remoteWorking": true,
  "username": "pm_john",
  "firstName": "John Updated",
  "lastName": "Doe",
  "email": "john.doe@workforce.com",
  "department": "IT",
  "position": "Senior Project Manager",
  "role": "PROJECT_MANAGER",
  "message": null
}
```

### Error Response (404 Not Found)
No response body - HTTP status 404

### Error Response (403 Forbidden)
No response body - HTTP status 403 (insufficient permissions)

### Authorization
Accessible to users with roles:
- SYSTEM_ADMIN
- DEPARTMENT_HEAD
- PROJECT_MANAGER
- RESOURCE_PLANNER

---

## 4. Get All Employees

Retrieve a list of all employees in the system.

### Endpoint
```
GET /api/employees
```

### Request Headers
```
Content-Type: application/json
Cookie: JSESSIONID={session_id}
```

### Sample cURL Request
```bash
curl -X GET http://localhost:8080/api/employees \
  -H "Content-Type: application/json" \
  -b cookies.txt
```

### Success Response (200 OK)
```json
[
  {
    "id": "6910fe3547a984367dd713d9",
    "employeeId": 1001,
    "userId": "uuid-1",
    "remoteWorking": false,
    "username": "admin",
    "firstName": "System",
    "lastName": "Admin",
    "email": "admin@workforce.com",
    "department": "IT",
    "position": "System Administrator",
    "role": "SYSTEM_ADMIN",
    "message": null
  },
  {
    "id": "6910fe3547a984367dd713da",
    "employeeId": 12345,
    "userId": "uuid-2",
    "remoteWorking": true,
    "username": "pm_john",
    "firstName": "John",
    "lastName": "Doe",
    "email": "john.doe@workforce.com",
    "department": "IT",
    "position": "Project Manager",
    "role": "PROJECT_MANAGER",
    "message": null
  }
]
```

### Authorization
Accessible to users with roles:
- SYSTEM_ADMIN
- PROJECT_MANAGER
- DEPARTMENT_HEAD
- RESOURCE_PLANNER
- EMPLOYEE

---

## 5. Create Employee

Create a new employee in the system.

### Endpoint
```
POST /api/employees
```

### Request Headers
```
Content-Type: application/json
Cookie: JSESSIONID={session_id}
```

### Request Body
```json
{
  "username": "new_employee",
  "password": "password123",
  "firstName": "Jane",
  "lastName": "Smith",
  "email": "jane.smith@workforce.com",
  "department": "Engineering",
  "position": "Software Engineer",
  "role": "EMPLOYEE",
  "employeeId": 2001,
  "userId": "custom-uuid",
  "remoteWorking": true
}
```

**Note:** If `employeeId` is not provided or is 0, it will be auto-generated. If `userId` is not provided, a UUID will be auto-generated.

### Sample cURL Request
```bash
curl -X POST http://localhost:8080/api/employees \
  -H "Content-Type: application/json" \
  -b cookies.txt \
  -d '{
    "username": "new_employee",
    "password": "password123",
    "firstName": "Jane",
    "lastName": "Smith",
    "email": "jane.smith@workforce.com",
    "department": "Engineering",
    "position": "Software Engineer",
    "role": "EMPLOYEE",
    "remoteWorking": true
  }'
```

### Success Response (201 Created)
```json
{
  "id": "692221a90ea9be09a66b5dcc",
  "employeeId": 2001,
  "userId": "e0041510-7e30-4e53-a075-b3f6f3d20757",
  "remoteWorking": true,
  "username": "new_employee",
  "firstName": "Jane",
  "lastName": "Smith",
  "email": "jane.smith@workforce.com",
  "department": "Engineering",
  "position": "Software Engineer",
  "role": "EMPLOYEE",
  "message": null
}
```

### Authorization
Accessible to users with roles:
- SYSTEM_ADMIN
- DEPARTMENT_HEAD
- PROJECT_MANAGER

---

## 6. Delete Employee

Delete an employee from the system by their MongoDB ObjectId.

### Endpoint
```
DELETE /api/employees/{id}
```

### Path Parameters
- `id` (String): The MongoDB ObjectId of the employee to delete

### Request Headers
```
Cookie: JSESSIONID={session_id}
```

### Sample cURL Request
```bash
curl -X DELETE http://localhost:8080/api/employees/6910fe3547a984367dd713da \
  -b cookies.txt
```

### Success Response (204 No Content)
No response body

### Error Response (404 Not Found)
No response body - HTTP status 404

### Authorization
Accessible to users with role:
- SYSTEM_ADMIN only

---

## 7. Get Current User

Retrieve the current logged-in user's details from the session.

### Endpoint
```
GET /api/auth/current-user
```

### Request Headers
```
Cookie: JSESSIONID={session_id}
```

### Sample cURL Request
```bash
curl -X GET http://localhost:8080/api/auth/current-user \
  -b cookies.txt
```

### Success Response (200 OK)
```json
{
  "id": "6910fe3547a984367dd713d9",
  "employeeId": 1001,
  "userId": "uuid-1",
  "username": "admin",
  "firstName": "System",
  "lastName": "Admin",
  "email": "admin@workforce.com",
  "department": "IT",
  "position": "System Administrator",
  "role": "SYSTEM_ADMIN"
}
```

### Error Response (401 Unauthorized)
```json
{
  "error": "Not authenticated"
}
```

---

## 8. Logout

Invalidate the current session and logout.

### Endpoint
```
POST /api/auth/logout
```

### Request Headers
```
Cookie: JSESSIONID={session_id}
```

### Sample cURL Request
```bash
curl -X POST http://localhost:8080/api/auth/logout \
  -b cookies.txt
```

### Success Response (200 OK)
```json
{
  "message": "Logout successful"
}
```

---

## Valid Role Values

When creating or updating employees, use one of these role values:

- `SYSTEM_ADMIN`
- `PROJECT_MANAGER`
- `DEPARTMENT_HEAD`
- `RESOURCE_PLANNER`
- `EMPLOYEE`

---

## Using Postman

### Step 1: Login
1. Create a new POST request to `http://localhost:8080/api/auth/login`
2. Set header: `Content-Type: application/json`
3. Set body (raw JSON):
   ```json
   {
     "username": "admin",
     "password": "admin123"
   }
   ```
4. In the **Tests** tab, add this script to automatically save the cookie:
   ```javascript
   pm.cookies.get("JSESSIONID");
   ```
5. Send the request

### Step 2: Use Authenticated Endpoints
1. The JSESSIONID cookie will be automatically sent with subsequent requests
2. No need to manually add the cookie - Postman handles it automatically

---

## Testing with Insomnia or Thunder Client

Similar to Postman, these clients automatically handle cookies. Just:
1. Login first using the login endpoint
2. The session cookie will be saved automatically
3. Use other endpoints normally

---

## Important Notes

1. **Employee ID vs MongoDB ID:**
   - `employeeId` (Integer): Business identifier used in GET/PUT endpoints
   - `id` (String): MongoDB ObjectId used only in DELETE endpoint

2. **Password Handling:**
   - Passwords are automatically hashed before saving
   - Password field is write-only (not returned in responses)
   - Only update password if you want to change it (optional in PUT requests)

3. **Auto-generated Fields:**
   - `employeeId`: Auto-generated if not provided or is 0
   - `userId`: Auto-generated UUID if not provided

4. **Session Management:**
   - Sessions are stored server-side
   - Use cookies to maintain session state
   - Logout to invalidate the session

---

## Common Error Codes

| Status Code | Description |
|-------------|-------------|
| 200 | Success |
| 201 | Created (for POST requests) |
| 204 | No Content (for DELETE requests) |
| 400 | Bad Request (invalid input) |
| 401 | Unauthorized (not logged in or invalid credentials) |
| 403 | Forbidden (insufficient permissions) |
| 404 | Not Found (resource doesn't exist) |

---

## Quick Start Guide

1. **Login:**
   ```bash
   curl -X POST http://localhost:8080/api/auth/login \
     -H "Content-Type: application/json" \
     -d '{"username":"admin","password":"admin123"}' \
     -c cookies.txt
   ```

2. **Get Employee:**
   ```bash
   curl -X GET http://localhost:8080/api/employees/12345 \
     -b cookies.txt
   ```

3. **Update Employee:**
   ```bash
   curl -X PUT http://localhost:8080/api/employees/12345 \
     -H "Content-Type: application/json" \
     -b cookies.txt \
     -d '{"employeeId":12345,"firstName":"Updated Name",...}'
   ```

4. **Logout:**
   ```bash
   curl -X POST http://localhost:8080/api/auth/logout \
     -b cookies.txt
   ```
