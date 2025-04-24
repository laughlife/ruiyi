<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>所有功能</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0">
    <meta name="_csrf" content="${_csrf.token}"/>
    <meta name="_csrf_header" content="${_csrf.headerName}"/>

    <link rel="stylesheet" href="/static/layui/css/layui.css" media="all">
    <script src="/static/layui/layui.js"></script>
    <script src="/static/jquery/jquery-3.7.1.min.js"></script>
    <script>
        $.ajaxSetup({
            beforeSend: function (xhr) {
                var header = $('meta[name="_csrf_header"]').attr('content');
                var token  = $('meta[name="_csrf"]').attr('content');
                xhr.setRequestHeader(header, token);
            }
        });
    </script>
</head>
<body>
<div class="layui-fluid">
    <div class="layui-row layui-col-space15">
        <div class="layui-col-md6">
            <div class="layui-card">
                <div class="layui-card-header">接口数据</div>
                <div class="layui-card-body">
                    <div class="layui-btn-container">
                        <button class="layui-btn" lay-on="getIp">获取本地IP</button>
                        <button class="layui-btn" lay-on="checkToken">检查Token信息</button>
                    </div>
                </div>
                <div class="layui-card-header">市场数据</div>
                <div class="layui-card-body">
                    <div class="layui-btn-container">
                        <button class="layui-btn" lay-on="refreshMarketplace">同步市场列表</button>
                        <button class="layui-btn" lay-on="marketplace_list">同步国家下地区列表</button>
                    </div>
                </div>
                <div class="layui-card-header">店铺数据</div>
                <div class="layui-card-body">
                    <div class="layui-btn-container">
                        <button class="layui-btn" lay-on="seller_list">同步亚马逊店铺列表</button>
                    </div>
                </div>
                <div class="layui-card-header">汇率上传，数据来源在：https://www.safe.gov.cn/safe/rmbhlzjj/index.html
                </div>
                <div class="layui-card-body">
                    <div class="layui-upload-drag" style="display: block;" id="currency_upload">
                        <i class="layui-icon layui-icon-upload"></i>
                        <div>点击上传，或将文件拖拽到此处</div>
                        <div id="currency_upload_preview">
                            <hr>
                            <img src="/images/logo.ico" alt="上传成功后渲染"
                                 style="max-width: 50px;max-height: 50px;">
                        </div>
                    </div>
                </div>
            </div>

            <div class="layui-card">
                <div class="layui-card-header">订单数据</div>
                <div class="layui-card-body">
                    <div class="layui-form-item">
                        <div class="layui-inline">
                            <div class="layui-inline" id="laydate-rangeLinked">
                                <div class="layui-input-inline">
                                    <input type="text" autocomplete="off" id="laydate-start" class="layui-input"
                                           placeholder="开始日期">
                                </div>
                                <div class="layui-form-mid">-</div>
                                <div class="layui-input-inline">
                                    <input type="text" autocomplete="off" id="laydate-end" class="layui-input"
                                           placeholder="结束日期">
                                </div>
                            </div>
                        </div>
                        <div class="layui-inline">
                            <button class="layui-btn" lay-on="order_list">同步订单信息</button>
                        </div>
                        <div class="layui-inline">
                            <button class="layui-btn" lay-on="order_details">订单明细</button>
                            <button class="layui-btn" lay-on="profit_day">查询利润报表/日</button>
                        </div>
                    </div>
                    <div class="layui-form-item">
                        <button class="layui-btn layui-btn-normal" lay-on="product_performance">产品表现</button>
                    </div>
                </div>
            </div>
        </div>

        <div class="layui-col-md6">
            <div class="layui-card">
                <div class="layui-card-header">财务、费用</div>
                <div class="layui-card-body">
                    <div class="layui-btn-container">
                        <button class="layui-btn layui-btn-normal">费用类型</button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<script>
    layui.use(function () {
        var layer = layui.layer;
        var util = layui.util;
        var $ = layui.jquery;
        var upload = layui.upload;
        var laydate = layui.laydate;

        //文件上传
        upload.render({
            elem: '#currency_upload',
            accept: 'file',
            exts: 'xlsx|xls',
            url: '/currency/uploadCurrency',
            data: {
                'id': 'xxx'
            },
            done: function (res) {
                layer.msg(res.msg);
                $('#currency_upload_preview').find('img').attr('src', '/images/excel.png');
            }
        });

        //日期范围
        // 日期范围 - 左右面板联动选择模式
        laydate.render({
            elem: '#laydate-rangeLinked',
            range: ['#laydate-start', '#laydate-end'],
            rangeLinked: true
        });

        //点击事件
        util.on('lay-on', {
            //获取本地IP
            "getIp": function () {
                $.ajax({
                    url: "/lingxing/getIp",
                    type: "POST",
                    dataType: "json",
                    success: function (data) {
                        if (data.status) {
                            layer.msg(data.msg);
                        }
                    }
                });
            },
            //检查Token信息
            "checkToken": function () {
                $.ajax({
                    url: "/lingxing/checkToken",
                    type: "POST",
                    dataType: "json",
                    success: function (data) {
                        if (data.status) {
                            layer.msg(data.msg);
                        }
                    }
                });
            },
            //同步市场列表
            "refreshMarketplace": function () {
                $.ajax({
                    url: "/marketplace/refreshMarketplace",
                    type: "POST",
                    dataType: "json",
                    success: function (data) {
                        if (data.status) {
                            layer.msg(data.msg);
                        }
                    }
                });
            },
            //获取国家下地区列表
            "marketplace_list": function () {
                $.ajax({
                    url: "/marketplace/marketplace_list",
                    type: "POST",
                    dataType: "json",
                    success: function (data) {
                        if (data.status) {
                            layer.msg(data.msg);
                        }
                    }
                });
            },
            //获取店铺列表
            "seller_list": function () {
                $.ajax({
                    url: "/seller/seller_list",
                    type: "POST",
                    dataType: "json",
                    success: function (data) {
                        if (data.status) {
                            layer.msg(data.msg);
                        }
                    }
                });
            },
            //获取订单列表
            "order_list": function () {
                var start = $('#laydate-start').val();
                var end = $('#laydate-end').val();
                var loadIndex = layer.msg('数据正在加载中，请勿操作！', {
                    icon: 16,
                    shade: 0.3,
                    time: 0
                });
                $.ajax({
                    url: "/order/queryOrders",
                    type: "POST",
                    data: {
                        'start_date': start,
                        'end_date': end
                    },
                    dataType: "json",
                    success: function (data) {
                        layer.close(loadIndex);
                        if (data.status) {
                            layer.msg(data.msg);
                        }
                    }
                });
            },
            //获取订单明细
            "order_details":function(){
                var start = $('#laydate-start').val();
                var end = $('#laydate-end').val();
                var loadIndex = layer.msg('数据正在加载中，请勿操作！', {
                    icon: 16,
                    shade: 0.3,
                    time: 0
                });
                $.ajax({
                    url: "/order/orderDetails",
                    type: "POST",
                    data: {
                        'start_date': start,
                        'end_date': end
                    },
                    dataType: "json",
                    success: function (data) {
                        layer.close(loadIndex);
                        if (data.status) {
                            layer.msg(data.msg);
                        }
                    }
                });
            },
            //获取财务-利润报表-店铺   按天获取
            "profit_day":function(){
                var start = $('#laydate-start').val();
                var end = $('#laydate-end').val();
                var loadIndex = layer.msg('数据正在加载中，请勿操作！', {
                    icon: 16,
                    shade: 0.3,
                    time: 0
                });
                $.ajax({
                    url: "/order/profit_day",
                    type: "POST",
                    data: {
                        'start_date': start,
                        'end_date': end
                    },
                    dataType: "json",
                    success: function (data) {
                        layer.close(loadIndex);
                        if (data.status) {
                            layer.msg(data.msg);
                        }
                    }
                });

            },
            //获取费用类型
            "product_performance":function(){
                var start = $('#laydate-start').val();
                var end = $('#laydate-end').val();
                var loadIndex = layer.msg('数据正在加载中，请勿操作！', {
                    icon: 16,
                    shade: 0.3,
                    time: 0
                });
                $.ajax({
                    url: "/product/get_product_performance",
                    type: "POST",
                    data: {
                        'start_date': start,
                        'end_date': end
                    },
                    dataType: "json",
                    success: function (data) {
                        layer.close(loadIndex);
                        if (data.status) {
                            layer.msg(data.msg);
                        }
                    }
                });

            }

        });
    });
</script>
</body>
</html>
