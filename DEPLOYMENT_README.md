# College ERP Backend - Deployment Guide

## 📋 System Requirements

- **Java**: 17 or higher ([Download](https://www.oracle.com/java/technologies/downloads/))
- **MySQL**: 8.0 or higher ([Download](https://dev.mysql.com/downloads/mysql/))
- **RAM**: Minimum 2GB
- **Disk Space**: 500MB

---

## 🚀 Quick Start (3 Steps)

### Step 1: Setup Database

Open MySQL and run:
```sql
CREATE DATABASE college_erp;
```

### Step 2: Configure Database Connection

Create a file named `application.properties` in the same folder as the JAR:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/college_erp
spring.datasource.username=root
spring.datasource.password=YOUR_MYSQL_PASSWORD
```

### Step 3: Run Application

```bash
java -jar erp-0.0.1-SNAPSHOT.jar
```

**Application will start on:** http://localhost:8080

---

## ✅ Verify Installation

### 1. Check Application Health
Open browser: http://localhost:8080/actuator/health

**Expected Response:**
```json
{
  "status": "UP"
}
```

### 2. Test Public Endpoint
Open browser: http://localhost:8080/api/test/public

**Expected Response:**
```
Public endpoint - accessible without authentication
```

### 3. View API Documentation
Open browser: http://localhost:8080/swagger-ui.html

---

## 🔐 Default Admin Account

After first run, an admin account is created:

- **Email**: `admin@college.edu`
- **Password**: `admin123`

**⚠️ Change this password immediately in production!**

---

## 📡 API Testing

### Register New User
```bash
POST http://localhost:8080/api/auth/register
Content-Type: application/json

{
  "name": "John Doe",
  "email": "john@example.com",
  "password": "password123",
  "phoneNumber": "1234567890",
  "departmentId": 1
}
```

### Login
```bash
POST http://localhost:8080/api/auth/login
Content-Type: application/json

{
  "email": "admin@college.edu",
  "password": "admin123"
}
```

**Response includes JWT token:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "userId": 1,
  "role": "ADMIN"
}
```

### Access Protected Endpoint
```bash
GET http://localhost:8080/api/users
Authorization: Bearer YOUR_JWT_TOKEN
```

---

## ⚙️ Configuration Options

### Change Port
```bash
java -jar erp-0.0.1-SNAPSHOT.jar --server.port=8081
```

### Custom Database
```bash
java -jar erp-0.0.1-SNAPSHOT.jar \
  --spring.datasource.url=jdbc:mysql://localhost:3306/college_erp \
  --spring.datasource.username=root \
  --spring.datasource.password=yourpassword
```

### Increase Memory
```bash
java -Xmx1024m -jar erp-0.0.1-SNAPSHOT.jar
```

### Enable Debug Logging
```bash
java -jar erp-0.0.1-SNAPSHOT.jar --logging.level.root=DEBUG
```

---

## 🐛 Troubleshooting

### Problem: "Port 8080 already in use"
**Solution 1:** Stop other application using port 8080
**Solution 2:** Use different port:
```bash
java -jar erp-0.0.1-SNAPSHOT.jar --server.port=8081
```

### Problem: "Unable to connect to database"
**Checklist:**
- [ ] MySQL is running
- [ ] Database `college_erp` exists
- [ ] Username and password are correct
- [ ] MySQL is listening on port 3306

**Test MySQL Connection:**
```bash
mysql -u root -p
USE college_erp;
```

### Problem: "java: command not found"
**Solution:** Install Java 17 or add to PATH
```bash
# Windows
set PATH=%PATH%;C:\Program Files\Java\jdk-17\bin

# Linux/Mac
export PATH=$PATH:/usr/lib/jvm/java-17-openjdk/bin
```

### Problem: "Out of memory error"
**Solution:** Increase heap size:
```bash
java -Xms512m -Xmx2048m -jar erp-0.0.1-SNAPSHOT.jar
```

### Problem: "Application starts but APIs return 404"
**Check:**
1. Application started successfully (check console logs)
2. Using correct URL: http://localhost:8080/api/...
3. No proxy/firewall blocking requests

---

## 📚 Available Endpoints

### Public Endpoints (No Authentication)
- `POST /api/auth/register` - Register new user
- `POST /api/auth/login` - Login
- `GET /api/test/public` - Test endpoint

### Admin Only
- `GET /api/users` - List all users
- `POST /api/users` - Create user
- `PUT /api/users/{id}` - Update user
- `DELETE /api/users/{id}` - Delete user
- `GET /api/departments` - List departments
- `POST /api/departments` - Create department

### Faculty & Admin
- `GET /api/students` - List students
- `POST /api/students` - Create student
- `POST /api/attendance` - Mark attendance
- `POST /api/marks` - Enter marks

### Student Access
- `GET /api/attendance/my` - View own attendance
- `GET /api/marks/my` - View own marks
- `GET /api/fees/my` - View own fees

**Full API documentation:** http://localhost:8080/swagger-ui.html

---

## 🔒 Security Notes

1. **JWT Token Expiry**: 24 hours
2. **Password Encryption**: BCrypt
3. **CORS**: Configured for localhost:3000 (update for production)
4. **SQL Injection**: Protected via JPA parameterized queries

---

## 📊 Database Schema

The application automatically creates these tables:
- `users` - User authentication
- `student` - Student profiles
- `faculty` - Faculty profiles
- `department` - Departments
- `subject` - Subjects/Courses
- `enrollment` - Student enrollments
- `attendance` - Attendance records
- `exam` - Examinations
- `marks` - Student marks
- `fees` - Fee records
- `timetable` - Class schedules
- `syllabus` - Course syllabus
- `homework` - Assignments

---

## 🛠️ Development Mode

### Run with Development Profile
```bash
java -jar erp-0.0.1-SNAPSHOT.jar --spring.profiles.active=dev
```

### Enable SQL Logging
```bash
java -jar erp-0.0.1-SNAPSHOT.jar \
  --spring.jpa.show-sql=true \
  --spring.jpa.properties.hibernate.format_sql=true
```

---

## 📞 Support

### Check Application Logs
Logs are printed to console. To save to file:
```bash
java -jar erp-0.0.1-SNAPSHOT.jar > application.log 2>&1
```

### Health Check Endpoints
- **Health**: http://localhost:8080/actuator/health
- **Info**: http://localhost:8080/actuator/info

### Common Issues
1. **Database connection failed** → Check MySQL credentials
2. **Port already in use** → Change port with --server.port
3. **Out of memory** → Increase heap with -Xmx
4. **401 Unauthorized** → Check JWT token in Authorization header

---

## 🎯 Testing Workflow

1. **Start Application**
   ```bash
   java -jar erp-0.0.1-SNAPSHOT.jar
   ```

2. **Login as Admin**
   ```bash
   POST http://localhost:8080/api/auth/login
   Body: {"email": "admin@college.edu", "password": "admin123"}
   ```

3. **Copy JWT Token** from response

4. **Create Department**
   ```bash
   POST http://localhost:8080/api/departments
   Authorization: Bearer YOUR_TOKEN
   Body: {"name": "Computer Science"}
   ```

5. **Register Student**
   ```bash
   POST http://localhost:8080/api/auth/register
   Body: {
     "name": "Student Name",
     "email": "student@example.com",
     "password": "password123",
     "departmentId": 1
   }
   ```

6. **Create Student Profile**
   ```bash
   POST http://localhost:8080/api/students
   Authorization: Bearer YOUR_TOKEN
   Body: {
     "userId": 2,
     "prnNumber": "PRN001",
     "batch": "2024",
     "semester": 1
   }
   ```

---

## 📦 What's Included

- ✅ Spring Boot 3.5.5
- ✅ Spring Security with JWT
- ✅ Spring Data JPA
- ✅ MySQL Database Support
- ✅ Swagger API Documentation
- ✅ Actuator Health Checks
- ✅ Lombok for cleaner code
- ✅ Jakarta Validation
- ✅ Embedded Tomcat Server

---

## 🚀 Production Deployment

### Using Docker (Optional)
```dockerfile
FROM openjdk:17-jdk-slim
COPY erp-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar"]
```

### Using Cloud (AWS/Azure/GCP)
1. Upload JAR to cloud instance
2. Install Java 17
3. Setup MySQL (or use cloud database)
4. Run JAR with production properties
5. Configure firewall/security groups

---

## 📝 Notes

- **First Run**: Application creates database tables automatically
- **Data Persistence**: All data stored in MySQL database
- **Stateless**: Uses JWT tokens (no session storage)
- **API Format**: RESTful JSON APIs
- **Documentation**: Swagger UI available at /swagger-ui.html

---

## ✅ Success Indicators

Application is running correctly when you see:

```
  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::                (v3.5.5)

2024-XX-XX INFO  Started ErpApplication in 5.234 seconds
```

And these endpoints respond:
- ✅ http://localhost:8080/actuator/health → {"status":"UP"}
- ✅ http://localhost:8080/api/test/public → Success message
- ✅ http://localhost:8080/swagger-ui.html → API documentation

---

**For questions or issues, please contact the development team.**

**Version**: 0.0.1-SNAPSHOT  
**Last Updated**: 2024  
**License**: Educational Project
