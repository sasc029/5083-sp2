package com.bjsasc.adm.common;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.cascc.avidm.util.SplitString;

/**
 * �����༭��������
 *
 * @author gengancong 2013-07-01
 */
public class EditMultiConditon {
	/**
	 * ���ʽ���õ�ģ��id
	 */
	private String typeId;

	/**
	 * ����ֵ����
	 */
	private String attrFieldType;

	/**
	 * ����id
	 */
	private String attrId;

	/**
	 * ȫ���޸�("0")/�����޸�("1")
	 */
	private String allFlag;

	/**
	 * �޸ĺ�
	 */
	private String valueStringAfter;

	/**
	 * �޸�ǰ
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
	 * ͨ��JSON������������༭��������
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