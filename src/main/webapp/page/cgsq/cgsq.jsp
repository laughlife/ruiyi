<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>采购申请</title>
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
                <div class="layui-card-header">采购申请</div>
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
                                <label class="layui-form-label">状态筛选</label>
                                <div class="layui-input-inline" style="width: 180px;">
                                    <select name="status" id="status" lay-verify="required"
                                                    lay-search="">
                                        <option value="">请选择状态</option>
                                        <option value="已申报">已申报</option>
                                        <option value="已确认">已确认</option>
                                        <option value="已采购">已采购</option>
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
                    <table id="declarationTable" class="layui-hide" lay-filter="declarationTableFilter"></table>
                </div>
            </div>
        </div>
    </div>
</div>
<script type="text/html" id="declarationTableToolbar">
    <div class="layui-btn-container">
        <button class="layui-btn layui-btn-sm layui-bg-blue" lay-event="chakan">
            <i class="fa-solid fa-eye"></i>查看
        </button>
        {{#  if(d.status == '已申报'){ }}
            <button class="layui-btn layui-btn-sm layui-bg-red" lay-event="queren">
                <i class="fa-solid fa-check"></i>确认
            </button>
        {{#  } else if(d.status == '已确认') { }}
            <button class="layui-btn layui-btn-sm layui-bg-blue" lay-event="chuli">
                <i class="fa-solid fa-angle-double-down"></i>处理
            </button>
        {{#  }else if(d.status == '已采购'){ }}
            <button class="layui-btn layui-btn-sm layui-bg-red" lay-event="confirm_arrival">
                <i class="fa-solid fa-check"></i>确认到货
            </button>
        {{#  } }}
    </div>
</script>
<script type="text/html" id="imageView">
    {{#  if(d.image_path && d.image_path != ''){ }}
        <img src="{{d.image_path}}" style="max-height:80px; width:auto; display:block; margin:0 auto;">
    {{#  }else{ }}
        <div style="text-align: center;">-</div>
    {{#  } }}
</script>
<script>
    layui.use(function () {
        var table = layui.table;
        var layer = layui.layer;
        var $ = layui.jquery;


        var declarationTable = table.render({
            elem: '#declarationTable',
            url: '/declaration/queryAllDeclaration',
            lineStyle: 'height: 100px;',
            cols: [[
                {type: 'numbers', title: '编号', width: 80},
                {
                    field: 'image_path',
                    title: '图片',
                    width: 130,
                    templet: '#imageView'
                },
                {
                    field: 'pro_name',
                    title: '商品名称',
                    width: 200,
                    templet: function(d){
                        return d.link ?
                            '<a href="'+ d.link +'" target="_blank" style="color:#1890ff;">'+ d.pro_name +'</a>' :
                            d.pro_name;
                    }
                },
                {field: 'asin', title: 'asin',  width: 280},
                {field: 'shc', title: '收货仓',  width: 280},
                {field: 'purchase_packages', title: '需求件数', width:120},
                {field: 'per_package_quantity', title: '单件数量', width:120},
                {field: 'total_quantity', title: '总需求量', width:120},
                {field: 'declare_time', title: '申报时间', width:180},
                {field: 'other', title: '其他备注'},
                {
                    field: 'status',
                    title: '状态',
                    width:120,
                    templet: function(d){
                        var status = {
                            '已申报': '<span class="layui-badge layui-bg-green">已申报</span>',
                            '已确认': '<span class="layui-badge layui-bg-blue">已确认</span>',
                            '已采购': '<span class="layui-badge layui-bg-blue">已采购</span>'
                        };
                        return status[d.status];
                    }
                },
                {align: 'center', title: '操作', toolbar: '#declarationTableToolbar', width: 350}
            ]],
            page: true,
            limits: [50, 100, 200],
            limit: 50
        });

        $('#search_declaration_btn').click(function () {
            var key = $('#key').val();
            var date_start = $('#laydate-start').val();
            var date_end = $('#laydate-end').val();
            declarationTable.reload({
                where: {
                    key: key,
                    date_start: date_start,
                    date_end: date_end
                }
            });
        });

        table.on('tool(declarationTableFilter)', function (obj) {
                var _data = obj.data;
                if (obj.event === 'queren') {
                    $.ajax({
                        url: '/declaration/queren',
                        type: 'POST',
                        data: {
                            'id': _data.id
                        },
                        dataType: 'json',
                        success: function (res) {
                            layer.msg(res.msg);
                            declarationTable.reload();
                        }
                    });
                }else if(obj.event === 'chakan'){
                    layer.open({
                        title: '查看采购申报',
                        type: 2,
                        shade: 0.5,
                        shadeClose: true,
                        area: ['60%', '90%'],
                        content: '/declaration/chakan?id='+_data.id
                    });
                }else if(obj.event === 'chuli'){
                    layer.open({
                        title: '处理采购申请',
                        type: 2,
                        shade: 0.5,
                        shadeClose: true,
                        area: ['60%', '90%'],
                        content: '/declaration/chuli?id='+_data.id,
                        end: function () {
                            declarationTable.reload();
                        }
                    });
                }
            }
        );
    });
</script>
</body>
</html>
