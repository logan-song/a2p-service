let tooltip;
let hideTimeout;

function getTooltip() {
    if (tooltip) return tooltip;

    tooltip = document.createElement("div");
    tooltip.className = "app-tooltip";
    tooltip.setAttribute("role", "tooltip");
    tooltip.hidden = true;
    document.body.appendChild(tooltip);

    return tooltip;
}

function showTooltip(trigger) {
    const element = getTooltip();
    const rect = trigger.getBoundingClientRect();
    const position = trigger.dataset.tooltipPosition || "bottom";
    const message = trigger.dataset.tooltip;
    const highlight = trigger.dataset.tooltipHighlight;

    clearTimeout(hideTimeout);
    element.replaceChildren();

    if (highlight && message.includes("{highlight}")) {
        const [before, after] = message.split("{highlight}");
        const highlightElement = document.createElement("span");

        highlightElement.className = "app-tooltip-highlight";
        highlightElement.textContent = highlight;
        element.append(before, highlightElement, after);
    } else {
        element.textContent = message;
    }

    element.dataset.position = position;
    element.hidden = false;
    element.classList.remove("visible");

    const left = Math.min(
        window.innerWidth - element.offsetWidth / 2 - 8,
        Math.max(element.offsetWidth / 2 + 8, rect.left + rect.width / 2)
    );

    element.style.left = `${left}px`;
    element.style.top = position === "top"
        ? `${rect.top - element.offsetHeight - 10}px`
        : `${rect.bottom + 10}px`;

    requestAnimationFrame(() => element.classList.add("visible"));
}

function hideTooltip() {
    if (!tooltip) return;

    tooltip.classList.remove("visible");
    hideTimeout = setTimeout(() => {
        tooltip.hidden = true;
    }, 150);
}

export function initTooltips() {
    document.querySelectorAll("[data-tooltip]").forEach(trigger => {
        trigger.addEventListener("mouseenter", () => showTooltip(trigger));
        trigger.addEventListener("mouseleave", hideTooltip);
        trigger.addEventListener("focusin", () => showTooltip(trigger));
        trigger.addEventListener("focusout", hideTooltip);
    });
}
