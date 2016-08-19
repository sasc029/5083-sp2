package com.bjsasc.ddm.common;

import java.util.ArrayList;
import java.util.List;

import com.bjsasc.adm.active.helper.AdmHelper;
import com.bjsasc.adm.active.model.ActiveObjOrder;
import com.bjsasc.adm.active.model.ActiveObjSet;
import com.bjsasc.adm.active.model.ActiveOrdered;
import com.bjsasc.adm.active.model.ActiveSeted;
import com.bjsasc.adm.active.model.activedocument.ActiveDocument;
import com.bjsasc.adm.active.model.activeorder.ActiveOrder;
import com.bjsasc.adm.active.model.activeset.ActiveSet;
import com.bjsasc.ddm.distribute.helper.DistributeHelper;
import com.bjsasc.ddm.distribute.model.distributeobject.DistributeObject;
import com.bjsasc.ddm.distribute.service.distributeobject.DistributeObjectService;
import com.bjsasc.plm.Helper;
import com.bjsasc.plm.core.approve.ApproveHelper;
import com.bjsasc.plm.core.approve.ApproveOrder;
import com.bjsasc.plm.core.approve.Approved;
import com.bjsasc.plm.core.baseline.BaselineHelper;
import com.bjsasc.plm.core.baseline.model.Baselined;
import com.bjsasc.plm.core.baseline.model.ManagedBaseline;
import com.bjsasc.plm.core.cad.CADDocument;
import com.bjsasc.plm.core.change.Change;
import com.bjsasc.plm.core.change.ChangeService;
import com.bjsasc.plm.core.change.Changed;
import com.bjsasc.plm.core.change.ECO;
import com.bjsasc.plm.core.change.PPCO;
import com.bjsasc.plm.core.change.TNO;
import com.bjsasc.plm.core.change.Variance;
import com.bjsasc.plm.core.change.link.VariancedLink;
import com.bjsasc.plm.core.context.model.Context;
import com.bjsasc.plm.core.doc.Document;
import com.bjsasc.plm.core.option.OptionHelper;
import com.bjsasc.plm.core.option.OptionValue;
import com.bjsasc.plm.core.part.Part;
import com.bjsasc.plm.core.persist.model.Persistable;
import com.bjsasc.plm.core.suit.ATSuit;
import com.bjsasc.plm.core.suit.ATSuitMemberLink;
import com.bjsasc.plm.core.suit.Suit;
import com.bjsasc.plm.core.suit.SuitCoreContants;
import com.bjsasc.plm.core.suit.SuitHelper;
import com.bjsasc.plm.core.suit.Suited;
import com.bjsasc.plm.core.util.PlmException;
import com.bjsasc.plm.core.vc.model.ControlBranch;

/**
 * 相关对象收集器。 (1)文档，收集文档对象 (2)部件，收集部件对象 (3)送审单，收集送审对象(送审对象是管理基线时,则不收集)
 * (4)更改单，收集更改单和改后对象 (5)管理基线,收集基线内容对象 (6)技术通知单,收集技术通知单和偏离后对象
 * (7)超差代料质疑单,收集超差代料质疑单和超差对象 (8)现行文件,收集现行文件对象 (9)现行套,收集现行套相关对象
 * (10)现行单据,收集现行单据和改后对象
 * 
 * @author yanjia 2013-4-27
 */
public class CollectReferenceObject {
	/** 分发对象服务 */
	private static DistributeObjectService objService = DistributeHelper
			.getDistributeObjectService();
	/** 收集对象是更改单标识 */
	private static int collectFlag;

	/**
	 * 构造方法。
	 */
	private CollectReferenceObject() {

	}

