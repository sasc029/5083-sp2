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
	String title = "分发信息";

	String treeUrl = contextPath + "/ddm/distributeinfo/distributeInfo_tree.jsp?OID=" + oid;
	String contentUrl = "";
	//获得发放单OID
	String distributeOrderOid = null;
	if(oid == null){
		distributeOrderOid = (String)request.getSession().getAttribute(ConstUtil.DISTRIBUTE_ORDER_OID);
	}else{
		distributeOrderOid = oid;
	}
	//查询发放单
	Persistable obj = Helper.getPersistService().getObject(distributeOrderOid);
	DistributeOrder dis = (DistributeOrder)obj;
	
	//回收单显示信息
	if(null != dis.getOrderType() && ConstUtil.C_ORDERTYPE_2.equals(dis.getOrderType())){
		contentUrl = contextPath + "/ddm/distribute/recDesInfo!getRecInfoByLinkOids.action?OID=" + distributeOrderOid;
	}else if(null != dis.getOrderType() && ConstUtil.C_ORDERTYPE_3.equals(dis.getOrderType())){//销毁单显示信息
		contentUrl = contextPath + "/ddm/distribute/recDesInfo!getDesInfoByLinkOids.action?OID=" + distributeOrderOid;
	}else{//发放单显示信息
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
	//增加splitbar
	$(document).ready(function() {
		$("#table").splitter({
			splitVertical:true,				    //水平分割还是垂直分割,如果为true,就是垂直分割,也就是左右分割
			A:"tdLeft",						    //左侧容器的id,必须
			B:"tdRight",					    //右侧容器的id,必须
			closeableto:20,					    //自动隐藏的最小宽度或高度,保持默认即可
			slaveleft:"distributeObjectTree",	//左侧容器中grid或tree控件的id
			slaveright:"distributeInfoContent",	//右侧容器中grid或tree控件的id
			retfunc:"plm.resizecontrol"});	    //回调函数,保持默认即可
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
