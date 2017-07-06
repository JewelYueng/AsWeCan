<template>
  <div class="box">
    <i class="el-icon-warning"></i>
    <div>您的账号已被管理员禁用</div>
  </div>
</template>

<style lang="less" rel="stylesheet/less">
.box{
  font-family: 微软雅黑;
  color: red;
  width: 300px;
  height: 200px;
  background-color: white;
  box-shadow: 0 0 3px 0 #324157;
  border-radius: 5px;
i{
  margin-top: 50px;
  margin-bottom: 5px;
  font-size: 30px;
  width: 30px;
  height: 30px;
  color: red;
}
}
</style>

<script>
  import BaseBox from './BaseBox'
  export default{
    mixins: [BaseBox],
    methods: {
      back(){
        this.commit(true)
//        浏览器测试：this.$modal({type:'alert',data: {key: 'q'}}).then((a)=>{console.log(a)})
      }
    }
  }
</script>
