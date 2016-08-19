<%@page contentType="text/html; charset=UTF-8" pageEncoding="GBK"%>
<%@page language="java"%>
<%@page session="true"%>

<%@page import="java.net.URLDecoder"%>
<%@page import="java.util.*"%>

<%@page import="com.bjsasc.ui.json.DataUtil"%>
<%@page import="com.bjsasc.plm.KeyS"%>
<%@page import="com.bjsasc.plm.Helper"%>
<%@page import="com.bjsasc.plm.core.folder.Folder"%>
<%@page import="com.bjsasc.plm.core.folder.Foldered"%>
<%@page import="com.bjsasc.plm.core.folder.SubFolder"%>
<%@page import="com.bjsasc.plm.core.folder.FolderInfo"%>
<%@page import="com.bjsasc.plm.core.persist.model.Persistable"%>
<%@page import="com.bjsasc.plm.core.persist.PersistHelper"%>
<%@page import="com.bjsasc.adm.active.helper.AdmHelper"%>
<%@page import="com.bjsasc.adm.active.model.ActiveBase"%>
<%@page import="com.bjsasc.adm.active.model.activerecycle.ActiveRecycle"%>

<%@page errorPage="/plm/ajaxError.jsp"%>
<%
	//清除缓存
	response.setHeader("Pragma", "No-cache");
	response.setHeader("Cache-Control", "no-cache");
	response.setDateHeader("Expires", 0);
	// 页面使用UTF-8编码
	request.setCharacterEncoding("UTF-8");

	String op = request.getParameter("op");
	int count = 0;
	List<ActiveRecycle> recycleObjects = new ArrayList<ActiveRecycle>();
	
	if ("isFolder".equals(op)) {
		//得到grid中传来的字符串
		String griddata = request.getParameter(KeyS.DATA);
		griddata  = URLDecoder.decode(griddata, "UTF-8");
		//将数据转换为list
		List<Map<String, Object>> dataList = DataUtil.JsonToList(griddata);
		
		for (int i = 0; i < dataList.size(); i++) {
			Map<String, Object> data = dataList.get(i);
			String rid = (String) data.get("INNERID");
			String rcid = (String) data.get("CLASSID");
			//待移动的对象
			Persistable recycleObject = PersistHelper.getService().getObject(rcid, rid);

			ActiveRecycle activeRecycle = (ActiveRecycle)recycleObject;
			String innerId = activeRecycle.getItemId();
			String classId = activeRecycle.getItemClassId();
			
			//待移动的对象
			Persistable obj = PersistHelper.getService().getObject(classId, innerId);
			FolderInfo folderInfo = ((Foldered)obj).getFolderInfo();
			Folder folder = folderInfo.getFolder();
			if (folder != null) {
				//添加待移动对象
				recycleObjects.add(activeRecycle);				
			}
		}
		//整理完需要还原的所有元素，开始还原
		AdmHelper.getActiveRecycleService().reductionItems(recycleObjects);
		count = dataList.size()-recycleObjects.size();
	} else {
		//得到grid中传来的字符串
		String griddata = request.getParameter("griddata");
		//将数据转换为list
		List<Map<String, Object>> dataList = DataUtil.JsonToList(griddata);

		//待移动的对象，源文件夹，目标文件夹
		List<Persistable> targetFolders = new ArrayList<Persistable>(); 
		
		for (int i = 0; i < dataList.size(); i++) {
			Map<String, Object> data = dataList.get(i);
			String rid = (String) data.get("INNERID");
			String rcid = (String) data.get("CLASSID");
			//待移动的对象
			Persistable recycleObject = PersistHelper.getService().getObject(rcid, rid);

			ActiveRecycle activeRecycle = (ActiveRecycle)recycleObject;
			String innerId = activeRecycle.getItemId();
			String classId = activeRecycle.getItemClassId();
			
			//移动的目标文件夹
			String targetFolderOid = (String) data.get("NEWFOLDER_OID");
			Persistable targetFolder = PersistHelper.getService().getObject(targetFolderOid);
			
			//待移动的对象
			Persistable obj = PersistHelper.getService().getObject(classId, innerId);
			FolderInfo folderInfo = ((Foldered)obj).getFolderInfo();
			Folder folder = folderInfo.getFolder();
			if (folder == null) {
				//添加待移动对象
				targetFolders.add(targetFolder);
				recycleObjects.add(activeRecycle);			
			}
		}
		//整理完需要还原的所有元素，开始还原
		AdmHelper.getActiveRecycleService().reductionItems(recycleObjects, targetFolders);
		count = recycleObjects.size();
	}
	
	Map<String, String> result = new HashMap<String, String>();
	result.put("success", "true");
	result.put("count", count+"");
	String json = DataUtil.mapToSimpleJson(result);
	out.println(json);
%>