<template>
  <div class="fram">
    <button @click="back" style="position: absolute;right: 2px;top: 7px;
       color: #324157;background-color: white;border: none;cursor: pointer"><i class="el-icon-close"></i>
    </button>
    <div class="form-data">
      <el-table :data="eventData" border>
        <el-table-column class="form-item" prop="name" label="名称" width="180">
        </el-table-column>
        <el-table-column class="form-item" prop="value" label="项值" width="180">
        </el-table-column>
      </el-table>
    </div>
    <div class="back-btn"><el-button type="primary" @click="back">返回</el-button></div>
  </div>
</template>

<style lang="less" rel="stylesheet/less">
  .fram {
    position: fixed;
    left: 50%;
    top: 50%;
    z-index: 11;
    /*设定这个div的margin-top的负值为自身的高度的一半,margin-left的值也是自身的宽度的一半的负值.*/
    /*宽为400,那么margin-top为-200px*/
    /*高为200那么margin-left为-100px;*/
    margin: -300px 0 0 -200px;
    padding: 30px 3px 10px 30px;
    width: 400px;
    background-color: white;
    box-shadow: 0 0 3px 0 #324157;
    border-radius: 5px;

    overflow-y: auto;
    overflow-x: hidden;
  }

  .form-data {
    text-align: left;
    max-height: 500px;
    position: relative;
    left: 6px;
    overflow-y: auto;
    overflow-x: hidden;
    margin-bottom: 10px;
    .el-table {
      width: 90%;
      overflow-x: hidden;

    }
  }

  .el-table__body-wrapper{
    overflow-x: hidden;
    overflow-y: auto;
  }

  .form-item {
    overflow-y: auto;
    overflow-x: hidden;
    /*text-overflow:ellipsis;*/
  }

  .back-btn {
    margin: 20px auto;
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
        },]
      }
    },
    methods: {
      back() {
        this.commit(true)
      }
    }
  }
</script>
