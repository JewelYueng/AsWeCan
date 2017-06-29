<template>
  <div class="fram">
    <button @click="back" style="position: absolute;right: 0px;
       background-color: indianred;z-index: 100;border: none;">x
    </button>
    <el-menu :default-active="activeIndex" class="tabChoose" mode="horizontal" @select="handleSelect">
      <el-menu-item index="0">数据项格式配置</el-menu-item>
      <el-menu-item index="1">数据项整合配置</el-menu-item>
      <el-menu-item index="2">记录格式配置</el-menu-item>
    </el-menu>
    <component :is="current_view" @SAVE_FORMAT="changeFormat" @SAVE_RECORD="changeRecord"
               @SAVE_INTEGRATION="changeIntegration"></component>
    <div style="position:absolute;bottom: 15px;margin: auto;right: 0;left: 0;">
      <el-button type="primary" style="width: 80px;margin-top: 30px" @click="Normalizing()">规范化</el-button>
      <el-button type="primary" @click="back" style="width: 80px;margin-top: 30px">取消</el-button>
    </div>
  </div>

</template>

<style lang="less" rel="stylesheet/less">
  @import "~assets/colors.less";
  @import "~assets/layout.less";

  .fram {
    height: 600px;
    width: 700px;
    background-color: white;
    position: relative;
  }

  .el-button + .el-button {
    margin-left: 0;
  }

  .add-btn {
    text-align: left;
    margin: 20px 35px;
  }

  .el-table .cell, .el-table th>div{
    padding: 5px;
    textarea{
      border: none;
      height: 100%;
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
        selectedTab: 0,
        activeIndex: '0',
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
          console.log(res);
          if (res.data.code === 1) {
            this.$hint('规范化成功', 'success');
            this.commit(true)
          }
          else {
            this.$hint('规范化失败，该文件不是格式正确的原始日志文件', 'erorr');
          }

        }, res => {
          if (res.status === 500) {
            this.$hint('服务器错误，请稍后再试', 'error');
          }else if (res.status === 400){
            this.$hint('服务器没有这个文件请')
          }else if(res.status === 403){
            this.$hint('你没有这个权限')
          }
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
      handleSelect(key, keyPath) {
        this.selectedTab = parseInt(key)
      },
      back(){
        this.commit(true)
      },
    },
    computed: {
      current_view(){
        return this.view_dict[this.selectedTab]
      }

    }
  }
</script>
