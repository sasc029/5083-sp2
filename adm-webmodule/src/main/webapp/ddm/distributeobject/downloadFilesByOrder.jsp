<%@page import="com.bjsasc.plm.core.system.avidmProperty.AvidmPropertyHelper"%>
<%@page import="com.bjsasc.plm.core.util.JsonUtil"%>
<%@page import="com.bjsasc.plm.core.util.DateTimeUtil"%>
<%@page import="com.bjsasc.platform.webframework.util.DateUtil"%>
<%@page import="com.bjsasc.plm.core.util.StringUtil"%>
<%@page import="com.bjsasc.plm.core.session.SessionHelper"%>
<%@page import="com.bjsasc.plm.core.attachment.AttachTypeEnum"%>
<%@page import="com.bjsasc.plm.core.vc.model.Mastered"%>
<%@page import="com.bjsasc.plm.core.vc.model.Versioned"%>
<%@page import="com.bjsasc.plm.core.identifier.Identified"%>
<%@page import="com.bjsasc.plm.core.folder.Foldered"%>
<%@page import="com.bjsasc.plm.core.persist.PersistHelper"%>
<%@page import="com.bjsasc.plm.Helper"%>
<%@page import="com.bjsasc.plm.core.folder.Cabinet"%>
<%@page import="com.bjsasc.plm.core.option.OptionHelper"%>
<%@page import="com.bjsasc.plm.core.option.OptionValue"%>
<%@page import="com.bjsasc.plm.core.folder.Folder"%>
<%@page import="com.bjsasc.plm.core.context.model.Contexted"%>
<%@page import="com.bjsasc.plm.core.persist.model.Persistable"%>
<%@page import="com.bjsasc.plm.url.Url"%>
<%@page import="com.bjsasc.platform.filecomponent.model.PtFileStorageObj"%>
<%@page import="com.bjsasc.platform.filecomponent.util.PtFileCptServiceProvider"%>
<%@page import="com.bjsasc.platform.filecomponent.service.PtStorageService"%>
<%@page import="com.bjsasc.ui.json.DataUtil"%>
<%@page import="com.bjsasc.plm.core.doc.DocumentMaster"%>
<%@page import="com.bjsasc.plm.core.folder.FolderHelper"%>
<%@page import="com.bjsasc.plm.core.doc.Document"%>
<%@page import="com.bjsasc.plm.core.doc.DocumentHelper"%>
<%@page import="com.bjsasc.plm.core.context.model.Context"%>
<%@page import="com.bjsasc.plm.core.context.ContextHelper"%>
<%@page import="com.bjsasc.plm.core.attachment.FileHolder"%>
<%@page import="com.bjsasc.plm.KeyS"%>
<%@page import="com.bjsasc.plm.core.attachment.AttachHelper"%>
<%@page import="com.bjsasc.plm.core.attachment.AttachService"%>
<%@page import="com.bjsasc.plm.core.vc.model.Iterated"%>
<%@page import="com.bjsasc.ddm.distribute.model.distributeobject.DistributeObject"%>
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="GBK"%>
<%@page import="java.io.File"%>
<%@page import="java.util.*"%>
<%@page import="com.bjsasc.platform.objectmodel.business.version.VersionControlUtil"%>
<%@page import="com.bjsasc.plm.tool.model.Tool"%>
<%@page import="com.bjsasc.plm.tool.util.ToolManHelper"%>
<%@page import="com.bjsasc.platform.filecomponent.model.PtFileItemBean"%>
<%@page import = "com.bjsasc.platform.i18n.*"%>
<%@include file="/plm/plm.jsp" %>
<%

