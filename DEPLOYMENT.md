# Deployment Guide - Render

This guide explains how to deploy the University Support System to Render.com

## Project Structure
```
├── backend/          (Spring Boot Java)
├── frontend/         (React)
├── ai-service/       (FastAPI Python)
└── DEPLOYMENT.md
```

## Prerequisites
1. GitHub account with the project repository
2. Render.com account (free tier available)
3. Groq API key (get from console.groq.com)

---

## Step 1: Prepare Repository

Before deploying, remove these directories (add to .gitignore):
```
backend/target/
frontend/node_modules/
ai-service/venv/
ai-service/__pycache__/
*.class
.m2/
```

Create `.gitignore` in root:
```
backend/target/
backend/.class
backend/.m2/

frontend/node_modules/
frontend/build/
frontend/.env.local

ai-service/venv/
ai-service/__pycache__/
ai-service/*.pyc
ai-service/.env

.env
.env.local
```

---

## Step 2: Create Backend Service (Render)

### 2.1 Create `backend/Procfile`
```
web: java -jar target/university-system.jar
```

### 2.2 Create `backend/render.yaml` (Optional)
```yaml
services:
  - type: web
    name: university-backend
    env: java
    buildCommand: "mvn clean package -DskipTests"
    startCommand: "java -jar target/university-system-1.0.jar"
    envVars:
      - key: GROQ_API_KEY
        scope: build
```

### 2.3 Steps:
1. Go to https://dashboard.render.com
2. Click "New" → "Web Service"
3. Connect GitHub repository
4. Select branch (main/master)
5. Set build command: `mvn clean package -DskipTests`
6. Set start command: `java -jar target/university-system.jar`
7. Add environment variables:
   - `GROQ_API_KEY`: Your Groq API key
   - `AI_SERVICE_URL`: https://your-ai-service.onrender.com
8. Choose plan (free or paid)
9. Deploy

**Note:** Backend will run on port 8080 (Render auto-assigns)

---

## Step 3: Create Frontend Service (Render)

### 3.1 Create `frontend/.env.production`
```
REACT_APP_API_URL=https://your-backend-url.onrender.com
```

### 3.2 Steps:
1. In Render dashboard, click "New" → "Static Site"
2. Connect GitHub repository
3. Set root directory: `frontend`
4. Build command: `npm install && npm run build`
5. Publish directory: `build`
6. Deploy

**Note:** Frontend will be available at `https://your-frontend.onrender.com`

---

## Step 4: Create AI Service (Render)

### 4.1 Create `ai-service/render.yaml`
```yaml
services:
  - type: web
    name: university-ai-service
    env: python
    buildCommand: "pip install -r requirements.txt"
    startCommand: "uvicorn main:app --host 0.0.0.0 --port 8000"
    envVars:
      - key: GROQ_API_KEY
        scope: build
      - key: GROQ_MODEL
        value: llama-3.1-8b-instant
      - key: MAX_TOKENS
        value: "1024"
      - key: TEMPERATURE
        value: "0.7"
```

### 4.2 Steps:
1. Click "New" → "Web Service"
2. Connect GitHub repository
3. Select `ai-service` directory
4. Python version: 3.11+
5. Build command: `pip install -r requirements.txt`
6. Start command: `uvicorn main:app --host 0.0.0.0 --port 8000`
7. Add environment variables:
   - `GROQ_API_KEY`: Your Groq API key
   - `GROQ_MODEL`: llama-3.1-8b-instant
   - `MAX_TOKENS`: 1024
   - `TEMPERATURE`: 0.7
8. Deploy

**Note:** The service URL will be something like `https://your-ai-service.onrender.com`

---

## Step 5: Configure CORS and Environment Variables

### Backend: Update `application.properties`
```properties
spring.web.cors.allowed-origins=https://your-frontend.onrender.com
server.port=8080
spring.datasource.url=jdbc:h2:mem:universitydb
spring.jpa.hibernate.ddl-auto=create-drop
```

### Frontend: Update API endpoint
In `frontend/src/services/api.js`, ensure API calls point to your backend:
```javascript
const API_URL = process.env.REACT_APP_API_URL || 'http://localhost:8080';
```

---

## Step 6: Environment Variables to Set

Set these in each Render service:

**Backend Service:**
- `GROQ_API_KEY`: Your Groq API key
- `AI_SERVICE_URL`: https://your-ai-service.onrender.com
- `SERVER_SERVLET_CONTEXT_PATH`: /

**AI Service:**
- `GROQ_API_KEY`: Your Groq API key
- `GROQ_MODEL`: llama-3.1-8b-instant
- `MAX_TOKENS`: 1024
- `TEMPERATURE`: 0.7

**Frontend:**
- `REACT_APP_API_URL`: https://your-backend.onrender.com

---

## Step 7: Deploy and Test

1. Push code to GitHub
2. Render will auto-deploy from your repository
3. Monitor deployment logs in Render dashboard
4. Once all services are deployed:
   - Frontend: https://your-frontend.onrender.com
   - Backend: https://your-backend.onrender.com
   - AI Service: https://your-ai-service.onrender.com

5. Test API connections:
   ```bash
   curl https://your-backend.onrender.com/api/queries/records
   curl https://your-ai-service.onrender.com/docs
   ```

---

## Troubleshooting

### Backend won't start
- Check if `pom.xml` is in root of backend directory
- Verify Java version (need Java 11+)
- Check logs: Render Dashboard → Logs

### Frontend not loading
- Verify `REACT_APP_API_URL` is set correctly
- Check browser console for CORS errors
- Build command should be: `npm install && npm run build`

### AI Service connection fails
- Verify Groq API key is valid
- Check firewall/CORS settings
- Ensure AI service URL is correct in backend

### Database issues
- H2 database runs in-memory (data lost on restart)
- For persistent storage, use Render's PostgreSQL add-on
- Update `application.properties` with PostgreSQL connection

---

## Optional: Add PostgreSQL Database

1. In Render, click "New" → "PostgreSQL"
2. Create database
3. Update backend `application.properties`:
   ```properties
   spring.datasource.url=jdbc:postgresql://your-db-host:5432/your-db
   spring.datasource.username=your-user
   spring.datasource.password=your-password
   spring.jpa.hibernate.ddl-auto=update
   ```

---

## Cost Estimation

**Render Free Tier:**
- 3 free web services (backend, AI service, static frontend)
- 750 compute hours/month
- Spins down after 15 minutes of inactivity
- Perfect for testing and small projects

**Render Paid:**
- Start at $7/month per service
- Full uptime (no spin down)
- Better performance

---

## Next Steps

1. Create GitHub repository
2. Commit all files
3. Follow steps 1-7 above
4. Update this document with your actual Render URLs
5. Share deployed app with stakeholders

**Good luck with your deployment!** 🚀
