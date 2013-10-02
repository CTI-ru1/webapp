function createHighChart(containerName, chartTitle, xLabel, yLabel, dataLabel, chartData) {


    return new Highcharts.Chart({
        chart: {
            renderTo: containerName,
            defaultSeriesType: 'spline',
            zoomType: 'x',
            spacingRight: 20
        },
        title: {
            text: chartTitle
        },
        subtitle: {
            text: document.ontouchstart === undefined ?
                'Click and drag in the plot area to zoom in' :
                'Drag your cursor over the plot to zoom in'
        },
        xAxis: {
            type: xLabel,
            tickPixelInterval: 150,
            maxZoom: 1000
        },
        yAxis: {
            title: {
                text: yLabel
            },
            min: 0.6,
            startOnTick: false,
            showFirstLabel: false
        },
        tooltip: {
            shared: true
        },
        legend: {
            enabled: false
        },
        series: [
            {
                name: dataLabel,
                data: chartData
            }
        ],
        plotOptions: {
            series: {
                marker: {
                    enabled: false
                }
            }
        }
    });
}