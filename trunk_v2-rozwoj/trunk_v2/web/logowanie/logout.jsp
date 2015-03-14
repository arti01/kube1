<%@page  %>
<% 


Cookie[] cookies = request.getCookies();
for (int i = 0; i < cookies.length; i++){
    Cookie cookie = cookies[i];
    System.out.println("NAZWA:>"+cookie.getName()+"<");
    //if((cookie.getName( )).indexOf("JSESSIONIDSSO") == 0 ){
         
    	cookie.setMaxAge(0);
    	cookie.setPath("/");
         response.addCookie(cookie);
         
    }//}
request.getSession().invalidate();

%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<html>
<head>
    <META HTTP-EQUIV="Refresh" CONTENT="3;URL=../index.html">
</head>

<body>
<p>Loading EOD....</p>
</body>
</html>
