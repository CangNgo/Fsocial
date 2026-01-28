# 🚀 Kế Hoạch CI/CD cho FSocial Microservices

## 📋 Tổng Quan Dự Án

- **Kiến trúc**: Microservices (Monorepo)
- **Services**: 8 services (accountService, messageService, notificationService, postService, profileService, apigateway, relationshipService, timelineService)
- **Công nghệ**: Spring Boot, Docker, Docker Compose
- **Registry**: Docker Hub (cangngo/fsocial-*)
- **Deployment**: AWS EC2

---

## 🎯 Mục Tiêu CI/CD

1. **Tự động hóa build và test** khi có code mới
2. **Build Docker images** cho từng service
3. **Push images** lên Docker Registry
4. **Deploy tự động** lên AWS EC2
5. **Rollback** khi có lỗi
6. **Zero-downtime deployment**

---

## 🛠️ Giải Pháp Đề Xuất

### **Option 1: GitHub Actions (Khuyến nghị)**
- ✅ Miễn phí cho public repos
- ✅ Tích hợp sẵn với GitHub
- ✅ Dễ setup và maintain
- ✅ Hỗ trợ matrix strategy cho multi-service

### **Option 2: GitLab CI/CD**
- ✅ Miễn phí và mạnh mẽ
- ✅ Tích hợp Docker Registry
- ✅ Runner có thể self-hosted

### **Option 3: Jenkins**
- ✅ Tự host, kiểm soát hoàn toàn
- ⚠️ Cần server riêng
- ⚠️ Setup phức tạp hơn

**→ Chọn GitHub Actions vì đơn giản và phù hợp nhất**

---

## 📚 Kiến Thức Cần Học

### 1. **GitHub Actions Fundamentals**
- Workflow syntax (YAML)
- Events (push, pull_request, workflow_dispatch)
- Jobs và Steps
- Matrix strategy
- Secrets và Environment variables
- Actions marketplace

**Tài liệu**: https://docs.github.com/en/actions

### 2. **Docker Advanced**
- Multi-stage builds
- BuildKit
- Image tagging strategies
- Docker layer caching
- Image scanning

**Tài liệu**: https://docs.docker.com/

### 3. **AWS EC2 & Deployment**
- EC2 instance management
- SSH key management
- Security Groups
- EC2 User Data scripts
- AWS Systems Manager (SSM) - optional

**Tài liệu**: https://docs.aws.amazon.com/ec2/

### 4. **Docker Compose Production**
- Environment variables
- Health checks
- Restart policies
- Network configuration
- Volume management

**Tài liệu**: https://docs.docker.com/compose/

### 5. **CI/CD Best Practices**
- Branching strategy (Git Flow, GitHub Flow)
- Semantic versioning
- Blue-Green deployment
- Canary deployment
- Rollback strategies

---

## 🏗️ Kiến Trúc CI/CD Pipeline

```
┌─────────────────┐
│  Developer      │
│  Push Code      │
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│  GitHub Actions │
│  (CI Pipeline)  │
└────────┬────────┘
         │
         ├──► Checkout Code
         ├──► Run Tests
         ├──► Build Docker Images
         ├──► Scan Images (Security)
         ├──► Tag Images
         └──► Push to Docker Hub
         │
         ▼
┌─────────────────┐
│  Docker Hub     │
│  (Registry)     │
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│  GitHub Actions │
│  (CD Pipeline)  │
└────────┬────────┘
         │
         ├──► SSH to EC2
         ├──► Pull Latest Images
         ├──► Update docker-compose.yml
         ├──► Deploy Services
         └──► Health Check
         │
         ▼
┌─────────────────┐
│  AWS EC2        │
│  (Production)   │
└─────────────────┘
```

---

## 📝 Chi Tiết Từng Bước

### **PHASE 1: Setup Cơ Bản (Tuần 1)**

#### **Bước 1.1: Chuẩn bị Docker Registry**
- [ ] Tạo Docker Hub account (nếu chưa có)
- [ ] Tạo organization/repository cho từng service
- [ ] Setup Docker Hub access token
- [ ] Lưu credentials vào GitHub Secrets

**GitHub Secrets cần tạo:**
```
DOCKERHUB_USERNAME=cangngo
DOCKERHUB_TOKEN=<your-dockerhub-token>
EC2_HOST=<your-ec2-ip-or-domain>
EC2_USER=ubuntu (hoặc ec2-user)
EC2_SSH_KEY=<private-key-content>
```

#### **Bước 1.2: Setup AWS EC2**
- [ ] Tạo EC2 instance (t2.medium trở lên)
- [ ] Cài đặt Docker và Docker Compose
- [ ] Cấu hình Security Group (mở ports cần thiết)
- [ ] Setup SSH key pair
- [ ] Tạo `.env` file trên EC2 với tất cả environment variables

