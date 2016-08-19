package com.bjsasc.adm.common;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.cascc.avidm.util.SplitString;

/**
 * 批量编辑条件对象
 *
 * @author gengancong 2013-07-01
 */
public class EditMultiConditon {
	/**
	 * 表达式作用的模型id
	 */
	private String typeId;

	/**
	 * 属性值类型
	 */
	private String attrFieldType;

	/**
	 * 属性id
	 */
	private String attrId;

	/**
	 * 全部修改("0")/部分修改("1")
	 */
	private String allFlag;

	/**
	 * 修改后
	 */
	private String valueStringAfter;

	/**
	 * 修改前
	 */
	private String valueStringBefore;

	public String getAttrId() {
		return attrId;
	}

	public void setAttrId(String attrId) {
		this.attrId = attrId;
	}

	public String getTypeId() {
		return typeId;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}

	public String getValueStringAfter() {
		return valueStringAfter;
	}

	public void setValueStringAfter(String valueStringAfter) {
		this.valueStringAfter = valueStringAfter;
	}

	public String getValueStringBefore() {
		return valueStringBefore;
	}

	public void setValueStringBefore(String valueStringBefore) {
		this.valueStringBefore = valueStringBefore;
	}


	public String getAllFlag() {
		return allFlag;
	}

	public void setAllFlag(String allFlag) {
		this.allFlag = allFlag;
	}

	public String getAttrFieldType() {
		return attrFieldType;
	}

	public void setAttrFieldType(String attrFieldType) {
		this.attrFieldType = attrFieldType;
	}

	/**
	 * 通过JSON对象加载批量编辑条件对象
	 * @param jsonObject
	 */
	public void loadFromJsonObject(JSONObject jsonObject) {
		typeId = getKeyValue("typeId", jsonObject);
		attrId = getKeyValue("attrId", jsonObject);
		attrFieldType = getKeyValue("attrFieldType", jsonObject);
		allFlag = getKeyValue("allFlag", jsonObject);
		String editValue = getKeyValue("editValue", jsonObject);
		if ("0".equals(allFlag)) {
			List<String> temp = SplitString.string2List(editValue, ",");
			valueStringAfter = temp.get(0);
		} else {
			List<String> temp = SplitString.string2List(editValue, ",");
			valueStringBefore = temp.get(0);
			valueStringAfter = temp.get(1);
		}
	}

	private String getKeyValue(String key, JSONObject jsonObject) {
		try {
			Object value = jsonObject.get(key);
			return value == null ? null : value.toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return null;
	}

}