	/**
	 * 收集相关对象。
	 * 
	 * @param dataObject
	 *            Persistable
	 * @return List<Persistable>
	 */
	public static List<Persistable> collectRefObject(Persistable dataObject) {
		// 收集结果List
		List<Persistable> collectResultList = new ArrayList<Persistable>();

		// 送审单
		if (dataObject instanceof ApproveOrder) {
			collectFlag = ConstUtil.COLLECTFLAG_0;
			// 收集送审单相关对象
			List<Persistable> collectApproveOrderResList = collectApproveOrderRefrenceObject(dataObject);
			// 收集结果List
				collectResultList.addAll(collectApproveOrderResList);
			// 更改单
		} else if (dataObject instanceof ECO) {
			collectFlag = ConstUtil.COLLECTFLAG_1;
			// 更改单相关对象收集结果List
			List<Persistable> collectECOResList = collectECORefrenceObject(dataObject);
			// 收集结果List
				collectResultList.addAll(collectECOResList);
			// 部件
		} else if (dataObject instanceof Part) {//
			collectFlag = ConstUtil.COLLECTFLAG_0;
			// 部件相关对象收集结果List
			List<Persistable> collectPartRefResList = collectPartfrenceObject(dataObject);
			// 收集结果List
			collectResultList.addAll(collectPartRefResList);
			// 文档
		} else if (dataObject instanceof Document) {//
			collectFlag = ConstUtil.COLLECTFLAG_0;
			// 文档相关对象收集结果List
			List<Persistable> collectDocumentResList = collectDocumentRefrenceObject(dataObject);
			// 收集结果List
			collectResultList.addAll(collectDocumentResList);
			// CADDocument
		} else if (dataObject instanceof CADDocument) {
			collectFlag = ConstUtil.COLLECTFLAG_0;
			// CAD文档相关对象收集结果List
			List<Persistable> collectCADDocumentResList = collectCADDocumentRefrenceObject(dataObject);
			// 收集结果List
			collectResultList.addAll(collectCADDocumentResList);

			// 管理基线
		} else if (dataObject instanceof ManagedBaseline) {
			collectFlag = ConstUtil.COLLECTFLAG_0;
			// 管理基线相关对象收集结果List
			List<Persistable> collectManagedBaselineResList = collectManagedBaselineRefrenceObject(dataObject);
			// 收集结果List
				collectResultList.addAll(collectManagedBaselineResList);
			// 技术通知单
		} else if (dataObject instanceof TNO) {
			collectFlag = ConstUtil.COLLECTFLAG_0;
			// 技术通知单相关对象收集结果List
			List<Persistable> collectTNOResList = collectTNORefrenceObject(dataObject);
			// 收集结果List
				collectResultList.addAll(collectTNOResList);
			// 超差代料质疑单
		} else if (dataObject instanceof Variance) {
			collectFlag = ConstUtil.COLLECTFLAG_0;
			// 超差代料质疑单相关对象收集结果List
			List<Persistable> collectVarianceResList = collectVarianceRefrenceObject(dataObject);
			// 收集结果List
				collectResultList.addAll(collectVarianceResList);
			// 现行文件,收集现行文件对象
		} else if (dataObject instanceof ActiveDocument) {//
			collectFlag = ConstUtil.COLLECTFLAG_0;
			// 现行文件收集结果List
			List<Persistable> collectActiveDocumentList = collectActiveDocumentObject(dataObject);
			// 收集结果List
			collectResultList.addAll(collectActiveDocumentList);
			// 现行套,收集现行套相关对象
		} else if (dataObject instanceof ActiveSet) {
			collectFlag = ConstUtil.COLLECTFLAG_0;
			// 现行文件收集结果List
			List<Persistable> collectActiveSetList = collectActiveSetRefrenceObject(dataObject);
			// 收集结果List
			collectResultList.addAll(collectActiveSetList);
			// 现行单据,收集现行单据和改后对象
		} else if (dataObject instanceof ActiveOrder) {
			collectFlag = ConstUtil.COLLECTFLAG_2;
			// 现行单据相关对象收集结果List
			List<Persistable> collectActiveOrderResList = collectActiveOrderRefrenceObject(dataObject);
			// 收集结果List
			collectResultList.addAll(collectActiveOrderResList);
		} else if (dataObject instanceof ATSuit) {
			ATSuit atSuit = (ATSuit) dataObject;
			if ("A".equals(atSuit.getIterationInfo().getVersionNo())) {
				collectFlag = ConstUtil.COLLECTFLAG_0;
			} else {
				collectFlag = ConstUtil.COLLECTFLAG_3;
			}
			// 套对象相关对象收集结果List
			List<Persistable> collectSuitResList = collectSuitRefrenceObject(
					dataObject, collectFlag);
			// 收集结果List
				collectResultList.addAll(collectSuitResList);
			// 没有相关对象的所有类
		} else if (dataObject instanceof PPCO) {
			collectFlag = ConstUtil.COLLECTFLAG_1;
			// 更改单相关对象收集结果List
		//	List<Persistable> collectECOResList = collectECORefrenceObject(dataObject);
			List<Persistable> collectPPCOResList = collectPPCORefrenceObject(dataObject);
			// 收集结果List
				collectResultList.addAll(collectPPCOResList);
			// 部件
		} else {
			collectFlag = ConstUtil.COLLECTFLAG_0;
			// 将没有相关对象的类，统一直接加到结果List中
			collectResultList.add(dataObject);
		}
		return collectResultList;
	}

