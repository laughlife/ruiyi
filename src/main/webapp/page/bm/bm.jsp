<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>部门管理</title>
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
                <div class="layui-card-header">部门管理</div>
                <div class="layui-card-body">
                    <table id="bmTable" class="layui-hide" lay-filter="bmTableFilter"></table>
                </div>
            </div>

            <div class="layui-card" id="showBmcy" style="display: none;">
                <div class="layui-card-header">部门信息</div>
                <div class="layui-card-body">
                    <table id="bmcyTable" class="layui-table" lay-filter="bmcyTableFilter"></table>
                </div>
            </div>
        </div>
    </div>
</div>

<script type="text/html" id="bmTableToolbar">
    <div class="layui-btn-container">
        {{# if(d.level === 1 || d.level === 2){ }}
            <button class="layui-btn layui-bg-blue layui-btn-sm" lay-event="addChildBm">
                <i class="fa-solid fa-folder-plus"></i>添加子部门
            </button>
        {{# } }}
        <button class="layui-btn layui-btn-sm" lay-event="query">
            <i class="fa-solid fa-user fa-fw"></i>查看信息
        </button>
        <button class="layui-btn layui-btn-sm layui-btn-danger" lay-event="delete">
            <i class="fa-solid fa-trash"></i>删除
        </button>
    </div>
</script>

<script type="text/html" id="bm-table-toolbar">
    <div class="layui-btn-container">
        <c:if test="${user.isAdmin eq 1}">
            <button class="layui-btn layui-bg-blue layui-btn-sm" lay-event="addRootNode">添加一级部门</button>
        </c:if>
    </div>
</script>
<script>
    layui.use(function () {
        var treeTable = layui.treeTable;
        var table = layui.table;
        var layer = layui.layer;
        var $ = layui.jquery;
        var bmcyTable;

        var bmTable = treeTable.render({
            elem: '#bmTable',
            url: '/department/queryAllBm',
            tree: {},
            toolbar: '#bm-table-toolbar',
            cols: [[
                {type: 'numbers', title: '编号', width: 80},
                {field: 'name', title: '名称', edit: 'text', width: 200},
                {field: 'description', title: '描述信息', edit: 'text'},
                {field: 'px', title: '排序', edit: 'number', width: 80},
                {field: 'code', title: '编码', width: 300},
                {field: 'path', title: '路径', width: 300},
                {field: 'level', title: '层级', width: 80},
                {align: 'center', title: '操作', toolbar: '#bmTableToolbar', width: 400}
            ]],
            page: false
        });

        treeTable.on("toolbar(bmTableFilter)", function (obj) {
            // 获取选中行
            if (obj.event === "addRootNode") {
                layer.open({
                    title: '新建部门',
                    type: 2,
                    shade: 0.5,
                    shadeClose: true,
                    area: ['60%', '60%'],
                    content: '/department/goCreateBmPage',
                    end: function () {
                        bmTable.reload();
                    }
                });
            }
        });


        treeTable.on('edit(bmTableFilter)', function (obj) {
            var data = obj.data;
            var field = obj.field;
            var value = obj.value;
            $.ajax({
                url: '/department/updateBm',
                type: 'POST',
                data: {
                    id: data.id,
                    field: field,
                    value: value
                },
                dataType: 'json',
                success: function (res) {
                    layer.msg(res.message);
                }
            });
        });

        treeTable.on('tool(bmTableFilter)', function (obj) {
                var _data = obj.data;
                if (obj.event === 'addChildBm') {
                    layer.open({
                        title: '新建子部门',
                        type: 2,
                        shade: 0.5,
                        shadeClose: true,
                        area: ['60%', '60%'],
                        content: '/department/goCreateBmPage?parentId=' + _data.id,
                        end: function () {
                            bmTable.reload();
                        }
                    });
                }else if (obj.event === 'delete') {
                    $.ajax({
                        url: '/department/deleteDepartment',
                        type: 'POST',
                        data: {
                            'id': obj.data.id
                        },
                        dataType: 'json',
                        success: function (res) {
                            layer.msg(res.msg);
                            if (res.status) {
                                bmTable.reload();
                            }
                        }
                    });
                } else if (obj.event === 'query') {
                    $('#showBmcy').show();
                    bmcyTable = table.render({
                        elem: '#bmcyTable',
                        url: '/department/queryAllBmcy',
                        where: {'id': _data.id},
                        page: false,
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
                            }
                        ]]
                    });
                }
            }
        );


        table.on('tool(bmcyTableFilter)', function (obj) {
            var _data = obj.data;
            if (obj.event === 'delete') {
                $.ajax({
                    url: '/bm/deleteBmcy',
                    type: 'POST',
                    data: {'id': _data.id},
                    dataType: 'json',
                    success: function (res) {
                        bmcyTable.reload();
                    }
                });
            } else if (obj.event === 'setld') {
                $.ajax({
                    url: '/bm/setld',
                    type: 'POST',
                    data: {'id': _data.id},
                    dataType: 'json',
                    success: function (res) {
                        bmcyTable.reload();
                    }
                });
            }
        });

    });
</script>
</body>
</html>
