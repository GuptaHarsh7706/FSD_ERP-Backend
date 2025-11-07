# 🚀 Deploy College ERP Backend on Free Hosting

## 🎯 Best Free Hosting Options for Spring Boot

### **Recommended Platforms:**
1. ✅ **Render.com** - BEST for Spring Boot (Easiest)
2. ✅ **Railway.app** - Great for beginners
3. ✅ **Fly.io** - Good performance
4. ⚠️ **Heroku** - No longer free (paid only)

---

## 🏆 OPTION 1: RENDER.COM (RECOMMENDED)

**Why Render?**
- ✅ Truly free tier (750 hours/month)
- ✅ PostgreSQL database included
- ✅ Easy deployment from GitHub
- ✅ Automatic HTTPS
- ✅ No credit card required
- ⚠️ Sleeps after 15 min inactivity (wakes on request)

### **Step-by-Step Deployment on Render**

#### **Step 1: Prepare Your Project**

Create `render.yaml` in project root:

```yaml
services:
  - type: web
    name: college-erp-backend
    env: java
    buildCommand: mvn clean package -DskipTests
    startCommand: java -jar target/erp-0.0.1-SNAPSHOT.jar
    envVars:
      - key: JAVA_VERSION
        value: 17
      - key: SPRING_PROFILES_ACTIVE
        value: prod
      - key: DATABASE_URL
        fromDatabase:
          name: college-erp-db
          property: connectionString

databases:
  - name: college-erp-db
    databaseName: college_erp
    user: college_erp_user
```

#### **Step 2: Update application.properties**

Add to `src/main/resources/application-prod.properties`:

```properties
# Render.com configuration
server.port=${PORT:8090}

# Database from Render environment variable
spring.datasource.url=${DATABASE_URL}
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false

# JWT
jwt.secret=${JWT_SECRET:mySecretKeyForCollegeERPSystemThatShouldBeVerySecureAndLong}
jwt.expiration=86400000

# Logging
logging.level.root=INFO
```

#### **Step 3: Push to GitHub**

```bash
# Initialize git (if not already)
git init

# Add files
git add .

# Commit
git commit -m "Prepare for Render deployment"

# Create GitHub repo and push
git remote add origin https://github.com/YOUR_USERNAME/college-erp-backend.git
git branch -M main
git push -u origin main
```

#### **Step 4: Deploy on Render**

1. Go to https://render.com
2. Sign up with GitHub
3. Click **"New +"** → **"Web Service"**
4. Connect your GitHub repository
5. Configure:
   - **Name:** college-erp-backend
   - **Environment:** Java
   - **Build Command:** `mvn clean package -DskipTests`
   - **Start Command:** `java -jar target/erp-0.0.1-SNAPSHOT.jar`
   - **Instance Type:** Free
6. Add Environment Variables:
   - `JAVA_VERSION` = `17`
   - `SPRING_PROFILES_ACTIVE` = `prod`
   - `JWT_SECRET` = `your-secret-key-here`
7. Click **"Create Web Service"**

#### **Step 5: Create Database**

1. In Render dashboard, click **"New +"** → **"PostgreSQL"**
2. Configure:
   - **Name:** college-erp-db
   - **Database:** college_erp
   - **User:** college_erp_user
   - **Region:** Same as web service
   - **Instance Type:** Free
3. Click **"Create Database"**
4. Copy **Internal Database URL**
5. Go to your web service → **Environment**
6. Add variable:
   - `DATABASE_URL` = [paste internal database URL]

#### **Step 6: Access Your App**

Your app will be available at:
```
https://college-erp-backend.onrender.com
```

**Test endpoints:**
- Health: https://college-erp-backend.onrender.com/actuator/health
- API: https://college-erp-backend.onrender.com/api/test/public
- Swagger: https://college-erp-backend.onrender.com/swagger-ui.html

---

## 🚂 OPTION 2: RAILWAY.APP

**Why Railway?**
- ✅ $5 free credit/month
- ✅ Very easy deployment
- ✅ PostgreSQL included
- ✅ No sleep mode
- ⚠️ Requires credit card (not charged)

### **Step-by-Step Deployment on Railway**

#### **Step 1: Install Railway CLI**

```bash
# Windows (PowerShell)
iwr https://railway.app/install.ps1 | iex

# Mac/Linux
curl -fsSL https://railway.app/install.sh | sh
```

