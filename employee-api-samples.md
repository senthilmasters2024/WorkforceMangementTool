# Employee API Sample Requests

## Create Employee (POST)

### Endpoint
```
POST /api/employees
```

### Headers
```
Content-Type: application/json
```

### Sample Request Body - System Admin
```json
{
  "employeeId": 1001,
  "username": "john_admin",
  "password": "SecurePass123!",
  "firstName": "John",
  "lastName": "Smith",
  "email": "john.smith@workforce.com",
  "department": "IT",
  "position": "System Administrator",
  "role": "SYSTEM_ADMIN",
  "remoteWorking": false
}
```

### Sample Request Body - Project Manager
```json
{
  "employeeId": 2001,
  "username": "sarah_pm",
  "password": "SecurePass123!",
  "firstName": "Sarah",
  "lastName": "Johnson",
  "email": "sarah.johnson@workforce.com",
  "department": "Engineering",
  "position": "Senior Project Manager",
  "role": "PROJECT_MANAGER",
  "remoteWorking": true
}
```

### Sample Request Body - Department Head
```json
{
  "employeeId": 3001,
  "username": "mike_head",
  "password": "SecurePass123!",
  "firstName": "Mike",
  "lastName": "Williams",
  "email": "mike.williams@workforce.com",
  "department": "Finance",
  "position": "Finance Head",
  "role": "DEPARTMENT_HEAD",
  "remoteWorking": false
}
```

### Sample Request Body - Resource Planner
```json
{
  "employeeId": 4001,
  "username": "lisa_planner",
  "password": "SecurePass123!",
  "firstName": "Lisa",
  "lastName": "Brown",
  "email": "lisa.brown@workforce.com",
  "department": "HR",
  "position": "Resource Planning Manager",
  "role": "RESOURCE_PLANNER",
  "remoteWorking": true
}
```

### Sample Request Body - Employee
```json
{
  "employeeId": 5001,
  "username": "david_dev",
  "password": "SecurePass123!",
  "firstName": "David",
  "lastName": "Martinez",
  "email": "david.martinez@workforce.com",
  "department": "IT",
  "position": "Full Stack Developer",
  "role": "EMPLOYEE",
  "remoteWorking": true
}
```

### Response (201 Created)
```json
{
  "id": "507f1f77bcf86cd799439011",
  "employeeId": 5001,
  "userId": "a1b2c3d4-e5f6-7890-g1h2-i3j4k5l6m7n8",
  "username": "david_dev",
  "firstName": "David",
  "lastName": "Martinez",
  "email": "david.martinez@workforce.com",
  "department": "IT",
  "position": "Full Stack Developer",
  "role": "EMPLOYEE",
  "remoteWorking": true
}
```

### Notes for Create Employee
- **userId** is automatically generated as UUID - do NOT include it in request
- **password** is required and will be encrypted before saving
- **role** must be one of: `SYSTEM_ADMIN`, `PROJECT_MANAGER`, `DEPARTMENT_HEAD`, `RESOURCE_PLANNER`, `EMPLOYEE`
- **username** and **email** must be unique
- **remoteWorking** is optional (defaults to false if not provided)
- **employeeId** is optional (can be 0 or omitted)

---

## Update Employee (PUT)

### Endpoint
```
PUT /api/employees/{id}
```

### Headers
```
Content-Type: application/json
```

### Sample Request Body - Update Basic Info
```json
{
  "employeeId": 5001,
  "userId": "a1b2c3d4-e5f6-7890-g1h2-i3j4k5l6m7n8",
  "username": "david_dev",
  "firstName": "David",
  "lastName": "Martinez-Garcia",
  "email": "david.garcia@workforce.com",
  "department": "IT",
  "position": "Senior Full Stack Developer",
  "role": "EMPLOYEE",
  "remoteWorking": true
}
```

### Sample Request Body - Update with Password Change
```json
{
  "employeeId": 5001,
  "userId": "a1b2c3d4-e5f6-7890-g1h2-i3j4k5l6m7n8",
  "username": "david_dev",
  "password": "NewSecurePass456!",
  "firstName": "David",
  "lastName": "Martinez",
  "email": "david.martinez@workforce.com",
  "department": "IT",
  "position": "Full Stack Developer",
  "role": "EMPLOYEE",
  "remoteWorking": true
}
```

