import {initTheme} from './theme.js';

document.addEventListener("DOMContentLoaded", () => {
    initTheme();

    const downloadLink = document.getElementById("desktopDownload");
    const downloadClose = document.getElementById("desktopDownloadClose");
    const isElectron = navigator.userAgent.includes("Electron")
        || navigator.userAgent.includes("A2PDesktop")
        || Boolean(window.electronAPI);
    const isDownloadDismissed = sessionStorage.getItem("desktopDownloadDismissed") === "true";

    downloadLink.hidden = isElectron || isDownloadDismissed;

    downloadClose.addEventListener("click", () => {
        downloadLink.hidden = true;
        sessionStorage.setItem("desktopDownloadDismissed", "true");
    });
});
