package com.bjsasc.ddm.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.bjsasc.avidm.core.site.Site;
import com.bjsasc.avidm.core.site.SiteHelper;
import com.bjsasc.avidm.core.site.constant.DTSiteConstant;
import com.bjsasc.ddm.distribute.helper.DistributeHelper;
import com.bjsasc.ddm.distribute.model.convertversion.DcA3SigObjectLink;
import com.bjsasc.ddm.distribute.model.convertversion.DcA3TaskSignature;
import com.bjsasc.ddm.distribute.model.distributeelectask.DistributeElecTask;
import com.bjsasc.ddm.distribute.model.distributeinfo.DistributeInfo;
import com.bjsasc.ddm.distribute.model.distributeobject.DistributeObject;
import com.bjsasc.ddm.distribute.model.distributeorder.DistributeOrder;
import com.bjsasc.ddm.distribute.model.distributeorderobjectlink.DistributeOrderObjectLink;
import com.bjsasc.ddm.distribute.service.convertversion.DcA3SigObjectLinkService;
import com.bjsasc.ddm.distribute.service.convertversion.DcA3TaskSignatureService;
import com.bjsasc.ddm.distribute.service.distributeinfo.DistributeInfoService;
import com.bjsasc.plm.collaboration.a4x.model.DataMap;
import com.bjsasc.plm.collaboration.config.service.DataConvertConfigHelper;
import com.bjsasc.plm.collaboration.config.service.DataConvertConfigService;
import com.bjsasc.plm.collaboration.config.service.ImportVersionCfgHelper;
import com.bjsasc.plm.collaboration.site.model.DCSiteAttribute;
import com.bjsasc.plm.collaboration.site.service.DCSiteAttributeHelper;
import com.bjsasc.plm.collaboration.util.A4XObjTypeConstant;
import com.bjsasc.plm.core.Helper;
import com.bjsasc.plm.core.approve.ApproveOrder;
import com.bjsasc.plm.core.cad.CADDocument;
import com.bjsasc.plm.core.cad.CADDocumentMaster;
import com.bjsasc.plm.core.change.ECO;
import com.bjsasc.plm.core.change.ECOMaster;
import com.bjsasc.plm.core.doc.Document;
import com.bjsasc.plm.core.doc.DocumentMaster;
import com.bjsasc.plm.core.part.Part;
import com.bjsasc.plm.core.part.PartMaster;
import com.bjsasc.plm.core.part.PartService;
import com.bjsasc.plm.core.part.link.PartUsageLink;
import com.bjsasc.plm.core.persist.model.Persistable;
import com.bjsasc.plm.core.system.principal.User;
import com.bjsasc.plm.core.type.TypeHelper;
import com.bjsasc.plm.core.util.StringUtil;
import com.cascc.avidm.util.UUIDService;

public class A3A5DataConvertUtil {
	//����ת���� A3��������ClassName
	public static final String dataMapClassName = Map.class.getName();
	
	public static final String orderClassName = "com.bjsasc.avidm.datacenter.order.model.DmcOrder";

	public static final String orderTargetClassName = "com.bjsasc.avidm.datacenter.order.model.DmcOrderTarget";

	public static final String dmcObjectClassName = "com.bjsasc.avidm.datacenter.order.model.DmcObject";

	public static final String orderReceiverClassName = "com.bjsasc.avidm.datacenter.order.model.DmcOrderReceiver";
	
	public static final String taskClassName = "com.bjsasc.avidm.datacenter.task.model.DmcTask";
	
	public static final String dmcSendDocbindClassName = "com.bjsasc.avidm.datacenter.signature.model.DmcSendDocBind";
	
	public static final String taskSignatureClassName = "com.bjsasc.avidm.datacenter.task.model.DmcTaskSignature";
	
	public static final String dmcDispatchClassName = "com.bjsasc.avidm.datacenter.distribute.model.DmcDispatch";
	
	public static final String dmcTaskDataBindClassName = "com.bjsasc.avidm.datacenter.distribute.model.DmcTaskDataBind";
	//����ת����id��ʶ��
	public static final String ORDERIID = "orderIID";
	
	public static final String CREATORIID = "creatorIID";
	public static final String CREATORNAME = "creatorName";
	
	public static final String PRODUCTIID = "productIID";
	public static final String PRODUCTID = "productID";
	public static final String PRODUCTNAME = "productName";
	
	public static final String DOMAINIID = "domainIID";
	
	public static final String ENDTIME = "endTime";
	
	public static final String TARGETIID = "targetIID";
	public static final String IID = "IID";
	public static final String DISPATCHNUM = "dispatchNum";
	public static final String TASKIID = "taskIID";
	public static final String DEALUSERID = "dealUserID";
	public static final String DEALUSERIID = "dealUserIID";
	public static final String DEALUSERNAME = "dealUserName";
	public static final String TASKSTATE = "taskState";

	/**
	 * ����״̬-�½�
	 */
	public static final int DISPATCH_DEALSTATE_NEW = 0;

	/**
	 * ��������״̬-������
	 */
	public static final int DMCORDER_FLOWSTATE_DEALING = 6;
	
	/**
	 * ������������-�ĵ�
	 */
	public static final String DMCOBJECT_TYPE_DOC = "_Doc";

	/**
	 * ������������-������
	 */
	public static final String DMCOBJECT_TYPE_ITEM = "_Item";
	/**
	 * ������������-�ṹ��
	 */
	public static final String DMCOBJECT_TYPE_ORDER = "_Order";

	/**
	 * ���ݽ�����״̬-�½�
	 */
	public static final int DMCRECEIVER_DEALSTATE_NEW = 0;
	
	/**
	 * ���ݽ�����״̬-������
	 */
	public static final int DMCRECEIVER_DEALSTATE_SENDING = 1;
	
	/**
	 * ���ݽ�����״̬-�����
	 */
	public static final int DMCRECEIVER_DEALSTATE_ADDING = 2;
	
	/**
	 * ���ݽ�����״̬-ɾ����
	 */
	public static final int DMCRECEIVER_DEALSTATE_DELING = 3;
	
	/**
	 * ���ݽ�����״̬-�ѽ���
	 */
	public static final int DMCRECEIVER_DEALSTATE_ACCPTED = 4;

	/**
	 * �������ʶ-������
	 */
	public static final String TASK_PARENTIID_ROOT = "-1";
	
	/**
	 * �����Ƿ�ת��-��
	 */
	public static final int TASK_ISTRANSMIT_YES = 1;
	
	/**
	 * �����Ƿ�ת��-��
	 */
	public static final int TASK_ISTRANSMIT_NO = 0;
	
	/**
	 * �Ƿ�ȴ�������-��
	 */
	public static final int TASK_ISWAITCHILDTASK_YES = 1;
	
	/**
	 * �Ƿ�ȴ�������-��
	 */
	public static final int TASK_ISWAITCHILDTASK_NO = 0;
	
	/**
	 * �Ƿ�����-��
	 */
	public static final int TASK_ISLEADUP_YES = 1;
	
	/**
	 * �Ƿ�����-��
	 */
	public static final int TASK_ISLEADUP_NO = 0;
	
	/**
	 * �Ƿ���������-��
	 */
	public static final int TASK_ISFLOWOVER_YES = 1;
	
	/**
	 * �Ƿ���������-��
	 */
	public static final int TASK_ISFLOWOVER_NO = 0;
	
	/**
	 * ����״̬-������
	 */
	public static final int TASK_STATE_WAITDEAL = 0;
	
	/**
	 * ת��ģʽ-����ת��
	 */
	public static int TASK_FORWARDMODE_PARALLEL = 0;
	
	/**
	 * ת��ģʽ-����ת��
	 */
	public static int TASK_FORWARDMODE_FLOW = 1;
	
	/**
	 * ����Ȩ��--��������
	 */
	public static final int ISCANDEAL_TRUE=0; 
	/**
	 * ����Ȩ��--���ܿ�������
	 */
	public static final int ISCANDEAL_FALSE=1; 
	/**
	 * �����������-����
	 */
	public static String TASK_MINDTYPE_PERSON = "person";
	
	/**
	 * �����������-��λ
	 */
	public static String TASK_MINDTYPE_DIVISION = "division";
	
	/**
	 * �Ƿ���Ҫ��λ���-��
	 */
	public static int TASK_NEEDDIVMIND_NO = 0;
	
	/**
	 * �Ƿ���Ҫ��λ���-��
	 */
	public static int TASK_NEEDDIVMIND_YES = 1;
	