	/**
	 * 收集送审单相关对象。 (1)送审对象
	 * 
	 * @param dataObject
	 *            Persistable
	 * @return List<Persistable>
	 */
	private static List<Persistable> collectApproveOrderRefrenceObject(
			Persistable dataObject) {
		// 送审单相关对象收集结果List
		List<Persistable> collectApproveOrderRefResList = new ArrayList<Persistable>();

		if (!(dataObject instanceof ApproveOrder)) {
			throw new PlmException("Object[" + Helper.getOid(dataObject)
					+ "] is not ApproveOrder");
		}
		// 送审对象List
		List<Approved> ptList = ApproveHelper.getApproveOrderService()
				.getApprovedList((ApproveOrder) dataObject);
		List<Persistable> ptList2 = new ArrayList<Persistable>();
		for (Persistable ptobj : ptList) {
			if (ptobj instanceof ControlBranch) {
				ptList2.add((Persistable) Helper.getVersionService()
						.getLatestIteration((ControlBranch) ptobj));
			} else {
				ptList2.add(ptobj);
			}
		}
		ApproveOrder dataObjectApproveOrder = (ApproveOrder) dataObject;
		Context context = dataObjectApproveOrder.getContextInfo().getContext();
		OptionValue value = OptionHelper.getService().getOptionValue(context,
				"disMange_disStrategy_allDis");
		String optionValue = value.getValue();
		for (Persistable pt : ptList2) {
			if (pt == null) {
				continue;
			}
			// 送审对象是管理基线，则不收集
			if (pt instanceof ManagedBaseline) {
				continue;
			}
			// 是否满足首选项配置，若不是则不收集
			String objOid = Helper.getOid(pt);

			String isControling[] = objService.isTransferControlling(objOid)
					.split(",");
			if ("false".equals(optionValue)) {
				if ("false".equals(isControling[0])) {
					continue;
				}
				// 该对象加入到送审单相关对象收集结果List中
				collectApproveOrderRefResList.add(pt);
			} else {
				if ("false".equals(isControling[0])) {
					//如果首选项选择的是整体分发，子对象有一个不符合发放条件的则清空list
					collectApproveOrderRefResList.clear();
					break;
				}
				// 该对象加入到送审单相关对象收集结果List中
				collectApproveOrderRefResList.add(pt);
			}
		}
		return collectApproveOrderRefResList;
	}

