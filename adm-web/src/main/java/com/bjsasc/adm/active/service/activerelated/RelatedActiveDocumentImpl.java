package com.bjsasc.adm.active.service.activerelated;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.bjsasc.adm.active.model.activedocument.ActiveDocument;
import com.bjsasc.adm.active.model.activeorder.ActiveOrder;
import com.bjsasc.adm.active.model.activeset.ActiveSet;
import com.bjsasc.plm.Helper;
import com.bjsasc.plm.core.persist.model.Persistable;
import com.bjsasc.plm.core.query.condition.Condition;
import com.bjsasc.plm.grid.data.GridDataService;
import com.bjsasc.plm.type.type.Type;

/**
 * 相关对象（现行文件）查询服务实现类。
 * 
 * @author gengancong 2013-6-3
 */
public class RelatedActiveDocumentImpl implements GridDataService {

	public List<Map<String, Object>> getRows(String spot, String spotInstance, Map<Type, Condition> typeCondition,
			Map<String, Object> map) {

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		// 取得当前现行OID
		String activeOid = (String) map.get("OID");
		Persistable object = Helper.getPersistService().getObject(activeOid);
		String sql = "";
		if (object instanceof ActiveOrder) {
			sql = "SELECT DISTINCT D.* FROM ADM_ACTIVEDOCUMENT D, ADM_ACTIVEORDEREDLINK L "
					+ " WHERE L.FROMOBJECTCLASSID || ':' || L.FROMOBJECTID = ? "
					+ "   AND (L.TOOBJECTCLASSID || ':' || L.TOOBJECTID = D.CLASSID || ':' || D.INNERID"
					+ "    OR L.ORDEREDBEFORECLASSID || ':' || L.ORDEREDBEFOREID = D.CLASSID || ':' || D.INNERID)";
		} else if (object instanceof ActiveSet) {
			sql = "SELECT DISTINCT D.* FROM ADM_ACTIVEDOCUMENT D, ADM_ACTIVESETLINK L "
					+ " WHERE L.FROMOBJECTCLASSID  || ':' ||  L.FROMOBJECTID = ? "
					+ "   AND L.TOOBJECTCLASSID || ':' || L.TOOBJECTID = D.CLASSID || ':' || D.INNERID ";
		}
		if (sql.length() > 0) {
			List<ActiveDocument> resultList = Helper.getPersistService().query(sql, ActiveDocument.class, activeOid);

			List<String> numberList = new ArrayList<String>();
			for (ActiveDocument order : resultList) {
				Map<String, Object> orderMap = Helper.getTypeManager().format(order);

				Object sObject = orderMap.get("SOURCE");
				Map sMap = (Map) sObject;
				Object number = sMap.get("NUMBER");
				if (number != null && !numberList.contains(number.toString())) {
					numberList.add(number.toString());
					list.add(orderMap);
				}
			}
		}
		return list;
	}

}
