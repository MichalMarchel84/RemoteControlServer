let stompClient = null;
connect();

function connect() {

    const socket = new SockJS('/endpoint');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {

        stompClient.subscribe('/app/public', function (message) {
            updateList(JSON.parse(message.body));
        });
    });
}

function updateList(message){
    const data = JSON.parse(message.data);
    const id = data.robotId;
    const status = data.status;
    const list = document.querySelector("#robots");
    const li = list.querySelector(`#id${id}`);
    li.querySelector("label").innerText = `Status: ${status}`;
}