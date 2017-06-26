<template id="table3" >
  <div>
    <el-table :data="record" border style="top: 90px;width: 90%;margin: auto;">
      <el-table-column prop="item" label="原数据项分隔符"></el-table-column>
      <el-table-column prop="name" label="原名称值分隔符"></el-table-column>
      <el-table-column prop="null" label="原空值分隔符"></el-table-column>
    </el-table>
  </div>
</template>
<style>

</style>

<script>
  export default {
    data(){
      return {
        record: [
          {
            item: '/t',
            name: ' ',
            null: ' '
          }
        ]
      }
    }
  }
</script>
