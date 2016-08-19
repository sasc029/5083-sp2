package com.bjsasc.adm.active.action.admstatistics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.bjsasc.adm.active.action.AbstractAction;
import com.bjsasc.adm.active.helper.AdmHelper;
import com.bjsasc.adm.active.service.activestatisticsservice.ActiveStatisticsService;
import com.bjsasc.adm.common.ActiveModelUtil;
import com.bjsasc.platform.objectmodel.managed.model.ClassAttrDef;
import com.bjsasc.plm.KeyS;
import com.bjsasc.plm.core.util.StringUtil;

/**
 * 统计与输出Action实现类。
 * 
 * @author guowei 2013-5-30
 */
@SuppressWarnings("deprecation")
public class ActiveStatisticsAction extends AbstractAction {

	/** serialVersionUID */
	private static final long serialVersionUID = 8735049587920142993L;

	/** 统计与输出服务 */
	ActiveStatisticsService statisticsService = AdmHelper.getActiveStatisticsService();

	//private Logger log = Logger.getLogger(this.getClass());

	/**
	 * 取得模型标识OID。
	 * 
	 * @return JSON对象
	 */
	public String getComModelOids() {
		try {
			String oids = request.getParameter(KeyS.OIDS);
			String comModelOids = statisticsService.getModelId(oids);
			// 输出结果
			result.put("success", "true");
			result.put("modelOids", comModelOids);
		} catch (Exception ex) {
			jsonError(ex);
		}
		mapToSimpleJson();
		return OUTPUTDATA;
	}

	public String getClassAttrList() {
		//String oids = request.getParameter(KeyS.OIDS);
		//listMap = statisticsService.getClassAttrList(oids);
		List<ClassAttrDef> activeModel = ActiveModelUtil.activeModels;
		// 格式化显示数据
		//TypeService typeManager = Helper.getTypeManager();
		for (ClassAttrDef target : activeModel) {
			if (target == null) {
				continue;
			}
			Map<String, Object> mainMap = new HashMap<String, Object>();
			mainMap.put("ID", target.getAttrId());
			mainMap.put("ATTRNAME", target.getAttrName());
			mainMap.put("DATATYPE", target.getDataType());
			//Map<String, Object> mainMap = typeManager.format((PTFactor) target);
			listMap.add(mainMap);
		}
		listToJson();
		return OUTPUTDATA;
	}

	public String getListStatistics() {
		try {
			//String modelOids = request.getParameter("MODELOIDS");
			String folderOids = request.getParameter("activeDocumentId");
			String file = request.getParameter("files");
			//listMap = statisticsService.getClassAttrList(modelOids);
			List<ClassAttrDef> activeModel = ActiveModelUtil.activeModels;
			// 格式化显示数据
			//TypeService typeManager = Helper.getTypeManager();
			for (ClassAttrDef target : activeModel) {
				if (target == null) {
					continue;
				}
				Map<String, Object> mainMap = new HashMap<String, Object>();
				mainMap.put("ID", target.getAttrId());
				mainMap.put("ATTRNAME", target.getAttrName());
				mainMap.put("DATATYPE", target.getDataType());
				//Map<String, Object> mainMap = typeManager.format((PTFactor) target);
				listMap.add(mainMap);
			}
			Map<String, Object> optallMap = new HashMap<String, Object>();
			List<Map<String, Object>> stList = new ArrayList<Map<String, Object>>();
			List<Map<String, String>> selList = new ArrayList<Map<String, String>>();
			List<Map<String, Object>> optList = new ArrayList<Map<String, Object>>();
			for (int i = 0; i < listMap.size(); i++) {
				String id = (String) listMap.get(i).get("ID");
				String name = (String) listMap.get(i).get("ATTRNAME");
				String[] stValue = request.getParameterValues("st_" + id);
				if (!StringUtil.isNull(stValue)) {
					Map<String, Object> stMap = new HashMap<String, Object>();
					stMap.put("id", id);
					stMap.put("name", name);
					stMap.put(id + "_value", stValue);
					stList.add(stMap);
				}
				String[] selValue = request.getParameterValues("sel_" + id);
				String[] selContext = request.getParameterValues("selContext_" + id);
				String[] andOrValue = request.getParameterValues("andOr_" + id);
				if (!StringUtil.isNull(selContext)) {
					for (int j = 0; j < selContext.length; j++) {
						Map<String, String> selMap = new HashMap<String, String>();
						selMap.put("id", id);
						selMap.put("context", selContext[j]);
						if (!StringUtil.isNull(selValue)) {
							selMap.put("sel_" + id, selValue[j]);
						}
						if (!StringUtil.isNull(andOrValue)) {
							selMap.put("andOr", andOrValue[j]);
						}
						selList.add(selMap);
					}
				}
				String[] optValue = request.getParameterValues("opt_" + id);
				String[] optName = request.getParameterValues("opt_name_" + id);
				if (!StringUtil.isNull(optValue)) {

					Map<String, Object> optMap = new HashMap<String, Object>();
					optMap.put("id", id);
					optMap.put("opt", optValue);
					optMap.put("name", optName);
					optList.add(optMap);
				}
			}

			session.setAttribute("optname", optList);
			String strid = "";
			String strname = "";
			String stropt = "0";
			listMap = statisticsService.getActiveDocumentlist(folderOids, file, stList, selList, optList);
			for (int h = 0; h < optList.size(); h++) {
				String id = (String) optList.get(h).get("id");
				String[] optname = (String[]) optList.get(h).get("name");
				String[] opt = (String[]) optList.get(h).get("opt");
				if (opt.length == 2) {
					for (int t = 0; t < opt.length; t++) {
						for (int m = t + 1; m < opt.length; m++) {
							if (opt[t].equals(opt[m])) {
								stropt = "1";
							}
						}

					}
				}
				strid += id + ",";
				for (int p = 0; p < optname.length; p++) {
					strname += optname[p] + ",";
				}

			}
			strname += stropt + ",";
			session.setAttribute("listStatistic", listMap);
			optallMap.put("optid", strid);
			optallMap.put("optname", strname);
			listMap.add(optallMap);
			success();
		} catch (Exception ex) {
			error(ex);
		}
		listToJson();
		return OUTPUTDATA;
	}

	public void dataExport() {
		String filename = "统计查询";
		try {
			List<Map<String, Object>> listname = (List<Map<String, Object>>) session.getAttribute("optname");
			List<Map<String, Object>> listStatistic = (List<Map<String, Object>>) session.getAttribute("listStatistic");
			// 取得对象。
			HSSFWorkbook wBook = statisticsService.getExcelObject(listname, listStatistic);

			// Excel文件输出。
			exportExcel(wBook, filename);
		} catch (Exception ex) {
			//log.debug("Excel导出异常", ex);
			throw new RuntimeException(ex);
		}

	}
}
