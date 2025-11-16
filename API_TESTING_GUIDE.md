# API Testing Guide - Session-Based Authentication

## Overview
This API uses **session-based authentication** with JSESSIONID cookies. You must first login, then use the returned JSESSIONID cookie for all subsequent API calls.

## Test Credentials
```
Admin:            username: admin      password: admin123
Project Manager:  username: pm_john    password: pm123
Employee:         username: emp_alice  password: emp123
```

---

## Testing with Postman

### Option 1: Automatic Cookie Handling (Recommended)

1. **Login Request:**
   - Method: `POST`
   - URL: `http://localhost:8080/api/auth/login`
   - Headers: `Content-Type: application/json`
   - Body (raw JSON):
     ```json
     {
       "username": "admin",
       "password": "admin123"
     }
     ```
   - Click **Send**
   - Response should be `200 OK` with user details

2. **Check Cookies:**
   - Click **Cookies** link (below the Send button)
   - Verify you see `JSESSIONID` for `localhost` domain
   - Postman should automatically save this cookie

3. **Authenticated Request:**
   - Method: `GET`
   - URL: `http://localhost:8080/api/employees`
   - **No additional headers needed** - Postman automatically sends the JSESSIONID
   - Click **Send**
   - Should return `200 OK` with employee list

### Option 2: Manual Cookie Passing (If automatic fails)

1. **Login Request** (same as above)
   - After sending, go to **Headers** tab in the response
   - Find: `Set-Cookie: JSESSIONID=XXXXXXXXXXXXXXXXX`
   - **Copy the JSESSIONID value** (example: `3A0297686ABDEA94DDBCF3FAC7C1EBBE`)

2. **Authenticated Request:**
   - Method: `GET`
   - URL: `http://localhost:8080/api/employees`
   - Go to **Headers** tab
   - Add new header:
     - **Key:** `Cookie`
     - **Value:** `JSESSIONID=3A0297686ABDEA94DDBCF3FAC7C1EBBE`
   - Click **Send**
   - Should return `200 OK`

---

## Testing with SoapUI

### Step 1: Login Request

1. Create a new REST Request
2. Configure:
   - Method: `POST`
   - Endpoint: `http://localhost:8080/api/auth/login`
   - Media Type: `application/json`
3. Add Request Body:
   ```json
   {
     "username": "admin",
     "password": "admin123"
   }
   ```
4. Click **Submit**
5. In the response, look at **Raw** tab
6. Find the header: `Set-Cookie: JSESSIONID=XXXXXXXXXX`
7. **Copy the JSESSIONID value**

### Step 2: Authenticated Request

1. Create a new REST Request
2. Configure:
   - Method: `GET`
   - Endpoint: `http://localhost:8080/api/employees`
3. Go to **Headers** tab
4. Add header:
   - Name: `Cookie`
   - Value: `JSESSIONID=XXXXXXXXXX` (paste the value from login)
5. Click **Submit**
6. Should return employee list

---

## Testing with cURL (Command Line)

### Login and Save Cookie:
```bash
curl -i -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}' \
  -c cookies.txt
```

### Use Cookie in Subsequent Requests:
```bash
curl -X GET http://localhost:8080/api/employees \
  -b cookies.txt
```

### Or Manually Pass Cookie:
```bash
# First, extract JSESSIONID from login response
curl -i -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}'

# Copy the JSESSIONID value, then use it:
curl -X GET http://localhost:8080/api/employees \
  -H "Cookie: JSESSIONID=YOUR_SESSION_ID_HERE"
```

---

## Common Issues

### Issue 1: Getting 403 Forbidden
**Cause:** JSESSIONID cookie is not being sent or is invalid

**Solution:**
- Verify the JSESSIONID was copied correctly
- Check that the cookie hasn't expired
- Make sure you're using the Cookie header: `Cookie: JSESSIONID=XXX`
- Try logging in again to get a fresh session

### Issue 2: Getting 401 Unauthorized on Login
**Cause:** Wrong credentials

**Solution:** Check username/password against the test credentials above

### Issue 3: Postman not saving cookies
**Cause:** Cookie management disabled

**Solution:**
1. Go to Postman Settings (gear icon)
2. Ensure "Automatically follow redirects" is enabled
3. Check that cookies are enabled for the domain
4. Try the manual method (Option 2) instead

---

## API Endpoints

### Public Endpoints (No Authentication)
- `POST /api/auth/login` - Login
- `GET /swagger-ui.html` - API Documentation

### Protected Endpoints (Requires JSESSIONID)
- `GET /api/auth/current-user` - Get current user info
- `POST /api/auth/logout` - Logout
- `GET /api/employees` - Get all employees
- `GET /api/employees/{id}` - Get employee by ID
- `POST /api/employees` - Create employee (Admin/PM only)
- `PUT /api/employees/{id}` - Update employee
- `DELETE /api/employees/{id}` - Delete employee (Admin only)

---

## Session Behavior

- **Session Duration:** Session expires after inactivity
- **Max Sessions:** 1 concurrent session per user
- **Cookie Attributes:**
  - `Path=/` (valid for all paths)
  - `HttpOnly` (not accessible via JavaScript)
  - No `SameSite` attribute (allows cross-domain testing)

---

## Need Help?

If you're still experiencing issues:
1. Check that the backend is running on `http://localhost:8080`
2. Verify you can access Swagger UI at `http://localhost:8080/swagger-ui.html`
3. Check that MongoDB is connected (look for errors in backend logs)
4. Try using cURL to isolate if it's a tool-specific issue
