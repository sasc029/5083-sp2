package com.bjsasc.ddm.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bjsasc.ddm.distribute.helper.DistributeHelper;
import com.bjsasc.ddm.distribute.model.distributeorder.DistributeOrder;
import com.bjsasc.ddm.distribute.model.distributepapertask.DistributePaperTask;
import com.bjsasc.ddm.distribute.model.recdespapertask.RecDesPaperTask;
import com.bjsasc.ddm.distribute.service.distributeconfigparameter.DistributeConfigParameterService;
import com.bjsasc.ddm.distribute.service.distributeobjecttype.DistributeObjectTypeService;
import com.bjsasc.ddm.distribute.service.distributeorder.DistributeOrderService;
import com.bjsasc.ddm.distribute.service.distributepapertask.DistributePaperTaskService;
import com.bjsasc.ddm.distribute.service.recdespapertask.RecDesPaperTaskService;
import com.bjsasc.platform.objectmodel.business.persist.PTFactor;
import com.bjsasc.plm.Helper;
import com.bjsasc.plm.KeyS;
import com.bjsasc.plm.core.context.model.Context;
import com.bjsasc.plm.core.context.model.Contexted;
import com.bjsasc.plm.core.domain.Domained;
import com.bjsasc.plm.core.managed.model.Manageable;
import com.bjsasc.plm.core.option.OptionHelper;
import com.bjsasc.plm.core.option.OptionValue;
import com.bjsasc.plm.core.persist.model.Persistable;
import com.bjsasc.plm.core.session.SessionHelper;
import com.bjsasc.plm.core.system.access.AccessControlHelper;
import com.bjsasc.plm.core.system.access.PtPermS;
import com.bjsasc.plm.core.system.access.operate.Operate;
import com.bjsasc.plm.core.system.principal.User;
import com.bjsasc.plm.core.util.StringUtil;
import com.bjsasc.plm.grid.GridHelper;

/**
 * ������֤Ȩ���ࡣ
 * 
 * @author gengancong 2014-5-12
 */
public class CheckPermission {

	/**
	 * ���������ĺ͵�ǰ�û�ȡ�ð��ĵ�����������Ϣ
	 * 
	 * @param context
	 *            ������
	 * @param currentUser
	 *            ��ǰ�û�
	 * @return ���ĵ�����������Ϣ
	 */
	private static List<String> getTypeMap(Context context, User currentUser) {
		String paramId = "DistributeObjectType";
		Context cont = getContext(context, paramId);
		String contextOid = Helper.getOid(cont);
		String userOid = Helper.getOid(currentUser);
		String key = contextOid + "_" + userOid;
		List<String> typeList = DdmTypePermissionCache.getTypeData(key);
		if(typeList == null){
			typeList = new ArrayList<String>();
		}
		return typeList;
	}
	
	/**
	 * ָ����������û�ж���������Ϣ ȡ�ø������ļ����ж�
	 * 
	 * @param context
	 *            ������
	 * @param paramId
	 *            ����ID
	 * @return ������
	 */
	private static Context getContext(Context context, String paramId) {
		List<String> contextOidList = DdmTypePermissionCache.getTypeData(paramId);
		if(null == contextOidList){
			return context;
		}
		String contextOid = Helper.getOid(context);
		if (!contextOidList.contains(contextOid)) {
			if (context instanceof Contexted) {
				Contexted contexted = (Contexted) context;
				Context parentContext = contexted.getContextInfo().getContext();
				return getContext(parentContext, paramId);
			}
		}
		return context;
	}

