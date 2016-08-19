<%@page import="com.bjsasc.plm.core.vc.model.Iterated"%>
<%@page import="com.bjsasc.ddm.distribute.helper.DistributeHelper"%>
<%@page import="com.bjsasc.ddm.distribute.service.distributeobject.DistributeObjectService"%>
<%@page import="com.bjsasc.plm.grid.GridHelper"%>
<%@page import="com.bjsasc.plm.Helper"%>
<%@page import="com.bjsasc.plm.util.SortUtil"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.bjsasc.plm.approve.approveorder.ApproveOrderManager"%>
<%@page import="com.bjsasc.plm.core.doc.Document"%>
<%@page import="com.bjsasc.plm.core.vc.VersionControlHelper"%>
<%@page import="com.bjsasc.plm.core.part.PartMaster"%>
<%@page import="com.bjsasc.plm.core.part.link.PartUsageLink"%>
<%@page import="com.bjsasc.plm.core.part.PartHelper"%>
<%@page import="com.bjsasc.plm.core.persist.PersistHelper"%>
<%@page import="com.bjsasc.plm.core.persist.model.Persistable"%>
<%@page import="com.bjsasc.plm.core.part.Part"%>
<%@page import="java.util.ArrayList"%>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="GBK"%>
<%@page import="com.bjsasc.plm.KeyS"%>
<%@page import="com.bjsasc.plm.core.identifier.UniqueIdentified"%>
<%@page import="com.bjsasc.ui.json.DataUtil"%>
<%@page import="java.util.Vector"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%@page import="com.bjsasc.plm.core.vc.model.ControlBranch"%>
<%@page errorPage="/plm/ajaxError.jsp"%>
<%
    //清除缓存
	response.setHeader("Pragma", "No-cache");
	response.setHeader("Cache-Control", "no-cache");
	response.setDateHeader("Expires", 0);
	// 页面使用UTF-8编码
	request.setCharacterEncoding("UTF-8");
	String contextPath = request.getContextPath();
	String op = request.getParameter("op");
	String taskoid=request.getParameter("oid");
	DistributeObjectService service = DistributeHelper.getDistributeObjectService();
	if ("getRefObjectTop".equals(op)) {
		List<Map<String, Object>> ObjectMapList = new ArrayList<Map<String, Object>>();
			List<Persistable>  listRoot= service.getAllElecTaskDataRootObject(taskoid);
			for (Persistable root : listRoot) {
				Map<String, Object> targetMap = null;
				targetMap = Helper.getTypeManager().getAttrValues(root);
				if(root instanceof Part){
					targetMap.put("__viewicon", "true"); 
					targetMap.put("ChildUrl", new StringBuffer(request.getContextPath())
						.append("/ddm/distributeobject/listDistributeDataObjects_get.jsp?op=getRefObjectSon")
						.append("&parentOid=").append(Helper.getOid((Persistable)root))
						  .append("&taskOid=").append(taskoid).toString());
				}
				
				targetMap.put("expanded", false);
				ObjectMapList.add(targetMap);
			}
		String json = DataUtil.listToJson(ObjectMapList);
		out.print(json);
	}
     else if("getRefObjectSon".equals(op)){
        String parentOid = request.getParameter("parentOid");
        String taskOid = request.getParameter("taskOid");
        List<Persistable> listobject=service.getAllElecTaskDataObject(taskOid);
    	Map<String, String> map = new HashMap<String, String>();
    	int i=0;
		for(Persistable object : listobject){
			map.put(Helper.getOid(object), ""+i++);
		}
 	 	List<Map<String, Object>> listMap = new ArrayList<Map<String, Object>>();
 		Persistable top = PersistHelper.getService().getObject(parentOid);
 		
 		if(top instanceof Part){
 			List<Persistable> ptList = new ArrayList<Persistable>();
 			List<PartUsageLink> lists = PartHelper.getService().getPartUsageLinkByFrom((Part)top);
 			if(lists != null && lists.size() > 0){
 				for(PartUsageLink link : lists){
 					Object obj2 = link.getToObject();
 					if(obj2 instanceof PartMaster){
 						//Part part = (Part) VersionControlHelper.getService().allIterationsOf((PartMaster)obj2).get(0);
 						List<Iterated> listpart=VersionControlHelper.getService().allIterationsOf((PartMaster)obj2);
 						for(int j=0;j<listpart.size();j++){
 							Part part=(Part)listpart.get(j);
 							if(map.containsKey(Helper.getOid(part))){
 								ptList.add(part);
 							}
 							
 						}
 					
 					
 					}
 				}
 			}
 			List<Document> docList = service.getLastestPartLinkDocument((Part)top, true);
 			docList.addAll(service.getLastestPartLinkDocument((Part)top, false));
 			if(docList != null && docList.size() > 0){
 				for(Document doc : docList){
 					List<Iterated> listdoc=VersionControlHelper.getService().iterationsOf((Iterated)doc);
 					for(int j=0;j<listdoc.size();j++){
					Document doc1=(Document)listdoc.get(j);
 					if(map.containsKey(Helper.getOid(doc1))){
							ptList.add(doc);
						}
 					}
 					}
 				}	
 			if(ptList.size() > 0){
 				Map<String, Object> attrMap = null;
 				for(Persistable pt : ptList){
 					String objOid = Helper.getOid(pt);
 					attrMap = Helper.getTypeManager().getAttrValues(pt);
 					if(pt instanceof Part){
 						attrMap.put("__viewicon", "true");
 						attrMap.put("ChildUrl", new StringBuffer(request.getContextPath())
 						.append("/ddm/distributeobject/listDistributeDataObjects_get.jsp?op=getRefObjectSon")
						.append("&parentOid=").append(Helper.getOid((Persistable)pt))
						.append("&taskOid=").append(taskOid).toString());
 					}
 					attrMap.put("expanded", false);
 					listMap.add(attrMap);
 				}
 			}
     }
 			out.println(DataUtil.listToJson(listMap));
 		}
%>