	/**
	 * 收集更改单相关对象。 (1)更改单 (2)改后对象
	 * 
	 * @param dataObject
	 *            Persistable
	 * @return List<Persistable>
	 */
	private static List<Persistable> collectECORefrenceObject(
			Persistable dataObject) {
		// 更改单相关对象收集结果List
		ECO dataObjectECO = (ECO) dataObject;
		Context context = dataObjectECO.getContextInfo().getContext();
		// 分发策略（是否是整体分发）
		OptionValue value = OptionHelper.getService().getOptionValue(context,
				"disMange_disStrategy_allDis");
		String optionValue = value.getValue();

		List<Persistable> collectECOResList = new ArrayList<Persistable>();

		// 更改单加入到更改单相关对象收集结果List
		// 先判断更改单本身符不符合首选项配置发放条件
		String ecoOid = Helper.getOid(dataObject);
		String ecoControling[] = objService.isTransferControlling(ecoOid).split(",");
		if ("false".equals(ecoControling[0])) {
		} else {
			collectECOResList.add(dataObject);
		}
		ChangeService changeSerive = Helper.getChangeService();

		// 更改后对象
		List<Persistable> changedAfterList = changeSerive.getChangedAfterObjList((Change) dataObject);
		for (Persistable changeAfter : changedAfterList) {

			if (changeAfter == null) {
				continue;
			}

			// 是否满足首选项配置，若不是则不发放
			String objOid = Helper.getOid(changeAfter);
			String isControling[] = objService.isTransferControlling(objOid).split(",");
			// 当配置的首先项时不是整体分发时
			if ("false".equals(optionValue)) {
				if ("false".equals(isControling[0])) {
					continue;
				}
				// 该对象加入到更改单相关对象收集结果List
				collectECOResList.add(changeAfter);
			} else {
				if ("false".equals(isControling[0])) {
					//如果首选项选择的是整体分发，子对象有一个不符合发放条件的则清空list
					collectECOResList.clear();
					break;
				}
				// 该对象加入到更改单相关对象收集结果List
				collectECOResList.add(changeAfter);
			}
		}
		return collectECOResList;

	}
	
	
	/**
	 * 收集转阶段更改单相关对象。 (1)转阶段更改单 (2)改后对象
	 * 
	 * @param dataObject
	 *            Persistable
	 * @return List<Persistable>
	 */
	private static List<Persistable> collectPPCORefrenceObject(
			Persistable dataObject) {
		// 更改单相关对象收集结果List
		PPCO dataObjectPPCO = (PPCO) dataObject;
		Context context = dataObjectPPCO.getContextInfo().getContext();
		// 分发策略（是否是整体分发）
		OptionValue value = OptionHelper.getService().getOptionValue(context,
				"disMange_disStrategy_allDis");
		String optionValue = value.getValue();

		List<Persistable> collectPPCOResList = new ArrayList<Persistable>();

		// 更改单加入到更改单相关对象收集结果List
		// 先判断更改单本身符不符合首选项配置发放条件
		String ppcoOid = Helper.getOid(dataObject);
		String ecoControling[] = objService.isTransferControlling(ppcoOid).split(",");
		if ("false".equals(ecoControling[0])) {
		} else {
			collectPPCOResList.add(dataObject);
		}
		ChangeService changeSerive = Helper.getChangeService();

		// 更改后对象
		List<Persistable> changedAfterList = changeSerive.getChangedAfterObjList((Change) dataObject);
		for (Persistable changeAfter : changedAfterList) {

			if (changeAfter == null) {
				continue;
			}

			// 是否满足首选项配置，若不是则不发放
			String objOid = Helper.getOid(changeAfter);
			String isControling[] = objService.isTransferControlling(objOid).split(",");
			// 当配置的首先项时不是整体分发时
			if ("false".equals(optionValue)) {
				if ("false".equals(isControling[0])) {
					continue;
				}
				// 该对象加入到更改单相关对象收集结果List
				collectPPCOResList.add(changeAfter);
			} else {
				if ("false".equals(isControling[0])) {
					//如果首选项选择的是整体分发，子对象有一个不符合发放条件的则清空list
					collectPPCOResList.clear();
					break;
				}
				// 该对象加入到更改单相关对象收集结果List
				collectPPCOResList.add(changeAfter);
			}
		}
		return collectPPCOResList;

	}

