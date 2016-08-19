<%@page import="jmathlib.toolbox.io.systemcommand"%>
<%@page import="java.util.List"%>
<%@page import="com.bjsasc.plm.KeyS"%>
<%@page import="com.bjsasc.plm.Helper"%>
<%@page import="com.bjsasc.plm.url.Url"%>
<%@page import="com.bjsasc.plm.url.Url"%>
<%@page import="com.bjsasc.plm.ui.ValidateHelper"%>
<%@page import="com.bjsasc.plm.util.RequestUtil"%>
<%@page import="com.bjsasc.plm.core.doc.Document"%>
<%@page import="com.bjsasc.plm.core.doc.DocumentMaster"%>
<%@page import="com.bjsasc.plm.core.persist.model.Persistable"%>
<%@page import="com.bjsasc.ddm.common.ConstUtil"%>

<%@page import="com.bjsasc.ddm.distribute.helper.DistributeHelper"%>
<%@page import="com.bjsasc.ddm.distribute.model.distributeobject.DistributeObject"%>
<%@page import="com.bjsasc.ddm.distribute.service.distributeobject.DistributeObjectService"%>
<%@page import="com.bjsasc.ddm.transfer.service.DdmDataTransferService"%>
<%@page import="com.bjsasc.ddm.transfer.helper.DdmDataTransferHelper"%>
<%@page import="com.bjsasc.ddm.distribute.service.distributeorderobjectlink.DistributeOrderObjectLinkService"%>

<%@page import="com.bjsasc.plm.core.vc.model.Iterated"%>
<%@page import="com.bjsasc.plm.core.vc.model.Mastered"%>
<%@page import="com.bjsasc.plm.Helper"%>
<%@page import="com.bjsasc.plm.core.persist.model.Persistable"%>
<%@page import="com.bjsasc.ddm.distribute.model.distributeorderobjectlink.DistributeOrderObjectLink"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>



<%@page contentType="text/html; charset=UTF-8" pageEncoding="GBK"%>
<%@include file="/plm/plm.jsp" %>

<%
	// 数据转换服务
	String contextPath = request.getContextPath();
	String title = "创建回收发放单";
	String classId = "DistributeOrder";
	String oid = request.getParameter("oid");
	String firstOid= oid.split(",")[0];
	// 分发对象服务
	DistributeObjectService disObjService = DistributeHelper.getDistributeObjectService();
	List<DistributeObject> disObjList = disObjService.getDistributeObjectsByDistributeOrderOid(firstOid);
	
    String orderTypeName;
	DistributeObject disObject;
	String orderType;
	String number;
	String name;
	if (disObjList == null || disObjList.isEmpty()) {
		orderType = ConstUtil.C_ORDERTYPE_0;
		orderTypeName="发放单";
		// 数据转换服务
		DdmDataTransferService tranService = DdmDataTransferHelper.getDistributeObjectService();
		Persistable obj = Helper.getPersistService().getObject(oid);
		disObject = tranService.transferToDdmData(obj);
		number = disObject.getNumber() + ConstUtil.C_DISTRIBUTEORDER_0;
		name= disObject.getName() + ConstUtil.C_DISTRIBUTEORDER_0;
	} else {
		orderType = ConstUtil.C_ORDERTYPE_2;
		orderTypeName="回收发放单";
		disObject = disObjList.get(0);
		
		Persistable persit = Helper.getPersistService().getObject(Helper.getOid(disObject.getDataClassId(), disObject.getDataInnerId()));
		List<DistributeOrderObjectLink> linkList = new ArrayList<DistributeOrderObjectLink>();
		
		int seriaNo=0;
		//以下是为了获取补发单的下个个号
		if (persit instanceof Iterated) {
			Iterated it= (Iterated) persit;
			Mastered master= it.getMaster();
			List<Iterated> itList= Helper.getVersionService().allIterationsOf(master);
			//获取版本列表
			for (int i = 0; i < itList.size(); i++) {
				Iterated iter=itList.get(i);
				String version= iter.getIterationInfo().getFullVersionNo();
				//同一个版本下--不同发放单
				List<DistributeOrderObjectLink> returnList =disObjService.historyDistributeObjectList(Helper.getOid(iter.getClassId(), iter.getInnerId()),"2");
				seriaNo+=returnList.size();
			}
		}
		number = disObject.getNumber() + ConstUtil.C_DISTRIBUTEORDER_2;
		name= disObject.getName() + ConstUtil.C_DISTRIBUTEORDER_2;
		/** 发放单与分发数据Link服务 */
		DistributeOrderObjectLinkService linkService = DistributeHelper.getDistributeOrderObjectLinkService();
		number = number + "-" + String.valueOf(seriaNo + 1);
		name = name + "-" + String.valueOf(seriaNo + 1);
	}
	String note= disObject.getNote();
