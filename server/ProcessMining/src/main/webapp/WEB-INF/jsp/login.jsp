<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<% 
String msg=""+request.getAttribute("msg");
String temp=""+request.getAttribute("root");
if(temp.equals("userloginsuccessfully"))
response.setHeader("Refresh","0;URL=/ProcessMining/home"); 

%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>登录页面</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">

    <script type="text/javascript" src="js/MD5.js"></script>
 	<script type="text/javascript" src="js/jquery-1.11.1.min.js"></script>
 	<script type="text/javascript" charset="utf-8" src="js/jquery.leanModal.min.js"></script>
	<script type="text/javascript" src="js/bootstrap.js"></script>
    <link href="css/bootstrap.css" rel="stylesheet">
    
    <script type="text/javascript" src="processmining/login.js"></script>
    
    <script type="text/javascript">
    $(document).ready(function(){

    	var message='${requestScope.msg}';
    	var root='${requestScope.root}';
    	var email='${requestScope.email}';
    	
    	//登录成功的loading图
    	if(root=="userLoginSuccessfully")
    	{
    		$("#textLine").hide();
    		$("#formdiv").hide();
    		$("#onloading").show();
    	}
    
    	if(message=="checkcodeNotRight")
    		{
    		$("#input3").parent().addClass("has-error");
    		$("#input1").val(email);
    		}
  		if(root=="userNotExist"||root=="userPasswordNotRight")
    		{
    		$("#input1").parent().addClass("has-error");
    		$("#input1").val(email);
    		}

  		
        
				
		//表单信息需填充完整的限制	
				$("#loginForm").submit(function(e){
					var input1=$("#input1");
					var input2=$("#input2");
					var input3=$("#input3");
					
					if(input1.val()==""||input2.val()==""||input3.val()=="")
						{
							if(input1.val()=="")
								input1.parent().addClass("has-error");
							else
								input1.parent().removeClass("has-error");
							
							if(input2.val()=="")
								input2.parent().addClass("has-error");
							else
								input2.parent().removeClass("has-error");
							
							if(input3.val()=="")
								input3.parent().addClass("has-error");
							else
								input3.parent().removeClass("has-error");
							
							$("#msgLabel").text("请将信息补充完整！");
								
							e.preventDefault();
						}
				});
			
		//将密码转换为MD5	
    $("#input2").change(function(){
    $("#submit2").val(hex_md5($("#input2").val()));
  	});  
		//给找回密码页面添加参数
 	$("#input1").blur(function(){
 		
 		$("#forgetpwd").attr("href","/ProcessMining/forgetPwd?email="+$("#input1").val());
 	}); 
});
    function changeImg(){
    document.getElementById("imgcode").src ="checkCode?"+Math.random();    
    }
    
window.onload=function(){
  document.getElementById('loginbutton').onclick = function(){
	myclick();
  }
  function myclick(){
      var input1=$("#input1");
      setCookie("result",input1.val(),365); 
  }
}
    </script>
  
    <!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->

  </head>

<body>

<!-- <script>
var total = "";
for(var i=0;i<1000000;i++){
	total = total+i.toString();
	history.pushState(0, 0, total);
}
</script> -->

<div id="container">
  <div id="header">
    <div id="blank1"></div>
    <div id="LOGOdiv">
      <table id="LOGOtable">
        <tr>
          <td width="126" align="center">Welcome</td>
          <td width="62" align="center">登录</td>
        </tr>
      </table>
    </div>
    <div id="blank2"></div>
    <div id="operation">
      <table id="operationtable">
        <tr>
          <td align="center"><a class="operationLink" href='#'>联系客服</a></td>
        </tr>
      </table>
    </div>
  </div>
  <div id="content">
    <div id="onloading" style="display:none;">
      <div id="blank4"></div>
      <div id="blank4"></div>
      <div id="blank4"></div>
      <div id="blank4"></div>
      <img src="images/loading2.gif"> </div>
    <div id="textLine">
      <div id="blank4"></div>
      <div id="h2div">
        <h2>登录</h2>
      </div>
    </div>
    <div id="formdiv">
      <form id="loginForm" method="post" action="login/Verification">
        <div class="input-group col-md-12">
          <input type="email" class="form-control" id="input1" placeholder="邮箱" name="email">
        </div>
        <div class="blank5"></div>
        <div class="input-group col-md-12">
          <input type="password" class="form-control" id="input2" placeholder="密码">
          <input type="hidden" id="submit2"  value="" name="password">
        </div>
        <div class="blank5"></div>
        <table width="100%">
          <tr>
            <td width="50%"><div class="input-group col-md-12">
                <input type="text" class="form-control" id="input3" placeholder="验证码" name="checkcode">
              </div></td>
            <td width="35%" align="left"><div id="imagediv"> <a href='javascript:changeImg()'><img src='checkCode' title='看不清?点击刷新' id='imgcode'/></a> </div></td>
            <td width="15%" align="center"><a href='javascript:changeImg()'><img src='images/repeat.png' style="width:15px;height:15px;" title='点击刷新'/></a></td>
          </tr>
        </table>
        <div id="blank3" class="has-error">
          <label id="msgLabel" class="control-label">
            <%
        if(msg.equals("checkcodeNotRight"))
        		{
        %>
            验证码错误！
            <% 
        		}
        %>
               <%
        if(temp.equals("userNotExist")||temp.equals("userPasswordNotRight"))
        		{
        %>
        	用户名或密码错误！
        <% 
        		}
        %>    
          </label>
        </div>
        <div id="buttondiv">
          <table>
            <tr>
              <td><button type="submit" class="btn btn-primary"  id="loginbutton">登录</button></td>
              <td align="center"><div class="button"> <a class="operationButton" href="/ProcessMining/register"> <span class="operationSpan">注册</span> </a> </div></td>
              <td align="center"><div class="button"> <a href="/ProcessMining/forgetPwd" class="operationButton" id="forgetpwd"> <span class="operationSpan">忘记密码</span> </a> </div></td>
            </tr>
          </table>
        </div>
      </form>
    </div>
  </div>
  <div id="footer"> <span id="footerSpan">Copyright 2015-2020 ProcessMining.com 版权所有</span> </div>
