<template id="table1">
  <div>
    <br/>
    <div style="text-align: left;margin-left: 10px;">
      <el-button size="small" style="width: 80px;font-size:15px" @click="addBlankRow">添加</el-button>
    </div>
    <el-table :data="format" border style="margin-top: 30px;width: 90%;margin: auto;" max-height="400">
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

<style>

</style>

<script>
  export default {
    data() {
      return {
        editingRow: -1,
        format: [
          {
            name: '[QC]',
            character: 'ABCD',
            identifier: '[Method]',
            source: 'Incident:A-B-C-D,Plan:C/B/ATD,Task:A/B/CTD,DEFAULT:A-B-CTD',
            target: 'A-B-CTD'
          },
        ],

      }
    },
    methods: {
      //      表格的操作函数
      addBlankRow(){
        this.format.push({})
      },
      handleEdit(index, row){
        this.editingRow = index
      },
      saveEdit(index, row){
        this.format[index] = this.editing
        this.editingRow = -1
        this.$emit('SAVE_FORMAT', this.format)
      },
      handleDelete(index, row){
        this.format.splice(index, 1)
      },
      isEditing(index){
        return index === this.editingRow
      },
    },
    computed: {
      editing(){
        return this.editingRow === -1 ? {} : this.format[this.editingRow]
      }
    }
  }
</script>
