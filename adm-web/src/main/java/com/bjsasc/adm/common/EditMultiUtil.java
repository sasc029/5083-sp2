package com.bjsasc.adm.common;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.bjsasc.avidm.core.standard.SOType;
import com.bjsasc.plm.core.Helper;
import com.bjsasc.plm.core.persist.model.Persistable;

/**
 * 批量编辑处理类
 *
 * @author gengancong 2013-07-01
 */
public class EditMultiUtil {
	public List<EditMultiConditon> conditions = new ArrayList<EditMultiConditon>();

	/**
	 * 从json对象中加载
	 * @param jsonObject
	 */
	public void loadFromJsonObject(String jsonObject) {
		try {
			JSONArray jsonArray = new JSONArray(jsonObject);
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonExp = (JSONObject) jsonArray.get(i);
				EditMultiConditon condition = new EditMultiConditon();
				condition.loadFromJsonObject(jsonExp);
				conditions.add(condition);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public Object getAttrValueById(String attrId, Object target) {
		if (target == null) {
			return null;
		}
		try {
			Class<?> clazz = Class.forName(target.getClass().getName());
			SOType type = new SOType();

			type.setId(clazz.getSimpleName());
			//type.setName(clazz.getSimpleName());
			type.setClassName(clazz.getName());
			String methodName = "get" + attrId.substring(0, 1).toUpperCase() + attrId.substring(1).toLowerCase();
			Method method = clazz.getMethod(methodName);
			return method.invoke(target);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public Object setAttrValueById(String attrId, Object target, String value) {
		if (target == null) {
			return null;
		}
		try {
			Class<?> clazz = Class.forName(target.getClass().getName());
			SOType type = new SOType();

			type.setId(clazz.getSimpleName());
			//type.setName(clazz.getSimpleName());
			type.setClassName(clazz.getName());
			String methodName = "set" + attrId.substring(0, 1).toUpperCase() + attrId.substring(1).toLowerCase();
			Field f = clazz.getDeclaredField(attrId.toLowerCase());
			Method method = clazz.getMethod(methodName, new Class[] { f.getType() });
			return method.invoke(target, new Object[] { value });
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public Class<?>[] getClassArray(SOType soType) {
		Class<?>[] clazzs = new Class[1];

		String className = soType.getClassName();
		if ("long".equals(className)) {
			clazzs[0] = long.class;
		} else if ("int".equals(className)) {
			clazzs[0] = int.class;
		} else if ("boolean".equals(className)) {
			clazzs[0] = boolean.class;
		} else if ("float".equals(className)) {
			clazzs[0] = float.class;
		} else if ("double".equals(className)) {
			clazzs[0] = double.class;
		} else if ("String".equals(className)) {
			clazzs[0] = String.class;
		} else if ("Integer".equals(className)) {
			clazzs[0] = Integer.class;
		}
		return clazzs;
	}

	public Object convertValueType(String val, SOType soType) {
		String className = soType.getClassName();

		if ("long".equals(className)) {
			return Long.parseLong(val);
		} else if ("int".equals(className)) {
			return Integer.parseInt(val);
		} else if ("boolean".equals(className)) {
			return Boolean.parseBoolean(val);
		} else if ("float".equals(className)) {
			return Float.parseFloat(val);
		} else if ("double".equals(className)) {
			return Double.parseDouble(val);
		} else if ("Integer".equals(className)) {
			return Integer.parseInt(val);
		} else {
			return val;
		}
	}

	public void updataEditMulti(List<Persistable> targetList, String jsonObject) {
		loadFromJsonObject(jsonObject);
		for (Persistable target : targetList) {
			for (EditMultiConditon condition : conditions) {
				String attrId = condition.getAttrId();
				if ("0".equals(condition.getAllFlag())) {
					String value = condition.getValueStringAfter();
					setAttrValueById(attrId, target, value);
				} else {
					String valueBefore = condition.getValueStringBefore();
					String valueTemp = (String) getAttrValueById(attrId, target);
					if (valueTemp == null) {
						valueTemp = "";
					}
					if (valueBefore.equals(valueTemp)) {
						String value = condition.getValueStringAfter();
						setAttrValueById(attrId, target, value);
					} else {
						continue;
					}
				}
			}
			Helper.getPersistService().update(target);
		}

	}
}
