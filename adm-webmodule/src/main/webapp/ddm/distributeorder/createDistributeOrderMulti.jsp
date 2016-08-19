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
<%@page import="com.bjsasc.plm.core.context.rule.RuleHelper"%>
<%@page import="com.bjsasc.plm.core.context.rule.TypeBasedRule"%>
<%@page import="com.bjsasc.plm.core.context.model.Context"%>
<%@page import="com.bjsasc.plm.core.context.model.Contexted"%>
<%@page import="com.bjsasc.plm.core.context.model.ContextInfo"%>
	
<%@page import="com.bjsasc.ddm.distribute.helper.DistributeHelper"%>
<%@page import="com.bjsasc.ddm.distribute.model.distributeobject.DistributeObject"%>
<%@page import="com.bjsasc.ddm.distribute.service.distributeobject.DistributeObjectService"%>
<%@page import="com.bjsasc.ddm.transfer.service.DdmDataTransferService"%>
<%@page import="com.bjsasc.ddm.transfer.helper.DdmDataTransferHelper"%>
<%@page import="com.bjsasc.ddm.distribute.service.distributeorderobjectlink.DistributeOrderObjectLinkService"%>
<%@page import="com.cascc.avidm.util.SplitString"%>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="GBK"%>
<%@include file="/plm/plm.jsp" %>
<%@include file="/plm/code/numberinput/NumberInput_include.jsp"%>

<%
	// 数据转换服务
	String contextPath = request.getContextPath();
	String title = "创建发放单";
	String classId = "DistributeOrder";
	String oids = request.getParameter("oids");
	List<String> oidList = SplitString.string2List(oids, ",");
	DistributeObjectService disObjService = DistributeHelper.getDistributeObjectService();
    String orderTypeName= "";
	DistributeObject disObject;
	String disObjVersion;
	String orderType= "";
	String number = "";
	String name= "";
	//String note = "";
	//List<DistributeObject> list = new ArrayList<DistributeObject>();
	//disObjList中只有一个对象
	String oidForOrder = "";
	
	for (String oid : oidList) {
		List<DistributeObject> disObjList = disObjService.getDistributeObjectsByDataOid(oid);
		if (disObjList == null || disObjList.isEmpty()) {
			if("".equals(oidForOrder)){
				orderType = ConstUtil.C_ORDERTYPE_0;
				orderTypeName="发放单";
				oidForOrder = oid;
			}
			//note= disObject.getNote();
			continue;
		} else {
			orderType = ConstUtil.C_ORDERTYPE_1;
			orderTypeName="补发发放单";
			disObject = disObjList.get(0);
			disObjVersion=disObjService.getCurrentDisObjectVersion(oid);
			
			number = disObject.getNumber() + ConstUtil.C_DISTRIBUTEORDER_1+disObjVersion;
			name= disObject.getName() + ConstUtil.C_DISTRIBUTEORDER_1+disObjVersion;
			/** 发放单与分发数据Link服务 */
			DistributeOrderObjectLinkService linkService = DistributeHelper.getDistributeOrderObjectLinkService();
			int seriaNo = linkService.getDistributeOrderObjectLinkListByDistributeObjectOid(Helper.getOid(disObject))
					.size();
			number = number + "-" + String.valueOf(seriaNo);
			name = name + "-" + String.valueOf(seriaNo);
			//note= disObject.getNote();
			break;
		}
	}
	//如果不存在补发的数据的情况
	if(ConstUtil.C_ORDERTYPE_0.equals(orderType)){
		// 数据转换服务
		DdmDataTransferService tranService = DdmDataTransferHelper.getDistributeObjectService();
		Persistable obj = Helper.getPersistService().getObject(oidForOrder);
		disObject = tranService.transferToDdmData(obj);
		disObjVersion=disObjService.getCurrentDisObjectVersion(oidForOrder);	
		number = disObject.getNumber() + ConstUtil.C_DISTRIBUTEORDER_0+disObjVersion;
		name= disObject.getName() + ConstUtil.C_DISTRIBUTEORDER_0+disObjVersion;
	}
	
		
	//根据初始化规则获取编码的获取方式	,如果初始化规则禁用，则默认为输入。
	String oid = oidList.get(0);
	String editType = "input";
	String contextOid = "";
	Persistable obj = Helper.getPersistService().getObject(oid);
	if (obj instanceof Contexted) {
		ContextInfo contextInfo = ((Contexted)obj).getContextInfo();
		Context context = contextInfo.getContext();
		contextOid = context.getOid();
		TypeBasedRule rule = RuleHelper.getService().findRule("DistributeOrder", context);
		if (null != rule) {
			editType = rule.getRuleItemArg("number", "editType");
		}
	}
	//如果是  "select"自动申请编码；"mix"自动申请编码，并可以手工修改；的时候，
	//不自动生成 number,需要把前边编辑好的number重新设置成空
	if("select".equals(editType) || "mix".equals(editType)){
		number = "";
	}
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
	    <input type="hidden" name="oids" id="oids" value="<%=oids%>"/>
		<table width="100%" border="0" cellspacing="0" cellpadding="0" class="avidmTable">
			<tr>
			    <td class="left_col AvidmW150" ><span>*</span>编号：</td>
			    <td>
    	<jsp:include page="/plm/code/numberinput/NumberInput.jsp">
			<jsp:param name="CLASSID" value="DistributeOrder"/>
			<jsp:param name="CONTEXT_OID" value="<%=contextOid %>"/>
		</jsp:include>	
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
	var createOrderFlag = "false";// 用于发放单是否创建成功的标示，用来确定是否编码回收。//关闭窗体时回收编码
	window.onbeforeunload = function(){
		codeRecycle();
		//window.close();
	};
	window.onload = function(){
		codeRecycle();
	};
	
	// 编号设置默认值。
	$(document).ready(function(){
		if ($("#NUMBER").val() == "") {
			$("#NUMBER").val("<%=number%>");
		}
	});
	
	//保存提交表单
	function saveSubmitForm() {
		<%=ValidateHelper.buildCheck()%>
		plm.startWait();
		var main_form = pt.ui.get("form_distributeorder");
		var options = {
				
				url:"<%=contextPath%>/ddm/distribute/distributeOrderHandle!createDistributeOrderMulti.action",
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
						createOrderFlag = "true";//用来标示发放单创建成功
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
	};
	
	function cancleButton(){
		codeRecycle();
		window.close();
	};

	//关闭修改页面并调用父页面刷新方法
	function onAjaxExecuted(result){
		window.close();
	};

	function setState(){
		var num = document.getElementById("NUMBER");
		num.onblur=function(){};
		//$("#NUMBER").unbind();
	};
	
	//申请编码
	function applyCode(){
		//点击后让编号后面选项提示消失
		var num = document.getElementById("NUMBER");
		plm.hideTip(num);
		var number = $("#NUMBER").val();
		var classId = "DistributeOrder";
		//暂时获取默认的上下文oid
		var contextOid = "<%=contextOid %>";
		var applyType = "<%=editType%>"; 
		var data ={
				CLASSID : classId,
				CONTEXT_OID :contextOid,
				NUMBER : number
		};
		var callback = "getCode";
		plm.getNumber(data,callback);
	};
	//获取编码
	function  getCode(value){
		var number = value;
		if(number == undefined){
			number = "";
		}
		document.getElementById("NUMBER").value = number ;
	};	
	//编码回收
	function codeRecycle(){
		var code = document.getElementById("NUMBER").value;
		if(code != null && code != "" && createOrderFlag != "true"){
			plm.codeRecycle(code);
		}
	};
</script>
