<template>
  <div class="loading">
    <img src="static/img/loading.gif" alt="请等待">
    <div>加载中</div>
  </div>
</template>
<style rel="stylesheet/less" scoped lang="less">
  .loading {
    background-color: white;
    position: fixed;
    left: 50%;
    top: 50%;
    z-index: 11;
    /*设定这个div的margin-top的负值为自身的高度的一半,margin-left的值也是自身的宽度的一半的负值.*/
    /*宽为400,那么margin-top为-200px*/
    /*高为200那么margin-left为-100px;*/
    margin: -100px 0 0 -100px;
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
