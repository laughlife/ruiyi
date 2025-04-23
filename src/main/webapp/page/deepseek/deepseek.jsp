<%@ page import="com.liwei.ruiyi.model.SocketMessage" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
    request.setAttribute("basePath", basePath);
%>
<!DOCTYPE html>
<head>
    <meta charset="utf-8">
    <title>睿翼</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0">
    <link rel="stylesheet" href="/static/layui/css/layui.css" media="all">
    <link rel="stylesheet" href="/static/layui/style/admin.css" media="all">
    <script src="/static/layui/layui.js"></script>
    <script src="/static/jquery/jquery-3.7.1.min.js"></script>
    <style>
        pre {
            border-width: 1px;
            border-color: rgb(126 122 122 / 15%);
            background-color: #1f1f1f;
            color:#f6f6f6;
            font-size: 16px;
            line-height: 25px;
            padding:10px;
        }
    </style>
</head>
<body>
<div class="layui-fluid">
    <div class="layui-row layui-col-space15">

        <div class="layui-col-md7">
            <div class="layui-card">
                <div class="layui-card-header">向Deepseek发送消息</div>
                <div class="layui-card-body">
                    <form class="layui-form" action="">
                        <div class="layui-form-item layui-form-text">
                            <label class="layui-form-label">发送信息</label>
                            <div class="layui-input-block">
                                <input type="hidden" id="uuid" value="">
                                <textarea name="message" id="messageTextarea" placeholder="请输入内容"
                                          class="layui-textarea"></textarea>
                            </div>
                        </div>
                        <div class="layui-form-item">
                            <div class="layui-input-block">
                                <button type="submit" class="layui-btn" lay-submit lay-filter="sendToDeepseek">
                                    发送到Deepseek
                                </button>
                                <button type="reset" class="layui-btn layui-btn-primary">清空</button>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
            <div class="layui-card">
                <div class="layui-card-header">
                    Deepseek回复消息&nbsp;&nbsp;
                    <a id="chearDeepseekMessage" href="javascript:void(0);"><i class="layui-icon layui-icon-refresh-3"></i></a>
                </div>
                <div class="layui-card-body">
                    <pre id="leftMessage" style="min-height:600px;max-height: 820px;overflow-y: scroll"></pre>
                </div>
            </div>
        </div>
        <div class="layui-col-md5">
            <div class="layui-card">
                <div class="layui-card-header">socket消息</div>
                <div class="layui-card-body">
                    <pre id="rightMessage" style="min-height:290px;max-height:980px;overflow-y: scroll"></pre>
                </div>
            </div>
        </div>
    </div>
</div>
<script>
    var websocket = null;
    var socketPath = '${basePath}';
    var socketLink;
    var type_ping = '<%=SocketMessage.TYPE_PING%>'
    var type_pong = '<%=SocketMessage.TYPE_PONG%>'
    var type_message = '<%=SocketMessage.TYPE_MESSAGE%>'
    var type_answer = '<%=SocketMessage.TYPE_ANSWER%>'
    var uuid = Math.random().toString(36).substr(2);
    layui.use(function () {
        var form = layui.form;
        var layer = layui.layer;

        form.on('submit(sendToDeepseek)', function (data) {
            var message = data.field.message;
            var _uid = $('#uuid').val();
            $.ajax({
                url: "/deepseek/sendMessage",
                data: {
                    'uuid': _uid,
                    'message': message
                },
                type: "post",
                dataType: "json",
                success: function (data) {
                    $("#leftMessage").append(getNowTime() + ":   " + data.message + "<br />");
                    $("#leftMessage").scrollTop($("#leftMessage")[0].scrollHeight);
                }
            });
            return false;
        });

        $('#chearDeepseekMessage').on('click', function () {
            $("#leftMessage").html("");
        });

    });

    function leftAppend(msg) {
        var message = JSON.parse(msg);
        var outmessage = message.message;
        var processedMessage = outmessage.replace(/```([\s\S]*?)```/g, function(match, codeContent) {
            // 替换为Layui代码块模板
            return '<pre class="layui-code code-demo" lay-options="{}">' + codeContent + '</pre>';
        });
        // 添加处理后的消息到DOM
        $("#leftMessage").append(message.time + ":   " + processedMessage + "<br />");
        // 初始化Layui代码块
        layui.use(['code'], function(){
            layui.code({
                elem: '.code-demo',
                about: false,   // 隐藏右下角"Layui"标识
                encode: true,   // 自动转义HTML符号
                height: 'auto'  // 自动高度
            });
        });
        $("#leftMessage").scrollTop($("#leftMessage")[0].scrollHeight);
    }
</script>
<script src="/page/deepseek/js/home.js"></script>
</body>
</html>
