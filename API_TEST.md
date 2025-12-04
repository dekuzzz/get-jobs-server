# API 接口测试指南

## 前置准备

### 1. 数据库迁移
执行以下SQL脚本添加role字段：
```bash
# 连接到PostgreSQL数据库
psql -h <数据库地址> -U postgres -d postgres

# 执行迁移脚本
\i src/main/resources/migration_add_role.sql
```

或者直接执行SQL：
```sql
ALTER TABLE user_accounts 
ADD COLUMN IF NOT EXISTS role VARCHAR(20) CHECK (role IN ('job_seeker', 'recruiters')) DEFAULT 'job_seeker';
```

### 2. 启动服务
```bash
# 编译项目
mvn clean package -DskipTests

# 运行服务
java -jar target/backend-1.0.jar
```

服务默认运行在：`http://localhost:8080`

---

## 接口测试步骤

### 步骤1: 发送验证码
```bash
curl -X POST http://localhost:8080/api/auth/user/send-verification-code \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com"
  }'
```

**预期响应**:
```json
{
  "code": 200,
  "message": "验证码发送成功",
  "data": {
    "email": "test@example.com",
    "purpose": "register",
    "expire_in": 300,
    "retry_after": 60
  }
}
```

### 步骤2: 验证验证码
```bash
curl -X POST http://localhost:8080/api/auth/user/verify-code \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com",
    "code": "1234"
  }'
```

**预期响应**:
```json
{
  "code": 200,
  "message": "验证码验证成功",
  "data": {
    "verified": true,
    "verification_token": "xxx"
  }
}
```

### 步骤3: 注册用户（求职者）
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com",
    "password": "password123",
    "user_type": "job_seeker",
    "name": "张三",
    "verification_token": "从步骤2获取的token"
  }'
```

**预期响应**（包含role字段）:
```json
{
  "code": 200,
  "message": "注册成功",
  "data": {
    "user_id": 123,
    "email": "test@example.com",
    "user_type": "job_seeker",
    "name": "张三",
    "role": "job_seeker",
    "talent_id": 123
  }
}
```

### 步骤4: 用户登录
```bash
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com",
    "password": "password123"
  }'
```

**预期响应**（包含role字段）:
```json
{
  "code": 200,
  "message": "登录成功",
  "data": {
    "access_token": "eyJhbGci...",
    "refresh_token": "eyJhbGci...",
    "token_type": "bearer",
    "expires_in": 7200,
    "user_info": {
      "user_id": 123,
      "email": "test@example.com",
      "user_type": "job_seeker",
      "name": "张三",
      "role": "job_seeker"
    }
  }
}
```

**保存token**:
```bash
export TOKEN="从响应中获取的access_token"
```

### 步骤5: 获取用户身份（新接口）
```bash
curl -X GET http://localhost:8080/api/user/profile \
  -H "Authorization: Bearer $TOKEN"
```

**预期响应**:
```json
{
  "code": 200,
  "message": "获取用户信息成功",
  "data": {
    "user_id": 123,
    "email": "test@example.com",
    "role": "job_seeker"
  }
}
```

### 步骤6: 切换身份（新接口）
```bash
curl -X POST http://localhost:8080/api/switch-role \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "target_role": "recruiters"
  }'
```

**预期响应**:
```json
{
  "code": 200,
  "message": "切换身份成功",
  "data": {
    "user_id": 123,
    "email": "test@example.com",
    "role": "recruiters"
  }
}
```

### 步骤7: 验证身份已切换
```bash
curl -X GET http://localhost:8080/api/user/profile \
  -H "Authorization: Bearer $TOKEN"
```

**预期响应**（role应该已变为recruiters）:
```json
{
  "code": 200,
  "message": "获取用户信息成功",
  "data": {
    "user_id": 123,
    "email": "test@example.com",
    "role": "recruiters"
  }
}
```

---

## 其他接口测试

### 职位列表（无需认证）
```bash
curl -X GET "http://localhost:8080/api/job?page=1&size=12"
```

### 人才列表（无需认证）
```bash
curl -X GET "http://localhost:8080/api/talents?page=1&pageSize=8&language=en"
```

### 创建职位（需要认证）
```bash
curl -X POST http://localhost:8080/api/job \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "title": "前端开发工程师",
    "work_location": "北京市朝阳区",
    "work_type": "full_time",
    "office_mode": "online",
    "min_salary": "15000",
    "max_salary": "45000",
    "category": "Frontend",
    "overview": "负责前端开发",
    "responsibility": "开发前端功能",
    "qualification": "3年以上经验",
    "details": "详细描述"
  }'
```

---

## 测试检查清单

- [ ] 数据库迁移脚本已执行
- [ ] 服务已启动（端口8080）
- [ ] 注册接口返回role字段
- [ ] 登录接口返回role字段
- [ ] GET /api/user/profile 接口正常
- [ ] POST /api/switch-role 接口正常
- [ ] 身份切换后，再次获取profile确认role已更新
- [ ] 所有其他接口正常工作

---

## 常见问题

### 1. 401 Unauthorized
- 检查token是否正确
- 确认token未过期（默认2小时）
- 检查请求头格式：`Authorization: Bearer {token}`

### 2. 数据库连接错误
- 检查 `application.properties` 中的数据库配置
- 确认数据库服务已启动
- 验证数据库用户权限

### 3. role字段为null
- 确认已执行数据库迁移脚本
- 检查注册时是否正确传入了user_type参数
- 查看数据库表结构是否包含role字段

