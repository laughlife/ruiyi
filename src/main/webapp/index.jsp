<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>玖零壹品</title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport"
          content="width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no"/>
    <meta name="apple-mobile-web-app-status-bar-style" content="black">
    <meta name="mobile-web-app-capable" content="yes">
    <meta name="format-detection" content="telephone=no">
    <meta name="_csrf" content="${_csrf.token}"/>
    <meta name="_csrf_header" content="${_csrf.headerName}"/>
    <link rel="icon" href="/static/image/favicon.ico" type="image/x-icon"/>
    <link rel="stylesheet" type="text/css" href="/static/layui/css/layui.css"/>
    <link rel="stylesheet" type="text/css" href="/static/admin/css/login.css"/>
    <style>
        .footer {
            position: fixed;
            bottom: 0;
            width: 100%;
            text-align: center;
            margin-bottom: 20px;
            font-size: 14px;
            line-height: 30px;
        }
    </style>
</head>
<body>
<div class="m-login-bg">
    <div class="m-login">
        <h3>深圳市玖零壹品科技有限公司</h3>
        <div class="m-login-warp">
            <form class="layui-form" lay-filter="loginForm">
                <div class="layui-form-item">
                    <div class="layui-input-prefix">
                        <i class="layui-icon layui-icon-username"></i>
                    </div>
                    <input type="text" name="username" lay-verify="required|username" placeholder="用户名" class="layui-input" lay-affix="clear">
                </div>
                <div class="layui-form-item">
                    <div class="layui-input-prefix">
                        <i class="layui-icon layui-icon-password"></i>
                    </div>
                    <input type="password" name="password" required lay-verify="required|password" placeholder="密码"
                           class="layui-input" lay-affix="eye">
                </div>
                <div class="layui-form-item m-login-btn">
                    <div class="layui-inline">
                        <input type="checkbox" name="remember-me" id="rememberMe" checked/>
                        <div lay-checkbox>一周内免登录</div>
                    </div>
                    <div class="layui-inline">
                        <button class="layui-btn layui-btn-normal" lay-submit lay-filter="login" type="button">登录
                        </button>
                    </div>
                </div>
            </form>
        </div>
    </div>
</div>

<div class="footer">
    深圳市玖零壹品科技有限公司 &copf; 2025
    <a href="https://beian.miit.gov.cn/" target="_blank" style="margin-left:30px;"><img src="/static/image/gh.png"
                                                                                        style="width:20px;"></a>
    <a href="https://beian.miit.gov.cn/" target="_blank">粤ICP备2024337209号-1</a>
    <%--    <a href="https://beian.mps.gov.cn/#/query/webSearch?code=44030002006071" target="_blank"><img src="/static/image/ba.png" style="width:20px;"></a>--%>
    <%--    <a href="https://beian.mps.gov.cn/#/query/webSearch?code=44030002006071" rel="noreferrer" target="_blank">粤公网安备44030002006071号</a>--%>
</div>
<script src="/static/layui/layui.js" type="text/javascript" charset="utf-8"></script>
<script src="/static/jquery/jquery-3.7.1.min.js" type="text/javascript" charset="utf-8"></script>
<script src="/static/js/crypto-js.min.js" type="text/javascript" charset="utf-8"></script>
<script>
    layui.use(function () {
        var form = layui.form,
            layer = layui.layer;
        //自定义验证规则
        form.verify({
            username: function (value, elem) {
                if (value.length < 5) {
                    return '用户名至少5个字符';
                }
            },
            password: function (value, elem) {
                if (value.length < 3) {
                    return '密码在3位以上';
                }
            }
        });

        //监听提交
        form.on('submit(login)', function (data) {
            var submitData = data.field;
            var username = submitData.username;
            var pwd = md5WithCryptoJS(submitData.password);

            var token  = $('meta[name="_csrf"]').attr('content');
            var header = $('meta[name="_csrf_header"]').attr('content');

            $.ajax({
                url: "/login/userLogin",
                type: "post",
                data: {
                    username: username,
                    password: pwd,
                    'remember-me': $('#rememberMe').prop('checked') ? 'on' : ''
                },
                beforeSend: function(xhr) {
                    xhr.setRequestHeader(header, token);
                },
                dataType: "json",
                success: function (data) {
                    if (data.status) {
                        window.location = '/home/goHomePage';
                        layer.msg(data.msg, {icon: data.icon, time: 500});
                    } else {
                        layer.msg(data.msg, {icon: data.icon, time: 3000});
                    }
                }
            });
            return false;
        });
    });

    function md5WithCryptoJS(string) {
        const hash = CryptoJS.MD5(string).toString();
        return hash;
    }
</script>
</body>
</html>
