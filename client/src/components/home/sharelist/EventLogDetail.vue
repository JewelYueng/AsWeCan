<template>
  <div class="event-log">
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
        <div class="normal-log">规范化日志</div>
        <div class="merge-relation">融合来源</div>
        <div class="operations"></div>
      </div>
      <div class="list">
        <div class="list-item" v-for="(item,index) in items" :class="{selectedItem: isSelected(index)}">
          <div class="img-button log-name" @click="showDetail(index)" :title="item.eventLog.logName">
            {{item.eventLog.logName}}
          </div>
          <div class="uploader">{{item.user.name}}</div>
          <div class="date">
            {{`${new Date(item.eventLog.createDate).getFullYear()}-${new Date(item.eventLog.createDate).getMonth() + 1}-${new Date(item.eventLog.createDate).getDate()} ${new Date(item.eventLog.createDate).getHours()}:${new Date(item.eventLog.createDate).getMinutes()}`}}
          </div>
          <div  @click="jumpToRaw(index)" class="raw-log " :class="{pointer: item.rawLog}"
                :title="item.rawLog ? item.rawLog.logName : '无'">
            {{item.rawLog ? item.rawLog.logName : '无'}}
          </div>
          <div @click="jumpToNormal(index)" class="normal-log " :class="{pointer: item.normalLog}"
               :title="item.normalLog ? item.normalLog.logName : '无'">
            {{item.normalLog ? item.normalLog.logName : '无'}}
          </div>
          <div class="merge-relation">
            <div v-if="item.eventLog.mergeRelation" class="relation1" @click="selectedRel(index,0)"
                 :class="{'pointer': item.eventLog,'mergeCss':item.eventLog.mergeRelationLogs[0].isShared==0||item.eventLog.mergeRelationLogs[0].state==2}"
                 :title="item.eventLog.mergeRelationLogs[0].logName">{{item.eventLog.mergeRelationLogs[0].logName}}
            </div>
            <div v-if="item.eventLog.mergeRelation" class="relation2" @click="selectedRel(index,1)"
                 :class="{'pointer': item.eventLog,'mergeCss':item.eventLog.mergeRelationLogs[1].isShared==0||item.eventLog.mergeRelationLogs[1].state==2}"
                 :title="item.eventLog.mergeRelationLogs[1].logName">{{item.eventLog.mergeRelationLogs[1].logName}}
            </div>
            <div v-show="!item.eventLog.mergeRelation">没有融合来源</div>
          </div>
          <div class="operations">
            <img class="download_button" title="下载" src="static/img/cloud_download.svg" @click="download(index)">
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
      overflow-y: auto;
      overflow-x: hidden;
    }
    .list-item .log-name{
      cursor: pointer;
    }
    .list-item, .list-head{
      display: flex;
      flex-direction: row;
      width: 100%;
      padding: 10px 0px 10px 0px;
      border-bottom: 0.5px solid @light_theme;
      justify-content: flex-start;
      align-items: center;
      .log-name {
       //cursor:pointer;
        min-width: 190px;
        flex: 0 0 20%;
        .too-long-text;
        text-align: left;
      }
      .operations {
        flex: 0 0 3%;
        width: 25px;
        img {
          cursor: pointer;
          width: 18px;
          height: 18px;
          position: relative;
          top: 2px;
        }
      }
      .uploader {
        flex: 0 0 10%;
        min-width: 80px;
        .too-long-text;
      }
      .date {
        flex: 0 0 10%;
        min-width: 120px;
        .too-long-text;
      }
      .raw-log {
        flex: 0 0 17%;
        min-width: 170px;
        .too-long-text;
      }
      .normal-log {
        flex: 0 0 17%;
        min-width: 170px;
        .too-long-text;
      }
      .merge-relation {
        flex: 0 0 17%;
        min-width: 170px;
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
        isSearching: false,
        keyWord: '',
        items: [],
        currentPage: 1,
        total_items_num: 10
      }
    },
    created(){
      if(this.$store.getters.selectedLog.type === 5){
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
        return this.$store.getters.selectedLog.type === 5 && this.items[index].eventLog.id === this.$store.getters.selectedLog.id
      },
      jumpToNormal(index){
        if (this.items[index].normalLog) {
          this.$api({method: 'getShareNormalPage', query: {id: this.items[index].normalLog.id}}).then( res => {
            this.selectLog({type: 4, id: this.items[index].normalLog.id, page: res.data.page})
            this.changeFilePath('2-2')
          }, err => {
            console.log(err)
            this.$hint(err.data.msg, 'error')
          })
        }
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
      selectedRel(log_index, index){
        let relation_id = this.items[log_index].eventLog.mergeRelationLogs[index].id
        let relation_state = this.items[log_index].eventLog.mergeRelationLogs[index].isShared

        if (relation_id && relation_state === 1) {
          this.$api({method: 'getShareEventPage', query: {id: relation_id}}).then( res => {
            this.selectLog({type: 5, id: relation_id, page: res.data.page})
            this.currentPage = res.data.page
          }, err => {
            console.log(err)
            this.$hint(err.data.msg, 'error')
          })
        } else{
          this.$hint('融合来源没有被分享', 'warn')
        }
      },
      searchLog(){
        this.totalAmount = []
        this.checkedAll = false
        this.checked = []
        this.$api({method: 'searchSharedEventLog', query: {keyWord: this.keyWord, page: this.currentPage}}).then(res => {
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
        const _this = this
        this.$api({method: 'getShareEventLog', query: {page: this.currentPage}}).then((res) => {
          console.log(res)
          _this.items = res.data.logGroups
          _this.total_items_num = res.data.pageNum * 10
        })
      },

    },
    watch: {
      checked: function () {
        this.amount = this.items.length;
      }
    }
  }
</script>
