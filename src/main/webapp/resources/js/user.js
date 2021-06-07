let stompClient = null;

initializeList();
connect();

function connect() {

    const socket = new SockJS('/endpoint');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {

        stompClient.subscribe('/app/private', function (msg) {
            onMessage(JSON.parse(msg.body));
        });
    });
}

function onMessage(msg) {
    // if (msg.type === "info") {
    //     updateList(msg);
    // }
}

function initializeList(){
    // const list = document.querySelector("#robots");
    // for (let i = 0; i < list.children.length; i++) {
    //     setStatus(list.children[i], list.children[i].querySelector("label").innerText);
    // }
}

function updateList(message) {
    // const id = message.robotId;
    // const status = message.status.toString();
    // const list = document.querySelector("#robots");
    // const li = list.querySelector(`#id${id}`);
    // setStatus(li, status);
}

function setStatus(listItem, status){

    // const label = listItem.querySelector("label");
    // const btn = listItem.querySelector("button");
    // const lamp = btn.querySelector("canvas");
    // switch (status){
    //     case "0":
    //         label.innerText = "Offline";
    //         btn.disabled = true;
    //         lamp.className = "lamp-red";
    //         break;
    //     case "1":
    //         label.innerText = "Ready";
    //         btn.disabled = false;
    //         lamp.className = "lamp-green";
    //         break;
    //     case "2":
    //         label.innerText = "In use";
    //         btn.disabled = true;
    //         lamp.className = "lamp-blue";
    //         break;
    // }
}

function setup(){
    const lis = document.querySelector("#robots").children;
    for (let li in lis) {
        li.querySelector("#settings").addEventListener("click", )
    }
}

function displaySettings(robotId){

}