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
 * 现行文件管理 Helper
 * 
 * @author yanjia 2013-5-13
 */
public abstract class AdmHelper {

	/** 现行文件服务 */
	private static ActiveDocumentService activeDocumentService;
	/** 现行单据服务 */
	private static ActiveOrderService activeOrderSerivice;
	/** 现行套服务 */
	private static ActiveSetService activeSetSerivice;
	/** 现行文件模型服务 */
	private static AdmModelService admModelSerivice;
	/** Excel数据服务 */
	private static ExcelDataService excelDataService;
	/** Xml数据服务 */
	private static XmlDataService xmlDataService;
	/** 回收站服务 */
	private static ActiveRecycleService activeRecycleService;
	/** 统计与输出服务 */
	private static ActiveStatisticsService activeStatisticsService;
	/** 模型视图服务 */
	private static ModelViewService modelViewService;
	/** 生命周期服务*/
	private static ActiveLifecycleService activeLifecycleService;
	/** 上下文服务 */
	private static ActiveContextService activeContextSerivce;
	/**现行统计图服务*/
	private static ActiveChangeService activeChangeService;
	/** 校验权限服务 */
	private static ActiveOptValidationService activeOptValidationService;
	/** 现行文件,现行单据,现行套直接入库服务 */
	private static PutIntoStorageService putIntoStorageService;
	/** 现行子文件夹 文件夹管理服务类 */
	private static FolderManagerService activeSubFolderManagerService;

	/**
	 * 获得现行文件服务
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
	 * 获得现行单据服务
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
	 * 获得现行套服务
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
	 * 获得现行文件模型服务
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
	 *  Excel数据服务
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
	 *  Xml数据服务
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
	 *  回收站服务
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
	 * 获得现行文件服务
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
	 * 获得模型视图服务
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
	 * 获得生命周期服务
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
	 * 上下文管理业务实现
	 * @return
	 */
	public static ActiveContextService getActiveContextService() {
		if (activeContextSerivce == null) {
			activeContextSerivce = (ActiveContextService) SpringUtil.getBean("adm_active_context_service");
		}
		return activeContextSerivce;
	}
	/**
	 * 获得现行对象服务
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
	 * 校验权限服务
	 * @return
	 */
	public static ActiveOptValidationService getActiveOptValidationService() {
		if (activeOptValidationService == null) {
			activeOptValidationService = (ActiveOptValidationService) SpringUtil.getBean("adm_optvalidation_service");
		}
		return activeOptValidationService;
	}

	/**
	 * 获得现行文件,现行单据,现行套直接入库服务
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
	 * 获得现行子文件夹 文件夹管理服务类
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
