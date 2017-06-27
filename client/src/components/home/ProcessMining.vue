<template>
  <div>
    <div>我是流程挖掘界面</div>
  </div>
</template>
<style>

</style>
<script>
  import {mapActions} from 'vuex'
  export default{
    data(){
      return {
        msg: 'hello vue'
      }
    },
    methods: {
      ...mapActions(['changeHomePath'])
    },
    components: {},
    created(){
      this.changeHomePath('/mining')
      this.$d3.selectAll('p').style('color', 'white');
    }


  }

</script>
