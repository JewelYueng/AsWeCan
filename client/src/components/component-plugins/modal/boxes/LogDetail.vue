<template>
  <div class="fram">
    <button @click="back" style="position: absolute;right: 0;top: 5px;
       color: #324157;background-color: white;border: none;cursor: pointer"><i class="el-icon-close"></i>
    </button>
  <div class="form-data">
    <el-table :data="eventData">
      <el-table-column  class="form-item" prop="name" label="名称" width="180" >
      </el-table-column>
      <el-table-column class="form-item"  prop="value" label="项值" width="180" >
      </el-table-column>
    </el-table>
    <el-button type="primary" @click="back" class="back-btn">返回</el-button>
  </div>
  </div>
</template>

<style lang="less" rel="stylesheet/less">
  .fram{
    position: relative;
    padding-left: 30px;
    padding-top: 30px;
    padding-right: 3px;
    width: 400px;
    border-bottom: 60px solid white;
    background-color: white;
    box-shadow: 0 0 3px 0 #324157;
    border-radius: 5px;
  }
  .form-data{
    text-align: left;
    max-height: 500px;
    overflow: auto;
    .el-table{
      width: 95%;
    }
  }
  .form-item{
    overflow: auto;
    /*text-overflow:ellipsis;*/
  }
  .back-btn{
    margin-left: 165px;
    position: fixed;
    bottom: 100px;
  }
</style>

<script>
  import BaseBox from './BaseBox'

  export default {
    mixins: [BaseBox],
    data() {
      return {
        test: 'msg',
        eventData: [{
          name: '总实例数',
          value: this.data.caseNumber || 0
        }, {
          name: '总事件数',
          value: this.data.eventNumber || 0
        }, {
          name: '平均每实例中事件数',
          value: this.data.perEventInCase || 0
        }, {
          name: '流程活动事件',
          value: this.data.eventNames || '无'
        }, {
          name: '流程活动操作人',
          value: this.data.operatorNames || '无'
        }, ]
      }
    },
    methods:{
      back() {
        this.commit(true)
      }
    }
  }
</script>
