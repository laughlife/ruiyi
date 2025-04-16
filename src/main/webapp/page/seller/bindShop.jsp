<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>店铺绑定</title>
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
            <div class="layui-card" id="showShop">
                <div class="layui-card-header">店铺列表</div>
                <div class="layui-card-body">
                    <table id="shopTable" class="layui-table" lay-filter="shopTableFilter"></table>
                </div>
            </div>
        </div>
    </div>
</div>

<script type="text/html" id="shopTableToolbar">
    <div class="layui-btn-container">
        <button class="layui-btn layui-bg-blue layui-btn-sm" lay-event="unbindShop">
            <i class="fa-solid fa-user fa-fw"></i>绑定
        </button>
    </div>
</script>
<script>
    layui.use(function () {
        var table = layui.table;
        var layer = layui.layer;
        var $ = layui.jquery;

        var shopTable = table.render({
            elem: '#shopTable',
            url: '/seller/queryUnbindShop',
            where: {'userId': '${userId}'},
            page: false,
            cols: [[
                {type: 'numbers', title: '编号', width: 80},
                {title: '店铺', width: 150, field: 'name'},
                {title: '国家', width: 150, field: 'country'},
                {align: 'center', title: '操作', toolbar: '#shopTableToolbar'},
                {field: 'status', title: '状态', templet: function(d) {
                        return d.status === '正常' ? '<span style="color:green">'+d.status+'</span>' : '<span style="color:red">'+d.status+'</span>';
                    }
                }
            ]]
        });

        table.on('tool(shopTableFilter)', function (obj) {
                var _data = obj.data;
                if (obj.event === 'query') {

                }else if (obj.event === 'unbindShop') {
                    $.ajax({
                        url: '/bm/deleteBmcy',
                        type: 'POST',
                        data: {'id': _data.id},
                        dataType: 'json',
                        success: function (res) {
                            bmcyTable.reload();
                        }
                    });
                }
            }
        );


    });
</script>
</body>
</html>
