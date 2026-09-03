<template>
  <div class="dashbord">
    <el-row class="infoCrads">
      <el-col :span="6">
        <div class="cardItem">
          <div class="cardItem_txt">
            <CountTo class="cardItem_p0 color-green1"
                     :startVal="startVal"
                     :endVal="statistics.totalCount"
                     :duration="2000"></CountTo>
            <p class="cardItem_p1">配置接口数</p>
          </div>
          <div class="cardItem_icon">
            <i class="el-icon-s-grid color-green1"></i>
          </div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="cardItem">
          <div class="cardItem_txt">
            <CountTo class="cardItem_p0 color-blue"
                     :startVal="startVal"
                     :endVal="statistics.openCount"
                     :duration="2000"></CountTo>
            <p class="cardItem_p1">开放接口数</p>
          </div>
          <div class="cardItem_icon">
            <i class="el-icon-s-data color-blue"></i>
          </div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="cardItem">
          <div class="cardItem_txt">
            <CountTo class="cardItem_p0 color-green2"
                     :startVal="startVal"
                     :endVal="statistics.publishCount"
                     :duration="2000"></CountTo>
            <p class="cardItem_p1">发布接口数</p>
          </div>
          <div class="cardItem_icon">
            <i class="el-icon-loading color-green2"></i>
          </div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="cardItem">
          <div class="cardItem_txt">
            <CountTo class="cardItem_p0 color-red"
                     :startVal="startVal"
                     :endVal="statistics.datasourceCount"
                     :duration="2000"></CountTo>
            <p class="cardItem_p1">数据源总数</p>
          </div>
          <div class="cardItem_icon">
            <i class="el-icon-office-building color-red"></i>
          </div>
        </div>
      </el-col>
    </el-row>
    <el-card class="box-card">
      <div slot="header"
           class="clearfix">
        <el-row>
          <el-col :span="8">
            <span>时间范围:</span>
            <el-select v-model="selectDays"
                       @change="selectChangedRangeTime"
                       placeholder="请选择统计时间">
              <el-option v-for="item in optionDays"
                         :key="item.value"
                         :label="item.label"
                         :value="item.value">
              </el-option>
            </el-select>
          </el-col>
          <el-col :span="8">
            <span>TOPN数:</span>
            <el-select v-model="topNum"
                       @change="selectChangedTopNum"
                       placeholder="请选择topN">
              <el-option v-for="item in optionTopN"
                         :key="item.value"
                         :label="item.label"
                         :value="item.value">
              </el-option>
            </el-select>
          </el-col>
          <el-col :span="8">
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="8">
            <div id="topPathChart"></div>
          </el-col>
          <el-col :span="8">
            <div id="topAppChart"></div>
          </el-col>
          <el-col :span="8">
            <div id="topAddrChart"></div>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="16">
            <div id="barChart"></div>
          </el-col>
          <el-col :span="8">
            <div id="pieChart"></div>
          </el-col>
        </el-row>
      </div>
    </el-card>
  </div>
</template>
<script>
import CountTo from "vue-count-to";

