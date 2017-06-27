<template>
  <div class="normal-log">
    <div class="head">
      <div class="button" @click="upload"><img src="static/img/upload.png">上传</div>
      <div class="button" @click="shareSome"><img src="static/img/share_white.png">分享</div>
      <div class="button" @click="deleteSome"><img src="static/img/Delete.png" style="padding-top: 2px">删除</div>
      <input type="text" id="searchBox" placeholder="请输入关键字" v-model="keyWord">
      <div v-show="isSearching" class="img-button close-btn" @click="close_search">
        <i class="el-icon-circle-cross"></i>
      </div>
      <img v-show="!isSearching" id="search_button" src="static/img/search.png" @click="searchLog">
    </div>
    <div class="title"><span>全部文件，共{{count}}个，已选{{amount}}个</span><span>关联文件</span></div>
    <div id="log-list">
      <div class="list">
        <div class="log-head"><input type="checkbox" v-model="checkAll" id="文件名" value="文件名">
          <span class="log-name">文件名</span>
        </div>
        <div class="operations"></div>
        <div class="date">日期</div>
        <div class="raw-log">原始日志</div>
        <div class="event-log">事件日志</div>
      </div>
      <div class="list" v-for="(item, index) in items" :class="{selectedItem: isSelected(index)}">
        <div class="log-head">
          <input type="checkbox" v-model="checked" :value="item.normalLog.id" @click="currClick(item,index)">
          <span class="log-name">
            {{item.normalLog.logName}}</span>
        </div>
        <div class="operations">
          <img style="cursor:pointer" class="process_button" title="生成事件日志"
               v-on:click="transferToEvent(index)"
               src="static/img/process_color.png">
          <img @click="download(index)"
               class="img-button download_button" title="下载" src="static/img/download_color.png">
          <img @click="share(index)"
               class="img-button share_button" title="分享"
               :src="item.normalLog.isShared === 0 ? 'static/img/share_color.png' : 'static/img/forbidden_color.png'">
          <img @click="deleteLog(index)"
               class="delete_button img-button" title="删除" src="static/img/Delete_color.png">
        </div>
        <div class="date">
          {{`${new Date(item.normalLog.createDate).getFullYear()}-${new Date(item.normalLog.createDate).getMonth() + 1}-${new Date(item.normalLog.createDate).getDate()}`}}
        </div>
        <div class="relation-logs raw-log" @click="jumpToRaw(index)">
          {{item.rawLog ? item.rawLog.logName : '无'}}
        </div>
        <div class="relation-logs event-log" @click="jumpToEvent(index)">
          {{item.eventLog ? item.eventLog.logName : '无'}}
        </div>
      </div>
    </div>
  </div>

</template>

<style lang="less" scoped rel="stylesheet/less">
  @import '~assets/colors.less';
  @import "~assets/layout.less";

  .img-button {
    cursor: pointer;
  }

  .close-btn {
    position: relative;
    left: -40px;
    top: 5px;
    i {
      color: #5c8aac;
    }
  }

  .normal-log {
    padding-top: 20px;
  }

  .head {
    display: flex;
    flex-direction: row;
    justify-content: space-around;
    position: relative;
  }

  .title {
    display: flex;
    flex-direction: row;
    justify-content: space-between;
    margin-left: 20px;
    margin-right: 210px;
    font-size: 20px;
  }

  .button {
    cursor: pointer;
    color: white;
    font-size: 24px;
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

  #searchBox {
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

  #search_button {
    width: 20px;
    height: 20px;
    position: relative;
    left: -40px;
    top: 5px;
    cursor: pointer;
  }

  .list:hover {
    background-color: @logList_Choose;
  }

  .too-long-text{
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
        flex:  0 0 200px;
        text-align: left;
        display: flex;
        flex-direction: row;
        .log-name{
          cursor: pointer;
          max-width: 180px;
          .too-long-text;
        }
      }
      .operations {
        flex: 0 0 150px;
      }
      .date{
        flex:0 0 120px;
        .too-long-text;
      }
      .raw-log{
        flex: 0 0 200px;
        .too-long-text;
      }
      .normal-log{
        flex: 0 0 200px;
        .too-long-text;
      }
      .event-log{
        flex: 0 0 200px;
        .too-long-text;
      }
    }
    .selectedItem {
      background-color: #cbd7ea;
    }
  }


