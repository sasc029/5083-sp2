package com.bjsasc.adm.active.service.activerelated;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.bjsasc.adm.active.model.activeset.ActiveSet;
import com.bjsasc.plm.Helper;
import com.bjsasc.plm.core.query.condition.Condition;
import com.bjsasc.plm.grid.data.GridDataService;
import com.bjsasc.plm.type.type.Type;

/**
 * ��ض��������ף���ѯ����ʵ���ࡣ
 * 
 * @author gengancong 2013-6-3
 */
public class RelatedActiveSetImpl implements GridDataService {

	public List<Map<String, Object>> getRows(String spot, String spotInstance, 
			Map<Type, Condition> typeCondition, Map<String, Object> map) {

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		// ȡ�õ�ǰ�����ļ�OID
		String activeDocumentOid = (String) map.get("OID");

		String sql = "SELECT DISTINCT S.* FROM ADM_ACTIVESET S, ADM_ACTIVESETLINK L "
				+ " WHERE S.CLASSID = L.FROMOBJECTCLASSID AND S.INNERID = L.FROMOBJECTID "
				+ "   AND L.TOOBJECTCLASSID || ':' || L.TOOBJECTID = ? ";

		List<ActiveSet> resultList = Helper.getPersistService().query(sql, ActiveSet.class, 
				activeDocumentOid);

		for (ActiveSet set : resultList) {
			Map<String, Object> setMap = Helper.getTypeManager().format(set);
			list.add(setMap);
		}

		return list;
	}

}
