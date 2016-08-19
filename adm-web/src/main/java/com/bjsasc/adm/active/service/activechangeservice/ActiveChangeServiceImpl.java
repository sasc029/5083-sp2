package com.bjsasc.adm.active.service.activechangeservice;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.bjsasc.adm.active.helper.AdmHelper;
import com.bjsasc.adm.active.model.activedocument.ActiveDocument;
import com.bjsasc.adm.active.model.activeorder.ActiveOrder;
import com.bjsasc.adm.active.model.activeset.ActiveSet;
import com.bjsasc.adm.active.model.query.ActiveQuerySchema;
import com.bjsasc.platform.objectmodel.business.persist.PTFactor;
import com.bjsasc.plm.Helper;
import com.bjsasc.plm.core.persist.model.Persistable;
import com.bjsasc.plm.core.util.DateTimeUtil;

/**
 * 现行信息统计图服务实现类。
 * 
 * @author tudong 2013-06-03
 */
@SuppressWarnings({ "unchecked", "unused", "rawtypes" })
public class ActiveChangeServiceImpl implements ActiveChangeService {
	public List<Map<String, Object>> getStatResultByGroup(String activename, String activeModelId, Long begintime,
			Long endtime, String showtype) {
		endtime=endtime+86399000L;
		String oids = AdmHelper.getActiveStatisticsService().getOids(activeModelId);
		oids = oids.substring(0, oids.length() - 1);
		List<Map<String, Object>> listall = new ArrayList<Map<String, Object>>();
		Map<String, Object> mtime = new LinkedHashMap<String, Object>();
		SimpleDateFormat eformatter = new SimpleDateFormat("yyyy-mm-dd");
		String active = "";
		String activemaster = "";
		if ("doc".equals(activename)) {
			active = " adm_activedocument";
			activemaster = "adm_activedocumentmaster";
		}
		if ("set".equals(activename)) {
			active = "adm_activeset";
			activemaster = "adm_activesetmaster";
		}
		if (("order".equals(activename))) {
			active = "adm_activeorder";
			activemaster = "adm_activeordermaster";
		}
		String activetablesql = "select a.* from " + active + " a,PT_OM_CONTROLBRANCH CB2," + activemaster + " B, ";
		activetablesql += "( SELECT PCB.MASTERID, PCB.MASTERCLASSID,MAX(MODIFYSTAMP) MODIFYSTAMP FROM PT_OM_CONTROLBRANCH PCB ";
		activetablesql += "GROUP BY PCB.MASTERID, PCB.MASTERCLASSID) CB1 where  A.CONTROLBRANCHID = CB2.INNERID AND A.CONTROLBRANCHCLASSID = CB2.CLASSID ";
		activetablesql += "AND B.INNERID = CB1.MASTERID AND B.CLASSID = CB1.MASTERCLASSID AND CB1.MODIFYSTAMP = CB2.MODIFYSTAMP and A.MASTERID = B.INNERID ";
		activetablesql += "AND A.MASTERCLASSID = B.CLASSID  and  A.STATENAME  not in ('已删除') ";

		if ("YEAR".equals(showtype)) {
			String sqlyear = "";
			if ("doc".equals(activename)) {
				sqlyear = "select  cnt, yy  from (select count(innerid) cnt, to_char(LongToDate(createtime),'YYYY') yy ";
				sqlyear += "from  (" + activetablesql + ") where folderClassId || ':' || folderId in(" + oids
						+ ") and  createtime between " + begintime + " and " + endtime
						+ " group by to_char(LongToDate(createtime),'YYYY') ) ";
				sqlyear += " where  yy >=to_char(LongToDate(?),'YYYY')  and yy <= to_char(LongToDate(?),'YYYY') ";
			}
			if ("set".equals(activename)) {
				sqlyear = "select  cnt, yy  from (select count(innerid) cnt, to_char(LongToDate(createtime),'YYYY') yy ";
				sqlyear += "from  (" + activetablesql + ") where folderClassId || ':' || folderId in(" + oids
						+ ")  and  createtime between " + begintime + " and " + endtime
						+ " group by to_char(LongToDate(createtime),'YYYY') )";
				sqlyear += " where  yy >=to_char(LongToDate(?),'YYYY')  and yy <=to_char(LongToDate(?),'YYYY') ";
			}
			if ("order".equals(activename)) {
				sqlyear = "select  cnt, yy  from (select count(innerid) cnt, to_char(LongToDate(createtime),'YYYY') yy ";
				sqlyear += "from  (" + activetablesql + ") where folderClassId || ':' || folderId in(" + oids
						+ ")  and createtime between " + begintime + " and " + endtime
						+ " group by to_char(LongToDate(createtime),'YYYY')) ";
				sqlyear += " where  yy >=to_char(LongToDate(?),'YYYY')  and yy <= to_char(LongToDate(?),'YYYY')";
			}
			int btime = Integer.parseInt(eformatter.format(begintime).substring(0, 4));
			int etime = Integer.parseInt(eformatter.format(endtime).substring(0, 4));
			List<Map<String, Object>> list = Helper.getPersistService().query(sqlyear,begintime,endtime);
			for (int i = btime; i <= etime; i++) {
				mtime.put(i + "年", "0");
				for (int j = 0; j < list.size(); j++) {
					Map<String, Object> listyear = list.get(j);
					if (listyear.get("YY").toString().equals(i + "")) {
						mtime.put(i + "年", listyear.get("CNT").toString());
					}
				}
			}

		}

		if ("QUARTER".equals(showtype)) {
			String sqlquarter = "";
			if ("doc".equals(activename) && activeModelId != "") {
				sqlquarter = "select count(innerid)  as cnt ,to_char(LongToDate(createtime),'YYYY') yy, ";
				sqlquarter += "To_Char(LongToDate(createtime), 'q')  qq  ";
				sqlquarter += "	from (" + activetablesql + ") where folderClassId || ':' || folderId in(" + oids
						+ ") and createtime between " + begintime + " and " + endtime + "  group by ";
				sqlquarter += "To_Char(LongToDate(createtime), 'q') ,";
				sqlquarter += "to_char(LongToDate  (createtime),'YYYY') ";
			}
			if ("set".equals(activename)) {
				sqlquarter = "select count(innerid)  as cnt ,to_char(LongToDate(createtime),'YYYY') yy, ";
				sqlquarter += "To_Char(LongToDate(createtime), 'q')  qq  ";
				sqlquarter += "	from (" + activetablesql + ")  where folderClassId || ':' || folderId in(" + oids
						+ ")  and createtime between " + begintime + " and " + endtime + "  group by ";
				sqlquarter += "To_Char(LongToDate(createtime), 'q') , ";
				sqlquarter += " to_char(LongToDate(createtime),'YYYY') ";
			}
			if ("order".equals(activename)) {
				sqlquarter = "select count(innerid)  as cnt ,to_char(LongToDate(createtime),'YYYY') yy, ";
				sqlquarter += "To_Char(LongToDate(createtime), 'q')  qq ";
				sqlquarter += "	from (" + activetablesql + ")  where folderClassId || ':' || folderId in(" + oids
						+ ")  and createtime between " + begintime + " and " + endtime + "  group by ";
				sqlquarter += "To_Char(LongToDate(createtime), 'q')  , ";
				sqlquarter += " to_char(LongToDate(createtime),'YYYY') ";
			}
			List<Map<String, Object>> list = Helper.getPersistService().query(sqlquarter);
			String timeStart = DateTimeUtil.dateDisplay(begintime, "yyyy-MM-dd");
			String timeEnd = DateTimeUtil.dateDisplay(endtime, "yyyy-MM-dd");
			int time1 = Integer.parseInt(timeStart.replaceAll("-", "").substring(0, 6));
			int time2 = Integer.parseInt(timeEnd.replaceAll("-", "").substring(0, 6));
			for (int i = time1; i <= time2; i++) {
				int year = Integer.parseInt(String.valueOf(i).substring(0, 4));
				int mon = Integer.parseInt(String.valueOf(i).substring(4, 6));
				if (mon > 12) {
					i += 87;
				} else {
					if (mon >= 1 && mon <= 3) {
						mtime.put(year + "年1季度", 0);
						for (int j = 0; j < list.size(); j++) {
							if (list.get(j).get("YY").equals(year + "")
									&& Integer.parseInt(list.get(j).get("QQ").toString()) == 1) {
								mtime.put(year + "年1季度", list.get(j).get("CNT"));
							}
						}
					}
					if (mon >= 4 && mon <= 6) {
						mtime.put(year + "年2季度", 0);
						for (int j = 0; j < list.size(); j++) {
							if (list.get(j).get("YY").equals(year + "")
									&& Integer.parseInt(list.get(j).get("QQ").toString()) == 2) {
								mtime.put(year + "年2季度", list.get(j).get("CNT"));
							}

						}
					}
					if (mon >= 7 && mon <= 9) {
						mtime.put(year + "年3季度", 0);
						for (int j = 0; j < list.size(); j++) {
							if (list.get(j).get("YY").equals(year + "")
									&& Integer.parseInt(list.get(j).get("QQ").toString()) == 3) {
								mtime.put(year + "年3季度", list.get(j).get("CNT"));
							}
						}
					}
					if (mon >= 10 && mon <= 12) {
						mtime.put(year + "年4季度", 0);
						for (int j = 0; j < list.size(); j++) {
							if (list.get(j).get("YY").equals(year + "")
									&& Integer.parseInt(list.get(j).get("QQ").toString()) == 4) {
								mtime.put(year + "年4季度", list.get(j).get("CNT"));
							}

						}
					}
					if (mon % 3 == 1) {
						i += 2;
					} else if (mon % 3 == 2) {
						i += 1;
					}
				}

			}
		}
		if ("MONTH".equals(showtype)) {
			String sqlmonth = "";
			if ("doc".equals(activename)) {
				sqlmonth = "select count(innerid)  as cnt ,to_char(LongToDate(createtime),'YYYY') yy, ";
				sqlmonth += "To_Char(LongToDate(createtime), 'mm')  mm ";
				sqlmonth += "	from (" + activetablesql + ") where folderClassId || ':' || folderId in(" + oids
						+ ") and createtime between " + begintime + " and " + endtime + "  group by ";
				sqlmonth += "To_Char(LongToDate(createtime), 'mm') , ";
				sqlmonth += " to_char(LongToDate  (createtime),'YYYY') ";
			}
			if ("set".equals(activename)) {
				sqlmonth = "select count(innerid)  as cnt ,to_char(LongToDate(createtime),'YYYY') yy, ";
				sqlmonth += "To_Char(LongToDate(createtime), 'mm')  mm ";
				sqlmonth += "	from (" + activetablesql + ")  where folderClassId || ':' || folderId in(" + oids
						+ ")  and createtime between " + begintime + " and " + endtime + "  group by ";
				sqlmonth += "To_Char(LongToDate(createtime), 'mm') , ";
				sqlmonth += " to_char(LongToDate(createtime),'YYYY') ";
			}
			if ("order".equals(activename)) {
				sqlmonth = "select count(innerid)  as cnt ,to_char(LongToDate(createtime),'YYYY') yy, ";
				sqlmonth += "To_Char(LongToDate(createtime), 'mm')  mm";
				sqlmonth += "	from (" + activetablesql + ")  where folderClassId || ':' || folderId in(" + oids
						+ ")  and createtime between " + begintime + " and " + endtime + "  group by ";
				sqlmonth += "To_Char(LongToDate(createtime), 'mm')  , ";
				sqlmonth += " to_char(LongToDate(createtime),'YYYY') ";
			}
			List<Map<String, Object>> list = Helper.getPersistService().query(sqlmonth);
			String timeStart = DateTimeUtil.dateDisplay(begintime, "yyyy-MM-dd");
			String timeEnd = DateTimeUtil.dateDisplay(endtime, "yyyy-MM-dd");
			int time1 = Integer.parseInt(timeStart.replaceAll("-", "").substring(0, 6));
			int time2 = Integer.parseInt(timeEnd.replaceAll("-", "").substring(0, 6));
			for (int i = time1; i <= time2; i++) {
				if ((Integer.parseInt(String.valueOf(i).substring(4, 6))) > 12) {
					i += 87;
				} else {
					mtime.put(
							String.valueOf(i).substring(0, 4) + "年"
									+ Integer.parseInt(String.valueOf(i).substring(4, 6)) + "月", 0);
					for (int j = 0; j < list.size(); j++) {
						if (String.valueOf(i).substring(0, 4).equals(list.get(j).get("YY"))
								&& Integer.parseInt(String.valueOf(i).substring(4, 6)) == Integer.parseInt(list.get(j)
										.get("MM").toString())) {
							mtime.put(String.valueOf(i).substring(0, 4) + "年" + list.get(j).get("MM") + "月", list
									.get(j).get("CNT"));
						}
					}
				}
			}
		}
		listall.add(mtime);
		return listall;
	}