	/**
	 * 收集部件相关对象。 (1)部件
	 * 
	 * @param dataObject
	 *            Persistable
	 * @return List<Persistable>
	 */
	private static List<Persistable> collectPartfrenceObject(
			Persistable dataObject) {
		// 部件相关对象收集结果List
		List<Persistable> collectPartRefResList = new ArrayList<Persistable>();

		// 部件加入到部件相关对象收集结果List
		// 判断部件符不符合首选项发放状态条件
		String partOid = Helper.getOid(dataObject);
		String partControling[] = objService.isTransferControlling(partOid)
				.split(",");
		if ("false".equals(partControling[0])) {
		} else {
			collectPartRefResList.add(dataObject);
		}
		return collectPartRefResList;
	}

	/**
	 * 收集文档相关对象。 (1)文档
	 * 
	 * @param dataObject
	 *            Persistable
	 * @return List<Persistable>
	 */
	private static List<Persistable> collectDocumentRefrenceObject(
			Persistable dataObject) {
		// 文档相关对象收集结果List
		List<Persistable> collectDocumentRefResList = new ArrayList<Persistable>();

		// 文档加入到文档相关对象收集结果List
		// 判断文档符不符合首选项发放状态条件
		String docOid = Helper.getOid(dataObject);
		String docControling[] = objService.isTransferControlling(docOid)
				.split(",");
		if ("false".equals(docControling[0])) {
		} else {
			collectDocumentRefResList.add(dataObject);
		}
		return collectDocumentRefResList;
	}

	/**
	 * 收集CAD文档相关对象。 (1)CAD文档
	 * 
	 * @param dataObject
	 * @return
	 */
	private static List<Persistable> collectCADDocumentRefrenceObject(
			Persistable dataObject) {
		// CAD文档相关对象收集结果List
		List<Persistable> collectCADDocumentRefResList = new ArrayList<Persistable>();

		// CAD文档加入到文档相关对象收集结果List
		// 判断CAD文档符不符合首选项发放状态条件
		String caddocOid = Helper.getOid(dataObject);
		String caddocControling[] = objService.isTransferControlling(caddocOid)
				.split(",");
		if ("false".equals(caddocControling[0])) {
		} else {
			collectCADDocumentRefResList.add(dataObject);
		}
		return collectCADDocumentRefResList;
	}

	/** 收集对象是更改单标识 */
	public static int getIsECOCollect() {
		return collectFlag;
	}

	/**
	 * 收集管理基线内容对象。
	 * 
	 * (1)基线内容
	 * 
	 * @param dataObject
	 *            Persistable
	 * @return List<Persistable>
	 */
	private static List<Persistable> collectManagedBaselineRefrenceObject(
			Persistable dataObject) {
		// 管理基线相关对象收集结果List
		List<Persistable> collectManagedBaselineResList = new ArrayList<Persistable>();
		// 获取基线内容对象
		List<Baselined> baselinedList = BaselineHelper.getService()
				.getBaselineItems((ManagedBaseline) dataObject);
		ManagedBaseline dataObjectBaseline = (ManagedBaseline) dataObject;
		Context context = dataObjectBaseline.getContextInfo().getContext();
		OptionValue value = OptionHelper.getService().getOptionValue(context,
				"disMange_disStrategy_allDis");
		String optionValue = value.getValue();
		for (Persistable baselined : baselinedList) {
			if (baselined == null) {
				continue;
			}
			// 是否满足首选项配置，若不是则不发放
			String objOid = Helper.getOid(baselined);
			String isControling[] = objService.isTransferControlling(objOid)
					.split(",");
			if ("false".equals(optionValue)) {
				if ("false".equals(isControling[0])) {
					continue;
				}
			} else {
				if ("false".equals(isControling[0])) {
					//如果首选项选择的是整体分发，子对象有一个不符合发放条件的则清空list
					collectManagedBaselineResList.clear();
					break;
				}
			}
			// 该对象加入到管理基线相关对象收集结果List
			collectManagedBaselineResList.add(baselined);
		}
		return collectManagedBaselineResList;
	}

