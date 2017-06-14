<%@ page contentType="image/jpeg" import="java.util.*,java.awt.*,java.io.*,java.awt.image.*,javax.imageio.*" pageEncoding="UTF-8"%>
<%!
Color getRandColor(int fc,int bc){
Random random = new Random();
if(fc>255) fc=255;
if(bc>255) bc=255;
int r=fc+random.nextInt(bc-fc);
int g=fc+random.nextInt(bc-fc);
int b=fc+random.nextInt(bc-fc);
return new Color(r,g,b);
};
%>
<%
response.setHeader("Pragma","No-cache");
response.setHeader("Cache-Control","no-cache");
response.setDateHeader("Expires",0);

int width=93,height=36;
BufferedImage image = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
Graphics g=image.getGraphics();
Random random=new Random();
g.setColor(getRandColor(200,250));
g.fillRect(0,0,width,height);
g.setFont(new Font("Times_New_Roman",Font.PLAIN,26));

g.setColor(getRandColor(160,200));
for (int i=0;i<155;i++)
{
int x = random.nextInt(width);
int y = random.nextInt(height);
int xl = random.nextInt(12);
int yl = random.nextInt(12);
g.drawLine(x,y,x+xl,y+yl);
}

String sRand="";
for (int i=0;i<4;i++){
String rand=String.valueOf(random.nextInt(10));
sRand+=rand;
g.setColor(new Color(20+random.nextInt(110),20+random.nextInt(110),20+random.nextInt(110)));
g.drawString(rand,13*i+19,27);
}
session.setAttribute("rand",sRand);
//String code = (String)session.getAttribute("rand");
//System.out.println("验证码"+code);
g.dispose();
OutputStream output =response.getOutputStream();
ImageIO.write(image,"JPEG",response.getOutputStream());
output.flush();
out.clear();
out = pageContext.pushBody();
%>
