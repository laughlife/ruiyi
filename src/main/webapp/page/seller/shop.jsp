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

            <div class="layui-card">
                <div class="layui-card-header">用户列表</div>
                <div class="layui-card-body">
                    <table id="userTable" class="layui-hide" lay-filter="userTableFilter"></table>
                </div>
            </div>

            <div class="layui-card" id="showShop" style="display: none;">
                <div class="layui-card-header">店铺信息</div>
                <div class="layui-card-body">
                    <table id="shopTable" class="layui-table" lay-filter="shopTableFilter"></table>
                </div>
            </div>
        </div>
    </div>
</div>

<script type="text/html" id="userTableToolbar">
    <div class="layui-btn-container">
        <button class="layui-btn layui-btn-sm" lay-event="query">
            <i class="fa-solid fa-user fa-fw"></i>查看店铺
        </button>
        <button class="layui-btn layui-btn-sm" lay-event="bindShop">
            <i class="fa-solid fa-user fa-fw"></i>绑定店铺
        </button>
    </div>
</script>
<script type="text/html" id="shopTableToolbar">
    <div class="layui-btn-container">
        <button class="layui-btn layui-bg-red layui-btn-sm" lay-event="unbindShop">
            <i class="fa-solid fa-user fa-fw"></i>解除绑定
        </button>
    </div>
</script>
<script>
    layui.use(function () {
        var table = layui.table;
        var layer = layui.layer;
        var $ = layui.jquery;
        var userTable,shopTable;

        var userTable = table.render({
            elem: '#userTable',
            url: '/seller/queryUser',
            tree: {},
            cols: [[
                {type: 'numbers', title: '编号', width: 80},
                {field: 'name', title: '姓名', width: 200},
                {field: 'phone', title: '联系方式', width: 200},
                {field: 'department', title: '部门', width: 200},
                {field: 'shops', title: '绑定店铺列表'},
                {align: 'center', title: '操作', toolbar: '#userTableToolbar', width: 400}
            ]],
            page: false
        });


        table.on('tool(userTableFilter)', function (obj) {
                var _data = obj.data;
                if (obj.event === 'bindShop') {
                    layer.open({
                        title: '绑定店铺',
                        type: 2,
                        shade: 0.5,
                        shadeClose: true,
                        area: ['60%', '60%'],
                        content: '/seller/goBindSellerPage?userId=' + _data.id,
                        end: function () {
                            userTable.reload();
                        }
                    });
                } else if (obj.event === 'query') {
                    $('#showShop').show();
                    shopTable = table.render({
                        elem: '#shopTable',
                        url: '/seller/queryShop',
                        where: {'id': _data.id},
                        page: false,
                        cols: [[
                            {type: 'numbers', title: '编号', width: 80},
                            {title: '店铺',  field: 'name'},
                            {title: '国家',  field: 'country'},
                            {align: 'center', title: '操作', toolbar: '#shopTableToolbar'}
                        ]]
                    });
                }
            }
        );


        table.on('tool(shopTableFilter)', function (obj) {
            var _data = obj.data;
            var userId = _data.userId;
            if (obj.event === 'unbindShop') {
                $.ajax({
                    url: '/seller/unbindShop',
                    type: 'POST',
                    data: {
                        'sellerId': _data.id,
                        'userId': userId
                    },
                    dataType: 'json',
                    success: function (res) {
                        userTable.reload();
                        shopTable.reload({
                            where: {'id': userId}
                        });
                    }
                });
            }
        });

    });
</script>
</body>
</html>
