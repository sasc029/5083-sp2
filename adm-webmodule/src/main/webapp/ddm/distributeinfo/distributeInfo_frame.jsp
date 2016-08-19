<%@page import="java.util.List"%>
<%@page import="com.bjsasc.plm.KeyS"%>
<%@page import="com.bjsasc.plm.url.Url"%>
<%@page import="com.bjsasc.plm.util.RequestUtil"%>
<%@page import="com.bjsasc.ddm.common.ConstUtil"%>
<%@page import="com.bjsasc.plm.core.persist.model.Persistable"%>
<%@page import="com.bjsasc.plm.Helper"%>
<%@page import="com.bjsasc.ddm.distribute.model.distributeorder.DistributeOrder"%>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="GBK"%>
<%@include file="/plm/plm.jsp" %>
<%
	String contextPath = request.getContextPath();
	String oid = request.getParameter("OID");
	if(oid == null){
		oid = RequestUtil.getParamOid(request);
	}
	String title = "�ַ���Ϣ";

	String treeUrl = contextPath + "/ddm/distributeinfo/distributeInfo_tree.jsp?OID=" + oid;
	String contentUrl = "";
	//��÷��ŵ�OID
	String distributeOrderOid = null;
	if(oid == null){
		distributeOrderOid = (String)request.getSession().getAttribute(ConstUtil.DISTRIBUTE_ORDER_OID);
	}else{
		distributeOrderOid = oid;
	}
	//��ѯ���ŵ�
	Persistable obj = Helper.getPersistService().getObject(distributeOrderOid);
	DistributeOrder dis = (DistributeOrder)obj;
	
	//���յ���ʾ��Ϣ
	if(null != dis.getOrderType() && ConstUtil.C_ORDERTYPE_2.equals(dis.getOrderType())){
		contentUrl = contextPath + "/ddm/distribute/recDesInfo!getRecInfoByLinkOids.action?OID=" + distributeOrderOid;
	}else if(null != dis.getOrderType() && ConstUtil.C_ORDERTYPE_3.equals(dis.getOrderType())){//���ٵ���ʾ��Ϣ
		contentUrl = contextPath + "/ddm/distribute/recDesInfo!getDesInfoByLinkOids.action?OID=" + distributeOrderOid;
	}else{//���ŵ���ʾ��Ϣ
		contentUrl = contextPath + "/ddm/distribute/distributeInfoHandle!getAllDistributeInfo.action?OID=" + oid;
	}

	session.removeAttribute(ConstUtil.DISTRIBUTE_ORDER_OBJECT_LINK_OIDS);
%>
<html>
<head>
	<title><%=title%></title>
	<script type="text/javascript" src="<%=Url.PLMJS%>" charset="GBK"></script>
	<script type="text/javascript" src="<%=Url.RPMJS%>" charset="GBK"></script>
	<script type="text/javascript"	src="<%=contextPath%>/plm/javascript/uisplitter.js" charset="UTF-8"></script>
	<script type="text/javascript">
	//����splitbar
	$(document).ready(function() {
		$("#table").splitter({
			splitVertical:true,				    //ˮƽ�ָ�Ǵ�ֱ�ָ�,���Ϊtrue,���Ǵ�ֱ�ָ�,Ҳ�������ҷָ�
			A:"tdLeft",						    //���������id,����
			B:"tdRight",					    //�Ҳ�������id,����
			closeableto:20,					    //�Զ����ص���С��Ȼ�߶�,����Ĭ�ϼ���
			slaveleft:"distributeObjectTree",	//���������grid��tree�ؼ���id
			slaveright:"distributeInfoContent",	//�Ҳ�������grid��tree�ؼ���id
			retfunc:"plm.resizecontrol"});	    //�ص�����,����Ĭ�ϼ���
	});
	</script>
</head>
<body>
	<table id="table" class="splittable" style="height:100%;width:100%;" cellspacing="0" cellpadding="0" border="0"> 
		<tr> 
			<td id="tdLeft" class="leftpanel" style="width:20%;"> 
				<iframe id="leftTree" name="leftTree" style="width:100%;height:99.9%;"
					src="<%=treeUrl%>"></iframe>
			</td>
			<td id="tdRight">
				<iframe id="rightContent" name="rightContent" style="width:100%;height:100%; "
					src="<%=contentUrl%>"></iframe>
			</td>
		</tr>
	</table>
</body>
</html>
