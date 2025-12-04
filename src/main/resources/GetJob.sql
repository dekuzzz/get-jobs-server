DROP TABLE IF EXISTS job_content CASCADE;
DROP TABLE IF EXISTS job_qualification CASCADE;
DROP TABLE IF EXISTS job_responsibility CASCADE;
DROP TABLE IF EXISTS jobs CASCADE;
DROP TABLE IF EXISTS resume_languages CASCADE;
DROP TABLE IF EXISTS resume_skills CASCADE;
DROP TABLE IF EXISTS resume_experiences CASCADE;
DROP TABLE IF EXISTS resume_educations CASCADE;
DROP TABLE IF EXISTS resume_advantages CASCADE;
DROP TABLE IF EXISTS resumes CASCADE;
DROP TABLE IF EXISTS company_links CASCADE;
DROP TABLE IF EXISTS company_content CASCADE;
DROP TABLE IF EXISTS company_recruiters CASCADE;
DROP TABLE IF EXISTS companies CASCADE;
DROP TABLE IF EXISTS recruiters CASCADE;
DROP TABLE IF EXISTS talents_links CASCADE;
DROP TABLE IF EXISTS talents CASCADE;
DROP TABLE IF EXISTS user_accounts CASCADE;
DROP TABLE IF EXISTS verification_codes CASCADE;

-- 主用户账户表（邮箱密码登录）
CREATE TABLE user_accounts (
                               user_id SERIAL PRIMARY KEY,
                               email VARCHAR(255) UNIQUE NOT NULL,
                               password_hash VARCHAR(255) NOT NULL,
                               salt VARCHAR(100) NOT NULL,
                               role VARCHAR(20) CHECK (role IN ('job_seeker', 'recruiters')) DEFAULT 'job_seeker',
                               is_active BOOLEAN NOT NULL DEFAULT TRUE,
                               last_login_at TIMESTAMPTZ,
                               login_attempts INTEGER DEFAULT 0,
                               locked_until TIMESTAMPTZ,
                               created_at TIMESTAMPTZ DEFAULT NOW(),
                               updated_at TIMESTAMPTZ DEFAULT NOW()
);

-- 验证码表（新增）
CREATE TABLE verification_codes (
                                    verification_id SERIAL PRIMARY KEY,
                                    email VARCHAR(255) NOT NULL,
                                    code VARCHAR(10) NOT NULL, -- 验证码（4位数字）
                                    code_type VARCHAR(20) NOT NULL CHECK (code_type IN ('register', 'login', 'reset_password', 'change_email')),
                                    is_used BOOLEAN NOT NULL DEFAULT FALSE,
                                    expires_at TIMESTAMPTZ NOT NULL, -- 验证码过期时间
                                    used_at TIMESTAMPTZ, -- 使用时间
                                    created_at TIMESTAMPTZ DEFAULT NOW()
);

-- 求职者账户表
CREATE TABLE talents (
                         talent_id SERIAL PRIMARY KEY,
                         user_id INT NOT NULL REFERENCES user_accounts(user_id) ON DELETE CASCADE,
                         full_name VARCHAR(100) NOT NULL,
                         avatar TEXT,
                         location VARCHAR(255),
                         vote NUMERIC DEFAULT 0,
                         is_active BOOLEAN NOT NULL DEFAULT TRUE,
                         created_at TIMESTAMPTZ DEFAULT NOW(),
                         updated_at TIMESTAMPTZ DEFAULT NOW(),
                         UNIQUE (user_id)
);

-- 求职者社交链接表
CREATE TABLE talents_links (
                               talent_link_id SERIAL PRIMARY KEY,
                               talent_id INT NOT NULL REFERENCES talents(talent_id) ON DELETE CASCADE,
                               link_type VARCHAR(20) NOT NULL CHECK (link_type IN ('twitter', 'qq', 'github', 'linkedin', 'portfolio')),
                               url TEXT NOT NULL,
                               UNIQUE (talent_id, link_type)
);

-- 招聘者账户表
CREATE TABLE recruiters (
                            recruiter_id SERIAL PRIMARY KEY,
                            user_id INT NOT NULL REFERENCES user_accounts(user_id) ON DELETE CASCADE,
                            full_name VARCHAR(100) NOT NULL,
                            avatar TEXT,
                            professional_title VARCHAR(100),
                            is_verified BOOLEAN DEFAULT FALSE,
                            is_active BOOLEAN NOT NULL DEFAULT TRUE,
                            created_at TIMESTAMPTZ DEFAULT NOW(),
                            updated_at TIMESTAMPTZ DEFAULT NOW(),
                            UNIQUE (user_id)
);

