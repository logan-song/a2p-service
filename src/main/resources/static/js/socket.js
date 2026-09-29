export function connectSocket(username, viewerUser, onMessage, onPresence) {
    const wsUrl = "ws://192.168.2.6:9082";
    // const wsUrl = "ws://localhost:9082";
    const client = new StompJs.Client({
        brokerURL: `${wsUrl}/ws`
    });

    client.onConnect = () => {
        client.subscribe(`/channel/note.${username}`, (msg) => {
            const data = JSON.parse(msg.body);
            onMessage(data);
        });

        client.subscribe(`/channel/note.presence.${username}`, (msg) => {
            const viewers = JSON.parse(msg.body);
            onPresence(viewers);
        });

        client.publish({
            destination: `/app/note.presence.join.${username}`,
            body: viewerUser
        });
    };

    client.onStompError = (frame) => {
        console.error("STOMP 에러", frame);
    }

    client.activate();

    window.addEventListener("pagehide", () => {
        client.deactivate({force: true});
    }, {once: true});

    return client;
}
