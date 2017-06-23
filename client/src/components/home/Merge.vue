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
      <div v-for="params in item.parameters">
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
      <el-button type="success" @click="merge()">开始融合</el-button>
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
    border: 2px solid green;
  }

  .merge-list {
    height: @main_height;
    background-color: #f1f1f1;
    border-top-left-radius: 20px;
    border-top-right-radius: 20px;
    border-bottom-right-radius: 0px;
    border-bottom-left-radius: 0px;
    overflow: hidden;
  }

  .title {
    width: auto;
    font-size: 20px;
    font-weight: bold;
    color: green;
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
    border-top: 2px solid green;
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
        methods: [
//          {
//            "id": "1",
//            "state": 1,
//            "parameters": [
//              {
//                "type": "Double",
//                "key": "relativeToBestThreshold",
//                "name": "相对频度阈值",
//                "description": "相对频度阈值des",
//                "defaultVal": 0.9,
//                "maxVal": 1.0,
//                "minVal": 0.0
//              },
//              {
//                "type": "Double",
//                "key": "dependencyThreshold",
//                "name": "依赖度阈值",
//                "description": "依赖度阈值des",
//                "defaultVal": 0.9,
//                "maxVal": 1.0,
//                "minVal": 0.0
//              },
//              {
//                "type": "Double",
//                "key": "l1lThreshold",
//                "name": "一元循环阈值",
//                "description": "一元循环阈值des",
//                "defaultVal": 0.9,
//                "maxVal": 1.0,
//                "minVal": 0.0
//              },
//              {
//                "type": "Double",
//                "key": "l2lThreshold",
//                "name": "二元循环阈值",
//                "description": "二元循环阈值des",
//                "defaultVal": 0.9,
//                "maxVal": 1.0,
//                "minVal": 0.0
//              },
//              {
//                "type": "Enum",
//                "key": "isLoop",
//                "name": "是否循环",
//                "description": "是否循环des",
//                "defaultVal": "FALSE",
//                "values": ["TRUE", "FALSE"]
//              }
//            ],
//            "name": "启发式算法",
//            "description": "这是启发式算法的描述",
//            "key": "heuristics"
//          },
//          {
//            "id": "2",
//            "state": 1,
//            "parameters": [
//              {
//                "type": "Double",
//                "key": "crossRate",
//                "name": "交叉率",
//                "description": "交叉率des",
//                "defaultVal": 0.8,
//                "maxVal": 1.0,
//                "minVal": 0.0
//              },
//              {
//                "type": "Double",
//                "key": "mutationRate",
//                "name": "变异率",
//                "description": "变异率des",
//                "defaultVal": 0.2,
//                "maxVal": 1.0,
//                "minVal": 0.0
//              },
//              {
//                "type": "Double",
//                "key": "endFitness",
//                "name": "终止适应度值",
//                "description": "终止适应度值des",
//                "defaultVal": 0.9,
//                "maxVal": 1.0,
//                "minVal": 0.0
//              },
//              {
//                "type": "Integer",
//                "key": "populationSize",
//                "name": "种群规模",
//                "description": "种群规模des",
//                "defaultVal": 10,
//                "maxVal": 1000,
//                "minVal": 0
//              },
//              {
//                "type": "Integer",
//                "key": "maxGeneration",
//                "name": "最大遗传代数",
//                "description": "最大遗传代数des",
//                "defaultVal": 10,
//                "maxVal": 1000,
//                "minVal": 0
//              }],
//            "name": "遗传算法", "description": "这是遗传算法的描述", "key": "genetic"
//          }
        ]

      }
    },
    created(){
      this.methods.map((method) => {
        let param = {}
        method.parameters.map((params) => {
          param[params.key] = params.defaultVal
        })
        this.send_params_arr.push(param)
      })
      this.$api({method: 'getMergeMethods'}).then( res => {
        console.log(res)
        this.methods = res.data.methods
      })
    },
    methods: {
      ...mapActions(['jumpView']),
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
//        this.jumpView('/home/mergeresult')
        this.$api({
          method: 'merge', body: {
            eventLogId1: this.log1.id,
            eventLogId2: this.log2.id,
            methodId: this.selectedId,
            parameters: this.send_params_arr[parseInt(this.selectedId)]
          }
        }).then(res => {
          console.log(res)
          if(res.status === 200) {
            this.$router.push({name: 'mergeResult', params: {log: res.data}})
          }else{
            this.$hint('融合失败：' + res.data.msg, 'warn')
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
