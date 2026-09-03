<template>
  <div style="display: inline-block">
    <i class="iconfont icon-my-SQL colored_db"
       v-if="type == 'MYSQL'"></i>
    <i class="iconfont icon-MariaDB-chanpinicon colored_db"
       v-else-if="type == 'MARIADB'"></i>
    <i class="iconfont icon-oracle-copy-png colored_db"
       v-else-if="type == 'ORACLE'"></i>
    <i class="iconfont icon-SQLServer colored_db"
       v-else-if="type == 'SQLSERVER'"></i>
    <i class="iconfont icon-postgresql colored_db"
       v-else-if="type == 'POSTGRESQL'"></i>
    <i class="iconfont icon-DB2 colored_db"
       v-else-if="type == 'DB2'"></i>
    <i class="iconfont icon-hive colored_db"
       v-else-if="type == 'HIVE'"></i>
    <i class="iconfont icon-clickhouse2 colored_db"
       v-else-if="type == 'CLICKHOUSE'"></i>
    <i class="iconfont icon-OTHER colored_db"
       v-else></i>
  </div>
</template>

<script>
export default {
  name: "databaseIcon",
  props: ['type']
}
</script>

<style scoped>
.colored_db {
  color: #0698a5;
  font-size: 18px;
  font-weight: 500;
  margin-right: 5px;
}
</style>