	/**
	 * �Ƿ�ͬ��-��
	 */
	public static int TASK_ISAGREE_NO = 0;
	
	/**
	 * �Ƿ�ͬ��-��
	 */
	public static int TASK_ISAGREE_YES = 1;
	
	/**
	 * �Ƿ�ͬ�� -��
	 */
	public static int TASK_ISAGREE_EMPTY = -1;

	/**
	 * �ַ���ʽ-����
	 */
	public static final int DISPATCH_TYPE_ADD = 0;

	/**
	 * �ַ���ʽ-ɾ��
	 */
	public static final int DISPATCH_TYPE_DELETE = 1;

	/**
	 * �ַ���ʽ-����
	 */
	public static final int DISPATCH_TYPE_CANCEL = 2;
	
	/**
	 * �ַ�ҵ������-�·�
	 */
	public static final int DISPATCH_BUSTYPE_NEW = 0;

	/**
	 * �ַ�ҵ������-����
	 */
	public static final int DISPATCH_BUSTYPE_ADD = 1;

	/**
	 * �ַ�ҵ������-���ĺ�ַ�
	 */
	public static final int DISPATCH_BUSTYPE_CHANGE = 2;

	/**
	 * Description�ֶ�
	 * �ַ�ҵ������-����
	 */
	public static final String DISPATCH_TYPE_ADD_NORMAL = "normal";

	/**
	 * �ַ�ҵ������-��ʷ��ѡȡ
	 */
	public static final String DISPATCH_TYPE_ADD_HISTORY = "history";
	/**
	 * ����ַ�
	 */
	public static final String BUSINESS_TYPE_DISTRIBUTE = "DISTRIBUTE";

	/**
	 * ���ӷַ�
	 */
	public static final String TYPE_NO_DISPATCH = "DISPATCH";
	
	/**
	 * ʵ��ַ�
	 */
	public static final String TYPE_NO_DISTRIBUTE = "DISTRIBUTE";
	/**
	 * ǩ��״̬-0:δǩ��
	 */
	public static final int SIGNATURE_STATE_WAITSIGN = 0;
	
	/**
	 * ǩ��״̬-1:��ǩ��
	 */
	public static final int SIGNATURE_STATE_SIGNED = 1;
	
	/**
	 * ǩ��״̬-2:����Ҫǩ��
	 */
	public static final int SIGNATURE_STATE_ENDWITHNOSIGN = 2;
	
//	//A3�ж���ĳ��� ע�͵�����
//	/**
//	 * ����״̬-������
//	 */
//		public static final int TASK_STATE_WAITDEAL = 0;
//
//	/**
//	 * ����״̬-����������
//	 */
//		public static final int TASK_STATE_CHILDDEAL = 1;
//	
//	/**
//	 * ����״̬-�����������
//	 */
//		public static final int TASK_STATE_CHILDEND = 2;
//	
//	/**
//	 * ����״̬-���񱻴��
//	 */
//		public static final int TASK_STATE_REJECTED = 3;
//	
//	/**
//	 * ����״̬-�ȴ���λ���
//	 */
//		public static final int TASK_STATE_WAITAGREE = 4;
	
	/**
	 * ����״̬-��������
	 */
	public static final int TASK_STATE_SUSPEND = 5;
	
	/**
	 * ����״̬-����ɾ��
	 */
	public static final int TASK_STATE_DELETE = 6;
	
	/**
	 * ����״̬-�����ɾ��
	 */
	public static final int TASK_STATE_DELETEMIND = 7;
	
	/**
	 * ����״̬-���������
	 */
	public static final int TASK_STATE_DEALEND = 8;
	
//	/**
//	 * ����״̬-��������������,��Ҫ��λ���
//	 */
//		public static final int TASK_STATE_SUSPEND_WAITAGREE = 9;
	
	/**
	 * ����״̬-�����������Ȳ���Ҫ��λ��������������
	 */
	public static final int TASK_STATE_SUSPEND_END = 10;
	
	/**
	 * ����״̬-ǿ�ƽ���
	 */
	public static final int TASK_STATE_FORCE_END = 11;
	
//	/**
//	 * ����״̬-���д���������ȴ���
//	 */
//		public static final int TASK_STATE_FLOWWAIT = 12;
	
	/**
	 * �����,��û��ǩ�������״̬
	 */
	public static final int[] TASK_END_NOSIGN_STATE = { TASK_STATE_SUSPEND,
		TASK_STATE_SUSPEND_END,
		TASK_STATE_DELETE,
		TASK_STATE_DELETEMIND,
		TASK_STATE_FORCE_END};
	
	/**
	 * �����״̬
	 */
	public static final int[] TASK_END_STATE = { TASK_STATE_SUSPEND,
		TASK_STATE_SUSPEND_END,
		TASK_STATE_DELETE,
		TASK_STATE_DELETEMIND,
		TASK_STATE_DEALEND,
		TASK_STATE_FORCE_END};

	/**
	 * ������������-���ӷַ�
	 */
	public static final String DMC_ORDER_TYPE_NAME = "���ӷַ�";
	
	
	private static DataConvertConfigService dataConvertCfgService = DataConvertConfigHelper.getService();
//	private static DmcValueMappingConfigService valMappingCfgService = DmcValueMappingConfigHelper.getService();
	
	/**
	 * �ж϶����Ƿ��Ƿ���A3.5�Ķ���
	 * 
	 * @param object Persistable
	 * @return
	 */
	public boolean isSendToA3Obj(Persistable object) {
		boolean isSendObjFlag = false;
		String classId = object.getClassId();
		if (classId.equals(Document.class.getSimpleName())
				|| classId.equals(Part.class.getSimpleName())
				|| classId.equals(ECO.class.getSimpleName())) {
			// �ǿ��Է��͵Ķ��󣬼�������
			isSendObjFlag = true;
		}
		return isSendObjFlag;
	}
	
	/**
	 * @param a5order
	 * @param sourceSite
	 * @param typeNo
	 * @return DataMap
	 */
	public static DataMap makeA3DmcOrderMsgDataMap(DistributeOrder a5order, Site sourceSite, String typeNo) {
		DataMap dataMap = new DataMap();
		dataMap.setClassName(orderClassName);
		
		dataMap.put(ORDERIID, a5order.getInnerId());
		dataMap.put("orderID", a5order.getNumber());
		dataMap.put("orderName", a5order.getName());
		dataMap.put("orderDescription", a5order.getNote());
		dataMap.put(CREATORIID, a5order.getManageInfo().getCreateBy().getAaUserInnerId());
		dataMap.put("creatorID", a5order.getManageInfo().getCreateBy().getNumber());
		dataMap.put(CREATORNAME, a5order.getManageInfo().getCreateBy().getName());
		dataMap.put("createTime", a5order.getManageInfo().getCreateTime() + "");

		//
		dataMap.put("typeNo", typeNo);
//		TypeService typeManager = Helper.getTypeService().getAttrValue("orderType", target);
		//���ӷַ���ʵ��ַ����ڵ���������˵�����ڵ��ӷַ�����
		dataMap.put("typeName", DMC_ORDER_TYPE_NAME);
		
		//����������id(ȷ��:A5��A3�ò�����
		//20150910��ţ����ȷ�ϣ���ʱ�����ӷַ�����DISPATCH����ʵ��ַ�����DISTRIBUTE��)
		dataMap.put("subTypeID", typeNo);
		//A3��Ӧ���½�����������ʱ��"1:�½�"
		dataMap.put("flowState", DMCORDER_FLOWSTATE_DEALING + "");

		//(ȷ��:A5��A3�ò���)
		dataMap.put("orderURL", "");
		
		dataMap.put(PRODUCTIID, a5order.getContextInfo().getContextRef().getInnerId());
		dataMap.put(PRODUCTID, a5order.getContextInfo().getContext().getNumber());
		dataMap.put(PRODUCTNAME, a5order.getContextInfo().getContext().getName());
		dataMap.put(DOMAINIID, sourceSite.getSiteData().getInnerId());
		dataMap.put("domainID", sourceSite.getSiteData().getSiteNo());
		dataMap.put("domainName", sourceSite.getSiteData().getSiteName());
		
		//(ȷ��:��ʱû�� ����collaboration����ҪparseInt��ʱ����ǰʱ��)
		dataMap.put(ENDTIME, System.currentTimeMillis()+"");
		dataMap.put("msgGroupIID", a5order.getInnerId());
		
		return dataMap;
	}

