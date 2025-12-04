# 更新日志

## 2024-12-19 功能更新

### 新增功能

1. **用户角色管理**
   - 在 `user_accounts` 表中新增 `role` 字段，支持 `job_seeker`（求职者）和 `recruiters`（招聘者）两种角色
   - 注册时自动写入用户角色
   - 登录时返回用户角色信息

2. **新增接口**

   - **获取用户身份接口** (`GET /api/user/profile`)
     - 功能：获取当前登录用户的身份（role）
     - 认证：需要Bearer Token
     - 响应：返回用户ID、邮箱和角色信息

   - **切换身份接口** (`POST /api/switch-role`)
     - 功能：切换用户身份（在求职者和招聘者之间切换）
     - 认证：需要Bearer Token
     - 请求参数：`target_role` (job_seeker | recruiters)
     - 响应：返回更新后的用户信息

### 数据库变更

1. **user_accounts表结构更新**
   ```sql
   ALTER TABLE user_accounts 
   ADD COLUMN role VARCHAR(20) CHECK (role IN ('job_seeker', 'recruiters')) DEFAULT 'job_seeker';
   ```

2. **迁移脚本**
   - 位置：`src/main/resources/migration_add_role.sql`
   - 功能：为现有数据自动设置默认角色

### 代码变更

1. **实体类更新**
   - `UserAccountEntity.java`: 新增 `role` 字段

2. **DTO更新**
   - `AuthDTO.java`: 
     - `RegisterResponse`: 新增 `role` 字段
     - `UserInfo`: 新增 `role` 字段
     - 新增 `UserProfileResponse`、`SwitchRoleRequest`、`SwitchRoleResponse`

3. **服务层更新**
   - `AuthService.java`:
     - `register()`: 注册时写入role字段
     - `login()`: 登录时返回role字段，兼容旧数据

4. **控制器新增**
   - `UserController.java`: 新增用户管理相关接口

### 接口列表

所有接口已更新，支持role字段：

1. **注册登录**
   - `POST /api/auth/user/send-verification-code` - 发送验证码
   - `POST /api/auth/user/verify-code` - 验证验证码
   - `POST /api/auth/register` - 注册（返回role）
   - `POST /auth/login` - 登录（返回role）
   - `POST /api/auth/refresh` - 刷新token
   - `POST /api/auth/logout` - 登出

2. **用户管理**（新增）
   - `GET /api/user/profile` - 获取当前用户身份
   - `POST /api/switch-role` - 切换用户身份

3. **公司管理**
   - `GET /api/company/companyinfo` - 获取公司信息
   - `PUT /api/company/companyinfo` - 更新公司信息
   - `POST /api/company/companyinfo` - 创建公司信息

4. **职位管理**
   - `POST /api/job` - 创建职位
   - `GET /api/job` - 获取职位列表
   - `GET /api/jobs/{jobId}` - 获取职位详情
   - `PUT /api/jobs/{jobId}` - 更新职位
   - `DELETE /api/jobs/{jobId}` - 删除职位

5. **简历管理**
   - `POST /api/resumes` - 创建简历
   - `GET /api/resumes/{resumeId}` - 获取简历详情
   - `PUT /api/resumes/{resumeId}` - 更新简历
   - `DELETE /api/resumes/{resumeId}` - 删除简历

6. **授权管理**
   - `GET /api/roles` - 搜索授权
   - `GET /api/roles/exists` - 搜索已有授权
   - `POST /api/roles` - 添加授权
   - `DELETE /api/roles/{recruiterId}` - 删除授权

7. **人才管理**
   - `GET /api/talents` - 获取人才列表

### 使用说明

1. **数据库迁移**
   - 执行 `src/main/resources/migration_add_role.sql` 脚本
   - 或手动执行SQL添加role字段

2. **接口调用示例**

   **获取用户身份**:
   ```bash
   curl -X GET http://localhost:8080/api/user/profile \
     -H "Authorization: Bearer {token}"
   ```

   **切换身份**:
   ```bash
   curl -X POST http://localhost:8080/api/switch-role \
     -H "Authorization: Bearer {token}" \
     -H "Content-Type: application/json" \
     -d '{"target_role": "recruiters"}'
   ```

### 注意事项

1. 旧数据兼容：对于没有role字段的旧数据，系统会自动从talents/recruiters表推断并更新
2. 角色验证：切换角色时，系统会验证target_role必须是 `job_seeker` 或 `recruiters`
3. 所有需要认证的接口都需要在请求头中携带 `Authorization: Bearer {token}`

