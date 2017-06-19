<template>
  <div id="app">
    <Home></Home>
  </div>
</template>

<script>
  import Home from './components/home/Index.vue'
  export default {
    name: 'app',
    components: {
        Home
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
</style>
