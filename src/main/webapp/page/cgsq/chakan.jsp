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
                                <div class="layui-colla-content layui-show" style="color:#333;font-weight: bold;">
                                    ${dec.shc}
                                </div>
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
                            <label class="layui-form-label">需求量</label>
                            <div class="layui-input-block">
                                <div class="layui-colla-content layui-show" style="color:#333;font-weight: bold;">
                                    ${dec.purchasePackages}(件数) × ${dec.perPackageQuantity}(单件数量) = ${dec.totalQuantity} (总量)
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
                        <c:if test="${dec.status eq '已采购'}">
                            <div class="layui-form-item">
                                <label class="layui-form-label">采购时间</label>
                                <div class="layui-input-block">
                                    <div class="layui-colla-content layui-show" style="color:#333;font-weight: bold;">
                                        ${dec.purchaseTime}
                                    </div>
                                </div>
                            </div>
                            <div class="layui-form-item">
                                <label class="layui-form-label">采购数量</label>
                                <div class="layui-input-block">
                                    <div class="layui-colla-content layui-show" style="color:#333;font-weight: bold;">
                                        ${dec.buyQuantity}
                                    </div>
                                </div>
                            </div>
                            <div class="layui-form-item">
                                <label class="layui-form-label">计划发货</label>
                                <div class="layui-input-block">
                                    <div class="layui-colla-content layui-show" style="color:#333;font-weight: bold;">
                                            ${dec.planTotalQuantity}
                                    </div>
                                </div>
                            </div>
                            <div class="layui-form-item">
                                <label class="layui-form-label">发货时间</label>
                                <div class="layui-input-block">
                                    <div class="layui-colla-content layui-show" style="color:#333;font-weight: bold;">
                                            ${dec.planShipTime}(预估)
                                    </div>
                                </div>
                            </div>
                        </c:if>
                        <c:if test="${!empty(dec.fapiao)}">
                            <div class="layui-form-item" id="fapiao_div">
                                <label class="layui-form-label">发票文件</label>
                                <div class="layui-input-block">
                                    <div class="layui-colla-content layui-show" style="color:#333;font-weight: bold;">
                                        <a id="fapiao_link" href="javascript:downloadFile('${imageServiceUrl}${dec.fapiao}','${dec.proName}——发票文件')" download="" style="color:#1890ff;">${dec.proName}——发票文件</a>
                                    </div>
                                </div>
                            </div>
                        </c:if>

                        <c:if test="${!empty(dec.tips)}">
                            <div class="layui-form-item" id="tips_div" style="${empty(dec.tips) ? 'display: none;' : ''}">
                                <label class="layui-form-label">标签</label>
                                <div class="layui-input-block">
                                    <div class="layui-colla-content layui-show" style="color:#333;font-weight: bold;">
                                        <a id="biaoqian_link" href="javascript:downloadFile('${imageServiceUrl}${dec.tips}','${dec.proName}——标签文件')" download="" style="color:#1890ff;">${dec.proName}——标签文件</a>
                                    </div>
                                </div>
                            </div>
                        </c:if>

                        <div class="layui-form-item" style="text-align: center;">
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

        $("#close_win_btn").click(function () {
            var iframeIndex = parent.layer.getFrameIndex(window.name);
            parent.layer.close(iframeIndex);
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