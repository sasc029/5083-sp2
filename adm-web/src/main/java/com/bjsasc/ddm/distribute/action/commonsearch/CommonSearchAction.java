package com.bjsasc.ddm.distribute.action.commonsearch;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.bjsasc.ddm.common.ConstUtil;
import com.bjsasc.ddm.common.DateCondition;
import com.bjsasc.ddm.common.InCondition;
import com.bjsasc.ddm.common.ViewProviderManager;
import com.bjsasc.ddm.distribute.action.AbstractAction;
import com.bjsasc.ddm.distribute.helper.DistributeHelper;
import com.bjsasc.ddm.distribute.model.distributeobject.DistributeObject;
import com.bjsasc.ddm.distribute.model.distributeorderobjectlink.DistributeOrderObjectLink;
import com.bjsasc.ddm.distribute.service.commonsearch.CommonSearchService;
import com.bjsasc.ddm.distribute.service.distributeorderobjectlink.DistributeOrderObjectLinkService;
import com.bjsasc.plm.Helper;
import com.bjsasc.plm.KeyS;
import com.bjsasc.plm.common.search.ConditionManager;
import com.bjsasc.plm.common.search.MultiColumnCondition;
import com.bjsasc.plm.common.search.SearchViewProvider;
import com.bjsasc.plm.core.persist.PersistHelper;
import com.bjsasc.plm.core.persist.model.Persistable;
import com.bjsasc.plm.core.system.principal.User;
import com.bjsasc.plm.core.system.principal.UserHelper;
import com.bjsasc.plm.core.util.DateTimeUtil;
import com.bjsasc.plm.core.util.StringUtil;
import com.bjsasc.plm.core.util.JsonUtil;
import com.bjsasc.plm.grid.GridHelper;
import com.bjsasc.plm.grid.data.GridDataSortUtil;
import com.bjsasc.plm.grid.data.GridDataUtil;
import com.bjsasc.plm.grid.spot.Spot;
import com.bjsasc.plm.grid.spot.SpotUtil;
import com.bjsasc.plm.type.TypeService;
import com.cascc.avidm.util.SplitString;

/**
 * �ۺϲ�ѯActionʵ���ࡣ
 * 
 * @author gengancong 2013/02/20
 */
public class CommonSearchAction extends AbstractAction {

	/** serialVersionUID */
	private static final long serialVersionUID = 1345348097220976989L;

	/** CommonSearchService */
	private final CommonSearchService service = DistributeHelper.getCommonSearchService();

	/** �ۺϲ�ѯ���� */
	Map<String, String> map = new HashMap<String, String>();
	private static final Logger LOG = Logger.getLogger(CommonSearchAction.class);

	/**
	 * ��ʼ��ҳ��
	 * @return
	 */
	public String initDistributeCommonSearch() {
		try {
			// ������
			GridDataUtil.prepareRowObjectMaps(listMap, ConstUtil.SPOT_LISTDISTRIBUTECOMMONSEARCH);
			request.setAttribute("init", "true");
		} catch (Exception ex) {
			error(ex);
		}
		return INITPAGE;
	}

	/**
	 * ȡ�÷��Ŷ������ݡ�
	 * 
	 * @return JSON����
	 */
	public String getAllDistributeObjects() {
		try {
			// ���ŵ���ַ����ݶ���Link����
			DistributeOrderObjectLinkService linkService = DistributeHelper.getDistributeOrderObjectLinkService();
			// ��ʼ����ѯ����
			initParam();
			List<Persistable> listDis = getResult();

			String spot = ConstUtil.SPOT_LISTDISTRIBUTECOMMONSEARCH;

			// ������֤Ȩ��
			List<Object> checkList = checkPermission(listDis, spot, spot);

			// ������
			GridDataUtil.prepareRowObjects(checkList, spot);
			result.put("success","true");
			result.put("flag", checkList.size()+"");
			
		} catch (Exception ex) {
			jsonError(ex);
		}
		mapToSimpleJson();
		return OUTPUTDATA;
	}

