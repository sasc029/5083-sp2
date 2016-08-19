 package com.bjsasc.ddm.common;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bjsasc.platform.objectmodel.managed.external.data.ClassInfoDef;
import com.bjsasc.platform.objectmodel.managed.external.service.ModelInfoService;
import com.bjsasc.platform.objectmodel.managed.external.util.ModelInfoUtil;
import com.bjsasc.plm.KeyS;
import com.bjsasc.plm.common.search.ViewProviderManager;
import com.bjsasc.plm.common.search.ConditionManager;
import com.bjsasc.plm.common.search.MultiColumnCondition;
import com.bjsasc.plm.common.search.SearchViewProvider;
import com.bjsasc.plm.common.search.spotcheck.CheckBuilder;
import com.bjsasc.plm.core.persist.model.Persistable;
import com.bjsasc.plm.core.session.SessionHelper;
import com.bjsasc.plm.core.system.principal.User;
import com.bjsasc.plm.core.system.principal.UserHelper;
import com.bjsasc.plm.core.view.View;
import com.bjsasc.plm.foundation.Helper;
import com.bjsasc.plm.query.QueryManager;

public class CommonSearcher {
    public static final int  MAX_SIZE = 3000;
    
    private int totalResult;
    
	public int getTotalResult() {
		return totalResult;
	}
	public void setTotalResult(int totalResult) {
		this.totalResult = totalResult;
	}
	
	private String contextPath = "";

	public static String[] CONDITIONS = { "keyword", "scope", "types", "qname",
			"qnumber", "qmodifydate", "modifypastdays", "qmodifydatefrom",
			"qmodifydateto", "qcreatedate", "createpastdays",
			"qcreatedatefrom", "qcreatedateto", "blur", "limit", "start",
			"latestinbranch", "versionno", "modifier", "creator", "viewid","condition", 
			"addfield","isequal","filedvalue","workingInclude","newInclude","isAllVersion"
	};
	public static final String SPOT_CONDITIONS = "SPOT_CONDITIONS";
	private Map<String, String> params = new HashMap<String, String>();

	/**
	 * �볡����ص���������������ҳ���ϵ�����Ϊspot_conditions
	 */
	private String spotConditions = null;
	public void addQueryCondition(String key, String value) {
		params.put(key, value);
	}
	
	public void addQueryCondition_2(String key, String value) {
		params.put(key, value.replace("/", "//"));
	}
	/**
	 * @return the contextPath
	 */
	public String getContextPath() {
		return contextPath;
	}

	/**
	 * @param contextPath
	 *            the contextPath to set
	 */
	public void setContextPath(String contextPath) {
		this.contextPath = contextPath;
	}
	