-- 公司表
CREATE TABLE companies (
                           company_id SERIAL PRIMARY KEY,
                           owner_recruiter_id INT NOT NULL REFERENCES recruiters(recruiter_id) ON DELETE CASCADE, -- 公司创建者/所有者
                           full_name VARCHAR(255) NOT NULL,
                           avatar TEXT,
                           official_website TEXT,
                           location VARCHAR(255),
                           language_code VARCHAR(2) DEFAULT 'en',
                           size VARCHAR(50),
                           category_tags TEXT,
                           email_address VARCHAR(255),
                           vote NUMERIC DEFAULT 0,
                           is_active BOOLEAN NOT NULL DEFAULT TRUE,
                           created_at TIMESTAMPTZ DEFAULT NOW(),
                           updated_at TIMESTAMPTZ DEFAULT NOW()
);

-- 公司招聘者关联表（招聘者加入公司）
CREATE TABLE company_recruiters (
                                    company_recruiter_id SERIAL PRIMARY KEY,
                                    company_id INT NOT NULL REFERENCES companies(company_id) ON DELETE CASCADE,
                                    recruiter_id INT NOT NULL REFERENCES recruiters(recruiter_id) ON DELETE CASCADE,
                                    role VARCHAR(20) NOT NULL CHECK (role IN ('owner', 'admin', 'recruiter', 'viewer')) DEFAULT 'recruiter',
                                    status VARCHAR(20) NOT NULL CHECK (status IN ('pending', 'active', 'rejected', 'suspended')) DEFAULT 'pending',
                                    invited_by INT REFERENCES recruiters(recruiter_id),
                                    joined_at TIMESTAMPTZ,
                                    created_at TIMESTAMPTZ DEFAULT NOW(),
                                    updated_at TIMESTAMPTZ DEFAULT NOW(),
                                    UNIQUE (company_id, recruiter_id)
);

-- 公司多语言内容表
CREATE TABLE company_content (
                                 company_content_id SERIAL PRIMARY KEY,
                                 company_id INT NOT NULL REFERENCES companies(company_id) ON DELETE CASCADE,
                                 language_code VARCHAR(2) NOT NULL,
                                 slogan TEXT,
                                 support TEXT,
                                 description TEXT,
                                 UNIQUE (company_id, language_code)
);

-- 公司社交链接表
CREATE TABLE company_links (
                               company_link_id SERIAL PRIMARY KEY,
                               company_id INT NOT NULL REFERENCES companies(company_id) ON DELETE CASCADE,
                               link_type VARCHAR(20) NOT NULL CHECK (link_type IN ('x', 'telegram', 'discord', 'github', 'linkedin', 'facebook')),
                               url TEXT NOT NULL,
                               UNIQUE (company_id, link_type)
);

-- 简历表
CREATE TABLE resumes (
                         resume_id SERIAL PRIMARY KEY,
                         talent_id INT NOT NULL REFERENCES talents(talent_id) ON DELETE CASCADE,
                         name VARCHAR(100),
                         title VARCHAR(100),
                         phone_number VARCHAR(20),
                         email_address VARCHAR(255),
                         telegram VARCHAR(100),
                         wechat VARCHAR(100),
                         office_mode VARCHAR(100),
                         work_nature VARCHAR(20) NOT NULL CHECK (work_nature IN ('full-time', 'part-time')),
                         permission VARCHAR(100),
                         position VARCHAR(100),
                         min_salary NUMERIC,
                         max_salary NUMERIC,
                         currency_code CHAR(3),
                         settlement VARCHAR(100),
                         is_active BOOLEAN NOT NULL DEFAULT TRUE,
                         created_at TIMESTAMPTZ DEFAULT NOW(),
                         updated_at TIMESTAMPTZ DEFAULT NOW()
);

-- 简历优势表
CREATE TABLE resume_advantages (
                                   resume_advantage_id SERIAL PRIMARY KEY,
                                   resume_id INT NOT NULL REFERENCES resumes(resume_id) ON DELETE CASCADE,
                                   language_code CHAR(2) NOT NULL,
                                   content TEXT,
                                   description TEXT,
                                   UNIQUE (resume_id, language_code)
);

