<template>
  <div id="home">
    <div class="header">
      <div class="logo">
        <img src="static/img/logo.png" alt="K2" title="K2流程挖掘平台">
      </div>
      <el-menu theme="dark" :default-active="activeIndex" class="el-menu-demo" mode="horizontal" router>
        <el-menu-item index="/">日志文件管理</el-menu-item>
        <el-menu-item index="/merge">日志融合</el-menu-item>
        <el-menu-item index="/mining">流程挖掘</el-menu-item>
      </el-menu>
    </div>
    <router-view name="home"></router-view>
  </div>
</template>

<style rel="stylesheet/less" lang="less" scoped>
  @import "~assets/layout.less";
  @import "~assets/colors.less";

  .header {
    background-color: @dark_theme;
    width: 100%;
    box-sizing: border-box;
    display: flex;
    flex-direction: row;
  }

  .logo {
    padding: 4px;
    width: @left_side_width;
    img {
      width: 60px;
    }
  }


</style>

<script>
  import LeftSide from 'components/home/LeftSide.vue'
  import LogList from 'components/home/logList/Index'
  import ShareList from 'components/home/sharelist/Index'
  import LogMerge from 'components/home/Merge'
  import MergeResult from 'components/home/MergeResult'
  import ProcessMining from 'components/home/ProcessMining'


  export default{
    data(){
      return {
        msg: 'leftSide',
        activeIndex: '/',
        view_dict: {
          loglist: LogList,
          sharelist: ShareList,
          merge: LogMerge,
          mining: ProcessMining,
          mergeresult: MergeResult
        }
      }
    },
    components: {
      LeftSide,
      LogList,
      ShareList,
      LogMerge,
      ProcessMining,
      MergeResult
    },
    computed: {
      current_view() {
        return this.view_dict[this.$store.getters.view_level1 || 'loglist']
      }
    }
  }
</script>
