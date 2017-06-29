<template>
  <div>
    <h1>Sankey Diagrams</h1>
    <p id="chart"></p>
  </div>
</template>

<style type="text/css" rel="stylesheet/less">

  #chart {
    height: 500px;
    background-color: #58a181;
  }

  path:hover {
    stroke-opacity: 0.9;
  }

</style>
<script>
  import * as d3 from 'd3'
  import {mapActions} from 'vuex'
  let sankey = function () {
    var sankey = {},
      nodeWidth = 24,
      nodePadding = 8,
      size = [1, 1],
      nodes = [],
      links = [];

    sankey.nodeWidth = function (_) {
      if (!arguments.length) return nodeWidth;
      nodeWidth = +_;
      return sankey;
    };

    sankey.nodePadding = function (_) {
      if (!arguments.length) return nodePadding;
      nodePadding = +_;
      return sankey;
    };

    sankey.nodes = function (_) {
      if (!arguments.length) return nodes;
      nodes = _;
      return sankey;
    };

    sankey.links = function (_) {
      if (!arguments.length) return links;
      links = _;
      return sankey;
    };

    sankey.size = function (_) {
      if (!arguments.length) return size;
      size = _;
      return sankey;
    };

    sankey.layout = function (iterations) {
      computeNodeLinks();
      computeNodeValues();
      debugger
      computeNodeBreadths();
      debugger
      computeNodeDepths(iterations);
      computeLinkDepths();
      return sankey;
    };

    sankey.relayout = function () {
      computeLinkDepths();
      return sankey;
    };

    sankey.link = function () {
      var curvature = .5;

      function link(d) {
        var x0 = d.source.x + d.source.dx,
          x1 = d.target.x,
          xi = d3.interpolateNumber(x0, x1),
          x2 = xi(curvature),
          x3 = xi(1 - curvature),
          y0 = d.source.y + d.sy + d.dy / 2,
          y1 = d.target.y + d.ty + d.dy / 2;
        return "M" + x0 + "," + y0
          + "C" + x2 + "," + y0
          + " " + x3 + "," + y1
          + " " + x1 + "," + y1;
      }

      link.curvature = function (_) {
        if (!arguments.length) return curvature;
        curvature = +_;
        return link;
      };

      return link;
    };

    // Populate the sourceLinks and targetLinks for each node.
    // Also, if the source and target are not objects, assume they are indices.
    function computeNodeLinks() {
      nodes.forEach(function (node) {
        node.sourceLinks = [];
        node.targetLinks = [];
      });

      links.forEach(function (link) {
        var source = link.source,
          target = link.target;

        if (typeof source === 'number') source = link.source = nodes[link.source];
        if (typeof target === "number") target = link.target = nodes[link.target];
        source.sourceLinks.push(link);
        target.targetLinks.push(link);

      });
    }

    // Compute the value (size) of each node by summing the associated links.
    function computeNodeValues() {
      nodes.forEach(function (node) {
        node.value = Math.max(
          d3.sum(node.sourceLinks, value),
          d3.sum(node.targetLinks, value)
        );
      });
    }

    // Iteratively assign the breadth (x-position) for each node.
    // Nodes are assigned the maximum breadth of incoming neighbors plus one;
    // nodes with no incoming links are assigned breadth zero, while
    // nodes with no outgoing links are assigned the maximum breadth.
    function computeNodeBreadths() {
      var remainingNodes = nodes,
        nextNodes,
        x = 0;

      while (remainingNodes.length) {
        nextNodes = [];
        remainingNodes.forEach(function (node) {
          node.x = x;
          node.dx = nodeWidth;
          node.sourceLinks.forEach(function (link) {
            if (nextNodes.indexOf(link.target) < 0) {
              nextNodes.push(link.target);
            }
          });
        });
        remainingNodes = nextNodes;
        ++x;
        debugger
      }

      //
      moveSinksRight(x);
      scaleNodeBreadths((size[0] - nodeWidth) / (x - 1));
    }

    function moveSourcesRight() {
      nodes.forEach(function (node) {
        if (!node.targetLinks.length) {
          node.x = d3.min(node.sourceLinks, function (d) {
              return d.target.x;
            }) - 1;
        }
      });
    }

    function moveSinksRight(x) {
      nodes.forEach(function (node) {
        if (!node.sourceLinks.length) {
          node.x = x - 1;
        }
      });
    }

    function scaleNodeBreadths(kx) {
      nodes.forEach(function (node) {
        node.x *= kx;
      });
    }

    function computeNodeDepths(iterations) {
      var nodesByBreadth = d3.nest()
        .key(function (d) {
          return d.x;
        })
        .sortKeys(d3.ascending)
        .entries(nodes)
        .map(function (d) {
          return d.values;
        });

      //
      initializeNodeDepth();
      resolveCollisions();
      for (var alpha = 1; iterations > 0; --iterations) {
        relaxRightToLeft(alpha *= .99);
        resolveCollisions();
        relaxLeftToRight(alpha);
        resolveCollisions();
      }

      function initializeNodeDepth() {
        var ky = d3.min(nodesByBreadth, function (nodes) {
          return (size[1] - (nodes.length - 1) * nodePadding) / d3.sum(nodes, value);
        });

        nodesByBreadth.forEach(function (nodes) {
          nodes.forEach(function (node, i) {
            node.y = i;
            node.dy = node.value * ky;
          });
        });

        links.forEach(function (link) {
          link.dy = link.value * ky;
        });
      }

      function relaxLeftToRight(alpha) {
        nodesByBreadth.forEach(function (nodes, breadth) {
          nodes.forEach(function (node) {
            if (node.targetLinks.length) {
              var y = d3.sum(node.targetLinks, weightedSource) / d3.sum(node.targetLinks, value);
              node.y += (y - center(node)) * alpha;
            }
          });
        });

        function weightedSource(link) {
          return center(link.source) * link.value;
        }
      }

      function relaxRightToLeft(alpha) {
        nodesByBreadth.slice().reverse().forEach(function (nodes) {
          nodes.forEach(function (node) {
            if (node.sourceLinks.length) {
              var y = d3.sum(node.sourceLinks, weightedTarget) / d3.sum(node.sourceLinks, value);
              node.y += (y - center(node)) * alpha;
            }
          });
        });

        function weightedTarget(link) {
          return center(link.target) * link.value;
        }
      }

      function resolveCollisions() {
        nodesByBreadth.forEach(function (nodes) {
          var node,
            dy,
            y0 = 0,
            n = nodes.length,
            i;

          // Push any overlapping nodes down.
          nodes.sort(ascendingDepth);
          for (i = 0; i < n; ++i) {
            node = nodes[i];
            dy = y0 - node.y;
            if (dy > 0) node.y += dy;
            y0 = node.y + node.dy + nodePadding;
          }

          // If the bottommost node goes outside the bounds, push it back up.
          dy = y0 - nodePadding - size[1];
          if (dy > 0) {
            y0 = node.y -= dy;

            // Push any overlapping nodes back up.
            for (i = n - 2; i >= 0; --i) {
              node = nodes[i];
              dy = node.y + node.dy + nodePadding - y0;
              if (dy > 0) node.y -= dy;
              y0 = node.y;
            }
          }
        });
      }

      function ascendingDepth(a, b) {
        return a.y - b.y;
      }
    }

    function computeLinkDepths() {
      nodes.forEach(function (node) {
        node.sourceLinks.sort(ascendingTargetDepth);
        node.targetLinks.sort(ascendingSourceDepth);
      });
      nodes.forEach(function (node) {
        var sy = 0, ty = 0;
        node.sourceLinks.forEach(function (link) {
          link.sy = sy;
          sy += link.dy;
        });
        node.targetLinks.forEach(function (link) {
          link.ty = ty;
          ty += link.dy;
        });
      });

      function ascendingSourceDepth(a, b) {
        return a.source.y - b.source.y;
      }

      function ascendingTargetDepth(a, b) {
        return a.target.y - b.target.y;
      }
    }

    function center(node) {
      return node.y + node.dy / 2;
    }

    function value(link) {
      return link.value;
    }

    return sankey;
  };

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
        data: {
          'nodes': [
            {name:"accept"},
            {name:"collect reviews"},
            {name:"decide"},
            {name:"get review 1"},
            {name:"get review 2"},
            {name:"get review 3"},
            {name:"get review X"},
            {name:"invite additional reviewer"},
            {name:"invite reviewers"},
          ],
          'links': [
            {source: 0, target: 3, value: 10},
            {source: 1, target: 4, value: 10},
            {source: 2, target: 4, value: 5},
            {source: 1, target: 5, value: 5},
            {source: 3, target: 6, value: 5},
            {source: 3, target: 7, value: 5},
            {source: 4, target: 7, value: 10},
            {source: 4, target: 8, value: 5},
            {source: 5, target: 8, value: 5}
          ]
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
      initSVG(){
        var margin = {top: 1, right: 1, bottom: 6, left: 1},
          width = 660 - margin.left - margin.right,
          height = 300 - margin.top - margin.bottom;

        var formatNumber = d3.format(",.0f"),
          format = function(d) { return formatNumber(d) + " TWh"; },
          color = d3.scaleOrdinal(d3.schemeCategory10);

        var svg = d3.select("#chart").append("svg")
          .attr("width", width + margin.left + margin.right)
          .attr("height", height + margin.top + margin.bottom)
          .append("g")
          .attr("transform", "translate(" + margin.left + "," + margin.top + ")");

        // 定义桑基布局
        var sankeyDiagram = sankey()
//        console.log(sankey())
          .nodeWidth(80)
          .nodePadding(40)
          .size([width, height])
          .nodes(this.data.nodes)
          .links(this.data.links)
          .layout(3);

        var path = sankeyDiagram.link();
//
        // 绘制连接数据
        var links = svg.append("g").selectAll("path")
          .data(this.data.links)
          .enter()
//
        // 绑定节点数据
        var nodes = svg.append("g").selectAll(".node")
          .data(this.data.nodes)
          .enter();
        links.append("path")
          .attr({
            fill: "none",   //填充色
            stroke: function(d,i){ return color(i); },  //描边色
            "stroke-opacity": 0.5,  //描边透明度
            d: path,  //路径数据
            id: function(d,i){ return 'link' +i }  //ID
          })
          links.style("stroke-width", function (d) {  //连线的宽度
            return Math.max(1, d.dy);
          });

        // 绘制矩形节点
        nodes.append("rect")
          .attr({
            x: function (d) { return d.x; },
            y: function (d) { return d.y; },
            height: function (d) { return d.dy; },
            width: sankey().nodeWidth(),
            fill: "tomato"
          })
          .call(d3.behavior.drag()
            .origin(function(d) { return d; })
            .on("drag", dragmove)
          );

        // 绘制节点文本
        nodes.append("text")
          .attr({
            x: function (d) { return d.x+sankey().nodeWidth() / 2; },
            y: function (d) { return d.y+d.dy / 2; }
          })
          .text(function (d) { return d.name; });

        // 绘制连接文本
        links.append('text')
          .append('textPath')
          .attr('xlink:href', function (d,i) { return '#link' + i; })
          .attr('startOffset','50%')
          .text(function (d) { return '流量:' + d.value; });

        function dragmove(d) {
          d3.select(this).attr({
            "x": (d.x = Math.max(0, Math.min(width - d.dx, d3.event.x))),
            "y": (d.y = Math.max(0, Math.min(height - d.dy, d3.event.y)))
          });

          sankey.relayout();
          paths.attr('d',path);
        }
      }
    },
    created(){
      this.changeDiagramPath('3')
      this.resolvedLinks()
//      console.log(d3.select("svg").attr('width'))
      console.log(sankey().nodeWidth)
      this.initSVG()
    }
  }
</script>