</style>

<script>
  import ElButton from "../../../../node_modules/element-ui/packages/button/src/button"
  import {mapActions} from 'vuex'
  export default{
    data(){
      return {
        checked: [],
        totalAmount: [],
        items: [],
        isSearching: false,
        keyWord: ''
      }
    },
    created(){
      this.getTotalItems()
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
          return this.checkedCount == this.items.length;
        },
        set: function (value) {
          let _this = this;
          if (value) {
            this.totalAmount = [];
            this.checked = this.items.map(function (item) {
              item.checked = true;
              let total = item.normalLog.id;
              _this.totalAmount.push(total);
              return item.normalLog.id;
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
      ...mapActions(['selectLog', 'changeFilePath']),
      isSelected(index){
        return this.$store.getters.selectedLog.type === 1 && this.items[index].normalLog.id === this.$store.getters.selectedLog.id
      },
      jumpToRaw(index){
        if (this.items[index].rawLog) {
          this.selectLog({type: 0, id: this.items[index].rawLog.id})
          this.changeFilePath('1-1')
        }
      },
      jumpToEvent(index){
        if (this.items[index].eventLog) {
          this.selectLog({type: 2, id: this.items[index].eventLog.id})
          this.changeFilePath('1-3')
        }
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
      },

      upload(){
        this.$modal({type: 'upload', data: {type: 'normal'}}).then((res) => {
          console.log(res)
          this.getTotalItems()
        })
      },

      transferToEvent: function (index) {
        this.$api({method: 'toEventLog', query: {id: this.items[index].normalLog.id}}).then((res) => {
          console.log(res.data)
          if (res.data.code === 1) {
            this.$hint('生成成功', 'success')
            this.getTotalItems()
          } else {
            this.$hint('请使用格式正确的规范化日志进行转换', 'warn')
          }
        })
      },

      download: function (index) {
        this.$api({method: "downLoadNormalLog", query: {id: this.items[index].normalLog.id}}).then((res) => {
          console.log(res.data)
          this.createAndDownloadFile(this.items[index].normalLog.logName, res.data)
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

      deleteLog: function (index) {

        this.$api({
          method: 'deleteNormalLog',
          opts: {body: {idList: [this.items[index].normalLog.id]}}
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
      deleteSome: function () {

        this.$api({
          method: 'deleteNormalLog',
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

      shareSome(){
        this.$api({method: 'shareNormalLog', body: {idList: this.checked}}).then(res => {
          if (res.data.code === 1) {
            this.$hint('分享成功', 'success')
            this.getTotalItems()
          } else {
            this.$hint('分享失败', 'warn')
          }

        })
      },
      share(index){
        if (this.items[index].normalLog.isShared === 0) {
          this.$api({method: 'shareNormalLog', body: {idList: [this.items[index].normalLog.id]}}).then(res => {
            if (res.data.code === 1) {
              this.$hint('分享成功', 'success')
              this.getTotalItems()
            } else {
              this.$hint('分享失败', 'warn')
            }
          })
        } else {
          this.$api({method: 'unShareNormalLog', body: {idList: [this.items[index].normalLog.id]}}).then(res => {
            if (res.data.code === 1) {
              this.$hint('取消分享成功', 'success')
              this.getTotalItems()
            } else {
              this.$hint('取消分享失败', 'warn')
            }
          })
        }
      },

      searchLog () {
        this.totalAmount = []
        this.checkedAll = false
        this.checked = []
        this.$api({method: 'searchNormalLog', query: {keyWord: this.keyWord}}).then(res => {
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
      getTotalItems(){
        const _this = this
        this.$api({method: 'getNormalLog'}).then((res) => {
          console.log(res)
          _this.items = res.data.logGroups
        })
      }
    },
    watch: {
      checked: function () {
        this.amount = this.checked.length;
      }
    }
  }
</script>
