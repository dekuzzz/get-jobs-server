-- 数据库迁移脚本：为user_accounts表添加role字段
-- 执行此脚本前请备份数据库

-- 添加role字段
ALTER TABLE user_accounts 
ADD COLUMN IF NOT EXISTS role VARCHAR(20) CHECK (role IN ('job_seeker', 'recruiters')) DEFAULT 'job_seeker';

-- 为现有数据设置默认role（根据talents和recruiters表）
-- 如果用户在talents表中，设置为job_seeker
UPDATE user_accounts ua
SET role = 'job_seeker'
WHERE ua.role IS NULL 
  AND EXISTS (SELECT 1 FROM talents t WHERE t.user_id = ua.user_id);

-- 如果用户在recruiters表中，设置为recruiters
UPDATE user_accounts ua
SET role = 'recruiters'
WHERE ua.role IS NULL 
  AND EXISTS (SELECT 1 FROM recruiters r WHERE r.user_id = ua.user_id);

-- 如果既不在talents也不在recruiters表中，默认设置为job_seeker
UPDATE user_accounts ua
SET role = 'job_seeker'
WHERE ua.role IS NULL;