	/**
	 * @param a5order
	 * @param a5info
	 * @param targetSite
	 * @return DataMap
	 */
	public static DataMap makeA3DmcOrderTargetMsgDataMap(DistributeOrder a5order, DistributeInfo a5info, Site targetSite) {
		DataMap dataMap = new DataMap();
		dataMap.setClassName(orderTargetClassName);

		//(ȷ��:����DmcOrderTarget�ǵ�λ�Ķ���������ʱ�÷ַ���Ϣ��id�趨)
		dataMap.put(TARGETIID, a5info.getInnerId());
		
		dataMap.put(ORDERIID, a5order.getInnerId());
		dataMap.put(DOMAINIID, targetSite.getSiteData().getInnerId());
		dataMap.put("domainID", targetSite.getSiteData().getSiteNo());
		dataMap.put("domainName", targetSite.getSiteData().getSiteName());
		//(ȷ��:1:�½�)
		dataMap.put("state", "1");
		//(ȷ��:��ţ����ȷ�ϵ����������λ�ж����˴����������ݣ���������ΪĬ�ϵ�1)
		dataMap.put(DISPATCHNUM, 1 + "");
		//(ȷ��:�����ⲿ��λ)
		dataMap.put("isExtenalSys", "0");
		//(ȷ��:A5��A3�ò���)
		dataMap.put("passportURL", "");
		//(ȷ��:��ʱû�� ����collaboration����ҪparseInt��ʱ����ǰʱ��)
		dataMap.put(ENDTIME, System.currentTimeMillis()+"");
		dataMap.put("msgGroupIID", a5order.getInnerId());

		return dataMap;
	}

	/**
	 * @param a5order
	 * @param a5object
	 * @param targetSite
	 * @return DataMap
	 */
	public static DataMap makeA3DmcObjectMsgDataMap(DistributeOrder a5order, Object a5object,
			DistributeObject a5disObj, Map<String, String> partsParentIIDMap, Site targetSite, String typeNo) {
		//ֱ����ֱ����a5Object��������ԣ�����Ҫת��
		//���ģ�ȡ�õ��뷽������վ�㣩�İ汾�����ǽ��շ��İ汾,Ȼ����ݰ汾ȥת��һЩ�ֶ�
		//����ģʽʱ��ҵ������
		DataMap dataMap = new DataMap();
		dataMap.setClassName(dmcObjectClassName);
		
		Persistable a5persist = (Persistable)a5object;
		//String a5objectInnerId = a5persist.getInnerId();
		String a5objectOid = Helper.getOid(a5persist.getClassId(), a5persist.getInnerId());

		String convertOID = getConvertOID(a5persist, targetSite);
		if(convertOID != null && !"".equals(convertOID)){
			dataMap.put("objectIID", convertOID);
		} else {
			dataMap.put("objectIID", a5objectOid);
		}
		
		String docGlobalIID = "";
		String objectID = "";
		String objectName = "";
		String objectType = "";
		String productIID = "";
		String productID = "";
		String productName = "";
		String objectState = "";
		String objectVerIID = "";
		String creatorName = "";
		String creatorIID = "";
		String versionNo = "";
		String type = "";
		String classType = "";
		String parentIID = "-1";
		
		if(a5persist instanceof Document){
			Document obj = (Document)a5persist;
			docGlobalIID = obj.getMaster().getInnerId();
			objectID = ((DocumentMaster)obj.getMaster()).getNumber();
			objectName = ((DocumentMaster)obj.getMaster()).getName();
			objectType = TypeHelper.getService().getType(obj.getClassId()).getName();
			productIID = obj.getContextInfo().getContextRef().getInnerId();
			productID = obj.getContextInfo().getContext().getNumber();
			productName = obj.getContextInfo().getContext().getName();
			objectState = obj.getLifeCycleInfo().getStateName();
			// �ĵ���VersionIIDһ��Ҫת������ֵ����Ϊnull��
			if("�ܿ���".equals(objectState)){
				objectVerIID = dataConvertCfgService.getA4xSendObjIID(DTSiteConstant.DTSITE_APPVERSION_3_5, a5objectOid, A4XObjTypeConstant.OBJTYPE_CONTROLDOCUMENT);
			} else {
				objectVerIID = dataConvertCfgService.getA4xSendObjIID(DTSiteConstant.DTSITE_APPVERSION_3_5, a5objectOid, A4XObjTypeConstant.OBJTYPE_DMDOCUMENT);
			}
			versionNo = obj.getIterationInfo().getFullVersionNo();
			creatorName = obj.getManageInfo().getCreateBy().getName();
			creatorIID = obj.getManageInfo().getCreateBy().getAaUserInnerId();
			parentIID = "-1";
			type = DMCOBJECT_TYPE_DOC;
			classType = "com.cascc.avidm.docman.model.DmDocument";
		}else if(a5persist instanceof Part){
			Part obj = (Part)a5persist;
			docGlobalIID = obj.getMaster().getInnerId();
			objectID = ((PartMaster)obj.getMaster()).getNumber();
			objectName = ((PartMaster)obj.getMaster()).getName();
			objectType = TypeHelper.getService().getType(obj.getClassId()).getName();
			productIID = obj.getContextInfo().getContextRef().getInnerId();
			productID = obj.getContextInfo().getContext().getNumber();
			productName = obj.getContextInfo().getContext().getName();
			objectState = obj.getLifeCycleInfo().getStateName();
			//ȷ�ϣ���ʱ���ղ��ô���
			objectVerIID = convertOID;
			versionNo = obj.getIterationInfo().getFullVersionNo();
			creatorName = obj.getManageInfo().getCreateBy().getName();
			creatorIID = obj.getManageInfo().getCreateBy().getAaUserInnerId();
			//����A3������Ҫ�����ĸ��ӹ�ϵ��������ֶ���Ҫ��ֵ
			parentIID = partsParentIIDMap.get(obj.getInnerId());
			type = DMCOBJECT_TYPE_ITEM;
			classType = "com.cascc.avidm.prodstruct.model.itemmgr.VerVo";
		}else if(a5persist instanceof ECO){
			ECO obj = (ECO)a5persist;
			docGlobalIID = obj.getMaster().getInnerId();
			objectID = ((ECOMaster)obj.getMaster()).getNumber();
			objectName = ((ECOMaster)obj.getMaster()).getName();
			objectType = TypeHelper.getService().getType(obj.getClassId()).getName();
			productIID = obj.getContextInfo().getContextRef().getInnerId();
			productID = obj.getContextInfo().getContext().getNumber();
			productName = obj.getContextInfo().getContext().getName();
			objectState = obj.getLifeCycleInfo().getStateName();
			//ȷ�ϣ���ʱ���ղ��ô���
			objectVerIID = "";
			versionNo = obj.getIterationInfo().getFullVersionNo();
			creatorName = obj.getManageInfo().getCreateBy().getName();
			creatorIID = obj.getManageInfo().getCreateBy().getAaUserInnerId();
			parentIID = "-1";
			type = DMCOBJECT_TYPE_ORDER;
			//3.5������£���classTypeֵ����ת������ֵ��A5ϵͳû��
			//����A3�ĸ��ĵ����������Ͳ���������classType�ǲ�һ����
			if(convertOID != null && !"".equals(convertOID)){
				classType = "com.cascc.avidm.prodstruct.model.approve.change.PcChangeOrderVo";
			}else{
				classType = "com.bjsasc.avidm.changeman.web.form.ChangeOrderForm";
			}
		}else if(a5persist instanceof CADDocument){
			CADDocument obj = (CADDocument)a5persist;
			docGlobalIID = obj.getMaster().getInnerId();
			objectID = ((CADDocumentMaster)obj.getMaster()).getNumber();
			objectName = ((CADDocumentMaster)obj.getMaster()).getName();
			objectType = TypeHelper.getService().getType(obj.getClassId()).getName();
			productIID = obj.getContextInfo().getContextRef().getInnerId();
			productID = obj.getContextInfo().getContext().getNumber();
			productName = obj.getContextInfo().getContext().getName();
			objectState = obj.getLifeCycleInfo().getStateName();
			if("�ܿ���".equals(objectState)){
				objectVerIID = dataConvertCfgService.getA4xSendObjIID(DTSiteConstant.DTSITE_APPVERSION_3_5, a5objectOid, A4XObjTypeConstant.OBJTYPE_CONTROLDOCUMENT);
			} else {
				objectVerIID = dataConvertCfgService.getA4xSendObjIID(DTSiteConstant.DTSITE_APPVERSION_3_5, a5objectOid, A4XObjTypeConstant.OBJTYPE_DMDOCUMENT);
			}
			versionNo = obj.getIterationInfo().getFullVersionNo();
			creatorName = obj.getManageInfo().getCreateBy().getName();
			creatorIID = obj.getManageInfo().getCreateBy().getAaUserInnerId();
			parentIID = "-1";
			type = DMCOBJECT_TYPE_DOC;
			classType = "com.cascc.avidm.docman.model.DmDocument";
		}
		//IID��A3��û�к���ֻ��һ��Ψһ��ʶ,����A3��û�в���
		dataMap.put(IID, UUIDService.getUUID());
		
		dataMap.put("objectGlobalIID", docGlobalIID);
		dataMap.put("objectID", objectID );
		
		dataMap.put("objectName", objectName);
		dataMap.put("objectType", objectType);
		dataMap.put(PRODUCTIID, productIID);
		dataMap.put(PRODUCTID, productID);
		dataMap.put(PRODUCTNAME, productName);
		dataMap.put("objectState", objectState);
		dataMap.put("objectVerIID", objectVerIID);
		dataMap.put("versionNo", versionNo);
		
		dataMap.put(CREATORNAME, creatorName);
		dataMap.put(CREATORIID, creatorIID);
		dataMap.put(ORDERIID, a5order.getInnerId());
		dataMap.put("parentIID", parentIID);
		dataMap.put("type", type);
		dataMap.put("classType", classType);
		dataMap.put("msgGroupIID", a5order.getInnerId());

		return dataMap;
	}
	
