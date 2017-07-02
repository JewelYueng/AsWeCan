<template>
  <div class="transition-system">
    <h1>Transition System</h1>
    <el-button type="primary" @click="renderDiagraph(items.diagram, '#diagraph')">静态工作流图</el-button>
    <div class="all">
  <span>全记录动画</span>
    <el-button type="primary" @click="allTraceAniInit()" id="all-trace-run">运行</el-button>
    </div>
    <div class="simple">
      <span>单记录动画</span>
      <el-select v-model="selectedTrace" placeholder="请选择轨迹" id="trace-input">
        <el-option
          v-for="item in items.diagram.traces"
          :key="item"
          :label="item"
          :value="item">
        </el-option>
      </el-select>
      <el-button type="primary" @click="traceAniInit()" id="trace-submit">运行</el-button>
      <el-button type="primary" @click="traceAniInit().Res">清除</el-button>
    </div>
    <svg id="diagraph" width="1000" height="450"></svg>
  </div>
</template>

<style lang="less" scoped rel="stylesheet/less">
.all{
  margin-left: 50px;
  text-align: left;
  margin-bottom: 30px;
}

  .simple{
    margin-left: 50px;
    text-align: left;
    margin-bottom: 20px;
  }
</style>

<script>
  import ElButton from "../../../../node_modules/element-ui/packages/button/src/button";
  export default{
    components: {ElButton},
    data(){
      return {
        selectedTrace:"",
        items:{
          "timeCost":541,
          "diagram":
            {
              "links":
                [
                  {"source":"[__INVALID__]","target":"[Anne, Mike]","value":136},
                  {"source":"[__INVALID__]","target":"[Wil]","value":526},
                  {"source":"[Anne, Mike]","target":"[Pete, Sara, Mary, Sam, Pam, John, Carol]","value":526},
                  {"source":"[Wil]","target":"[Anne, Mike]","value":1252},
                  {"source":"[Anne, Mike]","target":"[Wil]","value":200},
                  {"source":"[Pete, Mary, Sara, Sam, Pam, John, Carol]","target":"[Anne, Mike]","value":64},
                  {"source":"[Pete, Sara, Mary, Sam, Pam, John, Carol]","target":"[Wil]","value":526},
                  {"source":"[Anne, Mike]","target":"[Pete, Mary, Sara, Sam, Pam, John, Carol]","value":108},
                  {"source":"[Anne, Mike]","target":"[__INVALID__]","value":618}
                ],
              "nodes":
                [
                  {"name":"[__INVALID__]"},
                  {"name":"[Wil]"},
                  {"name":"[Anne, Mike]"},
                  {"name":"[Pete, Mary, Sara, Sam, Pam, John, Carol]"},
                  {"name":"[Pete, Sara, Mary, Sam, Pam, John, Carol]"}
                ],
              "traces":["a,b,d,c,f","a,c,b,d,f","a,c,d,b,f","a,d,e,f","a,b,c,d,f","a,e,d,f"],
              "Alltraces":["a,b,d,c,f","a,c,b,d,f","a,c,d,b,f","a,d,e,f","a,b,c,d,f","a,e,d,f"]
            }
        }
      }
    },
    methods:{
      renderDiagraph:function (data,selector) {
        var params = {
          'radius': 10,
          'margin': 30
        };
        var nodeValueMap = {};
        var nodeSizeMap = {};
        var edgeValueMap = {};
        var edgeWidthMap = {};
        var linkMap = {};


        data.nodes.forEach(function (node) {
          nodeValueMap[node.name] = 0;
          nodeSizeMap[node.name] = params.radius;
        });
        data.links.forEach(function (link) {
          nodeValueMap[link.source] += parseInt(link.value);
          nodeValueMap[link.target] += parseInt(link.value);
          edgeValueMap[link.source + ":" + link.target] = parseInt(link.value);
        });

        function getMinMax(someMap) {
          var range = [-1, -1];
          for (var key in someMap) {
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

        var nodeRange = getMinMax(nodeValueMap);
        var edgeRange = getMinMax(edgeValueMap);

        for (var key in nodeValueMap) {
          nodeSizeMap[key] += parseInt(20 * getRatio(nodeValueMap[key], nodeRange));
        }
        for (var key in edgeValueMap) {
          edgeWidthMap[key] = 1 + parseInt(10 * getRatio(edgeValueMap[key], edgeRange));
        }

        function produceLayout(input, conf) {
          var g = new dagre.graphlib.Graph();
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

        var line = d3.svg.line()
          .x(function (d) {
            return d.x;
          })
          .y(function (d) {
            return d.y;
          })
          .interpolate("basis");

        var layout = produceLayout(data, {
          rankdir: "LR",
          nodesep: 80,
          ranksep: 150,
          edgesep: 10,
          marginx: params.radius * 2 + params.margin,
          marginy: params.radius * 2 + params.margin
        });


        function runTrace(trace) {
          var traceTimers = [];
          var du = 1000;
          var traceMap = {};

          for (var i = 0; i !== trace.length; i++) {
            var node = layout.node(trace[i]);
            if (node === undefined) {
              console.log("没有找到节点");
              return traceTimers;
            }

            if (i + 1 >= trace.length) {
              (function () {
                var tNode = node;

                var t = setTimeout(function () {
                  traceGroup.append("circle")
                    .attr('cx', tNode.x)
                    .attr('cy', tNode.y)
                    .attr('r', 5);
                }, du * i);
                traceTimers.push(t);
              })();
            } else {
              var edge = layout.edge({
                v: trace[i],
                w: trace[i + 1]
              });
              var str = trace[i] + '->' + trace[i + 1];
              if (edge === undefined) {
                console.log("没有找到边");
                continue;
              }

              (function () {
                var tEdge = edge;
                var tNode = node;
                var tIndex = i;
                var tStr = str;

                var t = setTimeout(function () {
                  traceGroup.append("circle")
                    .attr('cx', tNode.x)
                    .attr('cy', tNode.y)
                    .attr('r', 5);

                  var p = traceGroup.append("path")
                    .attr("class", "edge")
                    .attr("id", "trace-" + tIndex)
                    .attr("d", line(tEdge.points))
                    .attr("stroke-width", function (d) {
                      return 2;
                    })
                    .style("stroke", function (d) {
                      if (traceMap[tStr] === undefined) {
                        traceMap[tStr] = 1;
                      } else {
                        traceMap[tStr] += 1;
                      }
                      return traceMap[tStr] % 2 === 1 ? 'red' : 'blue';
                    });

                  var l = document.getElementById('trace-' + tIndex)[0].getTotalLength();
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


        function Return_edgeWidthMap (d) {
          return edgeWidthMap[d.v + ':' + d.w];
        }


        if (data.traces !== undefined) {
          this.traceAniInit();
        }

        if (data.allTraces !== undefined) {
          this.allTraceAniInit();
        }

        var svg = d3.select(selector).attr('class', 'daiding');

        var marker =
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


        var wrapper = svg.append("g");
        var g = wrapper.append("g");
        var traceGroup = wrapper.append("g").attr("class", "trace-group");

        svg.call(d3.behavior.zoom()
          .scaleExtent([1 / 8, 8])
          .on("zoom", zoomed));

        function zoomed() {
          wrapper.attr("transform",
            "translate(" + d3.event.translate + ")scale(" + d3.event.scale + ")");
        }

        var edges = g.selectAll(".edge")
          .data(layout.edges())
          .enter()
          .append('g')
          .attr("class", "edge");


        var links = edges.append("path")
          .attr("d", function (d) {
            linkMap[d.v + ':' + d.w] = d3.select(this);
            return line(layout.edge(d).points);
          })
          .attr("stroke-width", function (d) {
            return edgeWidthMap[d.v + ':' + d.w];
          });
         // .attr("marker-end", "url(processMining.jsp#arrow)");


        links.append("title")
          .text(function (d) {
            return edgeValueMap[d.v + ':' + d.w];
          });

        var nodes = g.selectAll(".node")
          .data(nodesep.nodes())
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
          });


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

        return{
          Res:runTrace,
          Res2:Return_edgeWidthMap(d),
          Res3:linkMap,
          Res4:edgeValueMap,
          Res5:edgeWidthMap
        }
      },

      traceAniInit:function () {
        function  cleanTrace() {
          var timers1;
          if (timers1 !== undefined) {
            timers1.forEach(function (timer) {
              clearTimeout(timer);
            })
          }

          document.getElementById('trace-submit').onclick=function () {
            cleanTrace();

            var trace = document.getElementById("trace-input").value.split(",");
            timers1 = runTrace(trace);
          };
          document.getElementById('trace-clean').onclick=function () {
            cleanTrace();
          }
        }
        return{
          Res:cleanTrace()
        }
      },

      allTraceAniInit:function () {
        var traceTimers = [];
        var timers2;
        function cleanTrace () {
          if (timers2 !== undefined) {
            timers2.forEach(function (timer) {
              clearTimeout(timer);
            })
            links.attr("stroke-width", this.renderDiagraph().Res2);
          }
        }

        document.getElementById("all-trace-run").onclick=function () {
          cleanTrace();
          links.attr("stroke-width", 0);

          var timerArr = [];
          for (var i = 0; i !== this.items.diagram.Alltraces.length; i++) {
            var trace = this.items.diagram.Alltraces[i].split(",");
            for (var j = 0; j !== trace.length; j++) {
              if (j + 1 < trace.length) {
                var tStr = trace[j] + ':' + trace[j + 1];
                if (!this.renderDiagraph().Res3[tStr]) {
                  continue;
                }
                var v = 1 / this.renderDiagraph().Res4[tStr] * this.renderDiagraph().Res5[tStr];

                function creator(str, v) {
                  return function () {
                    this.renderDiagraph().Res3[str].attr("stroke-width", function () {
                      return parseFloat(d3.select(this).attr("stroke-width")) + v;
                    });
                    this.renderDiagraph().Res3[str].style("stroke", "red").transition().duration(500).style("stroke", "#ccc");
                  }
                }

                timerArr.push(creator(tStr, v));
              }
            }
          }

          for (var k = 0; k !== timerArr.length; k++) {
            traceTimers.push(setTimeout(timerArr[k], 100 * k));
          }
          traceTimers.push(setTimeout(function () {
            links.attr("stroke-width", function (d) {
              return this.renderDiagraph().Res5[d.v + ':' + d.w];
            });
          }, 100 * timerArr.length));
          timers2 = traceTimers;
        };
      }

      }

  }
</script>
