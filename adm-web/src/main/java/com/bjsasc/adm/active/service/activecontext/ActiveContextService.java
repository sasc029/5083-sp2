package com.bjsasc.adm.active.service.activecontext;

import java.util.List;

import com.bjsasc.adm.active.model.activecontext.ActiveContext;
import com.bjsasc.plm.core.context.ContextService;

/**
 * �����ļ�����ӿڡ�
 * 
 * @author gengancong 2013-07-01
 */
public interface ActiveContextService extends ContextService {

	/**
	 * �½�һ�� ActiveContext(�����ļ�������)���� 
	 * @return �����ļ������Ķ���
	 */
	public ActiveContext newActiveContext();

	/**
	 * ��ѯ�����ļ�����
	 * @return ActiveContext
	 */
	public ActiveContext getActiveContext();

	/**
	 * ���������ļ������ģ���������ļ��������Ѿ����ڣ��򲻻ᴴ���ڶ���
	 * @param context �������������ļ�������
	 * @return �����ļ�������
	 */
	public ActiveContext createActiveContext();

	/**
	 * ��ѯ�����ļ�����
	 * @return List<ActiveContext>
	 */
	public List<ActiveContext> getAllActiveContext();
}
