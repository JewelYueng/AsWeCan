<template id="table2">
  <div>
    <br/>
    <div style="text-align: left;margin-left: 10px;">时间项整合</div>
    <br/>
    <el-table :data="dataTime" border style=
      "margin-top: 20px;width: 90%;margin: auto;">
      <el-table-column prop="sourceItem" label="源数据项" ></el-table-column>
      <el-table-column prop="targetItem" label="目标数据项" ></el-table-column>
    </el-table>
    <br/>
    <div style="text-align: left;margin-left: 10px;">数据项整合（不含时间项）</div>
    <div class="add-btn">
      <el-button size="small" icon="plus" style="width: 80px;font-size:15px" @click="addBlankRow">添加</el-button>

    </div>

    <el-table :data="ui_item" border style=
      "margin-top: 20px;width: 90%;margin: auto;" max-height="200">
      <el-table-column prop="oriName" label="源数据项" >
        <template scope="scope">
          <el-input v-model="editing.oriName" v-show="isEditing(scope.$index)"></el-input>
          <div v-show="!isEditing(scope.$index)">{{scope.row.oriName}}</div>
        </template>
      </el-table-column>
      <el-table-column prop="target" label="目标数据项" >
        <template scope="scope">
          <el-input v-model="editing.target" v-show="isEditing(scope.$index)"></el-input>
          <div v-show="!isEditing(scope.$index)">{{scope.row.target}}</div>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="187px">
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

<style>

</style>

<script>
  export default{
    data(){
      return {
        editingRow: -1,
        integration: [
          {target: "EventName", oriName: ["[Method]", "[Status]"]},
          {target: "FKPlanID", oriName: ["[FKPlanID]"]},
          {target: "PKIncidentId", oriName: ["[PKIncidentId]"]},
          {target: "PKTaskID", oriName: ["[PKTaskID]"]},
          {target: "PKPlanID", oriName: ["[PKPlanID]"]},
          {target: "FKIncidentID", oriName: ["[FKIncidentID]"]},
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
        items_arr.map(i => {
          item[i.target] = i.oriName
        })
        return item
      },
      addBlankRow(){
        this.integration.push({})
      },
      handleEdit(index, row){
        this.editingRow = index
      },
      saveEdit(index, row){

//        this.integration[index].target = this.editing.target
//        this.integration[index].oriName = this.editing.oriName.split(',')
      this.integration[index] = this.editing
        this.editingRow = -1
        this.$emit('SAVE_INTEGRATION', this.resolveData(this.integration))
      },
      handleDelete(index, row){

        if(this.editingRow === -1) {
          this.integration.splice(index, 1)
          this.$emit('SAVE_INTEGRATION', this.resolveData(this.integration))
        }else{
          this.$hint('请保存编辑后再删除', 'warn')
        }


//        this.integration.splice(index, 1)
//        this.$emit('SAVE_INTEGRATION', this.resolveData(this.integration))
      },
      isEditing(index){
        return index === this.editingRow
      },
    },
    computed: {
      editing(){
        return this.editingRow === -1 ? {} : this.ui_item[this.editingRow]
      },
      ui_item(){
        let items = []
        this.integration.map(item => {
          if(item.oriName !== undefined) {
            let resolved_item = {}
            resolved_item.target = item.target
            resolved_item.oriName = item.oriName.join(', ')
            items.push(resolved_item)
          }else{
            items.push(item)
          }
        })
        return items
      }
    }
  }
</script>
