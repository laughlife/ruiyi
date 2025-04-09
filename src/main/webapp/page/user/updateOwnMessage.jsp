<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>修改个人信息</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0">

    <link rel="stylesheet" href="/static/layui/css/layui.css" media="all">
    <link rel="stylesheet" href="/static/fontawesome6/css/all.min.css" media="all">
    <script src="/static/layui/layui.js"></script>
</head>
<body>

<div class="layui-fluid">
    <div class="layui-row layui-col-space15">
        <div class="layui-col-md12">
            <div class="layui-card">
                <div class="layui-card-header">修改个人信息</div>
                <div class="layui-card-body">
                    <form class="layui-form layui-form-pane">
                        <div class="layui-form-item">
                            <label class="layui-form-label">用户名</label>
                            <div class="layui-input-inline layui-input-wrap">
                                <input type="text" name="username" readonly disabled class="layui-input" value="${user.username}">
                            </div>
                            <div class="layui-form-mid layui-text-em">用户登录名，默认不能修改，如需修改，请联系管理员或小组组长进行修改.</div>
                        </div>
                        <div class="layui-form-item">
                            <label class="layui-form-label">用户姓名</label>
                            <div class="layui-input-inline layui-input-wrap">
                                <input type="text" name="name" lay-verify="required" placeholder="用户姓名"
                                       autocomplete="off" class="layui-input" value="${user.name}">
                            </div>
                            <div class="layui-form-mid layui-text-em">用户真实姓名</div>
                        </div>
                        <div class="layui-form-item">
                            <label class="layui-form-label">手机号</label>
                            <div class="layui-input-inline layui-input-wrap">
                                <input type="number" name="phone" lay-verify="required" placeholder="手机号"
                                       autocomplete="off" class="layui-input" value="${user.phone}">
                            </div>
                            <div class="layui-form-mid layui-text-em">后期可发送短信通知，如各种紧急通知或预警信息.</div>
                        </div>
                        <div class="layui-form-item">
                            <label class="layui-form-label">部门</label>
                            <div class="layui-input-inline layui-input-wrap">
                                <div class="layui-input-inline layui-input-wrap">
                                    <input type="text" name="department" readonly disabled class="layui-input" value="${department.name}">
                                </div>
                            </div>
                            <div class="layui-form-mid layui-text-em">默认不能修改，如需修改，请联系管理员或小组组长进行修改.</div>
                        </div>
                        <div class="layui-form-item">
                            <button type="button" lay-submit class="layui-btn layui-bg-blue"
                                    lay-filter="update-own-filter">确认修改
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>
<script>
    layui.use(function () {
        var $ = layui.jquery;
        var layer = layui.layer;
        var form = layui.form;

        form.verify({});

        form.on('submit(update-own-filter)', function (data) {
            $.ajax({
                url: '/user/updateOwnMessage',
                type: 'post',
                data: data.field,
                dataType: 'json',
                success: function (res) {
                    if (res.status) {
                        layer.msg(res.msg, {icon: 1, time: 1000});
                    } else {
                        layer.msg(res.msg, {icon: 2, time: 1000});
                    }
                }
            });
        });

    });

</script>
</body>
</html>