	/**
	 * Excel������á�
	 */
	public void dataExport() {
		String filename = "�ۺϲ�ѯ";
		String spot = ConstUtil.SPOT_LISTDISTRIBUTECOMMONSEARCH;
		String spotInstance = ConstUtil.SPOT_LISTDISTRIBUTECOMMONSEARCH;
		//ȡ��ҳ��ѡ�еĶ����oid����
		String oids = request.getParameter(KeyS.OIDS);
		List<String> oidList = SplitString.string2List(oids, ",");
		// ���ŵ���ַ����ݶ���Link����
		DistributeOrderObjectLinkService linkService = DistributeHelper.getDistributeOrderObjectLinkService();

		List<Object> objectList = new ArrayList<Object>();
		List<Map<String, Object>> objectMapList = new ArrayList<Map<String, Object>>();
		//�������ѡ���˶����򵼳�ѡ�ж������û��ѡ���򵼳���ѯ���
		if(StringUtil.isStringEmpty(oids)){
			objectList = (List<Object>)GridDataUtil.getRowObjects(spot);
		}else{
			for(String oid:oidList){
				Persistable p = Helper.getPersistService().getObject(oid);
				objectList.add(p);
			}
		}
		
		List<String> keys = GridHelper.getService().getMyLatestGridViewColumnIds(spot, spot);
		
		TypeService typeManager = Helper.getTypeManager();
		for (Object obj : objectList) {
			Map<String, Object> allMap = new HashMap<String, Object>();
			Persistable target = (Persistable)obj;
			//�ַ��������⴦��
			if (target instanceof DistributeObject) {
				String distributeObjectOid = target.getClassId() + ":" + target.getInnerId();
				List<DistributeOrderObjectLink> links = linkService
						.getDistributeOrderObjectLinkListByDistributeObjectOid(distributeObjectOid);
				if (links == null || links.isEmpty()) {
					continue;
				}
				DistributeOrderObjectLink distributeOrderObjectLink = links.get(0);
				distributeOrderObjectLink.setContextInfo(((DistributeObject) target).getContextInfo());
				Map<String, Object> linkMap = typeManager.format(distributeOrderObjectLink, keys, false);
				allMap.putAll(linkMap);
			}
			Map<String, Object> map = typeManager.format(target, keys, false);
			allMap.putAll(map);
			allMap.put(KeyS.ACCESS, true);
			objectMapList.add(allMap);
		}

		// ���򣨱�������ͻ���һ�£�
		Spot spt = SpotUtil.getSpot(spot);
		if (spt.hasSort()) {
			objectMapList = GridDataSortUtil.sort(request, spot, spotInstance, objectMapList);
		}
		
		try {
			// ȡ�ö���
			HSSFWorkbook wBook = service.getExcelObject(objectMapList);

			// Excel�ļ������
			exportExcel(wBook, filename);
		} catch (Exception ex) {
			LOG.debug("Excel�����쳣", ex);
			throw new RuntimeException(ex);
		}

	}

	private void initParam() {
		map.put("sourceDomainIID", getParameter("sourceDomainIID"));
		map.put("receiveDomainIID", getParameter("receiveDomainIID"));
		map.put("distributeType", getParameter("rdoDistributeType"));
		map.put("distributeTypes", getParameter("hidDistributeTypes"));
		map.put("distributeState", getParameter("rdoDistributeState"));
		map.put("distributeStates", getParameter("hidDistributeStates"));
		map.put("keywords", request.getParameter("keywords"));
		map.put("blur", getParameter("blur"));
		map.put("queryName", request.getParameter("queryName"));
		map.put("queryNumber", request.getParameter("queryNumber"));
		map.put("queryCode", request.getParameter("queryCode"));
		map.put("modifyDate", getParameter("qModifyDate"));
		map.put("modifyPastDays", getParameter("modifyPastDays"));
		map.put("queryModifyDateFrom", request.getParameter("queryModifyDateFrom"));
		map.put("queryModifyDateTo", request.getParameter("queryModifyDateTo"));
		map.put("createDate", request.getParameter("qCreateDate"));
		map.put("createPastDays", request.getParameter("createPastDays"));
		map.put("queryCreateDateFrom", request.getParameter("queryCreateDateFrom"));
		map.put("queryCreateDateTo", request.getParameter("queryCreateDateTo"));
		map.put("modifier", getParameter("modifier"));
		map.put("creator", getParameter("creator"));
		map.put("modifierName", getParameter("modifierName"));
		map.put("creatorName", getParameter("creatorName"));
	}

	/**
	 * 
	 * ��ϳ�Sql��䣬����json����
	 * 
	 * @author ������ 2011-12-14 ����11:02:25
	 * @return
	 * 
	 */
	public List<Persistable> getResult() {
		List<Persistable> outputs = new ArrayList<Persistable>();
		String types = map.get("distributeTypes");
		// ���û��ѡ�����ͣ��򷵻ؿ�
		if (StringUtil.isNull(types)) {
			types = "DistributeOrder,DistributePaperTask,DistributeObject,DistributeElecTask";
		}
		Sqls sqls = getSelectSql(types, "t");
		if (sqls != null) {
			// ִ�в�ѯ
			List<Map<String, Object>> results = PersistHelper.getService().query(sqls.selectsql);

			// ��һЩ�н��д���
			if (results != null) {
				for (Map<String, Object> map : results) {
					String classId = (String) map.get(KeyS.CLASSID);
					String innerId = (String) map.get(KeyS.INNERID);
					String oid = Helper.getOid(classId, innerId);
					Persistable obj = PersistHelper.getService().getObject(oid);
					outputs.add(obj);
				}
				return outputs;
			}
		}
		// ִ�в�ѯ����װΪ��Ӧ������
		return outputs;
	}

