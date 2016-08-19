package com.bjsasc.ddm.distribute.service.distributecommonconfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bjsasc.plm.Helper;
import com.bjsasc.plm.core.query.condition.Condition;
import com.bjsasc.plm.grid.GridHelper;
import com.bjsasc.plm.grid.data.GridDataService;
import com.bjsasc.plm.type.type.Type;
import com.bjsasc.ddm.distribute.model.distributecommonconfig.DistributeCommonConfig;

public class DisComConfigDataGridImpl implements GridDataService{

	public List<Map<String, Object>> getRows(String spot, String spotInstance, 
			Map<Type, Condition> typeCondition, Map<String, Object> map) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		
		List<String> keys = GridHelper.getService().getMyLatestGridViewColumnIds(spot, spotInstance);
		
		String hql = "from DistributeCommonConfig order by createTime asc";
		List<DistributeCommonConfig> result = Helper.getPersistService().find(hql);
		for (DistributeCommonConfig distributeCommonConfig : result) {
			Map<String, Object> roleMap = new HashMap<String, Object>();
			roleMap = Helper.getTypeManager().format(distributeCommonConfig, keys);
			list.add(roleMap);
		}
		return list;
	}
}