**Script cài đặt trên EC2:**
```bash
#!/bin/bash
# Update system
sudo yum update -y

# Install Docker
sudo yum install docker -y
sudo service docker start
sudo usermod -a -G docker ec2-user

# Install Docker Compose
sudo curl -L "https://github.com/docker/compose/releases/latest/download/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
sudo chmod +x /usr/local/bin/docker-compose

# Install Git
sudo yum install git -y
```

#### **Bước 1.3: Tạo GitHub Workflow Files**
- [ ] Tạo folder `.github/workflows/`
- [ ] Tạo workflow cho CI (build và push images)
- [ ] Tạo workflow cho CD (deploy lên EC2)

---

### **PHASE 2: CI Pipeline - Build & Push (Tuần 2)**

#### **Bước 2.1: Workflow Trigger**
- Trigger khi:
  - Push vào `main` branch
  - Pull request vào `main`
  - Manual trigger (workflow_dispatch)

#### **Bước 2.2: Build Matrix Strategy**
- Build song song tất cả services
- Sử dụng matrix để tối ưu thời gian

#### **Bước 2.3: Build Docker Images**
- Build với Docker BuildKit
- Tag images với:
  - `latest` (cho main branch)
  - `{version}` (semantic versioning)
  - `{sha}` (commit hash)

#### **Bước 2.4: Push to Docker Hub**
- Push images lên registry
- Sử dụng Docker Hub token authentication

---

### **PHASE 3: CD Pipeline - Deploy (Tuần 3)**

#### **Bước 3.1: Deployment Strategy**
- **Blue-Green Deployment** (khuyến nghị)
- Hoặc **Rolling Update** với Docker Compose

#### **Bước 3.2: SSH vào EC2**
- Sử dụng SSH action hoặc script
- Authenticate bằng SSH private key

#### **Bước 3.3: Pull Latest Images**
- Pull images mới từ Docker Hub
- Verify images đã pull thành công

#### **Bước 3.4: Update và Deploy**
- Update docker-compose.yml với version mới
- Stop containers cũ
- Start containers mới
- Health check

#### **Bước 3.5: Rollback Mechanism**
- Lưu version cũ
- Script rollback khi có lỗi

---

### **PHASE 4: Testing & Security (Tuần 4)**

#### **Bước 4.1: Unit Tests**
- Chạy tests trước khi build
- Fail build nếu tests fail

#### **Bước 4.2: Integration Tests**
- Test services với nhau
- Sử dụng Docker Compose để test local

#### **Bước 4.3: Security Scanning**
- Scan Docker images với Trivy hoặc Snyk
- Fail build nếu có critical vulnerabilities

#### **Bước 4.4: Code Quality**
- SonarQube hoặc CodeQL
- Linting và formatting checks

---

### **PHASE 5: Monitoring & Notifications (Tuần 5)**

#### **Bước 5.1: Health Checks**
- Health check endpoints cho mỗi service
- Verify sau khi deploy

#### **Bước 5.2: Notifications**
- Slack/Discord notifications
- Email notifications cho deploy status

#### **Bước 5.3: Logging**
- Centralized logging (ELK stack hoặc CloudWatch)
- Log aggregation từ tất cả services

---

## 🔄 Workflow Chi Tiết

### **CI Workflow (Build & Push)**

```yaml
name: CI - Build and Push Images

on:
  push:
    branches: [main, develop]
  pull_request:
    branches: [main]
  workflow_dispatch:

jobs:
  build-and-push:
    runs-on: ubuntu-latest
    strategy:
      matrix:
        service:
          - accountService
          - messageService
          - notificationService
          - postService
          - profileService
          - apigateway
          - relationshipService
          - timelineService
    steps:
      - uses: actions/checkout@v4
      
      - name: Set up Docker Buildx
        uses: docker/setup-buildx-action@v3
      
      - name: Login to Docker Hub
        uses: docker/login-action@v3
        with:
          username: ${{ secrets.DOCKERHUB_USERNAME }}
          password: ${{ secrets.DOCKERHUB_TOKEN }}
      
      - name: Extract metadata
        id: meta
        uses: docker/metadata-action@v5
        with:
          images: cangngo/fsocial-${{ matrix.service }}
          tags: |
            type=ref,event=branch
            type=sha,prefix={{branch}}-
            type=semver,pattern={{version}}
            type=semver,pattern={{major}}.{{minor}}
      
      - name: Build and push
        uses: docker/build-push-action@v5
        with:
          context: ./${{ matrix.service }}
          file: ./${{ matrix.service }}/Dockerfile
          push: true
          tags: ${{ steps.meta.outputs.tags }}
          labels: ${{ steps.meta.outputs.labels }}
          cache-from: type=registry,ref=cangngo/fsocial-${{ matrix.service }}:buildcache
          cache-to: type=registry,ref=cangngo/fsocial-${{ matrix.service }}:buildcache,mode=max
```

