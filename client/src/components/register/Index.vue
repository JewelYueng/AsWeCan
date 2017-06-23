<template>
  <div>
    <div id="box">
      <div id="title">注册</div>
      <el-form :model="ruleForm" ref="ruleForm" label-width="70px"
               id="LoginForm" style="padding-right: 40px;padding-top: 20px">
        <el-form-item label="注册邮箱" prop="email"
                      :rules="[
      { required: true, message: '请输入邮箱地址', trigger: 'blur' },
      { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur,change' }
    ]"
        >
          <el-input id="email" v-model="ruleForm.email"></el-input>
        </el-form-item>
        <el-form-item label="用户名" prop="userName">
          <el-input id="userName" v-model="ruleForm.userName"></el-input>
        </el-form-item>
        <el-form-item label="密码" prop="pass">
          <el-input type="password" v-model="ruleForm.pass" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="确认密码" prop="checkPass">
          <el-input type="password" v-model="ruleForm.checkPass" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="验证码" prop="check">
          <el-input size="small" v-model="ruleForm.check" id="checkCode" style="width: 40%"></el-input>
          <span style="padding: 10px;width: 40%;border:1px solid black">验证码的框</span>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="submitForm('ruleForm')">注册</el-button>
          <el-button @click="jumpToLogin">返回登陆页</el-button>
        </el-form-item>
      </el-form>


    </div>
  </div>
</template>

<style>
  #box{
    position: absolute;
    right: 80px;
    top: 100px;
    height: 400px;
    width: 300px;
    border-radius: 5px;
    box-shadow:0 0 1px 0 #8492a6;
  //border: 1px solid #8492a6;
    background-color: #e5e9f2;
  }
  #title{
    font-size: 24px;
    padding-top: 5px;
    padding-bottom: 6px;
    border-bottom: 1px solid #8492a6;
    border-radius: 5px 5px 0 0;
    background-color: #99a9bf
  }
</style>

<script>
  export default{
    data(){
      let validateUserName = (rule, value, callback) => {
        if (value === '') {
          callback(new Error('用户名不能为空'));
        }else {
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
        }else {
          callback();
        }
      };
      return {

        ruleForm: {
          email: '',
          userName: '',
          pass: '',
          checkPass: '',
          check: ''
        },
        rulesInput: {
          email: [
            { validator: validateEmail, trigger: 'blur' }
          ],
          UserName: [
            { validator: validateUserName, trigger: 'blur' }
          ],
          pass: [
            { validator: validatePass, trigger: 'blur' }
          ],
          checkPass: [
            { validator: validatePass2, trigger: 'blur' }
          ],
          check: [
            { validator: checkCode, trigger: 'blur' }
          ]
        }
      };

    },
    methods: {
      submitForm(formName) {
        this.$refs[formName].validate((valid) => {
          if (valid) {
            alert('submit!');
          } else {
            console.log('error submit!!');
            return false;
          }
        });
      },
      jumpToLogin() {
        //跳转到login
      },
    }

  }

</script>