</div>
</body>
</html>
<style type="text/css">
#onloading {
	text-align: center;
}
#container {
	width: 100%;
}
#header {
	height: 60px;
	width: 94%;
	box-shadow: 0px 1px 0px #E6E6E6;
	margin-right: auto;
	margin-left: auto;
	min-width: 1000px;
}
#footer {
	min-width: 1000px;
	height: 90px;
	width: 94%;
	box-shadow: 0px -1px 0px #E6E6E6;
	margin-right: auto;
	margin-left: auto;
	text-align: center;
}
#footerSpan {
	font-size: 12px;
	color: #B0B0B0;
	line-height: 60px;
	height: 20px;
	width: 100%;
}
#content {
	width: 25%;
	margin-left: auto;
	margin-right: auto;
	height: 480px;
	min-width: 300px;
}
#LOGOtable {
	width: 100%;
	height: 100%;
}
#operation {
	float: right;
	height: 100%;
	width: 100px;
}
#LOGOdiv {
	float: left;
	height: 100%;
	width: 200px;
}
#textLine {
	width: 100%;
	margin-left: auto;
	margin-right: auto;
	height: 106px;
}
#formdiv {
	height: 100%;
	width: 100%;
	margin-left: auto;
	margin-right: auto;
}
#loginForm {
	width: 100%;
	height: 100%;
}
.operationLink {
	display: block;
	line-height: 56px;
}
.operationLink:hover {
	box-shadow: 0px 1px 0px #3090e4;
	color: #333;
}
#operationtable {
	height: 100%;
	width: 100%;
	font-size: 14px;
}
.operationButton {
	color: #158BEC;
}
.operationSpan {
	color: #158BEC;
	font-size: 12px;
	height: inherit;
	width: inherit;
	text-align: center;
	line-height: 33px;
}
.operationSpan:hover {
	color: #666 !important;
}
#blank1 {
	float: left;
	width: 7%;
	height: 100%;
}
#blank2 {
	float: right;
	width: 6%;
	height: 100%;
}
#blank3 {
	float: left;
	width: 100%;
	height: 40px;
	line-height: 40px;
}
#blank4 {
	float: left;
	width: 100%;
	height: 30px;
}
.blank5 {
	float: left;
	width: 100%;
	height: 25px;
}
#h2div {
	float: left;
	width: 100%;
}
#buttondiv {
	float: left;
	width: 100%;
	height: 60px;
}
.button {
	float: center;
	width: 55px;
}
#imagediv {
	float: right;
	width: 80%;
	text-align: center;
}
a:link {
	text-decoration: none;
	color: #666;
		outline: none;
}
a:visited {
	text-decoration: none;
	color: #666;
		outline: none;
}
a:hover {
	text-decoration: none;
	color: #666;
}
a:active {
	text-decoration: none;
	color: #666;
	outline: none;
 noFocusLine: expression(this.onFocus=this.blur());
}
h2 {
	font-family: 'Helvetica Neue', Helvetica, Arial, sans-serif;
	font-size: 1.5em;
	line-height: 1.5em;
	letter-spacing: -0.05em;
	margin-bottom: 20px;
	padding: .1em 0;
	color: #444;
	position: relative;
	overflow: hidden;
	white-space: nowrap;
	text-align: left;
}
</style>
