package com.bjsasc.adm.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bjsasc.adm.active.model.activefolder.ActiveCabinet;
import com.bjsasc.plm.Helper;
import com.bjsasc.plm.KeyS;
import com.bjsasc.plm.core.context.model.Context;
import com.bjsasc.plm.core.context.model.Contexted;
import com.bjsasc.plm.core.folder.Folder;
import com.bjsasc.plm.core.option.OptionHelper;
import com.bjsasc.plm.core.persist.PersistHelper;
import com.bjsasc.plm.core.persist.model.Persistable;
import com.bjsasc.plm.core.session.SessionHelper;
import com.bjsasc.plm.core.system.access.AccessControlHelper;
import com.bjsasc.plm.core.util.ListUtil;
import com.bjsasc.plm.type.type.Type;
import com.bjsasc.plm.ui.tree.TreeNode;
import com.bjsasc.plm.url.Url;
import com.bjsasc.plm.util.JsonUtil;
import com.bjsasc.plm.util.TitleUtil;

/**
 * �ļ������ڵ㹹������
 * @author linjinzhi
 *
 */
public abstract class FolderTreeUtil {
	
	public static boolean isFolderDeniedVisible(Context context) {
		String value = OptionHelper.getService().getOptionValue(context, "show_folderDeniedVisible").getValue();
		return "true".equals(value);
	}

	public static Map<String, Object> buildFolder(Folder target) {
		List<Folder> path = Helper.getFolderService().getFolderPath(target);

		Map<TreeNode, Folder> map1 = new HashMap<TreeNode, Folder>();
		Map<Folder, TreeNode> map2 = new HashMap<Folder, TreeNode>();
		TreeNode treeNode = recursiveBuildTreeNode(0, path, map1, map2);

		Map<String, Boolean> map3 = canAccess(target.getContextInfo().getContext(),
				ListUtil.format(map1.values(), Object.class));

		if (isFolderDeniedVisible(target.getContextInfo().getContext())) {
			process1(treeNode, map1, map2, map3);
			return treeNode.toMap();
		} else {
			treeNode = process2(treeNode, map1, map2, map3);
			if (treeNode != null) {
				return treeNode.toMap();
			} else {
				return null;
			}
		}
	}

	public static Map<String, Object> buildSubFolder(Folder target, String asRootOid) {
		if (asRootOid != null && asRootOid.length() > 0) {
			List<Folder> path = new ArrayList<Folder>();
			if (target instanceof ActiveCabinet) {
				target = (Folder) Helper.getPersistService().getObject(asRootOid);
			}
			path.add(target);
			Map<TreeNode, Folder> map1 = new HashMap<TreeNode, Folder>();
			Map<Folder, TreeNode> map2 = new HashMap<Folder, TreeNode>();
			TreeNode treeNode = recursiveBuildTreeNode(0, path, map1, map2);			
			return treeNode.toMap();
		} else {
			return buildFolder(target);
		}
	}

	/**
	 * ����ļ�����Ȩ���ʣ����ļ�����Ϊ��***��
	 * @param treeNode
	 * @param map1
	 * @param map2
	 * @param map3
	 */
	public static void process1(TreeNode treeNode, Map<TreeNode, Folder> map1, Map<Folder, TreeNode> map2,
			Map<String, Boolean> map3) {
		Folder folder = map1.get(treeNode);
		boolean canAccess = map3.get(Helper.getOid(folder));
		if (!canAccess) {
			treeNode.setNodeName("***");
		}

		//�ݹ鴦���ӽڵ�
		for (TreeNode child : treeNode.getChildren()) {
			process1(child, map1, map2, map3);
		}
	}

	/**
	 * ��Ȩ�޵��ļ��нڵ㲻��ʾ
	 * @param treeNode
	 * @param map1
	 * @param map2
	 * @param map3
	 * @return
	 */
	public static TreeNode process2(TreeNode treeNode, Map<TreeNode, Folder> map1, Map<Folder, TreeNode> map2,
			Map<String, Boolean> map3) {
		Folder folder = map1.get(treeNode);
		boolean canAccess = map3.get(Helper.getOid(folder));
		if (!canAccess) {
			return null;
		}

		List<TreeNode> newChildren = new ArrayList<TreeNode>();
		for (TreeNode child : treeNode.getChildren()) {
			TreeNode newChild = process2(child, map1, map2, map3);
			if (newChild != null) {
				newChildren.add(newChild);
			}
		}

		treeNode.setChildren(newChildren);
		return treeNode;
	}

