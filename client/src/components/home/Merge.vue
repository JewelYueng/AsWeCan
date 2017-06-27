<template>
  <div class="merge-list">
    <div class="title">日志融合</div>
    <hr>
    <div class="file1_choose">
      <el-button type="primary" @click="chooseLog(1)">选择文件1</el-button>
      <span>{{log1.name}}</span>
    </div>
    <div class="file2_choose">
      <el-button type="primary" @click="chooseLog(2)">选择文件2</el-button>
      <span>{{log2.name}}</span>
    </div>
    <hr>
    <div class="algorithm_choose">
      <el-select v-model="selectedId" placeholder="请选择算法类型">
        <el-option
          v-for="item in methods"
          :key="item.id"
          :label="item.name"
          :value="item.id">
        </el-option>
      </el-select>
    </div>

    <div class="para" v-for="(item,itemIndex) in methods" v-if="item.id === selectedId">
      <div v-for="params in item.paramters">
        <br>{{params.name}}:
        <el-input size="small" type="number" v-if="params.type!='Enum'" :min="params.minVal" :max="params.maxVal"
                  v-model="send_params_arr[itemIndex][params.key]"
                  @blur="change(itemIndex, params.key, params.minVal,params.maxVal)">

        </el-input>
        <el-select v-model="send_params_arr[itemIndex][params.key]" v-if="params.type=='Enum'">
          <el-option
            v-for="item in params.values"
            :key="item"
            :label="item"
            :value="item"></el-option>
        </el-select>
      </div>
      <br>
      <el-button type="primary" @click="merge()">开始融合</el-button>
    </div>


  </div>
</template>
<style lang="less" scoped rel="stylesheet/less">
  @import "~assets/colors.less";
  @import "~assets/layout.less";

  .para {
    margin: auto;
    width: 30%;
  }

  span {
    margin-left: 20px;
  }

  a {
    text-decoration: none;
    text-align: left;

  }

  .button {
    color: bisque;
    font-size: 24px;
    height: @log_button_height;
    width: @log_button_width;
    border-radius: @log_button_border-radius;
    background-color: white;
    border: 2px solid @dark_theme;
  }

  .merge-list {
    height: @main_height;
    background-color: @light_theme;
    overflow: hidden;
  }

  .title {
    width: auto;
    font-size: 20px;
    font-weight: bold;
    color: @dark_theme;
    margin-left: 30px;
    margin-top: 5px;
    text-align: left;
  }

  .file1_choose, .file2_choose {
    margin-left: 50px;
    margin-top: 5px;
    margin-bottom: 10px;
    text-align: left;

  }

  hr {
    height: 2px;
    border: none;
    border-top: 2px solid @dark_theme;
  }


</style>
<script>
  import {mapActions} from 'vuex'
  export default{
    data(){
      return {

        selectedId: "",
        isActive: true,
        msg: 'hello vue',
        send_params_arr: [],
        log1: {},
        log2: {},
        methods: []

      }
    },
    created(){
      this.changeHomePath('/merge')
      this.$api({method: 'getMergeMethods'}).then( res => {
        console.log(res)
        this.methods = res.data.methods
        this.methods.map((method) => {
          let param = {}
          method.paramters.map((params) => {
            param[params.key] = params.defaultVal
          })
          this.send_params_arr.push(param)
        })
      })

    },
    methods: {
      ...mapActions(['jumpView','changeHomePath']),
      change: function (m_index, p_key, min, max) {
//  console.log(event.target.value);
        let send_data = this.send_params_arr[m_index][p_key]

        if (parseFloat(send_data) <= min) {
          this.send_params_arr[m_index][p_key] = min;
        }
        else if (parseFloat(send_data) >= max) {
          this.send_params_arr[m_index][p_key] = max;
        }
        else if (isNaN(parseFloat(send_data))) {
          this.send_params_arr[m_index][p_key] = 0;
        }
      },
      merge: function () {
        let selectedIndex = this.methods.findIndex( method => {
          return method.id === this.selectedId
        })
        this.$api({
          method: 'merge', body: {
            eventLogId1: this.log1.id,
            eventLogId2: this.log2.id,
            methodId: this.selectedId,
            parameters: this.send_params_arr[selectedIndex]
          }
        }).then(res => {
          console.log(res)
          if(res.status === 200) {
            this.$router.push({name: 'mergeResult', params: {log: res.data}})
          }
        }, err => {
          if(err.status === 500){
            this.$hint('参数设置不当', 'warn')
          }
        })

      },
      chooseLog(index){
        this.$modal({type: 'show-logs'}).then((res)=>{
          this[`log${index}`] = res
        })

      }

    }
  }

</script>
