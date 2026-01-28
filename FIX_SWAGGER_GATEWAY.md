# Fix Swagger API Docs trong API Gateway

## 🔍 Vấn đề

Khi truy cập Swagger UI qua Gateway:
```
http://localhost:8888/swagger-ui/index.html?urls.primaryName=Account+Service
```

Bạn gặp lỗi:
```
Failed to load API definition.
Fetch error
Bad Request /account/v3/api-docs
```

## 🎯 Nguyên nhân

### Problem 1: Route configuration không nhất quán
Account Service đang dùng pattern khác với các service khác:

**Account Service (SAI):**
```yaml
- id: account_service_swagger
  uri: ${ACCOUNT_SERVICE_URL:http://localhost:8081}
  predicates:
    - Path=/account/v3/api-docs
  filters:
    - RewritePath=/account/v3/api-docs, /account/v3/api-docs  # ❌ Vô dụng
```

**Các service khác (ĐÚNG):**
```yaml
- id: message_service_swagger
  uri: ${MESSAGE_SERVICE_URL:http://localhost:8082}
  predicates:
    - Path=/v3/api-docs/message
  filters:
    - RewritePath=/v3/api-docs/message, /message/v3/api-docs  # ✅ Có ý nghĩa
```

### Problem 2: Swagger UI URL không khớp

**Swagger UI config:**
```yaml
springdoc:
  swagger-ui:
    urls:
      - name: Account Service
        url: /account/v3/api-docs  # Trỏ đến path này
```

**Nhưng route lại dùng pattern khác:**
- Account: `/account/v3/api-docs`
- Message: `/v3/api-docs/message` (khác pattern)
- Post: `/v3/api-docs/post`
- Profile: `/v3/api-docs/profile`

## ✅ Giải pháp

### Bước 1: Sửa Swagger UI URLs (KHUYẾN NGHỊ)

Thay đổi Swagger UI config để nhất quán với route pattern:

**File:** `apigateway/src/main/resources/application-dev.yml`

**TỪ:**
```yaml
springdoc:
  swagger-ui:
    enabled: true
    path: /swagger-ui.html
    urls:
      - name: Account Service
        url: /account/v3/api-docs  # ❌ Pattern cũ
      - name: Message Service
        url: /message/v3/api-docs  # ❌ Pattern cũ
```

**SANG:**
```yaml
springdoc:
  swagger-ui:
    enabled: true
    path: /swagger-ui.html
    urls:
      - name: Account Service
        url: /v3/api-docs/account  # ✅ Pattern mới
      - name: Message Service
        url: /v3/api-docs/message
      - name: Post Service
        url: /v3/api-docs/post
      - name: Profile Service
        url: /v3/api-docs/profile
      - name: Timeline Service
        url: /v3/api-docs/timeline
      - name: Notification Service
        url: /v3/api-docs/notification
      - name: Relationship Service
        url: /v3/api-docs/relationship
```

### Bước 2: Sửa Gateway Routes cho Account Service

**File:** `apigateway/src/main/resources/application-dev.yml`

**TỪ:**
```yaml
- id: account_service_swagger
  uri: ${ACCOUNT_SERVICE_URL:http://localhost:8081}
  predicates:
    - Path=/account/v3/api-docs
  filters:
    - RewritePath=/account/v3/api-docs, /account/v3/api-docs
```

**SANG:**
```yaml
- id: account_service_swagger
  uri: ${ACCOUNT_SERVICE_URL:http://localhost:8081}
  predicates:
    - Path=/v3/api-docs/account
  filters:
    - RewritePath=/v3/api-docs/account, /account/v3/api-docs
```

### Bước 3: Verify các routes khác

Kiểm tra tất cả các service routes đều dùng pattern `/v3/api-docs/{service}`:

```yaml
routes:
  # ✅ Account Service
  - id: account_service_swagger
    uri: ${ACCOUNT_SERVICE_URL:http://localhost:8081}
    predicates:
      - Path=/v3/api-docs/account
    filters:
      - RewritePath=/v3/api-docs/account, /account/v3/api-docs

  # ✅ Message Service (đã đúng)
  - id: message_service_swagger
    uri: ${MESSAGE_SERVICE_URL:http://localhost:8082}
    predicates:
      - Path=/v3/api-docs/message
    filters:
      - RewritePath=/v3/api-docs/message, /message/v3/api-docs

  # ✅ Post Service (đã đúng)
  - id: post_service_swagger
    uri: ${POST_SERVICE_URL:http://localhost:8083}
    predicates:
      - Path=/v3/api-docs/post
    filters:
      - RewritePath=/v3/api-docs/post, /post/v3/api-docs

  # ... và tiếp tục cho các service khác
```

