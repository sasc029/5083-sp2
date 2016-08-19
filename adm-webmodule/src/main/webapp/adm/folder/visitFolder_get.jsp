<%@ page import="java.util.*"%>
<%@page import="com.bjsasc.adm.common.FolderTreeUtil"%>
<%@page import="com.cascc.avidm.util.AvidmConstDefine"%>
<%@page import="com.cascc.avidm.login.model.PersonModel"%>
<%@page import="com.bjsasc.plm.KeyS"%>
<%@page import="com.bjsasc.plm.Helper"%>
<%@page import="com.bjsasc.plm.ui.tree.TreeNode"%>
<%@page import="com.bjsasc.plm.core.util.ListUtil"%>
<%@page import="com.bjsasc.plm.core.folder.Folder" %>
<%@page import="com.bjsasc.plm.core.context.model.Context"%>
<%@page import="com.bjsasc.plm.core.context.model.Contexted"%>
<%@page import="com.bjsasc.ui.json.DataUtil"%>

<%@page language="java"%>
<%@page session="true"%>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="GBK"%>
<%@include file="/plm/plm.jsp" %>
<%
	String op = request.getParameter("op");
	if(op == null){
		op = "getCabinet";
	}
	PersonModel person = (PersonModel) session.getAttribute(AvidmConstDefine.SESSIONPARA_USERINFO);	
	String oid = request.getParameter(KeyS.OID);

	Object asRootOid = session.getAttribute("asRootOid");
	if (asRootOid != null) {
		if(op == null || op.equals("getCabinet")){
			op = "getRootFolder";
		}
	}
	
	Folder folder = (Folder)Helper.getPersistService().getObject(oid);
	
	List nodes = new ArrayList();
	if(op.equals("getCabinet")){
		Map map = FolderTreeUtil.buildFolder(folder);
		if(map != null){
			nodes.add(map);
		}
	} else if(op.equals("getRootFolder")){
		Map map2 = FolderTreeUtil.buildSubFolder(folder, String.valueOf(asRootOid));
		if(map2 != null){
			nodes.add(map2);
		} else {
			session.removeAttribute("asRootOid");
			Map map3 = FolderTreeUtil.buildFolder(folder);
			if(map3 != null){
				nodes.add(map3);
			}
		}
	} else if(op.equals("getSubFolders")){
		Contexted contexted = (Contexted)folder;
		
		Context context = contexted.getContextInfo().getContext();
		
		List<Folder> subFolders = Helper.getFolderService().getChildFolders(folder);
		if(subFolders.size() > 0){
			boolean isFolderDeniedVisible = FolderTreeUtil.isFolderDeniedVisible(context); //首选项设置无权限的文件夹是否显示
			Map<String, Boolean> canAccessMap = FolderTreeUtil.canAccess(context, ListUtil.format(subFolders, Object.class)); //批量校验权限
			Map<TreeNode, Folder> map1 = new HashMap<TreeNode, Folder>();
			Map<Folder, TreeNode> map2 = new HashMap<Folder, TreeNode>();
			for(Folder subFolder:subFolders){
				TreeNode treeNode = FolderTreeUtil.buildTreeNode(subFolder, map1, map2);
				
				boolean canAccess = canAccessMap.get(Helper.getOid(subFolder));
				if(!canAccess){
					if(isFolderDeniedVisible){
						treeNode.setNodeName("***");
					}else{
						continue;
					}
				}

				Map node = treeNode.toMap();
				
				node.put(TreeNode.KEY_LINK, "/plm/folder/folderlist.jsp?OID="+Helper.getOid(subFolder));
				List<Folder> subsublist = Helper.getFolderService().getChildFolders(subFolder);
				if(subsublist.size()>0){
					node.put(TreeNode.KEY_CHILDURL, request.getContextPath() + "/adm/folder/visitFolder_get.jsp?op=getSubFolders&OID="+Helper.getOid(subFolder));
					node.put(TreeNode.KEY_EXPANDED, false);
				}else{
					node.put(TreeNode.KEY_VIEWICON, false);
					node.put(TreeNode.KEY_EXPANDED, true);
				}
				nodes.add(node);
			}
		}
	}
	
	out.print(DataUtil.encode(nodes));
%>