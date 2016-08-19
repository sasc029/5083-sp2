package com.bjsasc.ddm.distribute.action.distributeobject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.bjsasc.ddm.common.ConstUtil;
import com.bjsasc.ddm.distribute.action.AbstractAction;
import com.bjsasc.ddm.distribute.action.distributeinfo.DistributeInfoAction;
import com.bjsasc.ddm.distribute.helper.DistributeHelper;
import com.bjsasc.ddm.distribute.model.distributeobject.DistributeObject;
import com.bjsasc.ddm.distribute.model.distributeorder.DistributeOrder;
import com.bjsasc.ddm.distribute.service.distributeobject.DistributeObjectService;
import com.bjsasc.ddm.distribute.service.distributeorder.DistributeOrderService;
import com.bjsasc.ddm.transfer.helper.DdmDataTransferHelper;
import com.bjsasc.ddm.transfer.service.DdmDataTransferService;
import com.bjsasc.platform.objectmodel.business.persist.ObjectReference;
import com.bjsasc.plm.Helper;
import com.bjsasc.plm.KeyS;
import com.bjsasc.plm.core.cad.CADDocument;
import com.bjsasc.plm.core.cad.mapping.CADDocType;
import com.bjsasc.plm.core.cad.mapping.MappingHelper;
import com.bjsasc.plm.core.change.Change;
import com.bjsasc.plm.core.change.ChangeService;
import com.bjsasc.plm.core.change.Changed;
import com.bjsasc.plm.core.change.ECO;
import com.bjsasc.plm.core.change.PPCO;
import com.bjsasc.plm.core.change.TNO;
import com.bjsasc.plm.core.change.link.ChangedLink;
import com.bjsasc.plm.core.persist.model.Persistable;
import com.bjsasc.plm.core.suit.ATSuit;
import com.bjsasc.plm.core.suit.ATSuitMemberLink;
import com.bjsasc.plm.core.suit.Suit;
import com.bjsasc.plm.core.suit.SuitCoreContants;
import com.bjsasc.plm.core.suit.SuitHelper;
import com.bjsasc.plm.core.suit.Suited;
import com.bjsasc.plm.core.util.StringUtil;
import com.bjsasc.plm.grid.data.GridDataUtil;
import com.bjsasc.plm.type.TypeService;
import com.bjsasc.plm.type.type.Type;
import com.bjsasc.plm.ui.tree.TreeHelper;
import com.bjsasc.plm.ui.tree.TreeNode;
import com.bjsasc.plm.url.Url;
import com.bjsasc.plm.util.RequestUtil;
import com.bjsasc.plm.util.TitleUtil;
import com.cascc.avidm.util.SplitString;

/**
 * 分发对象Action实现类。
 * 
 * @author gengancong 2013-2-22
 */
public class DistributeObjectAction extends AbstractAction {

	/** serialVersionUID */
	private static final long serialVersionUID = -6586307868452072684L;
	private static final Logger LOG = Logger.getLogger(DistributeObjectAction.class);
	/** DistributeObjectService */
	DistributeObjectService service = DistributeHelper.getDistributeObjectService();
	DistributeOrderService orderService = DistributeHelper.getDistributeOrderService();

	/**
	 * 取得分发数据对象。
	 * 
	 * @return JSON对象
	 */
	public String getAllDistributeObject() {
		try {
			// 获取发放单OIDS
			String distributeOrderOid = getAttributeFromSession(ConstUtil.DISTRIBUTE_ORDER_OID);
			if(distributeOrderOid == null){
				distributeOrderOid = RequestUtil.getParamOid(request);
			}
			service.getAllDistributeObject(distributeOrderOid);
			
			
		} catch (Exception ex) {
			error(ex);
		}
		return INITPAGE;
	}