#### **Step 2: Login to Railway**

```bash
railway login
```

#### **Step 3: Initialize Project**

```bash
cd D:\FSD_ERP-Backend\erp
railway init
```

#### **Step 4: Add PostgreSQL**

```bash
railway add postgresql
```

#### **Step 5: Deploy**

```bash
railway up
```

#### **Step 6: Set Environment Variables**

```bash
railway variables set SPRING_PROFILES_ACTIVE=prod
railway variables set JWT_SECRET=your-secret-key-here
```

#### **Step 7: Get Your URL**

```bash
railway domain
```

Your app will be at: `https://your-app.up.railway.app`

---

## ✈️ OPTION 3: FLY.IO

**Why Fly.io?**
- ✅ Good free tier
- ✅ Fast performance
- ✅ Multiple regions
- ⚠️ Requires credit card
- ⚠️ More complex setup

### **Step-by-Step Deployment on Fly.io**

#### **Step 1: Install Fly CLI**

```bash
# Windows (PowerShell)
iwr https://fly.io/install.ps1 -useb | iex

# Mac/Linux
curl -L https://fly.io/install.sh | sh
```

#### **Step 2: Login**

```bash
fly auth login
```

#### **Step 3: Create Dockerfile**

Create `Dockerfile` in project root:

```dockerfile
FROM eclipse-temurin:17-jdk-alpine AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN apk add --no-cache maven
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/target/erp-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8090
ENTRYPOINT ["java", "-jar", "app.jar"]
```

#### **Step 4: Initialize Fly App**

```bash
fly launch
```

Follow prompts:
- App name: college-erp-backend
- Region: Choose closest
- PostgreSQL: Yes
- Deploy now: Yes

#### **Step 5: Set Environment Variables**

```bash
fly secrets set SPRING_PROFILES_ACTIVE=prod
fly secrets set JWT_SECRET=your-secret-key-here
```

#### **Step 6: Deploy**

```bash
fly deploy
```

Your app will be at: `https://college-erp-backend.fly.dev`

---

## 🐳 OPTION 4: DOCKER + FREE HOSTING

### **Create Docker Image**

#### **Step 1: Create Dockerfile**

```dockerfile
FROM eclipse-temurin:17-jdk-alpine AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN apk add --no-cache maven
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/target/erp-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8090
ENV SPRING_PROFILES_ACTIVE=prod
ENTRYPOINT ["java", "-Xmx512m", "-jar", "app.jar"]
```

#### **Step 2: Create .dockerignore**

```
target/
.git/
.idea/
*.md
*.log
```

#### **Step 3: Build Docker Image**

```bash
docker build -t college-erp-backend .
```

#### **Step 4: Test Locally**

```bash
docker run -p 8090:8090 \
  -e DATABASE_URL=jdbc:postgresql://localhost:5432/college_erp \
  -e JWT_SECRET=your-secret-key \
  college-erp-backend
```

#### **Step 5: Push to Docker Hub**

```bash
# Login
docker login

# Tag image
docker tag college-erp-backend YOUR_USERNAME/college-erp-backend:latest

# Push
docker push YOUR_USERNAME/college-erp-backend:latest
```

Now you can deploy this Docker image to any platform!

---

## 📊 Platform Comparison

| Platform | Free Tier | Database | Sleep Mode | Credit Card | Difficulty |
|----------|-----------|----------|------------|-------------|------------|
| **Render** | 750 hrs/mo | PostgreSQL Free | Yes (15 min) | No | ⭐ Easy |
| **Railway** | $5 credit/mo | PostgreSQL Free | No | Yes | ⭐ Easy |
| **Fly.io** | 3 VMs free | PostgreSQL Free | No | Yes | ⭐⭐ Medium |
| **Heroku** | ❌ No longer free | - | - | Yes | ⭐ Easy |

---

## 🎯 RECOMMENDED APPROACH (Render.com)

### **Quick Start Guide**

#### **1. Prepare Files**

Create these files in your project:

**`render.yaml`:**
```yaml
services:
  - type: web
    name: college-erp-backend
    env: java
    buildCommand: mvn clean package -DskipTests
    startCommand: java -jar target/erp-0.0.1-SNAPSHOT.jar
    envVars:
      - key: JAVA_VERSION
        value: 17
      - key: PORT
        value: 8090
```

