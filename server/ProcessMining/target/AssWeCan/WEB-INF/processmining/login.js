//document.getElementById('loginbutton').onClick = function(){
//	myclick();
//}
//function myclick(){
//      var text1 = document.getElementById('input1').innerHTML;
//      var text2 = document.getElementById('input2').innerHTML;
//      var str = text1 + ',' + text2;
//      setCookie('result',str,365); 
//}
    
function setCookie(name,value,expiredays){
    var exdate = new Date();
    exdate.setDate(exdate.getDate()+expiredays);
    document.cookie = name + "=" + escape(value) + ((expiredays==null) ? "" : ";expires=" + exdate.toGMTString());
}
    
function getCookie(name){
      if(document.cookie.length > 0){
        var start = document.cookie.indexOf(name + "=");
        if(start != -1){
          start = start+ name.length+1;
          var end = document.cookie.indexOf(";",start);
          if(end != -1){
//            end = document.cookie.length;
            return unescape(document.cookie.substring(start,end));
          }
        }
        return "";
     } 
}