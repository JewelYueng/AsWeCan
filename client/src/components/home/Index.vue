<template>
  <div id="home">
    <LeftSide ></LeftSide>
    <div id="right-window">
      <component :is="current_view"></component>
    </div>
  </div>
</template>

<style rel="stylesheet/less" lang="less" scoped>
  @import "~assets/layout.less";
  #home{
    display: flex;
    flex-direction: row;
  }
  #right-window{
    width: @right_side_width;
    height: @main_height;
    box-sizing: border-box;
    padding: 30px 30px 0 30px;
    overflow: auto;
  }
</style>

<script>
  import LeftSide from 'components/home/LeftSide.vue'
  import LogList from 'components/home/logList/Index'
  import ShareList from 'components/home/sharelist/Index'
  import LogMerge from 'components/home/MergeResult'
  import ProcessMining from 'components/home/ProcessMining'


  export default{
    data(){
      return {
        msg: 'leftSide',
        view_dict: {
        	loglist: LogList,
          sharelist: ShareList,
          merge: LogMerge,
          mining: ProcessMining
        }
      }
    },
    components: {
      LeftSide,
      LogList,
      ShareList,
      LogMerge,
      ProcessMining
    },
    computed: {
    	current_view() {
    		return this.view_dict[this.$store.getters.view_level1 || 'loglist']
      }
    }
  }
</script>
