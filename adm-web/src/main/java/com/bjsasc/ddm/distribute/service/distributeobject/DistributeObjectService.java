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
 * 分发对象服务接口。
 * 
 * @author gengancong 2013-2-22
 */
public interface DistributeObjectService {

	/**
	 * 通过分发单ID获取分发对象数据。
	 * 
	 * @param distributeOrderOid String
	 * @return List
	 */
	public List<DistributeObject> getDistributeObjectsByDistributeOrderOid(String distributeOrderOid);
	public void getAllDistributeObject(String distributeOrderOid);
	
	/**
	 * 通过电子任务ID获取分发对象数据。
	 * 
	 * @param distributeOrderOid String
	 * @return List
	 */
	public List<DistributeObject> getDistributeObjectsByDistributeElecTaskOid(String distributeElecTaskOid);
	
	/**
	 * 通过纸质签收任务ID获取分发对象数据。
	 * 
	 * @param distributeOrderOid String
	 * @return List
	 */
	public List<DistributeObject> getDistributeObjectsByDistributePaperSignTaskOid(String distributePaperSignTaskOid);
	
	/**
	 * 通过纸质任务的oid，获取任务相关的数据，该纸质任务可以是回收销毁任务，或者是分发任务
	 * 
	 * @param distributePaperOid 纸质任务的oid
	 * @return 任务相关的数据列表
	 * @modify Sun Zongqing
	 * @modifyDate 2014/7/2
	 */
	public List<DistributeObject> getDistributeObjectsByDistributePaper(String distributePaperOid);
	/**
	 * 通过分发数据源OID获取分发对象数据。
	 * 
	 * @param dataOid 分发数据源OID
	 * @return List<DistributeObject>
	 */
	public List<DistributeObject> getDistributeObjectsByDataOid(String dataOid);
	
	/**
	 * 通过分发数据源IID获取分发对象数据。
	 * 
	 * @param dataIid 分发数据源OID
	 * @return List<DistributeObject>
	 */
	public List<DistributeObject> getDistributeObjectsByDataIid(String dataIid);
	/**
	 * 创建分发数据对象。
	 * 
	 * @param dis DistributeObject
	 */
	public void createDistributeObject(DistributeObject dis);

	/**
	 * 更新分发数据对象。
	 * 
	 * @param dis DistributeObject
	 */
	public void updateDistributeObject(DistributeObject dis);

	/**
	 * 更新分发数据对象。
	 * 
	 * @param oid String
	 * @param dataInnerId String
	 * @param dataClassId String
	 * @param dataFrom String
	 */
	public void updateDistributeObject(String oid, String dataInnerId, String dataClassId, String dataFrom);

	/**
	 * 刪除分发数据对象。
	 * 
	 * @param dis DistributeObject
	 */
	public void deleteDistributeObject(DistributeObject dis);

	/**
	 * 刪除分发数据对象。
	 * 
	 * @param distributeOrderOid String
	 * @param oids String
	 */
	public void deleteDistributeObjectByOids(String distributeOrderOid, String oids);

	/**
	 * 通过分发单OID,分发数据OID刪除分发数据对象。
	 * 
	 * @param distributeOrderOid String
	 * @param distributeObjectOid String
	 */
	public void deleteDistributeObjectByOid(String distributeOrderOid, String distributeObjectOid);

	/**
	 * 创建分发数据对象。
	 * 
	 * @return DistributeObject
	 */
	public DistributeObject newDistributeObject();

	/**
	 * 创建发放单与分发数据link对象。
	 * 
	 * @param disObject DistributeObject
	 * @param disOrder DistributeOrder 
	 * @param isMaster String
	 * @return String 
	 */
	public String createDistributeOrderObjectLink(DistributeOrder disOrder, DistributeObject disObject, String isMaster);

	/**
	 * 是否可转换且受控中。
	 * 
	 * @param objOid String
	 * @return String 
	 * 				"true,yes"
	 * 				"false,生命周期状态为【state】的对象不允许加入发放单！"
	 */
	public String isTransferControlling(String objOid);

	/**
	 * 删除发放单
	 * 
	 * @param distributeOrderOid
	 */
	public String deleteDistributeOrder(String distributeOrderOid);

	/**
	 * 更新紧急程度
	 * 
	 * @param linkOids
	 * @param disUrgent
	 * @return
	 */
	public void updateDistributeObjectLink(String linkOids, String disUrgent);

