<template>
  <div class="logs">
    <button @click="back" style="position: absolute;right: 0px;top: 5px;
       color: #324157;background-color: white;border: none;cursor: pointer"><i class="el-icon-close"></i>
    </button>
    <el-table
      ref="singleTable"
      :data="logs"
      highlight-current-row
      @current-change="handleCurrentChange"
      max-height="300"
      style="width: 100%"
      >
      <el-table-column
        type="index"
        label="索引"
        width="80"
        >
      </el-table-column>
      <el-table-column
        property="eventLog.logName"
        label="文件名"
        width="120" show-overflow-tooltip
       >
      </el-table-column>
      <el-table-column
        property="rawLog.logName"
        label="原始日志"
        width="120" show-overflow-tooltip
        >
      </el-table-column>
      <el-table-column
        property="normalLog.logName"
        label="规范化日志"
        width="120" show-overflow-tooltip
       >
      </el-table-column>
      <el-table-column
        property="eventLog.mergeRelation"
        label="融合来源"
        width="150" show-overflow-tooltip
        >
      </el-table-column>
    </el-table>
    <div class="btn-group">
      <el-button type="primary" @click="sure()">确定</el-button>
      <el-button type="text" @click="cancel()">取消</el-button>
    </div>
  </div>
</template>

<style lang="less"  rel="stylesheet/less">
  @import '~assets/colors.less';

  .logs {
    position: relative;
    padding: 30px;
    margin-top: 20px;
    background-color: white;
    box-shadow: 0 0 3px 0 #324157;
    border-radius: 5px;
  }

  .btn-group{
    margin-top: 20px;
  }
  .el-table__body tr.current-row>td{
    background-color: @light_silver;
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
      },
      back(){
        this.commit(true)
      }
    }
  }
</script>
