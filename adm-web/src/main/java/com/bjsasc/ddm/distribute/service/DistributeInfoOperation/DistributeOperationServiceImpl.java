package com.bjsasc.ddm.distribute.service.DistributeInfoOperation;

import java.util.ArrayList;
import java.util.List;




import com.bjsasc.ddm.distribute.helper.DistributeHelper;
import com.bjsasc.ddm.distribute.model.distributeinfo.DistributeInfo;
import com.bjsasc.ddm.distribute.model.distributeobject.DistributeObject;
import com.bjsasc.ddm.distribute.model.distributeorder.DistributeOrder;
import com.bjsasc.ddm.distribute.model.distributeorderobjectlink.DistributeOrderObjectLink;
import com.bjsasc.ddm.distribute.service.distributelifecycle.DistributeLifecycleService;
import com.bjsasc.platform.objectmodel.business.persist.PersistUtil;
import com.bjsasc.plm.core.persist.PersistHelper;
import com.bjsasc.plm.core.util.StringUtil;
/**
 * 
 * @author kangyanfei 2014-07-15
 *
 */
public class DistributeOperationServiceImpl implements
		DistributeOperationService {

	/* ���� Javadoc��
	 * @see  com.bjsasc.ddm.distribute.service.DistributeOperation#getDistributeObjectsByDisOrder
	(DistributeOrder disOrder)
	 */
	@Override
	public List<DistributeObject> getDistributeObjectsByDisOrder(DistributeOrder disOrder) {
		List<DistributeObject> disObjects = null;
		//�жϷַ����Ƿ�δ����ʼ����
		if(disOrder != null){
			//��ѯ�ַ�����
			disObjects = DistributeHelper.getDistributeObjectService().getDistributeObjectsByDistributeOrderOid(disOrder.getOid());
		} else {
			disObjects = new ArrayList<DistributeObject>();
		}
		return disObjects;
	}

	/* ���� Javadoc��
	 * @see  com.bjsasc.ddm.distribute.service.DistributeOperation#DistributeOperationService
	(DistributeOrder disOrderOid,DistributeObject disObjOid, List<DistributeInfo> disInfos)
	 */
	@Override
	public void inserDisInfos(DistributeOrder disOrder,
			DistributeObject disObj, List<DistributeInfo> disInfos) {
		//���ŵ���ClassId��InnerId
		String disOrderInnerId = disOrder.getInnerId();
		String disOrderClassId = disOrder.getClassId();
		//�ַ������ClassId��InnerId
		String distributeObjectInnerId = disObj.getInnerId();
		String distributeObjectClassId = disObj.getClassId();

		String hql = "from DistributeOrderObjectLink t "
				+ " where t.fromObjectRef.innerId=? and t.fromObjectRef.classId=? "
				+ " and t.toObjectRef.innerId=? and t.toObjectRef.classId=? ";

		//���������Ҫ����ķַ���Ϣ
		List<DistributeInfo> saveDisInfos = new ArrayList<DistributeInfo>();
		//��ѯ���ŵ��ͷַ����ݼ�Links
		List<DistributeOrderObjectLink> links = PersistHelper.getService().find(hql, disOrderInnerId,
				disOrderClassId, distributeObjectInnerId, distributeObjectClassId);
		if(links.size() > 0){
			String disOrderObjLinkId = links.get(0).getInnerId();
			String disOrderObjLinkClassId = links.get(0).getClassId();
			DistributeLifecycleService life = DistributeHelper.getDistributeLifecycleService();
			for(DistributeInfo disInfo :disInfos){
				DistributeInfo saveDisInfo = (DistributeInfo) PersistUtil.createObject(DistributeInfo.CLASSID);

				//���÷��ŵ���ַ�����LINK���ʶ 
				saveDisInfo.setDisOrderObjLinkClassId(disOrderObjLinkClassId);
				//���÷��ŵ���ַ�����LINK�ڲ���ʶ 
				saveDisInfo.setDisOrderObjLinkId(disOrderObjLinkId);
				//���÷ַ���ϢIID����Ա����֯���ڲ���ʶ��
				if(!StringUtil.isStringEmpty(disInfo.getDisInfoId())){
					saveDisInfo.setDisInfoId(disInfo.getDisInfoId());
				}
				//���÷ַ���Ϣ���ƣ���λ/��Ա��
				if(!StringUtil.isStringEmpty(disInfo.getDisInfoName())){
					saveDisInfo.setDisInfoName(disInfo.getDisInfoName());
				}
				//���÷ַ���Ϣ�����ʶ����Ա������֯�����ʶ��
				if(!StringUtil.isStringEmpty(disInfo.getInfoClassId())){
					saveDisInfo.setInfoClassId(disInfo.getInfoClassId());
				}
				//�������������IID����Ա�ڲ���ʶ��
				if(!StringUtil.isStringEmpty(disInfo.getOutSignId())){
					saveDisInfo.setOutSignId(disInfo.getOutSignId());
				}
				//���� ������������ƣ���Ա��
				if(!StringUtil.isStringEmpty(disInfo.getOutSignName())){
					saveDisInfo.setOutSignName(disInfo.getOutSignName());
				}
				//������������˵����ʶ����Ա���ʶ��
				if(!StringUtil.isStringEmpty(disInfo.getOutSignClassId())){
					saveDisInfo.setOutSignClassId(disInfo.getOutSignClassId());
				}
				//�ַ���Ϣ���ͣ�0Ϊ��λ��1Ϊ��Ա��
				if(!StringUtil.isStringEmpty(disInfo.getDisInfoType())){
					saveDisInfo.setDisInfoType(disInfo.getDisInfoType());
				}
				//���÷ַ����� 
				if(disInfo.getDisInfoNum() > 0){
					saveDisInfo.setDisInfoNum(disInfo.getDisInfoNum());
				}
				//���û��շ��� 
				if(disInfo.getRecoverNum() > 0){
					saveDisInfo.setRecoverNum(disInfo.getRecoverNum());
				}
				//�������ٷ���
				if(disInfo.getDestroyNum() > 0){
					saveDisInfo.setDestroyNum(disInfo.getDestroyNum());
				}
				//���÷ַ��������ͣ�0Ϊֽ�ʣ�1Ϊ���ӣ�2Ϊ����
				if(!StringUtil.isStringEmpty(disInfo.getDisMediaType())){
					saveDisInfo.setDisMediaType(disInfo.getDisMediaType());
				}
				//���÷ַ���ʽ��0Ϊֱ�ӷַ���1Ϊ������2Ϊ�Ƴ���3Ϊת����
				if(!StringUtil.isStringEmpty(disInfo.getDisType())){
					saveDisInfo.setDisType(disInfo.getDisType());
				}
				//������֯���� (0Ϊ�ڲ�,1Ϊ�ⲿ)
				if(!StringUtil.isStringEmpty(disInfo.getSendType())){
					saveDisInfo.setSendType(disInfo.getSendType());
				}
				//���÷ַ���Ϣ��Ϊ�ѷַ�״̬��ʱ��
				if(disInfo.getSendTime() > 0){
					saveDisInfo.setSendTime(disInfo.getSendTime());
				}
				//���ñ�ע
				if(!StringUtil.isStringEmpty(disInfo.getNote())){
					saveDisInfo.setNote(disInfo.getNote());
				}
				//������Ϣ
				if(!StringUtil.isStringEmpty(disInfo.getSealInfo())){
					saveDisInfo.setSealInfo(disInfo.getSealInfo());
				}

				// �������ڳ�ʼ��
				life.initLifecycle(saveDisInfo);

				//���浽�������漯����
				saveDisInfos.add(saveDisInfo);
			}
		}

		//��������ַ���Ϣ
		PersistHelper.getService().save(saveDisInfos);
	}

}
