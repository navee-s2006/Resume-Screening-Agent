# Resume Screening Agent - Backend Setup

## Run Panna Steps

### 1. MySQL Ready Pannunga
MySQL server start pannunga (XAMPP/WAMP/local install edhu vena).
Database automatic ah create aagum (`createDatabaseIfNotExist=true` already set pannirukken).

### 2. application.properties Edit Pannunga
`src/main/resources/application.properties` file open pannunga:
- `spring.datasource.password` -> unga MySQL password podunga
- `gemini.api.key` -> https://aistudio.google.com poi free Gemini API key eduthu podunga

### 3. Project Import Pannunga
IntelliJ IDEA / Eclipse / VS Code la:
- File -> Open -> `resume-screener` folder select pannunga
- Maven dependencies automatic ah download aagum (internet venum)

### 4. Run Pannunga
`ResumeScreenerApplication.java` file open pannitu Run button click pannunga.
Terminal la idhu print aagum:
```
Resume Screener Backend Running Successfully
http://localhost:8080
```

### 5. Postman Vachi Test Pannunga

**a) Job create panna:**
```
POST http://localhost:8080/api/jobs
Body (JSON):
{
  "title": "Java Backend Developer",
  "description": "We need a Java developer with Spring Boot and MySQL experience.",
  "requiredSkills": "Java, Spring Boot, MySQL, REST API"
}
```

**b) Resume upload panna:**
```
POST http://localhost:8080/api/resumes/upload
Body (form-data):
  file -> (select PDF/DOCX file)
  name -> "Karthik"
  email -> "karthik@example.com"
```

**c) Screening trigger panna (Gemini call aagum):**
```
POST http://localhost:8080/api/screen/{candidateId}/{jobId}
```

**d) Results eduka:**
```
GET http://localhost:8080/api/results/{jobId}
```

## Project Structure
```
resume-screener/
├── pom.xml
└── src/main/
    ├── java/com/resumescreener/
    │   ├── ResumeScreenerApplication.java
    │   ├── entity/          (Candidate, JobDescription, ScreeningResult)
    │   ├── repository/      (Spring Data JPA interfaces)
    │   ├── service/         (ResumeParserService, GeminiService, ScreeningService)
    │   ├── controller/      (JobController, ResumeController, ScreeningController)
    │   └── dto/              (Request/Response objects)
    └── resources/
        └── application.properties
```

## Next Step
Backend fully working ah confirm pannitu, frontend (HTML/CSS/JS) build pannalam.
Frontend indha 4 endpoints ah than call pannum:
- POST /api/jobs
- POST /api/resumes/upload
- POST /api/screen/{candidateId}/{jobId}
- GET /api/results/{jobId}
