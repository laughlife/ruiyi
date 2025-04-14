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


    <link rel="stylesheet" href="/static/zTree/css/zTreeStyle/zTreeStyle.css" />
    <script src="/static/zTree/js/jquery-1.4.4.min.js"></script>
    <script src="/static/zTree/js/jquery.ztree.core.min.js"></script>
    <script src="/static/zTree/js/jquery.ztree.excheck.min.js"></script>
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
            <div class="layui-card">
                <div class="layui-card-header" id="permission_title_div">权限</div>
                <div class="layui-card-body ztree" id="permission_div">

                </div>
            </div>
        </div>
    </div>
</div>
<script>
    layui.use(function () {
        var tree = layui.tree;
        $(document).ready(function () {
            $.ajax({
                url: '/permission/initDepartmentTree',
                type: 'get',
                dataType: 'json',
                success: function (data) {
                    if (data.status) {
                        var _data = data.data;
                        tree.render({
                            elem: '#department_div',
                            data: _data,
                            onlyIconControl: true,
                            id: 'department_tree',
                            isJump: true,
                            click: function (obj) {
                                var data = obj.data;
                                var title = data.title;
                                $('#permission_title_div').html( title + " —— 权限信息：");
                                $("#permission_div").html("");
                                initPermissionTree(data.id);
                            }
                        });
                    }
                }
            })
        });

        function initPermissionTree(departmentId) {
            $.ajax({
                url: '/permission/getPermissionTree',
                type: 'get',
                dataType: 'json',
                data: {
                    departmentId: departmentId
                },
                success: function (rt) {
                    if (rt.status) {
                        showPermissionTree(rt.data, departmentId);
                    }
                }
            });
        }

    });

    function showPermissionTree(_data, departmentId) {
        var setting = {
            check: {
                enable: true,
                chkStyle: "checkbox",
                chkboxType: { "Y": "ps", "N": "ps" }
            },
            data: {
                simpleData: {
                    enable: true,
                    pIdKey: "pId",
                    idKey: "id",
                    rootPId: -1
                },
                key: {
                    name: "name"
                }
            },
            callback: {
                onCheck: function(event, treeId, treeNode){
                    var nodes = $.fn.zTree.getZTreeObj("permission_div").getCheckedNodes(true);
                    var arr = nodes.map(function(node) {
                        return node.id;
                    });

                    // 发送选中的权限数据到后台
                    $.ajax({
                        url: '/permission/updatePermission',
                        type: 'post',
                        dataType: 'json',
                        data: {
                            departmentId: departmentId,
                            permissionIds: arr
                        },
                        success: function (rt) {
                            layer.msg(rt.msg);
                        }
                    });
                }
            }
        };

        // 渲染 zTree
        $.fn.zTree.init($("#permission_div"), setting, _data);
    }

</script>
</body>
</html>
