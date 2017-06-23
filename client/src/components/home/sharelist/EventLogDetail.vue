<template>
  <div class="event-log">
    <input type="text" class="search" placeholder="请输入关键字"><img id="search_button" src="static/img/search.png">
    <div class="head">
      <span>全部文件，共{{amount}}个</span>
      <span>关联文件</span>
    </div>
    <div id="log-list">
      <div class="list">
        <div><input type="checkbox" v-model="checkAll" id="文件名" value="文件名"><span>文件名</span></div>
        <div>上传者</div>
        <div>日期</div>
        <div>原始日志</div>
        <div>规范化日志</div>
        <div>融合来源</div>
      </div>
      <div class="list" v-for="(item,index) in items">
        <div><input type="checkbox" v-model="checked" :value="item.eventLog.id" @click="currClick(item,index)">
          <span class="img-button" @click="showDetail(index)">{{item.eventLog.logName}}</span>
        </div>
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
    justify-content: space-between;
    margin-left: 20px;
    margin-right: 210px;
    font-size: 20px;
  }

  .search {
    margin-left: 400px;
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
    position: absolute;
    right: 400px;
    padding-top: 5px;
  }

  .button {
    color: white;
    font-size: 24px;
    text-decoration: none;
    height: @log_button_height;
    width: @log_button_width;
    border-radius: @log_button_border-radius;
    background-color: @main_green;
    cursor: pointer;
    img {
      width: 30px;
      height: 30px;
      vertical-align: text-top;
    }
  }

  .download_button, .img-button{
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
        /*  checkAll:false,*/
        /* amount: 0,*/
        items: []
      }
    },
    created(){
      this.$api({method: 'getShareEventLog'}).then((res) => {
        console.log(res)
        res.data.logGroups.map((log) => {
          this.items.push(log)
        })
      })
    },
    computed: {
      amount: function (item, index) {
        return this.totalAmount.length;
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
              let total = item.id;
              _this.totalAmount.push(total);
              return item.id;
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
      currClick: function (item, index) {
        let _this = this;
        if (typeof item.checked === 'undefined') {
          this.$set(item, 'checked', true);
          let total = item.eventLog.id;
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
    },
    watch: {
      checked: function () {
        this.amount = this.totalAmount.length;
      }
    }
  }
</script>
