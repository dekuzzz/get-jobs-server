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
DROP TABLE IF EXISTS companies CASCADE;
DROP TABLE IF EXISTS talents_links CASCADE;
DROP TABLE IF EXISTS talents CASCADE;
DROP TABLE IF EXISTS clerk_users CASCADE;

CREATE TABLE clerk_users (
                             clerk_user_id VARCHAR(255) PRIMARY KEY,
                             email VARCHAR(255),
                             phone VARCHAR(20),
                             created_at TIMESTAMPTZ DEFAULT NOW(),
                             updated_at TIMESTAMPTZ DEFAULT NOW()
);

CREATE TABLE talents (
                         talent_id SERIAL PRIMARY KEY,
                         clerk_user_id VARCHAR(255) NOT NULL REFERENCES clerk_users(clerk_user_id) ON DELETE CASCADE,
                         full_name VARCHAR(100),
                         avatar TEXT,
                         location VARCHAR(255),
                         vote NUMERIC,
                         created_at TIMESTAMPTZ DEFAULT NOW(),
                         updated_at TIMESTAMPTZ DEFAULT NOW(),
                         UNIQUE (clerk_user_id)
);

CREATE TABLE talents_links (
                               talent_link_id SERIAL PRIMARY KEY,
                               talent_id INT NOT NULL REFERENCES talents(talent_id) ON DELETE CASCADE,
                               link_type VARCHAR(20) NOT NULL CHECK (link_type IN ('twitter', 'qq', 'github', 'linkedin', 'portfolio')),
                               url TEXT NOT NULL,
                               UNIQUE (talent_id, link_type)
);

CREATE TABLE companies (
                           company_id SERIAL PRIMARY KEY,
                           clerk_user_id VARCHAR(255) NOT NULL REFERENCES clerk_users(clerk_user_id) ON DELETE CASCADE,
                           full_name VARCHAR(255),
                           avatar TEXT,
                           official_website TEXT,
                           location VARCHAR(255),
                           language_code VARCHAR(2),
                           size VARCHAR(50),
                           category_tags TEXT,
                           email_address VARCHAR(255),
                           vote NUMERIC,
                           created_at TIMESTAMPTZ DEFAULT NOW(),
                           updated_at TIMESTAMPTZ DEFAULT NOW(),
                           UNIQUE (clerk_user_id, language_code)
);

CREATE TABLE company_content (
                                 company_content_id SERIAL PRIMARY KEY,
                                 company_id INT NOT NULL REFERENCES companies(company_id) ON DELETE CASCADE,
                                 language_code VARCHAR(2) NOT NULL,
                                 slogan TEXT,
                                 support TEXT,
                                 description TEXT,
                                 UNIQUE (company_id, language_code)
);

CREATE TABLE company_links (
                               company_link_id SERIAL PRIMARY KEY,
                               company_id INT NOT NULL REFERENCES companies(company_id) ON DELETE CASCADE,
                               link_type VARCHAR(20) NOT NULL CHECK (link_type IN ('x', 'telegram', 'discord', 'github', 'linkedin', 'facebook')),
                               url TEXT NOT NULL,
                               UNIQUE (company_id, link_type)
);

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
                         created_at TIMESTAMPTZ DEFAULT NOW(),
                         updated_at TIMESTAMPTZ DEFAULT NOW()
);

CREATE TABLE resume_advantages (
                                   resume_advantage_id SERIAL PRIMARY KEY,
                                   resume_id INT NOT NULL REFERENCES resumes(resume_id) ON DELETE CASCADE,
                                   language_code CHAR(2) NOT NULL,
                                   content TEXT,
                                   description TEXT,
                                   UNIQUE (resume_id, language_code)
);

