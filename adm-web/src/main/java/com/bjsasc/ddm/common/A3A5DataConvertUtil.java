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
	//数据转换用 A3数据类型ClassName
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
	//数据转换用id标识名
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
	 * 处理状态-新建
	 */
	public static final int DISPATCH_DEALSTATE_NEW = 0;

	/**
	 * 单据流程状态-处理中
	 */
	public static final int DMCORDER_FLOWSTATE_DEALING = 6;
	
	/**
	 * 单据数据类型-文档
	 */
	public static final String DMCOBJECT_TYPE_DOC = "_Doc";

	/**
	 * 单据数据类型-配置项
	 */
	public static final String DMCOBJECT_TYPE_ITEM = "_Item";
	/**
	 * 单据数据类型-结构项
	 */
	public static final String DMCOBJECT_TYPE_ORDER = "_Order";

	/**
	 * 单据接收人状态-新建
	 */
	public static final int DMCRECEIVER_DEALSTATE_NEW = 0;
	
	/**
	 * 单据接收人状态-发送中
	 */
	public static final int DMCRECEIVER_DEALSTATE_SENDING = 1;
	
	/**
	 * 单据接收人状态-添加中
	 */
	public static final int DMCRECEIVER_DEALSTATE_ADDING = 2;
	
	/**
	 * 单据接收人状态-删除中
	 */
	public static final int DMCRECEIVER_DEALSTATE_DELING = 3;
	
	/**
	 * 单据接收人状态-已接收
	 */
	public static final int DMCRECEIVER_DEALSTATE_ACCPTED = 4;

	/**
	 * 父任务标识-根任务
	 */
	public static final String TASK_PARENTIID_ROOT = "-1";
	
	/**
	 * 任务是否转发-是
	 */
	public static final int TASK_ISTRANSMIT_YES = 1;
	
	/**
	 * 任务是否转发-否
	 */
	public static final int TASK_ISTRANSMIT_NO = 0;
	
	/**
	 * 是否等待子任务-是
	 */
	public static final int TASK_ISWAITCHILDTASK_YES = 1;
	
	/**
	 * 是否等待子任务-否
	 */
	public static final int TASK_ISWAITCHILDTASK_NO = 0;
	
	/**
	 * 是否抢先-是
	 */
	public static final int TASK_ISLEADUP_YES = 1;
	
	/**
	 * 是否抢先-否
	 */
	public static final int TASK_ISLEADUP_NO = 0;
	
	/**
	 * 是否流程任务-是
	 */
	public static final int TASK_ISFLOWOVER_YES = 1;
	
	/**
	 * 是否流程任务-否
	 */
	public static final int TASK_ISFLOWOVER_NO = 0;
	
	/**
	 * 任务状态-待处理
	 */
	public static final int TASK_STATE_WAITDEAL = 0;
	
	/**
	 * 转发模式-并行转发
	 */
	public static int TASK_FORWARDMODE_PARALLEL = 0;
	
	/**
	 * 转发模式-串行转发
	 */
	public static int TASK_FORWARDMODE_FLOW = 1;
	
	/**
	 * 下载权限--可以下载
	 */
	public static final int ISCANDEAL_TRUE=0; 
	/**
	 * 下载权限--不能可以下载
	 */
	public static final int ISCANDEAL_FALSE=1; 
	/**
	 * 任务意见类型-个人
	 */
	public static String TASK_MINDTYPE_PERSON = "person";
	
	/**
	 * 任务意见类型-单位
	 */
	public static String TASK_MINDTYPE_DIVISION = "division";
	
	/**
	 * 是否需要单位意见-否
	 */
	public static int TASK_NEEDDIVMIND_NO = 0;
	
	/**
	 * 是否需要单位意见-是
	 */
	public static int TASK_NEEDDIVMIND_YES = 1;
	
	/**
	 * 是否同意-否
	 */
	public static int TASK_ISAGREE_NO = 0;
	
	/**
	 * 是否同意-是
	 */
	public static int TASK_ISAGREE_YES = 1;
	
	/**
	 * 是否同意 -空
	 */
	public static int TASK_ISAGREE_EMPTY = -1;

	/**
	 * 分发方式-新增
	 */
	public static final int DISPATCH_TYPE_ADD = 0;

	/**
	 * 分发方式-删除
	 */
	public static final int DISPATCH_TYPE_DELETE = 1;

	/**
	 * 分发方式-作废
	 */
	public static final int DISPATCH_TYPE_CANCEL = 2;
	
	/**
	 * 分发业务类型-新发
	 */
	public static final int DISPATCH_BUSTYPE_NEW = 0;

	/**
	 * 分发业务类型-补发
	 */
	public static final int DISPATCH_BUSTYPE_ADD = 1;

	/**
	 * 分发业务类型-更改后分发
	 */
	public static final int DISPATCH_BUSTYPE_CHANGE = 2;

	/**
	 * Description字段
	 * 分发业务类型-增加
	 */
	public static final String DISPATCH_TYPE_ADD_NORMAL = "normal";

	/**
	 * 分发业务类型-历史中选取
	 */
	public static final String DISPATCH_TYPE_ADD_HISTORY = "history";
	/**
	 * 跨域分发
	 */
	public static final String BUSINESS_TYPE_DISTRIBUTE = "DISTRIBUTE";

	/**
	 * 电子分发
	 */
	public static final String TYPE_NO_DISPATCH = "DISPATCH";
	
	/**
	 * 实体分发
	 */
	public static final String TYPE_NO_DISTRIBUTE = "DISTRIBUTE";
	/**
	 * 签署状态-0:未签署
	 */
	public static final int SIGNATURE_STATE_WAITSIGN = 0;
	
	/**
	 * 签署状态-1:已签署
	 */
	public static final int SIGNATURE_STATE_SIGNED = 1;
	
	/**
	 * 签署状态-2:不需要签署
	 */
	public static final int SIGNATURE_STATE_ENDWITHNOSIGN = 2;
	
