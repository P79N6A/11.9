/**
 * Created by lewis on 2016/10/18.
 */
var symbolArrays = ['square', 'diamond'];//点形状
var s = 0 ;

Highcharts.setOptions({
    global: {
        useUTC: false
    }
});

function drawSplineChart(elementId, chartData){
    //time
    //wechat-count
    //zfb-count
    var categories = chartData.categories;
    var series = [];
    for (var i = 0; i< chartData.series.length; i++){
        if (s > (symbolArrays.length - 1)){
            s = s -  symbolArrays.length ;
        }
        var obj = {
            name : chartData.series[i].name,
            symbol : symbolArrays[s],
            data : chartData.series[i].data
        };
        series.push(obj);
    }
    var title = chartData.title;//标题
    var sub_title = chartData.subtitle;//子标题
    var yAxisText = chartData.yAxisText ;//Y轴描述
    drawChart(elementId, categories, series, title, sub_title, yAxisText);
}

function drawChart(elementId, categories, series, title, sub_title, yAxisText) {
    $('#' + elementId).highcharts({
        chart: {
            type: 'spline'
        },
        title: {
            text: title
        },
        subtitle: {
            text: sub_title
        },
        xAxis: {
            categories: categories
        },
        yAxis: {
            title: {
                text: yAxisText
            },
            labels: {
                formatter: function () {
                    return this.value ;
                }
            }
        },
        tooltip: {
            formatter: function () {
                return '<b>' + this.series.name  +'</b><br/>' + this.x + ':00'
                       + '   交易量:' + this.y;
            }
        },
        plotOptions: {
            line: {
                marker: {
                    radius: 4,
                    lineColor: '#666666',
                    lineWidth: 1
                }
            }
        },
        series: series
    });
}

function drawRealtimeChart(elementId, chartData) {
    $('#' + elementId).highcharts({
        chart: {
            type: 'line',
            animation: Highcharts.svg, // don't animate in old IE
            marginRight: 10,
            events: {
                load: function () {
                    var series = this.series[0];
                    setInterval(function () {
                        $.ajax({
                            type: 'GET',
                            url: GV.ctxPath + chartData.refreshUrl,
                            contentType: "text/plain;charset=UTF-8",
                            dataType: 'json',
                            data:'type='+chartData.type,
                            success: function (json) {
                                if (json == null || json.series == null || json.series.length < 1){
                                    return;
                                }
                                var len = series.data.length;
                                old_x = series.data[len - 1].x;
                                // console.info("series.data.length:" + len + ";endData:" + series.data[len - 1].x)
                                var x = (new Date(json.series[0].ORDER_DATE)).getTime();
                                var y = json.series[0].TRX_COUNT;
                                if(old_x == x){
                                    return;
                                }
                                var shift = true;
                                if (len < chartData.fixedLength)
                                    shift = false;
                                series.addPoint([x, y], true, shift);
                            }
                        });
                    }, 60000);
                }
            }
        },
        title: {
            text: chartData.title
        },
        xAxis: {
            type: 'datetime',
            tickPixelInterval: 1
        },
        yAxis: {
            title: {
                text: chartData.yAxisText
            },
            plotLines: [{
                value: 0,
                width: 1,
                color: '#808080'
            }]
        },
        tooltip: {
            formatter: function () {
                return '<b>' + this.series.name + '</b><br/>' +
                    Highcharts.dateFormat('%Y-%m-%d %H:%M', this.x) + '<br/>交易量:' + this.y;
            }
        },
        plotOptions: {
            line: {
                marker: {
                    radius: 4,
                    lineColor: '#666666',
                    lineWidth: 1
                },
                dataLabels: {
                    enabled: true
                },
                enableMouseTracking: true
            }
        },
        legend: {
            enabled: false
        },
        exporting: {
            enabled: false
        },
        series: chartData.series
    });
}