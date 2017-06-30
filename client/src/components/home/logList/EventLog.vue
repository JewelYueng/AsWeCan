<template>
  <div class="event-log-details">
    <div class="head">
      <div class="button" @click="upload"><img src="static/img/upload.png">上传</div>
      <div class="button" @click="shareSome"><img src="static/img/share_white.png">分享</div>
      <div class="button" @click="deleteSome"><img src="static/img/Delete.png" style="padding-top: 2px">删除</div>
      <input type="text" id="search" placeholder="请输入关键字" v-model="keyWord">
      <div v-show="isSearching" class="img-button close-btn" @click="close_search">
        <i class="el-icon-circle-cross"></i>
      </div>
      <img v-show="!isSearching" id="search_button" src="static/img/search.png" @click="searchLog">
    </div>
    <div class='title'>
      <span class='title_left'>全部文件，共{{count}}个，已选{{amount}}个</span>
      <span class='title_right'>关联文件</span>
    </div>
    <div id="log-list">
      <div class="list">
        <div class="log-head">
          <input type="checkbox" v-model="checkAll" id="文件名" value="文件名">
          <span class="log-name">文件名</span>
        </div>
        <div class="operations"></div>
        <div class="date">日期</div>
        <div class="raw-log">原始日志</div>
        <div class="normal-log">规范化日志</div>
        <div class="merge-relation">融合来源</div>
      </div>
      <div class="list" v-for="(item,index) in items" :class="{selectedItem: isSelected(index)}">
        <div class="input-box log-head">
          <input type="checkbox" v-model="checked" :value="item.eventLog.id" @click="currClick(item,index)">
          <span @click="showDetail(index)" class="log-name" :title="item.eventLog.logName">{{item.eventLog.logName}}</span>
        </div>
        <div class="operations">
          <img class="process_button img-button" title="开始流程挖掘" v-on:click="processMining(index)"
               src="static/img/process_color.png">
          <img class="download_button img-button" title="下载" src="static/img/download_color.png"
               @click="download(index)">
          <img class="share_button img-button" title="分享"
               :src="item.eventLog.isShared === 0 ? 'static/img/share_color.png' : 'static/img/forbidden_color.png'"
               @click="share(index)">
          <img class="delete_button img-button" src="static/img/Delete_color.png" alt="删除" title="删除"
               @click="deleteLog(index)">
        </div>
        <div class="date">
          {{`${new Date(item.eventLog.createDate).getFullYear()}-${new Date(item.eventLog.createDate).getMonth() + 1}-${new Date(item.eventLog.createDate).getDate()}`}}
        </div>
        <div @click="jumpToRaw(index)" class="relation-logs raw-log" :title="item.rawLog ? item.rawLog.logName : '无'">
          {{item.rawLog ? item.rawLog.logName : '无'}}
        </div>
        <div @click="jumpToNormal(index)" class="relation-logs normal-log" :title="item.normalLog ? item.normalLog.logName : '无'">
          {{item.normalLog ? item.normalLog.logName : '无'}}
        </div>
        <div class="merge-relation">
          <div v-if="item.eventLog.mergeRelation" class="relation1" @click="selectedRel(index,0)" :title="item.eventLog.mergeRelation.split(',')[0]">{{item.eventLog.mergeRelation.split(',')[0]}}</div>
          <div v-if="item.eventLog.mergeRelation" class="relation2" @click="selectedRel(index,1)" :title="item.eventLog.mergeRelation.split(',')[1]">{{item.eventLog.mergeRelation.split(',')[1]}}</div>
          <div v-show="!item.eventLog.mergeRelation">没有融合来源</div>
        </div>
      </div>
    </div>
  </div>
</template>

