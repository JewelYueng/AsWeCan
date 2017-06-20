<template>
  <div>
    测试弹出框,key值为{{data.key}}
    <button @click="back"></button>
  </div>
</template>
<style lang="less" scoped rel="stylesheet/less">

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
