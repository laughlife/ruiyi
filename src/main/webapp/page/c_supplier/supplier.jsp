<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>供应商管理</title>
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
                <div class="layui-card-header">供应商管理</div>
                <div class="layui-card-body">
                    <form class="layui-form layui-form-pane" action="">
                        <div class="layui-form-item">
                            <div class="layui-inline">
                                <label class="layui-form-label">供应商信息</label>
                                <div class="layui-input-block">
                                    <input type="text" id="key" name="key" placeholder="请输入搜索提示信息"
                                           class="layui-input">
                                </div>
                            </div>
                            <div class="layui-inline">
                                <button type="button" id="search_supplier_btn"
                                        class="layui-btn layui-bg-green"
                                        lay-filter="data-search-btn">
                                    <i class="fa-solid fa-magnifying-glass"></i> 搜索
                                </button>
                            </div>
                            <div class="layui-inline">
                                <button type="button" id="create_supplier_btn"
                                        class="layui-btn layui-bg-blue"
                                        lay-filter="data-create-btn">
                                    <i class="fa-solid fa-magnifying-glass"></i> 新建供应商
                                </button>
                            </div>
                        </div>
                    </form>
                </div>
                <div class="layui-card-body">
                    <table id="supplierTable" class="layui-hide" lay-filter="supplierTableFilter"></table>
                </div>
            </div>

        </div>
    </div>
</div>

<script type="text/html" id="supplierTableToolbar">
    <div class="layui-btn-container">
        <button class="layui-btn layui-btn-sm layui-btn-danger" lay-event="delete">
            <i class="fa-solid fa-trash"></i>删除
        </button>
    </div>
</script>

<script>
    layui.use(function () {
        var table = layui.table;
        var layer = layui.layer;
        var $ = layui.jquery;

        var supplierTable = table.render({
            elem: '#supplierTable',
            url: '/supplier/queryAllSupplier',
            cols: [[
                {type: 'numbers', title: '编号', width: 80},
                {field: 'name', title: '名称', edit: 'text', width: 200},
                {field: 'fzr', title: '联系人', edit: 'text', width: 280},
                {field: 'phone', title: '联系方式', edit: 'text', width: 280},
                {field: 'address', title: '联系地址', edit: 'text'},
                {field: 'otherInfo', title: '其他信息', edit: 'text'},
                {field: 'createTime', title: '创建时间', width:180},
                {align: 'center', title: '操作', toolbar: '#supplierTableToolbar', width: 200}
            ]],
            page: true,
            limits: [50, 100, 200],
            limit: 50
        });

        $('#search_supplier_btn').click(function () {
            var key = $('#key').val();
            supplierTable.reload({
                where: {
                    key: key
                }
            });
        });

        $('#create_supplier_btn').click(function () {
            layer.open({
                title: '新建供应商',
                type: 2,
                shade: 0.5,
                shadeClose: true,
                area: ['60%', '60%'],
                content: '/supplier/goCreateSupplier',
                end: function () {
                    supplierTable.reload();
                }
            });
        });

        table.on('edit(supplierTableFilter)', function (obj) {
            var data = obj.data;
            var field = obj.field;
            var value = obj.value;
            $.ajax({
                url: '/supplier/updateSupplier',
                type: 'POST',
                data: {
                    id: data.id,
                    field: field,
                    value: value
                },
                dataType: 'json',
                success: function (res) {
                    layer.msg(res.msg);
                }
            });
        });

        table.on('tool(supplierTableFilter)', function (obj) {
                var _data = obj.data;
                if (obj.event === 'delete') {
                    $.ajax({
                        url: '/supplier/deleteSupplier',
                        type: 'POST',
                        data: {
                            'id': _data.id
                        },
                        dataType: 'json',
                        success: function (res) {
                            layer.msg(res.msg);
                            supplierTable.reload();
                        }
                    });
                }
            }
        );

    });
</script>
</body>
</html>
