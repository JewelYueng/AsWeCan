<template>
  <div>
    <h1>Sankey Diagrams</h1>
    <p id="chart">
      <svg width="960" height="500"></svg>
    </p>

  </div>
</template>

<style type="text/css" rel="stylesheet/less">


  path:hover {
    stroke-opacity: 0.9;
  }

</style>

<script>
  import * as d3 from 'd3'
  import * as d3_sankey from 'd3-sankey'
  import {mapActions} from 'vuex'
  //  let sankey = function () {
  //    var sankey = {},
  //      nodeWidth = 24,
  //      nodePadding = 8,
  //      size = [1, 1],
  //      nodes = [],
  //      links = [];
  //
  //    sankey.nodeWidth = function (_) {
  //      if (!arguments.length) return nodeWidth;
  //      nodeWidth = +_;
  //      return sankey;
  //    };
  //
  //    sankey.nodePadding = function (_) {
  //      if (!arguments.length) return nodePadding;
  //      nodePadding = +_;
  //      return sankey;
  //    };
  //
  //    sankey.nodes = function (_) {
  //      if (!arguments.length) return nodes;
  //      nodes = _;
  //      return sankey;
  //    };
  //
  //    sankey.links = function (_) {
  //      if (!arguments.length) return links;
  //      links = _;
  //      return sankey;
  //    };
  //
  //    sankey.size = function (_) {
  //      if (!arguments.length) return size;
  //      size = _;
  //      return sankey;
  //    };
  //
  //    sankey.layout = function (iterations) {
  //      computeNodeLinks();
  //      computeNodeValues();
  //      debugger
  //      computeNodeBreadths();
  //      debugger
  //      computeNodeDepths(iterations);
  //      computeLinkDepths();
  //      return sankey;
  //    };
  //
  //    sankey.relayout = function () {
  //      computeLinkDepths();
  //      return sankey;
  //    };
  //
  //    sankey.link = function () {
  //      var curvature = .5;
  //
  //      function link(d) {
  //        var x0 = d.source.x + d.source.dx,
  //          x1 = d.target.x,
  //          xi = d3.interpolateNumber(x0, x1),
  //          x2 = xi(curvature),
  //          x3 = xi(1 - curvature),
  //          y0 = d.source.y + d.sy + d.dy / 2,
  //          y1 = d.target.y + d.ty + d.dy / 2;
  //        return "M" + x0 + "," + y0
  //          + "C" + x2 + "," + y0
  //          + " " + x3 + "," + y1
  //          + " " + x1 + "," + y1;
  //      }
  //
  //      link.curvature = function (_) {
  //        if (!arguments.length) return curvature;
  //        curvature = +_;
  //        return link;
  //      };
  //
  //      return link;
  //    };
  //
  //    // Populate the sourceLinks and targetLinks for each node.
  //    // Also, if the source and target are not objects, assume they are indices.
  //    function computeNodeLinks() {
  //      nodes.forEach(function (node) {
  //        node.sourceLinks = [];
  //        node.targetLinks = [];
  //      });
  //
  //      links.forEach(function (link) {
  //        var source = link.source,
  //          target = link.target;
  //
  //        if (typeof source === 'number') source = link.source = nodes[link.source];
  //        if (typeof target === "number") target = link.target = nodes[link.target];
  //        source.sourceLinks.push(link);
  //        target.targetLinks.push(link);
  //
  //      });
  //    }
  //
  //    // Compute the value (size) of each node by summing the associated links.
  //    function computeNodeValues() {
  //      nodes.forEach(function (node) {
  //        node.value = Math.max(
  //          d3.sum(node.sourceLinks, value),
  //          d3.sum(node.targetLinks, value)
  //        );
  //      });
  //    }
  //
  //    // Iteratively assign the breadth (x-position) for each node.
  //    // Nodes are assigned the maximum breadth of incoming neighbors plus one;
  //    // nodes with no incoming links are assigned breadth zero, while
  //    // nodes with no outgoing links are assigned the maximum breadth.
  //    function computeNodeBreadths() {
  //      var remainingNodes = nodes,
  //        nextNodes,
  //        x = 0;
  //
  //      while (remainingNodes.length) {
  //        nextNodes = [];
  //        remainingNodes.forEach(function (node) {
  //          node.x = x;
  //          node.dx = nodeWidth;
  //          node.sourceLinks.forEach(function (link) {
  //            if (nextNodes.indexOf(link.target) < 0) {
  //              nextNodes.push(link.target);
  //            }
  //          });
  //        });
  //        remainingNodes = nextNodes;
  //        ++x;
  //        debugger
  //      }
  //
  //      //
  //      moveSinksRight(x);
  //      scaleNodeBreadths((size[0] - nodeWidth) / (x - 1));
  //    }
  //
  //    function moveSourcesRight() {
  //      nodes.forEach(function (node) {
  //        if (!node.targetLinks.length) {
  //          node.x = d3.min(node.sourceLinks, function (d) {
  //              return d.target.x;
  //            }) - 1;
  //        }
  //      });
  //    }
  //
  //    function moveSinksRight(x) {
  //      nodes.forEach(function (node) {
  //        if (!node.sourceLinks.length) {
  //          node.x = x - 1;
  //        }
  //      });
  //    }
  //
  //    function scaleNodeBreadths(kx) {
  //      nodes.forEach(function (node) {
  //        node.x *= kx;
  //      });
  //    }
  //
  //    function computeNodeDepths(iterations) {
  //      var nodesByBreadth = d3.nest()
  //        .key(function (d) {
  //          return d.x;
  //        })
  //        .sortKeys(d3.ascending)
  //        .entries(nodes)
  //        .map(function (d) {
  //          return d.values;
  //        });
  //
  //      //
  //      initializeNodeDepth();
  //      resolveCollisions();
  //      for (var alpha = 1; iterations > 0; --iterations) {
  //        relaxRightToLeft(alpha *= .99);
  //        resolveCollisions();
  //        relaxLeftToRight(alpha);
  //        resolveCollisions();
  //      }
  //
  //      function initializeNodeDepth() {
  //        var ky = d3.min(nodesByBreadth, function (nodes) {
  //          return (size[1] - (nodes.length - 1) * nodePadding) / d3.sum(nodes, value);
  //        });
  //
  //        nodesByBreadth.forEach(function (nodes) {
  //          nodes.forEach(function (node, i) {
  //            node.y = i;
  //            node.dy = node.value * ky;
  //          });
  //        });
  //
  //        links.forEach(function (link) {
  //          link.dy = link.value * ky;
  //        });
  //      }
  //
  //      function relaxLeftToRight(alpha) {
  //        nodesByBreadth.forEach(function (nodes, breadth) {
  //          nodes.forEach(function (node) {
  //            if (node.targetLinks.length) {
  //              var y = d3.sum(node.targetLinks, weightedSource) / d3.sum(node.targetLinks, value);
  //              node.y += (y - center(node)) * alpha;
  //            }
  //          });
  //        });
  //
  //        function weightedSource(link) {
  //          return center(link.source) * link.value;
  //        }
  //      }
  //
  //      function relaxRightToLeft(alpha) {
  //        nodesByBreadth.slice().reverse().forEach(function (nodes) {
  //          nodes.forEach(function (node) {
  //            if (node.sourceLinks.length) {
  //              var y = d3.sum(node.sourceLinks, weightedTarget) / d3.sum(node.sourceLinks, value);
  //              node.y += (y - center(node)) * alpha;
  //            }
  //          });
  //        });
  //
  //        function weightedTarget(link) {
  //          return center(link.target) * link.value;
  //        }
  //      }
  //
  //      function resolveCollisions() {
  //        nodesByBreadth.forEach(function (nodes) {
  //          var node,
  //            dy,
  //            y0 = 0,
  //            n = nodes.length,
  //            i;
  //
  //          // Push any overlapping nodes down.
  //          nodes.sort(ascendingDepth);
  //          for (i = 0; i < n; ++i) {
  //            node = nodes[i];
  //            dy = y0 - node.y;
  //            if (dy > 0) node.y += dy;
  //            y0 = node.y + node.dy + nodePadding;
  //          }
  //
  //          // If the bottommost node goes outside the bounds, push it back up.
  //          dy = y0 - nodePadding - size[1];
  //          if (dy > 0) {
  //            y0 = node.y -= dy;
  //
  //            // Push any overlapping nodes back up.
  //            for (i = n - 2; i >= 0; --i) {
  //              node = nodes[i];
  //              dy = node.y + node.dy + nodePadding - y0;
  //              if (dy > 0) node.y -= dy;
  //              y0 = node.y;
  //            }
  //          }
  //        });
  //      }
  //
  //      function ascendingDepth(a, b) {
  //        return a.y - b.y;
  //      }
  //    }
  //
  //    function computeLinkDepths() {
  //      nodes.forEach(function (node) {
  //        node.sourceLinks.sort(ascendingTargetDepth);
  //        node.targetLinks.sort(ascendingSourceDepth);
  //      });
  //      nodes.forEach(function (node) {
  //        var sy = 0, ty = 0;
  //        node.sourceLinks.forEach(function (link) {
  //          link.sy = sy;
  //          sy += link.dy;
  //        });
  //        node.targetLinks.forEach(function (link) {
  //          link.ty = ty;
  //          ty += link.dy;
  //        });
  //      });
  //
  //      function ascendingSourceDepth(a, b) {
  //        return a.source.y - b.source.y;
  //      }
  //
  //      function ascendingTargetDepth(a, b) {
  //        return a.target.y - b.target.y;
  //      }
  //    }
  //
  //    function center(node) {
  //      return node.y + node.dy / 2;
  //    }
  //
  //    function value(link) {
  //      return link.value;
  //    }
  //
  //    return sankey;
  //  };

  export default{
    data(){
      return {
        raw: {
          "nodes": [
            {name: "accept"},
            {name: "collect reviews"},
            {name: "decide"},
            {name: "get review 1"},
            {name: "get review 2"},
            {name: "get review 3"},
            {name: "get review X"},
            {name: "invite additional reviewer"},
            {name: "invite reviewers"},
            {name: "reject"},
            {name: "time-out 1"},
            {name: "time-out 2"},
            {name: "time-out 3"},
            {name: "time-out X"}
          ],
          'links': [
            {source: "collect reviews", target: "decide", value: 100},
            {source: "decide", target: "accept", value: 45},
            {source: "decide", target: "invite additional reviewer", value: 526},
            {source: "decide", target: "reject", value: 55},
            {source: "get review 1", target: "collect reviews", value: 11},
            {source: "get review 2", target: "collect reviews", value: 10},
            {source: "get review 3", target: "collect reviews", value: 11},
            {source: "get review X", target: "decide", value: 263},
            {source: "invite additional reviewer", target: "get review X", value: 263},
            {source: "invite additional reviewer", target: "time-out X", value: 263},
            {source: "invite reviewers", target: "get review 1", value: 16},
            {source: "invite reviewers", target: "time-out 1", value: 16},
            {source: "invite reviewers", target: "get review 2", value: 19},
            {source: "invite reviewers", target: "time-out 2", value: 12},
            {source: "invite reviewers", target: "get review 3", value: 19},
            {source: "invite reviewers", target: "time-out 3", value: 18},
            {source: "time-out 1", target: "collect reviews", value: 22},
            {source: "time-out 2", target: "collect reviews", value: 26},
            {source: "time-out 3", target: "collect reviews", value: 20},
            {source: "time-out X", target: "decide", value: 263}
          ]
        },
        energy: {
          "nodes": [{
            "name": "Agricultural 'waste'"
          }, {
            "name": "Bio-conversion"
          }, {
            "name": "Liquid"
          }, {
            "name": "Losses"
          }, {
            "name": "Solid"
          }, {
            "name": "Gas"
          }, {
            "name": "Biofuel imports"
          }, {
            "name": "Biomass imports"
          }, {
            "name": "Coal imports"
          }, {
            "name": "Coal"
          }, {
            "name": "Coal reserves"
          }, {
            "name": "District heating"
          }, {
            "name": "Industry"
          }, {
            "name": "Heating and cooling - commercial"
          }, {
            "name": "Heating and cooling - homes"
          }, {
            "name": "Electricity grid"
          }, {
            "name": "Over generation / exports"
          }, {
            "name": "H2 conversion"
          }, {
            "name": "Road transport"
          }, {
            "name": "Agriculture"
          }, {
            "name": "Rail transport"
          }, {
            "name": "Lighting & appliances - commercial"
          }, {
            "name": "Lighting & appliances - homes"
          }, {
            "name": "Gas imports"
          }, {
            "name": "Ngas"
          }, {
            "name": "Gas reserves"
          }, {
            "name": "Thermal generation"
          }, {
            "name": "Geothermal"
          }, {
            "name": "H2"
          }, {
            "name": "Hydro"
          }, {
            "name": "International shipping"
          }, {
            "name": "Domestic aviation"
          }, {
            "name": "International aviation"
          }, {
            "name": "National navigation"
          }, {
            "name": "Marine algae"
          }, {
            "name": "Nuclear"
          }, {
            "name": "Oil imports"
          }, {
            "name": "Oil"
          }, {
            "name": "Oil reserves"
          }, {
            "name": "Other waste"
          }, {
            "name": "Pumped heat"
          }, {
            "name": "Solar PV"
          }, {
            "name": "Solar Thermal"
          }, {
            "name": "Solar"
          }, {
            "name": "Tidal"
          }, {
            "name": "UK land based bioenergy"
          }, {
            "name": "Wave"
          }, {
            "name": "Wind"
          }],
          "links": [{
            "source": 0,
            "target": 1,
            "value": 124.729
          }, {
            "source": 1,
            "target": 2,
            "value": 0.597
          }, {
            "source": 1,
            "target": 3,
            "value": 26.862
          }, {
            "source": 1,
            "target": 4,
            "value": 280.322
          }, {
            "source": 1,
            "target": 5,
            "value": 81.144
          }, {
            "source": 6,
            "target": 2,
            "value": 35
          }, {
            "source": 7,
            "target": 4,
            "value": 35
          }, {
            "source": 8,
            "target": 9,
            "value": 11.606
          }, {
            "source": 10,
            "target": 9,
            "value": 63.965
          }, {
            "source": 9,
            "target": 4,
            "value": 75.571
          }, {
            "source": 11,
            "target": 12,
            "value": 10.639
          }, {
            "source": 11,
            "target": 13,
            "value": 22.505
          }, {
            "source": 11,
            "target": 14,
            "value": 46.184
          }, {
            "source": 15,
            "target": 16,
            "value": 104.453
          }, {
            "source": 15,
            "target": 14,
            "value": 113.726
          }, {
            "source": 15,
            "target": 17,
            "value": 27.14
          }, {
            "source": 15,
            "target": 12,
            "value": 342.165
          }, {
            "source": 15,
            "target": 18,
            "value": 37.797
          }, {
            "source": 15,
            "target": 19,
            "value": 4.412
          }, {
            "source": 15,
            "target": 13,
            "value": 40.858
          }, {
            "source": 15,
            "target": 3,
            "value": 56.691
          }, {
            "source": 15,
            "target": 20,
            "value": 7.863
          }, {
            "source": 15,
            "target": 21,
            "value": 90.008
          }, {
            "source": 15,
            "target": 22,
            "value": 93.494
          }, {
            "source": 23,
            "target": 24,
            "value": 40.719
          }, {
            "source": 25,
            "target": 24,
            "value": 82.233
          }, {
            "source": 5,
            "target": 13,
            "value": 0.129
          }, {
            "source": 5,
            "target": 3,
            "value": 1.401
          }, {
            "source": 5,
            "target": 26,
            "value": 151.891
          }, {
            "source": 5,
            "target": 19,
            "value": 2.096
          }, {
            "source": 5,
            "target": 12,
            "value": 48.58
          }, {
            "source": 27,
            "target": 15,
            "value": 7.013
          }, {
            "source": 17,
            "target": 28,
            "value": 20.897
          }, {
            "source": 17,
            "target": 3,
            "value": 6.242
          }, {
            "source": 28,
            "target": 18,
            "value": 20.897
          }, {
            "source": 29,
            "target": 15,
            "value": 6.995
          }, {
            "source": 2,
            "target": 12,
            "value": 121.066
          }, {
            "source": 2,
            "target": 30,
            "value": 128.69
          }, {
            "source": 2,
            "target": 18,
            "value": 135.835
          }, {
            "source": 2,
            "target": 31,
            "value": 14.458
          }, {
            "source": 2,
            "target": 32,
            "value": 206.267
          }, {
            "source": 2,
            "target": 19,
            "value": 3.64
          }, {
            "source": 2,
            "target": 33,
            "value": 33.218
          }, {
            "source": 2,
            "target": 20,
            "value": 4.413
          }, {
            "source": 34,
            "target": 1,
            "value": 4.375
          }, {
            "source": 24,
            "target": 5,
            "value": 122.952
          }, {
            "source": 35,
            "target": 26,
            "value": 839.978
          }, {
            "source": 36,
            "target": 37,
            "value": 504.287
          }, {
            "source": 38,
            "target": 37,
            "value": 107.703
          }, {
            "source": 37,
            "target": 2,
            "value": 611.99
          }, {
            "source": 39,
            "target": 4,
            "value": 56.587
          }, {
            "source": 39,
            "target": 1,
            "value": 77.81
          }, {
            "source": 40,
            "target": 14,
            "value": 193.026
          }, {
            "source": 40,
            "target": 13,
            "value": 70.672
          }, {
            "source": 41,
            "target": 15,
            "value": 59.901
          }, {
            "source": 42,
            "target": 14,
            "value": 19.263
          }, {
            "source": 43,
            "target": 42,
            "value": 19.263
          }, {
            "source": 43,
            "target": 41,
            "value": 59.901
          }, {
            "source": 4,
            "target": 19,
            "value": 0.882
          }, {
            "source": 4,
            "target": 26,
            "value": 400.12
          }, {
            "source": 4,
            "target": 12,
            "value": 46.477
          }, {
            "source": 26,
            "target": 15,
            "value": 525.531
          }, {
            "source": 26,
            "target": 3,
            "value": 787.129
          }, {
            "source": 26,
            "target": 11,
            "value": 79.329
          }, {
            "source": 44,
            "target": 15,
            "value": 9.452
          }, {
            "source": 45,
            "target": 1,
            "value": 182.01
          }, {
            "source": 46,
            "target": 15,
            "value": 19.013
          }, {
            "source": 47,
            "target": 15,
            "value": 289.366
          }]
        }
      }
    },
    methods: {
      ...mapActions(['changeDiagramPath']),
      resolvedLinks(){
//        深复制原始数据对象
        this.solvedResult = JSON.parse(JSON.stringify(this.raw))
        this.solvedResult.links.map((link) => {
          link.source = this.solvedResult.nodes.findIndex((node) => {
            return node.name === link.source

          })
          link.target = this.solvedResult.nodes.findIndex(node => {
            return node.name === link.target
          })
        })
      },
//      initSVG(){
//        var margin = {top: 1, right: 1, bottom: 6, left: 1},
//          width = 660 - margin.left - margin.right,
//          height = 300 - margin.top - margin.bottom;
//
//        var formatNumber = d3.format(",.0f"),
//          format = function(d) { return formatNumber(d) + " TWh"; },
//          color = d3.scaleOrdinal(d3.schemeCategory10);
//
//        var svg = d3.select("#chart").append("svg")
//          .attr("width", width + margin.left + margin.right)
//          .attr("height", height + margin.top + margin.bottom)
//          .append("g")
//          .attr("transform", "translate(" + margin.left + "," + margin.top + ")");
//
//        // 定义桑基布局
//        var sankeyDiagram = sankey()
////        console.log(sankey())
//          .nodeWidth(80)
//          .nodePadding(40)
//          .size([width, height])
//          .nodes(this.data.nodes)
//          .links(this.data.links)
//          .layout(3);
//
//        var path = sankeyDiagram.link();
////
//        // 绘制连接数据
//        var links = svg.append("g").selectAll("path")
//          .data(this.data.links)
//          .enter()
////
//        // 绑定节点数据
//        var nodes = svg.append("g").selectAll(".node")
//          .data(this.data.nodes)
//          .enter();
//        links.append("path")
//          .attr({
//            fill: "none",   //填充色
//            stroke: function(d,i){ return color(i); },  //描边色
//            "stroke-opacity": 0.5,  //描边透明度
//            d: path,  //路径数据
//            id: function(d,i){ return 'link' +i }  //ID
//          })
//          links.style("stroke-width", function (d) {  //连线的宽度
//            return Math.max(1, d.dy);
//          });
//
//        // 绘制矩形节点
//        nodes.append("rect")
//          .attr({
//            x: function (d) { return d.x; },
//            y: function (d) { return d.y; },
//            height: function (d) { return d.dy; },
//            width: sankey().nodeWidth(),
//            fill: "tomato"
//          })
//          .call(d3.behavior.drag()
//            .origin(function(d) { return d; })
//            .on("drag", dragmove)
//          );
//
//        // 绘制节点文本
//        nodes.append("text")
//          .attr({
//            x: function (d) { return d.x+sankey().nodeWidth() / 2; },
//            y: function (d) { return d.y+d.dy / 2; }
//          })
//          .text(function (d) { return d.name; });
//
//        // 绘制连接文本
//        links.append('text')
//          .append('textPath')
//          .attr('xlink:href', function (d,i) { return '#link' + i; })
//          .attr('startOffset','50%')
//          .text(function (d) { return '流量:' + d.value; });
//
//        function dragmove(d) {
//          d3.select(this).attr({
//            "x": (d.x = Math.max(0, Math.min(width - d.dx, d3.event.x))),
//            "y": (d.y = Math.max(0, Math.min(height - d.dy, d3.event.y)))
//          });
//
//          sankey.relayout();
//          paths.attr('d',path);
//        }
//      }
      initSVG(){
        var svg = d3.select("svg"),
          width = +svg.attr("width"),
          height = +svg.attr("height");

        var formatNumber = d3.format(",.0f"),
          format = function (d) {
            return formatNumber(d) + " TWh";
          },
          color = d3.scaleOrdinal(d3.schemeCategory10);

        d3_sankey.sankey()
          .nodeWidth(15)
          .nodePadding(10)
          .extent([
            [1, 1],
            [width - 1, height - 6]
          ]);


        var link = svg.append("g")
          .attr("class", "links")
          .attr("fill", "none")
          .attr("stroke", "#000")
          .attr("stroke-opacity", 0.2)
          .selectAll("path");

        var node = svg.append("g")
          .attr("class", "nodes")
          .attr("font-family", "sans-serif")
          .attr("font-size", 10)
          .selectAll("g");

        d3_sankey.sankey(this.energy);
        console.log(d3_sankey.sankeyLinkHorizontal())
        debugger
        link = link
          .data(this.energy.links)
          .enter().append("path")
//          .attr("d", 'M16,136.6610907581682C75.85714285714286,136.6610907581682,75.85714285714286,141.90009870097748,135.71428571428572,141.90009870097748')
          .attr("d", d3_sankey.sankeyLinkHorizontal())
          .attr("stroke-width", function (d) {
            return Math.max(1, d.width);
          });

        console.log(link.attr("d"))

        link.append("title")
          .text(function (d) {
            return d.source.name + " → " + d.target.name + "\n" + format(d.value);
          });

        node = node
          .data(this.energy.nodes)
          .enter().append("g");

        node.append("rect")
          .attr("x", function (d) {
            return d.x0;
          })
          .attr("y", function (d) {
            return d.y0;
          })
          .attr("height", function (d) {
            return d.y1 - d.y0;
          })
          .attr("width", function (d) {
            return d.x1 - d.x0;
          })
          .attr("fill", function (d) {
            return color(d.name.replace(/ .*/, ""));
          })
          .attr("stroke", "#000");

        node.append("text")
          .attr("x", function (d) {
            return d.x0 - 6;
          })
          .attr("y", function (d) {
            return (d.y1 + d.y0) / 2;
          })
          .attr("dy", "0.35em")
          .attr("text-anchor", "end")
          .text(function (d) {
            return d.name;
          })
          .filter(function (d) {
            return d.x0 < width / 2;
          })
          .attr("x", function (d) {
            return d.x1 + 6;
          })
          .attr("text-anchor", "start");

        node.append("title")
          .text(function (d) {
            return d.name + "\n" + format(d.value);
          });

      }


    },
    created(){
      this.changeDiagramPath('3')
      this.resolvedLinks()
      console.log(d3.select("svg").attr('width'))
      this.initSVG()
    }
  }
</script>
