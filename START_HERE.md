# 🚀 从这里开始

## 数据库信息
- **PostgreSQL版本**: 15
- **密码**: WCwsad123456
- **默认配置**: localhost:5432/postgres

---

## 第一步：启动PostgreSQL服务

如果PostgreSQL服务未运行，请先启动：

### macOS (使用Homebrew安装的PostgreSQL)
```bash
# 启动PostgreSQL服务
brew services start postgresql@15

# 或使用pg_ctl
pg_ctl -D /usr/local/var/postgresql@15 start
```

### 验证服务是否运行
```bash
# 检查端口是否监听
lsof -i :5432

# 或尝试连接
psql -h localhost -U postgres -d postgres
# 输入密码: WCwsad123456
```

---

## 第二步：执行数据库迁移

### 方式A：使用Python脚本（推荐）
```bash
# 直接运行迁移脚本
python3 migrate_database.py
```

### 方式B：使用便捷脚本
```bash
./run_migration.sh
```

### 方式C：手动执行SQL
如果Python脚本无法运行，可以使用任何PostgreSQL客户端工具执行：
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

---

## 第三步：启动应用服务

### 方式A：使用Docker（推荐，无需Java 17）
```bash
docker-compose up -d
```

### 方式B：直接运行（需要Java 17）
```bash
# 编译
mvn clean package -DskipTests

# 运行
java -jar target/backend-1.0.jar
```

---

## 第四步：测试接口

```bash
# 运行自动化测试
./test_api.sh
```

---

## 📋 快速执行命令

```bash
# 1. 启动PostgreSQL（如果未运行）
brew services start postgresql@15

# 2. 执行数据库迁移
python3 migrate_database.py

# 3. 启动应用（使用Docker）
docker-compose up -d

# 4. 测试接口
./test_api.sh
```

---

## ⚠️ 常见问题

### 问题1: 连接被拒绝
```
connection to server at "localhost" (::1), port 5432 failed: Connection refused
```

**解决方案**:
```bash
# 检查PostgreSQL是否运行
brew services list | grep postgresql

# 启动服务
brew services start postgresql@15
```

### 问题2: 密码错误
确保使用密码: `WCwsad123456`

### 问题3: 数据库不存在
```bash
# 创建数据库（如果需要）
createdb -h localhost -U postgres postgres
```

---

## ✅ 验证清单

- [ ] PostgreSQL服务已启动
- [ ] 数据库迁移已执行
- [ ] 应用服务已启动（端口8080）
- [ ] 接口测试通过

完成以上步骤后，所有功能即可正常使用！

