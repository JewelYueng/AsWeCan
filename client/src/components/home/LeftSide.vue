<template>
  <div class="left-side">

  </div>
</template>

<style lang="less" scoped rel="stylesheet/less">

  @import '~assets/colors.less';

  .left-side {
    background-color: @main_green;
    width: 425px;
    height: 800px;
  }
</style>

<script>
  export default{
    data(){
      return {
        msg: 'leftSide',
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
