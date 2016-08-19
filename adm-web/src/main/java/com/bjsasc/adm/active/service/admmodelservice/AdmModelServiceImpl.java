package com.bjsasc.adm.active.service.admmodelservice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ognl.OgnlException;

import com.bjsasc.adm.active.model.activefolder.ActiveModelFolder;
import com.bjsasc.adm.active.model.activemodel.ActiveModel;
import com.bjsasc.adm.active.model.modelfolderlink.ModelFolderLink;
import com.bjsasc.adm.common.ActiveInitParameter;
import com.bjsasc.adm.common.ConstUtil;
import com.bjsasc.platform.objectmodel.business.persist.PersistUtil;
import com.bjsasc.plm.core.Helper;
import com.bjsasc.plm.core.context.model.Context;
import com.bjsasc.plm.core.folder.Folder;
import com.bjsasc.plm.core.folder.FolderHelper;
import com.bjsasc.plm.core.folder.FolderInfo;
import com.bjsasc.plm.core.folder.FolderLink;
import com.bjsasc.plm.core.folder.SubFolder;
import com.bjsasc.plm.core.persist.PersistHelper;
import com.bjsasc.plm.core.persist.model.Persistable;
import com.bjsasc.plm.folder.FolderManagerHelper;
import com.bjsasc.plm.type.TypeTreeUtil;
import com.bjsasc.plm.ui.tree.TreeNode;

/**
 * �����ļ�ģ�ͷ���ʵ���ࡣ
 * 
 * @author yanjia 2013-5-13
 */
@SuppressWarnings({ "deprecation", "static-access" })
public class AdmModelServiceImpl implements AdmModelService {

	@Override
	public void synchronizeModel(String spot, String cabinetOid) {
		List<TreeNode> nodes;
		try {
			nodes = TypeTreeUtil.buildTypeTree(spot, null);
			Persistable obj = Helper.getPersistService().getObject(cabinetOid);
			Folder parentFolder = (Folder) obj;
			//ȡ��ģ�ͱ�������ģ��
			String sql = "SELECT A .MODELID, A .MODELNAME, A .INNERID AS MODELINNERID, A .CLASSID AS MODELCLASSID, B.INNERID AS LINKINNERID, B.CLASSID AS LINKCLASSID, B.TOOBJECTID, B.TOOBJECTCLASSID FROM ADM_ACTIVEMODEL A, ADM_MODELFOLDERLINK B WHERE A .INNERID = B.FROMOBJECTID AND A .CLASSID = B.FROMOBJECTCLASSID";
			List<Map<String, Object>> modelAndfolderlinkList = PersistHelper.getService().query(sql);
	
			TreeNode nodea = nodes.get(0);
			List<TreeNode> children = nodea.getChildren();
	
			for (TreeNode node : children) {
				toFolder(node, parentFolder, modelAndfolderlinkList);
			}
		} catch (OgnlException e) {
			e.printStackTrace();
		}

		/*for (Map<String, Object> mapTemp : modelAndfolderlinkList) {
			ActiveModel model = (ActiveModel) Helper.getPersistService().getObject(
					mapTemp.get("MODELCLASSID") + ":" + mapTemp.get("MODELINNERID"));
			//ɾ��ģ��
			PersistHelper.getService().delete(model);

			//ɾ��ģ�����ļ���link
			ModelFolderLink modelFolderlink = (ModelFolderLink) Helper.getPersistService().getObject(
					mapTemp.get("LINKCLASSID") + ":" + mapTemp.get("LINKINNERID"));
			PersistHelper.getService().delete(modelFolderlink);

			//ɾ���ļ���PLM_FOLDER_FOLDERLINK
			SubFolder subFolder = (SubFolder) Helper.getPersistService().getObject(
						mapTemp.get("TOOBJECTCLASSID") + ":" + mapTemp.get("TOOBJECTID"));
			PersistHelper.getService().delete(subFolder);
			
			//ɾ�����ļ����븸�ļ���link
			String linkSql = "select * from PLM_FOLDER_FOLDERLINK link where link.fromObjectId = ? and link.fromObjectClassId = ?";
			List<FolderLink> forlderlinkList = PersistHelper.getService().query(linkSql, FolderLink.class, subFolder.getInnerId(),
					subFolder.getClassId());
			PersistHelper.getService().delete(forlderlinkList);
			//ɾ���ļ���������ļ���ɾ�����ļ���
		}*/
	}

