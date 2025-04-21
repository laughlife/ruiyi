<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>编辑采购申报</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">

    <link rel="stylesheet" href="/static/layui/css/layui.css" media="all">
    <link rel="stylesheet" href="/static/fontawesome6/css/all.min.css" media="all">
    <script src="/static/layui/layui.js"></script>
</head>
<body>

<div class="layui-fluid">
    <div class="layui-row layui-col-space15">
        <div class="layui-col-md12">
            <div class="layui-card">
                <div class="layui-card-body">
                    <form class="layui-form layui-form-pane" action="">
                        <div class="layui-form-item">
                            <div class="layui-inline">
                                <label class="layui-form-label">商品名称</label>
                                <div class="layui-input-block">
                                    <input type="text" id="key" name="key" placeholder="请输入搜索提示信息"
                                           class="layui-input">
                                </div>
                            </div>
                            <div class="layui-inline" id="laydate-rangeLinked">
                                <label class="layui-form-label">供应商</label>
                                <div class="layui-input-block">
                                    <select name="supplier_id" id="supplier_id" lay-search="">
                                        <option value="">--请选择或搜索--</option>
                                        <c:forEach items="${supplierList}" var="supplier">
                                            <option value="${supplier.id}">${supplier.name}</option>
                                        </c:forEach>
                                    </select>
                                </div>
                            </div>
                            <div class="layui-inline">
                                <button type="button" id="search_declaration_btn"
                                        class="layui-btn layui-bg-green"
                                        lay-filter="data-search-btn">
                                    <i class="fa-solid fa-magnifying-glass"></i> 搜索
                                </button>
                            </div>
                        </div>
                    </form>
                </div>

                <div class="layui-card-body">
                    <table class="layui-hide" id="productTable" lay-filter="productTableFilter"></table>
                </div>
            </div>
        </div>
    </div>
</div>

</body>
</html>
<script type="text/html" id="productTableToolbar">
    <div class="layui-btn-container">
        <button class="layui-btn layui-btn-sm layui-bg-blue" lay-event="choose">
            <i class="fa-solid fa-check"></i>选择
        </button>
    </div>
</script>
<script type="text/html" id="imageView">
    {{#  if(d.image_url && d.image_url != ''){ }}
        <img src="{{d.image_url}}" style="max-height:80px; width:auto; display:block; margin:0 auto;">
    {{#  }else{ }}
        <div style="text-align: center;">-</div>
    {{#  } }}
</script>
<script>
    layui.use(function () {
        var table = layui.table;
        var layer = layui.layer;
        var $ = layui.jquery;

        var productTable = table.render({
            elem: '#productTable',
            url: '/product/queryAllProduct',
            lineStyle: 'height: 100px;',
            cols: [[
                {type: 'numbers', title: '编号', width: 60},
                {
                    field: 'image_path',
                    title: '图片',
                    width: 130,
                    align: 'center',
                    templet: '#imageView'
                },
                {
                    field: 'name',
                    title: '商品名称',
                    templet: function (d) {
                        return d.link ?
                            '<a href="' + d.link + '" target="_blank" style="color:#1890ff;">' + d.name + '</a>' :
                            d.name;
                    }
                },
                {field: 'supplier_name', title: '供应商'},
                {field: 'cost_price', title: '采购成本', width: 120},
                {field: 'unship_quantity', title: '未发货数', width: 120},
                {field: 'unship_price', title: '未发货价值', width: 120},
                {align: 'center', title: '操作', toolbar: '#productTableToolbar', width: 100}
            ]],
            page: true,
            limits: [50, 100, 200],
            limit: 50
        });

        $("#search_declaration_btn").click(function () {
            productTable.reload({
                where: {
                    key: $("#key").val(),
                    supplier_id: $("#supplier_id").val()
                }
            });
        });

        $("#close_win_btn").click(function () {
            var iframeIndex = parent.layer.getFrameIndex(window.name);
            parent.layer.close(iframeIndex);
        });

        table.on('tool(productTableFilter)', function (obj) {
            var _data = obj.data;
            if (obj.event === 'choose') {
                window.parent.handleProductChoice(_data);
                var iframeIndex = parent.layer.getFrameIndex(window.name);
                parent.layer.close(iframeIndex);
            }
        });
    });

</script>