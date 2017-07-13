<template>
  <div class="normal-log">
    <div class="share-head">
      <div class="search">
        <input type="text" placeholder="请输入关键字" v-model="keyWord">
        <div v-show="isSearching" class="img-button close-btn" @click="close_search">
          <i class="el-icon-circle-cross"></i>
        </div>
        <div v-show="!isSearching" class="search-button" @click="searchLog"><i class="el-icon-search"></i></div>
      </div>
    </div>
    <div class='title'>所有文件已加载，共{{items.length}}个</div>
    <div id="log-list">
      <div class="list-head" style="border-bottom: 0.8px solid #324157">
        <div class="log-name">文件名</div>
        <div class="uploader">上传者</div>
        <div class="date">日期</div>
        <div class="raw-log">原始日志</div>
        <div class="event-log">事件日志</div>
      </div>
      <div class="list">
        <div class="list-item" v-for="(item,index) in items" :class="{selectedItem: isSelected(index)}">
          <div class="log-name" :title="item.normalLog.logName">{{item.normalLog.logName}}</div>
          <div class="uploader">{{item.user.name}}</div>
          <div class="date">
            {{`${new Date(item.normalLog.createDate).getFullYear()}-${new Date(item.normalLog.createDate).getMonth() + 1}-${new Date(item.normalLog.createDate).getDate()}`}}
          </div>
          <div @click="jumpToRaw(index)" class="raw-log" :class="{pointer: item.rawLog}"
               :title="item.rawLog ? item.rawLog.logName : '无'">
            {{item.rawLog ? item.rawLog.logName : '无'}}
          </div>
          <div  @click="jumpToEvent(index)" class="event-log" :class="{pointer: item.eventLog}"
                :title="item.eventLog ? item.eventLog.logName : '无'">
            {{item.eventLog ? item.eventLog.logName : '无'}}
          </div>
          <div class="operations">
            <img class="download_button img-button" title="下载" src="static/img/cloud_download.svg"
                 @click="download(index)">
          </div>
        </div>
      </div>
    </div>
    <div class="block pageDiv">
      <el-pagination
        @current-change="handleCurrentChange"
        :current-page="currentPage"
        :page-size="10"
        layout=" prev, pager, next, jumper"
        :total="total_items_num">
      </el-pagination>
    </div>
  </div>
</template>

<style lang="less" scoped rel="stylesheet/less">
  @import '~assets/colors.less';
  @import "~assets/layout.less";

  .share-head {
    display: flex;
    flex-direction: row;
    align-items: flex-end;
    justify-content: flex-end;
    padding-bottom: 30px;
  }

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
    .list{
      height: 530px;
      overflow: auto;
    }
    .list-item, .list-head{
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
      justify-content: flex-start;
      align-items: center;
      .log-name {
        min-width: 250px;
        flex: 0 0 25%;
        .too-long-text;
        text-align: left;
      }
      .operations {
        flex: 0 0 5%;
        min-width: 25px;
        img {
          cursor: pointer;
          width: 18px;
          height: 18px;
          position: relative;
          top: 2px;
        }
      }
      .uploader {
        flex: 0 0 8%;
        min-width: 80px;
        .too-long-text;
      }
      .date {
        flex: 0 0 10%;
        min-width: 100px;
        .too-long-text;
      }
      .raw-log {
        flex: 0 0 25%;
        min-width: 250px;
        .too-long-text;
      }
      .event-log {
        flex: 0 0 25%;
        min-width: 250px;
        .too-long-text;
      }
    }
  }

</style>

<script>
  import {mapActions} from 'vuex'
  export default{
    data(){
      return {
        checked: [],
        totalAmount: [],
        items: [],
        isSearching: false,
        keyWord: '',
        currentPage: 1,
        total_items_num: 10
      }
    },
    created(){
      if(this.$store.getters.selectedLog.type === 4){
        this.currentPage = parseInt(this.$store.getters.selectedLog.page)
      }
      this.getTotalItems()
    },
    computed: {
      amount: function (item, index) {
        return this.items.length;
      }
    },

    methods: {
      ...mapActions(['selectLog', 'changeFilePath']),
      handleCurrentChange(val) {
        this.currentPage = val
        if(this.isSearching){
          this.searchLog()
        }else{
          this.getTotalItems()
        }
      },
      isSelected(index){
        return this.$store.getters.selectedLog.type === 4 && this.items[index].normalLog.id === this.$store.getters.selectedLog.id
      },
      jumpToRaw(index){

        if (this.items[index].rawLog) {
          this.$api({method: 'getShareRawPage', query: {id: this.items[index].rawLog.id}}).then( res => {
            this.selectLog({type: 3, id: this.items[index].rawLog.id, page: res.data.page})
            this.changeFilePath('2-1')
          }, err => {
          console.log(err)
          this.$hint(err.data.msg, 'error')
        })
        }
      },
      jumpToEvent(index){
        if (this.items[index].eventLog) {
          this.$api({method: 'getShareEventPage', query: {id: this.items[index].eventLog.id}}).then( res => {
            this.selectLog({type: 5, id: this.items[index].eventLog.id, page: res.data.page})
            this.changeFilePath('2-3')
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
        this.$api({method: 'searchSharedNormalLog', query: {keyWord: this.keyWord, page: this.currentPage}}).then(res => {
          console.log(res)
          this.items = res.data.logGroups
          this.total_items_num = res.data.pageNum * 10
          this.isSearching = true
        })
      },
      close_search(){
        this.isSearching = false
        this.items = this.getTotalItems()
        this.keyWord = ''
      },
      getTotalItems(){
        const _this = this
        this.$api({method: 'getShareNormalLog', query: {page: this.currentPage}}).then((res) => {
          console.log(res)
          _this.items = res.data.logGroups
          _this.total_items_num = res.data.pageNum * 10
        })
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
