package com.bjsasc.adm.active.helper;

import com.bjsasc.adm.active.service.PutIntoStorageService;
import com.bjsasc.adm.active.service.activechangeservice.ActiveChangeService;
import com.bjsasc.adm.active.service.activecontext.ActiveContextService;
import com.bjsasc.adm.active.service.activedocumentservice.ActiveDocumentService;
import com.bjsasc.adm.active.service.activelifecycle.ActiveLifecycleService;
import com.bjsasc.adm.active.service.activeoptvalidation.ActiveOptValidationService;
import com.bjsasc.adm.active.service.activeorderservice.ActiveOrderService;
import com.bjsasc.adm.active.service.activerecycleservice.ActiveRecycleService;
import com.bjsasc.adm.active.service.activesetservice.ActiveSetService;
import com.bjsasc.adm.active.service.activestatisticsservice.ActiveStatisticsService;
import com.bjsasc.adm.active.service.admmodelservice.AdmModelService;
import com.bjsasc.adm.active.service.exceldataservice.ExcelDataService;
import com.bjsasc.adm.active.service.modelviewservice.ModelViewService;
import com.bjsasc.adm.active.service.xmldataservice.XmlDataService;
import com.bjsasc.plm.core.util.SpringUtil;
import com.bjsasc.plm.folder.FolderManagerService;

/**
 * �����ļ����� Helper
 * 
 * @author yanjia 2013-5-13
 */
public abstract class AdmHelper {

	/** �����ļ����� */
	private static ActiveDocumentService activeDocumentService;
	/** ���е��ݷ��� */
	private static ActiveOrderService activeOrderSerivice;
	/** �����׷��� */
	private static ActiveSetService activeSetSerivice;
	/** �����ļ�ģ�ͷ��� */
	private static AdmModelService admModelSerivice;
	/** Excel���ݷ��� */
	private static ExcelDataService excelDataService;
	/** Xml���ݷ��� */
	private static XmlDataService xmlDataService;
	/** ����վ���� */
	private static ActiveRecycleService activeRecycleService;
	/** ͳ����������� */
	private static ActiveStatisticsService activeStatisticsService;
	/** ģ����ͼ���� */
	private static ModelViewService modelViewService;
	/** �������ڷ���*/
	private static ActiveLifecycleService activeLifecycleService;
	/** �����ķ��� */
	private static ActiveContextService activeContextSerivce;
	/**����ͳ��ͼ����*/
	private static ActiveChangeService activeChangeService;
	/** У��Ȩ�޷��� */
	private static ActiveOptValidationService activeOptValidationService;
	/** �����ļ�,���е���,������ֱ�������� */
	private static PutIntoStorageService putIntoStorageService;
	/** �������ļ��� �ļ��й�������� */
	private static FolderManagerService activeSubFolderManagerService;

	/**
	 * ��������ļ�����
	 * 
	 * @return activeDocumentService
	 */
	public static ActiveDocumentService getActiveDocumentService() {
		if (activeDocumentService == null) {
			activeDocumentService = (ActiveDocumentService) SpringUtil.getBean("adm_activedocumentservice");
		}
		return activeDocumentService;
	}

	/**
	 * ������е��ݷ���
	 * 
	 * @return activeOrderSerivice
	 */
	public static ActiveOrderService getActiveOrderService() {
		if (activeOrderSerivice == null) {
			activeOrderSerivice = (ActiveOrderService) SpringUtil.getBean("adm_activeorderservice");
		}
		return activeOrderSerivice;
	}

	/**
	 * ��������׷���
	 * 
	 * @return activeSetSerivice
	 */
	public static ActiveSetService getActiveSetService() {
		if (activeSetSerivice == null) {
			activeSetSerivice = (ActiveSetService) SpringUtil.getBean("adm_activesetservice");
		}
		return activeSetSerivice;
	}

