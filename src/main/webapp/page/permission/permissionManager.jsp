<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>权限管理</title>
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
        <div class="layui-col-md3">
            <div class="layui-card">
                <div class="layui-card-header">部门</div>
                <div class="layui-card-body" id="department_div">

                </div>
            </div>
        </div>
        <div class="layui-col-md9">
            <div class="layui-card-header">权限</div>
            <div class="layui-card-body">

            </div>
        </div>
    </div>
</div>
<script>
    var department_tree;
    var permission_tree;
    layui.use(function () {
        var tree = layui.tree;
        var layer = layui.layer;
        var util = layui.util;
        var $ = layui.jquery;

        $(document).ready(function () {
            $.ajax({
                url: '/permission/initDepartmentTree',
                type: 'get',
                dataType: 'json',
                success: function (data) {
                    if (data.status) {
                        var _data = data.data;
                        department_tree = tree.render({
                            elem: '#department_div',
                            data: _data,
                            onlyIconControl: true,
                            id: 'department_tree',
                            isJump: true,
                            click: function (obj) {
                                var data = obj.data;
                                initPermissionTree(data.id);
                            }
                        });
                    }
                }
            })
        });

    });

    function initPermissionTree(departmentId) {

        layui.use(function () {
            var tree = layui.tree;
            var layer = layui.layer;
            var util = layui.util;
            var $ = layui.jquery;

            $.ajax({
                url: '/permission/getPermissionTree',
                type: 'get',
                dataType: 'json',
                data: {
                    departmentId: departmentId
                },
                success: function (data) {
                    if (data.status) {

                    }
                }
            });
        });
    }

</script>
</body>
</html>