export default {
  name: "Dashboard",
  components: {
    CountTo
  },
  data () {
    return {
      startVal: 0,
      statistics: {},
      optionDays: [
        { label: '1日内', value: 1 },
        { label: '3日内', value: 3 },
        { label: '7日内', value: 7 },
        { label: '30日内', value: 30 },
      ],
      optionTopN: [
        { label: 'Top3', value: 3 },
        { label: 'Top5', value: 5 },
        { label: 'Top6', value: 6 },
        { label: 'Top8', value: 8 },
        { label: 'Top10', value: 10 },
      ],
      selectDays: 7,
      topNum: 6,
      barChart: null,
      pieChart: null,
      topPathChart: null,
      topAppChart: null,
      topAddrChart: null,
      barChartData: {
        title: {
          text: '趋势统计'
        },
        tooltip: {
          trigger: "axis"
        },
        legend: {
          data: [
            {
              name: '总数',
              textStyle: {
                color: '#000'
              }
            },
            {
              name: '成功数',
              textStyle: {
                color: '#000'
              }
            }
          ]
        },
        grid: {
          left: "3%",
          right: "4%",
          bottom: "3%",
          containLabel: true
        },
        xAxis: {
          type: "category",
          boundaryGap: true,
          data: [],
          axisLabel: {
            interval: 0,
            textStyle: {
              color: '#000',
              fontSize: 10
            },
            margin: 8
          },
          axisLine: {
            show: true,
            lineStyle: {
              color: 'rgb(2,121,253)'
            }
          },
          axisTick: {
            show: false,
          }
        },
        yAxis: {
          type: "value"
        },
        series: [
          {
            name: "总数",
            type: "bar",
            barWidth: '8%',
            data: []
          },
          {
            name: "成功数",
            type: "bar",
            barWidth: '8%',
            data: []
          }
        ]
      },
      pieChartData: {
        title: {
          text: '失败率'
        },
        tooltip: {
          trigger: 'item'
        },
        legend: {
          orient: 'vertical',
          left: 'right',
        },
        series: [
          {
            name: '操作状态',
            type: 'pie',
            radius: '55%',
            data: [
              { value: 0, name: '成功' },
              { value: 0, name: '失败' },
            ],
            emphasis: {
              itemStyle: {
                shadowBlur: 10,
                shadowOffsetX: 0,
                shadowColor: 'rgba(0, 0, 0, 0.5)'
              }
            }
          }
        ]
      },
      topPathData: {
        title: {
          text: 'TOP接口'
        },
        color: ['#40c9c6'],
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'shadow'
          }
        },
        legend: {},
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: {
          type: 'value',
          boundaryGap: [0, 0.01]
        },
        yAxis: {
          type: 'category',
          data: []
        },
        series: [
          {
            type: 'bar',
            data: [12, 44, 55, 67, 89, 112]
          }
        ]
      },
      topAppData: {
        title: {
          text: 'TOP应用'
        },
        color: ['#36a3f7'],
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'shadow'
          }
        },
        legend: {},
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: {
          type: 'value',
          boundaryGap: [0, 0.01]
        },
        yAxis: {
          type: 'category',
          data: []
        },
        series: [
          {
            type: 'bar',
            data: [12, 44, 55, 67, 89, 112]
          }
        ]
      },
      topAddrData: {
        title: {
          text: 'TOP地址'
        },
        color: ['#34bfa3'],
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'shadow'
          }
        },
        legend: {},
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: {
          type: 'value',
          boundaryGap: [0, 0.01]
        },
        yAxis: {
          type: 'category',
          data: []
        },
        series: [
          {
            type: 'bar',
            data: [12, 44, 55, 67, 89, 112]
          }
        ]
      }
    };
  },
  methods: {
    loadTotal: function () {
      this.$http.get("/sqlrest/manager/api/v1/overview/counter")
        .then(
          res => {
            if (0 === res.data.code) {
              this.statistics = res.data.data;
            }
          }
        );
    },
    loadData: function () {
      this.$http.get("/sqlrest/manager/api/v1/overview/trend/" + this.selectDays)
        .then(
          res => {
            if (0 === res.data.code) {
              var lists = res.data.data;
              var xAxisData = [];
              var y1AxisData = [];
              var y2AxisData = [];
              for (var i = 0; i < lists.length; i++) {
                xAxisData.push(lists[i].ofDate);
                y1AxisData.push(lists[i].total);
                y2AxisData.push(lists[i].success);
              }
              this.barChartData.xAxis.data = xAxisData;
              this.barChartData.series[0].data = y1AxisData;
              this.barChartData.series[1].data = y2AxisData;

              this.barChart.setOption(this.barChartData, true);
            }
          }
        );

      this.$http.get("/sqlrest/manager/api/v1/overview/ratio/" + this.selectDays)
        .then(
          res => {
            if (0 === res.data.code) {
              var result = res.data.data;
              var list = []
              result.forEach(item => list.push({ name: item.name, value: item.count }))
              this.pieChartData.series[0].data = list
              this.pieChart.setOption(this.pieChartData, true);
            }
          }
        );

      this.$http.get("/sqlrest/manager/api/v1/overview/top/path/" + this.selectDays + "?n=" + this.topNum)
        .then(
          res => {
            if (0 === res.data.code) {
              var result = res.data.data;
              this.topPathData.yAxis.data = result.map(t => t.name).reverse()
              this.topPathData.series[0].data = result.map(t => t.count).reverse()
              this.topPathData.title.text = 'TOP' + this.topNum + '接口'
              this.topPathChart.setOption(this.topPathData, true);
            }
          }
        );

      this.$http.get("/sqlrest/manager/api/v1/overview/top/client/" + this.selectDays + "?n=" + this.topNum)
        .then(
          res => {
            if (0 === res.data.code) {
              var result = res.data.data;
              this.topAppData.yAxis.data = result.map(t => t.name).reverse()
              this.topAppData.series[0].data = result.map(t => t.count).reverse()
              this.topAppData.title.text = 'TOP' + this.topNum + '应用'
              this.topAppChart.setOption(this.topAppData, true);
            }
          }
        );

      this.$http.get("/sqlrest/manager/api/v1/overview/top/addr/" + this.selectDays + "?n=" + this.topNum)
        .then(
          res => {
            if (0 === res.data.code) {
              var result = res.data.data;
              this.topAddrData.yAxis.data = result.map(t => t.name).reverse()
              this.topAddrData.series[0].data = result.map(t => t.count).reverse()
              this.topAddrData.title.text = 'TOP' + this.topNum + '地址'
              this.topAddrChart.setOption(this.topAddrData, true);
            }
          }
        );

    },
    selectChangedRangeTime: function () {
      this.loadData();
    },
    selectChangedTopNum: function () {
      this.loadData();
    }
  },
  created () {
    this.loadTotal();
  },
  mounted () {
    this.barChart = this.$echarts.init(document.getElementById("barChart"));
    this.pieChart = this.$echarts.init(document.getElementById("pieChart"));
    this.topPathChart = this.$echarts.init(document.getElementById("topPathChart"));
    this.topAppChart = this.$echarts.init(document.getElementById("topAppChart"));
    this.topAddrChart = this.$echarts.init(document.getElementById("topAddrChart"));
    this.loadData();
  }
};
</script>

