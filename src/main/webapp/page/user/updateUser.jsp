<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>修改人员信息</title>
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
                <div class="layui-card-body">
                    <form class="layui-form layui-form-pane">
                        <div class="layui-form-item">
                            <label class="layui-form-label">用户名</label>
                            <div class="layui-input-inline layui-input-wrap">
                                <input type="text" name="username" lay-verify="required|username" placeholder="用户名"
                                       autocomplete="off" class="layui-input" value="${queryUser.username}">
                                <input type="hidden" name="id" value="${queryUser.id}">
                            </div>
                            <div class="layui-form-mid layui-text-em">
                                <button type="button" id="check_username_btn"
                                        class="layui-btn layui-btn-sm layui-bg-blue">验证用户名
                                </button>
                            </div>
                        </div>
                        <div class="layui-form-item">
                            <label class="layui-form-label">用户姓名</label>
                            <div class="layui-input-inline layui-input-wrap">
                                <input type="text" name="name" lay-verify="required" placeholder="用户姓名"
                                       autocomplete="off" class="layui-input" value="${queryUser.name}">
                            </div>
                            <div class="layui-form-mid layui-text-em">用户真实姓名</div>
                        </div>
                        <div class="layui-form-item">
                            <label class="layui-form-label">手机号</label>
                            <div class="layui-input-inline layui-input-wrap">
                                <input type="number" name="phone" lay-verify="required" placeholder="手机号"
                                       autocomplete="off" class="layui-input" value="${queryUser.phone}">
                            </div>
                            <div class="layui-form-mid layui-text-em">后期可发送短信通知，如各种紧急通知或预警信息.</div>
                        </div>
                        <div class="layui-form-item">
                            <label class="layui-form-label">部门</label>
                            <div class="layui-input-inline layui-input-wrap">
                                <select name="departmentId" id="departmentId" lay-verify="required" lay-search="">
                                    <c:forEach items="${departments}" var="department">
                                        <option value="${department.id}" ${queryUser.departmentId == department.id ? 'selected' : ''}>${department.name}(${department.code})</option>
                                    </c:forEach>
                                </select>
                            </div>
                            <div class="layui-form-mid layui-text-em">请选择部门信息.</div>
                        </div>
                        <div class="layui-form-item">
                            <button type="button" lay-submit class="layui-btn layui-bg-blue"
                                    lay-filter="update-user-filter">确认修改
                            </button>
                            <button type="button" id="btn_cancel" class="layui-btn">取消</button>
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

        /**
         * 取消按钮
         */
        $("#btn_cancel").click(function () {
            var index = parent.layer.getFrameIndex(window.name);
            parent.layer.close(index);
        });

        form.verify({
            username: function (value) {
                if (!new RegExp("^[a-zA-Z0-9_\u4e00-\u9fa5\\s·]+$").test(value)) {
                    return '用户名不能有特殊字符';
                }
                if (/(^\_)|(\__)|(\_+$)/.test(value)) {
                    return '用户名首尾不能出现下划线\'_\'';
                }
                if (/^\d+\d+\d$/.test(value)) {
                    return '用户名不能全为数字';
                }
                if (!/(.+){5,12}$/.test(value)) {
                    return '用户名必须 5 到 12 位';
                }
            }
        });

        form.on('submit(update-user-filter)', function (data) {
            $.ajax({
                url: '/user/updateUserMessage',
                type: 'post',
                data: data.field,
                dataType: 'json',
                success: function (res) {
                    if (res.status) {
                        layer.msg(res.msg, {icon: 1, time: 1000}, function () {
                            var index = parent.layer.getFrameIndex(window.name);
                            parent.layer.close(index);
                        });
                    } else {
                        layer.msg(res.msg, {icon: 2, time: 1000});
                    }
                }
            });
        });
        /**
         * 验证用户名
         */
        $("#check_username_btn").click(function () {
            var username = $("input[name='username']").val();
            if (username === "") {
                layer.msg("用户名不能为空", {icon: 2, time: 1000});
            } else {
                $.ajax({
                    url: '/user/checkUsername',
                    type: 'post',
                    data: {
                        username: username
                    },
                    dataType: 'json',
                    success: function (res) {
                        if (res.status) {
                            layer.msg(res.msg, {icon: 1, time: 1000});
                        } else {
                            layer.msg(res.msg, {icon: 2, time: 1000});
                        }
                    }
                });
            }
        });


    });

    function md5WithCryptoJS(string) {
        const hash = CryptoJS.MD5(string).toString();
        return hash;
    }
</script>
</body>
</html>