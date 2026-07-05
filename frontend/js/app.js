// ===== Shared utilities used across all pages =====

const API_BASE = "https://resume-screening-agent-bqdf.onrender.com/api";

/* ---------- Theme (dark/light) ---------- */
function initTheme() {
  const saved = localStorage.getItem("theme") || "dark";
  document.documentElement.setAttribute("data-theme", saved);
  updateThemeIcon(saved);
}
function toggleTheme() {
  const current = document.documentElement.getAttribute("data-theme");
  const next = current === "dark" ? "light" : "dark";
  document.documentElement.setAttribute("data-theme", next);
  localStorage.setItem("theme", next);
  updateThemeIcon(next);
}
function updateThemeIcon(theme) {
  const btn = document.getElementById("themeToggle");
  if (btn) btn.textContent = theme === "dark" ? "☀" : "☾";
}

/* ---------- Auth (localStorage-based session) ---------- */
function getCurrentUser() {
  const raw = localStorage.getItem("currentUser");
  return raw ? JSON.parse(raw) : null;
}
function setCurrentUser(user) {
  localStorage.setItem("currentUser", JSON.stringify(user));
}
function logout() {
  localStorage.removeItem("currentUser");
  window.location.href = "login.html";
}
function requireAuth() {
  const user = getCurrentUser();
  if (!user) {
    window.location.href = "login.html";
    return null;
  }
  return user;
}

/* ---------- Nav bar user badge ---------- */
function renderNavUser() {
  const user = getCurrentUser();
  const badge = document.getElementById("navAvatar");
  if (badge && user) {
    badge.textContent = (user.username || user.fullName || "U").charAt(0).toUpperCase();
    badge.title = user.username;
  }
}

/* ---------- Toast messages ---------- */
function showToast(message, type = "success") {
  let toast = document.getElementById("toast");
  if (!toast) {
    toast = document.createElement("div");
    toast.id = "toast";
    toast.className = "toast";
    document.body.appendChild(toast);
  }
  toast.textContent = message;
  toast.className = `toast show ${type}`;
  clearTimeout(window._toastTimer);
  window._toastTimer = setTimeout(() => toast.classList.remove("show"), 3200);
}

/* ---------- Init common bits on every page ---------- */
document.addEventListener("DOMContentLoaded", () => {
  initTheme();
  renderNavUser();
  const themeBtn = document.getElementById("themeToggle");
  if (themeBtn) themeBtn.addEventListener("click", toggleTheme);
  const logoutBtn = document.getElementById("logoutBtn");
  if (logoutBtn) logoutBtn.addEventListener("click", logout);
});
