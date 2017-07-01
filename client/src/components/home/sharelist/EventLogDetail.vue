<template>
  <div class="event-log">
    <div class="head">
      <input type="text" class="search" placeholder="请输入关键字" v-model="keyWord">
      <div v-show="isSearching" class="img-button close-btn" @click="close_search">
        <i class="el-icon-circle-cross"></i>
      </div>
      <div v-show="!isSearching" id="search_button" @click="searchLog"><i class="el-icon-search"></i></div>
    </div>
    <div class='title'>所有文件已加载，共{{count}}个</div>
    <div id="log-list">
      <div class="list" style="border-bottom: 0.8px solid #324157">
        <div class="log-name">文件名</div>
        <div class="uploader">上传者</div>
        <div class="date">日期</div>
        <div class="raw-log">原始日志</div>
        <div class="normal-log">规范化日志</div>
        <div class="merge-relation">融合来源</div>
        <div class="operations"></div>
      </div>
      <div class="list" v-for="(item,index) in items">
        <div class="img-button log-name" @click="showDetail(index)" :title="item.eventLog.logName">{{item.eventLog.logName}}</div>
        <div class="uploader">{{item.eventLog.userId}}</div>
        <div class="date">
          {{`${new Date(item.eventLog.createDate).getFullYear()}-${new Date(item.eventLog.createDate).getMonth() + 1}-${new Date(item.eventLog.createDate).getDate()}`}}
        </div>
        <div class="raw-log" :title="item.rawLog ? item.rawLog.logName : '无'">{{item.rawLog ? item.rawLog.logName : '无'}}</div>
        <div class="normal-log" :title="item.normalLog ? item.normalLog.logName : '无'">{{item.normalLog ? item.normalLog.logName : '无'}}</div>
        <div class="merge-relation">
          <div v-if="item.eventLog.mergeRelation" class="relation1" @click="selectedRel(index,0)" :title="item.eventLog.mergeRelation.split(',')[0]">{{item.eventLog.mergeRelation.split(',')[0]}}</div>
          <div v-if="item.eventLog.mergeRelation" class="relation2" @click="selectedRel(index,1)" :title="item.eventLog.mergeRelation.split(',')[1]">{{item.eventLog.mergeRelation.split(',')[1]}}</div>
          <div v-show="!item.eventLog.mergeRelation">没有融合来源</div>
        </div>
        <div class="operations">
          <img class="download_button" title="下载" src="static/img/cloud_download.png" @click="download(index)">
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
    padding-bottom: 30px;
  }

  .title {
    position: absolute;
    right: 55px;
    font-size: 14px;
    color: #b5b5b5;
  }

  .search {
    margin-left: 654px;
    background-color: @tab_selected;
    color: @dark_theme;
    text-align: center;
    width: @search_width;
    height: @search_height;
    border-radius: @search_border-radius;
    border: none;
    outline-style: none;
  }

  .close-btn {
    position: relative;
    right: 28px;
    top: -5px;
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

  .download_button {
    cursor: pointer;
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
        margin-right: 10px;
      }
      display: flex;
      flex-direction: row;
      width: 100%;
      padding: 10px 0px 10px 0px;
      border-bottom: 0.5px solid @light_theme;
      .log-name {
        cursor: pointer;
        max-width: 200px;
        flex:0 0 170px;
        .too-long-text;
        text-align: left;
      }
      .operations {
        flex: 0 0 40px;
      }
      .uploader{
        flex: 0 0 90px
      }
      .date {
        flex: 0 0 120px;
        .too-long-text;
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
        flex: 0 0 200px;
        .too-long-text;
      }
      .merge-relation {
        flex: 0 0 250px;
      }
    }
  }

</style>

<script>
  export default{
    data(){
      return {
        checked: [],
        totalAmount: [],
        isSearching: false,
        keyWord: '',
        items: []
      }
    },
    created(){
      this.items = this.getTotalItems()
    },
    computed: {
      amount: function (item, index) {
        return this.items.length;
      }
    },
    methods: {
      searchLog(){
        this.totalAmount = []
        this.checkedAll = false
        this.checked = []
        this.$api({method: 'searchSharedEventLog', query: {keyWord: this.keyWord}}).then(res => {
          console.log(res)
          this.items = res.data.logGroups
          this.isSearching = true
        })
      },
      close_search(){
        this.isSearching = false
        this.items = this.getTotalItems()
        this.keyWord = ''
      },
      download(index){
        this.$api({method: 'downLoadEventLog', query: {id: this.items[index].eventLog.id}}).then((res) => {
          console.log(res.data)
          this.createAndDownloadFile(this.items[index].eventLog.logName, res.data)
        })
      },
      showDetail: function (index) {
        this.$modal({
          type: 'log-detail',
          data: this.items[index].eventLog
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
      getTotalItems(){
        let totalItems = []
        this.$api({method: 'getShareEventLog'}).then((res) => {
          console.log(res)
          res.data.logGroups.map((log) => {
            totalItems.push(log)
          })
        })
        return totalItems
      },

    },
    watch: {
      checked: function () {
        this.amount = this.items.length;
      }
    }
  }
</script>
