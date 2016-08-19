package com.bjsasc.ddm.distribute.helper;

import com.bjsasc.ddm.distribute.service.DistributeInfoOperation.DistributeOperationService;
import com.bjsasc.ddm.distribute.service.commonsearch.CommonSearchService;
import com.bjsasc.ddm.distribute.service.convertversion.DcA3SigObjectLinkService;
import com.bjsasc.ddm.distribute.service.convertversion.DcA3TaskSignatureService;
import com.bjsasc.ddm.distribute.service.disinfoistrack.DisInfoIsTrackService;
import com.bjsasc.ddm.distribute.service.distributeconfigparameter.DistributeConfigParameterService;
import com.bjsasc.ddm.distribute.service.distributedestroyorder.DistributeDestroyOrderService;
import com.bjsasc.ddm.distribute.service.distributeelectask.DistributeElecTaskService;
import com.bjsasc.ddm.distribute.service.distributeinfo.DistributeInfoService;
import com.bjsasc.ddm.distribute.service.distributeinfoconfig.DistributeInfoConfigService;
import com.bjsasc.ddm.distribute.service.distributelifecycle.DistributeLifecycleService;
import com.bjsasc.ddm.distribute.service.distributelifecycle.LifeCycleUpdateService;
import com.bjsasc.ddm.distribute.service.distributeobject.DistributeObjectService;
import com.bjsasc.ddm.distribute.service.distributeobjectnotautocreate.DistributeObjectNotAutoCreateService;
import com.bjsasc.ddm.distribute.service.distributeobjectprint.DistributeObjectPrintService;
import com.bjsasc.ddm.distribute.service.distributeobjecttype.DistributeObjectTypeService;
import com.bjsasc.ddm.distribute.service.distributeoptvalidation.DistributeOptValidationService;
import com.bjsasc.ddm.distribute.service.distributeorder.DistributeOrderService;
import com.bjsasc.ddm.distribute.service.distributeorderobjectlink.DistributeOrderObjectLinkService;
import com.bjsasc.ddm.distribute.service.distributepapersigntask.DistributePaperSignTaskService;
import com.bjsasc.ddm.distribute.service.distributepapertask.DistributePaperTaskService;
import com.bjsasc.ddm.distribute.service.distributeprincipal.DistributePrincipalService;
import com.bjsasc.ddm.distribute.service.distributesendorder.DistributeSendOrderService;
import com.bjsasc.ddm.distribute.service.distributetask.DistributeTaskService;
import com.bjsasc.ddm.distribute.service.distributetaskinfolink.DistributeTaskInfoLinkService;
import com.bjsasc.ddm.distribute.service.distributeworkload.DistributeWorkloadService;
import com.bjsasc.ddm.distribute.service.duplicateprocess.DuplicateProcessService;
import com.bjsasc.ddm.distribute.service.distributecommonconfig.DistributeCommonConfigService;
import com.bjsasc.ddm.distribute.service.integrate.IntegrateService;
import com.bjsasc.ddm.distribute.service.recdesinfo.RecDesInfoService;
import com.bjsasc.ddm.distribute.service.recdespapertask.RecDesPaperTaskService;
import com.bjsasc.plm.core.util.SpringUtil;

/**
 * 分发管理 Helper
 * 
 * @author gengancong 2013-2-22
 */
public abstract class DistributeHelper {

	private static CommonSearchService commonSearchService;
	private static DistributeInfoService distributeInfoService;
	private static DistributeOrderService distributeOrderService;
	private static DistributeObjectService distributeOjectService;
	private static DistributeTaskService distributeTaskService;
	private static DistributePaperTaskService distributePaperTaskService;
	private static DistributeElecTaskService distributeElecTaskService;
	private static DistributeTaskInfoLinkService distributeTaskInfoLinkService;
	private static DistributeOrderObjectLinkService distributeOrderObjectLinkService;
	private static DistributeLifecycleService distributeLifecycleService;
	private static DuplicateProcessService duplicateProcessService;
	private static DistributeWorkloadService distributeWorkloadService;
	private static DistributeSendOrderService distributeSendOrderService;
	private static DistributeDestroyOrderService distributeDestroyOrderService;
	private static DistributeOptValidationService distributeOptValidationService;
	private static DisInfoIsTrackService disInfoIsTrackService;
	private static DistributePrincipalService distributePrincipalService;
	private static DistributeObjectTypeService distributeOjectTypeService;
	private static DistributeObjectNotAutoCreateService distributeOjectNotAutoCreateService;
	private static DistributeConfigParameterService distributeConfigParameterService;
	private static LifeCycleUpdateService lifeCycleUpdateService;
	private static DistributeInfoConfigService distributeInfoConfigService;
	private static DistributeCommonConfigService distributeCommonConfigService;
	private static IntegrateService integrateService;
	//回收销毁相关服务
	private static RecDesInfoService recDesInfoService;
	private static RecDesPaperTaskService recDesPaperTaskService;
	//对插件提供的接口方法
	private static DistributeOperationService distributeOperationService;
	//纸质签收任务相关服务
	private static DistributePaperSignTaskService distributePaperSignTaskService;
	//纸质签收任务相关服务
	private static DcA3TaskSignatureService dcA3TaskSignatureService;
	//纸质签收任务相关服务
	private static DcA3SigObjectLinkService dcA3SigObjectLinkService;
	//任务下分发数据是否已打印
	private static DistributeObjectPrintService distributeObjectPrintService;
	/**
	 * 获得发放信息服务
	 * 
	 * @return DistributeInfoService
	 */
	public static DistributeInfoService getDistributeInfoService() {
		if (distributeInfoService == null) {
			distributeInfoService = (DistributeInfoService) SpringUtil.getBean("ddm_distributeinfo_service");
		}
		return distributeInfoService;
	}

