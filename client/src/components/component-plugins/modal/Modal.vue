<template>
  <div id="modal" v-if="show">
    <div class="mask"></div>
    <transition-group name="list" tag="div" appear>
      <div class="modal-container list-item" :key="box.id" v-for="box in boxes">
        <component :is="box.type" :data="box.data" :onCommit="box.onCommit"></component>
      </div>
    </transition-group>
  </div>
</template>

<style lang="less" rel="stylesheet/less" scoped>
  #modal {
    /*position: fixed;*/
    position: absolute;
    top: 0;
    left: 0;
    height: 100%;
    width: 100%;
    overflow: hidden;
  }

  .modal-container {
    position: absolute;
    height: 100%;
    width: 100%;

    display: flex;
    align-items: center;
    justify-content: center;

    overflow: auto;
  }

  .mask {
    position: absolute;
    height: 100%;
    width: 100%;
    background-color: black;
    opacity: 0.3;
  }

  .pointer-none {
    pointer-events: none;
  }

  .fade-enter-active, .fade-leave-active {
    transition: opacity 1s
  }

  .fade-enter, .fade-leave-active {
    opacity: 0;
    pointer-events: none;
  }

  .list-item {
    margin-right: 10px;
  }

  .list-enter-active, .list-leave-active {
    transition: all .3s;
  }

  .list-enter {
    opacity: 0;
    transform: translateY(30px);
  }

  .list-leave-active {
    opacity: 0;
    transform: translateY(-30px);
  }
</style>

<script>
  import Alert from './boxes/Alert.vue'
  import { register } from './modal'
  export default {
    components: {
      Alert
    },
    data(){
      return {
        show: false,
        boxes: [],
        id: 0
      }
    },
    created(){
      register(this)
      window.$modal = this.modal
    },
    methods: {
      modal ({type, data}) {
        const id = this.id++
        let __onCommit__
        const promise = new Promise(resolve => {
          __onCommit__ = data => {
            const _id = id
            resolve(data)
            this.boxes = this.boxes.filter(({id}) => id !== _id)
          }
          this.boxes.push({
            id, type, data, onCommit: __onCommit__
          })
        })
        promise.commit = __onCommit__
        return promise
      }
    },
    watch: {
      boxes(val) {
        val.length > 0
          ? this.$nextTick(() => {
          this.show = true
        })
          : this.$nextTick(() => {
          this.show = false
        })

      }
    }

  }
</script>
