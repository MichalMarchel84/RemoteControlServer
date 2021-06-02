const configuration = {
    iceServers: [
        {url: "stun:stun.l.google.com:19302"},
        {url: 'stun:stun1.l.google.com:19302'},
        {url: 'stun:stun2.l.google.com:19302'},
        {url: 'stun:stun3.l.google.com:19302'}
    ]
};
const keysDown = [];
let peerConnection = null;
let stompClient = null;
let dataChannel = null;
let stream = null;
let timer;

connect();
document.querySelector("#disconnect").addEventListener("click", disconnect);

function connect() {

    const id = document.querySelector("#head").dataset.id;
    const socket = new SockJS('/endpoint');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {

        stompClient.subscribe('/app/begin',
            function (msg) {
                onMessage(JSON.parse(msg.body));
            },
            {"robotId": id});
    });
    initializePeerConnection();
}

function onMessage(msg) {
    if (msg.type === "candidate") {
        peerConnection.addIceCandidate(new RTCIceCandidate(msg.data));
    } else if (msg.type === "offer") {
        peerConnection.setRemoteDescription(new RTCSessionDescription(msg.data));
        peerConnection.createAnswer(function (answer) {
            peerConnection.setLocalDescription(answer);
            send("answer", answer);
        }, function (error){
            console.log(error);
        });
    } else if (msg.type === "answer") {
        peerConnection.setRemoteDescription(new RTCSessionDescription(msg.data));
    } else if (msg.type === "message") {
        document.querySelector("#message").innerHTML = msg.data;
    }
}

function send(type, data) {
    stompClient.send("/app/signalling", {"caller": "user"}, JSON.stringify({"type": type, "data": data}));
}

function sendKeys(keys) {
    dataChannel.send(JSON.stringify({"keys": keys}));
}

function disconnect() {

    document.querySelector("#video").style.display = "none";
    document.querySelector("#disconnect").style.display = "none";
    finalizePeerConnection();
    initializePeerConnection();
}

function keyDown(e) {
    const i = keysDown.indexOf(e.key);
    if (i === -1) keysDown.push(e.key);
    sendKeys(keysDown);
}

function keyUp(e) {
    const i = keysDown.indexOf(e.key);
    if (i > -1) keysDown.splice(i, 1);
    sendKeys(keysDown);
};

function initializePeerConnection() {

    stream = new MediaStream();
    peerConnection = new RTCPeerConnection(configuration);

    peerConnection.ondatachannel = function (event) {
        dataChannel = event.channel;
        dataChannel.onerror = function (error) {
            console.log("Error:", error);
        };
        dataChannel.onclose = function () {
            console.log("Data channel is closed");
        };
        dataChannel.onmessage = function (msg) {
            //handle message from robot
        };
        dataChannel.onopen = function () {

            document.addEventListener("keydown", keyDown);
            document.addEventListener("keyup", keyUp);
            timer = window.setInterval(() => {
                if (keysDown.length) {
                    sendKeys(keysDown)
                }
            }, 500);
        }
        dataChannel.onclose = function () {
            window.clearInterval(timer);
            document.removeEventListener("keydown", keyDown);
            document.removeEventListener("keyup", keyUp);
        }
    };

    peerConnection.onicecandidate = function (event) {
        if (event.candidate) {
            send("candidate", event.candidate);
        }
    };

    peerConnection.ontrack = function (event) {
        const video = document.querySelector("#video");
        video.srcObject = stream;
        stream.addTrack(event.track);
        video.style.cssText = "-moz-transform: scale(-1, -1); -webkit-transform: scale(-1, -1); -o-transform: scale(-1, -1); transform: scale(-1, -1); filter: FlipV; filter: FlipH";
    }

    peerConnection.onconnectionstatechange = function (event) {
        switch (peerConnection.connectionState) {
            case "connected":
                document.querySelector("#video").style.display = "block";
                document.querySelector("#disconnect").style.display = "block";
                break;
            case "disconnected":
                disconnect();
                break;
            case "failed":
                console.log("<<<<<<<<<Failed>>>>>>>>>>");
        }
    }
}

function finalizePeerConnection() {
    if (stream != null) {
        stream.getTracks().forEach(function (track) {
            track.stop();
        });
    }
    stream = null;
    if (dataChannel != null) dataChannel.close();
    if (peerConnection != null) peerConnection.close();
    dataChannel = null;
    peerConnection = null;
}