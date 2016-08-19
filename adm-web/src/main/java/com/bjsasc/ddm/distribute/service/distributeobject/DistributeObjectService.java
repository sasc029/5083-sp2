package com.bjsasc.ddm.distribute.service.distributeobject;

import java.util.List;
import java.util.Map;

import com.bjsasc.ddm.distribute.model.distributeobject.DistributeObject;
import com.bjsasc.ddm.distribute.model.distributeorder.DistributeOrder;
import com.bjsasc.ddm.distribute.model.distributeorderobjectlink.DistributeOrderObjectLink;
import com.bjsasc.platform.filecomponent.model.PtFileItemBean;
import com.bjsasc.plm.core.attachment.FileHolder;
import com.bjsasc.plm.core.doc.Document;
import com.bjsasc.plm.core.part.Part;
import com.bjsasc.plm.core.persist.model.Persistable;

/**
 * �ַ��������ӿڡ�
 * 
 * @author gengancong 2013-2-22
 */
public interface DistributeObjectService {

	/**
	 * ͨ���ַ���ID��ȡ�ַ��������ݡ�
	 * 
	 * @param distributeOrderOid String
	 * @return List
	 */
	public List<DistributeObject> getDistributeObjectsByDistributeOrderOid(String distributeOrderOid);
	public void getAllDistributeObject(String distributeOrderOid);
	
	/**
	 * ͨ����������ID��ȡ�ַ��������ݡ�
	 * 
	 * @param distributeOrderOid String
	 * @return List
	 */
	public List<DistributeObject> getDistributeObjectsByDistributeElecTaskOid(String distributeElecTaskOid);
	
	/**
	 * ͨ��ֽ��ǩ������ID��ȡ�ַ��������ݡ�
	 * 
	 * @param distributeOrderOid String
	 * @return List
	 */
	public List<DistributeObject> getDistributeObjectsByDistributePaperSignTaskOid(String distributePaperSignTaskOid);
	
	/**
	 * ͨ��ֽ�������oid����ȡ������ص����ݣ���ֽ����������ǻ����������񣬻����Ƿַ�����
	 * 
	 * @param distributePaperOid ֽ�������oid
	 * @return ������ص������б�
	 * @modify Sun Zongqing
	 * @modifyDate 2014/7/2
	 */
	public List<DistributeObject> getDistributeObjectsByDistributePaper(String distributePaperOid);
	/**
	 * ͨ���ַ�����ԴOID��ȡ�ַ��������ݡ�
	 * 
	 * @param dataOid �ַ�����ԴOID
	 * @return List<DistributeObject>
	 */
	public List<DistributeObject> getDistributeObjectsByDataOid(String dataOid);
	
	/**
	 * ͨ���ַ�����ԴIID��ȡ�ַ��������ݡ�
	 * 
	 * @param dataIid �ַ�����ԴOID
	 * @return List<DistributeObject>
	 */
	public List<DistributeObject> getDistributeObjectsByDataIid(String dataIid);
	/**
	 * �����ַ����ݶ���
	 * 
	 * @param dis DistributeObject
	 */
	public void createDistributeObject(DistributeObject dis);

	/**
	 * ���·ַ����ݶ���
	 * 
	 * @param dis DistributeObject
	 */
	public void updateDistributeObject(DistributeObject dis);

	/**
	 * ���·ַ����ݶ���
	 * 
	 * @param oid String
	 * @param dataInnerId String
	 * @param dataClassId String
	 * @param dataFrom String
	 */
	public void updateDistributeObject(String oid, String dataInnerId, String dataClassId, String dataFrom);

	/**
	 * �h���ַ����ݶ���
	 * 
	 * @param dis DistributeObject
	 */
	public void deleteDistributeObject(DistributeObject dis);

	/**
	 * �h���ַ����ݶ���
	 * 
	 * @param distributeOrderOid String
	 * @param oids String
	 */
	public void deleteDistributeObjectByOids(String distributeOrderOid, String oids);

	/**
	 * ͨ���ַ���OID,�ַ�����OID�h���ַ����ݶ���
	 * 
	 * @param distributeOrderOid String
	 * @param distributeObjectOid String
	 */
	public void deleteDistributeObjectByOid(String distributeOrderOid, String distributeObjectOid);

	/**
	 * �����ַ����ݶ���
	 * 
	 * @return DistributeObject
	 */
	public DistributeObject newDistributeObject();

	/**
	 * �������ŵ���ַ�����link����
	 * 
	 * @param disObject DistributeObject
	 * @param disOrder DistributeOrder 
	 * @param isMaster String
	 * @return String 
	 */
	public String createDistributeOrderObjectLink(DistributeOrder disOrder, DistributeObject disObject, String isMaster);

	/**
	 * �Ƿ��ת�����ܿ��С�
	 * 
	 * @param objOid String
	 * @return String 
	 * 				"true,yes"
	 * 				"false,��������״̬Ϊ��state���Ķ���������뷢�ŵ���"
	 */
	public String isTransferControlling(String objOid);

