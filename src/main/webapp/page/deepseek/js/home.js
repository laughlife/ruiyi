function send(socket) {
    websocket.send(JSON.stringify(socket));
}

function clientAppend(msg) {
    $("#rightMessage").append(getNowTime() + ":   " + msg + "<br />");
    $("#rightMessage").scrollTop($("#rightMessage")[0].scrollHeight);
}

function rightAppend(msg) {
    var message = JSON.parse(msg);
    $("#rightMessage").append(message.time + ":   " + message.message + "<br />");
    $("#rightMessage").scrollTop($("#rightMessage")[0].scrollHeight);
}

function clearRightTerminal() {
    $("#rightMessage").html("");
}


function startTimer() {
    socketLink = setInterval(() => {
        var sendMessage = {
            type: type_ping,
            message: '',
            time: getNowTime()
        }
        send(sendMessage);
    }, 1000 * 10);
}

//接收服务器端发送的消息的处理函数
function receiveMessage(message) {
    var reciveMessage = JSON.parse(message);
    if (reciveMessage.type == type_pong) {
        return;
    } else if(reciveMessage.type == type_message){
        rightAppend(message);
    }else if(reciveMessage.type == type_answer) {
        leftAppend(message);
    }
}

$(document).ready(function () {
    socketPath = socketPath.replaceAll('http://', "ws://").replaceAll('https://', "wss://");
    var socketUrl = socketPath + "/deepseek/" + uuid;

    clientAppend("socket连接中:" + socketUrl);

    //判断当前浏览器是否支持WebSocket
    if ('WebSocket' in window) {
        //连接WebSocket节点(这里的地址是你后端的接口地址)
        websocket = new WebSocket(socketUrl);
    }

    //连接发生错误的回调方法
    //服务器发生错误，尝试重新连接服务
    websocket.onerror = function () {
        reloadService();
    };

    //连接成功建立的回调方法
    websocket.onopen = function () {
        clientAppend("服务器连接成功。");
        $("#uuid").val(uuid);
    }

    //接收到消息的回调方法
    websocket.onmessage = function (event) {
        receiveMessage(event.data);
    }

    //连接关闭的回调方法
    websocket.onclose = function () {
        $("#now_connent_status").removeClass("icon");
        $("#now_connent_status").addClass("icon-tip");
        $("#create_task_btn").addClass("layui-btn-disabled");
        clientAppend("服务器连接已经断开，如需重连请刷新页面。");
    }

    startTimer();

    //监听窗口关闭事件，当窗口关闭时，主动去关闭websocket连接，防止连接还没断开就关闭窗口，server端会抛异常。
    window.onbeforeunload = function () {
        clearInterval(socketLink);
        websocket.close();
    }

});

function getNowTime() {
    var now = new Date();
    // 获取年份
    var year = now.getFullYear();
    // 获取月份（注意：月份从0开始，所以需要加1）
    var month = String(now.getMonth() + 1).padStart(2, '0');
    // 获取日期
    var date = String(now.getDate()).padStart(2, '0');
    // 获取小时
    var hours = String(now.getHours()).padStart(2, '0');
    // 获取分钟
    var minutes = String(now.getMinutes()).padStart(2, '0');
    // 获取秒钟
    var seconds = String(now.getSeconds()).padStart(2, '0');
    // 格式化为 yyyy-mm-dd HH:mm:ss
    return year + "-" + month + "-" + date + " " + hours + ":" + minutes + ":" + seconds;
}