CREATE TABLE resume_educations (
                                   resume_education_id SERIAL PRIMARY KEY,
                                   resume_id INT NOT NULL REFERENCES resumes(resume_id) ON DELETE CASCADE,
                                   language_code CHAR(2) NOT NULL,
                                   institution VARCHAR(255) NOT NULL,
                                   degree VARCHAR(100),
                                   field_of_study VARCHAR(100),
                                   start_date DATE,
                                   end_date DATE,
                                   description TEXT,
                                   UNIQUE (resume_id, language_code)
);

CREATE TABLE resume_experiences (
                                    resume_experience_id SERIAL PRIMARY KEY,
                                    resume_id INT NOT NULL REFERENCES resumes(resume_id) ON DELETE CASCADE,
                                    language_code CHAR(2) NOT NULL,
                                    company_name VARCHAR(255) NOT NULL,
                                    position VARCHAR(100) NOT NULL,
                                    working_time VARCHAR(100),
                                    start_date DATE,
                                    end_date DATE,
                                    description TEXT,
                                    UNIQUE (resume_id, language_code)
);

CREATE TABLE resume_skills (
                               resume_skill_id SERIAL PRIMARY KEY,
                               resume_id INT NOT NULL REFERENCES resumes(resume_id) ON DELETE CASCADE,
                               language_code CHAR(2) NOT NULL,
                               skill_name VARCHAR(100) NOT NULL,
                               proficiency VARCHAR(50),
                               UNIQUE (resume_id, language_code)
);

CREATE TABLE resume_languages (
                                  resume_language_id SERIAL PRIMARY KEY,
                                  resume_id INT NOT NULL REFERENCES resumes(resume_id) ON DELETE CASCADE,
                                  language_code CHAR(2) NOT NULL,
                                  language_name VARCHAR(100) NOT NULL,
                                  proficiency VARCHAR(50),
                                  UNIQUE (resume_id, language_code)
);

CREATE TABLE jobs (
                      job_id SERIAL PRIMARY KEY,
                      company_id INT NOT NULL REFERENCES companies(company_id) ON DELETE CASCADE,
                      language_code CHAR(2) NOT NULL,
                      job_name VARCHAR(255),
                      office_mode VARCHAR(100),
                      work_nature VARCHAR(20) NOT NULL CHECK (work_nature IN ('full-time', 'part-time')),
                      location VARCHAR(255),
                      min_salary NUMERIC,
                      max_salary NUMERIC,
                      currency_code CHAR(3),
                      settlement VARCHAR(100),
                      is_active BOOLEAN NOT NULL,
                      created_at TIMESTAMPTZ DEFAULT NOW(),
                      updated_at TIMESTAMPTZ DEFAULT NOW(),
                      update_time TIMESTAMPTZ DEFAULT NOW(),
                      UNIQUE (company_id, language_code, job_name, is_active)
);

CREATE TABLE job_responsibility (
                                    job_responsibility_id SERIAL PRIMARY KEY,
                                    job_id INT NOT NULL REFERENCES jobs(job_id) ON DELETE CASCADE,
                                    language_code CHAR(2) NOT NULL,
                                    responsibility_text TEXT NOT NULL,
                                    UNIQUE (job_id, language_code)
);

CREATE TABLE job_qualification (
                                   job_qualification_id SERIAL PRIMARY KEY,
                                   job_id INT NOT NULL REFERENCES jobs(job_id) ON DELETE CASCADE,
                                   language_code CHAR(2) NOT NULL,
                                   qualification_text TEXT NOT NULL,
                                   UNIQUE (job_id, language_code)
);

CREATE TABLE job_content (
                             job_content_id SERIAL PRIMARY KEY,
                             job_id INT NOT NULL REFERENCES jobs(job_id) ON DELETE CASCADE,
                             language_code CHAR(2) NOT NULL,
                             content_text TEXT NOT NULL,
                             UNIQUE (job_id, language_code)
);

//添加授权
ALTER TABLE users
    ADD COLUMN is_active BOOLEAN NOT NULL DEFAULT FALSE;