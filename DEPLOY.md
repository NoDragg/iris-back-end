# Deploy lên Render với Docker

## Các bước thực hiện

### 1. Push code lên GitHub

```bash
cd /Users/macbook/Documents/Iris/App/back-end
git init
git add .
git commit -m "Initial commit"
git remote add origin https://github.com/YOUR_USERNAME/iris-backend.git
git push -u origin main
```

### 2. Tạo Render Account

1. Đăng ký tại https://render.com
2. Connect GitHub repo

### 3. Tạo Web Service trên Render

1. **Dashboard** → **New** → **Web Service**
2. Connect GitHub repo `iris-backend`
3. Cấu hình:
   - **Name:** `iris-backend`
   - **Region:** Singapore (gần VN)
   - **Branch:** `main`
   - **Root Directory:** (để trống)
   - **Runtime:** `Docker`
   - **Instance Type:** `Free`

4. **Environment Variables** - Thêm:
   ```
   DB_HOST = gateway01.ap-southeast-1.prod.aws.tidbcloud.com
   DB_PORT = 4000
   DB_USERNAME = 2bDLcYtYgZXjyo5.root
   DB_PASSWORD = QtpUwZ6JJWlky24w
   DB_DATABASE = iris
   ```

5. Click **Create Web Service**

### 4. (Tùy chọn) Auto-deploy với CI/CD

#### Cách 1: Render Auto-Deploy
- Render tự động deploy mỗi khi có push lên main (bật trong settings)

#### Cách 2: GitHub Actions + Render Webhook
1. Trên Render Dashboard → Web Service → **Settings** → **Deploy Hooks**
2. Copy URL dạng: `https://api.render.com/deploy/srv-xxx?key=xxx`
3. Trên GitHub repo → **Settings** → **Secrets** → **New repository secret**
   - Name: `RENDER_DEPLOY_HOOK_URL`
   - Value: (URL vừa copy)

File `.github/workflows/deploy.yml` đã được cấu hình sẵn.

### 5. Lấy URL API

Sau khi deploy thành công, API sẽ có URL:
```
https://iris-backend.onrender.com
```

Test:
```bash
curl https://iris-backend.onrender.com/api/v1/members
```

## Environment Variables cần thiết

| Key | Value |
|-----|-------|
| `DB_HOST` | `gateway01.ap-southeast-1.prod.aws.tidbcloud.com` |
| `DB_PORT` | `4000` |
| `DB_USERNAME` | `2bDLcYtYgZXjyo5.root` |
| `DB_PASSWORD` | `QtpUwZ6JJWlky24w` |
| `DB_DATABASE` | `iris` |

## Cấu trúc files

```
back-end/
├── Dockerfile              # Docker image definition
├── .dockerignore          # Ignore files cho Docker
├── .github/
│   └── workflows/
│       └── deploy.yml     # GitHub Actions CI/CD
└── src/                   # Spring Boot source
```

## Troubleshooting

### Lỗi "Permission denied"
```bash
chmod +x gradlew
git update-index --chmod +x gradlew
git commit -m "fix: make gradlew executable"
git push
```

### Free tier sleeping
Render Free tier sẽ sleep sau 15 phút không có traffic. Để wake up, gọi endpoint hoặc dùng UptimeRobot monitoring.

### Logs
Xem logs trên Render Dashboard → **Logs**
