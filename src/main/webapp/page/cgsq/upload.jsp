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
    <meta name="_csrf" content="${_csrf.token}"/>
    <meta name="_csrf_header" content="${_csrf.headerName}"/>

    <link rel="stylesheet" href="/static/layui/css/layui.css" media="all">
    <link rel="stylesheet" href="/static/fontawesome6/css/all.min.css" media="all">
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
    <style>
        table td{
            text-align: left;
        }
        .layui-table-body td{font-size: 12px;}
        .text-title {
            font-weight: bold;
            font-size: 14px !important;
            color: #333;
        }
    </style>
</head>
<body>

<div class="layui-fluid">
    <div class="layui-row layui-col-space15">
        <div class="layui-col-md12">
            <div class="layui-card">
                <div class="layui-card-body">
                    <table class="layui-table">
                        <colgroup>
                            <col width="160">
                            <col width="200">
                            <col width="160">
                            <col>
                        </colgroup>
                        <tbody class="layui-table-body">
                            <c:if test="${!empty(dec.imagePath)}">
                                <tr>
                                    <td class="text-title">商品图片：</td>
                                    <td colspan="3">
                                        <img src="${imageServiceUrl}${dec.imagePath}" style="width: 100px;height: 100px">
                                    </td>
                                </tr>
                            </c:if>
                            <tr>
                                <td class="text-title">商品名称：</td>
                                <td>
                                    <c:choose>
                                        <c:when test="${!empty(dec.link)}">
                                            <a href="${dec.link}" target="_blank" style="color:#1890ff;">${dec.proName}</a>
                                        </c:when>
                                        <c:otherwise>
                                            ${dec.proName}
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td class="text-title">asin：</td>
                                <td>${dec.asin}</td>
                            </tr>
                            <tr>
                                <td class="text-title">店铺：</td>
                                <td>
                                    ${dec.sellerName}
                                </td>
                                <td class="text-title">收货仓：</td>
                                <td>${dec.shc}</td>
                            </tr>
                            <tr>
                                <td class="text-title">需求量：</td>
                                <td>
                                    ${dec.purchasePackages} × ${dec.perPackageQuantity} = ${dec.totalQuantity} <br />
                                    (件数) × (单件数量) = (总量)
                                </td>
                                <td class="text-title">申请时间：</td>
                                <td>${dec.declareTime}</td>
                            </tr>
                            <c:if test="${!empty(dec.other)}">
                                <tr>
                                    <td class="text-title">备注信息：</td>
                                    <td colspan="3">
                                            ${dec.other}
                                    </td>
                                </tr>
                            </c:if>
                            <c:if test="${!empty(dec.confirmTime)}">
                                <tr>
                                    <td class="text-title">仓库确认时间：</td>
                                    <td colspan="3">${dec.confirmTime}</td>
                                </tr>
                            </c:if>
                            <c:if test="${!empty(dec.purchaseTime)}">
                                <tr>
                                    <td class="text-title">库存数量：</td>
                                    <td>${dec.kcsl}</td>
                                    <td class="text-title">库存用量：</td>
                                    <td>${dec.kcyl}</td>
                                </tr>
                                <tr>
                                    <td class="text-title">采购量：</td>
                                    <td>${dec.buyQuantity}</td>
                                    <td class="text-title">计划发货量：</td>
                                    <td>${dec.planTotalQuantity}</td>
                                </tr>
                            </c:if>
                            <c:if test="${!empty(dec.planShipTime)}">
                                <tr>
                                    <td class="text-title">预估到仓时间：</td>
                                    <td>${dec.planShipTime}</td>
                                    <td class="text-title">实际到仓时间：</td>
                                    <td>${dec.shipTime}</td>
                                </tr>
                            </c:if>
                        </tbody>
                    </table>
                </div>
                <div class="layui-card-body">
                    <form class="layui-form layui-form-pane" action="">
                        <c:if test="${dec.status eq '已采购' || dec.status eq '已到货'}">
                            <div class="layui-form-item" id="fapiao_div" style="${empty(dec.fapiao) ? 'display: none;' : ''}">
                                <label class="layui-form-label">发票文件</label>
                                <div class="layui-input-block">
                                    <div class="layui-colla-content layui-show" style="color:#333;font-weight: bold;">
                                        <a id="fapiao_link" href="javascript:downloadFile('${imageServiceUrl}${dec.fapiao}','${dec.proName}——发票文件')" download="" style="color:#1890ff;">${dec.proName}——发票文件</a>
                                    </div>
                                </div>
                            </div>

                            <div class="layui-form-item">
                                <label class="layui-form-label">发票</label>
                                <div class="layui-input-block">
                                    <div class="layui-upload-drag" style="display: block;" id="ID-upload-fapiao-drag">
                                        <i class="layui-icon layui-icon-upload"></i>
                                        <div>上传、修改发票信息</div>
                                        <input type="hidden" name="fapiao" value="${dec.fapiao}">
                                    </div>
                                </div>
                            </div>

                            <div class="layui-form-item" id="tips_div" style="${empty(dec.tips) ? 'display: none;' : ''}">
                                <label class="layui-form-label">标签</label>
                                <div class="layui-input-block">
                                    <div class="layui-colla-content layui-show" style="color:#333;font-weight: bold;">
                                        <a id="biaoqian_link" href="javascript:downloadFile('${imageServiceUrl}${dec.tips}','${dec.proName}——标签文件')" download="" style="color:#1890ff;">${dec.proName}——标签文件</a>
                                    </div>
                                </div>
                            </div>

                            <div class="layui-form-item">
                                <label class="layui-form-label">标签</label>
                                <div class="layui-input-block">
                                    <div class="layui-upload-drag" style="display: block;" id="ID-upload-biaoqian-drag">
                                        <i class="layui-icon layui-icon-upload"></i>
                                        <div>上传、修改标签信息</div>
                                        <input type="hidden" name="tips" value="${dec.tips}">
                                    </div>
                                </div>
                            </div>
                        </c:if>
                        <div class="layui-form-item" style="text-align: center;">
                            <button type="button" id="update_dec_button"
                                    class="layui-btn layui-bg-blue"><i class="fa-solid fa-check"></i>确定
                            </button>
                            <button type="button" id="close_win_btn"
                                    class="layui-btn layui-btn-warm"><i class="fa-solid fa-circle-xmark fa-fw"></i>关闭
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
        var upload = layui.upload;

        $("#update_dec_button").click(function () {
            var iframeIndex = parent.layer.getFrameIndex(window.name);
            parent.layer.close(iframeIndex);
        });
        $("#close_win_btn").click(function () {
            var iframeIndex = parent.layer.getFrameIndex(window.name);
            parent.layer.close(iframeIndex);
        });

        upload.render({
            elem: '#ID-upload-fapiao-drag',
            url: '/declaration/uploadFile',
            accept: 'file',
            data: {
                id: function () {
                    return $("#pro_id").val();
                },
                types: 'fapiao'
            },
            dataType: 'json',
            done: function(res){
                layer.msg(res.msg);
                if(res.status){
                    $("#fapiao_div").show();
                    $("#fapiao_link").attr("href", "javascript:downloadFile('"+res.href+"','${dec.proName}——发票信息')");
                    $("input[name='fapiao']").val(res.src);
                }
            }
        });

        upload.render({
            elem: '#ID-upload-biaoqian-drag',
            url: '/declaration/uploadFile',
            accept: 'file',
            data: {
                id: function () {
                    return $("#pro_id").val();
                },
                types: 'tips'
            },
            dataType: 'json',
            done: function(res){
                layer.msg(res.msg);
                if(res.status){
                    $("#tips_div").show();
                    $("#biaoqian_link").attr("href", "javascript:downloadFile('"+res.href+"','${dec.proName}——标签信息')");
                    $("input[name='tips']").val(res.src);
                }
            }
        });



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