<template id="table3" >
  <div>
    <div class="add-btn">
      <el-button size="small" icon="plus" style="width: 80px;font-size:15px" @click="addBlankRow">添加</el-button>
    </div>
    <el-table :data="record" border style="width: 90%;margin: auto;">
      <el-table-column prop="oriItemSeparator" label="原数据项分隔符" width="120" show-overflow-tooltip>
        <template scope="scope">
          <el-input v-model="editing.oriItemSeparator" v-show="isEditing(scope.$index)"></el-input>
          <div v-show="!isEditing(scope.$index)">{{scope.row.oriItemSeparator}}</div>
        </template>
      </el-table-column>
      <el-table-column prop="oriNameValSeparator" label="原名称值分隔符" width="120" show-overflow-tooltip>
        <template scope="scope">
          <el-input v-model="editing.oriNameValSeparator" v-show="isEditing(scope.$index)"></el-input>
          <div v-show="!isEditing(scope.$index)">{{scope.row.oriNameValSeparator}}</div>
        </template>
      </el-table-column>
      <el-table-column prop="oriNameValSeparator" label="原空值分隔符" width="120" show-overflow-tooltip>
        <template scope="scope">
          <el-input v-model="editing.oriNameValSeparator" v-show="isEditing(scope.$index)"></el-input>
          <div v-show="!isEditing(scope.$index)">{{scope.row.oriNameValSeparator}}</div>
        </template>
      </el-table-column>
      <el-table-column>
        <template scope="scope" class="btn-column">
          <el-button
            size="small"
            @click="handleEdit(scope.$index, scope.row)"
            v-show="!isEditing(scope.$index)">编辑
          </el-button>
          <el-button
            size="small"
            @click="saveEdit(scope.$index, scope.row)"
            v-show="isEditing(scope.$index)">保存
          </el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>
<style>

</style>

<script>
  export default {
    data(){
      return {
        editingRow: -1,
        record: [
          {
            oriItemSeparator: '/t',
            oriNameValSeparator: ' ',
            oriNulVal: ' '
          }
        ]
      }
    },
    methods: {

      //      表格的操作函数
      addBlankRow(){
        this.record.push({})
      },
      handleEdit(index, row){
        this.editingRow = index
      },
      saveEdit(index, row){
        this.record[index] = this.editing
        this.editingRow = -1
        this.$emit('SAVE_RECORD', this.record)
      },
      isEditing(index){
        return index === this.editingRow
      },

    },
    computed: {
      editing(){
        return this.editingRow === -1 ? {} : this.record[this.editingRow]
      }
    }
  }
</script>
