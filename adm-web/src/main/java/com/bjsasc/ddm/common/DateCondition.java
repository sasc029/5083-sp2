package com.bjsasc.ddm.common;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.bjsasc.plm.common.search.AbstractCondition;

public class DateCondition extends AbstractCondition {
	private long fromdate = 0l;
	private long todate = 0l;

	public DateCondition(String columns, String fromdatestr, String todatestr) {
		setColumns(columns);
		if(!"".equals(fromdatestr)){
			setFromdate(formatDateFromStr(fromdatestr));
		}
		if(!"".equals(todatestr)){
			setTodate(formatDateFromStr(todatestr));
		}
	}

	public DateCondition(String column, int daysBefore) {
		setColumns(column);
		Date d = new Date();
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		c.add(Calendar.DATE, -1 * (daysBefore -1));
		SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd");
		setFromdate(formatDateFromStr(sf.format(c.getTime())));
		setTodate(System.currentTimeMillis());
		// 初始化目前的日期
	}

	public String getConditionSql() {
		// 日期转换为Long型
		StringBuffer sb = new StringBuffer();
		if(todate == 0){
			sb.append("(" + getPrefixStr() + columns + " >=" + fromdate + ")");
		}else{
			sb.append("(" + getPrefixStr() + columns + " >=" + fromdate + " and " + getPrefixStr() + columns + " <" + todate
					+ ")");
		}
		return sb.toString();
	}

	private long formatDateFromStr(String dateStr) {
		long result = 0l;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date d = sdf.parse(dateStr);
			result = d.getTime();
		} catch (Exception e) {
			e.printStackTrace();
			result = 0;
		}
		return result;

	}

	/**
	 * @return the fromdate
	 */
	public long getFromdate() {
		return fromdate;
	}

	/**
	 * @param fromdate
	 *            the fromdate to set
	 */
	public void setFromdate(long fromdate) {
		this.fromdate = fromdate;
	}

	/**
	 * @return the todate
	 */
	public long getTodate() {
		return todate;
	}

	/**
	 * @param todate
	 *            the todate to set
	 */
	public void setTodate(long todate) {
		this.todate = todate;
	}

}
