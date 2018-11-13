<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
    <meta name="decorator" content="none"/>
    <title>日交易对比</title>
    <%@ include file="commonIndex.jsp" %>
    <script type="text/javascript">
        $(function () {
            var wechat = {
                name : 'wechat',
                title : '微信日交易量对比',
                eid : 'container_wechat',
                data : ${wechat}
            };
            var alipay = {
                name : 'alipay',
                title : '支付宝日交易量对比',
                eid : 'container_alipay',
                data : ${alipay}
            };
            var net = {
                name : 'net',
                title : '网银日交易量对比',
                eid : 'container_net',
                data : ${net}
            };
            var mpay = {
                name : 'MPAY',
                title : 'MPAY日交易量对比',
                eid :  'container_mpay',
                data : ${mpay}
            };
            var tableData = {
                categories:wechat.data.categories,
                data : [wechat,alipay,net,mpay]
            }
            drawByData(wechat);
            drawByData(alipay);
            drawByData(mpay);
            drawByData(net);
            createDataTable(tableData);
        });
        /**
         * 获取同步数据
         * @returns {{categories: Array, series: *[], title: string, subtitle: string, yAxisText: string}|*}
         */
        function drawByData(type) {
            console.info(type);
            var orgData = type.data;
            var categories = orgData.categories;
            var series = [];
            for (var i = 0; i < orgData.series.length; i++){
                var se = {
                    name:orgData.series[i].name,
                    data:orgData.series[i].data
                }
                series.push(se);
            }
            var chartData = {
                categories : categories,
                series : series,
                title : type.title,
                subtitle : '',
                yAxisText : '交易量'
            };
            createChartDiv(type.eid);
            drawSplineChart(type.eid,chartData);
        }

        function createChartDiv(elementId) {
            $('#chartContainer').append('<div class="chart" id="'+ elementId +'"></div>');
        }
        
        function createDataTable(tableData) {
            $('#chartContainer').append('<div class="chart" id="dataTable"></div>');
            $('#dataTable').append('<ul id="categories" class="table_row"></ul>');
            $('#categories').append('<li class="row_header" style="height: 50px">时间(升序)</li>');
            for(var i = 0; i < tableData.categories.length; i++){
                $('#categories').append('<li>' + tableData.categories[i] + '</li>');
            }
            for (var i = 0; i < tableData.data.length;i++){
                var name = tableData.data[i].name;
                var data = tableData.data[i].data.series;
                for (var j = 0; j < data.length; j++){
                    var eid = name + '-' + data[j].name;
                    var realData = data[j].data;
                    $('#dataTable').append('<ul class="table_row" id="' + eid+ '"></ur>');
                    $('#' + eid).append('<li class="row_header" style="height: 50px">'+eid+'</li>');
                    for (var k = 0; k < realData.length; k++){
                        $('#' + eid).append('<li>' + realData[k]+ '</li>')
                    }
                }

            }
        }
    </script>
</head>
<body>
<div id="chartContainer" class="container">
</div>
</body>
</html>
