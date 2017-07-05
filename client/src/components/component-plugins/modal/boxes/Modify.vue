<template>
  <div class="modify">
    <button @click="cancel" style="position: absolute;right: 0px;top: 5px;
       color: #324157;background-color: white;border: none;cursor: pointer">
      <i class="el-icon-close"></i>
    </button>
    <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="80px" class="demo-ruleForm">
      <el-form-item label="旧密码" prop="old">
        <el-input type="password" v-model="ruleForm.old" auto-complete="off"></el-input>
      </el-form-item>
      <el-form-item label="新密码" prop="new_pass">
        <el-input type="password" v-model="ruleForm.new_pass" auto-complete="off"></el-input>
      </el-form-item>
      <el-form-item label="确认密码" prop="check_pass">
        <el-input type="password" v-model="ruleForm.check_pass" auto-complete="off"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="submitForm('ruleForm')">提交</el-button>
        <el-button @click="resetForm('ruleForm')">重置</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>
<style rel="stylesheet/less" scoped lang="less">
  .modify {
    position: relative;
    background-color: white;
    border-radius: 20px;
    overflow: hidden;
    height: 300px;
    width: 400px;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
  }
</style>
<script>
  import BaseBox from './BaseBox'
  export default{
    mixins: [BaseBox],
    data() {
      let validatePass = (rule, value, callback) => {
        if (value === '') {
          callback(new Error('请输入密码'));
        } else {
          if (this.ruleForm.check_pass !== '') {
            this.$refs.ruleForm.validateField('check_pass');
          }
          callback();
        }
      }
      let validatePass2 = (rule, value, callback) => {
        if (value === '') {
          callback(new Error('请再次输入密码'));
        } else if (value !== this.ruleForm.new_pass) {
          callback(new Error('两次输入密码不一致!'));
        } else {
          callback();
        }
      }
      let validateOld = (rule, value, callback) => {
        if (value === '') {
          callback(new Error('请输入旧密码'));
        } else if (value !== this.data.old_password) {
          callback(new Error('旧密码输入错误!'));
        } else {
          callback();
        }
      }
      return {
        ruleForm: {
          old: '',
          new_pass: '',
          check_pass: ''
        },
        rules: {
          new_pass: [
            { validator: validatePass, trigger: 'blur' }
          ],
          check_pass: [
            { validator: validatePass2, trigger: 'blur' }
          ],
          old: [
            { validator: validateOld, trigger: 'blur' }
          ]
        }
      }
    },
    methods: {
      cancel(){
        this.commit(true)
      },
      submitForm(formName){
        this.$refs[formName].validate((valid) => {
          if (valid) {
            this.$api({method: 'modifyPass', body: {password: this.ruleForm.new_pass}}).then( res => {
              if(res.data.code === 200){
                this.$hint('修改成功,请重新登录','success')
                window.location.href = '/AssWeCan/home/loginPage'
              }
            })
          } else {
            console.log('error submit!!');
            return false;
          }
        });
      },
      resetForm(formName) {
        this.$refs[formName].resetFields();
      }
    }
  }
</script>
