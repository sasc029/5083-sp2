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
 * �ַ����� Helper
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
	//����������ط���
	private static RecDesInfoService recDesInfoService;
	private static RecDesPaperTaskService recDesPaperTaskService;
	//�Բ���ṩ�Ľӿڷ���
	private static DistributeOperationService distributeOperationService;
	//ֽ��ǩ��������ط���
	private static DistributePaperSignTaskService distributePaperSignTaskService;
	//ֽ��ǩ��������ط���
	private static DcA3TaskSignatureService dcA3TaskSignatureService;
	//ֽ��ǩ��������ط���
	private static DcA3SigObjectLinkService dcA3SigObjectLinkService;
	//�����·ַ������Ƿ��Ѵ�ӡ
	private static DistributeObjectPrintService distributeObjectPrintService;
	/**
	 * ��÷�����Ϣ����
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
	 * ��÷��ŵ�����
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
	 * ��÷��Ŷ������
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
	 * ��÷��ŵ���ַ�����link�������
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
	 * ��÷ַ���Ϣ��ֽ������link�������
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
	 * ���ֽ���������
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
	 * ��õ����������
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
	 * ��÷ַ��������
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
	 * ����������ڷ���
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
	 * ��ø��Ƽӹ��������
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
	 * ����ۺϲ�ѯ����
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
	 * ��ù�����ͳ�Ʒ���
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
	 * ����ڷ������ⷢ������
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
	 * ���DistributeObjectPrint
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
	 * ��û��յ������ٵ�����
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
	 * ����Ȩ�޷���
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
	 * ����Ƿ���ٷ���
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
	 * ��ö���ģ�����÷���
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
	 * ��ö���ģ�����ò��Զ����ŷ���
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
	 * ��÷��Ź���������÷���
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
	 * �������������������
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
	 * ���Ĭ�Ϸַ���Ϣ���÷���
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
	 * ��÷��Ź���ͨ�����÷���
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
	 * ���ϵͳ���ɷ���
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
	 * ��û������ٵ�����
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
	 * ��û������ٵ�������ķ���
	 * 
	 * @return RecDesPaperTaskService �������ٵ����������
	 */
	public static RecDesPaperTaskService getRecDesPaperTaskService(){
		if(recDesPaperTaskService == null){
			recDesPaperTaskService = (RecDesPaperTaskService) SpringUtil.getBean("ddm_recdespapertask_service");
		}
		return recDesPaperTaskService;
	}

	/**
	 * @author kangyanfei
	 * �������ݲ�ѯ�ͷ�����Ϣ��������
	 * 
	 * @return DistributeOperationService �������ݲ�ѯ�ͷ�����Ϣ�����������
	 */
	public static DistributeOperationService getDistributeOperationService(){
		if(distributeOperationService == null){
			distributeOperationService = (DistributeOperationService) SpringUtil.getBean("ddm_distributeoperation_service");
		}
		return distributeOperationService;
	}

	/**
	 * ���ֽ��ǩ������ķ���
	 * @author zhangguoqiang 2014-09-11
	 * 
	 * @return DistributePaperSignTaskService ֽ��ǩ������ķ���
	 */
	public static DistributePaperSignTaskService getDistributePaperSignTaskService(){
		if(distributePaperSignTaskService == null){
			distributePaperSignTaskService = (DistributePaperSignTaskService) SpringUtil.getBean("ddm_distributepapersigntask_service");
		}
		return distributePaperSignTaskService;
	}
	
	/**
	 * ��ÿ�汾����ǩ����Ϣ����
	 * @author zhangguoqiang 2015-05-01
	 * 
	 * @return DcA3TaskSignatureService ��汾����ǩ����Ϣ����
	 */
	public static DcA3TaskSignatureService getDcA3TaskSignatureService(){
		if(dcA3TaskSignatureService == null){
			dcA3TaskSignatureService = (DcA3TaskSignatureService) SpringUtil.getBean("ddm_dca3tasksignature_service");
		}
		return dcA3TaskSignatureService;
	}
	/**
	 * ��ÿ�汾����ǩ����Ϣ��ַ�ҵ�����link����
	 * @author zhangguoqiang 2015-05-01
	 * 
	 * @return dcA3SigObjectLinkService ��汾����ǩ����Ϣ��ַ�ҵ�����link����
	 */
	public static DcA3SigObjectLinkService getDcA3SigObjectLinkService(){
		if(dcA3SigObjectLinkService == null){
			dcA3SigObjectLinkService = (DcA3SigObjectLinkService) SpringUtil.getBean("ddm_dca3sigobjectlink_service");
		}
		return dcA3SigObjectLinkService;
	}
}
