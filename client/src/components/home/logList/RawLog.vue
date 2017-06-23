<template>
  <div class="raw-log-details">
    <div id="head">
      <a class=" btn bgbtn02 btn_upload btn_common" @click="upload()"><img src="static/img/upload.png"/>上传</a>
      <a class="btn bgbtn02 btn_share btn_common" @click="shareSome()"><img src="static/img/share_white.png"/>分享</a>
      <input type="text" class='search' placeholder='请输入关键字' ref="input1">
      <img id="search_button"
           src="static/img/search.png"
           @click="searchRawLog()">
    </div>
    <div class='title'>
      <span class='title_left'>全部文件，共{{amount}}个</span>
      <span class='title_right'>关联文件</span>
    </div>
    <div id="log-list">
      <div class="list">
        <div><input type="checkbox" v-model="checkAll" id="文件名" value="文件名">
          <span>文件名</span></div>
        <div></div>
        <div>日期</div>
        <div>规范化日志</div>
        <div>事件日志</div>
      </div>
      <div class="list" v-for="(item,index) in items">
        <div><input type="checkbox" v-model="checked" :value="item.rawLog.id" @click="currClick(item,index)">
          <span @click="showDetail(index)" class="log-name">{{`${item.rawLog.logName}.${item.rawLog.format}`}}</span>
        </div>
        <div><img class="process_button" title="生成规范化日志" v-on:click="transferToNormal(index)"
                  src="static/img/process_color.png">
          <img class="download_button" title="下载" src="static/img/download_color.png" @click="download(index)"
               v-model="item.rawLog.id">
          <img class="share_button" title="分享" src="static/img/share_color.png" @click="share(index)">
          <img class="delete_button" title="删除" src="static/img/Delete_color.png" @click="deleteRawLog(index)">
        </div>
        <div>
          {{`${new Date(item.rawLog.createDate).getFullYear()}-${new Date(item.rawLog.createDate).getMonth() + 1}-${new Date(item.rawLog.createDate).getDate()}`}}
        </div><div>{{item.normalLog ? `${item.normalLog.logName}.${item.normalLog.format}` : '无'}}</div><div>{{item.eventLog ? `${item.eventLog.logName}.${item.eventLog.format}` : '无'}}</div>
      </div>
    </div>
  </div>
</template>

<style lang="less" scoped rel="stylesheet/less">
  @import '~assets/colors.less';
  @import "~assets/layout.less";

  .log-name, .download_button, .share_button, .delete_button, .process_button {
    cursor: pointer;
  }

  #head {
    display: flex;
    flex-direction: row;
    justify-content: space-around;
  }
  .raw-log-details{
    padding-top: 20px;
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
    position: relative;
    left: -40px;
    top: 5px;
  }

  .btn_common {
    color: white;
    font-size: 24px;
    text-decoration: none;
    height: @log_button_height;
    width: @log_button_width;
    border-radius: @log_button_border-radius;
    background-color: @main_green;
    img {
      width: 30px;
      height: 30px;
      vertical-align: text-top;
    }
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

  #log-list {
    margin-left: 10px;
    margin-right: 10px;
    .list {
      img {
        width: 30px;
        height: 30px;
        margin-right: 10px;
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
        items: []
      }
    },
    created(){
      this.$api({method: 'getRawLog'}).then((res) => {
        console.log(res);
        res.data.logGroups.map((log) => {
          this.items.push(log);
        })
      })
    },
    computed: {
      amount: function (item, index) {
        let sum = this.totalAmount.length;
        return sum;
      },
      checkAll: {
        get: function () {
          return this.checkedCount == this.items.length;
        },
        set: function (value) {
          var _this = this;
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
      showDetail: function (index) {
        this.$modal({
          type: 'log-detail',
          data: this.items[index].eventLog
        })
      },
      shareSome(){
        this.$api({method: 'shareRawLog', body: {idList: this.checked}}).then(res => {
          if (res.data.code === 1) {
            this.$hint('分享成功', 'success')
          } else {
            this.$hint('分享失败', 'warn')
          }

        })
      },
      upload: function () {
        this.$modal({type: 'upload', data: {type: 'raw'}}).then((res) => {
          console.log(res)
        })
      },
      deleteRawLog: function (index) {
        this.$api({method: 'deleteRawLog', opts: {body: {idList: [this.items[index].rawLog.id]}}}).then((res) => {
          if (parseInt(res.data.code) == 1) {
            this.$hint('删除成功', 'success');
          }
        })
      },
      share(index){
        if (this.items[index].rawLog.isShared === 0) {
          this.$api({method: 'shareRawLog', body: {idList: [this.items[index].rawLog.id]}}).then(res => {
            if (res.data.code === 1) {
              this.$hint('分享成功', 'success')
            } else {
              this.$hint('分享失败', 'warn')
            }
          })
        } else {
          this.$api({method: 'unShareRawLog', body: {idList: [this.items[index].rawLog.id]}}).then(res => {
            if (res.data.code === 1) {
              this.$hint('取消分享成功', 'success')
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
      searchRawLog: function () {
        console.log(this.$refs.input1.value);
        this.$api({method: 'searchRawLog', query: {keyWord: this.$refs.input1.value}}).then((res) => {
          this.items = [];
          res.data.logGroups.map((log) => {
            this.items.push(log);
          })
        })
      },
      currClick: function (item, index) {
        var _this = this;
        if (typeof item.checked == 'undefined') {
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
        this.amount = this.totalAmount.length;
      }
    }
  }
</script>