	/**
	 * 取得分发数据对象。 。
	 * 
	 * @return JSON对象
	 */
	public String getDistributeObjectTree() {
		// 获取发放单OIDS
		String distributeOrderOid = getAttributeFromSession(ConstUtil.DISTRIBUTE_ORDER_OID);
		if(distributeOrderOid == null){
			distributeOrderOid = RequestUtil.getParamOid(request);
		}
		List<DistributeObject> listDis = service.getDistributeObjectsByDistributeOrderOid(distributeOrderOid);
		Map<String, Object> mapChild = new HashMap<String, Object>();
		List<Map<String, Object>> prepareNodeObjList = new ArrayList<Map<String, Object>>();
		//key:classid(cad的时候是modeltypeid) value:iconUrl
		Map<String, String> iconUrlMap = new HashMap<String, String>();
		for (DistributeObject dis : listDis) {
			Map<String, Object> node = buildTreeNodeSelfDefined(dis).toMap();
			// 显示图片
			String dataOid = Helper.getOid(dis.getDataClassId(), dis.getDataInnerId());
			
			Persistable obj = Helper.getPersistService().getObject(dataOid);
			
			if (obj != null && !"".equals(obj)) {
				String iconUrl = getIconUrlByTarget(iconUrlMap,obj);
				node.put("iconsrc", iconUrl);
			} else {
				node.put("iconsrc", "");
			}
			node.put("Name", dis.getName());
			node.put("Number", dis.getNumber());
			node.put("Version", dis.getVersion());
			node.put(TreeNode.KEY_CHILDURL, "");
			node.put(TreeNode.KEY_EXPANDED, true);
			node.put("DISTRIBUTEORDEROBJECTLINKOID", dis.getDistributeOrderObjectLinkOid());
			//如果发放单的主对象是"套对象"，"更改单"
			if (obj instanceof ATSuit || obj instanceof ECO||obj instanceof PPCO) {
				node.put(TreeNode.KEY_CHILDURL, request.getContextPath() + "/ddm/distribute/distributeObjectHandle!getDisObjectTreeChild.action?dataOid="+dataOid+"&type="+ConstUtil.C_DISORDER);
				node.put(TreeNode.KEY_EXPANDED, false);
				if (obj instanceof ATSuit){
					// 取得套内对象
					List<Suited> suitedList = SuitHelper.getService().getSuitItems((ATSuit) obj);
					for(Suited suited : suitedList){
						String disDataOid = Helper.getOid(suited.getClassId(), suited.getInnerId());
						mapChild.put(disDataOid, suited);
					}
				} else if (obj instanceof ECO) {
					ChangeService changeSerive = Helper.getChangeService();
					List<ChangedLink> changeLinkList = changeSerive.getChangedLinkList((Change)obj);
					for(ChangedLink changeLink : changeLinkList){
						Persistable pt =  changeLink.getAfterObject();
						if(pt == null){
							continue;
						}
						String disDataOid = Helper.getOid(pt.getClassId(), pt.getInnerId());
						//ObjectReference disObj = (ObjectReference)changeLink.getChangedAfterVersionRef();
						//String disDataOid = Helper.getOid(disObj.getClassId(), disObj.getInnerId());
						mapChild.put(disDataOid, changeLink);
					}
				}else if (obj instanceof PPCO) {
					ChangeService changeSerive = Helper.getChangeService();
					List<ChangedLink> changeLinkList = changeSerive.getChangedLinkList((Change)obj);
					for(ChangedLink changeLink : changeLinkList){
						Persistable pt =  changeLink.getAfterObject();
						if(pt == null){
							continue;
						}
						String disDataOid = Helper.getOid(pt.getClassId(), pt.getInnerId());
						//ObjectReference disObj = (ObjectReference)changeLink.getChangedAfterVersionRef();
						//String disDataOid = Helper.getOid(disObj.getClassId(), disObj.getInnerId());
						mapChild.put(disDataOid, changeLink);
					}
				}
			}
			prepareNodeObjList.add(node);
			
		}
/*	for(Map<String, Object> prepareNodeObj : prepareNodeObjList){
			Object sObject = prepareNodeObj.get("SOURCE");
			Map sMap = (Map) sObject;
			String dataClassId = sMap.get("DATACLASSID").toString();
			String dataInnerId = sMap.get("DATAINNERID").toString();
			String disDataOid = Helper.getOid(dataClassId, dataInnerId);
			if(mapChild.containsKey(disDataOid)){
				continue;
			}
			listMap.add(prepareNodeObj);
		}
	*/
		for(Map<String, Object> prepareNodeObj : prepareNodeObjList){
			String dataClassId = prepareNodeObj.get("DATACLASSID").toString();
			String dataInnerId = prepareNodeObj.get("DATAINNERID").toString();
			String disDataOid = Helper.getOid(dataClassId, dataInnerId);
			if(mapChild.containsKey(disDataOid)){
				continue;
			}
			listMap.add(prepareNodeObj);
		}
		LOG.debug("取得分发数据 " + getDataSize() + " 条，分发数据源oid为 " + distributeOrderOid);
		// 输出结果
		encode();
		return OUTPUTDATA;
	}

	/**
	 * 取得分发数据对象的iconUrl
	 * 
	 * @param iconUrlMap Map<String, String>
	 * @param target Persistable
	 * @return IconUrl
	 */
	private String getIconUrlByTarget(Map<String, String> iconUrlMap,Persistable target){
		String iconUrl = "";
		String key ="";
		CADDocType docType = null;
		//特殊处理CAD文档对象的图标
		if(target instanceof CADDocument) {
			docType = MappingHelper.getService().getDocType((CADDocument)target);
			if(docType != null) {
				iconUrl =  Url.APP + docType.getModelIcon();
			}
		}else{
			key = target.getClassId();
			if(StringUtil.isNull(iconUrlMap.get(key))){
				Type targetType = Helper.getTypeManager().getType(target.getClassId());
				iconUrl =  Url.APP + targetType.getIcon();
				iconUrlMap.put(key, iconUrl);
			}else{
				iconUrl=iconUrlMap.get(key);
			}
		}
		return iconUrl;
	}
	
	/**
	 * 取得分发数据对象。 。
	 * 
	 * @return JSON对象
	 */
	public String getDistributeObjectHistoryTree(){
		// 获取选中的分发数据OIDS
		String oid_linkid_str = request.getParameter("oid_linkid_str");
		String [] oid_linkid_arr=oid_linkid_str.split(",");
		
		//key:classid(cad的时候是modeltypeid) value:iconUrl
		Map<String, String> iconUrlMap = new HashMap<String, String>();

		  for(int i=0;i<oid_linkid_arr.length;i++){
			  
			  String [] oid_linkid = oid_linkid_arr[i].split("_");
			  Persistable object = Helper.getPersistService().getObject(oid_linkid[0]);
			  DistributeObject dis = (DistributeObject)object;
			  Map<String, Object> node = TreeHelper.buildTreeNode(dis).toMap();
			  
			  // 显示图片
			    String dataOid = Helper.getOid(dis.getDataClassId(), dis.getDataInnerId());
				Persistable obj = Helper.getPersistService().getObject(dataOid);
				if (obj != null && !"".equals(obj)) {
					String iconUrl = getIconUrlByTarget(iconUrlMap,obj);
					node.put("iconsrc", iconUrl);
				} else {
					node.put("iconsrc", "");
				}

				node.put("Name", dis.getName());
				node.put(TreeNode.KEY_VIEWICON, false);
				
				node.put(TreeNode.KEY_CHILDURL, "");
				node.put(TreeNode.KEY_EXPANDED, false);
				node.put("DISTRIBUTEOBJECTOID", dis.getOid());
				//node.put(TreeNode.KEY_ENABLESELECT, false);
				listMap.add(node);
			  
		  }
		LOG.debug("取得分发数据 " + getDataSize() + " 条");
		// 输出结果
		encode();
		return OUTPUTDATA;
	}
	