	/**
	 * 收集技术通知单相关对象。
	 * 
	 * (1)技术通知单 (2)偏离后对象
	 * 
	 * @param dataObject
	 *            Persistable
	 * @return List<Persistable>
	 */
	private static List<Persistable> collectTNORefrenceObject(
			Persistable dataObject) {
		// 技术通知单相关对象收集结果List
		List<Persistable> collectTNOResList = new ArrayList<Persistable>();
		TNO dataObjectTNO = (TNO) dataObject;
		Context context = dataObjectTNO.getContextInfo().getContext();
		OptionValue value = OptionHelper.getService().getOptionValue(context,
				"disMange_disStrategy_allDis");
		String optionValue = value.getValue();
		// 技术通知单加入到技术通知单相关对象收集结果List
		// 判断技术通知单符不符合首选项发放状态条件
		String TNOOid = Helper.getOid(dataObject);
		String TNOControling[] = objService.isTransferControlling(TNOOid)
				.split(",");
		if ("false".equals(TNOControling[0])) {
		} else {
			collectTNOResList.add(dataObject);
		}
		ChangeService changeSerive = Helper.getChangeService();
		// 偏离后对象
		List<Persistable> waivedAfterList = changeSerive
				.getWaivedAfterObjList((Change) dataObject);
		for (Persistable waivedAfter : waivedAfterList) {
			if (waivedAfter == null) {
				continue;
			}
			// 是否满足首选项配置，若不是则不发放
			String objOid = Helper.getOid(waivedAfter);
			String isControling[] = objService.isTransferControlling(objOid)
					.split(",");
			if ("false".equals(optionValue)) {
				if ("false".equals(isControling[0])) {
					continue;
				}
			} else {
				if ("false".equals(isControling[0])) {
					//如果首选项选择的是整体分发，子对象有一个不符合发放条件的则清空list
					collectTNOResList.clear();
					break;
				}
				collectTNOResList.add(waivedAfter);
			}
			// 该对象加入到更改单相关对象收集结果List
			collectTNOResList.add(waivedAfter);
		}
		return collectTNOResList;

	}

	/**
	 * 收集超差代料质疑单相关对象。
	 * 
	 * (1)超差代料质疑单 (2)超差对象
	 * 
	 * @param dataObject
	 *            Persistable
	 * @return List<Persistable>
	 */
	private static List<Persistable> collectVarianceRefrenceObject(
			Persistable dataObject) {
		// 超差代料质疑单相关对象收集结果List
		List<Persistable> collectVarianceResList = new ArrayList<Persistable>();

		// 超差代料质疑单加入到超差代料质疑单相关对象收集结果List
		// 判断超差代料质疑单符不符合首选项发放状态条件
		String varOid = Helper.getOid(dataObject);
		String varControling[] = objService.isTransferControlling(varOid)
				.split(",");
		if ("false".equals(varControling[0])) {
		} else {
			collectVarianceResList.add(dataObject);
		}
		ChangeService changeSerive = Helper.getChangeService();

		// 超差代料质疑单和超差对象Link
		List<VariancedLink> variancedLinkList = changeSerive
				.getVariancedList((Change) dataObject);
		Variance dataObjectVariance = (Variance) dataObject;
		Context context = dataObjectVariance.getContextInfo().getContext();
		OptionValue value = OptionHelper.getService().getOptionValue(context,
				"disMange_disStrategy_allDis");
		String optionValue = value.getValue();
		for (VariancedLink variancedLink : variancedLinkList) {
			if (variancedLink == null) {
				continue;
			}
			// 超差对象
			Persistable varianced = variancedLink.getVarianced();
			if (varianced == null) {
				continue;
			}
			// 是否满足首选项配置，若不是则不发放
			String objOid = Helper.getOid(varianced);
			String isControling[] = objService.isTransferControlling(objOid)
					.split(",");
			if ("false".equals(optionValue)) {
				if ("false".equals(isControling[0])) {
					continue;
				}
			} else {
				if ("false".equals(isControling[0])) {
					//如果首选项选择的是整体分发，子对象有一个不符合发放条件的则清空list
					collectVarianceResList.clear();
					break;
				}

			}
			// 该对象加入到更改单相关对象收集结果List
			collectVarianceResList.add(varianced);
		}
		return collectVarianceResList;
	}

