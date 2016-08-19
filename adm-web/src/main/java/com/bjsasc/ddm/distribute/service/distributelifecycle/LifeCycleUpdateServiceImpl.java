package com.bjsasc.ddm.distribute.service.distributelifecycle;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.bjsasc.platform.config.util.ConfigServiceUtil;
import com.bjsasc.platform.lifecycle.model.PhaseDefinitionBean;
import com.bjsasc.platform.lifecycle.util.LifeCycleManagedUtil;
import com.bjsasc.platform.objectmodel.business.lifeCycle.LifeCycleInfo;
import com.bjsasc.platform.objectmodel.business.lifeCycle.LifeCycleTemplate;
import com.bjsasc.platform.objectmodel.business.lifeCycle.LifeCycleUtil;
import com.bjsasc.platform.objectmodel.business.lifeCycle.State;
import com.bjsasc.plm.core.Helper;
import com.bjsasc.plm.core.context.ContextHelper;
import com.bjsasc.plm.core.context.model.Context;
import com.bjsasc.plm.core.context.template.TemplateHelper;
import com.bjsasc.plm.core.lifecycle.LifeCycleHelper;
import com.bjsasc.plm.core.lifecycle.LifeCycleManaged;
import com.bjsasc.plm.core.persist.model.Persistable;
import com.bjsasc.plm.core.system.config.ConfigInitUtil;
import com.bjsasc.plm.core.util.ConfigFileUtil;
import common.Logger;

/**
 * 生命周期升级服务实现类。
 * 
 * @author gengancong 2014-5-15
 */
public class LifeCycleUpdateServiceImpl implements LifeCycleUpdateService {

	private static final Logger LOG = Logger.getLogger(ConfigInitUtil.class);
	Context context = ContextHelper.getService().getRootContext();
	private List<Map<String, String>> paramList = new ArrayList<Map<String, String>>();

	@Override
	public List<String> updateLifeCycle(List<Map<String, String>> paramList,
			String step) {
		this.paramList = paramList;
		List<String> results = new ArrayList<String>();
		try {
			if (step == null) {
				step = "";
			}

			if (step.indexOf("1") >= 0) {
				// 删除生命周期状态定义
				String folderId = "/platform/LifeCycle/stateDefinition/adm";
				deleteLifeCycleState(folderId, results);
				folderId = "/platform/LifeCycle/stateDefinition/ddm";
				deleteLifeCycleState(folderId, results);
			}

			if (step.indexOf("2") >= 0) {
				List<String> tList = new ArrayList<String>();
				tList.add("发放单生命周期");
				tList.add("分发对象生命周期");
				tList.add("分发信息生命周期");
				tList.add("电子任务生命周期");
				tList.add("纸质任务生命周期");
				tList.add("现行数据生命周期");
				// 删除生命周期模板
				for (String lifeCycleName : tList) {
					deleteLifeCycle(lifeCycleName, results);
				}
			}
			if (step.indexOf("3") >= 0) {

				results.add("正在导入生命周期状态定义");
				// 导入生命周期状态定义
				ConfigInitUtil.init();
				results.add("导入生命周期状态定义完成");
			}

			if (step.indexOf("4") >= 0) {
				// 导入生命周期模板
				importLifeCycleTemplate(results);
			}

			if (step.indexOf("5") >= 0) {
				results.add("正在更新现有数据");
				for (Map<String, String> params : paramList) {
					// 更新现有数据
					int cnt = updateLifeCycle(params, results);
					String log = params.get("templateName") + ": ( "
							+ params.get("objectName") + " ) ["
							+ params.get("stateNameFrom") + "] --> ["
							+ params.get("stateNameTo") + "] [ " + cnt
							+ " ]<br>";
					results.add(log);
				}
				results.add("更新现有数据完成");
			}
		} finally {
			for (String log : results) {
				LOG.debug(log);
			}
		}

		return results;
	}

