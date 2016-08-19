package com.bjsasc.ddm.distribute.action.distributeworkload;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.bjsasc.ddm.common.ConstUtil;
import com.bjsasc.ddm.common.DateCondition;
import com.bjsasc.ddm.common.InCondition;
import com.bjsasc.ddm.distribute.action.AbstractAction;
import com.bjsasc.ddm.distribute.helper.DistributeHelper;
import com.bjsasc.ddm.distribute.model.distributeworkload.DistributeWorkload;
import com.bjsasc.ddm.distribute.service.distributeworkload.DistributeWorkloadService;
import com.bjsasc.plm.common.search.ConditionManager;
import com.bjsasc.plm.common.search.MultiColumnCondition;
import com.bjsasc.plm.core.security.audit.AuditLogHelper;
import com.bjsasc.plm.core.session.SessionHelper;
import com.bjsasc.plm.core.system.principal.User;
import com.bjsasc.plm.core.system.principal.UserHelper;
import com.bjsasc.plm.core.util.DateTimeUtil;
import com.bjsasc.plm.grid.data.GridDataUtil;
import com.cascc.avidm.util.SplitString;

/**
 * 工作量统计Action实现类。
 * 
 * @author gengancong 2013-3-21
 */
public class DistributeWorkloadAction extends AbstractAction {

	/** serialVersionUID */
	private static final long serialVersionUID = -4544074835473008548L;

	private static final Logger LOG = Logger.getLogger(DistributeWorkloadAction.class);
	/** DistributeWorkloadService */
	DistributeWorkloadService service = DistributeHelper.getDistributeWorkloadService();

	/**
	 * 取得分发数据对象。
	 * 
	 * @return JSON对象
	 */
	public String initDistributeWorkload() {
		try {
			// 输出结果
			GridDataUtil.prepareRowObjectMaps(listMap, ConstUtil.SOPT_LISTDISTRIBUTEWORKLOAD);
			request.setAttribute("init", "true");
		} catch (Exception ex) {
			error(ex);
		}
		return INITPAGE;
	}

