<template>
  <div class="upload">
    <button @click="cancel" style="position: absolute;right: 0px;top: 5px;
       color: #324157;background-color: white;border: none;cursor: pointer"><i class="el-icon-close"></i>
    </button>
    <form action="" class="activityForm">
      　　<div class="file">
      <div class="userdefined-file">
        　　<label class="button" for="file">选择文件</label>
        <label class="text" name="userdefinedFile" id="userdefinedFile" >{{fileName}}</label>
      </div>
      <input type="file" name="file" id="file" ref="file" v-on:change="fileChange">
      　　</div>
    </form>
    <div style="padding: 20px;margin-top: 20px" class="btns">
      是否分享
      <el-switch
        v-model="share_status"
        on-color="#20A0FF"
        off-color="#c8c8c8"
        :disabled="is_uploading">
      </el-switch>
    </div>
    <div class="btns">
      <el-button type="primary" v-on:click="upload">上传</el-button>
      <el-button v-on:click="cancel">取消</el-button>
    </div>
    <el-progress :percentage="progress" v-show="progress !== 0"></el-progress>
  </div>
</template>

<style scoped lang="less" rel="stylesheet/less">
  .upload {
    display: flex;
    flex-flow: column;
    justify-content: flex-start;
    position: relative;
    background-color: white;
    width: 500px;
    box-shadow: 0 0 3px 0 #324157;
    border-radius: 5px;
    padding: 20px;
  }

  .file {
    position: relative;
    height: 40px;
    line-height: 40px;
  }
  .file label {
    display: inline-block;
  }

  .userdefined-file {
    display: flex;
    flex-flow: column;
    justify-content: flex-start;
    position: absolute;
    top: 0;
    left: 20px;
    z-index: 2;
    width: 380px;
    height: 30px;
    line-height: 30px;
    font-size: 0;  /*应对子元素为 inline-block 引起的外边距*/
  }
  .text {
    display: inline-block;
    vertical-align: middle;
    width: 450px;
    height: 50px;
    line-height: 40px;
    font-size: 14px;
    text-align: left;
    text-overflow: ellipsis;
    white-space: nowrap;
    border: none;
  }
  .button {
    display: inline-block;
    vertical-align: middle;
    width: 80px;
    border-radius: 5px;
    text-align: center;
    height: 40px;
    line-height: 40px;
    font-size: 14px;
    background-color: #20A0FF;
    border: none;
    color: white;
    cursor: pointer;
  }
  .file input[type="file"] {
    position: absolute;
    top: 0;
    left: 60px;
    z-index: 3;
    opacity: 0;
    visibility: hidden;
    width: 300px;
    height: 40px;
    line-height: 40px;
    cursor: pointer;
  }

</style>

<script>
  import BaseBox from './BaseBox'
  const base_url = 'http://116.56.129.93:8088/AssWeCan'
//  const base_url = ""
//  const base_url = "http://192.168.0.100:8080"
  const type_map = {
    'raw': base_url + '/rawLog/upload',
    'normal': base_url + '/normalLog/upload',
    'event': base_url + '/eventLog/upload'
  }
  export default {
    data(){
      return {
        share_status: false,
        file: new FormData(),
        progress: 0,
        is_uploading: false,
        fileName: '未选择任何文件'
      }
    },
    mixins: [BaseBox],
    methods: {
      fileChange(){
        this.fileName= this.$refs.file.value
      },
      upload(){
        let file_info = new FormData()
        file_info.append('isShare', Number(this.share_status))
        file_info.append('file', this.$refs.file.files[0])
        file_info.append('format', this.$refs.file.files[0].name.split('.').pop())
        const _this = this
        this.is_uploading = true
        this.$http.post(type_map[this.data.type], file_info, {
          contentType: 'multipart/form-data',
          progress(e) {
            if (e.lengthComputable) {
              _this.progress = parseInt(e.loaded / e.total  * 100);
            }
          }

        }).then(res => {
//          this.totalAmount=[]
//          this.checkedAll=false
//          this.checked=[]
          console.log('success', res)
          if (res.body.code === 1){
            this.commit(true)
          }
        }, err => {
          console.log(err)
          this.$hint(err.data.msg,'error')
        })
      },
      cancel(){
        this.commit(true)
      },
    }
  }
</script>
