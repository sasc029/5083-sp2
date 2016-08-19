package com.bjsasc.ddm.distribute.action.distributepapertask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.bjsasc.ddm.common.ConstUtil;
import com.bjsasc.ddm.distribute.action.AbstractAction;
import com.bjsasc.ddm.distribute.helper.DistributeHelper;
import com.bjsasc.ddm.distribute.model.distributepapertask.DistributePaperTask;
import com.bjsasc.ddm.distribute.service.distributeorderobjectlink.DistributeOrderObjectLinkService;
import com.bjsasc.ddm.distribute.service.distributepapertask.DistributePaperTaskService;
import com.bjsasc.plm.Helper;
import com.bjsasc.plm.KeyS;
import com.bjsasc.plm.core.context.model.Context;
import com.bjsasc.plm.core.persist.model.Persistable;
import com.bjsasc.plm.core.security.audit.AuditLogHelper;
import com.bjsasc.plm.core.session.SessionHelper;
import com.bjsasc.plm.core.util.StringUtil;
import com.bjsasc.plm.grid.GridHelper;
import com.bjsasc.plm.grid.data.GridDataUtil;
import com.bjsasc.plm.type.TypeService;

/**
 * ֽ������Actionʵ����
 * 
 * @author guowei 2013-2-22
 * 
 */
public class DistributePaperTaskAction extends AbstractAction {

	/** serialVersionUID */
	private static final long serialVersionUID = 1485443385203981845L;
	private static final Logger LOG = Logger.getLogger(DistributePaperTaskAction.class);
	private final DistributePaperTaskService service = DistributeHelper.getDistributePaperTaskService();

	/**
	 * ���Ź���--�ӹ����˵�
	 * ��������ҳ������һ����ʾ��
	 * ȡ��ֽ��������������Ϊ���ӹ��С������ݡ�
	 * /ddm/distributepapertask/listDistributePaperTasks.jsp
	 * 
	 * @return strutsҳ�����
	 */	
	public String getAllDistributePaperTask() {
		try {
			String lc = ConstUtil.LC_PROCESSING.getName();
			List<DistributePaperTask> listDis = service.getAllDistributePaperTaskByAuth(lc);
			LOG.debug("ȡ��ֽ������ " + getDataSize(listDis) + " ������������״̬��" + lc);
			String spot = ConstUtil.SPOT_LISTDISTRIBUTEPAPERTASKS;
			// ������֤Ȩ��
			List<Map<String, Object>> listDatas = checkPermissionAF(listDis, spot, spot);
			// ������
			GridDataUtil.prepareRowObjects(listDatas, spot);
		} catch (Exception ex) {
			error(ex);
		}
		return "listPage";
	}
	/**
	 * �ӹ������´�ӡҳ��������������ť�Լ����ݳ�ʼ��ʱ����
	 * @return
	 */
	public String getSearchPrintProcessingDistributePaperTask() {
		String ret="";
		String stateName = ConstUtil.LC_PROCESSING.getName();
		try {
			//ȡ�üӹ���
			String lc = ConstUtil.LC_PROCESSING.getName();
			//�������е������ֶ�
			String keywords=("%"+getParameter("keywords")+"%").trim();
			String flag1=getParameter("flag1");
			//�������
			if(""!=flag1&&flag1!=null&&"2".equals(flag1)){
				List<DistributePaperTask> listDis = service.getSearchProcessingDistributePaperTaskByAuth(stateName,keywords);
				LOG.debug("ȡ��ֽ������ " + getDataSize(listDis) + " ������������״̬��" + lc);
				//String spot = ConstUtil.SPOT_LISTDISTRIBUTEPAPERTASKS;
				String spot=ConstUtil.SPOT_LISTDISTRIBUTEPAPERTASKSPRINT;
				// ������֤Ȩ��
				List<Map<String, Object>> listDatas = checkPermissionAF(listDis, spot, spot);
				// ������
				GridDataUtil.prepareRowObjects(listDatas, spot);
				result.put("success","true");
				result.put("flag", listDatas.size()+"");
				mapToSimpleJson();
				ret=OUTPUTDATA;
			}else{
				//ҳ���ʼ����
				long currentTime = System.currentTimeMillis();
				long monthtime = 30*24*60*60*1000L;
				long time = currentTime-monthtime;
				List<DistributePaperTask> listDis = service.getSearchProcessingDistributePaperTaskByTime(time,stateName,currentTime);
				LOG.debug("ȡ��ֽ������ " + getDataSize(listDis) + " ������������״̬��" + lc);
				//String spot = ConstUtil.SPOT_LISTDISTRIBUTEPAPERTASKS;
				String spot=ConstUtil.SPOT_LISTDISTRIBUTEPAPERTASKSPRINT;
				// ������֤Ȩ��
				List<Map<String, Object>> listDatas = checkPermissionAF(listDis, spot, spot);
				// ������
				GridDataUtil.prepareRowObjects(listDatas, spot);
				ret="getPrintProcessingDistributePaperTask";
			}
			
		} catch (Exception ex) {
			jsonError(ex);
		}
		return ret;
		
	}
	
	
	protected String getParameter(String key) {
		String parameter = request.getParameter(key);
		String encoding = StringUtil.encoding(parameter);
		return encoding;
	}


