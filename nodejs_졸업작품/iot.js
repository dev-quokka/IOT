const express = require("express");
const app = express();
const http = require("http");
const server = http.createServer(app);
const { Server } = require("socket.io");
const io = new Server(server);

var mysql = require("mysql");

var connection = mysql.createConnection({
  host: "localhost",
  user: "root",
  password: "111111",
  database: "iot",
  dateStrings: "date",
});
connection.connect();

const macarr = [
  "EC:FA:BC:77:47:2F",
  "84:F3:EB:1C:3D:67",
  "5C:CF:7F:61:27:86",
  "EC:FA:BC:A5:F4:DB",
]; //등록가능한 장치들의 mac 주소
const reg = [false, false]; // 등록 여부

function makeRandomName() {
  var name = "";
  var possible = "abcdefghijklmnopqrstuvwxyz";
  for (var i = 0; i < 3; i++) {
    name += possible.charAt(Math.floor(Math.random() * possible.length));
  }
  return name;
} // 랜덤으로 endpointID 생성

var isRequesth = false;
var isRequestm = false;
var isRequestp = false;
var isRequestt = false;
var isRequestM = false;
var isRequestH = false;
var isRequestP = false;
var isRequestT = false;
var val;
var dustval;
var co2Val;
var gasval;
var tmpval;
var humval;

var SData = [];

var requestm = setInterval(() => {
  if (isRequestm) {
    //const randomNum = Math.floor(Math.random() * 10 + 1);
    var msg = {
      gasval: gasval,
      //gastime: gastime,
    };
    // console.log(randNum);
    io.emit("gasval", msg);
  }
}, 3000);

var requesth = setInterval(() => {
  if (isRequesth) {
    //const randomNum = Math.floor(Math.random() * 10 + 1);
    var msg = {
      co2val: co2Val,
    };

    io.emit("co2val", msg);
  }
}, 3000);

var requestp = setInterval(() => {
  if (isRequestp) {
    //const randomNum = Math.floor(Math.random() * 10 + 1);
    var msg = {
      dustval: dustval,
    };

    io.emit("dustval", msg);
    console.log(dustval);
  }
}, 3000);

var requestt = setInterval(() => {
  if (isRequestt) {
    //const randomNum = Math.floor(Math.random() * 10 + 1);
    var msg = {
      tmpval: tmpval,
    };
    var msg2 = {
      humval: humval,
    };
    io.emit("tmpval", msg);
    console.log(tmpval);
    io.emit("humval", msg2);
    console.log(humval);
  }
}, 3000);

function requestM() {
  if (isRequestM) {
    var sql = "INSERT INTO gas (gas) VALUES(" + val + ")";
    connection.query(sql, function (err, rows, fields) {
      if (err) {
        console.log(err);
      } else {
        console.log(rows);
      }
    });
  }
}

function requestH() {
  if (isRequestH) {
    var sql = "INSERT INTO co2 (co2) VALUES(" + val + ")";
    connection.query(sql, function (err, rows, fields) {
      if (err) {
        console.log(err);
      } else {
        console.log(rows);
      }
    });
  }
}

function requestP() {
  if (isRequestH) {
    var sql = "INSERT INTO dust (dust) VALUES(" + val + ")";
    connection.query(sql, function (err, rows, fields) {
      if (err) {
        console.log(err);
      } else {
        console.log(rows);
      }
    });
  }
}