	/**
	 * ������֤Ȩ��
	 * 
	 * @param forCheckObj
	 *            ���ݶ���
	 * @param spot
	 *            ������ʾ����
	 * @param spotInstance
	 *            ������ʾ����
	 * @return Ȩ����֤�����
	 */
	public static List<Object> checkPermission(List listObj, String spot,
			String spotInstance) {

		// ����У��Ȩ�޵ļ���
		List<Object> forCheckObj = new ArrayList<Object>();
		List<Object> rtnObjList = new ArrayList<Object>();
		for (Object obj : listObj) {
			//�ַ����ݣ���������ֽ��ǩ�����������Թ����Ȩ��У��
			if (obj instanceof Domained) {
				forCheckObj.add(obj);
			}else{
				rtnObjList.add(obj);
			}
		}

		// �Ѷ�����뻺�棬׼��û��Ȩ�޶�����б�
		Map<String, Object> objMap = new HashMap<String, Object>();
		for (Object obj : forCheckObj) {
			objMap.put(Helper.getOid((Persistable) obj), obj);
		}
		List<Object> noAccessObjs = new ArrayList<Object>();
		// ����У��Ȩ��
		List<Map<String, Object>> permRS = AccessControlHelper.getService()
				.hasEntityPermission(SessionHelper.getService().getUser(),
						Operate.ACCESS, null, forCheckObj);

		for (Map<String, Object> temp : permRS) {
			Object obj = temp.get("OBJ");
			String oid = null;
			if (obj instanceof Persistable) {
				Persistable obj2 = (Persistable) obj;
				oid = com.bjsasc.plm.core.Helper.getOid(obj2);
			} else if (obj instanceof PtPermS) {
				PtPermS ptObj = (PtPermS) obj;
				oid = com.bjsasc.plm.core.Helper.getOid(ptObj.getClassId(),
						ptObj.getInnerId());
			}

			// flag>0��˵�������ʾΪoid�Ķ������Ȩ�ޣ�����Ӧ�ù��˵�
			// ���û��Ȩ��,�Ѹö��������Ƴ��б�
			if ((Integer) temp.get("FLAG") <= 0) {
				noAccessObjs.add(objMap.get(oid));
			}
		}
		// �Ƴ�û��Ȩ�޵Ķ���
		forCheckObj.removeAll(noAccessObjs);
		rtnObjList.addAll(forCheckObj);
		return rtnObjList;
	}

