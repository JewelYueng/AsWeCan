<template>
  <div id="home">
    <div class="header">
      <div class="logo">
        <img src="static/img/logo.png" alt="K2" title="K2流程挖掘平台">
      </div>
      <el-menu theme="dark" :default-active="active_index" class="el-menu-demo" mode="horizontal" router>
        <el-menu-item index="/">日志文件管理</el-menu-item>
        <el-menu-item index="/merge">日志融合</el-menu-item>
        <el-menu-item index="/mining">流程挖掘</el-menu-item>
      </el-menu>
    </div>
    <router-view></router-view>
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

  export default{
    data(){
      return {
        msg: 'leftSide',
      }
    },
    components: {
    },
    computed: {
      active_index: function(){
        return this.$store.getters.home_path || '/'
      }
    }
  }
</script>
