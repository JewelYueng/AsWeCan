<template>
  <div class="sankey">
    <svg class="chart">
    </svg>
    <div class="download"><el-button type="primary" @click="downloadImg">下载</el-button></div>
  </div>
</template>

<style type="text/css" rel="stylesheet/less" lang="less" scoped>

  .sankey{
    display: flex;
    flex-direction: column;
    .show{
      text-align: center;
    }

    .node rect {
      cursor: move;
      fill-opacity: .9;
      shape-rendering: crispEdges;
    }
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
  props: ['sankey'],
  data(){
    return {
      items:{
        "timeCost":541,
        "diagram": {}
      }
    }
  },
  created(){
    this.items.diagram = this.sankey
  },
  mounted(){
    this.produceLayout()
  },
  methods: {
    downloadImg(){
      let svg = d3.select('svg');
      let margin = {
          top: 1,
          right: 1,
          bottom: 6,
          left: 1
        },
        width = 1130 - margin.left - margin.right,
        height = 700;
      let serializer = new XMLSerializer();
      let source = serializer.serializeToString(svg.node());

      source = '<?xml version="1.0" standalone="no"?>\r\n' + source;
      let url = "data:image/svg+xml;charset=utf-8," + encodeURIComponent(source);

      let canvas = document.createElement("canvas");
      canvas.width = width;
      canvas.height = height;

      let context = canvas.getContext("2d");
      let image = new Image;
      image.src = url
      image.onload = function() {
        context.drawImage(image, 0, 0);

        let a = document.createElement("a");
        a.download = "sankey.png";
        a.href = canvas.toDataURL("image/png");
        a.click();
      };
    },
    produceLayout: function () {
      let margin = {
          top: 1,
          right: 1,
          bottom: 6,
          left: 1
        },
        width = 1130 - margin.left - margin.right,
        height = 680;

      let formatNumber = d3.format(",.0f"),
        format = function (d) {
          return formatNumber(d) + " 次";
        },
        color = d3.scale.category20();

      let svg = d3.select(".chart").attr("class", "sankey")
        .attr("width", width + margin.left + margin.right)
        .attr("height", height + margin.top + margin.bottom)
        .append("g")
        .attr("transform", "translate(" + margin.left + "," + margin.top + ")");

      let sankey = d3.sankey()
        .nodeWidth(15)
        .nodePadding(10)
        .size([width, height]);

      let path = sankey.link();


      let kimap = {};
      let outEdgeCountMap = {};
      let inEdgeCountMap = {};
      let nGraph = {
        nodes: [],
        links: []
      }

      for (let i = 0; i !== this.items.diagram.nodes.length; i++) {
        kimap[this.items.diagram.nodes[i].name] = i;
        outEdgeCountMap[this.items.diagram.nodes[i].name] = 1;
        inEdgeCountMap[this.items.diagram.nodes[i].name] = 1;
      }

      let posMap = {};
      for (let j = 0; j !== this.items.diagram.links.length; j++) {
        let s = this.items.diagram.links[j].source;
        let t = this.items.diagram.links[j].target;
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

      for (let i = 0; i !== nGraph.nodes.length; i++) {
        kimap[nGraph.nodes[i].name] = i;
      }

      for (let i = 0; i !== this.items.diagram.links.length; i++) {
        let s = kimap[this.items.diagram.links[i].source];
        let t = kimap[this.items.diagram.links[i].target];
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
      let link = svg.append("g").selectAll(".link")
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
      let node = svg.append("g").selectAll(".node")
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
