<template>
  <div id="app">
    <router-view></router-view>
    <Modal></Modal>
    <Hint></Hint>
  </div>
</template>

<script>
  import Modal from './components/component-plugins/modal/Modal.vue'
  import Hint from './components/component-plugins/hintMessage/HintMessage.vue'
  export default {
    name: 'app',
    components: {
       Modal, Hint
    },
    data() {
      return {
      }
    },
    methods: {},
    created(){
//      $api定义全局动作
      this.$api.onEach('request', request => {
        window.is_requesting = true
        setTimeout( () => {
          if(window.is_requesting === true && !window.loading_modal)
            window.loading_modal = window.$modal({type: 'loading'})
        }, 1000)
      })
      this.$api.onEach('response', response => {
        window.is_requesting = false
        if (window.loading_modal && ('commit' in window.loading_modal)) {
          window.loading_modal.commit()
          window.loading_modal = null
        }
      })
    }
  }
</script>

<style lang="less" rel="stylesheet/less">
  @import './assets/layout.less';

  .border {
    border: 1px solid red;
  }

  #app {
    -moz-osx-font-smoothing: grayscale;
    text-align: center;
    width: 100%;
    margin-left: auto;
    margin-right: auto;

  }

  * {
    font-family: "Helvetica Neue",Helvetica,"PingFang SC","Hiragino Sans GB","Microsoft YaHei","微软雅黑",Arial,sans-serif;
  }
</style>
