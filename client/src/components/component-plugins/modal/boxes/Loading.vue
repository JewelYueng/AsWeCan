<template>
  <div class="loading">
    <img src="static/img/loading.gif" alt="请等待">
  </div>
</template>
<style rel="stylesheet/less" scoped lang="less">
  .loading{
    background-color: white;
    border-radius: 20px;
    overflow: hidden;
  }
</style>
<script>
  import BaseBox from './BaseBox'
  export default{
    mixins: [BaseBox]
  }
</script>