	/**
	 * �ڱ��ε����Ķ����б��л�ȡ���в����ĸ�����IID
	 * 
	 * @param disObjList
	 * @return partsParentIIDMap
	 */
	public static  Map<String,String> getPartsParentIIDMap(List<DistributeObject> disObjList) {
		Map<String,String> partsParentIIDMap = new HashMap<String,String>(); 
		List<Part> partList = new ArrayList<Part>(); 
		PartService partService = Helper.getPartService();
		for(DistributeObject disObj : disObjList) {
			Persistable dataObj = Helper.getPersistService().getObject(disObj.getDataClassId(), disObj.getDataInnerId());
			if(dataObj instanceof Part) {
				partList.add((Part)dataObj);
			}
		}
		for(Part part : partList) {
			List<PartUsageLink> usageLinks = partService.getPartUsageLinksByTo(part.getPartMaster());
			boolean hasParent = false;
			for(PartUsageLink link:usageLinks){
				Part parentPart = (Part)link.getFromObject();
				if(partList.contains(parentPart)){
					partsParentIIDMap.put(part.getInnerId(), parentPart.getInnerId());
					break;
				}
			}
			if(!hasParent){
				partsParentIIDMap.put(part.getInnerId(), "-1");
			}
		}
		return partsParentIIDMap;
	}
	/**
	 * @param a5order
	 * @param a5info
	 * @param targetSite
	 * @return DataMap
	 */
	public static DataMap makeA3DmcOrderReceiverMsgDataMap(DistributeOrder a5order, DistributeInfo a5info, Site targetSite) {
		DataMap dataMap = new DataMap();

		dataMap.setClassName(orderReceiverClassName);
		//����A5û�д˶����IID�÷ַ���Ϣ��id�趨
		dataMap.put(IID, a5info.getInnerId());
		dataMap.put(TARGETIID, targetSite.getInnerId());
		dataMap.put(DOMAINIID, targetSite.getSiteData().getInnerId());

		User receiver = (User) Helper.getPersistService()
				.getObject(a5info.getInfoClassId() + ":" + a5info.getDisInfoId());
		
		dataMap.put("receiverIID", receiver.getAaUserInnerId());
		dataMap.put("receiverID", a5info.getDisInfoId());
		dataMap.put("receiverName", a5info.getDisInfoName());
		dataMap.put(ORDERIID, a5order.getInnerId());
		dataMap.put("dealState", DMCRECEIVER_DEALSTATE_NEW + "");
		dataMap.put("msgGroupIID", a5order.getInnerId());

		return dataMap;
	}
	
	/**
	 * ȡ�÷ַ���Ϣ�з��ŵĶ�������
	 * 
	 * @author zhangguoqiang, 2016-1-4
	 * @param a5order ������Ϣ
	 * @return DataMap
	 */
	public static Persistable getDisObjectByDisInfo(List<DistributeOrderObjectLink> orderObjLinkList,
			 List<Object> objectList, DistributeInfo a5info) {
		Persistable a5persist = null;
		
		Map<String, DistributeOrderObjectLink>a5OrderObjLinkMap = new HashMap<String, DistributeOrderObjectLink>();
		Map<String, Object> a5ObjMap = new HashMap<String, Object>();
		for (DistributeOrderObjectLink orderObjLink : orderObjLinkList) {
			a5OrderObjLinkMap.put(orderObjLink.getInnerId(),orderObjLink);
		}
		for (Object obj : objectList) {
			a5ObjMap.put(((Persistable)obj).getInnerId(),obj);
		}
		DistributeOrderObjectLink a5OrderObjLink = a5OrderObjLinkMap.get(a5info.getDisOrderObjLinkId());
		DistributeObject a5disObj = (DistributeObject)a5OrderObjLink.getTo();
		String a5objDataInnerId = a5disObj.getDataInnerId();
		Object a5object = a5ObjMap.get(a5objDataInnerId);
		a5persist = (Persistable)a5object;
		return a5persist;
	}
	/**
	 * DmcDispatchת��ΪDataMap
	 * 
	 * @author zhangguoqiang, 2015-4-20
	 * @param a5order ������Ϣ
	 * @return DataMap
	 */
	public static DataMap makeA3DmcDispatchMsgDataMap(DistributeOrder a5order, List<DistributeOrderObjectLink> orderObjLinkList,
			 List<Object> objectList, DistributeInfo a5info, Site targetSite) {
        DataMap dataMap = new DataMap();
		
		dataMap.setClassName(dmcDispatchClassName);

		String objectGlobalIID = "";
//		Map<String, DistributeOrderObjectLink>a5OrderObjLinkMap = new HashMap<String, DistributeOrderObjectLink>();
//		Map<String, Object> a5ObjMap = new HashMap<String, Object>();
//		for (DistributeOrderObjectLink orderObjLink : orderObjLinkList) {
//			a5OrderObjLinkMap.put(orderObjLink.getInnerId(),orderObjLink);
//		}
//		for (Object obj : objectList) {
//			a5ObjMap.put(((Persistable)obj).getInnerId(),obj);
//		}
////		
//		DistributeOrderObjectLink a5OrderObjLink = a5OrderObjLinkMap.get(a5info.getDisOrderObjLinkId());
//		DistributeObject a5disObj = (DistributeObject)a5OrderObjLink.getTo();
//		String a5objDataInnerId = ((DistributeObject)a5disObj).getDataInnerId();
//		Object a5object = a5ObjMap.get(a5objDataInnerId);
//		Persistable a5persist = (Persistable)a5object;
		Persistable a5persist = getDisObjectByDisInfo(orderObjLinkList,objectList,a5info);
		
		if (a5persist instanceof Document) {
			Document obj = (Document)a5persist;
			objectGlobalIID = obj.getMaster().getInnerId();
		}else if (a5persist instanceof Part) {
			Part obj = (Part)a5persist;
			objectGlobalIID = obj.getMaster().getInnerId();
		}else if (a5persist instanceof ECO) {
			ECO obj = (ECO)a5persist;
			objectGlobalIID = obj.getMaster().getInnerId();
		}else if (a5persist instanceof CADDocument){
			CADDocument obj = (CADDocument)a5persist;
			objectGlobalIID = obj.getMaster().getInnerId();
			dataMap.put("type", DMCOBJECT_TYPE_DOC);
		}

		dataMap.put("IID", a5info.getInnerId());
		dataMap.put("orderIID", a5order.getInnerId() );
//		dataMap.put("objectIID", a5objDataInnerId);
		String a5objectOid = Helper.getOid(a5persist.getClassId(), a5persist.getInnerId());
		String convertOID = getConvertOID(a5persist, targetSite);
		if(convertOID != null && !"".equals(convertOID)){
			dataMap.put("objectIID", convertOID);
		} else {
			dataMap.put("objectIID", a5objectOid);
		}
		
		dataMap.put("objectGlobalIID", objectGlobalIID);
		dataMap.put("targetDomainIID", targetSite.getSiteData().getInnerId());
		dataMap.put("targetDomainName", targetSite.getSiteData().getSiteName());
		
		//TODO A3�ࣨ0:�·�:1:����;2:���ĺ�ַ�����Ҫ��һ��ת��
		dataMap.put("busType", a5info.getDisType());
		//�·�
		dataMap.put("dispatchType", DISPATCH_TYPE_ADD + "");
		dataMap.put("description", DISPATCH_TYPE_ADD_NORMAL);
		dataMap.put("dispatchNum", a5info.getDisInfoNum() + "");
		dataMap.put("dispatchPath", "");
		//���ӷַ� ֻ�����½�
		dataMap.put("dealState", DISPATCH_DEALSTATE_NEW + "");
		dataMap.put("conclusion", "");
		dataMap.put("createTime", a5info.getManageInfo().getCreateTime() + "");
		//(ȷ��:��ʱû�� ����collaboration����ҪparseInt��ʱ����ǰʱ��)
		dataMap.put("startTime", System.currentTimeMillis() + "");
		dataMap.put("endTime", System.currentTimeMillis() + "");
		dataMap.put("msg", System.currentTimeMillis() + "");
		dataMap.put("msgGroupIID", a5order.getInnerId());

		return dataMap;
	}