	/**
	 * 套对象，更改单，转阶段更改单，取得子分发数据对象。 
	 * 
	 * @return JSON对象
	 */
	public String getDisObjectTreeChild() {
		String type = request.getParameter("type");
		List<DistributeObject> listDis = new ArrayList<DistributeObject>();
		if (ConstUtil.C_DISORDER.equals(type)) {
			// 获取发放单OIDS
			String distributeOrderOid = getAttributeFromSession(ConstUtil.DISTRIBUTE_ORDER_OID);

			listDis = service.getDistributeObjectsByDistributeOrderOid(distributeOrderOid);
		} else if (ConstUtil.C_PAPER.equals(type)) {
			// 获取纸质任务oids
			String distributePaperOid = getAttributeFromSession(ConstUtil.SPOT_DISTRIBUTEPAPERTASK_OID);

			listDis = service.getDistributeObjectsByDistributePaper(distributePaperOid);
		} else if (ConstUtil.C_ELEC.equals(type)) {
			// 获取发放单OIDS
			String distributeElecTaskOid = getAttributeFromSession(ConstUtil.DISTRIBUTE_ELECTASK_OID);

			listDis = service.getDistributeObjectsByDistributeElecTaskOid(distributeElecTaskOid);
		} else if (ConstUtil.C_PAPERSIGN.equals(type)) {
			// 获取纸质签收任务OIDS
			String distributePaperSignTaskOid = getAttributeFromSession(ConstUtil.DISTRIBUTE_PAPERSIGNTASK_OID);

			listDis = service.getDistributeObjectsByDistributePaperSignTaskOid(distributePaperSignTaskOid);
		}
		
		String oid = request.getParameter("dataOid");
		Persistable obj = Helper.getPersistService().getObject(oid);
		// 分发对象服务
		DistributeObjectService objService = DistributeHelper.getDistributeObjectService();
		
		List<Persistable> list = new ArrayList<Persistable>();
		// 套对象的场合
		if (obj instanceof ATSuit) {
			ATSuit suit = (ATSuit)obj;
			// 取得套内对象
			List<Suited> suitedList = SuitHelper.getService().getSuitItems(suit);

			if ("A".equals(suit.getIterationInfo().getVersionNo())) {
				list.addAll(suitedList);
			} else {
				for(Persistable suited : suitedList){
					if (suited == null) {
						continue;
					}
					list.add(suited);
					//套内对象是可被更改对象，并且更改类别是“更新”时，将其关联的更改单也添加到发放单中
					if (suited instanceof Changed) {
						ATSuitMemberLink link = SuitHelper.getService().getSuitMemberLink(suit, (Suited)suited);
						if (link != null && SuitCoreContants.UPDATE_TYPE.equals(link.getSuitChangeType())) {
							List<ECO> listAfter =  Helper.getChangeService().getRelatedECOList(suited);
							boolean flag = false;
							if(listAfter!= null && listAfter.size()>0){
								for(int i = 0; i < list.size();i++){
									String collectOid = Helper.getOid(list.get(i));
									String afterOid = Helper.getOid(listAfter.get(0));
									if(collectOid.equals(afterOid)){
										flag = true;
										break;
									}
								}
								if (!flag) {
									list.add(listAfter.get(0));
								}
							}
						}
					}
				}
			}
		} else if (obj instanceof ECO) {//更改单
			ChangeService changeSerive = Helper.getChangeService();
			// 更改后对象
			list = changeSerive.getChangedAfterObjList((Change) obj);
		}else if (obj instanceof PPCO) {//转阶段更改单
			ChangeService changeSerive = Helper.getChangeService();
			// 更改后对象
			list = changeSerive.getChangedAfterObjList((Change) obj);
		}
		
		//key:classid(cad的时候是modeltypeid) value:iconUrl
		Map<String, String> iconUrlMap = new HashMap<String, String>();
		
		for(Persistable objed : list){
			if (objed == null) {
				continue;
			}
			// 根据当前分发数据源对象取得分发数据对象List
			List<DistributeObject> disObjList = objService.getDistributeObjectsByDataOid(Helper.getOid(objed));

			boolean objFlag = disObjList == null || disObjList.isEmpty();
			
			if (!objFlag) {
				int i = 0;
				// 分发数据对象
				DistributeObject disObj = disObjList.get(0);
				for (DistributeObject dis : listDis) {
					// 套内对象或改后对象在发放单中有
					if (Helper.getOid(disObj).equals(Helper.getOid(dis))) {
						disObj = dis;
						break;
					}
					i++;
				}
				// 套内对象或改后对象在发放单中没有
				if (i == listDis.size()) {
					continue;
				}
				//Map<String, Object> node = TreeHelper.buildTreeNode(disObj).toMap();
				Map<String, Object> node = buildTreeNodeSelfDefined(disObj).toMap();
				// 显示图片
				if (objed != null && !"".equals(objed)) {
					String iconUrl = getIconUrlByTarget(iconUrlMap,objed);
					node.put("iconsrc", iconUrl);
				} else {
					node.put("iconsrc", "");
				}

				node.put("Name", disObj.getName());
				node.put("Number", disObj.getNumber());
				node.put("Version", disObj.getVersion());
				node.put(TreeNode.KEY_CHILDURL, "");
				node.put(TreeNode.KEY_EXPANDED, true);
				node.put("DISTRIBUTEORDEROBJECTLINKOID", disObj.getDistributeOrderObjectLinkOid());

				// 套对象的场合
				if (obj instanceof ATSuit) {
					//如果套内对象的更新类别是"移除"状态，则添加到发放单中后，以删除线的形式显示
					ATSuitMemberLink link = SuitHelper.getService().getSuitMemberLink((Suit)obj, (Suited)objed);
					if (link != null && SuitCoreContants.DELETE_TYPE.equals(link.getSuitChangeType())) {
						node.put("deleterow", true);//是否有删除线
					}
				}
				
				listMap.add(node);
				
			}
		}
		// 输出结果
		encode();
		return OUTPUTDATA;
	}

