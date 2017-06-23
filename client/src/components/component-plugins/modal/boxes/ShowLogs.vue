<template>
  <div class="logs">
    <el-table
      ref="singleTable"
      :data="logs"
      highlight-current-row
      @current-change="handleCurrentChange"
      height="250"
      style="width: 100%">
      <el-table-column
        type="index"
        label="索引"
        width="120">
      </el-table-column>
      <el-table-column
        property="eventLog.logName"
        label="文件名"
        width="120">
      </el-table-column>
      <el-table-column
        property="rawLog.logName"
        label="原始日志"
        width="120">
      </el-table-column>
      <el-table-column
        property="nomalLog.logName"
        label="规范化日志"
        width="120">
      </el-table-column>
      <el-table-column
        property="eventLog.mergeRelation"
        label="融合来源"
        width="150">
      </el-table-column>
    </el-table>
    <div class="btn-group">
      <el-button type="primary" @click="sure()">确定</el-button>
      <el-button type="text" @click="cancel()">取消</el-button>
    </div>
  </div>
</template>

<style lang="less" scoped rel="stylesheet/less">
  @import '~assets/colors.less';

  .logs {
    padding: 10px;
    background-color: @light_theme;
  }

  .btn-group{
    margin-top: 10px;
  }
</style>

<script>
  import BaseBox from './BaseBox'
  export default{
    data(){
      return {
        logs: [],
        selectedLog: null
      }
    },
    mixins: [BaseBox],
    created(){
      this.$api({method: 'getEventLog'}).then(res => {
        console.log(res)
        this.logs = res.data.logGroups
      })
    },
    methods: {
      sure(){
        this.commit({
          id: this.selectedLog.eventLog.id,
          name: this.selectedLog.eventLog.logName,
        })
      },
      cancel(){
        this.commit(true)
      },
      handleCurrentChange(val) {
        this.selectedLog = val;
      }
    }
  }
</script>
