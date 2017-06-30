<template>
  <div id="app">
    <router-view></router-view>
    <Modal></Modal>
    <Hint></Hint>
    <Loading class="loading" v-show="showLoading">Loading</Loading>
  </div>
</template>

<script>
  import Modal from './components/component-plugins/modal/Modal.vue'
  import Hint from './components/component-plugins/hintMessage/HintMessage.vue'
  import Loading from './components/component-plugins/Loading.vue'
  export default {
    name: 'app',
    components: {
       Modal, Hint,Loading
    },
    data() {
      return {
        showLoading: false
      }
    },
    methods: {},
    created(){
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
