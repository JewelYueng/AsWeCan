<template>
  <div class="log-list">
    <div class="list-tabs">
      <div class="list-tab" v-for="(tab,index) in tab_map" @click="jumpTo(index)"
           :class="{selectedTab: selected_tab===index, separator: index !== tab_map.length - 1}">
        {{tab.name}}
      </div>
    </div>
    <div id="right-down-list">
      <component :is="current_view"></component>
    </div>

  </div>
</template>

<style lang="less" scoped rel="stylesheet/less">
  @import "~assets/colors.less";
  @import "~assets/layout.less";

  .log-list {
    height: 100%;
  }
  .list-tabs {
    display: flex;
    flex-direction: row;
    width: 100%;
    border-radius: 20px 20px 0 0;
    overflow: hidden;
    .list-tab {
      cursor: pointer;
      flex: 1;
      height: 40px;
      line-height: 40px;
      color: @tab_color;
      border-bottom: @tab_separator 2px solid;
      background-color: #f1f1f1;
    }
    .separator {
      border-right: @tab_separator 2px solid;
    }

    .selectedTab {
      background-color: @tab_selected;
    }
  }

  #right-down-list {
    background-color: #f1f1f1;
    height: @main_height - 70px;
  }
</style>

<script>
  import {mapActions} from 'vuex'
  import EventLog from './EventLog.vue'
  import RawLog from './RawLogDetails'
  import NormalLog from './NormalLog'
  export default{
    data(){
      return {
        tab_map: [
          {
            name: '原始日志',
            path: '/home/loglist/raw'
          },
          {
            name: '规范化日志',
            path: '/home/loglist/normal'
          },
          {
            name: '事件日志',
            path: '/home/loglist/event'
          }
        ],
        view_dict: {
          raw: RawLog,
          normal: NormalLog,
          event: EventLog
        },
        selected_tab: 0
      }
    },
    computed: {
      current_view(){
        return this.view_dict[this.$store.getters.view_level2 || 'raw']
      }
    },
    methods: {
      ...mapActions(['jumpView']),
      jumpTo(index){
        this.selected_tab = index
        this.jumpView(this.tab_map[index].path)
      }
    },
    components: {
      EventLog,
      RawLog,
      NormalLog
    }
  }

</script>
