package com.bjsasc.ddm.distribute.service.distributeinfoconfig;

import java.util.List;
import java.util.Map;

import com.bjsasc.ddm.distribute.helper.DistributeHelper;
import com.bjsasc.plm.core.query.condition.Condition;
import com.bjsasc.plm.grid.data.GridDataService;
import com.bjsasc.plm.type.type.Type;

/**
 * 默认分发信息配置服务查询类
 * 
 * @author yangqun 2014-05-09
 *
 */
public class DistributeInfoConfig_GridDataServiceImpl implements
		GridDataService {

	public List<Map<String, Object>> getRows(String spot, String spotInstance,
			Map<Type, Condition> typeCondition, Map<String, Object> map) {

		DistributeInfoConfigService infoConfigservice = DistributeHelper
				.getDistributeInfoConfigService();
		List<Map<String, Object>> listDis = infoConfigservice
				.listDistributeInfoConfig(spot, spotInstance);

		return listDis;
	}
}
