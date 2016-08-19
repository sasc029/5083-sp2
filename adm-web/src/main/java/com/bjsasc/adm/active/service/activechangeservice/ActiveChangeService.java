package com.bjsasc.adm.active.service.activechangeservice;

import java.util.List;
import java.util.Map;

import com.bjsasc.adm.active.model.query.ActiveQuerySchema;

/**
 * 现行信息统计图服务接口。
 * 
 * @author tudong 2013-6-03
 */
public interface ActiveChangeService {
	/**
	 * 取得现行信息条数
	 * @return List<Map<String,Object>>
	 * */
	public List<Map<String, Object>> getStatResultByGroup(String activename, String activeModelId, Long begintime,
			Long endtime, String showtype);

	/**
	 * 修改已添加的方案
	 * @return ActiveQuerySchema
	 * */
	public ActiveQuerySchema modifyQuerySchema(String oid, String activename, String schemaName, int defaultSchema,
			String begintime, String endtime, String activeModelId, String activeModelName, String showdate);

	/**
	 * 添加方案
	 * @return ActiveQuerySchema
	 * */
	public ActiveQuerySchema createQuerySchema(String activename, String schemaName, int defaultSchema,
			String begintime, String endtime, String activeModelId, String activeModelName, String showdate);

	/**
	 * 查询所有方案
	 * @return List<ActiveQuerySchema>
	 * */
	public List<ActiveQuerySchema> getActiveQuerySchemaList();

	/**
	 * 根据oid查询所有方案
	 * @return List<ActiveQuerySchema>
	 * */
	public List<ActiveQuerySchema> findActiveQuerySchemaList(String oid);

	/**
	 * 点击图片得到所有的现行信息
	 * @return List<Map<String,Object>>
	 * */
	public List<Object> listdetail(String changclassid, String time, String activeids, String showdate, Long btime,
			Long etime);

	public List<Map<String, Object>> listMap(List<String> keys, List<Object> resultdate);
}
