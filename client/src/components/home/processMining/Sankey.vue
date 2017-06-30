<template>
  <div class="sankey">
    <h1>Sankey Diagrams</h1>
    <div class="show">
    <el-button type="primary" @click="produceLayout()">展现桑基图</el-button>
    </div>
    <svg class="chart">
    </svg>
  </div>
</template>

<style type="text/css" rel="stylesheet/less">

  .sankey{
    display: flex;
    flex-direction: column;
    .show{
      text-align: center;
    }
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
        "timeCost":541,
        "diagram":
          {
            "links":
              [
                {"source":"collect reviews","target":"decide","value":100},
                {"source":"decide","target":"accept","value":45},
                {"source":"decide","target":"invite additional reviewer","value":526},
                {"source":"decide","target":"reject","value":55},
                {"source":"get review 1","target":"collect reviews","value":11},
                {"source":"get review 2","target":"collect reviews","value":10},
                {"source":"get review 3","target":"collect reviews","value":11},
                {"source":"get review X","target":"decide","value":263},
                {"source":"invite additional reviewer","target":"get review X","value":263},
                {"source":"invite additional reviewer","target":"time-out X","value":263},
                {"source":"invite reviewers","target":"get review 1","value":16},
                {"source":"invite reviewers","target":"time-out 1","value":16},
                {"source":"invite reviewers","target":"get review 2","value":19},
                {"source":"invite reviewers","target":"time-out 2","value":12},
                {"source":"invite reviewers","target":"get review 3","value":19},
                {"source":"invite reviewers","target":"time-out 3","value":18},
                {"source":"time-out 1","target":"collect reviews","value":22},
                {"source":"time-out 2","target":"collect reviews","value":26},
                {"source":"time-out 3","target":"collect reviews","value":20},
                {"source":"time-out X","target":"decide","value":263}
              ],
            "nodes":
              [
                {"name":"accept"},
                {"name":"collect reviews"},
                {"name":"decide"},
                {"name":"get review 1"},
                {"name":"get review 2"},
                {"name":"get review 3"},
                {"name":"get review X"},
                {"name":"invite additional reviewer"},
                {"name":"invite reviewers"},
                {"name":"reject"},
                {"name":"time-out 1"},
                {"name":"time-out 2"},
                {"name":"time-out 3"},
                {"name":"time-out X"}
              ]
          }
      }
    }
  },
  methods: {
    produceLayout: function () {
      var margin = {
          top: 1,
          right: 1,
          bottom: 6,
          left: 1
        },
        width = 960 - margin.left - margin.right,
        height = 300 * (1 + parseInt(this.items.diagram.nodes.length / 10)) - margin.top - margin.bottom;

      var formatNumber = d3.format(",.0f"),
        format = function (d) {
          return formatNumber(d) + " 次";
        },
        color = d3.scale.category20();

      var svg = d3.select(".chart").attr("class", "sankey")
        .attr("width", width + margin.left + margin.right)
        .attr("height", height + margin.top + margin.bottom)
        .append("g")
        .attr("transform", "translate(" + margin.left + "," + margin.top + ")");

      var sankey = d3.sankey()
        .nodeWidth(15)
        .nodePadding(10)
        .size([width, height]);

      var path = sankey.link();


      var kimap = {};
      var outEdgeCountMap = {};
      var inEdgeCountMap = {};
      var nGraph = {
        nodes: [],
        links: []
      }

      for (var i = 0; i !== this.items.diagram.nodes.length; i++) {
        kimap[this.items.diagram.nodes[i].name] = i;
        outEdgeCountMap[this.items.diagram.nodes[i].name] = 1;
        inEdgeCountMap[this.items.diagram.nodes[i].name] = 1;
      }

      debugger
      var posMap = {};
      for (var j = 0; j !== this.items.diagram.links.length; j++) {
        var s = this.items.diagram.links[j].source;
        var t = this.items.diagram.links[j].target;
        if (posMap[s] !== undefined) {
          posMap[t] = posMap[s] + outEdgeCountMap[s];
          outEdgeCountMap[s]++;
        } else if (posMap[t] !== undefined) {
          posMap[s] = posMap[t] - inEdgeCountMap[t];
          inEdgeCountMap[t]++;
        } else {
          posMap[s] = 0;
          posMap[t] = 1;
          outEdgeCountMap[s]++;
        }
      }



      nGraph.nodes = this.items.diagram.nodes.sort(function (a, b) {
        return posMap[a.name] - posMap[b.name];
      });

      for (var i = 0; i !== nGraph.nodes.length; i++) {
        kimap[nGraph.nodes[i].name] = i;
      }

      for (var i = 0; i !== this.items.diagram.links.length; i++) {
        var s = kimap[this.items.diagram.links[i].source];
        var t = kimap[this.items.diagram.links[i].target];
        if (s > t) {
          continue;
        }
        nGraph.links.push({
          source: s,
          target: t,
          value: parseInt(this.items.diagram.links[i].value),
        });
      }


      console.log(nGraph)


      sankey
        .nodes(nGraph.nodes)
        .links(nGraph.links)
        .layout(32);

// add in the links
      var link = svg.append("g").selectAll(".link")
        .data(nGraph.links)
        .enter().append("path")
        .attr("class", "link")
        .attr("d", path)
        .attr("stroke-width", function(d) { return Math.max(1, d.dy); })
        .sort(function(a, b) { return b.dy - a.dy; })
        .attr({
          fill: "none",   //填充色
          stroke: function(d,i){ return color(i); },  //描边色
          "stroke-opacity": 0.5,  //描边透明度
          id: function(d,i){ return 'link' +i }  //ID
        });

// add the link titles
      link.append("text")
        .append('textPath')
        .attr('xlink:href', function (d,i) { return '#link' + i; })
        .attr('startOffset','50%')
        .text(function(d) {
          return d.source.name + " → " +
            d.target.name + "\n" + format(d.value); });


// add in the nodes
      var node = svg.append("g").selectAll(".node")
        .data(nGraph.nodes)
        .enter().append("g")
        .attr("class", "node")
        .attr("transform", function(d) {
          return "translate(" + d.x + "," + d.y + ")"; })
        .call(d3.behavior.drag()
          .origin(function(d) { return d; })
          .on("dragstart", function() {
            this.parentNode.appendChild(this); })
          .on("drag", dragmove));

// add the rectangles for the nodes
      node.append("rect")
        .attr("height", function(d) { return d.dy; })
        .attr("width", sankey.nodeWidth())
        .style("fill", function(d) {
          return d.color = color(d.name.replace(/ .*/, "")); })
        .style("stroke", function(d) {
          return d3.rgb(d.color).darker(2); })
        .append("title")
        .text(function(d) {
          return d.name + "\n" + format(d.value); });

// add in the title for the nodes
      node.append("text")
        .attr("y", function(d) { return d.dy/2; })
        .attr("x", function(d) { return d.dx - 6; })
        .attr("dy", "0.35em")
        .attr("text-anchor", "end")
        .text(function(d) { return d.name; })
        .filter(function(d) { return d.x < width / 2; })
        .attr("x", function(d) { return d.dx + 6; })
        .attr("text-anchor", "start")


      node.append("title")
        .text(function(d) { return d.name + "\n" + format(d.value); });

// the function for moving the nodes
      function dragmove(d) {
        d3.select(this).attr("transform",
          "translate(" + (
            d.x = Math.max(0, Math.min(width - d.dx, d3.event.x))
          ) + "," + (
            d.y = Math.max(0, Math.min(height - d.dy, d3.event.y))
          ) + ")");
        sankey.relayout();
        link.attr("d", path);
      }

    }

  }
}
</script>