	/**
	 * ������֤Ȩ�� & Ĭ�ϸ�ʽ��
	 * 
	 * @param forCheckObj
	 *            ���ݶ���
	 * @param spot
	 *            ������ʾ����
	 * @param spotInstance
	 *            ������ʾ����
	 * @return Ȩ����֤�����
	 */
	public static List<Map<String, Object>> checkPermissionAF(List listObj,
			String spot, String spotInstance) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		
		List<String> keys = GridHelper.getService()
				.getMyLatestGridViewColumnIds(spot, spotInstance);
		// ����У��Ȩ�޵ļ���
		List<Object> forCheckObj = checkPermission(listObj, spot, spotInstance);
		// ��֤�������������õĽ�ɫȨ��
		checkDistributeOrderType(forCheckObj);
		for (Object obj : forCheckObj) {

			Map<String, Object> map = Helper.getTypeManager().format(
					(PTFactor) obj, keys, false);
			map.put(KeyS.ACCESS, true);
			list.add(map);
		}
		return list;
	}

	/**
	 * �ж�������������������������ͣ� ��ǰ�û��Ƿ��в������ݵ�Ȩ�ޡ�
	 * 
	 * @param listObj
	 *            ���ݼ�
	 */
	public static void checkDistributeOrderType(List<Object> listObj) {
		User currentUser = SessionHelper.getService().getUser();
		List<Object> rl = new ArrayList<Object>();
		Map<String, List<String>> map = new HashMap<String, List<String>>();
		Map<String, OptionValue> optionValueMap = new HashMap<String, OptionValue>();
		for (Object object : listObj) {
			//�жϰ������������ý�ɫȨ�޵Ŀ����Ƿ�򿪣����û�д������������
			Context context = null;
			String contextInnerId = null;
			if(object instanceof Contexted){
				context = ((Contexted) object).getContextInfo().getContext();
				//��ͬ�����Ĳ��ٽ��п��ز�ѯ
				contextInnerId = context.getInnerId();
			}
			OptionValue value = null;
			if(StringUtil.isNull(optionValueMap.get(contextInnerId))){
				value = OptionHelper.getService().getOptionValue(context,
						"disMange_disManageConfig_isOpenConfigRoleAuthByObjType");
				optionValueMap.put(contextInnerId, value);
			}else{
				value = (OptionValue)optionValueMap.get(contextInnerId);
			}
			if ("false".equals(value.getValue())) {
				continue;
			}
			
			if (object instanceof Manageable) {
				Manageable m = (Manageable) object;
				String innerId = m.getManageInfo().getCreateByRef().getInnerId();
				String userId = currentUser.getInnerId();
				if (userId != null && userId.equals(innerId)) {
					continue;
				}
			}

			if (object instanceof DistributeOrder) {
				DistributeOrder distributeOrder = (DistributeOrder) object;
				//��ͬ�����Ĳ��ٽ��в�ѯ��������
//				String contextInnerId=distributeOrder.getContextInfo().getContext().getInnerId();
//				Context context=distributeOrder.getContextInfo().getContext();
				List<String> typeList=new ArrayList<String>();
				if(StringUtil.isNull(map.get(contextInnerId))){
					typeList = getTypeMap(context, currentUser);
					map.put(contextInnerId, typeList);
				}else{
					typeList=(List<String>)map.get(contextInnerId);
				}
				if (!checkType(distributeOrder , typeList)) {
					rl.add(object);
				}
			} else if (object instanceof DistributePaperTask) {
				DistributePaperTask distributePaperTask = (DistributePaperTask) object;

				DistributePaperTaskService dpts = DistributeHelper
						.getDistributePaperTaskService();
				DistributePaperTask dpt = dpts
						.getDistributePaperTaskProperty(Helper
								.getOid(distributePaperTask));
				String orderOid = dpt.getOrderOid();

				DistributeOrder distributeOrder = (DistributeOrder) Helper
						.getPersistService().getObject(orderOid);
				//��ͬ�����Ĳ��ٽ��в�ѯ��������
//				String contextInnerId=distributeOrder.getContextInfo().getContext().getInnerId();
//				Context context=distributeOrder.getContextInfo().getContext();
				List<String> typeList=new ArrayList<String>();
				if(StringUtil.isNull(map.get(contextInnerId))){
					typeList = getTypeMap(context, currentUser);
					map.put(contextInnerId, typeList);
				}else{
					typeList=(List<String>)map.get(contextInnerId);
				}
				if (!checkType(distributeOrder , typeList)) {
					rl.add(object);
				}
			} else if (object instanceof RecDesPaperTask) {
				RecDesPaperTask recDesPaperTask = (RecDesPaperTask) object;

				RecDesPaperTaskService dpts = DistributeHelper
						.getRecDesPaperTaskService();
				RecDesPaperTask dpt = dpts.getRecDesPaperTaskProperty(Helper
								.getOid(recDesPaperTask));
				String orderOid = dpt.getOrderOid();

				DistributeOrder distributeOrder = (DistributeOrder) Helper
						.getPersistService().getObject(orderOid);

				List<String> typeList=new ArrayList<String>();
				if(StringUtil.isNull(map.get(contextInnerId))){
					typeList = getTypeMap(context, currentUser);
					map.put(contextInnerId, typeList);
				}else{
					typeList=(List<String>)map.get(contextInnerId);
				}
				if (!checkType(distributeOrder , typeList)) {
					rl.add(object);
				}
			}
		}
		listObj.removeAll(rl);
	}

	/**
	 * �жϷ��ŵ���Ĭ�Ϸַ����ݵķַ������Ƿ��ǵ�ǰ�û��ɲ��������͡�
	 * 
	 * @param distributeOrder
	 *            ���ŵ�����
	 * @param currentUser
	 *            �û�
	 * @return boolean true���ǿɲ�������false�����ǿɲ�������
	 */
	private static boolean checkType(DistributeOrder distributeOrder, List<String> typeList) {
		//DistributeOrderService dos = DistributeHelper.getDistributeOrderService();
		//String distributeOrderOid = Helper.getOid(distributeOrder);
		//String defaultDistributeOrderType = dos.getDefaultDistributeOrderType(distributeOrderOid);
		String defaultDistributeOrderType = distributeOrder.getMasterDataClassID();
		//Context context = distributeOrder.getContextInfo().getContext();
		//List<String> typeList = getTypeMap(context, currentUser);
		if (typeList.contains(defaultDistributeOrderType)) {
			return true;
		}
		return false;
	}

}
