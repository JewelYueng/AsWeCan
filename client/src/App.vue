<template>
  <div id="app">
    <Home></Home>
    <Modal @show="blurStart" @hide="blurEnd"></Modal>
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
      return {
        isBlur: false
      }
    },
    methods: {
      blurStart(){
        this.isBlur = true
      },
      blurEnd(){
        this.isBlur = false
      }
    }
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
  .blur {
    filter: blur(3px);
  }
</style>
