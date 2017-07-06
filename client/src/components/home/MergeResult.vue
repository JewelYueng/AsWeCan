<template>
  <div>
    <div id="merge-result" v-if="$route.params.log">
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
          <div class="relations">
            <div class="value" v-for="(log,index) in result.eventLog.mergeRelationLogs" @click="jumpToEvent(index)">
              {{log.logName}}
            </div>
          </div>
        </div>
      </div>
      <div class="btn-group">
        <el-button type="primary" @click="jumpToMining">流程挖掘</el-button>
        <el-button type="primary" @click="backToMerge">返回</el-button>
      </div>
    </div>
    <div v-if="!$route.params.log">
      暂无数据
    </div>
  </div>
</template>

<style lang="less" scoped rel="stylesheet/less">
  @import "~assets/colors.less";
  @import "~assets/layout.less";

  #merge-result {
    background-color: white;
    height: 100%;
    width: @major_width;
    padding: 10px;
    box-sizing: border-box;
    .header {
      text-align: left;
      font-size: 24px;
      color: @dark_theme;
      font-weight: bold;
      padding: 10px 10px 5px 10px;
      border-bottom: @tab_separator 1px solid;
    }
    .result-body {
      display: flex;
      flex-direction: column;
      align-items: flex-start;
      justify-content: flex-start;
      overflow-y: auto;
      width: 100%;
      padding: 10px;
      box-sizing: border-box;
      .result-item {
        display: flex;
        flex-direction: row;
        width: 96%;
        border-bottom: @dark_theme 1px solid;
        padding: 20px;
        .key {
          text-align: left;
          flex: 0 0 20%;
        }
        .value {
          text-align: left;
          flex: 0 0 80%;
        }
        .relations {
          display: flex;
          flex-direction: row;
          cursor: pointer;
        }

      }
    }

  }
</style>

<script>
  import {mapActions} from 'vuex'
  export default {
    data(){
      return {
        result: {
          "timeCost": 895,
          "eventLog": {
            "type": "EventLog",
            "id": "9b075229-4ff5-4178-95c9-34e38b593eb7",
            "logName": "12-merge.xes",
            "createDate": 1497852883817,
            "format": "xes",
            "state": 1,
            "isShared": 0,
            "userId": "1",
            "normalLogId": null,
            "caseNumber": 132,
            "eventNumber": 1670,
            "perEventInCase": 0,
            "eventNames": "end,check if sufficient information is available,register claim,determine likelihood of claim,assess claim,initiate payment,d,advise claimant on reimbursement,close claim,a,b,f,e,c",
            "operatorNames": "Call Centre Agent,Claims handler,UNDEFINED",
            "mergeRelation": "1,2"
          }

        }
      }
    },
    created(){
      console.log(this.$route.params.log)
      if (this.$route.params.log) {
        this.result = this.$route.params.log
      }
    },
    methods: {
      ...mapActions(['selectLog', 'changeFilePath']),
      backToMerge: function () {
//        this.jumpView('/home/merge')
        this.$router.push({name: 'merge'})
        console.log(this.$api)

      },
      jumpToMining: function () {
//        this.jumpView('/home/mining'
        this.$router.push({name: 'mining', params: {log: this.result.eventLog}})
      },
      jumpToEvent(index){
        if (this.result.eventLog.mergeRelationLogs) {
          this.$api({method: 'getEventLogPage', query: {id: this.result.eventLog.mergeRelationLogs[index].id}}).then(res => {
            this.selectLog({type: 2, id: this.result.eventLog.mergeRelationLogs[index].id, page: res.data.page})
            this.changeFilePath('1-3')
            this.$router.push({name: 'home'})
          }, err => {
            console.log(err)
            this.$hint(err.data.msg, 'error')
          })
        }
      }
    }
  }
</script>
