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
				//如果是目标站点
				if (selfSiteInnerId.equals(targetSiteInnerId)) {
					Map dataMap = ObjectToMapUtil.mapToObject(event.getReqParamMap());
					String flag = (String) dataMap.get("flag");
					//flag为0表示反馈生成同步任务，为1表示反馈同步生命周期
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
									//创建任务
									disElecTask.setNumber(disOrder.getNumber() + ConstUtil.C_ELECTASK_STR);
									disElecTask.setName(disOrder.getName() + ConstUtil.C_ELECTASK_STR);

									disElecTask.setNote("");
									//2为反馈
									disElecTask.setElecTaskType(ConstUtil.ELEC_TASKTYPE_REPLY);

									disElecTask.setContextInfo(disOrder.getContextInfo());
									//添加域信息
									disElecTask.setDomainInfo(disOrder.getDomainInfo());
									
									//添加任务来源
									disElecTask.setSourceSiteId(disOrder.getSiteId());
									disElecTask.setSourceSiteClassId(disOrder.getSiteClassId());
									disElecTask.setSourceSiteName(disOrder.getSiteName());
									
									//添加目标站点
									disElecTask.setTargetSiteId(disInfo.getDisInfoId());
									disElecTask.setTargetSiteClassId(disInfo.getInfoClassId());
									disElecTask.setTargetSiteName(disInfo.getDisInfoName());
									
									//反馈任务创建人和修改人设置为发放单的创建人和修改人
									disElecTask.setManageInfo(disOrder.getManageInfo());
									disElecTask.getManageInfo().setCreateTime(System.currentTimeMillis());
									disElecTask.getManageInfo().setModifyTime(System.currentTimeMillis());
									
									// 创建电子任务对象。
									service.createDistributeElecTask(disElecTask);

									// 更新分发信息的发送时间
									disInfo.setSendTime(System.currentTimeMillis());
									// 分发信息生命周期升级
									//DistributeLifecycleService life = DistributeHelper.getDistributeLifecycleService();
									if (ConstUtil.LC_NOT_DISTRIBUT.getName().equals(disInfo.getLifeCycleInfo().getStateName())) {
										//屏蔽权限校验
										SessionHelper.getService().setCheckPermission(false);
										//生命周期升级
										life.promoteLifeCycle(disInfo);
										//打开权限校验
										SessionHelper.getService().setCheckPermission(true);
									}
									Helper.getPersistService().update(disInfo);

									// 创建分发信息与纸质任务link对象。
									createDistributeTaskInfoLink(disElecTask, disInfo);
									
									//创建任务关联
									DistributeTaskSyn disTaskSyn = newDistributeTaskSyn();
									disTaskSyn.setTaskId(disElecTask.getInnerId());
									disTaskSyn.setTaskClassId(disElecTask.getClassId());
									disTaskSyn.setTaskSynClassId(elecTaskOidList.get(0));
									disTaskSyn.setTaskSynId(elecTaskOidList.get(1));
									disTaskSyn.setSiteType(siteType);
									createDistributeTaskSyn(disTaskSyn);
								} else {
									//创建分发信息与电子任务link
									createDistributeTaskInfoLink(disElecTask, disInfo);
									//分发信息添加发送时间，生命周期升级
									disInfo.setSendTime(System.currentTimeMillis());
									//DistributeLifecycleService life = DistributeHelper.getDistributeLifecycleService();
									if (ConstUtil.LC_NOT_DISTRIBUT.getName().equals(disInfo.getLifeCycleInfo().getStateName())) {
										//屏蔽权限校验
										SessionHelper.getService().setCheckPermission(false);
										//生命周期升级
										life.promoteLifeCycle(disInfo);
										//打开权限校验
										SessionHelper.getService().setCheckPermission(true);
									}
									Helper.getPersistService().update(disInfo);
								}
								i = i + 1;
								
								//分发信息升级后，判断分发数据和分发单是否需要升级
								String objSql = "SELECT DISTINCT * FROM DDM_DIS_ORDEROBJLINK OBJLINK"
										+ " WHERE OBJLINK.FROMOBJECTCLASSID || ':' || OBJLINK.FROMOBJECTID = ?";
								//查询分发单对象的所有分发数据
								List<DistributeOrderObjectLink> linkList = Helper.getPersistService().query(objSql, DistributeOrderObjectLink.class, disOrderOid);
								for(DistributeOrderObjectLink link : linkList){
									boolean infoFlag = true;
									//根据分发数据查找分发信息
									String infoSql = "SELECT DISTINCT * FROM DDM_DIS_INFO INFO"
											+ " WHERE INFO.DISORDEROBJLINKCLASSID || ':' || INFO.DISORDEROBJLINKID = ?";
									List<DistributeInfo> infoList = Helper.getPersistService().query(infoSql, DistributeInfo.class, link.getClassId() + ":" + link.getInnerId());
									for(DistributeInfo info : infoList){
										if(!ConstUtil.LC_DISTRIBUTED.getName().equals(info.getLifeCycleInfo().getStateName())){
											//分发信息含有未发送状态的
											infoFlag = false;
											break;
										}
									}
									//如果分发数据下分发信息全部为已发送状态
									if(infoFlag == true){
										//分发数据升级
										//屏蔽权限校验
										SessionHelper.getService().setCheckPermission(false);
										//生命周期升级
										life.promoteLifeCycle(link);
										//打开权限校验
										SessionHelper.getService().setCheckPermission(true);
										Helper.getPersistService().update(link);
									}
								}
								//查询发放单相关所有分发信息
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
								//如果发放单下的所有分发信息状态为已发送
								if(orderInfoFlag == true){
									//发放单生命周期升级
									//屏蔽权限校验
									SessionHelper.getService().setCheckPermission(false);
									//生命周期升级
									life.promoteLifeCycle(disOrder);
									//打开权限校验
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
						
						//根据外域任务查找对应域内任务
						String sql = "select * from DDM_DIS_TASKSYN where taskSynClassId || ':' || taskSynId = ?";
						List<DistributeTaskSyn> list = Helper.getPersistService().query(sql, DistributeTaskSyn.class, taskOid);
						DistributeTaskSyn distributeTaskSyn = list.get(0);
						DistributeElecTask disTask = (DistributeElecTask)Helper.getPersistService().getObject(distributeTaskSyn.getTaskClassId() + ":" + distributeTaskSyn.getTaskId());
						DistributeLifecycleService life = DistributeHelper.getDistributeLifecycleService();
						if("PROMOTE".equals(opt)){
							//屏蔽权限校验
							SessionHelper.getService().setCheckPermission(false);
							// 生命周期升级
							life.promoteLifeCycle(disTask);
							//打开权限校验
							SessionHelper.getService().setCheckPermission(true);
						}else if("REJECT".equals(opt)){
							// 生命周期拒绝
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
					//不是目标站点：转发
					TransferObjectHelper.getTransferService().transmit(event.getInnerId());
				}
			} catch (Exception e) {
				//打开权限校验
				SessionHelper.getService().setCheckPermission(true);
				throw new RuntimeException("ReplyDistributeMessageDealService类中，消息反馈处理失败！", e);
			}
		}

	}

	/**
	 * 创建分发信息与任务link对象。
	 * 
	 * @param disTask
	 *            DistributeObject
	 */
	private void createDistributeTaskInfoLink(DistributeTask disTask, DistributeInfo disInfo) {

		DistributeTaskDomainLink taskDomainLink = newDistributeTaskDomainLink();
		// 电子任务内部标识
		taskDomainLink.setFromObject(disTask);
		// 分发信息内部标识
		taskDomainLink.setToObject(disInfo);
		// 任务类型（0：纸质任务，1：电子任务）
		taskDomainLink.setTaskType("1");
		Helper.getPersistService().save(taskDomainLink);
	}
	
	public DistributeTaskDomainLink newDistributeTaskDomainLink() {
		DistributeTaskDomainLink disLink = (DistributeTaskDomainLink) PersistUtil
				.createObject(DistributeTaskDomainLink.CLASSID);
		return disLink;
	}
	
	/**
	 * 创建同步任务
	 * @return
	 */
	public DistributeTaskSyn newDistributeTaskSyn() {
		DistributeTaskSyn disTaskSyn = (DistributeTaskSyn) PersistUtil.createObject(DistributeTaskSyn.CLASSID);
		return disTaskSyn;
	}
	
	/**
	 * 保存同步任务
	 * @param dis
	 */
	public void createDistributeTaskSyn(DistributeTaskSyn dis) {
		Helper.getPersistService().save(dis);
	}
	
	/**
	 * 创建拒签原因
	 * @return
	 */
	private ReturnReason newReturnReason() {
		ReturnReason reason = (ReturnReason) PersistUtil.createObject(ReturnReason.CLASSID);
		return reason;
	}
}
