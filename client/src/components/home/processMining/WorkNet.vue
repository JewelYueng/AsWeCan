<template>
  <div class="workNet">
    <svg class="chart" width="1130" height="500">

        </svg>
    <div class="download"><el-button type="primary" @click="downloadImage" >下载</el-button></div>
  </div>
</template>

<style type="text/css" rel="stylesheet/less" lang="less">

  .workNet {
    display: flex;
    flex-direction: column;
    .show {
      text-align: center;
    }


    .link {
      fill: none;
      stroke: #ffa21d;
      stroke-opacity: .2;
    }

    .link:hover {
      stroke-opacity: .3;
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
          "diagram": null
        }
      }
    },
    created(){
//      深复制
      this.items.diagram = JSON.parse(JSON.stringify(this.petri))
    },
    mounted(){
      this.renderWorkflow()
    },
    methods: {
      downloadImage(){
        let svg = d3.select('.chart')
          .attr('background-color', 'white')
        let serializer = new XMLSerializer();
        let source = serializer.serializeToString(svg.node());

        source = '<?xml version="1.0" standalone="no"?>\r\n' + source;
        let url = "data:image/svg+xml;charset=utf-8," + encodeURIComponent(source);

        let canvas = document.createElement("canvas");
        canvas.width = 1130;
        canvas.height = 700;

        console.log(d3.select('.svg-body').attr('width'), d3.select('.svg-body').attr('height'))
        debugger
        let context = canvas.getContext("2d");
        let image = new Image;
        image.src = url;
        image.onload = function () {
          context.drawImage(image, 0, 0);

          let a = document.createElement("a");
          a.download = "worknet.png";
          a.href = canvas.toDataURL("image/png");
          a.click();
        };
      },
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
              if (arr !== null)
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
          inner = svg.append("g")
            .attr('class','svg-body')


// Set up zoom support
        let zoom = d3.behavior.zoom().on("zoom", function () {
          inner.attr("transform", "translate(" + d3.event.translate + ")" +
            "scale(" + d3.event.scale + ")")
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
        svg.attr('height', 650);

        d3.selectAll('rect')
          .attr({
            'cursor': 'move',
            'stroke': '#333',
            'fill': '#fff'
          })
        d3.selectAll('.node text')
          .attr({
            'pointer-events': 'none',
            'color': '#82a6da',
            'text-shadow': '0 1px 0 #fff'
          })
        d3.selectAll('circle')
          .attr({
            'fill': '#c0c0c0'
          })
        d3.selectAll('.edgePath path')
          .attr({
            'stroke': '#516082',
            'fill': '#516082',
            'stroke-width': '2px'
          })
      }

    }

  }
</script>
