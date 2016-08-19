package com.bjsasc.ddm.distribute.service.distributeobjecttype;

import java.util.List;
import java.util.Map;

import com.bjsasc.ddm.distribute.helper.DistributeHelper;
import com.bjsasc.plm.core.query.condition.Condition;
import com.bjsasc.plm.grid.data.GridDataService;
import com.bjsasc.plm.type.type.Type;

public class DistributeObjectType_GridDataServiceImpl implements
		GridDataService {

	public List<Map<String, Object>> getRows(String spot, String spotInstance,
			Map<Type, Condition> typeCondition, Map<String, Object> params) {
		String contextOid = (String) params.get("contextOid");
		DistributeObjectTypeService service = DistributeHelper.getDistributeObjectTypeService();
		List<Map<String, Object>> resultList = service.findByContextOid(contextOid, spot, spotInstance);
		return resultList;
	}
}
