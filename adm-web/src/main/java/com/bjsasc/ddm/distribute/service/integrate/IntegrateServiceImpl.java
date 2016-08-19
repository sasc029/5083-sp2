package com.bjsasc.ddm.distribute.service.integrate;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.security.Key;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.activation.DataHandler;

import org.apache.axiom.attachments.ByteArrayDataSource;
import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.axiom.om.OMText;
import org.apache.axis2.AxisFault;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.databinding.utils.ConverterUtil;
import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import com.bjsasc.ddm.common.FileCryptUtil;
import com.bjsasc.ddm.distribute.helper.DistributeHelper;
import com.bjsasc.ddm.distribute.model.distributeinfo.DistributeInfo;
import com.bjsasc.ddm.distribute.model.distributeobject.DistributeObject;
import com.bjsasc.ddm.distribute.service.distributecommonconfig.DistributeCommonConfigService;
import com.bjsasc.platform.filecomponent.model.PtFileHolderBean;
import com.bjsasc.platform.filecomponent.model.PtFileItemBean;
import com.bjsasc.platform.filecomponent.service.PtFileService;
import com.bjsasc.platform.filecomponent.util.PtFileCptServiceProvider;
import com.bjsasc.platform.filecomponent.util.PtFileCptUtil;
import com.bjsasc.platform.objectmodel.business.persist.PersistUtil;
import com.bjsasc.platform.webframework.bean.FilterParam;
import com.bjsasc.platform.webframework.util.DateUtil;
import com.bjsasc.platform.workflow.engine.model.instance.WorkItem;
import com.bjsasc.plm.Helper;
import com.bjsasc.plm.core.approve.Approved;
import com.bjsasc.plm.core.attachment.AttachHelper;
import com.bjsasc.plm.core.attachment.FileHolder;
import com.bjsasc.plm.core.change.Change;
import com.bjsasc.plm.core.change.ChangeBaseMaster;
import com.bjsasc.plm.core.change.ChangeService;
import com.bjsasc.plm.core.change.ECO;
import com.bjsasc.plm.core.persist.PersistHelper;
import com.bjsasc.plm.core.persist.model.Persistable;
import com.bjsasc.plm.core.suit.ATSuit;
import com.bjsasc.plm.core.suit.SuitHelper;
import com.bjsasc.plm.core.suit.Suited;
import com.bjsasc.plm.core.type.ATObject;
import com.bjsasc.plm.core.type.TypeHelper;
import com.bjsasc.plm.core.util.FileUtil;
import com.bjsasc.plm.core.vc.model.Versioned;
import com.bjsasc.plm.core.workflow.WorkFlowUtil;
import com.bjsasc.plm.type.TypeManager;
import com.bjsasc.plm.type.attr.Attr;
import com.bjsasc.plm.view.ViewUtil;
import com.cascc.avidm.util.SysConfig;

/**
 * 嫺�֮��ϵͳ���ɷ���ʵ����
 * 
 * @author zhangguoqiang 2014-07-09
 */
public class IntegrateServiceImpl implements IntegrateService {

	//���Ź���ͨ�����÷�����
	DistributeCommonConfigService distributeCommonConfigService = DistributeHelper
			.getDistributeCommonConfigService();
	
	private static final String STR_ENCODE = "GB2312";
	private static final String STR_RTN_SUCCESS = "S0001";
	private static final String STR_SUFFIX = "/";
	private static final Logger LOG = Logger
			.getLogger(IntegrateServiceImpl.class);

