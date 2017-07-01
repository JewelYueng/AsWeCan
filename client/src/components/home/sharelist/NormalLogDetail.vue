<template>
  <div class="normal-log">
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
        <div class="event-log">事件日志</div>
      </div>
      <div class="list" v-for="(item,index) in items">
        <div class="log-name" :title="item.normalLog.logName">{{item.normalLog.logName}}</div>
        <div class="uploader">{{item.normalLog.userId}}</div>
        <div class="date">
          {{`${new Date(item.normalLog.createDate).getFullYear()}-${new Date(item.normalLog.createDate).getMonth() + 1}-${new Date(item.normalLog.createDate).getDate()}`}}
        </div>
        <div class="raw-log" :title="item.rawLog ? item.rawLog.logName : '无'">{{item.rawLog ? item.rawLog.logName : '无'}}</div>
        <div class="event-log" :title="item.eventLog ? item.eventLog.logName : '无'">{{item.eventLog ? item.eventLog.logName : '无'}}</div>
        <div class="operations">
          <img class="download_button img-button" title="下载" src="static/img/cloud_download.png"
               @click="download(index)">
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

  .img-button {
    cursor: pointer;
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

  #search_button {
    width: 20px;
    height: 20px;
    position: relative;
    left: -50px;
    top: 5px;
    cursor: pointer;
  }

  .close-btn {
    position: relative;
    right: 28px;
    top: -5px;
    i {
      color: #5c8aac;
    }
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
        flex: 0 0 250px;
        .too-long-text;
        text-align: left;
      }
      .operations {
        flex: 0 0 40px;
      }
      .uploader {
        flex: 0 0 90px
      }
      .date {
        flex: 0 0 120px;
        .too-long-text;
      }
      .raw-log {
        flex: 0 0 250px;
        .too-long-text;
      }
      .event-log {
        flex: 0 0 250px;
        .too-long-text;
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
        items: [],
        isSearching: false,
        keyWord: ''
      }
    },
    created(){
      this.$api({method: 'getShareNormalLog'}).then((res) => {
        console.log(res)
        res.data.logGroups.map((log) => {
          this.items.push(log)
        })
      })
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
        this.$api({method: 'searchSharedNormalLog', query: {keyWord: this.keyWord}}).then(res => {
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
      getTotalItems(){
        let totalItems = []
        this.$api({method: 'getShareNormalLog'}).then((res) => {
          console.log(res)
          res.data.logGroups.map((log) => {
            totalItems.push(log)
          })
        })
        return totalItems
      },

      download(index){
        this.$api({method: 'downLoadNormalLog', query: {id: this.items[index].normalLog.id}}).then((res) => {
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
      }
    },
    watch: {
      checked: function () {
        this.amount = this.items.length;
      }
    }
  }
</script>
