# Fix: "mvn: command not found" Error on Render

## Problem
When deploying backend to Render, you get:
```
bash: line 1: mvn: command not found
Build failed 😞
```

## Solution ✅
Maven Wrapper is now included! It downloads Maven automatically on Render.

---

## What Changed

### New Files Added:
- `backend/.mvn/wrapper/maven-wrapper.properties`
- `backend/mvnw` (Unix script)
- `backend/mvnw.cmd` (Windows script)

### Updated Build Command:
**OLD (doesn't work):**
```
mvn clean package -DskipTests
```

**NEW (works on Render):**
```
./mvnw clean package -DskipTests
```

---

## For Manual Deployment on Render

### Backend Service Settings:

**Build Command:**
```
cd backend && ./mvnw clean package -DskipTests
```

**Start Command:**
```
java -jar backend/target/university-system-1.0.jar
```

---

## How It Works

1. Render detects `./mvnw` script
2. Script downloads Maven automatically
3. Maven builds your backend
4. JAR is created in `target/`
5. Backend starts successfully ✅

---

## Quick Steps to Fix

### 1. Commit These Files
```bash
git add backend/.mvn/
git add backend/mvnw
git add backend/mvnw.cmd
git commit -m "Add Maven Wrapper for Render deployment"
git push origin main
```

### 2. Update Render Backend Service
Go to Render Dashboard → Backend Service → Settings

**Find "Build Command"**
```
OLD: cd backend && mvn clean package -DskipTests
NEW: cd backend && ./mvnw clean package -DskipTests
```

3. **Save Changes**
4. **Redeploy** (click Redeploy button)
5. **Wait 5-10 minutes** for build ⏳
6. **Check logs** for success

---

## Testing Locally

To test Maven Wrapper locally:

```bash
cd backend

# Unix/Mac/Linux:
./mvnw clean package -DskipTests

# Windows:
mvnw.cmd clean package -DskipTests
```

---

## If Still Getting Error

1. **Clear Render Cache:**
   - Render Dashboard → Backend Service
   - Click "..." menu → "Clear Build Cache"
   - Click "Redeploy"

2. **Check Java Version:**
   - Ensure Java 11+ is available
   - Render should auto-detect from pom.xml

3. **Verify Files:**
   - Confirm `backend/mvnw` exists
   - Confirm `backend/.mvn/wrapper/` exists

---

## Files Structure

```
backend/
├── .mvn/
│   └── wrapper/
│       └── maven-wrapper.properties ✅
├── mvnw ✅
├── mvnw.cmd ✅
├── pom.xml
└── src/
```

---

## Success! 🎉

Once build succeeds:
- Backend JAR is created
- Backend starts automatically
- API responds at service URL
- Connect with frontend and AI service

No more Maven installation needed on Render!