//	//A3中定义的常量 注释掉备用
//	/**
//	 * 任务状态-待处理
//	 */
//		public static final int TASK_STATE_WAITDEAL = 0;
//
//	/**
//	 * 任务状态-子任务处理中
//	 */
//		public static final int TASK_STATE_CHILDDEAL = 1;
//	
//	/**
//	 * 任务状态-子任务处理完成
//	 */
//		public static final int TASK_STATE_CHILDEND = 2;
//	
//	/**
//	 * 任务状态-任务被打回
//	 */
//		public static final int TASK_STATE_REJECTED = 3;
//	
//	/**
//	 * 任务状态-等待单位意见
//	 */
//		public static final int TASK_STATE_WAITAGREE = 4;
	
	/**
	 * 任务状态-任务被抢先
	 */
	public static final int TASK_STATE_SUSPEND = 5;
	
	/**
	 * 任务状态-任务被删除
	 */
	public static final int TASK_STATE_DELETE = 6;
	
	/**
	 * 任务状态-意见被删除
	 */
	public static final int TASK_STATE_DELETEMIND = 7;
	
	/**
	 * 任务状态-任务处理完成
	 */
	public static final int TASK_STATE_DEALEND = 8;
	
//	/**
//	 * 任务状态-任务被子任务抢先,需要单位意见
//	 */
//		public static final int TASK_STATE_SUSPEND_WAITAGREE = 9;
	
	/**
	 * 任务状态-被子任务抢先并需要单位意见的任务处理完成
	 */
	public static final int TASK_STATE_SUSPEND_END = 10;
	
	/**
	 * 任务状态-强制结束
	 */
	public static final int TASK_STATE_FORCE_END = 11;
	
