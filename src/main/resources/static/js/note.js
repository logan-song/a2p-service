import {saveNote} from './api.js';
import {debounce, limitText} from './util.js';
import {connectSocket} from "./socket.js";
import {showAlert, showConfirm} from "./dialog.js";
import {initTooltips} from "./tooltip.js";

const MAX_LENGTH = 10000;

const storageUsername = localStorage.getItem('username');

if (!storageUsername) {
    window.location.href = `/note/setup`;
}

export function initNote() {
    const counterTooltip = document.querySelector("[data-tooltip-type='counter']");
    const formattedMaxLength = MAX_LENGTH.toLocaleString("en-US");

    counterTooltip.dataset.tooltip = "현재 입력된 글자수예요.\n최대 글자수는 {highlight}";
    counterTooltip.dataset.tooltipHighlight = `${formattedMaxLength}자!`;
    initTooltips();

    const textarea = document.getElementById("note");
    const highlight = document.getElementById("highlight");
    const counter = document.getElementById("counter");
    const counterCurrent = document.getElementById("counterCurrent");
    const clear = document.getElementById("clear");
    const searchContainer = document.getElementById("searchContainer");
    const searchInput = document.getElementById("searchInput");
    const searchPrev = document.getElementById("searchPrev");
    const searchNext = document.getElementById("searchNext");
    const searchCount = document.getElementById("searchCount");
    const username = textarea.dataset.user;
    const user = document.querySelector("select[name=user]");
    const owner = document.getElementById("owner");
    const viewers = document.getElementById("viewers");
    const viewerExtra = document.getElementById("viewerExtra");
    const viewerList = document.getElementById("viewerList");

    counter.setAttribute("aria-valuemax", String(MAX_LENGTH));

    const myNoteOption = [...user.options]
        .find(option => option.value === storageUsername);

    if (myNoteOption) {
        myNoteOption.textContent = `* ${myNoteOption.textContent}`;
        user.insertBefore(myNoteOption, user.options[1] ?? null);
    }

    let lastContent = textarea.value;
    let isTyping = false;
    let matches = [];
    let currentMatch = -1;

    function getContent() {
        return textarea.value;
    }

    function updateLength() {
        const len = getContent().length;
        counterCurrent.textContent = `${len.toLocaleString("en-US")}자`;
        counter.style.setProperty("--usage", `${len / MAX_LENGTH * 100}%`);
        counter.setAttribute("aria-valuenow", String(len));
        counter.classList.toggle("is-warning", len >= 7000 && len < 9000);
        counter.classList.toggle("is-danger", len >= 9000);
        return len;
    }

    function init() {
        textarea.focus();
        highlight.textContent = getContent();
        const len = updateLength();
        textarea.setSelectionRange(len, len);
        textarea.scrollTop = textarea.scrollHeight;
        highlight.scrollTop = textarea.scrollHeight;
    }

    function escapeHtml(text) {
        return text
            .replace(/&/g, "&amp;")
            .replace(/</g, "&lt;")
            .replace(/>/g, "&gt;");
    }

    function renderHighlight() {
        let html = escapeHtml(getContent());
        const keyword = searchInput.value;

        if (keyword) {
            const regex = new RegExp(
                keyword.replace(/[.*+?^${}()|[\]\\]/g, "\\$&"),
                "gi"
            );

            html = html.replace(regex, "<mark>$&</mark>");
        }

        highlight.innerHTML = html;

        updateSearchResult();
    }

    function updateSearchResult() {
        if (searchInput.value.trim().length <= 0) {
            searchCount.textContent = "";
            return;
        }

        matches = [...highlight.querySelectorAll("mark")];

        if (matches.length === 0) {
            currentMatch = -1;
            searchCount.textContent = "0 / 0";
            return;
        }

        if (currentMatch < 0) {
            currentMatch = 0;
        }

        if (currentMatch >= matches.length) {
            currentMatch = matches.length - 1;
        }

        matches.forEach(mark => mark.classList.remove("active"));

        matches[currentMatch].classList.add("active");

        searchCount.textContent = `${currentMatch + 1} / ${matches.length}`;
    }

    function moveToMatch() {
        if (!matches.length) return;

        const mark = matches[currentMatch];

        const top = mark.offsetTop - highlight.clientHeight / 2;

        highlight.scrollTop = top;
        textarea.scrollTop = top;
    }

    searchInput.addEventListener("input", () => {
        currentMatch = 0;
        renderHighlight();
        moveToMatch();
    });

    searchNext.addEventListener("click", () => {
        if (!matches.length) {
            return;
        }

        currentMatch = (currentMatch + 1) % matches.length;

        updateSearchResult();

        moveToMatch();
    });

    searchPrev.addEventListener("click", () => {
        if (!matches.length) {
            return;
        }

        currentMatch--;

        if (currentMatch < 0) {
            currentMatch = matches.length - 1;
        }

        updateSearchResult();

        moveToMatch();
    })

    const client = connectSocket(
        username,
        storageUsername,
        (data) => {
            if (isTyping) return;

            textarea.value = data.content;
            lastContent = data.content;
            updateLength();
            renderHighlight();
        },
        (connectedViewers) => {
            viewers.hidden = connectedViewers.length === 0;
            viewerExtra.textContent = connectedViewers.length > 1
                ? `+${connectedViewers.length - 1}`
                : "";
            viewerList.textContent = connectedViewers
                .map(viewer => viewer.name)
                .join(", ");
        }
    );

    function sendSocket(content) {
        client.publish({
            destination: `/app/note.update.${username}`,
            body: JSON.stringify({
                username,
                content,
                timestamp: Date.now()
            })
        })
    }

    async function save() {
        const content = getContent();

        if (content === lastContent) return;

        lastContent = content;

        try {
            await saveNote(username, content);

            sendSocket(content);
        } catch (e) {
            showAlert(e.message);
        }
    }

    const debouncedSave = debounce(save, 500);

    textarea.addEventListener("scroll", () => {
        highlight.scrollTop = textarea.scrollTop;
        highlight.scrollLeft = textarea.scrollLeft;
    });

    textarea.addEventListener("input", () => {
        isTyping = true;
        const original = getContent();
        const limited = limitText(getContent(), MAX_LENGTH);
        const pos = textarea.selectionStart;

        textarea.value = limited;
        if (original !== limited) {
            textarea.value = limited;

            const newPos = Math.min(pos, limited.length);
            textarea.setSelectionRange(newPos, newPos);
        }

        updateLength();
        renderHighlight();

        debouncedSave();

        setTimeout(() => {
            isTyping = false;
        }, 600);
    });

    owner.addEventListener("click", async () => {
        // 체크된 상태일 때만 confirm
        if (owner.checked) {
            const confirmed = await showConfirm("현재 보고 있는 노트를 내 노트로 지정하시겠어요?");

            if (!confirmed) {
                owner.checked = false;
                return;
            }
        }

        localStorage.setItem("username", textarea.dataset.user);
        window.location.reload();
    });

    clear.addEventListener("click", async () => {
        const confirmed = await showConfirm("모든 내용을 지우고 저장하시겠어요?", "삭제");

        if (confirmed) {
            textarea.value = "";
            renderHighlight();
            save();
        }

        requestAnimationFrame(() => {
            textarea.focus();

            const position = textarea.value.length;
            textarea.setSelectionRange(position, position);
        });
    });

    user.addEventListener("change", (e) => {
        if (e.target.value) {
            window.location.href = `./${e.target.value}`;
        }
    })

    window.addEventListener("DOMContentLoaded", init);

    document.addEventListener("keydown", (e) => {
        if ((e.ctrlKey || e.metaKey) && e.key.toLowerCase() === "f") {
            e.preventDefault();

            searchContainer.classList.add("active");

            setTimeout(() => {
                const selected = window.getSelection().toString().trim();

                if (selected && !searchInput.value) {
                    searchInput.value = selected;

                    renderHighlight();
                }

                const len = searchInput.value.length;
                searchInput.setSelectionRange(len, len);
                searchInput.focus();
            }, 300)
        }

        if (e.key === "Escape") {
            searchInput.value = "";
            renderHighlight();
            textarea.focus();
            searchContainer.classList.remove("active");
        }
    });

    searchInput.addEventListener("keydown", (e) => {
        if (e.key !== "Enter") {
            return;
        }

        e.preventDefault();

        if (e.shiftKey) {
            searchPrev.click();
        } else {
            searchNext.click();
        }
    })
    if (storageUsername === textarea.dataset.user) {
        owner.checked = true;
        owner.disabled = true;
    }
}