<style lang="less" scoped rel="stylesheet/less">
  @import '~assets/colors.less';
  @import "~assets/layout.less";

  .head {
    display: flex;
    flex-direction: row;
    justify-content: space-around;
    position: relative;
  }

  .event-log-details {
    padding-top: 20px;
  }

  #search {
    margin-left: 300px;
    background-color: @light_theme;
    color: @dark_theme;
    text-align: center;
    width: @search_width;
    height: @search_height;
    border-radius: @search_border-radius;
    border: 1px solid @dark_theme;
    outline-style: none;
  }

  .close-btn {
    position: relative;
    left: -50px;
    top: 5px;
    i {
      color: #5c8aac;
    }
  }

  #search_button {
    width: 20px;
    height: 20px;
    position: relative;
    left: -50px;
    top: 5px;
    cursor: pointer;
  }

  .button:hover{background-color: @light_blue}

  .button {
    cursor: pointer;
    display: inline-block;
    color: white;
    font-size: 16px;
    font-weight: lighter;
    height: @log_button_height;
    line-height: @log_button_height;
    width: @log_button_width;
    border-radius: @log_button_border-radius;
    background-color: @main_green;
    img {
      width: 20px;
      height: 20px;
      vertical-align: middle;
      position: relative;
      top: -3px;
      left: -5px;
    }
  }

  .img-button {
    cursor: pointer;
  }

  .title {
    display: flex;
    flex-direction: row;
    justify-content: space-between;
    margin-left: 20px;
    margin-right: 210px;
    font-size: 20px;
  }

  .list:hover {
    background-color: @logList_Choose;
  }

  .too-long-text {
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
  }

  #log-list {
    margin-left: 10px;
    margin-right: 10px;
    .list {
      img {
        width: 20px;
        height: 20px;
        margin-right: 10px;
      }
      display: flex;
      flex-direction: row;
      width: 100%;
      padding: 10px 0px 10px 0px;
      border-bottom: 1px solid #afbfb8;
      .log-head {
        flex: 0 0 200px;
        display: flex;
        flex-direction: row;
        text-align: left;
        .log-name {
          cursor: pointer;
          width: 180px;
          .too-long-text;
        }
      }
      .operations {
        flex: 0 0 150px;
      }
      .date {
        flex: 0 0 120px;
      }
      .raw-log {
        flex: 0 0 150px;
        .too-long-text;
      }
      .normal-log {
        flex: 0 0 150px;
        .too-long-text;
      }
      .event-log {
        flex: 0 0 150px;
        .too-long-text;
      }
      .merge-relation {
        flex: 0 0 150px;
        .too-long-text;
        .relation1{
          width: 130px;
          .too-long-text;
          cursor: pointer;
        }
        .relation2{
          width: 130px;
          .too-long-text;
          cursor: pointer;
        }
      }
    }
    .selectedItem {
      background-color: #cbd7ea;
    }
  }

  .log-name {
    cursor: pointer;
  }
</style>

