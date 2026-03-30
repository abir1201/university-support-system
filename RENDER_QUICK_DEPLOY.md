# Render Deployment - Quick Guide

## Files Already Prepared
✅ `DEPLOYMENT.md` - Complete deployment guide
✅ `render.yaml` - Automated deployment configuration
✅ `backend/Procfile` - Backend run configuration
✅ `.gitignore` - Build artifacts excluded

## Quick 5-Step Deployment

### Step 1: GitHub Setup
```bash
git init
git add .
git commit -m "Initial commit - ready for deployment"
git remote add origin https://github.com/YOUR_USERNAME/oop-project-2.git
git push -u origin main
```

### Step 2: Get Groq API Key
- Go to https://console.groq.com
- Create API key
- Copy it (you'll need it for environment variables)

### Step 3: Create Render Services

**Option A: One-Click Deploy (Using render.yaml)**
1. Go to https://render.com
2. Click "New" → "Blueprint"
3. Connect GitHub repository
4. Select main/master branch
5. Add environment variables:
   - `GROQ_API_KEY`: Your Groq API key
6. Deploy!

**Option B: Manual Deploy (If Option A doesn't work)**

**Deploy Backend:**
1. https://dashboard.render.com
2. New → Web Service
3. Connect GitHub repo
4. Build: `cd backend && mvn clean package -DskipTests`
5. Start: `java -jar backend/target/university-system-1.0.jar`
6. Add `GROQ_API_KEY` env var
7. Deploy

**Deploy AI Service:**
1. New → Web Service
2. Connect GitHub repo
3. Build: `cd ai-service && pip install -r requirements.txt`
4. Start: `cd ai-service && uvicorn main:app --host 0.0.0.0 --port 8000`
5. Add `GROQ_API_KEY` env var
6. Deploy

**Deploy Frontend:**
1. New → Static Site
2. Connect GitHub repo
3. Build: `cd frontend && npm install && npm run build`
4. Publish: `frontend/build`
5. Deploy

### Step 4: Get URLs
After deployment, you'll have:
- **Backend URL**: https://university-system-backend.onrender.com
- **AI Service URL**: https://university-system-ai.onrender.com
- **Frontend URL**: https://university-system-frontend.onrender.com

### Step 5: Update Environment Variables
Set these in each Render service:

**Backend:**
```
GROQ_API_KEY = your_key
AI_SERVICE_URL = https://university-system-ai.onrender.com
```

**AI Service:**
```
GROQ_API_KEY = your_key
GROQ_MODEL = llama-3.1-8b-instant
MAX_TOKENS = 1024
TEMPERATURE = 0.7
```

**Frontend:**
```
REACT_APP_API_URL = https://university-system-backend.onrender.com
```

## Testing Deployment

After all services are live:

```bash
# Test backend
curl https://university-system-backend.onrender.com/api/queries/records

# Test AI service
curl https://university-system-ai.onrender.com/docs

# Visit frontend
https://university-system-frontend.onrender.com
```

## Costs
- **Free Plan**: $0/month (3 services included)
  - Services spin down after 15 min inactivity
  - Good for testing

- **Paid Plan**: Start at $7/month per service
  - Always running
  - Better performance

## Troubleshooting

| Issue | Solution |
|-------|----------|
| Build fails | Check logs in Render Dashboard |
| 404 errors | Verify service URLs in env vars |
| CORS errors | Check frontend API URL points to backend |
| AI not working | Verify Groq API key is valid |

## Important Files Structure

```
project/
├── backend/
│   ├── src/
│   ├── pom.xml
│   └── Procfile ✓
├── frontend/
│   ├── src/
│   ├── package.json
│   └── .env.production (optional)
├── ai-service/
│   ├── main.py
│   ├── requirements.txt
│   └── .env (add GROQ_API_KEY)
├── render.yaml ✓
├── DEPLOYMENT.md ✓
├── .gitignore ✓
└── RENDER_QUICK_DEPLOY.md ✓
```

## Success Checklist

- [ ] GitHub repository created
- [ ] Code committed and pushed
- [ ] Groq API key obtained
- [ ] Backend deployed on Render
- [ ] AI Service deployed on Render
- [ ] Frontend deployed on Render
- [ ] All environment variables set
- [ ] Services tested and working
- [ ] Shared deployment URLs with team

## Need Help?

- **Render Docs**: https://render.com/docs
- **Groq API**: https://console.groq.com/docs
- **Spring Boot**: https://spring.io/projects/spring-boot
- **React**: https://react.dev

---

**Your app is ready for production!** 🚀
