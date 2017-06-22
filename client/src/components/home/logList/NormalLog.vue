<template>
  <div class="normal-log">
    <div class="head">
      <a href="" class="button"><img src="static/img/upload.png">上传</a>
      <a href="" class="button"><img src="static/img/share_white.png">分享</a>
      <input type="text" class="search" placeholder="请输入关键字"><img id="search_button" src="static/img/search.png">
    </div>
    <div class="head-2"><span>全部文件，共{{amount}}个</span><span>关联文件</span></div>
    <div id="log-list">
      <div class="list">
        <div><input type="checkbox" v-model="checkAll" id="文件名" value="文件名">
          <span>文件名</span></div>
        <div></div>
        <div>日期</div>
        <div>原始日志</div>
        <div>事件日志</div>
      </div>
      <div class="list" v-for="(item,index) in items">
        <div><input type="checkbox" v-model="checked" :value="item.id" @click="currClick(item,index)">
          <span>{{`${item.normalLog.logName}.${item.normalLog.format}`}}</span></div>
        <div><img class="process_button" title="生成事件日志" v-on:click="tranferToEvent(index)"
                  src="static/img/process_color.png">
          <img class="download_button" title="下载" src="static/img/download_color.png">
          <img class="share_button" title="分享" src="static/img/share_color.png"></div>
        <div>{{new Date(item.normalLog.createDate).toString()}}</div>
        <div>{{item.rawLog ? `${item.rawLog.logName}.${item.rawLog.format}` : '无'}}</div>
        <div>{{item.eventLog ? `${item.eventLog.logName}.${item.eventLog.format}` : '无'}}</div>
      </div>
    </div>
  </div>
</template>

<style lang="less" scoped rel="stylesheet/less">
  @import '~assets/colors.less';
  @import "~assets/layout.less";

  .normal-log {
    padding-top: 20px;
  }

  .head {
    display: flex;
    flex-direction: row;
    justify-content: space-around;
  }

  .head-2 {
    display: flex;
    flex-direction: row;
    justify-content: space-between;
    margin-left: 20px;
    margin-right: 210px;
    font-size: 20px;
  }

  .search {
    margin-left: 400px;
    background-color: @tab_separator;
    color: @main_font_color;

    text-align: center;
    width: @search_width;
    height: @search_height;
    border-radius: @search_border-radius;
    border: 1px solid @tab_color;
    outline-style: none;
  }

  #search_button {
    width: 20px;
    height: 20px;
    position: relative;
    left: -40px;
    top: 5px;
  }

  .button {
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
        /*  checkAll:false,*/
        /* amount: 0,*/
        items: []
      }
    },
    created(){
      this.$api({method: 'getNormalLog'}).then((res) => {
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
          const _this = this;
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
      currClick: function (item, index) {
        const _this = this;
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
      tranferToEvent: function (index) {
        console.log(index)
      }
    },
    watch: {
      checked: function () {
        this.amount = this.totalAmount.length;
      }
    }
  }
</script>
