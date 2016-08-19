<%@page import="java.net.URLDecoder"%>
<%@page import="com.bjsasc.plm.Helper"%>
<%@page import="com.bjsasc.plm.core.persist.model.Persistable"%>
<%@page import="com.bjsasc.plm.core.session.SessionHelper"%>
<%@page import="com.bjsasc.ddm.common.ConstUtil"%>
<%@page import="com.bjsasc.plm.core.system.principal.User"%>
<%@page import="com.bjsasc.plm.core.system.principal.UserHelper"%>
<%@page import="com.bjsasc.ddm.distribute.model.distributeworkload.DistributeWorkload"%>
<%@page import="com.bjsasc.ddm.distribute.model.distributeobject.DistributeObject"%>
<%@page import="com.cascc.avidm.login.model.PersonModel"%>
<%@page import="com.bjsasc.plm.core.domain.Domained"%>
<%@page import="com.bjsasc.plm.core.persist.PersistHelper"%>
<%@page import="com.bjsasc.plm.core.system.access.AccessControlHelper"%>
<%@page import="com.bjsasc.plm.core.system.access.operate.Operate"%>
<%@page import="com.bjsasc.ddm.distribute.service.distributeelectask.DistributeElecTaskService"%>
<%@page import="com.bjsasc.ddm.distribute.helper.DistributeHelper"%>
<%@page import="com.cascc.avidm.util.SplitString"%>
<%@page import="com.bjsasc.plm.core.change.ECO"%>
<%@page import="com.bjsasc.plm.core.change.TNO"%>
<%@page import="com.bjsasc.ddm.distribute.model.distributeorder.DistributeOrder"%>
<%@page import="com.bjsasc.ddm.distribute.model.distributeobject.DistributeObject"%>
<%@page import="com.bjsasc.ddm.distribute.helper.DistributeHelper"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.bjsasc.plm.core.change.Changed"%>
<%@page import="com.bjsasc.plm.core.change.Waived"%>
<%@page import="java.net.URLEncoder"%>