	/**
	 * 获得发放单服务
	 * 
	 * @return DistributeOrderService
	 */
	public static DistributeOrderService getDistributeOrderService() {
		if (distributeOrderService == null) {
			distributeOrderService = (DistributeOrderService) SpringUtil.getBean("ddm_distributeorder_service");
		}
		return distributeOrderService;
	}

	/**
	 * 获得发放对象服务
	 * 
	 * @return DistributeObjectService
	 */
	public static DistributeObjectService getDistributeObjectService() {
		if (distributeOjectService == null) {
			distributeOjectService = (DistributeObjectService) SpringUtil.getBean("ddm_distributeobject_service");
		}
		return distributeOjectService;
	}

	/**
	 * 获得发放单与分发数据link对象服务
	 * 
	 * @return DistributeOrderObjectLinkService
	 */
	public static DistributeOrderObjectLinkService getDistributeOrderObjectLinkService() {
		if (distributeOrderObjectLinkService == null) {
			distributeOrderObjectLinkService = (DistributeOrderObjectLinkService) SpringUtil
					.getBean("ddm_distributeorderobjectlink_service");
		}
		return distributeOrderObjectLinkService;
	}

	/**
	 * 获得分发信息与纸质任务link对象服务
	 * 
	 * @return DistributeTaskInfoLinkService
	 */
	public static DistributeTaskInfoLinkService getDistributeTaskInfoLinkService() {
		if (distributeTaskInfoLinkService == null) {
			distributeTaskInfoLinkService = (DistributeTaskInfoLinkService) SpringUtil
					.getBean("ddm_distributetaskinfolink_service");
		}
		return distributeTaskInfoLinkService;
	}

	/**
	 * 获得纸质任务服务
	 * 
	 * @return DistributePaperTaskService
	 */
	public static DistributePaperTaskService getDistributePaperTaskService() {
		if (distributePaperTaskService == null) {
			distributePaperTaskService = (DistributePaperTaskService) SpringUtil
					.getBean("ddm_distributepapertask_service");
		}
		return distributePaperTaskService;
	}

	/**
	 * 获得电子任务服务
	 * 
	 * @return DistributeElecTaskService
	 */
	public static DistributeElecTaskService getDistributeElecTaskService() {
		if (distributeElecTaskService == null) {
			distributeElecTaskService = (DistributeElecTaskService) SpringUtil
					.getBean("ddm_distributeelectask_service");
		}
		return distributeElecTaskService;
	}

	/**
	 * 获得分发任务服务
	 * 
	 * @return DistributeTaskService
	 */
	public static DistributeTaskService getDistributeTaskService() {
		if (distributeTaskService == null) {
			distributeTaskService = (DistributeTaskService) SpringUtil.getBean("ddm_distributetask_service");
		}
		return distributeTaskService;
	}

	/**
	 * 获得生命周期服务
	 * 
	 * @return DistributeLifecycleService
	 */
	public static DistributeLifecycleService getDistributeLifecycleService() {
		if (distributeLifecycleService == null) {
			distributeLifecycleService = (DistributeLifecycleService) SpringUtil.getBean("ddm_lifecycle_service");
		}
		return distributeLifecycleService;
	}