	/**
	 * 
	 * ��ϳ�Sql��䣬����json����
	 * 
	 * @author ������ 2011-12-14 ����11:02:25
	 * @return
	 * @throws Exception 
	 * plmwebservice��Ҳ�����˸÷���
	 */
	public List<Persistable> getResult(String OID,boolean reset){
		//���巵�صĶ����б�
		List<Persistable> rsfiltered = new ArrayList<Persistable>();
	    String types = getValue("types");
		//�Ƿ������������
        String workingInclude=getValue("workingInclude");
        String newInclude=getValue("newInclude");
		String condition=getValue("condition");
		String spotCondition="";
		if(condition!=null && condition!="" && !condition.equals("null")){
			spotCondition= URLDecoder.decode(condition);
		}
		// ���û��ѡ�����ͣ��򷵻ؿ�
		if(types == null){
			System.out.println("û�в�ѯ����ʲôҲ����");
			return rsfiltered;
		}
		String sqls = getSelectSql(types, "t",workingInclude,newInclude);
		
		if(sqls == null){
			// doNothing
			System.out.println("ʲôҲ����!");
		}else{
			// ִ�в�ѯ
			//List<Map<String, Object>> results = PersistHelper.getService()
					//.query(sqls.SELECTSQL);
			List<Persistable> results = getCheckResult(workingInclude,newInclude,sqls,reset);
			//��ȡ������ĳ���
		    this.setTotalResult(results.size());
		    
			
			// ��һЩ�н��д���
			if (results.size() > 0) {
				CheckBuilder checker = CheckBuilder.builder(spotCondition); 
				for (Persistable obj : results) {
					 String oid = Helper.getOid(obj);
					//����ĳ������
					if (!oid.equals(OID)&&(checker==null|| checker.check(obj))) {
					    // ��ӳ�����صļ��,���ڳ�����صļ��ȽϿ죬����Ҫ��ѯ���ݿ⣬���Էŵ�ǰ����м��
						rsfiltered.add(obj);
			        }
		        }
	      }
	    }
		 return rsfiltered;
	}
    /**
     * �������������Ľ�������й���
     * @author ���� 2013-12-25 10:59
	 * @param param
	 * @return
     * 1. ��� checkoutStateΪcheckin������
     * 2. ���newInclude �����||checkoutstate='new'
     * 3. �Ƿ������������ 
     * 3.1 �������������� ��� checkoutstate='checkout'
     * 3.2 ������������
     * 3.2.1 ��ӵ�ǰ�û�����Ϊworking������
     * 3.2.2��ӷǵ�ǰ�û�����Ϊcheckout������
	 * 
     */
    public List<Persistable> getCheckResult(String workingInclude,String newInclude,String selectSql,boolean reset){
    	User u = SessionHelper.getService().getUser();
    	
    	final String startkey = u.getInnerId()+"_sever_search_start_key";
    	final String totalNumkey = u.getInnerId()+"_sever_search_totalNum_key";
    	//��ǰ�û�
    	String strUser = Helper.getSessionService().getUser().getAaUserInnerId();
    	StringBuffer sql = new StringBuffer();
    	sql.append("select * from (");
    	sql.append(selectSql);//�������������鵽����������
    	sql.append(") t where t.CHECKOUTSTATE='checkin'");
    	if(newInclude!=null && newInclude.equals("true")){
			 sql.append(" or t.CHECKOUTSTATE='new'");
		}
    	if(workingInclude!=null && workingInclude.equals("true")){
    		sql.append(" or (t.CHECKOUTSTATE='working' and t.MODIFIER='");
    		sql.append(strUser);
    		sql.append("')");
    		sql.append("or (t.CHECKOUTSTATE='checkout' and t.MODIFIER<>'");
    		sql.append(strUser);
    		sql.append("')");
    	 }else{
    		 sql.append(" or t.CHECKOUTSTATE='checkout'");
    	 }
    	//Ϊ�˱���������ֽ������Ҫ��������
    	sql.append(" order by t.innerId");
    	//ִ�в�ѯ
    	List<Persistable> result = QueryManager.getManager().queryForStep(sql.toString(), startkey, totalNumkey, reset);
    	return result;
    }

	public int getIntParam(String param, int defValue) {
		int r = defValue;
		try {
			r = Integer.parseInt(getValue(param));
		} catch (Exception e) {
			r = defValue;
		}
		return r;

	}

