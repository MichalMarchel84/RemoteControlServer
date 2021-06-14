let stompClient = null;

init();
connect();

function connect() {

    const socket = new SockJS('/endpoint');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {

        stompClient.subscribe('/app/public', function (msg) {
            onMessage(JSON.parse(msg.body));
        });
    }, function () {
        window.setTimeout(connect, 3000);
    });
}

function onMessage(msg) {
    if (msg.type === "info") {
        updateList(msg);
    }
}

function init(){
    const list = document.querySelector("#robots");
    for (let i = 0; i < list.children.length; i++) {
        setStatus(list.children[i], list.children[i].querySelector("label").innerText);
    }
    setArticle("art1", "art1cont");
    setArticle("art2", "art2cont");
    setArticle("art3", "art3cont");
}

function setArticle(artName, bodyName){
    const head = document.querySelector("#" + artName);
    head.addEventListener("click", () => {
        const body = document.querySelector("#" + bodyName);
        if(body.style.display === "none") {
            body.style.display = "flex";
            head.querySelector("img").style.transform = "rotate(-90deg)";
        }
        else {
            body.style.display = "none";
            head.querySelector("img").style.transform = "";
        }
    });
}

function updateList(message) {
    const id = message.robotId;
    const status = message.status.toString();
    const list = document.querySelector("#robots");
    const li = list.querySelector(`#id${id}`);
    setStatus(li, status);
}

function setStatus(listItem, status){

    const label = listItem.querySelector("label");
    const btn = listItem.querySelector("button");
    const lamp = btn.querySelector("canvas");
    switch (status){
        case "0":
            label.innerText = "Offline";
            btn.disabled = true;
            lamp.className = "lamp-red";
            break;
        case "1":
            label.innerText = "Ready";
            btn.disabled = false;
            lamp.className = "lamp-green";
            break;
        case "2":
            label.innerText = "In use";
            btn.disabled = true;
            lamp.className = "lamp-blue";
            break;
    }
}