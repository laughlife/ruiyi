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
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0">
    <link rel="stylesheet" href="${basePath}static/layui/css/layui.css" media="all">
    <script src="${basePath}static/layui/layui.js"></script>
    <style>
        .image-container {
            display: flex;
            flex-wrap: wrap;
            gap: 20px; /* 增大图片之间的间距 */
            padding: 20px;
            justify-content: space-between;
        }

        .image-item {
            width: calc(10% - 20px); /* 一行 5 张，考虑间距 */
            height: auto;
            max-height: 480px;
            position: relative;
            border-radius: 5px;
            overflow: hidden;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
            transition: transform 0.3s ease-in-out;
        }

        .image-item img {
            width: 100%;
            height: auto;
            object-fit: cover;
        }

        .image-item:hover {
            transform: scale(1.05);
        }

        /* 按钮组 */
        .image-actions {
            position: absolute;
            bottom: 10px;
            left: 50%;
            transform: translateX(-50%);
            background: rgba(0, 0, 0, 0.7);
            padding: 8px;
            border-radius: 5px;
            display: flex;
            gap: 5px;
            opacity: 0;
            transition: opacity 0.3s ease-in-out;
        }

        /* 鼠标悬停时显示按钮 */
        .image-item:hover .image-actions {
            opacity: 1;
        }
    </style>
</head>
<body>
<div class="layui-fluid">
    <div class="layui-row layui-col-space15">
        <div class="layui-card">
            <div class="layui-card-body">

                <div class="layui-form-item">
                    <label class="layui-form-label">商品名称</label>
                    <div class="layui-input-block">
                        <input name="name" id="imageName" type="text" placeholder="请输入商品名称" autocomplete="off" class="layui-input">
                    </div>
                </div>
                <div class="layui-form-item">
                    <label class="layui-form-label">输入数量</label>
                    <div class="layui-input-block">
                        <input name="number" id="imageNumber" type="number" lay-affix="number" placeholder="请输入数量" autocomplete="off" class="layui-input">
                    </div>
                </div>
                <div class="layui-form-item">
                    <label class="layui-form-label">上传图片</label>
                    <div class="layui-input-block">
                        <div class="layui-upload-drag" style="display: block;" id="image_upload">
                            <i class="layui-icon layui-icon-upload"></i>
                            <div>点击上传，或将文件拖拽到此处</div>
                            <div id="currency_upload_preview">
                                <hr>
                            </div>
                        </div>
                    </div>
                </div>
                <div id="show_image_view" class="image-container">

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

        //文件上传
        upload.render({
            elem: '#image_upload',
            accept: 'file',
            exts: 'jpg|png|gif',
            size: 1024 * 5,
            url: '${basePath}image/uploadImageToCreateOrderImage',
            data: {
                'name': function (){
                    return $('#imageName').val();
                },
                'number': function (){
                    return $('#imageNumber').val();
                }
            },
            done: function (data) {
                if(data){
                    $("#show_image_view").html("");
                    layui.each(data.data, function (index, item) {
                        var _html =`<div class="image-item">
                                        <img src="\${item}" alt="图片" layer-src="\${item}">
                                        <div class="image-actions">
                                            <a class="layui-btn layui-btn-sm layui-btn-normal" href='\${item}' target="_blank" download="">下载</a>
                                        </div>
                                    </div>`;
                        $("#show_image_view").append(_html);
                    });
                }
            }
        });

        //点击事件
        util.on('lay-on', {
            //获取本地IP
            "getIp": function () {
                $.ajax({
                    url: "${basePath}lingxing/getIp",
                    type: "POST",
                    dataType: "json",
                    success: function (data) {
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