	/**
	 * 取得分发数据是更改单的对象。 
	 * 
	 * @return JSON对象
	 */
	public String getDisObjectTree() {

		// 获取发放单OIDS
		String distributeOrderOid = getAttributeFromSession(ConstUtil.DISTRIBUTE_ORDER_OID);

		List<DistributeObject> listDis = service.getDistributeObjectsByDistributeOrderOid(distributeOrderOid);
		
		//key:classid(cad的时候是modeltypeid) value:iconUrl
		Map<String, String> iconUrlMap = new HashMap<String, String>();
		
		for (DistributeObject dis : listDis) {

			// 显示图片
			String dataOid = Helper.getOid(dis.getDataClassId(), dis.getDataInnerId());
			Persistable obj = Helper.getPersistService().getObject(dataOid);
			if (obj instanceof ECO || obj instanceof TNO) {
				Map<String, Object> node = TreeHelper.buildTreeNode(dis).toMap();
				if (obj != null && !"".equals(obj)) {
					String iconUrl = getIconUrlByTarget(iconUrlMap,obj);
					node.put("iconsrc", iconUrl);
				} else {
					node.put("iconsrc", "");
				}

				node.put("Name", dis.getName());
				node.put(TreeNode.KEY_CHILDURL, "");
				node.put(TreeNode.KEY_EXPANDED, false);
				node.put("DISTRIBUTEORDEROBJECTLINKOID", dis.getDistributeOrderObjectLinkOid());
				if (obj instanceof ECO) {
					node.put("ISTRACK", ((ECO) obj).getIsTrack());
				} else {
					node.put("ISTRACK", ((TNO) obj).getIsTrack());
				}
				listMap.add(node);
			}
		}
		LOG.debug("取得分发数据 " + getDataSize() + " 条，分发数据源oid为 " + distributeOrderOid);
		// 输出结果
		encode();
		return OUTPUTDATA;
	}

	/**
	 * 电子任务取得分发数据对象。 。
	 * 
	 * @return JSON对象
	 */
	public String getDistributeElecTaskObjectTree() {

		// 获取发放单OIDS
		String distributeElecTaskOid = getAttributeFromSession(ConstUtil.DISTRIBUTE_ELECTASK_OID);

		List<DistributeObject> listDis = service.getDistributeObjectsByDistributeElecTaskOid(distributeElecTaskOid);

		Map<String, Object> mapChild = new HashMap<String, Object>();
		List<Map<String, Object>> prepareNodeObjList = new ArrayList<Map<String, Object>>();
		
		//key:classid(cad的时候是modeltypeid) value:iconUrl
		Map<String, String> iconUrlMap = new HashMap<String, String>();
		
		for (DistributeObject dis : listDis) {
			Map<String, Object> node = TreeHelper.buildTreeNode(dis).toMap();

			// 显示图片
			String dataOid = Helper.getOid(dis.getDataClassId(), dis.getDataInnerId());
			Persistable obj = Helper.getPersistService().getObject(dataOid);
			if (obj != null && !"".equals(obj)) {
				String iconUrl = getIconUrlByTarget(iconUrlMap,obj);
				node.put("iconsrc", iconUrl);
			} else {
				node.put("iconsrc", "");
			}

			node.put("Name", dis.getName());
			node.put(TreeNode.KEY_CHILDURL, "");
			node.put(TreeNode.KEY_EXPANDED, true);
			node.put("DISTRIBUTEORDEROBJECTLINKOID", dis.getDistributeOrderObjectLinkOid());

			if (obj instanceof ATSuit || obj instanceof ECO) {
				node.put(TreeNode.KEY_CHILDURL, request.getContextPath() + "/ddm/distribute/distributeObjectHandle!getDisObjectTreeChild.action?dataOid="+dataOid+"&type="+ConstUtil.C_DISORDER);
				node.put(TreeNode.KEY_EXPANDED, false);

				if (obj instanceof ATSuit){
					// 取得套内对象
					List<Suited> suitedList = SuitHelper.getService().getSuitItems((ATSuit) obj);
					for(Suited suited : suitedList){
						String disDataOid = Helper.getOid(suited.getClassId(), suited.getInnerId());
						mapChild.put(disDataOid, suited);
					}
				} else if (obj instanceof ECO) {
					ChangeService changeSerive = Helper.getChangeService();
					List<ChangedLink> changeLinkList = changeSerive.getChangedLinkList((Change)obj);
					for(ChangedLink changeLink : changeLinkList){
						ObjectReference disObj = (ObjectReference)changeLink.getChangedAfterVersionRef();
						String disDataOid = Helper.getOid(disObj.getClassId(), disObj.getInnerId());
						mapChild.put(disDataOid, changeLink);
					}
				}
			}
			prepareNodeObjList.add(node);
		}
		for(Map<String, Object> prepareNodeObj : prepareNodeObjList){
			Object sObject = prepareNodeObj.get("SOURCE");
			Map sMap = (Map) sObject;
			String dataClassId = sMap.get("DATACLASSID").toString();
			String dataInnerId = sMap.get("DATAINNERID").toString();
			String disDataOid = Helper.getOid(dataClassId, dataInnerId);
			if(mapChild.containsKey(disDataOid)){
				continue;
			}
			listMap.add(prepareNodeObj);
		}
		LOG.debug("取得分发数据 " + getDataSize() + " 条，电子任务oid为 " + distributeElecTaskOid);
		// 输出结果
		encode();
		return OUTPUTDATA;
	}

