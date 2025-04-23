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
                <div class="layui-card-header">添加部门</div>
                <div class="layui-card-body">
                    <form class="layui-form layui-form-pane" action="">
                        <div class="layui-form-item">
                            <label class="layui-form-label">上级部门</label>
                            <div class="layui-input-block">
                                <input type="text" readonly value="${empty(sjbm)? "南阳睿翼" : sjbm.name}"
                                       class="layui-input">
                                <input type="hidden" name="parentId" value="${empty(sjbm) ? "-1" : sjbm.id}">
                            </div>
                        </div>
                        <div class="layui-form-item">
                            <label class="layui-form-label">部门名称</label>
                            <div class="layui-input-block">
                                <input type="text" name="name" lay-verify="required"
                                       autocomplete="off" placeholder="请输入部门名称" class="layui-input">
                            </div>
                        </div>
                        <div class="layui-form-item">
                            <label class="layui-form-label">部门编码</label>
                            <c:choose>
                                <c:when test="${empty(sjbm)}">
                                    <div class="layui-input-block">
                                        <input type="text" name="code" lay-verify="required"
                                               autocomplete="off" placeholder="**1" class="layui-input">
                                    </div>
                                </c:when>
                                <c:otherwise>
                                    <div class="layui-input-inline" style="width: 100px;">
                                        <input type="text" lay-verify="required" readonly
                                               autocomplete="off" value="${sjbm.code}" class="layui-input">
                                    </div>
                                    <div class="layui-input-inline">
                                        <input type="text" name="code" lay-verify="required"
                                               autocomplete="off" class="layui-input">
                                    </div>
                                </c:otherwise>
                            </c:choose>
                        </div>
                        <div class="layui-form-item">
                            <label class="layui-form-label">描述信息</label>
                            <div class="layui-input-block">
                                <input type="text" name="description"
                                       autocomplete="off" placeholder="请输入部门描述信息"
                                       class="layui-input">
                            </div>
                        </div>
                        <div class="layui-form-item" style="text-align: center;">
                            <button type="button" id="create_gd_btn"
                                    class="layui-btn layui-btn-normal" lay-submit
                                    lay-filter="xjbm_filter"><i class="fa-solid fa-plus"></i>确定添加
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

        form.on('submit(xjbm_filter)', function (data) {
            var _save_date = data.field;
            $.ajax({
                url: '/department/addBm',
                type: 'post',
                data: _save_date,
                success: function (data) {
                    layer.msg(data.message, {icon: 1, time: 1000});
                    var iframeIndex = parent.layer.getFrameIndex(window.name);
                    parent.layer.close(iframeIndex);
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