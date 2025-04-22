<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>发货申报</title>
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
                <div class="layui-card-header">我的申报</div>
                <div class="layui-card-body">
                    <form class="layui-form layui-form-pane" action="">
                        <div class="layui-form-item">
                            <div class="layui-inline">
                                <label class="layui-form-label">申报商品</label>
                                <div class="layui-input-block">
                                    <input type="text" id="key" name="key" placeholder="请输入搜索提示信息"
                                           class="layui-input">
                                </div>
                            </div>
                            <div class="layui-inline">
                                <label class="layui-form-label">状态筛选</label>
                                <div class="layui-input-inline" style="width: 180px;">
                                    <select name="status" id="status" lay-verify="required"
                                            lay-search="">
                                        <option value="">请选择状态</option>
                                        <option value="已申报">已申报</option>
                                        <option value="已确认">已确认</option>
                                        <option value="已采购">已采购</option>
                                        <option value="已到货">已到货</option>
                                        <option value="已发出">已发出</option>
                                        <option value="已完成">已完成</option>
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
                            <div class="layui-inline">
                                <button type="button" id="create_declaration_btn"
                                        class="layui-btn layui-bg-blue"
                                        lay-filter="data-create-btn">
                                    <i class="fa-solid fa-plus"></i> 新建采购申报
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
        <button class="layui-btn layui-btn-sm layui-bg-blue" lay-event="show_log">
            <i class="fa-solid fa-eye"></i> 进度
        </button>
        {{# if(d.status == '已申报'){ }}
            <button class="layui-btn layui-btn-sm layui-bg-blue" lay-event="update">
                <i class="fa-solid fa-rotate"></i> 修改
            </button>
            <button class="layui-btn layui-btn-sm layui-btn-danger" lay-event="delete">
                <i class="fa-solid fa-trash"></i> 删除
            </button>
        {{# } else if(d.status == '已采购' || d.status == '已到货'){ }}
            <button class="layui-btn layui-btn-sm layui-bg-red" lay-event="upload">
                <i class="fa-solid fa-file-upload"></i> 发票、标签
            </button>
        {{# } else if(d.status == '已发出'){ }}
            <button class="layui-btn layui-btn-sm layui-bg-red" lay-event="fba_receive">
                <i class="fa-solid fa-truck-plane"></i> FBA追踪
            </button>
        {{# } }}
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
                    templet: function (d) {
                        return d.link ?
                            '<a href="' + d.link + '" target="_blank" style="color:#1890ff;">' + d.pro_name + '</a>' :
                            d.pro_name;
                    }
                },
                {field: 'asin', title: 'asin'},
                {field: 'shc', title: '收货仓'},
                {field: 'purchase_packages', title: '需求件数'},
                {field: 'per_package_quantity', title: '单件数量'},
                {field: 'total_quantity', title: '采需求量'},
                {field: 'plan_total_quantity', title: '采购+库存'},
                {field: 'declare_time', title: '申报时间'},
                {field: 'other', title: '其他备注'},
                {field: 'status', title: '状态',templet: function(d){
                        var status = {
                            '已申报': '<span class="layui-badge layui-bg-green">已申报</span>',
                            '已确认': '<span class="layui-badge layui-bg-blue">已确认</span>',
                            '已采购': '<span class="layui-badge layui-bg-blue">已采购</span>',
                            '已到货': '<span class="layui-badge layui-bg-blue">已到货</span>',
                            '已发出': '<span class="layui-badge layui-bg-blue">已发出</span>',
                            '已完成': '<span class="layui-badge layui-bg-gray">已完成</span>',

                        };
                        return status[d.status];
                    }
                },
                {field: 'fapiao', title: '发票',templet: function(d) {
                        return d.fapiao ? '<span style="color:green">√</span>' : '<span style="color:red">×</span>';
                    }},
                {field: 'tips', title: '标签',templet: function(d) {
                        return d.tips ? '<span style="color:green">√</span>' : '<span style="color:red">×</span>';
                    }},
                {align: 'center', width:400,title: '操作', toolbar: '#declarationTableToolbar'}
            ]],
            page: true,
            limits: [50, 100, 200],
            limit: 50
        });

        $('#search_declaration_btn').click(function () {
            var key = $('#key').val();
            var status = $('#status').val();
            declarationTable.reload({
                where: {
                    key: key,
                    status: status
                }
            });
        });

        $('#create_declaration_btn').click(function () {
            layer.open({
                title: '新建采购申报',
                type: 2,
                shade: 0.5,
                shadeClose: true,
                area: ['60%', '80%'],
                content: '/declaration/goCreateDeclaration',
                end: function () {
                    declarationTable.reload();
                }
            });
        });

        table.on('tool(declarationTableFilter)', function (obj) {
                var _data = obj.data;
                if (obj.event === 'delete') {
                    $.ajax({
                        url: '/declaration/deleteDeclaration',
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
                }  else if (obj.event === 'update') {
                    layer.open({
                        title: '修改采购申报',
                        type: 2,
                        shade: 0.5,
                        shadeClose: true,
                        area: ['60%', '80%'],
                        content: '/declaration/goEditDeclaration?id=' + _data.id,
                        end: function () {
                            declarationTable.reload();
                        }
                    });
                } else if (obj.event === 'upload') {
                    layer.open({
                        title: '上传发票、标签',
                        type: 2,
                        shade: 0.5,
                        shadeClose: true,
                        area: ['60%', '90%'],
                        content: '/declaration/goUploadDeclaration?id=' + _data.id,
                        end: function () {
                            declarationTable.reload();
                        }
                    });
                } else if (obj.event === 'show_log') {
                    layer.open({
                        title: '查看流程',
                        type: 2,
                        shade: 0.5,
                        shadeClose: true,
                        area: ['60%', '90%'],
                        content: '/declaration/queryDeclarationLog?id=' + _data.id
                    });
                } else if (obj.event === 'fba_receive') {
                    layer.open({
                        title: 'FBA货物追踪',
                        type: 2,
                        shade: 0.5,
                        shadeClose: true,
                        area: ['60%', '90%'],
                        content: '/declaration/goFbaReceivePage?id=' + _data.id
                    });
                }
            }
        );

    });

    function downloadFile(url, name) {
        const ext = url.split('.').pop().split('?')[0].split('#')[0];
        const filename = `\${name}.\${ext}`;
        fetch(url)
            .then(response => {
                if (!response.ok) {
                    throw new Error('文件下载失败');
                }
                return response.blob();
            })
            .then(blob => {
                const blobUrl = URL.createObjectURL(blob);
                const a = document.createElement('a');
                a.href = blobUrl;
                a.download = filename;
                document.body.appendChild(a);
                a.click();
                document.body.removeChild(a);
                URL.revokeObjectURL(blobUrl); // 清理对象URL
            })
            .catch(err => {
                console.error('下载出错:', err);
                alert('下载失败，请稍后重试');
            });
    }

</script>
</body>
</html>
