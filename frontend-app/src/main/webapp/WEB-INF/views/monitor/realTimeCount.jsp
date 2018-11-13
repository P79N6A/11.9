<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
    <meta name="decorator" content="none"/>
    <title>实时下单量监控图</title>
    <%@ include file="commonIndex.jsp" %>
    <script type="text/javascript">
        function drawByData(type) {
            var series = [];
            var data = [];
            var orgData = type.data;
            for(var i = 0; i < orgData.series.length; i++){
                data.push({
                    x:new Date(orgData.series[i].ORDER_DATE).getTime(),
                    y:orgData.series[i].TRX_COUNT
                })
            }
            var se = {
                name:orgData.name,
                data:data
            }
            series.push(se);

            var chartData = {
                series : series,
                title : type.name,
                subtitle : '',
                yAxisText : '交易量',
                refreshUrl : 'monitor/realtimeRefresh',
                type:type.type,
                fixedLength:30 //固定长度
            };
            createChartDiv(type.eid);
            drawRealtimeChart(type.eid, chartData);
        }

        function createChartDiv(elementId) {
            $('#chartContainer').append('<div class="chart" id="'+ elementId +'"></div>');
        }

        $(function () {
            $(document).ready(function () {
                var wechat_total = {
                    type : 'WECHAT-TOTAL',
                    name : '微信近30分钟内每分钟交易量',
                    eid :  'container_wechat',
                    data : ${wechat_total}
                };
                var wechat_success = {
                    type : 'WECHAT-SUCCESS',
                    name : '微信近30分钟内每分钟成功交易量',
                    eid :  'container_wechat_success',
                    data : ${wechat_success}
                };
                var alipay_total = {
                    type : 'ALIPAY-TOTAL',
                    name : '支付宝近30分钟内每分钟交易量',
                    eid :  'container_alipay',
                    data : ${alipay_total}
                };
                var alipay_success = {
                    type : 'ALIPAY-SUCCESS',
                    name : '支付宝近30分钟内每分钟成功交易量',
                    eid :  'container_alipay_success',
                    data : ${alipay_success}
                };
                var net_total = {
                    type : 'NET-TOTAL',
                    name : '网银近30分钟内每分钟交易量',
                    eid :  'container_net',
                    data : ${net_total}
                };
                var net_success = {
                    type : 'NET-SUCCESS',
                    name : '网银近30分钟内每分钟成功交易量',
                    eid :  'container_net_success',
                    data : ${net_success}
                };
                var mpay = {
                    type : 'MPAY-TOTAL',
                    name : 'MPAY近30分钟内每分钟交易量',
                    eid :  'container_mpay',
                    data : ${mpay_total}
                };
                var dataArrays = [wechat_total,wechat_success,alipay_total,alipay_success,net_total,net_success,mpay];
                for (var i = 0; i< dataArrays.length; i++){
                    drawByData(dataArrays[i]);
                }
            });
        });
    </script>
</head>
<body>
<div id="chartContainer" class="container">
</div>
</body>
</html>