	/**
	 * 取得分发数据对象。
	 * 
	 * @return JSON对象
	 */
	public String getDistributeWorkload() {
		try {

			String creatorId = request.getParameter("creator");
			String qcreatedate = request.getParameter("qCreateDate");
			String createPastDays = request.getParameter("createPastDays");
			String queryCreateDateFrom = request.getParameter("queryCreateDateFrom");
			String queryCreateDateTo = request.getParameter("queryCreateDateTo");
			String lifeCycleType = request.getParameter("lifeCycleType");

			ConditionManager manager = new ConditionManager();

			manager.setPrefix("t");

			// 创建人
			if (null != creatorId && !"".equals(creatorId)) {
				// 获取用户的UserId
				User user = UserHelper.getService().getUser(creatorId);
				if (user != null) {
					addMultiColumnCondition(manager, "CREATEBYID", user.getInnerId());
				}
			}

			// 状态
			int iState = Integer.parseInt(lifeCycleType);
			String distributeState;
			String type;
			String endState = "";
			switch (iState) {
			case 0:
				type = "DistributeOrder";
				distributeState = ConstUtil.LC_LC_SCHEDULING_STATE;
				break;
			case 1:
				type = "DistributePaperTask";
				distributeState = ConstUtil.LC_PROCESSING_STATE;
				break;
			case 2:
				type = "DistributePaperTask";
				distributeState = ConstUtil.LC_DUPLICATED_STATE;
				break;
			case 3:
				type = "DistributePaperTask";
				distributeState = ConstUtil.LC_SENDING_STATE;
				endState = ConstUtil.LC_SENT_STATE;
				break;
			default:
				type = "DistributeOrder";
				distributeState = ConstUtil.LC_LC_SCHEDULING_STATE;
			}
			
			

			List<String> states = new ArrayList<String>();
			List<String> endStates = new ArrayList<String>();
			List<String> valList = SplitString.string2List(distributeState, ",");
			for (String val : valList) {
				states.add(ConstUtil.lifeCycleMap.get(val));
			}
			manager.addCondition(new InCondition("FROMSTATENAME", states));
			if (!"".equals(endState)) {
				List<String> endList = SplitString.string2List(endState, ",");
				for (String val : endList) {
					endStates.add(ConstUtil.lifeCycleMap.get(val));
				}
				manager.addCondition(new InCondition("TOSTATENAME", endStates));
			}
			addMultiColumnCondition(manager, "OBJECTCLASSID", type);

			// 创建时间
			if (qcreatedate != null) {
				if ("past".equals(qcreatedate)) {
					// 添加日期判断
					addDateCondition(manager, "CREATETIME", createPastDays);
				} else {
					String toDate = "";
					if (!"".equals(queryCreateDateTo)) {
						Date dateTo = DateTimeUtil.parseDate(queryCreateDateTo, DateTimeUtil.DATE_FORMAT_ZH);
						toDate = getDate(dateTo, 1);
					}
					addDateCondition(manager, "CREATETIME", queryCreateDateFrom, toDate);
				}
			}
			
			String where = manager.getConditionSql("true","true");

			List<DistributeWorkload> listDis = service.getDistributeWorkloads(where);
			//统计工作量记录日志
			String distributeStateName="";
			switch (iState) {
			case 0:
				distributeStateName = "调度";
				break;
			case 1:
				distributeStateName = "加工";
				break;
			case 2:
				distributeStateName = "复制加工";
				break;
			case 3:
				distributeStateName = "发送";
				break;
			default:
				distributeStateName = "调度";
			}
			List<String> objects = new ArrayList<String>();
			objects.add(SessionHelper.getService().getUser().getName());//用户名
			objects.add("统计工作量");
			objects.add(distributeStateName);
			int level=1;
			String logType="module";
			int objectSecurity=0;
			String moduleSource="发放管理";
			String objectType="发放工作量";
			String operation="统计工作量";
			String messageId="ddm.log.searchWorkLoads";
			AuditLogHelper.getService().addLog( level, logType, null, 
					null, null, null, objectSecurity, 
					moduleSource, objectType, operation, messageId, objects);
			
			LOG.debug("取得工作量统计 " + getDataSize(listDis) + " 条");
			
			String spot = ConstUtil.SOPT_LISTDISTRIBUTEWORKLOAD;
			
			// 批量验证权限
			List<Map<String, Object>> listDatas = checkPermissionAF(listDis, spot, spot);
			
			// 输出结果
			GridDataUtil.prepareRowObjects(listDatas, spot);	
			
			result.put("success","true");
			result.put("flag", listDatas.size()+"");
			//success();
		} catch (Exception ex) {
			jsonError(ex);
		}
		mapToSimpleJson();
		return OUTPUTDATA;
	}

	// 添加多列多值搜索条件
	private void addMultiColumnCondition(ConditionManager manager, String columns, String value) {
		if (value == null || value.length() == 0) {
			return;
		}
		manager.addCondition(new MultiColumnCondition(columns, value));
	}

	// 过去多少天的查询条件添加进来
	private void addDateCondition(ConditionManager manager, String column, String pastdays) {
		int days = Integer.parseInt(pastdays);
		if (days != 0) {
			manager.addCondition(new DateCondition(column, days));
		}
	}

	// 添加根据日期的查询
	private void addDateCondition(ConditionManager manager, String column, String fromdate, String todate) {
		if (fromdate != null && todate != null) {
			manager.addCondition(new DateCondition(column, fromdate, todate));
		}
	}

	private String getDate(Date date, int day) {
		String format = "yyyy-MM-dd";
		SimpleDateFormat sf = new SimpleDateFormat(format);

		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_MONTH, day);

		return sf.format(cal.getTime());
	}
}