	/**
	 * 纸质签收任务取得分发数据对象。
	 * 
	 * @return JSON对象
	 * @author zhangguoqiang 2014-09-11
	 */
	public String getDistributePaperSignTaskObjectTree() {

		// 获取发放单OIDS
		String distributePaperSignTaskOid = getAttributeFromSession(ConstUtil.DISTRIBUTE_PAPERSIGNTASK_OID);

		List<DistributeObject> listDis = service.getDistributeObjectsByDistributePaperSignTaskOid(distributePaperSignTaskOid);

		Map<String, Object> mapChild = new HashMap<String, Object>();
		List<Map<String, Object>> prepareNodeObjList = new ArrayList<Map<String, Object>>();
		
		//key:classid(cad的时候是modeltypeid) value:iconUrl
		Map<String, String> iconUrlMap = new HashMap<String, String>();
		
		for (DistributeObject dis : listDis) {
			Map<String, Object> node = TreeHelper.buildTreeNode(dis).toMap();

			// 显示图片
			String dataOid = Helper.getOid(dis.getDataClassId(), dis.getDataInnerId());
			Persistable obj = Helper.getPersistService().getObject(dataOid);
			if (obj != null && !"".equals(obj)) {
				String iconUrl = getIconUrlByTarget(iconUrlMap,obj);
				node.put("iconsrc", iconUrl);
			} else {
				node.put("iconsrc", "");
			}

			node.put("Name", dis.getName());
			node.put(TreeNode.KEY_CHILDURL, "");
			node.put(TreeNode.KEY_EXPANDED, true);
			node.put("DISTRIBUTEORDEROBJECTLINKOID", dis.getDistributeOrderObjectLinkOid());

			//如果发放单的主对象是"套对象"，"更改单"
			if (obj instanceof ATSuit || obj instanceof ECO) {
				node.put(TreeNode.KEY_CHILDURL, request.getContextPath() + "/ddm/distribute/distributeObjectHandle!getDisObjectTreeChild.action?dataOid="+dataOid+"&type="+ConstUtil.C_DISORDER);
				node.put(TreeNode.KEY_EXPANDED, false);

				if (obj instanceof ATSuit){
					// 取得套内对象
					List<Suited> suitedList = SuitHelper.getService().getSuitItems((ATSuit) obj);
					for(Suited suited : suitedList){
						String disDataOid = Helper.getOid(suited.getClassId(), suited.getInnerId());
						mapChild.put(disDataOid, suited);
					}
				} else if (obj instanceof ECO) {
					ChangeService changeSerive = Helper.getChangeService();
					List<ChangedLink> changeLinkList = changeSerive.getChangedLinkList((Change)obj);
					for(ChangedLink changeLink : changeLinkList){
						ObjectReference disObj = (ObjectReference)changeLink.getChangedAfterVersionRef();
						String disDataOid = Helper.getOid(disObj.getClassId(), disObj.getInnerId());
						mapChild.put(disDataOid, changeLink);
					}
				}
			}
			prepareNodeObjList.add(node);
		}
		
		for(Map<String, Object> prepareNodeObj : prepareNodeObjList){
			Object sObject = prepareNodeObj.get("SOURCE");
			Map sMap = (Map) sObject;
			String dataClassId = sMap.get("DATACLASSID").toString();
			String dataInnerId = sMap.get("DATAINNERID").toString();
			String disDataOid = Helper.getOid(dataClassId, dataInnerId);
			if(mapChild.containsKey(disDataOid)){
				continue;
			}
			listMap.add(prepareNodeObj);
		}
		
		LOG.debug("取得分发数据 " + getDataSize() + " 条，纸质签收任务oid为 " + distributePaperSignTaskOid);
		// 输出结果
		encode();
		return OUTPUTDATA;
	}

