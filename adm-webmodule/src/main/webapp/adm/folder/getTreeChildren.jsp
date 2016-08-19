<%@page import="java.util.Comparator"%>
<%@page import="java.util.Collections"%>
<%@page import="com.bjsasc.plm.core.system.access.AccessControlHelper"%>
<%@page import="com.bjsasc.plm.Helper"%>
<%@page import="com.bjsasc.plm.KeyS"%>
<%@page import="com.bjsasc.ui.json.DataUtil"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="GBK"%>
<%@ page language="java"%>
<%@ page session="true"%>

<%@ page import="com.bjsasc.plm.type.type.Type"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="com.bjsasc.plm.core.folder.*" %>
<%@ page import="com.bjsasc.plm.folder.*" %>
<%@include file="/plm/plm.jsp" %>
<%
	//清除缓存
	response.setHeader("Pragma", "No-cache");
	response.setHeader("Cache-Control", "no-cache");
	response.setDateHeader("Expires", 0);
	// 页面使用UTF-8编码
	request.setCharacterEncoding("UTF-8");
	String folderiid = request.getParameter("id");
	
	List<FolderLink> list = FolderManagerHelper.getService().getFolderChildren(folderiid);
	//构造树
	List lists = new ArrayList();
	for(FolderLink link : list){
		try{
			SubFolder subfolder = (SubFolder)link.getChildFolder();
			if(!AccessControlHelper.getService().canAccess(subfolder)){
				continue;
			}
			Map map = new HashMap();
			map.put(KeyS.INNERID, subfolder.getInnerId());
			map.put(KeyS.CLASSID,subfolder.getClassId());
			map.put(KeyS.FOLDER, subfolder.getName());
			map.put(KeyS.CREATE_TIME, subfolder.getManageInfo().getCreateTime());
			map.put(KeyS.OID, Helper.getOid(subfolder));
			map.put("__viewicon", true);
			List<Folder> subsublist = Helper.getFolderService().getChildFolders(subfolder);
			if(subsublist.size()>0){
				map.put("expanded", false);
			}else{
				map.put("expanded", true);
			}
			map.put("loadType","mainFrame");
			Type targetType = Helper.getTypeManager().getType(subfolder.getClassId());
			String iconUrl = targetType.getIcon();
			
			// map.put("iconsrc", request.getContextPath()+ "//platform/images/folder/folder.gif");
			map.put("iconsrc", request.getContextPath()+ iconUrl);
			map.put("ChildUrl",request.getContextPath()+"/adm/folder/getTreeChildren.jsp?id=" + subfolder.getInnerId());
			lists.add(map);
		}catch(Exception e){
			
		}
	}
	
	//对获取到的文件夹按创建时间进行排序
	Collections.sort(lists, new Comparator<Map>(){
		public int compare(Map map1,Map map2){
			long time1 = (Long)map1.get(KeyS.CREATE_TIME);
			long time2 = (Long)map2.get(KeyS.CREATE_TIME);
			
			if(time1 == time2){
				return 0;
			}else if(time1 < time2){
				return 1;
			}else{
				return -1;
			}
		}

	});
	
	String xx=DataUtil.encode(lists);
	out.print(xx);
%>