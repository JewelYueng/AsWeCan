<template>
  <div class="logs">
    <button @click="back" style="position: absolute;right: 2px;top: 7px;
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
        property="rawLog"
        label="原始日志"
        width="120" show-overflow-tooltip
        >
      </el-table-column>
      <el-table-column
        property="normalLog"
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
    <div class="block pageDiv">
      <el-pagination
        @current-change="handleCurrentPageChange"
        :current-page="currentPage"
        :page-size="10"
        layout="prev, pager, next, jumper"
        :total="total_items_num">
      </el-pagination>
    </div>
    <div class="btn-group">
      <el-button type="primary" @click="sure()">确定</el-button>
      <el-button @click="cancel()">取消</el-button>
    </div>
  </div>
</template>

<style lang="less"  rel="stylesheet/less">
  @import '~assets/colors.less';

  .logs {
    padding: 30px;
    height: 400px;
    position: fixed;
    left: 45%;
    top: 50%;
    z-index: 11;
    /*设定这个div的margin-top的负值为自身的高度的一半,margin-left的值也是自身的宽度的一半的负值.*/
    /*宽为400,那么margin-top为-200px*/
    /*高为200那么margin-left为-100px;*/
    margin: -200px 0 0 -250px;
    background-color: white;
    box-shadow: 0 0 3px 0 #324157;
    border-radius: 5px;
    .pageDiv{
      margin: 20px;
    }
    .el-table__body tr.current-row>td{
      background-color: @light_silver;
    }
  }

  .btn-group{
    margin-top: 20px;
  }



</style>

<script>
  import BaseBox from './BaseBox'
  export default{
    data(){
      return {
        logs: [],
        currentPage: 1,
        total_items_num: 10,
        selectedLog: null
      }
    },
    mixins: [BaseBox],
    created(){
      this.getTotalItems()
    },
    methods: {
      handleCurrentPageChange(val) {
        this.currentPage = val
        this.getTotalItems()
      },
      getTotalItems(){
        this.$api({method: 'getEventLog', query: {page: this.currentPage}}).then((res) => {
          console.log(res)
          this.logs = res.data.logGroups
          this.resolveLogs()
          this.total_items_num = res.data.pageNum * 10
        })
      },
      sure(){
        if(this.selectedLog) {
          this.commit({
            id: this.selectedLog.eventLog.id,
            logName: this.selectedLog.eventLog.logName,
          })
        }else{
          this.$hint('请选择一个事件日志','warn')
        }
      },
      cancel(){
        this.commit(true)
      },
      handleCurrentChange(val) {
        this.selectedLog = val;
      },
      back(){
        this.commit(true)
      },
      resolveLogs(){
        this.logs.map(log => {
          log.normalLog = log.normalLog ? log.normalLog.logName : '无'
          log.rawLog = log.rawLog ? log.rawLog.logName : '无'
          log.eventLog.mergeRelation = log.eventLog.mergeRelation ? `${log.eventLog.mergeRelationLogs[0].logName},${log.eventLog.mergeRelationLogs[1].logName}` : '无'
        })

      }
    }
  }
</script>
