package com.bjsasc.ddm.distribute.service.distributereceive;

import java.util.List;
import java.util.Map;

import com.bjsasc.avidm.core.site.Site;
import com.bjsasc.avidm.core.site.SiteHelper;
import com.bjsasc.avidm.core.transfer.TransferObjectHelper;
import com.bjsasc.avidm.core.transfer.event.TransferEvent;
import com.bjsasc.ddm.common.ConstUtil;
import com.bjsasc.ddm.distribute.helper.DistributeHelper;
import com.bjsasc.ddm.distribute.model.distributeelectask.DistributeElecTask;
import com.bjsasc.ddm.distribute.model.distributeinfo.DistributeInfo;
import com.bjsasc.ddm.distribute.model.distributeorder.DistributeOrder;
import com.bjsasc.ddm.distribute.model.distributeorderobjectlink.DistributeOrderObjectLink;
import com.bjsasc.ddm.distribute.model.distributetask.DistributeTask;
import com.bjsasc.ddm.distribute.model.distributetaskdomainlink.DistributeTaskDomainLink;
import com.bjsasc.ddm.distribute.model.distributetasksyn.DistributeTaskSyn;
import com.bjsasc.ddm.distribute.model.returnreason.ReturnReason;
import com.bjsasc.ddm.distribute.service.distributeelectask.DistributeElecTaskService;
import com.bjsasc.ddm.distribute.service.distributelifecycle.DistributeLifecycleService;
import com.bjsasc.platform.objectmodel.business.persist.PersistUtil;
import com.bjsasc.plm.collaboration.adapter.base.MessageDealService;
import com.bjsasc.plm.collaboration.util.ObjectToMapUtil;
import com.bjsasc.plm.core.Helper;
import com.bjsasc.plm.core.session.SessionHelper;
import com.cascc.avidm.util.SplitString;

@SuppressWarnings({ "deprecation", "rawtypes" })
public class ReplyDistributeMessageDealServiceImpl implements MessageDealService {

