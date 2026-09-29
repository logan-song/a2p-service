import {showAlert} from "./dialog.js";

export function limitText(value, max) {
    if (value.length > max) {
        showAlert(`아쉽지만 최대 ${max}자 까지만 입력 가능해요.`);
        return value.substring(0, max);
    }
    return value;
}

export function debounce(fn, delay) {
    let timeout;
    return (...args) => {
        clearTimeout(timeout);
        timeout = setTimeout(() => fn(...args), delay);
    };
}