<style scoped>
.dashbord {
  background-color: #f0f3f4;
}

.color-green1 {
  color: #40c9c6 !important;
}
.color-blue {
  color: #36a3f7 !important;
}
.color-red {
  color: #f4516c !important;
}
.color-green2 {
  color: #34bfa3 !important;
}
.dashbord {
  background-color: #f0f3f4;
}

.infoCrads {
  margin: 20px 20px 20px 20px;
}

.infoCrads .el-col {
  padding: 10px 20px;
}

.infoCrads .el-col .cardItem {
  height: 128px;
  background: #fff;
}

.cardItem {
  color: #666;
}

.cardItem .cardItem_txt {
  float: left;
  margin: 26px 0 0 20px;
}

.cardItem .cardItem_txt .cardItem_p0 {
  font-size: 20px;
  margin: 26px 0 0 20px;
}

.cardItem .cardItem_txt .cardItem_p1 {
  font-size: 20px;
  margin: 26px 0 0 20px;
}

.cardItem .cardItem_icon {
  font-size: 64px;
  font-weight: bold;
}

#barChart {
  width: 95%;
  height: 300px;
  box-shadow: 1px 3px 3px #f0eeee;
  padding: 20px;
}
#pieChart {
  width: 95%;
  height: 300px;
  box-shadow: 1px 3px 3px #f0eeee;
  padding: 20px;
}
#topPathChart {
  width: 95%;
  height: 300px;
  box-shadow: 2px 3px 3px #f0eeee;
  padding: 20px;
}
#topAppChart {
  width: 95%;
  height: 300px;
  box-shadow: 2px 3px 3px #f0eeee;
  padding: 20px;
}
#topAddrChart {
  width: 95%;
  height: 300px;
  box-shadow: 2px 3px 3px #f0eeee;
  padding: 20px;
}
</style>