<template>
  <div id="home">
    <LeftSide></LeftSide>
    <RawLogDetails></RawLogDetails>
  </div>
</template>

<style>
  #home{
    display: flex;
    flex-direction: row;
  }
</style>

<script>
  import LeftSide from 'components/home/LeftSide.vue'
  import RawLogDetails from 'components/home/RawLogDetails.vue'
  export default{
    data(){
      return {
        msg: 'leftSide'
      }
    },
    components: {
        LeftSide,RawLogDetails
    }
  }
</script>
