let stompClient = null;

initializeList();
connect();

function connect() {

    const socket = new SockJS('/endpoint');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {

        stompClient.subscribe('/app/public', function (msg) {
            onMessage(JSON.parse(msg.body));
        });
    });
}

function onMessage(msg) {
    if (msg.type === "info") {
        updateList(msg);
    }
}

function initializeList(){
    const list = document.querySelector("#robots");
    for (let i = 0; i < list.children.length; i++) {
        const label = list.children[i].querySelector("label");
        const button = list.children[i].querySelector("button");
        switch (label.innerText){
            case "0":
                label.innerText = "Offline";
                button.disabled = true;
                break;
            case "1":
                label.innerText = "Ready";
                break;
            case "2":
                label.innerText = "In use";
                button.disabled = true;
                break;
        }
    }
}

function updateList(message) {
    const id = message.robotId;
    const status = message.status;
    const list = document.querySelector("#robots");
    const li = list.querySelector(`#id${id}`);
    switch (status){
        case 0:
            li.querySelector("label").innerText = "Offline";
            li.querySelector("button").disabled = true;
            break;
        case 1:
            li.querySelector("label").innerText = "Ready";
            li.querySelector("button").disabled = false;
            break;
        case 2:
            li.querySelector("label").innerText = "In use";
            li.querySelector("button").disabled = true;
            break;
    }

}