	/**
	 * DmcTaskת��ΪDataMap
	 * 
	 * @author zhangguoqiang, 2015-4-20
	 * @param a5order ������Ϣ
	 * @return
	 */
	public static DataMap makeA3DmcTaskMsgDataMap(Site sourceSite, Site targetSite,  
			String orderIID, DistributeOrder disOrder, DistributeElecTask a5task,String dealUserOid) {
        DataMap dataMap = new DataMap();
		dataMap.setClassName(taskClassName);
		dataMap.put(TASKIID, a5task.getInnerId());
		dataMap.put("taskName", a5task.getName());
		dataMap.put("sendDomainIID", targetSite.getInnerId());
		dataMap.put("sendDomainID", targetSite.getSiteData().getSiteNo());
		dataMap.put("sendDomainName", targetSite.getSiteData().getSiteName());
		//TODO ���û��ͬ���û����ܻ�ȡ�õ� UserService.getUser�д���������û�
		dataMap.put("sendUserIID", disOrder.getManageInfo().getCreateBy().getAaUserInnerId());
		dataMap.put("sendUserID", disOrder.getManageInfo().getCreateBy().getNumber());
		dataMap.put("sendUserName", disOrder.getManageInfo().getCreateBy().getName());
		dataMap.put("dealDomainIID", sourceSite.getInnerId());
		dataMap.put("dealDomainID", sourceSite.getSiteData().getSiteNo());
		dataMap.put("dealDomainName", sourceSite.getSiteData().getSiteName());
		if(!StringUtil.isStringEmpty(dealUserOid)){
			User dealUser = (User) Helper.getPersistService().getObject(dealUserOid);
			dataMap.put(DEALUSERIID, dealUser.getAaUserInnerId());
			dataMap.put(DEALUSERID, dealUser.getNumber());
			dataMap.put(DEALUSERNAME, dealUser.getName());
		}else{
			dataMap.put(DEALUSERIID, "");
			dataMap.put(DEALUSERID, "");
			dataMap.put(DEALUSERNAME, "");
		}
		dataMap.put("parTaskIID", TASK_PARENTIID_ROOT);
		
		dataMap.put("taskType", BUSINESS_TYPE_DISTRIBUTE);
		dataMap.put("forwardMode", TASK_FORWARDMODE_PARALLEL+ "");
		dataMap.put("isTranSmit", TASK_ISTRANSMIT_YES + "");
		dataMap.put("isWaitChildTask", TASK_ISWAITCHILDTASK_YES + "");
		dataMap.put("isLeadUp", TASK_ISLEADUP_YES + "");
		dataMap.put("isFlowOver", TASK_ISFLOWOVER_NO + "");
		dataMap.put("startTime", System.currentTimeMillis() + "");
		dataMap.put(ENDTIME, System.currentTimeMillis() + "");
		dataMap.put("flowTaskStep", 0 + "");
		dataMap.put(TASKSTATE, TASK_STATE_WAITDEAL+ "");
		
		dataMap.put("taskRemark", "");
		dataMap.put(PRODUCTIID, disOrder.getContextInfo().getContextRef().getInnerId());
		dataMap.put(PRODUCTID, disOrder.getContextInfo().getContext().getNumber());
		dataMap.put(PRODUCTNAME, disOrder.getContextInfo().getContext().getName());
		dataMap.put(ORDERIID, orderIID);
		
		return dataMap;
	}

	/**
	 * ǩ����Ϣת��ΪDataMap
	 * 
	 * @author zhangguoqiang, 2015-4-20
	 * @param a5order ������Ϣ
	 * @return
	 */
	public static DataMap makeA3DmcTaskSignatureMsgDataMap(Site sourceSite, 
			String orderIID, String receiverOid, String receiveTime, String returnReason, 
			 String opt, DistributeElecTask a5task) {
		Site selfSite = SiteHelper.getSiteService().findLocalSiteInfo();
        DataMap dataMap = new DataMap();
		dataMap.setClassName(taskSignatureClassName);
		int isAgree = -1;
		if ("PROMOTE".equals(opt)) {
			isAgree = TASK_ISAGREE_YES;
		} else if ("REJECT".equals(opt)) {
			isAgree = TASK_ISAGREE_NO;
		}
		//����A5ϵͳ��ǩ�յ�ʱ��û��ǩ���������ͬ���ʱ���������id��ΪΨһid������A3
		dataMap.put("signatrueIID", a5task.getInnerId());
		dataMap.put(TASKIID, a5task.getInnerId());

		if(!StringUtil.isStringEmpty(receiverOid)){
			User receiver = (User) Helper.getPersistService()
					.getObject(receiverOid);
			dataMap.put(DEALUSERIID, receiver.getAaUserInnerId());
			dataMap.put(DEALUSERID, receiver.getNumber());
			dataMap.put(DEALUSERNAME, receiver.getName());
			dataMap.put("dealDevisionIID", receiver.getOrganization().getAaOrgInnerId());
			dataMap.put("dealDevisionID", receiver.getOrganization().getNumber());
			dataMap.put("dealDevisionName", receiver.getOrganization().getName());
		}
		//�����˽�ɫ���Բ��趨
		dataMap.put("dealRole", "");
		dataMap.put("isAgree", isAgree + "");
		dataMap.put("dealMind", returnReason);
		dataMap.put("mindType", TASK_MINDTYPE_PERSON);
		dataMap.put("isNeedDevisionMind", TASK_NEEDDIVMIND_NO + "");
		dataMap.put("signatureTime", receiveTime);
		dataMap.put(DOMAINIID, selfSite.getInnerId());
		dataMap.put(ORDERIID, orderIID);
		
		return dataMap;
	}
	/**
	 * DmcTaskDataBindת��ΪDataMap
	 * 
	 * @author zhangguoqiang, 2015-4-20
	 * @param a5order ������Ϣ
	 * @return
	 */
	public static DataMap makeA3DmcTaskDataBindMsgDataMap(Site targetSite, String orderIID, DistributeElecTask a5task,
			DistributeObject disObj, DistributeInfo disInfo) {
        DataMap dataMap = new DataMap();
		dataMap.setClassName(dmcTaskDataBindClassName);
		dataMap.put(IID, UUIDService.getUUID());
		dataMap.put(ORDERIID, orderIID);
		dataMap.put(TASKIID, a5task.getInnerId());
		//dataMap.put("objectIID", disObj.getDataInnerId());
		Persistable a5persist = Helper.getPersistService().getObject(disObj.getDataClassId(), disObj.getDataInnerId());
		//����ǿ���ĵ��ӷַ���ʱ������ȡ�����ַ���������,������Ҫ���Ӷ�a5persist��null�ж�
		if(a5persist != null){
			String a5objectOid = Helper.getOid(a5persist.getClassId(), a5persist.getInnerId());

			String convertOID = getConvertOID(a5persist, targetSite);
			if(convertOID != null && !"".equals(convertOID)){
				dataMap.put("objectIID", convertOID);
			} else {
				dataMap.put("objectIID", a5objectOid);
			}
		}else{
			dataMap.put("objectIID", disObj.getDataInnerId());
		}
		
		dataMap.put("objectName", disObj.getName());
		//�ַ���Ϣ�Ƿ����û��Ĳ��Ҵ��ڷַ��û���id��ʱ���趨�û���Ϣ
		if(User.CLASSID.equals(disInfo.getInfoClassId())
				&& !StringUtil.isStringEmpty(disInfo.getDisInfoId())){
			User dealUser = (User) Helper.getPersistService().getObject(
					disInfo.getInfoClassId(),disInfo.getDisInfoId());
			dataMap.put("userIID", dealUser.getAaUserInnerId());
			dataMap.put("userName", dealUser.getName());
		}else{
			//���������ʱ����A5���Ƿ�����֯�ģ�û���û���Ϣ��ֻ��ĳһ���û�ǩ���˲��ܵõ��û���Ϣ
			dataMap.put("userIID", "");
			dataMap.put("userName", "");
		}
		//�Ƿ��������ת��Ȩ�ޣ����ڣ�
		dataMap.put("isCandeal", ISCANDEAL_FALSE + "");
		dataMap.put("createTime", a5task.getManageInfo().getCreateTime()+ "");
		return dataMap;
	}
	
