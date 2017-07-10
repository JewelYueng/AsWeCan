<template>
  <div class="normal-fram">
    <button @click="back" style="position: absolute;right: 0px;top: 5px;
       color: #324157;background-color: white;z-index: 100;border: none;cursor: pointer"><i class="el-icon-close"></i>
    </button>
    <el-tabs v-model="activeName" type="card" >
      <el-tab-pane label="数据项格式配置" name="0"></el-tab-pane>
      <el-tab-pane label="数据项整合配置" name="1"></el-tab-pane>
      <el-tab-pane label="记录格式配置" name="2"></el-tab-pane>
    </el-tabs>
    <component :is="current_view" @SAVE_FORMAT="changeFormat" @SAVE_RECORD="changeRecord"
               @SAVE_INTEGRATION="changeIntegration"></component>
    <div style="margin: auto;right: 0;left: 0;">
      <el-button type="primary" style="width: 80px;margin-top: 20px" @click="Normalizing()">规范化</el-button>
      <el-button @click="back" style="width: 80px;margin-top: 20px">取消</el-button>
    </div>
  </div>

</template>

<style lang="less" rel="stylesheet/less">
  @import "~assets/colors.less";
  @import "~assets/layout.less";

  .normal-fram {
    padding: 20px;
    max-height: 600px;
    width: 700px;
    box-shadow: 0 0 3px 0 #324157;
    border-radius: 5px;
    overflow: auto;
    background-color: white;
    position: relative;
    .el-button + .el-button {
      margin-left: 0;
    }
    .el-table .cell, .el-table th>div{
      padding: 5px;
      textarea{
        border: none;
        height: 100%;
      }
  }



  .add-btn {
    text-align: left;
    margin: 15px 35px;
  }


  }

</style>

<script>
  import BaseBox from '../BaseBox'
  import Format from './Format.vue'
  import Integration from './Integration.vue'
  import Record from './Record.vue'
  import _ from 'lodash'
  export default{
    mixins: [BaseBox],
    data() {
      return {
        activeName: '0',
        format: [
          {
            "dataItem": "[QC]",
            "placeholder": "ABCD",
            "identifyItem": "[Method]",
            "oriFormat": {
              "Incident": "A-B-C-D",
              "Plan": "C/B/ATD",
              "Task": "A/B/CTD",
              "DEFAULT": "A-B-CTD"
            },
            "goalFormat": "A-BCTD"
          },
        ],
        integration: {
          "EventName": ["[Method]", "[Status]"],
          "FKPlanID": ["[FKPlanID]"],
          "PKIncidentId": ["[PKIncidentId]"],
          "PKTaskID": ["[PKTaskID]"],
          "PKPlanID": ["[PKPlanID]"],
          "FKIncidentID": ["[FKIncidentID]"]
        },
        record: {
          "oriItemSeparator": "\t",
          "oriNameValSeparator": " ",
          "oriNulVal": ""
        },
        send_data: {},
        view_dict: [Format, Integration, Record],

      }

    },
    components: {
      Format,
      Integration,
      Record
    },
    methods: {
      Normalizing(){
        this.$api({
          method: 'normalize', body: _.extend({
            "rawLogId": this.data.id,
            "formats": this.format,
            "timeNames": "[QC]",
            "renameOrMergeItems": this.integration,
          }, this.record)
        }).then((res) => {
          //console.log(res);
          if (res.data.code === 1) {
            this.$hint('规范化成功', 'success');

            this.commit(res.data.code)
          }
          else {
            this.$hint('不明原因失败，建议刷新', 'erorr');
          }
        }, err => {
          console.log(err)
          this.$hint(err.data.msg,'error')

        })
      },
      changeFormat(format){
        this.format = format
      },
      changeRecord(record){
        this.record = record
      },
      changeIntegration(integration){
        this.integration = integration
      },
      back(){
        this.commit(true)
      },

    },
    computed: {
      current_view(){
        return this.view_dict[this.activeName]
      }

    }
  }
</script>
