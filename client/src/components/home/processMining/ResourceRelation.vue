<template>
  <div class="resource-relation">
<h1>Resource-Relation Diagram</h1>
    <el-button v-show="state==1" type="primary" @click="DownloadImage" >xiazai</el-button>
    <div>
      <el-select v-model="selectedAttr" placeholder="请选择" @change="produceLayout()">
        <el-option
          v-for="item in items.diagram"
          :key="item.resourceAttr"
          :label="item.resourceAttr"
          :value="item.resourceAttr">
        </el-option>
      </el-select>
    </div>
    <svg class="chart" v-for="item in items.diagram" v-if="item.resourceAttr === selectedAttr">
    </svg>
  </div>
</template>

<style lang="less" scoped rel="stylesheet/less">
  .link {
    fill: none;
    stroke: #666;
    stroke-width: 1.5px;
  }

  .chart {
    height: 500px;
  }
</style>


<script>
//  import * as d3 from "d3"
//  import { forceSimulation,forceCenter, forceLink } from 'd3-force'
  export default{
    props: ['resource'],
    data(){
      return {
        state:0,
        selectedAttr:"",
//        nGraph:{
//          "links":[],
//          "nodes":[]
//        },
        items:{
          "timeCost":541,
          "diagram":
            [
              { "resourceAttr":"test2",
                "links":
                  [
                    {"source":"[__aaa__]","target":"[Anne, Mike]","value":136},
                    {"source":"[__aaa__]","target":"[Wil]","value":526},
                    {"source":"[Anne, Mike]","target":"[Pete, Sara, Mary, Sam, Pam, John, Carol]","value":526},
                    {"source":"[Wil]","target":"[Anne, Mike]","value":1252},
                    {"source":"[Anne, Mike]","target":"[Wil]","value":200},
                    {"source":"[Pete, Mary, Sara, Sam, Pam, John, Carol]","target":"[Anne, Mike]","value":64},
                    {"source":"[Pete, Sara, Mary, Sam, Pam, John, Carol]","target":"[Wil]","value":526},
                    {"source":"[Anne, Mike]","target":"[Pete, Mary, Sara, Sam, Pam, John, Carol]","value":108},
                    {"source":"[Anne, Mike]","target":"[__aaa__]","value":618}
                  ],
                "nodes":
                  [
                    {"name":"[__aaa__]"},
                    {"name":"[Wil]"},
                    {"name":"[Anne, Mike]"},
                    {"name":"[Pete, Mary, Sara, Sam, Pam, John, Carol]"},
                    {"name":"[Pete, Sara, Mary, Sam, Pam, John, Carol]"}
                  ]},
              { "resourceAttr":"test3",
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
                    ]}

            ]}

      }
    },
    created(){
      this.items.diagram = this.resource
    },
    methods:{
      DownloadImage(){
        let svg = d3.select(".chart");
        let width = 5000;
        let height = 5000;

        var serializer = new XMLSerializer();

        var source = serializer.serializeToString(svg.node());



        source = '<?xml version="1.0" standalone="no"?>\r\n' + source;
        var url = "data:image/svg+xml;charset=utf-8," + encodeURIComponent(source);
        document.write('<img src="' + url + '"/>');

        var canvas = document.createElement("canvas");
        canvas.width = width;
        canvas.height = height;

        var context = canvas.getContext("2d");
        var image = new Image;
        image.src = document.getElementsByTagName('img')[0].src;
        image.onload = function() {
          context.drawImage(image, 0, 0);

          var a = document.createElement("a");
          a.download = "fallback.png";
          a.href = canvas.toDataURL("image/png");
          a.click();
        };
      },
      produceLayout:function () {

        var _this = this;
        _this.state=1;
//        var canvas = document.querySelector("canvas"),
//          context = canvas.getContext("2d"),
//          width = canvas.width,
//          height = canvas.height;
        console.log(_this.selectedAttr)

        let targetObject=_this.items.diagram.find(_this.findObject);
        let width = 1000;
        let height = 1000;

        console.log(targetObject.nodes.length)

        let kimap = {};

        let nGraph = {
          nodes: [],
          links: []
        }
        for (var i = 0; i !== targetObject.nodes.length; i++) {
          nGraph.nodes.push({
            name: targetObject.nodes[i].name
          });
          kimap[targetObject.nodes[i].name] = i;
        }

        for (var j = 0; j !== targetObject.links.length; j++) {
          var s = targetObject.links[j].source;
          var t = targetObject.links[j].target;

          nGraph.links.push({
            source: kimap[s],
            target: kimap[t],
            value: targetObject.links[j].value
          });
        }

        console.log(nGraph)

        var svg = d3.select(".chart")
//          .append("svg")
          .attr("width",width)
          .attr("height",height);
        var color = d3.scale.category20();


        var force = d3.layout.force()
          .nodes(nGraph.nodes)
          .links(nGraph.links)
          .size([width, height])
          .linkDistance(150)
          .charge([-400]);

        force.start();

        var g = svg.append("g");

        svg.call(d3.behavior.zoom()
          .scaleExtent([1 / 8, 8])
          .on("zoom", zoomed));

        var links = g.append("g")
          .attr("style", "fill: none;stroke: #666;stroke-width: 1.5px;");

        var link = links.selectAll("line")
          .data(nGraph.links)
          .enter().append("line")
          .attr("stroke-width", function (d) {
            return Math.sqrt(d.value) > 10 ? 10 : Math.sqrt(d.value);
          })
          .style("stroke","#333")
          ;

        link.append("title")
          .text(function (d) {
            return d.value;
          });

        var linkText = links.selectAll("text")
          .data(nGraph.links)
          .enter().append("text")
          .attr("class", "link-text")
          .text(function (d) {
            return d.value;
          });

        var forceDrag = force.drag()
          .on("dragstart", function () {
            d3.event.sourceEvent.stopPropagation();
          });

        var nodes = g.append("g")
          .attr("class", "nodes");
        var node = nodes.selectAll("circle")
          .data(nGraph.nodes)
          .enter().append("circle")
          .attr("r", 5)
          .attr("fill", function (d) {
            return color(d.group);
          })
          .call(forceDrag);

        function zoomed() {
          g.attr("transform",
            "translate(" + d3.event.translate + ")scale(" + d3.event.scale + ")");
        };

        node.append("title")
          .text(function (d) {
            return d.name;
          });

        var nodeText = nodes.selectAll("text")
          .data(nGraph.nodes)
          .enter().append("text")
          .attr("class", "node-text")
          .text(function (d) {
            if (d.name.length > 10)
              return d.name.slice(0, 10) + '...';
            else
              return d.name;
          });



        force.on("tick", function () {
          // 更新连线坐标
          link.attr("x1", function (d) {
            return d.source.x;
          })
            .attr("y1", function (d) {
              return d.source.y;
            })
            .attr("x2", function (d) {
              return d.target.x;
            })
            .attr("y2", function (d) {
              return d.target.y;
            });
          linkText.attr("x", function (d) {
            return (d.source.x + d.target.x) / 2;
          })
            .attr("y", function (d) {
              return (d.source.y + d.target.y) / 2;
            });

          // 更新节点坐标
          node.attr("cx", function (d) {
            return d.x;
          })
            .attr("cy", function (d) {
              return d.y;
            });
          nodeText.attr("x", function (d) {
            return d.x - 10;
          })
            .attr("y", function (d) {
              return d.y - 10;
            });
        });

      },

      findObject:function (graph) {
        var _this = this;
        return graph.resourceAttr===_this.selectedAttr;
      }
    }
  }
</script>
