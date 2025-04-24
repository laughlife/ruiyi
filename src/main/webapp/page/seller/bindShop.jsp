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
    <script src="/static/jquery/jquery-3.7.1.min.js"></script>
</head>
<body>

<div class="layui-fluid">
    <div class="layui-row layui-col-space15">
        <div class="layui-col-md12">
            <div class="layui-card" id="showShop">
                <div class="layui-card-header">${queryUser.name} —— 绑定店铺</div>
                <div class="layui-card-body">
                    <table id="shopTable" class="layui-table" lay-filter="shopTableFilter"></table>
                </div>
            </div>
        </div>
    </div>
</div>
<script type="text/html" id="shopToolbar">
    <div class="layui-form-item">
        <div class="layui-inline">
            <label class="layui-form-label">店铺：</label>
            <div class="layui-input-block">
                <input type="text" id="key" name="key" placeholder="请输入要搜索的店铺" class="layui-input">
            </div>
        </div>
        <div class="layui-inline">
            <button type="button" id="search_shop_btn"
                    class="layui-btn layui-btn-sm layui-bg-green"
                    lay-event="search">
                <i class="fa-solid fa-magnifying-glass"></i> 搜索
            </button>
            <button class="layui-btn layui-btn-sm layui-bg-blue" lay-event="getCheckData">
                <i class="fa-solid fa-link"></i>同步绑定状态
            </button>
        </div>
    </div>
</script>
<script>
    layui.use(function () {
        var table = layui.table;
        var layer = layui.layer;
        var $ = layui.jquery;

        table.render({
            elem: '#shopTable',
            id: 'shopTableId',
            url: '/seller/queryShopToBind',
            where: {'userId': '${queryUser.id}'},
            toolbar: '#shopToolbar',
            page: true,
            limits: [20, 50, 100],
            limit: 20,
            cols: [[
                {type: 'checkbox', fixed: 'left'},
                {title: '店铺', width: 150, field: 'name'},
                {title: '国家', width: 150, field: 'country'},
                {title: '绑定关系', field: 'bind'},
                {
                    field: 'status',
                    title: '授权状态',
                    templet: function(d) {
                        return d.status === '正常'
                            ? '<span style="color:green">'+d.status+'</span>'
                            : '<span style="color:red">'+d.status+'</span>';
                    }
                }
            ]]
        });


        table.on('toolbar(shopTableFilter)', function(obj){
            var checkStatus = table.checkStatus('shopTableId');
            var data = checkStatus.data;
            switch(obj.event){
                case 'getCheckData':
                    // 获取当前页所有数据
                    var allData = table.cache['shopTableId'];
                    // 提取已选中的ID
                    var checkedIds = data.map(item => item.id);
                    // 过滤未选中的ID
                    var notCheckedIds = allData.filter(item =>
                        !checkedIds.includes(item.id)
                    ).map(item => item.id);

                    var requestData = {
                        userId:"${queryUser.id}",
                        sellers: JSON.stringify(checkedIds),
                        notCheck: JSON.stringify(notCheckedIds)
                    };
                    $.ajax({
                        url: '/seller/bindSeller',
                        type: 'POST',
                        data: requestData,
                        dataType: 'json',
                        success: function (res) {
                            if(res.status){
                                layer.msg(res.msg, {icon: 1, time: 1500});
                            }else{
                                layer.msg(res.msg, {icon: 2, time: 1500});
                            }
                        }
                    });
                    break;
                case 'search':
                    var key = $("#key").val();
                    var searchParams = {
                        key: key,
                        'userId': '${queryUser.id}'
                    };
                    shopTable.reload('shopTableId', {
                        where: searchParams,
                        page: {
                            curr: 1
                        }
                    });
                    break;
            }
        });

    });
</script>
</body>
</html>