	/**
	 * check发放单中有无更改单的对象。 
	 * 
	 * @return JSON对象
	 */
	public String checkDisOrdersHaveEco() {

		// 获取发放单OIDS
		String distributeOrderOid = getAttributeFromSession(ConstUtil.DISTRIBUTE_ORDER_OID);

		List<DistributeObject> listDis = service.getDistributeObjectsByDistributeOrderOid(distributeOrderOid);
		String flag = "false";
		for (DistributeObject dis : listDis) {

			// 显示图片
			String dataOid = Helper.getOid(dis.getDataClassId(), dis.getDataInnerId());
			Persistable obj = Helper.getPersistService().getObject(dataOid);
			if (obj instanceof ECO || obj instanceof TNO) {
				flag = "true";
				LOG.debug("发放单中有更改单或者技术通知单的对象 ");
				break;
			}
		}
		result.put("success", flag);
		// 输出结果
		mapToSimpleJson();
		return OUTPUTDATA;
	}

	/**
	 * 取得关于纸质任务分发数据对象。 。
	 * 
	 * @return JSON对象
	 */
	public String getDistributePaperObjectTree() {

		// 获取纸质任务oids
		String distributePaperOid = getAttributeFromSession(ConstUtil.SPOT_DISTRIBUTEPAPERTASK_OID);

		List<DistributeObject> listDis = service.getDistributeObjectsByDistributePaper(distributePaperOid);

		Map<String, Object> mapChild = new HashMap<String, Object>();
		List<Map<String, Object>> prepareNodeObjList = new ArrayList<Map<String, Object>>();
		
		//key:classid(cad的时候是modeltypeid) value:iconUrl
		Map<String, String> iconUrlMap = new HashMap<String, String>();
		
		for (DistributeObject dis : listDis) {
			Map<String, Object> node = TreeHelper.buildTreeNode(dis).toMap();

			// 显示图片
			String dataOid = Helper.getOid(dis.getDataClassId(), dis.getDataInnerId());
			Persistable obj = Helper.getPersistService().getObject(dataOid);
			if (obj != null && !"".equals(obj)) {
				String iconUrl = getIconUrlByTarget(iconUrlMap,obj);
				node.put("iconsrc", iconUrl);
			} else {
				node.put("iconsrc", "");
			}

			node.put("Name", dis.getName());
			node.put(TreeNode.KEY_CHILDURL, "");
			node.put(TreeNode.KEY_EXPANDED, true);
			node.put("DISTRIBUTEORDEROBJECTLINKOID", dis.getDistributeOrderObjectLinkOid());

			//如果发放单的主对象是"套对象"，"更改单"
			if (obj instanceof ATSuit || obj instanceof ECO) {
				node.put(TreeNode.KEY_CHILDURL, request.getContextPath() + "/ddm/distribute/distributeObjectHandle!getDisObjectTreeChild.action?dataOid="+dataOid+"&type="+ConstUtil.C_PAPER);
				node.put(TreeNode.KEY_EXPANDED, false);

				if (obj instanceof ATSuit){
					// 取得套内对象
					List<Suited> suitedList = SuitHelper.getService().getSuitItems((ATSuit) obj);
					for(Suited suited : suitedList){
						String disDataOid = Helper.getOid(suited.getClassId(), suited.getInnerId());
						mapChild.put(disDataOid, suited);
					}
				} else if (obj instanceof ECO) {
					ChangeService changeSerive = Helper.getChangeService();
					List<ChangedLink> changeLinkList = changeSerive.getChangedLinkList((Change)obj);
					for(ChangedLink changeLink : changeLinkList){
						ObjectReference disObj = (ObjectReference)changeLink.getChangedAfterVersionRef();
						String disDataOid = Helper.getOid(disObj.getClassId(), disObj.getInnerId());
						mapChild.put(disDataOid, changeLink);
					}
				}
			}
			prepareNodeObjList.add(node);
		}
		
		for(Map<String, Object> prepareNodeObj : prepareNodeObjList){
			Object sObject = prepareNodeObj.get("SOURCE");
			Map sMap = (Map) sObject;
			String dataClassId = sMap.get("DATACLASSID").toString();
			String dataInnerId = sMap.get("DATAINNERID").toString();
			String disDataOid = Helper.getOid(dataClassId, dataInnerId);
			if(mapChild.containsKey(disDataOid)){
				continue;
			}
			listMap.add(prepareNodeObj);
		}
		
		LOG.debug("取得分发数据 " + getDataSize() + " 条，分发数据源oid为 " + distributePaperOid);
		// 输出结果
		encode();
		return OUTPUTDATA;
	}
	
