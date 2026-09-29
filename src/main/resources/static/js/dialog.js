function showDialog(message, {showCancel = false, confirmText = "확인"} = {}) {
    const dialog = document.getElementById("appDialog");
    const messageElement = document.getElementById("appDialogMessage");
    const cancelButton = document.getElementById("appDialogCancel");
    const confirmButton = document.getElementById("appDialogConfirm");

    if (dialog.open) {
        return Promise.resolve(false);
    }

    messageElement.textContent = message;
    cancelButton.hidden = !showCancel;
    confirmButton.textContent = confirmText;
    dialog.returnValue = "";

    return new Promise(resolve => {
        dialog.addEventListener("close", () => {
            resolve(dialog.returnValue === "confirm");
        }, {once: true});

        dialog.showModal();
        (showCancel ? cancelButton : confirmButton).focus();
    });
}

export function showAlert(message) {
    return showDialog(message);
}

export function showConfirm(message, confirmText = "확인") {
    return showDialog(message, {showCancel: true, confirmText});
}
