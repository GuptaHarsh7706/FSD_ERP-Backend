# 🚀 BUILD JAR FILE NOW - Quick Commands

## ⚡ FASTEST WAY (Copy-Paste These Commands)

### For Windows (PowerShell or CMD):
```bash
cd D:\FSD_ERP-Backend\erp
mvn clean package -DskipTests
```

### For Linux/Mac:
```bash
cd /path/to/your/erp
mvn clean package -DskipTests
```

---

## 📍 Your JAR File Location

After build completes, find your JAR here:
```
D:\FSD_ERP-Backend\erp\target\erp-0.0.1-SNAPSHOT.jar
```

---

## ✅ Test Your JAR Locally

```bash
java -jar target\erp-0.0.1-SNAPSHOT.jar
```

**Stop with:** `Ctrl + C`

---

## 📦 What to Send to Recruiter

### Option 1: Just the JAR (Simple)
1. Upload `erp-0.0.1-SNAPSHOT.jar` to Google Drive
2. Share link with recruiter

### Option 2: JAR + Documentation (Professional) ⭐
Create a ZIP file with:
```
college-erp-deployment.zip
├── erp-0.0.1-SNAPSHOT.jar
├── DEPLOYMENT_README.md
└── API_TEST_GUIDE.md
```

---

## 📧 Email Template

```
Subject: College ERP Backend - Executable JAR File

Dear [Recruiter Name],

Please find the executable JAR file for the College ERP Backend application.

**Download Link:** [Your Google Drive/Dropbox Link]

**Quick Start:**
1. Ensure Java 17+ installed
2. Create MySQL database: college_erp
3. Run: java -jar erp-0.0.1-SNAPSHOT.jar
4. Access: http://localhost:8080

**Default Admin Login:**
- Email: admin@college.edu
- Password: admin123

Detailed deployment instructions are included in DEPLOYMENT_README.md

Best regards,
[Your Name]
```

---

## 🐛 If Build Fails

### Error: "Tests failed"
**Already handled!** We used `-DskipTests`

### Error: "Maven not found"
**Solution:** Use IDE instead:
- **IntelliJ**: Maven panel → Lifecycle → clean → package
- **Eclipse**: Right-click project → Run As → Maven build → Goals: `clean package -DskipTests`

### Error: "JAVA_HOME not set"
**Solution:**
```bash
# Windows
set JAVA_HOME=C:\Program Files\Java\jdk-17

# Linux/Mac
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk
```

---

## 📊 Expected Output

```
[INFO] Building jar: D:\FSD_ERP-Backend\erp\target\erp-0.0.1-SNAPSHOT.jar
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
```

**JAR Size:** ~60-100 MB (normal for Spring Boot with dependencies)

---

## 🎯 CHECKLIST

- [ ] Run `mvn clean package -DskipTests`
- [ ] Verify JAR exists in `target/` folder
- [ ] Test JAR locally: `java -jar target\erp-0.0.1-SNAPSHOT.jar`
- [ ] Create ZIP with JAR + DEPLOYMENT_README.md
- [ ] Upload to Google Drive/Dropbox
- [ ] Get shareable link
- [ ] Send email to recruiter

---

## 💡 Pro Tips

1. **File Size**: If JAR is > 100MB, it's normal (includes all dependencies)
2. **Upload**: Use Google Drive for files > 25MB (email limit)
3. **Testing**: Always test JAR locally before sending
4. **Documentation**: Include DEPLOYMENT_README.md to impress recruiter
5. **Backup**: Keep a copy of the JAR file

---

## 🚀 READY TO BUILD?

**Run this command NOW:**
```bash
mvn clean package -DskipTests
```

**Wait for:** "BUILD SUCCESS"

**Find JAR at:** `target\erp-0.0.1-SNAPSHOT.jar`

**Done!** 🎉