	/**
	 * ���ݲ�����������װhql���<br>
	 * ���ذ�����ѯͳ������HQL
	 * 
	 * @author ������ 2011-12-14 ����11:01:04
	 * @param type
	 * @param prefix
	 *            ��ϳɵĶ�̬��ͼ����
	 * @return
	 */
	private Sqls getSelectSql(String types, String prefix) {

		ConditionManager manager = new ConditionManager();

		manager.setPrefix(prefix);

		// ���keyWord�ؼ���
		String keyword = map.get("keywords");

		if (keyword != null) {
			// ����ж�Ӧͬһ��ֵ
			// ���Ϊblur��ֵ����ǰ�����ͨ���'%'
			// �������'*'���ʾ��������
			if ("*".equals(keyword.trim())) {
				keyword = "";
			}
			String blur = map.get("blur");
			// �滻_ ���滻��������
			if (keyword != null) {
				keyword = keyword.replace("_", "/_");
			}

			String[] keys = keyword.split(",");
			for (int i = 0; i < keys.length; i++) {
				keys[i] = "%" + keys[i] + "%";
			}
			addMultiColumnCondition(manager, KeyS.NAME + "," + KeyS.NUMBER, keys, blur);
		}

		String sourceDomainIID =map.get("sourceDomainIID");
		String receiveDomainIID =map.get("receiveDomainIID"); 
		//���
		String queryNumber = map.get("queryNumber");
		if (!StringUtil.isNull(queryNumber)) {
			queryNumber = "%" + queryNumber + "%";
			addMultiColumnCondition(manager, KeyS.NUMBER, queryNumber);
		}

		//����
		String queryName = map.get("queryName");
		if (!StringUtil.isNull(queryName)) {
			queryName = "%" + queryName + "%";
			addMultiColumnCondition(manager, KeyS.NAME, queryName);
		}

		//������Χ�����Ĵ���
    	Map<String,Object> mapTaskCode=JsonUtil.toMap(map.get("queryCode"));
    	//������Χ����������������
    	String strContainerScope=mapTaskCode.get("containerScope").toString();
    	String strFavoriteScope=mapTaskCode.get("favoriteScope").toString();
    	//���������� ����Ҫ׷���κ�����
    	//���ղص�������
		StringBuffer sbSql = new StringBuffer();
    	if(!strFavoriteScope.equals("") && strContainerScope.equals("")){
    		if (!StringUtil.isNull(strFavoriteScope)) {
				List<String> valList = SplitString.string2List(strFavoriteScope, ",");
	    		boolean isFirst=true;
	    		for (String val : valList) {
					String innerId = Helper.getInnerId(val);
					String classId = Helper.getClassId(val);
					if(isFirst){
						sbSql.append(" and (( t.contextId = '" + innerId + "' and t.contextClassId = '" + classId + "') ");
						isFirst=false;
					}
					else{
						sbSql.append(" or ( t.contextId = '" + innerId + "' and t.contextClassId = '" + classId + "') ");
					}
				}
	    		
	    		if (valList.size() > 0 && valList != null) {
	    			sbSql.append(")");
	    		}
    		}
    	}

		// ����ʱ��
		String qcreatedate = map.get("createDate");
		if (qcreatedate != null&& !"".equals(qcreatedate)) {
			//if ("past".equals(qcreatedate)) {
				// ��������ж�
				//addDateCondition(manager, KeyS.CREATE_TIME, map.get("createPastDays"));
			//} else {
				String fromdate = map.get("queryCreateDateFrom");
				String toDate = "";
				if (!"".equals(map.get("queryCreateDateTo"))) {
					Date dateTo = DateTimeUtil.parseDate(map.get("queryCreateDateTo"), DateTimeUtil.DATE_FORMAT_ZH);
					toDate = getDate(dateTo, 1);
				}
				addDateCondition(manager, KeyS.CREATE_TIME, fromdate, toDate);
			//}
		}

		// ״̬
		String rdoDistributeState = map.get("distributeState");
		String cbDistributeStates = map.get("distributeStates");
		if ("CUS".equals(rdoDistributeState) && !StringUtil.isNull(cbDistributeStates)) {
			List<String> states = new ArrayList<String>();
			List<String> valList = SplitString.string2List(cbDistributeStates, ",");
			for (String val : valList) {
				states.add(ConstUtil.lifeCycleMap.get(val));
			}
			manager.addCondition(new InCondition("WORKSTATE", states));
		}

		// ������
		String creatorId = map.get("creator");
		if (null != creatorId && !"".equals(creatorId)) {
			// ��ȡ�û���UserId
			User user = UserHelper.getService().getUser(creatorId);
			if (user != null) {
				addMultiColumnCondition(manager, "CREATOR", user.getInnerId());
			}
		}
		// ��ȡ��ѯ��SQL
		String conditionStr = manager.getConditionSql() + sbSql.toString();
		String orderBy = " ORDER BY MODIFY_TIME DESC ";
		Sqls sqls = null;
		if (conditionStr != null) {
			String viewSql=getViewSql(types);
			StringBuffer s1=new StringBuffer(viewSql); 
			if (!StringUtil.isNull(sourceDomainIID)) {
				sourceDomainIID=map.get("sourceDomainIID");
			}else{
				sourceDomainIID="";
			}
			if (!StringUtil.isNull(receiveDomainIID)) {
				receiveDomainIID=map.get("receiveDomainIID");
			}else{
				receiveDomainIID="";
			}
			String condition1=" a.SOURCESITEID ="+sourceDomainIID;
			String condition2=" and a.TARGETSITEID ="+receiveDomainIID;
			String condition3=" a.TARGETSITEID ="+receiveDomainIID;
			String whereSql="";
			Pattern p=Pattern.compile("from DDM_DIS_ELECTASK a");
			Matcher m=p.matcher(s1.toString());
			if(!StringUtil.isNull(sourceDomainIID)||!StringUtil.isNull(receiveDomainIID)){
				if(m.find()){
					if(!StringUtil.isNull(sourceDomainIID)&&!StringUtil.isNull(receiveDomainIID)){
						whereSql=" where "+condition1+condition2;
					}else if(!StringUtil.isNull(sourceDomainIID)&&StringUtil.isNull(receiveDomainIID)){
						whereSql=" where "+condition1;
					}else if(StringUtil.isNull(sourceDomainIID)&&!StringUtil.isNull(receiveDomainIID)){
						whereSql=" where "+condition3;
					}
					s1.insert(m.end(), whereSql);
				}
			}
			String shql = "select *  from  (" + s1+ ") " + prefix + " where " + conditionStr + orderBy;
			String chql = "select count(1) as countNum  from  (" + getViewSql(types) + ")  " + prefix + " where "
					+ conditionStr;
			sqls = new Sqls(shql, chql);
		}

		return sqls;
	}