### **CD Workflow (Deploy)**

```yaml
name: CD - Deploy to EC2

on:
  workflow_run:
    workflows: ["CI - Build and Push Images"]
    types:
      - completed
  workflow_dispatch:

jobs:
  deploy:
    runs-on: ubuntu-latest
    if: ${{ github.event.workflow_run.conclusion == 'success' || github.event_name == 'workflow_dispatch' }}
    steps:
      - uses: actions/checkout@v4
      
      - name: Deploy to EC2
        uses: appleboy/ssh-action@v1.0.0
        with:
          host: ${{ secrets.EC2_HOST }}
          username: ${{ secrets.EC2_USER }}
          key: ${{ secrets.EC2_SSH_KEY }}
          script: |
            cd /home/ec2-user/fsocial
            git pull origin main
            docker-compose pull
            docker-compose up -d
            docker-compose ps
```

---

## 📦 File Structure

```
.github/
└── workflows/
    ├── ci-build-push.yml      # CI pipeline
    ├── cd-deploy.yml          # CD pipeline
    └── security-scan.yml      # Security scanning

scripts/
├── deploy.sh                  # Deployment script
├── rollback.sh                # Rollback script
└── health-check.sh            # Health check script

docker-compose.prod.yml        # Production compose file
.env.example                   # Environment variables template
```

---

## 🎓 Learning Path

### **Tuần 1-2: GitHub Actions Basics**
1. Đọc tài liệu GitHub Actions
2. Tạo workflow đơn giản đầu tiên
3. Hiểu về jobs, steps, actions
4. Thực hành với matrix strategy

### **Tuần 3: Docker Advanced**
1. Multi-stage builds
2. BuildKit và caching
3. Image optimization
4. Security best practices

### **Tuần 4: AWS EC2 & SSH**
1. EC2 instance management
2. SSH và key management
3. Security groups
4. User data scripts

### **Tuần 5: CI/CD Patterns**
1. Blue-Green deployment
2. Canary deployment
3. Rollback strategies
4. Monitoring và alerting

---

## ✅ Checklist Triển Khai

### **Setup Ban Đầu**
- [ ] Tạo Docker Hub account và repositories
- [ ] Setup GitHub Secrets
- [ ] Tạo và cấu hình EC2 instance
- [ ] Cài đặt Docker và Docker Compose trên EC2
- [ ] Tạo SSH key pair và lưu vào GitHub Secrets
- [ ] Tạo `.env` file trên EC2

### **CI Pipeline**
- [ ] Tạo workflow file cho CI
- [ ] Test build images locally
- [ ] Test push images lên Docker Hub
- [ ] Setup matrix strategy
- [ ] Add caching cho Docker builds
- [ ] Test với pull request

### **CD Pipeline**
- [ ] Tạo workflow file cho CD
- [ ] Test SSH connection từ GitHub Actions
- [ ] Test pull images trên EC2
- [ ] Test docker-compose up
- [ ] Setup health checks
- [ ] Test rollback mechanism

### **Testing & Security**
- [ ] Add unit tests vào CI
- [ ] Add integration tests
- [ ] Setup security scanning (Trivy)
- [ ] Setup code quality checks
- [ ] Test fail scenarios

### **Monitoring**
- [ ] Setup health check endpoints
- [ ] Add notifications (Slack/Discord)
- [ ] Setup logging
- [ ] Monitor deployment metrics

---

## 🚨 Troubleshooting

### **Common Issues**

1. **Docker build fails**
   - Check Dockerfile syntax
   - Verify dependencies
   - Check build context

2. **Push to Docker Hub fails**
   - Verify credentials
   - Check image name format
   - Verify permissions

3. **SSH connection fails**
   - Check SSH key format
   - Verify EC2 security group
   - Check EC2 instance status

4. **Deployment fails**
   - Check docker-compose syntax
   - Verify environment variables
   - Check service dependencies
   - Review logs: `docker-compose logs`

---

## 📚 Tài Liệu Tham Khảo

1. **GitHub Actions**: https://docs.github.com/en/actions
2. **Docker Documentation**: https://docs.docker.com/
3. **Docker Compose**: https://docs.docker.com/compose/
4. **AWS EC2**: https://docs.aws.amazon.com/ec2/
5. **CI/CD Best Practices**: https://www.docker.com/blog/ci-cd-best-practices/

---

## 🎯 Next Steps

1. Bắt đầu với Phase 1 - Setup cơ bản
2. Test từng bước một cách cẩn thận
3. Document mọi thay đổi
4. Review và optimize pipeline
5. Scale và improve dần dần

---

**Chúc bạn thành công với CI/CD pipeline! 🚀**

