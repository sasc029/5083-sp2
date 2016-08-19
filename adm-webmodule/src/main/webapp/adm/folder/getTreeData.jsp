<%@page import="com.bjsasc.plm.core.system.access.operate.Operate"%>
<%@page import="com.bjsasc.plm.core.system.access.AccessControlHelper"%>
<%@page import="com.bjsasc.plm.core.system.principal.User"%>
<%@page import="com.bjsasc.plm.core.session.SessionHelper"%>
<%@page import="com.bjsasc.plm.Helper"%>
<%@page import="com.bjsasc.plm.folder.FolderManagerHelper"%>
<%@page import="com.bjsasc.plm.folder.FolderSelector"%>
<%@page import="com.bjsasc.plm.core.context.model.Context"%>
<%@page import="com.bjsasc.plm.core.context.ContextHelper"%>
<%@ page import="com.bjsasc.plm.type.type.Type"%>
<%@page import="com.bjsasc.plm.KeyS"%>
<%@page import="com.bjsasc.ui.json.DataUtil"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="GBK"%>
<%@ page language="java"%>
<%@ page session="true"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="com.bjsasc.plm.core.folder.*"%>
<%@include file="/plm/plm.jsp"%>
<%
	//清除缓存
	response.setHeader("Pragma", "No-cache");
	response.setHeader("Cache-Control", "no-cache");
	response.setDateHeader("Expires", 0);
	// 页面使用UTF-8编码
	request.setCharacterEncoding("UTF-8");
	String treeType = request.getParameter(FolderSelector.FOLDER_TREE_ID);
	String foleriid = request.getParameter(FolderSelector.FOLDER_IID);
	Folder folder = null;
	String classid="";
	String folderiid = request.getParameter("folderiid");
	if(treeType!=null&&treeType.equals(FolderSelector.FOLDERTREE_TYPE_WHOLE)){
		//获取上下文标识		
		//获取该上下文对应得顶级文件夹
		//如果没有传递上下文标识，则取有权限的所有上下文
	if(folderiid.equals("")){
		User user = SessionHelper.getService().getUser();
		List<Context> allconlist = ContextHelper.getService().getAllProducts();
		allconlist.addAll( ContextHelper.getService().getAllLibraryContexts());
		List<Context> conlist = ContextHelper.getService().filterContextsByUserPrincipal(allconlist, user);
		List lists = new ArrayList();
	for(Context context:conlist){
		if(!AccessControlHelper.getService().canAccess(context)){				
			continue;
		}
		Cabinet cabinet = FolderManagerHelper.getService().getPubCabinet(context.getInnerId());
		folder = (Folder)cabinet;
		
		String folderName = cabinet.getContextInfo().getContext().getName();
		
		Map map = new HashMap();
		map.put(KeyS.INNERID, folder.getInnerId());
		map.put(KeyS.CLASSID,folder.getClassId());
		map.put(KeyS.FOLDER, folderName);
		map.put(KeyS.OID, Helper.getOid(folder));
		map.put("__viewicon", true);
		map.put("expanded", false);
		map.put("loadType","mainFrame");
		map.put("iconsrc", request.getContextPath()+ "/plm/images/folder/cabinet.gif");
		map.put("ChildUrl",request.getContextPath()+"/adm/folder/getTreeChildren.jsp?id=" + folder.getInnerId());
		lists.add(map);
	}
	String xx = DataUtil.encode(lists);
	out.print(xx);
	return;
		}else{
	Cabinet cabinet = ((Context)Helper.getPersistService().getObject(folderiid)).getCabinetDefault();
	folder = (Folder)cabinet;
	classid = "Cabinet";
		}
	}else{
		//获取文件夹标识	
		SubFolder subfolder = (SubFolder)FolderHelper.getService().getFolder("SubFolder", folderiid);
		folder = (Folder)subfolder;
		classid = "SubFolder";
	}
	
	if(folder!=null){
		String folderName = folder.getName();
		if(folder instanceof Cabinet){
	Cabinet cabinet = (Cabinet)folder;
	folderName = cabinet.getContextInfo().getContext().getName();
		}
		
		List lists = new ArrayList();
		Map map = new HashMap();
		map.put(KeyS.INNERID, folder.getInnerId());
		map.put(KeyS.CLASSID,classid);
		map.put(KeyS.OID, Helper.getPersistService().getOid(folder));
		map.put(KeyS.FOLDER, folderName);
		map.put("__viewicon", true);
		map.put("expanded", false);
		map.put("loadType","mainFrame");
		Type targetType = Helper.getTypeManager().getType(folder.getClassId());
		String iconUrl = targetType.getIcon();
		// map.put("iconsrc", request.getContextPath()+ "/platform/images/app/application_form.png");
		map.put("iconsrc", request.getContextPath()+ iconUrl);
		map.put("ChildUrl",request.getContextPath()+"/adm/folder/getTreeChildren.jsp?id=" + folder.getInnerId());
		lists.add(map);
	
		String xx = DataUtil.encode(lists);
		out.print(xx);
	}
%>