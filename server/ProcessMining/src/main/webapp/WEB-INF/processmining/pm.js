function renderWorkflow(datas, selector){
	// Create a new directed graph
	var g = new dagreD3.graphlib.Graph().setGraph({rankdir:"LR"});
	for (var index in datas)
	{
		var data=datas[index];
		var name=data.name;
		// 先设置节点
		data.label=name;
		data.rx=data.ry=5;
		g.setNode(name,data);
		// 此处将output转化为二维数组
	    var subnetNum=data.subnetNum;
	    var subnetList=data.subnetList; 
	 	var output=translate(data.output,subnetNum,subnetList);
	 	var isStart=data.isStart;
	 	var isEnd=data.isEnd;
		var kusuo;
		// 开始画库所和边
	 	if(isStart=="true"){
	 	// 设置开始库所
		   	g.setNode("start",{label:"start",shape:"circle"});
	 	    g.setEdge("start",data.name,{label:"",lineInterpolate: 'basis' , width:30});
	 	}
		if(isEnd=="true"){
		// 设置结束库所
		   	g.setNode("end",{label:"end",shape:"circle"});
			g.setEdge(name,"end",{label:"",lineInterpolate: 'basis' , width:30});
		}else{
			    // 如果输出不为空，则先画source到库所的边
				for(var i in output){
				kusuo=output[i].join("").toUpperCase();
				g.setNode(kusuo,{label:"",shape: "circle"});
				g.setEdge(name,kusuo,{label:"",lineInterpolate: 'basis',width:30});
				// 库所到output的边
				for(var j in output[i]){
				 g.setEdge(kusuo,output[i][j],{label:"",lineInterpolate:"basis",width:30});
			    }
			}
		}
	}
	// 一维数组转化为二维数组
	function translate(arr,col,row)
	{
		var arr1=[];
	    for(var i=0;i<col;i++)
	    {
			var arr2=[];
	        for(var j=0;j<row[i];j++)
			{
				if(arr!=null)
			    arr2[j]=arr.shift();	
			}
			arr1.push(arr2);
		}
		return arr1;
	}
	// Create the renderer
	var render = new dagreD3.render();

	// Set up an SVG group so that we can translate the final graph.
	var svg = d3.select(selector),
	    inner = svg.append("g");

	// Set up zoom support
	var zoom = d3.behavior.zoom().on("zoom", function() {
	    inner.attr("transform", "translate(" + d3.event.translate + ")" +
	                                "scale(" + d3.event.scale + ")");
	  });
	svg.call(zoom).on("dblclick.zoom",null);

	inner.on("dblclick",function(d){
		document.getElementById("attr").innerHTML="双击事件";
		 });
	// Run the renderer. This is what draws the final graph.
	render(inner, g);
	// Center the graph
	var initialScale = 0.5;
	zoom
	  .translate([(svg.attr("width") - g.graph().width * initialScale) / 2, 150])
	  .scale(initialScale)
	  .event(svg);
	// svg.attr('height', g.graph().height * initialScale + 40);
}

