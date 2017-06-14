<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
// System.out.print(path);
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<base href="<%=basePath%>">
     
	<meta charset="UTF-8">
	<title>Basic Layout - jQuery EasyUI Demo</title>
	<link rel="stylesheet" type="text/css" href="js/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="js/themes/icon.css">
	<link rel="stylesheet" type="text/css" href="js/demo/demo.css">
	<script type="text/javascript" src="js/jquery.min.js"></script>
	<script type="text/javascript" src="js/jquery.easyui.min.js"></script>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	
	<!-- 加载自定义js -->
    <script type="text/javascript" src="js/raphael.js"></script>
    
    <script type="text/javascript">	
    $(function () {
    	
    	var openDiv = document.createElement("div");
    	openDiv.id = "testid";
    	openDiv.innerHTML = "HelloWorld";
    	document.body.appendChild(openDiv);
    	
    	var hashMap = {   
    		    Set : function(key,value){this[key] = value},   
    		    Get : function(key){return this[key]},   
    		    Contains : function(key){return this.Get(key) == null?false:true},   
    		    Remove : function(key){delete this[key]}   
    		} 
    	
    	hashMap.Set("name","John Smith");   
    	hashMap.Set("age",24);
    	//alert(hashMap.Get("name"));
    	hashMap.Set("name","Hellen");
    	//alert(hashMap.Get("name"));
    	hashMap.Remove("age");
    	
    	
    	//用来存储节点的顺序
    	var connections = [];
    	
    	//拖动节点开始时的事件
    	var dragger = function () {
        	this.ox = this.attr("x");
        	this.oy = this.attr("y");
        	this.animate({ "fill-opacity": .2 }, 500);
    	};
    	
    	var circledragger = function () {
        	this.ox = this.attr("cx");
        	this.oy = this.attr("cy");
        	this.animate({ "fill-opacity": .2 }, 500);
    	};
    	
    	//拖动事件
    	var move = function (dx, dy) {
       		var att = { x: this.ox + dx, y: this.oy + dy };
        	this.attr(att);
        	//var att1 = { x: this.ox+dx+10, y: this.oy+dy+10 };
        	//labels[this.id-1].attr(att1);
        	//$("#test" + this.id).offset({ top: this.oy + dy + 30, left: this.ox + dx + 30 });
        	for (var i = connections.length; i--; ) {
            	r.drawArr(connections[i]);
        	}
    	};
    
    	var circlemove = function (dx, dy) {
       		var att = { cx: this.ox + dx, cy: this.oy + dy };
        	this.attr(att);
        	//$("#circle" + this.id).offset({ top: this.oy + dy + 20, left: this.ox + dx + 20 });
        	for (var i = connections.length; i--; ) {
            	r.drawArr(connections[i]);
        	}
    	};
    	
    	//拖动结束后的事件
    	var up = function () {
        	this.animate({ "fill-opacity": 0 }, 500);
    	};
    	
    	//创建绘图对象
    	var r = Raphael("holder", $(window).width(), $(window).height());
    	
    	//绘制节点
    var shapes = [
                r.rect(190, 100, 60, 40, 4),
                r.rect(290, 80, 60, 40, 4),
                r.rect(290, 180, 60, 40, 4),
                r.rect(450, 100, 60, 40, 4)               
             ];
    
    var circleshapes = [
						r.circle(500,200,20)
                        ];
    
    var test = "testid"
    //定位节点上的文字
    var labels = [
					r.text(190+20,100+20,"ww"),
					r.text(290+10,80+10,"hh"),
					r.text(290+20,180+10,"ef5"),
					r.text(450+20,100+20,"tj")
                  ];
    
    var groups = [];
    var indexofgroup = 0;
    for(var i = 0, ii = shapes.length; i < ii; i++){
    	var group = r.set();
    	group.push(shapes[i]);
    	group.push(labels[i]);
    	groups[indexofgroup] = group;
    	indexofgroup++;
    	
    }
    
    /* $("#" + test).offset({ top: 100 + 30, left: 190 + 30 });
    $("#test2").offset({ top: 80 + 10, left: 290 + 10 });
    $("#test3").offset({ top: 180 + 10, left: 290 + 10 });
    $("#test4").offset({ top: 100 + 10, left: 450 + 10 }); */
    //为节点添加样式和事件，并且绘制节点之间的箭头
    for (var i = 0, ii = shapes.length; i < ii; i++) {
        var color = Raphael.getColor();
        shapes[i].attr({ fill: color, stroke: color, "fill-opacity": 0, "stroke-width": 2, cursor: "move" });
        shapes[i].id = i + 1;
        shapes[i].drag(move, dragger, up);
        shapes[i].dblclick(function () { alert(this.id) })
    }
    
    for (var i = 0, ii = circleshapes.length; i < ii; i++) {
        var color = Raphael.getColor();
        circleshapes[i].attr({ fill: color, stroke: color, "fill-opacity": 0, "stroke-width": 2, cursor: "move" });
        circleshapes[i].id = 5;
        circleshapes[i].drag(circlemove, circledragger, up);
        circleshapes[i].dblclick(function () { alert(this.id) })
    }
    
    //存储节点间的顺序
    connections.push(r.drawArr({ obj1: shapes[0], obj2: shapes[1] }));
    connections.push(r.drawArr({ obj1: shapes[1], obj2: shapes[2] }));
    connections.push(r.drawArr({ obj1: shapes[2], obj2: shapes[3] }));
   // connections.push(r.drawArr({ obj1: groups[3], obj2: circleshapes[0] }));
});
		
	//随着节点位置的改变动态改变箭头
    Raphael.fn.drawArr = function (obj) {
        var point = getStartEnd(obj.obj1, obj.obj2);
        var path1 = getArr(point.start.x, point.start.y, point.end.x, point.end.y, 8);
        if (obj.arrPath) {
            obj.arrPath.attr({ path: path1 });
        } 
        else {
            obj.arrPath = this.path(path1);
        }
        return obj;
    };
    
    function getStartEnd(obj1, obj2) {
        var bb1 = obj1.getBBox(),
            bb2 = obj2.getBBox();
        var p = [
                { x: bb1.x + bb1.width / 2, y: bb1.y - 1 },
                { x: bb1.x + bb1.width / 2, y: bb1.y + bb1.height + 1 },
                { x: bb1.x - 1, y: bb1.y + bb1.height / 2 },
                { x: bb1.x + bb1.width + 1, y: bb1.y + bb1.height / 2 },
                { x: bb2.x + bb2.width / 2, y: bb2.y - 1 },
                { x: bb2.x + bb2.width / 2, y: bb2.y + bb2.height + 1 },
                { x: bb2.x - 1, y: bb2.y + bb2.height / 2 },
                { x: bb2.x + bb2.width + 1, y: bb2.y + bb2.height / 2 }
            ];
        var d = {}, dis = [];
        for (var i = 0; i < 4; i++) {
            for (var j = 4; j < 8; j++) {
                var dx = Math.abs(p[i].x - p[j].x),
                    dy = Math.abs(p[i].y - p[j].y);
                if (
                     (i == j - 4) ||
                     (((i != 3 && j != 6) || p[i].x < p[j].x) &&
                     ((i != 2 && j != 7) || p[i].x > p[j].x) &&
                     ((i != 0 && j != 5) || p[i].y > p[j].y) &&
                     ((i != 1 && j != 4) || p[i].y < p[j].y))
                   ) {
                    dis.push(dx + dy);
                    d[dis[dis.length - 1]] = [i, j];
                }
            }
        }
        if (dis.length == 0) {
            var res = [0, 4];
        } else {
            res = d[Math.min.apply(Math, dis)];
        }
        var result = {};
        result.start = {};
        result.end = {};
        result.start.x = p[res[0]].x;
        result.start.y = p[res[0]].y;
        result.end.x = p[res[1]].x;
        result.end.y = p[res[1]].y;
        return result;
    }
    
  //获取组成箭头的三条线段的路径
    function getArr(x1, y1, x2, y2, size) {
        var angle = Raphael.angle(x1, y1, x2, y2);//得到两点之间的角度
        var a45 = Raphael.rad(angle - 45);//角度转换成弧度
        var a45m = Raphael.rad(angle + 45);
        var x2a = x2 + Math.cos(a45) * size;
        var y2a = y2 + Math.sin(a45) * size;
        var x2b = x2 + Math.cos(a45m) * size;
        var y2b = y2 + Math.sin(a45m) * size;
        var result = ["M", x1, y1, "Q", 200, 200 ,x2, y2, "L", x2a, y2a, "M", x2, y2, "L", x2b, y2b];
        return result;
    }
    </script>
    
</head>

<body>
     
     <div id="holder">
     	
     </div>
    <div id="test1" class="test">测试1</div>
    <div id="test2" class="test"> 测试2</div>
    <div id="test3" class="test"> 测试3</div>
    <div id="test4" class="test">测试4</div>
    
    <div id="circle5" class="test">圆1</div>
 	
</body>
</html>