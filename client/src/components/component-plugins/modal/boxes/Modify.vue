<template>
  <div class="modify">
    <el-form>

    </el-form>
  </div>
</template>
<style rel="stylesheet/less" scoped lang="less">
  .loading {
    background-color: white;
    border-radius: 20px;
    overflow: hidden;
    height: 200px;
    width: 200px;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    img {
      margin: 30px;
    }
    div {
      font-size: 16px;
      color: #1f2d3d;
    }

  }
</style>
<script>
  import BaseBox from './BaseBox'
  export default{
    mixins: [BaseBox],
  }
</script>
