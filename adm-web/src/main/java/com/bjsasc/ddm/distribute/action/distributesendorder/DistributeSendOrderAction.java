package com.bjsasc.ddm.distribute.action.distributesendorder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.bjsasc.ddm.common.ConstUtil;
import com.bjsasc.ddm.distribute.action.AbstractAction;
import com.bjsasc.ddm.distribute.helper.DistributeHelper;
import com.bjsasc.ddm.distribute.model.distributeobject.DistributeObject;
import com.bjsasc.ddm.distribute.model.distributeobjectprint.DistributeObjectPrint;
import com.bjsasc.ddm.distribute.model.distributepapertask.DistributePaperTask;
import com.bjsasc.ddm.distribute.service.distributecommonconfig.DistributeCommonConfigService;
import com.bjsasc.ddm.distribute.service.distributeobjectprint.DistributeObjectPrintService;
import com.bjsasc.ddm.distribute.service.distributepapersigntask.DistributePaperSignTaskService;
import com.bjsasc.ddm.distribute.service.distributesendorder.DistributeSendOrderService;
import com.bjsasc.plm.Helper;
import com.bjsasc.plm.KeyS;
import com.bjsasc.plm.core.context.model.Context;
import com.bjsasc.plm.core.persist.model.Persistable;
import com.bjsasc.plm.core.security.audit.AuditLogHelper;
import com.bjsasc.plm.core.session.SessionHelper;
import com.bjsasc.plm.core.util.JsonUtil;
import com.bjsasc.plm.grid.data.GridDataUtil;
import com.bjsasc.plm.type.TypeService;
import com.cascc.avidm.util.SplitString;

import org.apache.log4j.Logger;

