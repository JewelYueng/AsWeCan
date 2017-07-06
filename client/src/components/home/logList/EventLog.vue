<template>
  <div class="event-log-details">
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
        <div v-show="!isSearching" class="search-button" @click="searchLog"><i class="el-icon-search"></i></div>
      </div>
    </div>
    <div class='title'>所有文件已加载，共{{count}}个</div>
    <div id="log-list">
      <div class="list-head" style="border-bottom: 0.8px solid #324157">
        <div class="log-head">
          <input type="checkbox" v-model="checkAll" id="文件名" value="文件名">
          <span class="log-name">文件名</span>
        </div>
        <div class="date">日期</div>
        <div class="raw-log">原始日志</div>
        <div class="normal-log">规范化日志</div>
        <div class="merge-relation">融合来源</div>
        <div class="operations"></div>
      </div>
      <div class="list">
        <div class="list-item" v-for="(item,index) in items" :class="{selectedItem: isSelected(index)}">
          <div class="input-box log-head">
            <input type="checkbox" v-model="checked" :value="item.eventLog.id" @click="currClick(item,index)">
            <span @click="showDetail(index)" class="log-name"
                  :title="item.eventLog.logName">{{item.eventLog.logName}}</span>
          </div>
          <div class="date">
            {{`${new Date(item.eventLog.createDate).getFullYear()}-${new Date(item.eventLog.createDate).getMonth() + 1}-${new Date(item.eventLog.createDate).getDate()}`}}
          </div>
          <div @click="jumpToRaw(index)" class="relation-logs raw-log" :class="{pointer: item.rawLog}"
               :title="item.rawLog ? item.rawLog.logName : '无'">
            {{item.rawLog ? item.rawLog.logName : '无'}}
          </div>
          <div @click="jumpToNormal(index)" class="relation-logs normal-log" :class="{pointer: item.normalLog}"
               :title="item.normalLog ? item.normalLog.logName : '无'">
            {{item.normalLog ? item.normalLog.logName : '无'}}
          </div>
          <div class="merge-relation">
            <div v-if="item.eventLog.mergeRelation" class="relation1" :class="{pointer: item.eventLog}"
                 @click="selectedRel(index,0)"
                 :title="item.eventLog.mergeRelationLogs[0].logName">{{item.eventLog.mergeRelationLogs[0].logName}}
            </div>
            <div v-if="item.eventLog.mergeRelation" class="relation2" :class="{pointer: item.eventLog}"
                 @click="selectedRel(index,1)"
                 :title="item.eventLog.mergeRelationLogs[1].logName">{{item.eventLog.mergeRelationLogs[1].logName}}
            </div>
            <div v-show="!item.eventLog.mergeRelation">没有融合来源</div>
          </div>
          <div class="operations">
            <i class="el-icon-setting" title="开始流程挖掘" v-on:click="processMining(index)"></i>
            <img class="download-btn" title="下载" src="static/img/cloud_download.svg"
                 @click="download(index)">
            <i class="el-icon-share" v-show="item.eventLog.isShared==0" title="分享" @click="share(index)"></i>
            <i class="el-icon-minus" v-show="item.eventLog.isShared!=0" title="取消分享" @click="share(index)"></i>
            <i class="el-icon-delete" title="删除" @click="deleteLog(index)"></i>
          </div>
        </div>
      </div>
    </div>
    <div class="block pageDiv">
      <el-pagination
        @current-change="handleCurrentChange"
        :current-page="currentPage"
        :page-size="10"
        layout="prev, pager, next, jumper"
        :total="total_items_num">
      </el-pagination>
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

  .list-item:hover {
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
      height: 530px;
      overflow: auto;
    }
    .list-item, .list-head{
      img {
        width: 12px;
        height: 12px;
      }
      display: flex;
      flex-direction: row;
      width: 100%;
      padding: 10px 0px 10px 0px;
      border-bottom: 0.5px solid @light_theme;
      justify-content: center;
      align-items: flex-start;
      .log-head {
        flex: 0 0 20%;
        min-width: 210px;
        display: flex;
        flex-direction: row;
        text-align: left;
        justify-content: center;
        align-items: center;
        .log-name {
          cursor: pointer;
          flex: 0 0 17%;
          min-width: 175px;
          .too-long-text;
        }
      }
      .operations {
        flex: 0 0 15%;
        min-width: 150px;
        color: @dark_theme;
        i {
          margin: 0 5px;
          cursor: pointer;
          font-size: 18px;
        }
        img {
          width: 18px;
          height: 18px;
          position: relative;
          top: 2px;
        }
      }
      .date {
        flex: 0 0 10%;
        min-width: 94px;
      }
      .raw-log {
        flex: 0 0 18%;
        min-width: 160px;
        .too-long-text;
      }
      .normal-log {
        flex: 0 0 18%;
        min-width: 160px;
        .too-long-text;
      }
      .event-log {
        flex: 0 0 150px;
        .too-long-text;
      }
      .merge-relation {
        flex: 0 0 18%;
        min-width: 160px;
        .too-long-text;
        .relation1 {
          min-width: 180px;
          .too-long-text;
          cursor: pointer;
        }
        .relation2 {
          min-width: 180px;
          .too-long-text;
          cursor: pointer;
        }
      }
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
        currentPage: 1,
        total_items_num: 10,
        checked: [],
        totalAmount: [],
        isSearching: false,
        items: [],
        keyWord: ''
      }
    },
    created(){
      if(this.$store.getters.selectedLog.type === 2){
        this.currentPage = parseInt(this.$store.getters.selectedLog.page)
      }
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
      handleCurrentChange(val) {
        this.currentPage = val
        if(this.isSearching){
          this.searchLog()
        }else{
          this.getTotalItems()
        }
      },
      isSelected(index){
        return this.$store.getters.selectedLog.type === 2 && this.items[index].eventLog.id === this.$store.getters.selectedLog.id

      },
      jumpToNormal(index){
        if (this.items[index].normalLog) {
          this.$api({method: 'getNormalLogPage', query: {id: this.items[index].normalLog.id}}).then( res => {
            this.selectLog({type: 1, id: this.items[index].normalLog.id, page: res.data.page})
            this.changeFilePath('1-2')
          }, err => {
            console.log(err)
            this.$hint(err.data.msg, 'error')
          })
        }
      },
      jumpToRaw(index){
        if (this.items[index].rawLog) {
          this.$api({method: 'getRawLogPage', query: {id: this.items[index].rawLog.id}}).then( res => {
            debugger
            this.selectLog({type: 0, id: this.items[index].rawLog.id, page: res.data.page})
            this.changeFilePath('1-1')
          }, err => {
            console.log(err)
            this.$hint(err.data.msg, 'error')
          })
        }
      },
      selectedRel(log_index, index){
        let relation_id = this.items[log_index].eventLog.mergeRelationLogs[index].id
        if (relation_id) {
          this.$api({method: 'getEventLogPage', query: {id: relation_id}}).then( res => {
            this.selectLog({type: 2, id: relation_id, page: res.data.page})
            this.currentPage = res.data.page
          }, err => {
            console.log(err)
            this.$hint(err.data.msg, 'error')
          })
        }
      },
      searchLog(){
        this.totalAmount = []
        this.checkedAll = false
        this.checked = []
        this.$api({method: 'searchEventLog', query: {keyWord: this.keyWord, page: this.currentPage}}).then(res => {
          console.log(res)
          this.items = res.data.logGroups
          this.total_items_num = res.data.pageNum * 10
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
        if(this.checked.length==0){
          this.$hint('请选择至少一个日志分享', 'error')
        }
        else{
        this.$api({method: 'shareEventLog', body: {idList: this.checked}}).then(res => {
          if (res.data.code === 1) {
            this.$hint('分享成功', 'success')
            this.getTotalItems()
          } else {
            this.$hint('不明原因失败,建议刷新', 'error')
          }
        }, err => {
          console.log(err)
          this.$hint(err.data.msg, 'error')
        })}
      },
      share(index){
        if (this.items[index].eventLog.isShared === 0) {
          this.$api({method: 'shareEventLog', body: {idList: [this.items[index].eventLog.id]}}).then(res => {
            if (res.data.code === 1) {
              this.$hint('分享成功', 'success')
              this.getTotalItems()
            } else {
              this.$hint('不明原因失败，建议刷新', 'error')
            }
          }, err => {
            console.log(err)
            this.$hint(err.data.msg, 'error')
          })
        } else {
          this.$api({method: 'unShareEventLog', body: {idList: [this.items[index].eventLog.id]}}).then(res => {
            if (res.data.code === 1) {
              this.$hint('取消分享成功', 'success')
              this.getTotalItems()
            } else {
              this.$hint('不明原因失败，建议刷新', 'error')
            }
          }, err => {
            console.log(err)
            this.$hint(err.data.msg, 'error')
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
            this.$hint('不明原因失败，建议刷新', 'error')
          }
        }, err => {
          console.log(err)
          this.$hint(err.data.msg, 'error')
        })
      },
      deleteSome: function () {
        if(this.checked.length==0){
          this.$hint('请选择至少一个日志删除', 'error')
        }
        else{
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
            this.$hint('不明原因失败，建议刷新', 'error')
          }
        }, err => {
          console.log(err)
          this.$hint(err.data.msg, 'error')
        })}

      },
      processMining(index){
        this.$router.push({name: 'mining', params: {log: this.items[index].eventLog}})
      },
      getTotalItems(){
        const _this = this
        this.$api({method: 'getEventLog', query: {page: this.currentPage}}).then((res) => {
          console.log(res)
          _this.items = res.data.logGroups
          _this.total_items_num = res.data.pageNum * 10
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
