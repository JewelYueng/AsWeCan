<template>
  <div>
    <div id="box">
      <div id="title">注册</div>
      <el-form :model="ruleForm" ref="ruleForm" label-width="70px"
               id="RegisterForm" style="padding-right: 40px;padding-top: 20px">
        <div style="padding-left: 20px">
          <el-form-item label="注册邮箱" prop="email"
                        :rules="[
      { message: '请输入邮箱地址', trigger: 'blur' },
      { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur,change' }
    ]"
          >
            <el-input id="email" v-model="ruleForm.email" style="width: 240px"></el-input>
          </el-form-item>
          <el-form-item label="用户名" prop="userName">
            <el-input id="userName" v-model="ruleForm.userName" style="width: 240px"></el-input>
          </el-form-item>
          <el-form-item label="密码" prop="pass">
            <el-input type="password" v-model="ruleForm.pass" auto-complete="off" style="width: 240px"></el-input>
          </el-form-item>
          <el-form-item label="确认密码" prop="checkPass">
            <el-input type="password" v-model="ruleForm.checkPass" auto-complete="off" style="width: 240px"></el-input>
          </el-form-item>
          <el-form-item label="验证码" prop="check" class="code">
            <el-input size="small" v-model="ruleForm.check" id="checkCode" style="width: 40%;right: 15px;vertical-align: top"></el-input>
            <div class="img">
              <div>
                <img :src='imgUrl' alt="验证码">
              </div>
              <div>
                <a @click="changeCode">看不清楚？</a>
              </div>
            </div>
          </el-form-item>
        </div>
      </el-form>
      <div class="register-btns">
        <el-button type="primary" @click="register">注册</el-button>
        <el-button @click="jumpToLogin">返回登陆页</el-button>
      </div>
    </div>
  </div>
</template>

<style lang="less" rel="stylesheet/less">
  #box {
    margin: 100px auto;
    height: 430px;
    width: 400px;
    border-radius: 5px;
    box-shadow: 0 0 1px 0 #8492a6;
    border: 1px solid #8492a6;
    background-color: white;
    padding: 20px 0 20px 20px;
  }

  #title {
    font-size: 18px;
    padding-top: 5px;
    padding-bottom: 6px;
    border-radius: 5px 5px 0 0;
  }

  .register-btns {

    button {
      width: 100px;
    }

  }

  .el-form-item__label {
    text-align: left;
  }
  .code {
    .el-form-item__error{
      top: 60%;
    }
    .img{
      div{
        height: 28px;
      }
      display: inline-block;
      a{
        position: relative;
        text-decoration: underline;
        cursor: pointer;
        color: #5f6e96;
      }
    }
  }

</style>

<script>
  export default{
    data(){
      let validateUserName = (rule, value, callback) => {
        if (value === '') {
          callback(new Error('用户名不能为空'));
        } else {
          callback();
        }
      };
      let validateEmail = (rule, value, callback) => {
        if (value === '') {
          callback(new Error('请输入邮箱地址'));
        } else {
          callback();
        }
      };
      let validatePass = (rule, value, callback) => {
        if (value === '') {
          callback(new Error('请输入密码'));
        } else {
          if (this.ruleForm.checkPass !== '') {
            this.$refs.ruleForm.validateField('checkPass');
          }
          callback();
        }
      };
      let validatePass2 = (rule, value, callback) => {
        if (value === '') {
          callback(new Error('请再次输入密码'));
        } else if (value !== this.ruleForm.pass) {
          callback(new Error('两次输入密码不一致!'));
        } else {
          callback();
        }
      };
      let checkCode = (rule, value, callback) => {
        if (!value) {
          return callback(new Error('验证码不能为空'));
        } else {
          callback();
        }
      };
      return {
        imgUrl: '',
        ruleForm: {
          email: '',
          userName: '',
          pass: '',
          checkPass: '',
          check: ''
        },
        rulesInput: {
          email: [
            {validator: validateEmail, trigger: 'blur'}
          ],
          UserName: [
            {validator: validateUserName, trigger: 'blur'}
          ],
          pass: [
            {validator: validatePass, trigger: 'blur'}
          ],
          checkPass: [
            {validator: validatePass2, trigger: 'blur'}
          ],
          check: [
            {validator: checkCode, trigger: 'blur'}
          ]
        }
      };
    },
    created(){
      this.imgUrl = "http://116.56.129.93:8088/AssWeCan/home/code?date=" + Date()
    },
    methods: {
      changeCode(){
        this.imgUrl = "http://116.56.129.93:8088/AssWeCan/home/code?date=" + Date()
      },
      register() {
        this.$api({
          method: 'register', body: {
            name: this.ruleForm.userName,
            password: this.ruleForm.pass,
            email: this.ruleForm.email,
          }
        }).then((res) => {
          console.log(res.data)
          this.$hint("已发送激活邮件，请尽快激活", 'success')

          this.$router.push({name: 'home'})
        }, err => {
          this.$hint(err.data.message, 'error')
        })
      },
      jumpToLogin() {
        this.$router.push({path: './login'})
      },
    }

  }

</script>
