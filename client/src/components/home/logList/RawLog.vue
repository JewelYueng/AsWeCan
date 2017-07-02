<template>
  <div class="raw-log-details">
    <div class="head">
      <div class="bash-btns">
        <el-button type="primary" @click="upload" icon="upload">上传</el-button>
        <el-button @click="shareSome" icon="share">分享</el-button>
        <el-button @click="deleteSome" icon="delete">删除</el-button>
      </div>
      <div class="search">
        <input type="text" placeholder="请输入关键字" v-model="keyWord">
        <div v-show="isSearching" class="close-btn" @click="close_search">
          <i class="el-icon-circle-cross"></i>
        </div>
        <div v-show="!isSearching" class="search-button" @click="searchLog"><i class="el-icon-search"></i>
        </div>
      </div>
    </div>
    <div class='title'>所有文件已加载，共{{count}}个</div>
    <div id="log-list">
      <div class="list" style="border-bottom: 0.8px solid #324157">
        <div class="log-head">
          <input type="checkbox" v-model="checkAll" id="文件名" value="文件名">
          <span class="log-name">文件名</span></div>
        <div class="date">日期</div>
        <div class="normal-log">规范化日志</div>
        <div class="event-log">事件日志</div>
        <div class="operations"></div>
      </div>
      <div class="list" v-for="(item,index) in items" :class="{selectedItem: isSelected(index)}">
        <div class="log-head">
          <input type="checkbox" v-model="checked" :value="item.rawLog.id" @click="currClick(item,index)">
          <span @click="showDetail(index)" class="log-name" :title="item.rawLog.logName">{{item.rawLog.logName}}</span>
        </div>
        <div class="date">
          {{`${new Date(item.rawLog.createDate).getFullYear()}-${new Date(item.rawLog.createDate).getMonth() + 1}-${new Date(item.rawLog.createDate).getDate()}`}}
        </div>
        <div @click="jumpToNormal(index)" class="relation-logs normal-log"
             :title="item.normalLog ? item.normalLog.logName : '无'">{{item.normalLog ? item.normalLog.logName : '无'}}
        </div>
        <div @click="jumpToEvent(index)" class="relation-logs event-log"
             :title="item.eventLog ? item.eventLog.logName : '无'">{{item.eventLog ? item.eventLog.logName : '无'}}
        </div>
        <div class="operations">
          <i class="el-icon-setting" title="生成规范化日志" v-on:click="transferToNormal(index)"></i>
          <img class="download-btn" title="下载" src="static/img/cloud_download.png"
               @click="download(index)">
          <i class="el-icon-share" v-show="item.rawLog.isShared==0" title="分享" @click="share(index)"></i>
          <i class="el-icon-minus" v-show="item.rawLog.isShared!=0" title="取消分享" @click="share(index)"></i>
          <i class="el-icon-delete" title="删除" @click="deleteLog(index)"></i>
        </div>
      </div>
    </div>
  </div>
</template>

<style lang="less" scoped rel="stylesheet/less">
  @import '~assets/colors.less';
  @import "~assets/layout.less";


  .title {
    position: absolute;
    right: 55px;
    font-size: 14px;
    color: #b5b5b5;
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
    padding-top: 20px;
    margin-left: 10px;
    margin-right: 10px;
    font-size: 14px;
    .list {
      img {
        width: 12px;
        height: 12px;
      }
      display: flex;
      flex-direction: row;
      width: 100%;
      padding: 10px 0px 10px 0px;
      border-bottom: 0.5px solid @light_theme;
      .log-head {
        flex: 0 0 200px;
        text-align: left;
        display: flex;
        flex-direction: row;
        .log-name {
          cursor: pointer;
          width: 180px;
          .too-long-text;
        }
      }
      .operations {
        flex: 0 0 150px;
        color: @dark_theme;
        i {
          margin: 0 5px;
          cursor: pointer;
        }
      }
      .date {
        flex: 0 0 120px;
        .too-long-text;
      }
      .raw-log {
        flex: 0 0 250px;
        .too-long-text;
      }
      .normal-log {
        flex: 0 0 250px;
        .too-long-text;
      }
      .event-log {
        flex: 0 0 250px;
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
    components: {ElButton},
    data(){
      return {
        isSearching: false,
        keyWord: '',
        checked: [],
        totalAmount: [],
        items: [],

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
          return this.checkedCount === this.items.length;
        },
        set: function (value) {
          let _this = this;
          if (value) {
            this.totalAmount = [];
            this.checked = this.items.map(function (item) {
              item.checked = true;
              let total = item.rawLog.id;
              _this.totalAmount.push(total);
              return item.rawLog.id;
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
        return this.$store.getters.selectedLog.type === 0 && this.items[index].rawLog.id === this.$store.getters.selectedLog.id
      },
      jumpToNormal(index){
        if (this.items[index].normalLog) {
          this.selectLog({type: 1, id: this.items[index].normalLog.id})
          this.changeFilePath('1-2')
        }
      },
      jumpToEvent(index){
        if (this.items[index].eventLog) {
          this.selectLog({type: 2, id: this.items[index].eventLog.id})
          this.changeFilePath('1-3')
        }
      },
      shareSome(){
        this.$api({method: 'shareRawLog', body: {idList: this.checked}}).then(res => {
          if (res.data.code === 1) {
            this.$hint('分享成功', 'success')
            this.getTotalItems()
          } else {
            this.$hint('分享失败', 'warn')
          }
        })
      },
      upload: function () {
        this.$modal({type: 'upload', data: {type: 'raw'}}).then((res) => {
          console.log(res)
          this.getTotalItems()
        })
      },
      deleteLog: function (index) {

        this.$api({method: 'deleteRawLog', opts: {body: {idList: [this.items[index].rawLog.id]}}}).then((res) => {
          if (parseInt(res.data.code) === 1) {
            this.$hint('删除成功', 'success');
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
          method: 'deleteRawLog',
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
      share(index){
        if (this.items[index].rawLog.isShared === 0) {
          this.$api({method: 'shareRawLog', body: {idList: [this.items[index].rawLog.id]}}).then(res => {
            if (res.data.code === 1) {
              this.$hint('分享成功', 'success')
              this.getTotalItems()
            } else {
              this.$hint('分享失败', 'warn')
            }
          })
        } else {
          this.$api({method: 'unShareRawLog', body: {idList: [this.items[index].rawLog.id]}}).then(res => {
            if (res.data.code === 1) {
              this.$hint('取消分享成功', 'success')
              this.getTotalItems()
            } else {
              this.$hint('取消分享失败', 'warn')
            }
          })
        }
      },
      download: function (index) {
        this.$api({method: 'downLoadRawLog', query: {id: this.items[index].rawLog.id}}).then((res) => {
          console.log(res.data)
          this.createAndDownloadFile(this.items[index].rawLog.logName, res.data)
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
      searchLog: function () {
        this.totalAmount = []
        this.checkedAll = false
        this.checked = []
        this.$api({method: 'searchRawLog', query: {keyWord: this.keyWord}}).then(res => {
          console.log(res)
          this.items = res.data.logGroups
          this.isSearching = true
        })
      },
      getTotalItems(){
        const _this = this
        this.$api({method: 'getRawLog'}).then((res) => {
          console.log(res)
          _this.items = res.data.logGroups
        })
      },
      close_search(){
        this.isSearching = false
        this.getTotalItems()
        this.keyWord = ''
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
      transferToNormal: function (index) {
        this.$modal({type: 'normal-info', data: {id: this.items[index].rawLog.id}}).then((a) => {
          console.log(a)
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
