<template>
  <div class="raw-log-details">
    <div>
      <a href="" class=" btn bgbtn02 btn_upload btn_common">
        <img src="F:\实训\界面设计\icon\upload.png"/>上传
      </a>
      <a href="" class="btn bgbtn02 btn_generate btn_common">
        <img src="F:\实训\界面设计\icon\生成.png"/>生成
      </a>
      <a href="" class="btn bgbtn02 btn_download btn_common">
        <img src="F:\实训\界面设计\icon\download.png"/>下载
      </a>
      <a href="" class="btn bgbtn02 btn_share btn_common">
        <img src="F:\实训\界面设计\icon\share_white.png"/>分享
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
        <li><input type='checkbox'  @click="selectAll">&nbsp;文件名&#12288;&#12288;&#12288;&#12288;&#12288;&#12288;&#12288;&#12288;&#12288;&#12288;&#12288;&#12288;日期&#12288;&#12288;&#12288;&#12288;&#12288;&#12288;规范化日志&#12288;&#12288;&#12288;&#12288;&#12288;&#12288;事件日志</li>
        <hr>
        <template v-for="item in items">
          <li><input type='checkbox' v-model="selectArr">{{item.log_name}}&#12288;&#12288;<img src="F:\实训\界面设计\icon\生成_color.png"><img src="F:\实训\界面设计\icon\download_color.png"><img src="F:\实训\界面设计\icon\share_color.png">{{item.create_date}}&#12288;&#12288;&#12288;&#12288;&#12288;&#12288;{{item.normal_log}}&#12288;&#12288;&#12288;&#12288;&#12288;&#12288;{{item.event_log}}</li>
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
        selectArr: [],
        amount: 0,
        items:[
          {
            log_name:'first-log',
            create_date:'2017-1-1',
            normal_log:'first-normal-log',
            event_log:'first-event-log'
          },
          {
            log_name:'second-log',
            create_date:'2017-2-1',
            normal_log:'second-normal-log',
            event_log:'second-event-log'
          }
        ]
      }
    },
    methods:{
      selectAll(event){
        let  _this = this;
        console.log(event.currentTarget);
        if (!event.currentTarget.checked){
          this.selectArr = [];
        }
        else{
          _this.selectArr = [];
          _this.items.forEach(function(item, i) {
            _this.selectArr.push(i);
          });
        }
      }
    },
    watch:{
      selectArr:function(){
        this.amount=this.selectArr.length;
      }
    }
  }
</script>