**`application-prod.properties`:**
```properties
server.port=${PORT:8090}
spring.datasource.url=${DATABASE_URL}
spring.jpa.hibernate.ddl-auto=update
jwt.secret=${JWT_SECRET}
```

#### **2. Push to GitHub**

```bash
git init
git add .
git commit -m "Deploy to Render"
git remote add origin YOUR_GITHUB_REPO_URL
git push -u origin main
```

#### **3. Deploy on Render**

1. Go to https://render.com
2. Sign up with GitHub
3. New Web Service → Connect repo
4. Configure as shown above
5. Deploy!

#### **4. Add Database**

1. New PostgreSQL database
2. Copy connection string
3. Add to web service environment variables

#### **5. Test**

Visit: `https://your-app.onrender.com/actuator/health`

---

## 🔧 Configuration for Production

### **Update CORS for Production**

In `SecurityConfig.java`:

```java
@Bean
public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOrigins(Arrays.asList(
        "http://localhost:3000",
        "https://your-frontend-app.com",
        "https://college-erp-backend.onrender.com"
    ));
    configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
    configuration.setAllowedHeaders(Arrays.asList("*"));
    configuration.setAllowCredentials(true);
    
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
}
```

### **Environment Variables Needed**

```bash
DATABASE_URL=jdbc:postgresql://host:5432/database
JWT_SECRET=your-very-secure-secret-key-here
SPRING_PROFILES_ACTIVE=prod
PORT=8090
```

---

## 🐛 Troubleshooting

### **App Not Starting**

**Check logs:**
```bash
# Render
View logs in dashboard

# Railway
railway logs

# Fly.io
fly logs
```

### **Database Connection Failed**

1. Verify DATABASE_URL is set
2. Check database is running
3. Verify connection string format:
   ```
   jdbc:postgresql://host:5432/dbname?user=username&password=password
   ```

### **Out of Memory**

Add to start command:
```bash
java -Xmx512m -jar target/erp-0.0.1-SNAPSHOT.jar
```

### **Port Issues**

Ensure you're using `${PORT}` environment variable:
```properties
server.port=${PORT:8090}
```

---

## 📱 After Deployment

### **Test Your Deployed App**

```bash
# Health check
curl https://your-app.onrender.com/actuator/health

# Test public endpoint
curl https://your-app.onrender.com/api/test/public

# Login
curl -X POST https://your-app.onrender.com/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"admin@college.edu","password":"admin123"}'
```

### **Share with Recruiter**

```
Subject: College ERP Backend - Live Demo

Dear [Recruiter Name],

The College ERP Backend is now deployed and accessible online!

**Live URL:** https://college-erp-backend.onrender.com

**API Documentation:** https://college-erp-backend.onrender.com/swagger-ui.html

**Health Check:** https://college-erp-backend.onrender.com/actuator/health

**Test Credentials:**
- Email: admin@college.edu
- Password: admin123

**Key Endpoints:**
- POST /api/auth/login - Login
- GET /api/users - List users (Admin only)
- GET /api/students - List students

**Technology Stack:**
- Spring Boot 3.5.5
- PostgreSQL Database
- JWT Authentication
- Deployed on Render.com

Note: Free tier may sleep after 15 minutes of inactivity. 
First request may take 30-60 seconds to wake up.

Best regards,
[Your Name]
```

---

## 💡 Pro Tips

1. **Keep App Awake:** Use UptimeRobot (free) to ping your app every 5 minutes
2. **Monitor Logs:** Check logs regularly for errors
3. **Use Environment Variables:** Never hardcode secrets
4. **Enable HTTPS:** All platforms provide free SSL
5. **Set Up CI/CD:** Auto-deploy on git push

---

## 🎯 Next Steps

1. ✅ Choose platform (Render recommended)
2. ✅ Create account
3. ✅ Push code to GitHub
4. ✅ Deploy application
5. ✅ Add database
6. ✅ Test endpoints
7. ✅ Share with recruiter

---

## 📞 Need Help?

**Render Support:** https://render.com/docs  
**Railway Support:** https://docs.railway.app  
**Fly.io Support:** https://fly.io/docs

---

**Ready to deploy?** Start with Render.com - it's the easiest! 🚀
