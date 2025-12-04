# 下一步操作指南

## ✅ 已完成的工作

1. ✅ 数据库表结构更新（添加role字段）
2. ✅ 实体类和DTO更新
3. ✅ 注册和登录逻辑更新（支持role字段）
4. ✅ 新增两个接口：
   - `GET /api/user/profile` - 获取用户身份
   - `POST /api/switch-role` - 切换用户身份
5. ✅ 接口文档更新
6. ✅ 数据库迁移脚本创建

---

## 📋 下一步操作清单

### 1. 执行数据库迁移 ⚠️ 重要

**方式一：使用psql命令行**
```bash
# 连接到数据库
psql -h db.gnlmdkgtheoeaibvrvvo.supabase.co -U postgres -d postgres

# 执行迁移脚本
\i src/main/resources/migration_add_role.sql
```

**方式二：直接执行SQL**
```sql
-- 连接到数据库后执行
ALTER TABLE user_accounts 
ADD COLUMN IF NOT EXISTS role VARCHAR(20) CHECK (role IN ('job_seeker', 'recruiters')) DEFAULT 'job_seeker';

-- 为现有数据设置默认role
UPDATE user_accounts ua
SET role = 'job_seeker'
WHERE ua.role IS NULL 
  AND EXISTS (SELECT 1 FROM talents t WHERE t.user_id = ua.user_id);

UPDATE user_accounts ua
SET role = 'recruiters'
WHERE ua.role IS NULL 
  AND EXISTS (SELECT 1 FROM recruiters r WHERE r.user_id = ua.user_id);

UPDATE user_accounts ua
SET role = 'job_seeker'
WHERE ua.role IS NULL;
```

**验证迁移是否成功**
```sql
-- 检查表结构
\d user_accounts

-- 检查现有数据的role字段
SELECT user_id, email, role FROM user_accounts LIMIT 10;
```

---

### 2. 配置数据库连接

编辑 `src/main/resources/application.properties`，设置正确的数据库连接：

```properties
# 方式一：直接配置（开发环境）
spring.datasource.url=jdbc:postgresql://db.gnlmdkgtheoeaibvrvvo.supabase.co:5432/postgres
spring.datasource.username=postgres
spring.datasource.password=1siauqRpJiDksbB0

# 方式二：使用环境变量（推荐，生产环境）
# 设置环境变量：
# export DB_URL=jdbc:postgresql://db.gnlmdkgtheoeaibvrvvo.supabase.co:5432/postgres
# export DB_USERNAME=postgres
# export DB_PASSWORD=1siauqRpJiDksbB0
```

---

### 3. 编译项目

```bash
# 清理并编译
mvn clean package -DskipTests

# 如果遇到Java版本问题，可以指定Java版本
# 项目需要Java 17，如果系统是Java 11，需要升级
```

**注意**：项目需要Java 17，当前系统是Java 11，需要：
- 安装Java 17
- 或使用Docker运行（推荐）

---

### 4. 启动服务

**方式一：直接运行JAR**
```bash
java -jar target/backend-1.0.jar
```

**方式二：使用Maven运行**
```bash
mvn spring-boot:run
```

**方式三：使用Docker（推荐）**
```bash
# 构建镜像
docker build -f Dockerfile.backend -t getjob-backend .

# 运行容器
docker-compose up -d
```

服务启动后，访问：`http://localhost:8080`

---

### 5. 测试接口

**方式一：使用测试脚本**
```bash
./test_api.sh
```

**方式二：手动测试**

1. **发送验证码**
```bash
curl -X POST http://localhost:8080/api/auth/user/send-verification-code \
  -H "Content-Type: application/json" \
  -d '{"email": "test@example.com"}'
```

2. **验证验证码并注册**
```bash
curl -X POST http://localhost:8080/api/auth/user/verify-code \
  -H "Content-Type: application/json" \
  -d '{"email": "test@example.com", "code": "1234"}'

curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com",
    "password": "password123",
    "user_type": "job_seeker",
    "name": "测试用户",
    "verification_token": "从上面获取的token"
  }'
```

3. **登录并获取token**
```bash
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email": "test@example.com", "password": "password123"}'
```

4. **测试新接口**
```bash
# 获取用户身份
curl -X GET http://localhost:8080/api/user/profile \
  -H "Authorization: Bearer {token}"

# 切换身份
curl -X POST http://localhost:8080/api/switch-role \
  -H "Authorization: Bearer {token}" \
  -H "Content-Type: application/json" \
  -d '{"target_role": "recruiters"}'
```

详细测试步骤请参考：`API_TEST.md`

---

### 6. 验证所有接口

使用以下清单验证：

- [ ] 注册接口返回role字段
- [ ] 登录接口返回role字段
- [ ] GET /api/user/profile 正常工作
- [ ] POST /api/switch-role 正常工作
- [ ] 身份切换后，role字段正确更新
- [ ] 其他现有接口正常工作

---

### 7. 部署到生产环境

如果测试通过，可以部署到生产环境：

```bash
# 使用部署脚本
./build.sh
./deploy.sh <服务器IP> <root密码>
```

或手动部署：
```bash
# 1. 构建
mvn clean package -DskipTests

# 2. 复制JAR到服务器
scp target/backend-1.0.jar user@server:/opt/getjob/backend/

# 3. 在服务器上执行数据库迁移
# 4. 重启服务
```

---

## 🔍 问题排查

### 问题1：数据库连接失败
- 检查数据库服务是否运行
- 验证连接字符串和凭据
- 检查防火墙设置

### 问题2：role字段为null
- 确认已执行数据库迁移脚本
- 检查注册时是否正确传入user_type
- 查看数据库表结构

### 问题3：接口返回401
- 检查token是否正确
- 确认token未过期
- 验证请求头格式：`Authorization: Bearer {token}`

### 问题4：Java版本不匹配
- 项目需要Java 17
- 当前系统是Java 11
- 解决方案：升级Java或使用Docker

---

## 📚 相关文档

- `API_TEST.md` - 详细的接口测试指南
- `CHANGELOG.md` - 更新日志
- `鎺ュ彛鏂囨。.md` - 接口文档
- `migration_add_role.sql` - 数据库迁移脚本

---

## 🎯 快速开始

```bash
# 1. 执行数据库迁移
psql -h <数据库地址> -U postgres -d postgres -f src/main/resources/migration_add_role.sql

# 2. 编译项目
mvn clean package -DskipTests

# 3. 启动服务
java -jar target/backend-1.0.jar

# 4. 测试接口（新终端）
./test_api.sh
```

---

## ✨ 完成标准

所有工作完成的标准：
1. ✅ 数据库迁移已执行
2. ✅ 服务成功启动
3. ✅ 所有接口测试通过
4. ✅ 注册和登录返回role字段
5. ✅ 新接口正常工作
6. ✅ 身份切换功能正常

完成以上步骤后，项目即可投入使用！