	// ��Ӷ��ж�ֵ��������
	private void addMultiColumnCondition(ConditionManager manager, String columns, String value) {
		if (value == null || value.length() == 0){
			return;
		}
		manager.addCondition(new MultiColumnCondition(columns, value));
	}

	// ��Ӷ��ж�������� ��������
	private void addMultiColumnCondition(ConditionManager manager, String columns, String[] value, String blur) {
		if (value == null || value.length == 0){
			return;
		}
		MultiColumnCondition c = new MultiColumnCondition(columns, value);
		if (blur != null && blur.equals("0")) {
			c.setAndJoin();
		}
		manager.addCondition(c);
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

	private String getViewSql(String types) {
		String[] ts = types.split(",");
		List<SearchViewProvider> views = new ArrayList<SearchViewProvider>();
		for (String t : ts) {
			// ���ݾ������ͻ�ȡ��Ӧ����ͼ����
			SearchViewProvider view = (SearchViewProvider) ViewProviderManager.getInstance().getViewProviderByName(t);
			if (view != null){
				views.add(view);
			}
		}
		if (views.size() == 0){
			return null;
		}
		boolean isFirst = true;
		StringBuffer sb = new StringBuffer();
		sb.append("(");
		for (SearchViewProvider v : views) {
			if (isFirst) {
				isFirst = false;
			} else {
				sb.append(") union all (");
			}
			sb.append(v.getViewSql());
		}
		sb.append(")");
		return sb.toString();
	}

	private String getDate(Date date, int day) {
		String timeFormat = "yyyy-MM-dd";
		SimpleDateFormat sf = new SimpleDateFormat(timeFormat);

		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_MONTH, day);

		return sf.format(cal.getTime());
	}
}

class Sqls {
	public String selectsql = null;
	public String countsql = null;

	public Sqls(String ssql, String csql) {
		selectsql = ssql;
		countsql = csql;
	}
}