	/**
	 * List<DataMap> תΪJSON�ַ���
	 */
	public static String listDataMapToJson(List<DataMap> list){
		
		StringBuilder str = new StringBuilder("[");
		for(int i=0; i<list.size(); i++){
			DataMap map = list.get(i);
			str.append(dataMapToJson(map));
			if(i < list.size()-1){
				str.append(",");
			}
		}
		str.append("]");
		return str.toString();
	}
	
	/**
	 * DataMap תΪJSON�ַ���
	 */
	public static String dataMapToJson(DataMap map){
		StringBuilder str = new StringBuilder("{");
		Iterator iter = map.iterator();
		while(iter.hasNext()){
			String key = (String)iter.next();
			str.append("\"").append(key).append("\":\"").append(map.get(key)).append("\"");
			str.append(",");
		}
		str.delete(str.length() - 1,str.length());
		str.append("}");
		return str.toString();
	}

	/**
	 * ��Map�е�Value(����������ƴ�ӵ�JSON��)ת��Ϊ��Ӧ��DataMap����
	 * @param Map<key, value>
	 * ex: key ����Ϊ��������
	 *     value Ϊ����������ƴ�ӵ�JSON��
	 */
	public static <T extends Object> Map<String, Object> mapToDatamap(Map<String, T> map) throws Exception{
		Map<String, Object> objectMap = new HashMap<String, Object>();
		Iterator<Entry<String, T>>  iter = map.entrySet().iterator();
		while(iter.hasNext()){
			Entry<String,T> entry = iter.next();
			String key = entry.getKey();
			Object value = entry.getValue();
			Object object = jsonToDataMapOrListDataMap(key, value);
			objectMap.put(key, object);
		}
		return objectMap;
	}
	
	/**
	 * JSONObject��JSONArrayת��ΪObject
	 * @param
	 *     className ��������
	 *     objStr    ����JSON��
	 */
	private static Object jsonToDataMapOrListDataMap(String className, Object objStr) throws Exception{
		if(objStr instanceof String){
			String str = objStr.toString();
			//����ַ����� "["��ͷ�����ʾ��ΪJSONArray
			//��"{"��ͷ�����ʾ��ΪJSONObject
			//���������Ե���key value
			if(str.startsWith("[")){
				return jsonArrayToDatamap(className, str);
			}else if(str.startsWith("{")){
				return jsonDatamapToDatamap(className, str);
			}else{
				return objStr;
			}
		}else{
			return objStr;
		}
	}

	/**
	 * ����������ƴ�ӵ�JSON�� ת��ΪObject
	 * @param
	 *     className ��������
	 *     objStr    ����JSON��
	 */
	private static List jsonArrayToDatamap(String className, String objStr) throws Exception{
		List list = new ArrayList();
		JSONArray jsonArray = JSONArray.fromObject(objStr);
		for(int i=0; i<jsonArray.size(); i++){
			Object obj = jsonArray.get(i);
			list.add(jsonDatamapToDatamap(className, obj.toString()));
		}
		return list;
	}
	
	/**
	 * ����������ƴ�ӵ�JSON�� ת��ΪObject
	 * @param
	 *     className ��������
	 *     objStr    ����JSON��
	 */
	private static DataMap jsonDatamapToDatamap(String className, String objStr) throws Exception{
		DataMap dataMap = new DataMap();
		dataMap.setClassName(className);
		JSONObject jsonObj = JSONObject.fromObject(objStr);
		Iterator iter = jsonObj.entrySet().iterator();
		while(iter.hasNext()){
			Entry entry = (Entry)iter.next();
			String key = entry.getKey().toString();
			String value= entry.getValue().toString();
			dataMap.put(key, value);
		}
		return dataMap;
	}
	
	/**
	 * ȡ��ʱ���longֵ�������Ϊ�ջ��߲���ת����long��ʱ�䣬����ϵͳʱ��
	 * @param
	 *     timeStr    ʱ���ַ���
	 */
	public static Long parseTimeStrToLong(String timeStr){
		long time= -1L;
		try {
			time = Long.parseLong(timeStr);
		} catch (Exception e) {
			time = System.currentTimeMillis();
		}

		return time;
	}
	
	/**
	 * ȡ���ַ�����Ӧ��intֵ�������Ϊ�ջ��߲���ת����int�ͣ�����-1
	 * @param
	 *     intStr    ��ֵ�ַ���
	 */
	public static int parseStrToInt(String str){
		int time= -1;
		try {
			time = Integer.parseInt(str);
		} catch (Exception e) {
			time = -1;
		}
		return time;
	}
	
	/**
	 * ����A3������״̬��ǩ����Ϣ������ȡ��ǩ��״̬
	 * @param
	 *     String    ����״̬
	 *     boolean   �����Ƿ���ڶ�Ӧ��ǩ����Ϣ
	 */
	public static int getSigStateByTaskStateAndSigInfo(int taskState,boolean isExistSigInfo){
		int signatureState = -1;

		if(isExistSigInfo){
			signatureState = SIGNATURE_STATE_SIGNED;
		}else{
			if(isInStateSet(taskState, TASK_END_NOSIGN_STATE)){
				signatureState = SIGNATURE_STATE_ENDWITHNOSIGN;
			}else if(isInStateSet(taskState, TASK_END_STATE)){
				signatureState = SIGNATURE_STATE_SIGNED;
			}else{
				signatureState = SIGNATURE_STATE_WAITSIGN;
			}
		}

		return signatureState;
	}
	
	
	/**
	 * �ж�����״̬�Ƿ���״̬����
	 * 
	 * @param taskState
	 * @param taskStateSet
	 * @return
	 */
	private static boolean isInStateSet(int taskState, int[] taskStateSet) {
		boolean ret = false;	
		for(int i = 0; i < taskStateSet.length; i++) {
			if(taskState == taskStateSet[i]) {
				ret = true;
				break;
			}
		}
		return ret;
	}
	
	
	/**
	 * ȡ�÷ַ����ݶ���
	 * 
	 * @param orderIID
	 * @return     List<Persistable>
	 */
	public static List<Persistable> getOutSiteDisObjs(String orderIID) {

		List<Persistable> outSiteDisObjs = new ArrayList<Persistable>();
		//ȡ�����A5���ŵ���������Link�����б�
		DistributeInfoService disInfoService = DistributeHelper.getDistributeInfoService();
		List<DistributeInfo> disInfoList = disInfoService.getDistributeInfosByDistributeOrderInnerId(orderIID);
		List<String> disobjIDList = new ArrayList<String>();
		if(disInfoList != null && disInfoList.size() > 0){
			for(DistributeInfo disinfo : disInfoList){
				//�������:����ַ���Ϣ
				if(ConstUtil.C_DISMEDIATYPE_2.equals(disinfo.getDisMediaType())){
					//ȡ��A5���ŵ��뷢�����ݶ���Link
					DistributeOrderObjectLink disOrderObjLink = (DistributeOrderObjectLink)Helper.getPersistService()
							.getObject(disinfo.getDisOrderObjLinkClassId(),disinfo.getDisOrderObjLinkId());
					//ȡ��A5�������ݶ���
					DistributeObject disObj = (DistributeObject)disOrderObjLink.getTo();
					if(!disobjIDList.contains(disObj.getInnerId())){
						disobjIDList.add(disObj.getInnerId());
						outSiteDisObjs.add(disObj);
					}
				}
			}
		}
		return outSiteDisObjs;
	}
	
