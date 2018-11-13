<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
    <meta name="decorator" content="none"/>
    <%@ include file="/WEB-INF/views/common/common.jsp" %>
    <title>下单量统计</title>
    <script type="text/javascript" src="${ctx}/static/js/chart/exporting.js"></script>
    <script type="text/javascript" src="${ctx}/static/js/chart/highcharts.js"></script>
    <script type="text/javascript" src="${ctx}/static/js/chart/drawchart.js"></script>
    <script type="text/javascript">
        var chartData ; //将数据缓存在浏览器上
        $(function () {
            chartData = getChartData();
            drawSplineChart("container", chartData);
        });
        /**
         * 获取同步数据
         * @returns {{categories: Array, series: *[], title: string, subtitle: string, yAxisText: string}|*}
         */
        function getChartData() {
            var resultGroup = ${chartData};
            var categories = resultGroup.categories;
            var series = [];
            for (var i = 0; i < resultGroup.series.length; i++){
                var se = {
                    name:resultGroup.series[i].name,
                    data:resultGroup.series[i].data
                }
                series.push(se);
            }
            chartData = {
                categories : categories,
                series : series,
                title : '微信近30分钟交易量交易统计',
                subtitle : '',
                yAxisText : '交易量'
            };
            return chartData;
        }

        /**
         * 通过ajax获取当前时间的数据
         */
        function ajaxPost(type, url, orgData) {
            $.ajax({
                type: 'POST',
                url: url,
                // contentType: "text/plain;charset=UTF-8",
                // contentType: "application/json; charset=utf-8",
                dataType: 'json',
                // traditional:true,
                data: type,
                success: function (json) {
                    organizeData(json,orgData);
                }
            });
        }

        setInterval("drawTableInterval()",3000);//每分钟刷新一次

        function drawTableInterval()
        {
            <%--ajaxPost("container",'${ctx}monitor/refresh' , chartData);--%>
        }

        /**
         * 整理数据
         */
        function organizeData(json , orgData){
            if (json == null || json.categories == null || json.categories.length == 0)
                return;
            var len = orgData.categories.length;
            if(orgData.categories[len -1] == json.categories[0])
                return;

            for (var i = 0; i < json.categories.length; i++){
                orgData.categories.shift();
                orgData.categories.push(json.categories[i]);
                for (var j = 0; j < json.series.length; j++){
                    for (var k = 0; k < orgData.series.length; k++){
                        if(json.series[j].name == orgData.series[k].name){
                            orgData.series[k].data.shift();
                            orgData.series[k].data.push(json.series[j].data[i]);
                            break;
                        }
                    }
                }
            }

            drawSplineChart("container", orgData);
        }

        //定时查询
    </script>
</head>
<body>
    <%@ include file="commonIndex.jsp" %>
    <div id="container_wechat" style="min-width:700px;height:400px;float: left"></div>
</body>
</html>
