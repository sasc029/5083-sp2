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
 * ��ض����ռ����� (1)�ĵ����ռ��ĵ����� (2)�������ռ��������� (3)���󵥣��ռ��������(��������ǹ������ʱ,���ռ�)
 * (4)���ĵ����ռ����ĵ��͸ĺ���� (5)�������,�ռ��������ݶ��� (6)����֪ͨ��,�ռ�����֪ͨ����ƫ������
 * (7)����������ɵ�,�ռ�����������ɵ��ͳ������ (8)�����ļ�,�ռ������ļ����� (9)������,�ռ���������ض���
 * (10)���е���,�ռ����е��ݺ͸ĺ����
 * 
 * @author yanjia 2013-4-27
 */
public class CollectReferenceObject {
	/** �ַ�������� */
	private static DistributeObjectService objService = DistributeHelper
			.getDistributeObjectService();
	/** �ռ������Ǹ��ĵ���ʶ */
	private static int collectFlag;

	/**
	 * ���췽����
	 */
	private CollectReferenceObject() {

	}

	/**
	 * �ռ���ض���
	 * 
	 * @param dataObject
	 *            Persistable
	 * @return List<Persistable>
	 */
	public static List<Persistable> collectRefObject(Persistable dataObject) {
		// �ռ����List
		List<Persistable> collectResultList = new ArrayList<Persistable>();

		// ����
		if (dataObject instanceof ApproveOrder) {
			collectFlag = ConstUtil.COLLECTFLAG_0;
			// �ռ�������ض���
			List<Persistable> collectApproveOrderResList = collectApproveOrderRefrenceObject(dataObject);
			// �ռ����List
				collectResultList.addAll(collectApproveOrderResList);
			// ���ĵ�
		} else if (dataObject instanceof ECO) {
			collectFlag = ConstUtil.COLLECTFLAG_1;
			// ���ĵ���ض����ռ����List
			List<Persistable> collectECOResList = collectECORefrenceObject(dataObject);
			// �ռ����List
				collectResultList.addAll(collectECOResList);
			// ����
		} else if (dataObject instanceof Part) {//
			collectFlag = ConstUtil.COLLECTFLAG_0;
			// ������ض����ռ����List
			List<Persistable> collectPartRefResList = collectPartfrenceObject(dataObject);
			// �ռ����List
			collectResultList.addAll(collectPartRefResList);
			// �ĵ�
		} else if (dataObject instanceof Document) {//
			collectFlag = ConstUtil.COLLECTFLAG_0;
			// �ĵ���ض����ռ����List
			List<Persistable> collectDocumentResList = collectDocumentRefrenceObject(dataObject);
			// �ռ����List
			collectResultList.addAll(collectDocumentResList);
			// CADDocument
		} else if (dataObject instanceof CADDocument) {
			collectFlag = ConstUtil.COLLECTFLAG_0;
			// CAD�ĵ���ض����ռ����List
			List<Persistable> collectCADDocumentResList = collectCADDocumentRefrenceObject(dataObject);
			// �ռ����List
			collectResultList.addAll(collectCADDocumentResList);

			// �������
		} else if (dataObject instanceof ManagedBaseline) {
			collectFlag = ConstUtil.COLLECTFLAG_0;
			// ���������ض����ռ����List
			List<Persistable> collectManagedBaselineResList = collectManagedBaselineRefrenceObject(dataObject);
			// �ռ����List
				collectResultList.addAll(collectManagedBaselineResList);
			// ����֪ͨ��
		} else if (dataObject instanceof TNO) {
			collectFlag = ConstUtil.COLLECTFLAG_0;
			// ����֪ͨ����ض����ռ����List
			List<Persistable> collectTNOResList = collectTNORefrenceObject(dataObject);
			// �ռ����List
				collectResultList.addAll(collectTNOResList);
			// ����������ɵ�
		} else if (dataObject instanceof Variance) {
			collectFlag = ConstUtil.COLLECTFLAG_0;
			// ����������ɵ���ض����ռ����List
			List<Persistable> collectVarianceResList = collectVarianceRefrenceObject(dataObject);
			// �ռ����List
				collectResultList.addAll(collectVarianceResList);
			// �����ļ�,�ռ������ļ�����
		} else if (dataObject instanceof ActiveDocument) {//
			collectFlag = ConstUtil.COLLECTFLAG_0;
			// �����ļ��ռ����List
			List<Persistable> collectActiveDocumentList = collectActiveDocumentObject(dataObject);
			// �ռ����List
			collectResultList.addAll(collectActiveDocumentList);
			// ������,�ռ���������ض���
		} else if (dataObject instanceof ActiveSet) {
			collectFlag = ConstUtil.COLLECTFLAG_0;
			// �����ļ��ռ����List
			List<Persistable> collectActiveSetList = collectActiveSetRefrenceObject(dataObject);
			// �ռ����List
			collectResultList.addAll(collectActiveSetList);
			// ���е���,�ռ����е��ݺ͸ĺ����
		} else if (dataObject instanceof ActiveOrder) {
			collectFlag = ConstUtil.COLLECTFLAG_2;
			// ���е�����ض����ռ����List
			List<Persistable> collectActiveOrderResList = collectActiveOrderRefrenceObject(dataObject);
			// �ռ����List
			collectResultList.addAll(collectActiveOrderResList);
		} else if (dataObject instanceof ATSuit) {
			ATSuit atSuit = (ATSuit) dataObject;
			if ("A".equals(atSuit.getIterationInfo().getVersionNo())) {
				collectFlag = ConstUtil.COLLECTFLAG_0;
			} else {
				collectFlag = ConstUtil.COLLECTFLAG_3;
			}
			// �׶�����ض����ռ����List
			List<Persistable> collectSuitResList = collectSuitRefrenceObject(
					dataObject, collectFlag);
			// �ռ����List
				collectResultList.addAll(collectSuitResList);
			// û����ض����������
		} else if (dataObject instanceof PPCO) {
			collectFlag = ConstUtil.COLLECTFLAG_1;
			// ���ĵ���ض����ռ����List
		//	List<Persistable> collectECOResList = collectECORefrenceObject(dataObject);
			List<Persistable> collectPPCOResList = collectPPCORefrenceObject(dataObject);
			// �ռ����List
				collectResultList.addAll(collectPPCOResList);
			// ����
		} else {
			collectFlag = ConstUtil.COLLECTFLAG_0;
			// ��û����ض�����࣬ͳһֱ�Ӽӵ����List��
			collectResultList.add(dataObject);
		}
		return collectResultList;
	}

