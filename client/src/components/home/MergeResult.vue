<template>
  <div id="merge-result">
    <div class="header">融合结果</div>
    <div class="result-body">
      <div class="result-name result-item">
        <div class="key">logName</div>
        <div class="value">{{result.eventLog.logName}}</div>
      </div>
      <div class="result-name result-item">
        <div class="key">createDate</div>
        <div class="value">{{new Date(result.eventLog.createDate).toString()}}</div>
      </div>
      <div class="result-name result-item">
        <div class="key">caseNumber</div>
        <div class="value">{{result.eventLog.caseNumber}}</div>
      </div>
      <div class="result-name result-item">
        <div class="key">eventNumber</div>
        <div class="value">{{result.eventLog.eventNumber}}</div>
      </div>
      <div class="result-name result-item">
        <div class="key">perEventInCase</div>
        <div class="value">{{result.eventLog.perEventInCase}}</div>
      </div>
      <div class="result-name result-item">
        <div class="key">eventNames</div>
        <div class="value">{{result.eventLog.eventNames}}</div>
      </div>
      <div class="result-name result-item">
        <div class="key">operatorNames</div>
        <div class="value">{{result.eventLog.operatorNames}}</div>
      </div>
      <div class="result-name result-item">
        <div class="key">mergeRelation</div>
        <div class="value">{{result.eventLog.mergeRelation}}</div>
      </div>
    </div>
    <div class="btn-group">
      <button class="mining" @click="jumpToMining">流程挖掘</button>
      <button class="back" @click="backToMerge">返回</button>
    </div>
  </div>
</template>

<style lang="less" scoped rel="stylesheet/less">
  @import "~assets/colors.less";
  @import "~assets/layout.less";

  #merge-result{
    border-radius: 20px 20px 0 0;
    background-color: #f1f1f1;
    height: 100%;
    .header{
      text-align: left;
      font-size: 24px;
      color: @main_green;
      font-weight: bold;
      padding: 10px 10px 5px 10px;
      border-bottom: @tab_separator 1px solid;
    }
    .result-body{
      display: flex;
      flex-direction: column;
      align-items: flex-start;
      justify-content: flex-start;
      height: 650px;
      overflow-y: auto;
      width: @right_side_width - 80px;
      padding: 10px;
      box-sizing: border-box;
      .result-item{
        display: flex;
        flex-direction: row;
        width: @right_side_width - 130px;
        border-bottom: @main_green 1px solid;
        padding: 20px  ;
        .key{
          text-align: left;
          flex: 0 0 20%;
        }
        .value{
          text-align: left;
          flex: 0 0 80%;
        }
      }
    }
    .btn-group{
      button{
        border-radius: 8px;
        background-color: @main_green;
        color: white;
        border: none;
        font-size: 16px;
        width: 100px;
        height: 40px;
        margin: 0 20px;
        cursor: pointer;
      }
      button:hover{
        background-color: #92cbb4;
      }
    }
  }
</style>

<script>
  import { mapActions } from 'vuex'
  export default {
    data(){
      return {
        result: {
          "timeCost":895,
          "eventLog":
            {
              "type":"EventLog",
              "id":"9b075229-4ff5-4178-95c9-34e38b593eb7",
              "logName":"12-merge.xes",
              "createDate":1497852883817,
              "format":"xes",
              "state":1,
              "isShared":0,
              "userId":"1",
              "normalLogId":null,
              "caseNumber":132,
              "eventNumber":1670,
              "perEventInCase":0,
              "eventNames":"end,check if sufficient information is available,register claim,determine likelihood of claim,assess claim,initiate payment,d,advise claimant on reimbursement,close claim,a,b,f,e,c",
              "operatorNames":"Call Centre Agent,Claims handler,UNDEFINED",
              "mergeRelation":"1,2"
            }

        }
      }
    },
    methods: {
      ...mapActions(['jumpView']),
      backToMerge: function(){
//        this.jumpView('/home/merge')
        this.$router.push({name: 'merge'})
        console.log(this.$api)

      },
      jumpToMining: function(){
//        this.jumpView('/home/mining'
        this.$router.push({name: 'mining'})
      }
    }
  }
</script>
