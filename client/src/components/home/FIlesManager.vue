<template>
  <div class="files-list">
    <div class="left-side">
      <el-menu :default-active="current_index" class="el-menu-vertical-demo" @select="handleSelect">
        <el-submenu index="1">
          <template slot="title"><i class="el-icon-message"></i>我的日志</template>
          <el-menu-item index="1-1">原始日志</el-menu-item>
          <el-menu-item index="1-2">规范化日志</el-menu-item>
          <el-menu-item index="1-3">事件日志</el-menu-item>
        </el-submenu>
        <el-submenu index="2">
          <template slot="title"><i class="el-icon-message"></i>分享列表</template>
          <el-menu-item index="2-1">原始日志</el-menu-item>
          <el-menu-item index="2-2">规范化日志</el-menu-item>
          <el-menu-item index="2-3">事件日志</el-menu-item>
        </el-submenu>
      </el-menu>
    </div>
    <div id="right-window">
      <component :is="current_view"></component>
    </div>
  </div>
</template>

<style lang="less" rel="stylesheet/less">
  @import '~assets/colors.less';
  @import "~assets/layout.less";

  .left-side {
    background-color: @light_theme;
    width: @left_side_width;
    height: @main_height;
    box-sizing: border-box;
  }

  .files-list {
    display: flex;
    flex-direction: row;
  }

  #right-window {
    width: @right_side_width;
    height: @main_height - 30px;
    box-sizing: border-box;
    padding: 30px 30px 0 30px;
    overflow: auto;
  }
  .relation-logs{
    cursor: pointer;
  }
</style>

<script>
  import MyRawLog from 'components/home/logList/RawLog'
  import MyNormalLog from 'components/home/logList/NormalLog'
  import MyEventLog from 'components/home/logList/EventLog'
  import ShareRawLog from 'components/home/shareList/RawLogDetail'
  import ShareNormalLog from 'components/home/shareList/NormalLogDetail'
  import ShareEventLog from 'components/home/shareList/EventLogDetail'

  import {mapActions} from 'vuex'

  export default{
    data(){
      return {
        msg: 'leftSide',
        active_index: '1-1',
        view_dict: {
          "1-1": MyRawLog,
          "1-2": MyNormalLog,
          "1-3": MyEventLog,
          "2-1": ShareRawLog,
          "2-2": ShareNormalLog,
          "2-3": ShareEventLog
        }
      }
    },
    components: {
      MyRawLog,
      MyNormalLog,
      MyEventLog,
      ShareRawLog,
      ShareNormalLog,
      ShareEventLog
    },
    created(){
      this.changeHomePath('/')
    },
    methods: {
      ...mapActions(['changeHomePath','changeFilePath', 'selectLog']),
      handleSelect(key, keyPath) {
        this.active_index = key
        this.changeFilePath(key)
      }
    },
    computed: {
      current_view() {
        return this.view_dict[this.$store.getters.file_path || '1-1']
      },
      current_index(){
        return this.$store.getters.file_path
      }
    }
  }
</script>