### Sample Request Body - Promote Employee to Project Manager
```json
{
  "employeeId": 5001,
  "userId": "a1b2c3d4-e5f6-7890-g1h2-i3j4k5l6m7n8",
  "username": "david_dev",
  "firstName": "David",
  "lastName": "Martinez",
  "email": "david.martinez@workforce.com",
  "department": "IT",
  "position": "Project Manager",
  "role": "PROJECT_MANAGER",
  "remoteWorking": false
}
```

### Sample Request Body - Change Department
```json
{
  "employeeId": 5001,
  "userId": "a1b2c3d4-e5f6-7890-g1h2-i3j4k5l6m7n8",
  "username": "david_dev",
  "firstName": "David",
  "lastName": "Martinez",
  "email": "david.martinez@workforce.com",
  "department": "Engineering",
  "position": "Software Architect",
  "role": "EMPLOYEE",
  "remoteWorking": true
}
```

### Response (200 OK)
```json
{
  "id": "507f1f77bcf86cd799439011",
  "employeeId": 5001,
  "userId": "a1b2c3d4-e5f6-7890-g1h2-i3j4k5l6m7n8",
  "username": "david_dev",
  "firstName": "David",
  "lastName": "Martinez-Garcia",
  "email": "david.garcia@workforce.com",
  "department": "IT",
  "position": "Senior Full Stack Developer",
  "role": "EMPLOYEE",
  "remoteWorking": true
}
```

### Notes for Update Employee
- All fields are required in the request body
- **userId** should be included in the request (use the existing UUID)
- **password** is optional - only include it if you want to change the password
- If **password** is omitted or empty, the existing password will be retained
- **username** and **email** must remain unique across all employees
- The **id** in the URL path is the MongoDB document ID, not the employeeId

---

## Available Roles

| Role Value | Description |
|------------|-------------|
| `SYSTEM_ADMIN` | Full system access and administration |
| `PROJECT_MANAGER` | Can create and manage projects |
| `DEPARTMENT_HEAD` | Can manage department employees |
| `RESOURCE_PLANNER` | Can plan and allocate resources |
| `EMPLOYEE` | Standard employee access |

---

## Common Error Responses

### 400 Bad Request - Invalid Input
```json
{
  "error": "Invalid input",
  "message": "Username already exists"
}
```

### 403 Forbidden - Access Denied
```json
{
  "error": "Access denied",
  "message": "You do not have permission to perform this action"
}
```

### 404 Not Found - Employee Not Found
```json
{
  "error": "Employee not found",
  "message": "No employee exists with the given ID"
}
```

### 500 Internal Server Error
```json
{
  "error": "Internal server error",
  "message": "An unexpected error occurred"
}
```

---

## Authorization Requirements

### Create Employee
Requires one of the following roles:
- `SYSTEM_ADMIN`
- `DEPARTMENT_HEAD`
- `PROJECT_MANAGER`

### Update Employee
Requires one of the following roles:
- `SYSTEM_ADMIN`
- `DEPARTMENT_HEAD`
- `PROJECT_MANAGER`
- `RESOURCE_PLANNER`

### Delete Employee
Requires:
- `SYSTEM_ADMIN` only

---

## Testing with cURL

### Create Employee
```bash
curl -X POST http://localhost:8080/api/employees \
  -H "Content-Type: application/json" \
  -d '{
    "employeeId": 5001,
    "username": "david_dev",
    "password": "SecurePass123!",
    "firstName": "David",
    "lastName": "Martinez",
    "email": "david.martinez@workforce.com",
    "department": "IT",
    "position": "Full Stack Developer",
    "role": "EMPLOYEE",
    "remoteWorking": true
  }'
```

### Update Employee
```bash
curl -X PUT http://localhost:8080/api/employees/507f1f77bcf86cd799439011 \
  -H "Content-Type: application/json" \
  -d '{
    "employeeId": 5001,
    "userId": "a1b2c3d4-e5f6-7890-g1h2-i3j4k5l6m7n8",
    "username": "david_dev",
    "firstName": "David",
    "lastName": "Martinez-Garcia",
    "email": "david.garcia@workforce.com",
    "department": "IT",
    "position": "Senior Full Stack Developer",
    "role": "EMPLOYEE",
    "remoteWorking": true
  }'
```

### Get Employee by ID
```bash
curl -X GET http://localhost:8080/api/employees/507f1f77bcf86cd799439011
```

### Get All Employees
```bash
curl -X GET http://localhost:8080/api/employees
```

### Delete Employee
```bash
curl -X DELETE http://localhost:8080/api/employees/507f1f77bcf86cd799439011
```
