// ===== Static curated data (no external job API — as scoped) =====

// Job role -> required skills. Used by the search bar on Home.
const JOB_SKILLS_MAP = {
  "Java Backend Developer": ["Java", "Spring Boot", "MySQL", "REST API", "Git"],
  "Frontend Developer": ["HTML", "CSS", "JavaScript", "React", "Git"],
  "Full Stack Developer": ["JavaScript", "React", "Node.js", "MySQL", "Git", "REST API"],
  "Python Developer": ["Python", "Django", "MySQL", "REST API", "Git"],
  "Data Analyst": ["Python", "SQL", "Excel", "Power BI", "Statistics"],
  "Android Developer": ["Java", "Kotlin", "Android SDK", "Git", "REST API"],
  "DevOps Engineer": ["Linux", "Docker", "Git", "AWS", "CI/CD"],
  "UI/UX Designer": ["Figma", "Wireframing", "HTML", "CSS", "User Research"],
  "Machine Learning Engineer": ["Python", "Machine Learning", "Statistics", "SQL", "Git"],
  "Cloud Engineer": ["AWS", "Linux", "Docker", "Networking", "Git"]
};

// Skill -> where to learn it for free (curated, static).
// "platform" is shown as a clear badge so it's obvious WHERE it's available.
const SKILL_RESOURCES = {
  "Java": { name: "Java Programming", platform: "NPTEL", url: "https://nptel.ac.in/courses/106105191" },
  "Spring Boot": { name: "Spring Boot Full Course", platform: "YouTube", url: "https://www.youtube.com/results?search_query=spring+boot+freecodecamp+full+course" },
  "MySQL": { name: "SQL Tutorial", platform: "W3Schools", url: "https://www.w3schools.com/sql/" },
  "REST API": { name: "REST API Concepts", platform: "freeCodeCamp", url: "https://www.freecodecamp.org/news/tag/rest-api/" },
  "Git": { name: "Git & GitHub Crash Course", platform: "YouTube", url: "https://www.youtube.com/results?search_query=git+and+github+freecodecamp" },
  "HTML": { name: "HTML Basics", platform: "MDN Web Docs", url: "https://developer.mozilla.org/en-US/docs/Web/HTML" },
  "CSS": { name: "CSS Basics", platform: "MDN Web Docs", url: "https://developer.mozilla.org/en-US/docs/Web/CSS" },
  "JavaScript": { name: "JavaScript.info Course", platform: "javascript.info", url: "https://javascript.info/" },
  "React": { name: "Learn React", platform: "React Official Docs", url: "https://react.dev/learn" },
  "Node.js": { name: "Node.js Crash Course", platform: "freeCodeCamp", url: "https://www.freecodecamp.org/news/tag/node-js/" },
  "Python": { name: "Python for Everybody", platform: "Coursera (audit free)", url: "https://www.coursera.org/specializations/python" },
  "Django": { name: "Django Official Tutorial", platform: "Django Docs", url: "https://docs.djangoproject.com/en/stable/intro/tutorial01/" },
  "SQL": { name: "SQL Tutorial", platform: "W3Schools", url: "https://www.w3schools.com/sql/" },
  "Excel": { name: "Excel Basics", platform: "Microsoft Learn", url: "https://learn.microsoft.com/en-us/training/modules/excel-introduction/" },
  "Power BI": { name: "Power BI Fundamentals", platform: "Microsoft Learn", url: "https://learn.microsoft.com/en-us/training/powerplatform/power-bi" },
  "Statistics": { name: "Statistics & Probability", platform: "Khan Academy", url: "https://www.khanacademy.org/math/statistics-probability" },
  "Kotlin": { name: "Kotlin Getting Started", platform: "Kotlin Official Docs", url: "https://kotlinlang.org/docs/getting-started.html" },
  "Android SDK": { name: "Android Basics", platform: "Google Developers", url: "https://developer.android.com/courses" },
  "Linux": { name: "Linux Command Line", platform: "freeCodeCamp", url: "https://www.freecodecamp.org/news/tag/linux/" },
  "Docker": { name: "Docker Getting Started", platform: "Docker Official Docs", url: "https://docs.docker.com/get-started/" },
  "AWS": { name: "AWS Cloud Practitioner Essentials", platform: "AWS Skill Builder (Free)", url: "https://explore.skillbuilder.aws/learn/course/134/aws-cloud-practitioner-essentials" },
  "CI/CD": { name: "CI/CD Concepts", platform: "freeCodeCamp", url: "https://www.freecodecamp.org/news/tag/ci-cd/" },
  "Figma": { name: "Figma Basics", platform: "Figma Official Tutorials", url: "https://help.figma.com/hq/en/category/tutorials" },
  "Wireframing": { name: "Wireframing Guide", platform: "Adobe XD Ideas", url: "https://xd.adobe.com/ideas/process/wireframing/" },
  "User Research": { name: "UX Research Basics", platform: "Coursera (audit free)", url: "https://www.coursera.org/learn/user-research" },
  "Machine Learning": { name: "Machine Learning by Andrew Ng", platform: "Coursera (audit free)", url: "https://www.coursera.org/learn/machine-learning" },
  "Networking": { name: "Networking Basics", platform: "Cisco Networking Academy (Free)", url: "https://www.netacad.com/courses/networking" }
};

// Fallback if a skill isn't in our curated map — points to a general search.
function getResourceForSkill(skill) {
  if (SKILL_RESOURCES[skill]) return SKILL_RESOURCES[skill];
  return {
    name: `"${skill}" tutorial`,
    platform: "YouTube (search)",
    url: `https://www.youtube.com/results?search_query=${encodeURIComponent(skill + " tutorial free")}`
  };
}
