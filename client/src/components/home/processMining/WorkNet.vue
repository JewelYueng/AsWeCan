<template>
  <div class="workNet">
    <h1>Work Flow Net</h1>
    <div class="show">
      <el-button type="primary" @click="renderWorkflow()">展现工作流网</el-button>
    </div>
    <svg class="chart">
    </svg>
  </div>
</template>

<style type="text/css" rel="stylesheet/less">

  .workNet{
    display: flex;
    flex-direction: column;}

  .show{
    text-align: center;
  }
  .node rect {
    cursor: move;
    fill-opacity: .9;
    shape-rendering: crispEdges;
  }

  .node text {
    pointer-events: none;
    text-shadow: 0 1px 0 #fff;
  }

  .link {
    fill: none;
    stroke: #000;
    stroke-opacity: .2;
  }

  .link:hover {
    stroke-opacity: .5;
  }

  path:hover {
    stroke-opacity: 0.9;
  }
</style>

<script>

  export default{
    data(){
      return {
          items:{

        "timeCost" : 439,
        "diagram" :
        {
          "netElementList" :
          [
            {"element":"Activity0","isEnd":"false","isStart":"false","name":"time-out X","output":["decide"],"subnetList":[1],"subnetNum":1,"time":"439"},
            {"element":"Activity1","isEnd":"false","isStart":"false","name":"collect reviews","output":["collect reviews","decide"],"subnetList":[2],"subnetNum":1,"time":"439"},
            {"element":"Activity2","isEnd":"false","isStart":"false","name":"decide","output":["accept","decide","invite additional reviewer","reject"],"subnetList":[4],"subnetNum":1,"time":"439"},
            {"element":"Activity3","isEnd":"false","isStart":"false","name":"get review 1","output":["collect reviews"],"subnetList":[1],"subnetNum":1,"time":"439"},
            {"element":"Activity4","isEnd":"false","isStart":"false","name":"get review 2","output":["collect reviews"],"subnetList":[1],"subnetNum":1,"time":"439"},
            {"element":"Activity5","isEnd":"false","isStart":"false","name":"get review 3","output":["collect reviews"],"subnetList":[1],"subnetNum":1,"time":"439"},
            {"element":"Activity6","isEnd":"false","isStart":"false","name":"get review X","output":["decide"],"subnetList":[1],"subnetNum":1,"time":"439"},
            {"element":"Activity7","isEnd":"false","isStart":"false","name":"invite additional reviewer","output":["get review X","invite additional reviewer","time-out X"],"subnetList":[3],"subnetNum":1,"time":"439"},
            {"element":"Activity8","isEnd":"true","isStart":"false","name":"accept","output":["accept"],"subnetList":[1],"subnetNum":1,"time":"439"},
            {"element":"Activity9","isEnd":"true","isStart":"false","name":"reject","output":["reject"],"subnetList":[1],"subnetNum":1,"time":"439"},
            {"element":"Activity10","isEnd":"false","isStart":"false","name":"time-out 1","output":["collect reviews"],"subnetList":[1],"subnetNum":1,"time":"439"},
            {"element":"Activity11","isEnd":"false","isStart":"false","name":"time-out 2","output":["collect reviews"],"subnetList":[1],"subnetNum":1,"time":"439"},
            {"element":"Activity12","isEnd":"false","isStart":"false","name":"time-out 3","output":["collect reviews"],"subnetList":[1],"subnetNum":1,"time":"439"},
            {"element":"Activity13","isEnd":"false","isStart":"true","name":"invite reviewers","output":["get review 1","invite reviewers","time-out 1","get review 2","invite reviewers","time-out 2","get review 3","invite reviewers","time-out 3"],"subnetList":[3,3,3],"subnetNum":3,"time":"439"}
          ]
        }
          }
      }
    },

    methods: {
      renderWorkflow(){
// Create a new directed graph
    var g = new dagreD3.graphlib.Graph().setGraph({
      rankdir: "LR"
    });
    for (var index in this.items.diagram) {
      var data = this.items.diagram[index];
      var name = data.name;
// 先设置节点
      data.label = name;
      data.rx = data.ry = 5;
      g.setNode(name, data);
// 此处将output转化为二维数组
      var subnetNum = data.subnetNum;
      var subnetList = data.subnetList;
      var output = translate(data.output, subnetNum, subnetList);
      var isStart = data.isStart;
      var isEnd = data.isEnd;
      var kusuo;
// 开始画库所和边
      if (isStart == "true") {
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
      if (isEnd == "true") {
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
        for (var i in output) {
          kusuo = output[i].join("").toUpperCase();
          g.setNode(kusuo, {
            label: "",
            shape: "circle"
          });
          g.setEdge(name, kusuo, {
            label: "",
            lineInterpolate: 'basis',
            width: 30
          });
// 库所到output的边
          for (var j in output[i]) {
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
      var arr1 = [];
      for (var i = 0; i < col; i++) {
        var arr2 = [];
        for (var j = 0; j < row[i]; j++) {
          if (arr != null)
            arr2[j] = arr.shift();
        }
        arr1.push(arr2);
      }
      return arr1;
    }

// Create the renderer
    var render = new dagreD3.render();

// Set up an SVG group so that we can translate the final graph.
    var svg = d3.select('svg'),
      inner = svg.append("g");

    debugger
// Set up zoom support
    var zoom = d3.behavior.zoom().on("zoom", function () {
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
    var initialScale = 0.5;
    zoom
      .translate([(svg.attr("width") - g.graph().width * initialScale) / 2, 150])
      .scale(initialScale)
      .event(svg);
// svg.attr('height', g.graph().height * initialScale + 40);
  }

  }

  }
</script>
