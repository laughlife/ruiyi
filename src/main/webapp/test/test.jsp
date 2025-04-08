<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2025/4/8
  Time: 10:41
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<ul class="layui-nav layui-nav-tree" lay-shrink="all">

    <li class="layui-nav-item layui-nav-itemed">
        <a href="javascript:;" lay-tips="应用">
            <i class="layui-icon layui-icon-app"></i>
            <cite>应用</cite>
        </a>
        <dl class="layui-nav-child">
            <dd>
                <a href="javascript:;">图片处理</a>
                <dl class="layui-nav-child">
                    <dd>
                        <a lay-href="/image/goImagePage">图片生成</a>
                    </dd>
                </dl>
            </dd>
        </dl>
    </li>

    <li class="layui-nav-item ">
        <a href="javascript:;" lay-tips="管理">
            <i class="fa-solid fa-users"></i>
            <cite>管理</cite>
        </a>
        <dl class="layui-nav-child">
            <dd>
                <a lay-href="">部门管理</a>
            </dd>
            <dd>
                <a lay-href="">人员管理</a>
            </dd>
            <dd>
                <a lay-href="">权限分配</a>
            </dd>
        </dl>
    </li>

    <li class="layui-nav-item ">
        <a href="javascript:;" lay-tips="设置">
            <i class="fa-solid fa-screwdriver-wrench"></i>
            <cite>设置</cite>
        </a>
        <dl class="layui-nav-child">
            <dd>
                <a lay-href="/user/setting">基本资料</a>
            </dd>
            <dd>
                <a lay-href="/page/system/updatePwd.jsp">修改密码</a>
            </dd>
        </dl>
    </li>

    <li class="layui-nav-item ">
        <a href="javascript:;" lay-tips="开发">
            <i class="layui-icon layui-icon-util"></i>
            <cite>开发</cite>
        </a>

        <dl class="layui-nav-child">
            <dd>
                <a lay-href="/page/menu/menu.jsp">菜单设置</a>
            </dd>
            <dd>
                <a href="javascript:;">图标</a>
                <dl class="layui-nav-child">
                    <dd>
                        <a lay-href="/page/icon/icon.jsp">layui图标</a>
                    </dd>
                    <dd>
                        <a lay-href="/font/font_list">fontawesome6</a>
                    </dd>
                </dl>
            </dd>
            <dd>
                <a lay-href="/page/deepseek/deepseek.jsp">deepseek</a>
            </dd>
            <dd>
                <a lay-href="/lingxing/goSettingPage">功能测试</a>
            </dd>
        </dl>
    </li>

</ul>
</body>
</html>