<script>
  import ElButton from "../../../../node_modules/element-ui/packages/button/src/button"
  import {mapActions} from 'vuex'
  export default{
    components: {ElButton},
    data(){
      return {
        checked: [],
        totalAmount: [],
        isSearching: false,
        items: [],
        keyWord: ''
      }
    },
    created(){
      this.getTotalItems()
    },
    destroyed(){
      this.selectLog({type: -1, id: null})
    },
    computed: {
      count: function (item, index) {
        let sum = this.items.length;
        return sum;
      },
      amount: function (item, index) {
        return this.checked.length;
      },
      checkAll: {
        get: function () {
          return this.checkedCount === this.items.length;
        },
        set: function (value) {
          let _this = this;
          if (value) {
            this.totalAmount = [];
            this.checked = this.items.map(function (item) {
              item.checked = true;
              let total = item.eventLog.id;
              _this.totalAmount.push(total);
              return item.eventLog.id;
            })
          } else {
            this.checked = [];
            this.totalAmount = [];
            this.items.forEach(function (item, index) {
              item.checked = false;
            });
          }
        }
      },
      checkedCount: {
        get: function () {
          return this.checked.length;
        }
      }
    },
    methods: {
      ...mapActions(['selectLog', 'changeFilePath', 'changeHomePath']),
      isSelected(index){
        return this.$store.getters.selectedLog.type === 2 && this.items[index].eventLog.id === this.$store.getters.selectedLog.id

      },
      jumpToNormal(index){
        if (this.items[index].normalLog) {
          this.selectLog({type: 1, id: this.items[index].normalLog.id})
          this.changeFilePath('1-2')
        }
      },
      jumpToRaw(index){
        if (this.items[index].rawLog) {
          this.selectLog({type: 0, id: this.items[index].rawLog.id})
          this.changeFilePath('1-1')
        }
      },
      selectedRel(log_index, index){
        let relations = this.items[log_index].eventLog.mergeRelation
        if(relations){
          this.selectLog({type: 2, id: relations.split(',')[index]})
        }
      },
      searchLog(){
        this.totalAmount = []
        this.checkedAll = false
        this.checked = []
        this.$api({method: 'searchEventLog', query: {keyWord: this.keyWord}}).then(res => {
          console.log(res)
          this.items = res.data.logGroups
          this.isSearching = true
        })
      },
      close_search(){
        this.isSearching = false
        this.getTotalItems()
        this.keyWord = ''
      },
      upload(){
        this.$modal({type: 'upload', data: {type: 'event'}}).then((res) => {
          console.log(res)
          this.getTotalItems()
        })
      },
      download(index){
        this.$api({method: 'downLoadEventLog', query: {id: this.items[index].eventLog.id}}).then((res) => {
          console.log(res.data)
          this.createAndDownloadFile(this.items[index].eventLog.logName, res.data)
        })
      },
      shareSome(){
        this.$api({method: 'shareEventLog', body: {idList: this.checked}}).then(res => {
          if (res.data.code === 1) {
            this.$hint('分享成功', 'success')
            this.getTotalItems()
          } else {
            this.$hint('分享失败', 'warn')
          }

        })
      },
      share(index){
        if (this.items[index].eventLog.isShared === 0) {
          this.$api({method: 'shareEventLog', body: {idList: [this.items[index].eventLog.id]}}).then(res => {
            if (res.data.code === 1) {
              this.$hint('分享成功', 'success')
              this.getTotalItems()
            } else {
              this.$hint('分享失败', 'error')
            }
          })
        } else {
          this.$api({method: 'unShareEventLog', body: {idList: [this.items[index].eventLog.id]}}).then(res => {
            if (res.data.code === 1) {
              this.$hint('取消分享成功', 'success')
              this.getTotalItems()
            } else {
              this.$hint('取消分享失败', 'error')
            }
          })
        }
      },
      deleteLog(index){
        this.$api({method: 'deleteEventLog', opts: {body: {idList: [this.items[index].eventLog.id]}}}).then(res => {
          if (res.data.code === 1) {
            this.$hint('删除成功', 'success')
            this.getTotalItems()
            this.checked = [];
            this.totalAmount = [];
            this.items.forEach(function (item, index) {
              item.checked = false;
            });
          } else {
            this.$hint('删除失败', 'error')
          }
        })
      },
      deleteSome: function () {
        this.$api({
          method: 'deleteEventLog',
          opts: {body: {idList: this.checked}}
        }).then((res) => {
          console.log(res.data)
          if (res.data.code === 1) {
            this.$hint('删除成功', 'success')
            this.getTotalItems()
            this.checked = [];
            this.totalAmount = [];
            this.items.forEach(function (item, index) {
              item.checked = false;
            });
          } else {
            this.$hint('删除失败', 'error')
          }
        })

      },
      processMining(index){
        this.$router.push({name: 'mining', params: {log_id: this.items[index].eventLog.id}})
      },
      getTotalItems(){
        const _this = this
        this.$api({method: 'getEventLog'}).then((res) => {
          console.log(res)
          _this.items = res.data.logGroups
        })
      },
      createAndDownloadFile(fileName, content) {
        let aTag = document.createElement('a');
        let blob = new Blob([content]);
        aTag.download = fileName;
        aTag.href = URL.createObjectURL(blob);
        aTag.click();
        URL.revokeObjectURL(blob);
      },
      showDetail: function (index) {
        this.$modal({
          type: 'log-detail',
          data: this.items[index].eventLog
        })
      },
      currClick: function (item, index) {
        let _this = this;
        if (typeof item.checked === 'undefined') {
          this.$set(item, 'checked', true);
          let total = item.id;
          this.totalAmount.push(total);
          console.log(this.totalAmount);
        } else {
          item.checked = !item.checked;
          if (item.checked) {
            this.totalAmount = [];
            this.items.map(function (item, index) {
              if (item.checked) {
                let total = item.id;
                _this.totalAmount.push(total);
              }
            });
          } else {
            this.totalAmount = [];
            this.items.forEach(function (item, index) {
              if (item.checked) {
                let total = item.id;
                _this.totalAmount.push(total);
              }
            });
          }
        }
      }
    }
    ,
    watch: {
      checked: function () {
        this.amount = this.checked.length;
      }
    }
  }
</script>
