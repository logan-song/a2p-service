export function initTheme() {
    const btn = document.getElementById("themeToggle");
    const icon = document.getElementById("themeIcon");

    function applyTheme(theme) {
        document.documentElement.setAttribute("data-theme", theme);
        icon.textContent = theme === "dark" ? "☀️" : "🌙";
        localStorage.setItem("theme", theme);
    }

    function toggle() {
        const current = document.documentElement.getAttribute("data-theme") || "light";
        applyTheme(current === "dark" ? "light" : "dark");
    }

    btn.addEventListener("click", toggle);

    const saved = localStorage.getItem("theme");
    const theme = saved || (window.matchMedia("(prefers-color-scheme: dark)").matches ? "dark" : "light");

    applyTheme(theme);
}
