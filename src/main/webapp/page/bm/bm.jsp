<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>部门管理</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0">

    <link rel="stylesheet" href="/static/layui/css/layui.css" media="all">
    <link rel="stylesheet" href="/static/fontawesome6/css/all.min.css" media="all">
    <script src="/static/layui/layui.js"></script>
</head>
<body>

<div class="layui-fluid">
    <div class="layui-row layui-col-space15">
        <div class="layui-col-md12">
            <div class="layui-card">
                <div class="layui-card-header">部门信息维护</div>
                <div class="layui-card-body">
                    <div style="margin: 10px 10px 10px 10px">
                        <form class="layui-form layui-form-pane" action="">
                            <div class="layui-form-item">
                                <div class="layui-inline">
                                    <button type="button" id="create_bm_btn"
                                            class="layui-btn layui-btn-primary icon-blue" lay-submit
                                            lay-filter="xjbm_filter"><i class="fa-solid fa-plus"></i> 新建部门
                                    </button>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
            <div class="layui-card">
                <div class="layui-card-header">部门信息</div>
                <div class="layui-card-body">
                    <div class="layui-row layui-col-space15">
                        <table id="bmTable" class="layui-table" lay-filter="bmTableFilter"></table>
                    </div>
                </div>
            </div>

            <div class="layui-card" id="showBmcy" style="display: none;">
                <div class="layui-card-header">部门信息</div>
                <div class="layui-card-body">
                    <div class="layui-row layui-col-space15">
                        <table id="bmcyTable" class="layui-table" lay-filter="bmcyTableFilter"></table>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<script type="text/html" id="bmTableToolbar">
    <div class="layui-btn-container">
        <button class="layui-btn layui-btn-sm" lay-event="query"><i class="fa-solid fa-user fa-fw"></i>查看信息
        </button>
        <button class="layui-btn layui-btn-sm" lay-event="addBmcy"><i class="fa-solid fa-user-plus fa-fw"></i>添加成员
        </button>
        <button class="layui-btn layui-btn-sm layui-btn-danger" lay-event="delete"><i class="fa-solid fa-trash"></i>删除
        </button>
    </div>
</script>
<script type="text/html" id="bmcyTableToolbar">
    <div class="layui-btn-container">
        <button class="layui-btn layui-btn-sm" lay-event="setld"><i class="fa-solid fa-check fa-fw"></i>&nbsp;&nbsp;设为领导</button>
        <button class="layui-btn layui-btn-sm layui-btn-danger" lay-event="delete"><i class="fa-solid fa-minus fa-fw"></i>成员离岗</button>
    </div>
</script>

<script>
    var bmcyTable, bmTable;
    layui.use(['layer', 'form', 'table', 'miniTab'], function () {
        var layer = layui.layer,
            table = layui.table,
            miniTab = layui.miniTab;
        miniTab.listen();

        bmTable = table.render({
            elem: '#bmTable',
            url: '/bm/queryAllBm',
            page: false,
            cols: [[
                {type: 'numbers', title: '编号', width: 80},
                {field: 'id', title: 'ID', hide: true},
                {field: 'name', title: '名称', edit: 'text', width: 150},
                {field: 'ms', title: '描述信息', edit: 'text'},
                {field: 'idx', title: '权重', edit: 'number', width: 80},
                {align: 'center', title: '操作', toolbar: '#bmTableToolbar', width: 400}
            ]]
        });

        table.on('edit(bmTableFilter)', function(obj) {
            var data = obj.data; // Get the entire row data
            var field = obj.field; // Get the field name
            var value = obj.value; // Get the field value
            $.ajax({
                url: '/bm/updateBm',
                type: 'POST',
                data: {
                    id: data.id,
                    field: field,
                    value: value
                },
                dataType: 'json',
                success: function(res) {
                    layer.msg(res.message);
                }
            });
        });

        $("#create_bm_btn").click(function () {
            layer.open({
                title: '新建部门',
                type: 2,
                shade: 0.2,
                maxmin: true,
                shadeClose: true,
                area: ['60%', '60%'],
                content: '/bm/goCreateBmPage',
                end: function () {
                    bmTable.reload();
                }
            });
        });

        table.on('tool(bmTableFilter)', function (obj) {
            var _data = obj.data;
            if (obj.event === 'delete') {
                $.ajax({
                    url: '/bm/deleteBm',
                    type: 'POST',
                    data: {
                        'id': queryId
                    },
                    success: function (res) {
                        bmTable.reload();
                    }
                });
            } else if (obj.event === 'query') {
                $('#showBmcy').show();
                bmcyTable = table.render({
                    elem: '#bmcyTable',
                    url: '/bm/queryAllBmcy',
                    where: {'id': _data.id},
                    page: false,
                    cols: [[
                        {type: 'numbers', title: '编号', width: 80},
                        {type: 'id', hide: true},
                        {field: 'bmmc', title: '部门名称', width: 150},
                        {field: 'username', title: '人员', width: 200},
                        {field: 'phone', title: '电话', width: 200},
                        {
                            field: 'sfld', title: '是否领导', templet: function (d) {
                                return d.sfld === 1 ? '是' : '-';
                            }
                        },
                        {align: 'center', title: '操作', toolbar: '#bmcyTableToolbar', width: 400}
                    ]]
                });
            } else if (obj.event === 'addBmcy') {
                    var addBmcyLayer = layer.open({
                        title: '添加部门成员',
                        type: 2,
                        shade: 0.2,
                        maxmin: true,
                        shadeClose: true,
                        area: ['60%', '60%'],
                        content: '/bm/goAddBmcyPage?id=' + _data.id,
                        end: function () {
                            if ($('#showBmcy').is(':visible')) {
                                bmcyTable.reload();
                            }
                        }
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
