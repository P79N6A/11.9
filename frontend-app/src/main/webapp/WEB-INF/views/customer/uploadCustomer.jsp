<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
    <%@ include file="/WEB-INF/views/common/common.jsp" %>
    <title>迁移大算批量商户信息配置</title>
    <script type="text/javascript" src="${ctx}/static/js/common/jquery.form.js"></script>
    <script type="text/javascript">
    function upload() {
    	 $("#jsonFileForm").ajaxSubmit({
            url: GV.ctxPath + "customer/uploadList",
            type: "POST",
            success: function (response) {
                 var show = "成功数:" + response.success + ", 忽略数:" + response.ignore + "<br>";
                 if (response.errorList != null && response.errorList.length > 0) {
                     show += "失败数:" + response.errorList.length + ";失败信息如下:<br>"
                     for (var i= 0; i<response.errorList.length; i++){
                         show += response.errorList[i] + "<br>";
                     }
                 }
                 MessageBoxExt.alert(show);
            }
        });
    }
    </script>
</head>
<body>
<div class="Container">
    <div class="Content fontText">
        <div class="information">
            <div class="search">
               <div class="search_tit"><h2>上传商户配置信息文件(.xls)</h2></div>
               <div class="search_con" style="padding-left: 2%;padding-right: 0%;min-width: 1300px">
                <div style="margin-left: 100px;height: 30px;">
                 <p>上传文件模版如下，第一列是商户编号，第二列是商户要开通的网银产品支付场景，第三列是一键产品支付场景，
                  第四列是用户扫码支付产品(只支持SCCANPAY)，若某产品无需开通则该列为空即可。
                 </p>
                </div>
                <div style="margin-left: 300px;height: 30px;">
                    <table border="1">
                      <tr valign="middle">
                       <td align="center">10040007707</td>
                       <td align="center">B2C-STD-B2B-STD</td>
                       <td align="center">YJZF-WAP-USUAL</td>
                       <td align="center">SCCANPAY</td>
                      </tr>
                  </table>
                </div>
                <div style="margin-right: 512px;height: 30px;">
                 <a href="http://wiki.yeepay.com/pages/viewpage.action?pageId=50467633" target="_blank">
                 具体支付产品和支付场景等信息参考wiki【配置中心接口说明】</a>
                </div>
                  <form name="jsonFileForm" id="jsonFileForm" method="post" action="${ctxPath}customer/uploadCustomer"
                     enctype="multipart/form-data">
                    <input type="file" name="excelFile" value="商户配置信息">
                  </form>
                  <input type="button" name="upload" value="上传" onclick="upload()">
               </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>