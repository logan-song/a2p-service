export async function saveNote(user, content) {
    const res = await fetch(`/note/api/${user}`, {
        method: "PUT",
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify({content})
    });

    if (!res.ok) throw new Error("저장 실패");
}

export async function getNote(user) {
    const res = await fetch(`/note/api/${user}`);
    if (!res.ok) throw new Error("조회 실패");

    return await res.json();
}