	/**
	 * 删除生命周期状态
	 */
	private void deleteLifeCycleState(String folderId, List<String> results) {
		results.add("正在删除生命周期状态定义【" + folderId + "】");
		String sql = "SELECT INNERID FROM PT_CONFIG_FOLDER WHERE FOLDERID = ?";
		List<Map<String, Object>> mapList = Helper.getPersistService().query(
				sql, folderId);

		List<String> innerIdList = new ArrayList<String>();
		for (Map<String, Object> map : mapList) {
			String innerId = String.valueOf(map.get("INNERID"));

			if (innerId == null || innerId.length() == 0) {
				return;
			}
			innerIdList.add(innerId);
		}
		if (innerIdList != null && innerIdList.size() > 0) {
			// 按innerid进行删除操作
			ConfigServiceUtil.getConfigService().deleteFoldersByInnerIds(
					innerIdList);
		}
		results.add("删除生命周期状态定义【" + folderId + "】完成");
	}

	/**
	 * 删除生命周期模板
	 * 
	 * @param lifeCycleName
	 *            生命周期模板名称
	 */
	private void deleteLifeCycle(String lifeCycleName, List<String> results) {
		results.add("正在删除生命周期模板【" + lifeCycleName + "】");
		String delSql = "";
		String sql = "SELECT INNERID FROM PT_LFCY_LIFECYCLEDEFMASTER WHERE NAME = ?";
		List<Map<String, Object>> mapList = Helper.getPersistService().query(
				sql, lifeCycleName);

		String masterId = "";
		if (mapList != null && mapList.size() > 0) {
			masterId = String.valueOf(mapList.get(0).get("INNERID"));
		}
		if (masterId == null || masterId.length() == 0) {
			return;
		}

		sql = "SELECT * FROM PT_LFCY_LIFECYCLEDEF WHERE MASTERIID = ? ";
		mapList = Helper.getPersistService().query(sql, masterId);

		for (Map<String, Object> map : mapList) {
			String lcId = String.valueOf(map.get("INNERID"));

			if (lcId == null || lcId.length() == 0) {
				continue;
			}

			// delete PT_LFCY_LIFECYCLETYPELINK
			delSql = "DELETE FROM PT_LFCY_LIFECYCLETYPELINK WHERE LIFECYCLEDEFIID = ?";
			Helper.getPersistService().bulkUpdateBySql(delSql, lcId);

			// delete PT_LFCY_PHASEDEF
			delSql = "DELETE FROM PT_LFCY_PHASEDEF WHERE LIFECYCLEDEFIID = ?";
			Helper.getPersistService().bulkUpdateBySql(delSql, lcId);
		}

		sql = "SELECT * FROM PLM_TEMPLATE_LIFECYCLE WHERE LIFECYCLETEMPLATEMASTERINNERID = ? ";
		mapList = Helper.getPersistService().query(sql, masterId);

		for (Map<String, Object> map : mapList) {
			String tlcId = String.valueOf(map.get("INNERID"));

			if (tlcId == null || tlcId.length() == 0) {
				continue;
			}
			// delete PLM_FOLDER_MEMBERLINK
			delSql = "DELETE FROM PLM_FOLDER_MEMBERLINK WHERE FROMOBJECTID = ?";
			Helper.getPersistService().bulkUpdateBySql(delSql, tlcId);
		}

		// delete PLM_TEMPLATE_LIFECYCLE
		delSql = "DELETE FROM PLM_TEMPLATE_LIFECYCLE WHERE LIFECYCLETEMPLATEMASTERINNERID = ?";
		Helper.getPersistService().bulkUpdateBySql(delSql, masterId);

		// delete PT_LFCY_LIFECYCLEDEF
		delSql = "DELETE FROM PT_LFCY_LIFECYCLEDEF WHERE MASTERIID = ?";
		Helper.getPersistService().bulkUpdateBySql(delSql, masterId);

		// delete PT_LFCY_LIFECYCLEDEFMASTER
		delSql = "DELETE FROM PT_LFCY_LIFECYCLEDEFMASTER WHERE NAME = ?";
		Helper.getPersistService().bulkUpdateBySql(delSql, lifeCycleName);

		results.add("删除生命周期模板【" + lifeCycleName + "】完成");
	}

