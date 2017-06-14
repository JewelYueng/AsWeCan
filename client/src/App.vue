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

<style lang="less" scoped>
  .border {
    border: 1px solid red;
  }

  #app {

    -moz-osx-font-smoothing: grayscale;
    text-align: center;
    color: #2c3e50;
    margin-top: 60px;
  }
</style>