	/**
	 * �ռ�������ض��� (1)�������
	 * 
	 * @param dataObject
	 *            Persistable
	 * @return List<Persistable>
	 */
	private static List<Persistable> collectApproveOrderRefrenceObject(
			Persistable dataObject) {
		// ������ض����ռ����List
		List<Persistable> collectApproveOrderRefResList = new ArrayList<Persistable>();

		if (!(dataObject instanceof ApproveOrder)) {
			throw new PlmException("Object[" + Helper.getOid(dataObject)
					+ "] is not ApproveOrder");
		}
		// �������List
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
			// ��������ǹ�����ߣ����ռ�
			if (pt instanceof ManagedBaseline) {
				continue;
			}
			// �Ƿ�������ѡ�����ã����������ռ�
			String objOid = Helper.getOid(pt);

			String isControling[] = objService.isTransferControlling(objOid)
					.split(",");
			if ("false".equals(optionValue)) {
				if ("false".equals(isControling[0])) {
					continue;
				}
				// �ö�����뵽������ض����ռ����List��
				collectApproveOrderRefResList.add(pt);
			} else {
				if ("false".equals(isControling[0])) {
					//�����ѡ��ѡ���������ַ����Ӷ�����һ�������Ϸ��������������list
					collectApproveOrderRefResList.clear();
					break;
				}
				// �ö�����뵽������ض����ռ����List��
				collectApproveOrderRefResList.add(pt);
			}
		}
		return collectApproveOrderRefResList;
	}

	/**
	 * �ռ����ĵ���ض��� (1)���ĵ� (2)�ĺ����
	 * 
	 * @param dataObject
	 *            Persistable
	 * @return List<Persistable>
	 */
	private static List<Persistable> collectECORefrenceObject(
			Persistable dataObject) {
		// ���ĵ���ض����ռ����List
		ECO dataObjectECO = (ECO) dataObject;
		Context context = dataObjectECO.getContextInfo().getContext();
		// �ַ����ԣ��Ƿ�������ַ���
		OptionValue value = OptionHelper.getService().getOptionValue(context,
				"disMange_disStrategy_allDis");
		String optionValue = value.getValue();

		List<Persistable> collectECOResList = new ArrayList<Persistable>();

		// ���ĵ����뵽���ĵ���ض����ռ����List
		// ���жϸ��ĵ��������������ѡ�����÷�������
		String ecoOid = Helper.getOid(dataObject);
		String ecoControling[] = objService.isTransferControlling(ecoOid).split(",");
		if ("false".equals(ecoControling[0])) {
		} else {
			collectECOResList.add(dataObject);
		}
		ChangeService changeSerive = Helper.getChangeService();

		// ���ĺ����
		List<Persistable> changedAfterList = changeSerive.getChangedAfterObjList((Change) dataObject);
		for (Persistable changeAfter : changedAfterList) {

			if (changeAfter == null) {
				continue;
			}

			// �Ƿ�������ѡ�����ã��������򲻷���
			String objOid = Helper.getOid(changeAfter);
			String isControling[] = objService.isTransferControlling(objOid).split(",");
			// �����õ�������ʱ��������ַ�ʱ
			if ("false".equals(optionValue)) {
				if ("false".equals(isControling[0])) {
					continue;
				}
				// �ö�����뵽���ĵ���ض����ռ����List
				collectECOResList.add(changeAfter);
			} else {
				if ("false".equals(isControling[0])) {
					//�����ѡ��ѡ���������ַ����Ӷ�����һ�������Ϸ��������������list
					collectECOResList.clear();
					break;
				}
				// �ö�����뵽���ĵ���ض����ռ����List
				collectECOResList.add(changeAfter);
			}
		}
		return collectECOResList;

	}
	
	
	/**
	 * �ռ�ת�׶θ��ĵ���ض��� (1)ת�׶θ��ĵ� (2)�ĺ����
	 * 
	 * @param dataObject
	 *            Persistable
	 * @return List<Persistable>
	 */
	private static List<Persistable> collectPPCORefrenceObject(
			Persistable dataObject) {
		// ���ĵ���ض����ռ����List
		PPCO dataObjectPPCO = (PPCO) dataObject;
		Context context = dataObjectPPCO.getContextInfo().getContext();
		// �ַ����ԣ��Ƿ�������ַ���
		OptionValue value = OptionHelper.getService().getOptionValue(context,
				"disMange_disStrategy_allDis");
		String optionValue = value.getValue();

		List<Persistable> collectPPCOResList = new ArrayList<Persistable>();

		// ���ĵ����뵽���ĵ���ض����ռ����List
		// ���жϸ��ĵ��������������ѡ�����÷�������
		String ppcoOid = Helper.getOid(dataObject);
		String ecoControling[] = objService.isTransferControlling(ppcoOid).split(",");
		if ("false".equals(ecoControling[0])) {
		} else {
			collectPPCOResList.add(dataObject);
		}
		ChangeService changeSerive = Helper.getChangeService();

		// ���ĺ����
		List<Persistable> changedAfterList = changeSerive.getChangedAfterObjList((Change) dataObject);
		for (Persistable changeAfter : changedAfterList) {

			if (changeAfter == null) {
				continue;
			}

			// �Ƿ�������ѡ�����ã��������򲻷���
			String objOid = Helper.getOid(changeAfter);
			String isControling[] = objService.isTransferControlling(objOid).split(",");
			// �����õ�������ʱ��������ַ�ʱ
			if ("false".equals(optionValue)) {
				if ("false".equals(isControling[0])) {
					continue;
				}
				// �ö�����뵽���ĵ���ض����ռ����List
				collectPPCOResList.add(changeAfter);
			} else {
				if ("false".equals(isControling[0])) {
					//�����ѡ��ѡ���������ַ����Ӷ�����һ�������Ϸ��������������list
					collectPPCOResList.clear();
					break;
				}
				// �ö�����뵽���ĵ���ض����ռ����List
				collectPPCOResList.add(changeAfter);
			}
		}
		return collectPPCOResList;

	}

	/**
	 * �ռ�������ض��� (1)����
	 * 
	 * @param dataObject
	 *            Persistable
	 * @return List<Persistable>
	 */
	private static List<Persistable> collectPartfrenceObject(
			Persistable dataObject) {
		// ������ض����ռ����List
		List<Persistable> collectPartRefResList = new ArrayList<Persistable>();

		// �������뵽������ض����ռ����List
		// �жϲ�������������ѡ���״̬����
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
	 * �ռ��ĵ���ض��� (1)�ĵ�
	 * 
	 * @param dataObject
	 *            Persistable
	 * @return List<Persistable>
	 */
	private static List<Persistable> collectDocumentRefrenceObject(
			Persistable dataObject) {
		// �ĵ���ض����ռ����List
		List<Persistable> collectDocumentRefResList = new ArrayList<Persistable>();

		// �ĵ����뵽�ĵ���ض����ռ����List
		// �ж��ĵ�����������ѡ���״̬����
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
	 * �ռ�CAD�ĵ���ض��� (1)CAD�ĵ�
	 * 
	 * @param dataObject
	 * @return
	 */
	private static List<Persistable> collectCADDocumentRefrenceObject(
			Persistable dataObject) {
		// CAD�ĵ���ض����ռ����List
		List<Persistable> collectCADDocumentRefResList = new ArrayList<Persistable>();

		// CAD�ĵ����뵽�ĵ���ض����ռ����List
		// �ж�CAD�ĵ�����������ѡ���״̬����
		String caddocOid = Helper.getOid(dataObject);
		String caddocControling[] = objService.isTransferControlling(caddocOid)
				.split(",");
		if ("false".equals(caddocControling[0])) {
		} else {
			collectCADDocumentRefResList.add(dataObject);
		}
		return collectCADDocumentRefResList;
	}

	/** �ռ������Ǹ��ĵ���ʶ */
	public static int getIsECOCollect() {
		return collectFlag;
	}

	/**
	 * �ռ�����������ݶ���
	 * 
	 * (1)��������
	 * 
	 * @param dataObject
	 *            Persistable
	 * @return List<Persistable>
	 */
	private static List<Persistable> collectManagedBaselineRefrenceObject(
			Persistable dataObject) {
		// ���������ض����ռ����List
		List<Persistable> collectManagedBaselineResList = new ArrayList<Persistable>();
		// ��ȡ�������ݶ���
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
			// �Ƿ�������ѡ�����ã��������򲻷���
			String objOid = Helper.getOid(baselined);
			String isControling[] = objService.isTransferControlling(objOid)
					.split(",");
			if ("false".equals(optionValue)) {
				if ("false".equals(isControling[0])) {
					continue;
				}
			} else {
				if ("false".equals(isControling[0])) {
					//�����ѡ��ѡ���������ַ����Ӷ�����һ�������Ϸ��������������list
					collectManagedBaselineResList.clear();
					break;
				}
			}
			// �ö�����뵽���������ض����ռ����List
			collectManagedBaselineResList.add(baselined);
		}
		return collectManagedBaselineResList;
	}

	/**
	 * �ռ�����֪ͨ����ض���
	 * 
	 * (1)����֪ͨ�� (2)ƫ������
	 * 
	 * @param dataObject
	 *            Persistable
	 * @return List<Persistable>
	 */
	private static List<Persistable> collectTNORefrenceObject(
			Persistable dataObject) {
		// ����֪ͨ����ض����ռ����List
		List<Persistable> collectTNOResList = new ArrayList<Persistable>();
		TNO dataObjectTNO = (TNO) dataObject;
		Context context = dataObjectTNO.getContextInfo().getContext();
		OptionValue value = OptionHelper.getService().getOptionValue(context,
				"disMange_disStrategy_allDis");
		String optionValue = value.getValue();
		// ����֪ͨ�����뵽����֪ͨ����ض����ռ����List
		// �жϼ���֪ͨ������������ѡ���״̬����
		String TNOOid = Helper.getOid(dataObject);
		String TNOControling[] = objService.isTransferControlling(TNOOid)
				.split(",");
		if ("false".equals(TNOControling[0])) {
		} else {
			collectTNOResList.add(dataObject);
		}
		ChangeService changeSerive = Helper.getChangeService();
		// ƫ������
		List<Persistable> waivedAfterList = changeSerive
				.getWaivedAfterObjList((Change) dataObject);
		for (Persistable waivedAfter : waivedAfterList) {
			if (waivedAfter == null) {
				continue;
			}
			// �Ƿ�������ѡ�����ã��������򲻷���
			String objOid = Helper.getOid(waivedAfter);
			String isControling[] = objService.isTransferControlling(objOid)
					.split(",");
			if ("false".equals(optionValue)) {
				if ("false".equals(isControling[0])) {
					continue;
				}
			} else {
				if ("false".equals(isControling[0])) {
					//�����ѡ��ѡ���������ַ����Ӷ�����һ�������Ϸ��������������list
					collectTNOResList.clear();
					break;
				}
				collectTNOResList.add(waivedAfter);
			}
			// �ö�����뵽���ĵ���ض����ռ����List
			collectTNOResList.add(waivedAfter);
		}
		return collectTNOResList;

	}

	/**
	 * �ռ�����������ɵ���ض���
	 * 
	 * (1)����������ɵ� (2)�������
	 * 
	 * @param dataObject
	 *            Persistable
	 * @return List<Persistable>
	 */
	private static List<Persistable> collectVarianceRefrenceObject(
			Persistable dataObject) {
		// ����������ɵ���ض����ռ����List
		List<Persistable> collectVarianceResList = new ArrayList<Persistable>();

		// ����������ɵ����뵽����������ɵ���ض����ռ����List
		// �жϳ���������ɵ�����������ѡ���״̬����
		String varOid = Helper.getOid(dataObject);
		String varControling[] = objService.isTransferControlling(varOid)
				.split(",");
		if ("false".equals(varControling[0])) {
		} else {
			collectVarianceResList.add(dataObject);
		}
		ChangeService changeSerive = Helper.getChangeService();

		// ����������ɵ��ͳ������Link
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
			// �������
			Persistable varianced = variancedLink.getVarianced();
			if (varianced == null) {
				continue;
			}
			// �Ƿ�������ѡ�����ã��������򲻷���
			String objOid = Helper.getOid(varianced);
			String isControling[] = objService.isTransferControlling(objOid)
					.split(",");
			if ("false".equals(optionValue)) {
				if ("false".equals(isControling[0])) {
					continue;
				}
			} else {
				if ("false".equals(isControling[0])) {
					//�����ѡ��ѡ���������ַ����Ӷ�����һ�������Ϸ��������������list
					collectVarianceResList.clear();
					break;
				}

			}
			// �ö�����뵽���ĵ���ض����ռ����List
			collectVarianceResList.add(varianced);
		}
		return collectVarianceResList;
	}

	/**
	 * �ռ������ļ���ض��� (1)�����ļ�
	 * 
	 * @param dataObject
	 *            Persistable
	 * @return List<Persistable>
	 */
	private static List<Persistable> collectActiveDocumentObject(
			Persistable dataObject) {
		// �����ļ���ض����ռ����List
		List<Persistable> collectActiveDocumentRefResList = new ArrayList<Persistable>();

		// �����ļ����뵽�����ļ���ض����ռ����List
		// �ж������ļ�����������ѡ���״̬����
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
	 * �ռ������׶���
	 * 
	 * (1)��������ض���
	 * 
	 * @param dataObject
	 *            Persistable
	 * @return List<Persistable>
	 */
	private static List<Persistable> collectActiveSetRefrenceObject(
			Persistable dataObject) {
		// ��������ض����ռ����List
		List<Persistable> collectActiveSetResList = new ArrayList<Persistable>();

		// ��ȡ��������ض���
		List<ActiveSeted> activeSetedList = AdmHelper.getActiveSetService()
				.getActiveItems((ActiveObjSet) dataObject);

		for (Persistable activeSeted : activeSetedList) {
			if (activeSeted == null) {
				continue;
			}

			// �Ƿ�������ѡ�����ã��������򲻷���
			String objOid = Helper.getOid(activeSeted);
			String isControling[] = objService.isTransferControlling(objOid)
					.split(",");
			if ("false".equals(isControling[0])) {
				continue;
			}

			// �ö�����뵽���������ض����ռ����List
			collectActiveSetResList.add(activeSeted);
		}

		return collectActiveSetResList;
	}

	/**
	 * �ռ����е�����ض��� (1)���е��� (2)�ĺ����
	 * 
	 * @param dataObject
	 *            Persistable
	 * @return List<Persistable>
	 */
	private static List<Persistable> collectActiveOrderRefrenceObject(
			Persistable dataObject) {
		// ���е�����ض����ռ����List
		List<Persistable> collectActiveOrderResList = new ArrayList<Persistable>();

		// ���е��ݼ��뵽���е�����ض����ռ����List
		// �ж����е��ݷ���������ѡ���״̬����
		String aoOid = Helper.getOid(dataObject);
		String aoControling[] = objService.isTransferControlling(aoOid).split(
				",");
		if ("false".equals(aoControling[0])) {
		} else {
			collectActiveOrderResList.add(dataObject);
		}
		// ���ĺ����
		List<ActiveOrdered> changedAfterList = AdmHelper
				.getActiveOrderService().getAfterItems(
						(ActiveObjOrder) dataObject);
		for (Persistable changeAfter : changedAfterList) {

			if (changeAfter == null) {
				continue;
			}

			// �Ƿ�������ѡ�����ã��������򲻷���
			String objOid = Helper.getOid(changeAfter);
			String isControling[] = objService.isTransferControlling(objOid)
					.split(",");
			if ("false".equals(isControling[0])) {
				continue;
			}

			// �ö�����뵽���е�����ض����ռ����List
			collectActiveOrderResList.add(changeAfter);
		}
		return collectActiveOrderResList;
	}

	/**
	 * �ռ��׶�����ض��� (1)�׶���
	 * 
	 * @param dataObject
	 *            Persistable
	 * @return List<Persistable>
	 */
	private static List<Persistable> collectSuitRefrenceObject(
			Persistable dataObject, int collectFlag) {
		ATSuit dataObjectSuited = (ATSuit) dataObject;
		Context context = dataObjectSuited.getContextInfo().getContext();
		// �ַ����ԣ��Ƿ�������ַ���
		OptionValue value = OptionHelper.getService().getOptionValue(context,
				"disMange_disStrategy_allDis");
		String optionValue = value.getValue();

		// �׶�����ض����ռ����List
		List<Persistable> collectSuitRefResList = new ArrayList<Persistable>();
		// �׶�����뵽�׶�����ض����ռ����List
		// �ж��׶������������ѡ���״̬����
		String suitOid = Helper.getOid(dataObject);
		String suitControling[] = objService.isTransferControlling(suitOid)
				.split(",");
		if ("false".equals(suitControling[0])) {
		} else {
			collectSuitRefResList.add(dataObject);
		}
		// ��ȡ�׶�������ݶ���
		List<Suited> suitedList = SuitHelper.getService().getSuitItems(
				(ATSuit) dataObject);
		for (Persistable suited : suitedList) {
			if (suited == null) {
				continue;
			}
			// �Ƿ�������ѡ�����ã��������򲻷���
			String objOid = Helper.getOid(suited);
			String isControling[] = objService.isTransferControlling(objOid)
					.split(",");
			// �����õ�������ʱ��������ַ�ʱ
			if ("false".equals(optionValue)) {
				if ("false".equals(isControling[0])) {
					continue;
				}
				// �����õ���ѡ����������ַ�ʱ
			} else {
				// ���ڶ�������һ����������ѡ�����õĶ����򲻷���
				if ("false".equals(isControling[0])) {
					//�����ѡ��ѡ���������ַ����Ӷ�����һ�������Ϸ��������������list
					collectSuitRefResList.clear();
					break;
				}
			}
			if (collectFlag == ConstUtil.COLLECTFLAG_0) {// A�汾���׶���
				// �ö�����뵽�׶�����ض����ռ����List
				collectSuitRefResList.add(suited);
			} else {// A�汾������׶���
				// ���ݵ�ǰ�ַ�����Դ����ȡ�÷ַ����ݶ���List
				List<DistributeObject> disObjList = objService
						.getDistributeObjectsByDataOid(dataObject.getClassId()
								+ ":" + dataObject.getInnerId());
				// δ���Ź�
				if (disObjList == null || disObjList.isEmpty()) {
					ATSuitMemberLink link = SuitHelper.getService()
							.getSuitMemberLink((Suit) dataObject,
									(Suited) suited);
					// ���������""�����ڶ�����ӵ��ַ�������
					if (link != null && link.getSuitChangeType() != null
							&& !"".equals(link.getSuitChangeType())) {
						// �ö�����뵽�׶�����ض����ռ����List
						collectSuitRefResList.add(suited);
						// ���ڶ����ǿɱ����Ķ��󣬲��Ҹ�������ǡ����¡�ʱ����������ĸ��ĵ�Ҳ��ӵ����ŵ���
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
					// ���Ź�
					// �ö�����뵽�׶�����ض����ռ����List
					collectSuitRefResList.add(suited);
				}
			}

		}
		return collectSuitRefResList;
	}

}