<%@page contentType="text/html; charset=UTF-8" pageEncoding="GBK"%>
<%
	String path = request.getContextPath();
	String oid = request.getParameter("OID");
	String orderOid = request.getParameter("disOrderOid");
	String classId = Helper.getClassId(oid);
	String outUserIID = request.getParameter("outUserIID");
	
	String url = "";
	if("DistributeWorkload".equals(classId)){
		Persistable obj = Helper.getPersistService().getObject(oid);
		DistributeWorkload dis = (DistributeWorkload) obj;
		classId = dis.getObjectClassId();
		oid = dis.getObjectClassId()+":"+dis.getObjectId();
	}
	if ("DistributeOrder".equals(classId)) {
		session.setAttribute(ConstUtil.DISTRIBUTE_ORDER_OID, oid);
		url=path+"/plm/common/visit.jsp?OID="+oid;	
	} else if ("DistributePaperTask".equals(classId)) {
		url = path + "/plm/common/visit.jsp?OID=" +oid;
	    session.setAttribute(ConstUtil.SPOT_DISTRIBUTEPAPERTASK_OID, oid);
	} else if ("DistributeElecTask".equals(classId)) {
	    url = path + "/plm/common/visit.jsp?OID=" +oid;
	    session.setAttribute(ConstUtil.DISTRIBUTE_ELECTASK_OID, oid);
	} else if ("DistributePaperSignTask".equals(classId)) {
	    url = path + "/plm/common/visit.jsp?OID=" +oid;
	    session.setAttribute(ConstUtil.DISTRIBUTE_PAPERSIGNTASK_OID, oid);
	} else if ("RecDesPaperTask".equals(classId)){
		url = path + "/plm/common/visit.jsp?OID=" +oid;
	    session.setAttribute(ConstUtil.SPOT_RECDESPAPERTASK_OID, oid);
	}else if("DistributeObject".equals(classId)){
		Persistable obj = Helper.getPersistService().getObject(oid);
		DistributeObject dis = (DistributeObject) obj;
		oid = dis.getDataClassId() + ":" + dis.getDataInnerId();
		
		Persistable dataObj = Helper.getPersistService().getObject(dis.getDataClassId() + ":" + dis.getDataInnerId());
		if (dataObj != null){
			url = path + "/plm/common/visit/VisitObject.jsp?OID=" + oid;
			if(!"".equals(outUserIID) && outUserIID != null){
				//授权
				User user = UserHelper.getService().getUser(outUserIID);
				Domained domained = (Domained) PersistHelper.getService().getObject(
						dis.getDataClassId() + ":" + dis.getDataInnerId());
				boolean objFlag = AccessControlHelper.getService().hasEntityPermission(user, Operate.ACCESS, domained);
				if(objFlag == false){
					DistributeHelper.getDistributePrincipalService().setPriviledge(domained, user);
				}
				if(dataObj instanceof ECO || dataObj instanceof TNO){
					//取得分发数据相关发放单的所有分发数据
					DistributeOrder order = (DistributeOrder)PersistHelper.getService().getObject(orderOid);
					List<DistributeObject> objList = DistributeHelper.getDistributeObjectService().getDistributeObjectList(order);
					//取得改后对象并授权
					List<Map<String,Object>> afterList = new ArrayList<Map<String,Object>>();
					if(dataObj instanceof ECO){
						afterList = Helper.getChangeService().getChangedAfterList((ECO)dataObj);
						for(Map<String,Object> tempMap : afterList){
							Changed changeAfter =(Changed) tempMap.get("CHANGEDAFTER");
							for(DistributeObject disObj : objList){
								Persistable dataObject = Helper.getPersistService().getObject(disObj.getDataClassId() + ":" + disObj.getDataInnerId());
								if((dataObject.getInnerId()).equals(changeAfter.getInnerId())){
									Domained dataDomain = (Domained) PersistHelper.getService().getObject(
											disObj.getDataClassId() + ":" + disObj.getDataInnerId());
									boolean changeAccessFlag = AccessControlHelper.getService().hasEntityPermission(user, Operate.ACCESS, dataDomain);
									if(changeAccessFlag == false){
										DistributeHelper.getDistributePrincipalService().setPriviledge(dataDomain, user);
									}
								}
							}
						}
					} else if(dataObj instanceof TNO){
						afterList = Helper.getChangeService().getWaivedAfterList((TNO)dataObj);
						for(Map<String,Object> tempMap : afterList){
							Waived waivedAfter =(Waived) tempMap.get("WAIVEDAFTER");
							for(DistributeObject disObject : objList){
								Persistable disDataObj = Helper.getPersistService().getObject(disObject.getDataClassId() + ":" + disObject.getDataInnerId());
								if((disDataObj.getInnerId()).equals(waivedAfter.getInnerId())){
									Domained domain = (Domained) PersistHelper.getService().getObject(
											disObject.getDataClassId() + ":" + disObject.getDataInnerId());
									boolean waivedAccessFlag = AccessControlHelper.getService().hasEntityPermission(user, Operate.ACCESS, domain);
									if(waivedAccessFlag == false){
										DistributeHelper.getDistributePrincipalService().setPriviledge(domain, user);
									}
								}
							}
						}
					}
					
				}
			}
		} else {
			//A5跨域的url处理
			if("A5".equals(dis.getDataFrom())){
				PersonModel person = SessionHelper.getService().getPersonModel();
				if(person != null){
					String userIID = person.getUserIID();
					String accessUrl = URLDecoder.decode(dis.getAccessUrl());
					url = accessUrl.replace("{userIID}", userIID);
					List<String> urlList = SplitString.string2List(url, "TargetURL=");
					url = urlList.get(0) + "TargetURL=" + URLEncoder.encode(urlList.get(1), "UTF-8");
				}
			////A3.5跨版本的url处理
			}else{
				PersonModel person = SessionHelper.getService().getPersonModel();
				String outusername = person.getUserIID();
				String outuserrealname = person.getUserName();
				String realName = URLEncoder.encode(outuserrealname,"UTF-8");

				//替换用户相关信息
				url = dis.getAccessUrl();
				url = url.replace("{outusername}", outusername);
				url = url.replace("{outuserrealname}", outuserrealname);
				url = url.replace("{realName}", realName);
			}
		}		
	}
	if (url == null || url.length() == 0) {
		url = path + "/plm/common/visit/VisitObject.jsp?OID=" + oid;
	} 
	
	response.sendRedirect(url);
%>