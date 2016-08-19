<%@page import="com.bjsasc.plm.core.folder.FolderInfo"%>
<%@page import="com.bjsasc.plm.folder.FolderManagerHelper"%>
<%@page import="com.bjsasc.plm.core.persist.PersistHelper"%>
<%@page import="com.bjsasc.plm.core.domain.DomainInfo"%>
<%@page import="com.bjsasc.plm.core.domain.DomainHelper"%>
<%@page import="com.bjsasc.plm.core.domain.Domain"%>
<%@page import="com.bjsasc.plm.core.context.model.Context"%>
<%@page import="com.bjsasc.plm.core.folder.SubFolder"%>
<%@page import="com.bjsasc.adm.active.model.activefolder.ActiveCabinet"%>
<%@page import="com.bjsasc.adm.active.model.activefolder.ActiveSubFolder"%>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="GBK"%>
<%@page import="com.bjsasc.plm.util.Ajax"%>
<%@page import="com.bjsasc.plm.core.folder.Folder"%>
<%@page import="com.bjsasc.plm.KeyS"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="com.bjsasc.ui.json.DataUtil"%>
<%@page import="com.bjsasc.plm.Helper"%>
<%@page session="true"%>
<%@page import="java.util.List"%>
<%@page errorPage="/plm/ajaxError.jsp"%>

<%
	//��������
	String name = request.getParameter(KeyS.NAME);//����
	String note = request.getParameter(KeyS.NOTE);//����
	String parentFolderOid = request.getParameter("parentFolderOid");	
	
	Folder parentFolder = (Folder)Helper.getPersistService().getObject(parentFolderOid);

	//�����Ϣ
	Map<String, String> result = new HashMap<String, String>();
	if (parentFolder instanceof ActiveCabinet) {
		result.put(Ajax.SUCCESS, Ajax.FASLE);
		result.put(Ajax.MESSAGE, "���и�Ŀ¼�������½����ļ��У�����������ģ��Ŀ¼���½����ļ��С�");
	} else {
		Context context = parentFolder.getContextInfo().getContext();
		String domainoid = request.getParameter("domainoid");
		Domain domain = (Domain) PersistHelper.getService().getObject(domainoid);
		FolderInfo parentFolderInfo = new FolderInfo();
		parentFolderInfo.setFolder(parentFolder);
		parentFolderInfo.setFolderName(parentFolder.getName());
	
		//�����ļ���
		SubFolder subFolder = new ActiveSubFolder();
		subFolder.setClassId("ActiveSubFolder");
		subFolder.setDomaintype(SubFolder.DOMAINTYPE_EXTENDS);		
		subFolder.setName(name);
		subFolder.setNote(note);
		subFolder.setContextInfo(context.buildContextInfo());
		subFolder.setDomainInfo(domain.buildDomainInfo());
		subFolder.setFolderInfo(parentFolderInfo);
		String domaintype = request.getParameter("isinherit");
		if(domaintype==null){
			subFolder.setDomaintype(SubFolder.DOMAINTYPE_SELF);
		}
		FolderManagerHelper.getService().createSubFolder(subFolder, parentFolder);
		
		//��װ�մ����Ķ����json��ʽ
		Map<String, Object> mapObject = Helper.getTypeManager().getAttrValues(subFolder);
		String jsonObject = DataUtil.mapToSimpleJson(mapObject);
		result.put(Ajax.Object, jsonObject);
	}	
	String json = DataUtil.mapToSimpleJson(result);
	out.write(json);
%>