<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>人员管理</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0">
    <meta name="_csrf" content="${_csrf.token}" />
    <meta name="_csrf_header" content="${_csrf.headerName}" />
    <link rel="stylesheet" href="/static/layui/css/layui.css" media="all">
    <link rel="stylesheet" href="/static/fontawesome6/css/all.min.css" media="all">
    <script src="/static/layui/layui.js"></script>
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

<div class="layui-fluid">
    <div class="layui-row layui-col-space15">
        <div class="layui-col-md12">
            <div class="layui-card">
                <div class="layui-card-header">人员管理</div>
                <div class="layui-card-body">
                    <form class="layui-form layui-form-pane" action="">
                        <div class="layui-form-item">
                            <div class="layui-inline">
                                <label class="layui-form-label">用户信息</label>
                                <div class="layui-input-block">
                                    <input type="text" id="key" name="key" placeholder="请输入搜索提示信息" class="layui-input">
                                </div>
                            </div>
                            <div class="layui-inline">
                                <label class="layui-form-label">部门选择</label>
                                <div class="layui-input-inline">
                                    <select name="departmentCode" id="departmentCode" lay-search="">
                                        <option value="">选择部门</option>
                                        <c:forEach items="${departments}" var="department">
                                            <option value="${department.code}">${department.name}(${department.code})</option>
                                        </c:forEach>
                                    </select>
                                </div>
                            </div>
                            <div class="layui-inline">
                                <button type="button" id="search_user_btn"
                                        class="layui-btn layui-bg-green"
                                        lay-filter="data-search-btn">
                                    <i class="fa-solid fa-magnifying-glass"></i> 搜索
                                </button>
                            </div>
                            <div class="layui-inline">
                                <button type="button" id="create_user_btn"
                                        class="layui-btn layui-bg-blue"
                                        lay-filter="data-search-btn">
                                    <i class="fa-solid fa-magnifying-glass"></i> 新建用户
                                </button>
                            </div>
                        </div>
                    </form>
                </div>
                <div class="layui-card-body">
                    <table id="userTable" class="layui-hide" lay-filter="userTableFilter"></table>
                </div>
            </div>
        </div>
    </div>
</div>
<script type="text/html" id="userEdit">
    <%--{{# if(d.level === 1 || d.level === 2){ }}--%>
        <a class="layui-btn layui-btn-sm" lay-event="updateMessage">信息修改</a>
    <%--{{# } }}--%>
    <a class="layui-btn layui-btn-sm layui-bg-blue" lay-event="updatePassword">重置密码</a>
    <a class="layui-btn layui-btn-sm layui-bg-blue" lay-event="updateLadder">负责人设置/取消</a>
    <c:if test="${user.isAdmin eq 1}">
        <a class="layui-btn layui-btn-sm layui-bg-red" lay-event="updateAdmin">管理员设定/取消</a>
    </c:if>
    <a class="layui-btn layui-btn-sm layui-bg-red" lay-event="updateBan">禁用/启用用户</a>
</script>
<script src="/static/js/crypto-js.min.js" type="text/javascript" charset="utf-8"></script>
<script>
    var userTable = null;
    layui.use(function () {
        var table = layui.table;
        var layer = layui.layer;
        var $ = layui.jquery;
        userTable = table.render({
            elem: '#userTable',
            url: '/user/queryUser',
            method: 'post',
            page: true,
            limit: 20,
            limits: [20, 30, 50, 100],
            cols: [[
                {type: 'numbers', title: '编号', width: 80},
                {title: '用户名', width: 150, field: 'username'},
                {title: '姓名', width: 150, field: 'name'},
                {title: '手机号', width: 200, field: 'phone'},
                {title: '部门',  field: 'department'},
                {title: '负责人', width: 80, field: 'is_ladder',templet: function(d) {
                        return d.is_ladder === '是' ? '<span style="color:green">√</span>' : '<span style="color:red">×</span>';
                    }
                },
                {title: '管理员', width: 80, field: 'is_admin',templet: function(d) {
                        return d.is_admin === '是' ? '<span style="color:green">√</span>' : '<span style="color:red">×</span>';
                    }
                },
                {title: '状态', width: 80, field: 'is_ban',templet: function(d) {
                        return d.is_ban === '是' ? '<span style="color:green">√</span>' : '<span style="color:red">×</span>';
                    }
                },
                {field: 'right', align: 'center', title: '操作', toolbar: '#userEdit', width: 600}
            ]]
        });

        table.on('tool(userTableFilter)', function (obj) {
            var _data = obj.data;
            if (obj.event === 'updateMessage') {
                layer.open({
                    title: '修改用户角色',
                    type: 2,
                    shade: 0.5,
                    maxmin: true,
                    shadeClose: true,
                    area: ['40%', '60%'],
                    content: '/user/goUpdateUserPage?id=' + _data.id,
                    end: function () {
                        userTable.reload();
                    }
                });
            }else if(obj.event === 'updatePassword'){
                layer.prompt({title: '重置密码框', formType: 1}, function(pass, index){
                    layer.close(index);
                    $.ajax({
                        url: '/user/resetUserPassword',
                        type: 'post',
                        data: {
                            id: _data.id,
                            password: md5WithCryptoJS(pass)
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
                });
            }else if(obj.event === 'updateLadder'){
                $.ajax({
                    url: '/user/updateLadder',
                    type: 'post',
                    data: {
                        id: _data.id
                    },
                    dataType: 'json',
                    success: function (res) {
                        if (res.status) {
                            layer.msg(res.msg, {icon: 1, time: 1000});
                            userTable.reload();
                        } else {
                            layer.msg(res.msg, {icon: 2, time: 1000});
                        }
                    }
                });
            }else if(obj.event === 'updateAdmin'){
                $.ajax({
                    url: '/user/updateAdmin',
                    type: 'post',
                    data: {
                        id: _data.id
                    },
                    dataType: 'json',
                    success: function (res) {
                        if (res.status) {
                            layer.msg(res.msg, {icon: 1, time: 1000});
                            userTable.reload();
                        } else {
                            layer.msg(res.msg, {icon: 2, time: 1000});
                        }
                    }
                });
            }else if(obj.event === 'updateBan'){
                $.ajax({
                    url: '/user/updateBan',
                    type: 'post',
                    data: {
                        id: _data.id
                    },
                    dataType: 'json',
                    success: function (res) {
                        if (res.status) {
                            layer.msg(res.msg, {icon: 1, time: 1000});
                            userTable.reload();
                        } else {
                            layer.msg(res.msg, {icon: 2, time: 1000});
                        }
                    }
                });
            }
        });
        /**
         * 搜索按钮点击事件
         */
        $("#search_user_btn").click(function () {
            var key = $("#key").val();
            var departmentCode = $("#departmentCode").val();
            var searchParams = {
                key: key,
                departmentCode: departmentCode
            };
            userTable.reload({
                where: searchParams,
                page: {
                    curr: 1
                }
            });
        });
        /**
         * 新建用户按钮点击事件
         */
        $("#create_user_btn").click(function () {
            layer.open({
                title: '新建用户',
                type: 2,
                shade: 0.5,
                maxmin: true,
                shadeClose: true,
                area: ['40%', '60%'],
                content: '/user/goCreateUserPage',
                end: function () {
                    userTable.reload();
                }
            });
        });

    });
    function md5WithCryptoJS(string) {
        const hash = CryptoJS.MD5(string).toString();
        return hash;
    }
</script>
</body>
</html>