function renderDiagraph(data, selector) {
	var params = {
	  'radius': 10,
	  'margin': 30
	};

	var nodeValueMap = {};
	var nodeSizeMap = {};
	var edgeValueMap = {};
	var edgeWidthMap = {};
	var linkMap = {};

	data.nodes.forEach(function(node) {
		nodeValueMap[node.name] = 0;
	  nodeSizeMap[node.name] = params.radius;
	});
	data.links.forEach(function(link) {
		nodeValueMap[link.source] += parseInt(link.value);
	  nodeValueMap[link.target] += parseInt(link.value);
	  edgeValueMap[link.source+":"+link.target] = parseInt(link.value);
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
	function getRatio(v ,range) {
		if (range[1] - range[0] <= 0) {
			return 0;
		} else {
			return (v - range[0])/(range[1] - range[0]);
		}
	}
	var nodeRange = getMinMax(nodeValueMap);
	var edgeRange = getMinMax(edgeValueMap);

	for (var key in nodeValueMap) {
		nodeSizeMap[key] += parseInt(20 * getRatio(nodeValueMap[key], nodeRange));
	}
	for (var key in edgeValueMap) {
		edgeWidthMap[key] = 1 + parseInt(10*getRatio(edgeValueMap[key], edgeRange));
	}

	
	function produceLayout(input, conf) {
	  var g = new dagre.graphlib.Graph();
	  g.setGraph(conf);
	  g.setDefaultEdgeLabel(function() { return {}; });

	  input.nodes.forEach(function(node) {
	    g.setNode(node.name, { label: node.name,  width: nodeSizeMap[node.name]*2, height: nodeSizeMap[node.name]*2 });
	  });
	  input.links.forEach(function(link) {
	    g.setEdge(link.source, link.target);
	  });

	  dagre.layout(g);
	  return g;
	}

	// var line = d3.line()
	// .x(function(d) { return d.x; })
	// .y(function(d) { return d.y; })
	// .curve(d3.curveBasis);
	var line = d3.svg.line()
		.x(function(d) { return d.x; })
		.y(function(d) { return d.y; })
		 .interpolate("basis");

	var layout = produceLayout(data, { rankdir: "LR", nodesep: 80, ranksep: 150, edgesep: 10, marginx: params.radius*2 + params.margin, marginy: params.radius*2 + params.margin });
	
	
	var timers1, timers2;
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
				(function() {
	  			var tNode = node;

		  		var t = setTimeout(function() {
		  			traceGroup.append("circle")
							.attr('cx', tNode.x)
							.attr('cy', tNode.y)
							.attr('r', 5);
					}, du*i);
					traceTimers.push(t);
		  		})();
			} else {
		  		var edge = layout.edge({v: trace[i], w: trace[i+1]});
		  		var str = trace[i]+'->'+trace[i+1];
		  		if (edge === undefined) {
		  			console.log("没有找到边");
		  			continue;
		  		}

		  		(function() {
		  			var tEdge = edge;
		  			var tNode = node;
		  			var tIndex = i;
		  			var tStr = str;

			  		var t = setTimeout(function() {
			  			traceGroup.append("circle")
								.attr('cx', tNode.x)
								.attr('cy', tNode.y)
								.attr('r', 5);

			  			var p = traceGroup.append("path")
				  			.attr("class","edge")
				  			.attr("id", "trace-"+tIndex)
				  			.attr("d", line(tEdge.points)) 
				  			.attr("stroke-width", function(d) {
				  				return 2;
				  			})
				  			.style("stroke", function(d) {
				  				if (traceMap[tStr] === undefined) {
				  					traceMap[tStr] = 1;
				  				} else {
				  					traceMap[tStr] += 1;
				  				}
				  				return traceMap[tStr]%2===1?'red':'blue';
				  			});

				  		var l = $('#trace-'+tIndex)[0].getTotalLength();
				  		p.style("stroke-dasharray", l);
			  			p.style("stroke-dashoffset", l);
				  		p.transition()
				  			.style("stroke-dashoffset", 0)
				  			.duration(du);
						}, du*i);
						traceTimers.push(t);
		  		})();
	  		}
		}

		return traceTimers;
	}

	function cleanTrace() {
		if (timers1 !== undefined) {
			timers1.forEach(function(timer) {
				clearTimeout(timer);
			})
		}
		$(".trace-group").html('');

		if (timers2 !== undefined) {
			timers2.forEach(function(timer) {
				clearTimeout(timer);
			})
			links.attr("stroke-width", function(d) {
				return edgeWidthMap[d.v+':'+d.w];
			});
		}
	}

	function traceAniInit() {
		var selectStr = '<select id="trace-input">';
		for (var i = 0;i !== data.traces.length; i++) {
			selectStr += '<option value="'+data.traces[i]+'">'+data.traces[i]+'</option>';
		}
		selectStr += '</select>';

		$(selector).parent().prepend("<p>单记录动画</p><div class='trace-input'>"+selectStr+"<button id='trace-submit'>运行</button><button id='trace-clean'>清除</button></div>");
		$('#trace-submit').click(function() {
			cleanTrace();

			var trace = $("#trace-input").val().split(",");
			timers1 = runTrace(trace);
		});
		$('#trace-clean').click(function() {
			cleanTrace();
		})
	}

	function allTraceAniInit() {
		var traceTimers = []; 
		$(selector).parent().prepend("<p>全记录动画</p><div><button id='all-trace-run'>运行</button></div>");
		$("#all-trace-run").click(function() {
			cleanTrace();
			links.attr("stroke-width", 0);

			var timerArr = [];
			for (var i = 0 ; i !== data.allTraces.length; i++) {
				var trace = data.allTraces[i].split(",");
				for (var j = 0; j !== trace.length; j++) {
					if (j+1 < trace.length) {
						var tStr = trace[j]+':'+trace[j+1];
						if (!linkMap[tStr]) {
							continue;
						}
						var v = 1/edgeValueMap[tStr] * edgeWidthMap[tStr];

						function creator(str, v) {
							return function() {
								linkMap[str].attr("stroke-width", function() {
									return parseFloat(d3.select(this).attr("stroke-width")) + v;
								});
								linkMap[str].style("stroke", "red").transition().duration(500).style("stroke", "#ccc");
							}
						}

						timerArr.push(creator(tStr, v));
					}
				}
			}

			for (var k=0;k!==timerArr.length;k++) {
				traceTimers.push(setTimeout(timerArr[k], 100*k));
			}
			traceTimers.push(setTimeout(function() {
				links.attr("stroke-width", function(d) {
				  	return edgeWidthMap[d.v+':'+d.w];
				 });
			}, 100*timerArr.length));
			timers2 = traceTimers;
		});
	}

	if (data.traces !== undefined) {
		traceAniInit();
	}

	if (data.allTraces !== undefined) {
		allTraceAniInit();
	}

	var svg = d3.select(selector).attr('class', 'diagraph');

	var marker= 
	    svg.append("marker")
	    .attr("id","arrow")  
	    .attr("markerUnits","userSpaceOnUse")  
	    .attr("markerWidth","12")  
	    .attr("markerHeight","16")  
	    .attr("viewBox","0 0 12 18")   
	    .attr("refX","6")  
	    .attr("refY","10")  
	    .attr("orient","auto") 
	    .append("path")
	    .attr("d", "M2,2 L2,18 L10,10 L2,2")//箭头的路径
	    .attr('fill','red');//箭头颜色


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
	  .attr("d", function(d) {
	  	linkMap[d.v+':'+d.w] = d3.select(this);
	    return line(layout.edge(d).points);
	  })
	  .attr("stroke-width", function(d) {
	  	return edgeWidthMap[d.v+':'+d.w];
	  })
	  .attr("marker-end", "url(processMining.jsp#arrow)");


	// edges.append('text')
	//   .attr('x', function(d) { 
	//   	var p = layout.edge(d).points;
	//   	if (p[p.length-1].x > p[0].x) {
	//   		return p[p.length-1].x - 50;
	//   	} else {
	//   		return p[0].x;
	//   	}
	//   })
	//   .attr('y', function(d) {
	//   	var p = layout.edge(d).points;
	//     return p[p.length-1].y;
	//   })
	//   .text(function(d) {
	//     return edgeValueMap[d.v+':'+d.w];
	//   });

	links.append("title")
	  .text(function(d) {
	  	return edgeValueMap[d.v+':'+d.w];
	  });

	var nodes = g.selectAll(".node")
	  .data(layout.nodes())
	  .enter()
	  .append('g')
	  .attr("class", "node");

	nodes.append("title")
	  .text(function(d) {
	    return d;
	  });

	nodes.append('circle')
	  .attr('cx', function(d) { return layout.node(d).x; })
	  .attr('cy', function(d) { return layout.node(d).y; })
	  .attr('r', function(d) {
	  	return nodeSizeMap[d];
	  });

	nodes.append('text')
	  .attr('x', function(d) { return layout.node(d).x - 30; })
	  .attr('y', function(d) { return layout.node(d).y - nodeSizeMap[d] - 5; })
	  .text(function(d) {
	    if(d.length > 10) 
	      return d.slice(0,10) + '...';
	    else 
	      return d;
	  })
}

