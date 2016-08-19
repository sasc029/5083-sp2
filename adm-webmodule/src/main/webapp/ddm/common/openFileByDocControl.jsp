<%@ page contentType="text/html; charset=UTF-8" pageEncoding="GBK"%>
<%@page import="java.net.URLDecoder"%>
<%
	//获取要打开的文件url和文件名
	String downloadURL = request.getParameter("downloadURL");
	String fileName = request.getParameter("fileName");
	downloadURL = URLDecoder.decode(downloadURL,"UTF-8");
	fileName = URLDecoder.decode(fileName,"UTF-8");
	//插件下载地址
	String activexUrl = "http://"+request.getServerName()+":"+request.getLocalPort()
				+request.getContextPath()+"/ddm/bin/OpenFile.cab#Version=1,0,0,1";
	
%>
<html>
	<head>
		<title>等待打开文件</title>
		<script type="text/javascript">
			function closeWin(){
				docControl.DeleteFile();
			}
			function readyWin(){
				var fileName = "<%=fileName.replaceAll("[/\\\\:\"?<>|*]", "")%>";
				var url = "<%=downloadURL%>";
				docControl.OpenFile(url,fileName);
				if(document.getElementById("wait")){
					document.getElementById("wait").innerText="已加载完毕...";
				}
			}
		</script>
	</head>
	<body onbeforeunload="closeWin()" onload="readyWin()" style="text-align: center;vertical-align: middle;">
		<OBJECT classid="clsid:E043A40C-8384-4522-9B1E-258F5A517778" id="docControl" codebase="<%=activexUrl%>">
			<span style="color:red">文件ActiveX 控件装入失败!-- 请检查浏览器的安全设置。</span>
		</OBJECT>
		<div id="wait" style="border:#ff9900 2px solid; width:200px; height:35px; background-color:#eeeeee; text-align:center; z-index:10px;padding: 25px;"> 
			系统正在处理，请稍候.. 
		</div>
	</body>
</html>