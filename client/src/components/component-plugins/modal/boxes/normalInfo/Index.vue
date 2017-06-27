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
//        editingRow: -1,
//        data1: [
//          {
//            name: '[QC]',
//            character: 'ABCD',
//            identifier: '[Method]',
//            source: 'Incident:A-B-C-D,Plan:C/B/ATD,Task:A/B/CTD,DEFAULT:A-B-CTD',
//            target: 'A-B-CTD'
//          },
//        ],
//        dataTime: [
//          {
//            sourceItem: '[QC]',
//            targetItem: 'Time'
//          }
//        ],
//        data2: [
//          {oriName: "EventName", target: ["[Method]", "[Status]"]},
//          {oriName: "FKPlanID", target: ["[FKPlanID]"]},
//          {oriName: "PKIncidentId", target: ["[PKIncidentId]"]},
//          {oriName: "PKTaskID", target: ["[PKTaskID]"]},
//          {oriName: "PKPlanID", target: ["[PKPlanID]"]},
//          {oriName: "FKIncidentID", target: ["[FKIncidentID]"]}
//        ],
//        data3: [
//          {
//            item: '/t',
//            name: ' ',
//            null: ' '
//          }
//        ]
      }

    },
    components: {
      Format,
      Integration,
      Record
    },
    methods: {
//      表格的操作函数
//      addBlankRow(){
//        this.data1.push({})
//      },
//      handleEdit(index, row){
//        this.editingRow = index
//      },
//      saveEdit(index, row){
//        this.data1[index] = this.editing
//        this.editingRow = -1
//      },
//      handleDelete(index, row){
//        this.data1.splice(index, 1)
//      },
//      isEditing(index){
//        return index === this.editingRow
//      },
      Normalizing(){
//        this.$api({
//          method: 'normalize', body: {
//            "rawLogId": this.data.id,
//            "formats": this.format,
//            "timeNames": "[QC]",
//            "renameOrMergeItems": this.integration,
//            "oriItemSeparator": "\t",
//            "oriNameValSeparator": " ",
//            "oriNulVal": ""
//          }
//        }).then((res) => {
//          console.log(res);
//          if (res.data.code === 1) {
//            this.$hint('规范化成功', 'success');
//          }
//          else if(res.status === 500){
//            this.$hint('参数设置不当', 'warn');
//          }
//          else{
//            this.$hint('网络连接失败', 'erorr');
//          }
//
//        })
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
          }
          else {
            this.$hint('规范化失败', 'erorr');
          }

        }, res => {
          if (res.status === 500) {
            this.$hint('参数设置不当或文件不是原始日志', 'warn');
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
//      editing(){
//        return this.editingRow === -1 ? {} : this.data1[this.editingRow]
//      },
      current_view(){
        return this.view_dict[this.selectedTab]
      }

    }
  }
</script>
