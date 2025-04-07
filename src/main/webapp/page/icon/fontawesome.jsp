<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Font Awesome 图标展示</title>
    <link rel="stylesheet" href="/static/fontawesome6/css/all.min.css">
    <style>
        .icon-container {
            display: flex;
            flex-wrap: wrap;
            gap: 20px;
            justify-content: center;
            padding: 20px;
        }

        .icon {
            text-align: center;
            width: 100px;
        }

        .icon i {
            font-size: 24px;
            display: block;
        }
    </style>
</head>
<body>
<h1>Font Awesome 图标展示</h1>
<div class="icon-container">
    <c:forEach items="${fontList}" var="icon">
        <div class="icon">
            <i class="${icon.font_name}"></i>
            <span>${icon.font_name}</span>
        </div>
    </c:forEach>
</div>
</body>
</html>
