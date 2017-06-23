<template>
<div>
  <div id="box">
    <div id="title">登陆</div>
    <el-form :model="ruleForm" :rules="rulesInput" ref="ruleForm" label-width="60px"
             id="LoginForm" style="position: absolute; margin:auto;padding-top: 20px">
      <el-form-item label="邮箱" prop="email">
        <el-input id="email" v-model="ruleForm.email"></el-input>
      </el-form-item>
      <el-form-item label="密码" prop="password">
        <el-input type="password" v-model="ruleForm.password"></el-input>
      </el-form-item>
      <el-form-item label="验证码" prop="check">
        <el-input size="small" id="checkCode" v-model="ruleForm.check"style="width: 40%"></el-input>
        <span style="padding: 10px;width: 40%;border:1px solid black">验证码的框</span>
      </el-form-item>

      <el-form-item>
        <el-button type="primary" @click="submitForm('ruleForm')">登陆</el-button>
        <el-button @click="jumpToRegister()">尚未注册？</el-button>
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
  let checkCode = (rule, value, callback) => {
    if (!value) {
      return callback(new Error('验证码不能为空'));
    }else {
      callback();
    }

  };
  let validateEmail = (rule, value, callback) => {
    if (value === '') {
      callback(new Error('请输入注册时填写的邮箱'));
    }else {
      callback();
    }
  };
  let validatePass = (rule, value, callback) => {
    if (value === '') {
      callback(new Error('请输入密码'));
    }else {
      callback();
    }
  };
  return {
    ruleForm: {
      email: '',
      password: '',
      check: ''
    },
    rulesInput: {
      email: [
        { validator: validateEmail, trigger: 'blur' }
      ],
      password: [
        { validator: validatePass, trigger: 'blur' }
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
      jumpToRegister() {
        //跳转到register
      },

    }

  }

</script>
