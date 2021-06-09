const puppeteer = require('puppeteer-core');
const resolve = require('path').resolve;
const url = "file://" + resolve('main.html');

launchNetwork();

async function launchNetwork () {
    console.log("Starting...");
    const browser = await puppeteer.launch({
        executablePath: '/usr/bin/chromium-browser',
        headless: false,
        args: [ '--use-fake-ui-for-media-stream' ]
    });
    const page = await browser.newPage();
    await page.exposeFunction('sendToCore', onMessage);
    await page.exposeFunction('logOnCore', onLog);
    await page.goto(url);
}

function onLog(value){
    console.log(value);
}

function onMessage(msg){
    switch (msg.type){
        case "keys":
            handleKeys(msg.data);
            break;
    }
}

//---------Robot control code----------

const Gpio = require('onoff').Gpio;
class Motor{

    constructor(pin1, pin2) {
        this.pin1 = new Gpio(pin1, 'out');
        this.pin2 = new Gpio(pin2, 'out');
        this.timer = null;
    }

    run(dir){
        if(dir > 0){
            clearTimeout(this.timer);
            this.pin1.writeSync(1);
            this.pin2.writeSync(0);
            this.timer = setTimeout(() => this.run(0), 1000);
        }else if(dir < 0){
            clearTimeout(this.timer);
            this.pin1.writeSync(0);
            this.pin2.writeSync(1);
            this.timer = setTimeout(() => this.run(0), 1000);
        }else {
            clearTimeout(this.timer);
            this.pin1.writeSync(1);
            this.pin2.writeSync(1);
        }
    }
}
const leftMotor = new Motor(26, 19);
const rightMotor = new Motor(20, 16);

function handleKeys(keys){
    const command = [0, 0];
    keys.forEach(key => {
        switch (key){
            case "w":
                command[0] += 1;
                command[1] += 1;
                break;
            case "s":
                command[0] -= 1;
                command[1] -= 1;
                break;
            case "a":
                command[0] -= 1;
                command[1] += 1;
                break;
            case "d":
                command[0] += 1;
                command[1] -= 1;
                break;
        }
    });
    leftMotor.run(command[0]);
    rightMotor.run(command[1]);
}
