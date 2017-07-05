<template>
  <div id="home">
    <div class="header">
      <div class="nav">
      <div class="logo">
        <img src="static/img/logo.png" alt="K2" title="K2流程挖掘平台">
      </div>
      <el-menu theme="dark" :default-active="active_index" class="el-menu-demo" mode="horizontal" router>
        <el-menu-item index="/">日志文件管理</el-menu-item>
        <el-menu-item index="/merge">日志融合</el-menu-item>
        <el-menu-item index="/mining">流程挖掘</el-menu-item>
      </el-menu>
      </div>
      <div class="user-info">
        欢迎
        <div id="username">
          {{user? user.name: '游客'}}
          <i class="el-icon-edit" @click="modifyInfo"></i>
          <div class="logout" @click="logout">退出</div>
        </div>
        <div id="now">{{date}}</div>
      </div>
    </div>
    <router-view></router-view>
  </div>
</template>

<style rel="stylesheet/less" lang="less" scoped>
  @import "~assets/layout.less";
  @import "~assets/colors.less";

  #home{
    width: @major_width;
    margin: 0 auto;
  }
  .header {
    background-color: @dark_theme;
    width: 100%;
    box-sizing: border-box;
    display: flex;
    flex-direction: row;
    justify-content: space-between;
    .nav{
      display: flex;
      flex-direction: row;
    }
    .user-info{
      display: flex;
      flex-direction: row;
      color: #bfcbd9;
      line-height: 60px;
      font-size: 14px;
      margin-right: 30px;
      #username{
        margin: 0 30px;
        display: flex;
        flex-direction: row;
        justify-content: center;
        align-items: center;
        i{
          cursor: pointer;
          color: white;
          margin: 10px;
        }
        .logout{
          cursor: pointer;
          color: whitesmoke;
          text-decoration: underline;
        }
      }
    }
  }

  .logo {
    padding: 4px;
    min-width: 200px;
    img {
      width: 60px;
    }
  }


</style>

<script>
  export default{
    data(){
      return {
        msg: 'leftSide',
        user: {},
        date: null,
      }
    },
    components: {
    },
    methods: {
      modifyInfo(){
        console.log('modeifying')
        if(this.user){
          this.$modal({type: 'modify', data: {old_password: this.user.password}})
        }else{
          this.$hint('请先登录', 'warn')
          window.location.href = '/AssWeCan/home/loginPage'
        }
      },
      logout(){
        this.$api({method: 'logout'}).then(res => {
          if(res.status === 200){
            this.$hint('登出成功','success')
            window.location.href = '/AssWeCan/home/loginPage'
          }
        })
      }
    },
    created(){
      this.$api({method: 'getUser'}).then(res => {
        this.user = res.data.user
      })
      setInterval(()=> {
        let now = new Date()
        this.date = `${now.getFullYear()}年${now.getMonth()+1}月${now.getDate()}日 ${now.getHours()}:${now.getMinutes()}:${now.getSeconds()}`
      },1000)
    },
    computed: {
      active_index: function(){
        return this.$store.getters.home_path || '/'
      },

    }
  }
</script>