-- 简历教育经历表
CREATE TABLE resume_educations (
                                   resume_education_id SERIAL PRIMARY KEY,
                                   resume_id INT NOT NULL REFERENCES resumes(resume_id) ON DELETE CASCADE,
                                   language_code CHAR(2) NOT NULL,
                                   institution VARCHAR(255) NOT NULL,
                                   degree VARCHAR(100),
                                   field_of_study VARCHAR(100),
                                   start_date DATE,
                                   end_date DATE,
                                   description TEXT
);

-- 简历工作经历表
CREATE TABLE resume_experiences (
                                    resume_experience_id SERIAL PRIMARY KEY,
                                    resume_id INT NOT NULL REFERENCES resumes(resume_id) ON DELETE CASCADE,
                                    language_code CHAR(2) NOT NULL,
                                    company_name VARCHAR(255) NOT NULL,
                                    position VARCHAR(100) NOT NULL,
                                    working_time VARCHAR(100),
                                    start_date DATE,
                                    end_date DATE,
                                    description TEXT
);

-- 简历技能表
CREATE TABLE resume_skills (
                               resume_skill_id SERIAL PRIMARY KEY,
                               resume_id INT NOT NULL REFERENCES resumes(resume_id) ON DELETE CASCADE,
                               language_code CHAR(2) NOT NULL,
                               skill_name VARCHAR(100) NOT NULL,
                               proficiency VARCHAR(50)
);

-- 简历语言能力表
CREATE TABLE resume_languages (
                                  resume_language_id SERIAL PRIMARY KEY,
                                  resume_id INT NOT NULL REFERENCES resumes(resume_id) ON DELETE CASCADE,
                                  language_code CHAR(2) NOT NULL,
                                  language_name VARCHAR(100) NOT NULL,
                                  proficiency VARCHAR(50)
);

-- 职位表
CREATE TABLE jobs (
                      job_id SERIAL PRIMARY KEY,
                      company_id INT NOT NULL REFERENCES companies(company_id) ON DELETE CASCADE,
                      created_by INT NOT NULL REFERENCES recruiters(recruiter_id), -- 职位创建者
                      language_code CHAR(2) NOT NULL,
                      job_name VARCHAR(255) NOT NULL,
                      office_mode VARCHAR(100),
                      work_nature VARCHAR(20) NOT NULL CHECK (work_nature IN ('full-time', 'part-time')),
                      location VARCHAR(255),
                      min_salary NUMERIC,
                      max_salary NUMERIC,
                      currency_code CHAR(3),
                      settlement VARCHAR(100),
                      is_active BOOLEAN NOT NULL DEFAULT TRUE,
                      created_at TIMESTAMPTZ DEFAULT NOW(),
                      updated_at TIMESTAMPTZ DEFAULT NOW()
);

-- 职位职责表
CREATE TABLE job_responsibility (
                                    job_responsibility_id SERIAL PRIMARY KEY,
                                    job_id INT NOT NULL REFERENCES jobs(job_id) ON DELETE CASCADE,
                                    language_code CHAR(2) NOT NULL,
                                    responsibility_text TEXT NOT NULL
);

-- 职位资格要求表
CREATE TABLE job_qualification (
                                   job_qualification_id SERIAL PRIMARY KEY,
                                   job_id INT NOT NULL REFERENCES jobs(job_id) ON DELETE CASCADE,
                                   language_code CHAR(2) NOT NULL,
                                   qualification_text TEXT NOT NULL
);

-- 职位内容表
CREATE TABLE job_content (
                             job_content_id SERIAL PRIMARY KEY,
                             job_id INT NOT NULL REFERENCES jobs(job_id) ON DELETE CASCADE,
                             language_code CHAR(2) NOT NULL,
                             content_text TEXT NOT NULL
);

-- 创建索引以提高查询性能
CREATE INDEX idx_talents_user ON talents(user_id);
CREATE INDEX idx_recruiters_user ON recruiters(user_id);
CREATE INDEX idx_companies_owner ON companies(owner_recruiter_id);
CREATE INDEX idx_company_recruiters_company ON company_recruiters(company_id);
CREATE INDEX idx_company_recruiters_recruiter ON company_recruiters(recruiter_id);
CREATE INDEX idx_company_recruiters_status ON company_recruiters(status);
CREATE INDEX idx_jobs_company ON jobs(company_id);
CREATE INDEX idx_jobs_created_by ON jobs(created_by);
CREATE INDEX idx_user_accounts_email ON user_accounts(email);