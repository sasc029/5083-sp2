<%@page import="com.bjsasc.plm.core.context.model.Context"%>
<%@page import="com.bjsasc.plm.core.util.PlmException"%>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="GBK"%>
<%@page import="com.bjsasc.plm.core.persist.model.Persistable"%>
<%@page import="com.bjsasc.plm.context.ContextManager"%>
<%@page import="com.cascc.avidm.login.model.PersonModel"%>
<%@page import="com.bjsasc.plm.core.system.principal.User"%>
<%@page import="com.bjsasc.plm.core.system.principal.UserHelper"%>
<%@page import="com.bjsasc.plm.core.favorite.FavoriteHelper"%>
<%@page import="com.bjsasc.adm.active.model.activecontext.ActiveContext"%>
<%@page import="com.bjsasc.adm.active.helper.AdmHelper"%>
<%@page import="com.bjsasc.plm.navigator.NavigatorUtil"%>
<%@page import="com.bjsasc.plm.grid.data.GridDataUtil"%>
<%@page import="com.bjsasc.plm.url.Url"%>
<%@page import="com.bjsasc.plm.Helper"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@include file="/plm/plm.jsp" %>

<%
	String classId = "ActiveContext";
	String toolbarId = "context.active.list.toolbar";
	
	List<ActiveContext> list = AdmHelper.getActiveContextService().getAllActiveContext();

	List<Map<String,Object>> listMap = new ArrayList<Map<String,Object>>();
	for(ActiveContext context:list){
		if(context == null){
			continue;
		}
		
		Map<String, Object> map = Helper.getTypeManager().format(context);
		map.put("ISPRIVATE", (((ActiveContext)context).isPrivate()==true?"是":"否"));
		listMap.add(map);
	}
	
	String spot = "ListActiveContexts";
	GridDataUtil.prepareRowObjectMaps(listMap,  spot);
%>


<html>
<head>  
	<title>产品列表</title>
	<script type="text/javascript" src="<%=Url.PLMJS%>" charset="UTF-8"></script>
</head>
<script type="text/javascript">
	var container = {OID:'<%=classId%>'};
	//移除收藏对象
	plm.deleteFavoriteObjectSimple=function(records,callback,container){
		var params = {
				type : "POST",
				url : contextPath + "/plm/common/favorite/DeleteFavoriteObjectHandle.jsp",
				data : {"DATA" : pt.ui.JSON.encode(records)},
				success : function(o) {
					plm.endWait(); 
					if(o.SUCCESS == "false"){
			    		plm.showAjaxError(o);
			    		return;
				    }
					if(callback!=null){ 
						eval(callback+'(o)');
					}
				},
				failure : function() {
					plm.endWait();
					plm.alertMsg("移除失败,请重新操作!");
				}
			};
			UI.ajax(params);
	};
</script>
<body leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" scroll="no"  >		 
	<form id="mainForm" name="mainForm" method="POST" action="listActiveContexts.jsp">
		<table height="100%" width="100%" cellSpacing="0" cellPadding="0" border="0">
			<jsp:include page="/plm/common/grid/control/grid_with_toolbar.jsp">
				<jsp:param name="spot" value="<%=spot%>"/>
				<jsp:param name="gridTitle" value="现行文件上下文"/>
				<jsp:param name="toolbar_modelId" value="<%=toolbarId%>"/>
				<jsp:param name="operate_container" value="container"/>
			</jsp:include>
		</table>
	</form>
</body>
<script type="text/javascript">
function reload(){
	pt.ui.get("mainForm").submit(); 
	parent.frames['leftFrame'].refreshTree();
}
</script>
</html>