	/**
	 * ���ݲ�����������װhql���<br>
	 * ���ذ�����ѯͳ������HQL
	 * 
	 * @author ������ 2011-12-14 ����11:01:04
	 * @param type
	 * @param prefix
	 *            ��ϳɵĶ�̬��ͼ����
	 * @param newInclude 
	 * @param workingInclude 
	 * @return
	 */
	public String getSelectSql(String types, String prefix, String workingInclude, String newInclude) {

		ConditionManager manager = new ConditionManager(); 

		manager.setPrefix(prefix);
		// ���keyWord�ؼ���
		String keyword = getValue("keyword");
		if (keyword != null) {
			// ����ж�Ӧͬһ��ֵ
			// ���Ϊblur��ֵ����ǰ�����ͨ���'%'
			// �������'*'���ʾ��������
			if ("*".equals(keyword.trim())) {
				keyword = "%";
			}
			String blur = getValue("blur");
			// �滻_ ���滻��������
			if (keyword != null) {
				keyword = keyword.replace("/", "//");
				keyword = keyword.replace("_", "/_");
				keyword = keyword.replace("?", "_");
				keyword = keyword.replace(" ", "%");
				keyword = keyword.replace("*", "%");
				keyword = keyword.replace("'", "''");
				keyword="%"+keyword+"%";
			}

			String[] keys = keyword.split(","); 
			addMultiColumnCondition(manager, KeyS.NAME + "," + KeyS.NUMBER,
					keys, blur);
		} else {
			keyword = "";
			String blur = getValue("blur");
			// �滻_ ���滻��������
			if (keyword != null) {
				keyword = keyword.replace("_", "/_");
			}

			String[] keys = keyword.split(",");
			for (int i = 0; i < keys.length; i++) {
				keys[i] = "%" + keys[i] + "%";
			}
			addMultiColumnCondition(manager, KeyS.NAME + "," + KeyS.NUMBER,
					keys, blur);
		}

		addMultiColumnCondition(manager, KeyS.NAME, getValue("qname"));
		addMultiColumnCondition(manager, KeyS.NUMBER, getValue("qnumber"));

//		// �޸�ʱ��
//		String qmodifydate = getValue("qmodifydate");
//		if (qmodifydate != null) {
//			if (qmodifydate.equals("past")) {
//				addDateCondition(manager, KeyS.MODIFY_TIME,
//						getValue("modifypastdays"));
//			} else {
//				addDateCondition(manager, KeyS.MODIFY_TIME,
//						getValue("qmodifydatefrom"), getValue("qmodifydateto"));
//			}
//		}
//		// ����ʱ��
//		String qcreatedate = getValue("qcreatedate");
//		if (qcreatedate != null) {
//			if (qcreatedate.equals("past")) {
//				// ��������ж�
//				addDateCondition(manager, KeyS.CREATE_TIME,
//						getValue("createpastdays"));
//			} else {
//				addDateCondition(manager, KeyS.CREATE_TIME,
//						getValue("qcreatedatefrom"),getValue("qcreatedateto"));
//			}
//		}
		// �Ƿ�Ϊ���°汾
		String latestinbranch = getValue("latestinbranch");
		if (latestinbranch != null && !"".equals(latestinbranch)) {
			addMultiColumnCondition(manager, "LATEST_IN_LEVEL", "1");
			addMultiColumnCondition(manager, "LATEST_IN_BRANCH", "1");
		}

		// ��汾��
		String versionno = getValue("versionno");
		if (versionno != null && !"".equals(versionno)) {
			addMultiColumnCondition(manager, KeyS.VERSION, versionno + "%");
		}
		// ������
		String scope = getValue("scope");
		if(scope!=null&&!"".equals(scope)&&!"null".equals(scope)){
			String[] contextOids = scope.split(",");
			String contextInnerIds = "";
			for(String oid:contextOids){
				String contextInnerId = Helper.getInnerId(oid);
				if(contextInnerIds.length()==0){
					contextInnerIds+=contextInnerId;
				}else{
					contextInnerIds+=","+contextInnerId;
				}
			}
			String blur = getValue("blur");
			this.addMultiColumnCondition(manager, "CONTEXTID", contextInnerIds.split(","), blur);
		}
		// �޸���
		String modifierId = getValue("modifier");
		if (null != modifierId && !"".equals(modifierId)) {
			// ��ȡ�û���UserId
			User user = UserHelper.getService().getUser(modifierId);
			if (user != null) {
				addMultiColumnCondition(manager, "MODIFIER", user.getInnerId());
			}
		}

		// ������
		String creatorId = getValue("creator");
		if (null != creatorId && !"".equals(creatorId)) {
			// ��ȡ�û���UserId
			User user = UserHelper.getService().getUser(creatorId);
			if (user != null) {
				addMultiColumnCondition(manager, "CREATOR", user.getInnerId());
			}
		}
		String conditionStr =  manager.getConditionSql(workingInclude,newInclude);
		// viewid
		String viewoid = getValue("viewid");
		String isAllVersion=getValue("isAllVersion");
		if (viewoid == null || viewoid.equals("") || viewoid.equals("null")) {
			//�Ƿ���ʾ���еİ汾����������
			if(isAllVersion!=null && isAllVersion.equals("all") && (latestinbranch==null || "".equals(latestinbranch))){
				
			}
			else{
				addMultiColumnCondition(manager, "LATEST_IN_BRANCH", "1");
				conditionStr = manager.getConditionSql(workingInclude,newInclude);
			}
			
		} else {
			View self=(View) Helper.getPersistService().getObject(viewoid);
			List<View> allparentviewsandself=new ArrayList<View>();
			allparentviewsandself.add(self);
			allparentviewsandself.addAll(Helper.getViewDefinitionService().getAllParents(self));
			if (allparentviewsandself.size() == 0) {
				// ��Ŀǰ��ϵͳ�в�֧��
				conditionStr = "1=2";
			} else {
				String parentviewStr = "";
				for (int i = 0; i < allparentviewsandself.size(); i++) {
					String parentviewStr1 = ",'"
							+ allparentviewsandself.get(i).getInnerId() + "'";
					parentviewStr = parentviewStr1 + parentviewStr; 
				}
				String parentViewId = parentviewStr.substring(1);
				conditionStr = manager.getConditionSql() + " and (VIEWID in("
						+ parentViewId + ") or VIEWID is null)";
				// addMultiColumnCondition(manager, "VIEWID", viewId);
			}
		}
		//��ȡҳ������������������������һ���ֶε��ڻ��߲�����ĳ��ֵ��
		String addfield = getValue("addfield");
		//�Ƿ����
		String isequal=getValue("isequal");
	    //�ֶ�ֵ
		String filedvalue=getValue("filedvalue");
		if( addfield!=null && filedvalue!=null &&  isequal !=null && !addfield.equals("null") && !addfield.equals("null") && !filedvalue.equals("null")  &&!isequal.equals("null")){
			if(isequal=="false"){
				conditionStr=conditionStr+" and "+addfield+"!='"+filedvalue+"'";
			}
			else if(isequal=="true"){
				conditionStr=conditionStr+" and "+addfield+"='"+filedvalue+"'";
			}
		}
		StringBuffer type=new StringBuffer();
		String[] ts = types.split(",");
		boolean isFirst=true;
		Map<String,String> map=new HashMap<String,String>();
		for(int i=0;i<ts.length;i++){
			//������ͨ�ò�����ʵ��������classId��ͬ
			if(ts[i].equals("GeneralPart") || ts[i].equals("FamilyInstance")){
				ts[i]="Part";
			}
			if(map.get(ts[i])==null){
				map.put(ts[i], ts[i]);
				if(isFirst){
					type.append("'"+ts[i]+"'");
					isFirst=false;
				}
				else{
					type.append(",'"+ts[i]+"'");
				}
			}
		}
		//���������������
		conditionStr=conditionStr+" and CLASSID in("+type.toString()+")";
		// ��ȡ��ѯ��SQL
		// String conditionStr = manager.getConditionSql();
		String shql = "select * from  (" + getViewSql(types) + ") "
					+ prefix + " where " + conditionStr;
			 
		return shql;
	}
	// ��Ӷ��ж�ֵ��������
	public void addMultiColumnCondition(ConditionManager manager,
			String columns, String value) {
		if (value == null)
			return;
		manager.addCondition(new MultiColumnCondition(columns, value));
	}

