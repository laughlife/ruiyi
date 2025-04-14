<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>本地商品</title>
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
                <div class="layui-card-header">商品维护</div>
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
                                    <select name="supplier_name" id="supplier_name" lay-search="">
                                        <option value="">--请选择或搜索--</option>
                                        <c:forEach items="${supplierList}" var="supplier">
                                            <option value="${supplier.id}">${supplier.supplier_name}</option>
                                        </c:forEach>
                                    </select>
                                </div>
                            </div>
                            <div class="layui-inline">
                                <button type="button" id="search_product_btn"
                                        class="layui-btn layui-bg-green"
                                        lay-filter="data-search-btn">
                                    <i class="fa-solid fa-magnifying-glass"></i> 搜索
                                </button>
                            </div>
                            <div class="layui-inline">
                                <button type="button" id="create_product_btn"
                                        class="layui-btn layui-bg-blue"
                                        lay-filter="data-create-btn">
                                    <i class="fa-solid fa-plus"></i> 新建商品
                                </button>
                            </div>
                        </div>
                    </form>
                </div>
                <div class="layui-card-body">
                    <table id="productTable" class="layui-hide" lay-filter="productTableFilter"></table>
                </div>
            </div>

        </div>
    </div>
</div>

<script type="text/html" id="productTableToolbar">
    <div class="layui-btn-container">
<%--        {{#  if(d.status == '已申报'){ }}--%>
        <button class="layui-btn layui-btn-sm layui-bg-blue" lay-event="delete">
            <i class="fa-solid fa-share"></i>修改
        </button>
        <button class="layui-btn layui-btn-sm layui-btn-danger" lay-event="delete">
            <i class="fa-solid fa-trash"></i>删除
        </button>
        <button class="layui-btn layui-btn-sm layui-bg-blue" lay-event="delete">
            <i class="fa-solid fa-eye"></i>变动历史
        </button>
    </div>
</script>
<script type="text/html" id="imageView">
    <img src="{{d.image_path}}" style="max-height:200px; width:auto; display:block; margin:0 auto;">
</script>
<script>
    layui.use(function () {
        var table = layui.table;
        var layer = layui.layer;
        var $ = layui.jquery;

        var productTable = table.render({
            elem: '#productTable',
            url: '/product/queryAllProduct',
            lineStyle: 'height: 120px;',
            cols: [[
                {type: 'numbers', title: '编号', width: 80},
                {
                    field: 'image_path',
                    title: '图片',
                    width: 130,
                    templet: '#imageView'
                },
                {
                    field: 'product_name',
                    title: '商品名称',
                    width: 200,
                    templet: function(d){
                        return d.link ?
                            '<a href="'+ d.link +'" target="_blank" style="color:#1890ff;">'+ d.pro_name +'</a>' :
                            d.pro_name;
                    }
                },
                {field: 'supplier_name', title: '供应商',  width: 280},
                {field: 'cost_price', title: '采购成本', width:120},
                {field: 'create_time', title: '创建时间', width:120},
                {field: 'update_time', title: '更新时间', width:120},
                {field: 'unship_quantity', title: '未发货数', width:120},
                {field: 'unship_price', title: '未发货价值', width:120},
                {field: 'other', title: '其他'},
                {align: 'center', title: '操作', toolbar: '#productTableToolbar', width: 200}
            ]],
            page: true,
            limits: [50, 100, 200],
            limit: 50
        });

        $('#search_product_btn').click(function () {
            var key = $('#key').val();
            var supplier_name = $('#supplier_name').val();
            productTable.reload({
                where: {
                    key: key,
                    supplier_name: supplier_name
                }
            });
        });

        $('#create_product_btn').click(function () {
            layer.open({
                title: '新建商品',
                type: 2,
                shade: 0.5,
                shadeClose: true,
                area: ['60%', '60%'],
                content: '/product/goCreateProduct',
                end: function () {
                    productTable.reload();
                }
            });
        });

        table.on('tool(productTableFilter)', function (obj) {
                var _data = obj.data;
                if (obj.event === 'delete') {
                    $.ajax({
                        url: '/product/deleteProduct',
                        type: 'POST',
                        data: {
                            'id': _data.id
                        },
                        dataType: 'json',
                        success: function (res) {
                            layer.msg(res.msg);
                            productTable.reload();
                        }
                    });
                }else if(obj.event === 'edit'){
                    layer.open({
                        title: '编辑商品',
                        type: 2,
                        shade: 0.5,
                        shadeClose: true,
                        area: ['60%', '60%'],
                        content: '/product/goCreateProduct',
                        end: function () {
                            productTable.reload();
                        }
                    });
                }
            }
        );

    });
</script>
</body>
</html>
