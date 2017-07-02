<template>
  <div class="transition-system">
    <h1>Transition System</h1>
    <el-button type="primary" @click="renderDiagraph(items.diagram, '#diagraph')">静态工作流图</el-button>
    <div class="all" v-show="showTotalBtn" @click="runTotal">
      <span>全记录动画</span>
      <el-button type="primary" @click="runTotal()" id="all-trace-run">运行</el-button>
    </div>
    <div class="simple" v-show="showSelector">
      <span>单记录动画</span>
      <el-select v-model="selectedTrace" placeholder="请选择轨迹" id="trace-input">
        <el-option
          v-for="item in items.diagram.traces"
          :key="item"
          :label="item"
          :value="item">
        </el-option>
      </el-select>
      <el-button type="primary" @click="runTrace()" id="trace-submit">运行</el-button>
      <el-button type="primary" @click="cleanTrace(1)">清除</el-button>
    </div>
    <svg id="diagraph" width="1000" height="450"></svg>
  </div>
</template>

<style lang="less" scoped rel="stylesheet/less">
  .all {
    margin-left: 50px;
    text-align: left;
    margin-bottom: 30px;
  }

  .simple {
    margin-left: 50px;
    text-align: left;
    margin-bottom: 20px;
  }

  #diagraph  {
    circle{
      fill: steelblue;
    }

  }
  #diagraph .edge {
    stroke: #ccc;
    fill: none;
  }
  #diagraph text {
    fill: #333;
  }



  .trace-input select {
    display: inline-block;
    max-width: 900px;
    overflow: hidden;
  }

  .trace-input button {
    display: inline-block;
    width: 60px;
  }
</style>