	@Override
	public ActiveQuerySchema modifyQuerySchema(String oid, String acvtivename, String schemaName, int defaultSchema,
			String begintime, String endtime, String activeModelId, String activeModelName, String showdate) {
		String hql = "update ActiveQuerySchema t set t.defaultSchema=? where t.manageInfo.createByRef.innerId=?";
		Helper.getPersistService().bulkUpdate(hql,0, Helper.getSessionService().getUser().getInnerId());
		if (oid.length() <= "ActiveQuerySchema".length() + 5) {
			oid = "ActiveQuerySchema:" + getActiveQuerySchemaList().get(0).getInnerId();
		}
		Persistable obj = Helper.getPersistService().getObject(oid);
		ActiveQuerySchema as = (ActiveQuerySchema) obj;
		as.setActivename(acvtivename);
		as.setSchemaName(schemaName);
		as.setDefaultSchema(defaultSchema);
		as.setBeginTime(begintime);
		as.setEndTime(endtime);
		as.setActiveModelId(activeModelId);
		as.setActiveModelname(activeModelName);
		as.setGroupCondition(showdate);
		Helper.getPersistService().update(as);

		return as;
	}

	@Override
	public ActiveQuerySchema createQuerySchema(String activename, String schemaName, int defaultSchema,
			String begintime, String endtime, String activeModelId, String activeModelName, String showdate) {
		if (defaultSchema == 1) {
			String hql = "update ActiveQuerySchema t set t.defaultSchema=? where t.manageInfo.createByRef.innerId=?";
			Helper.getPersistService().bulkUpdate(hql,0, Helper.getSessionService().getUser().getInnerId());
		}
		ActiveQuerySchema as = new ActiveQuerySchema();
		as.setClassId(ActiveQuerySchema.CLASSID);
		as.setActivename(activename);
		as.setSchemaName(schemaName);
		as.setDefaultSchema(defaultSchema);
		as.setBeginTime(begintime);
		as.setEndTime(endtime);
		as.setActiveModelId(activeModelId);
		as.setActiveModelname(activeModelName);
		as.setGroupCondition(showdate);
		Helper.getPersistService().save(as);
		return as;
	}

