<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="q" uri="/ajaxquery-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<spring:url var="resourceUrl" value="/static"/>
<spring:url var="ctx" value="/"/>

<script type="text/javascript">
var GV = {
    ctxPath: '${ctx}',
    imgPath: '${resourceUrl}/images',
    jsPath: '${resourceUrl}/static/js'
  };
</script>
<script src="${resourceUrl}/js/common.js"></script>