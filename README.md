```
██████╗ ███████╗███████╗██╗   ██╗███╗   ███╗███████╗
██╔══██╗██╔════╝██╔════╝██║   ██║████╗ ████║██╔════╝
██████╔╝█████╗  ███████╗██║   ██║██╔████╔██║█████╗
██╔══██╗██╔══╝  ╚════██║██║   ██║██║╚██╔╝██║██╔══╝
██║  ██║███████╗███████║╚██████╔╝██║ ╚═╝ ██║███████╗
╚═╝  ╚═╝╚══════╝╚══════╝ ╚═════╝ ╚═╝     ╚═╝╚══════╝
        S C R E E N I N G   A G E N T
```

### AI-Powered Resume Screening & Career Readiness Platform

**Prompt-Engineered · LLM-Graded · Full-Stack**

![Java](https://img.shields.io/badge/Java-17-orange) ![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.0-brightgreen) ![MySQL](https://img.shields.io/badge/MySQL-8-blue) ![Gemini](https://img.shields.io/badge/LLM-Google%20Gemini-purple) ![HTML CSS JS](https://img.shields.io/badge/Frontend-HTML%20%7C%20CSS%20%7C%20JS-yellow) ![License](https://img.shields.io/badge/License-MIT-lightgrey)

Resume Screener is a full-stack web application that screens resumes against job roles the way a recruiter would — parsing the resume, running it through a carefully engineered LLM prompt, and returning a match score, strengths, gaps, and a verdict. It also tells candidates exactly which skills they're missing for a role, and where to learn each one for free.

📖 [How it works](#-four-stage-screening-pipeline) · 🚀 [Quick Start](#-quick-start) · 🎯 [Features](#-key-features) · 🏗️ [Architecture](#-system-architecture) · ⚙️ [Configuration](#-configuration)

---

## 📑 Table of Contents

| Core Sections | Technical Deep Dives | Resources |
|---|---|---|
| 🎯 [Key Features](#-key-features) | 🏛️ [Four-Stage Pipeline](#-four-stage-screening-pipeline) | ⚙️ [Configuration](#-configuration) |
| 🏗️ [System Architecture](#-system-architecture) | 🧠 [Prompt Engineering](#-prompt-engineering) | 📁 [Project Structure](#-project-structure) |
| 💬 [Screening Walkthrough](#-screening-walkthrough) | 🗂️ [Database Schema](#-database-schema) | 🗺️ [Roadmap](#-roadmap) |
| 🚀 [Quick Start](#-quick-start) | 🎫 [The Match Ticket](#-the-match-ticket) | 👥 [Team](#-team) |

---

## 🎯 Key Features

| Feature | Description |
|---|---|
| 🔐 **Authentication** | Register/login with BCrypt-hashed passwords, stored in MySQL |
| 📄 **Resume Parsing** | Extracts text from PDF and DOCX resumes (Apache PDFBox / POI) |
| 🧠 **LLM Screening** | Google Gemini analyses resume vs. job description using a structured prompt |
| 📊 **Match Scoring** | 0–100 match score, strengths, gaps, and a final verdict per screening |
| 🎫 **Boarding-Pass Result** | Every screening result renders as a ticket — role, score, verdict stamp |
| 🔍 **Skill Search** | Search any job title to see its core skills and where to learn each one free |
| 🎯 **Skill Gap Resolver** | After screening, missing skills are matched to curated free resources |
| 🧭 **Role Suggestions** | Suggests other roles that better match the candidate's existing skills |
| 👤 **Profile & History** | Tracks full screening history per account — every scan, every day |
| 🌗 **Dark / Light Theme** | Full theme toggle across the entire app, persisted per user |
| 🛡️ **Graceful LLM Fallback** | Falls back to a content-aware heuristic scorer if the LLM quota is exhausted |

---

## 🏗️ System Architecture

```
┌──────────────────────────────────────────────────────────┐
│                   FRONTEND (HTML / CSS / JS)               │
│   Login · Register · Home · Analysis · Profile · About     │
└───────────────────────────┬────────────────────────────────┘
                            │  REST (JSON + multipart)
┌───────────────────────────▼────────────────────────────────┐
│                  BACKEND — Spring Boot (Java)                │
│  ┌───────────────┐ ┌────────────────┐ ┌───────────────────┐│
│  │ AuthController │ │ ResumeController│ │ ScreeningController││
│  └───────┬───────┘ └────────┬────────┘ └─────────┬─────────┘│
│          │                  │                     │          │
│  ┌───────▼──────────────────▼─────────────────────▼────────┐│
│  │        Service Layer (Parser · Prompt · Gemini)          ││
│  └───────────────────────────┬──────────────────────────────┘│
└──────────────────────────────┼────────────────────────────────┘
                               │
                ┌──────────────┴──────────────┐
                ▼                             ▼
      ┌───────────────────┐        ┌────────────────────┐
      │   MySQL Database    │        │   Google Gemini API │
      │ users · candidates   │        │  (LLM screening call)│
      │ jobs · results        │        └────────────────────┘
      └───────────────────┘
```

## 🔄 Four-Stage Screening Pipeline

```
┌─────────────────────────────────────────────────────────────┐
│                   📄  RESUME UPLOADED (PDF/DOCX)               │
└──────────────────────────┬────────────────────────────────────┘
                           │
        ╔══════════════════▼══════════════════════════════════╗
        ║        STAGE 1 · RESUME PARSING                       ║
        ║   Apache PDFBox / POI → plain text extraction         ║
        ║   Candidate name auto-detected from first line        ║
        ╚══════════════════╤════════════════════════════════════╝
                           │
        ╔══════════════════▼══════════════════════════════════╗
        ║        STAGE 2 · PROMPT ENGINEERING                   ║
        ║   Recruiter persona + numbered instructions +         ║
        ║   strict JSON output schema (score/strengths/gaps)    ║
        ╚══════════════════╤════════════════════════════════════╝
                           │
        ╔══════════════════▼══════════════════════════════════╗
        ║        STAGE 3 · LLM SCREENING (Gemini)               ║
        ║   Evidence-based scoring, grounded only in resume      ║
        ║   text — no assumed skills. Falls back to a            ║
        ║   content-aware heuristic scorer if quota is hit.      ║
        ╚══════════════════╤════════════════════════════════════╝
                           │
        ╔══════════════════▼══════════════════════════════════╗
        ║        STAGE 4 · STORE & TRACK                        ║
        ║   Result saved to MySQL · profile history updated ·    ║
        ║   missing skills mapped to free learning resources     ║
        ╚═══════════════════════════════════════════════════════╝
```

## 🧠 Prompt Engineering

Every screening call uses a structured prompt, not a raw question:

| Principle | How it's applied |
|---|---|
| **Role assignment** | "You are an expert technical recruiter with 10+ years of experience" |
| **Task decomposition** | Numbered instructions (score → strengths → gaps → verdict) |
| **Output constraints** | Strict JSON schema, no markdown/prose — reliable, parseable output |
| **Anti-hallucination guardrail** | Explicitly instructed not to assume skills absent from the resume text |
| **Low temperature** | `temperature: 0.2` for consistent, repeatable scoring |

## 🎫 The Match Ticket

Every screening result is rendered like a boarding pass — one candidate, one role, one honest verdict:

```
┌──────────────────────────────────────┬───────────────┐
│  BOARDING PASS — SCREENING RESULT      │               │
│  Java Backend Developer                │       76      │
│  Candidate: Naveena G   Gate: ROLE-001  │  MATCH SCORE  │
│  ▌▌│▌▌▌│▌│▌▌│▌▌▌│▌│▌▌ (barcode)          │ [STRONG MATCH]│
└──────────────────────────────────────┴───────────────┘
```

## 💬 Screening Walkthrough

```
User     →  Registers / logs in
Home     →  Searches "Java Backend Developer"
         →  Sees required skills + free resource for each
Analysis →  Selects role, uploads resume.pdf
         →  Backend extracts text, builds prompt, calls Gemini
         →  Ticket rendered: Score 76 · Strong Match
         →  Strengths: Java, project experience
         →  Gaps: Spring Boot not mentioned
         →  "Skills to develop": Spring Boot → available on YouTube
Profile  →  Latest score + full screening history saved
```

## 🗂️ Database Schema

| Table | Purpose |
|---|---|
| `app_user` | Registered accounts — username, email, hashed password, profile details |
| `candidate` | Each uploaded resume — name (auto-detected), email, extracted text |
| `job_description` | Job roles screened against — title, description, required skills |
| `screening_result` | One row per screening — score, strengths, gaps, verdict, timestamp |

## 📁 Project Structure

```
resume-screener/
├── pom.xml
└── src/main/
    ├── java/com/resumescreener/
    │   ├── ResumeScreenerApplication.java
    │   ├── config/          SecurityConfig (BCrypt bean)
    │   ├── entity/           User · Candidate · JobDescription · ScreeningResult
    │   ├── repository/       Spring Data JPA interfaces
    │   ├── service/          ResumeParserService · GeminiService · ScreeningService
    │   ├── controller/       AuthController · JobController · ResumeController · ScreeningController
    │   └── dto/              Request/response objects
    └── resources/
        └── application.properties

frontend/
├── login.html            Login
├── register.html          Sign up
├── home.html               Dashboard + job/skill search
├── analysis.html           Resume upload + screening ticket
├── profile.html            Personal details + screening history
├── about.html              Pipeline explainer
├── css/style.css           Design system (dark/light theme)
├── js/app.js               Theme, session, toast utilities
└── data/skills-resources.js   Curated job→skills, skill→free resource maps
```

## 🚀 Quick Start

### 1 · Clone & configure MySQL

```bash
git clone <repo-url> && cd resume-screener
```

```sql
CREATE DATABASE resume_screener;
```

### 2 · Set your credentials

Edit `src/main/resources/application.properties`:

```properties
spring.datasource.password=YOUR_MYSQL_PASSWORD
gemini.api.key=YOUR_GEMINI_API_KEY
gemini.use.mock=false
```

> Get a free Gemini API key at [aistudio.google.com/apikey](https://aistudio.google.com/apikey).
> If you hit a quota/billing wall, set `gemini.use.mock=true` — the app falls back to a content-aware heuristic scorer so the pipeline still works end-to-end.

### 3 · Run the backend

```bash
mvn spring-boot:run
```

Backend starts on `http://localhost:8080`.

### 4 · Open the frontend

Open `frontend/login.html` directly in your browser (or serve it with Live Server).

### 5 · Try it

Register → Log in → Search a role on Home → Upload a resume on Analysis → Check your score on Profile.

---

## ⚙️ Configuration

| Property | Default | Purpose |
|---|---|---|
| `spring.datasource.url` | `jdbc:mysql://localhost:3306/resume_screener` | MySQL connection |
| `gemini.api.key` | — | Google Gemini API key |
| `gemini.api.model` | `gemini-1.5-flash` | Gemini model used for screening |
| `gemini.use.mock` | `false` | Use content-aware mock scorer instead of calling Gemini |
| `spring.servlet.multipart.max-file-size` | `10MB` | Max resume upload size |

---

## 🗺️ Roadmap

| Stage | Status | Description |
|---|---|---|
| 1 | ✅ Done | Spring Boot + MySQL backend, resume parsing, REST APIs |
| 2 | ✅ Done | Gemini LLM screening with engineered prompt + JSON schema |
| 3 | ✅ Done | Authentication, profile, full screening history |
| 4 | ✅ Done | Frontend: login/register/home/analysis/profile/about, dark/light theme |
| 5 | ✅ Done | Skill-gap resolver with curated free learning resources |
| 6 | 🔲 Planned | Real job-board API integration, PDF report export |

---

## 👥 Team

Built as a college project — Resume Screening Agent, powered by Spring Boot, MySQL, and Google Gemini.

## 🛠️ Built With

Java · Spring Boot · MySQL · Google Gemini API · HTML · CSS · JavaScript · Apache PDFBox · Apache POI
