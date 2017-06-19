<template>
  <div class="left-side">
    <div id="logo-box">
      <img src="" alt="Logo">
    </div>
    <div id="username">
      {{username}}
    </div>
    <div class="tabs" v-for="(tab,index) in tab_map">
      <div class="tab" @click="jumpTo(index)" :class="{selectedTab: selected_tab===index}">
        <div class="">{{tab_map[index].name}}</div>
      </div>
    </div>
    <div id="logout">
      <a href="">退出</a>
    </div>
  </div>
</template>

<style lang="less" scoped rel="stylesheet/less">

  @import '~assets/colors.less';
  @import "~assets/layout.less";

  .left-side {
    background-color: @main_green;
    width: @left_side_width;
    height: 800px;
  }

  #username {
    font-size: 24px;
    font-weight: bold;
  }

  .tabs {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: space-between;
    height: 80px;
  }

  .tab {
    font-size: 20px;
    line-height: 45px;
    color: white;
    width: 100%;
    height: 45px;
    cursor: pointer;
  }

  .tab:hover {
    border: 2px solid white;
    border-radius: 5px;
  }

  .selectedTab {
    background-color: #3c735a;
  }
</style>

<script>
  import {mapGetters, mapActions, mapMutations} from 'vuex'
  export default{
    data(){
      return {
        msg: 'leftSide',
        username: '张三',
        selected_tab: 0,
        view_dict: ['loglist', 'merge', 'mining', 'sharelist'],
        tab_map: [
          {
            name: '日志列表',
            path: 'loglist'
          },
          {
            name: '日志融合',
            path: 'merge'
          },
          {
            name: '流程挖掘',
            path: 'mining'
          },
          {
            name: '分享列表',
            path: 'sharelist'
          }]
      }
    },
    created()
    {
      this.$http.post('http://wemeet.tech:8081/team/delete_job/?jobId=1').then(response => {
        console.log(response.data)
      })
//      console.log(typeof this.$api)
    }
    ,
    methods: {
      ...
        mapActions(['jumpView']),
      jumpTo(tabIndex)
      {
        this.selected_tab = tabIndex
        return this.jumpView(`/home/${this.tab_map[tabIndex].path}`)
      }
    }
  }

</script>
