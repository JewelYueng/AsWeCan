<template>
  <div class="left-side">

  </div>
</template>

<style lang="less" scoped rel="stylesheet/less">

  @import '~assets/colors.less';

  .left-side{
    background-color: @main_green;
    width: 425px;
    height: 800px;
  }
</style>

<script>
  export default{
      data(){
          return {
              msg: 'leftSide'
          }
      }
  }
</script>
