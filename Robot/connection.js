const robotId = 3;
const password = "$2a$10$xADu7szZCW1I./nr8zXOLeWZrdVHEdOB/2WGcmid3I7NLzzUpjNwO";

// const host = 'http://192.168.1.12:8080/endpoint'
const host = 'https://remote-control-project.herokuapp.com/endpoint'
const signallingHost = "/app/signalling";
const reportingHost = "/app/reports";
const configHost = "/app/config";
const configuration = {
    iceServers: [
        {url: "stun:stun.l.google.com:19302"},
        {url: 'stun:stun1.l.google.com:19302'},
        {url: 'stun:stun2.l.google.com:19302'},
        {url: 'stun:stun3.l.google.com:19302'}
    ]
};
const videoConfig = {
    "name": "Video configuration",
    "params": [
        {"name": "width", "value": "1200"},
        {"name": "height", "value": "720"},
        {"name": "frame rate", "value": "10"}
    ]
}
let nativeConfig = [videoConfig];
let stompClient = null;
let peerConnection = null;
let dataChannel = null;
let stream = null;
let timeout = null;

connect();

//-------------Signalling----------------

function sendConfig() {
    stompClient.send(configHost, {}, JSON.stringify(nativeConfig));
}

function connect() {

    logCore("Connecting to signalling server...");
    const socket = new SockJS(host);
    stompClient = Stomp.over(socket);
    stompClient.connect({},
        function (frame) {
            logCore("Connected to signalling server");
            stompClient.subscribe('/app/authenticate',
                function (msg) {
                    onMessage(JSON.parse(msg.body));
                },
                {"robotId": robotId, "robotPass": password});
        }, function () {
        logCore("Failed to connect to signalling server");
            finalizePeerConnection();
            window.setTimeout(connect, 3000);
        });
}

function send(type, data) {

    stompClient.send(signallingHost, {"caller": "robot"}, JSON.stringify({"type": type, "data": data}));
}

function report(type, data) {

    stompClient.send(reportingHost, {}, JSON.stringify({"type": type, "data": data}));
}

//-------------WebRTC code----------------

function onMessage(msg) {

    switch (msg.type) {
        case "candidate":
            peerConnection.addIceCandidate(new RTCIceCandidate(msg.data));
            break;
        case "offer":
            peerConnection.setRemoteDescription(new RTCSessionDescription(msg.data));
            peerConnection.createAnswer(function (answer) {
                peerConnection.setLocalDescription(answer);
                send("answer", answer);
            }, function (error) {
                console.log(error);
            });
            break;
        case "answer":
            peerConnection.setRemoteDescription(new RTCSessionDescription(msg.data));
            break;
        case "start":
            startTransmission();
            timeout = setTimeout(disconnect, 10000);
            break
        case "config":
            notifyCore(msg);
            break;
    }
}

function initializePeerConnection() {
    peerConnection = new RTCPeerConnection(configuration);
    peerConnection.onicecandidate = function (event) {
        if (event.candidate) {
            send("candidate", event.candidate);
        }
    };

    peerConnection.onconnectionstatechange = function (event) {
        switch (peerConnection.connectionState) {
            case "connected":
                onConnect();
                break;
            case "disconnected":
                disconnect();
                break;
            case "failed":
                report("failed", "");
                logCore("Client connection failed");
                finalizePeerConnection();
                break;
        }
    };
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

function onConnect() {
    clearTimeout(timeout);
    report("connect", "");
    logCore("Client connected");
}

function disconnect() {
    report("disconnect", "");
    finalizePeerConnection();
    logCore("Client disconnected");
}

async function startVideo() {
    const constraints = {
        audio: false,
        video: {
            // width: videoConfig.params[0].value,
            // height: videoConfig.params[0].value,
            // frameRate: videoConfig.params[0].value
            width: 1200,
            height: 720,
            frameRate: 10
        }
    };
    stream = await navigator.mediaDevices.getUserMedia(constraints);
    peerConnection.addTrack(stream.getTracks()[0]);
}

async function startTransmission() {
    initializePeerConnection();
    dataChannel = peerConnection.createDataChannel("dc");
    dataChannel.onerror = function (error) {
        console.log("Error:", error);
    };
    dataChannel.onclose = function () {
        disconnect();
        console.log("Data channel closed");
    };
    dataChannel.onmessage = function (msg) {
        const message = JSON.parse(msg.data);
        console.log(message);
        notifyCore(message);
    };
    await startVideo();
    peerConnection.createOffer(function (offer) {
        send("offer", offer);
        peerConnection.setLocalDescription(offer);
    }, function (error) {
        console.log(error)
    });
}

async function notifyCore(msg) {
    window.sendToCore(msg);
}

async function logCore(value) {
    window.logOnCore(value);
}