	/**
	 * 更新完工期限
	 * 
	 * @param linkOids
	 * @param deadLinkDate
	 */
	public void updateDistributedeadLinkDate(String linkOids, String deadLineDate);
	
	/**
	 * 更新完工期限
	 * 
	 * @param linkOids
	 * @param deadLinkDate
	 */
	public List<DistributeObject> getDistributeObjectReturnDetail(String objOid, String taskOid);

	/**
	 * 通过分发单OID和分发数据源OID获取分发对象数据。
	 * 
	 * @param ordOid String
	 * @param dataOid String
	 * @return List
	 */
	public List<DistributeObject> getDistributeObjectsByOrdOidAndDataOid(String ordOid, String dataOid);

	/**
	 * 发放单的主对象是否是存在。
	 * 
	 * @param disOrdObjLinkOid String
	 * @return boolean
	 * 					true 是
	 * 					false 不是 
	 */
	public boolean getMasterLink(String disOrdObjLinkOid);

	/**
	 * 更新分发方式
	 * 
	 * @param linkOids
	 * @param disStyle
	 * @return
	 */
	public void updateDistributeObjectLinkByDisStyle(String linkOids, String disStyle);

	/**
	 * 通过分发单OID取得分发数据源是ActiveBase现行数据的 数据源对象。
	 * 
	 * @param ordOid String
	 * @param dataOid String
	 * @return List
	 */
	public List<Persistable> getActiveBaseDataSourceByDisOrdOid(String distributeOrderOid);
	
	/**
	 * 通过电子任务OID取得相关业务对象（文档，部件的跟节点）
	 * 
	 * @param taskOid
	 * @return
	 */
	public List<Persistable> getAllElecTaskDataRootObject(String taskOid);
	
	/**
	 * 通过电子任务OID取得相关业务对象（文档，部件）
	 * 
	 * @param taskOid
	 * @return
	 */
	public List<Persistable> getAllElecTaskDataObject(String taskOid);
	
	
	/**
	 * 挂接，添加部件与业务对象link
	 * 
	 * @param partOid
	 * @param objectOids
	 */
	public void addDistributeDataObjectLink(String partOid, String objectOids);
	
	/**
	 * 根据发放单OID获取相关分发数据集合
	 * 
	 * @param orderOid
	 * @return
	 */
	public List<DistributeObject> getDistributeObjectList(DistributeOrder order);
	
	/**
	 * 获取分发对象的历史分发单的历史列表
	 * 
	 * @param oid String
	 */
	public List<DistributeOrderObjectLink> historyDistributeObjectList(String oid);
	
	/**
	 * 设置默认分发对象
	 * 
	 * @param distributeOrderOid 发放单OID
	 * @param distributeObjectOid 分发数据OID
	 */
	public void setMaster(String distributeOrderOid, String distributeObjectOid);
	
	/**
	 * 获取要发放的对象的版本号
	 * @param dataOid 发放数据源对象OID
	 * @return finalVersion
	 */
	public String getCurrentDisObjectVersion(String dataOid);

	/**
	 * @author kangyanfei
	 *
	 * 获取分发对象的历史分发单的历史列表
	 * 
	 * @param oid String
	 * 			分发数据OID
	 * @param orderType
	 * 			单据类型：发放单（0发放单，1补发发放单，2回收放单，3销毁放单）
	 */
	public List<DistributeOrderObjectLink> historyDistributeObjectList(String oid, String orderType);
	
	/**
	 * 通过发放单oid获取文档对象及文件实体
	 * @param oid 发放单oid
	 * @return
	 */
	public Map<FileHolder, List<PtFileItemBean>> getDocumentAndFileByOrder(String oid);
	
	
	
	/**
	 * 通过分发信息的oid获取分发数据
	 * @param distributeInfoOids  分发信息名称集合
	 * @param disOrderoid 发放单OID 
	 * @return DistributeObject 对应分发信息的分发数据对象
	 */
	public List<DistributeObject> getDistributeObjByInfoOid(List<String> distributeInfoNames,String disOrderoid
			);
	
	/**
	 * 跨域接收方挂接用接口，原在approveorderservice中，后改造此方法被删掉，迁到这里使用
	 * @param part
	 * @param isDescripeDoc
	 * @return
	 */
	public List<Document> getLastestPartLinkDocument(Part part, boolean isDescripeDoc);
}