/**
 * 内发单Action实现类。
 * 
 * @author guowei 2013-3-25
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class DistributeSendOrderAction extends AbstractAction {

	/** serialVersionUID */
	private static final long serialVersionUID = 2190079956987592015L;
	private  static final Logger LOG = Logger.getLogger(DistributeSendOrderAction.class);
	private final DistributeSendOrderService  service = DistributeHelper.getDistributeSendOrderService();

	Map<String, String> map = new HashMap<String, String>();

	/**
	 * 初始化页面
	 * @return
	 */
	public String initDistributeInSideLaod() {
		initParam();
		try {
			// 输出结果
			GridDataUtil.prepareRowObjectMaps(listMap, ConstUtil.SOPT_LISTDISTRIBUTEINSIDEORDERS);
			request.setAttribute("init", "true");
			request.setAttribute("sendType", request.getParameter("sendType"));
			
		} catch (Exception ex) {
			error(ex);
		}
		return INITPAGE;
	}

	/**
	 * 发放管理--内发单/外发单菜单
	 * 页面数据一栏显示。
	 * 取得纸质任务生命周期为【发送中】的数据。
	 * /ddm/distributesendorder/listDistributeInsideSearch.jsp
	 * 
	 * @return struts页面参数
	 */
	public String getAllDistributeInsideTasks() {
		initParam();
		session.setAttribute("DDM_DISTRIBUTE_INSIDE", map);
		try {
			String lc = ConstUtil.LC_DISTRIBUTING.getName();
			List<DistributePaperTask> listDis = service.getAllDistributePaperTask(map);
			LOG.debug("取得内外发单纸质任务 " + getDataSize(listDis) + " 条,生命周期状态：" + lc);
			
			String spot = ConstUtil.SOPT_LISTDISTRIBUTEINSIDEORDERS;
			
			// 批量验证权限
			List<Map<String, Object>> listDatas = checkPermissionAF(listDis, spot, spot);
			
			// 输出结果
			GridDataUtil.prepareRowObjects(listDatas, spot);
			
			result.put("success","true");
			result.put("flag", listDis.size()+"");
			
			//success();
		} catch (Exception ex) {
			jsonError(ex);
		}
		mapToSimpleJson();
		return OUTPUTDATA;
	}

	/**
	 * 取得内外发单纸质任务属性。
	 * 
	 * @return JSON对象
	 */
	public String getDistributePaperTaskList() {
		try {
			map = (Map<String, String>) session.getAttribute("DDM_DISTRIBUTE_INSIDE");
			String oid=(String)session.getAttribute(ConstUtil.SPOT_DISTRIBUTEPAPERTASK_OID);
			List<DistributeObject> list = service.getDistributePaperTaskPropertyList(map, oid);
			//		listMap = new FormatUtil<DistributeObject>().format(list, "DISURGENT");
			// 格式化显示数据
			TypeService typeManager = Helper.getTypeManager();
			for (DistributeObject target : list) {
				if (target == null) {
					continue;
				}
				DistributeObjectPrintService distributeobjecprintService=DistributeHelper.getDistributeObjectPrintService();
				List<DistributeObjectPrint> printList=distributeobjecprintService.getDisObjPrintByDisPaperTaskAndObjOID(oid, target.getOid());
				Map<String, Object> mapAll2 = new HashMap<String, Object>();
				if(printList!=null&&printList.size()>0){
					if(ConstUtil.OBJECT_PRINT_SIGN_YES.equals(printList.get(0).getIsprint())){
						mapAll2.put("isprint", "是");
					}
				}
				Map<String, Object> mapAll = new HashMap<String, Object>();
				Map<String, Object> mainMap = typeManager.format(target);
				Map<String, Object> linkMap = typeManager.format(target.getDistributeOrderObjectLink());
				Map<String, Object> sourceMap = new HashMap<String, Object>();
				Map<String, Object> sourceMap1 = (Map<String, Object>)mainMap.get(KeyS.SOURCE);
				Map<String, Object> sourceMap2 = (Map<String, Object>)linkMap.get(KeyS.SOURCE);
				sourceMap.putAll(sourceMap1);
				sourceMap.putAll(sourceMap2);
				mapAll.putAll(linkMap);
				mapAll.putAll(mainMap);
				mapAll.putAll(mapAll2);
				//mapAll.put(KeyS.SOURCE, sourceMap);
				listMap.add(mapAll);
			}
			LOG.debug("取得内外发单数据条数 " + getDataSize() + " 条");
			// 输出结果
			GridDataUtil.prepareRowObjectMaps(listMap, ConstUtil.SPOT_LISTDISTRIBUTETSKPROPERTYS);
			
		} catch (Exception ex) {
			error(ex);
		}
		return "paperTaskObjects";
	}
	/**
	 * 对DistributeObjectPrint的isprint字段进行插入“1（是）”
	 * @return
	 */
	
	public String   InsertDistributeObjectPrint() {
		try {
			String objOids=request.getParameter("oids");
			List<Map<String, Object>> oidsList = JsonUtil.toList(objOids);
			String oid=(String)session.getAttribute(ConstUtil.SPOT_DISTRIBUTEPAPERTASK_OID);
			DistributeObjectPrintService distributeobjecprintService = DistributeHelper.getDistributeObjectPrintService();
			List<DistributeObjectPrint> dpList = new ArrayList<DistributeObjectPrint>();
			for(int i=0; i<oidsList.size(); i++){
				String objOid = (String)oidsList.get(i).get("oid");
				if (objOid == null) {
					continue;
				}
				List<DistributeObjectPrint> printList=distributeobjecprintService.getDisObjPrintByDisPaperTaskAndObjOID(oid, objOid);
				if(printList.size()>0){
					continue;
				}else{
					DistributeObjectPrint dp = distributeobjecprintService.setDistributeObjectPrint(oid, objOid);
					dpList.add(dp);
				}
			}
			distributeobjecprintService.saveDistributeObjectPrint(dpList);
			result.put("success", "true");
		} catch (Exception ex) {
			error(ex);
		}
		mapToSimpleJson();
		return OUTPUTDATA;
	}
	
	
	/**
	 * 取得回收销毁单纸质任务属性。
	 * 
	 * @return JSON对象
	 * @author Sun Zongqing
	 * @Date 2014/7/1
	 */
	public String getRecDesPaperTaskList() {
		try {
			map = (Map<String, String>) session.getAttribute("DDM_DISTRIBUTE_INSIDE");
			String oid=(String)session.getAttribute(ConstUtil.SPOT_RECDESPAPERTASK_OID);
			List<DistributeObject> list = service.getDistributePaperTaskPropertyList(map, oid);
			// 格式化显示数据
			TypeService typeManager = Helper.getTypeManager();
			for (DistributeObject target : list) {
				if (target == null) {
					continue;
				}
				Map<String, Object> mapAll = new HashMap<String, Object>();
				Map<String, Object> mainMap = typeManager.format(target);
				Map<String, Object> linkMap = typeManager.format(target.getDistributeOrderObjectLink());
				Map<String, Object> sourceMap = new HashMap<String, Object>();
				Map<String, Object> sourceMap1 = (Map<String, Object>)mainMap.get(KeyS.SOURCE);
				Map<String, Object> sourceMap2 = (Map<String, Object>)linkMap.get(KeyS.SOURCE);
				sourceMap.putAll(sourceMap1);
				sourceMap.putAll(sourceMap2);
				mapAll.putAll(linkMap);
				mapAll.putAll(mainMap);
				mapAll.put(KeyS.SOURCE, sourceMap);
				listMap.add(mapAll);
			}
			LOG.debug("取得内外发单数据条数 " + getDataSize() + " 条");
			// 输出结果
			GridDataUtil.prepareRowObjectMaps(listMap, ConstUtil.SPOT_LISTDISTRIBUTETSKPROPERTYS);
			
		} catch (Exception ex) {
			error(ex);
		}
		return "paperTaskObjects";
	}

	public String updateDistributeInsideCycles() {
		try {
			String taskOid = request.getParameter(KeyS.OIDS);
			map = (Map<String, String>) session.getAttribute("DDM_DISTRIBUTE_INSIDE");
			service.updateDistributeInsideCycles(map, taskOid);

			// 生成纸质签收任务
			DistributeCommonConfigService distributeCommonConfigService = DistributeHelper.getDistributeCommonConfigService();
			String Is_OpenPaperSignTask = distributeCommonConfigService.getConfigValueByConfigId("Is_OpenPaperSignTask");
			//是否启用纸质签收任务(true:启用；false：不用)
			if ("true".equalsIgnoreCase(Is_OpenPaperSignTask)) {
				DistributePaperSignTaskService paperSignTaskService = DistributeHelper.getDistributePaperSignTaskService();
				Map tempMap = new HashMap();
				tempMap.put("oids", taskOid);
				tempMap.putAll(map);
				paperSignTaskService.createDistributePaperSignTasks(tempMap);
			}
			
			List<DistributePaperTask> listDis = service.getAllDistributePaperTask(map);
			
			String spot = ConstUtil.SOPT_LISTDISTRIBUTEINSIDEORDERS;
			// 批量验证权限
			List<Map<String, Object>> listDatas = checkPermissionAF(listDis, spot, spot);
			
			LOG.debug("取得内外发单纸质任务 " + getDataSize(listDis) + " 条,生命周期状态：" + ConstUtil.LC_DISTRIBUTING.getName());
			GridDataUtil.prepareRowObjects(listDatas, spot);
			success();
		} catch (Exception ex) {
			jsonError(ex);
		}
		mapToSimpleJson();
		return OUTPUTDATA;
	}

	private void initParam() {
		map.put("taskName", request.getParameter("taskName"));
		map.put("taskCode", getParameter("taskCode"));
		map.put("creator", getParameter("creator"));
		map.put("creatorName", request.getParameter("creatorName"));
		map.put("disInfo", getParameter("disInfo"));
		map.put("infoClassId", getParameter("infoClassId"));
		map.put("disInfoName", request.getParameter("disInfoName"));
		map.put("flag", getParameter("flag"));
		map.put("queryCreateDateFrom", getParameter("queryCreateDateFrom"));
		map.put("queryCreateDateTo", getParameter("queryCreateDateTo"));
		map.put("querySendDateFrom", getParameter("querySendDateFrom"));
		map.put("querySendDateTo", getParameter("querySendDateTo"));
		map.put("sendType", getParameter("sendType"));
		map.put("oid", getParameter("oid"));
	}

	/**
	 * Excel输出调用。
	 */
	public void paperTaskExcel() {
		String taskOid = request.getParameter(KeyS.OIDS);
		List<String> oids = SplitString.string2List(taskOid, ",");
		LOG.debug("执行  paperTaskExcel() : 页面请求参数 OID = " + taskOid);
		map = (Map<String, String>) session.getAttribute("DDM_DISTRIBUTE_INSIDE");
		Map tempMap = new HashMap();
		tempMap.put("oids", taskOid);
		tempMap.putAll(map);
		try {
			// 取得对象。
			HSSFWorkbook workBook = service.getExcelObject(tempMap);
			for(String tOid: oids){
				Persistable obj = Helper.getPersistService().getObject(tOid);
				DistributePaperTask dis = (DistributePaperTask) obj;
				//打印发送单记录日志
				Context context= dis.getContextInfo().getContext();
				List<String> objects = new ArrayList<String>();
				objects.add(SessionHelper.getService().getUser().getName());//用户名
				objects.add("打印发送单");
				objects.add(dis.getNumber());
				int level=1;
				String logType="module";
				String objName=dis.getName();
				int objectSecurity=0;
				String moduleSource="发放管理";
				String objectType="纸质任务";
				String operation="打印发送单";
				String messageId="ddm.log.sendexportExcel";
				AuditLogHelper.getService().addLog( level, logType, context, 
						dis.getInnerId(), dis.getClassId(), objName, objectSecurity, 
						moduleSource, objectType, operation, messageId, objects);
			}
			// Excel文件输出。
			exportExcel(workBook, ConstUtil.SEND_SHEET_NAME);
		} catch (Exception ex) {
			LOG.debug("Excel导出异常", ex);
			throw new RuntimeException(ex);
		}
	}
}