function requestT() {
  if (isRequestT) {
    var sql = "INSERT hum (hum,tmp) VALUES(" + humval + "," + val + ")";
    connection.query(sql, function (err, rows, fields) {
      if (err) {
        console.log(err);
      } else {
        console.log(rows);
      }
    });
  }
}
// localhost:3000으로 방문 시 index.html로 라우팅
app.get("/", (req, res) => {
  res.sendFile(__dirname + "/index.html");
});
// socket이 connection 상태일때
io.on("connection", function (socket) {
  socket.on("start", function (data) {
    socket.broadcast.emit("start", "start");
  });
  socket.on("co2start", function (data) {
    isRequesth = true;
    requesth;
  });
  socket.on("gasstart", function (data) {
    isRequestm = true;
    requestm;
  });
  socket.on("duststart", function (data) {
    isRequestp = true;
    requestp;
  });
  socket.on("tmpstart", function (data) {
    isRequestt = true;
    requestt;
  });
  socket.on("on", function (data) {
    io.emit("2", "2");
  });
  socket.on("off", function (data) {
    io.emit("4", "4");
  });
  socket.on("1", function (data) {
    io.emit("1", "1");
  });
  socket.on("2", function (data) {
    io.emit("2", "2");
  });
  socket.on("3", function (data) {
    io.emit("3", "3");
  });

  // 접속한 클라이언트의 정보가 수신되면
  socket.on("login", function (data) {
    console.log(
      "Client logged-in:\n name:" + data.name + "\n userid: " + data.userid
    );

    // socket에 클라이언트 정보를 저장한다
    socket.name = data.name;
    socket.userid = data.userid;

    // 접속된 모든 클라이언트에게 메시지를 전송한다
    io.emit("login", data.name);
  });

  // 클라이언트로부터의 메시지가 수신되면
  socket.on("chat", function (data) {
    console.log("Message from %s: %s", socket.name, data.msg);

    var msg = {
      from: "server",
    };

    // 메시지를 전송한 클라이언트를 제외한 모든 클라이언트에게 메시지를 전송한다
    socket.broadcast.emit("start", msg);

    // 메시지를 전송한 클라이언트에게만 메시지를 전송한다
    // socket.emit('s2c chat', msg);

    // 접속된 모든 클라이언트에게 메시지를 전송한다
    // io.emit('s2c chat', msg);

    // 특정 클라이언트에게만 메시지를 전송한다
    // io.to(id).emit('s2c chat', data);
  });

  socket.on("SAM_REG", function (data) {
    var flag = false;
    var type = Number(data.type) + 1;
    if (flag == true) {
      var msg = {
        type: type,
        ack: 2,
      };
      socket.emit("SAM_REG", msg);
    } else if (data == null) {
      var msg = {
        type: type,
        ack: 3,
      };
      socket.emit("SAM_REG", msg);
    } else {
      for (var step = 0; step < macarr.length; step++) {
        if (data.mac == macarr[step]) {
          reg[step] = true;
          flag = reg[step];
          SData.push(new Obj(data.devName, data.SerialNum, ""));
        }
      }
      if (flag) {
        var msg = {
          type: type,
          ack: 0,
        };
        socket.emit("SAM_REG", msg);
      } else {
        var msg = {
          type: type,
          ack: 1,
        };
        socket.emit("SAM_REG", msg);
      }
    }
  });
  socket.on("SAM_CON", function (data) {
    var sn = false;
    var type = Number(data.type) + 1;
    if (sn == true) {
      var msg = {
        type: type,
        ack: 2,
      };
      socket.emit("SAM_CON", msg);
    } else if (data.SerialNum == null) {
      var msg = {
        type: type,
        ack: 3,
      };
      socket.emit("SAM_CON", msg);
    } else {
      for (var i = 0; i < SData.length; i++) {
        if (SData[i].SerialNum == data.SerialNum) {
          SData[i].endID = makeRandomName();
          var endID = SData[i].endID;
          socket.name = SData[i].endID;

          var msg = {
            type: type,
            ack: 0,
            endID: endID,
          };

          socket.emit("SAM_CON", msg);
          sn = true;
        }
      }
      if (sn == false) {
        var msg = {
          type: type,
          ack: 1,
        };
        socket.emit("SAM_CON", msg);
      }
    }
  });
  socket.on("SAM_DATA", function (data) {
    //console.log(data);
    var type = Number(data.type) + 1;
    for (var i = 0; i < SData.length; i++) {
      if (data.endID == SData[i].endID) {
        if (SData[i].devName == "MHZ19") {
          val = data.co2val;
          co2Val = val;
          console.log(co2Val);
          if (co2Val > 2500) {
            io.emit("3", "3");
          }

          isRequestH = true;
          requestH();

          var msg = {
            type: type,
            ack: 0,
            endID: SData[i].endID,
          };
          socket.emit("SAM_DATA", msg);
        } else if (SData[i].devName == "MQ-5") {
          val = data.gasval;
          gasval = val;
          if (gasval > 250) {
            io.emit("3", "3");
          }

          isRequestM = true;
          requestM();

          var msg = {
            type: type,
            ack: 0,
            endID: SData[i].endID,
          };
          socket.emit("SAM_DATA", msg);
        } else if (SData[i].devName == "PMS7003") {
          val = data.pMval;
          dustval = val;

          if (dustval > 65) {
            io.emit("3", "3");
          }

          isRequestP = true;
          requestP();

          var msg = {
            type: type,
            ack: 0,
            endID: SData[i].endID,
          };
          socket.emit("SAM_DATA", msg);
        } else if (SData[i].devName == "DHT11") {
          val = data.tmp;
          tmpval = val;
          humval = data.hum;

          isRequestT = true;
          requestT();

          var msg = {
            type: type,
            ack: 0,
            endID: SData[i].endID,
          };
          socket.emit("SAM_DATA", msg);
        }
      }
    }
  });

  // force client disconnect from server
  socket.on("forceDisconnect", function () {
    socket.disconnect();
  });

  socket.on("disconnect", function () {
    console.log("user disconnected: " + socket.name);
  });
});

// server는 localhost:3000
server.listen(3000, () => {
  console.log("listening on *:3000");
});

var Obj = function (devName, SerialNum, endID) {
  this.devName = devName;
  this.SerialNum = SerialNum;
  this.endID = endID;
};

var Ob = function (time, value) {
  this.time = time;
  this.value = value;
};
