const SerialPort = require("serialport").SerialPort;
const baudRate =9600;
const port = new SerialPort({path: 'COM5', baudRate: baudRate
});

port.on('open',function(){
    console.log('Serial Port OPEN');
    port.on('data',function(data){
        console.log("Sensor Value :",data[0]);
    });
});