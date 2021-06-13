let stompClient = null;

init();
connect();

function connect() {

    const socket = new SockJS('/endpoint');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {

        stompClient.subscribe('/app/private', function (msg) {
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
    const lis = [...document.querySelector("#robots").children];
    lis.forEach(li => {
        li.querySelector("#delete").addEventListener("click", () => deleteDevice(li.id.replace("id", "")));
        setStatus(li, li.dataset.status);
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

    const btn = listItem.querySelector("button");
    const lamp = btn.querySelector("canvas");
    switch (status){
        case "0":
            btn.disabled = true;
            lamp.className = "lamp-red";
            break;
        case "1":
            btn.disabled = false;
            lamp.className = "lamp-green";
            break;
        case "2":
            btn.disabled = true;
            lamp.className = "lamp-blue";
            break;
    }
}

function deleteDevice(id){
    if(confirm("Sure delete?")){
        fetch("/user/delete?robotId=" + id,
            {}).then(resp => {
                if(resp.ok){
                    document.querySelector("#id" + id).remove();
                }
        })
    }
}