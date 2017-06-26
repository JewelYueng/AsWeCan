<template>
  <div class="fram">
    <button @click="back" style="position: absolute;right: 0px;
       background-color: indianred;z-index: 100;border: none;">x
    </button>
    <el-menu :default-active="activeIndex" class="tabChoose" mode="horizontal" @select="handleSelect">
      <el-menu-item index="1">数据项格式配置</el-menu-item>
      <el-menu-item index="2">数据项整合配置</el-menu-item>
      <el-menu-item index="3">记录格式配置</el-menu-item>
    </el-menu>

    <template id="table1" v-if="selectedTab === 1">
      <div>
        <br/>
        <div style="text-align: left;margin-left: 10px;">
          <el-button size="small" style="width: 80px;font-size:15px" @click="addBlankRow">添加</el-button>
        </div>
        <el-table :data="data1" border style="margin-top: 30px;width: 90%;margin: auto;" max-height="400">
          <el-table-column prop="name" label="数据项名">
            <template scope="scope">
              <el-input v-model="editing.name" v-show="isEditing(scope.$index)"></el-input>
              <div v-show="!isEditing(scope.$index)">{{scope.row.name}}</div>
            </template>
          </el-table-column>
          <el-table-column prop="character" label="占位符">
            <template scope="scope">
              <el-input v-model="editing.character" v-show="isEditing(scope.$index)"></el-input>
              <div v-show="!isEditing(scope.$index)">{{scope.row.name}}</div>
            </template>
          </el-table-column>
          <el-table-column prop="identifier" label="格式标示符">
            <template scope="scope">
              <el-input v-model="editing.identifier" v-show="isEditing(scope.$index)"></el-input>
              <div v-show="!isEditing(scope.$index)">{{scope.row.identifier}}</div>
            </template>
          </el-table-column>
          <el-table-column prop="source" label="源格式">
            <template scope="scope">
              <el-input type="textarea" :row="2" v-model="editing.source" v-show="isEditing(scope.$index)"></el-input>
              <div v-show="!isEditing(scope.$index)">{{scope.row.source}}</div>
            </template>
          </el-table-column>
          <el-table-column prop="target" label="目标格式">
            <template scope="scope">
              <el-input v-model="editing.target" v-show="isEditing(scope.$index)"></el-input>
              <div v-show="!isEditing(scope.$index)">{{scope.row.target}}</div>
            </template>
          </el-table-column>
          <el-table-column
            label="操作"
            width="140">
            <template scope="scope" class="btn-column">
              <el-button
                size="small"
                @click="saveEdit(scope.$index, scope.row)"
                v-show="isEditing(scope.$index)">保存
              </el-button>
              <el-button
                size="small"
                @click="handleEdit(scope.$index, scope.row)"
                v-show="!isEditing(scope.$index)">编辑
              </el-button>
              <el-button
                size="small"
                type="danger"
                @click="handleDelete(scope.$index, scope.row)">删除
              </el-button>
            </template>
          </el-table-column>
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
          <el-button size="small" style="width: 80px;font-size:15px">下移</el-button>
        </div>

        <el-table :data="dataTime" border style=
          "margin-top: 20px;width: 90%;margin: auto;">
          <el-table-column prop="sourceItem" label="源数据项"></el-table-column>
          <el-table-column prop="targetItem" label="目标数据项"></el-table-column>
        </el-table>
      </div>
    </template>

    <template id="table3" v-if="selectedTab === 3">
      <div>
        <el-table :data="data3" border style="top: 90px;width: 90%;margin: auto;">
          <el-table-column prop="item" label="原数据项分隔符"></el-table-column>
          <el-table-column prop="name" label="原名称值分隔符"></el-table-column>
          <el-table-column prop="null" label="原空值分隔符"></el-table-column>
        </el-table>
      </div>
    </template>
    <div style="position:absolute;bottom: 15px;margin: auto;right: 0;left: 0;">
      <el-button type="primary" style="width: 80px;margin-top: 30px" @click="Normalizing()">规范化</el-button>
      <el-button type="primary" @click="back" style="width: 80px;margin-top: 30px">取消</el-button>
    </div>

  </div>

</template>

<style lang="less" rel="stylesheet/less" scoped>
  @import "~assets/colors.less";
  @import "~assets/layout.less";

  .fram {
    height: 600px;
    width: 700px;
    background-color: white;
    position: relative;
  }

  .fade-transition {
    transition: opacity 0.2s ease;
  }

  .fade-enter, .fade-leave {
    opacity: 0;
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
        editingRow: -1,
        data1: [
          {
            name: '[QC]',
            character: 'ABCD',
            identifier: '[Method]',
            source: 'Incident:A-B-C-D,Plan:C/B/ATD,Task:A/B/CTD,DEFAULT:A-B-CTD',
            target: 'A-B-CTD'
          },
        ],
        dataTime: [
          {
            sourceItem: '[QC]',
            targetItem: 'Time'
          }
        ],
        data2: [
          {oriName: "EventName", target: ["[Method]", "[Status]"]},
          {oriName: "FKPlanID", target: ["[FKPlanID]"]},
          {oriName: "PKIncidentId", target: ["[PKIncidentId]"]},
          {oriName: "PKTaskID", target: ["[PKTaskID]"]},
          {oriName: "PKPlanID", target: ["[PKPlanID]"]},
          {oriName: "FKIncidentID", target: ["[FKIncidentID]"]}
        ],
        data3: [
          {
            item: '/t',
            name: ' ',
            null: ' '
          }
        ]
      }

    },
    components: {},
    methods: {
//      表格的操作函数
      addBlankRow(){
        this.data1.push({})
      },
      handleEdit(index, row){
        this.editingRow = index
      },
      saveEdit(index, row){
        this.data1[index] = this.editing
        this.editingRow = -1
      },
      handleDelete(index, row){
        this.data1.splice(index, 1)
      },
      isEditing(index){
        return index === this.editingRow
      },
      Normalizing(){
        this.$api({
          method: 'normalize', body: {
            "rawLogId": "a77b05d7-298d-45dd-bf15-4d3c756ab02c",
            "formats": [
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
            "timeNames": "[QC]",
            "renameOrMergeItems": {
              "EventName": ["[Method]", "[Status]"],
              "FKPlanID": ["[FKPlanID]"],
              "PKIncidentId": ["[PKIncidentId]"],
              "PKTaskID": ["[PKTaskID]"],
              "PKPlanID": ["[PKPlanID]"],
              "FKIncidentID": ["[FKIncidentID]"]
            },
            "oriItemSeparator": "\t",
            "oriNameValSeparator": " ",
            "oriNulVal": ""
          }
        }).then((res) => {
          console.log(res);
          if (res.data.code === 1) {
            this.$hint('规范化成功', 'success');
          }
          else {
            this.$hint('规范化失败', 'warn');
          }

        })
      },
      handleSelect(key, keyPath) {
        this.selectedTab = parseInt(key)
      },
      back(){
        this.commit(true)
      },
    },
    computed: {
      editing(){
        return this.editingRow === -1 ? {} : this.data1[this.editingRow]
      },

    }
  }
</script>
