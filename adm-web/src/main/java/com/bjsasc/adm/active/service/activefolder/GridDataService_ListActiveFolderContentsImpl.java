package com.bjsasc.adm.active.service.activefolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bjsasc.adm.common.AdmLifeCycleConstUtil;
import com.bjsasc.platform.objectmodel.business.persist.PTFactor;
import com.bjsasc.plm.Helper;
import com.bjsasc.plm.KeyS;
import com.bjsasc.plm.core.context.model.Context;
import com.bjsasc.plm.core.folder.Folder;
import com.bjsasc.plm.core.folder.FolderHelper;
import com.bjsasc.plm.core.persist.model.Persistable;
import com.bjsasc.plm.core.query.condition.Condition;
import com.bjsasc.plm.core.session.SessionHelper;
import com.bjsasc.plm.core.system.access.AccessControlHelper;
import com.bjsasc.plm.core.system.access.PtPermS;
import com.bjsasc.plm.core.system.access.operate.Operate;
import com.bjsasc.plm.core.system.config.ConfigHelper;
import com.bjsasc.plm.core.vc.model.Iterated;
import com.bjsasc.plm.folder.extend.FolderQueryIterated;
import com.bjsasc.plm.folder.extend.FolderQueryManaged;
import com.bjsasc.plm.folder.extend.FolderQuerySubFolder;
import com.bjsasc.plm.grid.GridHelper;
import com.bjsasc.plm.grid.data.GridDataService;
import com.bjsasc.plm.type.type.Type;

/**
 * �ļ������ݱ�����ݲ�ѯ����
 * 
 * @author linjinzhi
 * 
 */
public class GridDataService_ListActiveFolderContentsImpl implements GridDataService {

	// ���ݲ�������ȷ���ڻ�ȡ�ļ��������б�ʱ�Ƿ���֤Ȩ��
	private static final String CHECKACCESSINFOLDER = ConfigHelper.getService().getParameter("checkAccessInFolder",
			"/plm/system");

	private static final String LC_RECYCLE_NAME = AdmLifeCycleConstUtil.LC_RECYCLE.getName();

	public List<Map<String, Object>> getRows(String spot, String spotInstance, Map<Type, Condition> typeCondition,
			Map<String, Object> params) {

		List<String> keys = GridHelper.getService().getMyLatestGridViewColumnIds(spot, spotInstance);

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		/**
		 * ��ѯ�ļ��������еĶ���δ����Ȩ�޵Ĺ���
		 */
		List<Object> forCheckObj = getObjectByTypeConditionAndParam(typeCondition, params);

		for (Object obj : forCheckObj) {

			Map<String, Object> map = Helper.getTypeManager().format((PTFactor) obj, keys, false);
			// ������ɾ�������ݡ�
			if (LC_RECYCLE_NAME.equals(map.get("LIFECYCLE_STATE"))) {
				continue;
			}
			map.put(KeyS.ACCESS, true);
			list.add(map);
		}
		return list;
	}

	/**
	 * �������ͺͲ����������в�ѯ�����ļ����µĶ���,������Ȩ�޹��ˣ����������ͬʱ�����ڹ����������б�
	 * 
	 * @author litianli
	 * @date 2013��10��15�� ����2:05:13
	 * @param typeCondition
	 * @param params
	 * @return
	 * 
	 */
	public static List<Object> getObjectByTypeConditionAndParam(Map<Type, Condition> typeCondition,
			Map<String, Object> params) {
		// ����У��Ȩ�޵ļ���
		List<Object> forCheckObj = new ArrayList<Object>();

		// Ŀ���ļ���
		Folder folder = (Folder) params.get("folder");

		// ����Folder��ȡ�����ģ�Ȼ���ȡ��ѡ���е�ֵ
		Context context = folder.getContextInfo().getContext();
		boolean isShowNewestBranch = FolderHelper.getService().isShowNewestBranch(context);

		for (Type type : typeCondition.keySet()) {
			String targetClassName = type.getId();
			String targetAlias = type.getAlias();

			// Ŀ����
			Class<?> target = Helper.getTypeService().getTargetClass(targetClassName);

			// �汾����ִ�������ѯ���ް汾����ֱ����Ŀ�����ִ�в�ѯ
			List result = new ArrayList();

			if (Iterated.class.isAssignableFrom(target)) {
				String masterClassName = targetClassName + "Master";
				Type master = Helper.getTypeManager().getType(masterClassName);
				String masterAlias = master.getAlias();

				// ���Ŀ������а汾���ƣ��������������
				result = FolderQueryIterated.query(folder, targetClassName, targetAlias, masterClassName, masterAlias,
						typeCondition.get(type), isShowNewestBranch);
			} else if (Folder.class.isAssignableFrom(target)) {
				// ��ѯ���ļ���
				result = FolderQuerySubFolder.query(folder, targetAlias, typeCondition.get(type));
			} else {
				// ��ѯ��ͨ����
				result = FolderQueryManaged.query(folder, targetClassName, targetAlias, typeCondition.get(type));
			}
			forCheckObj.addAll(result);
		}
		// �Ѷ�����뻺�棬׼��û��Ȩ�޶�����б�
		Map<String, Object> objMap = new HashMap<String, Object>();
		for (Object obj : forCheckObj) {
			objMap.put(Helper.getOid((Persistable) obj), obj);
		}
		List<Object> noAccessObjs = new ArrayList<Object>();
		if (!"false".equals(CHECKACCESSINFOLDER)) {
			// ����У��Ȩ��
			List<Map<String, Object>> permRS = AccessControlHelper.getService().hasEntityPermission(
					SessionHelper.getService().getUser(), Operate.ACCESS, context, forCheckObj);

			for (Map<String, Object> temp : permRS) {
				Object obj = temp.get("OBJ");
				String oid = null;
				if (obj instanceof Persistable) {
					Persistable obj2 = (Persistable) obj;
					oid = com.bjsasc.plm.core.Helper.getOid(obj2);
				} else if (obj instanceof PtPermS) {
					PtPermS ptObj = (PtPermS) obj;
					oid = com.bjsasc.plm.core.Helper.getOid(ptObj.getClassId(), ptObj.getInnerId());
				}

				// flag>0��˵�������ʾΪoid�Ķ������Ȩ�ޣ�����Ӧ�ù��˵�
				// ���û��Ȩ��,�Ѹö��������Ƴ��б�
				if ((Integer) temp.get("FLAG") <= 0) {
					noAccessObjs.add(objMap.get(oid));
				}
			}
		}
		// �Ƴ�û��Ȩ�޵Ķ���
		forCheckObj.removeAll(noAccessObjs);
		return forCheckObj;
	}
}
