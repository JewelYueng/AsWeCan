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
    methods: {
      mixins: [BaseBox],
      back(){
        this.commit(true)
      }
    }
  }
</script>
