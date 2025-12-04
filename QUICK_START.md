# 快速开始指南

## 🚀 一键执行脚本

我已经为您创建了自动化脚本，按以下步骤执行即可：

### 步骤1: 安装Python依赖（如果需要使用Python迁移脚本）

```bash
pip3 install psycopg2-binary
```

### 步骤2: 执行数据库迁移

**方式一：使用Python脚本（推荐）**
```bash
python3 migrate_database.py
```

**方式二：手动执行SQL**
如果您有数据库管理工具（如DBeaver、pgAdmin等），可以直接执行：
```sql
-- 文件位置: src/main/resources/migration_add_role.sql
ALTER TABLE user_accounts 
ADD COLUMN IF NOT EXISTS role VARCHAR(20) CHECK (role IN ('job_seeker', 'recruiters')) DEFAULT 'job_seeker';

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

### 步骤3: 解决Java版本问题

项目需要Java 17，但当前系统是Java 11。有两种解决方案：

**方案A：安装Java 17（推荐）**
```bash
# macOS使用Homebrew安装
brew install openjdk@17

# 设置JAVA_HOME
export JAVA_HOME=$(/usr/libexec/java_home -v 17)
export PATH=$JAVA_HOME/bin:$PATH

# 验证版本
java -version
```

**方案B：使用Docker运行（最简单）**
```bash
# 使用Docker Compose
docker-compose up -d

# 或使用Dockerfile
docker build -f Dockerfile.backend -t getjob-backend .
docker run -p 8080:8080 getjob-backend
```

### 步骤4: 编译项目

```bash
# 如果已安装Java 17
mvn clean package -DskipTests

# 如果使用Docker，跳过此步骤
```

### 步骤5: 启动服务

**方式A：直接运行**
```bash
java -jar target/backend-1.0.jar
```

**方式B：使用Maven**
```bash
mvn spring-boot:run
```

**方式C：使用Docker**
```bash
docker-compose up -d
```

### 步骤6: 测试接口

服务启动后（默认端口8080），运行测试脚本：

```bash
./test_api.sh
```

或手动测试：

```bash
# 1. 获取用户身份
curl -X GET http://localhost:8080/api/user/profile \
  -H "Authorization: Bearer {your_token}"

# 2. 切换身份
curl -X POST http://localhost:8080/api/switch-role \
  -H "Authorization: Bearer {your_token}" \
  -H "Content-Type: application/json" \
  -d '{"target_role": "recruiters"}'
```

---

## 📋 完整执行清单

- [ ] 执行数据库迁移（`python3 migrate_database.py` 或手动执行SQL）
- [ ] 安装Java 17 或配置Docker
- [ ] 编译项目（`mvn clean package -DskipTests`）
- [ ] 启动服务（`java -jar target/backend-1.0.jar`）
- [ ] 测试新接口（`./test_api.sh`）

---

## ⚠️ 当前状态

✅ **已完成**：
- 代码已更新（添加role字段支持）
- 新接口已创建（`/api/user/profile` 和 `/api/switch-role`）
- 数据库迁移脚本已准备
- 测试脚本已创建

⏳ **待完成**：
- 执行数据库迁移（需要数据库连接）
- 解决Java版本问题（需要Java 17）
- 编译和启动服务
- 测试接口

---

## 🔧 问题解决

### 问题1: Python脚本无法连接数据库
- 检查数据库连接信息是否正确
- 确认数据库服务可访问
- 检查防火墙设置

### 问题2: Java版本不匹配
- 安装Java 17
- 或使用Docker运行

### 问题3: 编译失败
- 确认Java版本是17
- 检查Maven配置
- 清理后重新编译：`mvn clean compile`

---

## 📞 需要帮助？

如果遇到问题，请检查：
1. `NEXT_STEPS.md` - 详细操作指南
2. `API_TEST.md` - 接口测试文档
3. `CHANGELOG.md` - 更新日志