	/**
	 * ��������ļ�ģ�ͷ���
	 * 
	 * @return activeModelSerivice
	 */
	public static AdmModelService getAdmModelService() {
		if (admModelSerivice == null) {
			admModelSerivice = (AdmModelService) SpringUtil.getBean("adm_activemodelservice");
		}
		return admModelSerivice;
	}

	/**
	 *  Excel���ݷ���
	 * 
	 * @return excelDataService
	 */
	public static ExcelDataService getExcelDataService() {
		if (excelDataService == null) {
			excelDataService = (ExcelDataService) SpringUtil.getBean("adm_exceldataservice");
		}
		return excelDataService;
	}

	/**
	 *  Xml���ݷ���
	 * 
	 * @return xmlDataService
	 */
	public static XmlDataService getXmlDataService() {
		if (xmlDataService == null) {
			xmlDataService = (XmlDataService) SpringUtil.getBean("adm_xmldataservice");
		}
		return xmlDataService;
	}

	/**
	 *  ����վ����
	 *  
	 * @return activeRecycleService
	 */
	public static ActiveRecycleService getActiveRecycleService() {
		if (activeRecycleService == null) {
			activeRecycleService = (ActiveRecycleService) SpringUtil.getBean("adm_activerecycleservice");
		}
		return activeRecycleService;
	}

	/**
	 * ��������ļ�����
	 * 
	 * @return activeDocumentService
	 */
	public static ActiveStatisticsService getActiveStatisticsService() {
		if (activeStatisticsService == null) {
			activeStatisticsService = (ActiveStatisticsService) SpringUtil.getBean("adm_activestatisticsservice");
		}
		return activeStatisticsService;
	}

	/**
	 * ���ģ����ͼ����
	 * 
	 * @return ModelViewService
	 */
	public static ModelViewService getModelViewService() {
		if (modelViewService == null) {
			modelViewService = (ModelViewService) SpringUtil.getBean("adm_modelviewservice");
		}
		return modelViewService;
	}

	/**
	 * ����������ڷ���
	 * 
	 * @return activeLifecycleService
	 */
	public static ActiveLifecycleService getActiveLifecycleService() {
		if (activeLifecycleService == null) {
			activeLifecycleService = (ActiveLifecycleService) SpringUtil.getBean("adm_activelifecycleservice");
		}
		return activeLifecycleService;
	}

	/**
	 * �����Ĺ���ҵ��ʵ��
	 * @return
	 */
	public static ActiveContextService getActiveContextService() {
		if (activeContextSerivce == null) {
			activeContextSerivce = (ActiveContextService) SpringUtil.getBean("adm_active_context_service");
		}
		return activeContextSerivce;
	}
	/**
	 * ������ж������
	 * 
	 * @return activeDocumentService
	 */
	public static ActiveChangeService getActiveChangeService() {
		if (activeChangeService == null) {
			activeChangeService = (ActiveChangeService) SpringUtil.getBean("adm_activechangeservice");
		}
		return activeChangeService;
	}
	
	/**
	 * У��Ȩ�޷���
	 * @return
	 */
	public static ActiveOptValidationService getActiveOptValidationService() {
		if (activeOptValidationService == null) {
			activeOptValidationService = (ActiveOptValidationService) SpringUtil.getBean("adm_optvalidation_service");
		}
		return activeOptValidationService;
	}

	/**
	 * ��������ļ�,���е���,������ֱ��������
	 * 
	 * @return putIntoStorageService
	 */
	public static PutIntoStorageService getPutIntoStorageService() {
		if (putIntoStorageService == null) {
			putIntoStorageService = (PutIntoStorageService) SpringUtil.getBean("adm_putintostorageservice");
		}
		return putIntoStorageService;
	}

	/**
	 * ����������ļ��� �ļ��й��������
	 * 
	 * @return activeSubFolderManagerService
	 */
	public static FolderManagerService getActiveSubFolderManagerService() {
		if (activeSubFolderManagerService == null) {
			activeSubFolderManagerService = (FolderManagerService) SpringUtil.getBean("adm_subfolder_manager");
		}
		return activeSubFolderManagerService;
	}
	
}