	/**
	 * 收集现行文件相关对象。 (1)现行文件
	 * 
	 * @param dataObject
	 *            Persistable
	 * @return List<Persistable>
	 */
	private static List<Persistable> collectActiveDocumentObject(
			Persistable dataObject) {
		// 现行文件相关对象收集结果List
		List<Persistable> collectActiveDocumentRefResList = new ArrayList<Persistable>();

		// 现行文件加入到现行文件相关对象收集结果List
		// 判断现行文件符不符合首选项发放状态条件
		String adOid = Helper.getOid(dataObject);
		String adControling[] = objService.isTransferControlling(adOid).split(
				",");
		if ("false".equals(adControling[0])) {
		} else {
			collectActiveDocumentRefResList.add(dataObject);
		}
		return collectActiveDocumentRefResList;
	}

	/**
	 * 收集现行套对象。
	 * 
	 * (1)现行套相关对象
	 * 
	 * @param dataObject
	 *            Persistable
	 * @return List<Persistable>
	 */
	private static List<Persistable> collectActiveSetRefrenceObject(
			Persistable dataObject) {
		// 现行套相关对象收集结果List
		List<Persistable> collectActiveSetResList = new ArrayList<Persistable>();

		// 获取现行套相关对象
		List<ActiveSeted> activeSetedList = AdmHelper.getActiveSetService()
				.getActiveItems((ActiveObjSet) dataObject);

		for (Persistable activeSeted : activeSetedList) {
			if (activeSeted == null) {
				continue;
			}

			// 是否满足首选项配置，若不是则不发放
			String objOid = Helper.getOid(activeSeted);
			String isControling[] = objService.isTransferControlling(objOid)
					.split(",");
			if ("false".equals(isControling[0])) {
				continue;
			}

			// 该对象加入到管理基线相关对象收集结果List
			collectActiveSetResList.add(activeSeted);
		}

		return collectActiveSetResList;
	}

	/**
	 * 收集现行单据相关对象。 (1)现行单据 (2)改后对象
	 * 
	 * @param dataObject
	 *            Persistable
	 * @return List<Persistable>
	 */
	private static List<Persistable> collectActiveOrderRefrenceObject(
			Persistable dataObject) {
		// 现行单据相关对象收集结果List
		List<Persistable> collectActiveOrderResList = new ArrayList<Persistable>();

		// 现行单据加入到现行单据相关对象收集结果List
		// 判断现行单据符不符合首选项发放状态条件
		String aoOid = Helper.getOid(dataObject);
		String aoControling[] = objService.isTransferControlling(aoOid).split(
				",");
		if ("false".equals(aoControling[0])) {
		} else {
			collectActiveOrderResList.add(dataObject);
		}
		// 更改后对象
		List<ActiveOrdered> changedAfterList = AdmHelper
				.getActiveOrderService().getAfterItems(
						(ActiveObjOrder) dataObject);
		for (Persistable changeAfter : changedAfterList) {

			if (changeAfter == null) {
				continue;
			}

			// 是否满足首选项配置，若不是则不发放
			String objOid = Helper.getOid(changeAfter);
			String isControling[] = objService.isTransferControlling(objOid)
					.split(",");
			if ("false".equals(isControling[0])) {
				continue;
			}

			// 该对象加入到现行单据相关对象收集结果List
			collectActiveOrderResList.add(changeAfter);
		}
		return collectActiveOrderResList;
	}

