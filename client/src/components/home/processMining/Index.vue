<template>
  <div class="diagram-list">
    <transition name="slide">
      <div class="left-side" v-show="is_showing">
        <el-menu :default-active="current_index" class="el-menu-vertical-demo" @select="handleSelect">
          <el-menu-item index="1">力导向图</el-menu-item>
          <el-submenu index="2">
            <template slot="title">流程模型</template>
            <el-menu-item index="2-1">流程图</el-menu-item>
            <el-menu-item index="2-2">工作流图</el-menu-item>
          </el-submenu>
          <el-menu-item index="3">桑基图</el-menu-item>
        </el-menu>
      </div>
    </transition>
    <div id="right-window">
      <div @click="hideLeft" class="hide"><i class="el-icon-d-arrow-left"></i></div>
      <component :is="current_view" :sankey="sankey" :petri="petri" :produce="produce" :resource="resource" class="svg-div"></component>
    </div>
  </div>
</template>

<style lang="less" rel="stylesheet/less">
  @import '~assets/colors.less';
  @import "~assets/layout.less";

  .left-side {
    background-color: @light_theme;
    width: 200px;
    height: @main_height;
    box-sizing: border-box;
  }

  .diagram-list {
    display: flex;
    flex-direction: row;
  }

  #right-window {
    flex: 1 1 @right_side_width;
    height: @main_height - 30px;
    box-sizing: border-box;
    padding: 30px 30px 0 30px;
    overflow: auto;
    display: flex;
    flex-direction: row;
    justify-content: flex-start;
    .hide{
      cursor: pointer;
      flex: 0 0 20px;
      font-size: 16px;
      display: flex;
      flex-direction: row;
      align-items: center;
      justify-content: center;
    }
    .svg-div{
      flex: 1;
    }
  }

  .relation-logs {
    cursor: pointer;
  }

  .slide-enter-active, .slide-leave-active {
    transition: all 1s ease;
  }

  .slide-enter, .slide-leave-active {
    transform: translateX(-200px);
  }
</style>

<script>
  import ResourceRelation from './ResourceRelation.vue'
  import Sankey from "./Sankey.vue"
  import WorkNet from "./WorkNet.vue"
  import TransitionSystem from "./TransitionSystem.vue"
  import _ from 'lodash'
  import {mapActions} from 'vuex'

  export default{
    data(){
      return {
        msg: 'leftSide',
        active_index: '1',
        view_dict: {
          "1": ResourceRelation,
          "2-1": TransitionSystem,
          "2-2": WorkNet,
          "3": Sankey
        },
        sankey: {},
        petri: {},
        resource: {},
        produce: {},
        is_showing: true
      }
    },
    components: {
      Sankey,
      ResourceRelation
    },
    created(){
      this.changeHomePath('/mining')
      this.resource = this.resource_data
      this.getDiagramData('PetriNet')
      this.getDiagramData('Sankey')
      this.getDiagramData('TransitionSystem')
    },
    destroyed(){
      this.changeDiagramPath('1')
    },
    props: ['resource_data', 'raw_data'],
    methods: {
      ...mapActions(['changeHomePath', 'changeDiagramPath']),
      handleSelect(key, keyPath) {
        this.active_index = key
        this.changeDiagramPath(key)
      },
      hideLeft(){
        this.is_showing = !this.is_showing
      },
      getDiagramData(diagram_type){
        const diagram_dict = {
          'PetriNet': 'petri',
          'Sankey': 'sankey',
          'TransitionSystem': 'produce',
          'ResourceRelation': 'resource'
        }
        let mining_params = this.raw_data
        this.$api({
          method: 'mining',
          body: _.extend({
            diagramType: diagram_type,
          }, mining_params)
        }).then(res => {
          if (res.status === 200) {
            this[diagram_dict[diagram_type]] = res.data.diagram
          }
        }, err => {
          console.log(err)
          this.$hint(err.data.msg, 'error')
        })
      }
    },
    computed: {
      current_view() {
        return this.view_dict[this.current_index || '1']
      },
      current_index(){
        return this.$store.getters.diagram_path
      }
    },
  }
</script>
