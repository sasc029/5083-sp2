<%@page import="com.bjsasc.ddm.common.ConstUtil"%>
<%@page import="com.bjsasc.plm.Helper"%>
<%@page contentType = "text/html; charset=UTF-8" pageEncoding="GBK"%>
<%@page import="com.bjsasc.plm.core.persist.PersistHelper"%>
<%@page import="com.bjsasc.plm.util.JsonUtil"%>
<%@page import="com.bjsasc.plm.url.Url"%>
<%@page import="com.bjsasc.plm.KeyS"%>
<%@page language = "java"%>
<%@page import="java.util.*"%>

<%
String oid=(String) session.getAttribute(ConstUtil.SPOT_DISTRIBUTEPAPERTASK_OID);
	String path = request.getContextPath();
	String contextPath = request.getContextPath();

	String gridId = "listProcessInfo";
	String gridUrl = contextPath + "/ddm/distribute/duplicateProcessHandle!listDuplicateProcess.action?OID="+oid;
	String gridTitle = "";
%>

<html>
<head>  
<title><%=gridTitle%></title>
<script type="text/javascript" src="<%=Url.PLMJS%>" charset="UTF-8"></script>
<script type="text/javascript"	src="<%=contextPath%>/ddm/javascript/ddmutil.js" charset="UTF-8"></script>
</head>
<body leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" scroll="no">
<table height="100%" cellSpacing="0" cellPadding="0" width="100%" border="0">
	<tbody>
			<tr>
				<td valign="top">
      			<table class="pt-grid" id="<%=gridId%>" singleSelect="false" fit="true" autoLoad="true" checkbox="true" 
      				useFilter="false" url="<%=gridUrl%>" pagination="false" onloadsuccess="onLoadSuccess()" >
						<thead>
						    <th field="NUMBER"  width="200" tips="���">���</th>
						    <th field="DDMCONTEXT"  width="150" tips="������">������</th>
						    <th field="VERSION"  width="80" tips="�汾">�汾</th>
					     	<th field="COLLATOR" 	 width="150"  tips="������">������</th>
					   	    <th field="CONTRACTOR"  width="150" tips="��ӡ��">��ӡ��</th>
					     	<th field="FINISHTIME"  width="150" tips="���ʱ��">���ʱ��</th>
					   	    <th field="OPENCOUNT"  width="150" tips="���˴���">���˴���</th>
					</thead>
				</table>
      		</td>
    	</tr>
	</tbody>
</table>
</body>
</html>

<script type="text/javascript">
	function onLoadSuccess() {
		onLoadSuccess_ddm("<%=gridId%>");
	}
	function doCustomizeMethod_returnCount(value) {
		var table = pt.ui.get("<%=gridId%>");
		var data = table.getSelections();
		var oid="";
		for(var i=0;i<data.length;i++){
			oid += data[i].OID;
		}
		var url = '<%=contextPath%>/ddm/distribute/distributeObjectHandle!getDistributeObjectReturnDetail.action?<%=KeyS.CALLBACK%>=reload&<%=KeyS.OID%>=' + oid + '&taskOid=<%=oid%>';
		window.open(url,900,600,"distributeOrderOpen");
	}
	</script>