	/**
	 * 取得关于回收销毁纸质任务分发数据对象。 
	 * 
	 * @return JSON对象
	 * @author Sun Zongqing
	 * @date 2014/7/2
	 */
	public String getRecDesPaperObjectTree() {

		// 获取纸质任务oids
		String distributePaperOid = getAttributeFromSession(ConstUtil.SPOT_RECDESPAPERTASK_OID);

		List<DistributeObject> listDis = service.getDistributeObjectsByDistributePaper(distributePaperOid);

		Map<String, Object> mapChild = new HashMap<String, Object>();
		List<Map<String, Object>> prepareNodeObjList = new ArrayList<Map<String, Object>>();
		
		//key:classid(cad的时候是modeltypeid) value:iconUrl
		Map<String, String> iconUrlMap = new HashMap<String, String>();
		
		for (DistributeObject dis : listDis) {
			Map<String, Object> node = TreeHelper.buildTreeNode(dis).toMap();

			// 显示图片
			String dataOid = Helper.getOid(dis.getDataClassId(), dis.getDataInnerId());
			Persistable obj = Helper.getPersistService().getObject(dataOid);
			if (obj != null && !"".equals(obj)) {
				String iconUrl = getIconUrlByTarget(iconUrlMap,obj);
				node.put("iconsrc", iconUrl);
			} else {
				node.put("iconsrc", "");
			}

			node.put("Name", dis.getName());
			node.put(TreeNode.KEY_CHILDURL, "");
			node.put(TreeNode.KEY_EXPANDED, true);
			node.put("DISTRIBUTEORDEROBJECTLINKOID", dis.getDistributeOrderObjectLinkOid());
			
			//如果发放单的主对象是"套对象"，"更改单"
			if (obj instanceof ATSuit || obj instanceof ECO) {
				node.put(TreeNode.KEY_CHILDURL, request.getContextPath() + "/ddm/distribute/distributeObjectHandle!getDisObjectTreeChild.action?dataOid="+dataOid+"&type="+ConstUtil.C_DISORDER);
				node.put(TreeNode.KEY_EXPANDED, false);

				if (obj instanceof ATSuit){
					// 取得套内对象
					List<Suited> suitedList = SuitHelper.getService().getSuitItems((ATSuit) obj);
					for(Suited suited : suitedList){
						String disDataOid = Helper.getOid(suited.getClassId(), suited.getInnerId());
						mapChild.put(disDataOid, suited);
					}
				} else if (obj instanceof ECO) {
					ChangeService changeSerive = Helper.getChangeService();
					List<ChangedLink> changeLinkList = changeSerive.getChangedLinkList((Change)obj);
					for(ChangedLink changeLink : changeLinkList){
						ObjectReference disObj = (ObjectReference)changeLink.getChangedAfterVersionRef();
						String disDataOid = Helper.getOid(disObj.getClassId(), disObj.getInnerId());
						mapChild.put(disDataOid, changeLink);
					}
				}
			}
			prepareNodeObjList.add(node);
		}
		
		for(Map<String, Object> prepareNodeObj : prepareNodeObjList){
			Object sObject = prepareNodeObj.get("SOURCE");
			Map sMap = (Map) sObject;
			String dataClassId = sMap.get("DATACLASSID").toString();
			String dataInnerId = sMap.get("DATAINNERID").toString();
			String disDataOid = Helper.getOid(dataClassId, dataInnerId);
			if(mapChild.containsKey(disDataOid)){
				continue;
			}
			listMap.add(prepareNodeObj);
		}
		
		LOG.debug("取得回收销毁数据 " + getDataSize() + " 条，回收销毁数据源oid为 " + distributePaperOid);
		// 输出结果
		encode();
		return OUTPUTDATA;
	}

	/**
	 * 修改分发数据对象。
	 * 
	 * @return JSON对象
	 */
	public String updateDistributeObject() {
		try {
			String oid = request.getParameter(KeyS.OID);

			String dataInnerId = request.getParameter("dataInnerId");// 分发数据源内部标识
			String dataClassId = request.getParameter("dataClassId");// 分发数据源类名
			String dataFrom = request.getParameter("dataFrom");// 分发数据源来源

			service.updateDistributeObject(oid, dataInnerId, dataClassId, dataFrom);

			getAllDistributeObject();
			success();
		} catch (Exception ex) {
			jsonError(ex);
		}
		mapToSimpleJson();
		return OUTPUTDATA;
	}

	/**
	 * 删除分发数据对象。
	 * 
	 * @return JSON对象
	 */
	public String deleteDistributeObject() {
		try {
			// 获取发放单OID
			String distributeOrderOid = getAttributeFromSession(ConstUtil.DISTRIBUTE_ORDER_OID);
			// 获取分发数据和发放单Link的OIDS
			String oids = request.getParameter(KeyS.OIDS);

			service.deleteDistributeObjectByOids(distributeOrderOid, oids);

			getAllDistributeObject();
			success();
			mapToSimpleJson();
		} catch (Exception ex) {
			jsonError(ex);
		}

		return OUTPUTDATA;
	}

	/**
	 * 创建分发数据对象。
	 * 
	 * @return JSON对象
	 */
	public String addDistributeObject() {
		try {
			// 获取发放单OID
			String ordOid = getAttributeFromSession(ConstUtil.DISTRIBUTE_ORDER_OID);
			DistributeOrder disOrd = (DistributeOrder) Helper.getPersistService().getObject(ordOid);
			// 获取分发数据OIDS
			String oids = request.getParameter(KeyS.OIDS);
			List<String> oidList = SplitString.string2List(oids, ",");
			orderService.addDisObjectToDisOrder(disOrd, oidList);
			getAllDistributeObject();
			success();
		} catch (Exception ex) {
			error(ex);
		}
		mapToSimpleJson();
		return OUTPUTDATA;
	}

	/**
	 * 是否可转换且受控。
	 * 
	 * @return JSON对象
	 */
	public String isTransferControlling() {
		// 获取数据对象标识
		String innerid = request.getParameter("innerId");
		String classid = request.getParameter("classId");
		String oid = Helper.getOid(classid, innerid);
		String isTransferControlling[] = service.isTransferControlling(oid).split(",");
		result.put("success", isTransferControlling[0]);
		result.put("flag", isTransferControlling[1]);
		mapToSimpleJson();
		return OUTPUTDATA;
	}