	private void toFolder(TreeNode node, Folder parentFolder, List<Map<String, Object>> modelAndfolderlinkList) {
		String nodeName = node.getNodeName();
		List<TreeNode> children = node.getChildren();
		FolderInfo parentFolderInfo = new FolderInfo();
		SubFolder subFolder = null;
		boolean containflag = false;

		for (Map<String, Object> model : modelAndfolderlinkList) {
			if (model.get("MODELID").equals(node.getNodeId()) && model.get("MODELNAME").equals(node.getNodeName())) {
				subFolder = (SubFolder) Helper.getPersistService().getObject(
						model.get("TOOBJECTCLASSID") + ":" + model.get("TOOBJECTID"));
				modelAndfolderlinkList.remove(model);
				containflag = true;
				break;
			}
		}
		if (containflag == false) {
			//����ģ�ͽṹ
			ActiveModel model = newActiveModel();
			model.setModelId(node.getNodeId());
			model.setModelName(node.getNodeName());
			Helper.getPersistService().save(model);

			//�����ļ���
			subFolder = new ActiveModelFolder();
			subFolder.setClassId("ActiveModelFolder");
			subFolder.setDomaintype(SubFolder.DOMAINTYPE_EXTENDS);			
			parentFolderInfo.setFolder(parentFolder);
			parentFolderInfo.setFolderName(parentFolder.getName());

			subFolder.setName(nodeName);
			subFolder.setNote(nodeName);

			// RootContext rootContext = (RootContext) Helper.getContextService().getRootContext();
			// ActiveContext activeContext = ActiveInitParameter.getActiveContext();
			subFolder.setContextInfo(parentFolder.getContextInfo());
			subFolder.setDomainInfo(parentFolder.getDomainInfo());

			subFolder.setFolderInfo(parentFolderInfo);
			FolderManagerHelper.getService().createSubFolder(subFolder, parentFolder);

			// ����ģ�����ļ���֮���link
			ModelFolderLink link = newModelFolderLink();
			link.setToObject(subFolder);
			link.setFromObject(model);
			Helper.getPersistService().save(link);
		}

		//�Ƿ�һ���Լ����¼��ڵ�
		if (children.size() > 0) {
			for (TreeNode temp : children) {
				toFolder(temp, subFolder, modelAndfolderlinkList);
			}
		}
	}

	private ModelFolderLink newModelFolderLink() {
		ModelFolderLink link = (ModelFolderLink) PersistUtil.createObject(ModelFolderLink.CLASSID);
		link.setClassId(link.CLASSID);
		return link;
	}

	private ActiveModel newActiveModel() {
		ActiveModel model = (ActiveModel) PersistUtil.createObject(ActiveModel.CLASSID);
		model.setClassId(model.CLASSID);
		return model;
	}

	@Override
	public Map<String, String> getCreateActiveDocumentParam(String requestFolderOid) {
		Map<String, String> result = new HashMap<String, String>();
		Folder folder = (Folder) Helper.getPersistService().getObject(requestFolderOid);

		Context context = folder.getContextInfo().getContext();
		//������OID
		String contextOid = context.getOid();
		//������NAME
		String contextName = context.getName();
		//�ļ���PATH
		String folderPath = FolderHelper.getService().getFolderPathStr(folder);
		//�����ļ���OID
		String classFolderOid = getModelFolder(requestFolderOid);
		//����
		String classId = getModelId(classFolderOid);

		//����
		result.put(ConstUtil.CLASSID_PPRAM, classId);
		//�ļ���OID
		result.put(ConstUtil.FOLDEROID_PPRAM, requestFolderOid);
		//�ļ���PATH
		result.put(ConstUtil.FOLDERPATH_PPRAM, folderPath);
		//������OID
		result.put(ConstUtil.CONTEXTOID_PPRAM, contextOid);
		//������NAME
		result.put(ConstUtil.CONTEXTNAME_PPRAM, contextName);

		return result;
	}

	@Override
	public String getModelFolder(String folderOid) {
		if (folderOid == null || folderOid.isEmpty()) {
			return "";
		}
		
		if ("ActiveCabinet".equals(Helper.getClassId(folderOid))){
			/*Folder folder = (Folder) Helper.getPersistService().getObject(folderOid);
			Context context = folder.getContextInfo().getContext();
			if ("RootContext".equals(context.getClassId())) {
				return folderOid;
			} else {
				return "";
			}*/
			if (folderOid.equals(ActiveInitParameter.getActiveCabinetOid())) {
				return folderOid;
			} else {
				return "";
			}
		}
		// �ļ����Ƿ���ƥ��ģ��
		boolean flag = isModelFolder(folderOid);
		if (flag) {
			return folderOid;
		} else {
			// �ҵ����ļ���ִ��getModelFolder����
			return getModelFolder(getParentFolder(folderOid));
		}
	}

