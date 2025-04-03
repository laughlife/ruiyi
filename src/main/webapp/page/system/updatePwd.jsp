<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>设置我的密码</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0">
    <link rel="stylesheet" href="${basePath}static/layui/css/layui.css" media="all">
    <script src="${basePath}static/layui/layui.js"></script>
    <script src="${basePath}static/jquery/jquery-3.7.1.min.js" type="text/javascript" charset="utf-8"></script>
    <script src="${basePath}static/js/crypto-js.min.js" type="text/javascript" charset="utf-8"></script>
</head>
<body>
<div class="layui-fluid">
    <div class="layui-row layui-col-space15">
        <div class="layui-col-md12">
            <div class="layui-card">
                <div class="layui-card-header">修改密码</div>
                <div class="layui-card-body">
                    <form class="layui-form" lay-filter="passwordForm" action="${basePath}admin/updatePwd"
                          method="post">
                        <div class="layui-form">
                            <div class="layui-form-item">
                                <label class="layui-form-label">当前密码</label>
                                <div class="layui-input-inline">
                                    <input type="password" name="oldPassword" lay-verify="required" lay-verType="tips"
                                           class="layui-input">
                                </div>
                            </div>
                            <div class="layui-form-item">
                                <label class="layui-form-label">新密码</label>
                                <div class="layui-input-inline">
                                    <input type="password" name="password" lay-verify="pass" lay-verType="tips"
                                           autocomplete="off" id="LAY_password" class="layui-input">
                                </div>
                                <div class="layui-form-mid layui-word-aux">6到16个字符</div>
                            </div>
                            <div class="layui-form-item">
                                <label class="layui-form-label">确认新密码</label>
                                <div class="layui-input-inline">
                                    <input type="password" name="repassword" lay-verify="repass" lay-verType="tips"
                                           autocomplete="off" class="layui-input">
                                </div>
                            </div>
                            <div class="layui-form-item">
                                <div class="layui-input-block">
                                    <button class="layui-btn" lay-submit lay-filter="setmypass">确认修改</button>
                                </div>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>
<script>
    layui.use(function () {
        var form = layui.form;
        // 自定义验证规则
        form.verify({
            pass: [
                /^[\S]{6,16}$/,
                '密码必须6到16个字符，且不能出现空格'
            ],
            repass: function (value) {
                if (value !== document.querySelector('input[name="password"]').value) {
                    return '两次密码输入不一致';
                }
            }
        });

        // 监听提交
        form.on('submit(setmypass)', function (data) {
            // 提交表单
            data.field.password = md5WithCryptoJS(data.field.password);
            data.field.oldPassword = md5WithCryptoJS(data.field.oldPassword);
            $.ajax({
                url: data.form.action, // 表单 action 属性
                type: data.form.method, // 表单 method 属性
                data: data.field, // 表单数据
                dataType: 'json',
                success: function (res) {
                    if (res.status) {
                        layer.msg('密码修改成功');
                    } else {
                        layer.msg(res.message || '密码修改失败');
                    }
                },
                error: function () {
                    layer.msg('请求失败，请稍后再试');
                }
            });
            return false; // 阻止默认提交
        });
    });

    function md5WithCryptoJS(string) {
        const hash = CryptoJS.MD5(string).toString();
        return hash;
    }
</script>
</body>
</html>