	/**
	 * ����һ�����ڵ�
	 * @param folder
	 * @return
	 */
	public static TreeNode buildTreeNode(Folder folder, Map<TreeNode, Folder> map1, Map<Folder, TreeNode> map2) {
		String oid = Helper.getOid(folder);
		String url = "/plm/folder/folderlist.jsp?OID=" + oid;
		String childUrl = Url.APP + "/adm/folder/visitFolder_get.jsp?op=getSubFolders&OID=" + oid;

		boolean temp = SessionHelper.getService().isCheckPermission();
		SessionHelper.getService().setCheckPermission(false);
		Map<String, Object> folderMap = Helper.getTypeManager().format(folder);
		SessionHelper.getService().setCheckPermission(temp);

		Type targetType = Helper.getTypeManager().getType(folder.getClassId());
		String iconUrl = targetType.getIcon();

		String title = TitleUtil.getTitle(folder, folderMap);

		TreeNode treeNode = new TreeNode();
		treeNode.setNodeId(oid);
		treeNode.setNodeName(title);
		treeNode.setNodeLink(url);
		treeNode.setNodeIconSrc(iconUrl);

		List<Folder> list = Helper.getFolderService().getChildFolders(folder);
		if (list.size() > 0) {
			treeNode.setNodeViewIcon(true);
			treeNode.setExpanded(false);
			treeNode.setChildUrl(childUrl);
		} else {
			treeNode.setNodeViewIcon(false);
			treeNode.setExpanded(true);
			treeNode.setChildUrl(null);
		}

		map1.put(treeNode, folder);
		map2.put(folder, treeNode);
		return treeNode;
	}

	/**
	 * �ݹ鹹�����ڵ�
	 * @param i
	 * @param path
	 * @return
	 */
	private static TreeNode recursiveBuildTreeNode(int i, List<Folder> path, Map<TreeNode, Folder> map1,
			Map<Folder, TreeNode> map2) {
		Folder target = path.get(i);

		TreeNode folderNode = buildTreeNode(target, map1, map2);

		List<TreeNode> nodes = new ArrayList<TreeNode>();
		folderNode.setChildren(nodes);
		folderNode.setExpanded(true);

		List<Folder> subFolders = Helper.getFolderService().getChildFolders(target);
		if (i == path.size() - 1) {
			for (Folder subFolder : subFolders) {
				if (subFolder == null) {
					continue;
				}
				nodes.add(buildTreeNode(subFolder, map1, map2));
			}
		} else {
			Folder subFolderOnPath = path.get(i + 1);
			for (Folder subFolder : subFolders) {
				if (subFolder.getInnerId().equals(subFolderOnPath.getInnerId())) {
					nodes.add(recursiveBuildTreeNode(i + 1, path, map1, map2));
				} else {
					nodes.add(buildTreeNode(subFolder, map1, map2));
				}
			}
		}

		return folderNode;

	}

	/**
	 * ����У��Ȩ��
	 * @param context
	 * @param list
	 * @return
	 */
	public static Map<String, Boolean> canAccess(Context context, List<Object> list) {
		Map<String, Boolean> parmMap = new HashMap<String, Boolean>();

		//TODO:����Ȩ��У���ʹ��ʱ�򣬲������´���
		for (Object obj : list) {
			boolean canAccess = AccessControlHelper.getService().canAccess(obj);
			parmMap.put(Helper.getOid((Persistable) obj), canAccess);
		}

		//����У��Ȩ��
		/*
		List<Map<String, Object>> permRS=AccessControlHelper.getService().hasEntityPermission(SessionHelper.getService().getUser(), Operate.ACCESS, context, list);		
		for(Map<String, Object> temp:permRS){
			Object obj=temp.get("OBJ");
			String oid=null;
			if(obj instanceof Persistable){
				oid=com.bjsasc.plm.core.Helper.getOid((Persistable)obj);
			}else if(obj instanceof PtPermS){
				PtPermS ptObj=(PtPermS)obj;
				oid=com.bjsasc.plm.core.Helper.getOid(ptObj.getClassId(), ptObj.getInnerId());
			}
			
			Integer flag=(Integer)temp.get("FLAG");
			parmMap.put(oid, flag>0);
		}*/

		return parmMap;
	}
	

	/**
	 * 
	 * �����û������������ݣ��ҵ����ʵ�������
	 * @param data
	 * @return
	 */
	public static String getContextOidByJsonData(String data) {
		List<Map<String, Object>> listMap = JsonUtil.toList(data);
		// ��ȡ���е�ContainerOID����ContainerOID��ֵ����map��
		for (Map<String, Object> record : listMap) {
			// ����classID,innerId
			String oid = (String) record.get(KeyS.OID);
			Persistable o = PersistHelper.getService().getObject(oid);
			// ����ö���ʵ����Contexted����ֱ�ӻ�ȡ��ContextInfo��Ϊ��������Ϣ
			if (o instanceof Contexted) {
				Context context = ((Contexted) o).getContextInfo().getContext();
				return context.getOid();
			}

		}
		Context activeContext =  ActiveInitParameter.getActiveContext();
		return activeContext.getOid();
	}
}
