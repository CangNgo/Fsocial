# 🔐 Khắc phục lỗi Authentication 401 trong Docker Container

## 🚨 **Vấn đề đã gặp phải:**

Khi chạy project trong Docker container, API Gateway trả về lỗi **401 Unauthorized** mặc dù đã có access token hợp lệ. Trong khi đó, cùng code chạy ngoài Docker bằng IDE thì hoạt động bình thường.

## 🔍 **Nguyên nhân gốc rễ:**

### 1. **Hard-coded localhost trong WebClientConfig**
```java
// TRƯỚC (SAI):
.baseUrl("http://localhost:8081/account")

// SAU (ĐÚNG):
.baseUrl(accountServiceUrl + "/account")
```

**Vấn đề:** `localhost:8081` trong Docker container trỏ đến chính container của API Gateway, không phải Account Service.

### 2. **Cấu hình routes sử dụng localhost**
```yaml
# TRƯỚC (SAI):
uri: ${ACCOUNT_SERVICE_URL:http://localhost:8081}

# SAU (ĐÚNG):
uri: ${ACCOUNT_SERVICE_URL:http://fsocial-accountservice:8081}
```

**Vấn đề:** Trong Docker network, các service giao tiếp qua tên container, không phải localhost.

### 3. **Thiếu cấu hình môi trường cho Docker**
- Không có profile production
- Không sử dụng biến môi trường cho service URLs

## 🛠️ **Các thay đổi đã thực hiện:**

### 1. **Cập nhật WebClientConfig.java**
- Thêm `@Value("${ACCOUNT_SERVICE_URL:http://fsocial-accountservice:8081}")`
- Sử dụng biến môi trường thay vì hard-code

### 2. **Cập nhật application-dev.yml**
- Thay đổi tất cả `localhost:port` thành tên container từ file `.env`
- Cập nhật Redis host từ `localhost` thành `fsocial-redis`

### 3. **Tạo application-prod.yml**
- Cấu hình tối ưu cho môi trường Docker
- Sử dụng tên container từ file `.env` làm default values

### 4. **Cập nhật docker-compose.yml**
- Thêm `SPRING_PROFILES_ACTIVE=prod` cho API Gateway

## 📋 **Danh sách thay đổi dựa trên file .env:**

| Service | Trước | Sau |
|---------|-------|-----|
| Account Service | `localhost:8081` | `fsocial-accountservice:8081` |
| Message Service | `localhost:8082` | `fsocial-messageservice:8082` |
| Post Service | `localhost:8083` | `fsocial-postservice:8083` |
| Processor Service | `localhost:8084` | `fsocial-processorservice:8084` |
| Profile Service | `localhost:8085` | `fsocial-profileservice:8085` |
| Timeline Service | `localhost:8086` | `fsocial-timelineservice:8086` |
| Notification Service | `localhost:8087` | `fsocial-notificationservice:8087` |
| Relationship Service | `localhost:8088` | `fsocial-relationshipservice:8088` |
| Redis | `localhost:6379` | `fsocial-redis:6379` |

## 🔧 **Cấu hình từ file .env:**

File `.env` đã có sẵn các biến môi trường:
```bash
# Service URLs
ACCOUNT_SERVICE_URL=http://fsocial-accountservice:8081
MESSAGE_SERVICE_URL=http://fsocial-messageservice:8082
POST_SERVICE_URL=http://fsocial-postservice:8083
PROCESSOR_SERVICE_URL=http://fsocial-processorservice:8084
PROFILE_SERVICE_URL=http://fsocial-profileservice:8085
TIMELINE_SERVICE_URL=http://fsocial-timelineservice:8086
NOTIFICATION_SERVICE_URL=http://fsocial-notificationservice:8087
RELATIONSHIP_SERVICE_URL=http://fsocial-relationshipservice:8088

# Redis
REDIS_CONTAINER=fsocial-redis
REDIS_PORT=6379
REDIS_PASSWORD=redispassword
```

## 🚀 **Cách khắc phục:**

### **Bước 1: Rebuild và restart containers**
```bash
docker-compose down
docker-compose build --no-cache
docker-compose up -d
```

### **Bước 2: Kiểm tra logs**
```bash
docker-compose logs apigateway
docker-compose logs accountservice
```

### **Bước 3: Test authentication**
```bash
# Login để lấy token
curl -X POST http://localhost:8888/api/v1/account/login \
  -H "Content-Type: application/json" \
  -d '{"username":"your_username","password":"your_password"}'

# Sử dụng token để truy cập API
curl -X GET http://localhost:8888/api/v1/profile/me \
  -H "Authorization: Bearer YOUR_ACCESS_TOKEN"
```

## 🔧 **Kiểm tra cấu hình:**

### **1. Xác nhận biến môi trường:**
```bash
docker-compose exec apigateway env | grep SERVICE_URL
```

### **2. Kiểm tra network connectivity:**
```bash
docker-compose exec apigateway ping fsocial-accountservice
docker-compose exec apigateway ping fsocial-redis
```

### **3. Xác nhận profile đang sử dụng:**
```bash
docker-compose logs apigateway | grep "The following profiles are active"
```

## ⚠️ **Lưu ý quan trọng:**

1. **Không bao giờ hard-code localhost** trong Docker environment
2. **Luôn sử dụng biến môi trường** cho service URLs
3. **Sử dụng tên container từ file .env** thay vì localhost trong Docker network
4. **Tạo profile riêng** cho production/Docker environment
5. **File .env đã có sẵn cấu hình đúng** - chỉ cần sử dụng

## 📚 **Tài liệu tham khảo:**

- [Spring Cloud Gateway Documentation](https://spring.io/projects/spring-cloud-gateway)
- [Docker Networking](https://docs.docker.com/network/)
- [Spring Profiles](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.profiles)

## 🎯 **Kết quả mong đợi:**

Sau khi áp dụng các thay đổi:
- ✅ Authentication hoạt động bình thường trong Docker
- ✅ API Gateway có thể giao tiếp với Account Service
- ✅ Token validation thành công
- ✅ Không còn lỗi 401 Unauthorized
- ✅ Sử dụng đúng cấu hình từ file .env
