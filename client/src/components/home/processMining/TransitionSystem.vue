<template>
  <div class="transition-system">
    <h1>Transition System</h1>
    <div class="all" v-show="showTotalBtn">
      <span>全记录动画</span>
      <el-button type="primary" @click="runTotal()" id="all-trace-run">运行</el-button>
      <!--<el-button type="primary" @click="cleanTrace()" >清除</el-button>-->
    </div>
    <div class="simple" v-show="showSelector">
      <span>单记录动画</span>
      <el-select v-model="selectedTrace" placeholder="请选择轨迹" id="trace-input">
        <el-option
          v-for="item in items.diagram.traces"
          :key="item"
          :label="item"
          :value="item"
          :title="item">
        </el-option>
      </el-select>
      <el-button type="primary" @click="runTrace('#diagraph')" id="trace-submit">运行</el-button>
      <el-button type="primary" @click="cleanTrace()">清除</el-button>
    </div>
    <svg id="diagraph" width="3500" height="450"></svg>
  </div>
</template>

<style lang="less" rel="stylesheet/less">
  .el-select-dropdown__list {
    max-width: 300px;
  }
  .transition-system {
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

    #diagraph {
      circle {
        fill: steelblue;
      }
      .trace-group circle{
        fill: red;
      }
    }
    #diagraph .edge {
      stroke: red;
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
  }
</style>

