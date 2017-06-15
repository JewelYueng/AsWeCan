<template>
  <div>
    <LeftSide></LeftSide>

  </div>
</template>

<style>

</style>

<script>
  import LeftSide from 'components/home/LeftSide.vue'
  export default{
    data(){
      return {
        msg: 'leftSide'
      }
    },
    components: {
        LeftSide
    }
  }
</script>
