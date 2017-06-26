<template>
  <div class="upload">
    <div>
      <input type="file" ref="file">
    </div>
    <div>
      是否分享
      <el-switch
        v-model="share_status"
        on-color="#13ce66"
        off-color="#ff4949">
      </el-switch>
    </div>
    <div>
      <el-button type="primary" v-on:click="upload">上传</el-button>
      <el-button v-on:click="cancel">取消</el-button>
    </div>
    <el-progress :percentage="progress" v-show="progress !== 0"></el-progress>
  </div>
</template>

<style scoped lang="less" rel="stylesheet/less">
  .upload {
    background-color: whitesmoke;
    div {
      margin: 20px;
    }
  }

</style>

<script>
  import BaseBox from './BaseBox'
  const base_url = 'http://192.168.0.100:8080'
//  const base_url = ''
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
        progress: 0
      }
    },
    mixins: [BaseBox],
    methods: {
      upload(){
        let file_info = new FormData()
        file_info.append('isShare', Number(this.share_status))
        file_info.append('file', this.$refs.file.files[0])
        file_info.append('format', this.$refs.file.files[0].name.split('.').pop())
        console.log(file_info.get('format'))
        const _this = this
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
          }else if(res.status === 400){
            this.$hint('请上传格式正确的事件日志', 'warn')
          }
        }, err => {
          console.log('err:', err)
        })
      },
      cancel(){
        this.commit(true)
      },
    }
  }
</script>
