<template>
  <div class="mining-list">
    <div class="title">流程挖掘</div>
    <hr>
    <div class="file_choose">
      <el-button type="primary" @click="chooseLog()">选择文件</el-button>
      <span>{{log.name}}</span>
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
      <el-button type="primary" @click="mining()">开始挖掘</el-button>
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
    margin: 30px;
  }

  .mining-list {
    min-height: 500px;
    padding: 10px;
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

  .file_choose{
    margin-left: 30px;
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
        log: {},
        methods: []
      }
    },
    methods: {
      ...mapActions(['changeHomePath']),
      change: function (m_index, p_key, min, max) {
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
      mining: function () {
        let selectedIndex = this.methods.findIndex( method => {
          return method.id === this.selectedId
        })
        let body_raw = {
          id: this.log.id,
          methodId: this.selectedId,
          parameters: this.send_params_arr[selectedIndex]
        }
        this.$api({
          method: 'mining',
          body: {
            id: this.log.id,
            methodId: this.selectedId,
            diagramType: 'ResourceRelation',
            parameters: this.send_params_arr[selectedIndex]
          }
        }).then( res => {
          if (res.status === 200) {
            console.log(res)
            this.$router.push({name: "miningResult", params: {raw_data: body_raw, resource_data: res.data.diagram}})
            this.$hint(`挖掘成功,耗时${parseInt(res.data.timeCost)/1000}秒`,'success')
          }else{
            this.$hint('不明原因失败，建议刷新','error')
          }
        }, err => {
          console.log(err)
          this.$hint(err.data.msg,'error')
        })
      },
      chooseLog(index){
        this.$modal({type: 'show-logs'}).then((res)=>{
          this.log = res
        })

      }
    },
    components: {},
    created(){
      this.changeHomePath('/mining')
      this.$api({method: 'getMiningMethods'}).then( res => {
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
    }


  }

</script>