	/**
	 * �ļ����Ƿ���ƥ��ģ��
	 * 
	 * @param folderOid String
	 * @return  boolean
	 */
	public boolean isModelFolder(String folderOid) {
		String sql = "SELECT * FROM ADM_MODELFOLDERLINK WHERE TOOBJECTCLASSID || ':' || TOOBJECTID = ?";
		List<ModelFolderLink> linkList = Helper.getPersistService().query(sql, ModelFolderLink.class, folderOid);
		if (linkList == null || linkList.isEmpty()) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * ȡ���ļ�����ƥ���ģ��ID
	 * 
	 * @param folderOid String
	 * @return  String
	 */
	public String getModelId(String folderOid) {
		if ("ActiveCabinet".equals(Helper.getClassId(folderOid))){
			return "ActiveDocument";
		}		
		String sql = "SELECT * FROM ADM_MODELFOLDERLINK WHERE TOOBJECTCLASSID || ':' || TOOBJECTID = ?";
		List<ModelFolderLink> linkList = Helper.getPersistService().query(sql, ModelFolderLink.class, folderOid);
		if (linkList == null || linkList.isEmpty()) {
			return "ActiveDocument";
		} else {
			ActiveModel model = (ActiveModel) linkList.get(0).getFrom();
			return model.getModelId();
		}
	}

	/**
	 * ȡ���ļ�����ƥ���ģ������
	 * 
	 * @param folderOid String
	 * @return  String
	 */
	public String getModelName(String folderOid) {
		if ("Cabinet".equals(Helper.getClassId(folderOid))){
			return ActiveInitParameter.getActiveContext().getName();
		}		
		String sql = "SELECT * FROM ADM_MODELFOLDERLINK WHERE TOOBJECTCLASSID || ':' || TOOBJECTID = ?";
		List<ModelFolderLink> linkList = Helper.getPersistService().query(sql, ModelFolderLink.class, folderOid);
		if (linkList == null || linkList.isEmpty()) {
			return ActiveInitParameter.getActiveContext().getName();
		} else {
			ActiveModel model = (ActiveModel) linkList.get(0).getFrom();
			return model.getModelName();
		}
	}

	/**
	 * ȡ���ļ��еĸ��ļ���
	 * 
	 * @param folderOid String
	 * @return  String
	 */
	private String getParentFolder(String folderOid) {
		String sql = "SELECT * FROM PLM_FOLDER_FOLDERLINK WHERE FROMOBJECTCLASSID || ':' || FROMOBJECTID = ?";
		List<FolderLink> linkList = Helper.getPersistService().query(sql, FolderLink.class, folderOid);
		if (linkList == null || linkList.isEmpty()) {
			return "";
		} else {
			Folder folder = linkList.get(0).getParentFolder();
			return Helper.getOid(folder);
		}
	}
	
	public Map<String,String> getFolderByModel(String classId){
		Map<String, String> result = new HashMap<String, String>();
		String sql="SELECT L.* FROM ADM_ACTIVEMODEL M, ADM_MODELFOLDERLINK L WHERE M .MODELID = ? AND L.FROMOBJECTCLASSID || ':' || L.FROMOBJECTID = M .CLASSID || ':' || M .INNERID";
		List<ModelFolderLink> linkList = Helper.getPersistService().query(sql, ModelFolderLink.class, classId);
		String folderOid;
		String folderPath;
		if (linkList == null || linkList.isEmpty()) {
			Folder folder = (Folder) Helper.getPersistService().getObject(ActiveInitParameter.getActiveCabinetOid());
			folderOid = Helper.getOid(folder);
			folderPath = FolderHelper.getService().getFolderPathStr(folder);
		} else {
			Folder folder = (Folder)linkList.get(0).getTo();
			folderOid=Helper.getOid(folder);
			folderPath=FolderHelper.getService().getFolderPathStr(folder);
		}
		//�ļ���OID
		result.put(ConstUtil.FOLDEROID_PPRAM, folderOid);
		//�ļ���PATH
		result.put(ConstUtil.FOLDERPATH_PPRAM, folderPath);
		return result;
	}
}