	/**
	 * 收集套对象相关对象。 (1)套对象
	 * 
	 * @param dataObject
	 *            Persistable
	 * @return List<Persistable>
	 */
	private static List<Persistable> collectSuitRefrenceObject(
			Persistable dataObject, int collectFlag) {
		ATSuit dataObjectSuited = (ATSuit) dataObject;
		Context context = dataObjectSuited.getContextInfo().getContext();
		// 分发策略（是否是整体分发）
		OptionValue value = OptionHelper.getService().getOptionValue(context,
				"disMange_disStrategy_allDis");
		String optionValue = value.getValue();

		// 套对象相关对象收集结果List
		List<Persistable> collectSuitRefResList = new ArrayList<Persistable>();
		// 套对象加入到套对象相关对象收集结果List
		// 判断套对象符不符合首选项发放状态条件
		String suitOid = Helper.getOid(dataObject);
		String suitControling[] = objService.isTransferControlling(suitOid)
				.split(",");
		if ("false".equals(suitControling[0])) {
		} else {
			collectSuitRefResList.add(dataObject);
		}
		// 获取套对象的内容对象
		List<Suited> suitedList = SuitHelper.getService().getSuitItems(
				(ATSuit) dataObject);
		for (Persistable suited : suitedList) {
			if (suited == null) {
				continue;
			}
			// 是否满足首选项配置，若不是则不发放
			String objOid = Helper.getOid(suited);
			String isControling[] = objService.isTransferControlling(objOid)
					.split(",");
			// 当配置的首先项时不是整体分发时
			if ("false".equals(optionValue)) {
				if ("false".equals(isControling[0])) {
					continue;
				}
				// 当配置的首选项中是整体分发时
			} else {
				// 套内对象中有一个不满足首选项配置的对象则不发放
				if ("false".equals(isControling[0])) {
					//如果首选项选择的是整体分发，子对象有一个不符合发放条件的则清空list
					collectSuitRefResList.clear();
					break;
				}
			}
			if (collectFlag == ConstUtil.COLLECTFLAG_0) {// A版本的套对象
				// 该对象加入到套对象相关对象收集结果List
				collectSuitRefResList.add(suited);
			} else {// A版本以外的套对象
				// 根据当前分发数据源对象取得分发数据对象List
				List<DistributeObject> disObjList = objService
						.getDistributeObjectsByDataOid(dataObject.getClassId()
								+ ":" + dataObject.getInnerId());
				// 未发放过
				if (disObjList == null || disObjList.isEmpty()) {
					ATSuitMemberLink link = SuitHelper.getService()
							.getSuitMemberLink((Suit) dataObject,
									(Suited) suited);
					// 更改类别是""的套内对象不添加到分发数据中
					if (link != null && link.getSuitChangeType() != null
							&& !"".equals(link.getSuitChangeType())) {
						// 该对象加入到套对象相关对象收集结果List
						collectSuitRefResList.add(suited);
						// 套内对象是可被更改对象，并且更改类别是“更新”时，将其关联的更改单也添加到发放单中
						if (suited instanceof Changed) {
							if (link != null
									&& SuitCoreContants.UPDATE_TYPE.equals(link
											.getSuitChangeType())) {
								List<ECO> listAfter = Helper.getChangeService()
										.getRelatedECOList(suited);
								boolean flag = false;
								if (listAfter != null && listAfter.size() > 0) {
									for (int i = 0; i < collectSuitRefResList
											.size(); i++) {
										String collectOid = Helper
												.getOid(collectSuitRefResList
														.get(i));
										String afterOid = Helper
												.getOid(listAfter.get(0));
										if (collectOid.equals(afterOid)) {
											flag = true;
											break;
										}
									}
									if (!flag) {
										collectSuitRefResList.add(listAfter
												.get(0));
									}
								}
							}
						}
					}
				} else {
					// 发放过
					// 该对象加入到套对象相关对象收集结果List
					collectSuitRefResList.add(suited);
				}
			}

		}
		return collectSuitRefResList;
	}

}
