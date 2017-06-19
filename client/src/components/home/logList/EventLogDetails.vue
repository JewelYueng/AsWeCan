<template>
  <div class="event-log-details">

      <div>
        <a href="" class=" btn bgbtn02 btn_upload btn_common">
          <img src="static/upload.png"/>上传
        </a>
        <a href="" class="btn bgbtn02 btn_generate btn_common">
          <img src="static/process.png"/>挖掘
        </a>
        <a href="" class="btn bgbtn02 btn_download btn_common">
          <img src="static/download.png"/>下载
        </a>
        <a href="" class="btn bgbtn02 btn_share btn_common">
          <img src="static/share_white.png"/>分享
        </a>
        <input type="text" class='search' placeholder='输入关键字'>
      </div>
      <div class='title'>
        <br>
        <span class='title_left'>全部文件，共{{amount}}个</span>
        <span class='title_right'>关联文件</span>
      </div>
      <div>
        <ul>
          <li><input type='checkbox'  @click="selectAll">&nbsp;文件名&#12288;&#12288;&#12288;&#12288;&#12288;&#12288;&#12288;&#12288;&#12288;&#12288;&#12288;&#12288;日期&#12288;&#12288;&#12288;&#12288;&#12288;&#12288;原始日志&#12288;&#12288;&#12288;&#12288;&#12288;&#12288;规范化日志</li>
          <hr>
          <template v-for="item in items">
            <li><input type='checkbox' v-model="selectArr">{{item.log_name}}&#12288;&#12288;<img src="static/process_color.png"><img src="static/download_color.png"><img src="static/share_color.png">{{item.create_date}}&#12288;&#12288;&#12288;&#12288;&#12288;&#12288;{{item.normal_log}}&#12288;&#12288;&#12288;&#12288;&#12288;&#12288;{{item.event_log}}</li>
            <hr>
          </template>
        </ul>
      </div>
    </div>

</template>

<style lang="less" scoped rel="stylesheet/less">


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
            log_name:'first-event-log',
            create_date:'2017-3-12',
            normal_log:'first-raw-log',
            event_log:'first-normal-log'
          },
          {
            id:2,
            log_name:'second-event-log',
            create_date:'2017-4-12',
            normal_log:'second-raw-log',
            event_log:'second-normal-log'
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
