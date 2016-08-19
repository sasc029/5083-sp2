package com.bjsasc.ddm.common;

import java.util.List;

import com.bjsasc.plm.common.search.AbstractCondition;

public class InCondition extends AbstractCondition {
	private static final String IN_KEY = " in ";

	private List<String> values;

	public InCondition(String columns, List<String> values) {
		setColumns(columns);
		setValues(values);
	}

	public String getConditionSql() {
		StringBuffer sb = new StringBuffer();

		sb.append("(" + getPrefixStr() + columns + IN_KEY + "(" + getValues() + "))");
		return sb.toString();
	}

	/**
	 * @return values
	 */
	public String getValues() {
		String result = "";
		for (String val : values) {
			result += ",'" + val + "'";
		}
		if (result.length() > 1) {
			result = result.substring(1);
		}
		return result;
	}

	/**
	 * @param values ÒªÉèÖÃµÄ values
	 */
	public void setValues(List<String> values) {
		this.values = values;
	}
}
