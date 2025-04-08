<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>所有功能</title>
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
                <div class="layui-card-header">菜单设置</div>
                <div class="layui-card-body">
                    <table class="layui-hide" id="permissionTreeTable" lay-filter="permissionTreeTable"></table>
                </div>
            </div>
        </div>
    </div>
</div>
<script type="text/html" id="table-toolbar">
    <div class="layui-btn-container">
        <button class="layui-btn layui-bg-blue layui-btn-sm" lay-event="addRootNode">添加根节点</button>
    </div>
</script>
<script type="text/html" id="permission-treeTable-tools">
    <div class="layui-btn-container">
        <a class="layui-btn layui-bg-blue layui-btn-xs" lay-event="addChild">新增子节点</a>
        <a class="layui-btn layui-bg-red layui-btn-xs" lay-event="delete">删除</a>
    </div>
</script>
<script>
    layui.use(function () {
        var treeTable = layui.treeTable;
        var layer = layui.layer;
        var $ = layui.jquery;
        // 渲染
        var inst = treeTable.render({
            elem: '#permissionTreeTable',
            url: '/home/initMenu',
            tree: {},
            toolbar: '#table-toolbar',
            cols: [[
                {type: 'numbers', title: '编号', width: 80},
                {field: 'name', title: '权限名称', edit: 'text', width: 220},
                {field: 'icon', title: '图标', edit: 'text', width: 200},
                {field: 'path', title: '路径', edit: 'text'},
                {field: 'description', title: '描述信息', edit: 'text', width: 400},
                {field: 'px', title: '排序', edit: 'text', width: 80},
                {field: 'isLink', title: '是否链接', edit: 'text', width: 160},
                {fixed: "right", title: "操作", width: 190, align: "center", toolbar: "#permission-treeTable-tools"}
            ]],
            page: false
        });

        treeTable.on('edit(permissionTreeTable)', function(obj) {
            var field = obj.field; // 得到字段
            var value = obj.value; // 得到修改后的值
            var data = obj.data; // 得到所在行所有键值
            $.ajax({
                url: "/home/updatePermission",
                type: "POST",
                data: {
                    "id": data.id,
                    "field": field,
                    "value": value
                },
                dataType: "json",
                success: function (data) {
                    if (data.status) {
                        layer.msg(data.msg);
                    }
                }
            });

        });
        // 表头工具栏工具事件
        treeTable.on("toolbar(permissionTreeTable)", function (obj) {
            // 获取选中行
            if (obj.event === "addRootNode") {
                layer.prompt({title: '添加根节点', formType: 2}, function (pass, index) {
                    layer.close(index);
                    $.ajax({
                        url: "/home/addRootMenu",
                        type: "POST",
                        data: {
                            "name": pass
                        },
                        dataType: "json",
                        success: function (data) {
                            if (data.status) {
                                layer.msg(data.msg);
                            }
                            inst.reload();
                        }
                    });
                });
            }
        });
        // 单元格工具事件
        treeTable.on('tool(' + inst.config.id + ')', function (obj) {
            var layEvent = obj.event; // 获得 lay-event 对应的值
            var trElem = obj.tr;
            var trData = obj.data;
            var tableId = obj.config.id;
            if (layEvent === "delete") {
                $.ajax({
                    url: "/home/deletePermission",
                    type: "POST",
                    data: {
                        "id": trData.id
                    },
                    dataType: "json",
                    success: function (data) {
                        if (data.status) {
                            layer.msg(data.msg);
                        }
                        inst.reload();
                    }
                });
            } else if (layEvent === "addChild") {
                layer.prompt({title: '添加子节点', formType: 2}, function (pass, index) {
                    layer.close(index);
                    $.ajax({
                        url: "/home/addChildMenu",
                        type: "POST",
                        data: {
                            "parentId": trData.id,
                            "name": pass
                        },
                        dataType: "json",
                        success: function (data) {
                            if (data.status) {
                                layer.msg(data.msg);
                            }
                            inst.reload();
                        }
                    });
                });
            }
        });
    });
</script>
</body>
</html>
