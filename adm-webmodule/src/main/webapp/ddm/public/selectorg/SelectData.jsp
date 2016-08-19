<%@page contentType="text/html; charset=UTF-8" pageEncoding="GBK"%>
<%@	page import="java.util.*"%>

<!--数据字典查询-->
<%
	// 分发介质类型（0为纸质，1为电子，2为跨域）
	List disMediaTypeListEditor = new ArrayList();
 	disMediaTypeListEditor.add("{id: 0,  name:\'纸质\'}");
 	disMediaTypeListEditor.add("{id: 1,  name:\'电子\'}");
 	disMediaTypeListEditor.add("{id: 2,  name:\'跨域\'}");
%>
var disMediaTypeListEditor =<%=disMediaTypeListEditor%>;