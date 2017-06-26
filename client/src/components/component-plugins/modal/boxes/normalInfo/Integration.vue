<template id="table2">
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

    <el-table :data="integration" border style=
      "margin-top: 20px;width: 90%;margin: auto;">
      <el-table-column prop="oriName" label="源数据项"></el-table-column>
      <el-table-column prop="target" label="目标数据项"></el-table-column>
    </el-table>
  </div>
</template>

<style>

</style>

<script>
  export default{
    data(){
      return {
        integration: [
          {target: "EventName", oriName: ["[Method]", "[Status]"]},
          {target: "FKPlanID", oriName: ["[FKPlanID]"]},
          {target: "PKIncidentId", oriName: ["[PKIncidentId]"]},
          {target: "PKTaskID", oriName: ["[PKTaskID]"]},
          {target: "PKPlanID", oriName: ["[PKPlanID]"]},
          {target: "FKIncidentID", oriName: ["[FKIncidentID]"]}
        ],
        dataTime: [
          {
            sourceItem: '[QC]',
            targetItem: 'Time'
          }
        ]
      }
    },
    methods: {
      resolveData(items_arr){
        let item = {}
        items_arr.map( i => {
          item[i.target] = i.oriName
        })
        return item
      },
      addBlankRow(){
        this.format.push({})
      },
      handleEdit(index, row){
        this.editingRow = index
      },
      saveEdit(index, row){
        this.integration[index] = this.editing
        this.editingRow = -1
        this.$emit('SAVE_INTEGRATION', this.resolveData(this.integration))
      },
      handleDelete(index, row){
        this.format.splice(index, 1)
      },
      isEditing(index){
        return index === this.editingRow
      },
    }
  }
</script>
