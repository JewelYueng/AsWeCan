<template>
  <div class="left-side">
    <div id="logo-box">
      <img src="" alt="Logo">
    </div>
    <div id="username">
      {{username}}
    </div>
    <div class="tabs">
      <div class="tab">用户列表</div>
      <div class="tab">日志列表</div>
      <div class="tab">分享列表</div>
      <div class="tab">算法列表</div>
    </div>
    <div id="logout">
      <a href="">退出</a>
    </div>
  </div>
</template>

<style lang="less" scoped rel="stylesheet/less">

  @import '~assets/colors.less';
  @import "~assets/layout.less";

  .left-side {
    background-color: @main_green;
    width: @left_side_width;
    height: 800px;
  }
  #username{
    font-size: 24px;
    font-weight:bold;
  }
  .tab{
    margin: 0 auto;
    font-size: 20px;
    line-height: 45px;
    color: white;
    width: 170px;
    height: 45px;
    cursor: pointer;
  }
  .tab:hover{
    border: 2px solid white;
    border-radius: 5px;
  }
  .selected-tab{
    background-color: #3c735a;
  }
</style>

<script>
  export default{
    data(){
      return {
        msg: 'leftSide',
        username: '张三'
      }
    },
    created() {
    	this.$http.post('http://wemeet.tech:8081/team/delete_job/?jobId=1').then(response => {
    		console.log(response.data)
      })
//      console.log(typeof this.$api)
    }
  }
</script>
