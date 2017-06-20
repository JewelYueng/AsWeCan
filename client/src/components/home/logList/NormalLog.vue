<template>
  <div class="normal-log">
    <div class="head">
    <a href="" class="button"><img src="">上传</a>
    <a href="" class="button"><img src="">分享</a>
    <input type="text" class="search" placeholder="请输入关键字"><img src="">
    </div>
    <div class="head-2"><span>全部文件，共{{amount}}个</span><span>关联文件</span></div>
    <ul id="log-list">
        <li id="head-3"><input type="checkbox" v-model="checkAll" id="文件名" value="文件名">
          <span>文件名</span><span>日期</span><span>原始日志</span><span>事件日志</span></li>
        <li class="list" v-for="item in items"><div>
          <input type="checkbox" v-model="checked" :value="item.id"  @click="currClick(item,$index)">
          <span>{{item.log_name}}</span></div>
          <span>{{item.create_date}}</span><span>{{item.rowLog_name}}</span><span>{{item.eventLog_name}}</span>
        </li>
    </ul>
  </div>
</template>

<style lang="less" scoped rel="stylesheet/less">
  @import '~assets/colors.less';
  @import "~assets/layout.less";
  .normal-log{
    padding-top: 20px;
  }
  .head{
    display: flex;
    flex-direction: row;
    justify-content: space-around;
  }
  .head-2{
    display: flex;
    flex-direction: row;
    justify-content: space-between;
    margin-left: 20px;
    margin-right: 180px;
    font-size: 20px;
  }
  .search{
    margin-left: 400px;
    background-color: @tab_separator;
    color: @main_font_color;
    width: @search_width;
    height: @search_height;
    border-radius: @search_border-radius;
    border: 1px solid @tab_color;
  }
  .button{
    color: white;
    font-size: 24px;
    height: @log_button_height;
    width: @log_button_width;
    border-radius: @log_button_border-radius;
    background-color: @main_green;

  }
 #head-3{
    list-style: none;
    border-bottom: 1px solid #afbfb8;
   span{
     margin-right: 200px;
   }
  }
  .list{
    list-style: none;
    border-bottom: 1px solid #afbfb8;
    padding: 10px 0px 10px 0px;
    margin-right: 20px;
    display: flex;
    flex-direction: row;
    justify-content: space-between;
  }


</style>

<script>
  export default{
    data(){
      return {
        checked:[],
        totalAmount:[],
        /*  checkAll:false,*/
        /* amount: 0,*/
        items:[
          {
            id:1,
            log_name:'first-log',
            create_date:'2017-1-1',
            rowLog_name:'first-row-log',
            eventLog_name:'first-event-log'
          },
          {
            id:2,
            log_name:'second-log',
            create_date:'2017-2-1',
            rowLog_name:'second-row-log',
            eventLog_name:'second-event-log'
          }
        ]
      }
    },
    computed:{
      amount:function(item,index){
        let sum = this.totalAmount.length;
        return sum;
      },
      checkAll: {
        get: function() {
          return this.checkedCount == this.items.length;
        },
        set: function(value){
          var _this = this;
          if (value) {
            this.totalAmount = [];
            this.checked = this.items.map(function(item) {
              item.checked = true;
              let total = item.id;
              _this.totalAmount.push(total);
              return item.id;
            })
          }else{
            this.checked = [];
            this.totalAmount=[];
            this.items.forEach(function(item,index){
              item.checked = false;
            });
          }
        }
      },
      checkedCount: {
        get: function() {
          return this.checked.length;
        }
      }
    },
    methods:{
      currClick:function(item,index){
        var _this = this;
        if(typeof item.checked == 'undefined'){
          this.$set(item,'checked',true);
          let total = item.id;
          this.totalAmount.push(total);
          console.log(this.totalAmount);
        }else{
          item.checked = !item.checked;
          if(item.checked){
            this.totalAmount = [];
            this.items.map(function(item,index){
              if(item.checked){
                let total = item.id;
                _this.totalAmount.push(total);
              }
            });
          }else{
            this.totalAmount = [];
            this.items.forEach(function(item,index){
              if(item.checked){
                let total = item.id;
                _this.totalAmount.push(total);
              }
            });
          }
        }
      }
    },
    watch:{
      checked:function(){
        this.amount=this.totalAmount.length;
      }
    }
  }
</script>
