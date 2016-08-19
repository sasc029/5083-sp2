package com.bjsasc.adm.active.service.activechangeservice;

import java.util.List;
import java.util.Map;

import com.bjsasc.adm.active.model.query.ActiveQuerySchema;

/**
 * ������Ϣͳ��ͼ����ӿڡ�
 * 
 * @author tudong 2013-6-03
 */
public interface ActiveChangeService {
	/**
	 * ȡ��������Ϣ����
	 * @return List<Map<String,Object>>
	 * */
	public List<Map<String, Object>> getStatResultByGroup(String activename, String activeModelId, Long begintime,
			Long endtime, String showtype);

	/**
	 * �޸�����ӵķ���
	 * @return ActiveQuerySchema
	 * */
	public ActiveQuerySchema modifyQuerySchema(String oid, String activename, String schemaName, int defaultSchema,
			String begintime, String endtime, String activeModelId, String activeModelName, String showdate);

	/**
	 * ��ӷ���
	 * @return ActiveQuerySchema
	 * */
	public ActiveQuerySchema createQuerySchema(String activename, String schemaName, int defaultSchema,
			String begintime, String endtime, String activeModelId, String activeModelName, String showdate);

	/**
	 * ��ѯ���з���
	 * @return List<ActiveQuerySchema>
	 * */
	public List<ActiveQuerySchema> getActiveQuerySchemaList();

	/**
	 * ����oid��ѯ���з���
	 * @return List<ActiveQuerySchema>
	 * */
	public List<ActiveQuerySchema> findActiveQuerySchemaList(String oid);

	/**
	 * ���ͼƬ�õ����е�������Ϣ
	 * @return List<Map<String,Object>>
	 * */
	public List<Object> listdetail(String changclassid, String time, String activeids, String showdate, Long btime,
			Long etime);

	public List<Map<String, Object>> listMap(List<String> keys, List<Object> resultdate);
}