<script>
  import ElButton from "../../../../node_modules/element-ui/packages/button/src/button";
  export default{
    components: {ElButton},
    props: ['produce'],
    data(){
      return {
        showSelector: false,
        showTotalBtn: false,
//        单记录运行的计时器
        timers1: null,
//        全记录运行的计时器
        timers2: null,
        selectedTrace: "",
        items: {
          "timeCost": 541,
          "diagram": {
            "links": [
              {"source": "[__INVALID__]", "target": "[Anne, Mike]", "value": 136},
              {"source": "[__INVALID__]", "target": "[Wil]", "value": 526},
              {"source": "[Anne, Mike]", "target": "[Pete, Sara, Mary, Sam, Pam, John, Carol]", "value": 526},
              {"source": "[Wil]", "target": "[Anne, Mike]", "value": 1252},
              {"source": "[Anne, Mike]", "target": "[Wil]", "value": 200},
              {"source": "[Pete, Mary, Sara, Sam, Pam, John, Carol]", "target": "[Anne, Mike]", "value": 64},
              {"source": "[Pete, Sara, Mary, Sam, Pam, John, Carol]", "target": "[Wil]", "value": 526},
              {"source": "[Anne, Mike]", "target": "[Pete, Mary, Sara, Sam, Pam, John, Carol]", "value": 108},
              {"source": "[Anne, Mike]", "target": "[__INVALID__]", "value": 618}
            ],
            "nodes": [
              {"name": "[__INVALID__]"},
              {"name": "[Wil]"},
              {"name": "[Anne, Mike]"},
              {"name": "[Pete, Mary, Sara, Sam, Pam, John, Carol]"},
              {"name": "[Pete, Sara, Mary, Sam, Pam, John, Carol]"}
            ],
            "traces": ["a,b,d,c,f", "a,c,b,d,f", "a,c,d,b,f", "a,d,e,f", "a,b,c,d,f", "a,e,d,f"],
            "allTraces": ["a,b,d,c,f", "a,c,b,d,f", "a,c,d,b,f", "a,d,e,f", "a,b,c,d,f", "a,e,d,f"]
          }
        }
      }
    },
    created(){
      this.items.diagram = JSON.parse( JSON.stringify(this.produce))
    },
    methods: {
      cleanTrace(type){
        if (this[`timers${type}`] !== null && this[`timers${type}`].length !== 0) {
          this[`timers${type}`].forEach(function (timer) {
            clearTimeout(timer);
          })
        }
        document.getElementsByClassName("trace-group")[0].innerHTML = '';
        if(type === 2) {
          let links = d3.selectAll('.link')
            .attr("stroke-width", (d) =>{
              return this.renderDiagraph(this.items.diagram,'#diagraph').Res5[d.v + ':' + d.w];
            });
        }
      },
      runTrace(){
        this.cleanTrace(1);
        let trace = this.selectedTrace.split(",");
        this.timers1 = this.renderDiagraph(this.items.diagram,'#diagraph').Res(trace);
      },
      runTotal(){
        this.cleanTrace(2);
        let traceTimers = []
        let links = d3.selectAll('.link')
          .attr("stroke-width", 0);
        debugger
        let timerArr = [];
        for (let i = 0; i !== this.items.diagram.allTraces.length; i++) {
          let trace = this.items.diagram.allTraces[i].split(",");
          for (let j = 0; j !== trace.length; j++) {
            if (j + 1 < trace.length) {
              let tStr = trace[j] + ':' + trace[j + 1];
              if (!this.renderDiagraph(this.items.diagram,'#diagraph').Res3[tStr]) {
                continue;
              }
              let v = 1 / this.renderDiagraph(this.items.diagram,'#diagraph').Res4[tStr] * this.renderDiagraph(this.items.diagram,'#diagraph').Res5[tStr];

              function creator(str, v) {
                return function () {
                  this.renderDiagraph(this.items.diagram,'#diagraph').Res3[str].attr("stroke-width", function () {
                    return parseFloat(d3.select(this).attr("stroke-width")) + v;
                  });
                  this.renderDiagraph(this.items.diagram,'#diagraph').Res3[str].style("stroke", "red").transition().duration(500).style("stroke", "#ccc");
                }
              }

              timerArr.push(creator(tStr, v));
            }
          }
        }

        for (let k = 0; k !== timerArr.length; k++) {
          traceTimers.push(setTimeout(timerArr[k], 100 * k));
        }
        const _this = this
        console.log(links)
        traceTimers.push(setTimeout(function () {
          links.attr("stroke-width", function (d) {
            return _this.renderDiagraph(_this.items.diagram,'#diagraph').Res5[d.v + ':' + d.w];
          });
        }, 100 * timerArr.length));
        this.timers2 = traceTimers;
      },
      renderDiagraph: function (data, selector) {
        let params = {
          'radius': 10,
          'margin': 30
        };
        let nodeValueMap = {};
        let nodeSizeMap = {};
        let edgeValueMap = {};
        let edgeWidthMap = {};
        let linkMap = {};


        data.nodes.forEach(function (node) {
          nodeValueMap[node.name] = 0;
          nodeSizeMap[node.name] = params.radius;
        });
        data.links.forEach(function (link) {
          nodeValueMap[link.source] += parseInt(link.value);
          nodeValueMap[link.target] += parseInt(link.value);
          edgeValueMap[link.source + ":" + link.target] = parseInt(link.value);
        });


        let nodeRange = getMinMax(nodeValueMap);
        let edgeRange = getMinMax(edgeValueMap);

        for (let key in nodeValueMap) {
          nodeSizeMap[key] += parseInt(20 * getRatio(nodeValueMap[key], nodeRange));
        }
        for (let key in edgeValueMap) {
          edgeWidthMap[key] = 1 + parseInt(10 * getRatio(edgeValueMap[key], edgeRange));
        }

        function getMinMax(someMap) {
          let range = [-1, -1];
          for (let key in someMap) {
            if (range[1] === -1 || range[1] < someMap[key]) {
              range[1] = someMap[key];
            }
            if (range[0] === -1 || range[0] > someMap[key]) {
              range[0] = someMap[key];
            }
          }
          return range;
        }

        function getRatio(v, range) {
          if (range[1] - range[0] <= 0) {
            return 0;
          } else {
            return (v - range[0]) / (range[1] - range[0]);
          }
        }

        function produceLayout(input, conf) {
          let g = new dagre.graphlib.Graph();
          g.setGraph(conf);
          g.setDefaultEdgeLabel(function () {
            return {};
          });

          input.nodes.forEach(function (node) {
            g.setNode(node.name, {
              label: node.name,
              width: nodeSizeMap[node.name] * 2,
              height: nodeSizeMap[node.name] * 2
            });
          });
          input.links.forEach(function (link) {
            g.setEdge(link.source, link.target);
          });

          dagre.layout(g);
          return g;
        }

        let line = d3.svg.line()
          .x(function (d) {
            return d.x;
          })
          .y(function (d) {
            return d.y;
          })
          .interpolate("basis");

        let layout = produceLayout(data, {
          rankdir: "LR",
          nodesep: 80,
          ranksep: 150,
          edgesep: 10,
          marginx: params.radius * 2 + params.margin,
          marginy: params.radius * 2 + params.margin
        });


        function runTrace(trace) {
          let traceTimers = [];
          let du = 1000;
          let traceMap = {};

          for (let i = 0; i !== trace.length; i++) {
            let node = layout.node(trace[i]);
            if (node === undefined) {
              console.log("没有找到节点");
              return traceTimers;
            }

            if (i + 1 >= trace.length) {
              (function () {
                let tNode = node;

                let t = setTimeout(function () {
                  traceGroup.append("circle")
                    .attr('cx', tNode.x)
                    .attr('cy', tNode.y)
                    .attr('r', 5);
                }, du * i);
                traceTimers.push(t);
              })();
            } else {
              let edge = layout.edge({
                v: trace[i],
                w: trace[i + 1]
              });
              let str = trace[i] + '->' + trace[i + 1];
              if (edge === undefined) {
                console.log("没有找到边");
                continue;
              }

              (function () {
                let tEdge = edge;
                let tNode = node;
                let tIndex = i;
                let tStr = str;

                let t = setTimeout(function () {
                  traceGroup.append("circle")
                    .attr('cx', tNode.x)
                    .attr('cy', tNode.y)
                    .attr('r', 5);

                  let p = traceGroup.append("path")
                    .attr("class", "edge")
                    .attr("id", "trace-" + tIndex)
                    .attr("d", line(tEdge.points))
                    .attr("stroke-width", function (d) {
                      return 2;
                    })
                    .attr("stroke", function (d) {
                      if (traceMap[tStr] === undefined) {
                        traceMap[tStr] = 1;
                      } else {
                        traceMap[tStr] += 1;
                      }
                      return traceMap[tStr] % 2 === 1 ? 'red' : 'blue';
                    });

                  let l = document.getElementById('trace-' + tIndex)[0].getTotalLength();
                  p.style("stroke-dasharray", l);
                  p.style("stroke-dashoffset", l);
                  p.transition()
                    .style("stroke-dashoffset", 0)
                    .duration(du);
                }, du * i);
                traceTimers.push(t);
              })();
            }
          }

          return traceTimers;
        }


        function Return_edgeWidthMap(d) {
          console.log(d)
          return edgeWidthMap[d.v + ':' + d.w];
        }

//        如果存在一条以上路径，则显示单记录动画模块
        if (data.traces !== undefined) {
          this.showSelector = true
        }
//        如果存在总路径，则显示全记录运行模块
        if (data.allTraces !== undefined) {
          this.showTotalBtn = true;
        }
//        目前没有发现以下这一句修改类名的必要
        let svg = d3.select(selector).attr('class', 'daiding');

        let marker =
          svg.append("marker")
            .attr("id", "arrow")
            .attr("markerUnits", "userSpaceOnUse")
            .attr("markerWidth", "12")
            .attr("markerHeight", "16")
            .attr("viewBox", "0 0 12 18")
            .attr("refX", "6")
            .attr("refY", "10")
            .attr("orient", "auto")
            .append("path")
            .attr("d", "M2,2 L2,18 L10,10 L2,2") //箭头的路径
            .attr('fill', 'red'); //箭头颜色


        let wrapper = svg.append("g");
        let g = wrapper.append("g");
        let traceGroup = wrapper.append("g").attr("class", "trace-group");

        svg.call(d3.behavior.zoom()
          .scaleExtent([1 / 8, 8])
          .on("zoom", zoomed));

        function zoomed() {
          wrapper.attr("transform",
            "translate(" + d3.event.translate + ")scale(" + d3.event.scale + ")");
        }

        let edges = g.selectAll(".edge")
          .data(layout.edges())
          .enter()
          .append('g')
          .attr("class", "edge");


        let links = edges.append("path")
          .attr("d", function (d) {
            linkMap[d.v + ':' + d.w] = d3.select(this);
            return line(layout.edge(d).points);
          })
          .attr("stroke-width", function (d) {
            return edgeWidthMap[d.v + ':' + d.w];
          })
          .attr('class', 'link')
          .attr('fill', 'none')
          .attr('stroke', '#a0a2a7')
         .attr("marker-end", "url(#arrow)");


        links.append("title")
          .text(function (d) {
            return edgeValueMap[d.v + ':' + d.w];
          });

        let nodes = g.selectAll(".node")
          .data(layout.nodes())
          .enter()
          .append('g')
          .attr("class", "node");

        nodes.append("title")
          .text(function (d) {
            return d;
          });

        nodes.append('circle')
          .attr('cx', function (d) {
            return layout.node(d).x;
          })
          .attr('cy', function (d) {
            return layout.node(d).y;
          })
          .attr('r', function (d) {
            return nodeSizeMap[d];
          })
          .attr('fill', '#516082')


        nodes.append('text')
          .attr('x', function (d) {
            return layout.node(d).x - 30;
          })
          .attr('y', function (d) {
            return layout.node(d).y - nodeSizeMap[d] - 5;
          })
          .text(function (d) {
            if (d.length > 10)
              return d.slice(0, 10) + '...';
            else
              return d;
          })

        return {
          Res: runTrace,
          Res2: Return_edgeWidthMap,
          Res3: linkMap,
          Res4: edgeValueMap,
          Res5: edgeWidthMap
        }
      },

//      traceAniInit: function () {
//        function cleanTrace() {
//          if (this.timers1 !== undefined) {
//            this.timers1.forEach(function (timer) {
//              clearTimeout(timer);
//            })
//          }
//
//          document.getElementById('trace-submit').onclick = function () {
//            cleanTrace();
//
//            let trace = document.getElementById("trace-input").value.split(",");
//            timers1 = runTrace(trace);
//          };
//        }
//
//        return {
//          Res: cleanTrace()
//        }
//      },


    }

  }
</script>