	/**
	 * 获得复制加工任务服务
	 * 
	 * @return DuplicateProcessService
	 */
	public static DuplicateProcessService getDuplicateProcessService() {
		if (duplicateProcessService == null) {
			duplicateProcessService = (DuplicateProcessService) SpringUtil.getBean("ddm_duplicateprocess_service");
		}
		return duplicateProcessService;
	}

	/**
	 * 获得综合查询服务
	 * 
	 * @return CommonSearchService
	 */
	public static CommonSearchService getCommonSearchService() {
		if (commonSearchService == null) {
			commonSearchService = (CommonSearchService) SpringUtil.getBean("ddm_commonsearch_service");
		}
		return commonSearchService;
	}

	/**
	 * 获得工作量统计服务
	 * 
	 * @return CommonSearchService
	 */
	public static DistributeWorkloadService getDistributeWorkloadService() {
		if (distributeWorkloadService == null) {
			distributeWorkloadService = (DistributeWorkloadService) SpringUtil
					.getBean("ddm_distributeworkload_service");
		}
		return distributeWorkloadService;
	}

	/**
	 * 获得内发单和外发单服务
	 * 
	 * @return sendOrderService
	 */
	public static DistributeSendOrderService getDistributeSendOrderService() {
		if (distributeSendOrderService == null) {
			distributeSendOrderService = (DistributeSendOrderService) SpringUtil
					.getBean("ddm_distributesendorder_service");
		}
		return distributeSendOrderService;
	}
	/**
	 * 获得DistributeObjectPrint
	 * 
	 * @return sendOrderService
	 */
	public static DistributeObjectPrintService getDistributeObjectPrintService() {
		if (distributeObjectPrintService == null) {
			distributeObjectPrintService = (DistributeObjectPrintService) SpringUtil
					.getBean("ddm_distributeobjectprint_service");
		}
		return distributeObjectPrintService;
	}

	/**
	 * 获得回收单和销毁单服务
	 * 
	 * @return destroyOrderService
	 */
	public static DistributeDestroyOrderService getDistributeDestroyOrderService() {
		if (distributeDestroyOrderService == null) {
			distributeDestroyOrderService = (DistributeDestroyOrderService) SpringUtil
					.getBean("ddm_distributedestroyorder_service");
		}
		return distributeDestroyOrderService;
	}
	
	/**
	 * 操作权限服务
	 * 
	 * @return
	 */
	public static DistributeOptValidationService getDistributeOptValidationService() {
		if (distributeOptValidationService == null) {
			distributeOptValidationService = (DistributeOptValidationService) SpringUtil
					.getBean("ddm_distributeoptvalidation_service");
		}
		return distributeOptValidationService;
	}

	/**
	 * 获得是否跟踪服务
	 * 
	 * @return DisInfoIsTrackService
	 */
	public static DisInfoIsTrackService getDisInfoIsTrackService() {
		if (disInfoIsTrackService == null) {
			disInfoIsTrackService = (DisInfoIsTrackService) SpringUtil.getBean("ddm_isTrack_service");
		}
		return disInfoIsTrackService;
	}
	
	public static DistributePrincipalService getDistributePrincipalService() {
		if (distributePrincipalService == null) {
			distributePrincipalService = (DistributePrincipalService) SpringUtil.getBean("ddm_distributeprincipal_service");
		}
		return distributePrincipalService;
	}
	
	/**
	 * 获得对象模型配置服务
	 * 
	 * @return DistributeObjectTypeService
	 */
	public static DistributeObjectTypeService getDistributeObjectTypeService() {
		if (distributeOjectTypeService == null) {
			distributeOjectTypeService = (DistributeObjectTypeService) SpringUtil.getBean("ddm_distributeobjecttype_service");
		}
		return distributeOjectTypeService;
	}

	/**
	 * 获得对象模型配置不自动发放服务
	 * 
	 * @return DistributeObjectTypeService
	 */
	public static DistributeObjectNotAutoCreateService getDistributeObjectNotAutoCreateService() {
		if (distributeOjectNotAutoCreateService == null) {
			distributeOjectNotAutoCreateService = (DistributeObjectNotAutoCreateService) SpringUtil.getBean("ddm_distributeobjectnotautocreate_service");
		}
		return distributeOjectNotAutoCreateService;
	}	
	/**
	 * 获得发放管理参数配置服务
	 * 
	 * @return DistributeConfigParameterService
	 */
	public static DistributeConfigParameterService getDistributeConfigParameterService() {
		if (distributeConfigParameterService == null) {
			distributeConfigParameterService = (DistributeConfigParameterService) SpringUtil.getBean("ddm_distributeconfigparameter_service");
		}
		return distributeConfigParameterService;
	}
	
