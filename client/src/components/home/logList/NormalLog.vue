<template>
  <div class="normal-log">
    <div class="head">
    <div class="button">生成</div>
    <div class="button">分享</div>
    <div class="search">请输入关键字</div>
    </div>
    <div class="log-list">
      <ul id="example">
        <li id="list-head"><label>全部文件共</label><label>number</label><label>关联文件</label></li>
        <li><input type="checkbox" id="文件名" value="文件名">
          <lable>日期</lable><lable>原始日志</lable><lable>事件日志</lable></li>
        <!--<li v-for="item in items">
          <input type="checkbox" id="" value="" >
          <label>{{item.name}}</label><label>{{item.date}}</label><label>{{item.name}}</label><lable>{{item.name}}</lable>
        </li>-->
      </ul>
    </div>
  </div>
</template>

<style lang="less" scoped rel="stylesheet/less">
  @import '~assets/colors.less';
  @import "~assets/layout.less";
  .normal-log{
    padding-top: 100px;
    background-color: lightgrey;
    left: 300px;
    width: 600px;
    height: 800px;
  }
  .head{
    display: flex;
    flex-direction: row;
    justify-content: space-around;
  }
  .search{
    margin-left: 100px;
    background-color: @tab_unselected;
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
  .log-list{
    justify-content: center
  }

</style>

<script>
  export default{
      data(){

      }
  }
</script>
