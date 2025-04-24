<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta charset="utf-8">
    <title>添加部门信息</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <meta name="_csrf" content="${_csrf.token}"/>
    <meta name="_csrf_header" content="${_csrf.headerName}"/>
    <link rel="stylesheet" href="/static/layui/css/layui.css" media="all">
    <link rel="stylesheet" href="/lib/font-awesome-6.6/css/all.min.css" media="all">
    <script src="/static/layui/layui.js" charset="utf-8"></script>
    <script src="/static/layui/config.js" charset="utf-8"></script>
    <script src="/static/jquery/jquery-3.7.1.min.js"></script>
    <script>
        $.ajaxSetup({
            beforeSend: function (xhr) {
                var header = $('meta[name="_csrf_header"]').attr('content');
                var token  = $('meta[name="_csrf"]').attr('content');
                xhr.setRequestHeader(header, token);
            }
        });
    </script>
</head>
<body>
<div class="layuimini-container">
    <div class="layuimini-main">
        <div class="layui-row layui-col-space15">
            <div class="layui-col-md12">
                <fieldset class="table-search-fieldset">
                    <legend>添加部门成员</legend>
                    <div style="margin: 10px 10px 10px 10px;min-height: 800px;">
                        <form class="layui-form layui-form-pane" action="">
                            <div class="layui-form-item">
                                <div class="layui-inline">
                                    <div class="layui-input-inline" id="chooseDateDiv" style="width:480px">
                                        <label class="layui-form-label">部门名称</label>
                                        <div class="layui-input-block">
                                            <input type="text" class="layui-input" name="name" lay-verify="required"
                                                   disabled value="${bm.name}">
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="layui-form-item">
                                <div class="layui-inline" style="width:90%;">
                                    <label class="layui-form-label">搜索员工</label>
                                    <div class="layui-input-inline" style="width:60%;">
                                        <input type="text" id="yg_input" name="yg_input"
                                               placeholder="请输入员工信息(姓名|电话|身份证号)" class="layui-input">
                                    </div>
                                    <div class="layui-input-inline">
                                        <button type="button" id="search_user_btn"
                                                class="layui-btn" lay-submit
                                                lay-filter="xjbm_filter"><i
                                                class="fa-solid fa-magnifying-glass fa-fw"></i>搜索员工
                                        </button>
                                    </div>
                                </div>
                            </div>
                            <div class="layui-form-item">
                                <div class="layui-form-item">
                                    <label class="layui-form-label">搜索结果</label>
                                    <div class="layui-input-block" id="search_user_div">
                                        <c:forEach items="${queryUser}" var="user">
                                            <input type="checkbox" name="nuser[${user.id}]" title="${user.name}">
                                        </c:forEach>
                                    </div>
                                </div>
                            </div>
                            <div class="layui-form-item">
                                <div class="layui-form-item">
                                    <label class="layui-form-label">现有人员</label>
                                    <div class="layui-input-block">
                                        <c:forEach items="${userList}" var="user">
                                            <input type="checkbox" name="user[${user.id}]" title="${user.name}" checked>
                                        </c:forEach>
                                    </div>
                                </div>
                            </div>
                            <div class="layui-form-item" style="text-align: center;">
                                <button type="button" id="create_gd_btn"
                                        class="layui-btn layui-btn-normal" lay-submit
                                        lay-filter="xjbm_filter"><i class="fa-solid fa-plus"></i>确定添加
                                </button>
                                <button type="button" id="close_win_btn"
                                        class="layui-btn layui-btn-warm">
                                    <i class="fa-solid fa-circle-xmark fa-fw"></i>取消
                                </button>
                            </div>
                        </form>
                    </div>
                </fieldset>
            </div>
        </div>
    </div>
</div>
<script>
    var form;
    $("#close_win_btn").click(function () {
        var iframeIndex = parent.layer.getFrameIndex(window.name);
        parent.layer.close(iframeIndex);
    });
    layui.use('form', function () {
        form = layui.form;
    });
    $("#search_user_btn").click(function () {
        var _search_date = $("#yg_input").val();
        $.ajax({
            url: '/bm/searchUser',
            type: 'post',
            data: {"id": "${id}", "key": _search_date},
            dataType: 'json',
            success: function (data) {
                var _users = data.users;

                if (_users.length > 0) {
                    $("#search_user_div").empty();
                    for (var i = 0; i < _users.length; i++) {
                        var user = _users[i];
                        var str = `<input type="checkbox" name="nuser[\${user.id}]" title="\${user.name}">`;
                        $("#search_user_div").append(str);
                    }
                    form.render();
                }
            }
        })
    });

    $("#create_gd_btn").click(function () {
        // 获取所有 name 以 nuser 开头的复选框，并筛选出选中的
        var selectedNewUsers = [];
        $("input[name^='nuser']:checked").each(function () {
            selectedNewUsers.push($(this).attr("name").match(/\d+/)[0]); // 提取数字 ID
        });

        // 获取所有 name=user[*] 的复选框，筛选出未选中的值
        var unselectedOldUsers = [];
        $("input[name^='user']:not(:checked)").each(function () {
            unselectedOldUsers.push($(this).attr("name").match(/\d+/)[0]); // 提取数字 ID
        });

        // 如果需要提交到后台
        $.ajax({
            url: '/bm/updateUsers',
            type: 'post',
            data: {
                'newUserIds': selectedNewUsers, // 新添加的用户 ID
                'removedUserIds': unselectedOldUsers, // 被移除的用户 ID
                'id': "${id}"
            },
            dataType: 'json',
            success: function (response) {
                if (response.status) {
                    layer.msg(response.message);
                    var iframeIndex = parent.layer.getFrameIndex(window.name);
                    parent.layer.close(iframeIndex); // 关闭弹窗
                } else {
                    layer.msg("添加失败：" + response.message);
                }
            },
            error: function () {
                layer.msg("请求失败，请稍后再试！");
            }
        });
    });
</script>
</body>
</html>
