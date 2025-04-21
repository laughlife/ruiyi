<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>编辑采购申报</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">

    <link rel="stylesheet" href="/static/layui/css/layui.css" media="all">
    <link rel="stylesheet" href="/static/fontawesome6/css/all.min.css" media="all">
    <script src="/static/layui/layui.js"></script>
    <script src="/static/jquery/jquery-3.7.1.min.js"></script>
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
                                <div class="layui-colla-content layui-show" style="font-weight: bold;">
                                    <c:choose>
                                        <c:when test="${!empty(dec.link)}">
                                            <a href="${dec.link}" target="_blank" style="color:#1890ff;">${dec.proName}</a>
                                        </c:when>
                                        <c:otherwise>
                                            ${dec.proName}
                                        </c:otherwise>
                                    </c:choose>
                                    <input type="hidden" name="id" value="${dec.id}">
                                </div>
                            </div>
                        </div>
                        <div class="layui-form-item">
                            <label class="layui-form-label">asin</label>
                            <div class="layui-input-block">
                                <div class="layui-colla-content layui-show" style="color:#333;font-weight: bold;">
                                    ${dec.asin}
                                </div>
                            </div>
                        </div>
                        <div class="layui-form-item">
                            <label class="layui-form-label">店铺</label>
                            <div class="layui-input-block">
                                <div class="layui-colla-content layui-show" style="color:#333;font-weight: bold;">
                                    ${dec.sellerName}
                                </div>
                            </div>
                        </div>

                        <div class="layui-form-item">
                            <label class="layui-form-label">收货仓</label>
                            <div class="layui-input-block">
                                <input type="text" name="shc" id="shc" autocomplete="off" placeholder="(必填)"
                                       class="layui-input" value="${dec.shc}" lay-verify="required">
                            </div>
                        </div>

                        <c:if test="${!empty(dec.imagePath)}">
                            <div class="layui-form-item">
                                <label class="layui-form-label">商品图片</label>
                                <div class="layui-input-inline layui-input-wrap">
                                    <img src="${imageServiceUrl}${dec.imagePath}" style="width: 100px;height: 100px">
                                </div>
                            </div>
                        </c:if>
                        <div class="layui-form-item">
                            <label class="layui-form-label">采购数量</label>
                            <div class="layui-input-block">
                                <div class="layui-colla-content layui-show" style="color:#333;font-weight: bold;">
                                    ${dec.purchasePackages}(件数) × ${dec.perPackageQuantity}(单件数量) = ${dec.totalQuantity} (总量)
                                    <input type="hidden" id="totalQuantity" name="totalQuantity" value="${dec.totalQuantity}">
                                </div>
                            </div>
                        </div>
                        <c:if test="${!empty(dec.other)}">
                            <div class="layui-form-item">
                                <label class="layui-form-label">其他信息</label>
                                <div class="layui-input-block">
                                    <div class="layui-colla-content layui-show">
                                            ${dec.other}
                                    </div>
                                </div>
                            </div>
                        </c:if>
                        <div class="layui-form-item">
                            <div class="layui-inline">
                                <label class="layui-form-label">本地商品</label>
                                <div class="layui-input-block">
                                    <input type="hidden" id="proId" name="proId" class="layui-input">
                                    <input type="text" id="proName" name="proName" readonly placeholder="本地商品信息" class="layui-input">
                                </div>
                            </div>
                            <div class="layui-inline">
                                <button type="button"  class="layui-btn layui-bg-green" id="chooseProduct">
                                    <i class="fa-solid fa-magnifying-glass"></i> 选择
                                </button>
                            </div>
                        </div>
                        <div class="layui-form-item">
                            <label class="layui-form-label">未发数</label>
                            <div class="layui-input-block">
                                <div class="layui-colla-content layui-show" id="unship_count" style="color:#333;font-weight: bold;">
                                    -
                                </div>
                            </div>
                        </div>
                        <div class="layui-form-item">
                            <label class="layui-form-label">未发用量</label>
                            <div class="layui-input-block">
                                <input type="hidden" id="wfyl" name="kcyl" class="layui-input">
                                <div class="layui-colla-content layui-show" id="wfyl_div" style="color:#333;font-weight: bold;">
                                    -
                                </div>
                            </div>
                        </div>
                        <div class="layui-form-item">
                            <label class="layui-form-label">需采购</label>
                            <div class="layui-input-block">
                                <div class="layui-colla-content layui-show" id="need_buy_count" style="color:#333;font-weight: bold;">
                                    -
                                </div>
                            </div>
                        </div>
                        <div class="layui-form-item">
                            <label class="layui-form-label">采购链接</label>
                            <div class="layui-input-block">
                                <div class="layui-colla-content layui-show" id="buy_link" style="color:#333;font-weight: bold;">
                                    -
                                </div>
                            </div>
                        </div>
                        <div class="layui-form-item">
                            <label class="layui-form-label">采购单价</label>
                            <div class="layui-input-block">
                                <input type="text" id="cost_price" name="costPrice" placeholder="采购单价" lay-verify="required" class="layui-input">
                            </div>
                        </div>
                        <div class="layui-form-item">
                            <label class="layui-form-label">本次采购</label>
                            <div class="layui-input-block">
                                <input type="number" id="buy_quantity" name="buyQuantity" placeholder="本次采购数量"
                                       lay-verify="required" class="layui-input">
                            </div>
                        </div>
                        <div class="layui-form-item">
                            <label class="layui-form-label">预估发货</label>
                            <div class="layui-input-block">
                                <input type="text" id="planShipTime" name="planShipTime" placeholder="预估发货时间，点击选择"
                                       class="layui-input" readonly>
                            </div>
                        </div>
                        <div class="layui-form-item" style="text-align: center;">
                            <button type="button" class="layui-btn layui-btn-normal" lay-submit
                                    lay-filter="update_declaration_filter"><i class="fa-solid fa-rotate"></i>确定
                            </button>
                            <button type="button" id="close_win_btn" class="layui-btn layui-btn-warm">
                                <i class="fa-solid fa-circle-xmark fa-fw"></i>关闭
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
        var $ = layui.jquery;
        var form = layui.form;
        var layer = layui.layer;
        var laydate = layui.laydate;
        // 渲染
        laydate.render({
            elem: '#planShipTime'
        });

        $("#chooseProduct").click(function () {
            layer.open({
                type: 2,
                title: '选择商品',
                shade: 0.2,
                area: ['90%', '90%'],
                shadeClose: true,
                content: '/product/goChooseProduct'
            });
        });

        form.on('submit(update_declaration_filter)', function (data) {
            //采购
            $.ajax({
                url: "/declaration/buy",
                type: "post",
                data: data.field,
                dataType: "json",
                success: function (res) {
                    if(res.status){
                        layer.msg(res.msg, {icon: 1, time: 1000}, function () {
                            var iframeIndex = parent.layer.getFrameIndex(window.name);
                            parent.layer.close(iframeIndex);
                        });
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

    function handleProductChoice(d) {
        var totalQuantity = $("#totalQuantity").val();
        $("#proId").val(d.id);
        //本地商品名称
        $("#proName").val(d.name);
        //商品采购链接
        var link = d.link ? '<a href="' + d.link + '" target="_blank" style="color:#1890ff;">' + d.link + '</a>' : "无采购链接";
        $("#buy_link").html(link);
        //需要购买数量
        if (d.unship_quantity > 0 && d.unship_quantity >= totalQuantity) {
            $("#need_buy_count").html("剩余未发货数量大于需采购数量，无需采购");
            $("#wfyl").val(d.totalQuantity);
            $("#wfyl_div").html(d.totalQuantity);
        }else if(d.unship_quantity > 0 && d.unship_quantity < totalQuantity){
            $("#need_buy_count").html(totalQuantity +"(采购总量) - " + d.unship_quantity + "(未发货数量) = " + (totalQuantity - d.unship_quantity) + "个");
            $("#wfyl").val(d.unship_quantity);
            $("#wfyl_div").html(d.unship_quantity);
        }else if(d.unship_quantity == 0){
            $("#need_buy_count").html(totalQuantity);
            $("#wfyl").val(0);
            $("#wfyl_div").html(0);
        }
        //本地未发货数量
        $("#unship_count").html(d.unship_quantity);
        //采购单价
        $("#cost_price").val(d.cost_price);
    }
</script>