//	/**
//	 * 任务状态-串行处理子任务等待中
//	 */
//		public static final int TASK_STATE_FLOWWAIT = 12;
	
	/**
	 * 已完成,但没有签署意见的状态
	 */
	public static final int[] TASK_END_NOSIGN_STATE = { TASK_STATE_SUSPEND,
		TASK_STATE_SUSPEND_END,
		TASK_STATE_DELETE,
		TASK_STATE_DELETEMIND,
		TASK_STATE_FORCE_END};
	
	/**
	 * 已完成状态
	 */
	public static final int[] TASK_END_STATE = { TASK_STATE_SUSPEND,
		TASK_STATE_SUSPEND_END,
		TASK_STATE_DELETE,
		TASK_STATE_DELETEMIND,
		TASK_STATE_DEALEND,
		TASK_STATE_FORCE_END};

	/**
	 * 单据类型名称-电子分发
	 */
	public static final String DMC_ORDER_TYPE_NAME = "电子分发";
	
	
	private static DataConvertConfigService dataConvertCfgService = DataConvertConfigHelper.getService();
//	private static DmcValueMappingConfigService valMappingCfgService = DmcValueMappingConfigHelper.getService();
	
	/**
	 * 判断对象是否是发往A3.5的对象
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
			// 是可以发送的对象，继续处理
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
		//电子分发和实体分发对于单据类型来说都属于电子分发类型
		dataMap.put("typeName", DMC_ORDER_TYPE_NAME);
		
		//单据子类型id(确认:A5发A3用不到，
		//20150910跟牛建义确认，暂时传电子分发：“DISPATCH”，实体分发：“DISTRIBUTE”)
		dataMap.put("subTypeID", typeNo);
		//A3中应该新建单据所以暂时传"1:新建"
		dataMap.put("flowState", DMCORDER_FLOWSTATE_DEALING + "");

		//(确认:A5发A3用不到)
		dataMap.put("orderURL", "");
		
		dataMap.put(PRODUCTIID, a5order.getContextInfo().getContextRef().getInnerId());
		dataMap.put(PRODUCTID, a5order.getContextInfo().getContext().getNumber());
		dataMap.put(PRODUCTNAME, a5order.getContextInfo().getContext().getName());
		dataMap.put(DOMAINIID, sourceSite.getSiteData().getInnerId());
		dataMap.put("domainID", sourceSite.getSiteData().getSiteNo());
		dataMap.put("domainName", sourceSite.getSiteData().getSiteName());
		
		//(确认:暂时没用 由于collaboration侧需要parseInt暂时传当前时间)
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

		//(确认:由于DmcOrderTarget是单位的对象所以暂时用分发信息的id设定)
		dataMap.put(TARGETIID, a5info.getInnerId());
		
		dataMap.put(ORDERIID, a5order.getInnerId());
		dataMap.put(DOMAINIID, targetSite.getSiteData().getInnerId());
		dataMap.put("domainID", targetSite.getSiteData().getSiteNo());
		dataMap.put("domainName", targetSite.getSiteData().getSiteName());
		//(确认:1:新建)
		dataMap.put("state", "1");
		//(确认:跟牛建义确认的是在这个单位有多少人处理过这个单据，可以设置为默认的1)
		dataMap.put(DISPATCHNUM, 1 + "");
		//(确认:不是外部单位)
		dataMap.put("isExtenalSys", "0");
		//(确认:A5发A3用不到)
		dataMap.put("passportURL", "");
		//(确认:暂时没用 由于collaboration侧需要parseInt暂时传当前时间)
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
		//直连：直接用a5Object对象的属性，不需要转换
		//中心：取得导入方（中心站点）的版本，而非接收方的版本,然后根据版本去转换一些字段
		//中心模式时的业务类型
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
			// 文档的VersionIID一定要转换，该值不能为null。
			if("受控中".equals(objectState)){
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
			//确认：暂时按照不用处理
			objectVerIID = convertOID;
			versionNo = obj.getIterationInfo().getFullVersionNo();
			creatorName = obj.getManageInfo().getCreateBy().getName();
			creatorIID = obj.getManageInfo().getCreateBy().getAaUserInnerId();
			//由于A3处理需要部件的父子关系所以这个字段需要赋值
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
			//确认：暂时按照不用处理
			objectVerIID = "";
			versionNo = obj.getIterationInfo().getFullVersionNo();
			creatorName = obj.getManageInfo().getCreateBy().getName();
			creatorIID = obj.getManageInfo().getCreateBy().getAaUserInnerId();
			parentIID = "-1";
			type = DMCOBJECT_TYPE_ORDER;
			//3.5的情况下，对classType值进行转换，该值在A5系统没用
			//发往A3的更改单，带部件和不带部件的classType是不一样的
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
			if("受控中".equals(objectState)){
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
		//IID在A3中没有含义只是一个唯一标识,而且A3中没有补发
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
	 * 在本次导出的对象列表中获取所有部件的父部件IID
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
		//由于A5没有此对象的IID用分发信息的id设定
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
	 * 取得分发信息中发放的对象类型
	 * 
	 * @author zhangguoqiang, 2016-1-4
	 * @param a5order 单据信息
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
	 * DmcDispatch转换为DataMap
	 * 
	 * @author zhangguoqiang, 2015-4-20
	 * @param a5order 单据信息
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
		
		//TODO A3侧（0:新发:1:补发;2:更改后分发）需要进一步转换
		dataMap.put("busType", a5info.getDisType());
		//新发
		dataMap.put("dispatchType", DISPATCH_TYPE_ADD + "");
		dataMap.put("description", DISPATCH_TYPE_ADD_NORMAL);
		dataMap.put("dispatchNum", a5info.getDisInfoNum() + "");
		dataMap.put("dispatchPath", "");
		//电子分发 只能是新建
		dataMap.put("dealState", DISPATCH_DEALSTATE_NEW + "");
		dataMap.put("conclusion", "");
		dataMap.put("createTime", a5info.getManageInfo().getCreateTime() + "");
		//(确认:暂时没用 由于collaboration侧需要parseInt暂时传当前时间)
		dataMap.put("startTime", System.currentTimeMillis() + "");
		dataMap.put("endTime", System.currentTimeMillis() + "");
		dataMap.put("msg", System.currentTimeMillis() + "");
		dataMap.put("msgGroupIID", a5order.getInnerId());

		return dataMap;
	}

	/**
	 * DmcTask转换为DataMap
	 * 
	 * @author zhangguoqiang, 2015-4-20
	 * @param a5order 任务信息
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
		//TODO 如果没有同步用户可能会取得到 UserService.getUser中创造的虚拟用户
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
	 * 签署信息转换为DataMap
	 * 
	 * @author zhangguoqiang, 2015-4-20
	 * @param a5order 任务信息
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
		//由于A5系统中签收的时候没有签署意见所以同意的时候用任务的id作为唯一id反馈给A3
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
		//处理人角色可以不设定
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
	 * DmcTaskDataBind转换为DataMap
	 * 
	 * @author zhangguoqiang, 2015-4-20
	 * @param a5order 单据信息
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
		//如果是跨域的电子分发的时候由于取不到分发对象数据,所以需要增加对a5persist的null判断
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
		//分发信息是发给用户的并且存在分发用户的id的时候设定用户信息
		if(User.CLASSID.equals(disInfo.getInfoClassId())
				&& !StringUtil.isStringEmpty(disInfo.getDisInfoId())){
			User dealUser = (User) Helper.getPersistService().getObject(
					disInfo.getInfoClassId(),disInfo.getDisInfoId());
			dataMap.put("userIID", dealUser.getAaUserInnerId());
			dataMap.put("userName", dealUser.getName());
		}else{
			//创建任务的时候在A5中是发给组织的，没有用户信息，只有某一个用户签收了才能得到用户信息
			dataMap.put("userIID", "");
			dataMap.put("userName", "");
		}
		//是否具有下载转发权限（由于）
		dataMap.put("isCandeal", ISCANDEAL_FALSE + "");
		dataMap.put("createTime", a5task.getManageInfo().getCreateTime()+ "");
		return dataMap;
	}
	
	/**
	 * List<DataMap> 转为JSON字符串
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
	 * DataMap 转为JSON字符串
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
	 * 把Map中的Value(对象属性所拼接的JSON串)转换为相应的DataMap对象
	 * @param Map<key, value>
	 * ex: key 必须为对象名称
	 *     value 为对象属性所拼接的JSON串
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
	 * JSONObject与JSONArray转换为Object
	 * @param
	 *     className 对象名称
	 *     objStr    对象JSON串
	 */
	private static Object jsonToDataMapOrListDataMap(String className, Object objStr) throws Exception{
		if(objStr instanceof String){
			String str = objStr.toString();
			//如何字符串以 "["开头，则表示其为JSONArray
			//以"{"开头，则表示其为JSONObject
			//否则，则是以单个key value
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
	 * 对象属性所拼接的JSON串 转换为Object
	 * @param
	 *     className 对象名称
	 *     objStr    对象JSON串
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
	 * 对象属性所拼接的JSON串 转换为Object
	 * @param
	 *     className 对象名称
	 *     objStr    对象JSON串
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
	 * 取得时间的long值如果参数为空或者不能转换成long型时间，返回系统时间
	 * @param
	 *     timeStr    时间字符串
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
	 * 取得字符串对应的int值如果参数为空或者不能转换成int型，返回-1
	 * @param
	 *     intStr    数值字符串
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
	 * 根据A3的任务状态和签署信息的有无取得签署状态
	 * @param
	 *     String    任务状态
	 *     boolean   任务是否存在对应的签署信息
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
	 * 判断任务状态是否在状态集中
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
	 * 取得分发数据对象。
	 * 
	 * @param orderIID
	 * @return     List<Persistable>
	 */
	public static List<Persistable> getOutSiteDisObjs(String orderIID) {

		List<Persistable> outSiteDisObjs = new ArrayList<Persistable>();
		//取得相关A5发放单发放数据Link对象列表
		DistributeInfoService disInfoService = DistributeHelper.getDistributeInfoService();
		List<DistributeInfo> disInfoList = disInfoService.getDistributeInfosByDistributeOrderInnerId(orderIID);
		List<String> disobjIDList = new ArrayList<String>();
		if(disInfoList != null && disInfoList.size() > 0){
			for(DistributeInfo disinfo : disInfoList){
				//处理对象:跨域分发信息
				if(ConstUtil.C_DISMEDIATYPE_2.equals(disinfo.getDisMediaType())){
					//取得A5发放单与发放数据对象Link
					DistributeOrderObjectLink disOrderObjLink = (DistributeOrderObjectLink)Helper.getPersistService()
							.getObject(disinfo.getDisOrderObjLinkClassId(),disinfo.getDisOrderObjLinkId());
					//取得A5发放数据对象
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
	 * 取得分发数据对象。
	 * 
	 * @param orderIID
	 * @param domainIID
	 * @return     List<Persistable>
	 */
	public static List<Persistable> getOutSiteDisObjs(String orderIID,String domainIID) {

		List<Persistable> outSiteDisObjs = new ArrayList<Persistable>();
		//取得相关A5发放单发放数据Link对象列表
		DistributeInfoService disInfoService = DistributeHelper.getDistributeInfoService();
		List<DistributeInfo> disInfoList = disInfoService.getDistributeInfosByDistributeOrderInnerId(orderIID);
		List<String> disobjIDList = new ArrayList<String>();
		if(disInfoList != null && disInfoList.size() > 0){
			for(DistributeInfo disinfo : disInfoList){
				//处理对象:跨域分发信息
				if(ConstUtil.C_DISMEDIATYPE_2.equals(disinfo.getDisMediaType()) && domainIID.equals(disinfo.getDisInfoId())){
					//取得A5发放单与发放数据对象Link
					DistributeOrderObjectLink disOrderObjLink = (DistributeOrderObjectLink)Helper.getPersistService()
							.getObject(disinfo.getDisOrderObjLinkClassId(),disinfo.getDisOrderObjLinkId());
					//取得A5发放数据对象
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
	
	/** 处理A5作为发起方发送到A3以后，A3反馈回来的消息。
	 * 统一记录A3的签署信息相关内容，在签署信息跟踪画面展示用
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
		//接收到的任务taskIID列表，用于判断是否删除任务
		List<String> existTaskIIDList = new ArrayList<String>();
		
		for(int i = 0; i < taskList.size(); i++){
			DataMap taskDataMap = (DataMap)taskList.get(i);
			String taskIID = taskDataMap.get(TASKIID);
			//接收到的任务taskIID列表，用于判断是否删除任务
			existTaskIIDList.add(taskIID);
			DcA3TaskSignature dcA3TaskSignature = dcA3TaskSignatureService.getDcA3TaskSignatureByTaskId(taskIID);
			boolean isUpdateFlag = false;
			if(dcA3TaskSignature == null){
				dcA3TaskSignature = dcA3TaskSignatureService.newDcA3TaskSignature();
			}else{
				isUpdateFlag = true;
			}
			//-----------------A3生成任务后反馈消息时设定内容 start--------------------
			String dealDomainIID = taskDataMap.get("dealDomainIID");
			dcA3TaskSignature.setDomainIID(dealDomainIID);
			dcA3TaskSignature.setOrderIID((String)taskDataMap.get("orderIID"));
			dcA3TaskSignature.setTaskIID(taskIID);
			dcA3TaskSignature.setStartTime(parseTimeStrToLong((String)taskDataMap.get("startTime")));
			//签署状态(0:未签署;1:已签署;2:不需要签署)
			int taskState = parseStrToInt((String)taskDataMap.get("taskState"));
			boolean isExistTaskSigInfo = signInfoDataHashMap.containsKey(taskIID);
			int signatureState = getSigStateByTaskStateAndSigInfo(taskState, isExistTaskSigInfo);
			dcA3TaskSignature.setSignatureState(signatureState);
			dcA3TaskSignature.setDealUserIID((String)taskDataMap.get("dealUserIID"));
			dcA3TaskSignature.setDealUserID((String)taskDataMap.get("dealUserID"));
			dcA3TaskSignature.setDealUserName((String)taskDataMap.get("dealUserName"));
			//-----------------A3生成任务后反馈消息时设定内容 end--------------------
			//-----------------A3反馈签署消息时设定内容 start--------------------
			if(signInfoDataHashMap.containsKey(taskIID)){
				DataMap taskSignatureDataMap = signInfoDataHashMap.get(taskIID);
				dcA3TaskSignature.setSignatureTime(A3A5DataConvertUtil.parseTimeStrToLong(taskSignatureDataMap.get("signatureTime")));
				dcA3TaskSignature.setDealMind((String)taskSignatureDataMap.get("dealMind"));
				dcA3TaskSignature.setMindType((String)taskSignatureDataMap.get("mindType"));
				dcA3TaskSignature.setDealRole((String)taskSignatureDataMap.get("dealRole"));
				//签署的处理人和任务的处理人是一个人所以没有必要再设置
//				dcA3TaskSignature.setDealUserIID((String)taskSignatureDataMap.get("dealUserIID"));
//				dcA3TaskSignature.setDealUserID((String)taskSignatureDataMap.get("dealUserID"));
//				dcA3TaskSignature.setDealUserName((String)taskSignatureDataMap.get("dealUserName"));
				dcA3TaskSignature.setIsAgree(A3A5DataConvertUtil.parseStrToInt(taskSignatureDataMap.get("isAgree")));
				dcA3TaskSignature.setIsNeedDevisionMind(A3A5DataConvertUtil.parseStrToInt(taskSignatureDataMap.get("isNeedDevisionMind")));
				dcA3TaskSignature.setDealDevisionID((String)taskSignatureDataMap.get("dealDevisionIID"));
				dcA3TaskSignature.setDealDevisionIID((String)taskSignatureDataMap.get("dealDevisionIID"));
				dcA3TaskSignature.setDealDevisionName((String)taskSignatureDataMap.get("dealDevisionName"));
			}else{
				//删除签署意见的情况,清空签署的内容
				if(TASK_STATE_DELETEMIND == taskState){
					dcA3TaskSignature.setSignatureTime(0);
					dcA3TaskSignature.setDealMind("");
					dcA3TaskSignature.setMindType("");
					dcA3TaskSignature.setDealRole("");
					//签署的处理人和任务的处理人是一个人所以没有必要再设置
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
			
			//-----------------A3反馈签署消息时设定内容  end--------------------
			//更新
			if(isUpdateFlag){
				dcA3TaskSignatureService.updateDcA3TaskSignature(dcA3TaskSignature);
			}else{//新建的同时需要创建跨域任务签署信息和签署业务对象的Link
				if(dcA3TaskSignature.getSignatureState() == SIGNATURE_STATE_WAITSIGN 
						|| dcA3TaskSignature.getSignatureState() == SIGNATURE_STATE_ENDWITHNOSIGN){
					dcA3TaskSignature.setIsAgree(TASK_ISAGREE_EMPTY);
				}
				dcA3TaskSignatureService.addDcA3TaskSignature(dcA3TaskSignature);
				
				//取得发放单关联的跨域业务对象列表，用以创建跨域任务签署信息和签署业务对象的Link
				List<Persistable> outSiteDisObjs =  new ArrayList<Persistable>();
				//判断当前的域是否为中心
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
		
		//接收方存在删除任务的处理，作为发起方最好同步这个内容。
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
	 * 根据A5的分发数据信息取得对应的其他版本的分发数据的ObjectIID
	 * 如果取不到则返回NULL
	 * 
	 * @param a5persist
	 * @param targetSite
	 * @return     String 
	 */
	public static String getConvertOID( Persistable a5persist, Site targetSite) {

		String convertOID = null;
		// 暂时和叶红军商量的结果是只用实体的业务类型,因为在配置的时候只用了这一种类型,没有区分实体和电子
		String businessType = A3A5DataConvertUtil.TYPE_NO_DISTRIBUTE;
		String	dcVersion = ImportVersionCfgHelper.getImportVersionCfgService().findCfgByTargetAndType(targetSite.getInnerId(), businessType).getSystemVersion();
		
		String a5objectOid = Helper.getOid(a5persist.getClassId(), a5persist.getInnerId());

		if(DTSiteConstant.DTSITE_APPVERSION_3_5.equals(dcVersion)){
			if(a5persist instanceof Document){
				Document tmpDoc = (Document)a5persist;
				if("受控中".equals(tmpDoc.getLifeCycleInfo().getStateName())){
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
				if("受控中".equals(obj.getLifeCycleInfo().getStateName())){
					convertOID = dataConvertCfgService.getA4xSendObjIID(DTSiteConstant.DTSITE_APPVERSION_3_5, a5objectOid, A4XObjTypeConstant.OBJTYPE_CONTROLDOCUMENT);
				} else {
					convertOID = dataConvertCfgService.getA4xSendObjIID(DTSiteConstant.DTSITE_APPVERSION_3_5, a5objectOid, A4XObjTypeConstant.OBJTYPE_DMDOCUMENT);
				}
			}
		}else if(DTSiteConstant.DTSITE_APPVERSION_4.equals(dcVersion)){
			if(a5persist instanceof Document){
				Document tmpDoc = (Document)a5persist;
				if("受控中".equals(tmpDoc.getLifeCycleInfo().getStateName())){
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
				if("受控中".equals(obj.getLifeCycleInfo().getStateName())){
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
