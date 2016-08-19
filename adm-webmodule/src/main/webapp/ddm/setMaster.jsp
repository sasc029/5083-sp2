<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
 <%@page import="com.bjsasc.ddm.distribute.model.distributeobject.DistributeObject"%>
 <%@page import="com.bjsasc.ddm.distribute.model.distributeorder.DistributeOrder"%>
 <%@page import="com.bjsasc.plm.Helper"%>
 <%@page import="com.bjsasc.ddm.distribute.model.distributeorderobjectlink.DistributeOrderObjectLink"%>
 <%@page import="com.bjsasc.plm.core.persist.PersistHelper"%>
 <%@page import="java.util.List"%>
 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>

<%
		String sql = " SELECT  * FROM DDM_DIS_ORDER ";
		List<DistributeOrder> list = Helper.getPersistService().query(sql, DistributeOrder.class);
		for(DistributeOrder disorder:list){
		String distributeOrderOid=disorder.getOid();
		String sql1 = "select * FROM DDM_DIS_ORDEROBJLINK  where FROMOBJECTID=? and FROMOBJECTCLASSID=? and ISMASTER='1'";
		String innerId = Helper.getInnerId(distributeOrderOid);
		String classId = Helper.getClassId(distributeOrderOid);
		List<DistributeOrderObjectLink> links = PersistHelper.getService().query(sql1,DistributeOrderObjectLink.class,innerId, classId);
			if(links!=null){
			DistributeObject distributeObject = (DistributeObject) links.get(0).getTo();
			disorder.setMasterDataClassID(distributeObject.getDataClassId());
			PersistHelper.getService().update(disorder);
		}else{
			out.println(disorder.getOid()+"中分发数据没有主对象");
		}
		}
		
		 %>
</body>
</html>