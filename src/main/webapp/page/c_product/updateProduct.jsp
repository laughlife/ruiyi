<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>修改商品信息</title>
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
                            <label class="layui-form-label">商品名称</label>
                            <div class="layui-input-block">
                                <input type="text" name="name" lay-verify="required" autocomplete="off"
                                       placeholder="商品名称(必填)" class="layui-input" value="${product.name}">
                                <input type="hidden" name="id" value="${product.id}">
                            </div>
                        </div>
                        <div class="layui-form-item">
                            <label class="layui-form-label">采购链接</label>
                            <div class="layui-input-block">
                                <input type="text" name="link" autocomplete="off" placeholder="(非必填，建议填写)"
                                       class="layui-input" value="${product.link}">
                            </div>
                        </div>
                        <c:if test="${not empty(product.imageUrl)}">
                            <div class="layui-form-item">
                                <label class="layui-form-label">商品图片</label>
                                <div class="layui-input-inline layui-input-wrap">
                                    <img src="${product.imagePath}" alt="商品图片" id="pro_img"
                                         style="max-height: 600px">
                                </div>
                            </div>
                        </c:if>
                        <div class="layui-form-item">
                            <label class="layui-form-label">商品图片</label>
                            <div class="layui-input-inline layui-input-wrap">
                                <button type="button" class="layui-btn layui-btn-normal" id="id_upload_image_choose">
                                    选择图片
                                </button>
                                <input type="hidden" name="imageUrl" id="imageUrl" value="${product.imageUrl}">
                            </div>
                            <div class="layui-form-mid layui-text-em" id="upload_image_result">
                                (选填，方便后期维护知道是哪款商品)点击按钮选择图片
                            </div>
                        </div>
                        <div class="layui-form-item">
                            <label class="layui-form-label">供应商</label>
                            <div class="layui-input-block">
                                <select name="supplierId" id="supplierId" lay-search="" lay-verify="required">
                                    <option value="">--请选择或搜索--</option>
                                    <c:forEach items="${supplierList}" var="supplier">
                                        <option value="${supplier.id}" ${supplier.id eq product.supplierId ? 'selected' : ''}>${supplier.name}</option>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>

                        <div class="layui-form-item">
                            <label class="layui-form-label">采购成本</label>
                            <div class="layui-input-block">
                                <input type="number" name="costPrice" autocomplete="off" placeholder="(必填)"
                                       lay-verify="required" value="${product.costPrice}"
                                       class="layui-input">
                            </div>
                        </div>
                        <div class="layui-form-item">
                            <label class="layui-form-label">其他信息</label>
                            <div class="layui-input-block">
                                <textarea placeholder="(非必填)" name="other" class="layui-textarea">${product.other}</textarea>
                            </div>
                        </div>
                        <div class="layui-form-item" style="text-align: center;">
                            <button type="button"
                                    class="layui-btn layui-btn-normal" lay-submit
                                    lay-filter="create_product_filter"><i class="fa-solid fa-plus"></i>确定修改
                            </button>
                            <button type="button" id="close_win_btn"
                                    class="layui-btn layui-btn-warm"><i class="fa-solid fa-circle-xmark fa-fw"></i>取消
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>

</body>
</html>

<script>
    layui.use(function () {
        var form = layui.form;
        var layer = layui.layer;
        var $ = layui.jquery;
        var upload = layui.upload;

        // 渲染
        upload.render({
            elem: '#id_upload_image_choose',
            url: '/product/uploadImage',
            auto: true,
            accept: 'images',
            acceptMime: 'image/*',
            exts: 'jpg|png|gif|bmp|jpeg',
            done: function (res) {
                layer.msg(res.msg);
                if (res.status) {
                    $("#imageUrl").val(res.src);
                    <c:if test="${not empty(product.imageUrl)}">
                        $("#pro_img").attr("src", res.imagePath);
                    </c:if>
                    $("#upload_image_result").html(res.fileName);
                }
            }
        });

        form.on('submit(create_product_filter)', function (data) {
            var _save_date = data.field;
            $.ajax({
                url: '/product/updateProduct',
                type: 'post',
                data: _save_date,
                dataType: 'json',
                success: function (res) {
                    layer.msg(res.msg);
                    if (res.status) {
                        var iframeIndex = parent.layer.getFrameIndex(window.name);
                        parent.layer.close(iframeIndex);
                    }
                }
            });
            return false;
        });

        $("#close_win_btn").click(function () {
            var iframeIndex = parent.layer.getFrameIndex(window.name);
            parent.layer.close(iframeIndex);
        });
    });
</script>