<script>
  import ElButton from "../../../../node_modules/element-ui/packages/button/src/button";
  export default{
    components: {ElButton},
    props: ['produce'],
    data(){
      return {
        see: true,
        layout: null,
        traceGroup: null,
        g: null,
        svg: null,
        wrapper: null,
        nodeValueMap: {},
        nodeSizeMap: {},
        edgeValueMap: {},
        edgeWidthMap: {},
        linkMap: {},
        showSelector: false,
        showTotalBtn: false,
//        单记录运行的计时器
        timers1: null,
//        全记录运行的计时器
        timers2: null,
        selectedTrace: "",
        items: {
          "timeCost": 541,
          "diagram": {}
        }
      }
    },
    created(){
      this.items.diagram = JSON.parse(JSON.stringify(this.produce))
    },
    mounted(){
      this.renderDiagraph(this.items.diagram, '#diagraph')
    },
    methods: {
      cleanTrace(){

        const _this = this;
        console.log(_this.timers1, _this.timers2)
        if (_this.timers1 !== null && _this.timers1.length !== 0) {
          _this.timers1.forEach(function (timer) {
            clearTimeout(timer);
          })
        }

        document.getElementsByClassName("trace-group").innerHTML = '';
        let groups = document.getElementsByClassName("trace-group")
        for (let i = 0; i < groups.length; i++) {
          groups[i].innerHTML = ""
        }
        if (_this.timers2 !== null && _this.timers2.length !== 0) {
          _this.timers2.forEach(function (timer) {
            clearTimeout(timer);

            let links = d3.selectAll('.link')
              .attr("stroke-width", (d) => {
                return _this.edgeWidthMap[d.v + ':' + d.w];
              });
          })
        }
//        if(type === 2) {
//          let links = d3.selectAll('.link')
//            .attr("stroke-width", (d) =>{
//              return _this.edgeWidthMap[d.v + ':' + d.w];
//            });
//        }
      },
      runTrace(selector){
        const _this = this;
        _this.cleanTrace(1);
        let trace = _this.selectedTrace.split(",");


        let traceTimers = [];
        let du = 1000;
        let traceMap = {};
        let line = d3.svg.line()
          .x(function (d) {
            return d.x;
          })
          .y(function (d) {
            return d.y;
          })
          .interpolate("basis");
        for (let i = 0; i !== trace.length; i++) {
          let node = _this.layout.node(trace[i]);
          if (node === undefined) {
            console.log("没有找到节点");
            return traceTimers;
          }

          if (i + 1 >= trace.length) {
            (function () {
              let tNode = node;

              let t = setTimeout(function () {
                d3.select(selector).append("g").append("g").attr("class", "trace-group").append("circle")
                  .attr('cx', tNode.x)
                  .attr('cy', tNode.y)
                  .attr('r', 5);
              }, du * i);
              traceTimers.push(t);
            })();
          } else {
            let edge = _this.layout.edge({
              v: trace[i],
              w: trace[i + 1],
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
                d3.select(selector).append("g").append("g").attr("class", "trace-group").append("circle")
                  .attr('cx', tNode.x)
                  .attr('cy', tNode.y)
                  .attr('r', 5);

                let p = d3.select(selector).append("g").append("g").attr("class", "trace-group").append("path")
                  .attr("class", "edge")
                  .attr("fill", "none")
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

                let l = document.getElementById('trace-' + tIndex).getTotalLength();
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
        this.timers1 = traceTimers;
      },
      runTotal(){
        const _this = this;
        _this.cleanTrace(2);
        let traceTimers = []
        let links = d3.selectAll('.link')
          .attr("stroke-width", 0);
        let timerArr = [];

        for (let i = 0; i !== _this.items.diagram.allTraces.length; i++) {
          let trace = _this.items.diagram.allTraces[i].split(",");
          for (let j = 0; j !== trace.length; j++) {
            if (j + 1 < trace.length) {
              let tStr = trace[j] + ':' + trace[j + 1];
              if (!_this.linkMap[tStr]) {
                continue;
              }
              let v = 1 / _this.edgeValueMap[tStr] * _this.edgeWidthMap[tStr];

              function creator(str, v) {
                return function () {
                  _this.linkMap[str].attr("stroke-width", function () {
                    return parseFloat(d3.select(this).attr("stroke-width")) + v;
                  });
                  _this.linkMap[str].style("stroke", "red").transition().duration(500).style("stroke", "#ccc");
                }
              }

              timerArr.push(creator(tStr, v));
            }
          }
        }

        for (let k = 0; k !== timerArr.length; k++) {
          traceTimers.push(setTimeout(timerArr[k], 100 * k));
        }

        console.log(links)
        traceTimers.push(setTimeout(function () {
          links.attr("stroke-width", function (d) {
            return _this.edgeWidthMap[d.v + ':' + d.w];
          });
        }, 100 * timerArr.length));
        _this.timers2 = traceTimers;

      },
      renderDiagraph: function (data, selector) {

        let params = {
          'radius': 10,
          'margin': 30
        };
//debugger
        const _this = this;
        _this.see = false;
        data.nodes.forEach(function (node) {
          _this.nodeValueMap[node.name] = 0;
          _this.nodeSizeMap[node.name] = params.radius;
        });
        data.links.forEach(function (link) {
          _this.nodeValueMap[link.source] += parseInt(link.value);
          _this.nodeValueMap[link.target] += parseInt(link.value);
          _this.edgeValueMap[link.source + ":" + link.target] = parseInt(link.value);
        });


        let nodeRange = getMinMax(_this.nodeValueMap);
        let edgeRange = getMinMax(_this.edgeValueMap);

        for (let key in _this.nodeValueMap) {
          _this.nodeSizeMap[key] += parseInt(20 * getRatio(_this.nodeValueMap[key], nodeRange));
        }
        for (let key in _this.edgeValueMap) {
          _this.edgeWidthMap[key] = 1 + parseInt(10 * getRatio(_this.edgeValueMap[key], edgeRange));
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
              width: _this.nodeSizeMap[node.name] * 2,
              height: _this.nodeSizeMap[node.name] * 2
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

        _this.layout = produceLayout(data, {
          rankdir: "LR",
          nodesep: 80,
          ranksep: 150,
          edgesep: 10,
          marginx: params.radius * 2 + params.margin,
          marginy: params.radius * 2 + params.margin
        });


        function Return_edgeWidthMap(d) {
          console.log(d)
          return _this.edgeWidthMap[d.v + ':' + d.w];
        }

//        如果存在一条以上路径，则显示单记录动画模块
        if (data.traces !== undefined) {
          _this.showSelector = true
        }
//        如果存在总路径，则显示全记录运行模块
        if (data.allTraces !== undefined) {
          _this.showTotalBtn = true;
        }
//        目前没有发现以下这一句修改类名的必要
        _this.svg = d3.select(selector).attr('class', 'daiding');

        let svg = d3.select(selector);
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


        _this.wrapper = svg.append("g");
        let wrapper = svg.append("g");
        _this.g = wrapper.append("g");
        let g = wrapper.append("g");
        _this.traceGroup = wrapper.append("g").attr("class", "trace-group");
        let traceGroup = wrapper.append("g").attr("class", "trace-group");
        svg.call(d3.behavior.zoom()
          .scaleExtent([1 / 8, 8])
          .on("zoom", zoomed));

        function zoomed() {
          wrapper.attr("transform",
            "translate(" + d3.event.translate + ")scale(" + d3.event.scale + ")");
        }

        let edges = d3.select(selector).append("g").append("g").selectAll(".edge")
          .data(_this.layout.edges())
          .enter()
          .append('g')
          .attr("class", "edge")
          .attr("fill", "none")


        let links = edges.append("path")
          .attr("d", function (d) {
            _this.linkMap[d.v + ':' + d.w] = d3.select(this);
            return line(_this.layout.edge(d).points);
          })
          .attr("stroke-width", function (d) {
            return _this.edgeWidthMap[d.v + ':' + d.w];
          })
          .attr('class', 'link')
          .attr('fill', 'none')
          .attr('stroke', '#a0a2a7')
          .attr("marker-end", "url(#arrow)");


        links.append("title")
          .text(function (d) {
            return _this.edgeValueMap[d.v + ':' + d.w];
          });

        let nodes = d3.select(selector).append("g").append("g").selectAll(".node")
          .data(_this.layout.nodes())
          .enter()
          .append('g')
          .attr("class", "node");

        nodes.append("title")
          .text(function (d) {
            return d;
          });

        nodes.append('circle')
          .attr('cx', function (d) {
            return _this.layout.node(d).x;
          })
          .attr('cy', function (d) {
            return _this.layout.node(d).y;
          })
          .attr('r', function (d) {
            return _this.nodeSizeMap[d];
          })
          .attr('fill', '#516082')


        nodes.append('text')
          .attr('x', function (d) {
            return _this.layout.node(d).x - 30;
          })
          .attr('y', function (d) {
            return _this.layout.node(d).y - _this.nodeSizeMap[d] - 5;
          })
          .text(function (d) {
            if (d.length > 10)
              return d.slice(0, 10) + '...';
            else
              return d;
          })


      }


    }

  }
</script>