	// ��Ӷ��ж�������� ��������
	public void addMultiColumnCondition(ConditionManager manager,
			String columns, String[] value, String blur) {
		if (value == null)
			return;
		MultiColumnCondition c = new MultiColumnCondition(columns, value);
		if (blur != null && blur.equals("0")) {
			c.setAndJoin();
		}
		manager.addCondition(c);
	}

//	// ��ȥ������Ĳ�ѯ������ӽ���
//	private void addDateCondition(ConditionManager manager, String column,
//			String pastdays) {
//		int days = Integer.parseInt(pastdays);
//		if (days != 0) {
//			manager.addCondition(new DateCondition(column, days));
//		}
//	}
//
//	// ��Ӹ������ڵĲ�ѯ
//	private void addDateCondition(ConditionManager manager, String column,
//			String fromdate, String todate) {
//		if (fromdate != null && todate != null) {
//			manager.addCondition(new DateCondition(column, fromdate,
//					todate));
//		}
//	}

	private String getValue(String key) {
		String value = params.get(key);
		if (value == null || "".equals(value.trim())) {
			return null;
		} else {
			return value.trim();
		}
	}

	public String getViewSql(String types) {
		String[] ts = types.split(",");
		List<SearchViewProvider> views = new ArrayList<SearchViewProvider>();
		ModelInfoService moExternalService = ModelInfoUtil.getService();
		Map<String,String> map=new HashMap<String,String>();
		
		for (String t : ts) {
			SearchViewProvider view=null;
			//ͨ�ò�����ʵ���������⴦�����߾����ڲ���(Part)
			if(t.equals("GeneralPart") || t.equals("FamilyInstance")){
				view = (SearchViewProvider) ViewProviderManager
						.getInstance().getViewProviderByName(t);
			}
			else{
			//�������������ȡ�丸���͡�
				t=Helper.getTypeService().getTargetClassId(t);
			     //ȥ���ظ�ֵ������Ѿ���������Ҫ��ȡ����ͼ����
				if(map.get(t)==null){
					// ���ݾ������ͻ�ȡ��Ӧ����ͼ����
						map.put(t, t);
						view = (SearchViewProvider) ViewProviderManager
								.getInstance().getViewProviderByName(t);
				}
			}
			if (view != null)
				views.add(view);
		}
		if (views.size() == 0)
			return null;
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
	
	public String getSpotConditions() {
		return spotConditions;
	}

	public void setSpotConditions(String spotConditions) {
		this.spotConditions = spotConditions;
	}
}
