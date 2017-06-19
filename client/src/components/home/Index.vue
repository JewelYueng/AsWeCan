<template>
  <div>
    <LeftSide ></LeftSide>
    <component :is="current_view"></component>
  </div>
</template>

<style>

</style>

<script>
  import LeftSide from 'components/home/LeftSide.vue'
  import LogList from 'components/home/logList/Index'
  import ShareList from 'components/home/sharelist/Index'

  export default{
    data(){
      return {
        msg: 'leftSide',
        view_dict: {
        	loglist: LogList,
          sharelist: ShareList,
//          merge: LogMerge,
//          mining: ProcessMining
        }
      }
    },
    components: {
      LeftSide,
      LogList,
      ShareList,
//      LogMerge,
//      ProcessMining
    },
    computed: {
    	current_view() {
    		return this.view_dict[this.$store.getters.view_level1 || 'loglist']
      }
    }
  }
</script>
