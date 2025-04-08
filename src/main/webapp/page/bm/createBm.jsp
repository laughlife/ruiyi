<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://"
            + request.getServerName() + ":" + request.getServerPort()
            + path + "/";
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>添加部门信息</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <link rel="stylesheet" href="<%=basePath%>lib/layui-v2.6.3/css/layui.css" media="all">
    <link rel="stylesheet" href="<%=basePath%>/css/public.css" media="all">
    <link rel="stylesheet" href="<%=basePath%>lib/font-awesome-6.6/css/all.min.css" media="all">
    <script src="<%=basePath%>/lib/layui-v2.6.3/layui.js" charset="utf-8"></script>
    <script src="<%=basePath%>/js/lay-config.js?v=2.0.4" charset="utf-8"></script>
    <script src="<%=basePath%>/lib/jquery-3.4.1/jquery-3.4.1.min.js" charset="utf-8"></script>
    <%@ include file="../index_include.html" %>
</head>
<body>
<div class="layuimini-container">
    <div class="layuimini-main">
        <div class="layui-row layui-col-space15">
            <div class="layui-col-md12">
                <fieldset class="table-search-fieldset">
                    <legend>添加部门信息</legend>
                    <div style="margin: 10px 10px 10px 10px;min-height: 800px;">
                        <form class="layui-form layui-form-pane" action="">
                            <div class="layui-form-item">
                                <div class="layui-inline">
                                    <div class="layui-input-inline" id="chooseDateDiv" style="width:480px">
                                        <label class="layui-form-label">部门名称</label>
                                        <div class="layui-input-block">
                                            <input type="text" id="bm_input" name="name" lay-verify="required"
                                                   autocomplete="off" placeholder="请输入部门名称"
                                                   class="layui-input">
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="layui-form-item">
                                <div class="layui-inline" style="width:90%;">
                                    <label class="layui-form-label">描述信息</label>
                                    <div class="layui-input-inline" style="width:60%;">
                                        <input type="text" id="ms_input" name="ms" lay-verify="required"
                                               autocomplete="off" placeholder="请输入部门描述信息"
                                               class="layui-input">
                                    </div>
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
                </fieldset>
            </div>
        </div>
    </div>
</div>
</body>
</html>

<script>
    layui.use(['form', 'miniTab', 'layer'], function () {
        var form = layui.form;
        var layer = layui.layer;

        form.on('submit(xjbm_filter)', function (data) {
            var _save_date = data.field;
            $.ajax({
                url: '<%=basePath%>bm/addBm',
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