	public void dealEvent(TransferEvent event) {
		synchronized (ReplyDistributeMessageDealServiceImpl.class){
			try {
				Site selfSite = SiteHelper.getSiteService().findLocalSiteInfo();
				String selfSiteInnerId = selfSite.getInnerId();
				String targetSiteInnerId = event.getTargetSite().getInnerId();
				//�����Ŀ��վ��
				if (selfSiteInnerId.equals(targetSiteInnerId)) {
					Map dataMap = ObjectToMapUtil.mapToObject(event.getReqParamMap());
					String flag = (String) dataMap.get("flag");
					//flagΪ0��ʾ��������ͬ������Ϊ1��ʾ����ͬ����������
					if("0".equals(flag)){
						String infosOid = (String) dataMap.get("infosOid");
						String disOrderOid = (String) dataMap.get("orderOid");
						String elecTaskOid = (String) dataMap.get("taskOid");
						String siteType = (String) dataMap.get("siteType");
						List<String> taskOidsList = SplitString.string2List(elecTaskOid, ";");
						List<String> infoOidsList = SplitString.string2List(infosOid, ";");
						DistributeOrder disOrder = (DistributeOrder)Helper.getPersistService().getObject(disOrderOid);
						DistributeLifecycleService life = DistributeHelper.getDistributeLifecycleService();
						for(int j = 0; j < taskOidsList.size(); j++){
							//i = i + 1;
							int i = 0;
							String infoOids = infoOidsList.get(j);
							List<String> disInfoOidsList = SplitString.string2List(infoOids, ",");
							//DistributeElecTask elecTask = (DistributeElecTask)Helper.getPersistService().getObject(taskOidsList.get(j));
							List<String> elecTaskOidList = SplitString.string2List(taskOidsList.get(j), ":");
							DistributeElecTaskService service = DistributeHelper.getDistributeElecTaskService();
							DistributeElecTask disElecTask = service.newDistributeElecTask();
							for(String infoOid : disInfoOidsList){
								DistributeInfo disInfo = (DistributeInfo)Helper.getPersistService().getObject(infoOid);
								if(i == 0){
									//��������
									disElecTask.setNumber(disOrder.getNumber() + ConstUtil.C_ELECTASK_STR);
									disElecTask.setName(disOrder.getName() + ConstUtil.C_ELECTASK_STR);

									disElecTask.setNote("");
									//2Ϊ����
									disElecTask.setElecTaskType(ConstUtil.ELEC_TASKTYPE_REPLY);

									disElecTask.setContextInfo(disOrder.getContextInfo());
									//�������Ϣ
									disElecTask.setDomainInfo(disOrder.getDomainInfo());
									
									//���������Դ
									disElecTask.setSourceSiteId(disOrder.getSiteId());
									disElecTask.setSourceSiteClassId(disOrder.getSiteClassId());
									disElecTask.setSourceSiteName(disOrder.getSiteName());
									
									//���Ŀ��վ��
									disElecTask.setTargetSiteId(disInfo.getDisInfoId());
									disElecTask.setTargetSiteClassId(disInfo.getInfoClassId());
									disElecTask.setTargetSiteName(disInfo.getDisInfoName());
									
									//�������񴴽��˺��޸�������Ϊ���ŵ��Ĵ����˺��޸���
									disElecTask.setManageInfo(disOrder.getManageInfo());
									disElecTask.getManageInfo().setCreateTime(System.currentTimeMillis());
									disElecTask.getManageInfo().setModifyTime(System.currentTimeMillis());
									
									// ���������������
									service.createDistributeElecTask(disElecTask);

									// ���·ַ���Ϣ�ķ���ʱ��
									disInfo.setSendTime(System.currentTimeMillis());
									// �ַ���Ϣ������������
									//DistributeLifecycleService life = DistributeHelper.getDistributeLifecycleService();
									if (ConstUtil.LC_NOT_DISTRIBUT.getName().equals(disInfo.getLifeCycleInfo().getStateName())) {
										//����Ȩ��У��
										SessionHelper.getService().setCheckPermission(false);
										//������������
										life.promoteLifeCycle(disInfo);
										//��Ȩ��У��
										SessionHelper.getService().setCheckPermission(true);
									}
									Helper.getPersistService().update(disInfo);

									// �����ַ���Ϣ��ֽ������link����
									createDistributeTaskInfoLink(disElecTask, disInfo);
									
									//�����������
									DistributeTaskSyn disTaskSyn = newDistributeTaskSyn();
									disTaskSyn.setTaskId(disElecTask.getInnerId());
									disTaskSyn.setTaskClassId(disElecTask.getClassId());
									disTaskSyn.setTaskSynClassId(elecTaskOidList.get(0));
									disTaskSyn.setTaskSynId(elecTaskOidList.get(1));
									disTaskSyn.setSiteType(siteType);
									createDistributeTaskSyn(disTaskSyn);
								} else {
									//�����ַ���Ϣ���������link
									createDistributeTaskInfoLink(disElecTask, disInfo);
									//�ַ���Ϣ��ӷ���ʱ�䣬������������
									disInfo.setSendTime(System.currentTimeMillis());
									//DistributeLifecycleService life = DistributeHelper.getDistributeLifecycleService();
									if (ConstUtil.LC_NOT_DISTRIBUT.getName().equals(disInfo.getLifeCycleInfo().getStateName())) {
										//����Ȩ��У��
										SessionHelper.getService().setCheckPermission(false);
										//������������
										life.promoteLifeCycle(disInfo);
										//��Ȩ��У��
										SessionHelper.getService().setCheckPermission(true);
									}
									Helper.getPersistService().update(disInfo);
								}
								i = i + 1;
								
								//�ַ���Ϣ�������жϷַ����ݺͷַ����Ƿ���Ҫ����
								String objSql = "SELECT DISTINCT * FROM DDM_DIS_ORDEROBJLINK OBJLINK"
										+ " WHERE OBJLINK.FROMOBJECTCLASSID || ':' || OBJLINK.FROMOBJECTID = ?";
								//��ѯ�ַ�����������зַ�����
								List<DistributeOrderObjectLink> linkList = Helper.getPersistService().query(objSql, DistributeOrderObjectLink.class, disOrderOid);
								for(DistributeOrderObjectLink link : linkList){
									boolean infoFlag = true;
									//���ݷַ����ݲ��ҷַ���Ϣ
									String infoSql = "SELECT DISTINCT * FROM DDM_DIS_INFO INFO"
											+ " WHERE INFO.DISORDEROBJLINKCLASSID || ':' || INFO.DISORDEROBJLINKID = ?";
									List<DistributeInfo> infoList = Helper.getPersistService().query(infoSql, DistributeInfo.class, link.getClassId() + ":" + link.getInnerId());
									for(DistributeInfo info : infoList){
										if(!ConstUtil.LC_DISTRIBUTED.getName().equals(info.getLifeCycleInfo().getStateName())){
											//�ַ���Ϣ����δ����״̬��
											infoFlag = false;
											break;
										}
									}
									//����ַ������·ַ���Ϣȫ��Ϊ�ѷ���״̬
									if(infoFlag == true){
										//�ַ���������
										//����Ȩ��У��
										SessionHelper.getService().setCheckPermission(false);
										//������������
										life.promoteLifeCycle(link);
										//��Ȩ��У��
										SessionHelper.getService().setCheckPermission(true);
										Helper.getPersistService().update(link);
									}
								}
								//��ѯ���ŵ�������зַ���Ϣ
								String infoSql = "SELECT INFO.* FROM DDM_DIS_ORDER DISORDER, DDM_DIS_ORDEROBJLINK OBJLINK, DDM_DIS_INFO INFO"
										+ " WHERE OBJLINK.FROMOBJECTCLASSID || ':' || OBJLINK.FROMOBJECTID = DISORDER.CLASSID || ':' || DISORDER.INNERID"
										+ " AND INFO.DISORDEROBJLINKCLASSID || ':' || INFO.DISORDEROBJLINKID = OBJLINK.CLASSID || ':' || OBJLINK.INNERID"
										+ " AND DISORDER.CLASSID || ':' || DISORDER.INNERID = ?";
								List<DistributeInfo> infoList = Helper.getPersistService().query(infoSql, DistributeInfo.class, disOrderOid);
								boolean orderInfoFlag = true;
								for(DistributeInfo info : infoList){
									if(!ConstUtil.LC_DISTRIBUTED.getName().equals(info.getLifeCycleInfo().getStateName())){
										orderInfoFlag = false;
										break;
									}
								}
								//������ŵ��µ����зַ���Ϣ״̬Ϊ�ѷ���
								if(orderInfoFlag == true){
									//���ŵ�������������
									//����Ȩ��У��
									SessionHelper.getService().setCheckPermission(false);
									//������������
									life.promoteLifeCycle(disOrder);
									//��Ȩ��У��
									SessionHelper.getService().setCheckPermission(true);
									Helper.getPersistService().update(disOrder);
								}
							}
						}
					} else if("1".equals(flag)){
						String taskOid = (String) dataMap.get("taskOid");
						String opt = (String) dataMap.get("opt");
						String userName = (String) dataMap.get("userName");
						String returnReason = (String) dataMap.get("returnReason");
						String receiveByUserName = (String) dataMap.get("receiveByUserName");
						String receiveTime = (String) dataMap.get("receiveTime");
						
						//��������������Ҷ�Ӧ��������
						String sql = "select * from DDM_DIS_TASKSYN where taskSynClassId || ':' || taskSynId = ?";
						List<DistributeTaskSyn> list = Helper.getPersistService().query(sql, DistributeTaskSyn.class, taskOid);
						DistributeTaskSyn distributeTaskSyn = list.get(0);
						DistributeElecTask disTask = (DistributeElecTask)Helper.getPersistService().getObject(distributeTaskSyn.getTaskClassId() + ":" + distributeTaskSyn.getTaskId());
						DistributeLifecycleService life = DistributeHelper.getDistributeLifecycleService();
						if("PROMOTE".equals(opt)){
							//����Ȩ��У��
							SessionHelper.getService().setCheckPermission(false);
							// ������������
							life.promoteLifeCycle(disTask);
							//��Ȩ��У��
							SessionHelper.getService().setCheckPermission(true);
						}else if("REJECT".equals(opt)){
							// �������ھܾ�
							life.rejectLifeCycle(disTask);
							ReturnReason retReason = newReturnReason();
							retReason.setTaskId(disTask.getInnerId());
							retReason.setTaskClassId(disTask.getClassId());
							retReason.setLifeCycleInfo(disTask.getLifeCycleInfo());
							retReason.setReturnReason(returnReason);
							retReason.setUserName(userName);
							retReason.setReturnCount(1);
							Helper.getPersistService().save(retReason);
						}
						if(!"".equals(receiveByUserName) && receiveByUserName!=null){
							disTask.setReceiveByName(receiveByUserName);
							disTask.setReceiveTime(Long.parseLong(receiveTime));
						}
						Helper.getPersistService().update(disTask);
					}
				} else {
					//����Ŀ��վ�㣺ת��
					TransferObjectHelper.getTransferService().transmit(event.getInnerId());
				}
			} catch (Exception e) {
				//��Ȩ��У��
				SessionHelper.getService().setCheckPermission(true);
				throw new RuntimeException("ReplyDistributeMessageDealService���У���Ϣ��������ʧ�ܣ�", e);
			}
		}

	}

