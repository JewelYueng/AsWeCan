<template>
  <div class="left-side">
    <div id="logo-box">
      <img src="static/img/logo.png" alt="Logo">
    </div>
    <div id="username" @click='modifyInfo'>
      {{username}}
    </div>
    <div class="tabs">
      <div class="tab" v-for="(tab,index) in tab_map" @click="jumpTo(index)" :class="{selectedTab: selected_tab===index}" >
        <div class="text">
          <div class="decoration">-</div>
          {{tab_map[index].name}}
          <div class="decoration">-</div>
        </div>
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
    height: @main_height;
    padding: 50px 0;
    box-sizing: border-box;
  }

  #username {
    font-size: 24px;
    font-weight: bold;
    margin: 20px 0;
    cursor: pointer;
  }

  #logo-box {
    margin: 20px auto;
    img {
      width: 200px;
    }
  }

  .tabs {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: space-between;
    height: 250px;
  }

  .tab {
    font-size: 20px;
    line-height: 45px;
    color: white;
    width: 100%;
    height: 45px;
    display: flex;
    flex-direction: row;
    align-items: center;
    justify-content: center;
  }

  .text {
    width: 80%;
    cursor: pointer;
    display: flex;
    flex-direction: row;
    align-items: center;
    justify-content: center;
    .decoration {
      margin: 0 10px;
    }
  }

  .text:hover {
    border: 2px solid white;
    border-radius: 5px;
  }

  .selectedTab {
    background-color: #3c735a;
  }
</style>

<script>
  import {mapActions} from 'vuex'
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
    created() {
      this.$http.post('http://wemeet.tech:8081/team/delete_job/?jobId=1').then(response => {
        console.log(response.data)
      })
//      console.log(typeof this.$api)
    },
    methods: {
      ...
        mapActions(['jumpView']),
      jumpTo(tabIndex) {
        this.selected_tab = tabIndex
        return this.jumpView('/home/' + this.tab_map[tabIndex].path)
      },
      modifyInfo() {
        console.log('modify')
      }
    }
  }

</script>