	/**
	 * ���Ź���--�ӹ����˵�
	 * �����������ҳ������һ����ʾ��
	 * ȡ��ֽ��������������Ϊ��������ɡ������ݡ�
	 * /ddm/distributepapertask/listDuplicateSubmit.jsp
	 * 
	 * @return strutsҳ�����
	 */	
	public String getAllDuplicateSubmitTask() {
		try {
			String lc = ConstUtil.LC_DUPLICATED.getName();
			List<DistributePaperTask> listDis = service.getAllDistributePaperTaskByAuth(lc);
			LOG.debug("ȡ��ֽ������ " + getDataSize(listDis) + " ��,��������״̬��" + lc);

			String spot = ConstUtil.SPOT_LISTDISTRIBUTEDUPLICATESUBMIT;
			
			// ������֤Ȩ��
			List<Map<String, Object>> listDatas = checkPermissionAF(listDis, spot, spot);
			
			// ������
			GridDataUtil.prepareRowObjects(listDatas, spot);
			
		} catch (Exception ex) {
			error(ex);
		}
		return "listDuplicateSubmit";
	}

	/**
	 * ���Ź���--�ӹ����˵�
	 * ���˻�����ҳ������һ����ʾ��
	 * ȡ��ֽ��������������Ϊ���ӹ����˻ء������ݡ�
	 * /ddm/distributepapertask/listDuplicateReturn.jsp
	 * 
	 * @return strutsҳ�����
	 */	
	public String getAllDistributeReturnTask() {
		try {
			String lc = ConstUtil.LC_PROCESSING_BACKOFF.getName();
			List<DistributePaperTask> listDis = service.getAllDistributePaperTaskReturnByAuth(lc);
			LOG.debug("ȡ��ֽ������ " + getDataSize(listDis) + " ��,��������״̬��" + lc);

			String spot = ConstUtil.SOPT_LISTDISTRIBUTEPAPERTASKRETURN;
			
			// ������֤Ȩ��
			List<Object> checkList = checkPermission(listDis, spot, spot);
			
			List<String> keys = GridHelper.getService().getMyLatestGridViewColumnIds(spot, spot);
			
			// ��ʽ����ʾ����
			TypeService typeManager = Helper.getTypeManager();
			Map<String, Object> mapAll = null;
			for (Object obj : checkList) {
				if (obj == null) {
					continue;
				}
				mapAll = new HashMap<String, Object>();
				DistributePaperTask target = (DistributePaperTask)obj;
				Map<String, Object> mainMap = typeManager.format(target, keys, false);
				Map<String, Object> returnMap = typeManager.format(target.getReturnReason(), keys, false);
				mapAll.putAll(returnMap);
				mapAll.putAll(mainMap);
				mapAll.put(KeyS.ACCESS, true);
				listMap.add(mapAll);
			}

			GridDataUtil.prepareRowObjectMaps(listMap, spot);
			
		} catch (Exception ex) {
			error(ex);
		}
		return "listDuplicateReturn";
	}

	/**
	 * ����ֽ���������
	 */
	public String createDistributePaperTask() {
		try {
			String number = request.getParameter("number");// ���
			String name = request.getParameter("name");// ����
			String note = request.getParameter("note");// ��ע

			service.createDistributePaperTask(number, name, note);

			success();
		} catch (Exception ex) {
			error(ex);
		}
		mapToSimpleJson();
		return OUTPUTDATA;
	}