	/**
	 * 生命周期导入
	 * 从默认路径下AVIDM_HOME/plm/core/init/context/root/template_lifecycle/default
	 * .zip
	 */
	private void importLifeCycleTemplate(List<String> results) {
		results.add("正在导入生命周期模板【AVIDM_HOME/plm/core/init/context/root/template_lifecycle/default.zip】");

		// File file = new File(filePath);
		// String serviceName = ContextInfoModelHelper.serviceNameMap
		// .get(ContextInfoModel.LIFECYCLETEMPLATE.toString());
		// ContextTemplateAttachmentService contextTemplateAttachmentService =
		// ContextInfoModelHelper
		// .getService(serviceName);
		// contextTemplateAttachmentService.initContextInfo(context, file);
		// 生命周期模板
		String configPath = "init/context/root/template_lifecycle";

		List<File> files = ConfigFileUtil.listFiles(configPath);
		for (File file : files) {
			LifeCycleHelper.getService().importLifeCycleTemplate(
					file.getAbsolutePath(), context);
			com.bjsasc.plm.core.context.template.LifeCycleUtil
					.createLifeCycleTemplates(context);
		}

		results.add("导入生命周期模板完成");
	}

	/**
	 * 更新生命周期。
	 * 
	 * @param params
	 * @return
	 */
	private int updateLifeCycle(Map<String, String> params, List<String> results) {
		String templateName = params.get("templateName");
		String stateNameFrom = params.get("stateNameFrom");
		String stateNameTo = params.get("stateNameTo");
		String objectName = params.get("objectName");

		String hql = "from " + objectName
				+ " t where t.lifeCycleInfo.stateName = ?";
		List<Persistable> list = Helper.getPersistService().find(hql,
				stateNameFrom);

		for (Persistable target : list) {
			updateLifeCycle(target, templateName, stateNameTo, results);
		}
		return list.size();
	}

	/**
	 * 更新生命周期。
	 * 
	 * @param target
	 * @param templateName
	 * @param stateName
	 */
	private void updateLifeCycle(Persistable target, String templateName,
			String stateName, List<String> results) {

		String classId = target.getClassId();
		LifeCycleTemplate lifeCycleTemplate = getLifeCycleTemplate(classId,
				templateName);

		State state = getState(lifeCycleTemplate, stateName);

		LifeCycleManaged lifeCycleManaged = (LifeCycleManaged) target;
		com.bjsasc.platform.objectmodel.business.lifeCycle.LifeCycleService lifeCycleService = LifeCycleUtil
				.getService();
		lifeCycleService.setLifeCycleTemplate(lifeCycleManaged,
				lifeCycleTemplate, state);

		updateLifeCycleHis(lifeCycleManaged, results);
	}

	/**
	 * 更新生命周期相关数据。 1，PT_LFCY_OBJLIFECYCLEHISTORY 2，PT_LFCY_LIFECYCLEINSTANCE
	 * 
	 * @param lifeCycleManaged
	 *            LifeCycleManaged
	 * @param results
	 *            List
	 */
	private void updateLifeCycleHis(LifeCycleManaged lifeCycleManaged,
			List<String> results) {

		String innerId = lifeCycleManaged.getInnerId();
		String classId = lifeCycleManaged.getClassId();

		LifeCycleInfo lifeCycleInfo = lifeCycleManaged.getLifeCycleInfo();
		String stateId = lifeCycleInfo.getStateId();
		String lifeCycleTemplateId = lifeCycleInfo.getLifeCycleTemplate();

		String updSql = "UPDATE PT_LFCY_LIFECYCLEINSTANCE SET LIFECYCLEDEFIID=?,CURRENTPHASEIID=?"
				+ " WHERE OBJECTIID=? AND CLASSID=? ";
		Helper.getPersistService().bulkUpdateBySql(updSql, lifeCycleTemplateId,
				stateId, innerId, classId);

		String sql = "SELECT * FROM PT_LFCY_OBJLIFECYCLEHISTORY WHERE OBJECTIID=? AND CLASSID=? ";
		List<Map<String, Object>> mapList = Helper.getPersistService().query(
				sql, innerId, classId);
		for (Map<String, Object> map : mapList) {
			String hidInnerId = String.valueOf(map.get("INNERID"));
			String lifeCycleName = String.valueOf(map.get("LIFECYCLENAME"));
			String oldStateName = String.valueOf(map.get("STATENAME"));

			String[] ids = getTargetLCTinfo(lifeCycleName, oldStateName);
			if (ids[1] == null) {
				continue;
			}
			String phaseIId = ids[1];
			String stateName = ids[2];
			String lifeCycleDefIId = ids[0];

			updSql = "UPDATE PT_LFCY_OBJLIFECYCLEHISTORY SET LIFECYCLEDEFIID=?,PHASEIID=?,STATENAME=?"
					+ " WHERE INNERID=? ";

			Helper.getPersistService().bulkUpdateBySql(updSql, lifeCycleDefIId,
					phaseIId, stateName, hidInnerId);
		}
	}

