# Render Manual Configuration - FIXED

## ⚠️ Current Issue
AI Service build is looking for requirements.txt in root instead of ai-service/

## ✅ Solution: Update Render Settings Manually

---

## 🔧 AI Service Fix (PRIORITY)

### Go to Render Dashboard:
1. https://dashboard.render.com
2. Select **university-system-ai** service
3. Click **"Settings"** tab
4. Find **"Build Command"** field

### Replace with:
```
cd ai-service && pip install -r requirements.txt
```

### Make sure Start Command is:
```
cd ai-service && uvicorn main:app --host 0.0.0.0 --port 8000
```

### Environment Variables:
```
GROQ_API_KEY = your_groq_api_key (required!)
GROQ_MODEL = llama-3.1-8b-instant
MAX_TOKENS = 1024
TEMPERATURE = 0.7
```

### Save & Redeploy:
1. Click **"Save"**
2. Click **"Redeploy"** button (top right)
3. **Wait 5-10 minutes** ⏳

---

## ✅ Backend Service Settings

Go to **university-system-backend** service:

**Build Command:**
```
cd backend && ./mvnw clean package -DskipTests
```

**Start Command:**
```
java -jar backend/target/university-system-1.0.jar
```

**Environment Variables:**
```
GROQ_API_KEY = your_groq_api_key
AI_SERVICE_URL = https://university-system-ai.onrender.com
```

---

## ✅ Frontend Service Settings

Go to **university-system-frontend** (Static Site):

**Build Command:**
```
cd frontend && npm install && npm run build
```

**Publish Directory:**
```
frontend/build
```

---

## 🆘 If Build Still Fails

1. Click on AI Service
2. Click **"..." menu** (top right)
3. Select **"Clear Build Cache"**
4. Click **"Redeploy"**
5. Wait for fresh build

---

## ✅ Test After Fix

```bash
curl https://your-ai-service-url.onrender.com/docs
```

Should show Swagger API documentation.

---

## Summary of Changes

| Service | Build Command | Fix Applied |
|---------|---------------|-------------|
| AI Service | `pip install -r requirements.txt` | ✅ Add `cd ai-service &&` prefix |
| Backend | `mvn clean package` | ✅ Use `./mvnw` with correct path |
| Frontend | `npm install && npm run build` | ✅ Add `cd frontend &&` prefix |

**All commands should include the directory prefix!**

