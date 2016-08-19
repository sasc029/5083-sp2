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
	//�������
	response.setHeader("Pragma", "No-cache");
	response.setHeader("Cache-Control", "no-cache");
	response.setDateHeader("Expires", 0);
	// ҳ��ʹ��UTF-8����
	request.setCharacterEncoding("UTF-8");

	String op = request.getParameter("op");
	int count = 0;
	List<ActiveRecycle> recycleObjects = new ArrayList<ActiveRecycle>();
	
	if ("isFolder".equals(op)) {
		//�õ�grid�д������ַ���
		String griddata = request.getParameter(KeyS.DATA);
		griddata  = URLDecoder.decode(griddata, "UTF-8");
		//������ת��Ϊlist
		List<Map<String, Object>> dataList = DataUtil.JsonToList(griddata);
		
		for (int i = 0; i < dataList.size(); i++) {
			Map<String, Object> data = dataList.get(i);
			String rid = (String) data.get("INNERID");
			String rcid = (String) data.get("CLASSID");
			//���ƶ��Ķ���
			Persistable recycleObject = PersistHelper.getService().getObject(rcid, rid);

			ActiveRecycle activeRecycle = (ActiveRecycle)recycleObject;
			String innerId = activeRecycle.getItemId();
			String classId = activeRecycle.getItemClassId();
			
			//���ƶ��Ķ���
			Persistable obj = PersistHelper.getService().getObject(classId, innerId);
			FolderInfo folderInfo = ((Foldered)obj).getFolderInfo();
			Folder folder = folderInfo.getFolder();
			if (folder != null) {
				//��Ӵ��ƶ�����
				recycleObjects.add(activeRecycle);				
			}
		}
		//��������Ҫ��ԭ������Ԫ�أ���ʼ��ԭ
		AdmHelper.getActiveRecycleService().reductionItems(recycleObjects);
		count = dataList.size()-recycleObjects.size();
	} else {
		//�õ�grid�д������ַ���
		String griddata = request.getParameter("griddata");
		//������ת��Ϊlist
		List<Map<String, Object>> dataList = DataUtil.JsonToList(griddata);

		//���ƶ��Ķ���Դ�ļ��У�Ŀ���ļ���
		List<Persistable> targetFolders = new ArrayList<Persistable>(); 
		
		for (int i = 0; i < dataList.size(); i++) {
			Map<String, Object> data = dataList.get(i);
			String rid = (String) data.get("INNERID");
			String rcid = (String) data.get("CLASSID");
			//���ƶ��Ķ���
			Persistable recycleObject = PersistHelper.getService().getObject(rcid, rid);

			ActiveRecycle activeRecycle = (ActiveRecycle)recycleObject;
			String innerId = activeRecycle.getItemId();
			String classId = activeRecycle.getItemClassId();
			
			//�ƶ���Ŀ���ļ���
			String targetFolderOid = (String) data.get("NEWFOLDER_OID");
			Persistable targetFolder = PersistHelper.getService().getObject(targetFolderOid);
			
			//���ƶ��Ķ���
			Persistable obj = PersistHelper.getService().getObject(classId, innerId);
			FolderInfo folderInfo = ((Foldered)obj).getFolderInfo();
			Folder folder = folderInfo.getFolder();
			if (folder == null) {
				//��Ӵ��ƶ�����
				targetFolders.add(targetFolder);
				recycleObjects.add(activeRecycle);			
			}
		}
		//��������Ҫ��ԭ������Ԫ�أ���ʼ��ԭ
		AdmHelper.getActiveRecycleService().reductionItems(recycleObjects, targetFolders);
		count = recycleObjects.size();
	}
	
	Map<String, String> result = new HashMap<String, String>();
	result.put("success", "true");
	result.put("count", count+"");
	String json = DataUtil.mapToSimpleJson(result);
	out.println(json);
%>