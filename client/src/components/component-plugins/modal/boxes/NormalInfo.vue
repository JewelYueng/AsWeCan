<template>
  <div class="fram">
    <button @click="back" style="position: absolute;right: 0px;
       background-color: indianred;z-index: 100;border: none;">x</button>
    <el-menu :default-active="activeIndex" class="tabChoose" mode="horizontal" @select="handleSelect">
      <el-menu-item index="1" >数据项格式配置</el-menu-item>
      <el-menu-item index="2">数据项整合配置</el-menu-item>
      <el-menu-item index="3">记录格式配置</el-menu-item>
    </el-menu>

    <template id="table1" v-if="selectedTab === 1">
    <div>
    <br/>
      <div style="text-align: left;margin-left: 10px;">
      <el-button size="small" style="width: 80px;font-size:15px">添加</el-button>
      <el-button size="small" style="width: 80px;font-size:15px">删除</el-button></div>
    <el-table :data="data1" border style="margin-top: 30px;width: 90%;margin: auto;">
      <el-table-column prop="name" label="数据项名" ></el-table-column>
      <el-table-column prop="character" label="占位符" ></el-table-column>
      <el-table-column prop="identifier" label="格式标示符"></el-table-column>
      <el-table-column prop="source" label="源格式"></el-table-column>
      <el-table-column prop="target" label="目标格式"></el-table-column>
    </el-table>
    </div>
    </template>

    <template id="table2" v-if="selectedTab === 2">
    <div>
      <div style="text-align: left;margin-left: 10px;">时间项整合</div>
      <el-table :data="dataTime" border style=
        "margin-top: 20px;width: 90%;margin: auto;">
          <el-table-column prop="sourceItem" label="源数据项"></el-table-column>
          <el-table-column prop="targetItem" label="目标数据项"></el-table-column>
      </el-table>
      <br/>
      <div style="text-align: left;margin-left: 10px;">数据项整合（不含时间项）</div>
      <div style="text-align: left;margin-left: 10px;">
      <el-button size="small" style="width: 80px;font-size:15px">添加</el-button>
      <el-button size="small" style="width: 80px;font-size:15px">删除</el-button>
      <el-button size="small" style="width: 80px;font-size:15px">上移</el-button>
      <el-button size="small" style="width: 80px;font-size:15px">下移</el-button></div>

      <el-table :data="data1" border style="width: 90%;margin: auto;">
        <el-table-column prop="name" label="数据项名" ></el-table-column>
        <el-table-column prop="character" label="占位符" ></el-table-column>
        <el-table-column prop="identifier" label="格式标示符"></el-table-column>
        <el-table-column prop="source" label="源格式"></el-table-column>
        <el-table-column prop="target" label="目标格式"></el-table-column>
      </el-table>
    </div>
    </template>

    <template id="table3" v-if="selectedTab === 3">
    <div>
      <el-table :data="data3" border style="top: 90px;width: 90%;margin: auto;">
        <el-table-column prop="item" label="原数据项分隔符" ></el-table-column>
        <el-table-column prop="name" label="原名称值分隔符" ></el-table-column>
        <el-table-column prop="null" label="原空值分隔符"></el-table-column>
      </el-table>
    </div>
    </template>
    <div style="position:absolute;bottom: 15px;margin: auto;right: 0;left: 0;">
    <el-button type="primary" style="width: 80px;margin-top: 30px" @click="Normalizing()">规范化</el-button>
    <el-button type="primary" @click="back" style="width: 80px;margin-top: 30px">取消</el-button></div>

  </div>

</template>

<style lang="less" rel="stylesheet/less" scoped>
  @import "~assets/colors.less";
  @import "~assets/layout.less";
.fram{
  height: 500px;
  width: 600px;
  background-color: white;
  position: relative;
}

  .fade-transition{
    transition: opacity 0.2s ease;
  }

  .fade-enter,.fade-leave{
    opacity:0;
  }

</style>

<script>
  import BaseBox from './BaseBox'
  export default{
      mixins: [BaseBox],
    data() {
          return {
            selectedTab: 1,
            activeIndex: '1',
            data1: [
                {
                    name: '[QC]',
                    character: 'ABCD',
                    identifier: '[Method]',
                    source: 'Incident:A-B-C-D,Plan:C/B/ATD,Task:A/B/CTD,DEFAULT:A-B-CTD',
                    target: 'A-B-CTD'
                }
            ],
            dataTime:[
              {
                sourceItem: '[QC]',
                targetItem: 'Time'
              }
            ],
            data3:[
              {
                  item: '/t',
                  name: ' ',
                  null: ' '
              }
            ]
          }

    },
    components: {


    },
    methods: {
      Normalizing(){
      this.$api({method:'normalize',body:{"rawLogId" : "a77b05d7-298d-45dd-bf15-4d3c756ab02c",
        "formats" :
          [
            {"dataItem":"[QC]",
              "placeholder":"ABCD",
              "identifyItem":"[Method]",
              "oriFormat":
                {
                  "Incident":"A-B-C-D",
                  "Plan":"C/B/ATD",
                  "Task":"A/B/CTD",
                  "DEFAULT":"A-B-CTD"
                },
              "goalFormat":"A-BCTD"},
          ],
        "timeNames":"[QC]",
        "renameOrMergeItems" :
          {
            "EventName":["[Method]", "[Status]"],
            "FKPlanID":["[FKPlanID]"],
            "PKIncidentId":["[PKIncidentId]"],
            "PKTaskID":["[PKTaskID]"],
            "PKPlanID":["[PKPlanID]"],
            "FKIncidentID":["[FKIncidentID]"]
          },
        "oriItemSeparator":"\t",
        "oriNameValSeparator":" ",
        "oriNulVal":""}}).then((res)=>{
        console.log(res);
        if(res.data.code===1){
          this.$hint('规范化成功', 'success');
        }
        else{
          this.$hint('规范化失败', 'warn');
        }

      })
      },
      handleSelect(key, keyPath) {
        this.selectedTab= parseInt(key)
      },
      back(){
          this.commit(true)
      }
    }

  }
</script>