	/**
	 * ȡ�÷ַ����ݶ���
	 * 
	 * @param orderIID
	 * @param domainIID
	 * @return     List<Persistable>
	 */
	public static List<Persistable> getOutSiteDisObjs(String orderIID,String domainIID) {

		List<Persistable> outSiteDisObjs = new ArrayList<Persistable>();
		//ȡ�����A5���ŵ���������Link�����б�
		DistributeInfoService disInfoService = DistributeHelper.getDistributeInfoService();
		List<DistributeInfo> disInfoList = disInfoService.getDistributeInfosByDistributeOrderInnerId(orderIID);
		List<String> disobjIDList = new ArrayList<String>();
		if(disInfoList != null && disInfoList.size() > 0){
			for(DistributeInfo disinfo : disInfoList){
				//�������:����ַ���Ϣ
				if(ConstUtil.C_DISMEDIATYPE_2.equals(disinfo.getDisMediaType()) && domainIID.equals(disinfo.getDisInfoId())){
					//ȡ��A5���ŵ��뷢�����ݶ���Link
					DistributeOrderObjectLink disOrderObjLink = (DistributeOrderObjectLink)Helper.getPersistService()
							.getObject(disinfo.getDisOrderObjLinkClassId(),disinfo.getDisOrderObjLinkId());
					//ȡ��A5�������ݶ���
					DistributeObject disObj = (DistributeObject)disOrderObjLink.getTo();
					if(!disobjIDList.contains(disObj.getInnerId())){
						disobjIDList.add(disObj.getInnerId());
						outSiteDisObjs.add(disObj);
					}
				}
			}
		}
		return outSiteDisObjs;
	}
	
	/** ����A5��Ϊ���𷽷��͵�A3�Ժ�A3������������Ϣ��
	 * ͳһ��¼A3��ǩ����Ϣ������ݣ���ǩ����Ϣ���ٻ���չʾ��
	 * @param orderIID
	 * @param domainIID
	 * @param taskList
	 * @param signInfoList
	 * @param isFromDCFlag
	 */
	public static void dealA3ReplyMessage(String orderIID, String domainIID, List<DataMap> taskList,
			List<DataMap> signInfoList) {

		DcA3TaskSignatureService dcA3TaskSignatureService = DistributeHelper.getDcA3TaskSignatureService();
		DcA3SigObjectLinkService dcA3SigObjectLinkService = DistributeHelper.getDcA3SigObjectLinkService();
		
		Map<String, DataMap> signInfoDataHashMap = new HashMap<String, DataMap>();
		if(signInfoList != null && signInfoList.size() > 0){
			for(int i = 0; i < signInfoList.size(); i++){
				DataMap tempDataMap =  (DataMap)signInfoList.get(i);
				signInfoDataHashMap.put((String)tempDataMap.get(TASKIID), tempDataMap);
			}
		}
		//���յ�������taskIID�б������ж��Ƿ�ɾ������
		List<String> existTaskIIDList = new ArrayList<String>();
		
		for(int i = 0; i < taskList.size(); i++){
			DataMap taskDataMap = (DataMap)taskList.get(i);
			String taskIID = taskDataMap.get(TASKIID);
			//���յ�������taskIID�б������ж��Ƿ�ɾ������
			existTaskIIDList.add(taskIID);
			DcA3TaskSignature dcA3TaskSignature = dcA3TaskSignatureService.getDcA3TaskSignatureByTaskId(taskIID);
			boolean isUpdateFlag = false;
			if(dcA3TaskSignature == null){
				dcA3TaskSignature = dcA3TaskSignatureService.newDcA3TaskSignature();
			}else{
				isUpdateFlag = true;
			}
			//-----------------A3�������������Ϣʱ�趨���� start--------------------
			String dealDomainIID = taskDataMap.get("dealDomainIID");
			dcA3TaskSignature.setDomainIID(dealDomainIID);
			dcA3TaskSignature.setOrderIID((String)taskDataMap.get("orderIID"));
			dcA3TaskSignature.setTaskIID(taskIID);
			dcA3TaskSignature.setStartTime(parseTimeStrToLong((String)taskDataMap.get("startTime")));
			//ǩ��״̬(0:δǩ��;1:��ǩ��;2:����Ҫǩ��)
			int taskState = parseStrToInt((String)taskDataMap.get("taskState"));
			boolean isExistTaskSigInfo = signInfoDataHashMap.containsKey(taskIID);
			int signatureState = getSigStateByTaskStateAndSigInfo(taskState, isExistTaskSigInfo);
			dcA3TaskSignature.setSignatureState(signatureState);
			dcA3TaskSignature.setDealUserIID((String)taskDataMap.get("dealUserIID"));
			dcA3TaskSignature.setDealUserID((String)taskDataMap.get("dealUserID"));
			dcA3TaskSignature.setDealUserName((String)taskDataMap.get("dealUserName"));
			//-----------------A3�������������Ϣʱ�趨���� end--------------------
			//-----------------A3����ǩ����Ϣʱ�趨���� start--------------------
			if(signInfoDataHashMap.containsKey(taskIID)){
				DataMap taskSignatureDataMap = signInfoDataHashMap.get(taskIID);
				dcA3TaskSignature.setSignatureTime(A3A5DataConvertUtil.parseTimeStrToLong(taskSignatureDataMap.get("signatureTime")));
				dcA3TaskSignature.setDealMind((String)taskSignatureDataMap.get("dealMind"));
				dcA3TaskSignature.setMindType((String)taskSignatureDataMap.get("mindType"));
				dcA3TaskSignature.setDealRole((String)taskSignatureDataMap.get("dealRole"));
				//ǩ��Ĵ����˺�����Ĵ�������һ��������û�б�Ҫ������
//				dcA3TaskSignature.setDealUserIID((String)taskSignatureDataMap.get("dealUserIID"));
//				dcA3TaskSignature.setDealUserID((String)taskSignatureDataMap.get("dealUserID"));
//				dcA3TaskSignature.setDealUserName((String)taskSignatureDataMap.get("dealUserName"));
				dcA3TaskSignature.setIsAgree(A3A5DataConvertUtil.parseStrToInt(taskSignatureDataMap.get("isAgree")));
				dcA3TaskSignature.setIsNeedDevisionMind(A3A5DataConvertUtil.parseStrToInt(taskSignatureDataMap.get("isNeedDevisionMind")));
				dcA3TaskSignature.setDealDevisionID((String)taskSignatureDataMap.get("dealDevisionIID"));
				dcA3TaskSignature.setDealDevisionIID((String)taskSignatureDataMap.get("dealDevisionIID"));
				dcA3TaskSignature.setDealDevisionName((String)taskSignatureDataMap.get("dealDevisionName"));
			}else{
				//ɾ��ǩ����������,���ǩ�������
				if(TASK_STATE_DELETEMIND == taskState){
					dcA3TaskSignature.setSignatureTime(0);
					dcA3TaskSignature.setDealMind("");
					dcA3TaskSignature.setMindType("");
					dcA3TaskSignature.setDealRole("");
					//ǩ��Ĵ����˺�����Ĵ�������һ��������û�б�Ҫ������
//					dcA3TaskSignature.setDealUserIID("");
//					dcA3TaskSignature.setDealUserID("");
//					dcA3TaskSignature.setDealUserName("");
					dcA3TaskSignature.setIsAgree(0);
					dcA3TaskSignature.setIsNeedDevisionMind(0);
					dcA3TaskSignature.setDealDevisionID("");
					dcA3TaskSignature.setDealDevisionIID("");
					dcA3TaskSignature.setDealDevisionName("");
				}
			}
			
			//-----------------A3����ǩ����Ϣʱ�趨����  end--------------------
			//����
			if(isUpdateFlag){
				dcA3TaskSignatureService.updateDcA3TaskSignature(dcA3TaskSignature);
			}else{//�½���ͬʱ��Ҫ������������ǩ����Ϣ��ǩ��ҵ������Link
				if(dcA3TaskSignature.getSignatureState() == SIGNATURE_STATE_WAITSIGN 
						|| dcA3TaskSignature.getSignatureState() == SIGNATURE_STATE_ENDWITHNOSIGN){
					dcA3TaskSignature.setIsAgree(TASK_ISAGREE_EMPTY);
				}
				dcA3TaskSignatureService.addDcA3TaskSignature(dcA3TaskSignature);
				
				//ȡ�÷��ŵ������Ŀ���ҵ������б����Դ�����������ǩ����Ϣ��ǩ��ҵ������Link
				List<Persistable> outSiteDisObjs =  new ArrayList<Persistable>();
				//�жϵ�ǰ�����Ƿ�Ϊ����
				DCSiteAttribute dcSiteAttr = DCSiteAttributeHelper.getDCSiteAttrService()
						.findDcSiteAttrByDTSiteId(dealDomainIID);
				String isSiteControl = dcSiteAttr.getIsSiteControl();
				if("true".equalsIgnoreCase(isSiteControl)){
					outSiteDisObjs = A3A5DataConvertUtil.getOutSiteDisObjs(orderIID);
				}else{
					outSiteDisObjs = A3A5DataConvertUtil.getOutSiteDisObjs(orderIID,dealDomainIID);
				}
				
				List<DcA3SigObjectLink> dcA3SigObjectLinkList = new ArrayList<DcA3SigObjectLink>();
				for(Persistable outSiteDisObj : outSiteDisObjs){
					DcA3SigObjectLink dcA3SigObjectLink = dcA3SigObjectLinkService.newDcA3SigObjectLink();
					dcA3SigObjectLink.setFromObject(dcA3TaskSignature);
					dcA3SigObjectLink.setToObject(outSiteDisObj);
					dcA3SigObjectLinkList.add(dcA3SigObjectLink);
				}
				if(dcA3SigObjectLinkList.size() > 0){
					dcA3SigObjectLinkService.addDcA3SigObjectLinkList(dcA3SigObjectLinkList);
				}
			}
		}
		
		//���շ�����ɾ������Ĵ�����Ϊ�������ͬ��������ݡ�
		List<DcA3TaskSignature> dcA3TaskSignatureList = 
				dcA3TaskSignatureService.listDcA3TaskSignatureByDomainIID(orderIID,domainIID);
		
		for(DcA3TaskSignature taskSignature : dcA3TaskSignatureList){
			if(!existTaskIIDList.contains(taskSignature.getTaskIID())){
				List<DcA3SigObjectLink> linkList = 
						dcA3SigObjectLinkService.getDcA3SigObjectLinkByTaskSigIID(taskSignature.getInnerId());
				dcA3SigObjectLinkService.deleteDcA3SigObjectLinkList(linkList);
				dcA3TaskSignatureService.deleteDcA3TaskSignature(taskSignature);
			}
		}
	}
	