	/**
	 * �����ַ���Ϣ������link����
	 * 
	 * @param disTask
	 *            DistributeObject
	 */
	private void createDistributeTaskInfoLink(DistributeTask disTask, DistributeInfo disInfo) {

		DistributeTaskDomainLink taskDomainLink = newDistributeTaskDomainLink();
		// ���������ڲ���ʶ
		taskDomainLink.setFromObject(disTask);
		// �ַ���Ϣ�ڲ���ʶ
		taskDomainLink.setToObject(disInfo);
		// �������ͣ�0��ֽ������1����������
		taskDomainLink.setTaskType("1");
		Helper.getPersistService().save(taskDomainLink);
	}
	
	public DistributeTaskDomainLink newDistributeTaskDomainLink() {
		DistributeTaskDomainLink disLink = (DistributeTaskDomainLink) PersistUtil
				.createObject(DistributeTaskDomainLink.CLASSID);
		return disLink;
	}
	
	/**
	 * ����ͬ������
	 * @return
	 */
	public DistributeTaskSyn newDistributeTaskSyn() {
		DistributeTaskSyn disTaskSyn = (DistributeTaskSyn) PersistUtil.createObject(DistributeTaskSyn.CLASSID);
		return disTaskSyn;
	}
	
	/**
	 * ����ͬ������
	 * @param dis
	 */
	public void createDistributeTaskSyn(DistributeTaskSyn dis) {
		Helper.getPersistService().save(dis);
	}
	
	/**
	 * ������ǩԭ��
	 * @return
	 */
	private ReturnReason newReturnReason() {
		ReturnReason reason = (ReturnReason) PersistUtil.createObject(ReturnReason.CLASSID);
		return reason;
	}
}
