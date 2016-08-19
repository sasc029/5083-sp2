package com.bjsasc.adm.common;

/**
 * 常量类。
 * 
 * @author yanjia 2013-5-13
 */
public class ConstUtil {
	/** 现行文件上下文ID */
	public static final String ID_ACTIVE_CONTEXT = "activeContext";
	/** 现行文件上下文NAME */
	public static final String NAME_ACTIVE_CONTEXT = "现行上下文";	
	/** 现行文件LIST_SPOT*/
	public static final String SPOT_LISTACTIVEDOCUMENT = "ACTIVEDOCUMENT";
	public static final String SPOT_LISTACTIVEDOCUMENTCONDITION = "ACTIVEDOCUMENTCONDITION";
	/** 现行文件LIST_GRID*/
	public static final String GRID_LISTACTIVEDOCUMENT = "GRID_ACTIVEDOCUMENT";
	/** 现行文件主对象LIST_SPOT*/
	public static final String SPOT_LISTACTIVEDOCUMENTMASTER = "ACTIVEDOCUMENTMASTER";
	/** 现行文件主对象LIST_GRID*/
	public static final String GRID_LISTACTIVEDOCUMENTMASTER = "GRID_ACTIVEDOCUMENTMASTER";
	/** 现行单据LIST_SPOT*/
	public static final String SPOT_LISTACTIVEORDER = "ACTIVEORDER";
	/** 现行单据数据LIST_SPOT*/
	public static final String SPOT_LISTACTIVEORDEROBJECT = "ACTIVEORDEROBJECT";
	/** 现行单据LIST_GRID*/
	public static final String GRID_LISTACTIVEORDER = "GRID_ACTIVEORDER";
	/** 现行单据主对象LIST_SPOT*/
	public static final String SPOT_LISTACTIVEORDERMASTER = "ACTIVEORDERMASTER";
	/** 现行单据主对象LIST_GRID*/
	public static final String GRID_LISTACTIVEORDERMASTER = "GRID_ACTIVEORDERMASTER";
	/** 现行套LIST_SPOT*/
	public static final String SPOT_LISTACTIVESET = "ACTIVESET";
	/** 现行套LIST_GRID*/
	public static final String GRID_LISTACTIVESET = "GRID_ACTIVESET";
	/** 现行套数据LIST_SPOT*/
	public static final String SPOT_LISTACTIVESETOBJECT = "ACTIVESETOBJECT";
	/** 现行套主对象LIST_SPOT*/
	public static final String SPOT_LISTACTIVESETMASTER = "ACTIVESETMASTER";
	/** 现行套主对象LIST_GRID*/
	public static final String GRID_LISTACTIVESETMASTER = "GRID_ACTIVESETMASTER";	
	/** 版本浏览LIST_SPOT*/
	public static final String SPOT_LISTVERSIONBROWSE = "VERSIONBROWSE";
	//**********************************
	// 创建现行文件必要参数
	//**********************************
	/** 创建现行文件必要参数(类型) */
	public static final String CLASSID_PPRAM = "CLASSID";
	/** 创建现行文件必要参数(文件夹OID) */
	public static final String FOLDEROID_PPRAM = "FOLDEROID";
	/** 创建现行文件必要参数(文件夹PATH) */
	public static final String FOLDERPATH_PPRAM = "FOLDERPATH";
	/** 创建现行文件必要参数(上下文OID) */
	public static final String CONTEXTOID_PPRAM = "CONTEXTOID";
	/** 创建现行文件必要参数(上下文NAME) */
	public static final String CONTEXTNAME_PPRAM = "CONTEXTNAME";
	//**********************************
	// message信息
	//**********************************
	/** MESSAGE_DELETEOBJECTFLAG_0 */
	public static final String MESSAGE_DELETEOBJECTFLAG_0 ="存在于现行套中，若要删除会删除相关LINK,请确认!";

	/** MESSAGE_DELETEOBJECTFLAG_1 */
	public static final String MESSAGE_DELETEOBJECTFLAG_1 ="存在于现行单据中，若要删除会删除相关LINK,请确认!";

	/** MESSAGE_ISACTIVESETEDFLAG_0 */
	public static final String MESSAGE_ISACTIVESETEDFLAG_0 ="不是现行套数据源对象，不允许加入现行套中!";

	/** MESSAGE_ISACTIVEORDEREDFLAG_0 */
	public static final String MESSAGE_ISACTIVEORDEREDFLAG_0 ="不是现行单据数据源对象，不允许加入现行套中!";

	/** MESSAGE_ISLASTESTVERSIONFLAG_0 */
	public static final String MESSAGE_ISLASTESTVERSIONFLAG_0 ="该对象已是最新版本，不能添加更改后对象!";
	/** 直接入库错误 message*/
	public static final String CREATE_DISTRIBUTEORDER_ERROE = "现行套中无受控或已发放分发对象，不能直接入库!";
	public static final String PUT_STORAGE_ERROE = "该对象已经入库!";
	public static final String PUTSTORAGE_OPERATE_ERROE = "操作失败!";
	
	public static final String ADM="/adm";
}
