<template>
  <div class="diagram-list">
    <div class="left-side">
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
    <div id="right-window">
      <component :is="current_view" :sankey="sankey" :petri="petri" :produce="produce" :resource="resource"></component>
    </div>
  </div>
</template>

<style lang="less" rel="stylesheet/less">
  @import '~assets/colors.less';
  @import "~assets/layout.less";

  .left-side {
    background-color: @light_theme;
    width: @left_side_width;
    height: @main_height;
    box-sizing: border-box;
  }

  .diagram-list {
    display: flex;
    flex-direction: row;
  }

  #right-window {
    width: @right_side_width;
    height: @main_height - 30px;
    box-sizing: border-box;
    padding: 30px 30px 0 30px;
    overflow: auto;
  }
  .relation-logs{
    cursor: pointer;
  }
</style>

<script>
  import ResourceRelation from './ResourceRelation.vue'
  import Sankey from "./Sankey.vue"
  import WorkNet from "./WorkNet.vue"
  import _ from 'lodash'
  import {mapActions} from 'vuex'

  export default{
    data(){
      return {
        msg: 'leftSide',
        active_index: '1',
        view_dict: {
          "1":ResourceRelation,
          //"2-1":
          "2-2": WorkNet,
          "3": Sankey
        },
        sankey: {},
        petri: {},
        resource: {},
        produce: {}
      }
    },
    components: {
        Sankey,
      ResourceRelation
    },
    created(){
      this.changeHomePath('/mining')
      this.resource = this.$route.params.resource_data
      this.getDiagramData('PetriNet')
      this.getDiagramData('Sankey')
      this.getDiagramData('TransitionSystem')

    },
    methods: {
      ...mapActions(['changeHomePath','changeDiagramPath']),
      handleSelect(key, keyPath) {
        this.active_index = key
        this.changeDiagramPath(key)
      },
      getDiagramData(diagram_type){
        const diagram_dict = {
          'PetriNet': 'petri',
          'Sankey': 'sankey',
          'TransitionSystem': 'produce',
        }
        let mining_params = this.$route.params.raw_data
        this.$api({
          method: 'mining',
          body: _.extend({
            diagramType: diagram_type,
          },mining_params)
        }).then( res => {
          if (res.status === 200) {
            this[diagram_dict[diagram_type]] = res.data.diagram
          }}, err => {
          this.$hint('网络出错，请刷新再试','warn')
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
    }
  }
</script>
