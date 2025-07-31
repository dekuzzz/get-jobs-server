-- WARNING: This schema is for context only and is not meant to be run.
-- Table order and constraints may not be valid for execution.

CREATE TABLE public.clerk_users (
  clerk_user_id character varying NOT NULL,
  email character varying,
  phone character varying,
  created_at timestamp with time zone DEFAULT now(),
  updated_at timestamp with time zone DEFAULT now(),
  CONSTRAINT clerk_users_pkey PRIMARY KEY (clerk_user_id)
);
CREATE TABLE public.companies (
  company_id bigint NOT NULL DEFAULT nextval('companies_company_id_seq'::regclass),
  clerk_user_id character varying NOT NULL,
  full_name character varying,
  official_website character varying,
  location character varying,
  language_code character varying,
  size character varying,
  category_tags character varying,
  email_address character varying,
  created_at timestamp with time zone DEFAULT now(),
  updated_at timestamp with time zone DEFAULT now(),
  CONSTRAINT companies_pkey PRIMARY KEY (company_id),
  CONSTRAINT companies_clerk_user_id_fkey FOREIGN KEY (clerk_user_id) REFERENCES public.clerk_users(clerk_user_id)
);
CREATE TABLE public.company_content (
  company_content_id bigint NOT NULL DEFAULT nextval('company_content_company_content_id_seq'::regclass),
  company_id bigint NOT NULL,
  language_code character varying NOT NULL,
  slogan character varying,
  support character varying,
  description character varying,
  CONSTRAINT company_content_pkey PRIMARY KEY (company_content_id),
  CONSTRAINT company_content_company_id_fkey FOREIGN KEY (company_id) REFERENCES public.companies(company_id)
);
CREATE TABLE public.company_links (
  company_link_id integer NOT NULL DEFAULT nextval('company_links_company_link_id_seq'::regclass),
  company_id integer NOT NULL,
  link_type character varying NOT NULL CHECK (link_type::text = ANY (ARRAY['x'::character varying, 'telegram'::character varying, 'discord'::character varying, 'github'::character varying, 'linkedin'::character varying, 'facebook'::character varying]::text[])),
  url text NOT NULL,
  CONSTRAINT company_links_pkey PRIMARY KEY (company_link_id),
  CONSTRAINT company_links_company_id_fkey FOREIGN KEY (company_id) REFERENCES public.companies(company_id)
);
CREATE TABLE public.job_content (
  job_content_id bigint NOT NULL DEFAULT nextval('job_content_job_content_id_seq'::regclass),
  job_id bigint NOT NULL,
  language_code character varying NOT NULL,
  content_text character varying NOT NULL,
  CONSTRAINT job_content_pkey PRIMARY KEY (job_content_id),
  CONSTRAINT job_content_job_id_fkey FOREIGN KEY (job_id) REFERENCES public.jobs(job_id)
);
CREATE TABLE public.job_qualification (
  job_qualification_id bigint NOT NULL DEFAULT nextval('job_qualification_job_qualification_id_seq'::regclass),
  job_id bigint NOT NULL,
  language_code character varying NOT NULL,
  qualification_text character varying NOT NULL,
  CONSTRAINT job_qualification_pkey PRIMARY KEY (job_qualification_id),
  CONSTRAINT job_qualification_job_id_fkey FOREIGN KEY (job_id) REFERENCES public.jobs(job_id)
);
CREATE TABLE public.job_responsibility (
  job_responsibility_id bigint NOT NULL DEFAULT nextval('job_responsibility_job_responsibility_id_seq'::regclass),
  job_id bigint NOT NULL,
  language_code character varying NOT NULL,
  responsibility_text character varying NOT NULL,
  CONSTRAINT job_responsibility_pkey PRIMARY KEY (job_responsibility_id),
  CONSTRAINT job_responsibility_job_id_fkey FOREIGN KEY (job_id) REFERENCES public.jobs(job_id)
);
CREATE TABLE public.job_seeker_links (
  job_seeker_link_id bigint NOT NULL DEFAULT nextval('job_seeker_links_job_seeker_link_id_seq'::regclass),
  job_seeker_id bigint NOT NULL,
  link_type character varying NOT NULL CHECK (link_type::text = ANY (ARRAY['twitter'::character varying, 'qq'::character varying, 'github'::character varying, 'linkedin'::character varying, 'portfolio'::character varying]::text[])),
  url character varying NOT NULL,
  CONSTRAINT job_seeker_links_pkey PRIMARY KEY (job_seeker_link_id),
  CONSTRAINT job_seeker_links_job_seeker_id_fkey FOREIGN KEY (job_seeker_id) REFERENCES public.job_seekers(job_seeker_id)
);
CREATE TABLE public.job_seekers (
  job_seeker_id bigint NOT NULL DEFAULT nextval('job_seekers_job_seeker_id_seq'::regclass),
  clerk_user_id character varying NOT NULL UNIQUE,
  full_name character varying,
  location character varying,
  created_at timestamp with time zone DEFAULT now(),
  updated_at timestamp with time zone DEFAULT now(),
  language_code character varying,
  CONSTRAINT job_seekers_pkey PRIMARY KEY (job_seeker_id),
  CONSTRAINT job_seekers_clerk_user_id_fkey FOREIGN KEY (clerk_user_id) REFERENCES public.clerk_users(clerk_user_id)
);
CREATE TABLE public.jobs (
  job_id bigint NOT NULL DEFAULT nextval('jobs_job_id_seq'::regclass),
  company_id bigint NOT NULL,
  language_code character varying NOT NULL,
  job_name character varying,
  office_mode character varying,
  work_nature character varying NOT NULL CHECK (work_nature::text = ANY (ARRAY['full-time'::character varying, 'part-time'::character varying]::text[])),
  location character varying,
  min_salary double precision,
  max_salary double precision,
  currency_code character varying,
  settlement character varying,
  is_active boolean NOT NULL,
  created_at timestamp with time zone DEFAULT now(),
  updated_at timestamp with time zone DEFAULT now(),
  update_time timestamp with time zone DEFAULT now(),
  CONSTRAINT jobs_pkey PRIMARY KEY (job_id),
  CONSTRAINT jobs_company_id_fkey FOREIGN KEY (company_id) REFERENCES public.companies(company_id)
);
CREATE TABLE public.resume_advantages (
  resume_advantage_id bigint NOT NULL DEFAULT nextval('resume_advantages_resume_advantage_id_seq'::regclass),
  resume_id bigint NOT NULL,
  language_code character varying NOT NULL,
  content character varying,
  description character varying,
  CONSTRAINT resume_advantages_pkey PRIMARY KEY (resume_advantage_id),
  CONSTRAINT resume_advantages_resume_id_fkey FOREIGN KEY (resume_id) REFERENCES public.resumes(resume_id)
);
CREATE TABLE public.resume_educations (
  resume_education_id bigint NOT NULL DEFAULT nextval('resume_educations_resume_education_id_seq'::regclass),
  resume_id bigint NOT NULL,
  language_code character varying NOT NULL,
  institution character varying NOT NULL,
  degree character varying,
  field_of_study character varying,
  start_date date,
  end_date date,
  description character varying,
  CONSTRAINT resume_educations_pkey PRIMARY KEY (resume_education_id),
  CONSTRAINT resume_educations_resume_id_fkey FOREIGN KEY (resume_id) REFERENCES public.resumes(resume_id)
);
CREATE TABLE public.resume_experiences (
  resume_experience_id bigint NOT NULL DEFAULT nextval('resume_experiences_resume_experience_id_seq'::regclass),
  resume_id bigint NOT NULL,
  language_code character varying NOT NULL,
  company_name character varying NOT NULL,
  position character varying NOT NULL,
  working_time character varying,
  start_date date,
  end_date date,
  description character varying,
  CONSTRAINT resume_experiences_pkey PRIMARY KEY (resume_experience_id),
  CONSTRAINT resume_experiences_resume_id_fkey FOREIGN KEY (resume_id) REFERENCES public.resumes(resume_id)
);
CREATE TABLE public.resume_languages (
  resume_language_id bigint NOT NULL DEFAULT nextval('resume_languages_resume_language_id_seq'::regclass),
  resume_id bigint NOT NULL,
  language_code character varying NOT NULL,
  language_name character varying NOT NULL,
  proficiency character varying,
  CONSTRAINT resume_languages_pkey PRIMARY KEY (resume_language_id),
  CONSTRAINT resume_languages_resume_id_fkey FOREIGN KEY (resume_id) REFERENCES public.resumes(resume_id)
);
CREATE TABLE public.resume_skills (
  resume_skill_id bigint NOT NULL DEFAULT nextval('resume_skills_resume_skill_id_seq'::regclass),
  resume_id bigint NOT NULL,
  language_code character varying NOT NULL,
  skill_name character varying NOT NULL,
  proficiency character varying,
  CONSTRAINT resume_skills_pkey PRIMARY KEY (resume_skill_id),
  CONSTRAINT resume_skills_resume_id_fkey FOREIGN KEY (resume_id) REFERENCES public.resumes(resume_id)
);
CREATE TABLE public.resumes (
  resume_id bigint NOT NULL DEFAULT nextval('resumes_resume_id_seq'::regclass),
  job_seeker_id bigint NOT NULL,
  avatar character varying,
  name character varying,
  phone_number character varying,
  email_address character varying,
  telegram character varying,
  wechat character varying,
  office_mode character varying,
  work_nature character varying NOT NULL CHECK (work_nature::text = ANY (ARRAY['full-time'::character varying, 'part-time'::character varying]::text[])),
  permission character varying,
  position character varying,
  min_salary double precision,
  max_salary double precision,
  currency_code character varying,
  settlement character varying,
  created_at timestamp with time zone DEFAULT now(),
  updated_at timestamp with time zone DEFAULT now(),
  language_code character varying,
  CONSTRAINT resumes_pkey PRIMARY KEY (resume_id),
  CONSTRAINT resumes_job_seeker_id_fkey FOREIGN KEY (job_seeker_id) REFERENCES public.job_seekers(job_seeker_id)
);