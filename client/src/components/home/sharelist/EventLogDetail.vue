<template>
  <div class="event-log">
    <div class="head">
      <input type="text" class="search" placeholder="请输入关键字" v-model="keyWord">
      <div v-show="isSearching" class="img-button close-btn" @click="close_search">
        <i class="el-icon-circle-cross"></i>
      </div>
      <img id="search_button" src="static/img/search.png" @click="searchLog">
    </div>
    <div class="title">
      <span>全部文件，共{{amount}}个</span>
      <span>关联文件</span>
    </div>
    <div id="log-list">
      <div class="list">
        <div>文件名</div>
        <div>上传者</div>
        <div>日期</div>
        <div>原始日志</div>
        <div>规范化日志</div>
        <div>融合来源</div>
        <div></div>
      </div>
      <div class="list" v-for="(item,index) in items">
        <div class="img-button" @click="showDetail(index)">{{item.eventLog.logName}}</div>
        <div>{{item.eventLog.userId}}</div>
        <div>
          {{`${new Date(item.eventLog.createDate).getFullYear()}-${new Date(item.eventLog.createDate).getMonth() + 1}-${new Date(item.eventLog.createDate).getDate()}`}}
        </div>
        <div>{{item.rawLog ? `${item.rawLog.logName}.${item.rawLog.format}` : '无'}}</div>
        <div>{{item.normalLog ? `${item.normalLog.logName}.${item.normalLog.format}` : '无'}}</div>
        <div>{{item.eventLog.mergeRelation ? `` : '无'}}</div>
        <div>
          <img class="download_button" title="下载" src="static/img/download_color.png" @click="download(index)">
        </div>
      </div>
    </div>
  </div>
</template>

<style lang="less" scoped rel="stylesheet/less">
  @import '~assets/colors.less';
  @import "~assets/layout.less";

  .event-log {
    padding-top: 20px;
  }

  .head {
    display: flex;
    flex-direction: row;
    align-items: flex-end;
    justify-content: flex-end;
    padding-right: 40px;
  }

  .title {
    text-align: left;
    display: flex;
    flex-direction: row;
    justify-content: space-between;
    margin-left: 20px;
    margin-right: 210px;
    font-size: 20px;
  }

  .search {
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
    position: absolute;
    right: 135px;
    top: 120px;
    i {
      color: #5c8aac;
    }
  }

  #search_button {
    width: 20px;
    height: 20px;
    position: relative;
    right: 28px;
    top: -5px;
    cursor: pointer;
  }

  .download_button{
    cursor: pointer;
  }

  .list:hover {
    background-color: @logList_Choose;
  }

  #log-list {
    margin-left: 10px;
    margin-right: 10px;
    .list {
      img {
        width: 30px;
        height: 30px;
      }
      display: flex;
      flex-direction: row;
      width: 100%;
      padding: 10px 0px 10px 0px;
      border-bottom: 1px solid #afbfb8;
      div {
        flex: 1;
        text-align: left;
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
        return this.totalAmount.length;
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
        this.amount = this.totalAmount.length;
      }
    }
  }
</script>
