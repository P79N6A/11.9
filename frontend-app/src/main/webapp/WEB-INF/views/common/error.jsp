<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
    <%@ include file="/WEB-INF/views/common/common.jsp" %>
    <title>错误信息</title>
    <style type="text/css">
        .errorMsg{
            width:30%;
            margin-left: 30%;
            margin-top: 50px;
            padding: 20px;
            border: 1px #E0E0E0 solid;
        }
        .errorMsg p{
            margin-bottom: 10px;
        }
    </style>
</head>

<body>
<div class="errorMsg">
    <p>错误码：<span>${errCode}</span></p>
    <p>错误信息：<span>${errMsg}</span></p>
    <p>详细：<span>${detail}</span></p>
    <p><input type="button" onclick="javascript:history.back()" value="返回上一页" class="btn_cancel" /> </p>
</div>
</body>
</html>