	@Override
	public List<ActiveQuerySchema> getActiveQuerySchemaList() {
		String sql = "select * from adm_QuerySchema";
		List<ActiveQuerySchema> list = Helper.getPersistService().query(sql, ActiveQuerySchema.class);
		return list;
	}

	public List<Object> listdetail(String changclassid, String time, String activeids, String showdate, Long stime,
			Long ftime) {
		List list = null;
		String oids = AdmHelper.getActiveStatisticsService().getOids(activeids);
		oids = oids.substring(0, oids.length() - 1);
		String active = "";
		String activemaster = "";
		if ("doc".equals(changclassid)) {
			active = " adm_activedocument";
			activemaster = "adm_activedocumentmaster";
		}
		if ("set".equals(changclassid)) {
			active = "adm_activeset";
			activemaster = "adm_activesetmaster";
		}
		if (("order".equals(changclassid))) {
			active = "adm_activeorder";
			activemaster = "adm_activeordermaster";
		}
		String activetablesql = "select a.* from " + active + " a,PT_OM_CONTROLBRANCH CB2," + activemaster + " B, ";
		activetablesql += "( SELECT PCB.MASTERID, PCB.MASTERCLASSID,MAX(MODIFYSTAMP) MODIFYSTAMP FROM PT_OM_CONTROLBRANCH PCB ";
		activetablesql += "GROUP BY PCB.MASTERID, PCB.MASTERCLASSID) CB1 where  A.CONTROLBRANCHID = CB2.INNERID AND A.CONTROLBRANCHCLASSID = CB2.CLASSID ";
		activetablesql += "AND B.INNERID = CB1.MASTERID AND B.CLASSID = CB1.MASTERCLASSID AND CB1.MODIFYSTAMP = CB2.MODIFYSTAMP and A.MASTERID = B.INNERID ";
		activetablesql += "AND A.MASTERCLASSID = B.CLASSID  and  A.STATENAME  not in ('已删除') ";
		String times = time.substring(0, 4);
		String begintime = "";
		String endtime = "";
		if ("YEAR".equals(showdate)) {
			times = time.substring(0, 4);
			begintime = times + "-01-01";
			endtime = times + "-12-31";
		}
		if ("QUARTER".equals(showdate)) {
			int q = Integer.parseInt(time.substring(time.indexOf("年") + 1, time.indexOf("季")));
			if (q == 1) {
				begintime = times + "-01-01";
				endtime = times + "-03-31";
			} else if (q == 2) {
				begintime = times + "-04-01"; 
				endtime = times + "-06-30";
			} else if (q == 3) {
				begintime = times + "-07-01";
				endtime = times + "-09-30";
			} else {
				begintime = times + "-10-01";
				endtime = times + "-12-31";
			}

		}
		if ("MONTH".equals(showdate)) {
			String mm = time.substring(time.indexOf("年") + 1, time.indexOf("月"));
			int m = Integer.parseInt(mm);

			if (m < 10) {
				mm = "0" + mm;
			}
			if (Integer.parseInt(times) / 4 == 0 && m == 2) {
				begintime = times + "-" + mm + "-01";
				endtime = times + "-" + mm + "-28";
			}
			if (Integer.parseInt(times) / 4 != 0 && m == 2) {
				begintime = times + "-" + mm + "-01";
				endtime = times + "-" + mm + "-29";
			}
			if (m == 1 || m == 3 || m == 5 || m == 7 || m == 8 || m == 10 || m == 12) {
				begintime = times + "-" + mm + "-01";
				endtime = times + "-" + mm + "-31";
			}
			if (m == 4 || m == 6 || m == 9 || m == 11) {
				begintime = times + "-" + mm + "-01";
				endtime = times + "-" + mm + "-30";

			}
		}

		Long btime = DateTimeUtil.parseDateLong(begintime, DateTimeUtil.DATE_YYYYMMDD);
		Long etime = DateTimeUtil.parseDateLong(endtime, DateTimeUtil.DATE_YYYYMMDD);
		if (stime > btime) {
			btime = stime;
		}
		if (ftime < etime) {
			etime = ftime;
		}
		etime=etime+86399000L;
		String sql = "";
		if ("doc".equals(changclassid)) {
			sql = "select a.* from  (" + activetablesql
					+ ")  a, adm_activedocumentmaster b where  a.masterid=b.innerid and a.masterclassid=b.classid";
			sql += " and a.createtime between  " + btime + " and " + etime
					+ " and a.folderClassId || ':' || a.folderId in(" + oids + ")";
			list = Helper.getPersistService().query(sql, ActiveDocument.class);
		}
		if ("set".equals(changclassid)) {
			sql = "select a.* from (" + activetablesql
					+ ")  a, adm_activesetmaster b where a.masterid=b.innerid and a.masterclassid=b.classid ";
			sql += " and  a.createtime between  " + btime + " and " + etime
					+ " and a.folderClassId || ':' || a.folderId in(" + oids + ") ";
			list = Helper.getPersistService().query(sql, ActiveSet.class);
		}
		if ("order".equals(changclassid)) {
			sql = "select a.*  from (" + activetablesql
					+ ")  a ,adm_activeordermaster b where a.masterid=b.innerid and a.masterclassid=b.classid ";
			sql += " and  createtime between  " + btime + " and " + etime
					+ " and a.folderClassId || ':' || a.folderId in(" + oids + ") ";
			list = Helper.getPersistService().query(sql, ActiveOrder.class);
		}
		return list;

	}

	@Override
	public List<ActiveQuerySchema> findActiveQuerySchemaList(String oid) {
		String sql = "select * from adm_QuerySchema where innerid=? ";
		List<ActiveQuerySchema> list = Helper.getPersistService().query(sql, ActiveQuerySchema.class,oid);
		return list;
	}

	@Override
	public List<Map<String, Object>> listMap(List<String> keys, List<Object> resultdate) {
		List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
		for (Object obj : resultdate) {
			Map<String, Object> mapObject = Helper.getTypeManager().format((PTFactor) obj, keys, false);
			mapList.add(mapObject);
		}
		return mapList;
	};
}
