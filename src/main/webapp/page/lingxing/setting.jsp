<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://"
            + request.getServerName() + ":" + request.getServerPort()
            + path + "/";
    request.setAttribute("path", path);
    request.setAttribute("basePath", basePath);
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>所有功能</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0">
    <link rel="stylesheet" href="${basePath}static/layui/css/layui.css" media="all">
    <script src="${basePath}static/layui/layui.js"></script>
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
            </div>
        </div>

        <div class="layui-col-md6">
            <div class="layui-card">
                <div class="layui-card-header">按钮主题</div>
                <div class="layui-card-body">
                    <div class="layui-btn-container">
                        <button class="layui-btn layui-btn-primary">原始按钮</button>
                        <button class="layui-btn">默认按钮</button>
                        <button class="layui-btn layui-btn-normal">百搭按钮</button>
                        <button class="layui-btn layui-btn-warm">暖色按钮</button>
                        <button class="layui-btn layui-btn-danger">警告按钮</button>
                        <button class="layui-btn layui-btn-disabled">禁用按钮</button>
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

        util.on('lay-on', {
            "getIp": function () {
                $.ajax({
                    url: "${basePath}lingxing/getIp",
                    type: "POST",
                    dataType: "json",
                    success: function (data) {
                        if(data.status) {
                            layer.msg(data.msg);
                        }
                    }
                });
            },
            "checkToken":function(){
                $.ajax({
                    url: "${basePath}lingxing/checkToken",
                    type: "POST",
                    dataType: "json",
                    success: function (data) {
                        if(data.status) {
                            layer.msg(data.msg);
                        }
                    }
                });
            },
            "refreshMarketplace":function(){
                $.ajax({
                    url: "${basePath}marketplace/refreshMarketplace",
                    type: "POST",
                    dataType: "json",
                    success: function (data) {
                        if(data.status) {
                            layer.msg(data.msg);
                        }
                    }
                });
            },
            "marketplace_list":function(){
                $.ajax({
                    url: "${basePath}marketplace/marketplace_list",
                    type: "POST",
                    dataType: "json",
                    success: function (data) {
                        if(data.status) {
                            layer.msg(data.msg);
                        }
                    }
                });
            },
            "seller_list":function (){
                $.ajax({
                    url: "${basePath}seller/seller_list",
                    type: "POST",
                    dataType: "json",
                    success: function (data) {
                        if(data.status) {
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
