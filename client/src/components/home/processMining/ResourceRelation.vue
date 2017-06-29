<template>
  <div class="resource-relation">
<h1>Resource-Relation Diagram</h1>
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
    <div class="chart" v-for="item in items.diagram" v-if="item.resourceAttr === selectedAttr">

    </div>
  </div>
</template>

<style lang="less" scoped rel="stylesheet/less">
  .links {
    fill: none;
    stroke: #000;
    stroke-opacity: .2;
  }
  .chart {
    height: 500px;
    background-color: #58a181;
  }
</style>
<script src="/static/js/sankey.js"></script>
<script src="http://d3js.org/d3.v3.min.js" charset="utf-8"></script>
<script>
  export default{
    data(){
      return {
        selectedAttr:"",
        items:{
          "timeCost":541,
          "diagram":
            [
              { "resourceAttr":"org:resource",
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
    methods:{
      produceLayout:function () {
        let targetObject=this.items.diagram.find(findObject);
        let width = 1000;
        let height = 1000;
        var svg = d3.select(".chart")
          .append("svg")
          .attr("width",width)
          .attr("height",height);
        let color = d3.scale.category20();
        var force = this.$d3.layout.force()
          .nodes(targetObject.nodes)
          .links(targetObject.links)
          .size([width, height])
          .linkDistance(150)
          .charge([-400]);

        force.start();

        var g = svg.append("g");

        svg.call(this.$d3.behavior.zoom()
          .scaleExtent([1 / 8, 8])
          .on("zoom", zoomed));

        var links = g.append("g")
          .attr("class", "links");

        var link = links.selectAll("line")
          .data(targetObject.links)
          .enter().append("line")
          .attr("stroke-width", function (d) {
            return Math.sqrt(d.value) > 10 ? 10 : Math.sqrt(d.value);
          });

        link.append("title")
          .text(function (d) {
            return d.value;
          });

        var linkText = links.selectAll("text")
          .data(targetObject.links)
          .enter().append("text")
          .attr("class", "link-text")
          .text(function (d) {
            return d.value;
          });

        var forceDrag = force.drag()
          .on("dragstart", function () {
            this.$d3.event.sourceEvent.stopPropagation();
          });

        var nodes = g.append("g")
          .attr("class", "nodes");
        var node = nodes.selectAll("circle")
          .data(targetObject.nodes)
          .enter().append("circle")
          .attr("r", 5)
          .attr("fill", function (d) {
            return color(d.group);
          })
          .call(forceDrag);

        node.append("title")
          .text(function (d) {
            return d.name;
          });

        var nodeText = nodes.selectAll("text")
          .data(targetObject.nodes)
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
      zoomed:function() {
    g.attr("transform",
      "translate(" + this.$d3.event.translate + ")scale(" + this.$d3.event.scale + ")");
  },
      findObject:function (graph) {
        return graph.resourceAttr===this.selectedAttr;
      }
    }
  }
</script>