	@Override
	public void exportXml(String oid) {
		//���ݷ��ŵ�Oid��ѯ�ַ�����
		List<DistributeObject> listObj = DistributeHelper
				.getDistributeObjectService()
				.getDistributeObjectsByDistributeOrderOid(oid);

		//���ŵ�innerId Ϊ�ļ������ƴ���,·��ΪD���£����ŵ�innerIdΪ���Ƶ��ļ���
		String export_ServerTransferPath = distributeCommonConfigService
				.getConfigValueByConfigId("Export_ServerTransferPath");
		if(export_ServerTransferPath == null || export_ServerTransferPath.isEmpty()){
			//��ȡWeb�������ϴ��ļ���ʱ·��
			export_ServerTransferPath = SysConfig.getTempDir();
		}
		//�÷��ŵ���innerid��Ϊ�ļ�����
		String transferDirPath = "";
		if (export_ServerTransferPath.endsWith(STR_SUFFIX)) {
			transferDirPath = export_ServerTransferPath
					+ Helper.getInnerId(oid);
		} else {
			transferDirPath = export_ServerTransferPath + STR_SUFFIX
					+ Helper.getInnerId(oid);
		}
		
		File transferDir = new File(transferDirPath);
		if (transferDir.exists() && transferDir.isDirectory()) {
			//ѭ��ɾ���ļ��м����ļ�
			FileUtil.deleteDirectory(transferDirPath);
		} else {
			transferDir.mkdir();
		}
		//�������зַ�����
		for (DistributeObject disObject : listObj) {
			//ȡ�÷ַ����ݵĿɳ־û�����
			Persistable target = PersistHelper.getService().getObject(
					disObject.getDataClassId() + ":"
							+ disObject.getDataInnerId());
			//�������ֵ��ȡ�÷����ͻ��汣��һ�£�
			Map<String, Object> attrValues = TypeManager.getManager()
					.formatWithSource(target, "detail");
			
			//������������չ���ԣ��������չ����ֵ
			if(target instanceof ATObject){
				ATObject containerIncluded = (ATObject) target;
				Map<String, Object> attrMap = containerIncluded.getExtAttrs();
				if (attrMap != null) {
					Set<String> keySet = attrMap.keySet();
					for(Iterator<String> it = keySet.iterator(); it.hasNext();){
						String key = it.next();
						String value = PersistUtil.getAttrDisplayValue(target, key);
						attrValues.put(key, value);
					}
				}
			}
			
			List<String> groups = TypeManager.getManager().getAttrGroups(
					target.getClassId(), "detail");
			//�÷ַ����ݵĸ��������innerid(�����԰汾����)
			//����masterid(���԰汾����)��Ϊ�ļ����������ظ�
			String iid = getIID(target);
			String fileName = transferDirPath + STR_SUFFIX + iid + ".xml";
			File file = newXmlFile(fileName);

			Document doc;
			SAXBuilder builder = new SAXBuilder();
			try {
				//ȡ�ø��ڵ�
				doc = builder.build(file);
				Element rootElement = doc.getRootElement();
				//ȡ�÷ַ����ݶ��������
				String type = TypeHelper.getService()
						.getType(disObject.getDataClassId()).getName();
				//�������������prop����ѭ����Ҫ��TYPE��ʾ����������ԭMAP��TYPE��value���ԣ�����дmap�е�TYPEֵ
				attrValues.put("TYPE", type);
				
				//����������������ĵ��ı��
				List<ECO> ecoList = new ArrayList<ECO>();
				ecoList = Helper.getChangeService().getRelatedECOList(target);
				String ecoNo = "";
				if(ecoList.size()>0){
					ECO eco = ecoList.get(0);
					ecoNo = ((ChangeBaseMaster)eco.getMaster()).getNumber();
				}
				attrValues.put("ECONumber", ecoNo);
				//����DocSource�ڵ�
				Element resource = getDocSourceNote(type);
				rootElement.addContent(resource);

				Element propElement = null;
				Element propertyElement = null;
				Element propEle = null;

				//����propnameΪIID�Ľڵ�
				propElement = getIIDNote(target);
				rootElement.addContent(propElement);

				//���԰汾����Ķ�������propnameΪINNERID�Ľڵ�
				propElement = getInnerIdNote(target);
				if (propElement != null) {
					rootElement.addContent(propElement);
				}

				//ѭ�����ɻ������Խڵ�
				for (int i = 0; i < groups.size(); i++) {
					String group = groups.get(i);

					//��������֯�����Լ�
					Map<String, List<Attr>> groupAttrs = TypeManager
							.getManager().getTypeAttrsByGroup(
									target.getClassId(), "detail");
					List<Attr> attrs = ViewUtil.filterByView(target,
							groupAttrs.get(group));
					
					//����������������ĵ��ı��
					if(i==0){
						Attr ECOattr = new Attr();
						ECOattr.setId("ECONumber");
						ECOattr.setName("�������ĵ����");
						attrs.add(ECOattr);
					}
					
					//���ɽڵ�
					for (Attr attr : attrs) {
						Object attrValue = attrValues.get(attr.getId());
						if (attrValue == null) {
							attrValue = "";
						}
						//��������ֶ�
						if (attr.getId().equals("LIFECYCLE_STATE")
								|| attr.getId().equals("WORK_STATE")
								|| attr.getId().equals("LOCK")
								|| attr.getId().equals("PROTECT")) {
							continue;
						}
						propEle = makePropNote(attr.getName(), attr.getId(),
								attrValue.toString());
						rootElement.addContent(propEle);
					}
				}
				//�׶������нڵ����
				if (target instanceof ATSuit) {//�׶���
					ATSuit suit = (ATSuit) target;
					//ȡ�����ڶ���
					List<Suited> suitedList = SuitHelper.getService()
							.getSuitItems(suit);
					if (suitedList.size() > 0) {
						propEle = new Element("papers");
						for (int i = 0; i < suitedList.size(); i++) {
							Suited suited = suitedList.get(i);
							String filetype = TypeHelper.getService()
									.getType(suited.getClassId()).getName();
							propertyElement = new Element("paperonlyid");
							propertyElement.setAttribute("filetype", filetype);
							propertyElement.setText(getIID(suited));
							propEle.addContent(propertyElement);
						}
						rootElement.addContent(propEle);
					}
				}
				//���ĵ����нڵ����
				if (target instanceof ECO) {
					ChangeService changeSerive = Helper.getChangeService();
					//���ĺ����
					List<Persistable> changeList = changeSerive
							.getChangedAfterObjList((Change) target);

					if (changeList.size() > 0) {
						Element changinfo = new Element("changeinfo");
						propEle = new Element("changepaper");
						for (int i = 0; i < changeList.size(); i++) {
							Persistable p = changeList.get(i);
							//����IID�ڵ�
							propElement = new Element("IID");
							propElement.setText(getIID(p));
							propEle.addContent(propElement);
						}
						changinfo.addContent(propEle);
						rootElement.addContent(changinfo);
					}
				}
				//�ַ����ݰ��������ļ������ذ����ĵ����ļ�
				List<PtFileItemBean> fileList = new ArrayList<PtFileItemBean>();
				if (target instanceof FileHolder) {
					fileList = AttachHelper.getAttachService().getAllFile(
							(FileHolder) target);
					
					if(target instanceof Approved){
						//�������������ϴ��ĸ���
						String proInstInnerIds = WorkFlowUtil.getObjectProcessInstanceId((Approved)target);
						List<WorkItem> lastRoundWorkItems = WorkFlowUtil.getWorkItemsByProcessInstanceIdAndRounds(proInstInnerIds, -1);
						
						String refType = "pt_workflow";
						String storageId = "pt_workflow";
						
						for(WorkItem wItem : lastRoundWorkItems){
							// ���service
							PtFileService fileService = PtFileCptServiceProvider.getFileService();
							// ����PtFileHolder
							PtFileHolderBean testFileHolderBean = new PtFileHolderBean();
							testFileHolderBean.setRefId(proInstInnerIds);
							testFileHolderBean.setRefType(refType);
							testFileHolderBean.setStorageId(storageId);
							// ����б�
							FilterParam filterA = new FilterParam();
							String paramStr = "[paramIn][=][" + wItem.id + "]";
						 	if(paramStr!=null && paramStr.trim().length()>0){
					 			//���ӹ���
					 			paramStr = PtFileCptUtil.decodeStr(paramStr);
					 			PtFileCptUtil.paramToFilterParam(paramStr, filterA);
						 	}

							List<PtFileItemBean> ptFlieList = fileService.getFileInfo(testFileHolderBean, filterA);
							if(ptFlieList!=null && ptFlieList.size()>0){
								fileList.addAll(ptFlieList);
							}
						}
					}
					//�����ļ�����
					if (fileList != null && fileList.size() > 0) {
						for (PtFileItemBean ptFileItemBean : fileList) {
							String ptFileName = ptFileItemBean.getFileName();
							InputStream inputStream = AttachHelper
									.getAttachService().getInputStream(
											(FileHolder) target,
											ptFileItemBean.getInnerId());

							//�����ļ����������Ψһ��ʶ��Ϊ����·��
							String elcFileDirPath = transferDirPath
									+ STR_SUFFIX + iid;
							if (inputStream != null) {
								boolean result_flag = false;
								//ȡ�÷���ͨ������[�Ƿ���ַܷ����ݵĵ����ļ�]������
								String is_EncryptElcFile = distributeCommonConfigService
										.getConfigValueByConfigId("Is_EncryptElcFile");
								if("true".equals(is_EncryptElcFile)){
									result_flag= downloadElcFileAndCrypt(inputStream, 
											elcFileDirPath, ptFileName);
								}else{
									result_flag= downloadElcFile(inputStream, 
											elcFileDirPath, ptFileName);
								}
								
								if(!result_flag){
									LOG.error("���ص����ļ������ļ���:" + ptFileName);
								}
							}
						}
					}
				}
				//��ʽ���ļ�Ϊxml��ʽ�������ļ�
				boolean result_flag = saveXmlFile(doc, file);
				if(!result_flag){
					LOG.error("��ʽ���ļ�Ϊxml��ʽ�������ļ������ļ���:" + file.getName());
				}

				//�ַ���Ϣ�ĵ���
				boolean result_flag1 = exportDisInfo(oid, disObject.getOid(), iid, transferDirPath, type);
				if(!result_flag1){
					LOG.error("�ַ���Ϣ�����ַ�����Ψһ��ʶ:" + iid);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		//ȡ�÷��Ź�����Ƿ���Ҫѹ������
		String is_NeedZip = distributeCommonConfigService
				.getConfigValueByConfigId("Is_NeedZip");
		String directoryZipName = transferDirPath + ".zip";
		if ("true".equalsIgnoreCase(is_NeedZip)) {
			//ѹ�������ļ���
			FileUtil.zip(transferDirPath, directoryZipName, STR_ENCODE);
			//ɾ��ѹ����Դ�ļ���
			if (transferDir.exists() && transferDir.isDirectory()) {
				//ѭ��ɾ���ļ��м����ļ�
				FileUtil.deleteDirectory(transferDirPath);
			}
		}
		//ȡ�ô��䷽ʽ
		String transferMethod = distributeCommonConfigService
				.getConfigValueByConfigId("TransferMethod");
		if ("webservice".equalsIgnoreCase(transferMethod)) {

			//����嫺�֮��webservice�����ļ�
			//ȡ�������������
			String webServiceUrl = distributeCommonConfigService
					.getConfigValueByConfigId("WebServiceUrl");
			String targetNamespace = distributeCommonConfigService
					.getConfigValueByConfigId("TargetNamespace");
			String method = distributeCommonConfigService
					.getConfigValueByConfigId("MethodName");
			try {
				//����WebService
				ServiceClient serviceClient = new ServiceClient();
				Options opts = new Options();	
				// ָ��������WebService��URL		
				EndpointReference targetEPR = new EndpointReference(		
						webServiceUrl);
				opts.setTo(targetEPR);

				String soapAction = "";
				if (targetNamespace.endsWith(STR_SUFFIX)) {
					soapAction = targetNamespace + method;
				} else {
					soapAction = targetNamespace + STR_SUFFIX
							+ method;
				}
				opts.setAction(soapAction);
				serviceClient.setOptions(opts);
				
				if ("true".equalsIgnoreCase(is_NeedZip)) {
					File directoryZip = new File(directoryZipName);
					//����webservice�����ļ���zip����
					boolean result_flag = transferFileByWebService(directoryZip, "", serviceClient,
							targetNamespace, method);
					if(!result_flag){
						LOG.error("�ļ���������ļ���:" + directoryZipName);
					}
					if (directoryZip.exists()) {
						//ɾ��zip��
						FileUtil.deleteDirectory(directoryZipName);
					}
				}else{
					File directory = new File(transferDirPath);
					//�ļ��е�����´����ļ����ڵ������ļ������ļ����ڵ������ļ�
					File[] files = directory.listFiles();
					if (files != null) {
						for (File file : files) {
							if (file.isFile()) {
								//����webservice�����ļ����ַ����ݣ��ַ���Ϣ�ļ���
								boolean result_flag = transferFileByWebService(file, "", serviceClient,
										targetNamespace, method);
								if(!result_flag){
									LOG.error("�ļ���������ļ���:" + file.getName());
								}
							} else {
								File[] subFiles = file.listFiles();
								if (subFiles != null) {
									for (File subFile : subFiles) {
										//����webservice�����ļ�������ԭ���ļ���
										boolean result_flag = transferFileByWebService(subFile,
												file.getName(), serviceClient,	targetNamespace, method);
										if(!result_flag){
											LOG.error("�ļ���������ļ���:" + subFile.getName());
										}
									}
								}
							}
						}
					}
					//ɾ���ļ��м����ļ�
					if (directory.exists() && directory.isDirectory()) {
						//ѭ��ɾ���ļ��м����ļ�
						FileUtil.deleteDirectory(transferDirPath);
					}
				}
			} catch (AxisFault e) {
				//TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	/**
	 * ����ַ���Ϣxml�ļ�
	 * 
	 * @param 
	 *         orderOid   ���ŵ�Oid 
	 *         objOid �ַ�����Oid
	 *         iid �ַ������ڶ����Ψһ��ʶ
	 *         dirPath �ļ�����·��
	 */
	public boolean exportDisInfo(String orderOid, String objOid, String iid,
			String dirPath, String type) {
		boolean rtn_flag = false;
		List<DistributeInfo> disInfos = DistributeHelper
				.getDistributeInfoService().getDistributeInfosByOid(orderOid,
						objOid);
		if (disInfos.size() > 0) {
			//�ַ����ݶ����Ψһ��ʶ + _�ַ���.xml ��Ϊ�ַ������ļ���
			String fileName = dirPath + STR_SUFFIX + iid + "_�ַ���.xml";
			File file = newXmlFile(fileName);

			Document doc;
			SAXBuilder builder = new SAXBuilder();
			try {
				//ȡ�ø��ڵ�
				doc = builder.build(file);
				Element rootElement = doc.getRootElement();
				Element propElement = null;
				Element propertyElement = null;

				//��ͷ��Ϣ
				Element resource = getDocSourceNote(type + "�ַ���");
				rootElement.addContent(resource);

				Element objiidElement = new Element("objectIID");
				objiidElement.setText(iid);
				rootElement.addContent(objiidElement);
				Element booknoElement = new Element("BookNo");
				booknoElement.setText("");
				rootElement.addContent(booknoElement);

				Element sendinfoElement = new Element("sendinfo");
				for (int i = 0; i < disInfos.size(); i++) {
					DistributeInfo disInfo = disInfos.get(i);

					propElement = new Element("sendobj");
					propertyElement = new Element("name");//����
					propertyElement.setText(disInfo.getDisInfoName());
					propElement.addContent(propertyElement);

					propertyElement = new Element("count");//�ַ�����
					propertyElement.setText(String.valueOf(disInfo
							.getDisInfoNum()));
					propElement.addContent(propertyElement);

					propertyElement = new Element("notes");//��ע
					propertyElement.setText(disInfo.getNote());
					propElement.addContent(propertyElement);

					propertyElement = new Element("SendDocType");//�ַ���ʽ
					String disMediaTypeName = disInfo.getDisMediaTypeName();
					propertyElement.setText(disMediaTypeName);
					propElement.addContent(propertyElement);

					propertyElement = new Element("Distype");//��������
					String disTypeName = disInfo.getDisTypeName();
					propertyElement.setText(disTypeName);
					propElement.addContent(propertyElement);
					
					propertyElement = new Element("SendTime");//����ʱ��
					Long sendTime = disInfo.getSendTime();
					String strSendTime = "";
					if(sendTime > 0 ){
						strSendTime = DateUtil.toDate(disInfo.getSendTime());
					}
					propertyElement.setText(strSendTime);
					propElement.addContent(propertyElement);

					propertyElement = new Element("DestroyNum");//���ٷ���
					propertyElement.setText(String.valueOf(disInfo
							.getDestroyNum()));
					propElement.addContent(propertyElement);

					propertyElement = new Element("SendBookNo");//���ı��
					propertyElement.setText(disInfo.getInnerId());
					propElement.addContent(propertyElement);

					sendinfoElement.addContent(propElement);
				}
				rootElement.addContent(sendinfoElement);

				//��ʽ���ļ�Ϊxml��ʽ�������ļ�
				boolean result_flag = saveXmlFile(doc, file);
				if(!result_flag){
					LOG.error("��ʽ���ļ�Ϊxml��ʽ�������ļ������ļ���:" + fileName);
					return rtn_flag;
				}
			} catch (Exception e) {
				//TODO Auto-generated catch block
				e.printStackTrace();
				return rtn_flag;
			}
		}
		rtn_flag = true;
		return rtn_flag;
	}

	/**
	 * ����DocSource�ڵ�
	 * 
	 * @param DistributeObject �ַ����ݶ���
	 * @return Element DocSource�ڵ����
	 */
	public Element getDocSourceNote(String type) {
		Element docSource = new Element("DocSource");

		String export_Company = distributeCommonConfigService
				.getConfigValueByConfigId("Export_Company");
		Element resource1 = new Element("company");
		resource1.setText(export_Company);
		docSource.addContent(resource1);
		Element resource2 = new Element("system");
		String export_System = distributeCommonConfigService
				.getConfigValueByConfigId("Export_System");
		resource2.setText(export_System);
		docSource.addContent(resource2);
		Element resource3 = new Element("type");
		resource3.setText(type);
		docSource.addContent(resource3);
		return docSource;
	}

	/**
	 * ����prop�ڵ�
	 * 
	 * @param 
	 * 		porpName �ֶ���ʾ����
	 *  	porpSign �ֶα�ʶ
	 *  	porpValue �ֶ�ֵ
	 * @return Element propNote�ڵ����
	 */
	public Element makePropNote(String porpName, String porpSign,
			String porpValue) {
		Element propNote = new Element("prop");
		Element propnameNote = new Element("propname");
		propnameNote.setText(porpName);
		propNote.addContent(propnameNote);
		Element propsignNote = new Element("propsign");
		propsignNote.setText(porpSign);
		propNote.addContent(propsignNote);
		Element propvalueNote = new Element("propvalue");
		propvalueNote.setText(porpValue);
		propNote.addContent(propvalueNote);
		return propNote;
	}

	/**
	 * ����propnameΪIID�Ľڵ�(prop�ڵ�)
	 * 
	 * @param Persistable �ɳ־û�����
	 * @return Element �ڵ����
	 */
	public Element getIIDNote(Persistable p) {
		String iid = "";
		//���԰汾����Ķ���
		if (p instanceof Versioned) {
			Versioned version = (Versioned) p;
			iid = version.getMasterRef().getInnerId();
		} else {
			iid = p.getInnerId();
		}
		Element propNote = makePropNote("�ڲ���ʶ", "IID", iid);
		return propNote;
	}

	/**
	 * ����propnameΪINNERID�Ľڵ�(prop�ڵ�)
	 * 
	 * @param Persistable �ɳ־û�����
	 * @return Element �ڵ����
	 */
	public Element getInnerIdNote(Persistable p) {
		Element propNote = null;
		//���԰汾����Ķ���
		if (p instanceof Versioned) {
			propNote = makePropNote("A5�ڲ���ʶ", "INNERID", p.getInnerId());
		}
		return propNote;
	}

	/**
	 * ���ش����ڵ��xml�ļ�����
	 * 
	 * @param 
	 * 		fileNameWithPath �ļ�·��
	 * 		rootNoteName ���ڵ�����
	 * @return file �ļ�����
	 */
	public File newXmlFile(String fileNameWithPath) {
		File file = new File(fileNameWithPath);
		if (file.exists()) {
			file.delete();
		}
		PrintWriter out0 = null;
		try {
			file.createNewFile();
			out0 = new PrintWriter(new FileOutputStream(file));
			out0.println("<?xml version=\"1.0\" encoding=\"" + STR_ENCODE
					+ "\"?>");
			out0.println("<archman>");
			out0.println("</archman>");
		} catch (Exception e) {
			//TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (null != out0) {
				try {
					out0.close();
				} catch (Exception e1) {
					//TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
		return file;
	}

	/**
	 * ����Xml�ļ�
	 * 
	 * @param 
	 * 		doc Document����
	 * 		file File����
	 */
	public boolean saveXmlFile(Document doc, File file) {
		boolean rtn_flag = false;
		OutputStream out = null;
		Format ff = Format.getPrettyFormat();
		ff.setEncoding(STR_ENCODE);
		XMLOutputter outputter = new XMLOutputter(ff);
		try {
			out = new BufferedOutputStream(new FileOutputStream(file));
			outputter.output(doc, out);
			rtn_flag = true;
			return rtn_flag;
		} catch (Exception e) {
			//TODO Auto-generated catch block
			e.printStackTrace();
			return rtn_flag;
		} finally {
			try {
				out.close();
			} catch (Exception e) {
				//TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * ����IID�����԰汾����ķ���masterId,���ⷵ��innerId
	 * 
	 * @param 
	 * 		Persistable �ɳ־û�����
	 * @return iid Ψһ��ʶ
	 */
	public String getIID(Persistable p) {
		String iid = "";
		//���԰汾����Ķ���
		if (p instanceof Versioned) {
			Versioned version = (Versioned) p;
			iid = version.getMasterRef().getInnerId();
		} else {
			iid = p.getInnerId();
		}
		return iid;
	}

	/**
	 * ���ص����ļ������浽ָ��·��
	 * 
	 * @param 
	 * 		inputStream �����ļ���
	 * 		saveDirPath ����·��
	 * 		fileName �ļ���
	 */
	public boolean downloadElcFile(InputStream inputStream, String saveDirPath,
			String fileName) {
		boolean rtn_flag = false;
		int byteRead = 0;
		FileOutputStream fileOutputStream = null;
		try {
			File saveDir = new File(saveDirPath);
			if (!saveDir.exists()) {
				saveDir.mkdir();
			}

			File saveFile = new File(saveDirPath + STR_SUFFIX + fileName);
			fileOutputStream = new FileOutputStream(saveFile);
			byte[] buffer = new byte[1024];
			while (-1 != (byteRead = inputStream.read(buffer))) {
				fileOutputStream.write(buffer, 0, byteRead);
			}
			rtn_flag = true;
			return rtn_flag;
		} catch (Exception e) {
			//TODO Auto-generated catch block
			e.printStackTrace();
			return rtn_flag;
		} finally {
			try {
				fileOutputStream.close();
			} catch (Exception e) {
				//TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * ���ص����ļ������ܺ󱣴浽ָ��·��
	 * 
	 * @param 
	 * 		inputStream �����ļ���
	 * 		saveDirPath ����·��
	 * 		fileName �ļ���
	 */
	public boolean downloadElcFileAndCrypt(InputStream inputStream, String saveDirPath,
			String fileName) {
		boolean rtn_flag = false;
		int byteRead = 0;
		FileOutputStream fileOutputStream = null;
		OutputStream outputStream = null;
		try {
			File saveDir = new File(saveDirPath);
			if (!saveDir.exists()) {
				saveDir.mkdir();
			}

			File saveFile = new File(saveDirPath + STR_SUFFIX + fileName);

//			//ȡ�÷���ͨ������[��Կ�洢�ļ���]������
//			String encryptElcKeyFileName = distributeCommonConfigService
//					.getConfigValueByConfigId("EncryptElcKeyFileName");

			String encryptElcKeyFileName = "plm/ddm/desKeyFile/hanhaiElecFileKey.dat";
			String keyFileName ="";
			if (SysConfig.getAvidmHome().endsWith(STR_SUFFIX)) {
				keyFileName = SysConfig.getAvidmHome() + encryptElcKeyFileName;
			}else{
				keyFileName = SysConfig.getAvidmHome() + STR_SUFFIX + encryptElcKeyFileName;
			}
			//ȡ�ü�����Կ
			Key key = FileCryptUtil.getKeyFromByteKeyFile(keyFileName);
			
			fileOutputStream = new FileOutputStream(saveFile);
			//���ļ���������м���
			outputStream = FileCryptUtil.encryptFileOutputStream(key, fileOutputStream);
			byte[] buffer = new byte[1024];
			while (-1 != (byteRead = inputStream.read(buffer))) {
				outputStream.write(buffer, 0, byteRead);
			}
			rtn_flag = true;
			return rtn_flag;
		} catch (Exception e) {
			//TODO Auto-generated catch block
			e.printStackTrace();
			return rtn_flag;
		} finally {
			try {
				fileOutputStream.close();
				outputStream.close();
			} catch (Exception e) {
				//TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * ����webservice�����ļ�
	 * 
	 * @param 
	 * 		file �����ļ�
	 * 		originalFileDir ����ԭ�������Ķ����Ψһ��ʶ
	 * 		serviceClient RPCServiceClient
	 * 		opAddEntry QName
	 *      classes Class<?>[] ����ֵ����
	 */
	public boolean transferFileByWebService(File file, String originalFileDir,
			ServiceClient serviceClient, String targetNamespace, String method) {
		boolean rtn_flag = false;
		FileInputStream fis = null;
		try {
			//�ļ�����
			String fileName = file.getName();
			//�����ļ���32λMD5ֵ
			String fileMd5 = FileUtil.getMD5(file);
			boolean isLastBlock = false;

			//buffer��С64K
			int bufferSize = 64 * 1024;
			long offset = 0;

			fis = new FileInputStream(file);
			//�ֿ��ϴ����ļ��ֽ���
			byte[] buffer = new byte[bufferSize];
			byte[] bufferLast = null;
			int length = -1;
			long fileLength = file.length();
			long readLength = 0;

			OMFactory factory = OMAbstractFactory.getOMFactory();
			OMNamespace omNS = factory.createOMNamespace(targetNamespace, "");
			
			while ((length = fis.read(buffer)) != -1) {
				readLength += length;
				if (readLength >= fileLength) {
					isLastBlock = true;
					
					bufferLast = new byte[length];
					for(int i=0;i<length;i++){
						bufferLast[i]=buffer[i];
					}
				}

				OMElement payLoad = factory.createOMElement(method, omNS);
				//fileName �ļ����ƣ����������������ͬ���ļ��򱻸���
				OMElement omeFileName = factory.createOMElement("fileName", omNS);
				omeFileName.setText(fileName);
				payLoad.addChild(omeFileName);
				
				//originalFileDir ����ԭ�������Ķ����Ψһ��ʶ������ʱ�����ļ�����
				OMElement omeOriginalFileDir = factory.createOMElement("originalFileDir", omNS);
				omeOriginalFileDir.setText(originalFileDir);
				payLoad.addChild(omeOriginalFileDir);
				
				//buffer �ֿ��ϴ����ļ��ֽ���������ÿ����СΪ64*1024�ֽ�
				OMElement omeBuffer = factory.createOMElement("buffer", omNS);
	            //����������תΪOMText
				ByteArrayDataSource dataSource = null;
				if(isLastBlock){
					dataSource= new ByteArrayDataSource(bufferLast);
				}else{
					dataSource= new ByteArrayDataSource(buffer);
				}
	            DataHandler fileHandler= new DataHandler(dataSource);
	            OMText textData = factory.createOMText(fileHandler, true);
				omeBuffer.addChild(textData);
				payLoad.addChild(omeBuffer);
				
				//offset ��ǰ�ļ��������ʼ�ļ�����ƫ��
				OMElement omeOffset = factory.createOMElement("offset", omNS);
				omeOffset.setText(ConverterUtil.convertToString(offset));
				payLoad.addChild(omeOffset);
				
				//isLastBlock �Ƿ������һ���ļ�����
				OMElement omeIsLastBlock = factory.createOMElement("isLastBlock", omNS);
				omeIsLastBlock.setText(ConverterUtil.convertToString(isLastBlock));
				payLoad.addChild(omeIsLastBlock);
				
				//fileMd5 �����ļ���32λMD5ֵ
				OMElement omeFileMd5 = factory.createOMElement("fileMd5", omNS);
				omeFileMd5.setText(fileMd5);
				payLoad.addChild(omeFileMd5);

				OMElement result = serviceClient.sendReceive(payLoad);
				String str_result = result.getFirstElement().getText();
				 //�жϷ���ֵ������ɹ����log��Ϣ
				// 嫺�֮��webservice�ɹ�ʱ����S0001
				// ʧ�ܷ���E��ͷ���ַ�����E0000-MD5��֤ʧ��;E0001�ڲ��쳣��
				if (!STR_RTN_SUCCESS.equals(str_result)) {
					//���log��Ϣ
					LOG.error("����嫺�֮��WebService��������ֵ:" + str_result);
					LOG.error("fileName:" + fileName);
					LOG.error("originalFileDir:" + originalFileDir);
					LOG.error("buffer����:" + length);
					LOG.error("offset:" + offset);
					LOG.error("isLastBlock:" + isLastBlock);
					LOG.error("fileMd5:" + fileMd5);
					return rtn_flag;
				}
				offset += bufferSize;
			}
			rtn_flag = true;
			return rtn_flag;
		} catch (Exception e) {
			//TODO Auto-generated catch block
			e.printStackTrace();
			return rtn_flag;
		} finally {
			try {
				fis.close();
			} catch (IOException ex) {
				//TODO Auto-generated catch block
				ex.printStackTrace();
			}
		}
	}

}
