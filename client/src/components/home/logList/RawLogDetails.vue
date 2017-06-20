<template>
  <div class="raw-log-details">
    <div>
      <a href="" class=" btn bgbtn02 btn_upload btn_common">
        <img src="static/img/upload.png"/>上传
      </a>
      <a href="" class="btn bgbtn02 btn_share btn_common">
        <img src="static/img/share_white.png"/>分享
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
        <li><input type='checkbox' v-model="checkAll">&nbsp;文件名日期规范化日志事件日志</li>
        <hr>
        <template v-for="item in items">
          <li><input type='checkbox' v-model="checked"  :value="item.id"  @click="currClick(item,$index)">{{item.log_name}}&#12288;&#12288;<img src="static/img/generator_color.png"><img src="static/img/download_color.png"><img src="static/img/share_color.png">{{item.create_date}}&#12288;&#12288;&#12288;&#12288;&#12288;&#12288;{{item.normal_log}}&#12288;&#12288;&#12288;&#12288;&#12288;&#12288;{{item.event_log}}</li>
          <hr>
        </template>
      </ul>
    </div>
  </div>
</template>

<style lang="less" scoped rel="stylesheet/less">
  a{
    text-decoration: none;
    font-size: 36px;
  }

  .btn_common{
    height: auto;
    width: auto;
    display: inline-block;
    margin-top: 10px;
    padding: 10px 24px;
    background-color: #b5b5b5;
    color: #fff;
    cursor: pointer;
  }

  .btn_upload{
    border-radius: 10px;
    margin-right: 20px;
    position:relative;
    left:10px;
    top:10px;
  }

  .btn_generate{
    border-top-left-radius:10px;
    border-top-right-radius:0px;
    border-bottom-right-radius:0px;
    border-bottom-left-radius:10px;
    margin-right: -3px;
    position:relative;
    left:20px;
    top:10px;
  }

  .btn_download{
    position:relative;
    left:20px;
    top:10px;
    border-radius: 0px;
    margin-right: -4px;
  }
  .btn_share{
    position:relative;
    left:20px;
    top:10px;
    border-top-left-radius:0px;
    border-top-right-radius:10px;
    border-bottom-right-radius:10px;
    border-bottom-left-radius:0px;

  }

  .btn:hover{
    background-color: #d3d3d3;
  }
  .bgbtn02 img{
    margin-bottom: -3px;
    margin-right: 10px;
  }

  .search{
    border-radius:10px;
    outline:none;
    position:absolute;
    right:20px;
    top:28px;
    /*height: 30px;*/
    /*width: auto;*/
    display: inline-block;
  }

  .title{
    font-size: 15px;
    font-weight: bold;
  }
  .title_left{
    position: relative;
    left: 3%;
  }
  .title_right{
    position: relative;
    left: 25%;
  }


  ul{
    list-style-type: none;
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
            normal_log:'first-normal-log',
            event_log:'first-event-log'
          },
          {
            id:2,
            log_name:'second-log',
            create_date:'2017-2-1',
            normal_log:'second-normal-log',
            event_log:'second-event-log'
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