	/**
	 * ������ϸ�б�
	 * 
	 * @return
	 */
	public String getDistributePaperTaskReturnDetail() {
		try {
			String oid = request.getParameter(KeyS.OID);
			String flag = request.getParameter("flag");
			String stateName = "";
			if ("duplicateReturn".equals(flag)) {
				stateName = ConstUtil.LC_PROCESSING_BACKOFF.getName();
			} else if ("paperTaskReturn".equals(flag)) {
				stateName = ConstUtil.LC_DUPLICATE_BACKOFF.getName();
			}
			List<DistributePaperTask> listDis = service.getDistributeTaskReturnDetail(oid, stateName);

			// ��ʽ����ʾ����
			TypeService typeManager = Helper.getTypeManager();
			for (DistributePaperTask target : listDis) {
				if (target == null) {
					continue;
				}
				Map<String, Object> mapAll = new HashMap<String, Object>();
				Map<String, Object> mainMap = typeManager.format(target);
				Map<String, Object> returnMap = typeManager.format(target.getReturnReason());
				mapAll.putAll(returnMap);
				mapAll.putAll(mainMap);
				listMap.add(mapAll);
			}
			LOG.debug("ȡ�÷��ŵ������� " + getDataSize() + " ������������״̬��" + ConstUtil.LC_BACKOFF.getName());
			// ������
			GridDataUtil.prepareRowObjectMaps(listMap, ConstUtil.SOPT_LISTDISTRIBUTEPAPERTASKRETURNDETAIL);
			
		} catch (Exception ex) {
			error(ex);
		}
		return "listDisPaperTaskReturnDetail";
	}

	/**
	 * ɾ��ֽ������
	 * 
	 * @return
	 */
	public String deleteDistributePaperTask() {
		try {
			// ��ȡֽ�������OIDS
			String oids = request.getParameter(KeyS.OIDS);
			service.deleteDistributePaperTaskProperty(oids);

			getAllDistributePaperTask();

			success();
		} catch (Exception ex) {
			error(ex);
		}
		mapToSimpleJson();
		return OUTPUTDATA;

	}
	
	public String listDistributeObjectFiles() {
		try {
			String oid = request.getParameter(KeyS.OID);
			String contextPath = request.getContextPath();
			listMap = service.listDistributeObjectFiles(oid,contextPath);
			LOG.debug("ȡ�ø�������" + getDataSize() + " ��");
		} catch (Exception ex) {
			error(ex);
		}
		listToJson();
		return OUTPUTDATA;
	}

	/**
	 * Excel������á�
	 */
	public void paperTaskExcel() {
		String oid = request.getParameter(KeyS.OID);
		LOG.debug("ִ��paperTaskExcel() ҳ��������� : OID = " + oid);
		Persistable obj = Helper.getPersistService().getObject(oid);
		DistributePaperTask dis = (DistributePaperTask) obj;

		String filename = dis.getNumber();
		try {
			// ȡ�ö���
			HSSFWorkbook wBook = service.getExcelObject(oid);

			// Excel�ļ������
			exportExcel(wBook, ConstUtil.PROCESS_TITLE_NAME);
			
			//��ӡ�ӹ�����¼��־
			
			Context context= dis.getContextInfo().getContext();
			List<String> objects = new ArrayList<String>();
			objects.add(SessionHelper.getService().getUser().getName());//�û���
			objects.add("��ӡ�ӹ���");
			objects.add(dis.getNumber());
			int level=1;
			String logType="module";
			String objName=dis.getName();
			int objectSecurity=0;
			String moduleSource="���Ź���";
			String objectType="ֽ������";
			String operation="��ӡ�ӹ���";
			String messageId="ddm.log.exportExcel";
			AuditLogHelper.getService().addLog( level, logType, context, 
					dis.getInnerId(), dis.getClassId(), objName, objectSecurity, 
					moduleSource, objectType, operation, messageId, objects);
			
			
		} catch (Exception ex) {
			LOG.debug("Excel�����쳣", ex);
			throw new RuntimeException(ex);
		}
	}
	
	/**
	 * "�߲����ŵ�ģ��"Excel�������
	 */
	public void paperTaskExcelForDept7(){

		String oid = request.getParameter(KeyS.OID);
		LOG.debug("ִ��paperTaskExcel() ҳ��������� : OID = " + oid);
		try {
			// ȡ�ö���
			HSSFWorkbook wBook = service.getExcelObjectForDept7(oid);
			// Excel�ļ������
			exportExcel(wBook, ConstUtil.DOCUMENT_COPY);	
	    } catch (Exception ex) {
			LOG.debug("Excel�����쳣", ex);
			throw new RuntimeException(ex);
		}	
	}

}
