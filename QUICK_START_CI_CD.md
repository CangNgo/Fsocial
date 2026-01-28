# ⚡ Quick Start CI/CD - FSocial

Hướng dẫn nhanh để setup CI/CD trong 30 phút.

## 🎯 Mục Tiêu

Tự động build Docker images và deploy lên AWS EC2 khi push code lên GitHub.

---

## 📋 Checklist Nhanh

### Bước 1: GitHub Secrets (5 phút)

Vào **Settings → Secrets and variables → Actions**, thêm:

```
DOCKERHUB_USERNAME=cangngo
DOCKERHUB_TOKEN=<token-từ-docker-hub>
EC2_HOST=<ip-ec2>
EC2_USER=ec2-user
EC2_SSH_KEY=<nội-dung-private-key>
```

**Lấy Docker Hub Token:**
1. Docker Hub → Account Settings → Security
2. New Access Token → Copy token

**Lấy SSH Key:**
```bash
cat ~/.ssh/id_rsa  # Copy toàn bộ nội dung
```

---

### Bước 2: Setup EC2 (10 phút)

SSH vào EC2 và chạy:

```bash
# Chạy setup script
curl -fsSL https://raw.githubusercontent.com/your-repo/main/scripts/setup-ec2.sh | bash

# Logout và login lại
exit
# SSH lại vào EC2

# Clone repo
cd /home/ec2-user
git clone <your-repo-url> fsocial
cd fsocial

# Copy production compose file
cp docker-compose.prod.yml docker-compose.yml

# Tạo .env file
cp .env.example .env
nano .env  # Điền tất cả giá trị
```

---

### Bước 3: Setup SSH Key trên EC2 (5 phút)

```bash
# Tạo .ssh folder nếu chưa có
mkdir -p ~/.ssh
chmod 700 ~/.ssh

# Thêm public key vào authorized_keys
echo "<your-public-key>" >> ~/.ssh/authorized_keys
chmod 600 ~/.ssh/authorized_keys
```

**Lấy public key:**
```bash
cat ~/.ssh/id_rsa.pub
```

---

### Bước 4: Tạo Docker Hub Repositories (5 phút)

Tạo các repositories sau trên Docker Hub (public):

- `cangngo/fsocial-accountservice`
- `cangngo/fsocial-messageservice`
- `cangngo/fsocial-notificationservice`
- `cangngo/fsocial-postservice`
- `cangngo/fsocial-profileservice`
- `cangngo/fsocial-apigateway`
- `cangngo/fsocial-relationshipservice`
- `cangngo/fsocial-timelineservice`

---

### Bước 5: Test Pipeline (5 phút)

```bash
# Push code lên GitHub
git add .
git commit -m "Add CI/CD pipeline"
git push origin main

# Vào GitHub → Actions tab
# Xem workflow chạy
```

---

## ✅ Verify

### CI Pipeline
- [ ] Workflow chạy thành công
- [ ] Images được build
- [ ] Images được push lên Docker Hub

### CD Pipeline
- [ ] Deploy workflow trigger sau CI
- [ ] SSH vào EC2 thành công
- [ ] Images được pull
- [ ] Services được start
- [ ] Health checks pass

---

## 🚀 Deploy Manual

Nếu muốn deploy manual:

1. Vào **GitHub → Actions**
2. Chọn **CD - Deploy to EC2**
3. Click **Run workflow**
4. Chọn version (hoặc để trống = latest)
5. Click **Run workflow**

---

## 🐛 Troubleshooting Nhanh

### Build fails
```bash
# Check Dockerfile
cat accountService/Dockerfile
```

### Push fails
- Check Docker Hub token
- Check repository name

### SSH fails
```bash
# Test SSH từ local
ssh -i ~/.ssh/id_rsa ec2-user@<ec2-ip>
```

### Deploy fails
```bash
# SSH vào EC2 và check logs
cd /home/ec2-user/fsocial
docker-compose logs
docker-compose ps
```

---

## 📚 Tài Liệu Đầy Đủ

Xem `CI_CD_PLAN.md` và `README_CI_CD.md` để biết chi tiết.

---

**Chúc bạn thành công! 🎉**

