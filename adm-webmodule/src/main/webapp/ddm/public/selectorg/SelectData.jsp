<%@page contentType="text/html; charset=UTF-8" pageEncoding="GBK"%>
<%@	page import="java.util.*"%>

<!--�����ֵ��ѯ-->
<%
	// �ַ��������ͣ�0Ϊֽ�ʣ�1Ϊ���ӣ�2Ϊ����
	List disMediaTypeListEditor = new ArrayList();
 	disMediaTypeListEditor.add("{id: 0,  name:\'ֽ��\'}");
 	disMediaTypeListEditor.add("{id: 1,  name:\'����\'}");
 	disMediaTypeListEditor.add("{id: 2,  name:\'����\'}");
%>
var disMediaTypeListEditor =<%=disMediaTypeListEditor%>;