//通用搜索里面增加下载文档
	String sessionId = request.getSession().getId();
	String oids = request.getParameter("oids");
	String isDebug = request.getParameter("debug");
	
	List<Map<String, Object>> oidsList = JsonUtil.toList(oids);
	
	List<Map<String, String>> listMap = new ArrayList<Map<String, String>>();
	for(int i=0; i<oidsList.size(); i++){
		String oid = (String)oidsList.get(i).get("oid");
		Persistable ptddm = PersistHelper.getService().getObject(oid);
		DistributeObject ddObject = (DistributeObject)ptddm;
		String dataInnerid = ddObject.getDataInnerId();
		String dataClassid = ddObject.getDataClassId();
		Persistable pt = PersistHelper.getService().getObject(dataClassid+":"+dataInnerid);
		Map<String,Object> attrMap=(Map<String,Object>)Helper.getTypeManager().format(pt).get(KeyS.SOURCE);
		String typeName = Helper.getTypeService().getTypeName(pt);
		String version = "";
		if(attrMap.get(KeyS.VERSION)!=null){
			version = attrMap.get(KeyS.VERSION).toString();
		}
		if(pt instanceof FileHolder){
			FileHolder doc = (FileHolder)pt;
			
			List<PtFileItemBean> itemBeans = AttachHelper.getObjectFileService(pt).getDownloadFiles(pt);//获取对象待下载的文件。
			if(itemBeans.size()>=1){
				for(PtFileItemBean bean : itemBeans){
					String fileDownloadInfo=AttachHelper.getAttachService().getFileDownloadInfo(bean, request);

					Map<String, String> map = new HashMap<String, String>(); //用于传输待下载文件信息，同时包含待生成excel文件的内容
					map.put(KeyS.NUMBER,attrMap.get(KeyS.NUMBER)+"");
					map.put(KeyS.NAME,attrMap.get(KeyS.NAME)+"");
					map.put(KeyS.VERSION,version);
					map.put(KeyS.TYPENAME,typeName);
					map.put(KeyS.LIFECYCLE_STATE,attrMap.get(KeyS.LIFECYCLE_STATE)+"");
					
					map.put("fileDownloadInfo",fileDownloadInfo); 
					map.put("downloadResult",""); 
					listMap.add(map);
				}
			}else{

				Map<String, String> map = new HashMap<String, String>(); //用于传输待下载文件信息，同时包含待生成excel文件的内容
				map.put(KeyS.NUMBER,attrMap.get(KeyS.NUMBER)+"");
				map.put(KeyS.NAME,attrMap.get(KeyS.NAME)+"");
				map.put(KeyS.VERSION,version);
				map.put(KeyS.TYPENAME,typeName);
				map.put(KeyS.LIFECYCLE_STATE,attrMap.get(KeyS.LIFECYCLE_STATE)+"");
				
				map.put("fileDownloadInfo",""); 
				map.put("downloadResult","不需要"); //没有文件在excel中输出“不需要”
				listMap.add(map);
			}
		}else{

			Map<String, String> map = new HashMap<String, String>(); //用于传输待下载文件信息，同时包含待生成excel文件的内容
			map.put(KeyS.NUMBER,attrMap.get(KeyS.NUMBER)+"");
			map.put(KeyS.NAME,attrMap.get(KeyS.NAME)+"");
			map.put(KeyS.VERSION,version);
			map.put(KeyS.TYPENAME,typeName);
			map.put(KeyS.LIFECYCLE_STATE,attrMap.get(KeyS.LIFECYCLE_STATE)+"");
			
			map.put("fileDownloadInfo","");
			map.put("downloadResult","不需要"); //没有文件在excel中输出“不需要”
			listMap.add(map);
		}
	}
	String callback = request.getParameter(KeyS.CALLBACK);
	String filesInfo = JsonUtil.listToJson(listMap);
	filesInfo=filesInfo.replaceAll("'", "&apos;"); //对单引号转义
	
	if(callback == null || "".equals(callback)){
		callback = "noCallBackDefined";
	}
	
	//excel的表头名称定义
	StringBuffer title = new StringBuffer(); 
	title.append("rowNo=序号").append(","); //列名为‘rowNo’时会自动填充当前行号（首行为0）
	title.append(KeyS.NUMBER+"=编号").append(",");
	title.append(KeyS.NAME+"=名称").append(",");
	title.append(KeyS.VERSION+"=版本").append(",");
	title.append(KeyS.TYPENAME+"=类型").append(",");
	title.append(KeyS.LIFECYCLE_STATE+"=生命周期状态").append(",");
	title.append("newFileName=文件名称").append(",");
	title.append("downloadResult=下载状态");
	
	
	String excelTitleJsonStr=title.toString();
	//excel文件命名方式为："批量下载"+"_"+"用户登录名"+"_"+"YYYYMMDD"+"_"+"四位流水号"，YYYYMMDD为当前操作日期，例如：批量下载_caichengzhi_20140512_0001；
	String excelFileName="批量下载"+"_"+SessionHelper.getService().getUser().getId()+"_"+DateTimeUtil.getCurrentDate("yyyyMMdd");
	String num=AvidmPropertyHelper.getService().getProperty(excelFileName);//流水号     
	if(num!=null){
		num=Integer.parseInt(num)+1+"";
		num="000"+num;
		if(num.length()>4){
			num = num.substring(num.length()-4);
		}
	}else{
		num="0001";
	}
	AvidmPropertyHelper.getService().updateProperty(excelFileName, num);
	excelFileName=excelFileName+"_"+num+".xls";

	if("true".equals(isDebug)){
		System.out.println("downloadMode=webserver");
		System.out.println("cellName=default");
		System.out.println("filesInfo="+filesInfo);
		System.out.println("approveButtonText=保存");	
		System.out.println("callback=afterDownloadFiles()");	
		System.out.println("excelTitleJsonStr="+excelTitleJsonStr);	
		System.out.println("excelFileName="+excelFileName);	
		System.out.println("callback="+callback);	
		System.out.println("listMap.size()="+listMap.size());	
	}