	/**
	 * 取得指定的生命周期信息。
	 * 
	 * @param lifeCycleName
	 *            String
	 * @param oldStateName
	 *            String
	 * @return String[]
	 */
	private String[] getTargetLCTinfo(String lifeCycleName, String oldStateName) {
		String[] ids = new String[3];
		for (Map<String, String> params : this.paramList) {
			String templateName = params.get("templateName");
			String stateNameFrom = params.get("stateNameFrom");
			String stateNameTo = params.get("stateNameTo");
			String objectName = params.get("objectName");

			if (lifeCycleName.equals(templateName)
					&& oldStateName.equals(stateNameFrom)) {
				String targetStateName = stateNameTo;
				LifeCycleTemplate lifeCycleTemplate = getLifeCycleTemplate(
						objectName, templateName);
				String lifeCycleTemplateId = lifeCycleTemplate.getInnerId();
				State state = getState(lifeCycleTemplate, targetStateName);

				PhaseDefinitionBean initPhase = LifeCycleManagedUtil
						.getPhaseDefService().getPhaseByState(
								lifeCycleTemplate.getInnerId(), state.getId());
				String stateId = initPhase.getInnerId();
				ids[0] = lifeCycleTemplateId;
				ids[1] = stateId;
				ids[2] = targetStateName;
			}

		}
		return ids;
	}

	/**
	 * 取得指定的生命周期模板。
	 * 
	 * @param classId
	 *            String
	 * @param templateName
	 *            String
	 * @return LifeCycleTemplate
	 */
	private LifeCycleTemplate getLifeCycleTemplate(String classId,
			String templateName) {
		LifeCycleTemplate lifeCycleTemplate = null;

		if ("DistributeOrderObjectLink".equals(classId)) {
			classId = "DistributeObject";
		}
		List<LifeCycleTemplate> templates = TemplateHelper.getService()
				.findLifeCycleTemplatesEnabled(context, classId);
		if (templates.size() == 0) {
			throw new RuntimeException("未找到该对象类型绑定的且已启用的生命周期模板");
		}
		for (LifeCycleTemplate template : templates) {
			if (template.getName().equals(templateName)) {
				lifeCycleTemplate = template;
				break;
			}
		}
		if (lifeCycleTemplate == null) {
			throw new RuntimeException("在对象绑定的生命周期模板中,未找到初始化规则指定的模板:"
					+ templateName);
		}
		return lifeCycleTemplate;
	}

	/**
	 * 取得指定生命周期模板中的节点。
	 * 
	 * @param lifeCycleTemplate
	 *            LifeCycleTemplate
	 * @param stateName
	 *            String
	 * @return State
	 */
	private State getState(LifeCycleTemplate lifeCycleTemplate, String stateName) {
		State state = null;
		if (stateName == null) {
			return state;
		}
		// 在目标模板里找给定的状态
		List<State> templateStates = Helper.getLifeCycleService().findStates(
				lifeCycleTemplate);

		for (State s : templateStates) {
			if (stateName.equals(s.getName())) {
				state = s;
				break;
			}
		}
		return state;
	}
}
