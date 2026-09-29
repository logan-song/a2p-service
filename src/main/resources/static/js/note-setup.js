export function initNoteSetup() {
    const storageUsername = localStorage.getItem('username');

    if (storageUsername) {
        window.location.href = `/note/${storageUsername}`;
    }

    const usernameInput = document.getElementById("username")
    const btnStart = document.querySelector("button.btn-start");

    btnStart.addEventListener('click', () => {
        localStorage.setItem("username", usernameInput.value);
        window.location.href = `/note/${usernameInput.value}`;
    })
}
