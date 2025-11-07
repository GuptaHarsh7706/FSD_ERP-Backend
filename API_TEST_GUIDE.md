# 🧪 API Testing Guide - College ERP Backend

## 📋 Quick Start Testing

### ✅ Step 1: Test Server is Running

**GET** `http://localhost:8090/`

**Expected Response:**
```json
{
  "application": "College ERP Backend",
  "documentation": "/swagger-ui.html",
  "health": "/actuator/health",
  "version": "1.0.0",
  "status": "running",
  "timestamp": "2025-10-06T21:30:00.000"
}
```

---

### ✅ Step 2: Create First Admin User

**Method:** `POST`  
**URL:** `http://localhost:8090/api/users/first-user`

**Headers:**
```
Content-Type: application/json
```

**Body:**
```json
{
  "name": "Admin User",
  "email": "admin@college.edu",
  "phoneNumber": "1234567890",
  "password": "admin12345"
}
```

**Expected Response (201 Created):**
```json
{
  "userId": 1,
  "departmentId": null,
  "name": "Admin User",
  "email": "admin@college.edu",
  "phoneNumber": "1234567890",
  "password": "$2a$10$...",
  "role": "ADMIN",
  "createdAt": "2025-10-06T21:30:00",
  "updatedAt": "2025-10-06T21:30:00"
}
```

---

### ✅ Step 3: Login to Get JWT Token

**Method:** `POST`  
**URL:** `http://localhost:8090/api/auth/login`

**Headers:**
```
Content-Type: application/json
```

**Body:**
```json
{
  "email": "admin@college.edu",
  "password": "admin12345"
}
```

**Expected Response (200 OK):**
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwicm9sZXMiOlsiQURNSU4iXSwiaWF0IjoxNzA0NTU2ODAwLCJleHAiOjE3MDQ2NDMyMDB9.xxxxx",
  "tokenType": "Bearer",
  "username": "admin@college.edu",
  "role": "ROLE_ADMIN"
}
```

**Save the `accessToken` value!** You'll need it for authenticated requests.

---

### ✅ Step 4: Test Authenticated Endpoint

**Method:** `GET`  
**URL:** `http://localhost:8090/api/users`

**Headers:**
```
Content-Type: application/json
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIi...
```

(Replace with your actual token from Step 3)

**Expected Response (200 OK):**
```json
[
  {
    "userId": 1,
    "name": "Admin User",
    "email": "admin@college.edu",
    "phoneNumber": "1234567890",
    "role": "ADMIN",
    "createdAt": "2025-10-06T21:30:00",
    "updatedAt": "2025-10-06T21:30:00"
  }
]
```

---

## 🔧 Insomnia Setup

### Import Collection:
1. Open Insomnia
2. Click **Create** → **Import From** → **File**
3. Select `insomnia-collection.json`
4. All requests will be imported!

### Manual Setup in Insomnia:

#### Request 1: Create First User
```
POST http://localhost:8090/api/users/first-user
Content-Type: application/json

{
  "name": "Admin User",
  "email": "admin@college.edu",
  "phoneNumber": "1234567890",
  "password": "admin12345"
}
```

#### Request 2: Login
```
POST http://localhost:8090/api/auth/login
Content-Type: application/json

{
  "email": "admin@college.edu",
  "password": "admin12345"
}
```

---

## 🐛 Troubleshooting

### Getting 401 Unauthorized?

**Check:**
1. ✅ Server is running on port 8090
2. ✅ URL is exactly: `http://localhost:8090/api/users/first-user`
3. ✅ Header `Content-Type: application/json` is set
4. ✅ JSON body has no extra newlines or characters
5. ✅ Application was restarted after code changes

**Test with:**
```bash
# PowerShell
Invoke-RestMethod -Uri "http://localhost:8090/api/users/first-user" -Method POST -ContentType "application/json" -Body '{"name":"Admin","email":"admin@test.com","phoneNumber":"1234567890","password":"admin12345"}'
```

### Getting 400 Bad Request?

**Check:**
- Email format is valid
- Password is at least 8 characters
- All required fields are present: name, email, password

### Getting 409 Conflict?

**Reason:** Email already exists in database

**Solution:** Use a different email or clear the database

---

## 📝 Alternative Endpoints

### Option 1: `/api/users/first-user`
- ✅ Simpler endpoint
- ✅ Always creates ADMIN
- ✅ Only works if database is empty

### Option 2: `/api/users/register`
- ✅ More flexible
- ✅ Auto-detects first user
- ✅ Requires authentication for subsequent users

**Both work the same for the first user!**

---

## 🎯 Quick Copy-Paste for Insomnia

### Create Admin User:
```json
POST http://localhost:8090/api/users/first-user

{
  "name": "Admin User",
  "email": "admin@college.edu",
  "phoneNumber": "1234567890",
  "password": "admin12345"
}
```

### Login:
```json
POST http://localhost:8090/api/auth/login

{
  "email": "admin@college.edu",
  "password": "admin12345"
}
```

---

## ✅ Success Checklist

- [ ] Server running on port 8090
- [ ] Root endpoint returns JSON
- [ ] First user created successfully
- [ ] Login returns JWT token
- [ ] Token works for authenticated endpoints

---

**Need Help?** Check the console logs for detailed error messages!
