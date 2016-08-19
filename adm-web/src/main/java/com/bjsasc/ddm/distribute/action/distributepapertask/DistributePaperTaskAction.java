package com.bjsasc.ddm.distribute.action.distributepapertask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.bjsasc.ddm.common.ConstUtil;
import com.bjsasc.ddm.distribute.action.AbstractAction;
import com.bjsasc.ddm.distribute.helper.DistributeHelper;
import com.bjsasc.ddm.distribute.model.distributepapertask.DistributePaperTask;
import com.bjsasc.ddm.distribute.service.distributeorderobjectlink.DistributeOrderObjectLinkService;
import com.bjsasc.ddm.distribute.service.distributepapertask.DistributePaperTaskService;
import com.bjsasc.plm.Helper;
import com.bjsasc.plm.KeyS;
import com.bjsasc.plm.core.context.model.Context;
import com.bjsasc.plm.core.persist.model.Persistable;
import com.bjsasc.plm.core.security.audit.AuditLogHelper;
import com.bjsasc.plm.core.session.SessionHelper;
import com.bjsasc.plm.core.util.StringUtil;
import com.bjsasc.plm.grid.GridHelper;
import com.bjsasc.plm.grid.data.GridDataUtil;
import com.bjsasc.plm.type.TypeService;

/**
 * 纸质任务Action实现类
 * 
 * @author guowei 2013-2-22
 * 
 */
public class DistributePaperTaskAction extends AbstractAction {

	/** serialVersionUID */
	private static final long serialVersionUID = 1485443385203981845L;
	private static final Logger LOG = Logger.getLogger(DistributePaperTaskAction.class);
	private final DistributePaperTaskService service = DistributeHelper.getDistributePaperTaskService();

	/**
	 * 发放管理--加工单菜单
	 * 代办任务页面数据一栏显示。
	 * 取得纸质任务生命周期为【加工中】的数据。
	 * /ddm/distributepapertask/listDistributePaperTasks.jsp
	 * 
	 * @return struts页面参数
	 */	
	public String getAllDistributePaperTask() {
		try {
			String lc = ConstUtil.LC_PROCESSING.getName();
			List<DistributePaperTask> listDis = service.getAllDistributePaperTaskByAuth(lc);
			LOG.debug("取得纸质任务 " + getDataSize(listDis) + " 条，生命周期状态：" + lc);
			String spot = ConstUtil.SPOT_LISTDISTRIBUTEPAPERTASKS;
			// 批量验证权限
			List<Map<String, Object>> listDatas = checkPermissionAF(listDis, spot, spot);
			// 输出结果
			GridDataUtil.prepareRowObjects(listDatas, spot);
		} catch (Exception ex) {
			error(ex);
		}
		return "listPage";
	}
	/**
	 * 加工单重新打印页面点击“搜索”按钮以及数据初始化时调用
	 * @return
	 */
	public String getSearchPrintProcessingDistributePaperTask() {
		String ret="";
		String stateName = ConstUtil.LC_PROCESSING.getName();
		try {
			//取得加工中
			String lc = ConstUtil.LC_PROCESSING.getName();
			//搜索框中的搜索字段
			String keywords=("%"+getParameter("keywords")+"%").trim();
			String flag1=getParameter("flag1");
			//点击搜索
			if(""!=flag1&&flag1!=null&&"2".equals(flag1)){
				List<DistributePaperTask> listDis = service.getSearchProcessingDistributePaperTaskByAuth(stateName,keywords);
				LOG.debug("取得纸质任务 " + getDataSize(listDis) + " 条，生命周期状态：" + lc);
				//String spot = ConstUtil.SPOT_LISTDISTRIBUTEPAPERTASKS;
				String spot=ConstUtil.SPOT_LISTDISTRIBUTEPAPERTASKSPRINT;
				// 批量验证权限
				List<Map<String, Object>> listDatas = checkPermissionAF(listDis, spot, spot);
				// 输出结果
				GridDataUtil.prepareRowObjects(listDatas, spot);
				result.put("success","true");
				result.put("flag", listDatas.size()+"");
				mapToSimpleJson();
				ret=OUTPUTDATA;
			}else{
				//页面初始加载
				long currentTime = System.currentTimeMillis();
				long monthtime = 30*24*60*60*1000L;
				long time = currentTime-monthtime;
				List<DistributePaperTask> listDis = service.getSearchProcessingDistributePaperTaskByTime(time,stateName,currentTime);
				LOG.debug("取得纸质任务 " + getDataSize(listDis) + " 条，生命周期状态：" + lc);
				//String spot = ConstUtil.SPOT_LISTDISTRIBUTEPAPERTASKS;
				String spot=ConstUtil.SPOT_LISTDISTRIBUTEPAPERTASKSPRINT;
				// 批量验证权限
				List<Map<String, Object>> listDatas = checkPermissionAF(listDis, spot, spot);
				// 输出结果
				GridDataUtil.prepareRowObjects(listDatas, spot);
				ret="getPrintProcessingDistributePaperTask";
			}
			
		} catch (Exception ex) {
			jsonError(ex);
		}
		return ret;
		
	}
	
	
	protected String getParameter(String key) {
		String parameter = request.getParameter(key);
		String encoding = StringUtil.encoding(parameter);
		return encoding;
	}


