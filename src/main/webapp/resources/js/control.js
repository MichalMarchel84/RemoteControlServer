const configuration = {
    iceServers: [
        {url: "stun:stun.l.google.com:19302"},
        {url: 'stun:stun1.l.google.com:19302'},
        {url: 'stun:stun2.l.google.com:19302'},
        {url: 'stun:stun3.l.google.com:19302'}
    ]
};
const keysDown = [];
const mousePosition = [2];
const mousePrevPosition = [2];
let videoRatio = 0.6;
let videoWidth = 0.7;
let peerConnection = null;
let stompClient = null;
let dataChannel = null;
let stream = null;
let keyTimer = null;
let mouseTimer = null;

init();
connect();

function init() {
    let videoConfig = null;
    let keyConfig = null;
    let mouseConfig = null;
    configs.forEach(cfg => {
        if (cfg.name === "Video configuration") {
            videoConfig = cfg;
        } else if (cfg.name === "Keyboard") {
            keyConfig = cfg;
        } else if(cfg.name === "Mouse"){
            mouseConfig = true;
        }
    });
    if (videoConfig != null) {
        let width = null;
        let height = null;
        videoConfig.params.forEach(param => {
            if (param.name === "width") width = param.value;
            else if (param.name === "height") height = param.value;
        });
        videoRatio = height / width;
    }
    if (keyConfig != null) {
        const list = document.querySelector("#info-list");
        keyConfig.params.forEach(param => {
            const tr = document.createElement("tr");
            tr.innerHTML = `<td>${param.value}</td><td>${param.name}</td>`;
            tr.className = "f20";
            list.append(tr);
        });
    }
    if (mouseConfig) {
        const p = document.querySelector("#other");
        p.innerHTML = `Click left mouse button to switch between drive mode/free camera mode<br/>
                        In free camera mode, move mouse to look around`;
    }
    document.querySelector("#info").addEventListener("click", () => {
        document.querySelector("#info-cont").style = "display: flex";
    });
    document.querySelector("#close").addEventListener("click", () => {
        document.querySelector("#info-cont").style = "display: none";
    });
    document.querySelector("#disconnect").addEventListener("click", () => {
        disconnect();
        close();
    });
}

function connect() {

    const socket = new SockJS('/endpoint');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {

        stompClient.subscribe('/app/begin',
            function (msg) {
                onMessage(JSON.parse(msg.body));
            },
            {"robotId": robotId});
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
        }, function (error) {
            console.log(error);
        });
    } else if (msg.type === "answer") {
        peerConnection.setRemoteDescription(new RTCSessionDescription(msg.data));
    } else if (msg.type === "message") {
        document.querySelector("#message").innerText = msg.data;
    }
}

function send(type, data) {
    stompClient.send("/app/signalling", {"caller": "user"}, JSON.stringify({"type": type, "data": data}));
}

function sendKeys(keys) {
    dataChannel.send(JSON.stringify({"type": "keys", "data": keys}));
}

function sendMouse(delta) {
    dataChannel.send(JSON.stringify({"type": "mouse", "data": delta}));
}

function disconnect() {
    finalizePeerConnection();
}

function keyDown(e) {
    const i = keysDown.indexOf(e.key.toLowerCase());
    if (i === -1) keysDown.push(e.key.toLowerCase());
    sendKeys(keysDown);
}

function keyUp(e) {
    const i = keysDown.indexOf(e.key.toLowerCase());
    if (i > -1) keysDown.splice(i, 1);
    sendKeys(keysDown);
};

function mouseClicked(e) {
    if((e.target.tagName !== "BUTTON") && (e.target.tagName !== "IMG")) {
        let delta = [3];
        delta[0] = 0;
        delta[1] = 0;
        delta[2] = 1;
        sendMouse(delta);
    }
}

function mouseMoved(e) {
    mousePosition[0] = e.pageX / window.innerWidth;
    mousePosition[1] = e.pageY / window.innerHeight;
    if(mousePrevPosition[0] == null){
        mousePrevPosition[0] = mousePosition[0];
        mousePrevPosition[1] = mousePosition[1];
    }
}

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
            document.addEventListener("click", mouseClicked);
            document.addEventListener("mousemove", mouseMoved);
            mousePosition[0] = null
            mousePrevPosition[0] = null;
            keyTimer = window.setInterval(() => {
                if (keysDown.length) {
                    sendKeys(keysDown)
                }
            }, 500);
            mouseTimer = window.setInterval(() => {
                if (mousePosition[0] != null) {
                    let delta = [2];
                    delta[0] = mousePosition[0] - mousePrevPosition[0];
                    delta[1] = mousePosition[1] - mousePrevPosition[1];
                    sendMouse(delta);
                    mousePrevPosition[0] = mousePosition[0];
                    mousePrevPosition[1] = mousePosition[1];
                    mousePosition[0] = null;
                }
            }, 50);
        }
        dataChannel.onclose = function () {
            window.clearInterval(keyTimer);
            document.removeEventListener("keydown", keyDown);
            document.removeEventListener("keyup", keyUp);
            document.removeEventListener("mousedown", mouseClicked);
            document.removeEventListener("mouseup", mouseMoved);
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
    }

    peerConnection.onconnectionstatechange = function (event) {
        switch (peerConnection.connectionState) {
            case "connected":
                let width = window.innerWidth * videoWidth;
                let height = width * videoRatio;
                if (height > window.innerHeight * 0.8) {
                    height = window.innerHeight * 0.8;
                    width = height / videoRatio;
                }
                const element = document.querySelector("#video-box");
                element.style = "display: flex";
                deploy(element, width, height, 500);
                break;
            case "disconnected":
                disconnect();
                break;
            case "failed":
                document.querySelector("#message").innerText = "Connection failed";
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

function deploy(element, width, height, deployTime) {
    const dt = 20;
    const step = width / (deployTime / dt);
    let aw = 0;
    let ah = 0;
    const timer = setInterval(() => {
        if (ah < height) {
            ah += step;
            if (ah > height) ah = height;
        }
        if (aw < width) {
            aw += step;
            if (aw > width) aw = width;
        }
        resize(element, aw, ah);
        if ((aw === width) && (ah === height)) clearInterval(timer);
        window.addEventListener("resize", () => {
            let w = window.innerWidth * videoWidth;
            let h = w * videoRatio;
            if (h > window.innerHeight * 0.8) {
                h = window.innerHeight * 0.8;
                w = h / videoRatio;
            }
            resize(element, w, h);
        })
    }, dt);
}

function resize(element, width, height) {
    element.style.width = width;
    element.style.height = height;
}