function renderForceLayout(data, selector) {
	var svg = d3.select(selector).attr('class', 'force-layout'),
    width = +svg.attr("width"),
    height = +svg.attr("height");

	var color = d3.scale.category20();

	// var simulation = d3.forceSimulation()
	// .force("link", d3.forceLink().id(function(d) { return d.id; }))
	// .force("charge", d3.forceManyBody())
	// .force("center", d3.forceCenter(width / 2, height / 2));


	function pre(graph) {
		var kimap = {};
		var nGraph = {
			nodes: [],
			links: []
		}
		for (var i = 0; i !== graph.nodes.length; i++) {
			nGraph.nodes.push({
				name: graph.nodes[i].name
			});
			kimap[graph.nodes[i].name] = i;
		}

		for (var j = 0;j !== graph.links.length; j++) {
			var s = graph.links[j].source;
			var t = graph.links[j].target;
			
			nGraph.links.push({
				source: kimap[s],
				target:  kimap[t],
				value: graph.links[j].value
			});
		}
		return nGraph;
	} 

	function produceLayout(graph) {
	  var force = d3.layout.force()
	    .nodes(graph.nodes)
	    .links(graph.links)
      .size([width,height]) 
      .linkDistance(150)
      .charge([-400]);

    force.start();

    var g = svg.append("g");

		svg.call(d3.behavior.zoom()
		    .scaleExtent([1 / 8, 8])
		    .on("zoom", zoomed));

		function zoomed() {
		  g.attr("transform",   
	        "translate(" + d3.event.translate + ")scale(" + d3.event.scale + ")");  
		}


		var links = g.append("g")
	    .attr("class", "links");

	  var link =  links.selectAll("line")
	    .data(graph.links)
	    .enter().append("line")
	    .attr("stroke-width", function(d) { return Math.sqrt(d.value)>10?10:Math.sqrt(d.value); });

	  link.append("title")
	    .text(function(d) { return d.value; });

	  var linkText = links.selectAll("text")
		.data(graph.links)
		.enter().append("text")
		.attr("class", "link-text")
	    .text(function(d) { return d.value; });



	  var forceDrag = force.drag()
    	.on("dragstart", function() { d3.event.sourceEvent.stopPropagation(); });

	  var nodes = g.append("g")
	    .attr("class", "nodes");
	  var node = nodes.selectAll("circle")
		.data(graph.nodes)
		.enter().append("circle")
	    .attr("r", 5)
	    .attr("fill", function(d) { return color(d.group); })
	    .call(forceDrag);

	  node.append("title")
	      .text(function(d) { return d.name; });

	  var nodeText = nodes.selectAll("text")
		.data(graph.nodes)
		.enter().append("text")
		.attr("class", "node-text")
	    .text(function(d) {
		    if(d.name.length > 10) 
		      return d.name.slice(0,10) + '...';
		    else 
		      return d.name;
	  	});


	  
	  force.on("tick", function(){ 
	    // 更新连线坐标
	    link.attr("x1",function(d){ return d.source.x; })
	        .attr("y1",function(d){ return d.source.y; })
	        .attr("x2",function(d){ return d.target.x; })
	        .attr("y2",function(d){ return d.target.y; });
	    linkText.attr("x",function(d){ return (d.source.x + d.target.x)/2; })
	    	.attr("y",function(d){ return (d.source.y + d.target.y)/2; });

	    // 更新节点坐标
	    node.attr("cx",function(d){ return d.x; })
	        .attr("cy",function(d){ return d.y; });
	    nodeText.attr("x",function(d){ return d.x - 10; })
	    	.attr("y",function(d){ return d.y - 10; });
	 });

	}

	produceLayout(pre(data));
}

