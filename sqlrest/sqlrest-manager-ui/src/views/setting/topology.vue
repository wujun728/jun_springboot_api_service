<template>
  <el-card>
    <div class="right-refresh-button">
      <el-button type="primary"
                 plain
                 size="mini"
                 icon="el-icon-refresh"
                 @click="handleClickRefresh"
                 round>刷新</el-button>
    </div>
    <br /> <br /> <br />
    <div ref="graph_container"
         id="graph_container">
    </div>
  </el-card>
</template>

<script>
// 参考文章：https://blog.csdn.net/qq_43955202/article/details/107829407
// VUE生命周期：https://blog.csdn.net/qq_43985303/article/details/128896018
import {
  mxGraph,
  mxClient,
  mxUtils,
  mxHierarchicalLayout,
} from 'mxgraph-js';

export default {
  name: 'topology',
  data () {
    return {
      v_graph: null,
      v_parent: null,
      mxgraphData: [
        {
          nodesList: [
            [0, "test01"],
            [1, "test02"],
            [2, "test03"],
          ],
          edgesList: [
            [0, "requst", 1],
            [0, "requst", 2],
          ]
        },
      ],
    }
  },
  methods: {
    loadData: function () {
      this.mxgraphData[0].nodesList = []
      this.mxgraphData[0].edgesList = []
      this.$http.get("/sqlrest/manager/api/v1/node/topology").then(res => {
        if (0 === res.data.code) {
          var gateways = []
          var executors = []
          var role = 'NONE'
          for (let node of res.data.data) {
            if (node.serviceId.includes('MANAGER')) {
              role = 'MANAGER'
            } else if (node.serviceId.includes('GATEWAY')) {
              role = 'GATEWAY'
              gateways.push(node.instanceId)
            } else if (node.serviceId.includes('EXECUTOR')) {
              role = 'EXECUTOR'
              executors.push(node.instanceId)
            }
            var item = [node.instanceId, role + '\n' + node.host + ':' + node.port]
            this.mxgraphData[0].nodesList.push(item)
          }

          for (let gateway of gateways) {
            for (let executor of executors) {
              this.mxgraphData[0].edgesList.push([gateway, 'request', executor])
            }
          }
        } else {
          alert("加载数据失败:" + res.data.message);
        }

        if (this.v_graph) {
          this.v_graph.destroy();
          this.mxGraph();
        }
      })

    },
    /**
     * 生成节点
     */
    createNodes (graph, parent) {
      //遍历mxgraphData生成节点
      var len = this.mxgraphData[0].nodesList.length;
      for (var i = 0; i < len; i++) {
        let text = this.mxgraphData[0].nodesList[i][1];
        let id = `node${this.mxgraphData[0].nodesList[i][0]}`;
        var node = graph.insertVertex(
          parent,
          id,
          text,
          0,
          0,
          200,
          80,
          "fillColor=#234c9e;strokeColor=white;fontStyle=1;fontColor=white;rounded=1;fontSize=15;"
        );
      }
    },

    /**
     * 生成边
     */
    createEdges (graph, parent) {
      var len = this.mxgraphData[0].edgesList.length;
      for (var i = 0; i < len; i++) {
        //寻找到对应的节点id
        var startIndex = `node${this.mxgraphData[0].edgesList[i][0]}`;
        var endIndex = `node${this.mxgraphData[0].edgesList[i][2]}`;

        //graph.getModel().getCell()  通过id找到对应的节点node
        var v1 = graph.getModel().getCell(startIndex);
        var v2 = graph.getModel().getCell(endIndex);

        var label = this.mxgraphData[0].edgesList[i][1];
        var eage = graph.insertEdge(parent, null, label, v1, v2, "");
      }
    },
    mxGraph () {
      if (!mxClient.isBrowserSupported()) {
        // 判断是否支持mxgraph
        mxUtils.error("Browser is not supported!", 200, false);
      } else {
        // 在容器中创建图表
        let container = document.getElementById("graph_container");

        var graph = new mxGraph(container);
        var parent = graph.getDefaultParent();
        this.v_graph = graph;
        this.v_parent = parent;

        graph.getModel().beginUpdate();

        //定义布局
        var layout = new mxHierarchicalLayout(graph);

        try {
          this.createNodes(graph, parent);
          this.createEdges(graph, parent);
          layout.execute(parent);
        } finally {
          // Updates the display
          graph.getModel().endUpdate();
        }
      }
    },
    handleClickRefresh: function () {
      this.loadData()
    }
  },
  created () {
    this.loadData()
    //console.log(JSON.stringify(this.mxgraphData))
  },
  mounted () {
    this.mxGraph()
  },
};
</script>
<style>
.el-card {
  width: 100%;
  height: 100%;
  overflow: auto;
}

.right-refresh-button {
  float: right;
  margin-right: 2px;
  margin: 10px 2px;
}
</style>
