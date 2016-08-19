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
	 * 与场景相关的其他搜索条件，页面上的搜索为spot_conditions
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
	 * 组合成Sql语句，生成json数据
	 * 
	 * @author 李天立 2011-12-14 上午11:02:25
	 * @return
	 * @throws Exception 
	 * plmwebservice中也调用了该方法
	 */
	public List<Persistable> getResult(String OID,boolean reset){
		//定义返回的对象列表
		List<Persistable> rsfiltered = new ArrayList<Persistable>();
	    String types = getValue("types");
		//是否包含工作副本
        String workingInclude=getValue("workingInclude");
        String newInclude=getValue("newInclude");
		String condition=getValue("condition");
		String spotCondition="";
		if(condition!=null && condition!="" && !condition.equals("null")){
			spotCondition= URLDecoder.decode(condition);
		}
		// 如果没有选择类型，则返回空
		if(types == null){
			System.out.println("没有查询对象，什么也不做");
			return rsfiltered;
		}
		String sqls = getSelectSql(types, "t",workingInclude,newInclude);
		
		if(sqls == null){
			// doNothing
			System.out.println("什么也不做!");
		}else{
			// 执行查询
			//List<Map<String, Object>> results = PersistHelper.getService()
					//.query(sqls.SELECTSQL);
			List<Persistable> results = getCheckResult(workingInclude,newInclude,sqls,reset);
			//获取结果集的长度
		    this.setTotalResult(results.size());
		    
			
			// 对一些列进行处理
			if (results.size() > 0) {
				CheckBuilder checker = CheckBuilder.builder(spotCondition); 
				for (Persistable obj : results) {
					 String oid = Helper.getOid(obj);
					//过滤某个对象
					if (!oid.equals(OID)&&(checker==null|| checker.check(obj))) {
					    // 添加场景相关的检查,由于场景相关的检查比较快，不需要查询数据库，所以放到前面进行检查
						rsfiltered.add(obj);
			        }
		        }
	      }
	    }
		 return rsfiltered;
	}
    /**
     * 根据搜索出来的结果集进行过滤
     * @author 刘永 2013-12-25 10:59
	 * @param param
	 * @return
     * 1. 添加 checkoutState为checkin的条件
     * 2. 如果newInclude ，添加||checkoutstate='new'
     * 3. 是否包含工作副本 
     * 3.1 不包含工作副本 添加 checkoutstate='checkout'
     * 3.2 包含工作副本
     * 3.2.1 添加当前用户并且为working的条件
     * 3.2.2添加非当前用户并且为checkout的条件
	 * 
     */
    public List<Persistable> getCheckResult(String workingInclude,String newInclude,String selectSql,boolean reset){
    	User u = SessionHelper.getService().getUser();
    	
    	final String startkey = u.getInnerId()+"_sever_search_start_key";
    	final String totalNumkey = u.getInnerId()+"_sever_search_totalNum_key";
    	//当前用户
    	String strUser = Helper.getSessionService().getUser().getAaUserInnerId();
    	StringBuffer sql = new StringBuffer();
    	sql.append("select * from (");
    	sql.append(selectSql);//根据搜索条件查到的所有数据
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
    	//为了避免随机出现结果，需要进行排序
    	sql.append(" order by t.innerId");
    	//执行查询
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
	 * 根据参数和类型组装hql语句<br>
	 * 返回包含查询统计两个HQL
	 * 
	 * @author 李天立 2011-12-14 上午11:01:04
	 * @param type
	 * @param prefix
	 *            组合成的动态视图别名
	 * @param newInclude 
	 * @param workingInclude 
	 * @return
	 */
	public String getSelectSql(String types, String prefix, String workingInclude, String newInclude) {

		ConditionManager manager = new ConditionManager(); 

		manager.setPrefix(prefix);
		// 组合keyWord关键字
		String keyword = getValue("keyword");
		if (keyword != null) {
			// 多个列对应同一个值
			// 如果为blur有值，则前后添加通配符'%'
			// 如果输入'*'则表示搜索所有
			if ("*".equals(keyword.trim())) {
				keyword = "%";
			}
			String blur = getValue("blur");
			// 替换_ 不替换其他内容
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
			// 替换_ 不替换其他内容
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

//		// 修改时间
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
//		// 创建时间
//		String qcreatedate = getValue("qcreatedate");
//		if (qcreatedate != null) {
//			if (qcreatedate.equals("past")) {
//				// 添加日期判断
//				addDateCondition(manager, KeyS.CREATE_TIME,
//						getValue("createpastdays"));
//			} else {
//				addDateCondition(manager, KeyS.CREATE_TIME,
//						getValue("qcreatedatefrom"),getValue("qcreatedateto"));
//			}
//		}
		// 是否为最新版本
		String latestinbranch = getValue("latestinbranch");
		if (latestinbranch != null && !"".equals(latestinbranch)) {
			addMultiColumnCondition(manager, "LATEST_IN_LEVEL", "1");
			addMultiColumnCondition(manager, "LATEST_IN_BRANCH", "1");
		}

		// 大版本号
		String versionno = getValue("versionno");
		if (versionno != null && !"".equals(versionno)) {
			addMultiColumnCondition(manager, KeyS.VERSION, versionno + "%");
		}
		// 上下文
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
		// 修改人
		String modifierId = getValue("modifier");
		if (null != modifierId && !"".equals(modifierId)) {
			// 获取用户的UserId
			User user = UserHelper.getService().getUser(modifierId);
			if (user != null) {
				addMultiColumnCondition(manager, "MODIFIER", user.getInnerId());
			}
		}

		// 创建人
		String creatorId = getValue("creator");
		if (null != creatorId && !"".equals(creatorId)) {
			// 获取用户的UserId
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
			//是否显示所有的版本，根据需求定
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
				// 在目前的系统中不支持
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
		//获取页面所传过来的条件（传过来一个字段等于或者不等于某个值）
		String addfield = getValue("addfield");
		//是否等于
		String isequal=getValue("isequal");
	    //字段值
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
			//部件，通用部件，实例部件的classId相同
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
		//根据搜索对象过滤
		conditionStr=conditionStr+" and CLASSID in("+type.toString()+")";
		// 获取查询的SQL
		// String conditionStr = manager.getConditionSql();
		String shql = "select * from  (" + getViewSql(types) + ") "
					+ prefix + " where " + conditionStr;
			 
		return shql;
	}
	// 添加多列多值搜索条件
	public void addMultiColumnCondition(ConditionManager manager,
			String columns, String value) {
		if (value == null)
			return;
		manager.addCondition(new MultiColumnCondition(columns, value));
	}

	// 添加多列多搜索添件 搜索条件
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

//	// 过去多少天的查询条件添加进来
//	private void addDateCondition(ConditionManager manager, String column,
//			String pastdays) {
//		int days = Integer.parseInt(pastdays);
//		if (days != 0) {
//			manager.addCondition(new DateCondition(column, days));
//		}
//	}
//
//	// 添加根据日期的查询
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
			//通用部件和实例部件特殊处理，两者均属于部件(Part)
			if(t.equals("GeneralPart") || t.equals("FamilyInstance")){
				view = (SearchViewProvider) ViewProviderManager
						.getInstance().getViewProviderByName(t);
			}
			else{
			//根据搜索对象获取其父类型。
				t=Helper.getTypeService().getTargetClassId(t);
			     //去除重复值，如果已经存在则不需要获取其视图定义
				if(map.get(t)==null){
					// 根据具体类型获取相应的视图定义
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