function renderSankey(data, selector) {
	var margin = {
	    top: 1,
	    right: 1,
	    bottom: 6,
	    left: 1
	  },
	  width = 960 - margin.left - margin.right,
	  height = 500 * (1 + parseInt(data.nodes.length/10)) - margin.top - margin.bottom;

	var formatNumber = d3.format(",.0f"),
	  format = function(d) {
	    return formatNumber(d) + " 次";
	  },
	  color = d3.scale.category20();

	var svg = d3.select(selector).attr("class", "sankey")
	  .attr("width", width + margin.left + margin.right)
	  .attr("height", height + margin.top + margin.bottom)
	  .append("g")
	  .attr("transform", "translate(" + margin.left + "," + margin.top + ")");

	var sankey = d3.sankey()
	  .nodeWidth(15)
	  .nodePadding(10)
	  .size([width, height]);

	var path = sankey.link();

	function produceLayout(energy) {
	  sankey
	    .nodes(energy.nodes)
	    .links(energy.links)
	    .layout(32);

	  var link = svg.append("g").selectAll(".link")
	    .data(energy.links)
	    .enter().append("path")
	    .attr("class", "link")
	    .attr("d", path)
	    .style("stroke-width", function(d) {
	      return Math.max(1, d.dy);
	    })
	    .sort(function(a, b) {
	      return b.dy - a.dy;
	    });

	  link.append("title")
	    .text(function(d) {
	      return d.source.name + " → " + d.target.name + "\n" + format(d.value);
	    });

	  var node = svg.append("g").selectAll(".node")
	    .data(energy.nodes)
	    .enter().append("g")
	    .attr("class", "node")
	    .attr("transform", function(d) {
	      return "translate(" + d.x + "," + d.y + ")";
	    });

	  node.append("rect")
	    .style("height", function(d) {
	      return d.dy + "px";
	    })
	    .attr("width", sankey.nodeWidth())
	    .style("fill", function(d) {
	      return d.color = color(d.name.replace(/ .*/, ""));
	    })
	    .style("stroke", function(d) {
	      return d3.rgb(d.color).darker(2);
	    })
	    .append("title")
	    .text(function(d) {
	      return d.name + "\n" + format(d.value);
	    });

	  node.append("text")
	    .attr("x", -6)
	    .attr("y", function(d) {
	      return d.dy / 2;
	    })
	    .attr("dy", ".35em")
	    .attr("text-anchor", "end")
	    .attr("transform", null)
	    .text(function(d) {
	      return d.name;
	    })
	    .filter(function(d) {
	      return d.x < width / 2;
	    })
	    .attr("x", 6 + sankey.nodeWidth())
	    .attr("text-anchor", "start");
	}

	function pre(graph) {
		var kimap = {};
		var outEdgeCountMap = {};
		var inEdgeCountMap = {};
		var nGraph = {
			nodes: [],
			links: []
		}

		for (var i = 0; i !== graph.nodes.length; i++) {
			kimap[graph.nodes[i].name] = i;
			outEdgeCountMap[graph.nodes[i].name] = 1;
			inEdgeCountMap[graph.nodes[i].name] = 1;
		}

		var posMap = {};
		for (var j = 0; j !== graph.links.length; j++) {
			var s = graph.links[j].source;
			var t = graph.links[j].target;
			if (posMap[s] !== undefined) {
				posMap[t] = posMap[s] + outEdgeCountMap[s];
				outEdgeCountMap[s] ++;
			} else if (posMap[t] !== undefined) {
				posMap[s] = posMap[t] - inEdgeCountMap[t];
				inEdgeCountMap[t] ++;
			} else {
				posMap[s] = 0;
				posMap[t] = 1;
				outEdgeCountMap[s] ++;
			}
		}







		nGraph.nodes = graph.nodes.sort(function(a, b) {
			return posMap[a.name] - posMap[b.name];
		});

		for (var i = 0; i !== nGraph.nodes.length; i++) {
			kimap[nGraph.nodes[i].name] = i;
		}

		for (var i = 0; i !== graph.links.length; i++) {
			var s = kimap[graph.links[i].source];
			var t = kimap[graph.links[i].target];
			 if (s > t) {
			 	continue;
			 }
			nGraph.links.push({
				source: s,
				target: t,
				value: parseInt(graph.links[i].value),
			});
		}


		return nGraph;
	}


	produceLayout(pre(data));
}