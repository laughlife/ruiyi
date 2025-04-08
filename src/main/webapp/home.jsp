<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>睿翼</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0">
    <link rel="icon" href="/static/image/favicon.ico" type="image/x-icon"/>
    <link rel="stylesheet" href="/static/layui/css/layui.css" media="all">
    <link rel="stylesheet" href="/static/fontawesome6/css/all.min.css" media="all">
    <link rel="stylesheet" href="/static/layui/style/admin.css" media="all">
    <script src="/static/jquery/jquery-3.7.1.min.js"></script>
    <style>
        cite{margin-left:10px;}
        .ml20{margin-left:20px;}
    </style>
</head>
<body class="layui-layout-body">
<div id="LAY_app">
    <div class="layui-layout layui-layout-admin">
        <div class="layui-header">
            <!-- 头部区域 -->
            <ul class="layui-nav layui-layout-left">
                <li class="layui-nav-item layadmin-flexible" lay-unselect>
                    <a href="javascript:;" layadmin-event="flexible" title="侧边伸缩">
                        <i class="layui-icon layui-icon-shrink-right" id="LAY_app_flexible"></i>
                    </a>
                </li>
                <li class="layui-nav-item layui-hide-xs" lay-unselect>
                    <a href="https://www.layui1.com/" target="_blank" title="前台">
                        <i class="layui-icon layui-icon-website"></i>
                    </a>
                </li>
                <li class="layui-nav-item" lay-unselect>
                    <%--<a href="javascript:void(0);" data-refresh="刷新" title="刷新">
                        <i class="layui-icon layui-icon-refresh-3"></i>
                    </a>--%>
                    <a href="javascript:;" layadmin-event="refresh" title="刷新">
                        <i class="layui-icon layui-icon-refresh-3"></i>
                    </a>
                </li>

                <li class="layui-nav-item layui-hide-xs" lay-unselect>
                    <input type="text" placeholder="搜索..." autocomplete="off" class="layui-input layui-input-search"
                           layadmin-event="serach" lay-action="template/search.html?keywords=">
                </li>
            </ul>
            <ul class="layui-nav layui-layout-right" lay-filter="layadmin-layout-right">

                <li class="layui-nav-item" lay-unselect>
                    <a lay-href="app/message/index.html" layadmin-event="message" lay-text="消息中心">
                        <i class="layui-icon layui-icon-notice"></i>
                        <!-- 如果有新消息，则显示小圆点 -->
                        <span class="layui-badge-dot"></span>
                    </a>
                </li>
                <li class="layui-nav-item layui-hide-xs" lay-unselect>
                    <a href="javascript:;" layadmin-event="theme">
                        <i class="layui-icon layui-icon-theme"></i>
                    </a>
                </li>
                <li class="layui-nav-item layui-hide-xs" lay-unselect>
                    <a href="javascript:;" layadmin-event="note">
                        <i class="layui-icon layui-icon-note"></i>
                    </a>
                </li>
                <li class="layui-nav-item layui-hide-xs" lay-unselect>
                    <a href="javascript:;" layadmin-event="fullscreen">
                        <i class="layui-icon layui-icon-screen-full"></i>
                    </a>
                </li>
                <li class="layui-nav-item" lay-unselect>
                    <a href="javascript:;">
                        <cite>超级管理员</cite>
                    </a>
                    <dl class="layui-nav-child">
                        <dd><a lay-href="set/user/info.html">基本资料</a></dd>
                        <dd><a lay-href="/page/system/updatePwd.jsp">修改密码</a></dd>
                        <hr>
                        <dd style="text-align: center;"><a href="/user/loginout">退出</a></dd>
                    </dl>
                </li>

                <li class="layui-nav-item layui-hide-xs" lay-unselect>
                    <a href="javascript:;" layadmin-event="about"><i
                            class="layui-icon layui-icon-more-vertical"></i></a>
                </li>
                <li class="layui-nav-item layui-show-xs-inline-block layui-hide-sm" lay-unselect>
                    <a href="javascript:;" layadmin-event="more"><i class="layui-icon layui-icon-more-vertical"></i></a>
                </li>
            </ul>
        </div>

        <!-- 侧边菜单 -->
        <div class="layui-side layui-side-menu">
            <div class="layui-side-scroll">
                <div class="layui-logo" lay-href="/page/deepseek/home.jsp">
                    <span>南阳睿翼</span>
                </div>

                <ul class="layui-nav layui-nav-tree" lay-shrink="all">
                    <c:forEach items="${menuList}" var="menu" varStatus="i">
                        <li class="layui-nav-item ${i.index eq 0 ? 'layui-nav-itemed' : ''}">
                            <a href="javascript:;" lay-tips="${menu.name}">
                                <i class="${menu.icon}"></i>
                                <cite>${menu.name}</cite>
                            </a>
                            <c:if test="${menu.children.size() gt 0}">
                                <dl class="layui-nav-child">
                                    <c:forEach items="${menu.children}" var="child">
                                        <dd>
                                            <c:choose>
                                                <c:when test="${child.isLink eq 1}">
                                                    <a class="ml20" lay-href="${child.path}">${child.name}</a>
                                                </c:when>
                                                <c:otherwise>
                                                    <a class="ml20" href="javascript:;">${child.name}</a>
                                                    <c:if test="${child.children.size() gt 0}">
                                                        <dl class="layui-nav-child">
                                                            <c:forEach items="${child.children}" var="c">
                                                                <dd>
                                                                    <a lay-href="${c.path}">${c.name}</a>
                                                                </dd>
                                                            </c:forEach>
                                                        </dl>
                                                    </c:if>
                                                </c:otherwise>
                                            </c:choose>
                                        </dd>
                                    </c:forEach>
                                </dl>
                            </c:if>
                        </li>
                    </c:forEach>
                </ul>
            </div>
        </div>

        <!-- 页面标签 -->
        <div class="layadmin-pagetabs" id="LAY_app_tabs">
            <div class="layui-icon layadmin-tabs-control layui-icon-prev" layadmin-event="leftPage"></div>
            <div class="layui-icon layadmin-tabs-control layui-icon-next" layadmin-event="rightPage"></div>
            <div class="layui-icon layadmin-tabs-control layui-icon-down">
                <ul class="layui-nav layadmin-tabs-select" lay-filter="layadmin-pagetabs-nav">
                    <li class="layui-nav-item" lay-unselect>
                        <a href="javascript:;"></a>
                        <dl class="layui-nav-child layui-anim-fadein">
                            <dd layadmin-event="closeThisTabs"><a href="javascript:;">关闭当前标签页</a></dd>
                            <dd layadmin-event="closeOtherTabs"><a href="javascript:;">关闭其它标签页</a></dd>
                            <dd layadmin-event="closeAllTabs"><a href="javascript:;">关闭全部标签页</a></dd>
                        </dl>
                    </li>
                </ul>
            </div>
            <div class="layui-tab" lay-unauto lay-allowClose="true" lay-filter="layadmin-layout-tabs">
                <ul class="layui-tab-title" id="LAY_app_tabsheader">
                    <li lay-id="home/console.html" lay-attr="home/console.html" class="layui-this"><i
                            class="layui-icon layui-icon-home"></i></li>
                </ul>
            </div>
        </div>


        <!-- 主体内容 -->
        <div class="layui-body" id="LAY_app_body">
            <div class="layadmin-tabsbody-item layui-show">
                <iframe src="/lingxing/goSettingPage" frameborder="0" class="layadmin-iframe"></iframe>
            </div>
        </div>

        <!-- 辅助元素，一般用于移动设备下遮罩 -->
        <div class="layadmin-body-shade" layadmin-event="shade"></div>
    </div>
</div>

<script src="/static/layui/layui.js"></script>
<script>
    layui.config({
        base: '/static/layui/'
    }).extend({
        index: 'lib/index'
    }).use('index');

    // $('body').on('click', '[data-refresh]', function () {
    //     $("#LAY_app_body").find("iframe")[0].contentWindow.location.reload();
    // });

    layui.use(function () {

    });

    $(document).ready(function () {
        setInterval(function () {
            //5分钟向后台请求一次，防止session过期
            $.ajax({
                url: "/system/refresh", // 保持Session 的后端接口
                type: "post",
                cache: false,
                dataType: "json",
                success: function (data) {
                    if (!data.status) {
                        layer.msg("登录已过期，请重新登录", {icon: 5});
                        setTimeout(function () {
                            window.location.href = "/index.jsp";
                        }, 1000);
                    }
                },
                error: function (data) {
                    layer.msg("登录已过期，请重新登录", {icon: 5});
                    setTimeout(function () {
                        window.location.href = "/index.jsp";
                    }, 1000);
                }
            });
        }, 1000 * 60 * 5);
    });
</script>


</body>
</html>