%>
<html>
	<head>
		<title>浏览文件</title>
		<meta http-equiv="Pragma" content="no-cache">
		<meta http-equiv="Cache-Control" content="no-cache">
		<meta http-equiv="expires" content="Thu, 1 January 1900 12:12:12 PST">
		<script type="text/javascript" src="<%=Url.PLMJS%>" charset="GBK"></script>
		<link rel="stylesheet" href="../../plm/css/avidm.css" type="text/css">
	</head>
	<body>
	<%if(listMap.size() > 0){ %>
		<object CODEBASE="../../plm/bin" CODE="com.bjsasc.plm.applet.DownloadSearchFilesApplet.class"
		ARCHIVE="uploadApplet.jar,commons-codec-1.4.jar,commons-httpclient-3.1.0.jar,commons-logging-1.1.1.jar,json.jar,ant-1.7.1.jar,poi-3.10-FINAL.jar" 
		NAME="fileUploadApplet"
		id="fileUploadApplet" 
		WIDTH="100" 
		HEIGHT="50" 
		HSPACE="0" 
		VSPACE="0"
		ALIGN="middle" MAYSCRIPT> 

			<param name="cellName" value="default">
			<param name="filesInfo" value='<%=filesInfo%>'>
			<param name="approveButtonText" value="保存">
			<param name="callback" value="afterDownloadFiles()">
			<param name="excelTitleJsonStr" value="<%=excelTitleJsonStr%>">
			<param name="excelFileName" value="<%=excelFileName%>">
			<param name="debug" value=<%=isDebug %>>
		
		</object>
		
		<script>
		function afterDownloadFiles() {
			var applet = document.getElementById("fileUploadApplet");
			var failObj = applet.getFailedFilesJson();
			var successObj = applet.getSuccessedFilesJson();
			
			try{
				parent.<%=callback%>(successObj,failObj);
			}catch(exception1){
				try{
					window.opener.<%=callback%>(successObj,failObj);
				}catch(exception){
					alert("回调函数失败!"+exception.message);
				}
			}
		}
		</script>
	<%}else{ %>
		<script>
			$(function(){
				try{
					parent.<%=callback%>(null,null);
				}catch(exception1){
					try{
						window.opener.<%=callback%>(null,null);
					}catch(exception){
						alert("回调函数失败!"+exception.message);
					}
				}
			});
		</script>
	<%} %>
		
	</body>
</html>