	/**
	 * 发放管理--加工单菜单
	 * 复制完成任务页面数据一栏显示。
	 * 取得纸质任务生命周期为【复制完成】的数据。
	 * /ddm/distributepapertask/listDuplicateSubmit.jsp
	 * 
	 * @return struts页面参数
	 */	
	public String getAllDuplicateSubmitTask() {
		try {
			String lc = ConstUtil.LC_DUPLICATED.getName();
			List<DistributePaperTask> listDis = service.getAllDistributePaperTaskByAuth(lc);
			LOG.debug("取得纸质任务 " + getDataSize(listDis) + " 条,生命周期状态：" + lc);

			String spot = ConstUtil.SPOT_LISTDISTRIBUTEDUPLICATESUBMIT;
			
			// 批量验证权限
			List<Map<String, Object>> listDatas = checkPermissionAF(listDis, spot, spot);
			
			// 输出结果
			GridDataUtil.prepareRowObjects(listDatas, spot);
			
		} catch (Exception ex) {
			error(ex);
		}
		return "listDuplicateSubmit";
	}

	/**
	 * 发放管理--加工单菜单
	 * 被退回任务页面数据一栏显示。
	 * 取得纸质任务生命周期为【加工被退回】的数据。
	 * /ddm/distributepapertask/listDuplicateReturn.jsp
	 * 
	 * @return struts页面参数
	 */	
	public String getAllDistributeReturnTask() {
		try {
			String lc = ConstUtil.LC_PROCESSING_BACKOFF.getName();
			List<DistributePaperTask> listDis = service.getAllDistributePaperTaskReturnByAuth(lc);
			LOG.debug("取得纸质任务 " + getDataSize(listDis) + " 条,生命周期状态：" + lc);

			String spot = ConstUtil.SOPT_LISTDISTRIBUTEPAPERTASKRETURN;
			
			// 批量验证权限
			List<Object> checkList = checkPermission(listDis, spot, spot);
			
			List<String> keys = GridHelper.getService().getMyLatestGridViewColumnIds(spot, spot);
			
			// 格式化显示数据
			TypeService typeManager = Helper.getTypeManager();
			Map<String, Object> mapAll = null;
			for (Object obj : checkList) {
				if (obj == null) {
					continue;
				}
				mapAll = new HashMap<String, Object>();
				DistributePaperTask target = (DistributePaperTask)obj;
				Map<String, Object> mainMap = typeManager.format(target, keys, false);
				Map<String, Object> returnMap = typeManager.format(target.getReturnReason(), keys, false);
				mapAll.putAll(returnMap);
				mapAll.putAll(mainMap);
				mapAll.put(KeyS.ACCESS, true);
				listMap.add(mapAll);
			}

			GridDataUtil.prepareRowObjectMaps(listMap, spot);
			
		} catch (Exception ex) {
			error(ex);
		}
		return "listDuplicateReturn";
	}

	/**
	 * 创建纸质任务对象
	 */
	public String createDistributePaperTask() {
		try {
			String number = request.getParameter("number");// 编号
			String name = request.getParameter("name");// 名称
			String note = request.getParameter("note");// 备注

			service.createDistributePaperTask(number, name, note);

			success();
		} catch (Exception ex) {
			error(ex);
		}
		mapToSimpleJson();
		return OUTPUTDATA;
	}