	/**
	 * 更新发放单与分发数据link的紧急程度。
	 * 
	 * @return String
	 */
	public String updateDistributeObjectLink() {
		try {
			String linkOids = request.getParameter(KeyS.OIDS);
			String disUrgent = request.getParameter("disUrgent");
			service.updateDistributeObjectLink(linkOids, disUrgent);

			DistributeInfoAction info = new DistributeInfoAction();
			info.getAllDistributeInfo();

			success();
		} catch (Exception ex) {
			error(ex);
		}
		mapToSimpleJson();
		return OUTPUTDATA;
	}

	/**
	 * 更新发放单与分发数据link的完工期限。
	 * 
	 * @return String
	 */
	public String updateDistributedeadLinkDate() {
		try {
			String linkOids = request.getParameter(KeyS.OIDS);
			String deadLineDate = request.getParameter("deadLineDate");
			service.updateDistributedeadLinkDate(linkOids, deadLineDate);

			DistributeInfoAction info = new DistributeInfoAction();
			info.getAllDistributeInfo();
			success();
		} catch (Exception ex) {
			error(ex);
		}
		mapToSimpleJson();
		return OUTPUTDATA;
	}

	/**
	 * 取得分发数据回退。
	 * 
	 * @param dataoid String
	 * @return DistributeObject
	 */
	public String getDistributeObjectReturnDetail() {
		String oid = request.getParameter(KeyS.OID);
		String taskOid = request.getParameter("taskOid");
		List<DistributeObject> listDis = service.getDistributeObjectReturnDetail(oid, taskOid);
		// 格式化显示数据
		TypeService typeManager = Helper.getTypeManager();
		for (DistributeObject target : listDis) {
			if (target == null) {
				continue;
			}
			Map<String, Object> mapAll = new HashMap<String, Object>();
			Map<String, Object> mainMap = typeManager.format(target);
			Map<String, Object> returnMap = typeManager.format(target.getReturnReason());
			mapAll.putAll(returnMap);
			mapAll.putAll(mainMap);
			listMap.add(mapAll);
		}
		LOG.debug("取得分发数据被回退 " + getDataSize() + " 条，生命周期状态：" + ConstUtil.LC_BACKOFF.getName());
		// 输出结果
		GridDataUtil.prepareRowObjectMaps(listMap, ConstUtil.SOPT_LISTDISTRIBUTEOBJECTRETURNDETAIL);
		return "listDisPaperObjectReturnDetail";
	}

	/**
	 * 取得转换后的分发数据对象。
	 * 
	 * @param dataoid String
	 * @return DistributeObject
	 */
	public DistributeObject getDistributeObject(String dataoid) {
		Persistable obj = Helper.getPersistService().getObject(dataoid);
		// 数据转换服务
		DdmDataTransferService tranService = DdmDataTransferHelper.getDistributeObjectService();
		DistributeObject disObject = tranService.transferToDdmData(obj);
		return disObject;
	}

	/**
	 * 更新发放单与分发数据link的分发方式。
	 * 
	 * @return String
	 */
	public String updateDistributeObjectLinkByDisStyle() {
		try {
			String linkOids = request.getParameter(KeyS.OIDS);
			String disStyle = request.getParameter("disStyle");
			service.updateDistributeObjectLinkByDisStyle(linkOids, disStyle);

			DistributeInfoAction info = new DistributeInfoAction();
			info.getAllDistributeInfo();

			success();
		} catch (Exception ex) {
			error(ex);
		}
		mapToSimpleJson();
		return OUTPUTDATA;
	}

	/**
	 * 创建分发数据对象。
	 * 
	 * @return JSON对象
	 */
	public String addDistributeDataObjectLink() {
		try {
			// 获取部件oid
			String partOid = request.getParameter("partOid");
			//获取业务对象oids
			String objectOids = request.getParameter("objectOids");

			service.addDistributeDataObjectLink(partOid, objectOids);

			success();
		} catch (Exception ex) {
			error(ex);
		}
		mapToSimpleJson();
		return OUTPUTDATA;
	}
	/**为了提高分发信息左侧树展示数据的效率，重写buildTreeNode方法，只加载需要展示的数据
	 * yangzhenzhou
	 * @param target
	 * @return
	 */
	public  TreeNode buildTreeNodeSelfDefined(Persistable target){
		//Map<String, Object> targetMap = Helper.getTypeManager().format(target, KeyS.VIEW_SIMPLE);
		DistributeObject dis=(DistributeObject)target;
		Map<String, Object> targetMap=new HashMap<String, Object> ();
		//targetMap.put("SOURCE", dis.get);
		targetMap.put("NUMBER", dis.getNumber());
		targetMap.put("NAME", dis.getName());
		targetMap.put("VERSION", dis.getVersion());
		targetMap.put("DATACLASSID", dis.getDataClassId());
		targetMap.put("DATAINNERID", dis.getDataInnerId());
		
		String oid = Helper.getOid(target);
		
		String title = TitleUtil.getTitle(target, targetMap);
		
		String url = "/plm/common/visit/VisitObject.jsp?OID="+oid;
		
		String iconUrl = Helper.getTypeService().getIcon(target);
		
		TreeNode treeNode = new TreeNode();
		treeNode.setNodeId(oid);
		treeNode.setNodeName(title);
		treeNode.setNodeLink(url);
		treeNode.setNodeIconSrc(iconUrl);
		treeNode.setNodeViewIcon(true);
		treeNode.getColumns().putAll(targetMap);
		
		return treeNode;
	}
}