	/**
	 * 获得生命周期升级服务
	 * 
	 * @return lifeCycleUpdateService
	 */
	public static LifeCycleUpdateService getLifeCycleUpdateService() {
		if (lifeCycleUpdateService == null) {
			lifeCycleUpdateService = (LifeCycleUpdateService) SpringUtil.getBean("ddm_lifecycleupdateservice_service");
		}
		return lifeCycleUpdateService;
	}

	/**
	 * 获得默认分发信息配置服务
	 * 
	 * @return DistributeConfigParameterService
	 */
	public static DistributeInfoConfigService getDistributeInfoConfigService() {
		if (distributeInfoConfigService == null) {
			distributeInfoConfigService = (DistributeInfoConfigService) SpringUtil.getBean("ddm_distributeinfoconfig_service");
		}
		return distributeInfoConfigService;
	}

	/**
	 * 获得发放管理通用配置服务
	 * 
	 * @return DistributeConfigParameterService
	 * @author zhangguoqiang 2014-7-11
	 */
	public static DistributeCommonConfigService getDistributeCommonConfigService() {
		if (distributeCommonConfigService == null) {
			distributeCommonConfigService = (DistributeCommonConfigService) SpringUtil.getBean("ddm_distributecommonconfig_service");
		}
		return distributeCommonConfigService;
	}
	/**
	 * 获得系统集成服务
	 * 
	 * @return IntegrateService
	 * @author zhangguoqiang 2014-7-11
	 */
	public static IntegrateService getIntegrateService(){
		if(integrateService==null){
			integrateService = (IntegrateService)SpringUtil.getBean("ddm_integrate_service");
		}
		return integrateService;
	}

	/**
	 * @author kangyanfei
	 * 获得回收销毁单服务
	 * 
	 * @return DistributeConfigParameterService
	 */
	public static RecDesInfoService getRecDesInfoService() {
		if (recDesInfoService == null) {
			recDesInfoService = (RecDesInfoService) SpringUtil.getBean("ddm_recdesinfo_service");
		}
		return recDesInfoService;
	}
	
	
	/**
	 * 获得回收销毁电子任务的服务
	 * 
	 * @return RecDesPaperTaskService 回收销毁电子任务服务
	 */
	public static RecDesPaperTaskService getRecDesPaperTaskService(){
		if(recDesPaperTaskService == null){
			recDesPaperTaskService = (RecDesPaperTaskService) SpringUtil.getBean("ddm_recdespapertask_service");
		}
		return recDesPaperTaskService;
	}

	/**
	 * @author kangyanfei
	 * 发放数据查询和发放信息批量保存
	 * 
	 * @return DistributeOperationService 发放数据查询和发放信息批量保存服务
	 */
	public static DistributeOperationService getDistributeOperationService(){
		if(distributeOperationService == null){
			distributeOperationService = (DistributeOperationService) SpringUtil.getBean("ddm_distributeoperation_service");
		}
		return distributeOperationService;
	}

	/**
	 * 获得纸质签收任务的服务
	 * @author zhangguoqiang 2014-09-11
	 * 
	 * @return DistributePaperSignTaskService 纸质签收任务的服务
	 */
	public static DistributePaperSignTaskService getDistributePaperSignTaskService(){
		if(distributePaperSignTaskService == null){
			distributePaperSignTaskService = (DistributePaperSignTaskService) SpringUtil.getBean("ddm_distributepapersigntask_service");
		}
		return distributePaperSignTaskService;
	}
	
	/**
	 * 获得跨版本任务签署信息服务
	 * @author zhangguoqiang 2015-05-01
	 * 
	 * @return DcA3TaskSignatureService 跨版本任务签署信息服务
	 */
	public static DcA3TaskSignatureService getDcA3TaskSignatureService(){
		if(dcA3TaskSignatureService == null){
			dcA3TaskSignatureService = (DcA3TaskSignatureService) SpringUtil.getBean("ddm_dca3tasksignature_service");
		}
		return dcA3TaskSignatureService;
	}
	/**
	 * 获得跨版本任务签署信息与分发业务对象link服务
	 * @author zhangguoqiang 2015-05-01
	 * 
	 * @return dcA3SigObjectLinkService 跨版本任务签署信息与分发业务对象link服务
	 */
	public static DcA3SigObjectLinkService getDcA3SigObjectLinkService(){
		if(dcA3SigObjectLinkService == null){
			dcA3SigObjectLinkService = (DcA3SigObjectLinkService) SpringUtil.getBean("ddm_dca3sigobjectlink_service");
		}
		return dcA3SigObjectLinkService;
	}
}