	/**
	 * 回退详细列表
	 * 
	 * @return
	 */
	public String getDistributePaperTaskReturnDetail() {
		try {
			String oid = request.getParameter(KeyS.OID);
			String flag = request.getParameter("flag");
			String stateName = "";
			if ("duplicateReturn".equals(flag)) {
				stateName = ConstUtil.LC_PROCESSING_BACKOFF.getName();
			} else if ("paperTaskReturn".equals(flag)) {
				stateName = ConstUtil.LC_DUPLICATE_BACKOFF.getName();
			}
			List<DistributePaperTask> listDis = service.getDistributeTaskReturnDetail(oid, stateName);

			// 格式化显示数据
			TypeService typeManager = Helper.getTypeManager();
			for (DistributePaperTask target : listDis) {
				if (target == null) {
					continue;
				}
				Map<String, Object> mapAll = new HashMap<String, Object>();
				Map<String, Object> mainMap = typeManager.format(target);
				Map<String, Object> returnMap = typeManager.format(target.getReturnReason());
				mapAll.putAll(returnMap);
				mapAll.putAll(mainMap);
				listMap.add(mapAll);
			}
			LOG.debug("取得发放单被回退 " + getDataSize() + " 条，生命周期状态：" + ConstUtil.LC_BACKOFF.getName());
			// 输出结果
			GridDataUtil.prepareRowObjectMaps(listMap, ConstUtil.SOPT_LISTDISTRIBUTEPAPERTASKRETURNDETAIL);
			
		} catch (Exception ex) {
			error(ex);
		}
		return "listDisPaperTaskReturnDetail";
	}

	/**
	 * 删除纸质任务
	 * 
	 * @return
	 */
	public String deleteDistributePaperTask() {
		try {
			// 获取纸质任务的OIDS
			String oids = request.getParameter(KeyS.OIDS);
			service.deleteDistributePaperTaskProperty(oids);

			getAllDistributePaperTask();

			success();
		} catch (Exception ex) {
			error(ex);
		}
		mapToSimpleJson();
		return OUTPUTDATA;

	}
	
	public String listDistributeObjectFiles() {
		try {
			String oid = request.getParameter(KeyS.OID);
			String contextPath = request.getContextPath();
			listMap = service.listDistributeObjectFiles(oid,contextPath);
			LOG.debug("取得附件数据" + getDataSize() + " 条");
		} catch (Exception ex) {
			error(ex);
		}
		listToJson();
		return OUTPUTDATA;
	}

	/**
	 * Excel输出调用。
	 */
	public void paperTaskExcel() {
		String oid = request.getParameter(KeyS.OID);
		LOG.debug("执行paperTaskExcel() 页面请求参数 : OID = " + oid);
		Persistable obj = Helper.getPersistService().getObject(oid);
		DistributePaperTask dis = (DistributePaperTask) obj;

		String filename = dis.getNumber();
		try {
			// 取得对象。
			HSSFWorkbook wBook = service.getExcelObject(oid);

			// Excel文件输出。
			exportExcel(wBook, ConstUtil.PROCESS_TITLE_NAME);
			
			//打印加工单记录日志
			
			Context context= dis.getContextInfo().getContext();
			List<String> objects = new ArrayList<String>();
			objects.add(SessionHelper.getService().getUser().getName());//用户名
			objects.add("打印加工单");
			objects.add(dis.getNumber());
			int level=1;
			String logType="module";
			String objName=dis.getName();
			int objectSecurity=0;
			String moduleSource="发放管理";
			String objectType="纸质任务";
			String operation="打印加工单";
			String messageId="ddm.log.exportExcel";
			AuditLogHelper.getService().addLog( level, logType, context, 
					dis.getInnerId(), dis.getClassId(), objName, objectSecurity, 
					moduleSource, objectType, operation, messageId, objects);
			
			
		} catch (Exception ex) {
			LOG.debug("Excel导出异常", ex);
			throw new RuntimeException(ex);
		}
	}
	
	/**
	 * "七部发放单模板"Excel输出调用
	 */
	public void paperTaskExcelForDept7(){

		String oid = request.getParameter(KeyS.OID);
		LOG.debug("执行paperTaskExcel() 页面请求参数 : OID = " + oid);
		try {
			// 取得对象。
			HSSFWorkbook wBook = service.getExcelObjectForDept7(oid);
			// Excel文件输出。
			exportExcel(wBook, ConstUtil.DOCUMENT_COPY);	
	    } catch (Exception ex) {
			LOG.debug("Excel导出异常", ex);
			throw new RuntimeException(ex);
		}	
	}

}
