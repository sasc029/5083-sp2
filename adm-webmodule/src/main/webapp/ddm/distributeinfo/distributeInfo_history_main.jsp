<%@page import="java.util.List"%>
<%@page import="com.bjsasc.plm.url.Url"%>
<%@page import="com.bjsasc.ddm.distribute.helper.DistributeHelper"%>
<%@page import="com.bjsasc.ddm.distribute.service.distributeorderobjectlink.DistributeOrderObjectLinkService"%>
<%@page import="com.bjsasc.plm.core.util.StringUtil"%>
<%@page import="com.bjsasc.plm.util.RequestUtil"%>
<%@page import="com.bjsasc.ddm.common.ConstUtil"%>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="GBK"%>
<%@page import="com.bjsasc.ddm.distribute.model.distributeobject.DistributeObject"%>
<%@page import="com.bjsasc.ddm.distribute.model.distributeorderobjectlink.DistributeOrderObjectLink"%>
<%@page import="com.bjsasc.plm.core.persist.model.Persistable"%>
<%@page import="com.bjsasc.plm.Helper"%>

<%
	String contextPath = request.getContextPath();
	String objectOid = null;
	String oid_linkid_str = request.getParameter("oid_linkid_str");
	String order_oids = request.getParameter("order_oids");
	String oid_linkid_strs = "";
	if(StringUtil.isStringEmpty(oid_linkid_str)){
		oid_linkid_str = "";
		//��sessionȡ�����ŵ���oid
		String distributeOrderOid = (String) session.getAttribute(ConstUtil.DISTRIBUTE_ORDER_OID);
		//���ŵ���ַ����ݹ����ķ���
		DistributeOrderObjectLinkService linkService = DistributeHelper.getDistributeOrderObjectLinkService();
		//���ݷ��ŵ���ѯ�ַ�����link
		List<DistributeOrderObjectLink> distributeOrderObjectLink = linkService.getDistributeOrderObjectLinkListByDistributeOrderOid(distributeOrderOid);
		//ƴ�ӷַ�����link��oid�ͷַ�����oid
		for(DistributeOrderObjectLink link : distributeOrderObjectLink){
			//�õ��ַ������oid
			String disObjClassid = link.getToObjectRef().getClassId();
			String disObjInnerid = link.getToObjectRef().getInnerId();
			String disObjOid = Helper.getOid(disObjClassid, disObjInnerid);
			//DistributeObject disObj = (DistributeObject) link.getTo();
			String linkInnerid = link.getInnerId();
			String linkClassid = link.getClassId();
			String linkOid = Helper.getOid(linkClassid, linkInnerid);
			//�ַ�����oid�ͷַ�����link��oidƴ��
			oid_linkid_strs += oid_linkid_str + disObjOid + "_" + linkOid + ",";
		}
		String [] oid_linkid_arr = oid_linkid_strs.split(",");
		String [] oid_linkid = oid_linkid_arr[0].split("_");
		objectOid = oid_linkid[0];
	}else{
		objectOid =	oid_linkid_str;
	}
	String treeUrl = contextPath + "/ddm/distributeobject/distributeObjectHistroy_tree.jsp";
	
	//String contentUrl = contextPath + "/ddm/distributeobject/distributeObject_history.jsp?distributeOrderObjectLinkOids="+oid_linkid[1]+"&OID="+oid_linkid[0]+"&order_oids="+order_oid;
	//String contentUrl = contextPath + "/ddm/distribute/distributeInfoHandle!getHistoryDistributeInfos.action?distributeObjectOid="+objectOid+"&order_oids="+order_oids;
	String contentUrl = contextPath + "/ddm/distribute/distributeInfoHandle!getHistoryDistributeInfos.action?distributeObjectOid="+objectOid;
	String title = "������ʷ";
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
				slaveleft:"distributeHistoryTree",	//���������grid��tree�ؼ���id
				slaveright:"distributeHistoryContent",	//�Ҳ�������grid��tree�ؼ���id
				retfunc:"plm.resizecontrol"});	    //�ص�����,����Ĭ�ϼ���
		});
		</script>
	</head>
	<body scroll="yes" class="body" onload="querySubmit()">
		 
		<table id="table" class="splittable" style="height:100%;width:100%;" cellspacing="0" cellpadding="0" border="0"> 
		<tr> 
			<td id="tdLeft" class="leftpanel" style="width:20%;"> 
				<iframe  name="leftTree" id="leftTree" style="width:100%;height:99.9%;" src=""></iframe></td>
					<form id="historyTree" action="<%=treeUrl %>" target="leftTree" method="post">
					</form>
			</td>
			<td id="tdRight">
					<iframe  name="rightContent" id="rightContent" style="width:100%;height:100%;" src=""></iframe></td>
					<form id="historyContent" action="<%=contentUrl %>" target="rightContent" method="post">
					<input type='hidden' id='order_oids' name='order_oids'/>
					</form>
			</td>
		</tr>
		
	</table>
	</body>
</html>
<script type="text/javascript">
function querySubmit(){
	var oid_linkid_str = window.dialogArguments.document.getElementById("historyOids").value;
	if(oid_linkid_str == "" || oid_linkid_str == null){
		oid_linkid_str = "<%=oid_linkid_strs%>";
	}
	$("#historyTree").append("<input type='hidden' id='oid_linkid_str' name='oid_linkid_str' value='"+oid_linkid_str+"'>");	
	$("#historyTree").submit();
	var disOrderOids = window.dialogArguments.document.getElementById("distributeOrderOids").value;
	$("#order_oids").val(disOrderOids);
	$("#historyContent").submit();
}
</script>