%>
<html>
	<head>
		<title><%=title%></title>
		<script type="text/javascript" src="<%=Url.PLMJS%>" charset="GBK"></script>
	</head>
<body class="openWinPage">
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
		<tr>
	    	<td class="AvidmTitleName">
				<div class="imgdiv"><img src="<%=contextPath%>/plm/images/project/createproject.gif"/></div>
				<div class="namediv"><%=title%></div>
			</td>
		</tr>
	</table>
	<form id="form_distributeorder" name="form_distributeorder" ui="form" action="" method="POST">
	    <input type="hidden" name="CLASSID" id="CLASSID" value="<%=classId%>"/>
	    <input type="hidden" name="orderOidsStr" id="oid" value="<%=oid%>"/>
		<table width="100%" border="0" cellspacing="0" cellpadding="0" class="avidmTable">
			<tr>
			    <td class="left_col AvidmW150" ><span>*</span>编号：</td>
			    <td>
					<input type='text' name='NUMBER' id='NUMBER' class='pt-text pt-validatebox AvidmW270' value="<%=number%>" <%=ValidateHelper.buildValidator()%>>
			    </td>
			</tr>
			<tr>
			    <td class="left_col AvidmW150" ><span>*</span>发放单名称：</td>
			    <td>
					<input type='text' name='NAME' id='NAME' class='pt-text pt-validatebox AvidmW270' value="<%=name%>" <%=ValidateHelper.buildValidator()%>>
			    </td>
			</tr>
			<TR>
				<TD class='left_col AvidmW150'>单据类型：</TD>
				<TD cols="1" rows="2">
				<!--  
					<SELECT id="orderType" name='orderType' class='pt-select AvidmW270'  url="<%=contextPath%>/plm/common/select/getSelect.jsp?select=orderType" >
					</SELECT>
					-->
					<input type='text' name='type' id='type' class='pt-text pt-validatebox AvidmW270' value="<%=orderTypeName%>" readonly = "readonly">
					<input type='hidden' name='orderType' id='orderType' class='pt-text pt-validatebox AvidmW270' value="<%=orderType%>">
				</TD>
			</TR>
			<tr>
			 	<td class="left_col AvidmW150">备注：</td>
				<td>
					<textarea name='NOTE' id='NOTE' class='pt-textarea' style="width:270px;height:50px;" <%=ValidateHelper.buildValidatorAndAssisant()%>></textarea>
				</td>
			</tr>
			<tr id="includeDynamicFields"></tr>
		</table>

		<table width="100%" height="40px" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td class="exegesis AvidmW150"><span>*</span> 为必选/必填项</td>
			    <td>
					<div class="pt-formbutton" text="确定"  onclick="saveSubmitForm();" <%=ValidateHelper.buildCheckAll()%>></div>
					<div class="pt-formbutton" text="取消"  onclick="cancleButton();"></div>
				</td>
			</tr>
		</table>
	</form>
</body>
</html>
<script type="text/javascript">
	
	//保存提交表单
	function saveSubmitForm() {
		<%=ValidateHelper.buildCheck()%>
		plm.startWait();
		var main_form = pt.ui.get("form_distributeorder");
		var options = {
				
				url:"<%=contextPath%>/ddm/distribute/recDesInfo!addRecDistributeOrder.action",
				dataType:"json",
				type:"POST",
				success:function(result) {
					plm.endWait();
					if(result.SUCCESS != null &&result.SUCCESS == "false"){
						plm.showAjaxError(result);
					}else if(result.SUCCESS == "message"){
						var mes=result.MESSAGE;
						plm.showMessage({title:'错误提示', message:mes, icon:'1'});
					}else if(result.FLAG == "false"){
						alert("您没有操作权限！");
						return;
					}else{
						setState();
						var tempUrl = '<%=contextPath%>/ddm/public/visitObject.jsp?<%=KeyS.CALLBACK%>=reload&<%=KeyS.OID%>=' + result.DISORDEROID;
						top.location.href = tempUrl;

						//plm.showMessage({title:'提示', message:"创建发放单操作成功!", icon:'2'});
						//top.opener.location.reload();
						plm.ddmReload();
					}
				},
				error:function(a){
					plm.endWait();
					plm.showMessage({title:'错误提示', message:"操作失败！", icon:'1'});
				}
			};
		main_form.submit(options);
	}
	
	function cancleButton(){
		window.close();
	}

	//关闭修改页面并调用父页面刷新方法
	function onAjaxExecuted(result){
		window.close();
	}

	function setState(){
		var num = document.getElementById("NUMBER");
		num.onblur=function(){};
		//$("#NUMBER").unbind();
	}
</script>
