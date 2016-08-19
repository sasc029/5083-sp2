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
 * ������ͳ��Actionʵ���ࡣ
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
	 * ȡ�÷ַ����ݶ���
	 * 
	 * @return JSON����
	 */
	public String initDistributeWorkload() {
		try {
			// ������
			GridDataUtil.prepareRowObjectMaps(listMap, ConstUtil.SOPT_LISTDISTRIBUTEWORKLOAD);
			request.setAttribute("init", "true");
		} catch (Exception ex) {
			error(ex);
		}
		return INITPAGE;
	}

	/**
	 * ȡ�÷ַ����ݶ���
	 * 
	 * @return JSON����
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

			// ������
			if (null != creatorId && !"".equals(creatorId)) {
				// ��ȡ�û���UserId
				User user = UserHelper.getService().getUser(creatorId);
				if (user != null) {
					addMultiColumnCondition(manager, "CREATEBYID", user.getInnerId());
				}
			}

			// ״̬
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

			// ����ʱ��
			if (qcreatedate != null) {
				if ("past".equals(qcreatedate)) {
					// ��������ж�
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
			//ͳ�ƹ�������¼��־
			String distributeStateName="";
			switch (iState) {
			case 0:
				distributeStateName = "����";
				break;
			case 1:
				distributeStateName = "�ӹ�";
				break;
			case 2:
				distributeStateName = "���Ƽӹ�";
				break;
			case 3:
				distributeStateName = "����";
				break;
			default:
				distributeStateName = "����";
			}
			List<String> objects = new ArrayList<String>();
			objects.add(SessionHelper.getService().getUser().getName());//�û���
			objects.add("ͳ�ƹ�����");
			objects.add(distributeStateName);
			int level=1;
			String logType="module";
			int objectSecurity=0;
			String moduleSource="���Ź���";
			String objectType="���Ź�����";
			String operation="ͳ�ƹ�����";
			String messageId="ddm.log.searchWorkLoads";
			AuditLogHelper.getService().addLog( level, logType, null, 
					null, null, null, objectSecurity, 
					moduleSource, objectType, operation, messageId, objects);
			
			LOG.debug("ȡ�ù�����ͳ�� " + getDataSize(listDis) + " ��");
			
			String spot = ConstUtil.SOPT_LISTDISTRIBUTEWORKLOAD;
			
			// ������֤Ȩ��
			List<Map<String, Object>> listDatas = checkPermissionAF(listDis, spot, spot);
			
			// ������
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

	// ��Ӷ��ж�ֵ��������
	private void addMultiColumnCondition(ConditionManager manager, String columns, String value) {
		if (value == null || value.length() == 0) {
			return;
		}
		manager.addCondition(new MultiColumnCondition(columns, value));
	}

	// ��ȥ������Ĳ�ѯ������ӽ���
	private void addDateCondition(ConditionManager manager, String column, String pastdays) {
		int days = Integer.parseInt(pastdays);
		if (days != 0) {
			manager.addCondition(new DateCondition(column, days));
		}
	}

	// ��Ӹ������ڵĲ�ѯ
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