	/**
	 * ����A5�ķַ�������Ϣȡ�ö�Ӧ�������汾�ķַ����ݵ�ObjectIID
	 * ���ȡ�����򷵻�NULL
	 * 
	 * @param a5persist
	 * @param targetSite
	 * @return     String 
	 */
	public static String getConvertOID( Persistable a5persist, Site targetSite) {

		String convertOID = null;
		// ��ʱ��Ҷ��������Ľ����ֻ��ʵ���ҵ������,��Ϊ�����õ�ʱ��ֻ������һ������,û������ʵ��͵���
		String businessType = A3A5DataConvertUtil.TYPE_NO_DISTRIBUTE;
		String	dcVersion = ImportVersionCfgHelper.getImportVersionCfgService().findCfgByTargetAndType(targetSite.getInnerId(), businessType).getSystemVersion();
		
		String a5objectOid = Helper.getOid(a5persist.getClassId(), a5persist.getInnerId());

		if(DTSiteConstant.DTSITE_APPVERSION_3_5.equals(dcVersion)){
			if(a5persist instanceof Document){
				Document tmpDoc = (Document)a5persist;
				if("�ܿ���".equals(tmpDoc.getLifeCycleInfo().getStateName())){
					convertOID = dataConvertCfgService.getA4xSendObjIID(DTSiteConstant.DTSITE_APPVERSION_3_5, a5objectOid, A4XObjTypeConstant.OBJTYPE_CONTROLDOCUMENT);
				}else{
					convertOID = dataConvertCfgService.getA4xSendObjIID(DTSiteConstant.DTSITE_APPVERSION_3_5, a5objectOid, A4XObjTypeConstant.OBJTYPE_DMDOCUMENT);
				}
			}else if(a5persist instanceof Part){
				convertOID = dataConvertCfgService.getA4xSendObjIID(DTSiteConstant.DTSITE_APPVERSION_3_5, a5objectOid, A4XObjTypeConstant.OBJTYPE_ITEM);
			}else if(a5persist instanceof ApproveOrder){
				convertOID = dataConvertCfgService.getA4xSendObjIID(DTSiteConstant.DTSITE_APPVERSION_3_5, a5objectOid, A4XObjTypeConstant.OBJTYPE_APPROVEORDER);
			}else if(a5persist instanceof ECO){
				convertOID = dataConvertCfgService.getA4xSendObjIID(DTSiteConstant.DTSITE_APPVERSION_3_5, a5objectOid, A4XObjTypeConstant.OBJTYPE_ECO);
			}else if(a5persist instanceof CADDocument){
				CADDocument obj = (CADDocument)a5persist;
				if("�ܿ���".equals(obj.getLifeCycleInfo().getStateName())){
					convertOID = dataConvertCfgService.getA4xSendObjIID(DTSiteConstant.DTSITE_APPVERSION_3_5, a5objectOid, A4XObjTypeConstant.OBJTYPE_CONTROLDOCUMENT);
				} else {
					convertOID = dataConvertCfgService.getA4xSendObjIID(DTSiteConstant.DTSITE_APPVERSION_3_5, a5objectOid, A4XObjTypeConstant.OBJTYPE_DMDOCUMENT);
				}
			}
		}else if(DTSiteConstant.DTSITE_APPVERSION_4.equals(dcVersion)){
			if(a5persist instanceof Document){
				Document tmpDoc = (Document)a5persist;
				if("�ܿ���".equals(tmpDoc.getLifeCycleInfo().getStateName())){
					convertOID = dataConvertCfgService.getA4xSendObjIID(DTSiteConstant.DTSITE_APPVERSION_4, a5objectOid, A4XObjTypeConstant.OBJTYPE_CONTROLDOCUMENT);
				}else{
					convertOID = dataConvertCfgService.getA4xSendObjIID(DTSiteConstant.DTSITE_APPVERSION_4, a5objectOid, A4XObjTypeConstant.OBJTYPE_DMDOCUMENT);
				}
			}else if(a5persist instanceof Part){
				convertOID = dataConvertCfgService.getA4xSendObjIID(DTSiteConstant.DTSITE_APPVERSION_4, a5objectOid, A4XObjTypeConstant.OBJTYPE_ITEM);
			}else if(a5persist instanceof ApproveOrder){
				convertOID = dataConvertCfgService.getA4xSendObjIID(DTSiteConstant.DTSITE_APPVERSION_4, a5objectOid, A4XObjTypeConstant.OBJTYPE_APPROVEORDER);
			}else if(a5persist instanceof ECO){
				convertOID = dataConvertCfgService.getA4xSendObjIID(DTSiteConstant.DTSITE_APPVERSION_4, a5objectOid, A4XObjTypeConstant.OBJTYPE_ECO);
			}else if(a5persist instanceof CADDocument){
				CADDocument obj = (CADDocument)a5persist;
				if("�ܿ���".equals(obj.getLifeCycleInfo().getStateName())){
					convertOID = dataConvertCfgService.getA4xSendObjIID(DTSiteConstant.DTSITE_APPVERSION_4, a5objectOid, A4XObjTypeConstant.OBJTYPE_CONTROLDOCUMENT);
				} else {
					convertOID = dataConvertCfgService.getA4xSendObjIID(DTSiteConstant.DTSITE_APPVERSION_4, a5objectOid, A4XObjTypeConstant.OBJTYPE_DMDOCUMENT);
				}
			}
		}
		return convertOID;
	}
	
//	private String getA35OrderState(String stateName) {
//		String statevalue = "0";
//		String stateVal = valMappingCfgService.findDmcValMapping(DmcValMappingCfgConstant.OBJECT_TYPE_ORDER, DmcValMappingCfgConstant.ATTRTYPE_STATE, stateName, DTSiteConstant.DTSITE_APPVERSION_5, DTSiteConstant.DTSITE_APPVERSION_3_5);
//		if(!stateVal.equals("") && stateVal!=null){
//			statevalue = stateVal;
//		}
//		return statevalue;
//	}
//	
//
}
