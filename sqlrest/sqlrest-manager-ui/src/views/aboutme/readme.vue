<template>
  <div>
    <h3>关于SQLREST工具</h3>
    <el-tabs v-model="activeName"
             @tab-click="handleClick()">
      <el-tab-pane label="系统功能"
                   name="first">
        <div class="content_tag">
          <el-tag type="danger">SQL接口</el-tag>
          <el-tag type="success">脚本接口</el-tag>
        </div>
        <div class="content_card">
          <el-card class="box-card">
            <div class="text item">
              <p>
                一句话，sqlrest工具提供快速构建RESTful的<B>API接口</B>工具，包括SQl方式和脚本方式。功能包括：
              </p>
              <ul>
                <li>项目托管</li>
                <p>
                  Gitee地址：<a href="https://gitee.com/inrgihc/sqlrest">https://gitee.com/inrgihc/sqlrest</a>
                </p>
                <li>SQL方式构建RESTful接口</li>
                <p>
                  提供类似mybatis的动态SQL语法方式构建接口。
                </p>
                <li>脚本方式构建RESTful接口</li>
                <p>
                  基于groovy脚本的语法方式构建复杂场景下的接口。
                </p>
                <li>支持接口的token安全认证功能</li>
                <p>
                  执行器支持生成token及token认证。
                </p>
                <li>支持生成在线接口文档功能</li>
                <p>
                  基于swagger-ui提供生成在线接口文档功能。
                </p>
                <li>支持接口的流量控制功能</li>
                <p>
                  执行器基于sentinel支持接口的流量控制功能。
                </p>
              </ul>
            </div>
          </el-card>
        </div>
      </el-tab-pane>
      <el-tab-pane label="关系数据库"
                   name="second">
        <div class="content_tag">
          <el-tag type="success">JDBC</el-tag>
          <el-tag type="danger">sqlrest</el-tag>
        </div>
        <div class="content_card">
          <el-card class="box-card">
            <div class="text item">
              <p>
                sqlrest配置支持的常规关系数据库有20+，包括：
              </p>
              <ul>
                <li>甲骨文的Oracle
                </li>
                <li>MySQL
                </li>
                <li>MariaDB
                </li>
                <li>微软的Microsoft SQLServer
                <li>PostgreSQL
                </li>
                <li>Greenplum(需使用PostgreSQL类型)
                </li>
                <li>IBM的DB2
                </li>
                <li>Sybase数据库
                </li>
                <li>国产达梦数据库DMDB
                </li>
                <li>国产人大金仓数据库Kingbase8
                </li>
                <li>国产翰高数据库HighGo
                </li>
                <li>国产神通数据库Oscar
                </li>
                <li>国产南大通用数据库GBase8a
                </li>
                <li>Apache Hive
                </li>
                <li>Apache Doris
                </li>
                <li>SQLite3
                </li>
                <li>OpenGuass
                </li>
                <li>ClickHouse
                </li>
                <li>StarRocks
                </li>
                <li>OceanBase
                </li>
              </ul>
            </div>
          </el-card>
        </div>
      </el-tab-pane>
      <el-tab-pane label="开发技术栈"
                   name="third">
        <div class="content_tag">
          <el-tag type="success">SpringBoot</el-tag>
          <el-tag type="danger">SpringCloud</el-tag>
          <el-tag type="success">Mybatis</el-tag>
          <el-tag type="danger">Vue/ElementUI</el-tag>
        </div>
        <div class="content_card">
          <el-card class="box-card">
            <div class="text item">
              <p>
                sqlrest基于Springboot脚手架进行的后端模块开发，模块组成结构如下：
              <pre>
            └── sqlrest
                ├── sqlrest-common                     // sqlrest通用定义模块
                ├── sqlrest-template                   // sqlrest的SQL内容模板模块
                ├── sqlrest-persistence                // sqlrest的数据库持久化模块
                ├── sqlrest-core                       // sqlrest-core的接口实现模块
                ├── sqlrest-gateway                    // Gateway网关节点
                ├── sqlrest-executor                   // Executor接口执行节点
                ├── sqlrest-manager                    // Manager管理节点
                ├── sqlrest-manager-ui                 // 基于Vue2的Manager前段WEB交互页面
                ├── sqlrest-dist                       // 基于maven-assembly-plugin插件的项目打包模块
                </pre>
              </p>
              <ul>
                <li>SpringBoot</li>
                <p>
                  sqlrest基于SpringBoot作为项目的基础脚手架。
                </p>
                <p>
                  sqlrest-gateway/sqlrest-executor/sqlrest-manager基于SpringCloud的微服务架构提供接口配置与访问功能。
                </p>
                <li>MyBatis</li>
                <p>
                  MyBatis 是一款优秀的持久层框架，它支持定制化 SQL、存储过程以及高级映射。
                <p>
                  sqlrest-persistence模块基于Mybatis/Mybatis-plus提供配置数据的持久化。
                </p>
                <li>Vue/ElementUI</li>
                <p>
                  Vue是一套用于构建用户界面的渐进式JavaScript框架。 Element是饿了么团队基于MVVM框架Vue开源出来的一套前端基于Vue 2.0的桌面端组件库。
                </p>
                <p>
                  sqlrest-manager-ui模块基于Vue和ElementUI提供可视化的操作WEB界面。
                </p>
              </ul>
            </div>
          </el-card>
        </div>
      </el-tab-pane>

    </el-tabs>
  </div>
</template>

<script>
import layout from '../layout.vue';
export default {
  components: { layout },
  data () {
    return {
      activeName: 'first'
    };
  },
  methods: {
    handleClick (tab, event) {
      //console.log(tab, event);
    }
  }
}
</script>

<style scoped>
div h3 {
  text-align: center;
}

.content_tag {
  text-align: center;
}

.content_card {
  padding-top: 20px;
}
</style>