	/**
	 * ɾ�����ŵ�
	 * 
	 * @param distributeOrderOid
	 */
	public String deleteDistributeOrder(String distributeOrderOid);

	/**
	 * ���½����̶�
	 * 
	 * @param linkOids
	 * @param disUrgent
	 * @return
	 */
	public void updateDistributeObjectLink(String linkOids, String disUrgent);

	/**
	 * �����깤����
	 * 
	 * @param linkOids
	 * @param deadLinkDate
	 */
	public void updateDistributedeadLinkDate(String linkOids, String deadLineDate);
	
	/**
	 * �����깤����
	 * 
	 * @param linkOids
	 * @param deadLinkDate
	 */
	public List<DistributeObject> getDistributeObjectReturnDetail(String objOid, String taskOid);

	/**
	 * ͨ���ַ���OID�ͷַ�����ԴOID��ȡ�ַ��������ݡ�
	 * 
	 * @param ordOid String
	 * @param dataOid String
	 * @return List
	 */
	public List<DistributeObject> getDistributeObjectsByOrdOidAndDataOid(String ordOid, String dataOid);

	/**
	 * ���ŵ����������Ƿ��Ǵ��ڡ�
	 * 
	 * @param disOrdObjLinkOid String
	 * @return boolean
	 * 					true ��
	 * 					false ���� 
	 */
	public boolean getMasterLink(String disOrdObjLinkOid);

	/**
	 * ���·ַ���ʽ
	 * 
	 * @param linkOids
	 * @param disStyle
	 * @return
	 */
	public void updateDistributeObjectLinkByDisStyle(String linkOids, String disStyle);

	/**
	 * ͨ���ַ���OIDȡ�÷ַ�����Դ��ActiveBase�������ݵ� ����Դ����
	 * 
	 * @param ordOid String
	 * @param dataOid String
	 * @return List
	 */
	public List<Persistable> getActiveBaseDataSourceByDisOrdOid(String distributeOrderOid);
	
	/**
	 * ͨ����������OIDȡ�����ҵ������ĵ��������ĸ��ڵ㣩
	 * 
	 * @param taskOid
	 * @return
	 */
	public List<Persistable> getAllElecTaskDataRootObject(String taskOid);
	
	/**
	 * ͨ����������OIDȡ�����ҵ������ĵ���������
	 * 
	 * @param taskOid
	 * @return
	 */
	public List<Persistable> getAllElecTaskDataObject(String taskOid);
	
	
	/**
	 * �ҽӣ���Ӳ�����ҵ�����link
	 * 
	 * @param partOid
	 * @param objectOids
	 */
	public void addDistributeDataObjectLink(String partOid, String objectOids);
	
	/**
	 * ���ݷ��ŵ�OID��ȡ��طַ����ݼ���
	 * 
	 * @param orderOid
	 * @return
	 */
	public List<DistributeObject> getDistributeObjectList(DistributeOrder order);
	
	/**
	 * ��ȡ�ַ��������ʷ�ַ�������ʷ�б�
	 * 
	 * @param oid String
	 */
	public List<DistributeOrderObjectLink> historyDistributeObjectList(String oid);
	
	/**
	 * ����Ĭ�Ϸַ�����
	 * 
	 * @param distributeOrderOid ���ŵ�OID
	 * @param distributeObjectOid �ַ�����OID
	 */
	public void setMaster(String distributeOrderOid, String distributeObjectOid);
	
	/**
	 * ��ȡҪ���ŵĶ���İ汾��
	 * @param dataOid ��������Դ����OID
	 * @return finalVersion
	 */
	public String getCurrentDisObjectVersion(String dataOid);

	/**
	 * @author kangyanfei
	 *
	 * ��ȡ�ַ��������ʷ�ַ�������ʷ�б�
	 * 
	 * @param oid String
	 * 			�ַ�����OID
	 * @param orderType
	 * 			�������ͣ����ŵ���0���ŵ���1�������ŵ���2���շŵ���3���ٷŵ���
	 */
	public List<DistributeOrderObjectLink> historyDistributeObjectList(String oid, String orderType);
	
	/**
	 * ͨ�����ŵ�oid��ȡ�ĵ������ļ�ʵ��
	 * @param oid ���ŵ�oid
	 * @return
	 */
	public Map<FileHolder, List<PtFileItemBean>> getDocumentAndFileByOrder(String oid);
	
	
	
	/**
	 * ͨ���ַ���Ϣ��oid��ȡ�ַ�����
	 * @param distributeInfoOids  �ַ���Ϣ���Ƽ���
	 * @param disOrderoid ���ŵ�OID 
	 * @return DistributeObject ��Ӧ�ַ���Ϣ�ķַ����ݶ���
	 */
	public List<DistributeObject> getDistributeObjByInfoOid(List<String> distributeInfoNames,String disOrderoid
			);
	
	/**
	 * ������շ��ҽ��ýӿڣ�ԭ��approveorderservice�У������˷�����ɾ����Ǩ������ʹ��
	 * @param part
	 * @param isDescripeDoc
	 * @return
	 */
	public List<Document> getLastestPartLinkDocument(Part part, boolean isDescripeDoc);
}