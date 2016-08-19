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
 * 瀚海之星系统集成服务实现类
 * 
 * @author zhangguoqiang 2014-07-09
 */
public class IntegrateServiceImpl implements IntegrateService {

	//发放管理通用配置服务类
	DistributeCommonConfigService distributeCommonConfigService = DistributeHelper
			.getDistributeCommonConfigService();
	
	private static final String STR_ENCODE = "GB2312";
	private static final String STR_RTN_SUCCESS = "S0001";
	private static final String STR_SUFFIX = "/";
	private static final Logger LOG = Logger
			.getLogger(IntegrateServiceImpl.class);

	@Override
	public void exportXml(String oid) {
		//根据发放单Oid查询分发数据
		List<DistributeObject> listObj = DistributeHelper
				.getDistributeObjectService()
				.getDistributeObjectsByDistributeOrderOid(oid);

		//发放单innerId 为文件夹名称创建,路径为D盘下，发放单innerId为名称的文件夹
		String export_ServerTransferPath = distributeCommonConfigService
				.getConfigValueByConfigId("Export_ServerTransferPath");
		if(export_ServerTransferPath == null || export_ServerTransferPath.isEmpty()){
			//获取Web服务器上传文件临时路径
			export_ServerTransferPath = SysConfig.getTempDir();
		}
		//用发放单的innerid作为文件夹名
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
			//循环删除文件夹及子文件
			FileUtil.deleteDirectory(transferDirPath);
		} else {
			transferDir.mkdir();
		}
		//遍历所有分发数据
		for (DistributeObject disObject : listObj) {
			//取得分发数据的可持久化对象
			Persistable target = PersistHelper.getService().getObject(
					disObject.getDataClassId() + ":"
							+ disObject.getDataInnerId());
			//获得属性值（取得方法和画面保持一致）
			Map<String, Object> attrValues = TypeManager.getManager()
					.formatWithSource(target, "detail");
			
			//如果对象包含扩展属性，则加载扩展属性值
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
			//用分发数据的各个对象的innerid(不可以版本管理)
			//或者masterid(可以版本管理)作为文件名，避免重复
			String iid = getIID(target);
			String fileName = transferDirPath + STR_SUFFIX + iid + ".xml";
			File file = newXmlFile(fileName);

			Document doc;
			SAXBuilder builder = new SAXBuilder();
			try {
				//取得根节点
				doc = builder.build(file);
				Element rootElement = doc.getRootElement();
				//取得分发数据对象的类型
				String type = TypeHelper.getService()
						.getType(disObject.getDataClassId()).getName();
				//新需求，在下面的prop属性循环中要将TYPE显示出来，由于原MAP中TYPE的value不对，故重写map中的TYPE值
				attrValues.put("TYPE", type);
				
				//新需求，添加所属更改单的编号
				List<ECO> ecoList = new ArrayList<ECO>();
				ecoList = Helper.getChangeService().getRelatedECOList(target);
				String ecoNo = "";
				if(ecoList.size()>0){
					ECO eco = ecoList.get(0);
					ecoNo = ((ChangeBaseMaster)eco.getMaster()).getNumber();
				}
				attrValues.put("ECONumber", ecoNo);
				//生成DocSource节点
				Element resource = getDocSourceNote(type);
				rootElement.addContent(resource);

				Element propElement = null;
				Element propertyElement = null;
				Element propEle = null;

				//生成propname为IID的节点
				propElement = getIIDNote(target);
				rootElement.addContent(propElement);

				//可以版本管理的对象，生成propname为INNERID的节点
				propElement = getInnerIdNote(target);
				if (propElement != null) {
					rootElement.addContent(propElement);
				}

				//循环生成基本属性节点
				for (int i = 0; i < groups.size(); i++) {
					String group = groups.get(i);

					//按分组组织的属性集
					Map<String, List<Attr>> groupAttrs = TypeManager
							.getManager().getTypeAttrsByGroup(
									target.getClassId(), "detail");
					List<Attr> attrs = ViewUtil.filterByView(target,
							groupAttrs.get(group));
					
					//新需求，添加所属更改单的编号
					if(i==0){
						Attr ECOattr = new Attr();
						ECOattr.setId("ECONumber");
						ECOattr.setName("所属更改单编号");
						attrs.add(ECOattr);
					}
					
					//生成节点
					for (Attr attr : attrs) {
						Object attrValue = attrValues.get(attr.getId());
						if (attrValue == null) {
							attrValue = "";
						}
						//不输出的字段
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
				//套对象特有节点输出
				if (target instanceof ATSuit) {//套对象
					ATSuit suit = (ATSuit) target;
					//取得套内对象
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
				//更改单特有节点输出
				if (target instanceof ECO) {
					ChangeService changeSerive = Helper.getChangeService();
					//更改后对象
					List<Persistable> changeList = changeSerive
							.getChangedAfterObjList((Change) target);

					if (changeList.size() > 0) {
						Element changinfo = new Element("changeinfo");
						propEle = new Element("changepaper");
						for (int i = 0; i < changeList.size(); i++) {
							Persistable p = changeList.get(i);
							//生成IID节点
							propElement = new Element("IID");
							propElement.setText(getIID(p));
							propEle.addContent(propElement);
						}
						changinfo.addContent(propEle);
						rootElement.addContent(changinfo);
					}
				}
				//分发数据包含电子文件的下载包含的电子文件
				List<PtFileItemBean> fileList = new ArrayList<PtFileItemBean>();
				if (target instanceof FileHolder) {
					fileList = AttachHelper.getAttachService().getAllFile(
							(FileHolder) target);
					
					if(target instanceof Approved){
						//下载在流程中上传的附件
						String proInstInnerIds = WorkFlowUtil.getObjectProcessInstanceId((Approved)target);
						List<WorkItem> lastRoundWorkItems = WorkFlowUtil.getWorkItemsByProcessInstanceIdAndRounds(proInstInnerIds, -1);
						
						String refType = "pt_workflow";
						String storageId = "pt_workflow";
						
						for(WorkItem wItem : lastRoundWorkItems){
							// 获得service
							PtFileService fileService = PtFileCptServiceProvider.getFileService();
							// 构造PtFileHolder
							PtFileHolderBean testFileHolderBean = new PtFileHolderBean();
							testFileHolderBean.setRefId(proInstInnerIds);
							testFileHolderBean.setRefType(refType);
							testFileHolderBean.setStorageId(storageId);
							// 获得列表
							FilterParam filterA = new FilterParam();
							String paramStr = "[paramIn][=][" + wItem.id + "]";
						 	if(paramStr!=null && paramStr.trim().length()>0){
					 			//增加过滤
					 			paramStr = PtFileCptUtil.decodeStr(paramStr);
					 			PtFileCptUtil.paramToFilterParam(paramStr, filterA);
						 	}

							List<PtFileItemBean> ptFlieList = fileService.getFileInfo(testFileHolderBean, filterA);
							if(ptFlieList!=null && ptFlieList.size()>0){
								fileList.addAll(ptFlieList);
							}
						}
					}
					//电子文件下载
					if (fileList != null && fileList.size() > 0) {
						for (PtFileItemBean ptFileItemBean : fileList) {
							String ptFileName = ptFileItemBean.getFileName();
							InputStream inputStream = AttachHelper
									.getAttachService().getInputStream(
											(FileHolder) target,
											ptFileItemBean.getInnerId());

							//电子文件所属对象的唯一标识作为保存路径
							String elcFileDirPath = transferDirPath
									+ STR_SUFFIX + iid;
							if (inputStream != null) {
								boolean result_flag = false;
								//取得发放通用配置[是否加密分发数据的电子文件]的配置
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
									LOG.error("下载电子文件出错！文件名:" + ptFileName);
								}
							}
						}
					}
				}
				//格式化文件为xml格式并保存文件
				boolean result_flag = saveXmlFile(doc, file);
				if(!result_flag){
					LOG.error("格式化文件为xml格式并保存文件出错！文件名:" + file.getName());
				}

				//分发信息的导出
				boolean result_flag1 = exportDisInfo(oid, disObject.getOid(), iid, transferDirPath, type);
				if(!result_flag1){
					LOG.error("分发信息出错！分发数据唯一标识:" + iid);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		//取得发放管理的是否需要压缩配置
		String is_NeedZip = distributeCommonConfigService
				.getConfigValueByConfigId("Is_NeedZip");
		String directoryZipName = transferDirPath + ".zip";
		if ("true".equalsIgnoreCase(is_NeedZip)) {
			//压缩导出文件夹
			FileUtil.zip(transferDirPath, directoryZipName, STR_ENCODE);
			//删除压缩的源文件夹
			if (transferDir.exists() && transferDir.isDirectory()) {
				//循环删除文件夹及子文件
				FileUtil.deleteDirectory(transferDirPath);
			}
		}
		//取得传输方式
		String transferMethod = distributeCommonConfigService
				.getConfigValueByConfigId("TransferMethod");
		if ("webservice".equalsIgnoreCase(transferMethod)) {

			//调用瀚海之星webservice传输文件
			//取得相关配置内容
			String webServiceUrl = distributeCommonConfigService
					.getConfigValueByConfigId("WebServiceUrl");
			String targetNamespace = distributeCommonConfigService
					.getConfigValueByConfigId("TargetNamespace");
			String method = distributeCommonConfigService
					.getConfigValueByConfigId("MethodName");
			try {
				//调用WebService
				ServiceClient serviceClient = new ServiceClient();
				Options opts = new Options();	
				// 指定被调用WebService的URL		
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
					//调用webservice传输文件（zip包）
					boolean result_flag = transferFileByWebService(directoryZip, "", serviceClient,
							targetNamespace, method);
					if(!result_flag){
						LOG.error("文件传输出错！文件名:" + directoryZipName);
					}
					if (directoryZip.exists()) {
						//删除zip包
						FileUtil.deleteDirectory(directoryZipName);
					}
				}else{
					File directory = new File(transferDirPath);
					//文件夹的情况下传输文件夹内的所有文件和子文件夹内的所有文件
					File[] files = directory.listFiles();
					if (files != null) {
						for (File file : files) {
							if (file.isFile()) {
								//调用webservice传输文件（分发数据，分发信息文件）
								boolean result_flag = transferFileByWebService(file, "", serviceClient,
										targetNamespace, method);
								if(!result_flag){
									LOG.error("文件传输出错！文件名:" + file.getName());
								}
							} else {
								File[] subFiles = file.listFiles();
								if (subFiles != null) {
									for (File subFile : subFiles) {
										//调用webservice传输文件（电子原文文件）
										boolean result_flag = transferFileByWebService(subFile,
												file.getName(), serviceClient,	targetNamespace, method);
										if(!result_flag){
											LOG.error("文件传输出错！文件名:" + subFile.getName());
										}
									}
								}
							}
						}
					}
					//删除文件夹及子文件
					if (directory.exists() && directory.isDirectory()) {
						//循环删除文件夹及子文件
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
	 * 输出分发信息xml文件
	 * 
	 * @param 
	 *         orderOid   发放单Oid 
	 *         objOid 分发数据Oid
	 *         iid 分发数据内对象的唯一标识
	 *         dirPath 文件保存路径
	 */
	public boolean exportDisInfo(String orderOid, String objOid, String iid,
			String dirPath, String type) {
		boolean rtn_flag = false;
		List<DistributeInfo> disInfos = DistributeHelper
				.getDistributeInfoService().getDistributeInfosByOid(orderOid,
						objOid);
		if (disInfos.size() > 0) {
			//分发数据对象的唯一标识 + _分发单.xml 作为分发单的文件名
			String fileName = dirPath + STR_SUFFIX + iid + "_分发单.xml";
			File file = newXmlFile(fileName);

			Document doc;
			SAXBuilder builder = new SAXBuilder();
			try {
				//取得根节点
				doc = builder.build(file);
				Element rootElement = doc.getRootElement();
				Element propElement = null;
				Element propertyElement = null;

				//表头信息
				Element resource = getDocSourceNote(type + "分发单");
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
					propertyElement = new Element("name");//名称
					propertyElement.setText(disInfo.getDisInfoName());
					propElement.addContent(propertyElement);

					propertyElement = new Element("count");//分发份数
					propertyElement.setText(String.valueOf(disInfo
							.getDisInfoNum()));
					propElement.addContent(propertyElement);

					propertyElement = new Element("notes");//备注
					propertyElement.setText(disInfo.getNote());
					propElement.addContent(propertyElement);

					propertyElement = new Element("SendDocType");//分发格式
					String disMediaTypeName = disInfo.getDisMediaTypeName();
					propertyElement.setText(disMediaTypeName);
					propElement.addContent(propertyElement);

					propertyElement = new Element("Distype");//发文类型
					String disTypeName = disInfo.getDisTypeName();
					propertyElement.setText(disTypeName);
					propElement.addContent(propertyElement);
					
					propertyElement = new Element("SendTime");//发送时间
					Long sendTime = disInfo.getSendTime();
					String strSendTime = "";
					if(sendTime > 0 ){
						strSendTime = DateUtil.toDate(disInfo.getSendTime());
					}
					propertyElement.setText(strSendTime);
					propElement.addContent(propertyElement);

					propertyElement = new Element("DestroyNum");//销毁份数
					propertyElement.setText(String.valueOf(disInfo
							.getDestroyNum()));
					propElement.addContent(propertyElement);

					propertyElement = new Element("SendBookNo");//发文编号
					propertyElement.setText(disInfo.getInnerId());
					propElement.addContent(propertyElement);

					sendinfoElement.addContent(propElement);
				}
				rootElement.addContent(sendinfoElement);

				//格式化文件为xml格式并保存文件
				boolean result_flag = saveXmlFile(doc, file);
				if(!result_flag){
					LOG.error("格式化文件为xml格式并保存文件出错！文件名:" + fileName);
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
	 * 返回DocSource节点
	 * 
	 * @param DistributeObject 分发数据对象
	 * @return Element DocSource节点对象
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
	 * 返回prop节点
	 * 
	 * @param 
	 * 		porpName 字段显示名称
	 *  	porpSign 字段标识
	 *  	porpValue 字段值
	 * @return Element propNote节点对象
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
	 * 返回propname为IID的节点(prop节点)
	 * 
	 * @param Persistable 可持久化对象
	 * @return Element 节点对象
	 */
	public Element getIIDNote(Persistable p) {
		String iid = "";
		//可以版本管理的对象
		if (p instanceof Versioned) {
			Versioned version = (Versioned) p;
			iid = version.getMasterRef().getInnerId();
		} else {
			iid = p.getInnerId();
		}
		Element propNote = makePropNote("内部标识", "IID", iid);
		return propNote;
	}

	/**
	 * 返回propname为INNERID的节点(prop节点)
	 * 
	 * @param Persistable 可持久化对象
	 * @return Element 节点对象
	 */
	public Element getInnerIdNote(Persistable p) {
		Element propNote = null;
		//可以版本管理的对象
		if (p instanceof Versioned) {
			propNote = makePropNote("A5内部标识", "INNERID", p.getInnerId());
		}
		return propNote;
	}

	/**
	 * 返回带根节点的xml文件对象
	 * 
	 * @param 
	 * 		fileNameWithPath 文件路径
	 * 		rootNoteName 根节点名称
	 * @return file 文件对象
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
	 * 保存Xml文件
	 * 
	 * @param 
	 * 		doc Document对象
	 * 		file File对象
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
	 * 返回IID，可以版本管理的返回masterId,以外返回innerId
	 * 
	 * @param 
	 * 		Persistable 可持久化对象
	 * @return iid 唯一标识
	 */
	public String getIID(Persistable p) {
		String iid = "";
		//可以版本管理的对象
		if (p instanceof Versioned) {
			Versioned version = (Versioned) p;
			iid = version.getMasterRef().getInnerId();
		} else {
			iid = p.getInnerId();
		}
		return iid;
	}

	/**
	 * 下载电子文件并保存到指定路径
	 * 
	 * @param 
	 * 		inputStream 对象文件流
	 * 		saveDirPath 保存路径
	 * 		fileName 文件名
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
	 * 下载电子文件并加密后保存到指定路径
	 * 
	 * @param 
	 * 		inputStream 对象文件流
	 * 		saveDirPath 保存路径
	 * 		fileName 文件名
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

//			//取得发放通用配置[密钥存储文件名]的配置
//			String encryptElcKeyFileName = distributeCommonConfigService
//					.getConfigValueByConfigId("EncryptElcKeyFileName");

			String encryptElcKeyFileName = "plm/ddm/desKeyFile/hanhaiElecFileKey.dat";
			String keyFileName ="";
			if (SysConfig.getAvidmHome().endsWith(STR_SUFFIX)) {
				keyFileName = SysConfig.getAvidmHome() + encryptElcKeyFileName;
			}else{
				keyFileName = SysConfig.getAvidmHome() + STR_SUFFIX + encryptElcKeyFileName;
			}
			//取得加密密钥
			Key key = FileCryptUtil.getKeyFromByteKeyFile(keyFileName);
			
			fileOutputStream = new FileOutputStream(saveFile);
			//对文件输出流进行加密
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
	 * 调用webservice传输文件
	 * 
	 * @param 
	 * 		file 对象文件
	 * 		originalFileDir 电子原文所属的对象的唯一标识
	 * 		serviceClient RPCServiceClient
	 * 		opAddEntry QName
	 *      classes Class<?>[] 返回值类型
	 */
	public boolean transferFileByWebService(File file, String originalFileDir,
			ServiceClient serviceClient, String targetNamespace, String method) {
		boolean rtn_flag = false;
		FileInputStream fis = null;
		try {
			//文件名称
			String fileName = file.getName();
			//整个文件的32位MD5值
			String fileMd5 = FileUtil.getMD5(file);
			boolean isLastBlock = false;

			//buffer大小64K
			int bufferSize = 64 * 1024;
			long offset = 0;

			fis = new FileInputStream(file);
			//分块上传的文件字节流
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
				//fileName 文件名称，如果服务器上有相同的文件则被覆盖
				OMElement omeFileName = factory.createOMElement("fileName", omNS);
				omeFileName.setText(fileName);
				payLoad.addChild(omeFileName);
				
				//originalFileDir 电子原文所属的对象的唯一标识，生成时所在文件夹名
				OMElement omeOriginalFileDir = factory.createOMElement("originalFileDir", omNS);
				omeOriginalFileDir.setText(originalFileDir);
				payLoad.addChild(omeOriginalFileDir);
				
				//buffer 分块上传的文件字节流，建议每个大小为64*1024字节
				OMElement omeBuffer = factory.createOMElement("buffer", omNS);
	            //将比特数组转为OMText
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
				
				//offset 当前文件库相对起始文件流得偏移
				OMElement omeOffset = factory.createOMElement("offset", omNS);
				omeOffset.setText(ConverterUtil.convertToString(offset));
				payLoad.addChild(omeOffset);
				
				//isLastBlock 是否是最后一个文件流块
				OMElement omeIsLastBlock = factory.createOMElement("isLastBlock", omNS);
				omeIsLastBlock.setText(ConverterUtil.convertToString(isLastBlock));
				payLoad.addChild(omeIsLastBlock);
				
				//fileMd5 整个文件的32位MD5值
				OMElement omeFileMd5 = factory.createOMElement("fileMd5", omNS);
				omeFileMd5.setText(fileMd5);
				payLoad.addChild(omeFileMd5);

				OMElement result = serviceClient.sendReceive(payLoad);
				String str_result = result.getFirstElement().getText();
				 //判断返回值如果不成功输出log信息
				// 瀚海之星webservice成功时返回S0001
				// 失败返回E开头的字符串（E0000-MD5验证失败;E0001内部异常）
				if (!STR_RTN_SUCCESS.equals(str_result)) {
					//输出log信息
					LOG.error("调用瀚海之星WebService出错！返回值:" + str_result);
					LOG.error("fileName:" + fileName);
					LOG.error("originalFileDir:" + originalFileDir);
					LOG.error("buffer长度:" + length);
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
