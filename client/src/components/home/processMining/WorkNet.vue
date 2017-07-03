<template>
  <div class="workNet">
    <h1>Work Flow Net</h1>
    <svg class="chart" width="1130" height="500">
    </svg>
  </div>
</template>

<style type="text/css" rel="stylesheet/less" lang="less">

  .workNet {
    display: flex;
    flex-direction: column;
    .show {
      text-align: center;
    }

    .node rect {
      cursor: move;
      stroke: #333;
      fill: #fff;
    }

    .node text {
      pointer-events: none;
      color: #82a6da;
      text-shadow: 0 1px 0 #fff;
    }

    circle {
      fill: #c0c0c0;
    }

    .link {
      fill: none;
      stroke: #ffa21d;
      stroke-opacity: .2;
    }

    .link:hover {
      stroke-opacity: .5;
    }

    .edgePath path {
      stroke: #516082;
      fill: #516082;
      stroke-width: 2px;
    }

    path:hover {
      stroke-opacity: 0.9;
    }
  }


</style>

<script>

  export default{
    props: ['petri'],
    data(){
      return {
        items: {

          "timeCost": 439,
          "diagram": {
            "netElementList": [
              {
                "element": "Activity0",
                "isEnd": "false",
                "isStart": "false",
                "name": "time-out X",
                "output": ["decide"],
                "subnetList": [1],
                "subnetNum": 1,
                "time": "439"
              },
              {
                "element": "Activity1",
                "isEnd": "false",
                "isStart": "false",
                "name": "collect reviews",
                "output": ["collect reviews", "decide"],
                "subnetList": [2],
                "subnetNum": 1,
                "time": "439"
              },
              {
                "element": "Activity2",
                "isEnd": "false",
                "isStart": "false",
                "name": "decide",
                "output": ["accept", "decide", "invite additional reviewer", "reject"],
                "subnetList": [4],
                "subnetNum": 1,
                "time": "439"
              },
              {
                "element": "Activity3",
                "isEnd": "false",
                "isStart": "false",
                "name": "get review 1",
                "output": ["collect reviews"],
                "subnetList": [1],
                "subnetNum": 1,
                "time": "439"
              },
              {
                "element": "Activity4",
                "isEnd": "false",
                "isStart": "false",
                "name": "get review 2",
                "output": ["collect reviews"],
                "subnetList": [1],
                "subnetNum": 1,
                "time": "439"
              },
              {
                "element": "Activity5",
                "isEnd": "false",
                "isStart": "false",
                "name": "get review 3",
                "output": ["collect reviews"],
                "subnetList": [1],
                "subnetNum": 1,
                "time": "439"
              },
              {
                "element": "Activity6",
                "isEnd": "false",
                "isStart": "false",
                "name": "get review X",
                "output": ["decide"],
                "subnetList": [1],
                "subnetNum": 1,
                "time": "439"
              },
              {
                "element": "Activity7",
                "isEnd": "false",
                "isStart": "false",
                "name": "invite additional reviewer",
                "output": ["get review X", "invite additional reviewer", "time-out X"],
                "subnetList": [3],
                "subnetNum": 1,
                "time": "439"
              },
              {
                "element": "Activity8",
                "isEnd": "true",
                "isStart": "false",
                "name": "accept",
                "output": ["accept"],
                "subnetList": [1],
                "subnetNum": 1,
                "time": "439"
              },
              {
                "element": "Activity9",
                "isEnd": "true",
                "isStart": "false",
                "name": "reject",
                "output": ["reject"],
                "subnetList": [1],
                "subnetNum": 1,
                "time": "439"
              },
              {
                "element": "Activity10",
                "isEnd": "false",
                "isStart": "false",
                "name": "time-out 1",
                "output": ["collect reviews"],
                "subnetList": [1],
                "subnetNum": 1,
                "time": "439"
              },
              {
                "element": "Activity11",
                "isEnd": "false",
                "isStart": "false",
                "name": "time-out 2",
                "output": ["collect reviews"],
                "subnetList": [1],
                "subnetNum": 1,
                "time": "439"
              },
              {
                "element": "Activity12",
                "isEnd": "false",
                "isStart": "false",
                "name": "time-out 3",
                "output": ["collect reviews"],
                "subnetList": [1],
                "subnetNum": 1,
                "time": "439"
              },
              {
                "element": "Activity13",
                "isEnd": "false",
                "isStart": "true",
                "name": "invite reviewers",
                "output": ["get review 1", "invite reviewers", "time-out 1", "get review 2", "invite reviewers", "time-out 2", "get review 3", "invite reviewers", "time-out 3"],
                "subnetList": [3, 3, 3],
                "subnetNum": 3,
                "time": "439"
              }
            ]
          }
        }
      }
    },
    created(){
//      深复制
      this.items.diagram = JSON.parse( JSON.stringify(this.petri))
    },
    mounted(){
      this.renderWorkflow()
    },
    methods: {
      renderWorkflow(){
// Create a new directed graph
        let g = new dagreD3.graphlib.Graph().setGraph({
//          是否需要横向布局
          rankdir: "LR"
        });
        for (let index in this.items.diagram.netElementList) {
          let data = this.items.diagram.netElementList[index];
          let name = data.name;
// 先设置节点
          data.label = name;
          data.rx = data.ry = 5;
          g.setNode(name, data);
// 此处将output转化为二维数组
          let subnetNum = data.subnetNum;
          let subnetList = data.subnetList;
          let output = translate(data.output, subnetNum, subnetList);
          let isStart = data.isStart;
          let isEnd = data.isEnd;
          let kusuo;
          console.log(data)
// 开始画库所和边
          if (isStart === "true") {
// 设置开所
            g.setNode("start", {
              label: "start",
              shape: "circle"
            });
            g.setEdge("start", data.name, {
              label: "",
              lineInterpolate: 'basis',
              width: 30
            });
          }
          if (isEnd === "true") {
// 设置结束库所
            g.setNode("end", {
              label: "end",
              shape: "circle"
            });
            g.setEdge(name, "end", {
              label: "",
              lineInterpolate: 'basis',
              width: 30
            });
          } else {
// 如果输出不为空，则先画source到库所的边
            for (let i in output) {
              kusuo = output[i].join("").toUpperCase();
              g.setNode(kusuo, {
                label: "",
                shape: "circle",
              });
              g.setEdge(name, kusuo, {
                label: "",
                lineInterpolate: 'basis',
                width: 30
              });
// 库所到output的边
              for (let j in output[i]) {
                g.setEdge(kusuo, output[i][j], {
                  label: "",
                  lineInterpolate: "basis",
                  width: 30
                });
              }
            }
          }
        }
// 一维数组转化为二维数组
        function translate(arr, col, row) {
          let arr1 = [];
          for (let i = 0; i < col; i++) {
            let arr2 = [];
            for (let j = 0; j < row[i]; j++) {
              if (arr != null)
                arr2[j] = arr.shift();
            }
            arr1.push(arr2);
          }
          return arr1;
        }

// Create the renderer
        let render = new dagreD3.render();

// Set up an SVG group so that we can translate the final graph.
        let svg = d3.select('svg'),
          inner = svg.append("g");


// Set up zoom support
        let zoom = d3.behavior.zoom().on("zoom", function () {
          inner.attr("transform", "translate(" + d3.event.translate + ")" +
            "scale(" + d3.event.scale + ")");
        });
        svg.call(zoom).on("dblclick.zoom", null);

        inner.on("dblclick", function (d) {
          document.getElementById("attr").innerHTML = "双击事件";
        });

// Run the renderer. This is what draws the final graph.
        render(inner, g);
// Center the graph
        let initialScale = 0.8;
        zoom
          .translate([(svg.attr("width") - g.graph().width * initialScale) / 2, 150])
          .scale(initialScale)
          .event(svg);
        svg.attr('height', 700);
      }

    }

  }
</script>