## 🧪 Testing

### 1. Restart Gateway
```bash
cd apigateway
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

### 2. Restart Account Service
```bash
cd accountService
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

### 3. Test API Docs endpoints

**Trực tiếp từ Account Service:**
```bash
curl http://localhost:8081/account/v3/api-docs
```
Phải trả về JSON OpenAPI definition

**Qua Gateway:**
```bash
curl http://localhost:8888/v3/api-docs/account
```
Phải trả về cùng JSON OpenAPI definition

### 4. Test Swagger UI

Truy cập:
```
http://localhost:8888/swagger-ui.html
```

Chọn "Account Service" từ dropdown, phải load được API documentation.

## 📊 Flow hoạt động

```
Browser Request:
http://localhost:8888/swagger-ui.html?urls.primaryName=Account+Service
                ↓
Swagger UI loads from Gateway
                ↓
Swagger UI requests API docs from:
http://localhost:8888/v3/api-docs/account
                ↓
Gateway receives: /v3/api-docs/account
                ↓
Gateway matches route: account_service_swagger
                ↓
RewritePath filter: /v3/api-docs/account → /account/v3/api-docs
                ↓
Gateway forwards to: http://localhost:8081/account/v3/api-docs
                ↓
Account Service returns OpenAPI JSON
                ↓
Swagger UI displays documentation
```

## 🔍 Debug Tips

### 1. Enable Gateway Debug Logging

Đã có trong config (dòng 189):
```yaml
logging:
  level:
    org.springframework.cloud.gateway: DEBUG
```

### 2. Check Gateway Logs

Khi request đến, logs sẽ hiển thị:
```
[Gateway] Matched route: account_service_swagger
[Gateway] Rewriting path: /v3/api-docs/account → /account/v3/api-docs
[Gateway] Forwarding to: http://localhost:8081/account/v3/api-docs
```

### 3. Verify Account Service Endpoint

```bash
# Phải trả về JSON với "openapi": "3.0.1"
curl http://localhost:8081/account/v3/api-docs | jq
```

## ⚠️ Common Issues

### Issue 1: 404 Not Found
**Nguyên nhân:** Account Service chưa chạy hoặc context-path sai
**Fix:** Kiểm tra Account Service có chạy không, và verify context-path là `/account`

### Issue 2: CORS Error
**Nguyên nhân:** CORS config không cho phép request từ Swagger UI
**Fix:** Đã có CORS config trong Gateway (dòng 59-85), không cần thay đổi

### Issue 3: Authentication Required
**Nguyên nhân:** Gateway hoặc Account Service yêu cầu authentication
**Fix:** 
- Disable security cho api-docs endpoint
- Hoặc add authentication header trong request

### Issue 4: Empty/Invalid OpenAPI JSON
**Nguyên nhân:** SpringDoc không được config đúng trong Account Service
**Fix:** Verify trong Account Service `application-dev.yml`:
```yaml
springdoc:
  api-docs:
    enabled: true
    path: /v3/api-docs
  swagger-ui:
    enabled: true
    path: /swagger-ui.html
```

## 🎯 Final Configuration Summary

**Gateway Swagger UI URLs:**
```
/v3/api-docs/account
/v3/api-docs/message
/v3/api-docs/post
/v3/api-docs/profile
/v3/api-docs/timeline
/v3/api-docs/notification
/v3/api-docs/relationship
```

**Service Actual Endpoints:**
```
http://localhost:8081/account/v3/api-docs
http://localhost:8082/message/v3/api-docs
http://localhost:8083/post/v3/api-docs
http://localhost:8085/profile/v3/api-docs
http://localhost:8086/timeline/v3/api-docs
http://localhost:8087/notification/v3/api-docs
http://localhost:8088/relationship/v3/api-docs
```

**Gateway Routes:**
- Receive: `/v3/api-docs/{service}`
- Rewrite to: `/{service}/v3/api-docs`
- Forward to: `http://localhost:{port}/{service}/v3/api-docs`
