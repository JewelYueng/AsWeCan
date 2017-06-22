<template>
  <div class="form-data">
    <el-table :data="eventData">
      <el-table-column prop="name" label="名称" width="180">
      </el-table-column>
      <el-table-column prop="value" label="项值" width="180">
      </el-table-column>
    </el-table>
    <el-button type="primary" @click="back" class="back-btn">返回</el-button>
  </div>
</template>

<style lang="less" scoped rel="stylesheet/less">
  .form-data{
    text-align: left;
    padding: 30px;
    background-color: whitesmoke;
  }
  .back-btn{
    margin: 20px auto 0 auto;
    display: block;
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
