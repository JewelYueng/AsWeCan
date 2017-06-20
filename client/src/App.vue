<template>
  <div id="app">
    <Home></Home>
    <Modal></Modal>
  </div>
</template>

<script>
  import Home from './components/home/Index.vue'
  import Modal from './components/component-plugins/modal/Modal.vue'
  export default {
    name: 'app',
    components: {
      Home, Modal
    },
    data() {
      return {}
    },
    methods: {}
  }
</script>

<style lang="less" scoped rel="stylesheet/less">
  @import './assets/layout.less';

  .border {
    border: 1px solid red;
  }

  #app {
    -moz-osx-font-smoothing: grayscale;
    text-align: center;
    width: @major_width;
    margin-left: auto;
    margin-right: auto;

  }
</style>
