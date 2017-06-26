<template>
  <div id="hint-msg" @click ='hide'>
    <transition name="slide">
      <div class="hint-container" v-show="is_showing" :class="[msg_type]">
        <span>{{show_message}}</span>
      </div>
    </transition>
  </div>
</template>
<style lang="less" scoped rel="stylesheet/less">
  @import "~assets/layout.less";
  #hint-msg{
    cursor: pointer;
    position: fixed;
    top: 0;
    text-align: center;
    left: @major_width * 0.33;;
    width: @major_width * 0.33;
    opacity: .95;
    z-index: 999;
  }

  .hint-container {
    position: relative;
    top: 0;
    height: 30px;
    line-height: 30px;
    margin: 0 auto;
    border-radius: 5px;
    background: #337ab7;
    color: white;
  }

  .success {
    background: #5cb85c;
  }

  .warn {
    background: #f0ad4e;
  }

  .error {
    background: #d9534f;
  }

  .slide-enter-active, .slide-leave-active {
    transition: all 1s;
  }

  .slide-enter, .slide-leave-active {
    transform: translateY(-30px);
  }
</style>
<script>
  import { register } from './hint'
  export default {
    data(){
      return {
        is_showing: false,
        show_message: '',
        msg_type: '',
        timeout_id: null
      }
    },
    created(){
      register(this)
      window.$hint = (...args) => this.hint(...args)
    },
    methods: {
      hint(msg, type = '') {
        this.is_showing = true
        this.show_message = msg
        this.msg_type = type
        if(this.timeout_id){
          clearTimeout(this.timeout_id)
        }
        this.timeout_id = setTimeout(() => {
          this.is_showing = false
        }, 1500)
      },
      hide(){
        this.is_showing = false
      }
    }
  }
</script>
