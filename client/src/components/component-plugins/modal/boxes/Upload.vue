<template>
  <div class="upload">
    <form :action=type_map[data.type] method="post"
          enctype="multipart/form-data">
      <input type="text" name="format"/>
      <input type="text" name="isShare"/>
      上传文件：<input type="file" name="file">
      <input type="submit" value="确定">
    </form>
    <div>
      <input type="file" ref="file">
    </div>
    <div>
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
  const base_url = 'http://192.168.0.104:8080'
  const type_map = {
    'raw': base_url + '/rawLog/upload',
    'normal': base_url + '/normalLog/upload',
    'event': base_url + '/eventLog/upload'
  }
  export default {
    data(){
      return {
        type_map: {
          'raw': base_url + '/rawLog/upload',
          'normal': base_url + '/normalLog/upload',
          'event': base_url + '/eventLog/upload'
        },
        share_status: false,
        file: new FormData(),
        fileList: []
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
        this.$http.post(type_map[this.data.type], file_info, {
          contentType: 'multipart/form-data'
        }).then(res => {
          console.log('success', res)
        }, err => {
          console.log('err:', err)
        })
      },
      cancel(){
        this.commit(true)
      },
      handlePreview(file){
        console.log(file)
      },
      handleRemove(file, fileList){
        console.log(file, fileList)
      }
    }
  }
</script>
