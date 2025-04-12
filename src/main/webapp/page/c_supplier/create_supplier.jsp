<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>添加部门信息</title>
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
                            <label class="layui-form-label">供应商</label>
                            <div class="layui-input-block">
                                <input type="text" name="name" lay-verify="required" autocomplete="off"
                                       placeholder="供应商名称(必填)" class="layui-input">
                            </div>
                        </div>
                        <div class="layui-form-item">
                            <label class="layui-form-label">采购商负责人</label>
                            <div class="layui-input-block">
                                <input type="text" name="fzr" autocomplete="off" placeholder="(非必填)"
                                       class="layui-input">
                            </div>
                        </div>
                        <div class="layui-form-item">
                            <label class="layui-form-label">联系方式</label>
                            <div class="layui-input-block">
                                <input type="text" name="phone" autocomplete="off" placeholder="(非必填)"
                                       class="layui-input">
                            </div>
                        </div>
                        <div class="layui-form-item">
                            <label class="layui-form-label">地址</label>
                            <div class="layui-input-block">
                                <input type="text" name="address" autocomplete="off" placeholder="(非必填)"
                                       class="layui-input">
                            </div>
                        </div>
                        <div class="layui-form-item">
                            <label class="layui-form-label">其他信息</label>
                            <div class="layui-input-block">
                                <input type="text" name="otherInfo"
                                       autocomplete="off" placeholder="请输入其他信息(非必填)"
                                       class="layui-input">
                            </div>
                        </div>
                        <div class="layui-form-item" style="text-align: center;">
                            <button type="button" id="create_gd_btn"
                                    class="layui-btn layui-btn-normal" lay-submit
                                    lay-filter="create_supplier_filter"><i class="fa-solid fa-plus"></i>确定添加
                            </button>
                            <button type="reset" id="reset_form_btn"
                                    class="layui-btn layui-btn-normal"><i
                                    class="fa-solid fa-rotate-right fa-fw"></i>重置
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

        form.on('submit(create_supplier_filter)', function (data) {
            var _save_date = data.field;
            $.ajax({
                url: '/supplier/createSupplier',
                type: 'post',
                data: _save_date,
                dataType: 'json',
                success: function (res) {
                    layer.msg(res.msg);
                    if(res.status){
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