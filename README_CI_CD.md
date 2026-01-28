# 🚀 CI/CD Setup Guide - FSocial Microservices

Hướng dẫn chi tiết để setup CI/CD pipeline cho dự án FSocial.

## 📋 Mục Lục

1. [Yêu Cầu](#yêu-cầu)
2. [Setup GitHub Secrets](#setup-github-secrets)
3. [Setup EC2 Instance](#setup-ec2-instance)
4. [Cấu Hình Docker Registry](#cấu-hình-docker-registry)
5. [Chạy Pipeline Lần Đầu](#chạy-pipeline-lần-đầu)
6. [Workflow Files](#workflow-files)
7. [Scripts](#scripts)
8. [Troubleshooting](#troubleshooting)

---

## ✅ Yêu Cầu

- GitHub repository (public hoặc private với GitHub Actions enabled)
- Docker Hub account
- AWS EC2 instance
- SSH access to EC2

---

## 🔐 Setup GitHub Secrets

Vào **Settings → Secrets and variables → Actions** trong GitHub repository và thêm các secrets sau:

### Required Secrets

```
DOCKERHUB_USERNAME=cangngo
DOCKERHUB_TOKEN=<your-dockerhub-access-token>
EC2_HOST=<your-ec2-ip-or-domain>
EC2_USER=ubuntu (hoặc ec2-user)
EC2_SSH_KEY=<private-ssh-key-content>
```

### Cách lấy Docker Hub Token

1. Đăng nhập Docker Hub
2. Vào **Account Settings → Security**
3. Click **New Access Token**
4. Đặt tên và copy token (chỉ hiện 1 lần)

### Cách lấy SSH Key

Nếu chưa có SSH key:

```bash
ssh-keygen -t rsa -b 4096 -C "your_email@example.com"
```

Copy nội dung file private key (`~/.ssh/id_rsa`) vào `EC2_SSH_KEY` secret.

**Lưu ý**: Copy toàn bộ nội dung bao gồm `-----BEGIN OPENSSH PRIVATE KEY-----` và `-----END OPENSSH PRIVATE KEY-----`

---

## 🖥️ Setup EC2 Instance

### 1. Tạo EC2 Instance

- **Instance Type**: t2.medium trở lên (đủ RAM cho nhiều containers)
- **OS**: Ubuntu 22.04 LTS hoặc Amazon Linux 2023
- **Storage**: 20GB trở lên
- **Security Group**: Mở các ports:
  - 22 (SSH)
  - 8081-8088 (services)
  - 8888 (API Gateway)

### 2. Cài Đặt Docker và Docker Compose

SSH vào EC2 và chạy script sau:

```bash
#!/bin/bash
# Update system
sudo yum update -y  # hoặc sudo apt update && sudo apt upgrade -y

# Install Docker
sudo yum install docker -y  # hoặc sudo apt install docker.io -y
sudo service docker start
sudo systemctl enable docker
sudo usermod -a -G docker $USER

# Install Docker Compose
sudo curl -L "https://github.com/docker/compose/releases/latest/download/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
sudo chmod +x /usr/local/bin/docker-compose

# Install Git
sudo yum install git -y  # hoặc sudo apt install git -y

# Install curl (for health checks)
sudo yum install curl -y  # hoặc sudo apt install curl -y

# Logout và login lại để áp dụng group changes
```

### 3. Clone Repository và Setup

```bash
cd /home/ec2-user  # hoặc /home/ubuntu
git clone <your-repo-url> fsocial
cd fsocial

# Copy docker-compose.prod.yml thành docker-compose.yml
cp docker-compose.prod.yml docker-compose.yml

# Tạo .env file với tất cả environment variables
nano .env
# Paste tất cả biến môi trường vào đây
```

### 4. Setup SSH Key trên EC2

Thêm public key vào `~/.ssh/authorized_keys`:

```bash
mkdir -p ~/.ssh
chmod 700 ~/.ssh
nano ~/.ssh/authorized_keys
# Paste public key vào đây
chmod 600 ~/.ssh/authorized_keys
```

### 5. Test SSH từ GitHub Actions

Từ local machine, test SSH:

```bash
ssh -i ~/.ssh/id_rsa ec2-user@<your-ec2-ip>
```

---

## 🐳 Cấu Hình Docker Registry

### Tạo Repositories trên Docker Hub

Tạo các repositories sau trên Docker Hub:

- `cangngo/fsocial-accountservice`
- `cangngo/fsocial-messageservice`
- `cangngo/fsocial-notificationservice`
- `cangngo/fsocial-postservice`
- `cangngo/fsocial-profileservice`
- `cangngo/fsocial-apigateway`
- `cangngo/fsocial-relationshipservice`
- `cangngo/fsocial-timelineservice`

**Lưu ý**: Đảm bảo repositories là **public** hoặc account có quyền push.

---

## 🎬 Chạy Pipeline Lần Đầu

### 1. Push Code lên GitHub

```bash
git add .
git commit -m "Add CI/CD pipeline"
git push origin main
```

### 2. Kiểm Tra Workflow

- Vào **Actions** tab trên GitHub
- Xem workflow **CI - Build and Push Docker Images** chạy
- Đợi build hoàn thành

### 3. Manual Deploy

Sau khi CI thành công, có thể deploy manual:

1. Vào **Actions** tab
2. Chọn workflow **CD - Deploy to EC2**
3. Click **Run workflow**
4. Chọn branch và version
5. Click **Run workflow**

---

## 📁 Workflow Files

### CI Workflow (`.github/workflows/ci-build-push.yml`)

- **Trigger**: Push vào main/develop, Pull request, Manual
- **Jobs**:
  - Detect changes (optimize build time)
  - Build và push Docker images
  - Security scanning với Trivy

### CD Workflow (`.github/workflows/cd-deploy.yml`)

- **Trigger**: Sau khi CI thành công, Manual
- **Jobs**:
  - Deploy lên EC2
  - Health checks
  - Rollback nếu fail

---

## 📜 Scripts

### `scripts/deploy.sh`

Script deployment chính:

```bash
./scripts/deploy.sh [version] [service]
```

**Ví dụ**:
```bash
# Deploy tất cả services với version latest
./scripts/deploy.sh latest all

# Deploy chỉ accountService với version cụ thể
./scripts/deploy.sh 1.0.0 accountService
```

### `scripts/rollback.sh`

Rollback về version trước:

```bash
./scripts/rollback.sh [backup_file]
```

### `scripts/health-check.sh`

Kiểm tra health của services:

```bash
./scripts/health-check.sh [service]
```

---

## 🔧 Cấu Hình Nâng Cao

### Environment Variables trên EC2

Tạo file `.env` trên EC2 với format:

```bash
# Service Names
ACCOUNT_SERVICE=fsocial-accountservice
MESSAGE_SERVICE=fsocial-messageservice
NOTIFICATION_SERVICE=fsocial-notificationservice
POST_SERVICE=fsocial-postservice
PROFILE_SERVICE=fsocial-profileservice
API_GATEWAY=fsocial-apigateway
RELATIONSHIP_SERVICE=fsocial-relationshipservice
TIMELINE_SERVICE=fsocial-timelineservice

# Ports
ACCOUNT_PORT=8081
MESSAGE_PORT=8082
NOTIFICATION_PORT=8087
POST_PORT=8083
PROFILE_PORT=8085
GATEWAY_PORT=8888
RELATIONSHIP_PORT=8086
TIMELINE_PORT=8084

# Database URLs
MYSQL_URL=jdbc:mysql://...
MONGODB_MESSAGE_URL=mongodb://...
MONGODB_POST_URL=mongodb://...
MONGODB_NOTIFICATION_URL=mongodb://...
MONGODB_TIMELINE_URL=mongodb://...
NEO4J_URL=bolt://...

# Redis
REDIS_URL=redis://...
REDIS_PASSWORD=...
REDIS_PORT=6379
REDIS_CONTAINER=redis
REDIS_USER=default

# RabbitMQ
RABBITMQ_HOSTNAME=...
RABBITMQ_PORT=5672
RABBITMQ_USER=...
RABBITMQ_PASSWORD=...
RABBITMQ_VHOST=/

# JWT
JWT_SECRET=...
JWT_EXPIRATION=86400000

# Other configs
PROFILE_SERVICE_URL=http://profileservice:8085
ACCOUNT_SERVICE_URL=http://accountservice:8081
POST_SERVICE_URL=http://postservice:8083
MESSAGE_SERVICE_URL=http://messageservice:8082
NOTIFICATION_SERVICE_URL=http://notificationservice:8087
FRONT_END=https://your-frontend.com
```

### Health Check Endpoints

Đảm bảo mỗi service có health check endpoint:

- Spring Boot Actuator: `/actuator/health`
- Hoặc custom: `/health`

Thêm vào `application.yml`:

```yaml
management:
  endpoints:
    web:
      exposure:
        include: health,info
  endpoint:
    health:
      show-details: always
```

---

## 🐛 Troubleshooting

### Build fails

**Lỗi**: Docker build fails

**Giải pháp**:
- Check Dockerfile syntax
- Verify dependencies trong pom.xml
- Check build context path

### Push fails

**Lỗi**: Cannot push to Docker Hub

**Giải pháp**:
- Verify `DOCKERHUB_TOKEN` secret
- Check repository name format
- Verify permissions trên Docker Hub

### SSH connection fails

**Lỗi**: Permission denied (publickey)

**Giải pháp**:
- Verify `EC2_SSH_KEY` format (phải có BEGIN/END lines)
- Check EC2 Security Group (port 22)
- Verify SSH key trên EC2 (`~/.ssh/authorized_keys`)

### Deployment fails

**Lỗi**: Services không start

**Giải pháp**:
```bash
# SSH vào EC2 và check logs
cd /home/ec2-user/fsocial
docker-compose logs

# Check specific service
docker-compose logs accountservice

# Check container status
docker-compose ps

# Check environment variables
docker-compose config
```

### Health check fails

**Lỗi**: Health check timeout

**Giải pháp**:
- Verify health endpoint exists
- Check service logs
- Increase `start_period` trong docker-compose.yml
- Check port mapping

---

## 📊 Monitoring

### View Logs

```bash
# All services
docker-compose logs -f

# Specific service
docker-compose logs -f accountservice

# Last 100 lines
docker-compose logs --tail=100 accountservice
```

### Resource Usage

```bash
docker stats
```

### Service Status

```bash
docker-compose ps
```

---

## 🔄 Workflow

### Normal Flow

1. Developer push code → GitHub
2. CI workflow triggers → Build images
3. Push images → Docker Hub
4. CD workflow triggers → Deploy to EC2
5. Health checks → Verify deployment

### Manual Deploy

1. Vào GitHub Actions
2. Chọn **CD - Deploy to EC2**
3. Click **Run workflow**
4. Chọn version và service
5. Deploy

### Rollback

1. SSH vào EC2
2. Chạy: `./scripts/rollback.sh`
3. Hoặc restore từ backup file cụ thể

---

## 📚 Tài Liệu Tham Khảo

- [GitHub Actions Documentation](https://docs.github.com/en/actions)
- [Docker Documentation](https://docs.docker.com/)
- [Docker Compose](https://docs.docker.com/compose/)
- [AWS EC2](https://docs.aws.amazon.com/ec2/)

---

## ✅ Checklist

- [ ] GitHub Secrets đã setup
- [ ] EC2 instance đã tạo và cấu hình
- [ ] Docker và Docker Compose đã cài đặt
- [ ] SSH key đã setup
- [ ] Docker Hub repositories đã tạo
- [ ] `.env` file đã tạo trên EC2
- [ ] Repository đã clone trên EC2
- [ ] Workflow files đã push lên GitHub
- [ ] CI pipeline đã test thành công
- [ ] CD pipeline đã test thành công
- [ ] Health checks đã verify
- [ ] Rollback đã test

---

**Chúc bạn thành công! 🚀**

